package com.gzedu.xlims.pojo.thesis;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the GJT_THESIS_STUDENT_PROG database table.
 * 
 */
@Entity
@Table(name="GJT_THESIS_STUDENT_PROG")
@NamedQuery(name="GjtThesisStudentProg.findAll", query="SELECT g FROM GjtThesisStudentProg g")
public class GjtThesisStudentProg implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PROGRESS_ID")
	private String progressId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="PROGRESS_CODE")
	private String progressCode;

	@Column(name="STUDENT_ID")
	private String studentId;

	@Column(name="THESIS_PLAN_ID")
	private String thesisPlanId;

	public GjtThesisStudentProg() {
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

	public String getThesisPlanId() {
		return this.thesisPlanId;
	}

	public void setThesisPlanId(String thesisPlanId) {
		this.thesisPlanId = thesisPlanId;
	}

}