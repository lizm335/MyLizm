package com.ouchgzee.headTeacher.pojo;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 教职工信息实体类<br>
 * The persistent class for the GJT_EMPLOYEE_INFO database table.
 * 
 */
@Entity
@Table(name = "GJT_EMPLOYEE_INFO")
// @NamedQuery(name = "GjtEmployeeInfo.findAll", query = "SELECT g FROM GjtEmployeeInfo g")
@Deprecated public class BzrGjtEmployeeInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EMPLOYEE_ID")
	private String employeeId;

	@OneToOne
	@JoinColumn(name = "ACCOUNT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtUserAccount gjtUserAccount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID") // 院校/机构ID
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtOrg gjtOrg;

	private String brcfm;

	private String bzlbm;

	private String cjgzny;

	private String cjny;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", updatable = false)
	private Date createdDt;

	private String csdm;

	private String csrq;

	private String cym;

	private String dabh;

	private String dawb;

	private String dzxx;

	private String eeno;

	@Column(name = "EMPLOYEE_TYPE")
	private String employeeType;

	private String gatqm;

	private String hkszd;

	private String hkxzm;

	private String hyzkm;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	private String jgm;

	private String jkzkm;

	private String jtcsm;

	private String jtzz;

	private String jzglbm;

	private String ksh;

	private String lxdh;

	private String lxny;

	@Column(name = "MANAGER_ROLE")
	private String managerRole;

	private String mzm;

	@Column(name = "ORG_CODE")
	private String orgCode;

	private String rkkc;

	private String rkzkm;

	private String sfzh;

	private String sjh;

	@Column(name = "SYNC_STATUS")
	private String syncStatus;

	private String tc;

	private String txdz;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION")
	private BigDecimal version;

	private String whcdm;

	@Column(name = "WORK_ADDR")
	private String workAddr;

	@Column(name = "WORK_TIME")
	private String workTime;

	private String xbm;

	private String xm;

	private String xmpy;

	@Column(name = "XX_ID")
	private String xxId;

	private String xxm;

	@Column(name = "XXZX_ID")
	private String xxzxId;

	private String xzz;

	private String yxsh;

	private String yzbm;

	private String zgh;

	private String zjxy;

	private String zp;

	private String zydz;

	private String zyh;

	private String qq; // QQ号码

	@Column(name = "INDIVIDUALITY_SIGN")
	private String individualitySign; // 个性签名


	/**
	 * 临时数据：管理的班级列表
	 */
	@Transient
	private List<BzrGjtClassInfo> manageClassList;

	public BzrGjtEmployeeInfo() {
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public BzrGjtUserAccount getGjtUserAccount() {
		return gjtUserAccount;
	}

	public void setGjtUserAccount(BzrGjtUserAccount gjtUserAccount) {
		this.gjtUserAccount = gjtUserAccount;
	}

	public String getBrcfm() {
		return this.brcfm;
	}

	public void setBrcfm(String brcfm) {
		this.brcfm = brcfm;
	}

	public String getBzlbm() {
		return this.bzlbm;
	}

	public void setBzlbm(String bzlbm) {
		this.bzlbm = bzlbm;
	}

	public String getCjgzny() {
		return this.cjgzny;
	}

	public void setCjgzny(String cjgzny) {
		this.cjgzny = cjgzny;
	}

	public String getCjny() {
		return this.cjny;
	}

	public void setCjny(String cjny) {
		this.cjny = cjny;
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

	public String getCsdm() {
		return this.csdm;
	}

	public void setCsdm(String csdm) {
		this.csdm = csdm;
	}

	public String getCsrq() {
		return this.csrq;
	}

	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}

	public String getCym() {
		return this.cym;
	}

	public void setCym(String cym) {
		this.cym = cym;
	}

	public String getDabh() {
		return this.dabh;
	}

	public void setDabh(String dabh) {
		this.dabh = dabh;
	}

	public String getDawb() {
		return this.dawb;
	}

	public void setDawb(String dawb) {
		this.dawb = dawb;
	}

	public String getDzxx() {
		return this.dzxx;
	}

	public void setDzxx(String dzxx) {
		this.dzxx = dzxx;
	}

	public String getEeno() {
		return this.eeno;
	}

	public void setEeno(String eeno) {
		this.eeno = eeno;
	}

	public String getEmployeeType() {
		return this.employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getGatqm() {
		return this.gatqm;
	}

	public void setGatqm(String gatqm) {
		this.gatqm = gatqm;
	}

	public String getHkszd() {
		return this.hkszd;
	}

	public void setHkszd(String hkszd) {
		this.hkszd = hkszd;
	}

	public String getHkxzm() {
		return this.hkxzm;
	}

	public void setHkxzm(String hkxzm) {
		this.hkxzm = hkxzm;
	}

	public String getHyzkm() {
		return this.hyzkm;
	}

	public void setHyzkm(String hyzkm) {
		this.hyzkm = hyzkm;
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

	public String getJgm() {
		return this.jgm;
	}

	public void setJgm(String jgm) {
		this.jgm = jgm;
	}

	public String getJkzkm() {
		return this.jkzkm;
	}

	public void setJkzkm(String jkzkm) {
		this.jkzkm = jkzkm;
	}

	public String getJtcsm() {
		return this.jtcsm;
	}

	public void setJtcsm(String jtcsm) {
		this.jtcsm = jtcsm;
	}

	public String getJtzz() {
		return this.jtzz;
	}

	public void setJtzz(String jtzz) {
		this.jtzz = jtzz;
	}

	public String getJzglbm() {
		return this.jzglbm;
	}

	public void setJzglbm(String jzglbm) {
		this.jzglbm = jzglbm;
	}

	public String getKsh() {
		return this.ksh;
	}

	public void setKsh(String ksh) {
		this.ksh = ksh;
	}

	public String getLxdh() {
		return this.lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getLxny() {
		return this.lxny;
	}

	public void setLxny(String lxny) {
		this.lxny = lxny;
	}

	public String getManagerRole() {
		return this.managerRole;
	}

	public void setManagerRole(String managerRole) {
		this.managerRole = managerRole;
	}

	public String getMzm() {
		return this.mzm;
	}

	public void setMzm(String mzm) {
		this.mzm = mzm;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getRkkc() {
		return this.rkkc;
	}

	public void setRkkc(String rkkc) {
		this.rkkc = rkkc;
	}

	public String getRkzkm() {
		return this.rkzkm;
	}

	public void setRkzkm(String rkzkm) {
		this.rkzkm = rkzkm;
	}

	public String getSfzh() {
		return this.sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getSjh() {
		return this.sjh;
	}

	public void setSjh(String sjh) {
		this.sjh = sjh;
	}

	public String getSyncStatus() {
		return this.syncStatus;
	}

	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
	}

	public String getTc() {
		return this.tc;
	}

	public void setTc(String tc) {
		this.tc = tc;
	}

	public String getTxdz() {
		return this.txdz;
	}

	public void setTxdz(String txdz) {
		this.txdz = txdz;
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

	public String getWhcdm() {
		return this.whcdm;
	}

	public void setWhcdm(String whcdm) {
		this.whcdm = whcdm;
	}

	public String getWorkAddr() {
		return this.workAddr;
	}

	public void setWorkAddr(String workAddr) {
		this.workAddr = workAddr;
	}

	public String getWorkTime() {
		return this.workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public String getXbm() {
		return this.xbm;
	}

	public void setXbm(String xbm) {
		this.xbm = xbm;
	}

	public String getXm() {
		return this.xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getXmpy() {
		return this.xmpy;
	}

	public void setXmpy(String xmpy) {
		this.xmpy = xmpy;
	}

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getXxm() {
		return this.xxm;
	}

	public void setXxm(String xxm) {
		this.xxm = xxm;
	}

	public String getXxzxId() {
		return this.xxzxId;
	}

	public void setXxzxId(String xxzxId) {
		this.xxzxId = xxzxId;
	}

	public String getXzz() {
		return this.xzz;
	}

	public void setXzz(String xzz) {
		this.xzz = xzz;
	}

	public String getYxsh() {
		return this.yxsh;
	}

	public void setYxsh(String yxsh) {
		this.yxsh = yxsh;
	}

	public String getYzbm() {
		return this.yzbm;
	}

	public void setYzbm(String yzbm) {
		this.yzbm = yzbm;
	}

	public String getZgh() {
		return this.zgh;
	}

	public void setZgh(String zgh) {
		this.zgh = zgh;
	}

	public String getZjxy() {
		return this.zjxy;
	}

	public void setZjxy(String zjxy) {
		this.zjxy = zjxy;
	}

	public String getZp() {
		return this.zp;
	}

	public void setZp(String zp) {
		this.zp = zp;
	}

	public String getZydz() {
		return this.zydz;
	}

	public void setZydz(String zydz) {
		this.zydz = zydz;
	}

	public String getZyh() {
		return this.zyh;
	}

	public void setZyh(String zyh) {
		this.zyh = zyh;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getIndividualitySign() {
		return individualitySign;
	}

	public void setIndividualitySign(String individualitySign) {
		this.individualitySign = individualitySign;
	}

	public List<BzrGjtClassInfo> getManageClassList() {
		return manageClassList;
	}

	public void setManageClassList(List<BzrGjtClassInfo> manageClassList) {
		this.manageClassList = manageClassList;
	}

	public BzrGjtOrg getGjtOrg() {
		return gjtOrg;
	}

	public void setGjtOrg(BzrGjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}
}