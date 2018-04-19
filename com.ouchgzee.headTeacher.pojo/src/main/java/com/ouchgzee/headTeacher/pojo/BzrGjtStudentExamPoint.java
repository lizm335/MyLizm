package com.ouchgzee.headTeacher.pojo;

import com.ouchgzee.headTeacher.id.BzrGjtStudentExamPointId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 考点预约实体类<br>
 * The persistent class for the GJT_STUDENT_EXAM_POINT database table.
 * 
 */
@Entity
@IdClass(BzrGjtStudentExamPointId.class)
@Table(name = "GJT_STUDENT_EXAM_POINT")
// @NamedQuery(name = "GjtStudentExamPoint.findAll", query = "SELECT g FROM GjtStudentExamPoint g")
@Deprecated public class BzrGjtStudentExamPoint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "STUDENT_ID")
	private String studentId;

	@Id
	@Column(name = "TERM_ID")
	private String termId;

	@ManyToOne
	@JoinColumn(name = "EXAM_POINT_ID")
	private BzrGjtExamPoint gjtExamPoint;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	public BzrGjtStudentExamPoint() {
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getTermId() {
		return this.termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public BzrGjtExamPoint getGjtExamPoint() {
		return gjtExamPoint;
	}

	public void setGjtExamPoint(BzrGjtExamPoint gjtExamPoint) {
		this.gjtExamPoint = gjtExamPoint;
	}
}