package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbook;

/**
 * The persistent class for the GJT_SPECIALTY_PLAN database table. 专业的教学计划
 */
@Entity
@Table(name = "GJT_SPECIALTY_PLAN")
// @NamedQuery(name = "GjtSpecialtyPlan.findAll", query = "SELECT g FROM GjtSpecialtyPlan g")
@Deprecated public class BzrGjtSpecialtyPlan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "SPECIALTY_ID")
	private String specialtyId;// 专业

	@Column(name = "COUNSELOR_ID")
	private String counselorId;// 辅导老师

	@Column(name = "COURSE_CATEGORY")
	private int courseCategory = 0;// 课程属性,0必修，1选修

	@Column(name = "COURSE_TYPE")
	private int coursetype = 0;// 课程类型,0统设，1非统设

	@Column(name = "EXAM_UNIT")
	private int examUnit = 1;// 考试单位:1-省, 2-中央

	@Column(name = "COURSE_ID")
	private String courseId;// 课程

	@Column(name = "REPLACE_COURSE_ID")
	private String replaceCourseId;// 替换课程ID

	@Column(name = "COURSE_TYPE_ID") // 0:公共基础课 1专业课 2公共拓展课
	private String courseTypeId;// 课程类别

	@Column(name = "EXAM_TYPE")
	private int examType = 0;// 考试方式,0网考，１场考

	@Column(name = "HOURS")
	private int hours = 0;// 学时

	private int score = 0;// 学分

	@Column(name = "STUDY_RATIO")
	private int studyRatio = 0;// 学习占比（１００分制）如：30
	@Column(name = "EXAM_RATIO")
	private int examRatio = 0;// 考试占比（１００分制）如：70

	@Column(name = "INSTRUCTIONS")
	private String instructions;

	@Column(name = "TERM_TYPE_CODE")
	private int termTypeCode;// 学期

	@Column(name = "XX_ID")
	private String xxId;// 学校

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "SUBJECT_ID")
	private String subjectId;

	@ManyToOne
	@JoinColumn(name = "COURSE_ID", insertable = false, updatable = false)
	private BzrGjtCourse gjtCourse;
	@ManyToOne
	@JoinColumn(name = "SUBJECT_ID", insertable = false, updatable = false)
	private BzrGjtExamSubjectNew gjtExamSubjectNew;

	@ManyToOne
	@JoinColumn(name = "REPLACE_COURSE_ID", referencedColumnName = "COURSE_ID", insertable = false, updatable = false)
	private BzrGjtCourse gjtReplaceCourse;

	@ManyToMany
	@JoinTable(name = "GJT_TEXTBOOK_PLAN_OWNERSHIP", joinColumns = {
			@JoinColumn(name = "SPECIALTY_PLAN_ID") }, inverseJoinColumns = { @JoinColumn(name = "TEXTBOOK_ID") })
	private List<BzrGjtTextbook> gjtTextbookList;

	@ManyToMany
	@JoinTable(name = "GJT_TEXTBOOK_PLAN_OWNERSHIP", joinColumns = {
			@JoinColumn(name = "SPECIALTY_PLAN_ID") }, inverseJoinColumns = { @JoinColumn(name = "TEXTBOOK_ID") })
	@Where(clause = "textbook_type = 1")
	private List<BzrGjtTextbook> gjtTextbookList1;// 主教材

	@ManyToMany
	@JoinTable(name = "GJT_TEXTBOOK_PLAN_OWNERSHIP", joinColumns = {
			@JoinColumn(name = "SPECIALTY_PLAN_ID") }, inverseJoinColumns = { @JoinColumn(name = "TEXTBOOK_ID") })
	@Where(clause = "textbook_type = 2")
	private List<BzrGjtTextbook> gjtTextbookList2;// 复习资料

	@OneToMany
	@JoinColumn(name = "SPECIALTY_PLAN_ID", referencedColumnName = "id")
	private List<BzrGjtTextbookPlanOwnership> gjtTextbookPlanOwnershipList;

	public BzrGjtExamSubjectNew getGjtExamSubjectNew() {
		return gjtExamSubjectNew;
	}

	public void setGjtExamSubjectNew(BzrGjtExamSubjectNew gjtExamSubjectNew) {
		this.gjtExamSubjectNew = gjtExamSubjectNew;
	}

	public List<BzrGjtTextbookPlanOwnership> getGjtTextbookPlanOwnershipList() {
		return gjtTextbookPlanOwnershipList;
	}

	public void setGjtTextbookPlanOwnershipList(List<BzrGjtTextbookPlanOwnership> gjtTextbookPlanOwnershipList) {
		this.gjtTextbookPlanOwnershipList = gjtTextbookPlanOwnershipList;
	}

	public List<BzrGjtTextbook> getGjtTextbookList() {
		return gjtTextbookList;
	}

	public void setGjtTextbookList(List<BzrGjtTextbook> gjtTextbookList) {
		this.gjtTextbookList = gjtTextbookList;
	}

	public BzrGjtCourse getGjtCourse() {
		return gjtCourse;
	}

	/**
	 * @param gjtCourse
	 *            the gjtCourse to set
	 */
	public void setGjtCourse(BzrGjtCourse gjtCourse) {
		this.gjtCourse = gjtCourse;
	}

	public BzrGjtSpecialtyPlan() {
	}

	public String getCounselorId() {
		return this.counselorId;
	}

	public void setCounselorId(String counselorId) {
		this.counselorId = counselorId;
	}

	public int getCourseCategory() {
		return courseCategory;
	}

	public void setCourseCategory(int courseCategory) {
		this.courseCategory = courseCategory;
	}

	public int getCoursetype() {
		return coursetype;
	}

	public void setCoursetype(int coursetype) {
		this.coursetype = coursetype;
	}

	public int getExamUnit() {
		return examUnit;
	}

	public void setExamUnit(int examUnit) {
		this.examUnit = examUnit;
	}

	public int getExamRatio() {
		return examRatio;
	}

	public void setExamRatio(int examRatio) {
		this.examRatio = examRatio;
	}

	public int getExamType() {
		return examType;
	}

	public void setExamType(int examType) {
		this.examType = examType;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getStudyRatio() {
		return studyRatio;
	}

	public void setStudyRatio(int studyRatio) {
		this.studyRatio = studyRatio;
	}

	public String getCourseId() {
		return this.courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseTypeId() {
		return this.courseTypeId;
	}

	public void setCourseTypeId(String courseTypeId) {
		this.courseTypeId = courseTypeId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSpecialtyId() {
		return this.specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public int getTermTypeCode() {
		return termTypeCode;
	}

	public void setTermTypeCode(int termTypeCode) {
		this.termTypeCode = termTypeCode;
	}

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
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

	public String getReplaceCourseId() {
		return replaceCourseId;
	}

	public void setReplaceCourseId(String replaceCourseId) {
		this.replaceCourseId = replaceCourseId;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public BzrGjtCourse getGjtReplaceCourse() {
		return gjtReplaceCourse;
	}

	public void setGjtReplaceCourse(BzrGjtCourse gjtReplaceCourse) {
		this.gjtReplaceCourse = gjtReplaceCourse;
	}

	public List<BzrGjtTextbook> getGjtTextbookList1() {
		return gjtTextbookList1;
	}

	public void setGjtTextbookList1(List<BzrGjtTextbook> gjtTextbookList1) {
		this.gjtTextbookList1 = gjtTextbookList1;
	}

	public List<BzrGjtTextbook> getGjtTextbookList2() {
		return gjtTextbookList2;
	}

	public void setGjtTextbookList2(List<BzrGjtTextbook> gjtTextbookList2) {
		this.gjtTextbookList2 = gjtTextbookList2;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

}