/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.opi;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年7月15日
 * @version 2.5
 *
 */
@XStreamAlias("STUDENT")
public class OpiTchchoose {

	String MANAGER_CHOOSE_ID;// 教师选课id
	String TERMCOURSE_ID;// 期id
	String MANAGER_ID;// 教师id
	String CLASS_ID;// 期班级id
	String MANAGER_ROLE;// 教师角色
	String LMS_NO;// 教师账号id
	String CREATED_BY;// 创建者id
	String FROM_JIJIAO = "Y";// 来自继教
	String IS_INVISIBILITY;// 是否为隐形老师（Y/N）

	public String getMANAGER_CHOOSE_ID() {
		return MANAGER_CHOOSE_ID;
	}

	public void setMANAGER_CHOOSE_ID(String mANAGER_CHOOSE_ID) {
		MANAGER_CHOOSE_ID = mANAGER_CHOOSE_ID;
	}

	public String getTERMCOURSE_ID() {
		return TERMCOURSE_ID;
	}

	public void setTERMCOURSE_ID(String tERMCOURSE_ID) {
		TERMCOURSE_ID = tERMCOURSE_ID;
	}

	public String getMANAGER_ID() {
		return MANAGER_ID;
	}

	public void setMANAGER_ID(String mANAGER_ID) {
		MANAGER_ID = mANAGER_ID;
	}

	public String getCLASS_ID() {
		return CLASS_ID;
	}

	public void setCLASS_ID(String cLASS_ID) {
		CLASS_ID = cLASS_ID;
	}

	public String getMANAGER_ROLE() {
		return MANAGER_ROLE;
	}

	public void setMANAGER_ROLE(String mANAGER_ROLE) {
		MANAGER_ROLE = mANAGER_ROLE;
	}

	public String getLMS_NO() {
		return LMS_NO;
	}

	public void setLMS_NO(String lMS_NO) {
		LMS_NO = lMS_NO;
	}

	public String getCREATED_BY() {
		return CREATED_BY;
	}

	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}

	public String getFROM_JIJIAO() {
		return FROM_JIJIAO;
	}

	public void setFROM_JIJIAO(String fROM_JIJIAO) {
		FROM_JIJIAO = fROM_JIJIAO;
	}

	public String getIS_INVISIBILITY() {
		return IS_INVISIBILITY;
	}

	public void setIS_INVISIBILITY(String iS_INVISIBILITY) {
		IS_INVISIBILITY = iS_INVISIBILITY;
	}

}
