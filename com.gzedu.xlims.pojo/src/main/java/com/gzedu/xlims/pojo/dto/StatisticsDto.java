/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.dto;

/**
 * 统计DTO<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年09月23日
 * @version 2.5
 * @since JDK 1.7
 */
public class StatisticsDto<T> {

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private T value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

}
