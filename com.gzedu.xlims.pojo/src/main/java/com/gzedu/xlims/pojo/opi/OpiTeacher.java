/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.opi;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年7月19日
 * @version 2.5
 * @since JDK1.7
 *
 */
@XStreamAlias("TEACHER")
public class OpiTeacher {
	String MANAGER_ID;
	String REALNAME;
	String LMS_NO;
	String MANAGER_ROLE;
	String MOBILE_NO;
	String EMAIL;
	String EE_NO;
	String IMG_PATH;
	String SEX;
	String CREATED_BY;

	public OpiTeacher() {

	}

	public String getMANAGER_ID() {
		return MANAGER_ID;
	}

	public void setMANAGER_ID(String mANAGER_ID) {
		MANAGER_ID = mANAGER_ID;
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

	public String getMANAGER_ROLE() {
		return MANAGER_ROLE;
	}

	public void setMANAGER_ROLE(String mANAGER_ROLE) {
		MANAGER_ROLE = mANAGER_ROLE;
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

	public String getCREATED_BY() {
		return CREATED_BY;
	}

	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}

}
