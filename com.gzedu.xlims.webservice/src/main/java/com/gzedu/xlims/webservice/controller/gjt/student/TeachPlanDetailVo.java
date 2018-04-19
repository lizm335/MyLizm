package com.gzedu.xlims.webservice.controller.gjt.student;

/**
 * 教学计划详情VO
 */
public class TeachPlanDetailVo {
	private String courseName;// 课程名称
	private String examType;// 考试形式
	private String examTypeName;// 考试形式名称
	private int credit; // 学分

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getExamTypeName() {
		return examTypeName;
	}

	public void setExamTypeName(String examTypeName) {
		this.examTypeName = examTypeName;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}
}