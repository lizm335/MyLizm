/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.third.eechat.data;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年8月18日
 * @version 2.5 注（1）：群类型GROUP_TYPE：Public 群， Private 讨论组。
 *          注（2）：老师本身默认是管理员。只有建群的时候才使用到。创建讨论组的时候不需要用到 注（3）：群主默认是班主任。
 *          注（4）：申请加群处理方式。包含 1：FreeAccess（自由加入）， 2：NeedPermission（需要验证），
 *          3：DisableApply（禁止加群）: 不填默认为NeedPermission（需要验证）
 * 
 */
public class EEIMGroup {
	String APP_ID;// 平台ID（公共约束1）
	String GROUP_ID;// 群ID-对应教务班级ID
	String OWNER_ID;// 群主ID（注3）EE_NO
	String GROUP_NAME;// 群名称
	String GROUP_TYPE;// 群类型
	String GROUP_INTRO = "";// 群简介
	String GROUP_IMG = "/opt/group/pictures/1.png";// 群头像 默认头像
	String GROUP_DESC = "";// 群说明
	String GROUP_VALIDATE = "NeedPermission";// 验证方式（注4）
	String GROUP_USERNUMS = "2000";// 群的最大人数
	String[] GROUP_USERS;// 群成员
	String[] TEACHERS;// 老师成员（注2）

	/**
	 * @param oWNER_ID
	 * @param gROUP_NAME
	 * @param gROUP_USERS
	 */
	public EEIMGroup(String gROUP_ID, String oWNER_ID, String gROUP_NAME, String[] gROUP_USERS) {
		super();
		GROUP_ID = gROUP_ID;
		OWNER_ID = oWNER_ID;
		GROUP_NAME = gROUP_NAME;
		GROUP_USERS = gROUP_USERS;
	}

	/**
	 * 
	 */
	public EEIMGroup() {
		super();
	}

	public String getAPP_ID() {
		return APP_ID;
	}

	public void setAPP_ID(String aPP_ID) {
		APP_ID = aPP_ID;
	}

	public String getGROUP_ID() {
		return GROUP_ID;
	}

	public void setGROUP_ID(String GROUP_ID) {
		this.GROUP_ID = GROUP_ID;
	}

	public String getOWNER_ID() {
		return OWNER_ID;
	}

	public void setOWNER_ID(String oWNER_ID) {
		OWNER_ID = oWNER_ID;
	}

	public String getGROUP_NAME() {
		return GROUP_NAME;
	}

	public void setGROUP_NAME(String gROUP_NAME) {
		GROUP_NAME = gROUP_NAME;
	}

	public String getGROUP_TYPE() {
		return GROUP_TYPE;
	}

	public void setGROUP_TYPE(String gROUP_TYPE) {
		GROUP_TYPE = gROUP_TYPE;
	}

	public String getGROUP_INTRO() {
		return GROUP_INTRO;
	}

	public void setGROUP_INTRO(String gROUP_INTRO) {
		GROUP_INTRO = gROUP_INTRO;
	}

	public String getGROUP_IMG() {
		return GROUP_IMG;
	}

	public void setGROUP_IMG(String gROUP_IMG) {
		GROUP_IMG = gROUP_IMG;
	}

	public String getGROUP_DESC() {
		return GROUP_DESC;
	}

	public void setGROUP_DESC(String gROUP_DESC) {
		GROUP_DESC = gROUP_DESC;
	}

	public String getGROUP_VALIDATE() {
		return GROUP_VALIDATE;
	}

	public void setGROUP_VALIDATE(String gROUP_VALIDATE) {
		GROUP_VALIDATE = gROUP_VALIDATE;
	}

	public String getGROUP_USERNUMS() {
		return GROUP_USERNUMS;
	}

	public void setGROUP_USERNUMS(String gROUP_USERNUMS) {
		GROUP_USERNUMS = gROUP_USERNUMS;
	}

	public String[] getGROUP_USERS() {
		return GROUP_USERS;
	}

	public void setGROUP_USERS(String[] gROUP_USERS) {
		GROUP_USERS = gROUP_USERS;
	}

	public String[] getTEACHERS() {
		return TEACHERS;
	}

	public void setTEACHERS(String[] tEACHERS) {
		TEACHERS = tEACHERS;
	}

}
