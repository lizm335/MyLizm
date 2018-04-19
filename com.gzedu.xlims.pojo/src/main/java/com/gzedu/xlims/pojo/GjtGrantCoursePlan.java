package com.gzedu.xlims.pojo;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.common.collect.Sets;

@Entity
@Table(name="GJT_GRANT_COURSE_PLAN")
public class GjtGrantCoursePlan {
	@Id
	@Column(name="GRANT_COURSE_PLAN_ID")
	private String grantCoursePlanId;
	// 定制课程ID
	@Column(name="CUSTOM_COURSE_ID")
	private String customCourseId;
	
	// 课程主题
	@Column(name="COURSE_THEME")
	private String courseTheme;
	// 授课地点
	@Column(name="ADDR")
	private String addr;
	// 授课开始时间
	@Column(name="START_DATE")
	private Date startDate;
	
	@Transient
	private String startDateStr;
	// 授课结束时间
	@Column(name="ENT_DATE")
	private Date endDate;
	@Transient
	private String endDateStr;
	// 授课老师
	@Column(name="TEACHER")
	private String teacher;
	// 授课凭证状态(0:未上传,1:上传中, 2:已上传)
	@Column(name="CERTIFICATE_STATUS")
	private Integer certificateStatus;
	// 授课凭证
	@OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name="GRANT_COURSE_PLAN_ID")
	private Set<GjtGrantCourseCertificate> certificates;
	
	// 授课凭证扫描件url
	@ElementCollection(targetClass = java.lang.String.class) 
	@CollectionTable(name="GJT_GRANT_COURSE_PLAN_IMAGE", joinColumns=@JoinColumn(name="GRANT_COURSE_PLAN_ID"))
    @Column(name = "IMAGE") 
    private Set<String> images = Sets.newHashSet();
	
	// 授课凭证excel文件url
	@Column(name="FILE_URL")
	private String fileUrl;
	// 授课凭证excel文件文
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT",insertable = false, updatable = false)
	private Date createdDt;
	
	@Column(name = "UPDATED_BY", insertable = false) // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;
	
	@Column(name = "IS_DELETED", insertable = false) // 是否删除
	private String isDeleted;

	public String getGrantCoursePlanId() {
		return grantCoursePlanId;
	}

	public void setGrantCoursePlanId(String grantCoursePlanId) {
		this.grantCoursePlanId = grantCoursePlanId;
	}

	public String getCustomCourseId() {
		return customCourseId;
	}

	public void setCustomCourseId(String customCourseId) {
		this.customCourseId = customCourseId;
	}

	public String getCourseTheme() {
		return courseTheme;
	}

	public void setCourseTheme(String courseTheme) {
		this.courseTheme = courseTheme;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public Integer getCertificateStatus() {
		return certificateStatus;
	}

	public void setCertificateStatus(Integer certificateStatus) {
		this.certificateStatus = certificateStatus;
	}

	public Set<GjtGrantCourseCertificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(Set<GjtGrantCourseCertificate> certificates) {
		this.certificates = certificates;
	}

	public Set<String> getImages() {
		return images;
	}

	public void setImages(Set<String> images) {
		this.images = images;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
