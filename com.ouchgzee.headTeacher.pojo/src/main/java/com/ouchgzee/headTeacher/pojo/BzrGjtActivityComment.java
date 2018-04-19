package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the GJT_ACTIVITY_COMMENT database table.
 * 
 */
@Entity
@Table(name = "GJT_ACTIVITY_COMMENT")
// @NamedQuery(name = "GjtActivityComment.findAll", query = "SELECT g FROM GjtActivityComment g")
@Deprecated public class BzrGjtActivityComment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "ACTIVITY_ID")
	private String activityId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "COMMENT_DT")
	private Date commentDt;

	@Lob
	private String comments;

	private BigDecimal star;

	@Column(name = "STUDENT_ID")
	private String studentId;

	public BzrGjtActivityComment() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActivityId() {
		return this.activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public Date getCommentDt() {
		return this.commentDt;
	}

	public void setCommentDt(Date commentDt) {
		this.commentDt = commentDt;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public BigDecimal getStar() {
		return this.star;
	}

	public void setStar(BigDecimal star) {
		this.star = star;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

}