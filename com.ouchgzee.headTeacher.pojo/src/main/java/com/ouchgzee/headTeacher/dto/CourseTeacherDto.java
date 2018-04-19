/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dto;

/**
 * 课程班的辅导老师信息DTO<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年08月06日
 * @version 2.5
 * @since JDK 1.7
 */
public class CourseTeacherDto {

	private String employeeId;

	private String xm;

	/**
	 * 课程班名称
	 */
	private String bjmc;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEMPLOYEEID(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getXm() {
		return xm;
	}

	public void setXM(String xm) {
		this.xm = xm;
	}

	public String getBjmc() {
		return bjmc;
	}

	public void setBJMC(String bjmc) {
		this.bjmc = bjmc;
	}
}
