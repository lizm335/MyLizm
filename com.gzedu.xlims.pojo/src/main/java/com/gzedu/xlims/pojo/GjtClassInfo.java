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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.pojo.system.StudyYear;

/**
 * The persistent class for the GJT_CLASS_INFO database table. 班级表
 * 
 */
@Entity
@Table(name = "GJT_CLASS_INFO")
@NamedQuery(name = "GjtClassInfo.findAll", query = "SELECT g FROM GjtClassInfo g")
public class GjtClassInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param plan
	 *            教学计划
	 * @param course
	 *            课程
	 * @param bh
	 *            班级编号
	 */
	public GjtClassInfo(GjtGradeSpecialtyPlan plan, GjtCourse course, int bh) {
		this.classId = UUIDUtils.random();
		this.gjtCourse = course;
		this.studyYearCode = plan.getStudyYearCode();
		this.classType = "course";
		this.gjtOrg = new GjtOrg(plan.getXxId());
		this.gjtSchoolInfo = new GjtSchoolInfo(plan.getXxId());
		this.bh = bh + "班";
		// 命名规则： 课程名+学年度+序号 例如： 职业英语（专）2016上学期01班
		this.bjmc = course.getKcmc() + StudyYear.getName(plan.getStudyYearCode()) + bh + "班";
		this.createdDt = new Date();
		this.createdBy = "教学计划自动生成";
	}

	@Id
	@Column(name = "CLASS_ID")
	private String classId; // 班级ID

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDYYEAR_COURSE_ID") // 学年度课程ID
	GjtStudyYearCourse studyYearCourse;// 学年度课程

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID", insertable = false, updatable = false) // 课程ID
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtCourse gjtCourse;

	@Column(name = "COURSE_ID")
	private String courseId;

	@Column(name = "STUDY_YEAR_CODE")
	private Integer studyYearCode;// 规则请看 StudyYear

	@ManyToOne
	@JoinColumn(name = "STUDY_YEAR_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtStudyYearInfo gjtStudyYearInfo;
	
	@ManyToMany
	@JoinTable(name = "GJT_CLASS_STUDENT", joinColumns = { @JoinColumn(name = "CLASS_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "STUDENT_ID") })
	List<GjtStudentInfo> gjtStudentInfos;// 拥有的学生列表

	private String bjmc;// 班级名称(学年度课程名称N班)

	@Column(name = "CLASS_TYPE") // 班级类别 teach 学籍班 course 课程班
	private String classType;

	@Column(insertable = false)
	private String bjlx = "N"; // 默认为普通课程班级 班级类型 N 普通课程 Y体验章 S体验课

	private Double bjrn; // 建班年月

	private String bh; // 班号 默认为1班

	@Column(name = "BZ_ID") // 班长学生ID
	private String bzId;

	@OneToOne(fetch = FetchType.LAZY) // 班主任和辅导老师
	@JoinColumn(name = "BZR_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtEmployeeInfo gjtBzr;

	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	private String eegroup;

	@OneToOne(fetch = FetchType.LAZY) // 学期
	@JoinColumn(name = "GRADE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtGrade gjtGrade;

	@Column(name = "GRADE_ID", insertable = false, updatable = false)
	private String gradeId;

	@ManyToOne // 实际开班学期
	@JoinColumn(name = "ACTUAL_GRADE_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtGrade actualGrade;

	@Column(name = "ACTUAL_GRADE_ID")
	private String actualGradeId;

	@OneToOne(fetch = FetchType.LAZY) // 专业ID
	@JoinColumn(name = "SPECIALTY_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSpecialty gjtSpecialty;

	@Column(name = "SPECIALTY_ID")
	private String specialtyId;

	@Column(name = "IS_DELETED", insertable = false) // 是否删除
	@Where(clause = "isDeleted='N'")
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false) // 是否启用
	private String isEnabled;

	@Column(name = "IS_FINISH", insertable = false)
	private BigDecimal isFinish;

	private String jbny;

	private String memo;// 备注

	@Column(name = "ORG_CODE") // 机构编码
	private String orgCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtOrg gjtOrg;
	
	@Column(name = "ORG_ID", insertable = false, updatable = false)
	private String orgId;
	
	@ManyToOne(fetch = FetchType.LAZY) // 学院信息
	@JoinColumn(name = "XX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSchoolInfo gjtSchoolInfo;

	private String pycc;// 培养层次

	@Column(name = "TEACH_PLAN_ID") 
	private String teachPlanId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEACH_PLAN_ID", insertable = false, updatable = false) 
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtTeachPlan gjtTeachPlan;

	@OneToOne
	@JoinColumn(name = "SUPERVISOR_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtEmployeeInfo gjtDuDao; // 督导教师
	
	@Column(name = "SUPERVISOR_ID", insertable = false, updatable = false) 
	private String supervisorId;

	@Column(name = "SYNC_STATUS", insertable = false) // 是否同步
	private String syncStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TERM_ID") // 学期Id
	private GjtTermInfo gjtTermInfo;

	@Column(name = "UPDATED_BY") // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;// 版本

	@ManyToOne(fetch = FetchType.LAZY) // 学习中心
	@JoinColumn(name = "XXZX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtStudyCenter gjtStudyCenter;

	@Column(name = "XXZX_ID", insertable = false, updatable = false)
	private String xxzxId;


	@Column(name="LIMIT_NUM")
	private Integer limitNum; //限制人数

	@Column(name = "TERMCOURSE_ID")
	private String termcourseId; // 期课程ID

	private BigDecimal xz;

	@Transient
	private Integer num;

	@Transient
	private String scoreAvg;

	@Transient
	private String progressAvg;

	@Transient
	private Integer status;  //开班状态： 1-待开班， 2-开班中， 3-已结束

	@Transient
	private Long day;

	public String getScoreAvg() {
		return scoreAvg;
	}

	public void setScoreAvg(String scoreAvg) {
		this.scoreAvg = scoreAvg;
	}

	public String getProgressAvg() {
		return progressAvg;
	}

	public void setProgressAvg(String progressAvg) {
		this.progressAvg = progressAvg;
	}

	public GjtClassInfo() {
	}

	/**
	 * @param classId2
	 */
	public GjtClassInfo(String classId) {
		this.classId = classId;
	}

	/**
	 * @return the gjtTermInfo
	 */
	public GjtTermInfo getGjtTermInfo() {
		return gjtTermInfo;
	}

	/**
	 * @param gjtTermInfo
	 *            the gjtTermInfo to set
	 */
	public void setGjtTermInfo(GjtTermInfo gjtTermInfo) {
		this.gjtTermInfo = gjtTermInfo;
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

	public Double getBjrn() {
		return this.bjrn;
	}

	public void setBjrn(Double bjrn) {
		this.bjrn = bjrn;
	}

	public String getBzId() {
		return this.bzId;
	}

	public void setBzId(String bzId) {
		this.bzId = bzId;
	}

	/**
	 * @return the gjtBzr
	 */
	public GjtEmployeeInfo getGjtBzr() {
		return gjtBzr;
	}

	/**
	 * @param gjtBzr
	 *            the gjtBzr to set
	 */
	public void setGjtBzr(GjtEmployeeInfo gjtBzr) {
		this.gjtBzr = gjtBzr;
	}

	/**
	 * @return the num
	 */
	public Integer getNum() {
		return num;
	}

	/**
	 * @param num
	 *            the num to set
	 */
	public void setNum(Integer num) {
		this.num = num;
	}

	public String getClassType() {
		return this.classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getTeachPlanId() {
		return teachPlanId;
	}

	public void setTeachPlanId(String teachPlanId) {
		this.teachPlanId = teachPlanId;
	}

	public GjtTeachPlan getGjtTeachPlan() {
		return gjtTeachPlan;
	}

	public void setGjtTeachPlan(GjtTeachPlan gjtTeachPlan) {
		this.gjtTeachPlan = gjtTeachPlan;
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

	public GjtCourse getGjtCourse() {
		return gjtCourse;
	}

	public void setGjtCourse(GjtCourse gjtCourse) {
		this.gjtCourse = gjtCourse;
	}

	public GjtGrade getGjtGrade() {
		return gjtGrade;
	}

	public void setGjtGrade(GjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	public GjtGrade getActualGrade() {
		return actualGrade;
	}

	public void setActualGrade(GjtGrade actualGrade) {
		this.actualGrade = actualGrade;
	}

	public String getActualGradeId() {
		return actualGradeId;
	}

	public void setActualGradeId(String actualGradeId) {
		this.actualGradeId = actualGradeId;
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

	public String getPycc() {
		return this.pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public GjtEmployeeInfo getGjtDuDao() {
		return gjtDuDao;
	}

	public void setGjtDuDao(GjtEmployeeInfo gjtDuDao) {
		this.gjtDuDao = gjtDuDao;
	}

	public String getSupervisorId() {
		return supervisorId;
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

	public GjtStudyCenter getGjtStudyCenter() {
		return gjtStudyCenter;
	}

	public void setGjtStudyCenter(GjtStudyCenter gjtStudyCenter) {
		this.gjtStudyCenter = gjtStudyCenter;
	}

	public BigDecimal getXz() {
		return this.xz;
	}

	public void setXz(BigDecimal xz) {
		this.xz = xz;
	}

	public Integer getStudyYearCode() {
		return studyYearCode;
	}

	public void setStudyYearCode(Integer studyYearCode) {
		this.studyYearCode = studyYearCode;
	}

	public GjtOrg getGjtOrg() {
		return gjtOrg;
	}

	public void setGjtOrg(GjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}

	public GjtSchoolInfo getGjtSchoolInfo() {
		return gjtSchoolInfo;
	}

	public void setGjtSchoolInfo(GjtSchoolInfo gjtSchoolInfo) {
		this.gjtSchoolInfo = gjtSchoolInfo;
	}

	public GjtStudyYearCourse getStudyyearCourse() {
		return studyYearCourse;
	}

	public void setStudyyearCourse(GjtStudyYearCourse studyyearCourse) {
		this.studyYearCourse = studyyearCourse;
	}

	public GjtStudyYearCourse getStudyYearCourse() {
		return studyYearCourse;
	}

	public void setStudyYearCourse(GjtStudyYearCourse studyYearCourse) {
		this.studyYearCourse = studyYearCourse;
	}

	public List<GjtStudentInfo> getGjtStudentInfos() {
		return gjtStudentInfos;
	}

	public void setGjtStudentInfos(List<GjtStudentInfo> gjtStudentInfos) {
		this.gjtStudentInfos = gjtStudentInfos;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public GjtSpecialty getGjtSpecialty() {
		return gjtSpecialty;
	}

	public void setGjtSpecialty(GjtSpecialty gjtSpecialty) {
		this.gjtSpecialty = gjtSpecialty;
	}

	public String getSpecialtyId() {
		return specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public String getXxzxId() {
		return xxzxId;
	}

	public void setXxzxId(String xxzxId) {
		this.xxzxId = xxzxId;
	}

	public Integer getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(Integer limitNum) {
		this.limitNum = limitNum;
	}

	public GjtStudyYearInfo getGjtStudyYearInfo() {
		return gjtStudyYearInfo;
	}

	public void setGjtStudyYearInfo(GjtStudyYearInfo gjtStudyYearInfo) {
		this.gjtStudyYearInfo = gjtStudyYearInfo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getDay() {
		return day;
	}

	public void setDay(Long day) {
		this.day = day;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getTermcourseId() {
		return termcourseId;
	}

	public void setTermcourseId(String termcourseId) {
		this.termcourseId = termcourseId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}