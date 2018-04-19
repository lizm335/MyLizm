package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the GJT_STUDENT_TERM database table.
 * 
 */
@Entity
@Table(name = "GJT_STUDENT_TERM")
// @NamedQuery(name = "GjtStudentTerm.findAll", query = "SELECT g FROM GjtStudentTerm g")
@Deprecated public class BzrGjtStudentTerm implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@ManyToOne
	@JoinColumn(name = "STUDENT_ID")
	private BzrGjtStudentInfo gjtStudentInfo;

	@ManyToOne
	@JoinColumn(name = "TERM_ID")
	private BzrGjtTermInfo gjtTermInfo;

	@Column(name = "COURSE_COUNT")
	private String courseCount;

	@Column(name = "PASS_COUNT")
	private String passCount;

	@Column(name = "STUDY_PROGRESS")
	private BigDecimal studyProgress;

	@Column(name = "TERM_STATE")
	private String termState;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	public BzrGjtStudentTerm() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCourseCount() {
		return this.courseCount;
	}

	public void setCourseCount(String courseCount) {
		this.courseCount = courseCount;
	}

	public String getPassCount() {
		return this.passCount;
	}

	public void setPassCount(String passCount) {
		this.passCount = passCount;
	}

	public BigDecimal getStudyProgress() {
		return this.studyProgress;
	}

	public void setStudyProgress(BigDecimal studyProgress) {
		this.studyProgress = studyProgress;
	}

	public String getTermState() {
		return this.termState;
	}

	public void setTermState(String termState) {
		this.termState = termState;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	/**
	 * @return the gjtStudentInfo
	 */
	public BzrGjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	/**
	 * @param gjtStudentInfo
	 *            the gjtStudentInfo to set
	 */
	public void setGjtStudentInfo(BzrGjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	/**
	 * @return the gjtTermInfo
	 */
	public BzrGjtTermInfo getGjtTermInfo() {
		return gjtTermInfo;
	}

	/**
	 * @param gjtTermInfo
	 *            the gjtTermInfo to set
	 */
	public void setGjtTermInfo(BzrGjtTermInfo gjtTermInfo) {
		this.gjtTermInfo = gjtTermInfo;
	}

}