package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the GJT_SCHOOL_ROLL_TRANS_AUDIT database table.
 * 
 */
@Entity
@Table(name="GJT_SCHOOL_ROLL_TRANS_AUDIT")
@NamedQuery(name="GjtSchoolRollTransAudit.findAll", query="SELECT g FROM GjtSchoolRollTransAudit g")
public class GjtSchoolRollTransAudit implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public GjtSchoolRollTransAudit(String id) {
		this.id = id;
	}
	@Id
	private String id;
	
	@Column(name="TRANSACTION_ID")
	private String transactionId;//申请记录ID
	
	@Column(name="APPROVAL_BOOK")
	private String approvalBook;//审批表
	
	@Column(name="APPROVAL_BOOK_NAME")
	private String approvalBookName;//审批表名称

	@Column(name="AUDIT_CONTENT")
	private String auditContent;//审核内容

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AUDIT_DT")
	private Date auditDt;////审核时间

	@Column(name="AUDIT_OPERATOR")
	private String auditOperator;

	@Column(name="AUDIT_OPERATOR_ROLE")
	private BigDecimal auditOperatorRole;

	@Column(name="AUDIT_STATE")
	private BigDecimal auditState;

	@Column(name="AUDIT_VOUCHER")
	private String auditVoucher;//审核凭证

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT",insertable = false, updatable = false)
	private Date createdDt;

	@Column(name="IS_DELETED",insertable = false)
	private String isDeleted;

	private String memo;
	
	@Column(name="STUDENT_ID")
	private String studentId;

	@Column(name="UPDATED_BY",insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT",insertable = false)
	private Date updatedDt;

	@Column(name="VERSION")
	private BigDecimal version;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="TRANSACTION_ID",referencedColumnName = "TRANSACTION_ID", insertable = false, updatable = false)
	private GjtSchoolRollTran gjtSchoolRollTran;

	public GjtSchoolRollTransAudit() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApprovalBook() {
		return this.approvalBook;
	}

	public void setApprovalBook(String approvalBook) {
		this.approvalBook = approvalBook;
	}

	public String getAuditContent() {
		return this.auditContent;
	}

	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}

	public Date getAuditDt() {
		return this.auditDt;
	}

	public void setAuditDt(Date auditDt) {
		this.auditDt = auditDt;
	}

	public String getAuditOperator() {
		return this.auditOperator;
	}

	public void setAuditOperator(String auditOperator) {
		this.auditOperator = auditOperator;
	}

	public BigDecimal getAuditOperatorRole() {
		return this.auditOperatorRole;
	}

	public void setAuditOperatorRole(BigDecimal auditOperatorRole) {
		this.auditOperatorRole = auditOperatorRole;
	}

	public BigDecimal getAuditState() {
		return this.auditState;
	}

	public void setAuditState(BigDecimal auditState) {
		this.auditState = auditState;
	}

	public String getAuditVoucher() {
		return this.auditVoucher;
	}

	public void setAuditVoucher(String auditVoucher) {
		this.auditVoucher = auditVoucher;
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

	public GjtSchoolRollTran getGjtSchoolRollTran() {
		return this.gjtSchoolRollTran;
	}

	public void setGjtSchoolRollTran(GjtSchoolRollTran gjtSchoolRollTran) {
		this.gjtSchoolRollTran = gjtSchoolRollTran;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getApprovalBookName() {
		return approvalBookName;
	}

	public void setApprovalBookName(String approvalBookName) {
		this.approvalBookName = approvalBookName;
	}
	
}