package com.ouchgzee.study.web.vo;

import java.util.Map;

public class SignInfoVo {
	/** 报读信息 */
	// 报读时间 
	 private String createdDt;
	// 院校
	private String schoolName;
	// 年级 
	private String className;
	// 专业
	private String specialty;
	// 层次
	private String pycc;
	// 学习方式(2.5年制)
	private String studyPeriod;
	// 姓名
	private String xm;
	// 证件类型
	private String certificatetype;
	// 证件号 
	private String sfzh;
	// 手机号
	private String sjh;
	// 邮箱
	private String dzxx;
	// 服务机构
	private String orgName;
	// 报读状态
	private String enrollStatus;
	// 学籍状态
	private String xjzt;
	
	// 学费及优惠政策信息
	private Map<String,Object> policyMap;
	
	private String eeUrl;
	
	
	public String getCreatedDt() {
		return createdDt;
	}
	public void setCreatedDt(String createdDt) {
		this.createdDt = createdDt;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	public String getPycc() {
		return pycc;
	}
	public void setPycc(String pycc) {
		this.pycc = pycc;
	}
	public String getStudyPeriod() {
		return studyPeriod;
	}
	public void setStudyPeriod(String studyPeriod) {
		this.studyPeriod = studyPeriod;
	}
	public String getXm() {
		return xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
	public String getCertificatetype() {
		return certificatetype;
	}
	public void setCertificatetype(String certificatetype) {
		this.certificatetype = certificatetype;
	}
	public String getSfzh() {
		return sfzh;
	}
	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}
	public String getSjh() {
		return sjh;
	}
	public void setSjh(String sjh) {
		this.sjh = sjh;
	}
	public String getDzxx() {
		return dzxx;
	}
	public void setDzxx(String dzxx) {
		this.dzxx = dzxx;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getEnrollStatus() {
		return enrollStatus;
	}
	public void setEnrollStatus(String enrollStatus) {
		this.enrollStatus = enrollStatus;
	}
	public String getXjzt() {
		return xjzt;
	}
	public void setXjzt(String xjzt) {
		this.xjzt = xjzt;
	}
	public Map<String, Object> getPolicyMap() {
		return policyMap;
	}
	public void setPolicyMap(Map<String, Object> policyMap) {
		this.policyMap = policyMap;
	}
	public String getEeUrl() {
		return eeUrl;
	}
	public void setEeUrl(String eeUrl) {
		this.eeUrl = eeUrl;
	}
	
}
