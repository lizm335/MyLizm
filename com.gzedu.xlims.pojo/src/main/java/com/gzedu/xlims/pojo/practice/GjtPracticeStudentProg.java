package com.gzedu.xlims.pojo.practice;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the GJT_PRACTICE_STUDENT_PROG database table.
 * 
 */
@Entity
@Table(name="GJT_PRACTICE_STUDENT_PROG")
@NamedQuery(name="GjtPracticeStudentProg.findAll", query="SELECT g FROM GjtPracticeStudentProg g")
public class GjtPracticeStudentProg implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PROGRESS_ID")
	private String progressId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="PRACTICE_PLAN_ID")
	private String practicePlanId;

	@Column(name="PROGRESS_CODE")
	private String progressCode;

	@Column(name="STUDENT_ID")
	private String studentId;

	public GjtPracticeStudentProg() {
	}

	public String getProgressId() {
		return this.progressId;
	}

	public void setProgressId(String progressId) {
		this.progressId = progressId;
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

	public String getPracticePlanId() {
		return this.practicePlanId;
	}

	public void setPracticePlanId(String practicePlanId) {
		this.practicePlanId = practicePlanId;
	}

	public String getProgressCode() {
		return this.progressCode;
	}

	public void setProgressCode(String progressCode) {
		this.progressCode = progressCode;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

}