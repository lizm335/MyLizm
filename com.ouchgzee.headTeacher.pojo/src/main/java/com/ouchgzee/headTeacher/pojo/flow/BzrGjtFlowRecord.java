package com.ouchgzee.headTeacher.pojo.flow;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the GJT_FLOW_RECORD database table.
 * 
 */
@Entity
@Table(name="GJT_FLOW_RECORD")
// @NamedQuery(name="GjtFlowRecord.findAll", query="SELECT g FROM GjtFlowRecord g")
@Deprecated public class BzrGjtFlowRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="FLOW_RECORD_ID")
	private String flowRecordId;

	@Column(name="AUDIT_CONTENT")
	private String auditContent;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AUDIT_DT")
	private Date auditDt;

	@Column(name="AUDIT_OPERATOR")
	private String auditOperator;

	@Column(name="AUDIT_OPERATOR_ROLE")
	private BigDecimal auditOperatorRole;

	@Column(name="AUDIT_STATE")
	private BigDecimal auditState;

	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name="STUDENT_ID")
	private String studentId;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	public BzrGjtFlowRecord() {
	}

	public String getFlowRecordId() {
		return this.flowRecordId;
	}

	public void setFlowRecordId(String flowRecordId) {
		this.flowRecordId = flowRecordId;
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

}