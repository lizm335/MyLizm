package com.gzedu.xlims.pojo.graduation;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 学历表
 * 
 */
@Entity
@Table(name="GJT_GRADUATION_REGISTER_EDU")
@NamedQuery(name="GjtGraduationRegisterEdu.findAll", query="SELECT g FROM GjtGraduationRegisterEdu g")
public class GjtGraduationRegisterEdu implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EDU_ID")
	private String eduId;

	@Temporal(TemporalType.DATE)
	@Column(name="BEGIN_TIME")
	private Date beginTime;

	@Column(name="CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT", updatable = false)
	private Date createdDt;

	private String degree;

	@Column(name="DELETED_BY", insertable = false)
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED_DT", insertable = false)
	private Date deletedDt;

	@Temporal(TemporalType.DATE)
	@Column(name="END_TIME")
	private Date endTime;

	@Column(name="IS_DELETED", insertable = false)
	private String isDeleted;

	private String region;

	private String school;

	@Column(name="STUDENT_ID")
	private String studentId;

	@Column(name="UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT", insertable = false)
	private Date updatedDt;

	public GjtGraduationRegisterEdu() {
	}

	public String getEduId() {
		return this.eduId;
	}

	public void setEduId(String eduId) {
		this.eduId = eduId;
	}

	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
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

	public String getDegree() {
		return this.degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
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

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSchool() {
		return this.school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
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

	@Override
	public String toString() {
		return "GjtGraduationRegisterEdu [eduId=" + eduId + ", beginTime=" + beginTime + ", createdBy=" + createdBy
				+ ", createdDt=" + createdDt + ", degree=" + degree + ", deletedBy=" + deletedBy + ", deletedDt="
				+ deletedDt + ", endTime=" + endTime + ", isDeleted=" + isDeleted + ", region=" + region + ", school="
				+ school + ", studentId=" + studentId + ", updatedBy=" + updatedBy + ", updatedDt=" + updatedDt + "]";
	}

}