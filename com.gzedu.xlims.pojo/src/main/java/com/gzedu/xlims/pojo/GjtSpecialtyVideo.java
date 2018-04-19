package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the GJT_SPECIALTY_VIDEO database table.
 * 
 */
@Entity
@Table(name="GJT_SPECIALTY_VIDEO")
@NamedQuery(name="GjtSpecialtyVideo.findAll", query="SELECT g FROM GjtSpecialtyVideo g")
public class GjtSpecialtyVideo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="VIDEO_ID")
	private String videoId;

	private String content;

	@Column(name="COVER_URL")
	private String coverUrl;

	@Column(name="CRAETED_BY")
	private String craetedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DT")
	private Date createdDt;

	private String title;

	@Column(name="TYPE_NAME")
	private String typeName;

	@Column(name="VIDEO_URL")
	private String videoUrl;

	@Column(name = "FIRST_COURSE_ID")
	private String firstCourseId;

	public GjtSpecialtyVideo() {
	}

	public String getVideoId() {
		return this.videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCoverUrl() {
		return this.coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getCraetedBy() {
		return this.craetedBy;
	}

	public void setCraetedBy(String craetedBy) {
		this.craetedBy = craetedBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getVideoUrl() {
		return this.videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getFirstCourseId() {
		return firstCourseId;
	}

	public void setFirstCourseId(String firstCourseId) {
		this.firstCourseId = firstCourseId;
	}

}