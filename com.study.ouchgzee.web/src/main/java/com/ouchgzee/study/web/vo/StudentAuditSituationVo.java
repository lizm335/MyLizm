package com.ouchgzee.study.web.vo;

import java.math.BigDecimal;

/**
 * 学籍审核情况VO
 * 功能说明：
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年11月17日
 * @version 3.0
 *
 */
public class StudentAuditSituationVo {

	// 姓名
	private String xm;
	// 学籍状态
	private String xjzt;
	// 学员类型
	private String userType;
	// 学习中心
	private String studyCenterName;
	// 教学班名称
	private String teachClassName;
	// 班主任
	private String headTeacherName;
	// 是否入学，默认为0 1-是 0-否
	private String isEnteringSchool;
	
	private int perfectSignup; // 报名资料完善状态，默认0 1-已完善 0-未完善
	
	private int perfectCertificate; // 证件资料完善状态，默认0 1-已完善 0-未完善
	// 班主任审核状态，null-未流到该角色中 0-待审核 1-通过 2-不通过
	private BigDecimal oneAuditState;
	// 招生办审核状态，null-未流到该角色中 0-待审核 1-通过 2-不通过
	private BigDecimal twoAuditState;
	// 学籍科审核状态，null-未流到该角色中 0-待审核 1-通过 2-不通过
	private BigDecimal threeAuditState;
	
	public StudentAuditSituationVo() {
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getXjzt() {
		return xjzt;
	}

	public void setXjzt(String xjzt) {
		this.xjzt = xjzt;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getStudyCenterName() {
		return studyCenterName;
	}

	public void setStudyCenterName(String studyCenterName) {
		this.studyCenterName = studyCenterName;
	}

	public String getTeachClassName() {
		return teachClassName;
	}

	public void setTeachClassName(String teachClassName) {
		this.teachClassName = teachClassName;
	}

	public String getHeadTeacherName() {
		return headTeacherName;
	}

	public void setHeadTeacherName(String headTeacherName) {
		this.headTeacherName = headTeacherName;
	}

	public String getIsEnteringSchool() {
		return isEnteringSchool;
	}

	public void setIsEnteringSchool(String isEnteringSchool) {
		this.isEnteringSchool = isEnteringSchool;
	}

	public int getPerfectSignup() {
		return perfectSignup;
	}

	public void setPerfectSignup(int perfectSignup) {
		this.perfectSignup = perfectSignup;
	}

	public int getPerfectCertificate() {
		return perfectCertificate;
	}

	public void setPerfectCertificate(int perfectCertificate) {
		this.perfectCertificate = perfectCertificate;
	}

	public BigDecimal getOneAuditState() {
		return oneAuditState;
	}

	public void setOneAuditState(BigDecimal oneAuditState) {
		this.oneAuditState = oneAuditState;
	}

	public BigDecimal getTwoAuditState() {
		return twoAuditState;
	}

	public void setTwoAuditState(BigDecimal twoAuditState) {
		this.twoAuditState = twoAuditState;
	}

	public BigDecimal getThreeAuditState() {
		return threeAuditState;
	}

	public void setThreeAuditState(BigDecimal threeAuditState) {
		this.threeAuditState = threeAuditState;
	}

}
