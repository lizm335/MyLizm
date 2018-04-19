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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the GJT_EXEMPT_EXAM_PROVE database table.
 * 学员申请免修免考材料证明 
 */
@Entity
@Table(name="GJT_EXEMPT_EXAM_PROVE")
@NamedQuery(name="GjtExemptExamProve.findAll", query="SELECT g FROM GjtExemptExamProve g")
public class GjtExemptExamProve implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="APPLY_ID")
	private String applyId;

	@Column(name="AWARD_DATE")
	private String awardDate;

	@Column(name="AWARD_UNIT")
	private String awardUnit;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT",insertable = false, updatable = false)
	private Date createdDt;

	@Column(name="EXEMPT_EXAM_ID")
	private String exemptExamId;

	@Column(name="MATERIAL_ID")
	private String materialId;

	private String memo;

	@Column(name="STUDENT_ID")
	private String studentId;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;
	
	@Column(name="URL")
	private String url;

	@Column(name="VERSION",insertable = false, updatable = false)
	private BigDecimal version;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="EXEMPT_EXAM_ID",referencedColumnName = "EXEMPT_EXAM_ID", insertable = false, updatable = false)	
	private GjtExemptExamInfo gjtExemptExamInfo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MATERIAL_ID",insertable = false, updatable = false)
	private GjtExemptExamMaterial gjtExemptExamMaterial;
	
	public GjtExemptExamProve() {
	}

	public String getApplyId() {
		return this.applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getAwardDate() {
		return this.awardDate;
	}

	public void setAwardDate(String awardDate) {
		this.awardDate = awardDate;
	}

	public String getAwardUnit() {
		return this.awardUnit;
	}

	public void setAwardUnit(String awardUnit) {
		this.awardUnit = awardUnit;
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

	public String getExemptExamId() {
		return this.exemptExamId;
	}

	public void setExemptExamId(String exemptExamId) {
		this.exemptExamId = exemptExamId;
	}

	public String getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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
		
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public GjtExemptExamInfo getGjtExemptExamInfo() {
		return gjtExemptExamInfo;
	}

	public void setGjtExemptExamInfo(GjtExemptExamInfo gjtExemptExamInfo) {
		this.gjtExemptExamInfo = gjtExemptExamInfo;
	}

	public GjtExemptExamMaterial getGjtExemptExamMaterial() {
		return gjtExemptExamMaterial;
	}

	public void setGjtExemptExamMaterial(GjtExemptExamMaterial gjtExemptExamMaterial) {
		this.gjtExemptExamMaterial = gjtExemptExamMaterial;
	}
}