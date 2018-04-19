package com.ouchgzee.study.web.vo;

import java.util.ArrayList;
import java.util.List;

public class AdminssionVo {
	
	private String studentId;
	private String admissionZkz;
	private String rId; // office2007图片rId
	private String stuPhoto;
	private String admissionName;
	private String stuNumber;
	private String examPointName;
	private String examAddress;
	private String type;
	private List admissionList = new ArrayList();
	
	public AdminssionVo(){
		
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getAdmissionZkz() {
		return admissionZkz;
	}

	public void setAdmissionZkz(String admissionZkz) {
		this.admissionZkz = admissionZkz;
	}

	/**
	 * @return the rId
	 */
	public String getrId() {
		return rId;
	}

	/**
	 * @param rId the rId to set
	 */
	public void setrId(String rId) {
		this.rId = rId;
	}

	public String getStuPhoto() {
		return stuPhoto;
	}

	public void setStuPhoto(String stuPhoto) {
		this.stuPhoto = stuPhoto;
	}

	public String getAdmissionName() {
		return admissionName;
	}

	public void setAdmissionName(String admissionName) {
		this.admissionName = admissionName;
	}

	public String getStuNumber() {
		return stuNumber;
	}

	public void setStuNumber(String stuNumber) {
		this.stuNumber = stuNumber;
	}

	public String getExamPointName() {
		return examPointName;
	}

	public void setExamPointName(String examPointName) {
		this.examPointName = examPointName;
	}

	public String getExamAddress() {
		return examAddress;
	}

	public void setExamAddress(String examAddress) {
		this.examAddress = examAddress;
	}

	public List getAdmissionList() {
		return admissionList;
	}

	public void setAdmissionList(List admissionList) {
		this.admissionList = admissionList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

}
