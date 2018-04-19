package com.ouchgzee.headTeacher.pojo;

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 班主任服务信息实体类<br>
 * The persistent class for the GJT_SERVICE_INFO database table.
 * 
 */
@Entity
@Table(name="GJT_SERVICE_INFO")
// @NamedQuery(name="GjtServiceInfo.findAll", query="SELECT g FROM GjtServiceInfo g")
@Deprecated public class BzrGjtServiceInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String serviceid;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name="GJT_SERVICE_STUDENT", joinColumns={@JoinColumn(name="serviceid")}, inverseJoinColumns={@JoinColumn(name="STUDENT_ID")})
	private List<BzrGjtStudentInfo> gjtStudentInfoList;
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name="serviceid")
	@Where(clause = "IS_DELETED='N'")
	@OrderBy(clause = "CREATED_DT DESC")
	private List<BzrGjtServiceRecord> gjtServiceRecordList;

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

	private String status;

	private String title;

	private int totaltime;

	@Column(name="UPDATED_BY",insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT",insertable = false)
	private Date updatedDt;

	/**
	 * 临时数据：服务次数
	 */
	@Transient
	private Long colRecordNum;

	public BzrGjtServiceInfo() {
	}

	public BzrGjtServiceInfo(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getServiceid() {
		return this.serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTotaltime() {
		return this.totaltime;
	}

	public void setTotaltime(int totaltime) {
		this.totaltime = totaltime;
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

	/**
	 * get 临时数据：服务次数
	 *
	 * @return the colRecordNum
	 */
	public Long getColRecordNum() {
		return colRecordNum;
	}

	/**
	 * set 临时数据：服务次数
	 *
	 * @param colRecordNum
	 *            the colRecordNum to set
	 */
	public void setColRecordNum(Long colRecordNum) {
		this.colRecordNum = colRecordNum;
	}

	public List<BzrGjtStudentInfo> getGjtStudentInfoList() {
		return gjtStudentInfoList;
	}

	public void setGjtStudentInfoList(List<BzrGjtStudentInfo> gjtStudentInfoList) {
		this.gjtStudentInfoList = gjtStudentInfoList;
	}

	public List<BzrGjtServiceRecord> getGjtServiceRecordList() {
		return gjtServiceRecordList;
	}

	public void setGjtServiceRecordList(List<BzrGjtServiceRecord> gjtServiceRecordList) {
		this.gjtServiceRecordList = gjtServiceRecordList;
	}

	public BzrGjtServiceRecord addGjtServiceRecordList(BzrGjtServiceRecord gjtServiceRecord) {
		getGjtServiceRecordList().add(gjtServiceRecord);
		gjtServiceRecord.setGjtServiceInfo(this);

		return gjtServiceRecord;
	}

	public BzrGjtServiceRecord removeGjtServiceRecordList(BzrGjtServiceRecord gjtServiceRecord) {
		getGjtServiceRecordList().remove(gjtServiceRecord);
		gjtServiceRecord.setGjtServiceInfo(null);

		return gjtServiceRecord;
	}

}