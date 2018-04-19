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
public class EEIMaddGroupMemberData {
	String APP_ID;//
	String OWNER_ID;// 群主或者管理员的EE账号
	String GROUP_ID;// 群组ID
	String[] USER_ID;// 用户ID列表

	public EEIMaddGroupMemberData() {
		super();
	}

	/**
	 * @param aPP_ID
	 * @param oWNER_ID
	 * @param gROUP_ID
	 * @param uSER_ID
	 */
	public EEIMaddGroupMemberData(String aPP_ID, String oWNER_ID, String gROUP_ID, String[] uSER_ID) {
		super();
		APP_ID = aPP_ID;
		OWNER_ID = oWNER_ID;
		GROUP_ID = gROUP_ID;
		USER_ID = uSER_ID;
	}

	public String getAPP_ID() {
		return APP_ID;
	}

	public void setAPP_ID(String aPP_ID) {
		APP_ID = aPP_ID;
	}

	public String getOWNER_ID() {
		return OWNER_ID;
	}

	public void setOWNER_ID(String oWNER_ID) {
		OWNER_ID = oWNER_ID;
	}

	public String[] getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(String[] uSER_ID) {
		USER_ID = uSER_ID;
	}

	public String getGROUP_ID() {
		return GROUP_ID;
	}

	public void setGROUP_ID(String gROUP_ID) {
		GROUP_ID = gROUP_ID;
	}

}
