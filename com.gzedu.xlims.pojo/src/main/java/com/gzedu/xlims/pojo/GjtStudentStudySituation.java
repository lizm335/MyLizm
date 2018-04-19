package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

/**
 * The persistent class for the GJT_STUDENT_STUDY_SITUATION database table.
 * 选课记录表
 */
@Entity
@Table(name = "GJT_STUDENT_STUDY_SITUATION")
@NamedQuery(name = "GjtStudentStudySituation.findAll", query = "SELECT g FROM GjtStudentStudySituation g")
public class GjtStudentStudySituation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CHOOSE_ID")
	private String chooseId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHOOSE_ID", insertable = false, updatable = false)
	@Where(clause = "IS_DELETED = 'N'")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtRecResult gjtRecResult;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "EXAM_SCORE")
	private Float examScore;

	@Column(name = "LOGIN_TIMES")
	private Integer loginTimes;

	private Float progress;

	private Float score;

	@Column(name = "STUDENT_ID")
	private String studentId;

	@Column(name = "TEACH_PLAN_ID")
	private String teachPlanId;// 学年度课程ID

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;
	

	public GjtStudentStudySituation() {
	}

	public String getChooseId() {
		return this.chooseId;
	}

	public void setChooseId(String chooseId) {
		this.chooseId = chooseId;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public Float getExamScore() {
		return this.examScore;
	}

	public void setExamScore(Float examScore) {
		this.examScore = examScore;
	}

	public Integer getLoginTimes() {
		return this.loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	public Float getProgress() {
		return this.progress;
	}

	public void setProgress(Float progress) {
		this.progress = progress;
	}

	public Float getScore() {
		return this.score;
	}

	public void setScore(Float score) {
		this.score = score;
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

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public GjtRecResult getGjtRecResult() {
		return gjtRecResult;
	}

	public void setGjtRecResult(GjtRecResult gjtRecResult) {
		this.gjtRecResult = gjtRecResult;
	}


}