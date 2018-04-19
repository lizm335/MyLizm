/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.exam.wk.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年7月14日
 * @version 2.5
 *
 */
@XStreamAlias("EXAM_USER_INFO")
public class ExamStudentInfo {
	String USER_ID;// 考生信息ID 否
	String USER_NAME;// String 考生信息姓名 否
	String USER_NO;// String 考生信息学号 是
	String PAPERS_NUMBER;// String 考生身份证 是
	String HEAD_IMAGE;// String 考生信息头像地址 是
	String REMARK;// String 是

	/**
	 * @param uSER_ID
	 * @param uSER_NAME
	 */
	public ExamStudentInfo(String uSER_ID, String uSER_NAME) {
		super();
		USER_ID = uSER_ID;
		USER_NAME = uSER_NAME;
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

	public String getUSER_NO() {
		return USER_NO;
	}

	public void setUSER_NO(String uSER_NO) {
		USER_NO = uSER_NO;
	}

	public String getPAPERS_NUMBER() {
		return PAPERS_NUMBER;
	}

	public void setPAPERS_NUMBER(String pAPERS_NUMBER) {
		PAPERS_NUMBER = pAPERS_NUMBER;
	}

	public String getHEAD_IMAGE() {
		return HEAD_IMAGE;
	}

	public void setHEAD_IMAGE(String hEAD_IMAGE) {
		HEAD_IMAGE = hEAD_IMAGE;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}

}
