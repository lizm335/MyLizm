package com.ouchgzee.study.web.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *  Created by paul on 2017/3/8.
 *
 *  分页通用反馈信息,相对Feedback增加了三个分页字段.
 */
public class FeedbackPage {

    /**结果状态值*/
    @JsonProperty("RESULT")
    private String result;

    /**自定义消息提示*/
    @JsonProperty("MSG")
    private String msg;

    /**查询结果 一般是数组*/
    @JsonProperty("DATA")
    private Object data;

    /**每页记录数*/
    @JsonProperty("NOMALCOUNT")
    private int nomalcount;

    /**当前页*/
    @JsonProperty("CURRENTPAGE")
    private int currentPage;

    /**总记录数*/
    @JsonProperty("SIZE")
    private int size;


    public FeedbackPage() {
    }

    public FeedbackPage(String result, String msg, Object data, int nomalcount, int currentPage, int size) {
        this.result = result;
        this.msg = msg;
        this.data = data;
        this.nomalcount = nomalcount;
        this.currentPage = currentPage;
        this.size = size;
    }
    @JsonIgnore
    public String getResult() {
        return result;
    }
    @JsonIgnore
    public void setResult(String result) {
        this.result = result;
    }
    @JsonIgnore
    public String getMsg() {
        return msg;
    }
    @JsonIgnore
    public void setMsg(String msg) {
        this.msg = msg;
    }
    @JsonIgnore
    public Object getData() {
        return data;
    }
    @JsonIgnore
    public void setData(Object data) {
        this.data = data;
    }
    @JsonIgnore
    public int getNomalcount() {
        return nomalcount;
    }
    @JsonIgnore
    public void setNomalcount(int nomalcount) {
        this.nomalcount = nomalcount;
    }
    @JsonIgnore
    public int getCurrentPage() {
        return currentPage;
    }
    @JsonIgnore
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    @JsonIgnore
    public int getSize() {
        return size;
    }
    @JsonIgnore
    public void setSize(int size) {
        this.size = size;
    }
}
