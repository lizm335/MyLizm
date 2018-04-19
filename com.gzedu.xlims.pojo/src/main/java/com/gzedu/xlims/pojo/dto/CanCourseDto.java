/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.dto;

/**
 * 可选课程DTO<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年06月02日
 * @version 2.5
 * @since JDK 1.7
 */
public class CanCourseDto {

	private static final long serialVersionUID = 1L;

	private String courseId; // 课程ID

	private String kch; // 课程号

	private String kcmc; // 课程名称

	private String wsjxzk;// 教学方式 1 网上教学 0 非网上教学

	private String isOpen;// 有一章验收通过，这个都可以开课 0否 1是

	private String wsjxzkName = "";// 教学方式Name

	private String courseNature; // 课程性质

	private String pycc; // 课程层次

	private String pyccName = ""; // 课程层次Name

	private String category; // 课程学科

	private String subject; // 课程学科

	private String syhy;// 适用行业

	private String syzy;// 适用专业

	private Integer hour;// 学时

	private String isEnabled; // 是否启用 1启用,0停用

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getKch() {
		return kch;
	}

	public void setKch(String kch) {
		this.kch = kch;
	}

	public String getKcmc() {
		return kcmc;
	}

	public void setKcmc(String kcmc) {
		this.kcmc = kcmc;
	}

	public String getWsjxzk() {
		return wsjxzk;
	}

	public void setWsjxzk(String wsjxzk) {
		this.wsjxzk = wsjxzk;
	}

	public String getWsjxzkName() {
		return wsjxzkName;
	}

	public void setWsjxzkName(String wsjxzkName) {
		this.wsjxzkName = wsjxzkName;
	}

	public String getCourseNature() {
		return courseNature;
	}

	public void setCourseNature(String courseNature) {
		this.courseNature = courseNature;
	}

	public String getPycc() {
		return pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public String getPyccName() {
		return pyccName;
	}

	public void setPyccName(String pyccName) {
		this.pyccName = pyccName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSyhy() {
		return syhy;
	}

	public void setSyhy(String syhy) {
		this.syhy = syhy;
	}

	public String getSyzy() {
		return syzy;
	}

	public void setSyzy(String syzy) {
		this.syzy = syzy;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

}
