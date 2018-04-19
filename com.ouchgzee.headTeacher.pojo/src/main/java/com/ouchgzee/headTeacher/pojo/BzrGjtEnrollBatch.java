package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 招生批次实体类<br>
 * The persistent class for the GJT_ENROLL_BATCH database table.
 * 
 */
@Entity
@Table(name = "GJT_ENROLL_BATCH")
// @NamedQuery(name = "GjtEnrollBatch.findAll", query = "SELECT g FROM GjtEnrollBatch g")
@Deprecated public class BzrGjtEnrollBatch implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ENROLL_BATCH_ID")
	private String enrollBatchId;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "GRADE_ID")
	private BzrGjtGrade gjtGrade;

	@Column(name = "BATCH_CODE")
	private String batchCode;

	@Column(name = "BATCH_NAME")
	private String batchName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BATCH_PUBLISH_DATE")
	private Date batchPublishDate;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENROLL_EDATE")
	private Date enrollEdate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENROLL_SDATE")
	private Date enrollSdate;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	@Column(name = "MANAGE_MODE")
	private String manageMode;

	private String memo;

	@Column(name = "ORG_CODE")
	private String orgCode;

	@Column(name = "ORG_ID")
	private String orgId;

	@Column(name = "TERM_ID")
	private String termId;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@Column(name = "XX_ID")
	private String xxId;

	@Column(name = "XXZX_ID")
	private String xxzxId;

	@Column(name = "YX_ID")
	private String yxId;

	public BzrGjtEnrollBatch() {
	}

	public String getEnrollBatchId() {
		return this.enrollBatchId;
	}

	public void setEnrollBatchId(String enrollBatchId) {
		this.enrollBatchId = enrollBatchId;
	}

	public String getBatchCode() {
		return this.batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getBatchName() {
		return this.batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public Date getBatchPublishDate() {
		return this.batchPublishDate;
	}

	public void setBatchPublishDate(Date batchPublishDate) {
		this.batchPublishDate = batchPublishDate;
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

	public Date getEnrollEdate() {
		return this.enrollEdate;
	}

	public void setEnrollEdate(Date enrollEdate) {
		this.enrollEdate = enrollEdate;
	}

	public Date getEnrollSdate() {
		return this.enrollSdate;
	}

	public void setEnrollSdate(Date enrollSdate) {
		this.enrollSdate = enrollSdate;
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

	public String getManageMode() {
		return this.manageMode;
	}

	public void setManageMode(String manageMode) {
		this.manageMode = manageMode;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getTermId() {
		return this.termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
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

	public String getYxId() {
		return this.yxId;
	}

	public void setYxId(String yxId) {
		this.yxId = yxId;
	}

	/**
	 * @return the gjtGrade
	 */
	public BzrGjtGrade getGjtGrade() {
		return gjtGrade;
	}

	/**
	 * @param gjtGrade
	 *            the gjtGrade to set
	 */
	public void setGjtGrade(BzrGjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

}