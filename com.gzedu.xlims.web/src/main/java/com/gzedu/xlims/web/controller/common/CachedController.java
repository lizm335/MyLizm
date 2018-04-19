/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.common;

import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.dictionary.DictionaryService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.controller.base.BaseController;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 缓存控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年04月25日
 * @version 2.5
 */
@Controller
@RequestMapping( "/static" )
public class CachedController extends BaseController {

	/**
	 * 刷新缓存数据字典
	 * @throws IOException
     */
	@RequestMapping(value = "flushCachedDictionary.html" )
	@ResponseBody
	public void flushCachedDictionary(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Feedback feedback = new Feedback(true, "刷新成功");
		// Spring 上下文
		ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());

		long startTime = System.currentTimeMillis();
		DictionaryService dictionaryService = appContext.getBean(DictionaryService.class);
		CacheService cacheService = appContext.getBean(CacheService.class);

		// 清空缓存
		cacheService.deleteCached(CacheService.HKEY_DICTIONARY);

//        // 加载数据字典
//        List<TblSysData> dataList = dictionaryService.queryBy(Collections.EMPTY_MAP, new Sort(new Sort.Order("typeCode"), new Sort.Order("ordNo")));
//        Dictionary.init(dataList);

		// 判断区域是否已缓存
		if(!cacheService.hasCachedDictionary(CacheService.DictionaryKey.PROVINCE)) {
			// 按照编码号排序查询到所有的区域信息，再将它加载到数据字典缓存中
			List<Object[]> areaList = dictionaryService.queryAllDistrict();
			// 获取所有的区域信息
			List<CacheService.Value> alllist = new ArrayList();
			// 获取所有的省份
			List<CacheService.Value> plist = new ArrayList();
			boolean isBreak = false;
			Object[] breakObj = null;
			for (Iterator<Object[]> iterator = areaList.iterator(); isBreak || iterator.hasNext(); ) {
				Object[] p = null;
				if (isBreak) {
					isBreak = false;
					p = breakObj;
				} else {
					p = iterator.next();
				}
				// 按照省份规则查找所有的省份，后四个字符为0000
				if (p[0].toString().endsWith("0000")) {
					CacheService.Value pv = new CacheService.Value(p[0].toString(), p[1].toString());
					alllist.add(pv);
					plist.add(pv);
					// 省份循环获取所有市
					List<CacheService.Value> clist = new ArrayList();
					while (isBreak || iterator.hasNext()) {
						Object[] c = null;
						if (isBreak) {
							isBreak = false;
							c = breakObj;
						} else {
							c = iterator.next();
						}
						// 按照市规则查找所有的市，前两个字符和省份编码号相同，后两个字符为00
						if (c[0].toString().startsWith(p[0].toString().substring(0, 2))) {
							if (c[0].toString().endsWith("00")) {
								CacheService.Value cv = new CacheService.Value(c[0].toString(), c[1].toString());
								alllist.add(cv);
								clist.add(cv);
								// 省市双重循环获取区/县
								List<CacheService.Value> alist = new ArrayList();
								while (iterator.hasNext()) {
									Object[] a = iterator.next();
									// 按照区/县规则查找所有的区/县，前四个字符和市编码号相同
									if (a[0].toString().startsWith(c[0].toString().substring(0, 4))) {
										CacheService.Value av = new CacheService.Value(a[0].toString(), a[1].toString());
										alllist.add(av);
										alist.add(av);
									} else {
										isBreak = true;
										breakObj = a;
										break;
									}
								}
								cacheService.setCachedDictionary(CacheService.DictionaryKey.AREA.replace("${Province}", p[0].toString()).replace("${City}", c[0].toString()), alist);
							}
						} else {
							isBreak = true;
							breakObj = c;
							break;
						}
					}
					cacheService.setCachedDictionary(CacheService.DictionaryKey.CITY.replace("${Province}", p[0].toString()), clist);
				}
			}
			// 所有省份存入缓存
			cacheService.setCachedDictionary(CacheService.DictionaryKey.PROVINCE, plist);
			// 所有区域存入缓存
			cacheService.setCachedDictionary(CacheService.DictionaryKey.ALLAREA, alllist);
		}

		System.out.println("初始化数据字典 success ！耗时:" + (System.currentTimeMillis() - startTime) + "ms");
		feedback.setMessage("初始化数据字典 success ！耗时:" + (System.currentTimeMillis() - startTime) + "ms");
		super.outputJsonData(response, feedback);
	}

}