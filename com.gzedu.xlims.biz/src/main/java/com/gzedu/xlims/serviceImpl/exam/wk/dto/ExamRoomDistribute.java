/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.exam.wk.dto;

/**
 * 功能说明：考场分配
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年7月14日
 * @version 2.5
 *
 */
public class ExamRoomDistribute {
	String DISTRIBUTE_ID;// String 考室分配ID 否
	String EXAM_ROOM_ID;// String 考室ID 否
	String PROJECT_TERM_ID;// String 考试安排ID 否
	String EXAM_ROOM_TYPE;// String 考试形式 是 线下offline 在线online
	String START_DT;// String 开始时间 是
	String FINISH_DT;// String 结束时间 是

	/**
	 * @param dISTRIBUTE_ID
	 * @param eXAM_ROOM_ID
	 * @param pROJECT_TERM_ID
	 */
	public ExamRoomDistribute(String dISTRIBUTE_ID, String eXAM_ROOM_ID, String pROJECT_TERM_ID) {
		super();
		DISTRIBUTE_ID = dISTRIBUTE_ID;
		EXAM_ROOM_ID = eXAM_ROOM_ID;
		PROJECT_TERM_ID = pROJECT_TERM_ID;
	}

	public String getDISTRIBUTE_ID() {
		return DISTRIBUTE_ID;
	}

	public void setDISTRIBUTE_ID(String dISTRIBUTE_ID) {
		DISTRIBUTE_ID = dISTRIBUTE_ID;
	}

	public String getEXAM_ROOM_ID() {
		return EXAM_ROOM_ID;
	}

	public void setEXAM_ROOM_ID(String eXAM_ROOM_ID) {
		EXAM_ROOM_ID = eXAM_ROOM_ID;
	}

	public String getPROJECT_TERM_ID() {
		return PROJECT_TERM_ID;
	}

	public void setPROJECT_TERM_ID(String pROJECT_TERM_ID) {
		PROJECT_TERM_ID = pROJECT_TERM_ID;
	}

	public String getEXAM_ROOM_TYPE() {
		return EXAM_ROOM_TYPE;
	}

	public void setEXAM_ROOM_TYPE(String eXAM_ROOM_TYPE) {
		EXAM_ROOM_TYPE = eXAM_ROOM_TYPE;
	}

	public String getSTART_DT() {
		return START_DT;
	}

	public void setSTART_DT(String sTART_DT) {
		START_DT = sTART_DT;
	}

	public String getFINISH_DT() {
		return FINISH_DT;
	}

	public void setFINISH_DT(String fINISH_DT) {
		FINISH_DT = fINISH_DT;
	}

}
