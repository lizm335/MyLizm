package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

/**
 * The persistent class for the GJT_SPECIALTY database table. 专业表
 * 
 */
@Entity
@Table(name = "GJT_SPECIALTY")
@NamedQuery(name = "GjtSpecialty.findAll", query = "SELECT g FROM GjtSpecialty g")
public class GjtSpecialty implements Serializable {
	private static final long serialVersionUID = 1L;

	public GjtSpecialty(String id) {
		super();
		this.specialtyId = id;
	}

	@Id
	@Column(name = "SPECIALTY_ID") // 专业ID
	private String specialtyId;
	
	@Column(name = "SPECIALTY_BASE_ID")
	private String specialtyBaseId; // 专业基本信息ID
	
	@ManyToOne
	@JoinColumn(name = "SPECIALTY_BASE_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSpecialtyBase gjtSpecialtyBase;

	@OneToMany
	@JoinColumn(name = "SPECIALTY_ID")
	@OrderBy(value = "TERM_TYPE_CODE, COURSE_ID, CREATED_DT ASC")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtSpecialtyPlan> gjtSpecialtyPlans;// 专业的教学计划

	@OneToMany(mappedBy = "gjtSpecialty")
	@Where(clause = "IS_DELETED='N'")
	List<GjtGradeSpecialty> gjtGradeSpecialties;

	private Double bxxf; // 必修学分
	private Integer xxxf; // 选修学分
	private Integer zxf;// 总学分
	private Double zdbyxf;// 最低毕业学分
	private Double zyddksxf;// 中央电大考试学分
	private Double xz;// 学制
	private String xslx;// 学生类型

	private String bzkzym;// 本专科专业码

	@Column(name = "CREATED_BY") // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "IS_DELETED") // 是否删除
	private String isDeleted;

	@Column(name = "IS_ENABLED") // 是否启用
	private String isEnabled;

	private String jlny;// 建立年月

	@Column(name = "MANAGE_MODE") // 办学模式
	private String manageMode;

	@Column(name = "ORG_CODE") // 机构代码
	private String orgCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID") // 院校
	private GjtOrg gjtOrg;

	private String pycc; // 培养层次

	private String rxny;// 入学年月

	@Column(name = "SPECIALTY_CATEGORY") // 专业分类
	private String specialtyCategory;

	@Column(name = "STUDY_OBJECT") // 学习对象
	private String studyObject;

	@Column(name = "STUDY_PERIOD") // 学习年制
	private String studyPeriod;

	@Column(name = "TYPE")
	private String type;// 1正式专业 2体验专业

	@Column(name = "UPDATED_BY") // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;// 版本号

	private String xlm;// 学历码

	private String xwm;// 学位码

	private String yjszym;// 研究生专业码

	@Column(name = "YX_ID") // 院系Id
	private String yxId;

	@Column(name = "XX_ID") // 院系Id
	private String xxId;

	private String zyh;// 专业号

	private String zyjc;// 专业简称

	@Lob
	private String zyjs;// 专业介绍

	private String zylb;// 专业类别

	private String zymc;// 专业名称

	private String zyywmc;// 专业英文名称

	@Column(name = "SYHY")
	private String syhy;// 适用行业

	@Column(name = "ZYFM")
	private String zyfm;// 专业封面

	@Column(name = "RULE_CODE")
	private String ruleCode;// 专业规则号

	@Column(name = "STATUS")
	private int status;//状态:1-编辑中,2-未发布,3-已发布

	@Column(name = "SUBJECT")
	private String subject;  //学科

	@Column(name = "CATEGORY")
	private String category;  //学科门类


	public GjtOrg getGjtOrg() {
		return gjtOrg;
	}

	public void setGjtOrg(GjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}

	/*
	 * public GjtSchoolInfo getGjtSchoolInfo() { return gjtSchoolInfo; }
	 * 
	 * public void setGjtSchoolInfo(GjtSchoolInfo gjtSchoolInfo) {
	 * this.gjtSchoolInfo = gjtSchoolInfo; }
	 */

	public Double getBxxf() {
		return bxxf;
	}

	public void setBxxf(Double bxxf) {
		this.bxxf = bxxf;
	}

	public Integer getXxxf() {
		return xxxf;
	}

	public void setXxxf(Integer xxxf) {
		this.xxxf = xxxf;
	}

	public Integer getZxf() {
		return zxf;
	}

	public void setZxf(Integer zxf) {
		this.zxf = zxf;
	}

	public Double getZdbyxf() {
		return zdbyxf;
	}

	public void setZdbyxf(Double zdbyxf) {
		this.zdbyxf = zdbyxf;
	}

	public Double getZyddksxf() {
		return zyddksxf;
	}

	public void setZyddksxf(Double zyddksxf) {
		this.zyddksxf = zyddksxf;
	}

	public Double getXz() {
		return xz;
	}

	public void setXz(Double xz) {
		this.xz = xz;
	}

	public String getXslx() {
		return xslx;
	}

	public void setXslx(String xslx) {
		this.xslx = xslx;
	}

	public GjtSpecialty() {
	}

	public String getSpecialtyId() {
		return this.specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public String getBzkzym() {
		return this.bzkzym;
	}

	public void setBzkzym(String bzkzym) {
		this.bzkzym = bzkzym;
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

	public String getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getJlny() {
		return this.jlny;
	}

	public void setJlny(String jlny) {
		this.jlny = jlny;
	}

	public String getManageMode() {
		return this.manageMode;
	}

	public void setManageMode(String manageMode) {
		this.manageMode = manageMode;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getPycc() {
		return this.pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public String getRxny() {
		return this.rxny;
	}

	public void setRxny(String rxny) {
		this.rxny = rxny;
	}

	public String getSpecialtyCategory() {
		return this.specialtyCategory;
	}

	public void setSpecialtyCategory(String specialtyCategory) {
		this.specialtyCategory = specialtyCategory;
	}

	public String getStudyObject() {
		return this.studyObject;
	}

	public void setStudyObject(String studyObject) {
		this.studyObject = studyObject;
	}

	public String getStudyPeriod() {
		return this.studyPeriod;
	}

	public void setStudyPeriod(String studyPeriod) {
		this.studyPeriod = studyPeriod;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getXlm() {
		return this.xlm;
	}

	public void setXlm(String xlm) {
		this.xlm = xlm;
	}

	public String getXwm() {
		return this.xwm;
	}

	public void setXwm(String xwm) {
		this.xwm = xwm;
	}

	public String getYjszym() {
		return this.yjszym;
	}

	public void setYjszym(String yjszym) {
		this.yjszym = yjszym;
	}

	public String getYxId() {
		return this.yxId;
	}

	public void setYxId(String yxId) {
		this.yxId = yxId;
	}

	public String getZyh() {
		return this.zyh;
	}

	public void setZyh(String zyh) {
		this.zyh = zyh;
	}

	public String getZyjc() {
		return this.zyjc;
	}

	public void setZyjc(String zyjc) {
		this.zyjc = zyjc;
	}

	public String getZyjs() {
		return this.zyjs;
	}

	public void setZyjs(String zyjs) {
		this.zyjs = zyjs;
	}

	public String getZylb() {
		return this.zylb;
	}

	public void setZylb(String zylb) {
		this.zylb = zylb;
	}

	public String getZymc() {
		return this.zymc;
	}

	public void setZymc(String zymc) {
		this.zymc = zymc;
	}

	public String getZyywmc() {
		return this.zyywmc;
	}

	public void setZyywmc(String zyywmc) {
		this.zyywmc = zyywmc;
	}

	public List<GjtSpecialtyPlan> getGjtSpecialtyPlans() {
		return gjtSpecialtyPlans;
	}

	public void setGjtSpecialtyPlans(List<GjtSpecialtyPlan> gjtSpecialtyPlans) {
		this.gjtSpecialtyPlans = gjtSpecialtyPlans;
	}

	/**
	 * @return the xxId
	 */
	public String getXxId() {
		return xxId;
	}

	/**
	 * @param xxId
	 *            the xxId to set
	 */
	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	/**
	 * @return the syhy
	 */
	public String getSyhy() {
		return syhy;
	}

	/**
	 * @param syhy
	 *            the syhy to set
	 */
	public void setSyhy(String syhy) {
		this.syhy = syhy;
	}

	public String getSpecialtyBaseId() {
		return specialtyBaseId;
	}

	public void setSpecialtyBaseId(String specialtyBaseId) {
		this.specialtyBaseId = specialtyBaseId;
	}

	public GjtSpecialtyBase getGjtSpecialtyBase() {
		return gjtSpecialtyBase;
	}

	public void setGjtSpecialtyBase(GjtSpecialtyBase gjtSpecialtyBase) {
		this.gjtSpecialtyBase = gjtSpecialtyBase;
	}

	public String getZyfm() {
		return zyfm;
	}

	public void setZyfm(String zyfm) {
		this.zyfm = zyfm;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<GjtGradeSpecialty> getGjtGradeSpecialties() {
		return gjtGradeSpecialties;
	}

	public void setGjtGradeSpecialties(List<GjtGradeSpecialty> gjtGradeSpecialties) {
		this.gjtGradeSpecialties = gjtGradeSpecialties;
	}

}