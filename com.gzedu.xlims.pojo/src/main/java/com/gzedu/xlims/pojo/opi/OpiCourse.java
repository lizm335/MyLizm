/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.opi;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年7月14日
 * @version 2.5
 *
 */
@XStreamAlias("COURSE")
public class OpiCourse {
	String COURSE_ID;		// 课程id
	String COURSE_CODE;		// 课程编号
	String COURSE_STATUS;	// 课程状态
	String COURSE_NAME;		// 课程名称
	String COURSE_DES;		// 课程描述
	String PUBLISH_DT;		// 课程发布时间
	String CREATED_BY;		// 创建者id
	String PROFESSION;      // 所属专业
	String EDUCATION_LEVEL; // 学历层次
	String LABEL;           // 标签
	
	public String getPROFESSION() {
		return PROFESSION;
	}

	public void setPROFESSION(String pROFESSION) {
		PROFESSION = pROFESSION;
	}

	public String getEDUCATION_LEVEL() {
		return EDUCATION_LEVEL;
	}

	public void setEDUCATION_LEVEL(String eDUCATION_LEVEL) {
		EDUCATION_LEVEL = eDUCATION_LEVEL;
	}

	public String getLABEL() {
		return LABEL;
	}

	public void setLABEL(String lABEL) {
		LABEL = lABEL;
	}

	public String getCOURSE_ID() {
		return COURSE_ID;
	}

	public void setCOURSE_ID(String cOURSE_ID) {
		COURSE_ID = cOURSE_ID;
	}

	public String getCOURSE_CODE() {
		return COURSE_CODE;
	}

	public void setCOURSE_CODE(String cOURSE_CODE) {
		COURSE_CODE = cOURSE_CODE;
	}

	public String getCOURSE_STATUS() {
		return COURSE_STATUS;
	}

	public void setCOURSE_STATUS(String cOURSE_STATUS) {
		COURSE_STATUS = cOURSE_STATUS;
	}

	public String getCOURSE_NAME() {
		return COURSE_NAME;
	}

	public void setCOURSE_NAME(String cOURSE_NAME) {
		COURSE_NAME = cOURSE_NAME;
	}

	public String getCOURSE_DES() {
		return COURSE_DES;
	}

	public void setCOURSE_DES(String cOURSE_DES) {
		COURSE_DES = cOURSE_DES;
	}

	public String getPUBLISH_DT() {
		return PUBLISH_DT;
	}

	public void setPUBLISH_DT(String pUBLISH_DT) {
		PUBLISH_DT = pUBLISH_DT;
	}

	public String getCREATED_BY() {
		return CREATED_BY;
	}

	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}

}
