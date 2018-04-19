package com.gzedu.xlims.pojo.exam;

import com.gzedu.xlims.pojo.GjtStudentInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 考试指引反馈实体类
 */
@Entity
@Table(name="GJT_EXAM_GUIDE_FEEDBACK")
@NamedQuery(name="GjtExamGuideFeedback.findAll", query="SELECT g FROM GjtExamGuideFeedback g")
public class GjtExamGuideFeedback implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@Id
	@Column(name="ID")
	private String id;

	/**
	 * 考试计划CODE
	 */
	@Column(name="EXAM_BATCH_CODE")
	private String examBatchCode;

	@ManyToOne
	@JoinColumn(name="STUDENT_ID")
	private GjtStudentInfo gjtStudentInfo;

	/**
	 * 查看状态 (0-未看 1-已看)
	 */
	@Column(name="VIEW_STATE")
	private Integer viewState;

	/**
	 * 反馈状态，默认0 (0-未反馈 1-已反馈)
	 */
	@Column(name="FEEDBACK_STATE", insertable = false)
	private Integer feedbackState;

	/**
	 * 反馈内容
	 */
	@Column(name = "FEEDBACK_CONTENT")
	private String feedbackContent;

	/**
	 * 反馈时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FEEDBACK_DT")
	private Date feedbackDt;

	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExamBatchCode() {
		return examBatchCode;
	}

	public void setExamBatchCode(String examBatchCode) {
		this.examBatchCode = examBatchCode;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public Integer getViewState() {
		return viewState;
	}

	public void setViewState(Integer viewState) {
		this.viewState = viewState;
	}

	public Integer getFeedbackState() {
		return feedbackState;
	}

	public void setFeedbackState(Integer feedbackState) {
		this.feedbackState = feedbackState;
	}

	public String getFeedbackContent() {
		return feedbackContent;
	}

	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}

	public Date getFeedbackDt() {
		return feedbackDt;
	}

	public void setFeedbackDt(Date feedbackDt) {
		this.feedbackDt = feedbackDt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
}