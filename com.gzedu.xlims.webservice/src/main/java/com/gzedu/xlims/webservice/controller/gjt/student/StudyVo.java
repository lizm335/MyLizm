package com.gzedu.xlims.webservice.controller.gjt.student;

public class StudyVo {
	private String name;// 姓名
	private String profession;// 专业
	private String pycc;// 层次
	private String grade;// 年级
	private String enrollmentStatus;// 入学状态
	private String creditStatus;// 学籍状态
	private String credit;// 已获学分
	private String creditSum; // 总学分
	private String studyNumberOfTimes;// 学习次数
	private String studyTimeLength;// 学习时长(小时)
	private String loginUrl;// 个人中心登录url
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getPycc() {
		return pycc;
	}
	public void setPycc(String pycc) {
		this.pycc = pycc;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getEnrollmentStatus() {
		return enrollmentStatus;
	}
	public void setEnrollmentStatus(String enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}
	public String getCreditStatus() {
		return creditStatus;
	}
	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getCreditSum() {
		return creditSum;
	}
	public void setCreditSum(String creditSum) {
		this.creditSum = creditSum;
	}
	public String getStudyNumberOfTimes() {
		return studyNumberOfTimes;
	}
	public void setStudyNumberOfTimes(String studyNumberOfTimes) {
		this.studyNumberOfTimes = studyNumberOfTimes;
	}
	public String getStudyTimeLength() {
		return studyTimeLength;
	}
	public void setStudyTimeLength(String studyTimeLength) {
		this.studyTimeLength = studyTimeLength;
	}
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

}