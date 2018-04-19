/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 功能说明：班级活动参与人员信息
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月28日
 * @version 2.5
 *
 */
public class ActivityJoinDto {
	private Date joinDt;// 报名日期
	private String name;// 报名人姓名
	private String avatar;// 报名人头像
	private String sex;// 报名人性别
	private String studentId;// 报名学生ID
	private String JoinDate;
	private BigDecimal auditStatus;// 审核状态

	private BigDecimal ROWNUM_;

	public BigDecimal getAuditStatus() {
		return auditStatus;
	}

	public void setAUDIT_STATUS(BigDecimal auditStatus) {
		this.auditStatus = auditStatus;
	}

	public BigDecimal getROWNUM_() {
		return ROWNUM_;
	}

	public void setROWNUM_(BigDecimal rOWNUM_) {
		ROWNUM_ = rOWNUM_;
	}

	public String getJoinDate() {
		return JoinDate;
	}

	public void setJoinDate(String joinDate) {
		JoinDate = joinDate;
	}

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
