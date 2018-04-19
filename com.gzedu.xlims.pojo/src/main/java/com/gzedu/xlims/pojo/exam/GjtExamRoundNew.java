package com.gzedu.xlims.pojo.exam;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the GJT_EXAM_ROUND_NEW database table.
 * 
 */
@Entity
@Table(name = "GJT_EXAM_ROUND_NEW")
@NamedQuery(name = "GjtExamRoundNew.findAll", query = "SELECT g FROM GjtExamRoundNew g")
public class GjtExamRoundNew implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EXAM_ROUND_ID")
	private String examRoundId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "START_TIME")
	private Date startTime;

	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "END_TIME")
	private Date endTime;

	@ManyToOne
	@JoinColumn(name = "EXAM_BATCH_CODE", referencedColumnName = "EXAM_BATCH_CODE", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtExamBatchNew examBatchNew;

	@Column(name = "EXAM_BATCH_CODE")
	private String examBatchCode;

	@ManyToOne
	@JoinColumn(name = "EXAM_ROOM_ID", referencedColumnName = "EXAM_ROOM_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtExamRoomNew examRoomNew;

	@Column(name = "EXAM_ROOM_ID")
	private String examRoomId;

	@ManyToOne
	@JoinColumn(name = "EXAM_POINT_ID", referencedColumnName = "EXAM_POINT_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtExamPointNew examPointNew;

	@Column(name = "EXAM_POINT_ID")
	private String examPointId;

	@ManyToOne
	@JoinColumn(name = "SUBJECT_ID", referencedColumnName = "SUBJECT_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtExamSubjectNew examSubjectNew;

	@Column(name = "SUBJECT_ID")
	private String subjectId;

	@Column(name = "EXAM_PLAN_ID")
	private String examPlanId;

	/*
	 * @Column(name = "IS_DELETED")
	 * 
	 * @Where(clause = "is_deleted=0") private int isDeleted;
	 */

	@Column(name = "ROUND_INDEX")
	private int roundIndex;

	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "XX_ID")
	private String xxId;

	public GjtExamRoundNew() {
	}

	public String getExamRoundId() {
		return this.examRoundId;
	}

	public void setExamRoundId(String examRoundId) {
		this.examRoundId = examRoundId;
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

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public GjtExamBatchNew getExamBatchNew() {
		return examBatchNew;
	}

	public void setExamBatchNew(GjtExamBatchNew examBatchNew) {
		this.examBatchNew = examBatchNew;
	}

	public String getExamBatchCode() {
		return examBatchCode;
	}

	public void setExamBatchCode(String examBatchCode) {
		this.examBatchCode = examBatchCode;
	}

	public String getExamRoomId() {
		return this.examRoomId;
	}

	public void setExamRoomId(String examRoomId) {
		this.examRoomId = examRoomId;
	}

	public String getExamPlanId() {
		return examPlanId;
	}

	public void setExamPlanId(String examPlanId) {
		this.examPlanId = examPlanId;
	}

	/*
	 * public int getIsDeleted() { return this.isDeleted; }
	 * 
	 * public void setIsDeleted(int isDeleted) { this.isDeleted = isDeleted; }
	 */

	public int getRoundIndex() {
		return this.roundIndex;
	}

	public void setRoundIndex(int roundIndex) {
		this.roundIndex = roundIndex;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public GjtExamRoomNew getExamRoomNew() {
		return examRoomNew;
	}

	public void setExamRoomNew(GjtExamRoomNew examRoomNew) {
		this.examRoomNew = examRoomNew;
	}

	public GjtExamPointNew getExamPointNew() {
		return examPointNew;
	}

	public void setExamPointNew(GjtExamPointNew examPointNew) {
		this.examPointNew = examPointNew;
	}

	public String getExamPointId() {
		return examPointId;
	}

	public void setExamPointId(String examPointId) {
		this.examPointId = examPointId;
	}

	public GjtExamSubjectNew getExamSubjectNew() {
		return examSubjectNew;
	}

	public void setExamSubjectNew(GjtExamSubjectNew examSubjectNew) {
		this.examSubjectNew = examSubjectNew;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

}