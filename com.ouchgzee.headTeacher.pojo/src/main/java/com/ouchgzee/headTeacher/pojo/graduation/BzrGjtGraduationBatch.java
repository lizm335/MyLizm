package com.ouchgzee.headTeacher.pojo.graduation;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;


/**
 * 毕业批次
 * 
 */
@Entity
@Table(name="GJT_GRADUATION_BATCH")
// @NamedQuery(name="GjtGraduationBatch.findAll", query="SELECT g FROM GjtGraduationBatch g where g.isDeleted='N'")
@Deprecated public class BzrGjtGraduationBatch implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 批次ID
	 */
	@Id
	@Column(name="BATCH_ID")
	private String batchId;

	/**
	 * 社会实践申请开始时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="APPLY_PRACTICE_BEGIN_DT")
	private Date applyPracticeBeginDt;

	/**
	 * 社会实践申请结束时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="APPLY_PRACTICE_END_DT")
	private Date applyPracticeEndDt;

	/**
	 * 论文申请开始时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="APPLY_THESIS_BEGIN_DT")
	private Date applyThesisBeginDt;

	/**
	 * 论文申请结束时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="APPLY_THESIS_END_DT")
	private Date applyThesisEndDt;

	/**
	 * 批次编号
	 */
	@Column(name="BATCH_CODE")
	private String batchCode;

	/**
	 * 批次名称
	 */
	@Column(name="BATCH_NAME")
	private String batchName;

	/**
	 * 社会实践定稿截止时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="CONFIRM_PRACTICE_END_DT")
	private Date confirmPracticeEndDt;

	/**
	 * 开题定稿截止时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="CONFIRM_PROPOSE_END_DT")
	private Date confirmProposeEndDt;

	/**
	 * 论文定稿截止时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="CONFIRM_THESIS_END_DT")
	private Date confirmThesisEndDt;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="CREATED_DT")
	private Date createdDt;

	/**
	 * 论文答辩老师指导人数上限
	 */
	@Column(name="DEFENCE_LIMIT_NUM")
	private int defenceLimitNum;

	/**
	 * 论文答辩时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="DEFENCE_THESIS_DT")
	private Date defenceThesisDt;

	@Column(name="DELETED_BY")
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED_DT")
	private Date deletedDt;

	/**
	 * 论文指导老师指导人数上限
	 */
	@Column(name="GUIDE_LIMIT_NUM")
	private int guideLimitNum;

	@Column(name="IS_DELETED")
	private String isDeleted;

	/**
	 * 社会实践老师指导人数上限
	 */
	@Column(name="PRACTICE_LIMIT_NUM")
	private int practiceLimitNum;

	/**
	 * 社会实践写作评分时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="REVIEW_PRACTICE_DT")
	private Date reviewPracticeDt;

	/**
	 * 论文初评时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="REVIEW_THESIS_DT")
	private Date reviewThesisDt;

	/**
	 * 学年度编号
	 */
	@Column(name="STUDY_YEAR_CODE")
	private int studyYearCode;

	/**
	 * 社会实践初稿提交截止时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="SUBMIT_PRACTICE_END_DT")
	private Date submitPracticeEndDt;

	/**
	 * 提交开题截止时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="SUBMIT_PROPOSE_END_DT")
	private Date submitProposeEndDt;

	/**
	 * 初稿提交截止时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="SUBMIT_THESIS_END_DT")
	private Date submitThesisEndDt;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	//bi-directional many-to-one association to GjtGraduationApply
	@OneToMany(mappedBy="gjtGraduationBatch")
	private List<BzrGjtGraduationApply> gjtGraduationApplies;

	@Column(name="ORG_ID")
	private String orgId;

	public BzrGjtGraduationBatch() {
	}

	public String getBatchId() {
		return this.batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public Date getApplyPracticeBeginDt() {
		return this.applyPracticeBeginDt;
	}

	public void setApplyPracticeBeginDt(Date applyPracticeBeginDt) {
		this.applyPracticeBeginDt = applyPracticeBeginDt;
	}

	public Date getApplyPracticeEndDt() {
		return this.applyPracticeEndDt;
	}

	public void setApplyPracticeEndDt(Date applyPracticeEndDt) {
		this.applyPracticeEndDt = applyPracticeEndDt;
	}

	public Date getApplyThesisBeginDt() {
		return this.applyThesisBeginDt;
	}

	public void setApplyThesisBeginDt(Date applyThesisBeginDt) {
		this.applyThesisBeginDt = applyThesisBeginDt;
	}

	public Date getApplyThesisEndDt() {
		return this.applyThesisEndDt;
	}

	public void setApplyThesisEndDt(Date applyThesisEndDt) {
		this.applyThesisEndDt = applyThesisEndDt;
	}

	public String getBatchCode() {
		return this.batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getBatchName() {
		return this.batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public Date getConfirmPracticeEndDt() {
		return this.confirmPracticeEndDt;
	}

	public void setConfirmPracticeEndDt(Date confirmPracticeEndDt) {
		this.confirmPracticeEndDt = confirmPracticeEndDt;
	}

	public Date getConfirmProposeEndDt() {
		return this.confirmProposeEndDt;
	}

	public void setConfirmProposeEndDt(Date confirmProposeEndDt) {
		this.confirmProposeEndDt = confirmProposeEndDt;
	}

	public Date getConfirmThesisEndDt() {
		return this.confirmThesisEndDt;
	}

	public void setConfirmThesisEndDt(Date confirmThesisEndDt) {
		this.confirmThesisEndDt = confirmThesisEndDt;
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

	public int getDefenceLimitNum() {
		return this.defenceLimitNum;
	}

	public void setDefenceLimitNum(int defenceLimitNum) {
		this.defenceLimitNum = defenceLimitNum;
	}

	public Date getDefenceThesisDt() {
		return this.defenceThesisDt;
	}

	public void setDefenceThesisDt(Date defenceThesisDt) {
		this.defenceThesisDt = defenceThesisDt;
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

	public int getGuideLimitNum() {
		return this.guideLimitNum;
	}

	public void setGuideLimitNum(int guideLimitNum) {
		this.guideLimitNum = guideLimitNum;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getPracticeLimitNum() {
		return this.practiceLimitNum;
	}

	public void setPracticeLimitNum(int practiceLimitNum) {
		this.practiceLimitNum = practiceLimitNum;
	}

	public Date getReviewPracticeDt() {
		return this.reviewPracticeDt;
	}

	public void setReviewPracticeDt(Date reviewPracticeDt) {
		this.reviewPracticeDt = reviewPracticeDt;
	}

	public Date getReviewThesisDt() {
		return this.reviewThesisDt;
	}

	public void setReviewThesisDt(Date reviewThesisDt) {
		this.reviewThesisDt = reviewThesisDt;
	}

	public int getStudyYearCode() {
		return this.studyYearCode;
	}

	public void setStudyYearCode(int studyYearCode) {
		this.studyYearCode = studyYearCode;
	}

	public Date getSubmitPracticeEndDt() {
		return this.submitPracticeEndDt;
	}

	public void setSubmitPracticeEndDt(Date submitPracticeEndDt) {
		this.submitPracticeEndDt = submitPracticeEndDt;
	}

	public Date getSubmitProposeEndDt() {
		return this.submitProposeEndDt;
	}

	public void setSubmitProposeEndDt(Date submitProposeEndDt) {
		this.submitProposeEndDt = submitProposeEndDt;
	}

	public Date getSubmitThesisEndDt() {
		return this.submitThesisEndDt;
	}

	public void setSubmitThesisEndDt(Date submitThesisEndDt) {
		this.submitThesisEndDt = submitThesisEndDt;
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

	public List<BzrGjtGraduationApply> getGjtGraduationApplies() {
		return this.gjtGraduationApplies;
	}

	public void setGjtGraduationApplies(List<BzrGjtGraduationApply> gjtGraduationApplies) {
		this.gjtGraduationApplies = gjtGraduationApplies;
	}

	public BzrGjtGraduationApply addGjtGraduationApply(BzrGjtGraduationApply gjtGraduationApply) {
		getGjtGraduationApplies().add(gjtGraduationApply);
		gjtGraduationApply.setGjtGraduationBatch(this);

		return gjtGraduationApply;
	}

	public BzrGjtGraduationApply removeGjtGraduationApply(BzrGjtGraduationApply gjtGraduationApply) {
		getGjtGraduationApplies().remove(gjtGraduationApply);
		gjtGraduationApply.setGjtGraduationBatch(null);

		return gjtGraduationApply;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}