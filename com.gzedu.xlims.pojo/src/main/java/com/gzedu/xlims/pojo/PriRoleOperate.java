package com.gzedu.xlims.pojo;

import java.io.Serializable;
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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the PRI_ROLE_OPERATE database table.
 * 
 */
@Entity
@Table(name = "PRI_ROLE_OPERATE")
@NamedQuery(name = "PriRoleOperate.findAll", query = "SELECT p FROM PriRoleOperate p")
public class PriRoleOperate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ROLE_OPERATE_ID")
	private String roleOperateId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	private String isdeleted;

	@Column(name = "MODEL_ID")
	private String modelId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODEL_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private PriModelInfo priModelInfo;

	@Column(name = "MODEL_OPERATE_ID")
	private String modelOperateId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private PriRoleInfo priRoleInfo;

	@Column(name = "ROLE_ID")
	private String roleId;

	@Column(name = "OPERATE_ID")
	private String operateId;

	@ManyToOne
	@JoinColumn(name = "OPERATE_ID", insertable = false, updatable = false)
	private PriOperateInfo priOperateInfo;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	public PriRoleOperate() {
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

	public String getModelId() {
		return this.modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public PriModelInfo getPriModelInfo() {
		return priModelInfo;
	}

	public void setPriModelInfo(PriModelInfo priModelInfo) {
		this.priModelInfo = priModelInfo;
	}

	public String getModelOperateId() {
		return this.modelOperateId;
	}

	public void setModelOperateId(String modelOperateId) {
		this.modelOperateId = modelOperateId;
	}

	public String getOperateId() {
		return this.operateId;
	}

	public void setOperateId(String operateId) {
		this.operateId = operateId;
	}

	public PriOperateInfo getPriOperateInfo() {
		return priOperateInfo;
	}

	public void setPriOperateInfo(PriOperateInfo priOperateInfo) {
		this.priOperateInfo = priOperateInfo;
	}

	public PriRoleInfo getPriRoleInfo() {
		return priRoleInfo;
	}

	public void setPriRoleInfo(PriRoleInfo priRoleInfo) {
		this.priRoleInfo = priRoleInfo;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleOperateId() {
		return this.roleOperateId;
	}

	public void setRoleOperateId(String roleOperateId) {
		this.roleOperateId = roleOperateId;
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