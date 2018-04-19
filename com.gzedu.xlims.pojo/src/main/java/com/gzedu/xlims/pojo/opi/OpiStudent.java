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
public class OpiStudent {
	String STUD_ID;// 学员id
	String STUD_USER_ID; // 学员ATID
	String REALNAME;// 学员姓名
	String LMS_NO;// 学员学号
	String STUD_AREA;// 学员所在省份
	String MOBILE_NO;// 学员手机号码
	String EMAIL;// 学员邮箱
	String EE_NO;// 学员EE账号
	String IMG_PATH;// 学员头像URL地址
	String SEX;// 学员性别
	String XLCLASS_ID;// 实体班级id
	String XLCLASS_NAME;/// 实体班级名称
	String MANAGER_ID;// 班主任id
	String CREATED_BY;// 创建者id

	public String getSTUD_ID() {
		return STUD_ID;
	}

	public void setSTUD_ID(String sTUD_ID) {
		STUD_ID = sTUD_ID;
	}

	public String getSTUD_USER_ID() {
		return STUD_USER_ID;
	}

	public void setSTUD_USER_ID(String STUD_USER_ID) {
		this.STUD_USER_ID = STUD_USER_ID;
	}

	public String getREALNAME() {
		return REALNAME;
	}

	public void setREALNAME(String rEALNAME) {
		REALNAME = rEALNAME;
	}

	public String getLMS_NO() {
		return LMS_NO;
	}

	public void setLMS_NO(String lMS_NO) {
		LMS_NO = lMS_NO;
	}

	public String getSTUD_AREA() {
		return STUD_AREA;
	}

	public void setSTUD_AREA(String sTUD_AREA) {
		STUD_AREA = sTUD_AREA;
	}

	public String getMOBILE_NO() {
		return MOBILE_NO;
	}

	public void setMOBILE_NO(String mOBILE_NO) {
		MOBILE_NO = mOBILE_NO;
	}

	public String getEMAIL() {
		return EMAIL;
	}

	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}

	public String getEE_NO() {
		return EE_NO;
	}

	public void setEE_NO(String eE_NO) {
		EE_NO = eE_NO;
	}

	public String getIMG_PATH() {
		return IMG_PATH;
	}

	public void setIMG_PATH(String iMG_PATH) {
		IMG_PATH = iMG_PATH;
	}

	public String getSEX() {
		return SEX;
	}

	public void setSEX(String sEX) {
		SEX = sEX;
	}

	public String getXLCLASS_ID() {
		return XLCLASS_ID;
	}

	public void setXLCLASS_ID(String xLCLASS_ID) {
		XLCLASS_ID = xLCLASS_ID;
	}

	public String getXLCLASS_NAME() {
		return XLCLASS_NAME;
	}

	public void setXLCLASS_NAME(String xLCLASS_NAME) {
		XLCLASS_NAME = xLCLASS_NAME;
	}

	public String getMANAGER_ID() {
		return MANAGER_ID;
	}

	public void setMANAGER_ID(String mANAGER_ID) {
		MANAGER_ID = mANAGER_ID;
	}

	public String getCREATED_BY() {
		return CREATED_BY;
	}

	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}

}
