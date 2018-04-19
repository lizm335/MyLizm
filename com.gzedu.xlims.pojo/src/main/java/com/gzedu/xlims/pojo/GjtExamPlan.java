package com.gzedu.xlims.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the GJT_EXAM_PLAN database table.
 * 
 */
@Entity
@Table(name="GJT_EXAM_PLAN")
@NamedQuery(name="GjtExamPlan.findAll", query="SELECT g FROM GjtExamPlan g")
public class GjtExamPlan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="EROOM_ID")
	private String eroomId;

	@Column(name="EROOM_SEAT")
	private BigDecimal eroomSeat;

	@Column(name="EXAM_COURSE")
	private String examCourse;

	@Column(name="EXAM_SCORE")
	private String examScore;

	@Column(name="EXAM_STAGE_ID")
	private String examStageId;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="IS_ENABLED")
	private String isEnabled;

	@Column(name="IS_SYNC")
	private String isSync;

	private String memo;

	@Column(name="SIGNUP_ID")
	private String signupId;

	@Column(name="STUDENT_ID")
	private String studentId;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name="VERSION")
	private BigDecimal version;

	public GjtExamPlan() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getEroomId() {
		return this.eroomId;
	}

	public void setEroomId(String eroomId) {
		this.eroomId = eroomId;
	}

	public BigDecimal getEroomSeat() {
		return this.eroomSeat;
	}

	public void setEroomSeat(BigDecimal eroomSeat) {
		this.eroomSeat = eroomSeat;
	}

	public String getExamCourse() {
		return this.examCourse;
	}

	public void setExamCourse(String examCourse) {
		this.examCourse = examCourse;
	}

	public String getExamScore() {
		return this.examScore;
	}

	public void setExamScore(String examScore) {
		this.examScore = examScore;
	}

	public String getExamStageId() {
		return this.examStageId;
	}

	public void setExamStageId(String examStageId) {
		this.examStageId = examStageId;
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

	public String getIsSync() {
		return this.isSync;
	}

	public void setIsSync(String isSync) {
		this.isSync = isSync;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

}