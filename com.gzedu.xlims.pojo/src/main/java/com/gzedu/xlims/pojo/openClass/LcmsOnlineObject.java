package com.gzedu.xlims.pojo.openClass;

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
 * The persistent class for the LCMS_ONLINE_OBJECT database table.
 * 
 */
@Entity
@Table(name="LCMS_ONLINE_OBJECT")
@NamedQuery(name="LcmsOnlineObject.findAll", query="SELECT l FROM LcmsOnlineObject l")
public class LcmsOnlineObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ONLINE_OBJECT_ID")
	private String onlineObjectId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="OBJECT_ID")
	private String objectId;

	@Column(name="OBJECT_TYPE")
	private String objectType;// 直播对象类型 1 课程班学员 2 课程班 3 期课程 4 年级 5 专业 6 院校
								// 7教学班学员 8层次 9课程

	@Column(name="ONLINETUTOR_ID")
	private String onlinetutorId;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	public LcmsOnlineObject() {
	}

	public String getOnlineObjectId() {
		return this.onlineObjectId;
	}

	public void setOnlineObjectId(String onlineObjectId) {
		this.onlineObjectId = onlineObjectId;
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

	public String getObjectId() {
		return this.objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getOnlinetutorId() {
		return this.onlinetutorId;
	}

	public void setOnlinetutorId(String onlinetutorId) {
		this.onlinetutorId = onlinetutorId;
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

}