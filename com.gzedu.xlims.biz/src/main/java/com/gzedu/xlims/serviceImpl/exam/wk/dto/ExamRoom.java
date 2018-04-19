/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.exam.wk.dto;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年7月14日
 * @version 2.5
 *
 */
public class ExamRoom {
	String EXAM_ROOM_ID;// String 考场ID 否
	String EXAM_ROOM_NAME;// String 考场名称 否

	/**
	 * @param eXAM_ROOM_ID
	 * @param eXAM_ROOM_NAME
	 */
	public ExamRoom(String eXAM_ROOM_ID, String eXAM_ROOM_NAME) {
		super();
		EXAM_ROOM_ID = eXAM_ROOM_ID;
		EXAM_ROOM_NAME = eXAM_ROOM_NAME;
	}

	public String getEXAM_ROOM_ID() {
		return EXAM_ROOM_ID;
	}

	public void setEXAM_ROOM_ID(String eXAM_ROOM_ID) {
		EXAM_ROOM_ID = eXAM_ROOM_ID;
	}

	public String getEXAM_ROOM_NAME() {
		return EXAM_ROOM_NAME;
	}

	public void setEXAM_ROOM_NAME(String eXAM_ROOM_NAME) {
		EXAM_ROOM_NAME = eXAM_ROOM_NAME;
	}

}
