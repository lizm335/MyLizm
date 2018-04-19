package com.gzedu.xlims.pojo.exam;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Where;

import com.gzedu.xlims.pojo.GjtStudentInfo;

import java.util.Date;


/**
 * The persistent class for the GJT_EXAM_POINT_APPOINTMENT_NEW database table.
 * 
 */
@Entity
@Table(name="GJT_EXAM_POINT_APPOINTMENT_NEW")
@NamedQuery(name="GjtExamPointAppointmentNew.findAll", query="SELECT g FROM GjtExamPointAppointmentNew g")
public class GjtExamPointAppointmentNew implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="EXAM_POINT_ID")
	private String examPointId;
	
	@ManyToOne
	@JoinColumn(name = "EXAM_POINT_ID", insertable = false, updatable = false)
	private GjtExamPointNew gjtExamPointNew;

	@Column(name="IS_DELETED", insertable = false)
	@Where(clause = "is_deleted=0")
	private int isDeleted;

	@Column(name="STUDENT_ID")
	private String studentId;

	@ManyToOne
	@JoinColumn(name = "STUDENT_ID", insertable = false, updatable = false)
	private GjtStudentInfo gjtStudentInfo;

	@Column(name="STUDY_YEAR_ID")
	private String studyYearId;

	@Column(name="GRADE_ID")
	private String gradeId;

	@Column(name="UPDATED_BY")
	private String updatedBy;
	
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name = "EXAM_BATCH_CODE")
	private String examBatchCode;

	public GjtExamPointAppointmentNew() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getExamPointId() {
		return this.examPointId;
	}

	public void setExamPointId(String examPointId) {
		this.examPointId = examPointId;
	}

	public GjtExamPointNew getGjtExamPointNew() {
		return gjtExamPointNew;
	}

	public void setGjtExamPointNew(GjtExamPointNew gjtExamPointNew) {
		this.gjtExamPointNew = gjtExamPointNew;
	}

	public int getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public String getStudyYearId() {
		return this.studyYearId;
	}

	public void setStudyYearId(String studyYearId) {
		this.studyYearId = studyYearId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getExamBatchCode() {
		return examBatchCode;
	}

	public void setExamBatchCode(String examBatchCode) {
		this.examBatchCode = examBatchCode;
	}
}