package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 学员信息实体类<br>
 * The persistent class for the GJT_STUDENT_INFO database table.
 * 
 */
@Entity
@Table(name = "GJT_STUDENT_INFO")
// @NamedQuery(name = "GjtStudentInfo.findAll", query = "SELECT g FROM GjtStudentInfo g")
@Deprecated public class BzrGjtStudentInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	public BzrGjtStudentInfo() {
	}

	public BzrGjtStudentInfo(String studentId) {
		this.studentId = studentId;
	}

	@Id
	@Column(name = "STUDENT_ID")
	private String studentId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrViewStudentInfo viewStudentInfo;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "STUDENT_ID")
	private List<BzrGjtClassStudent> gjtClassStudentList;

	// // 第二种写法
	// @OneToMany(mappedBy = "gjtStudentInfo", cascade = { CascadeType.PERSIST,
	// CascadeType.MERGE, CascadeType.REFRESH })
	// private List<GjtClassStudent> gjtClassStudentList;

	/*
	 * 不能用有问题，is_deleted没过滤 ，数据不准确
	 * 
	 * @ManyToMany
	 * 
	 * @JoinTable(name = "GJT_CLASS_STUDENT", joinColumns = { @JoinColumn(name =
	 * "STUDENT_ID") }, inverseJoinColumns = {
	 * 
	 * @JoinColumn(name = "CLASS_ID") })
	 * 
	 * @NotFound(action = NotFoundAction.IGNORE) private List<GjtClassInfo>
	 * gjtClassInfoList; // 报读班级
	 */
	@OneToMany(mappedBy = "gjtStudentInfo", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private List<BzrGjtStudentTerm> gjtStudentTermList;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "STUDENT_ID")
	private List<BzrGjtStudentProperty> gjtStudentPropertyList;

	@OneToOne
	@JoinColumn(name = "ACCOUNT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtUserAccount gjtUserAccount;

	@ManyToOne
	@JoinColumn(name = "MAJOR")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtSpecialty gjtSpecialty;

	@OneToOne(mappedBy = "gjtStudentInfo", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH })
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtSignup gjtSignup;

	@OneToOne(mappedBy = "gjtStudentInfo")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtGraduationStu gjtGraduationStu;

	@ManyToOne
	@JoinColumn(name = "XX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtSchoolInfo gjtSchoolInfo;

	@Column(name = "XX_ID", insertable = false, updatable = false) // 11正式学员12跟读学员21体验学员31测试学员
	private String xxId;

	@ManyToOne
	@JoinColumn(name = "XXZX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtStudyCenter gjtStudyCenter;

	private String academic;

	private String accountnature;

	private String area;

	private String avatar;

	private String bh;

	private String certificatetype;

	private String city;

	@Column(name = "CLASS_TYPE")
	private String classType;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", updatable = false)
	private Date createdDt;

	private String csdm;

	private String csrq;

	private String cym;

	private String distributestatus;

	private String dszgh;

	private String dzxx;

	private String eeno;

	private String eesync;

	private String excertificatenum;

	private String excertificateprove;

	private String excertificateprovenum;

	private String exedubaktype;

	private String exedulevel;

	private String exedumajor;

	private String exeduname;

	private String exedunum;

	private String exgraduatedtime;

	private String exschool;

	private String exsubject;

	private String exsubjectkind;

	private String forward;

	private String gatqm;

	private String gbm;

	@Column(name = "GRADUATION_STATUS")
	private String graduationStatus;

	private String hdxlfsm;

	private String hkszd;

	private String hkxzm;

	private String hyzkm;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	@Column(name = "IS_FIRST_LOGIN")
	private String isFirstLogin;

	private String isgraduatebytv;

	private String ismarry;

	private String isonjob;

	private String jgm;

	private String jkzkm;

	private String lxdh;

	private String major2;

	private String mzm;

	private String nation;

	private String nativeplace;

	@ManyToOne(fetch = FetchType.LAZY) // 年级
	@JoinColumn(name = "nj", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtGrade gjtGrade;

	private String nj;

	@Column(name = "ORG_CODE")
	private String orgCode;

	@Column(name = "ORG_ID")
	private String orgId;

	private String politicsstatus;

	private String province;

	private String pycc;

	private String pyfsm;

	private String ratifyno;

	private String rxny;

	@Column(name = "SC_CO")
	private String scCo;

	@Column(name = "SC_CO_ADDR")
	private String scCoAddr;

	@Column(name = "SC_NAME")
	private String scName;

	@Column(name = "SC_NO")
	private String scNo;

	@Column(name = "SC_PHONE")
	private String scPhone;

	private String schoolname;

	private String sfzh;

	private String sjh;

	private String status;

	@Column(name = "SYNC_STATUS")
	private String syncStatus;

	private String tc;

	private String testscore;

	private String tuitionsource;

	private String txdz;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "USER_TYPE")
	private String userType; // 学员类型 11-正式生 12-正式跟读生 13-非正式跟读生 21-体验学员 31-测试学员
								// 41-课程预读生 42-电大续读生 51-外校预科生 61-本科预读生

	private String userclass;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	private String xbm;

	private String xfzxszk;

	private String xh;

	private String xjh;

	private String xjzt;

	/**
	 * 学习状态	1-正常学习 2-停止学习
	 */
	@Column(name = "LEARNING_STATE", insertable = false)
	private Integer learningState;

	private String xm;

	private String xmpy;

	private String xslbm;

	private String xxm;

	private BigDecimal xz;

	private String xzz;

	private String yjfxm;

	private String yxsh;

	private String yzbm;

	private String zjxy;

	private String zp;

	private String zydz;

	private String zyh;

	private String zyklm;

	@Column(name = "IS_ENTERING_SCHOOL")
	private String isEnteringSchool; // 是否入学，默认为0 1-是 0-否

	@Column(name = "ROLL_REGISTER_DT")
	private String rollRegisterDt; // 学籍注册时间 格式:yyyy-MM

	@Column(name = "PERFECT_STATUS", insertable = false)
	private Integer perfectStatus; // 资料完善状态，默认0 1-已完善 0-未完善，进入第一步标识-确认个人信息
	// 2-进入第二步标识-确认通讯信息 3-进入第三步标识-确认报读信息
	// 4-进入第四步标识-确认原最高学历 5-进入第五步标识-确认证件信息
	// 6-进入第六步标识-确认签名

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getAcademic() {
		return this.academic;
	}

	public void setAcademic(String academic) {
		this.academic = academic;
	}

	public String getAccountnature() {
		return this.accountnature;
	}

	public void setAccountnature(String accountnature) {
		this.accountnature = accountnature;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getBh() {
		return this.bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}

	public String getCertificatetype() {
		return this.certificatetype;
	}

	public void setCertificatetype(String certificatetype) {
		this.certificatetype = certificatetype;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getClassType() {
		return this.classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
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

	public String getDistributestatus() {
		return this.distributestatus;
	}

	public void setDistributestatus(String distributestatus) {
		this.distributestatus = distributestatus;
	}

	public String getDszgh() {
		return this.dszgh;
	}

	public void setDszgh(String dszgh) {
		this.dszgh = dszgh;
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

	public String getEesync() {
		return this.eesync;
	}

	public void setEesync(String eesync) {
		this.eesync = eesync;
	}

	public String getExcertificatenum() {
		return this.excertificatenum;
	}

	public void setExcertificatenum(String excertificatenum) {
		this.excertificatenum = excertificatenum;
	}

	public String getExcertificateprove() {
		return this.excertificateprove;
	}

	public void setExcertificateprove(String excertificateprove) {
		this.excertificateprove = excertificateprove;
	}

	public String getExcertificateprovenum() {
		return this.excertificateprovenum;
	}

	public void setExcertificateprovenum(String excertificateprovenum) {
		this.excertificateprovenum = excertificateprovenum;
	}

	public String getExedubaktype() {
		return this.exedubaktype;
	}

	public void setExedubaktype(String exedubaktype) {
		this.exedubaktype = exedubaktype;
	}

	public String getExedulevel() {
		return this.exedulevel;
	}

	public void setExedulevel(String exedulevel) {
		this.exedulevel = exedulevel;
	}

	public String getExedumajor() {
		return this.exedumajor;
	}

	public void setExedumajor(String exedumajor) {
		this.exedumajor = exedumajor;
	}

	public String getExeduname() {
		return this.exeduname;
	}

	public void setExeduname(String exeduname) {
		this.exeduname = exeduname;
	}

	public String getExedunum() {
		return this.exedunum;
	}

	public void setExedunum(String exedunum) {
		this.exedunum = exedunum;
	}

	public String getExgraduatedtime() {
		return this.exgraduatedtime;
	}

	public void setExgraduatedtime(String exgraduatedtime) {
		this.exgraduatedtime = exgraduatedtime;
	}

	public String getExschool() {
		return this.exschool;
	}

	public void setExschool(String exschool) {
		this.exschool = exschool;
	}

	public String getExsubject() {
		return this.exsubject;
	}

	public void setExsubject(String exsubject) {
		this.exsubject = exsubject;
	}

	public String getExsubjectkind() {
		return this.exsubjectkind;
	}

	public void setExsubjectkind(String exsubjectkind) {
		this.exsubjectkind = exsubjectkind;
	}

	public String getForward() {
		return this.forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public String getGatqm() {
		return this.gatqm;
	}

	public void setGatqm(String gatqm) {
		this.gatqm = gatqm;
	}

	public String getGbm() {
		return this.gbm;
	}

	public void setGbm(String gbm) {
		this.gbm = gbm;
	}

	public String getGraduationStatus() {
		return this.graduationStatus;
	}

	public void setGraduationStatus(String graduationStatus) {
		this.graduationStatus = graduationStatus;
	}

	public String getHdxlfsm() {
		return this.hdxlfsm;
	}

	public void setHdxlfsm(String hdxlfsm) {
		this.hdxlfsm = hdxlfsm;
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

	public String getIsFirstLogin() {
		return this.isFirstLogin;
	}

	public void setIsFirstLogin(String isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public String getIsgraduatebytv() {
		return this.isgraduatebytv;
	}

	public void setIsgraduatebytv(String isgraduatebytv) {
		this.isgraduatebytv = isgraduatebytv;
	}

	public String getIsmarry() {
		return this.ismarry;
	}

	public void setIsmarry(String ismarry) {
		this.ismarry = ismarry;
	}

	public String getIsonjob() {
		return this.isonjob;
	}

	public void setIsonjob(String isonjob) {
		this.isonjob = isonjob;
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

	public String getLxdh() {
		return this.lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getMajor2() {
		return this.major2;
	}

	public void setMajor2(String major2) {
		this.major2 = major2;
	}

	public String getMzm() {
		return this.mzm;
	}

	public void setMzm(String mzm) {
		this.mzm = mzm;
	}

	public String getNation() {
		return this.nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getNativeplace() {
		return this.nativeplace;
	}

	public void setNativeplace(String nativeplace) {
		this.nativeplace = nativeplace;
	}

	public BzrGjtGrade getGjtGrade() {
		return gjtGrade;
	}

	public void setGjtGrade(BzrGjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	public String getNj() {
		return this.nj;
	}

	public void setNj(String nj) {
		this.nj = nj;
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

	public String getPoliticsstatus() {
		return this.politicsstatus;
	}

	public void setPoliticsstatus(String politicsstatus) {
		this.politicsstatus = politicsstatus;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPycc() {
		return this.pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public String getPyfsm() {
		return this.pyfsm;
	}

	public void setPyfsm(String pyfsm) {
		this.pyfsm = pyfsm;
	}

	public String getRatifyno() {
		return this.ratifyno;
	}

	public void setRatifyno(String ratifyno) {
		this.ratifyno = ratifyno;
	}

	public String getRxny() {
		return this.rxny;
	}

	public void setRxny(String rxny) {
		this.rxny = rxny;
	}

	public String getScCo() {
		return this.scCo;
	}

	public void setScCo(String scCo) {
		this.scCo = scCo;
	}

	public String getScCoAddr() {
		return this.scCoAddr;
	}

	public void setScCoAddr(String scCoAddr) {
		this.scCoAddr = scCoAddr;
	}

	public String getScName() {
		return this.scName;
	}

	public void setScName(String scName) {
		this.scName = scName;
	}

	public String getScNo() {
		return this.scNo;
	}

	public void setScNo(String scNo) {
		this.scNo = scNo;
	}

	public String getScPhone() {
		return this.scPhone;
	}

	public void setScPhone(String scPhone) {
		this.scPhone = scPhone;
	}

	public String getSchoolname() {
		return this.schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getTestscore() {
		return this.testscore;
	}

	public void setTestscore(String testscore) {
		this.testscore = testscore;
	}

	public String getTuitionsource() {
		return this.tuitionsource;
	}

	public void setTuitionsource(String tuitionsource) {
		this.tuitionsource = tuitionsource;
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

	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserclass() {
		return this.userclass;
	}

	public void setUserclass(String userclass) {
		this.userclass = userclass;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public String getXbm() {
		return this.xbm;
	}

	public void setXbm(String xbm) {
		this.xbm = xbm;
	}

	public String getXfzxszk() {
		return this.xfzxszk;
	}

	public void setXfzxszk(String xfzxszk) {
		this.xfzxszk = xfzxszk;
	}

	public String getXh() {
		return this.xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getXjh() {
		return this.xjh;
	}

	public void setXjh(String xjh) {
		this.xjh = xjh;
	}

	public String getXjzt() {
		return this.xjzt;
	}

	public void setXjzt(String xjzt) {
		this.xjzt = xjzt;
	}

	public Integer getLearningState() {
		return learningState;
	}

	public void setLearningState(Integer learningState) {
		this.learningState = learningState;
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

	public String getXslbm() {
		return this.xslbm;
	}

	public void setXslbm(String xslbm) {
		this.xslbm = xslbm;
	}

	public String getXxm() {
		return this.xxm;
	}

	public void setXxm(String xxm) {
		this.xxm = xxm;
	}

	public BigDecimal getXz() {
		return this.xz;
	}

	public void setXz(BigDecimal xz) {
		this.xz = xz;
	}

	public String getXzz() {
		return this.xzz;
	}

	public void setXzz(String xzz) {
		this.xzz = xzz;
	}

	public String getYjfxm() {
		return this.yjfxm;
	}

	public void setYjfxm(String yjfxm) {
		this.yjfxm = yjfxm;
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

	public String getZyklm() {
		return this.zyklm;
	}

	public void setZyklm(String zyklm) {
		this.zyklm = zyklm;
	}

	public String getIsEnteringSchool() {
		return isEnteringSchool;
	}

	public void setIsEnteringSchool(String isEnteringSchool) {
		this.isEnteringSchool = isEnteringSchool;
	}

	/**
	 * @return the gjtUserAccount
	 */
	public BzrGjtUserAccount getGjtUserAccount() {
		return gjtUserAccount;
	}

	/**
	 * @param gjtUserAccount
	 *            the gjtUserAccount to set
	 */
	public void setGjtUserAccount(BzrGjtUserAccount gjtUserAccount) {
		this.gjtUserAccount = gjtUserAccount;
	}

	/**
	 * @return the gjtSpecialty
	 */
	public BzrGjtSpecialty getGjtSpecialty() {
		return gjtSpecialty;
	}

	/**
	 * @param gjtSpecialty
	 *            the gjtSpecialty to set
	 */
	public void setGjtSpecialty(BzrGjtSpecialty gjtSpecialty) {
		this.gjtSpecialty = gjtSpecialty;
	}

	/**
	 * @return the gjtSignup
	 */
	public BzrGjtSignup getGjtSignup() {
		return gjtSignup;
	}

	/**
	 * @param gjtSignup
	 *            the gjtSignup to set
	 */
	public void setGjtSignup(BzrGjtSignup gjtSignup) {
		this.gjtSignup = gjtSignup;
	}

	/**
	 * @return the gjtGraduationStu
	 */
	public BzrGjtGraduationStu getGjtGraduationStu() {
		return gjtGraduationStu;
	}

	/**
	 * @param gjtGraduationStu
	 *            the gjtGraduationStu to set
	 */
	public void setGjtGraduationStu(BzrGjtGraduationStu gjtGraduationStu) {
		this.gjtGraduationStu = gjtGraduationStu;
	}

	public BzrGjtSchoolInfo getGjtSchoolInfo() {
		return gjtSchoolInfo;
	}

	public void setGjtSchoolInfo(BzrGjtSchoolInfo gjtSchoolInfo) {
		this.gjtSchoolInfo = gjtSchoolInfo;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getRollRegisterDt() {
		return rollRegisterDt;
	}

	public void setRollRegisterDt(String rollRegisterDt) {
		this.rollRegisterDt = rollRegisterDt;
	}

	public Integer getPerfectStatus() {
		return perfectStatus;
	}

	public void setPerfectStatus(Integer perfectStatus) {
		this.perfectStatus = perfectStatus;
	}

	public BzrGjtStudyCenter getGjtStudyCenter() {
		return gjtStudyCenter;
	}

	public void setGjtStudyCenter(BzrGjtStudyCenter gjtStudyCenter) {
		this.gjtStudyCenter = gjtStudyCenter;
	}

	/**
	 * @return the gjtClassStudentList
	 */
	public List<BzrGjtClassStudent> getGjtClassStudentList() {
		return gjtClassStudentList;
	}

	/**
	 * @param gjtClassStudentList
	 *            the gjtClassStudentList to set
	 */
	public void setGjtClassStudentList(List<BzrGjtClassStudent> gjtClassStudentList) {
		this.gjtClassStudentList = gjtClassStudentList;
	}

	public BzrGjtClassStudent addGjtClassStudentList(BzrGjtClassStudent gjtClassStudentList) {
		getGjtClassStudentList().add(gjtClassStudentList);
		gjtClassStudentList.setGjtStudentInfo(this);

		return gjtClassStudentList;
	}

	public BzrGjtClassStudent removeGjtClassStudentList(BzrGjtClassStudent gjtClassStudentList) {
		getGjtClassStudentList().remove(gjtClassStudentList);
		gjtClassStudentList.setGjtStudentInfo(null);

		return gjtClassStudentList;
	}

	/*
	 * public List<GjtClassInfo> getGjtClassInfoList() { return
	 * gjtClassInfoList; }
	 * 
	 * public void setGjtClassInfoList(List<GjtClassInfo> gjtClassInfoList) {
	 * this.gjtClassInfoList = gjtClassInfoList; }
	 */

	public List<BzrGjtStudentProperty> getGjtStudentPropertyList() {
		return gjtStudentPropertyList;
	}

	public void setGjtStudentPropertyList(List<BzrGjtStudentProperty> gjtStudentPropertyList) {
		this.gjtStudentPropertyList = gjtStudentPropertyList;
	}

	public BzrGjtStudentProperty addGjtStudentPropertyList(BzrGjtStudentProperty gjtStudentPropertyList) {
		getGjtStudentPropertyList().add(gjtStudentPropertyList);
		// gjtStudentPropertyList.setGjtStudentInfo(this);

		return gjtStudentPropertyList;
	}

	public BzrGjtStudentProperty removeGjtStudentPropertyList(BzrGjtStudentProperty gjtStudentPropertyList) {
		getGjtStudentPropertyList().remove(gjtStudentPropertyList);
		// gjtStudentPropertyList.setGjtStudentInfo(null);

		return gjtStudentPropertyList;
	}

	/**
	 * @return the gjtStudentTermList
	 */
	public List<BzrGjtStudentTerm> getGjtStudentTermList() {
		return gjtStudentTermList;
	}

	/**
	 * @param gjtStudentTermList
	 *            the gjtStudentTermList to set
	 */
	public void setGjtStudentTermList(List<BzrGjtStudentTerm> gjtStudentTermList) {
		this.gjtStudentTermList = gjtStudentTermList;
	}

	public BzrViewStudentInfo getViewStudentInfo() {
		return viewStudentInfo;
	}

	public void setViewStudentInfo(BzrViewStudentInfo viewStudentInfo) {
		this.viewStudentInfo = viewStudentInfo;
	}
}