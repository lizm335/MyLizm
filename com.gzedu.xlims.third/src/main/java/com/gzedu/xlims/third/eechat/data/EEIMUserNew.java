/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.third.eechat.data;

/**
 * 功能说明：
 * 
 * @author 卢林林 lulinlin@eenet.com
 * @Date 2017年8月17日
 * @version 2.5
 *
 */
public class EEIMUserNew {
	String USER_ID;// 用户ID
	String USER_NAME;// 用户名称
	String USER_TYPE;// 用户类型(1 老师 2 班主任 3 学生 4 督导)
	String USER_IDCARD;// 身份证
	String USER_IMG;// 头像

	/**
	 * @param uSER_NAME
	 * @param uSER_TYPE
	 * @param uSER_IDCARD
	 * @param uSER_IMG
	 */
	public EEIMUserNew(String uSER_ID, String uSER_NAME, String uSER_TYPE, String uSER_IDCARD,String uSER_IMG) {
		super();
		USER_ID = uSER_ID;
		USER_NAME = uSER_NAME;
		USER_TYPE = uSER_TYPE;
		USER_IDCARD = uSER_IDCARD;
		USER_IMG = uSER_IMG;
	}

	public EEIMUserNew() {
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
	
}
