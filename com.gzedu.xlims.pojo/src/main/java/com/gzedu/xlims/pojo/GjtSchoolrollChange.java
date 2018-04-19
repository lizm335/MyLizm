package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the GJT_SCHOOLROLL_CHANGE database table.
 * 
 */
@Entity
@Table(name = "GJT_SCHOOLROLL_CHANGE")
@NamedQuery(name = "GjtSchoolrollChange.findAll", query = "SELECT g FROM GjtSchoolrollChange g")
public class GjtSchoolrollChange implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "A_CHANGE_ID")
	private String aChangeId;

	@Column(name = "A_CHANGE_NAME")
	private String aChangeName;

	@Column(name = "AUDIT_BY")
	private String auditBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AUDIT_DATE")
	private Date auditDate;

	@Column(name = "AUDIT_RESULT")
	private String auditResult;

	@Column(name = "B_CHANGE_ID")
	private String bChangeId;

	@Column(name = "B_CHANGE_NAME")
	private String bChangeName;

	@Column(name = "CHANGE_TYPE")
	private String changeType;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "IS_ENABLED")
	private String isEnabled;

	private String memo;

	@Column(name = "STUDENT_ID")
	private String studentId;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@Column(name = "XX_ID")
	private String xxId;

	@Column(name = "XXZX_ID")
	private String xxzxId;

	public GjtSchoolrollChange() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAChangeId() {
		return this.aChangeId;
	}

	public void setAChangeId(String aChangeId) {
		this.aChangeId = aChangeId;
	}

	public String getAChangeName() {
		return this.aChangeName;
	}

	public void setAChangeName(String aChangeName) {
		this.aChangeName = aChangeName;
	}

	public String getAuditBy() {
		return this.auditBy;
	}

	public void setAuditBy(String auditBy) {
		this.auditBy = auditBy;
	}

	public Date getAuditDate() {
		return this.auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditResult() {
		return this.auditResult;
	}

	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}

	public String getBChangeId() {
		return this.bChangeId;
	}

	public void setBChangeId(String bChangeId) {
		this.bChangeId = bChangeId;
	}

	public String getBChangeName() {
		return this.bChangeName;
	}

	public void setBChangeName(String bChangeName) {
		this.bChangeName = bChangeName;
	}

	public String getChangeType() {
		return this.changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
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

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
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

}