package com.gzedu.xlims.pojo.exam;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the VIEW_EXAM_PLAN_SC database table.
 * 
 */
@Entity
@Table(name="VIEW_EXAM_PLAN_SC")
@NamedQuery(name="ViewExamPlanSc.findAll", query="SELECT v FROM ViewExamPlanSc v")
public class ViewExamPlanSc implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name="COURSE_ID")
	private String courseId;

	@Column(name="EXAM_BATCH_CODE")
	private String examBatchCode;

	@Column(name="EXAM_BATCH_ID")
	private String examBatchId;

	@Id
	@Column(name = "EXAM_PLAN_ID", unique = false, nullable = false)
	private String examPlanId;

	@Column(name="SPECIALTY_ID")
	private String specialtyId;
	
	@Column(name="EXAM_PLAN_LIMIT")
	private String examPlanLimit;

	@Column(name="TYPE")
	private int type;

	@Column(name="XX_ID")
	private String xxId;

	public ViewExamPlanSc() {
	}

	public String getCourseId() {
		return this.courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getExamBatchCode() {
		return this.examBatchCode;
	}

	public void setExamBatchCode(String examBatchCode) {
		this.examBatchCode = examBatchCode;
	}

	public String getExamBatchId() {
		return this.examBatchId;
	}

	public void setExamBatchId(String examBatchId) {
		this.examBatchId = examBatchId;
	}

	public String getExamPlanId() {
		return this.examPlanId;
	}

	public void setExamPlanId(String examPlanId) {
		this.examPlanId = examPlanId;
	}

	public String getSpecialtyId() {
		return this.specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getExamPlanLimit() {
		return examPlanLimit;
	}

	public void setExamPlanLimit(String examPlanLimit) {
		this.examPlanLimit = examPlanLimit;
	}
	
}