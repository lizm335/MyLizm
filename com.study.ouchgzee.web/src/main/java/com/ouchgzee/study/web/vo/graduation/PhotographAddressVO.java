/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.vo.graduation;

import com.gzedu.xlims.pojo.graduation.GjtPhotographData;

import java.util.Date;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月29日
 * @version 3.0
 *
 */
public class PhotographAddressVO {
	private String id;
	private String notesRemark;
	private String photographAddress;
	private Date photographEndDate;
	private String photographName;
	private Date photographSrartDate;
	private String telePhone;
	private String type;
	private String district;
	private String coordinate;//经讳度值
	private int expensiveStu;
	private String isAppointment;//0未预约;1.已预约

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNotesRemark() {
		return notesRemark;
	}

	public void setNotesRemark(String notesRemark) {
		this.notesRemark = notesRemark;
	}

	public String getPhotographAddress() {
		return photographAddress;
	}

	public void setPhotographAddress(String photographAddress) {
		this.photographAddress = photographAddress;
	}

	public Date getPhotographEndDate() {
		return photographEndDate;
	}

	public void setPhotographEndDate(Date photographEndDate) {
		this.photographEndDate = photographEndDate;
	}

	public String getPhotographName() {
		return photographName;
	}

	public void setPhotographName(String photographName) {
		this.photographName = photographName;
	}

	public Date getPhotographSrartDate() {
		return photographSrartDate;
	}

	public void setPhotographSrartDate(Date photographSrartDate) {
		this.photographSrartDate = photographSrartDate;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public int getExpensiveStu() {
		return expensiveStu;
	}

	public void setExpensiveStu(int expensiveStu) {
		this.expensiveStu = expensiveStu;
	}

	public String getIsAppointment() {
		return isAppointment;
	}

	public void setIsAppointment(String isAppointment) {
		this.isAppointment = isAppointment;
	}
}
