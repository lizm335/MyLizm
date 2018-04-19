/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dto;

/**
 * 课程班对应的课程信息DTO<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年03月29日
 * @version 2.5
 * @since JDK 1.7
 */
public class CourseClassInfoDto {

	/**
	 * 课程班
	 */
	private String classId;

	private String courseId;

	/**
	 * 课程名称
	 */
	private String kcmc;

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getKcmc() {
		return kcmc;
	}

	public void setKcmc(String kcmc) {
		this.kcmc = kcmc;
	}
}
