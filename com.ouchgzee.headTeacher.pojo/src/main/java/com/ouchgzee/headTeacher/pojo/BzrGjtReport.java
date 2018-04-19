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
 * The persistent class for the GJT_REPORT database table.
 * 
 */
@Entity
@Table(name = "GJT_REPORT")
// @NamedQuery(name = "GjtReport.findAll", query = "SELECT g FROM GjtReport g")
@Deprecated public class BzrGjtReport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BEGIN_TIME")
	private Date beginTime;

	@Column(name = "COMMENTENT_ID")
	private String commententId;

	@Column(name = "COMMENTENT_NAME")
	private String commententName;

	@Lob
	private String comments;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME")
	private Date endTime;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	@Lob
	private String nextplan;

	@Column(name = "REPORT_TYPE")
	private BigDecimal reportType;

	@Lob
	private String summary;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	public BzrGjtReport() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public String getCommententId() {
		return this.commententId;
	}

	public void setCommententId(String commententId) {
		this.commententId = commententId;
	}

	public String getCommententName() {
		return this.commententName;
	}

	public void setCommententName(String commententName) {
		this.commententName = commententName;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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

	public String getNextplan() {
		return this.nextplan;
	}

	public void setNextplan(String nextplan) {
		this.nextplan = nextplan;
	}

	public BigDecimal getReportType() {
		return this.reportType;
	}

	public void setReportType(BigDecimal reportType) {
		this.reportType = reportType;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"beginTime\":\"" + beginTime
				+ "\", \"commententId\":\"" + commententId
				+ "\", \"commententName\":\"" + commententName
				+ "\", \"comments\":\"" + comments + "\", \"createdBy\":\""
				+ createdBy + "\", \"createdDt\":\"" + createdDt
				+ "\", \"endTime\":\"" + endTime + "\", \"nextplan\":\""
				+ nextplan + "\", \"reportType\":\"" + reportType
				+ "\", \"summary\":\"" + summary + "\", \"updatedBy\":\""
				+ updatedBy + "\", \"updatedDt\":\"" + updatedDt + "\"}";
	}

}