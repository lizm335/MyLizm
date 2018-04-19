/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dto;

import java.util.Date;

/**
 * 学员状态信息DTO<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月28日
 * @version 2.5
 * @since JDK 1.7
 */
public class StudentStateDto {

	private String studentId;

	private String xm;

	private String sfzh;

	/**
	 * 报读时间
	 */
	private Date signupDt;

	/**
	 * 报读产品
	 */
	private String zymc;

	/**
	 * 年级
	 */
	private String gradeName;

	/**
	 * 学习状态 1-正常学习 0-停止学习
	 */
	private String learningState;

	/**
	 * 资料完善情况 1-已完善 0-未完善
	 */
	private String dataState;

	/**
	 * 学籍状态 数据字典：StudentNumberStatus 1-开除学籍 2-正常注册 3-正常未注册 4-休学 5-退学 6-学习期限已过 7-延期
	 */
	private String xjzt;

	/**
	 * 毕业状态 0 未领取 1 已领取毕业证 2 已领取毕业证学位证
	 */
	private String receiveStatus;

	/**
	 * 学位证书个数
	 */
	private Integer certificateNum;

	/**
	 * 缴费方式 A:全额缴费 B:首年缴费 C:分期付款
	 */
	private String gkxlPaymentTpye;

	/**
	 * 费用缴费状态 1-已缴费 0-未缴费
	 */
	private Integer feeStatus;

	public StudentStateDto(String studentId, String xm, String sfzh, Date signupDt, String zymc, String gradeName, String learningState, String dataState, String xjzt, String receiveStatus, Integer certificateNum) {
		this.studentId = studentId;
		this.xm = xm;
		this.sfzh = sfzh;
		this.signupDt = signupDt;
		this.zymc = zymc;
		this.gradeName = gradeName;
		this.learningState = learningState;
		this.dataState = dataState;
		this.xjzt = xjzt;
		this.receiveStatus = receiveStatus;
		this.certificateNum = certificateNum;
	}

	/**
	 * @return the studentId
	 */
	public String getStudentId() {
		return studentId;
	}

	/**
	 * @param studentId
	 *            the studentId to set
	 */
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	/**
	 * @return the xm
	 */
	public String getXm() {
		return xm;
	}

	/**
	 * @return the sfzh
	 */
	public String getSfzh() {
		return sfzh;
	}

	/**
	 * @param sfzh
	 *            the sfzh to set
	 */
	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	/**
	 * @param xm
	 *            the xm to set
	 */
	public void setXm(String xm) {
		this.xm = xm;
	}

	public Date getSignupDt() {
		return signupDt;
	}

	public void setSignupDt(Date signupDt) {
		this.signupDt = signupDt;
	}

	public String getZymc() {
		return zymc;
	}

	public void setZymc(String zymc) {
		this.zymc = zymc;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getLearningState() {
		return learningState;
	}

	public void setLearningState(String learningState) {
		this.learningState = learningState;
	}

	public String getDataState() {
		return dataState;
	}

	public void setDataState(String dataState) {
		this.dataState = dataState;
	}

	public String getXjzt() {
		return xjzt;
	}

	public void setXjzt(String xjzt) {
		this.xjzt = xjzt;
	}

	public String getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	public Integer getCertificateNum() {
		return certificateNum;
	}

	public void setCertificateNum(Integer certificateNum) {
		this.certificateNum = certificateNum;
	}

	public String getGkxlPaymentTpye() {
		return gkxlPaymentTpye;
	}

	public void setGkxlPaymentTpye(String gkxlPaymentTpye) {
		this.gkxlPaymentTpye = gkxlPaymentTpye;
	}

	public Integer getFeeStatus() {
		return feeStatus;
	}

	public void setFeeStatus(Integer feeStatus) {
		this.feeStatus = feeStatus;
	}
}
