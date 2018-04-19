/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 学员考勤信息DTO<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年06月04日
 * @version 2.5
 * @since JDK 1.7
 */
public class StudentClockingInDto {

	private String studentId;

	private String xm;

	private String xh;

	private String sjh;

	private String xxId;

	/**培养层次*/
	private String pyccName;

	/**期名称*/
	private String gradeName;

	/**年级*/
	private String yearName;

	/**专业*/
	private String zymc;

	/**班级名称*/
	private String bjmc;

	/**主要终端*/
	private String mainTerminal;

	/**是否在线*/
	private String isOnline;

	private String loginType;

	/**
	 * 首次登录
	 */
	private Date firstLogin;

	/**
	 * 最后一次登录
	 */
	private Date lastLogin;

	/**
	 * 登录总次数
	 */
	private BigDecimal countLogin;

	/**
	 * 登录总时长 [单位：分]
	 */
	private BigDecimal totalMinute;

	/**
	 * 未登录天数，离最后一次登录天数
	 */
	private BigDecimal noLoginDays;

	/**
	 * 班级id
	 */
	private String classId;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public Date getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Date firstLogin) {
		this.firstLogin = firstLogin;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public BigDecimal getCountLogin() {
		return countLogin;
	}

	public void setCountLogin(BigDecimal countLogin) {
		this.countLogin = countLogin;
	}

	public BigDecimal getTotalMinute() {
		return totalMinute;
	}

	public void setTotalMinute(BigDecimal totalMinute) {
		this.totalMinute = totalMinute;
	}

	public BigDecimal getNoLoginDays() {
		return noLoginDays;
	}

	public void setNoLoginDays(BigDecimal noLoginDays) {
		this.noLoginDays = noLoginDays;
	}


	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getSjh() {
		return sjh;
	}

	public void setSjh(String sjh) {
		this.sjh = sjh;
	}

	public String getPyccName() {
		return pyccName;
	}

	public void setPyccName(String pyccName) {
		this.pyccName = pyccName;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getYearName() {
		return yearName;
	}

	public void setYearName(String yearName) {
		this.yearName = yearName;
	}

	public String getZymc() {
		return zymc;
	}

	public void setZymc(String zymc) {
		this.zymc = zymc;
	}

	public String getBjmc() {
		return bjmc;
	}

	public void setBjmc(String bjmc) {
		this.bjmc = bjmc;
	}

	public String getMainTerminal() {
		return mainTerminal;
	}

	public void setMainTerminal(String mainTerminal) {
		this.mainTerminal = mainTerminal;
	}

	public String getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}
}
