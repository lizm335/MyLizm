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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 发票信息实体类<br>
 * The persistent class for the GJT_FEE_INVOICE database table.
 * 
 */
@Entity
@Table(name = "GJT_FEE_INVOICE")
// @NamedQuery(name = "GjtFeeInvoice.findAll", query = "SELECT g FROM GjtFeeInvoice g")
@Deprecated public class BzrGjtFeeInvoice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "INVOICE_ID")
	private String invoiceId;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "ORDER_NO", referencedColumnName = "FEE_ORDER_NO")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtSignupFee gjtSignupFee;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "APPLY_RECORD_ID")
	private BzrGjtBillApplyRecord gjtBillApplyRecord;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "INVOICE_CODE")
	private String invoiceCode;

	@Column(name = "INVOICE_FEE")
	private BigDecimal invoiceFee;

	@Column(name = "INVOICE_NUM")
	private String invoiceNum;

	@Column(name = "INVOICE_SERIAL")
	private String invoiceSerial;

	@Column(name = "INVOICE_TYPE")
	private String invoiceType;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ISSUE_DT")
	private Date issueDt;

	@Column(name = "ISSUE_STATUS")
	private String issueStatus;

	private String memo;

	@Column(name = "PAY_WAY")
	private String payWay;

	@Column(name = "RECEIVE_WAY")
	private String receiveWay;

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

	public BzrGjtFeeInvoice() {
	}

	public String getInvoiceId() {
		return this.invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
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

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public BigDecimal getInvoiceFee() {
		return this.invoiceFee;
	}

	public void setInvoiceFee(BigDecimal invoiceFee) {
		this.invoiceFee = invoiceFee;
	}

	public String getInvoiceNum() {
		return this.invoiceNum;
	}

	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public String getInvoiceSerial() {
		return this.invoiceSerial;
	}

	public void setInvoiceSerial(String invoiceSerial) {
		this.invoiceSerial = invoiceSerial;
	}

	public String getInvoiceType() {
		return this.invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
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

	public Date getIssueDt() {
		return this.issueDt;
	}

	public void setIssueDt(Date issueDt) {
		this.issueDt = issueDt;
	}

	public String getIssueStatus() {
		return this.issueStatus;
	}

	public void setIssueStatus(String issueStatus) {
		this.issueStatus = issueStatus;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPayWay() {
		return this.payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getReceiveWay() {
		return this.receiveWay;
	}

	public void setReceiveWay(String receiveWay) {
		this.receiveWay = receiveWay;
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

	/**
	 * @return the gjtSignupFee
	 */
	public BzrGjtSignupFee getGjtSignupFee() {
		return gjtSignupFee;
	}

	/**
	 * @param gjtSignupFee
	 *            the gjtSignupFee to set
	 */
	public void setGjtSignupFee(BzrGjtSignupFee gjtSignupFee) {
		this.gjtSignupFee = gjtSignupFee;
	}

	/**
	 * @return the gjtBillApplyRecord
	 */
	public BzrGjtBillApplyRecord getGjtBillApplyRecord() {
		return gjtBillApplyRecord;
	}

	/**
	 * @param gjtBillApplyRecord
	 *            the gjtBillApplyRecord to set
	 */
	public void setGjtBillApplyRecord(BzrGjtBillApplyRecord gjtBillApplyRecord) {
		this.gjtBillApplyRecord = gjtBillApplyRecord;
	}

}