package com.gzedu.xlims.pojo.graduation;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 毕业相关申请（毕业证、学位证等）审核流程记录实体类<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年09月16日
 * @version 3.0
 */
@Entity
@Table(name="GJT_GRA_APPLY_FLOW_RECORD")
@NamedQuery(name="GjtGraApplyFlowRecord.findAll", query="SELECT g FROM GjtGraApplyFlowRecord g")
public class GjtGraApplyFlowRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="FLOW_RECORD_ID")
	private String flowRecordId;

	/**
	 * 申请ID（审核毕业证、学位证等）
	 */
	@Column(name="APPLY_ID")
	private String applyId;

	/**
	 * 审核内容
	 */
	@Column(name="AUDIT_CONTENT")
	private String auditContent;

	/**
	 * 审核时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AUDIT_DT")
	private Date auditDt;

	/**
	 * 审核人
	 */
	@Column(name="AUDIT_OPERATOR")
	private String auditOperator;

	/**
	 * 审核人的角色 1-学员 2-学习中心 3-分部 4-总部
	 */
	@Column(name="AUDIT_OPERATOR_ROLE")
	private Integer auditOperatorRole;

	/**
	 * 审核状态，默认0 0-待审核 1-通过 2-不通过
	 */
	@Column(name="AUDIT_STATE")
	private Integer auditState;

	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private Integer version;

	public GjtGraApplyFlowRecord() {
	}

	public String getFlowRecordId() {
		return this.flowRecordId;
	}

	public void setFlowRecordId(String flowRecordId) {
		this.flowRecordId = flowRecordId;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
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

	public Integer getAuditOperatorRole() {
		return this.auditOperatorRole;
	}

	public void setAuditOperatorRole(Integer auditOperatorRole) {
		this.auditOperatorRole = auditOperatorRole;
	}

	public Integer getAuditState() {
		return this.auditState;
	}

	public void setAuditState(Integer auditState) {
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

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}