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
public class ImportExamAppointmentRecord extends ImportModel {
	String xh;// 学号
	String name;// 姓名
	String cardNo;// 身份证
	String courseNo;// 课程号
	String subjectName;// 课程名称
	String examType;// 考试类型, 笔考、网考、机考
	String operation;// 操作, 预约/取消预约
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

	public String getCourseNo() {
		return courseNo;
	}

	public void setCourseNo(String courseNo) {
		this.courseNo = courseNo;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

}
