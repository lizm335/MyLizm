package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * The persistent class for the PRI_USER_ROLE database table.
 * 
 */
@Entity
@Table(name = "PRI_ROLE_INFO")
@NamedQuery(name = "PriRoleInfo.findAll", query = "SELECT p FROM PriRoleInfo p")
public class PriRoleInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ROLE_ID")
	private String roleId;

	// @OneToMany(fetch = FetchType.EAGER, mappedBy = "priRoleInfo", cascade =
	// CascadeType.ALL)
	// @MapKey(name = "modelOperateId")
	// Map<String, PriRoleOperate> priRoleOperateMap;

	@ManyToMany
	@JoinTable(name = "PRI_ROLE_MODEL", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "MODEL_ID") })
	List<PriModelInfo> priModelInfos;

	/**
	 * 管理下属角色
	 */
	@OneToMany(mappedBy = "parentPriRoleInfo", fetch = FetchType.LAZY)
	private List<PriRoleInfoRun> priRoleInfoList;

	@OneToMany(mappedBy = "parentPriRoleInfo", fetch = FetchType.LAZY)
	private List<PriRoleInfoWork> workRoleInfoList;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	private String isdeleted;

	@Column(name = "ROLE_CODE")
	// @OrderBy(value = "ROLE_CODE ASC")
	private String roleCode;

	@Column(name = "ROLE_NAME")
	private String roleName;

	/**
	 * 所属平台 查看字典PLATFORM_TYPE
	 */
	@Column(name = "PLATFORM_TYPE")
	private String platformType;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Transient
	private Boolean isMng;

	@Transient
	private Boolean isCheck;// 是否被该角色管理

	@Transient
	private Boolean isWorkCheck;// 是否被该角色发布工单

	public PriRoleInfo() {
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

	/**
	 * @param roleId
	 */
	public PriRoleInfo(String roleId) {
		super();
		this.roleId = roleId;
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

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
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

	public List<PriModelInfo> getPriModelInfos() {
		return priModelInfos;
	}

	public void setPriModelInfos(List<PriModelInfo> priModelInfos) {
		this.priModelInfos = priModelInfos;
	}

	public Boolean getIsMng() {
		return isMng;
	}

	public void setIsMng(Boolean isMng) {
		this.isMng = isMng;
	}

	public Boolean getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Boolean isCheck) {
		this.isCheck = isCheck;
	}

	public Boolean getIsWorkCheck() {
		return isWorkCheck;
	}

	public void setIsWorkCheck(Boolean isWorkCheck) {
		this.isWorkCheck = isWorkCheck;
	}

	public List<PriRoleInfoRun> getPriRoleInfoList() {
		return priRoleInfoList;
	}

	public void setPriRoleInfoList(List<PriRoleInfoRun> priRoleInfoList) {
		this.priRoleInfoList = priRoleInfoList;
	}

	public List<PriRoleInfoWork> getWorkRoleInfoList() {
		return workRoleInfoList;
	}

	public void setWorkRoleInfoList(List<PriRoleInfoWork> workRoleInfoList) {
		this.workRoleInfoList = workRoleInfoList;
	}
}