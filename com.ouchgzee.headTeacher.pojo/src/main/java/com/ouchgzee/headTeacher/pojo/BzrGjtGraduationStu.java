package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

/**
 * 学员毕业信息实体类<br>
 * The persistent class for the GJT_GRADUATION_STU database table.
 * 
 */
@Entity
@Table(name = "GJT_GRADUATION_STU")
// @NamedQuery(name = "GjtGraduationStu.findAll", query = "SELECT g FROM GjtGraduationStu g")
@Deprecated public class BzrGjtGraduationStu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@OneToOne
	@JoinColumn(name = "STUDENT_ID")
	private BzrGjtStudentInfo gjtStudentInfo;

	private String byzh;

	private String byzysh;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	private String fhbytj;

	private String fhxwtj;

	private String hbyrq;

	private String hxwrq;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	private String memo;

	@Column(name = "RECEIVE_STATUS")
	private String receiveStatus;

	private String sfsqxw;

	@Column(name = "SIGNUP_ID")
	private String signupId;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	private String xwzsh;

	@Column(name = "XX_ID")
	private String xxId;

	@Column(name = "XXZX_ID")
	private String xxzxId;

	public BzrGjtGraduationStu() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getByzh() {
		return this.byzh;
	}

	public void setByzh(String byzh) {
		this.byzh = byzh;
	}

	public String getByzysh() {
		return this.byzysh;
	}

	public void setByzysh(String byzysh) {
		this.byzysh = byzysh;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getFhbytj() {
		return this.fhbytj;
	}

	public void setFhbytj(String fhbytj) {
		this.fhbytj = fhbytj;
	}

	public String getFhxwtj() {
		return this.fhxwtj;
	}

	public void setFhxwtj(String fhxwtj) {
		this.fhxwtj = fhxwtj;
	}

	public String getHbyrq() {
		return this.hbyrq;
	}

	public void setHbyrq(String hbyrq) {
		this.hbyrq = hbyrq;
	}

	public String getHxwrq() {
		return this.hxwrq;
	}

	public void setHxwrq(String hxwrq) {
		this.hxwrq = hxwrq;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getReceiveStatus() {
		return this.receiveStatus;
	}

	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	public String getSfsqxw() {
		return this.sfsqxw;
	}

	public void setSfsqxw(String sfsqxw) {
		this.sfsqxw = sfsqxw;
	}

	public String getSignupId() {
		return this.signupId;
	}

	public void setSignupId(String signupId) {
		this.signupId = signupId;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public String getXwzsh() {
		return this.xwzsh;
	}

	public void setXwzsh(String xwzsh) {
		this.xwzsh = xwzsh;
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

	public BzrGjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(BzrGjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}
}