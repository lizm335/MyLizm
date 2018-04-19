package com.ouchgzee.headTeacher.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 考试预约流水记录实体类<br>
 * The persistent class for the GJT_EXAM_RECORD database table.
 * 
 */
@Entity
@Table(name = "GJT_EXAM_RECORD")
// @NamedQuery(name = "GjtExamRecord.findAll", query = "SELECT g FROM GjtExamRecord g")
@Deprecated public class BzrGjtExamRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BOOK_TIME")
	private Date bookTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "EXAM_POINT_ID")
	private String examPointId;

	@Column(name = "EXAM_SCORE")
	private BigDecimal examScore;

	@Column(name = "EXAM_STATE")
	private String examState;

	@Column(name = "IS_CANCEL")
	private String isCancel;

	@Column(name = "REC_ID")
	private String recId;

	@Column(name = "STUDENT_ID")
	private String studentId;

	@Column(name = "TEACH_PLAN_ID")
	private String teachPlanId;

	public BzrGjtExamRecord() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getBookTime() {
		return this.bookTime;
	}

	public void setBookTime(Date bookTime) {
		this.bookTime = bookTime;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getExamPointId() {
		return this.examPointId;
	}

	public void setExamPointId(String examPointId) {
		this.examPointId = examPointId;
	}

	public BigDecimal getExamScore() {
		return this.examScore;
	}

	public void setExamScore(BigDecimal examScore) {
		this.examScore = examScore;
	}

	public String getExamState() {
		return this.examState;
	}

	public void setExamState(String examState) {
		this.examState = examState;
	}

	public String getIsCancel() {
		return this.isCancel;
	}

	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}

	public String getRecId() {
		return this.recId;
	}

	public void setRecId(String recId) {
		this.recId = recId;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getTeachPlanId() {
		return this.teachPlanId;
	}

	public void setTeachPlanId(String teachPlanId) {
		this.teachPlanId = teachPlanId;
	}

}