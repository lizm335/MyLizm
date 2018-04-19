package com.gzedu.xlims.pojo;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the GJT_EMPLOYEE_INFO database table. 教职员工表
 */
@Entity
@Table(name = "GJT_EMPLOYEE_INFO")
@NamedQuery(name = "GjtEmployeeInfo.findAll", query = "SELECT g FROM GjtEmployeeInfo g")
public class GjtEmployeeInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EMPLOYEE_ID") // 职工ID
	private String employeeId;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 用户ID
	@JoinColumn(name = "ACCOUNT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtUserAccount gjtUserAccount;

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "EMPLOYEE_ID")
	private List<GjtEmployeePosition> gjtEmployeePositionList;

	 @OneToOne(fetch = FetchType.LAZY) // 学院表
	 @JoinColumn(name = "XX_ID")
	 private GjtSchoolInfo gjtSchoolInfo;

	@Column(name = "XX_ID",insertable = false,updatable = false)
	private String xxId;// 学院信息

	 @OneToOne(fetch = FetchType.LAZY) // 学习中心
	 @JoinColumn(name = "XXZX_ID")
	 @NotFound(action = NotFoundAction.IGNORE)
	 private GjtStudyCenter gjtStudyCenter;

	@Column(name = "XXZX_ID",insertable = false,updatable = false)
	private String xxzxId;// 学习中心

	// @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
	// CascadeType.REFRESH }) // 学习机构
	// @JoinColumn(name = "ORG_ID")
	// @NotFound(action = NotFoundAction.IGNORE)
	// private GjtOrg gjtOrg;

	@Column(name = "ORG_ID")
	private String orgId;// 机构Id

	// 教师角色 headteacher 班主任，督导教师inspector，teacher默认教师
	@Column(name = "MANAGER_ROLE")
	private String managerRole;

	private String brcfm; // 本人成分码

	private String bzlbm;// 编制类编码

	private String cjgzny; // 参加工作年月

	private String cjny;// 从教年月

	@Column(name = "CREATED_BY") // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建日期
	@Column(name = "CREATED_DT")
	private Date createdDt;

	private String csdm;// 出生地码

	private String csrq;// 出生日期

	private String cym;// 曾用名

	private String dabh;// 档案编号

	private String dawb;// 档案文本

	private String dzxx;// 电子邮箱

	private String eeno;// ee号

	@Column(name = "EMPLOYEE_TYPE") // 1班主任(教学班),2辅导教师(课程班),3其它(职工),4督导教师
	private String employeeType;

	private String gatqm;// 港澳台码

	private String hkszd;// 户口所在地

	private String hkxzm;// 户口性质码

	private String hyzkm;// 婚姻状况码

	@Column(name = "IS_DELETED") // 是否删除
	private String isDeleted;

	@Column(name = "IS_ENABLED") // 是否启用
	private String isEnabled;

	private String jgm;// 籍贯码

	private String jkzkm;// 健康状态码

	private String jtcsm;// 家庭出生码

	private String jtzz;// 家庭住址

	private String jzglbm;// 教职工类别码

	private String ksh;// 科室号

	private String lxdh;// 联系电话

	private String lxny;// 来校年月

	private String mzm;// 民族码

	@Column(name = "ORG_CODE") // 机构编码
	private String orgCode;

	private String rkkc;// 任课课程,多个以逗号隔开 做参考

	private String rkzkm;// 任课状况码

	private String sfzh;// 身份证号

	private String sjh;// 手机号

	@Column(name = "SYNC_STATUS") // 同步状态
	private String syncStatus;

	private String tc;// 特长

	private String txdz;// 通信地址

	@Column(name = "UPDATED_BY") // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION")
	private BigDecimal version;// 版本号

	private String whcdm;// 文化程度

	@Column(name = "WORK_ADDR") // 办公地点
	private String workAddr;

	@Column(name = "WORK_TIME") // 办公时间
	private String workTime;

	private String xbm;// 性别码

	private String xm;// 姓名

	private String xmpy;// 姓名拼音

	private String xxm;// 血型码

	private String xzz;// 现在住址

	private String yxsh;// 院系所部中心号

	private String yzbm;// 邮政编号

	private String zgh;// 职工号

	private String zjxy;// 宗教信仰

	private String zp;// 照片地址

	private String zydz;// 主页地址

	private String zyh;// 现从事专业号

	private String qq; // QQ号码

	@Column(name = "INDIVIDUALITY_SIGN")
	private String individualitySign; // 个性签名

	@ManyToMany
	@JoinTable(name = "GJT_CLASS_TEACHER", joinColumns = { @JoinColumn(name = "EMPLOYEE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "CLASS_ID") })
	private List<GjtClassInfo> gjtClassInfoList;

	public List<GjtClassInfo> getGjtClassInfoList() {
		return gjtClassInfoList;
	}

	public void setGjtClassInfoList(List<GjtClassInfo> gjtClassInfoList) {
		this.gjtClassInfoList = gjtClassInfoList;
	}

	public GjtEmployeeInfo() {
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public GjtUserAccount getGjtUserAccount() {
		return gjtUserAccount;
	}

	public void setGjtUserAccount(GjtUserAccount gjtUserAccount) {
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

	// public GjtOrg getGjtOrg() {
	// return gjtOrg;
	// }
	//
	// public void setGjtOrg(GjtOrg gjtOrg) {
	// this.gjtOrg = gjtOrg;
	// }

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

	public String getXxm() {
		return this.xxm;
	}

	public void setXxm(String xxm) {
		this.xxm = xxm;
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

	public String getXxzxId() {
		return xxzxId;
	}

	public void setXxzxId(String xxzxId) {
		this.xxzxId = xxzxId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
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

	public List<GjtEmployeePosition> getGjtEmployeePositionList() {
		return gjtEmployeePositionList;
	}

	public void setGjtEmployeePositionList(List<GjtEmployeePosition> gjtEmployeePositionList) {
		this.gjtEmployeePositionList = gjtEmployeePositionList;
	}

	public GjtEmployeePosition addGjtEmployeePositionList(GjtEmployeePosition gjtEmployeePosition) {
		getGjtEmployeePositionList().add(gjtEmployeePosition);

		return gjtEmployeePosition;
	}

	public GjtEmployeePosition removeGjtEmployeePositionList(GjtEmployeePosition gjtEmployeePosition) {
		getGjtEmployeePositionList().remove(gjtEmployeePosition);

		return gjtEmployeePosition;
	}

	public void removeGjtEmployeePositionListAll() {
		getGjtEmployeePositionList().removeAll(getGjtEmployeePositionList());
	}

	public GjtSchoolInfo getGjtSchoolInfo() {
		return gjtSchoolInfo;
	}

	public void setGjtSchoolInfo(GjtSchoolInfo gjtSchoolInfo) {
		this.gjtSchoolInfo = gjtSchoolInfo;
	}

	public GjtStudyCenter getGjtStudyCenter() {
		return gjtStudyCenter;
	}

	public void setGjtStudyCenter(GjtStudyCenter gjtStudyCenter) {
		this.gjtStudyCenter = gjtStudyCenter;
	}
}