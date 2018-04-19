package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 专业信息实体类<br>
 * The persistent class for the GJT_SPECIALTY database table.
 * 
 */
@Entity
@Table(name = "GJT_SPECIALTY")
// @NamedQuery(name = "GjtSpecialty.findAll", query = "SELECT g FROM GjtSpecialty g")
@Deprecated public class BzrGjtSpecialty implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SPECIALTY_ID")
	private String specialtyId;

	private BigDecimal bxxf;

	private String bzkzym;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	private String jlny;

	@Column(name = "MANAGE_MODE")
	private String manageMode;

	@Column(name = "ORG_CODE")
	private String orgCode;

	@Column(name = "ORG_ID")
	private String orgId;

	private String pycc;

	private String rxny;

	@Column(name = "SPECIALTY_CATEGORY")
	private String specialtyCategory;

	@Column(name = "STUDY_OBJECT")
	private String studyObject;

	@Column(name = "STUDY_PERIOD")
	private String studyPeriod;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	private String xlm;

	private String xwm;

	@Column(name = "XX_ID")
	private String xxId;

	private BigDecimal xxxf;

	private String yjszym;

	@Column(name = "YX_ID")
	private String yxId;

	private BigDecimal zxf;

	private String zyh;

	private String zyjc;

	@Lob
	private String zyjs;

	private String zylb;

	private String zymc;

	private String zyywmc;

	public BzrGjtSpecialty() {
	}

	public String getSpecialtyId() {
		return this.specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public BigDecimal getBxxf() {
		return this.bxxf;
	}

	public void setBxxf(BigDecimal bxxf) {
		this.bxxf = bxxf;
	}

	public String getBzkzym() {
		return this.bzkzym;
	}

	public void setBzkzym(String bzkzym) {
		this.bzkzym = bzkzym;
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

	public String getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getJlny() {
		return this.jlny;
	}

	public void setJlny(String jlny) {
		this.jlny = jlny;
	}

	public String getManageMode() {
		return this.manageMode;
	}

	public void setManageMode(String manageMode) {
		this.manageMode = manageMode;
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

	public String getPycc() {
		return this.pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public String getRxny() {
		return this.rxny;
	}

	public void setRxny(String rxny) {
		this.rxny = rxny;
	}

	public String getSpecialtyCategory() {
		return this.specialtyCategory;
	}

	public void setSpecialtyCategory(String specialtyCategory) {
		this.specialtyCategory = specialtyCategory;
	}

	public String getStudyObject() {
		return this.studyObject;
	}

	public void setStudyObject(String studyObject) {
		this.studyObject = studyObject;
	}

	public String getStudyPeriod() {
		return this.studyPeriod;
	}

	public void setStudyPeriod(String studyPeriod) {
		this.studyPeriod = studyPeriod;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getXlm() {
		return this.xlm;
	}

	public void setXlm(String xlm) {
		this.xlm = xlm;
	}

	public String getXwm() {
		return this.xwm;
	}

	public void setXwm(String xwm) {
		this.xwm = xwm;
	}

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public BigDecimal getXxxf() {
		return this.xxxf;
	}

	public void setXxxf(BigDecimal xxxf) {
		this.xxxf = xxxf;
	}

	public String getYjszym() {
		return this.yjszym;
	}

	public void setYjszym(String yjszym) {
		this.yjszym = yjszym;
	}

	public String getYxId() {
		return this.yxId;
	}

	public void setYxId(String yxId) {
		this.yxId = yxId;
	}

	public BigDecimal getZxf() {
		return this.zxf;
	}

	public void setZxf(BigDecimal zxf) {
		this.zxf = zxf;
	}

	public String getZyh() {
		return this.zyh;
	}

	public void setZyh(String zyh) {
		this.zyh = zyh;
	}

	public String getZyjc() {
		return this.zyjc;
	}

	public void setZyjc(String zyjc) {
		this.zyjc = zyjc;
	}

	public String getZyjs() {
		return this.zyjs;
	}

	public void setZyjs(String zyjs) {
		this.zyjs = zyjs;
	}

	public String getZylb() {
		return this.zylb;
	}

	public void setZylb(String zylb) {
		this.zylb = zylb;
	}

	public String getZymc() {
		return this.zymc;
	}

	public void setZymc(String zymc) {
		this.zymc = zymc;
	}

	public String getZyywmc() {
		return this.zyywmc;
	}

	public void setZyywmc(String zyywmc) {
		this.zyywmc = zyywmc;
	}

}