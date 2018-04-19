package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 学员-班级关系实体类<br>
 * The persistent class for the GJT_CLASS_STUDENT database table.
 * 
 */
@Entity
@Table(name = "GJT_CLASS_STUDENT")
// @NamedQuery(name = "GjtClassStudent.findAll", query = "SELECT g FROM GjtClassStudent g")
@Deprecated public class BzrGjtClassStudent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CLASS_STUDENT_ID")
	private String classStudentId;

	@ManyToOne
	@JoinColumn(name = "CLASS_ID")
	private BzrGjtClassInfo gjtClassInfo;

	@ManyToOne
	@JoinColumn(name = "STUDENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtStudentInfo gjtStudentInfo;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "DELETED_TYPE")
	private String deletedType;

	@Column(name = "EEGROUPADDUSERS_STATUS")
	private String eegroupaddusersStatus;

	@Column(name = "GRADE_ID")
	private String gradeId;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	private String memo;

	@Column(name = "ORG_CODE")
	private String orgCode;

	@Column(name = "ORG_ID")
	private String orgId;

	@Column(name = "SYNC_STATUS")
	private String syncStatus;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@Column(name = "XX_ID")
	private String xxId;

	@Column(name = "XXZX_ID")
	private String xxzxId;

	public BzrGjtClassStudent() {
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

	public String getGradeId() {
		return this.gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
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

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
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

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getXxzxId() {
		return this.xxzxId;
	}

	public void setXxzxId(String xxzxId) {
		this.xxzxId = xxzxId;
	}

	/**
	 * @return the gjtClassInfo
	 */
	public BzrGjtClassInfo getGjtClassInfo() {
		return gjtClassInfo;
	}

	/**
	 * @param gjtClassInfo
	 *            the gjtClassInfo to set
	 */
	public void setGjtClassInfo(BzrGjtClassInfo gjtClassInfo) {
		this.gjtClassInfo = gjtClassInfo;
	}

	/**
	 * @return the gjtStudentInfo
	 */
	public BzrGjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	/**
	 * @param gjtStudentInfo
	 *            the gjtStudentInfo to set
	 */
	public void setGjtStudentInfo(BzrGjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

}