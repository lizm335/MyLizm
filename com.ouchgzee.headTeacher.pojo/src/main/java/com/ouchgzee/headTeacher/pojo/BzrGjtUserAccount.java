package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 用户登录账号信息实体类<br>
 * The persistent class for the GJT_USER_ACCOUNT database table.
 * 
 */
@Entity
@Table(name = "GJT_USER_ACCOUNT")
// @NamedQuery(name = "GjtUserAccount.findAll", query = "SELECT g FROM
// GjtUserAccount g")
@Deprecated
public class BzrGjtUserAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@OneToOne(mappedBy = "gjtUserAccount", fetch = FetchType.LAZY) // 学员信息
	private BzrGjtStudentInfo gjtStudentInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrPriRoleInfo priRoleInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID") // 院校/机构ID
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtOrg gjtOrg;

	@Column(name = "ALLOW_BACK_LOGIN")
	private String allowBackLogin;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "CURRENT_LOGIN_IP")
	private String currentLoginIp;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CURRENT_LOGIN_TIME")
	private Date currentLoginTime;

	@Column(name = "DATA_SCOPE")
	private String dataScope;

	private String eeno;

	private String email;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "GRANT_TYPE")
	private String grantType;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	@Column(name = "IS_SUPER_MGR")
	private BigDecimal isSuperMgr;

	@Column(name = "IS_SYNC")
	private String isSync;

	@Column(name = "LAST_LOGIN_IP")
	private String lastLoginIp;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_LOGIN_TIME")
	private Date lastLoginTime;

	@Column(name = "LOGIN_ACCOUNT")
	private String loginAccount;

	@Column(name = "LOGIN_COUNT")
	private BigDecimal loginCount;

	@Column(name = "NICK_NAME")
	private String nickName;

	@Column(name = "ORG_CODE")
	private String orgCode;

	private String password;

	private String password2;

	@Column(name = "REAL_NAME")
	private String realName;

	private String remark;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE")
	private Date startDate;

	private String status;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "USER_TYPE")
	private BigDecimal userType;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@Column(name = "IS_ONLINE") // 用户是否在线
	private String isOnline;

	@Column(name = "SJH")
	private String sjh;// 手机号

	/**
	 * 临时数据：密码加密的盐
	 */
	@Transient
	private String salt;

	public BzrGjtUserAccount() {
	}

	public String getSjh() {
		return sjh;
	}

	public void setSjh(String sjh) {
		this.sjh = sjh;
	}

	public BzrGjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(BzrGjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public String getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	public BzrGjtUserAccount(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAllowBackLogin() {
		return this.allowBackLogin;
	}

	public void setAllowBackLogin(String allowBackLogin) {
		this.allowBackLogin = allowBackLogin;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getCurrentLoginIp() {
		return this.currentLoginIp;
	}

	public void setCurrentLoginIp(String currentLoginIp) {
		this.currentLoginIp = currentLoginIp;
	}

	public Date getCurrentLoginTime() {
		return this.currentLoginTime;
	}

	public void setCurrentLoginTime(Date currentLoginTime) {
		this.currentLoginTime = currentLoginTime;
	}

	public String getDataScope() {
		return this.dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	public String getEeno() {
		return this.eeno;
	}

	public void setEeno(String eeno) {
		this.eeno = eeno;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public BigDecimal getIsSuperMgr() {
		return this.isSuperMgr;
	}

	public void setIsSuperMgr(BigDecimal isSuperMgr) {
		this.isSuperMgr = isSuperMgr;
	}

	public String getIsSync() {
		return this.isSync;
	}

	public void setIsSync(String isSync) {
		this.isSync = isSync;
	}

	public String getLastLoginIp() {
		return this.lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLoginAccount() {
		return this.loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public BigDecimal getLoginCount() {
		return this.loginCount;
	}

	public void setLoginCount(BigDecimal loginCount) {
		this.loginCount = loginCount;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
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

	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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

	public BigDecimal getUserType() {
		return this.userType;
	}

	public void setUserType(BigDecimal userType) {
		this.userType = userType;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	/**
	 * get 临时数据：密码加密的盐
	 * 
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * set 临时数据：密码加密的盐
	 * 
	 * @param salt
	 *            the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * 加盐后的结果
	 * 
	 * @return
	 */
	public String getCredentialsSalt() {
		return loginAccount + salt;
	}

	public BzrPriRoleInfo getPriRoleInfo() {
		return priRoleInfo;
	}

	public void setPriRoleInfo(BzrPriRoleInfo priRoleInfo) {
		this.priRoleInfo = priRoleInfo;
	}

	public BzrGjtOrg getGjtOrg() {
		return gjtOrg;
	}

	public void setGjtOrg(BzrGjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}
}