package com.gzedu.xlims.pojo.graduation;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the GJT_PHOTOGRAPH_DATA database table.
 * 
 */
@Entity
@Table(name = "GJT_PHOTOGRAPH_DATA")
@NamedQuery(name = "GjtPhotographData.findAll", query = "SELECT g FROM GjtPhotographData g")
public class GjtPhotographData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "IN_SCHOOL")
	private String inSchool;

	private String recipients;

	@Column(name = "RECIPIENTS_ADD")
	private String recipientsAdd;

	@Column(name = "SCHOOL_CODE")
	private String schoolCode;

	@Column(name = "SCHOOL_SET_CODE")
	private String schoolSetCode;

	@Column(name = "TELE_PHONE")
	private String telePhone;

	@Column(name = "UPDATE_BY")
	private String updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DT")
	private Date updateDt;

	@Column(name = "XX_ID")
	private String xxId;

	public GjtPhotographData() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getInSchool() {
		return this.inSchool;
	}

	public void setInSchool(String inSchool) {
		this.inSchool = inSchool;
	}

	public String getRecipients() {
		return this.recipients;
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	public String getRecipientsAdd() {
		return this.recipientsAdd;
	}

	public void setRecipientsAdd(String recipientsAdd) {
		this.recipientsAdd = recipientsAdd;
	}

	public String getSchoolCode() {
		return this.schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	public String getSchoolSetCode() {
		return this.schoolSetCode;
	}

	public void setSchoolSetCode(String schoolSetCode) {
		this.schoolSetCode = schoolSetCode;
	}

	public String getTelePhone() {
		return this.telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDt() {
		return this.updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

}