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
@XStreamAlias("EXAM_TERM")
public class ExamTeam {
	String TERM_ID;// 考试期ID
	String TERM_NAME;// 考试期名称
	String STARTED_DT;// 考试期起始时间 格式：2013-05-07
	String END_DT;// 考试期结束时间
	String REMARK;

	/**
	 * 必填字段
	 * 
	 * @param tERM_ID
	 * @param tERM_NAME
	 */
	public ExamTeam(String tERM_ID, String tERM_NAME) {
		super();
		TERM_ID = tERM_ID;
		TERM_NAME = tERM_NAME;
	}

	public String getTERM_ID() {
		return TERM_ID;
	}

	public void setTERM_ID(String tERM_ID) {
		TERM_ID = tERM_ID;
	}

	public String getTERM_NAME() {
		return TERM_NAME;
	}

	public void setTERM_NAME(String tERM_NAME) {
		TERM_NAME = tERM_NAME;
	}

	public String getSTARTED_DT() {
		return STARTED_DT;
	}

	public void setSTARTED_DT(String sTARTED_DT) {
		STARTED_DT = sTARTED_DT;
	}

	public String getEND_DT() {
		return END_DT;
	}

	public void setEND_DT(String eND_DT) {
		END_DT = eND_DT;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}

}
