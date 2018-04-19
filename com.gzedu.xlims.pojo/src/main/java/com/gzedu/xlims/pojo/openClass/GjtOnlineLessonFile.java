package com.gzedu.xlims.pojo.openClass;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the GJT_ONLINE_LESSON_FILE database table.
 * 
 */
@Entity
@Table(name="GJT_ONLINE_LESSON_FILE")
@NamedQuery(name="GjtOnlineLessonFile.findAll", query="SELECT g FROM GjtOnlineLessonFile g")
public class GjtOnlineLessonFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="FILE_NAME")
	private String fileName;

	@Column(name="FILE_URL")
	private String fileUrl;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="ONLINETUTOR_ID")
	private String onlinetutorId;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	public GjtOnlineLessonFile() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return this.fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOnlinetutorId() {
		return this.onlinetutorId;
	}

	public void setOnlinetutorId(String onlinetutorId) {
		this.onlinetutorId = onlinetutorId;
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

}