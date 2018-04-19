/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.third.eechat.data;

import java.util.List;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年8月21日
 * @version 2.5
 *
 */
public class EEIMcreateUserReturnData {
	String MESSAGE;
	String USER_ID;
	String STATUS;// 1成功 0,失败
	Integer NUM;
	List<EEIMMembersEENO> membersEENOlist;

	public String getMESSAGE() {
		return MESSAGE;
	}

	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}

	public String getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public Integer getNUM() {
		return NUM;
	}

	public void setNUM(Integer nUM) {
		NUM = nUM;
	}

	public List<EEIMMembersEENO> getMembersEENOlist() {
		return membersEENOlist;
	}

	public void setMembersEENOlist(List<EEIMMembersEENO> membersEENOlist) {
		this.membersEENOlist = membersEENOlist;
	}

}
