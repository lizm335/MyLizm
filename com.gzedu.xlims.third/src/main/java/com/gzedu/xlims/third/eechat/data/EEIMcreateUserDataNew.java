/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.third.eechat.data;

import java.util.List;

/**
 * 功能说明：
 * 
 * @author 卢林林 lulinlin@eenet.com
 * @Date 2017年8月17日
 * @version 2.5
 *
 */
public class EEIMcreateUserDataNew {
	String APP_ID;
	List<EEIMUserNew> USER_SET;

	/**
	 * @param aPP_ID
	 * @param uSER_SET
	 */
	public EEIMcreateUserDataNew(String aPP_ID, List<EEIMUserNew> uSER_SET) {
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

	public List<EEIMUserNew> getUSER_SET() {
		return USER_SET;
	}

	public void setUSER_SET(List<EEIMUserNew> uSER_SET) {
		USER_SET = uSER_SET;
	}

}
