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
public class ExamRoomDTO {
	String PROJECT_ID;

	ExamRoom EXAM_ROOM;

	ExamRoomDistribute EXAM_ROOM_DISTRIBUTE;

	/**
	 * @param pROJECT_ID
	 * @param eXAM_ROOM
	 * @param eXAM_ROOM_DISTRIBUTE
	 */
	public ExamRoomDTO(String pROJECT_ID, ExamRoom eXAM_ROOM, ExamRoomDistribute eXAM_ROOM_DISTRIBUTE) {
		super();
		PROJECT_ID = pROJECT_ID;
		EXAM_ROOM = eXAM_ROOM;
		EXAM_ROOM_DISTRIBUTE = eXAM_ROOM_DISTRIBUTE;
	}

	public String getPROJECT_ID() {
		return PROJECT_ID;
	}

	public void setPROJECT_ID(String pROJECT_ID) {
		PROJECT_ID = pROJECT_ID;
	}

	public ExamRoom getEXAM_ROOM() {
		return EXAM_ROOM;
	}

	public void setEXAM_ROOM(ExamRoom eXAM_ROOM) {
		EXAM_ROOM = eXAM_ROOM;
	}

	public ExamRoomDistribute getEXAM_ROOM_DISTRIBUTE() {
		return EXAM_ROOM_DISTRIBUTE;
	}

	public void setEXAM_ROOM_DISTRIBUTE(ExamRoomDistribute eXAM_ROOM_DISTRIBUTE) {
		EXAM_ROOM_DISTRIBUTE = eXAM_ROOM_DISTRIBUTE;
	}

}
