package com.gzedu.xlims.pojo.practice;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;


/**
 * The persistent class for the GJT_PRACTICE_GUIDE_RECORD database table.
 * 
 */
@Entity
@Table(name="GJT_PRACTICE_GUIDE_RECORD")
@NamedQuery(name="GjtPracticeGuideRecord.findAll", query="SELECT g FROM GjtPracticeGuideRecord g")
public class GjtPracticeGuideRecord implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="RECORD_ID")
	private String recordId;

	private String attachment;

	@Column(name="ATTACHMENT_NAME")
	private String attachmentName;

	private String content;

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

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="IS_STUDENT")
	private int isStudent;

	@Column(name="PRACTICE_PLAN_ID")
	private String practicePlanId;

	@Column(name="PROGRESS_CODE")
	private String progressCode;

	@Column(name="STUDENT_ID")
	private String studentId;

	@ManyToOne
	@JoinColumn(name="STUDENT_ID", insertable=false, updatable=false)
	private GjtStudentInfo gjtStudentInfo;

	@Column(name="TEACHER_ID")
	private String teacherId;
	
	@ManyToOne
	@JoinColumn(name="TEACHER_ID", insertable=false, updatable=false)
	private GjtEmployeeInfo teacher;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	public GjtPracticeGuideRecord() {
	}

	public String getRecordId() {
		return this.recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getAttachment() {
		return this.attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getAttachmentName() {
		return this.attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getIsStudent() {
		return this.isStudent;
	}

	public void setIsStudent(int isStudent) {
		this.isStudent = isStudent;
	}

	public String getPracticePlanId() {
		return this.practicePlanId;
	}

	public void setPracticePlanId(String practicePlanId) {
		this.practicePlanId = practicePlanId;
	}

	public String getProgressCode() {
		return this.progressCode;
	}

	public void setProgressCode(String progressCode) {
		this.progressCode = progressCode;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getTeacherId() {
		return this.teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
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

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public GjtEmployeeInfo getTeacher() {
		return teacher;
	}

	public void setTeacher(GjtEmployeeInfo teacher) {
		this.teacher = teacher;
	}

}