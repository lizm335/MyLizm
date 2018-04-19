package com.ouchgzee.headTeacher.biz;

import com.ouchgzee.headTeacher.dictionary.Dictionary;
import com.ouchgzee.headTeacher.service.BzrCacheService;
import com.ouchgzee.headTeacher.service.dictionary.BzrDictionaryService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 数据字典
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月15日
 * @version 2.5
 * @since JDK 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class DictionaryTest {

    @Autowired
    private BzrDictionaryService dictionaryService;

    @org.junit.Test
    public void dic() {
        // 按照编码号排序查询到所有的区域信息，再将它加载到数据字典缓存中
        List<Object[]> areaList = dictionaryService.queryAllDistrict();
        // 获取所有的区域信息
        List<BzrCacheService.Value> alllist = new ArrayList();
        // 获取所有的省份
        List<BzrCacheService.Value> plist = new ArrayList();
        boolean isBreak = false;
        Object[] breakObj = null;
        for (Iterator<Object[]> iterator = areaList.iterator(); isBreak || iterator.hasNext();) {
            Object[] p = null;
            if(isBreak) {
                isBreak = false;
                p = breakObj;
            } else {
                p = iterator.next();
            }
            // 按照省份规则查找所有的省份，后四个字符为0000
            if(p[0].toString().endsWith("0000")) {
                BzrCacheService.Value pv = new BzrCacheService.Value(p[0].toString(), p[1].toString());
                alllist.add(pv);
                plist.add(pv);
                // 省份循环获取所有市
                List<BzrCacheService.Value> clist = new ArrayList();
                while (isBreak || iterator.hasNext()) {
                    Object[] c = null;
                    if(isBreak) {
                        isBreak = false;
                        c = breakObj;
                    } else {
                        c = iterator.next();
                    }
                    // 按照市规则查找所有的市，前两个字符和省份编码号相同，后两个字符为00
                    if(c[0].toString().startsWith(p[0].toString().substring(0, 2))) {
                        if(c[0].toString().endsWith("00")) {
                            BzrCacheService.Value cv = new BzrCacheService.Value(c[0].toString(), c[1].toString());
                            alllist.add(cv);
                            clist.add(cv);
                            // 省市双重循环获取区/县
                            List<BzrCacheService.Value> alist = new ArrayList();
                            while (iterator.hasNext()) {
                                Object[] a = iterator.next();
                                // 按照区/县规则查找所有的区/县，前四个字符和市编码号相同
                                if(a[0].toString().startsWith(c[0].toString().substring(0, 4))) {
                                    BzrCacheService.Value av = new BzrCacheService.Value(a[0].toString(), a[1].toString());
                                    alllist.add(av);
                                    alist.add(av);
                                } else {
                                    isBreak = true;
                                    breakObj = a;
                                    break;
                                }
                            }
                            Dictionary.add(BzrCacheService.DictionaryKey.AREA.replace("${Province}", p[0].toString()).replace("${City}", c[0].toString()), alist);
                        }
                    } else {
                        isBreak = true;
                        breakObj = c;
                        break;
                    }
                }
                Dictionary.add(BzrCacheService.DictionaryKey.CITY.replace("${Province}", p[0].toString()), clist);
            }
        }
        // 所有省份存入缓存
        Dictionary.add(BzrCacheService.DictionaryKey.PROVINCE, plist);
        // 所有区域存入缓存
        Dictionary.add(BzrCacheService.DictionaryKey.ALLAREA, alllist);
    }
}
