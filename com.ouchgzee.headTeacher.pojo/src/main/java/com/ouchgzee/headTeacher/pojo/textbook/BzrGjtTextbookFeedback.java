package com.ouchgzee.headTeacher.pojo.textbook;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;


/**
 * The persistent class for the GJT_TEXTBOOK_FEEDBACK database table.
 * 
 */
@Entity
@Table(name="GJT_TEXTBOOK_FEEDBACK")
// @NamedQuery(name="GjtTextbookFeedback.findAll", query="SELECT g FROM GjtTextbookFeedback g")
@Deprecated public class BzrGjtTextbookFeedback implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="FEEDBACK_ID")
	private String feedbackId;

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

	@Column(name="FEEDBACK_TYPE")
	private int feedbackType;

	@Column(name="IS_DELETED")
	private String isDeleted;

	private String reason;

	private String reply;

	private int status;

	@ManyToOne
	@JoinColumn(name="STUDENT_ID")
	private BzrGjtStudentInfo gjtStudentInfo;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	//bi-directional many-to-one association to GjtTextbookFeedbackDetail
	@OneToMany(mappedBy="gjtTextbookFeedback")
	private List<BzrGjtTextbookFeedbackDetail> gjtTextbookFeedbackDetails;

	@Transient
	private String[] textbookIds;

	public BzrGjtTextbookFeedback() {
	}

	public String getFeedbackId() {
		return this.feedbackId;
	}

	public void setFeedbackId(String feedbackId) {
		this.feedbackId = feedbackId;
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

	public int getFeedbackType() {
		return this.feedbackType;
	}

	public void setFeedbackType(int feedbackType) {
		this.feedbackType = feedbackType;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReply() {
		return this.reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public BzrGjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(BzrGjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
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

	public List<BzrGjtTextbookFeedbackDetail> getGjtTextbookFeedbackDetails() {
		return this.gjtTextbookFeedbackDetails;
	}

	public void setGjtTextbookFeedbackDetails(List<BzrGjtTextbookFeedbackDetail> gjtTextbookFeedbackDetails) {
		this.gjtTextbookFeedbackDetails = gjtTextbookFeedbackDetails;
	}

	public BzrGjtTextbookFeedbackDetail addGjtTextbookFeedbackDetail(BzrGjtTextbookFeedbackDetail gjtTextbookFeedbackDetail) {
		getGjtTextbookFeedbackDetails().add(gjtTextbookFeedbackDetail);
		gjtTextbookFeedbackDetail.setGjtTextbookFeedback(this);

		return gjtTextbookFeedbackDetail;
	}

	public BzrGjtTextbookFeedbackDetail removeGjtTextbookFeedbackDetail(BzrGjtTextbookFeedbackDetail gjtTextbookFeedbackDetail) {
		getGjtTextbookFeedbackDetails().remove(gjtTextbookFeedbackDetail);
		gjtTextbookFeedbackDetail.setGjtTextbookFeedback(null);

		return gjtTextbookFeedbackDetail;
	}

	public String[] getTextbookIds() {
		return textbookIds;
	}

	public void setTextbookIds(String[] textbookIds) {
		this.textbookIds = textbookIds;
	}

}