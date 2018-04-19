/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.study.web.vo;

import com.gzedu.xlims.pojo.GjtStudentAddress;

/**
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年3月30日
 * @version 3.0
 *
 */
public class GjtStudentAddressVO {
	private String addressId;
	private String receiver;
	private String mobile;
	private String provinceCode;
	private String province;
	private String cityCode;
	private String city;
	private String areaCode;
	private String area;
	private String address;
	private int isDefualt;

	public GjtStudentAddressVO() {
	}

	public GjtStudentAddressVO(GjtStudentAddress studentAddress) {
		this.addressId = studentAddress.getAddressId();
		this.receiver = studentAddress.getReceiver();
		this.mobile = studentAddress.getMobile();
		this.provinceCode = studentAddress.getProvinceCode();
		this.cityCode = studentAddress.getCityCode();
		this.areaCode = studentAddress.getAreaCode();
		this.address = studentAddress.getAddress();
		this.isDefualt = studentAddress.getIsDefault();
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getIsDefualt() {
		return isDefualt;
	}

	public void setIsDefualt(int isDefualt) {
		this.isDefualt = isDefualt;
	}

}
