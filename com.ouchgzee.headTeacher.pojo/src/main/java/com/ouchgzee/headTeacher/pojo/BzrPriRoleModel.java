package com.ouchgzee.headTeacher.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the PRI_ROLE_MODEL database table.
 * 
 */
@Entity
@Table(name = "PRI_ROLE_MODEL")
// @NamedQuery(name = "PriRoleModel.findAll", query = "SELECT p FROM PriRoleModel p")
@Deprecated public class BzrPriRoleModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ROLE_MODEL_ID")
	private String roleModelId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	private String isdeleted;

	@Column(name = "MODEL_ID")
	private String modelId;

	@Column(name = "ROLE_ID")
	private String roleId;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	public BzrPriRoleModel() {
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

	public String getIsdeleted() {
		return this.isdeleted;
	}

	public void setIsdeleted(String isdeleted) {
		this.isdeleted = isdeleted;
	}

	public String getRoleModelId() {
		return this.roleModelId;
	}

	public void setRoleModelId(String roleModelId) {
		this.roleModelId = roleModelId;
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

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}