package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import com.gzedu.xlims.pojo.exam.GjtExamSubjectNew;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;

/**
 * The persistent class for the GJT_GRADE_SPECIALTY_PLAN database table.
 * 
 */
@Entity
@Table(name = "GJT_GRADE_SPECIALTY_PLAN")
@NamedQuery(name = "GjtGradeSpecialtyPlan.findAll", query = "SELECT g FROM GjtGradeSpecialtyPlan g")
public class GjtGradeSpecialtyPlan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "STUDY_YEAR_ID")
	private String studyYearId;// 学年度ID

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_YEAR_ID", insertable = false, updatable = false)
	private GjtStudyYearInfo gjtStudyYearInfo;

	@Column(name = "STUDYYEAR_COURSE_ID")
	private String studyYearCourseId;// 学年度课程ID（中间表ID）

	@Column(name = "STUDY_YEAR_CODE")
	private int studyYearCode;// 规则请看 StudyYear

	@Column(name = "GRADE_ID")
	private String gradeId;// 年级ID

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE_ID", insertable = false, updatable = false)
	private GjtGrade gjtGrade;

	@Column(name = "SPECIALTY_ID")
	private String specialtyId;// 专业

	@Column(name = "COUNSELOR_ID")
	private String counselorId;// 辅导老师

	@Column(name = "COURSE_CATEGORY")
	private int courseCategory;// 课程属性,0必修，１选修

	@Column(name = "COURSE_ID")
	private String courseId;// 课程

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID", insertable = false, updatable = false)
	private GjtCourse gjtCourse;

	@Column(name = "COURSE_TYPE_ID")
	private String courseTypeId;// 课程类别

	@Column(name = "EXAM_TYPE")
	private int examType;// 考试方式,0网考，１场考

	@Column(name = "HOURS")
	private int hours;// 学时

	private int score;// 学分

	@Column(name = "STUDY_RATIO")
	private int studyRatio;// 学习占比（１００分制）如：30
	@Column(name = "EXAM_RATIO")
	private int examRatio;// 考试占比（１００分制）如：70

	@Column(name = "TERM_TYPE_CODE")
	private int termTypeCode;// 学期

	@Column(name = "XX_ID")
	private String xxId;// 学校

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Temporal(TemporalType.DATE)
	@Column(name = "NETWORKTEST_END_DATE") // 网考结束时间
	private Date networktestEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "NETWORKTEST_START_DATE") // 网考开始时间
	private Date networktestStartDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "STUDY_END_DATE") // 学习结束时间
	private Date studyEndDate;

	@Temporal(TemporalType.DATE) // 学习开始时间
	@Column(name = "STUDY_START_DATE")
	private Date studyStartDate;

	@Column(name = "INSTRUCTIONS")
	private String instructions;

	@OneToMany(mappedBy = "gjtGradeSpecialtyPlanBook", cascade = CascadeType.ALL)
	private List<GjtGradeSpecialtyPlanBook> gjtGradeSpecialtyPlanBooks;// 教材中间表集合

	@ManyToMany
	@JoinTable(name = "GJT_GRADE_SPECIALTY_PLAN_BOOK", joinColumns = {
			@JoinColumn(name = "GRADE_SPECIALTY_PLAN_ID") }, inverseJoinColumns = { @JoinColumn(name = "TEXTBOOK_ID") })
	@Where(clause = "TEXTBOOK_TYPE = 1")
	private List<GjtTextbook> gjtTextbookList1;// 主教材

	@ManyToMany
	@JoinTable(name = "GJT_GRADE_SPECIALTY_PLAN_BOOK", joinColumns = {
			@JoinColumn(name = "GRADE_SPECIALTY_PLAN_ID") }, inverseJoinColumns = { @JoinColumn(name = "TEXTBOOK_ID") })
	@Where(clause = "TEXTBOOK_TYPE = 2")
	private List<GjtTextbook> gjtTextbookList2;// 复习资料

	@Column(name = "SUBJECT_ID")
	private String subjectId;
	@ManyToOne
	@JoinColumn(name = "SUBJECT_ID", insertable = false, updatable = false)
	private GjtExamSubjectNew gjtExamSubjectNew;

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public GjtExamSubjectNew getGjtExamSubjectNew() {
		return gjtExamSubjectNew;
	}

	public void setGjtExamSubjectNew(GjtExamSubjectNew gjtExamSubjectNew) {
		this.gjtExamSubjectNew = gjtExamSubjectNew;
	}

	public GjtGradeSpecialtyPlan() {
		super();
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

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public Date getNetworktestEndDate() {
		return networktestEndDate;
	}

	public void setNetworktestEndDate(Date networktestEndDate) {
		this.networktestEndDate = networktestEndDate;
	}

	public Date getNetworktestStartDate() {
		return networktestStartDate;
	}

	public void setNetworktestStartDate(Date networktestStartDate) {
		this.networktestStartDate = networktestStartDate;
	}

	public Date getStudyEndDate() {
		return studyEndDate;
	}

	public void setStudyEndDate(Date studyEndDate) {
		this.studyEndDate = studyEndDate;
	}

	public Date getStudyStartDate() {
		return studyStartDate;
	}

	public void setStudyStartDate(Date studyStartDate) {
		this.studyStartDate = studyStartDate;
	}

	public int getStudyYearCode() {
		return studyYearCode;
	}

	public void setStudyYearCode(int studyYearCode) {
		this.studyYearCode = studyYearCode;
	}

	public String getStudyYearId() {
		return studyYearId;
	}

	public void setStudyYearId(String studyYearId) {
		this.studyYearId = studyYearId;
	}

	public String getStudyYearCourseId() {
		return studyYearCourseId;
	}

	public void setStudyYearCourseId(String studyYearCourseId) {
		this.studyYearCourseId = studyYearCourseId;
	}

	public GjtCourse getGjtCourse() {
		return gjtCourse;
	}

	public void setGjtCourse(GjtCourse gjtCourse) {
		this.gjtCourse = gjtCourse;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public GjtGrade getGjtGrade() {
		return gjtGrade;
	}

	public void setGjtGrade(GjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	// public List<GjtGradeSpecialtyPlanBook> getGjtGradeSpecialtyPlanBooks() {
	// return gjtGradeSpecialtyPlanBooks;
	// }
	//
	// public void setGjtGradeSpecialtyPlanBooks(List<GjtGradeSpecialtyPlanBook>
	// gjtGradeSpecialtyPlanBooks) {
	// this.gjtGradeSpecialtyPlanBooks = gjtGradeSpecialtyPlanBooks;
	// }

	public GjtStudyYearInfo getGjtStudyYearInfo() {
		return gjtStudyYearInfo;
	}

	public void setGjtStudyYearInfo(GjtStudyYearInfo gjtStudyYearInfo) {
		this.gjtStudyYearInfo = gjtStudyYearInfo;
	}

	public List<GjtTextbook> getGjtTextbookList1() {
		return gjtTextbookList1;
	}

	public void setGjtTextbookList1(List<GjtTextbook> gjtTextbookList1) {
		this.gjtTextbookList1 = gjtTextbookList1;
	}

	public List<GjtTextbook> getGjtTextbookList2() {
		return gjtTextbookList2;
	}

	public void setGjtTextbookList2(List<GjtTextbook> gjtTextbookList2) {
		this.gjtTextbookList2 = gjtTextbookList2;
	}

	public List<GjtGradeSpecialtyPlanBook> getGjtGradeSpecialtyPlanBooks() {
		return gjtGradeSpecialtyPlanBooks;
	}

	public void setGjtGradeSpecialtyPlanBooks(List<GjtGradeSpecialtyPlanBook> gjtGradeSpecialtyPlanBooks) {
		this.gjtGradeSpecialtyPlanBooks = gjtGradeSpecialtyPlanBooks;
	}

}