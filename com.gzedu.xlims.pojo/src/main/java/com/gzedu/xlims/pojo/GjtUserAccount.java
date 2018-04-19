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
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

/**
 * The persistent class for the GJT_USER_ACCOUNT database table.
 * 
 * 学生登陆平台： 用户表
 */
@Entity
@Table(name = "GJT_USER_ACCOUNT")
@NamedQuery(name = "GjtUserAccount.findAll", query = "SELECT g FROM GjtUserAccount g")
public class GjtUserAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private PriRoleInfo priRoleInfo;// 角色

	@Column(name = "IS_SUPER_MGR", insertable = false) // 是否超级管理员
	private Boolean isSuperMgr;

	// 角色 GrantTypeEnum 超级管理员(0), 院校管理员(1),教学点管理员(2);
	@Column(name = "GRANT_TYPE")
	private String grantType;

	@Column(name = "SJH")
	private String sjh;// 手机号

	@Column(name = "USER_TYPE") // UserTypeEnum 0 管理员 1学生 2 教师 3职工
	private Integer userType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID") // 院校/机构ID
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtOrg gjtOrg;

	@OneToOne(mappedBy = "gjtUserAccount", fetch = FetchType.LAZY) // 学员信息
	private GjtStudentInfo gjtStudentInfo;

	// @ManyToOne
	// @JoinColumn(name = "ORG_ID") // 院校
	// @NotFound(action = NotFoundAction.IGNORE)
	// private GjtSchoolInfo gjtSchoolInfo;

	@Column(name = "ALLOW_BACK_LOGIN", insertable = false)
	private String allowBackLogin;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATE_TIME", updatable = false)
	private Date createTime;

	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "CURRENT_LOGIN_IP") // 当前登录SESSIONID
	private String currentLoginIp;

	@Temporal(TemporalType.TIMESTAMP) // 当前登录时间
	@Column(name = "CURRENT_LOGIN_TIME")
	private Date currentLoginTime;

	@Column(name = "IS_ONLINE")
	private String isOnline;

	@Column(name = "DATA_SCOPE")
	private String dataScope;

	private String eeno;// ee帐号

	private String email;// 电子信箱

	@Temporal(TemporalType.TIMESTAMP) // 最后登陆时间
	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "IS_DELETED", insertable = false) // 是否删除
	@Where(clause = "is_Deleted='N'")
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false) // 是否启用 1启用0停用
	private Boolean isEnabled = true;

	@Column(name = "IS_SYNC")
	private Boolean isSync;

	@Column(name = "LAST_LOGIN_IP") // 最后一次登陆IP
	private String lastLoginIp;

	@Temporal(TemporalType.TIMESTAMP) // 最后一次登陆时间
	@Column(name = "LAST_LOGIN_TIME")
	private Date lastLoginTime;

	@Column(name = "LOGIN_ACCOUNT") // 登录帐号
	private String loginAccount;

	@Column(name = "LOGIN_COUNT", insertable = false) // 总共登录次数
	private BigDecimal loginCount;

	@Column(name = "NICK_NAME")
	private String nickName;

	private String password;// 加密密码

	private String password2;// 显性密码

	@Column(name = "REAL_NAME")
	private String realName;// 真实姓名

	private String remark;

	@Temporal(TemporalType.TIMESTAMP) // 帐号有效开始时间
	@Column(name = "START_DATE")
	private Date startDate;

	private String status;

	@Column(name = "UPDATED_BY", insertable = false) // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;// 版本

	@Column(name = "WX_OPENID")
	private String wxOpenId;// 微信公众号-微信用户标识openId

	@Column(name = "WX_UNIONID")
	private String wxUnionId;// 微信公众号-微信用户标识unionId

	@Column(name = "WX_HEADPORTRAIT")
	private String wxHeadPortrait;// 微信公众号-微信头像

	@Column(name = "WX_NICKNAME")
	private String wxNickName;// 微信公众号-微信昵称

	@Column
	private String telephone;

	@Column(name = "SIGN_PHOTO")
	private String signPhoto;// 签名

	@Transient
	private String salt; // 加密密码的盐

	@Transient
	private int hasPermissionOperation = 1; // 是否有操作权限，也就是是否是学习空间的学员，默认1，1-有权限，0-无权限

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public GjtUserAccount() {
	}

	public GjtUserAccount(String id) {
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

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Boolean getIsSuperMgr() {
		return isSuperMgr;
	}

	public void setIsSuperMgr(Boolean isSuperMgr) {
		this.isSuperMgr = isSuperMgr;
	}

	public Boolean getIsSync() {
		return isSync;
	}

	public void setIsSync(Boolean isSync) {
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

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public BigDecimal getVersion() {
		return version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public String getCredentialsSalt() {
		return loginAccount + salt;
	}

	public PriRoleInfo getPriRoleInfo() {
		return priRoleInfo;
	}

	public void setPriRoleInfo(PriRoleInfo priRoleInfo) {
		this.priRoleInfo = priRoleInfo;
	}

	// public GjtSchoolInfo getGjtSchoolInfo() {
	// return gjtSchoolInfo;
	// }
	//
	// public void setGjtSchoolInfo(GjtSchoolInfo gjtSchoolInfo) {
	// this.gjtSchoolInfo = gjtSchoolInfo;
	// }

	public GjtOrg getGjtOrg() {
		return gjtOrg;
	}

	public String getSjh() {
		return sjh;
	}

	public void setSjh(String sjh) {
		this.sjh = sjh;
	}

	public void setGjtOrg(GjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public String getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getSignPhoto() {
		return signPhoto;
	}

	public void setSignPhoto(String signPhoto) {
		this.signPhoto = signPhoto;
	}

	public String getWxHeadPortrait() {
		return wxHeadPortrait;
	}

	public void setWxHeadPortrait(String wxHeadPortrait) {
		this.wxHeadPortrait = wxHeadPortrait;
	}

	public String getWxNickName() {
		return wxNickName;
	}

	public void setWxNickName(String wxNickName) {
		this.wxNickName = wxNickName;
	}

	public String getWxOpenId() {
		return wxOpenId;
	}

	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}

	public String getWxUnionId() {
		return wxUnionId;
	}

	public void setWxUnionId(String wxUnionId) {
		this.wxUnionId = wxUnionId;
	}

	public Boolean getSuperMgr() {
		return isSuperMgr;
	}

	public void setSuperMgr(Boolean superMgr) {
		isSuperMgr = superMgr;
	}

	public Boolean getEnabled() {
		return isEnabled;
	}

	public void setEnabled(Boolean enabled) {
		isEnabled = enabled;
	}

	public Boolean getSync() {
		return isSync;
	}

	public int isHasPermissionOperation() {
		return hasPermissionOperation;
	}

	public int getHasPermissionOperation() {
		return hasPermissionOperation;
	}

	public void setHasPermissionOperation(int hasPermissionOperation) {
		this.hasPermissionOperation = hasPermissionOperation;
	}

	@Override
	public String toString() {
		return "GjtUserAccount [id=" + id + ", priRoleInfo=" + priRoleInfo + ", isSuperMgr=" + isSuperMgr
				+ ", grantType=" + grantType + ", sjh=" + sjh + ", userType=" + userType + ", gjtOrg=" + gjtOrg
				+ ", gjtStudentInfo=" + gjtStudentInfo + ", allowBackLogin=" + allowBackLogin + ", createTime="
				+ createTime + ", createdBy=" + createdBy + ", createdDt=" + createdDt + ", currentLoginIp="
				+ currentLoginIp + ", currentLoginTime=" + currentLoginTime + ", isOnline=" + isOnline + ", dataScope="
				+ dataScope + ", eeno=" + eeno + ", email=" + email + ", endDate=" + endDate + ", isDeleted="
				+ isDeleted + ", isEnabled=" + isEnabled + ", isSync=" + isSync + ", lastLoginIp=" + lastLoginIp
				+ ", lastLoginTime=" + lastLoginTime + ", loginAccount=" + loginAccount + ", loginCount=" + loginCount
				+ ", nickName=" + nickName + ", password=" + password + ", password2=" + password2 + ", realName="
				+ realName + ", remark=" + remark + ", startDate=" + startDate + ", status=" + status + ", updatedBy="
				+ updatedBy + ", updatedDt=" + updatedDt + ", version=" + version + ", salt=" + salt + ", telephone="
				+ telephone + "]";
	}

}