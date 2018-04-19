package com.ouchgzee.headTeacher.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

/**
 * 年级信息实体类<br>
 * The persistent class for the GJT_GRADE database table.
 * 
 */
@Entity
@Table(name = "GJT_GRADE")
// @NamedQuery(name = "GjtGrade.findAll", query = "SELECT g FROM GjtGrade g")
@Deprecated public class BzrGjtGrade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "GRADE_ID")
	private String gradeId;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTER_DT")
	private Date enterDt;

	@Column(name = "GRADE_CODE")
	private String gradeCode;

	@Column(name = "GRADE_NAME")
	private String gradeName;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	@Column(name = "SPACE_MONTH")
	private BigDecimal spaceMonth;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@Column(name = "XX_ID")
	private String xxId;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE) // 学期开始时间
	@Column(name = "START_DATE")
	private Date startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE) // 学期结束时间
	@Column(name = "END_DATE")
	private Date endDate;

	@Temporal(TemporalType.DATE) // 招生计划开始时间
	@Column(name = "ENROLL_START_DATE")
	private Date enrollStartDate;

	@Temporal(TemporalType.DATE) // 招生计划结束时间
	@Column(name = "ENROLL_END_DATE")
	private Date enrollEndDate;

	@Column(name = "ENROLL_RESPONSIBLE") // 招生计划责任人岗位
	private String enrollResponsible;

	@Temporal(TemporalType.DATE) // 学籍计划开始时间
	@Column(name = "SCHOOLROLL_START_DATE")
	private Date schoolrollStartDate;

	@Temporal(TemporalType.DATE) // 学籍计划结束时间
	@Column(name = "SCHOOLROLL_END_DATE")
	private Date schoolrollEndDate;

	@Column(name = "SCHOOLROLL_RESPONSIBLE") // 学籍计划责任人岗位
	private String schoolrollResponsible;

	@Temporal(TemporalType.DATE) // 教务计划开始时间
	@Column(name = "EDUCATIONAL_START_DATE")
	private Date educationalStartDate;

	@Temporal(TemporalType.DATE) // 教务计划结束时间
	@Column(name = "EDUCATIONAL_END_DATE")
	private Date educationalEndDate;

	@Column(name = "EDUCATIONAL_RESPONSIBLE") // 教务计划责任人岗位
	private String educationalResponsible;

	@Temporal(TemporalType.DATE) // 教学计划开始时间
	@Column(name = "TEACHING_START_DATE")
	private Date teachingStartDate;

	@Temporal(TemporalType.DATE) // 教学计划结束时间
	@Column(name = "TEACHING_END_DATE")
	private Date teachingEndDate;

	@Column(name = "TEACHING_RESPONSIBLE") // 教学计划责任人岗位
	private String teachingResponsible;

	@Temporal(TemporalType.DATE) // 考试计划开始时间
	@Column(name = "EXAM_START_DATE")
	private Date examStartDate;

	@Temporal(TemporalType.DATE) // 考试计划结束时间
	@Column(name = "EXAM_END_DATE")
	private Date examEndDate;

	@Column(name = "EXAM_RESPONSIBLE") // 考试计划责任人岗位
	private String examResponsible;

	@Temporal(TemporalType.DATE) // 毕业计划开始时间
	@Column(name = "GRADUATION_START_DATE")
	private Date graduationStartDate;

	@Temporal(TemporalType.DATE) // 毕业计划结束时间
	@Column(name = "GRADUATION_END_DATE")
	private Date graduationEndDate;

	@Column(name = "GRADUATION_RESPONSIBLE") // 毕业计划责任人岗位
	private String graduationResponsible;

	@Temporal(TemporalType.DATE) // 开课计划开始时间
	@Column(name = "COURSE_START_DATE")
	private Date courseStartDate;

	@Temporal(TemporalType.DATE) // 开课计划结束时间
	@Column(name = "COURSE_END_DATE")
	private Date courseEndDate;

	@Column(name = "COURSE_RESPONSIBLE") // 开课计划责任人岗位
	private String courseResponsible;

	@Temporal(TemporalType.DATE)
	private Date payStartDate;// 老生缴费开始时间
	@Temporal(TemporalType.DATE)
	private Date payEndDate;// 老生缴费结束时间
	@Temporal(TemporalType.DATE)
	private Date oldStudentEnterDate;// 老生入学时间
	@Temporal(TemporalType.DATE)
	private Date newStudentEnterDate;// 新生入学时间

	private String enrollResponsiblePer;// 招生计划负责人
	private String schoolrollResponsiblePer;// 学籍计划负责人
	private String educationalResponsiblePer;// 教务计划负责人
	private String teachingResponsiblePer;// 教学计划负责人
	private String examResponsiblePer;// 考试计划负责人
	private String graduationResponsiblePer;// 毕业计划负责人

	@ManyToOne
	@JoinColumn(name = "YEAR_ID")
	private BzrGjtYear gjtYear;

	public BzrGjtGrade() {
	}

	public String getGradeId() {
		return this.gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
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

	public Date getEnterDt() {
		return this.enterDt;
	}

	public void setEnterDt(Date enterDt) {
		this.enterDt = enterDt;
	}

	public String getGradeCode() {
		return this.gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getGradeName() {
		return this.gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
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

	public BigDecimal getSpaceMonth() {
		return this.spaceMonth;
	}

	public void setSpaceMonth(BigDecimal spaceMonth) {
		this.spaceMonth = spaceMonth;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEnrollStartDate() {
		return enrollStartDate;
	}

	public void setEnrollStartDate(Date enrollStartDate) {
		this.enrollStartDate = enrollStartDate;
	}

	public Date getEnrollEndDate() {
		return enrollEndDate;
	}

	public void setEnrollEndDate(Date enrollEndDate) {
		this.enrollEndDate = enrollEndDate;
	}

	public String getEnrollResponsible() {
		return enrollResponsible;
	}

	public void setEnrollResponsible(String enrollResponsible) {
		this.enrollResponsible = enrollResponsible;
	}

	public Date getSchoolrollStartDate() {
		return schoolrollStartDate;
	}

	public void setSchoolrollStartDate(Date schoolrollStartDate) {
		this.schoolrollStartDate = schoolrollStartDate;
	}

	public Date getSchoolrollEndDate() {
		return schoolrollEndDate;
	}

	public void setSchoolrollEndDate(Date schoolrollEndDate) {
		this.schoolrollEndDate = schoolrollEndDate;
	}

	public String getSchoolrollResponsible() {
		return schoolrollResponsible;
	}

	public void setSchoolrollResponsible(String schoolrollResponsible) {
		this.schoolrollResponsible = schoolrollResponsible;
	}

	public Date getEducationalStartDate() {
		return educationalStartDate;
	}

	public void setEducationalStartDate(Date educationalStartDate) {
		this.educationalStartDate = educationalStartDate;
	}

	public Date getEducationalEndDate() {
		return educationalEndDate;
	}

	public void setEducationalEndDate(Date educationalEndDate) {
		this.educationalEndDate = educationalEndDate;
	}

	public String getEducationalResponsible() {
		return educationalResponsible;
	}

	public void setEducationalResponsible(String educationalResponsible) {
		this.educationalResponsible = educationalResponsible;
	}

	public Date getTeachingStartDate() {
		return teachingStartDate;
	}

	public void setTeachingStartDate(Date teachingStartDate) {
		this.teachingStartDate = teachingStartDate;
	}

	public Date getTeachingEndDate() {
		return teachingEndDate;
	}

	public void setTeachingEndDate(Date teachingEndDate) {
		this.teachingEndDate = teachingEndDate;
	}

	public String getTeachingResponsible() {
		return teachingResponsible;
	}

	public void setTeachingResponsible(String teachingResponsible) {
		this.teachingResponsible = teachingResponsible;
	}

	public Date getExamStartDate() {
		return examStartDate;
	}

	public void setExamStartDate(Date examStartDate) {
		this.examStartDate = examStartDate;
	}

	public Date getExamEndDate() {
		return examEndDate;
	}

	public void setExamEndDate(Date examEndDate) {
		this.examEndDate = examEndDate;
	}

	public String getExamResponsible() {
		return examResponsible;
	}

	public void setExamResponsible(String examResponsible) {
		this.examResponsible = examResponsible;
	}

	public Date getGraduationStartDate() {
		return graduationStartDate;
	}

	public void setGraduationStartDate(Date graduationStartDate) {
		this.graduationStartDate = graduationStartDate;
	}

	public Date getGraduationEndDate() {
		return graduationEndDate;
	}

	public void setGraduationEndDate(Date graduationEndDate) {
		this.graduationEndDate = graduationEndDate;
	}

	public String getGraduationResponsible() {
		return graduationResponsible;
	}

	public void setGraduationResponsible(String graduationResponsible) {
		this.graduationResponsible = graduationResponsible;
	}

	public Date getCourseStartDate() {
		return courseStartDate;
	}

	public void setCourseStartDate(Date courseStartDate) {
		this.courseStartDate = courseStartDate;
	}

	public Date getCourseEndDate() {
		return courseEndDate;
	}

	public void setCourseEndDate(Date courseEndDate) {
		this.courseEndDate = courseEndDate;
	}

	public String getCourseResponsible() {
		return courseResponsible;
	}

	public void setCourseResponsible(String courseResponsible) {
		this.courseResponsible = courseResponsible;
	}

	public Date getPayStartDate() {
		return payStartDate;
	}

	public void setPayStartDate(Date payStartDate) {
		this.payStartDate = payStartDate;
	}

	public Date getPayEndDate() {
		return payEndDate;
	}

	public void setPayEndDate(Date payEndDate) {
		this.payEndDate = payEndDate;
	}

	public Date getOldStudentEnterDate() {
		return oldStudentEnterDate;
	}

	public void setOldStudentEnterDate(Date oldStudentEnterDate) {
		this.oldStudentEnterDate = oldStudentEnterDate;
	}

	public Date getNewStudentEnterDate() {
		return newStudentEnterDate;
	}

	public void setNewStudentEnterDate(Date newStudentEnterDate) {
		this.newStudentEnterDate = newStudentEnterDate;
	}

	public String getEnrollResponsiblePer() {
		return enrollResponsiblePer;
	}

	public void setEnrollResponsiblePer(String enrollResponsiblePer) {
		this.enrollResponsiblePer = enrollResponsiblePer;
	}

	public String getSchoolrollResponsiblePer() {
		return schoolrollResponsiblePer;
	}

	public void setSchoolrollResponsiblePer(String schoolrollResponsiblePer) {
		this.schoolrollResponsiblePer = schoolrollResponsiblePer;
	}

	public String getEducationalResponsiblePer() {
		return educationalResponsiblePer;
	}

	public void setEducationalResponsiblePer(String educationalResponsiblePer) {
		this.educationalResponsiblePer = educationalResponsiblePer;
	}

	public String getTeachingResponsiblePer() {
		return teachingResponsiblePer;
	}

	public void setTeachingResponsiblePer(String teachingResponsiblePer) {
		this.teachingResponsiblePer = teachingResponsiblePer;
	}

	public String getExamResponsiblePer() {
		return examResponsiblePer;
	}

	public void setExamResponsiblePer(String examResponsiblePer) {
		this.examResponsiblePer = examResponsiblePer;
	}

	public String getGraduationResponsiblePer() {
		return graduationResponsiblePer;
	}

	public void setGraduationResponsiblePer(String graduationResponsiblePer) {
		this.graduationResponsiblePer = graduationResponsiblePer;
	}

	public BzrGjtYear getGjtYear() {
		return gjtYear;
	}

	public void setGjtYear(BzrGjtYear gjtYear) {
		this.gjtYear = gjtYear;
	}
}