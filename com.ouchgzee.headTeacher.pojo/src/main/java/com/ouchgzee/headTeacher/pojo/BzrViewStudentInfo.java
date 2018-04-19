package com.ouchgzee.headTeacher.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the VIEW_STUDENT_INFO database table.
 * 
 */
@Entity
@Table(name = "VIEW_STUDENT_INFO")
// @NamedQuery(name = "ViewStudentInfo.findAll", query = "SELECT v FROM ViewStudentInfo v")
@Deprecated public class BzrViewStudentInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "STUDENT_ID")
	private String studentId;

	private String xh;

	private String xm;

	@Column(name = "AUDIT_STATE")
	private String auditState;

	@Column(name = "GRADE_ID")
	private String gradeId;

	@Column(name = "GRADE_NAME")
	private String gradeName;

	private String major;

	private String pycc;

	private String xbm;

	@Column(name = "XX_ID")
	private String xxId;

	@Column(name = "XXZX_ID")
	private String xxzxId;

	private String zymc;

	public BzrViewStudentInfo() {
	}

	public String getAuditState() {
		return this.auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getGradeId() {
		return this.gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeName() {
		return this.gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getMajor() {
		return this.major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getPycc() {
		return this.pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getXbm() {
		return this.xbm;
	}

	public void setXbm(String xbm) {
		this.xbm = xbm;
	}

	public String getXh() {
		return this.xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getXm() {
		return this.xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getXxzxId() {
		return this.xxzxId;
	}

	public void setXxzxId(String xxzxId) {
		this.xxzxId = xxzxId;
	}

	public String getZymc() {
		return this.zymc;
	}

	public void setZymc(String zymc) {
		this.zymc = zymc;
	}

}