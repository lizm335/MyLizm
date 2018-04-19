package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * The persistent class for the GJT_STUDYYEAR_INFO database table.
 * 
 */
@Entity
@Table(name = "GJT_STUDYYEAR_INFO")
@NamedQuery(name = "GjtStudyYearInfo.findAll", query = "SELECT g FROM GjtStudyYearInfo g")
public class GjtStudyYearInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "study_year_code") // 所属学年度
	private int studyYearCode;

	@Column(name = "study_year_name") // 所属学年度名称
	private String studyYearName;

	@Temporal(TemporalType.DATE) // 学年度开始时间
	@Column(name = "STUDYYEAR_START_DATE")
	private Date studyyearStartDate;

	@Temporal(TemporalType.DATE) // 学年度结束时间
	@Column(name = "STUDYYEAR_END_DATE")
	private Date studyyearEndDate;

	@Temporal(TemporalType.DATE) // 招生计划开始时间
	@Column(name = "ENROLL_START_DATE")
	private Date enrollStartDate;

	@Temporal(TemporalType.DATE) // 招生计划结束时间
	@Column(name = "ENROLL_END_DATE")
	private Date enrollEndDate;

	@Column(name = "ENROLL_RESPONSIBLE")  //招生计划责任人岗位
	private String enrollResponsible;

	@Temporal(TemporalType.DATE) // 学籍计划开始时间
	@Column(name = "SCHOOLROLL_START_DATE")
	private Date schoolrollStartDate;

	@Temporal(TemporalType.DATE) // 学籍计划结束时间
	@Column(name = "SCHOOLROLL_END_DATE")
	private Date schoolrollEndDate;

	@Column(name = "SCHOOLROLL_RESPONSIBLE")  //学籍计划责任人岗位
	private String schoolrollResponsible;

	@Temporal(TemporalType.DATE) // 教务计划开始时间
	@Column(name = "EDUCATIONAL_START_DATE")
	private Date educationalStartDate;

	@Temporal(TemporalType.DATE) // 教务计划结束时间
	@Column(name = "EDUCATIONAL_END_DATE")
	private Date educationalEndDate;

	@Column(name = "EDUCATIONAL_RESPONSIBLE")  //教务计划责任人岗位
	private String educationalResponsible;

	@Temporal(TemporalType.DATE) // 教学计划开始时间
	@Column(name = "TEACHING_START_DATE")
	private Date teachingStartDate;

	@Temporal(TemporalType.DATE) // 教学计划结束时间
	@Column(name = "TEACHING_END_DATE")
	private Date teachingEndDate;

	@Column(name = "TEACHING_RESPONSIBLE")  //教学计划责任人岗位
	private String teachingResponsible;

	@Temporal(TemporalType.DATE) // 考试计划开始时间
	@Column(name = "EXAM_START_DATE")
	private Date examStartDate;

	@Temporal(TemporalType.DATE) // 考试计划结束时间
	@Column(name = "EXAM_END_DATE")
	private Date examEndDate;

	@Column(name = "EXAM_RESPONSIBLE")  //考试计划责任人岗位
	private String examResponsible;

	@Temporal(TemporalType.DATE) // 毕业计划开始时间
	@Column(name = "GRADUATION_START_DATE")
	private Date graduationStartDate;

	@Temporal(TemporalType.DATE) // 毕业计划结束时间
	@Column(name = "GRADUATION_END_DATE")
	private Date graduationEndDate;

	@Column(name = "GRADUATION_RESPONSIBLE")  //毕业计划责任人岗位
	private String graduationResponsible;

	@Temporal(TemporalType.DATE) // 开课计划开始时间
	@Column(name = "COURSE_START_DATE")
	private Date courseStartDate;

	@Temporal(TemporalType.DATE) // 开课计划结束时间
	@Column(name = "COURSE_END_DATE")
	private Date courseEndDate;

	@Column(name = "COURSE_RESPONSIBLE")  //开课计划责任人岗位
	private String courseResponsible;

	@Column(name = "CREATED_BY") // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建日期
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "UPDATED_BY") // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改日期
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "XX_ID") // 学院ID
	private String xxId;
	
	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Transient
	private Integer status;  //开班状态： 1-待开班， 2-开班中， 3-已结束

	@Transient
	private Long day;

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

	public GjtStudyYearInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getStudyYearCode() {
		return studyYearCode;
	}

	public void setStudyYearCode(int studyYearCode) {
		this.studyYearCode = studyYearCode;
	}

	public String getStudyYearName() {
		return studyYearName;
	}

	public void setStudyYearName(String studyYearName) {
		this.studyYearName = studyYearName;
	}

	public Date getStudyyearStartDate() {
		return studyyearStartDate;
	}

	public void setStudyyearStartDate(Date studyyearStartDate) {
		this.studyyearStartDate = studyyearStartDate;
	}

	public Date getStudyyearEndDate() {
		return studyyearEndDate;
	}

	public void setStudyyearEndDate(Date studyyearEndDate) {
		this.studyyearEndDate = studyyearEndDate;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}


}