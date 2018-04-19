package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 预约选课考试信息实体类<br>
 * The persistent class for the GJT_REC_RESULT database table.
 * 
 */
@Entity
@Table(name = "GJT_REC_RESULT")
// @NamedQuery(name = "GjtRecResult.findAll", query = "SELECT g FROM GjtRecResult g")
@Deprecated public class BzrGjtRecResult implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "REC_ID")
	private String recId;

	@Column(name = "COURSE_ID")
	private String courseId;

	@Column(name = "COURSE_SCHEDULE")
	private BigDecimal courseSchedule;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "DELETED_TYPE")
	private String deletedType;

	@Column(name = "EXAM_SCORE")
	private BigDecimal examScore;

	@Column(name = "EXAM_SCORE1")
	private BigDecimal examScore1;

	@Column(name = "EXAM_SCORE2")
	private BigDecimal examScore2;

	@Column(name = "EXAM_STATE", insertable = false)
	private String examState;

	@Column(name = "GET_CREDITS")
	private BigDecimal getCredits;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	@Column(name = "IS_OVER", insertable = false)
	private String isOver;

	@Column(name = "IS_RESERVE", insertable = false)
	private String isReserve;

	@Column(name = "IS_RESERVE_BOOK", insertable = false)
	private String isReserveBook;

	private String memo;

	@Column(name = "ORG_CODE")
	private String orgCode;

	@Column(name = "ORG_ID")
	private String orgId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RESERVE_TIME")
	private Date reserveTime;

	@Column(name = "SCORE_STATE")
	private String scoreState;

	@Column(name = "SIGNUP_ID")
	private String signupId;

	@Column(name = "STUDENT_ID")
	private String studentId;

	@Column(name = "SYNC_STATUS", insertable = false)
	private String syncStatus;

	@Column(name = "TEACH_PLAN_ID")
	private String teachPlanId;

	@Column(name = "TERM_ID")
	private String termId;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@Column(name = "XX_ID")
	private String xxId;

	@Column(name = "XXZX_ID")
	private String xxzxId;

	public BzrGjtRecResult() {
	}

	public String getRecId() {
		return this.recId;
	}

	public void setRecId(String recId) {
		this.recId = recId;
	}

	public String getCourseId() {
		return this.courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public BigDecimal getCourseSchedule() {
		return this.courseSchedule;
	}

	public void setCourseSchedule(BigDecimal courseSchedule) {
		this.courseSchedule = courseSchedule;
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

	public String getDeletedType() {
		return this.deletedType;
	}

	public void setDeletedType(String deletedType) {
		this.deletedType = deletedType;
	}

	public BigDecimal getExamScore() {
		return this.examScore;
	}

	public void setExamScore(BigDecimal examScore) {
		this.examScore = examScore;
	}

	public BigDecimal getExamScore1() {
		return this.examScore1;
	}

	public void setExamScore1(BigDecimal examScore1) {
		this.examScore1 = examScore1;
	}

	public BigDecimal getExamScore2() {
		return this.examScore2;
	}

	public void setExamScore2(BigDecimal examScore2) {
		this.examScore2 = examScore2;
	}

	public String getExamState() {
		return this.examState;
	}

	public void setExamState(String examState) {
		this.examState = examState;
	}

	public BigDecimal getGetCredits() {
		return this.getCredits;
	}

	public void setGetCredits(BigDecimal getCredits) {
		this.getCredits = getCredits;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getIsOver() {
		return this.isOver;
	}

	public void setIsOver(String isOver) {
		this.isOver = isOver;
	}

	public String getIsReserve() {
		return this.isReserve;
	}

	public void setIsReserve(String isReserve) {
		this.isReserve = isReserve;
	}

	public String getIsReserveBook() {
		return this.isReserveBook;
	}

	public void setIsReserveBook(String isReserveBook) {
		this.isReserveBook = isReserveBook;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Date getReserveTime() {
		return this.reserveTime;
	}

	public void setReserveTime(Date reserveTime) {
		this.reserveTime = reserveTime;
	}

	public String getScoreState() {
		return this.scoreState;
	}

	public void setScoreState(String scoreState) {
		this.scoreState = scoreState;
	}

	public String getSignupId() {
		return this.signupId;
	}

	public void setSignupId(String signupId) {
		this.signupId = signupId;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getSyncStatus() {
		return this.syncStatus;
	}

	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
	}

	public String getTeachPlanId() {
		return this.teachPlanId;
	}

	public void setTeachPlanId(String teachPlanId) {
		this.teachPlanId = teachPlanId;
	}

	public String getTermId() {
		return this.termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getXxzxId() {
		return this.xxzxId;
	}

	public void setXxzxId(String xxzxId) {
		this.xxzxId = xxzxId;
	}

}