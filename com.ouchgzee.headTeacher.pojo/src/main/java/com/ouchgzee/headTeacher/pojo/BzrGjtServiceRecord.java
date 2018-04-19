package com.ouchgzee.headTeacher.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 班主任服务记录信息实体类<br>
 * The persistent class for the GJT_SERVICE_RECORD database table.
 * 
 */
@Entity
@Table(name="GJT_SERVICE_RECORD")
// @NamedQuery(name="GjtServiceRecord.findAll", query="SELECT g FROM GjtServiceRecord g")
@Deprecated public class BzrGjtServiceRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SERVICE_RECORD_ID")
	private String serviceRecordId;

	@ManyToOne
	@JoinColumn(name = "serviceid")
	private BzrGjtServiceInfo gjtServiceInfo;

	private String content;

	@Column(name="CREATED_BY",updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT",insertable = false,updatable = false)
	private Date createdDt;

	@Column(name="DELETED_BY",insertable = false)
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED_DT",insertable = false)
	private Date deletedDt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endtime;

	@Column(name="IS_DELETED",insertable = false)
	private String isDeleted;

	@Temporal(TemporalType.TIMESTAMP)
	private Date starttime;

	private int taketime;

	@Column(name="UPDATED_BY",insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT",insertable = false)
	private Date updatedDt;

	private String way;

	public BzrGjtServiceRecord() {
	}

	public String getServiceRecordId() {
		return this.serviceRecordId;
	}

	public void setServiceRecordId(String serviceRecordId) {
		this.serviceRecordId = serviceRecordId;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Date getEndtime() {
		return this.endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getStarttime() {
		return this.starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public int getTaketime() {
		return this.taketime;
	}

	public void setTaketime(int taketime) {
		this.taketime = taketime;
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

	public String getWay() {
		return this.way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public BzrGjtServiceInfo getGjtServiceInfo() {
		return gjtServiceInfo;
	}

	public void setGjtServiceInfo(BzrGjtServiceInfo gjtServiceInfo) {
		this.gjtServiceInfo = gjtServiceInfo;
	}
}