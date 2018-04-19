package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the GJT_SPECIALTY_MODULE_LIMIT database table.
 * 
 */
@Entity
@Table(name = "GJT_SPECIALTY_MODULE_LIMIT")
@NamedQuery(name = "GjtSpecialtyModuleLimit.findAll", query = "SELECT g FROM GjtSpecialtyModuleLimit g")
public class GjtSpecialtyModuleLimit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Id
	private String id;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@ManyToOne // 课程信息原来字典
	@JoinColumn(name = "MODULE_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private TblSysData tblSysData;
	
	@Column(name = "MODULE_ID")
	private String moduleId;

	private String score;

	@Column(name = "TOTALSCORE")
	private String totalScore;

	@Column(name = "CRTVU_SCORE")
	private Double crtvuScore; // 中央电大考试最低学分

	@OneToOne // 专业
	@JoinColumn(name = "SPECIALTY_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSpecialty gjtSpecialty;
	
	@Column(name = "SPECIALTY_ID", insertable = false, updatable = false)
	private String specialtyId;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	public GjtSpecialtyModuleLimit() {
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

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the tblSysData
	 */
	public TblSysData getTblSysData() {
		return tblSysData;
	}

	/**
	 * @param tblSysData
	 *            the tblSysData to set
	 */
	public void setTblSysData(TblSysData tblSysData) {
		this.tblSysData = tblSysData;
	}

	/**
	 * @return the gjtSpecialty
	 */
	public GjtSpecialty getGjtSpecialty() {
		return gjtSpecialty;
	}

	public String getSpecialtyId() {
		return specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @param gjtSpecialty
	 *            the gjtSpecialty to set
	 */
	public void setGjtSpecialty(GjtSpecialty gjtSpecialty) {
		this.gjtSpecialty = gjtSpecialty;
	}

	public String getScore() {
		return this.score;
	}

	public void setScore(String score) {
		this.score = score;
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

	public String getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

	public Double getCrtvuScore() {
		return crtvuScore;
	}

	public void setCrtvuScore(Double crtvuScore) {
		this.crtvuScore = crtvuScore;
	}

}