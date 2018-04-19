package com.gzedu.xlims.pojo.thesis;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the GJT_THESIS_DEFENCE_PLAN database table.
 * 
 */
@Entity
@Table(name="GJT_THESIS_DEFENCE_PLAN")
@NamedQuery(name="GjtThesisDefencePlan.findAll", query="SELECT g FROM GjtThesisDefencePlan g")
public class GjtThesisDefencePlan implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PLAN_ID")
	private String planId;

	@Column(name="APPLY_ID")
	private String applyId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="DEFENCE_ADDRESS")
	private String defenceAddress;

	@Column(name="DEFENCE_TIME")
	private String defenceTime;

	@Column(name="DEFENCE_TYPE")
	private int defenceType;

	@Column(name="DELETED_BY")
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED_DT")
	private Date deletedDt;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="THESIS_PLAN_ID")
	private String thesisPlanId;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	public GjtThesisDefencePlan() {
	}

	public String getPlanId() {
		return this.planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getApplyId() {
		return this.applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
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

	public String getDefenceAddress() {
		return this.defenceAddress;
	}

	public void setDefenceAddress(String defenceAddress) {
		this.defenceAddress = defenceAddress;
	}

	public String getDefenceTime() {
		return this.defenceTime;
	}

	public void setDefenceTime(String defenceTime) {
		this.defenceTime = defenceTime;
	}

	public int getDefenceType() {
		return this.defenceType;
	}

	public void setDefenceType(int defenceType) {
		this.defenceType = defenceType;
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

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getThesisPlanId() {
		return this.thesisPlanId;
	}

	public void setThesisPlanId(String thesisPlanId) {
		this.thesisPlanId = thesisPlanId;
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

}