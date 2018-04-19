package com.ouchgzee.headTeacher.pojo.graduation;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 学员个人进度表
 * 
 */
@Entity
@Table(name="GJT_GRADUATION_STUDENT_PROG")
// @NamedQuery(name="GjtGraduationStudentProg.findAll", query="SELECT g FROM GjtGraduationStudentProg g")
@Deprecated public class BzrGjtGraduationStudentProg implements Serializable {
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

	@Column(name="PROGRESS_TYPE")
	private int progressType;

	@Column(name="STUDENT_ID")
	private String studentId;

	//bi-directional many-to-one association to GjtGraduationBatch
	@ManyToOne
	@JoinColumn(name="BATCH_ID")
	private BzrGjtGraduationBatch gjtGraduationBatch;

	public BzrGjtGraduationStudentProg() {
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

	public int getProgressType() {
		return this.progressType;
	}

	public void setProgressType(int progressType) {
		this.progressType = progressType;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public BzrGjtGraduationBatch getGjtGraduationBatch() {
		return this.gjtGraduationBatch;
	}

	public void setGjtGraduationBatch(BzrGjtGraduationBatch gjtGraduationBatch) {
		this.gjtGraduationBatch = gjtGraduationBatch;
	}

}