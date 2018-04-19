/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.third.eechat.data;

import java.util.List;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年8月17日
 * @version 2.5
 *
 */
public class EEIMcreateUserData {
	String APP_ID;
	List<EEIMUser> USER_SET;

	/**
	 * @param aPP_ID
	 * @param uSER_SET
	 */
	public EEIMcreateUserData(String aPP_ID, List<EEIMUser> uSER_SET) {
		super();
		APP_ID = aPP_ID;
		USER_SET = uSER_SET;
	}

	public String getAPP_ID() {
		return APP_ID;
	}

	public void setAPP_ID(String aPP_ID) {
		APP_ID = aPP_ID;
	}

	public List<EEIMUser> getUSER_SET() {
		return USER_SET;
	}

	public void setUSER_SET(List<EEIMUser> uSER_SET) {
		USER_SET = uSER_SET;
	}

}
