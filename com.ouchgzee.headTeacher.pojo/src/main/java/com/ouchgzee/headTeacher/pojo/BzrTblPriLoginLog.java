package com.ouchgzee.headTeacher.pojo;

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
 * 用户登录日志实体类<br>
 * The persistent class for the TBL_PRI_LOGIN_LOG database table.
 * 
 */
@Entity
@Table(name = "TBL_PRI_LOGIN_LOG")
// @NamedQuery(name = "TblPriLoginLog.findAll", query = "SELECT t FROM TblPriLoginLog t")
@Deprecated public class BzrTblPriLoginLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "LOGIN_ID")
	private String loginId;

	private String browser;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "LOGIN_IP")
	private String loginIp;

	@Column(name = "LOGIN_SESSION")
	private String loginSession;

	@Column(name = "LOGIN_TIME")
	private BigDecimal loginTime;

	@Column(name = "LOGIN_TYPE")
	private String loginType;

	private String os;

	private String remark;

	private String screen;

	private String status;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	private String username;

	private BigDecimal version;

	@Column(name = "ROLE_ID")
	private String roleId;

	public BzrTblPriLoginLog() {
	}

	public String getLoginId() {
		return this.loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getBrowser() {
		return this.browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
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

	public String getLoginIp() {
		return this.loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getLoginSession() {
		return this.loginSession;
	}

	public void setLoginSession(String loginSession) {
		this.loginSession = loginSession;
	}

	public BigDecimal getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(BigDecimal loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginType() {
		return this.loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getOs() {
		return this.os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getScreen() {
		return this.screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}