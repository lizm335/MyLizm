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
 * The persistent class for the GJT_PHOTOGRAPH_ADDRESS database table.
 * 
 */
@Entity
@Table(name = "GJT_PHOTOGRAPH_ADDRESS")
@NamedQuery(name = "GjtPhotographAddress.findAll", query = "SELECT g FROM GjtPhotographAddress g")
public class GjtPhotographAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	private String district;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "IS_ENABLED")
	private String isEnabled;

	@Column(name = "NOTES_REMARK")
	private String notesRemark;

	@Column(name = "ORG_ID")
	private String orgId;

	@Column(name = "PHOTOGRAPH_ADDRESS")
	private String photographAddress;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PHOTOGRAPH_END_DATE")
	private Date photographEndDate;

	@Column(name = "PHOTOGRAPH_NAME")
	private String photographName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PHOTOGRAPH_SRART_DATE")
	private Date photographSrartDate;

	/**
	 * 新华社拍摄点的时间（固定为每天或者每周的某个时间段）
	 */
	@Column(name = "PHOTOGRAPH_DATE")
	private String photographDate;

	private String remark;

	@Column(name = "TELE_PHONE")
	private String telePhone;

	private String type; // 1院校点。2新华社

	@Column(name = "UPDATE_BY")
	private String updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DT")
	private Date updateDt;

	/**
	 * 拍摄地址坐标经讳度值
	 */
	@Column(name = "COORDINATE")
	private String coordinate;

	/**
	 * 较贵状态 0.正常;1.较贵
	 */
	@Column(name = "EXPENSIVE_STU")
	private Integer expensiveStu;

	public GjtPhotographAddress() {
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

	public String getDistrict() {
		return this.district;
	}

	public void setDistrict(String district) {
		this.district = district;
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

	public String getNotesRemark() {
		return this.notesRemark;
	}

	public void setNotesRemark(String notesRemark) {
		this.notesRemark = notesRemark;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPhotographAddress() {
		return this.photographAddress;
	}

	public void setPhotographAddress(String photographAddress) {
		this.photographAddress = photographAddress;
	}

	public Date getPhotographEndDate() {
		return this.photographEndDate;
	}

	public void setPhotographEndDate(Date photographEndDate) {
		this.photographEndDate = photographEndDate;
	}

	public String getPhotographName() {
		return this.photographName;
	}

	public void setPhotographName(String photographName) {
		this.photographName = photographName;
	}

	public Date getPhotographSrartDate() {
		return this.photographSrartDate;
	}

	public void setPhotographSrartDate(Date photographSrartDate) {
		this.photographSrartDate = photographSrartDate;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTelePhone() {
		return this.telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public Integer getExpensiveStu() {
		return expensiveStu;
	}

	public void setExpensiveStu(Integer expensiveStu) {
		this.expensiveStu = expensiveStu;
	}

	public String getPhotographDate() {
		return photographDate;
	}

	public void setPhotographDate(String photographDate) {
		this.photographDate = photographDate;
	}
}