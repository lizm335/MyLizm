package com.ouchgzee.headTeacher.pojo.exam;

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

import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;

@Entity
@Table(name="GJT_EXAM_BATCH_APPROVAL")
// @NamedQuery(name = "GjtExamBatchApproval.findAll",query="SELECT g FROM GjtExamBatchApproval g")
@Deprecated public class BzrGjtExamBatchApproval implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="APPROVAL_ID")
	private String approvalId;
	
	@Column(name="USER_ID")
	private String userId;

	@ManyToOne
	@JoinColumn(name="USER_ID", insertable = false, updatable = false)
	private BzrGjtUserAccount createdUser;
	
	@Column(name="EXAM_BATCH_ID")
	private String examBatchId;
	
	@Column(name="AUDIT_STATE")
	private String auditState;
	
	@Column(name="AUDIT_OPERATOR")
	private String auditOperator;
	
	@Column(name="AUDIT_OPERATOR_ROLE")
	private String auditOperatorRole;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AUDIT_DT")
	private Date auditDt;
	
	@Column(name="AUDIT_CONTENT")
	private String auditContent;
	
	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;
	
	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;
	
	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;
	
	@Column(name = "XX_ID")
	private String xxId;
	
	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;
	
	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@ManyToOne
	@JoinColumn(name="EXAM_BATCH_ID", insertable = false, updatable = false)
	private BzrGjtExamBatchNew gjtExamBatchNew;
	
	public BzrGjtExamBatchApproval(){
		
	}


	public String getApprovalId() {
		return approvalId;
	}


	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public BzrGjtUserAccount getCreatedUser() {
		return createdUser;
	}


	public void setCreatedUser(BzrGjtUserAccount createdUser) {
		this.createdUser = createdUser;
	}


	public String getExamBatchId() {
		return examBatchId;
	}


	public void setExamBatchId(String examBatchId) {
		this.examBatchId = examBatchId;
	}


	public String getAuditState() {
		return auditState;
	}


	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}


	public String getAuditOperator() {
		return auditOperator;
	}


	public void setAuditOperator(String auditOperator) {
		this.auditOperator = auditOperator;
	}


	public String getAuditOperatorRole() {
		return auditOperatorRole;
	}


	public void setAuditOperatorRole(String auditOperatorRole) {
		this.auditOperatorRole = auditOperatorRole;
	}


	public Date getAuditDt() {
		return auditDt;
	}


	public void setAuditDt(Date auditDt) {
		this.auditDt = auditDt;
	}


	public String getAuditContent() {
		return auditContent;
	}


	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}


	public Date getCreatedDt() {
		return createdDt;
	}


	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public Date getUpdatedDt() {
		return updatedDt;
	}


	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}


	public String getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	public String getXxId() {
		return xxId;
	}


	public void setXxId(String xxId) {
		this.xxId = xxId;
	}


	public String getIsDeleted() {
		return isDeleted;
	}


	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}


	public BigDecimal getVersion() {
		return version;
	}


	public void setVersion(BigDecimal version) {
		this.version = version;
	}


	public BzrGjtExamBatchNew getGjtExamBatchNew() {
		return gjtExamBatchNew;
	}


	public void setGjtExamBatchNew(BzrGjtExamBatchNew gjtExamBatchNew) {
		this.gjtExamBatchNew = gjtExamBatchNew;
	}
	

}
