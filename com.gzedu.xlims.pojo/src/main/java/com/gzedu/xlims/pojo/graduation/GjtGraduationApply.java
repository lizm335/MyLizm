package com.gzedu.xlims.pojo.graduation;

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

import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;


/**
 * The persistent class for the GJT_GRADUATION_APPLY database table.
 * 毕业申请表
 */
@Entity
@Table(name="GJT_GRADUATION_APPLY")
@NamedQuery(name="GjtGraduationApply.findAll", query="SELECT g FROM GjtGraduationApply g")
public class GjtGraduationApply implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="APPLY_ID")
	private String applyId;

	@Column(name="APPLY_DEGREE")
	private int applyDegree;

	@Column(name="APPLY_TYPE")
	private int applyType;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="DEFENCE_SCORE")
	private Float defenceScore;

	@Column(name="DEFENCE_TEACHER")
	private String defenceTeacher;

	@Column(name="DELETED_BY")
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED_DT")
	private Date deletedDt;

	@Column(name="GUIDE_TEACHER")
	private String guideTeacher;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="NEED_DEFENCE")
	private int needDefence;

	@Column(name="REVIEW_SCORE")
	private Float reviewScore;

	private int status;

	@ManyToOne
	@JoinColumn(name="STUDENT_ID")
	private GjtStudentInfo gjtStudentInfo;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	//bi-directional many-to-one association to GjtGraduationBatch
	@ManyToOne
	@JoinColumn(name="BATCH_ID")
	private GjtGraduationBatch gjtGraduationBatch;
	
	@ManyToOne
	@JoinColumn(name="GUIDE_TEACHER", insertable=false, updatable=false)
	private GjtEmployeeInfo guideTeacher1;
	
	@ManyToOne
	@JoinColumn(name="DEFENCE_TEACHER", insertable=false, updatable=false)
	private GjtEmployeeInfo defenceTeacher1;

	@ManyToOne
	@JoinColumn(name="DEFENCE_PLAN_ID")
	private GjtGraduationDefencePlan gjtGraduationDefencePlan;

	public GjtGraduationApply() {
	}

	public String getApplyId() {
		return this.applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public int getApplyDegree() {
		return this.applyDegree;
	}

	public void setApplyDegree(int applyDegree) {
		this.applyDegree = applyDegree;
	}

	public int getApplyType() {
		return this.applyType;
	}

	public void setApplyType(int applyType) {
		this.applyType = applyType;
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

	public Float getDefenceScore() {
		return this.defenceScore;
	}

	public void setDefenceScore(Float defenceScore) {
		this.defenceScore = defenceScore;
	}

	public String getDefenceTeacher() {
		return this.defenceTeacher;
	}

	public void setDefenceTeacher(String defenceTeacher) {
		this.defenceTeacher = defenceTeacher;
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

	public int getNeedDefence() {
		return this.needDefence;
	}

	public void setNeedDefence(int needDefence) {
		this.needDefence = needDefence;
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

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public GjtGraduationBatch getGjtGraduationBatch() {
		return this.gjtGraduationBatch;
	}

	public void setGjtGraduationBatch(GjtGraduationBatch gjtGraduationBatch) {
		this.gjtGraduationBatch = gjtGraduationBatch;
	}

	public GjtEmployeeInfo getGuideTeacher1() {
		return guideTeacher1;
	}

	public void setGuideTeacher1(GjtEmployeeInfo guideTeacher1) {
		this.guideTeacher1 = guideTeacher1;
	}

	public GjtEmployeeInfo getDefenceTeacher1() {
		return defenceTeacher1;
	}

	public void setDefenceTeacher1(GjtEmployeeInfo defenceTeacher1) {
		this.defenceTeacher1 = defenceTeacher1;
	}

	public GjtGraduationDefencePlan getGjtGraduationDefencePlan() {
		return gjtGraduationDefencePlan;
	}

	public void setGjtGraduationDefencePlan(GjtGraduationDefencePlan gjtGraduationDefencePlan) {
		this.gjtGraduationDefencePlan = gjtGraduationDefencePlan;
	}

}