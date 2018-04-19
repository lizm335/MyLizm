package com.gzedu.xlims.pojo;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the GJT_CLASS_STUDENT database table.
 * 
 */
@Entity
@Table(name = "GJT_CLASS_STUDENT")
@NamedQuery(name = "GjtClassStudent.findAll", query = "SELECT g FROM GjtClassStudent g")
public class GjtClassStudent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CLASS_STUDENT_ID")
	private String classStudentId;

	@OneToOne // 班级
	@JoinColumn(name = "CLASS_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtClassInfo gjtClassInfo;

	@Column(name = "CLASS_ID", insertable = false, updatable = false)
	private String classId;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "DELETED_TYPE")
	private String deletedType;

	@Column(name = "EEGROUPADDUSERS_STATUS", insertable = false)
	private String eegroupaddusersStatus;

	@ManyToOne // 年级
	@JoinColumn(name = "GRADE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtGrade gjtGrade;

	@Column(name = "IS_DELETED", insertable = false)
	@Where(clause = "isDeleted='N'")
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	@Where(clause = "isEnabled='1'")
	private String isEnabled;

	private String memo;

	@Column(name = "ORG_CODE")
	private String orgCode;

	@ManyToOne // 机构
	@JoinColumn(name = "ORG_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtOrg gjtOrg;

	@ManyToOne // 学生
	@JoinColumn(name = "STUDENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtStudentInfo gjtStudentInfo;

	@Column(name = "SYNC_STATUS", insertable = false)
	private String syncStatus;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@ManyToOne // 院校
	@JoinColumn(name = "XX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSchoolInfo gjtSchoolInfo;

	@ManyToOne // 学习中心
	@JoinColumn(name = "XXZX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtStudyCenter gjtStudyCenter;
	
	@Column(name = "STUDENT_ID",insertable=false,updatable=false)
	private String studentId;
	
	public GjtClassStudent() {
	}

	
	
	public String getStudentId() {
		return studentId;
	}



	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}



	public String getClassStudentId() {
		return this.classStudentId;
	}

	public void setClassStudentId(String classStudentId) {
		this.classStudentId = classStudentId;
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

	public String getDeletedType() {
		return this.deletedType;
	}

	public void setDeletedType(String deletedType) {
		this.deletedType = deletedType;
	}

	public String getEegroupaddusersStatus() {
		return this.eegroupaddusersStatus;
	}

	public void setEegroupaddusersStatus(String eegroupaddusersStatus) {
		this.eegroupaddusersStatus = eegroupaddusersStatus;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getSyncStatus() {
		return this.syncStatus;
	}

	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
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

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public GjtClassInfo getGjtClassInfo() {
		return gjtClassInfo;
	}

	public void setGjtClassInfo(GjtClassInfo gjtClassInfo) {
		this.gjtClassInfo = gjtClassInfo;
	}

	public GjtGrade getGjtGrade() {
		return gjtGrade;
	}

	public void setGjtGrade(GjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	public GjtOrg getGjtOrg() {
		return gjtOrg;
	}

	public void setGjtOrg(GjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public GjtSchoolInfo getGjtSchoolInfo() {
		return gjtSchoolInfo;
	}

	public void setGjtSchoolInfo(GjtSchoolInfo gjtSchoolInfo) {
		this.gjtSchoolInfo = gjtSchoolInfo;
	}

	public GjtStudyCenter getGjtStudyCenter() {
		return gjtStudyCenter;
	}

	public void setGjtStudyCenter(GjtStudyCenter gjtStudyCenter) {
		this.gjtStudyCenter = gjtStudyCenter;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

}