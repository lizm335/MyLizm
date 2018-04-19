package com.ouchgzee.headTeacher.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the PRI_USER_ROLE database table.
 * 
 */
@Entity
@Table(name = "PRI_ROLE_INFO")
// @NamedQuery(name = "PriRoleInfo.findAll", query = "SELECT p FROM PriRoleInfo p")
@Deprecated public class BzrPriRoleInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ROLE_ID")
	private String roleId;

	// @OneToMany(fetch = FetchType.EAGER, mappedBy = "priRoleInfo", cascade =
	// CascadeType.ALL)
	// @MapKey(name = "modelOperateId")
	// Map<String, PriRoleOperate> priRoleOperateMap;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "PRI_ROLE_MODEL", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "MODEL_ID") })
	List<BzrPriModelInfo> priModelInfos;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	private String isdeleted;

	@Column(name = "ROLE_CODE")
	@OrderBy(value = "ROLE_CODE ASC")
	private String roleCode;

	@Column(name = "ROLE_NAME")
	private String roleName;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	public BzrPriRoleInfo() {
	}

	// public void addRoleOperate(PriModelOperate modelOperate) {
	// if (priRoleOperateMap == null) {
	// priRoleOperateMap = new HashMap();
	// }
	// if (!priRoleOperateMap.containsKey(modelOperate.getModelOperateId())) {
	// PriRoleOperate roleOperate = new PriRoleOperate();
	// roleOperate.setRoleOperateId(UUIDUtils.random());
	// roleOperate.setUpdatedDt(new Date());
	// roleOperate.setModelOperateId(modelOperate.getModelOperateId());
	// roleOperate.setPriRoleInfo(this);
	// priRoleOperateMap.put(roleOperate.getRoleOperateId(), roleOperate);
	// }
	// }

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

	public String getRoleCode() {
		return this.roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public List<BzrPriModelInfo> getPriModelInfos() {
		return priModelInfos;
	}

	public void setPriModelInfos(List<BzrPriModelInfo> priModelInfos) {
		this.priModelInfos = priModelInfos;
	}

}