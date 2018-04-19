package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the GJT_RULES_SETTING database table.
 * 
 */
@Entity
@Table(name = "GJT_RULES_SETTING")
@NamedQuery(name = "GjtRulesSetting.findAll", query = "SELECT g FROM GjtRulesSetting g")
public class GjtRulesSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String classtype; // 班级类别 teach 学籍班 course 课程班

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@OneToOne
	@JoinColumn(name = "GRADE_ID") // 年级
	private GjtGrade gjtGrade;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "IS_ENABLED")
	private String isEnabled;

	private String memo;

	// @OneToOne
	// @JoinColumn(name = "ORG_ID") // 机构
	// @NotFound(action = NotFoundAction.IGNORE)
	// private GjtOrg gjtOrg;

	@Column(name = "PEOPLE_NO")
	private int peopleNo;

	private String pycc;

	@OneToOne // 专业
	@JoinColumn(name = "SPECIALTY_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSpecialty gjtSpecialty;

	@OneToOne
	@JoinColumn(name = "STUDYCENTER_ID") // 学习中心
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtStudyCenter gjtStudyCenter;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@OneToOne // 学院信息
	@JoinColumn(name = "XX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSchoolInfo gjtSchoolInfo;

	public GjtRulesSetting() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClasstype() {
		return this.classtype;
	}

	public void setClasstype(String classtype) {
		this.classtype = classtype;
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

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getPeopleNo() {
		return peopleNo;
	}

	public void setPeopleNo(int peopleNo) {
		this.peopleNo = peopleNo;
	}

	public String getPycc() {
		return this.pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
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

	// /**
	// * @return the gjtOrg
	// */
	// public GjtOrg getGjtOrg() {
	// return gjtOrg;
	// }
	//
	// /**
	// * @param gjtOrg
	// * the gjtOrg to set
	// */
	// public void setGjtOrg(GjtOrg gjtOrg) {
	// this.gjtOrg = gjtOrg;
	// }

	/**
	 * @return the gjtSpecialty
	 */
	public GjtSpecialty getGjtSpecialty() {
		return gjtSpecialty;
	}

	/**
	 * @param gjtSpecialty
	 *            the gjtSpecialty to set
	 */
	public void setGjtSpecialty(GjtSpecialty gjtSpecialty) {
		this.gjtSpecialty = gjtSpecialty;
	}

	/**
	 * @return the gjtStudyCenter
	 */
	public GjtStudyCenter getGjtStudyCenter() {
		return gjtStudyCenter;
	}

	/**
	 * @param gjtStudyCenter
	 *            the gjtStudyCenter to set
	 */
	public void setGjtStudyCenter(GjtStudyCenter gjtStudyCenter) {
		this.gjtStudyCenter = gjtStudyCenter;
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