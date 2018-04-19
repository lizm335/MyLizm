package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 报名缴费费用实体类<br>
 * The persistent class for the GJT_SIGNUP_FEE database table.
 * 
 */
@Entity
@Table(name = "GJT_SIGNUP_FEE")
// @NamedQuery(name = "GjtSignupFee.findAll", query = "SELECT g FROM GjtSignupFee g")
@Deprecated public class BzrGjtSignupFee implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "FEE_ID")
	private String feeId;

	@ManyToOne
	@JoinColumn(name = "SIGNUP_ID")
	private BzrGjtSignup gjtSignup;

	@Column(name = "AUDIT_BY")
	private String auditBy;

	@Column(name = "AUDIT_RESULT")
	private String auditResult;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "FEE_METHOD_TYPE")
	private String feeMethodType;

	@Column(name = "FEE_NUM")
	private BigDecimal feeNum;

	@Column(name = "FEE_ORDER_NO")
	private String feeOrderNo;

	@Column(name = "FEE_STATUS")
	private String feeStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FEE_TIME")
	private Date feeTime;

	@Column(name = "FEE_TYPE")
	private String feeType;

	@Column(name = "INVOICE_NO")
	private String invoiceNo;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	@Column(name = "IS_INCOME")
	private String isIncome;

	private String memo;

	@Column(name = "PAYABLE_NUM")
	private BigDecimal payableNum;

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

	public BzrGjtSignupFee() {
	}

	public String getFeeId() {
		return this.feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public String getAuditBy() {
		return this.auditBy;
	}

	public void setAuditBy(String auditBy) {
		this.auditBy = auditBy;
	}

	public String getAuditResult() {
		return this.auditResult;
	}

	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
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

	public String getFeeMethodType() {
		return this.feeMethodType;
	}

	public void setFeeMethodType(String feeMethodType) {
		this.feeMethodType = feeMethodType;
	}

	public BigDecimal getFeeNum() {
		return this.feeNum;
	}

	public void setFeeNum(BigDecimal feeNum) {
		this.feeNum = feeNum;
	}

	public String getFeeOrderNo() {
		return this.feeOrderNo;
	}

	public void setFeeOrderNo(String feeOrderNo) {
		this.feeOrderNo = feeOrderNo;
	}

	public String getFeeStatus() {
		return this.feeStatus;
	}

	public void setFeeStatus(String feeStatus) {
		this.feeStatus = feeStatus;
	}

	public Date getFeeTime() {
		return this.feeTime;
	}

	public void setFeeTime(Date feeTime) {
		this.feeTime = feeTime;
	}

	public String getFeeType() {
		return this.feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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

	public String getIsIncome() {
		return this.isIncome;
	}

	public void setIsIncome(String isIncome) {
		this.isIncome = isIncome;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public BigDecimal getPayableNum() {
		return this.payableNum;
	}

	public void setPayableNum(BigDecimal payableNum) {
		this.payableNum = payableNum;
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

	/**
	 * @return the gjtSignup
	 */
	public BzrGjtSignup getGjtSignup() {
		return gjtSignup;
	}

	/**
	 * @param gjtSignup
	 *            the gjtSignup to set
	 */
	public void setGjtSignup(BzrGjtSignup gjtSignup) {
		this.gjtSignup = gjtSignup;
	}

}