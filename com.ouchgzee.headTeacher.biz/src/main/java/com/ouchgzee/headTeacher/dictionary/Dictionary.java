package com.ouchgzee.headTeacher.dictionary;

import com.gzedu.xlims.common.SerializeUtil;
import com.ouchgzee.headTeacher.pojo.BzrTblSysData;
import com.ouchgzee.headTeacher.service.BzrCacheService;

import java.util.*;

/**
 * 数据字典静态缓存<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月7日
 * @version 2.5
 * @since JDK 1.7
 */
public class Dictionary {

    // 缓存到Map
    private static Map<String, List<BzrCacheService.Value>> cacheMap = new HashMap();

    public static void init(List<BzrTblSysData> dataList) {
        if(dataList != null && dataList.size() > 0) {
            synchronized (Dictionary.class) {
                if (cacheMap.size() == 0) {
                    System.out.println("加载数据字典......");
                    // 上一个Key和当前的Key
                    String pkey = null, ckey = null;
                    List<BzrCacheService.Value> vlist = new ArrayList();
                    for (int i = 0; i < dataList.size(); i++) {
                        BzrTblSysData data = dataList.get(i);
                        if(i == 0) pkey = data.getTypeCode();
                        ckey = data.getTypeCode();
                        if(!pkey.equals(ckey)) {
                            // 使用序列化深克隆
                            List<BzrCacheService.Value> cplist = SerializeUtil.unserialize(SerializeUtil.serialize(vlist));
                            cacheMap.put(pkey, cplist);
                            vlist.clear();
                        }
                        pkey = data.getTypeCode();
                        BzrCacheService.Value v = new BzrCacheService.Value(data.getCode(), data.getName());
                        vlist.add(v);
                    }
                    if(vlist.size() != 0) {
                        // 使用序列化深克隆
                        List<BzrCacheService.Value> cplist = SerializeUtil.unserialize(SerializeUtil.serialize(vlist));
                        cacheMap.put(pkey, cplist);
                        vlist.clear();
                    }
                }
            }
        }
    }

    public static List<BzrCacheService.Value> get(String key) {
        return cacheMap.get(key);
    }

    public static void add(String key, List<BzrCacheService.Value> vlist) {
        if(vlist != null) {
            if (cacheMap.containsKey(key)) {
                // get(key).addAll(vlist);
                List<BzrCacheService.Value> values = get(key);
                values.addAll(vlist);
                cacheMap.put(key, values);
            } else {
                cacheMap.put(key, vlist);
            }
        }
    }

    public static void main(String[] args) {
        List<BzrCacheService.Value> list1 = new ArrayList();
        list1.add(new BzrCacheService.Value("1", "a"));
        add("a", list1);
        System.out.println(get("a").size());
        List<BzrCacheService.Value> list2 = new ArrayList();
        BzrCacheService.Value v2 = new BzrCacheService.Value("2", "b");
        BzrCacheService.Value v3 = new BzrCacheService.Value("3", "c");
        list2.add(v2);
        list2.add(v3);
        add("a", list2);
        System.out.println(get("a").size());

        Iterator<BzrCacheService.Value> iterator = list2.iterator();
        for(; iterator.hasNext();) {
            BzrCacheService.Value v = iterator.next();
            System.out.println(v.getName());
            iterator.remove();
        }
    }

}
