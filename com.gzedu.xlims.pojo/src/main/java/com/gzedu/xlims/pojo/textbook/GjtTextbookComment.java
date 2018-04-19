package com.gzedu.xlims.pojo.textbook;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gzedu.xlims.pojo.GjtStudentInfo;

/**
 * The persistent class for the GJT_TEXTBOOK_COMMENT database table.
 * 
 */
@Entity
@Table(name="GJT_TEXTBOOK_COMMENT")
@NamedQuery(name="GjtTextbookComment.findAll", query="SELECT g FROM GjtTextbookComment g")
public class GjtTextbookComment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COMMENT_ID")
	private String commentId;

	@Lob
	@Column(name="COMMENT_CONTENT")
	private String commentContent;

	@Column(name="COMMENT_TYPE")
	private int commentType;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="DISTRIBUTE_ID")
	private String distributeId;

	@Column(name = "STUDENT_ID")
	private String studentId;

	@ManyToOne()
	@JoinColumn(name = "STUDENT_ID", insertable = false, updatable = false)
	private GjtStudentInfo GjtStudentInfo;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@OneToOne
	@JoinColumn(name = "DISTRIBUTE_ID", insertable = false, updatable = false)
	private GjtTextbookDistribute gjtTextbookDistribute;

	public GjtTextbookComment() {
	}

	public String getCommentId() {
		return this.commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getCommentContent() {
		return this.commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public int getCommentType() {
		return this.commentType;
	}

	public void setCommentType(int commentType) {
		this.commentType = commentType;
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

	public String getDistributeId() {
		return this.distributeId;
	}

	public void setDistributeId(String distributeId) {
		this.distributeId = distributeId;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
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

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return GjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		GjtStudentInfo = gjtStudentInfo;
	}

	public GjtTextbookDistribute getGjtTextbookDistribute() {
		return gjtTextbookDistribute;
	}

	public void setGjtTextbookDistribute(GjtTextbookDistribute gjtTextbookDistribute) {
		this.gjtTextbookDistribute = gjtTextbookDistribute;
	}


}