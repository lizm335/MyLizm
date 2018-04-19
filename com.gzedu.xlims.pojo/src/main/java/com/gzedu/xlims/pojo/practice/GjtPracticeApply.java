package com.gzedu.xlims.pojo.practice;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;


/**
 * The persistent class for the GJT_PRACTICE_APPLY database table.
 * 
 */
@Entity
@Table(name="GJT_PRACTICE_APPLY")
@NamedQuery(name="GjtPracticeApply.findAll", query="SELECT g FROM GjtPracticeApply g")
public class GjtPracticeApply implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="APPLY_ID")
	private String applyId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="DELETED_BY")
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED_DT")
	private Date deletedDt;

	@Column(name="GUIDE_TEACHER")
	private String guideTeacher;
	
	@ManyToOne
	@JoinColumn(name="GUIDE_TEACHER", insertable=false, updatable=false)
	private GjtEmployeeInfo guideTeacher1;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="REVIEW_SCORE")
	private Float reviewScore;

	@Column(name="TEACHER_AUTOGRAPH")
	private String teacherAutograph;

	@Column(name="EXPRESS_COMPANY")
	private String expressCompany;

	@Column(name="EXPRESS_NUMBER")
	private String expressNumber;

	private int status;

	@Column(name="STUDENT_ID")
	private String studentId;

	@ManyToOne
	@JoinColumn(name="STUDENT_ID", insertable=false, updatable=false)
	private GjtStudentInfo gjtStudentInfo;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name="PRACTICE_PLAN_ID")
	private String practicePlanId;

	//bi-directional many-to-one association to GjtPracticePlan
	@ManyToOne
	@JoinColumn(name="PRACTICE_PLAN_ID", insertable=false, updatable=false)
	private GjtPracticePlan gjtPracticePlan;
	
	/**
	 * 记录学员最后一次提交的记录
	 */
	@Transient
	private GjtPracticeGuideRecord guideRecord;
	
	/**
	 * 记录指导老师的指导次数
	 */
	@Transient
	private int guideTimes;

	public GjtPracticeApply() {
	}

	public String getApplyId() {
		return this.applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
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

	public String getDeletedBy() {
		return this.deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedDt() {
		return this.deletedDt;
	}

	public void setDeletedDt(Date deletedDt) {
		this.deletedDt = deletedDt;
	}

	public String getGuideTeacher() {
		return this.guideTeacher;
	}

	public void setGuideTeacher(String guideTeacher) {
		this.guideTeacher = guideTeacher;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Float getReviewScore() {
		return this.reviewScore;
	}

	public void setReviewScore(Float reviewScore) {
		this.reviewScore = reviewScore;
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

	public GjtPracticePlan getGjtPracticePlan() {
		return this.gjtPracticePlan;
	}

	public void setGjtPracticePlan(GjtPracticePlan gjtPracticePlan) {
		this.gjtPracticePlan = gjtPracticePlan;
	}

	public GjtEmployeeInfo getGuideTeacher1() {
		return guideTeacher1;
	}

	public void setGuideTeacher1(GjtEmployeeInfo guideTeacher1) {
		this.guideTeacher1 = guideTeacher1;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public String getPracticePlanId() {
		return practicePlanId;
	}

	public void setPracticePlanId(String practicePlanId) {
		this.practicePlanId = practicePlanId;
	}

	public String getTeacherAutograph() {
		return teacherAutograph;
	}

	public void setTeacherAutograph(String teacherAutograph) {
		this.teacherAutograph = teacherAutograph;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getExpressNumber() {
		return expressNumber;
	}

	public void setExpressNumber(String expressNumber) {
		this.expressNumber = expressNumber;
	}

	public GjtPracticeGuideRecord getGuideRecord() {
		return guideRecord;
	}

	public void setGuideRecord(GjtPracticeGuideRecord guideRecord) {
		this.guideRecord = guideRecord;
	}

	public int getGuideTimes() {
		return guideTimes;
	}

	public void setGuideTimes(int guideTimes) {
		this.guideTimes = guideTimes;
	}

	@Override
	public String toString() {
		return "GjtPracticeApply [applyId=" + applyId + ", createdBy=" + createdBy + ", createdDt=" + createdDt
				+ ", deletedBy=" + deletedBy + ", deletedDt=" + deletedDt + ", guideTeacher=" + guideTeacher
				+ ", isDeleted=" + isDeleted + ", reviewScore=" + reviewScore + ", status=" + status + ", studentId="
				+ studentId + ", updatedBy=" + updatedBy + ", updatedDt=" + updatedDt + ", practicePlanId="
				+ practicePlanId + "]";
	}

}