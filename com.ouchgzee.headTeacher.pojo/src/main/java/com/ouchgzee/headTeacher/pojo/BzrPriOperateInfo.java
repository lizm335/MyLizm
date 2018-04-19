package com.ouchgzee.headTeacher.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the PRI_OPERATE_INFO database table.
 * 
 */
@Entity
@Table(name = "PRI_OPERATE_INFO")
// @NamedQuery(name = "PriOperateInfo.findAll", query = "SELECT p FROM PriOperateInfo p")
@JsonIgnoreProperties(value = { "priModelInfos" })
@Deprecated public class BzrPriOperateInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "OPERATE_ID")
	private String operateId;

	// @Transient
	// @OneToMany(mappedBy = "priOperateInfo")
	// List<PriModelOperate> priModelOperateList;

	@ManyToMany
	@JoinTable(name = "PRI_MODEL_OPERATE", joinColumns = { @JoinColumn(name = "OPERATE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "MODEL_ID") })
	protected List<BzrPriModelInfo> priModelInfos;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Where(clause = "isdeleted='N'")
	private String isdeleted;

	@NotBlank
	@Column(name = "OPERATE_CODE")
	private String operateCode;

	@Column(name = "OPERATE_DEC")
	private String operateDec;

	@NotBlank
	@Column(name = "OPERATE_NAME")
	private String operateName;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	public BzrPriOperateInfo() {
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

	public String getIsdeleted() {
		return this.isdeleted;
	}

	public void setIsdeleted(String isdeleted) {
		this.isdeleted = isdeleted;
	}

	public String getOperateCode() {
		return this.operateCode;
	}

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}

	public String getOperateDec() {
		return this.operateDec;
	}

	public void setOperateDec(String operateDec) {
		this.operateDec = operateDec;
	}

	public String getOperateId() {
		return this.operateId;
	}

	public void setOperateId(String operateId) {
		this.operateId = operateId;
	}

	public String getOperateName() {
		return this.operateName;
	}

	public void setOperateName(String operateName) {
		this.operateName = operateName;
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

	public List<BzrPriModelInfo> getPriModelInfos() {
		return priModelInfos;
	}

	public void setPriModelInfos(List<BzrPriModelInfo> priModelInfos) {
		this.priModelInfos = priModelInfos;
	}

}