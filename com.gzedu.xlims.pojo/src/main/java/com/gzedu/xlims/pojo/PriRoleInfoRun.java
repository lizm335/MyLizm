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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the GJT_WORK_ORDER_USER database table. 角色管理下属角色
 */
@Entity
@Table(name = "PRI_ROLE_INFO_RUN")
@NamedQuery(name = "PriRoleInfoRun.findAll", query = "SELECT g FROM PriRoleInfoRun g")
public class PriRoleInfoRun implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "ROLE_ID", insertable = false, updatable = false)
	private String roleId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private PriRoleInfo parentPriRoleInfo;

	@Column(name = "SUBSET_ROLE_ID", insertable = false, updatable = false)
	private String subsetRoleId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBSET_ROLE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private PriRoleInfo priRoleInfo;

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

	public PriRoleInfoRun() {
	}

	public PriRoleInfo getParentPriRoleInfo() {
		return parentPriRoleInfo;
	}

	public void setParentPriRoleInfo(PriRoleInfo parentPriRoleInfo) {
		this.parentPriRoleInfo = parentPriRoleInfo;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public PriRoleInfo getPriRoleInfo() {
		return priRoleInfo;
	}

	public void setPriRoleInfo(PriRoleInfo priRoleInfo) {
		this.priRoleInfo = priRoleInfo;
	}

	public String getSubsetRoleId() {
		return subsetRoleId;
	}

	public void setSubsetRoleId(String subsetRoleId) {
		this.subsetRoleId = subsetRoleId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

}