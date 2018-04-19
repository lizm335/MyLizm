package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the GJT_EXEMPT_EXAM_MATERIAL database table.
 * 免修免考证明清单
 */
@Entity
@Table(name="GJT_EXEMPT_EXAM_MATERIAL")
@NamedQuery(name="GjtExemptExamMaterial.findAll", query="SELECT g FROM GjtExemptExamMaterial g")
public class GjtExemptExamMaterial implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private String id;
	
	@Column(name="INSTALL_ID")
	private String installId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT",insertable = false, updatable = false)
	private Date createdDt;

	@Column(name="IS_DELETED",insertable = false, updatable = false)
	private String isDeleted;

	@Column(name="IS_ONLINE_EXAM")
	private String isOnlineExam;//状态:0-不需要 1-需要

	@Column(name="MATERIAL_NAME")
	private String materialName;//清单名称		

	private String memo;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name="VERSION",insertable = false, updatable = false)
	private BigDecimal version;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INSTALL_ID",referencedColumnName = "INSTALL_ID", insertable = false, updatable = false)
	private GjtExemptExamInstall gjtExemptExamInstall;

	public GjtExemptExamMaterial() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getInstallId() {
		return installId;
	}

	public void setInstallId(String installId) {
		this.installId = installId;
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

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsOnlineExam() {
		return this.isOnlineExam;
	}

	public void setIsOnlineExam(String isOnlineExam) {
		this.isOnlineExam = isOnlineExam;
	}

	public String getMaterialName() {
		return this.materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public GjtExemptExamInstall getGjtExemptExamInstall() {
		return this.gjtExemptExamInstall;
	}

	public void setGjtExemptExamInstall(GjtExemptExamInstall gjtExemptExamInstall) {
		this.gjtExemptExamInstall = gjtExemptExamInstall;
	}

}