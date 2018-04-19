/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.third.eechat.data;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年8月18日
 * @version 2.5
 *
 */
public class EEIMGroupReturnData {
	String MESSAGE;
	String GROUP_ID;
	String STATUS; // STATUS 1-建群成功 0-失败 3-群已存在
	String GROUP_EEIM_NO;
	String USER_NUMS;

	public String getMESSAGE() {
		return MESSAGE;
	}

	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}

	public String getGROUP_ID() {
		return GROUP_ID;
	}

	public void setGROUP_ID(String gROUP_ID) {
		GROUP_ID = gROUP_ID;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getGROUP_EEIM_NO() {
		return GROUP_EEIM_NO;
	}

	public void setGROUP_EEIM_NO(String gROUP_EEIM_NO) {
		GROUP_EEIM_NO = gROUP_EEIM_NO;
	}

	public String getUSER_NUMS() {
		return USER_NUMS;
	}

	public void setUSER_NUMS(String USER_NUMS) {
		this.USER_NUMS = USER_NUMS;
	}
}
