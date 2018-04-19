/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.third.eechat.data;

/**
 * 功能说明：个人信息更改返回信息
 * @author 卢林林 lulinlin@eenet.com
 * @Date 2017年09月21日
 * @version 2.5
 *
 */
public class EEIMUpdateInfoReturnData {
	String MESSAGE;
	String STATUS;// 1成功 0,失败
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
}
