package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 学期信息实体类<br>
 * The persistent class for the GJT_TERM_INFO database table.学期表
 *
 */
@Entity
@Table(name = "GJT_TERM_INFO")
// @NamedQuery(name = "GjtTermInfo.findAll", query = "SELECT g FROM GjtTermInfo g")
@Deprecated public class BzrGjtTermInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TERM_ID")
	private String termId;

	@OneToMany(mappedBy = "gjtTermInfo", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private List<BzrGjtStudentTerm> gjtStudentTermList;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE")
	private Date endDate;// 结束日期

	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }) // 年级
	@JoinColumn(name = "GRADE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtGrade gjtGrade;// 年级

	@Column(name = "IS_CURRENT")
	private String isCurrent;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	private String memo;// 备注

	@Column(name = "ORG_CODE")
	private String orgCode;// 机构代码

	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }) // 学习机构
	@JoinColumn(name = "ORG_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtOrg gjtOrg;// 机构

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

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }) // 学院信息
	@JoinColumn(name = "XX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtSchoolInfo gjtSchoolInfo;

	public BzrGjtTermInfo() {
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
	public BzrGjtGrade getGjtGrade() {
		return gjtGrade;
	}

	/**
	 * @param gjtGrade
	 *            the gjtGrade to set
	 */
	public void setGjtGrade(BzrGjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	/**
	 * @return the gjtOrg
	 */
	public BzrGjtOrg getGjtOrg() {
		return gjtOrg;
	}

	/**
	 * @param gjtOrg
	 *            the gjtOrg to set
	 */
	public void setGjtOrg(BzrGjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}

	/**
	 * @return the gjtSchoolInfo
	 */
	public BzrGjtSchoolInfo getGjtSchoolInfo() {
		return gjtSchoolInfo;
	}

	/**
	 * @param gjtSchoolInfo
	 *            the gjtSchoolInfo to set
	 */
	public void setGjtSchoolInfo(BzrGjtSchoolInfo gjtSchoolInfo) {
		this.gjtSchoolInfo = gjtSchoolInfo;
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

}
