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
 * 专业的教学计划
 */
@Entity
@Table(name = "GJT_SPECIALTY_BASE")
@NamedQuery(name = "GjtSpecialtyBase.findAll", query = "SELECT g FROM GjtSpecialtyBase g")
public class GjtSpecialtyBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SPECIALTY_BASE_ID")
	private String specialtyBaseId;
	// 院校ID
	@Column(name = "XX_ID")
	private String xxId;
	// 专业名称
	@Column(name = "SPECIALTY_NAME")
	private String specialtyName;
	// 专业代码
	@Column(name = "SPECIALTY_CODE")
	private String specialtyCode;
	// 专业层次
	@Column(name = "SPECIALTY_LAYER")
	private Integer specialtyLayer;
	// 状态
	@Column(name = "STATUS")
	private Integer status = 0;// 0:停用，1:启用
	// 责任教师
	@Column(name = "TEACHER")
	private String teacher;
	// 责任教师照片
	@Column(name = "TEACHER_IMG_URL")
	private String teacherImgUrl;
	// 专业封面
	@Column(name = "SPECIALTY_IMG_URL")
	private String specialtyImgUrl;
	// 责任教师简介
	@Column(name = "TEACHER_DETAILS")
	private String teacherDetails;
	// 专业简介
	@Column(name = "SPECIALTY_DETAILS")
	private String specialtyDetails;
	// 视频地址
	@Column(name = "VIDEO_ID")
	private String videoId;

	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", updatable = false)
	private Date createdDt;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "UPDATED_BY", insertable = false) // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	public GjtSpecialtyBase() {

	}
	public GjtSpecialtyBase(String specialtyBaseId) {
		this.specialtyBaseId = specialtyBaseId;
	}

	public String getSpecialtyBaseId() {
		return specialtyBaseId;
	}

	public void setSpecialtyBaseId(String specialtyBaseId) {
		this.specialtyBaseId = specialtyBaseId;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getSpecialtyName() {
		return specialtyName;
	}

	public void setSpecialtyName(String specialtyName) {
		this.specialtyName = specialtyName;
	}

	public String getSpecialtyCode() {
		return specialtyCode;
	}

	public void setSpecialtyCode(String specialtyCode) {
		this.specialtyCode = specialtyCode;
	}

	public Integer getSpecialtyLayer() {
		return specialtyLayer;
	}

	public void setSpecialtyLayer(Integer specialtyLayer) {
		this.specialtyLayer = specialtyLayer;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getTeacherImgUrl() {
		return teacherImgUrl;
	}

	public void setTeacherImgUrl(String teacherImgUrl) {
		this.teacherImgUrl = teacherImgUrl;
	}

	public String getSpecialtyImgUrl() {
		return specialtyImgUrl;
	}

	public void setSpecialtyImgUrl(String specialtyImgUrl) {
		this.specialtyImgUrl = specialtyImgUrl;
	}

	public String getTeacherDetails() {
		return teacherDetails;
	}

	public void setTeacherDetails(String teacherDetails) {
		this.teacherDetails = teacherDetails;
	}

	public String getSpecialtyDetails() {
		return specialtyDetails;
	}

	public void setSpecialtyDetails(String specialtyDetails) {
		this.specialtyDetails = specialtyDetails;
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

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
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

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

}
