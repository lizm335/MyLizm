package com.gzedu.xlims.pojo;

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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gzedu.xlims.pojo.college.GjtSpecialtyCollege;

/**
 * The persistent class for the GJT_STUDENT_INFO database table. 学生表
 */
@Entity
@Table(name = "GJT_STUDENT_INFO")
@NamedQuery(name = "GjtStudentInfo.findAll", query = "SELECT g FROM GjtStudentInfo g")
public class GjtStudentInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "STUDENT_ID") // 学生ID
	private String studentId;

	// 不能用，有问题，is_deleted没过滤数据不准确
	// 建议使用GjtClassStudentDao的findTeachClassByStudentId
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "STUDENT_ID")
	private List<GjtClassStudent> gjtClassStudentList;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "STUDENT_ID", insertable = false, updatable = false)
	private List<GjtSchoolRollTran> GjtSchoolRollTranList;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private ViewStudentInfo viewStudentInfo;

	private String academic;// 学习方式

	@OneToOne(mappedBy = "gjtStudentInfo", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSignup gjtSignup;

	@OneToOne(fetch = FetchType.LAZY) // 用户表
	@JoinColumn(name = "ACCOUNT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtUserAccount gjtUserAccount;

	@ManyToOne(fetch = FetchType.LAZY) // 专业
	@JoinColumn(name = "MAJOR", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSpecialty gjtSpecialty;

	@ManyToOne(fetch = FetchType.LAZY) // 院校模式-专业
	@JoinColumn(name = "MAJOR", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSpecialtyCollege gjtSpecialtyCollege;

	@Column(name = "MAJOR")
	private String major;

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
	@ManyToOne(fetch = FetchType.LAZY) // 学期
	@JoinColumn(name = "NJ")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtGrade gjtGrade;

	@Column(name = "NJ", insertable = false, updatable = false)
	private String nj;

	@ManyToOne(fetch = FetchType.LAZY) // 学院信息
	@JoinColumn(name = "XX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSchoolInfo gjtSchoolInfo;

	@Column(name = "XX_ID", insertable = false, updatable = false)
	private String xxId;

	@ManyToOne(fetch = FetchType.LAZY) // 学习中心
	@JoinColumn(name = "XXZX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtStudyCenter gjtStudyCenter;

	@Column(name = "XXZX_ID", insertable = false, updatable = false)
	private String xxzxId;

	// @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
	// CascadeType.REFRESH }) // 学习机构(废弃）
	// @JoinColumn(name = "ORG_ID")
	// @NotFound(action = NotFoundAction.IGNORE)
	// private GjtOrg gjtOrg;

	@Column(name = "ORG_ID") // 机构ID
	private String orgId;

	@Column(name = "ORG_CODE") // 机构编码
	private String orgCode;

	@Column(name = "USER_TYPE")
	private String userType; // 学员类型 11-正式生 12-正式跟读生 13-非正式跟读生 21-体验学员 31-测试学员
	// 41-课程预读生 42-电大续读生 51-外校预科生 61-本科预读生 字典表userType

	private String accountnature;// 户口性质

	private String area;// 区

	private String avatar;// 头像地址

	private String bh;// 班号

	private String certificatetype;// 结业证书类型

	private String city;// 市

	@Column(name = "CLASS_TYPE") // 班级类型：A：精英班；B：进取班；C: 默认班
	private String classType;

	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建日期
	@Column(name = "CREATED_DT", updatable = false)
	private Date createdDt;

	private String csdm;// 出生地码

	private String csrq;// 出生日期

	private String cym;// 曾用名

	private String distributestatus;// 分布情况

	private String dszgh;// 导师职工号

	private String dzxx;// 电子邮箱

	private String eeno;// ee帐号

	@Column(insertable = false)
	private String eesync;// 同步EE 默认N

	private String excertificatenum;// 原学历毕业证书编号

	private String excertificateprove;// 原学历证明材料

	private String excertificateprovenum;// 原学历证明材料编号

	private String exedubaktype;// 原学历类型

	private String exedulevel;// 原学历层次

	private String exedumajor;// 原学历所学专业

	private String exeduname;// 原学历姓名

	private String exedunum;// 原学历证件编号

	private String exgraduatedtime;// 毕业时间

	private String graduatedAvatar;// 毕业头像

	private String exschool;// 原毕业学校

	private String exsubject;// 原学科

	private String exsubjectkind;// 原学科类型

	@Column(insertable = false)
	private String forward;// 是否为跟读学员

	private String gatqm;// 港澳台侨码

	private String gbm;// 国别码

	@Column(name = "GRADUATION_STATUS")
	private String graduationStatus;

	private String hdxlfsm;// 获得学历方式码

	private String hkszd;// 户口所在地

	private String hkxzm;// 户口性质码

	private String hyzkm;// 婚姻状况码

	@Column(name = "GRADE_SPECIALTY_ID")
	private String gradeSpecialtyId;// 年级专业关联表GJT_GRADE_SPECAILTY的ID

	@Column(name = "IS_DELETED") // 是否删除
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false) // 是否启用
	private String isEnabled;

	@Column(name = "IS_FIRST_LOGIN", insertable = false) // 是否首次登录
	private String isFirstLogin;

	private String isgraduatebytv;// 是否电大毕业

	private String ismarry;// 是否已婚

	private String isonjob;// 在职状况

	private String jgm;// 籍贯码

	private String jkzkm;// 健康状况码

	private String lxdh;// 联系电话

	private String major2;// 专业2

	private String mzm;// 民族码

	private String nation;// 民族

	private String nativeplace;// 籍贯

	private String politicsstatus;// 政治面貌

	private String province;// 省

	private String pycc;// 培养层次

	private String pyfsm;// 培养方式码

	private String ratifyno;// 认可编号

	private String rxny;// 入学年月

	@Column(name = "SC_CO") // 所属单位名称
	private String scCo;

	@Column(name = "SC_CO_ADDR") // 单位地址
	private String scCoAddr;

	@Column(name = "SC_NAME") // 第二联系人姓名
	private String scName;

	@Column(name = "SC_NO") // 社保编号
	private String scNo;

	@Column(name = "SC_PHONE") // 第二联系人电话
	private String scPhone;

	private String schoolname;// 原学校名称

	private String sfzh;// 身份证号

	private String sjh;// 手机号

	private String status;// 0 未确认 1 已确认 2 已入库

	@Column(name = "SYNC_STATUS", insertable = false) // 同步学员到 课程平台
	private String syncStatus;

	private String tc;// 特长

	private String testscore;// 入学测试成绩

	private String tuitionsource;// 学分来源

	private String txdz;// 通信地址

	@Column(name = "UPDATED_BY", insertable = false) // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	private String userclass;// 班级名称

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;// 版本号

	private String xbm;// 性别码

	private String xfzxszk;// 1 是 0否

	private String xh;// 学号

	private String xjh;// 学籍号

	private String xjzt;// 学籍状态 字典 StudentNumberStatus

	/**
	 * 学习状态 1-正常学习 2-停止学习
	 */
	@Column(name = "LEARNING_STATE", insertable = false)
	private Integer learningState;

	private String xm;// 姓名

	private String xmpy;// 姓名拼音

	private String xslbm;// 学生类别码

	private String xxm;// 血型码

	private Double xz;// 学制

	private String xzz;// 现住址

	private String yjfxm;// 研究方向码

	private String yxsh;// 院系所部中心号

	private String yzbm;// 邮政编码

	private String zjxy;// 宗教信仰

	private String zp;// 照片

	private String zydz;// 主页地址

	private String zyh;// 专业号

	private String zyklm;// 专业科类码

	private String atid;

	@Column(name = "IS_ENTERING_SCHOOL", insertable = false)
	private String isEnteringSchool; // 是否入学，默认为0 1-是 0-否

	@Column(name = "ROLL_REGISTER_DT")
	private String rollRegisterDt; // 学籍注册时间 格式:yyyy-MM-dd

	@Column(name = "PERFECT_STATUS")
	private Integer perfectStatus; // 资料完善状态，默认0 1-已完善 0-未完善，进入第一步标识-确认个人信息
	// 2-进入第二步标识-确认通讯信息 3-进入第三步标识-确认报读信息
	// 4-进入第四步标识-确认原最高学历 5-进入第五步标识-确认证件信息
	// 6-进入第六步标识-确认签名

	@Column(name = "YUNYING_SYNC", insertable = false)
	private String yunyingSync; // 账号同步至运营平台同步状态，默认为N Y-已同步 N-未同步

	@Column(name = "ROLL_CACHE_STATUS", insertable = false)
	private Integer rollCacheStatus; // 学籍资料是否缓存到下载列表，默认0 1-已缓存 0-未缓存
	
	@Column(name = "ERP_REGISTRATION_NUMBER")
	private String erpRegistrationNumber;//电子注册号

	public GjtSpecialty getGjtSpecialty() {
		return gjtSpecialty;
	}

	public void setGjtSpecialty(GjtSpecialty gjtSpecialty) {
		this.gjtSpecialty = gjtSpecialty;
	}

	public GjtSpecialtyCollege getGjtSpecialtyCollege() {
		return gjtSpecialtyCollege;
	}

	public void setGjtSpecialtyCollege(GjtSpecialtyCollege gjtSpecialtyCollege) {
		this.gjtSpecialtyCollege = gjtSpecialtyCollege;
	}
	/*
	 * public List<GjtClassInfo> getGjtClassInfoList() { return
	 * gjtClassInfoList; }
	 *
	 * public void setGjtClassInfoList(List<GjtClassInfo> gjtClassInfoList) {
	 * this.gjtClassInfoList = gjtClassInfoList; }
	 */

	public GjtStudentInfo() {
	}

	/**
	 * @param studentid
	 */
	public GjtStudentInfo(String studentid) {
		this.studentId = studentid;
	}

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

	public GjtUserAccount getGjtUserAccount() {
		return gjtUserAccount;
	}

	public void setGjtUserAccount(GjtUserAccount gjtUserAccount) {
		this.gjtUserAccount = gjtUserAccount;
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

	public GjtGrade getGjtGrade() {
		return gjtGrade;
	}

	public void setGjtGrade(GjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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

	public GjtSchoolInfo getGjtSchoolInfo() {
		return gjtSchoolInfo;
	}

	public void setGjtSchoolInfo(GjtSchoolInfo gjtSchoolInfo) {
		this.gjtSchoolInfo = gjtSchoolInfo;
	}

	public String getXxm() {
		return this.xxm;
	}

	public void setXxm(String xxm) {
		this.xxm = xxm;
	}

	public GjtStudyCenter getGjtStudyCenter() {
		return gjtStudyCenter;
	}

	public void setGjtStudyCenter(GjtStudyCenter gjtStudyCenter) {
		this.gjtStudyCenter = gjtStudyCenter;
	}

	public String getXxzxId() {
		return xxzxId;
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

	public BigDecimal getVersion() {
		return version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public Double getXz() {
		return xz;
	}

	public void setXz(Double xz) {
		this.xz = xz;
	}

	public String getIsEnteringSchool() {
		return isEnteringSchool;
	}

	public void setIsEnteringSchool(String isEnteringSchool) {
		this.isEnteringSchool = isEnteringSchool;
	}

	public ViewStudentInfo getViewStudentInfo() {
		return viewStudentInfo;
	}

	public void setViewStudentInfo(ViewStudentInfo viewStudentInfo) {
		this.viewStudentInfo = viewStudentInfo;
	}

	public GjtSignup getGjtSignup() {
		return gjtSignup;
	}

	public void setGjtSignup(GjtSignup gjtSignup) {
		this.gjtSignup = gjtSignup;
	}

	public String getNj() {
		return nj;
	}

	public void setNj(String nj) {
		this.nj = nj;
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

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public List<GjtClassStudent> getGjtClassStudentList() {
		return gjtClassStudentList;
	}

	public void setGjtClassStudentList(List<GjtClassStudent> gjtClassStudentList) {
		this.gjtClassStudentList = gjtClassStudentList;
	}

	public String getAtid() {
		return atid;
	}

	public void setAtid(String atid) {
		this.atid = atid;
	}

	public String getGradeSpecialtyId() {
		return gradeSpecialtyId;
	}

	public void setGradeSpecialtyId(String gradeSpecialtyId) {
		this.gradeSpecialtyId = gradeSpecialtyId;
	}

	public String getYunyingSync() {
		return yunyingSync;
	}

	public void setYunyingSync(String yunyingSync) {
		this.yunyingSync = yunyingSync;
	}

	public Integer getRollCacheStatus() {
		return rollCacheStatus;
	}

	public void setRollCacheStatus(Integer rollCacheStatus) {
		this.rollCacheStatus = rollCacheStatus;
	}

	public String getErpRegistrationNumber() {
		return erpRegistrationNumber;
	}

	public void setErpRegistrationNumber(String erpRegistrationNumber) {
		this.erpRegistrationNumber = erpRegistrationNumber;
	}

	public String getGraduatedAvatar() {
		return graduatedAvatar;
	}

	public void setGraduatedAvatar(String graduatedAvatar) {
		this.graduatedAvatar = graduatedAvatar;
	}
}