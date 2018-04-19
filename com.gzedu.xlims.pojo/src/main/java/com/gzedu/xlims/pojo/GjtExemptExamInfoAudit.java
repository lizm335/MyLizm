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
 * The persistent class for the GJT_EXEMPT_EXAM_INFO_AUDIT database table.
 * 
 */
@Entity
@Table(name="GJT_EXEMPT_EXAM_INFO_AUDIT")
@NamedQuery(name="GjtExemptExamInfoAudit.findAll", query="SELECT g FROM GjtExemptExamInfoAudit g")
public class GjtExemptExamInfoAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="AUDIT_CONTENT")
	private String auditContent;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AUDIT_DT")
	private Date auditDt;

	@Column(name="AUDIT_OPERATOR")
	private String auditOperator;

	@Column(name="AUDIT_OPERATOR_ROLE")
	private String auditOperatorRole;

	@Column(name="AUDIT_STATE")
	private String auditState;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT",insertable = false, updatable = false)
	private Date createdDt;

	@Column(name="EXEMPT_EXAM_ID")
	private String exemptExamId;

	@Column(name="IS_DELETED",insertable = false, updatable = false)
	private String isDeleted;

	private String memo;

	@Column(name="STUDENT_ID")
	private String studentId;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name="VERSION",insertable = false, updatable = false)
	private BigDecimal version;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="EXEMPT_EXAM_ID",referencedColumnName = "EXEMPT_EXAM_ID", insertable = false, updatable = false)
	private GjtExemptExamInfo gjtExemptExamInfo;
	
	
	public GjtExemptExamInfoAudit() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getAuditOperatorRole() {
		return this.auditOperatorRole;
	}

	public void setAuditOperatorRole(String auditOperatorRole) {
		this.auditOperatorRole = auditOperatorRole;
	}

	public String getAuditState() {
		return this.auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
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

	public String getExemptExamId() {
		return this.exemptExamId;
	}

	public void setExemptExamId(String exemptExamId) {
		this.exemptExamId = exemptExamId;
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

	public GjtExemptExamInfo getGjtExemptExamInfo() {
		return gjtExemptExamInfo;
	}

	public void setGjtExemptExamInfo(GjtExemptExamInfo gjtExemptExamInfo) {
		this.gjtExemptExamInfo = gjtExemptExamInfo;
	}
}