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
 * The persistent class for the GJT_STUDY_CENTER_AUDIT database table. 学习中心审核记录表
 */
@Entity
@Table(name = "GJT_STUDY_CENTER_AUDIT")
@NamedQuery(name = "GjtStudyCenterAudit.findAll", query = "SELECT g FROM GjtStudyCenterAudit g")
public class GjtStudyCenterAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "INCIDENT_ID") // 审核事件Id
	private String incidentId;

	@Column(name = "AUDIT_CONTENT") // 审核内容
	private String auditContent;

	@Column(name = "AUDIT_STEP") // 审核进度，招生平台1，学支管理员2，院长3
	private String auditStep;

	@Temporal(TemporalType.TIMESTAMP) // 审核时间
	@Column(name = "AUDIT_DT")
	private Date auditDt;

	@Column(name = "AUDIT_OPERATOR") // 审核人
	private String auditOperator;

	@Column(name = "AUDIT_OPERATOR_ROLE") // 审核人角色
	private String auditOperatorRole;

	@Column(name = "AUDIT_STATE") // 审核状态，默认0 0-待审核 1-通过 2-不通过
	private String auditState;

	@Column(name = "CREATED_BY") // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "IS_DELETED") // 是否删除
	private String isDeleted;

	@Column(name = "UPDATED_BY") // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	private BigDecimal version;// 版本号，乐观锁

	public GjtStudyCenterAudit() {
	}

	public String getAuditStep() {
		return auditStep;
	}

	public void setAuditStep(String auditStep) {
		this.auditStep = auditStep;
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
		return auditOperatorRole;
	}

	public void setAuditOperatorRole(String auditOperatorRole) {
		this.auditOperatorRole = auditOperatorRole;
	}

	public String getAuditState() {
		return auditState;
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

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIncidentId() {
		return this.incidentId;
	}

	public void setIncidentId(String incidentId) {
		this.incidentId = incidentId;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
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