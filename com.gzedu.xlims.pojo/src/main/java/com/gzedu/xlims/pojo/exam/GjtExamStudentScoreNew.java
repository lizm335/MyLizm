package com.gzedu.xlims.pojo.exam;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Where;

import com.gzedu.xlims.pojo.GjtStudyYearInfo;

import java.util.Date;


/**
 * The persistent class for the GJT_EXAM_STUDENT_SCORE_NEW database table.
 * 
 */
@Entity
@Table(name="GJT_EXAM_STUDENT_SCORE_NEW")
@NamedQuery(name="GjtExamStudentScoreNew.findAll", query="SELECT g FROM GjtExamStudentScoreNew g")
public class GjtExamStudentScoreNew implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EXAM_SCORE_ID")
	private String examScoreId;

	@Column(name="COURSE_ID")
	private String courseId;

	@Column(name="CREATED_BY")
	private String createdBy;

	
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="EXAM_SCORE")
	private String examScore;

	@Column(name="IS_DELETED")
	@Where(clause = "is_deleted=0")
	private int isDeleted;

	@Column(name="IS_UPLOAD")
	private int isUpload;

	private String proportion;

	private int status;

	@Column(name="STUDENT_ID")
	private String studentId;

	@Column(name="STUDY_SCORE")
	private String studyScore;

	@ManyToOne
	@JoinColumn(name = "STUDY_YEAR_ID", insertable=false, updatable=false)
	private GjtStudyYearInfo studyYearInfo;
	
	@Column(name="STUDY_YEAR_ID")
	private String studyYearId;
	
	@Column(name="TYPE")
	private int type;

	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	public GjtExamStudentScoreNew() {
	}

	public String getExamScoreId() {
		return this.examScoreId;
	}

	public void setExamScoreId(String examScoreId) {
		this.examScoreId = examScoreId;
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

	public String getExamScore() {
		return this.examScore;
	}

	public void setExamScore(String examScore) {
		this.examScore = examScore;
	}

	public int getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getIsUpload() {
		return this.isUpload;
	}

	public void setIsUpload(int isUpload) {
		this.isUpload = isUpload;
	}

	public String getProportion() {
		return this.proportion;
	}

	public void setProportion(String proportion) {
		this.proportion = proportion;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudyScore() {
		return this.studyScore;
	}

	public void setStudyScore(String studyScore) {
		this.studyScore = studyScore;
	}

	public String getStudyYearId() {
		return studyYearId;
	}

	public void setStudyYearId(String studyYearId) {
		this.studyYearId = studyYearId;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdateDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public GjtStudyYearInfo getStudyYearInfo() {
		return studyYearInfo;
	}

	public void setStudyYearInfo(GjtStudyYearInfo studyYearInfo) {
		this.studyYearInfo = studyYearInfo;
	}

}