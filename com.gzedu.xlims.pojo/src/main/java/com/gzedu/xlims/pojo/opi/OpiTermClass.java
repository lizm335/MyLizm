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
@XStreamAlias("TERMCLASS")
public class OpiTermClass {
	String CLASS_ID;// 期班级id
	String TERMCOURSE_ID;// 期课程id
	String CLASS_NAME;// 期班级名称
	String YEAR;// 期名称
	String SPECIALTY_NAME;// 专业名称
	String IS_EXPERIENCE;// N普通班，Y体验章，S体验课
	String CREATED_BY;// 创建者id
	String FROM_JIJIAO = "Y";// 来自继教
	String OLD_TERMCOURSE_ID; // 转学期前的期课程
	String TURN_TERM_FLG;	  // 转学期标识 Y 是转学期生成的班级 N 不是
	
	public String getOLD_TERMCOURSE_ID() {
		return OLD_TERMCOURSE_ID;
	}

	public void setOLD_TERMCOURSE_ID(String oLD_TERMCOURSE_ID) {
		OLD_TERMCOURSE_ID = oLD_TERMCOURSE_ID;
	}

	public String getTURN_TERM_FLG() {
		return TURN_TERM_FLG;
	}

	public void setTURN_TERM_FLG(String tURN_TERM_FLG) {
		TURN_TERM_FLG = tURN_TERM_FLG;
	}

	public String getCLASS_ID() {
		return CLASS_ID;
	}

	public void setCLASS_ID(String cLASS_ID) {
		CLASS_ID = cLASS_ID;
	}

	public String getTERMCOURSE_ID() {
		return TERMCOURSE_ID;
	}

	public void setTERMCOURSE_ID(String tERMCOURSE_ID) {
		TERMCOURSE_ID = tERMCOURSE_ID;
	}

	public String getCLASS_NAME() {
		return CLASS_NAME;
	}

	public void setCLASS_NAME(String cLASS_NAME) {
		CLASS_NAME = cLASS_NAME;
	}

	public String getYEAR() {
		return YEAR;
	}

	public void setYEAR(String YEAR) {
		this.YEAR = YEAR;
	}

	public String getSPECIALTY_NAME() {
		return SPECIALTY_NAME;
	}

	public void setSPECIALTY_NAME(String SPECIALTY_NAME) {
		this.SPECIALTY_NAME = SPECIALTY_NAME;
	}

	public String getIS_EXPERIENCE() {
		return IS_EXPERIENCE;
	}

	public void setIS_EXPERIENCE(String iS_EXPERIENCE) {
		IS_EXPERIENCE = iS_EXPERIENCE;
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

}
