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
@XStreamAlias("EXAM_USER_CHOOSE")
public class ExamStudentChoose {
	String CHOOSE_ID;// String 考生报考ID 否
	String USER_ID;// String 考生ID 否
	String DISTRIBUTE_ID;// String 考场分配ID 否
	String PROJECT_TERM_ID;// String 考试安排ID 否
	String TICKET_NO;// String 准考证号 是
	String SEAT_NO;// long 座位号 是
	String EXAM_DT;// Date 考试完成时间 是
	String EXAM_IP;// String 考生电脑的ID 是
	String POINT;// String 得分 是
	String REMARK;// String 是

	/**
	 * @param cHOOSE_ID
	 * @param uSER_ID
	 * @param dISTRIBUTE_ID
	 * @param pROJECT_TERM_ID
	 */
	public ExamStudentChoose(String cHOOSE_ID, String uSER_ID, String dISTRIBUTE_ID, String pROJECT_TERM_ID) {
		super();
		CHOOSE_ID = cHOOSE_ID;
		USER_ID = uSER_ID;
		DISTRIBUTE_ID = dISTRIBUTE_ID;
		PROJECT_TERM_ID = pROJECT_TERM_ID;
	}

	public String getCHOOSE_ID() {
		return CHOOSE_ID;
	}

	public void setCHOOSE_ID(String cHOOSE_ID) {
		CHOOSE_ID = cHOOSE_ID;
	}

	public String getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}

	public String getDISTRIBUTE_ID() {
		return DISTRIBUTE_ID;
	}

	public void setDISTRIBUTE_ID(String dISTRIBUTE_ID) {
		DISTRIBUTE_ID = dISTRIBUTE_ID;
	}

	public String getPROJECT_TERM_ID() {
		return PROJECT_TERM_ID;
	}

	public void setPROJECT_TERM_ID(String pROJECT_TERM_ID) {
		PROJECT_TERM_ID = pROJECT_TERM_ID;
	}

	public String getTICKET_NO() {
		return TICKET_NO;
	}

	public void setTICKET_NO(String tICKET_NO) {
		TICKET_NO = tICKET_NO;
	}

	public String getSEAT_NO() {
		return SEAT_NO;
	}

	public void setSEAT_NO(String sEAT_NO) {
		SEAT_NO = sEAT_NO;
	}

	public String getEXAM_DT() {
		return EXAM_DT;
	}

	public void setEXAM_DT(String eXAM_DT) {
		EXAM_DT = eXAM_DT;
	}

	public String getEXAM_IP() {
		return EXAM_IP;
	}

	public void setEXAM_IP(String eXAM_IP) {
		EXAM_IP = eXAM_IP;
	}

	public String getPOINT() {
		return POINT;
	}

	public void setPOINT(String pOINT) {
		POINT = pOINT;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}

}
