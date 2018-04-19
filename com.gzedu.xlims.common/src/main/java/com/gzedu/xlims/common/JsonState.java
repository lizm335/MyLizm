package com.gzedu.xlims.common;

import com.gzedu.xlims.common.gzedu.EmptyUtils;

/**
 * json msg 状态
 * 
 * @author paul
 * @createTime 2016-11-15
 * @msg 值 :女0 男1
 */
public enum JsonState {

    /** 成功 success */
    SUCCESS("success", 1),
    
    /** 失败 error */
    ERROR("error", 2),
    
    /** 插入成功 success_insert */
    SUCCESS_INSERT("success_insert", 3),
    
    /** 插入失败 error_insert */
    ERROR_INSERT("error_insert", 4),
    
    /** 删除成功 success_insert */
    SUCCESS_DELETE("success_delete", 5),
    
    /** 删除失败 error_insert */
    ERROR_DELETE("error_delete", 6),
    
    /** 更新成功 success_insert */
    SUCCESS_UPDATE("success_update", 7),
    
    /** 更新失败 error_insert */
    ERROR_UPDATE("error_update", 8),
    
    /** 参数异常 error_param */
    ERROR_PARAM("error_param", 0);
    
    String  key;
    Integer value;
    
    private JsonState(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
    
    public Integer getValue() {
        return value;
    }

    /**
     * 
     * @param value
     * @return
     */
    public static JsonState getValue(Integer value) {
        if (EmptyUtils.isEmpty(value)) {
            return null;
        }
        for (JsonState state : values()) {
            if (state.getValue() == value) {
                return state;
            }
        }
        return null;
    }
    
}