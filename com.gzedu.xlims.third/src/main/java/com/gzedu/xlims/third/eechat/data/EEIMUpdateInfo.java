/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.third.eechat.data;

/**
 * 功能说明：个人信息更改
 * @author 卢林林 lulinlin@eenet.com
 * @Date 2017年09月21日
 * @version 2.5
 *
 */
public class EEIMUpdateInfo {
	String USER_ID;// 用户ID
	String USER_NAME;// 用户名称
	String USER_TYPE;// 用户类型(1 老师 2 班主任 3 学生 4 督导)
	String USER_IDCARD;// 身份证
	String USER_IMG;// 头像
	String USER_SEX;//性别
	String USER_PHONE;//手机
	String USER_EMAIL;//邮箱
	
	/**
	 * @param uSER_NAME
	 * @param uSER_TYPE
	 * @param uSER_IDCARD
	 * @param uSER_IMG
	 */
	public EEIMUpdateInfo(String uSER_ID, String uSER_NAME, String uSER_TYPE, String uSER_IDCARD,String uSER_IMG,
			String uSER_SEX,String uSER_PHONE, String uSER_EMAIL) {
		super();
		USER_ID = uSER_ID;
		USER_NAME = uSER_NAME;
		USER_TYPE = uSER_TYPE;
		USER_IDCARD = uSER_IDCARD;
		USER_IMG = uSER_IMG;
		USER_SEX=uSER_SEX;
		USER_PHONE=uSER_PHONE;
		USER_EMAIL=uSER_EMAIL;
	}

	public EEIMUpdateInfo() {
		super();
	}

	public String getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}

	public String getUSER_TYPE() {
		return USER_TYPE;
	}

	public void setUSER_TYPE(String uSER_TYPE) {
		USER_TYPE = uSER_TYPE;
	}

	public String getUSER_IDCARD() {
		return USER_IDCARD;
	}

	public void setUSER_IDCARD(String uSER_IDCARD) {
		USER_IDCARD = uSER_IDCARD;
	}

	public String getUSER_IMG() {
		return USER_IMG;
	}

	public void setUSER_IMG(String uSER_IMG) {
		USER_IMG = uSER_IMG;
	}

	public String getUSER_SEX() {
		return USER_SEX;
	}

	public void setUSER_SEX(String uSER_SEX) {
		USER_SEX = uSER_SEX;
	}

	public String getUSER_PHONE() {
		return USER_PHONE;
	}

	public void setUSER_PHONE(String uSER_PHONE) {
		USER_PHONE = uSER_PHONE;
	}

	public String getUSER_EMAIL() {
		return USER_EMAIL;
	}

	public void setUSER_EMAIL(String uSER_EMAIL) {
		USER_EMAIL = uSER_EMAIL;
	}	
}
