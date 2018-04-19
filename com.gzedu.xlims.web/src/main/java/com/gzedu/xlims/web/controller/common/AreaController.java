/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.common;

import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.controller.base.BaseController;
import com.gzedu.xlims.web.controller.common.vo.CommonSelect;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 区域控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月8日
 * @version 2.5
 */
@Controller
@RequestMapping("/area")
public class AreaController extends BaseController {

	@Autowired
	private CacheService cacheService;

	@RequestMapping(value = "getChildrens", method = RequestMethod.GET)
	@ResponseBody
	public Feedback updateHeadPortraitForm(@RequestParam("pcode") String pcode,
										   @RequestParam(value = "ccode", defaultValue = "") String ccode) {
		Feedback feedback = new Feedback(true, "成功");
		List<CacheService.Value> values = null;
		if("1".equals(pcode)) {
			values = cacheService.getCachedDictionary(CacheService.DictionaryKey.PROVINCE);
		} else if(StringUtils.isBlank(ccode)) {
			values = cacheService.getCachedDictionary(CacheService.DictionaryKey.CITY.replace("${Province}", pcode));
		} else {
			values = cacheService.getCachedDictionary(CacheService.DictionaryKey.AREA.replace("${Province}", pcode).replace("${City}", ccode));
		}
		feedback.setObj(values);
		return feedback;
	}

	@Autowired
	CommonMapService commonMapService;

	// 查询全部市
	@RequestMapping(value = "queryCity", method = RequestMethod.GET)
	@ResponseBody
	public List<CommonSelect> queryCity(String code) {
		List<CommonSelect> list = new ArrayList<CommonSelect>();
		if (StringUtils.isNotBlank(code)) {
			Map<String, String> cityMap = commonMapService.getCityMap(code);
			for (Entry<String, String> en : cityMap.entrySet()) {
				list.add(new CommonSelect(en.getKey(), en.getValue()));
			}
		}
		return list;
	}

	// 查询全部区
	@RequestMapping(value = "queryDistrict", method = RequestMethod.GET)
	@ResponseBody
	public List<CommonSelect> queryDistrict(String code) {
		List<CommonSelect> list = new ArrayList<CommonSelect>();
		if (StringUtils.isNotBlank(code)) {
			Map<String, String> map = commonMapService.getDistrictMap(code);
			for (Entry<String, String> en : map.entrySet()) {
				list.add(new CommonSelect(en.getKey(), en.getValue()));
			}
		}
		return list;
	}

}