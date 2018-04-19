package com.gzedu.xlims.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the GJT_GRADUATE_STANDARD database table.
 * 
 */
@Entity
@Table(name="GJT_GRADUATE_STANDARD")
@NamedQuery(name="GjtGraduateStandard.findAll", query="SELECT g FROM GjtGraduateStandard g")
public class GjtGraduateStandard implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="REQ_ID")
	private String reqId;

	private BigDecimal byxf;

	private BigDecimal byxs;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="IS_ENABLED")
	private String isEnabled;

	private BigDecimal jhxf;

	private BigDecimal jhxs;

	private String kclbm;

	private String lbdm;

	@Column(name="SPECIALTY_ID")
	private String specialtyId;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name="VERSION")
	private BigDecimal version;

	@Column(name="YX_ID")
	private String yxId;

	public GjtGraduateStandard() {
	}

	public String getReqId() {
		return this.reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public BigDecimal getByxf() {
		return this.byxf;
	}

	public void setByxf(BigDecimal byxf) {
		this.byxf = byxf;
	}

	public BigDecimal getByxs() {
		return this.byxs;
	}

	public void setByxs(BigDecimal byxs) {
		this.byxs = byxs;
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

	public BigDecimal getJhxf() {
		return this.jhxf;
	}

	public void setJhxf(BigDecimal jhxf) {
		this.jhxf = jhxf;
	}

	public BigDecimal getJhxs() {
		return this.jhxs;
	}

	public void setJhxs(BigDecimal jhxs) {
		this.jhxs = jhxs;
	}

	public String getKclbm() {
		return this.kclbm;
	}

	public void setKclbm(String kclbm) {
		this.kclbm = kclbm;
	}

	public String getLbdm() {
		return this.lbdm;
	}

	public void setLbdm(String lbdm) {
		this.lbdm = lbdm;
	}

	public String getSpecialtyId() {
		return this.specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
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

	public String getYxId() {
		return this.yxId;
	}

	public void setYxId(String yxId) {
		this.yxId = yxId;
	}

}