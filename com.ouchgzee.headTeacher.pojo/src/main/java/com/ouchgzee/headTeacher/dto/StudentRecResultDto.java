/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dto;

import java.math.BigDecimal;

/**
 * 学员预约选课考试信息DTO<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月13日
 * @version 2.5
 * @since JDK 1.7
 */
public class StudentRecResultDto {

	private String studentId;

	private String xm;

	/**
	 * 年级
	 */
	private String gradeName;

	/**
	 * 培养层次
	 */
	private String pycc;

	/**
	 * 报读产品
	 */
	private String zymc;

	/**
	 * 可预约考试数
	 */
	private BigDecimal canExamNum;

	/**
	 * 已预约考试数
	 */
	private BigDecimal alreadyExamNum;

	/**
	 * 已预约考点
	 */
	private String examPointName;



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
	 * @param xm
	 *            the xm to set
	 */
	public void setXm(String xm) {
		this.xm = xm;
	}

	/**
	 * @return the gradeName
	 */
	public String getGradeName() {
		return gradeName;
	}

	/**
	 * @param gradeName
	 *            the gradeName to set
	 */
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getPycc() {
		return pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public String getZymc() {
		return zymc;
	}

	public void setZymc(String zymc) {
		this.zymc = zymc;
	}

	public BigDecimal getCanExamNum() {
		return canExamNum;
	}

	public void setCanExamNum(BigDecimal canExamNum) {
		this.canExamNum = canExamNum;
	}

	public BigDecimal getAlreadyExamNum() {
		return alreadyExamNum;
	}

	public void setAlreadyExamNum(BigDecimal alreadyExamNum) {
		this.alreadyExamNum = alreadyExamNum;
	}

	public String getExamPointName() {
		return examPointName;
	}

	public void setExamPointName(String examPointName) {
		this.examPointName = examPointName;
	}
}
