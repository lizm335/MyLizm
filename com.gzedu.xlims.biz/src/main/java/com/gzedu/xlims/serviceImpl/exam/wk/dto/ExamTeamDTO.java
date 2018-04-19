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
@XStreamAlias("tranceData")
public class ExamTeamDTO {
	String APP_ID;

	ExamTeam EXAM_TERM;

	ExamProjectTeam EXAM_PROJECT_TERM;

	/**
	 * @param aPP_ID
	 * @param eXAM_TERM
	 * @param eXAM_PROJECT_TERM
	 */
	public ExamTeamDTO(String aPP_ID, ExamTeam eXAM_TERM, ExamProjectTeam eXAM_PROJECT_TERM) {
		super();
		APP_ID = aPP_ID;
		EXAM_TERM = eXAM_TERM;
		EXAM_PROJECT_TERM = eXAM_PROJECT_TERM;
	}

	public ExamTeam getEXAM_TERM() {
		return EXAM_TERM;
	}

	public void setEXAM_TERM(ExamTeam eXAM_TERM) {
		EXAM_TERM = eXAM_TERM;
	}

	public ExamProjectTeam getEXAM_PROJECT_TERM() {
		return EXAM_PROJECT_TERM;
	}

	public void setEXAM_PROJECT_TERM(ExamProjectTeam eXAM_PROJECT_TERM) {
		EXAM_PROJECT_TERM = eXAM_PROJECT_TERM;
	}

	public String getAPP_ID() {
		return APP_ID;
	}

	public void setAPP_ID(String aPP_ID) {
		APP_ID = aPP_ID;
	}

}
