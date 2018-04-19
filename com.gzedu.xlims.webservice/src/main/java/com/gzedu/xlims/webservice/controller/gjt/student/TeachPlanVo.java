package com.gzedu.xlims.webservice.controller.gjt.student;

import java.util.List;

/**
 * 教学计划VO
 */
public class TeachPlanVo {
	private String termName;// 学期名称
	private int courseNum;// 课程数量
	private int totalCredit;// 总学分
	private List<TeachPlanDetailVo> teachPlanDetailList;

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public int getCourseNum() {
		return courseNum;
	}

	public void setCourseNum(int courseNum) {
		this.courseNum = courseNum;
	}

	public int getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(int totalCredit) {
		this.totalCredit = totalCredit;
	}

	public List<TeachPlanDetailVo> getTeachPlanDetailList() {
		return teachPlanDetailList;
	}

	public void setTeachPlanDetailList(List<TeachPlanDetailVo> teachPlanDetailList) {
		this.teachPlanDetailList = teachPlanDetailList;
	}
}