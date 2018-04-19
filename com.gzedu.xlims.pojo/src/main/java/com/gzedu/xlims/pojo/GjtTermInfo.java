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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the GJT_TERM_INFO database table.学期表
 * 
 */
@Entity
@Table(name = "GJT_TERM_INFO")
@NamedQuery(name = "GjtTermInfo.findAll", query = "SELECT g FROM GjtTermInfo g")
public class GjtTermInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	// @GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "TERM_ID")
	private String termId;

	@Column(name = "CREATED_BY")
	private String createdBy;// 创建人

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;// 创建日期

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE")
	private Date endDate;// 结束日期

	@ManyToOne(fetch = FetchType.LAZY) // 年级
	@JoinColumn(name = "GRADE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtGrade gjtGrade;// 年级

	@Column(name = "IS_CURRENT")
	private String isCurrent;

	@Column(name = "IS_DELETED")
	private String isDeleted;// 是否删除

	@Column(name = "IS_ENABLED")
	private String isEnabled;

	private String memo;// 备注

	@Column(name = "ORG_CODE")
	private String orgCode;// 机构代码

	@ManyToOne(fetch = FetchType.LAZY) // 学习机构
	@JoinColumn(name = "ORG_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtOrg gjtOrg;// 机构

	@Column(name = "PLAN_TEACH_WEEK")
	private BigDecimal planTeachWeek;

	@Temporal(TemporalType.DATE)
	@Column(name = "RC_ETIME")
	private Date rcEtime;

	@Temporal(TemporalType.DATE)
	@Column(name = "RC_STIME")
	private Date rcStime;

	@Temporal(TemporalType.DATE)
	@Column(name = "RE_ETIME")
	private Date reEtime;

	@Temporal(TemporalType.DATE)
	@Column(name = "RE_STIME")
	private Date reStime;

	@Temporal(TemporalType.DATE)
	@Column(name = "RL_ETIME")
	private Date rlEtime;

	@Temporal(TemporalType.DATE)
	@Column(name = "RL_STIME")
	private Date rlStime;

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE")
	private Date startDate;// 开始时间

	@Column(name = "TERM_CODE")
	private String termCode;// 学期代码

	@Column(name = "TERM_DESC")
	private String termDesc;// 学期描述

	@Column(name = "TERM_NAME")
	private String termName;// 学期名称

	@Column(name = "TERM_YEAR")
	private String termYear;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@ManyToOne(fetch = FetchType.LAZY) // 学院信息
	@JoinColumn(name = "XX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSchoolInfo gjtSchoolInfo;

	public GjtTermInfo() {
	}

	public String getTermId() {
		return this.termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
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

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getIsCurrent() {
		return this.isCurrent;
	}

	public void setIsCurrent(String isCurrent) {
		this.isCurrent = isCurrent;
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

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public BigDecimal getPlanTeachWeek() {
		return this.planTeachWeek;
	}

	public void setPlanTeachWeek(BigDecimal planTeachWeek) {
		this.planTeachWeek = planTeachWeek;
	}

	public Date getRcEtime() {
		return this.rcEtime;
	}

	public void setRcEtime(Date rcEtime) {
		this.rcEtime = rcEtime;
	}

	public Date getRcStime() {
		return this.rcStime;
	}

	public void setRcStime(Date rcStime) {
		this.rcStime = rcStime;
	}

	public Date getReEtime() {
		return this.reEtime;
	}

	public void setReEtime(Date reEtime) {
		this.reEtime = reEtime;
	}

	public Date getReStime() {
		return this.reStime;
	}

	public void setReStime(Date reStime) {
		this.reStime = reStime;
	}

	public Date getRlEtime() {
		return this.rlEtime;
	}

	public void setRlEtime(Date rlEtime) {
		this.rlEtime = rlEtime;
	}

	public Date getRlStime() {
		return this.rlStime;
	}

	public void setRlStime(Date rlStime) {
		this.rlStime = rlStime;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getTermCode() {
		return this.termCode;
	}

	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}

	public String getTermDesc() {
		return this.termDesc;
	}

	public void setTermDesc(String termDesc) {
		this.termDesc = termDesc;
	}

	public String getTermName() {
		return this.termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public String getTermYear() {
		return this.termYear;
	}

	public void setTermYear(String termYear) {
		this.termYear = termYear;
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

	/**
	 * @return the gjtGrade
	 */
	public GjtGrade getGjtGrade() {
		return gjtGrade;
	}

	/**
	 * @param gjtGrade
	 *            the gjtGrade to set
	 */
	public void setGjtGrade(GjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	/**
	 * @return the gjtOrg
	 */
	public GjtOrg getGjtOrg() {
		return gjtOrg;
	}

	/**
	 * @param gjtOrg
	 *            the gjtOrg to set
	 */
	public void setGjtOrg(GjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}

	/**
	 * @return the gjtSchoolInfo
	 */
	public GjtSchoolInfo getGjtSchoolInfo() {
		return gjtSchoolInfo;
	}

	/**
	 * @param gjtSchoolInfo
	 *            the gjtSchoolInfo to set
	 */
	public void setGjtSchoolInfo(GjtSchoolInfo gjtSchoolInfo) {
		this.gjtSchoolInfo = gjtSchoolInfo;
	}

}