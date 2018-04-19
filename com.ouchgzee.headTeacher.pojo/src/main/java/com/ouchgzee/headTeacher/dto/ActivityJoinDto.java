/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.dto;

import java.util.Date;

/**
 * 班级活动参与人员信息<br>
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月20日
 * @version 1.0
 *
 */
public class ActivityJoinDto {
	/**
	 * 报名日期
	 */
	private Date joinDt;
	/**
	 * 报名人姓名
	 */
	private String name;
	/**
	 * 报名人头像
	 */
	private String avatar;
	/**
	 * 报名人性别
	 */
	private String sex;
	/**
	 * 报名学生ID
	 */
	private String studentId;

	/**
	 * @return the studentID
	 */
	public String getStudentId() {
		return studentId;
	}

	/**
	 * @param studentID
	 *            the studentID to set
	 */
	public void setSTUDENT_ID(String studentId) {
		this.studentId = studentId;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex
	 *            the sex to set
	 */
	public void setXB(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar
	 *            the avatar to set
	 */
	public void setAVATAR(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the joinDt
	 */
	public Date getJoinDt() {
		return joinDt;
	}

	/**
	 * @param joinDt
	 *            the joinDt to set
	 */
	public void setJOIN_DT(Date joinDt) {
		this.joinDt = joinDt;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setXM(String name) {
		this.name = name;
	}

}
