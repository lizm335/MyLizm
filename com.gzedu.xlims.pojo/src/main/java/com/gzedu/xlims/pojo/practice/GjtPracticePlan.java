package com.gzedu.xlims.pojo.practice;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * 社会实践计划
 * 
 */
@Entity
@Table(name="GJT_PRACTICE_PLAN")
@NamedQuery(name="GjtPracticePlan.findAll", query="SELECT g FROM GjtPracticePlan g")
public class GjtPracticePlan implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PRACTICE_PLAN_ID")
	private String practicePlanId;

	@Temporal(TemporalType.DATE)
	@Column(name="APPLY_BEGIN_DT")
	private Date applyBeginDt;

	@Temporal(TemporalType.DATE)
	@Column(name="APPLY_END_DT")
	private Date applyEndDt;

	@Temporal(TemporalType.DATE)
	@Column(name="CONFIRM_PRACTICE_END_DT")
	private Date confirmPracticeEndDt;

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

	@Column(name="GRADE_ID")
	private String gradeId;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="ORG_ID")
	private String orgId;

	@Column(name="PRACTICE_PLAN_CODE")
	private String practicePlanCode;

	@Column(name="PRACTICE_PLAN_NAME")
	private String practicePlanName;

	@Temporal(TemporalType.DATE)
	@Column(name="REVIEW_DT")
	private Date reviewDt;

	private int status;

	@Transient
	private int status2;

	@Temporal(TemporalType.DATE)
	@Column(name="SUBMIT_PRACTICE_END_DT")
	private Date submitPracticeEndDt;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@OneToMany(mappedBy="gjtPracticePlan", fetch = FetchType.LAZY)
	@OrderBy(value = "CREATED_DT")
	private List<GjtPracticePlanApproval> gjtPracticePlanApprovals;

	public GjtPracticePlan() {
	}

	public String getPracticePlanId() {
		return this.practicePlanId;
	}

	public void setPracticePlanId(String practicePlanId) {
		this.practicePlanId = practicePlanId;
	}

	public Date getApplyBeginDt() {
		return this.applyBeginDt;
	}

	public void setApplyBeginDt(Date applyBeginDt) {
		this.applyBeginDt = applyBeginDt;
	}

	public Date getApplyEndDt() {
		return this.applyEndDt;
	}

	public void setApplyEndDt(Date applyEndDt) {
		this.applyEndDt = applyEndDt;
	}

	public Date getConfirmPracticeEndDt() {
		return this.confirmPracticeEndDt;
	}

	public void setConfirmPracticeEndDt(Date confirmPracticeEndDt) {
		this.confirmPracticeEndDt = confirmPracticeEndDt;
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

	public String getGradeId() {
		return this.gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPracticePlanCode() {
		return this.practicePlanCode;
	}

	public void setPracticePlanCode(String practicePlanCode) {
		this.practicePlanCode = practicePlanCode;
	}

	public String getPracticePlanName() {
		return this.practicePlanName;
	}

	public void setPracticePlanName(String practicePlanName) {
		this.practicePlanName = practicePlanName;
	}

	public Date getReviewDt() {
		return this.reviewDt;
	}

	public void setReviewDt(Date reviewDt) {
		this.reviewDt = reviewDt;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus2() {
		return status2;
	}

	public void setStatus2(int status2) {
		this.status2 = status2;
	}

	public Date getSubmitPracticeEndDt() {
		return this.submitPracticeEndDt;
	}

	public void setSubmitPracticeEndDt(Date submitPracticeEndDt) {
		this.submitPracticeEndDt = submitPracticeEndDt;
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


	public List<GjtPracticePlanApproval> getGjtPracticePlanApprovals() {
		return this.gjtPracticePlanApprovals;
	}

	public void setGjtPracticePlanApprovals(List<GjtPracticePlanApproval> gjtPracticePlanApprovals) {
		this.gjtPracticePlanApprovals = gjtPracticePlanApprovals;
	}

	@Override
	public String toString() {
		return "GjtPracticePlan [practicePlanId=" + practicePlanId + ", applyBeginDt=" + applyBeginDt + ", applyEndDt="
				+ applyEndDt + ", confirmPracticeEndDt=" + confirmPracticeEndDt + ", createdBy=" + createdBy
				+ ", createdDt=" + createdDt + ", deletedBy=" + deletedBy + ", deletedDt=" + deletedDt + ", gradeId="
				+ gradeId + ", isDeleted=" + isDeleted + ", orgId=" + orgId + ", practicePlanCode=" + practicePlanCode
				+ ", practicePlanName=" + practicePlanName + ", reviewDt=" + reviewDt + ", status=" + status
				+ ", submitPracticeEndDt=" + submitPracticeEndDt + ", updatedBy=" + updatedBy + ", updatedDt="
				+ updatedDt + "]";
	}

}