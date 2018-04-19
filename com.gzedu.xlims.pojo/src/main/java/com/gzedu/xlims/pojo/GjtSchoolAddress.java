package com.gzedu.xlims.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the GJT_SCHOOL_ADDRESS database table. 院校收货地址表
 */
@Entity
@Table(name = "GJT_SCHOOL_ADDRESS")
@NamedQuery(name = "GjtSchoolAddress.findAll", query = "SELECT g FROM GjtSchoolAddress g")
public class GjtSchoolAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String addressId;
	private String xxId;
	private Integer type; // 收货地址类型 1-毕业生登记表收货地址
	private String mobile; // 收货人手机号
	private String receiver;// 收货人名字
	private String provinceCode;
	private String cityCode;
	private String areaCode;
	private String address; // 收货人详细地址
	private String postcode; // 邮政编码

	@Column(insertable = false)
	private String isDeleted;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(insertable = false, updatable = false)
	private Date createdDt;

	@Column(updatable = false)
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(insertable = false)
	private Date updatedDt;
	
	@Column(insertable = false)
	private String updatedBy;

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
}