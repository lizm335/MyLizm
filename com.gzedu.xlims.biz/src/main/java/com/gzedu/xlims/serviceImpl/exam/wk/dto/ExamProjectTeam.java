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
@XStreamAlias("EXAM_PROJECT_TERM")
public class ExamProjectTeam {
	String PROJECT_TERM_ID;// 考试安排ID
	String PROJECT_ID;// 考试项目ID
	String TERM_ID;// 考试期ID
	String EXAM_TIME;// 考试安排允许考核次数
	String REMARK;//

	/**
	 * 必填字段
	 * 
	 * @param pROJECT_TERM_ID
	 * @param pROJECT_ID
	 * @param tERM_ID
	 */
	public ExamProjectTeam(String pROJECT_TERM_ID, String pROJECT_ID, String tERM_ID) {
		super();
		PROJECT_TERM_ID = pROJECT_TERM_ID;
		PROJECT_ID = pROJECT_ID;
		TERM_ID = tERM_ID;
	}

	public String getPROJECT_TERM_ID() {
		return PROJECT_TERM_ID;
	}

	public void setPROJECT_TERM_ID(String pROJECT_TERM_ID) {
		PROJECT_TERM_ID = pROJECT_TERM_ID;
	}

	public String getPROJECT_ID() {
		return PROJECT_ID;
	}

	public void setPROJECT_ID(String pROJECT_ID) {
		PROJECT_ID = pROJECT_ID;
	}

	public String getTERM_ID() {
		return TERM_ID;
	}

	public void setTERM_ID(String tERM_ID) {
		TERM_ID = tERM_ID;
	}

	public String getEXAM_TIME() {
		return EXAM_TIME;
	}

	public void setEXAM_TIME(String eXAM_TIME) {
		EXAM_TIME = eXAM_TIME;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}

}
