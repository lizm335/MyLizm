package com.gzedu.xlims.pojo.thesis;

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
 * 论文计划
 * 
 */
@Entity
@Table(name="GJT_THESIS_PLAN")
@NamedQuery(name="GjtThesisPlan.findAll", query="SELECT g FROM GjtThesisPlan g")
public class GjtThesisPlan implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="THESIS_PLAN_ID")
	private String thesisPlanId;

	@Temporal(TemporalType.DATE)
	@Column(name="APPLY_BEGIN_DT")
	private Date applyBeginDt;

	@Temporal(TemporalType.DATE)
	@Column(name="APPLY_END_DT")
	private Date applyEndDt;

	@Temporal(TemporalType.DATE)
	@Column(name="CONFIRM_PROPOSE_END_DT")
	private Date confirmProposeEndDt;

	@Temporal(TemporalType.DATE)
	@Column(name="CONFIRM_THESIS_END_DT")
	private Date confirmThesisEndDt;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Temporal(TemporalType.DATE)
	@Column(name="DEFENCE_DT")
	private Date defenceDt;

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

	@Temporal(TemporalType.DATE)
	@Column(name="REVIEW_DT")
	private Date reviewDt;

	@Temporal(TemporalType.DATE)
	@Column(name="SUBMIT_PROPOSE_END_DT")
	private Date submitProposeEndDt;

	@Temporal(TemporalType.DATE)
	@Column(name="SUBMIT_THESIS_END_DT")
	private Date submitThesisEndDt;

	@Column(name="THESIS_PLAN_CODE")
	private String thesisPlanCode;

	@Column(name="THESIS_PLAN_NAME")
	private String thesisPlanName;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	private int status;

	@Transient
	private int status2;

	@OneToMany(mappedBy="gjtThesisPlan", fetch = FetchType.LAZY)
	@OrderBy(value = "CREATED_DT")
	private List<GjtThesisPlanApproval> gjtThesisPlanApprovals;

	public GjtThesisPlan() {
	}

	public String getThesisPlanId() {
		return this.thesisPlanId;
	}

	public void setThesisPlanId(String thesisPlanId) {
		this.thesisPlanId = thesisPlanId;
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

	public Date getDefenceDt() {
		return this.defenceDt;
	}

	public void setDefenceDt(Date defenceDt) {
		this.defenceDt = defenceDt;
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

	public Date getReviewDt() {
		return this.reviewDt;
	}

	public void setReviewDt(Date reviewDt) {
		this.reviewDt = reviewDt;
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

	public String getThesisPlanCode() {
		return this.thesisPlanCode;
	}

	public void setThesisPlanCode(String thesisPlanCode) {
		this.thesisPlanCode = thesisPlanCode;
	}

	public String getThesisPlanName() {
		return this.thesisPlanName;
	}

	public void setThesisPlanName(String thesisPlanName) {
		this.thesisPlanName = thesisPlanName;
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

	public int getStatus() {
		return status;
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

	public List<GjtThesisPlanApproval> getGjtThesisPlanApprovals() {
		return this.gjtThesisPlanApprovals;
	}

	public void setGjtThesisPlanApprovals(List<GjtThesisPlanApproval> gjtThesisPlanApprovals) {
		this.gjtThesisPlanApprovals = gjtThesisPlanApprovals;
	}

	@Override
	public String toString() {
		return "GjtThesisPlan [thesisPlanId=" + thesisPlanId + ", applyBeginDt=" + applyBeginDt + ", applyEndDt="
				+ applyEndDt + ", confirmProposeEndDt=" + confirmProposeEndDt + ", confirmThesisEndDt="
				+ confirmThesisEndDt + ", createdBy=" + createdBy + ", createdDt=" + createdDt + ", defenceDt="
				+ defenceDt + ", deletedBy=" + deletedBy + ", deletedDt=" + deletedDt + ", gradeId=" + gradeId
				+ ", isDeleted=" + isDeleted + ", orgId=" + orgId + ", reviewDt=" + reviewDt + ", submitProposeEndDt="
				+ submitProposeEndDt + ", submitThesisEndDt=" + submitThesisEndDt + ", thesisPlanCode=" + thesisPlanCode
				+ ", thesisPlanName=" + thesisPlanName + ", updatedBy=" + updatedBy + ", updatedDt=" + updatedDt
				+ ", status=" + status + "]";
	}

}