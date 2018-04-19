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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 班级信息实体类<br>
 * The persistent class for the GJT_CLASS_INFO database table.
 * 
 */
@Entity
@Table(name = "GJT_CLASS_INFO")
// @NamedQuery(name = "GjtClassInfo.findAll", query = "SELECT g FROM GjtClassInfo g")
@Deprecated public class BzrGjtClassInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CLASS_ID")
	private String classId;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "GRADE_ID")
	private BzrGjtGrade gjtGrade;

	@OneToMany(mappedBy = "gjtClassInfo", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private List<BzrGjtClassStudent> gjtClassStudentList;

	private String bh;

	private String bjlx;

	private String bjmc;

	private BigDecimal bjrn;

	@Column(name = "BZ_ID")
	private String bzId;

	@Column(name = "BZR_ID")
	private String bzrId;

	@Column(name = "CLASS_TYPE")
	private String classType;

	@Column(name = "COURSE_ID")
	private String courseId;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	private String eegroup;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	@Column(name = "IS_FINISH")
	private BigDecimal isFinish;

	private String jbny;

	private String memo;

	@Column(name = "ORG_CODE")
	private String orgCode;

	@Column(name = "ORG_ID")
	private String orgId;

	private String pycc;

	@Column(name = "SPECIALTY_ID")
	private String specialtyId;

	@Column(name = "SUPERVISOR_ID")
	private String supervisorId;

	@Column(name = "SYNC_STATUS")
	private String syncStatus;

	@Column(name = "TEACH_PLAN_ID")
	private String teachPlanId;

	@Column(name = "TERM_ID")
	private String termId;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@Column(name = "XX_ID")
	private String xxId;
	
	@ManyToOne(fetch = FetchType.LAZY) // 学习中心
	@JoinColumn(name = "XXZX_ID",insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtOrg gjtOrg;
	
	@Column(name = "XXZX_ID")
	private String xxzxId;

	private BigDecimal xz;

	/**
	 * 临时数据：班级学员数
	 */
	@Transient
	private Long colStudentNum;

	public BzrGjtClassInfo() {
	}

	public String getClassId() {
		return this.classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getBh() {
		return this.bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}

	public String getBjlx() {
		return this.bjlx;
	}

	public void setBjlx(String bjlx) {
		this.bjlx = bjlx;
	}

	public String getBjmc() {
		return this.bjmc;
	}

	public void setBjmc(String bjmc) {
		this.bjmc = bjmc;
	}

	public BigDecimal getBjrn() {
		return this.bjrn;
	}

	public void setBjrn(BigDecimal bjrn) {
		this.bjrn = bjrn;
	}

	public String getBzId() {
		return this.bzId;
	}

	public void setBzId(String bzId) {
		this.bzId = bzId;
	}

	public String getBzrId() {
		return this.bzrId;
	}

	public void setBzrId(String bzrId) {
		this.bzrId = bzrId;
	}

	public String getClassType() {
		return this.classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getCourseId() {
		return this.courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
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

	public String getEegroup() {
		return this.eegroup;
	}

	public void setEegroup(String eegroup) {
		this.eegroup = eegroup;
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

	public BigDecimal getIsFinish() {
		return this.isFinish;
	}

	public void setIsFinish(BigDecimal isFinish) {
		this.isFinish = isFinish;
	}

	public String getJbny() {
		return this.jbny;
	}

	public void setJbny(String jbny) {
		this.jbny = jbny;
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

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPycc() {
		return this.pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public String getSpecialtyId() {
		return this.specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public String getSupervisorId() {
		return this.supervisorId;
	}

	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}

	public String getSyncStatus() {
		return this.syncStatus;
	}

	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
	}

	public String getTeachPlanId() {
		return this.teachPlanId;
	}

	public void setTeachPlanId(String teachPlanId) {
		this.teachPlanId = teachPlanId;
	}

	public String getTermId() {
		return this.termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
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

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getXxzxId() {
		return this.xxzxId;
	}

	public void setXxzxId(String xxzxId) {
		this.xxzxId = xxzxId;
	}

	public BigDecimal getXz() {
		return this.xz;
	}

	public void setXz(BigDecimal xz) {
		this.xz = xz;
	}

	/**
	 * get 临时数据：班级学员数
	 * 
	 * @return the colStudentNum
	 */
	public Long getColStudentNum() {
		return colStudentNum;
	}

	/**
	 * set 临时数据：班级学员数
	 * 
	 * @param colStudentNum
	 *            the colStudentNum to set
	 */
	public void setColStudentNum(Long colStudentNum) {
		this.colStudentNum = colStudentNum;
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
		gjtClassStudentList.setGjtClassInfo(this);

		return gjtClassStudentList;
	}

	public BzrGjtClassStudent removeGjtClassStudentList(BzrGjtClassStudent gjtClassStudentList) {
		getGjtClassStudentList().remove(gjtClassStudentList);
		gjtClassStudentList.setGjtClassInfo(null);

		return gjtClassStudentList;
	}

	public BzrGjtOrg getGjtOrg() {
		return gjtOrg;
	}

	public void setGjtOrg(BzrGjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}
}