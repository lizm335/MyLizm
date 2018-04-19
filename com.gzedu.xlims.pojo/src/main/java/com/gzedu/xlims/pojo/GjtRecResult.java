package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 
 * 功能说明： 成绩表
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月12日
 * @version 2.5
 *
 */
@Entity
@Table(name = "GJT_REC_RESULT")
@NamedQuery(name = "GjtRecResult.findAll", query = "SELECT g FROM GjtRecResult g")
public class GjtRecResult implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "REC_ID")
	private String recId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "XX_ID") // 院校/机构ID
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSchoolInfo gjtSchoolInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtStudentInfo gjtStudentInfo;

	@Column(name = "STUDENT_ID")
	private String studentId;

	@Column(name = "COURSE_ID")
	private String courseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID", insertable = false, updatable = false)
	private GjtCourse gjtCourse;// 课程

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEACH_PLAN_ID", insertable = false, updatable = false)
	private GjtTeachPlan gjtTeachPlan;

	@Transient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TERM_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtTermInfo gjtTermInfo;// 学期

	@Column(name = "TERM_ID")
	private String termId;// 学期

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
	private BigDecimal examScore;// 学习成绩

	@Column(name = "EXAM_SCORE1")
	private BigDecimal examScore1;// 考试成绩

	@Column(name = "EXAM_SCORE2")
	private BigDecimal examScore2;// 总成绩

	/** 考试状态：0:未通过;1:已通过;2:学习中;3:登记中 (默认 2) */
	@Column(name = "EXAM_STATE", insertable = false)
	private String examState;

	/** 预约状态：0 待预约 1 已预约 2 无需预约 (默认 0) */
	@Column(name = "BESPEAK_STATE", insertable = false)
	private String bespeakState;

	/** 缴费状态：0 待缴费 1 已交费 2 无需缴费 (默认 2) */
	@Column(name = "PAY_STATE")
	private String payState;

	/**
	 * 本次缴费标识 缴费单号
	 */
	@Column(name = "ORDER_MARK")
	private String orderMark;

	@Column(name = "GET_CREDITS")
	private BigDecimal getCredits;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	@Column(name = "IS_OVER", insertable = false)
	private String isOver;// 是否免修/免考 0 否 1 免考 2 免修 3 免修免考

	@Column(name = "IS_RESERVE", insertable = false)
	private String isReserve;// N 未预约 Y已预约

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

	@Column(name = "SYNC_STATUS", insertable = false)
	private String syncStatus;

	@Column(name = "TEACH_PLAN_ID")
	private String teachPlanId;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@Column(name = "XXZX_ID")
	private String xxzxId;// 学习中心

	@Column(name = "TERMCOURSE_ID")
	private String termcourseId; // 期课程ID

	@Column(name = "REBUILD_STATE") // 是否重修
	private String rebuildState;

	@Column(name = "PROGRESS") // 学习进度
	private BigDecimal progress;

	public GjtRecResult() {
	}

	public String getRebuildState() {
		return rebuildState;
	}

	public void setRebuildState(String rebuildState) {
		this.rebuildState = rebuildState;
	}

	public String getRecId() {
		return this.recId;
	}

	public void setRecId(String recId) {
		this.recId = recId;
	}

	public GjtCourse getGjtCourse() {
		return gjtCourse;
	}

	public void setGjtCourse(GjtCourse gjtCourse) {
		this.gjtCourse = gjtCourse;
	}

	public GjtTeachPlan getGjtTeachPlan() {
		return gjtTeachPlan;
	}

	public void setGjtTeachPlan(GjtTeachPlan gjtTeachPlan) {
		this.gjtTeachPlan = gjtTeachPlan;
	}

	public String getTermId() {
		return termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
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

	public GjtTermInfo getGjtTermInfo() {
		return gjtTermInfo;
	}

	public void setGjtTermInfo(GjtTermInfo gjtTermInfo) {
		this.gjtTermInfo = gjtTermInfo;
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

	public String getXxzxId() {
		return this.xxzxId;
	}

	public void setXxzxId(String xxzxId) {
		this.xxzxId = xxzxId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public void setBespeakState(String bespeakState) {
		this.bespeakState = bespeakState;
	}

	public String getBespeakState() {
		return bespeakState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	public String getPayState() {
		return payState;
	}

	public String getOrderMark() {
		return orderMark;
	}

	public void setOrderMark(String orderMark) {
		this.orderMark = orderMark;
	}

	public GjtSchoolInfo getGjtSchoolInfo() {
		return gjtSchoolInfo;
	}

	public void setGjtSchoolInfo(GjtSchoolInfo gjtSchoolInfo) {
		this.gjtSchoolInfo = gjtSchoolInfo;
	}

	public String getTermcourseId() {
		return termcourseId;
	}

	public void setTermcourseId(String termcourseId) {
		this.termcourseId = termcourseId;
	}

	public BigDecimal getProgress() {
		return progress;
	}

	public void setProgress(BigDecimal progress) {
		this.progress = progress;
	}
}