/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 缓存服务接口<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月7日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public interface BzrCacheService {

    class Value implements Serializable {
        private String code;
        private String name;

        public Value(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    final class DictionaryKey {
        /**
         * 所有区域的key
         */
        public static final String ALLAREA = "ALLAREA";
        /**
         * 区域省份的key
         */
        public static final String PROVINCE = "Province";

        /**
         * 区域市的key
         */
        public static String CITY = "${Province}_City";

        /**
         * 区域区/县的key
         */
        public static String AREA = "${Province}_${City}_Area";
        /**
         * 性别
         */
        public static final String SEX = "Sex";
        /**
         * 文化程度码（最后学历,学历码）
         */
        public static final String CULTURALLEVELCODE = "CulturalLevelCode";
        /**
         * 学籍状态
         */
        public static final String STUDENTNUMBERSTATUS = "StudentNumberStatus";
        /**
         * 教职工类型
         */
        public static final String EMPLOYEETYPE = "EmployeeType";
        /**
         * 培养层次
         */
        public static final String TRAININGLEVEL = "TrainingLevel";
        /**
         * 用户类型
         */
        public static final String USERTYPE = "UserType";
        /**
         * 服务方式
         */
        public static final String SERVICEMETHOD = "ServiceMethod";
        /**
         * 考试状态
         */
        public static final String EXAM_STATE = "EXAM_STATE";
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    Object getCached(String key);

    /**
     * 获取缓存的数据字典
     * @param key
     * @return
     */
    List<Value> getCachedDictionary(String key);

    /**
     * 获取缓存的数据字典
     * @param key
     * @return
     */
    Map<String, String> getCachedDictionaryMap(String key);

    /**
     * 获取缓存数据字典对应的NAME
     * @param key
     * @param code
     * @return
     */
    String getCachedDictionaryName(String key, String code);

    /**
     * 更新缓存
     * @param key
     * @param value
     * @return
     */
    boolean updateCached(String key, Object value);

    /**
     * 删除缓存
     * @param key
     * @return
     */
    boolean deleteCached(String key);

}
