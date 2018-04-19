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
 * The persistent class for the TBL_PRI_MANAGER database table.
 * 
 */
@Entity
@Table(name = "TBL_PRI_MANAGER")
@NamedQuery(name = "TblPriManager.findAll", query = "SELECT t FROM TblPriManager t")
public class TblPriManager implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MGR_ID")
	private String mgrId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "DATA_SCOPE")
	private String dataScope;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "GRANT_TYPE")
	private String grantType;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "IS_SUPER")
	private BigDecimal isSuper;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_LOGIN_DATE")
	private Date lastLoginDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOGIN_DATE")
	private Date loginDate;

	@Column(name = "LOGIN_TIMES")
	private BigDecimal loginTimes;

	@Column(name = "MGR_NAME")
	private String mgrName;

	@Column(name = "MGR_TYPE")
	private BigDecimal mgrType;

	@Column(name = "ORG_CODE")
	private String orgCode;

	@Column(name = "ORG_ID")
	private String orgId;

	private String password;

	private String password2;

	private String remark;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	private Date startDate;

	private String status;

	@Column(name = "\"STOP\"")
	private String stop;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "USER_CODE")
	private String userCode;

	@Column(name = "VERSION")
	private BigDecimal version;

	public TblPriManager() {
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

	public String getDataScope() {
		return this.dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getGrantType() {
		return this.grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public BigDecimal getIsSuper() {
		return this.isSuper;
	}

	public void setIsSuper(BigDecimal isSuper) {
		this.isSuper = isSuper;
	}

	public Date getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Date getLoginDate() {
		return this.loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public BigDecimal getLoginTimes() {
		return this.loginTimes;
	}

	public void setLoginTimes(BigDecimal loginTimes) {
		this.loginTimes = loginTimes;
	}

	public String getMgrId() {
		return this.mgrId;
	}

	public void setMgrId(String mgrId) {
		this.mgrId = mgrId;
	}

	public String getMgrName() {
		return this.mgrName;
	}

	public void setMgrName(String mgrName) {
		this.mgrName = mgrName;
	}

	public BigDecimal getMgrType() {
		return this.mgrType;
	}

	public void setMgrType(BigDecimal mgrType) {
		this.mgrType = mgrType;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return this.password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStop() {
		return this.stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
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

	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

}