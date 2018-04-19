/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.exam.vo;

import com.gzedu.xlims.common.excel.ImportModel;

/**
 * 功能说明：导入并返回接口模版，属性的顺序就是excel列的顺序
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年12月2日
 * @version 2.5
 *
 */
public class ImportExamStudentRoomRecord extends ImportModel {
	String xh;// 学号
	String name;// 姓名
	String cardNo;// 身份证
//	String subjectName;// 科目名称
	String courseName;// 课程名称
	String examNo;// 科目试卷号
	String examType;// 考试类型, 笔考、机考
	String pointName;// 考点名称
	String roomName;// 考场名称
	String seatNo;// 座位号
	String resultMsg;// 反馈状态

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

//	public String getSubjectName() {
//		return subjectName;
//	}
//
//	public void setSubjectName(String subjectName) {
//		this.subjectName = subjectName;
//	}
	
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseName() {
		return courseName;
	}

	public String getExamNo() {
		return examNo;
	}

	public void setExamNo(String examNo) {
		this.examNo = examNo;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

}
