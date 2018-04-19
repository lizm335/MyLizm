package com.gzedu.xlims.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gzedu.xlims.pojo.comm.AuditStatus;
import com.gzedu.xlims.pojo.comm.OperateType;

@Entity
@Table(name="GJT_AUDIT_OPERATE_LINE")
public class GjtAuditOperateLine {
	@Id
	@Column(name="AUDIT_OPERATE_LINE_ID")
	private String auditOperateLineId;
	// 操作 
	@Column(name="OPERATE")
	private String operate;
	// 操作类型
	@Column(name="OPERATE_TYPE")
	@Enumerated
	private OperateType operateType;
	// 操作发生时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="OPERATE_TIME")
	private Date operateTime;
	// 操作人
	@Column(name="OPERATOR")
	private String operator;
	//操作人真实姓名
	@Column(name="OPERATOR_NAME")
	private String operatorName;
	// 操作人角色
	@Column(name="OPERATOR_ROLE")
	private String operatorRole;
	// 审核内容
	@Column(name="AUDIT_CONTENT")
	private String auditContent;
	// 审核状态，默认0 0-待审核 1-通过 2-不通过
	@Column(name="AUDIT_STATUS")
	@Enumerated
	private AuditStatus auditStatus;
	// 操作来源ID
	@Column(name="SOURCE_ID")
	private String sourceId;
	public String getAuditOperateLineId() {
		return auditOperateLineId;
	}
	public void setAuditOperateLineId(String auditOperateLineId) {
		this.auditOperateLineId = auditOperateLineId;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public OperateType getOperateType() {
		return operateType;
	}
	public void setOperateType(OperateType operateType) {
		this.operateType = operateType;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getOperatorRole() {
		return operatorRole;
	}
	public void setOperatorRole(String operatorRole) {
		this.operatorRole = operatorRole;
	}
	public String getAuditContent() {
		return auditContent;
	}
	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}
	public AuditStatus getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(AuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

}
