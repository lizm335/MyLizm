package com.gzedu.xlims.pojo.graduation;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 学位条件基础表
 * 
 */
@Entity
@Table(name="GJT_DEGREE_REQUIREMENT_BASE")
@NamedQuery(name="GjtDegreeRequirementBase.findAll", query="SELECT g FROM GjtDegreeRequirementBase g")
public class GjtDegreeRequirementBase implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BASE_ID")
	private String baseId;

	@Column(name="BASE_DESC")
	private String baseDesc;

	@Column(name="BASE_NAME")
	private String baseName;

	@Column(name="BASE_TYPE")
	private Integer baseType;

	@Column(name="CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT", updatable = false)
	private Date createdDt;

	@Column(name="DELETED_BY", insertable = false)
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED_DT", insertable = false)
	private Date deletedDt;

	@Column(name="IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name="UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT", insertable = false)
	private Date updatedDt;

	public GjtDegreeRequirementBase() {
	}

	public String getBaseId() {
		return this.baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}

	public String getBaseDesc() {
		return this.baseDesc;
	}

	public void setBaseDesc(String baseDesc) {
		this.baseDesc = baseDesc;
	}

	public String getBaseName() {
		return this.baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public Integer getBaseType() {
		return this.baseType;
	}

	public void setBaseType(Integer baseType) {
		this.baseType = baseType;
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
		return "GjtDegreeRequirementBase [baseId=" + baseId + ", baseDesc=" + baseDesc + ", baseName=" + baseName
				+ ", baseType=" + baseType + ", createdBy=" + createdBy + ", createdDt=" + createdDt + ", deletedBy="
				+ deletedBy + ", deletedDt=" + deletedDt + ", isDeleted=" + isDeleted + ", updatedBy=" + updatedBy
				+ ", updatedDt=" + updatedDt + "]";
	}

}