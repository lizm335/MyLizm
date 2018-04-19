package com.gzedu.xlims.common;

import java.math.BigDecimal;
import java.util.*;

/**
 * 键值对map工具类
 * Created by paul on 2017/5/2.
 */
public class MapKit {


    private static final Map<Comparable, Object> ASC_MAP = new TreeMap<Comparable, Object>(
            new Comparator<Comparable>() {
                public int compare(Comparable obj1, Comparable obj2) {
                    return obj1.compareTo(obj2);
                }
            });

    private static final Map<Comparable, Object> DESC_MAP = new TreeMap<Comparable, Object>(
            new Comparator<Comparable>() {
                public int compare(Comparable obj1, Comparable obj2) {
                    return obj2.compareTo(obj1);
                }
            });


    /*Map 根据整型 降序返回TreeMap*/
    public static Map toIntDescMap(Map<Comparable, Object> map){
        return toSort(map,DESC_MAP);
    }

    /*Map 根据整型 升序返回TreeMap*/
    public static Map toIntAscMap(Map<Comparable, Object> map){
        return toSort(map,ASC_MAP);
    }

    /**
     * @param map    key可以转化整型
     * @param sortMap 排序Map
     * @return
     */
    private static Map toSort(Map<Comparable, Object> map,Map sortMap) {

        // sort
        Iterator<Map.Entry<Comparable, Object>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Comparable, Object> entry = entries.next();
            sortMap.put(entry.getKey(), entry.getValue());
        }

        return sortMap;
    }

    public static void main(String[] args) {
        Map m=new HashMap();
        m.put(new BigDecimal(22),"gg");
        m.put(new BigDecimal(5),"4");
        System.out.println(toIntAscMap(m));
        System.out.println("1".equals(1+""));
    }
}
