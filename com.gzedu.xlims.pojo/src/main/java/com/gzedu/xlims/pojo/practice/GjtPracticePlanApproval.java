package com.gzedu.xlims.pojo.practice;

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

import com.gzedu.xlims.pojo.GjtUserAccount;


/**
 * The persistent class for the GJT_PRACTICE_PLAN_APPROVAL database table.
 * 
 */
@Entity
@Table(name="GJT_PRACTICE_PLAN_APPROVAL")
@NamedQuery(name="GjtPracticePlanApproval.findAll", query="SELECT g FROM GjtPracticePlanApproval g")
public class GjtPracticePlanApproval implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="APPROVAL_ID")
	private String approvalId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@ManyToOne
	@JoinColumn(name="CREATED_BY", insertable = false, updatable = false)
	private GjtUserAccount createdUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="DELETED_BY")
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED_DT")
	private Date deletedDt;

	private String description;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="OPERA_ROLE")
	private int operaRole;

	@Column(name="OPERA_TYPE")
	private int operaType;

	@Column(name="PRACTICE_PLAN_ID")
	private String practicePlanId;

	//bi-directional many-to-one association to GjtPracticePlan
	@ManyToOne
	@JoinColumn(name="PRACTICE_PLAN_ID", insertable = false, updatable = false)
	private GjtPracticePlan gjtPracticePlan;

	public GjtPracticePlanApproval() {
	}

	public String getApprovalId() {
		return this.approvalId;
	}

	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
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

	public String getDeletedBy() {
		return this.deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedDt() {
		return this.deletedDt;
	}

	public void setDeletedDt(Date deletedDt) {
		this.deletedDt = deletedDt;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getOperaRole() {
		return this.operaRole;
	}

	public void setOperaRole(int operaRole) {
		this.operaRole = operaRole;
	}

	public int getOperaType() {
		return this.operaType;
	}

	public void setOperaType(int operaType) {
		this.operaType = operaType;
	}

	public GjtPracticePlan getGjtPracticePlan() {
		return this.gjtPracticePlan;
	}

	public void setGjtPracticePlan(GjtPracticePlan gjtPracticePlan) {
		this.gjtPracticePlan = gjtPracticePlan;
	}

	public GjtUserAccount getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(GjtUserAccount createdUser) {
		this.createdUser = createdUser;
	}

	public String getPracticePlanId() {
		return practicePlanId;
	}

	public void setPracticePlanId(String practicePlanId) {
		this.practicePlanId = practicePlanId;
	}

	@Override
	public String toString() {
		return "GjtPracticePlanApproval [approvalId=" + approvalId + ", createdBy=" + createdBy + ", createdUser="
				+ createdUser + ", createdDt=" + createdDt + ", deletedBy=" + deletedBy + ", deletedDt=" + deletedDt
				+ ", description=" + description + ", isDeleted=" + isDeleted + ", operaRole=" + operaRole
				+ ", operaType=" + operaType + ", practicePlanId=" + practicePlanId + "]";
	}

}