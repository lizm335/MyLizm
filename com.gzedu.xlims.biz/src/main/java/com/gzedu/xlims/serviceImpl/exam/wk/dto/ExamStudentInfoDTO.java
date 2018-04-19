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
public class ExamStudentInfoDTO {
	String APP_ID;

	ExamStudentInfo EXAM_USER_INFO;

	ExamStudentChoose EXAM_USER_CHOOSE;

	/**
	 * @param aPP_ID
	 * @param eXAM_USER_INFO
	 * @param eXAM_USER_CHOOSE
	 */
	public ExamStudentInfoDTO(String aPP_ID, ExamStudentInfo eXAM_USER_INFO, ExamStudentChoose eXAM_USER_CHOOSE) {
		super();
		APP_ID = aPP_ID;
		EXAM_USER_INFO = eXAM_USER_INFO;
		EXAM_USER_CHOOSE = eXAM_USER_CHOOSE;
	}

	public String getAPP_ID() {
		return APP_ID;
	}

	public void setAPP_ID(String aPP_ID) {
		APP_ID = aPP_ID;
	}

	public ExamStudentInfo getEXAM_USER_INFO() {
		return EXAM_USER_INFO;
	}

	public void setEXAM_USER_INFO(ExamStudentInfo eXAM_USER_INFO) {
		EXAM_USER_INFO = eXAM_USER_INFO;
	}

	public ExamStudentChoose getEXAM_USER_CHOOSE() {
		return EXAM_USER_CHOOSE;
	}

	public void setEXAM_USER_CHOOSE(ExamStudentChoose eXAM_USER_CHOOSE) {
		EXAM_USER_CHOOSE = eXAM_USER_CHOOSE;
	}

}
