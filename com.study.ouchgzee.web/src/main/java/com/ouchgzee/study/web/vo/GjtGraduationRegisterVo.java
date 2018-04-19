package com.ouchgzee.study.web.vo;

import java.util.List;

/**
 * 毕业申请
 * 
 * @author lyj
 * @time 2017年4月1日 TODO
 */
public class GjtGraduationRegisterVo {

	private String studentId;// 学生id
	private String registerId;// 毕业申请注册id
	private int isUpdate; // 是否可以更新，默认0 1-是 0-否
	/**
	 * 学籍信息
	 */
	private String xm;// 姓名
	private String xbm;// 姓别
	private String xh;// 学号
	private String csrq; // 出生日期
	private String mzm; // 民族
	private String politicsstatus; // 政治面貌
	private String certificatetype; // 证件类型
	private String sfzh; // 证件号
	private String specialtyCategory; // 学生类别(1:开放教育专业,2:助力计划专业,3:开放教育专科)
	private String exedulevel; // 学历层次
	private String zymc; // 专业
	private String gradeName; // 入学时间
	private String graduationTime;// 毕业时间
	
	/**
	 * 毕业申请信息
	 */
	private String scCo;// 工作单位
	private String scCoPhone;// 单位电话
	private String sjh;// 手机号码
	private String homePhone;// 家庭电话
	private String dzxx; // E-mail
	private String erpRegistrationNumber;//电子注册号
	private String photo;// 头像url
	
	// 主要学习经历
	private List<EduLiveVo> eduLiveList;
	// 毕业实习单位及主要内容
	private String practiceContent;
	// 毕业论文及毕业设计题目
	private String graduationDesign;
	// 在校期间受过何种奖励和处分
	private String awardRecord;
	// 自我鉴定
	private String evaluation;

	/**
	 * 快递公司
	 */
	private String expressCompany;

	/**
	 * 快递单号
	 */
	private String expressNumber;

	//提交类型 1/保存  2/提交
	private String submitType;

	private String mobile; // 收货人手机号
	private String receiver;// 收货人名字
	private String postcode; // 邮政编码
	private String address; // 收货人详细地址
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getRegisterId() {
		return registerId;
	}
	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}
	public String getXm() {
		return xm;
	}
	public void setXm(String xm) {
		this.xm = xm;
	}
	public String getXbm() {
		return xbm;
	}
	public void setXbm(String xbm) {
		this.xbm = xbm;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getCsrq() {
		return csrq;
	}
	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}
	public String getMzm() {
		return mzm;
	}
	public void setMzm(String mzm) {
		this.mzm = mzm;
	}
	public String getPoliticsstatus() {
		return politicsstatus;
	}
	public void setPoliticsstatus(String politicsstatus) {
		this.politicsstatus = politicsstatus;
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
	public String getSpecialtyCategory() {
		return specialtyCategory;
	}
	public void setSpecialtyCategory(String specialtyCategory) {
		this.specialtyCategory = specialtyCategory;
	}
	
	public String getExedulevel() {
		return exedulevel;
	}
	public void setExedulevel(String exedulevel) {
		this.exedulevel = exedulevel;
	}
	public String getZymc() {
		return zymc;
	}
	public void setZymc(String zymc) {
		this.zymc = zymc;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getGraduationTime() {
		return graduationTime;
	}
	public void setGraduationTime(String graduationTime) {
		this.graduationTime = graduationTime;
	}
	public String getScCo() {
		return scCo;
	}
	public void setScCo(String scCo) {
		this.scCo = scCo;
	}
	public String getScCoPhone() {
		return scCoPhone;
	}
	public void setScCoPhone(String scCoPhone) {
		this.scCoPhone = scCoPhone;
	}
	public String getSjh() {
		return sjh;
	}
	public void setSjh(String sjh) {
		this.sjh = sjh;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getDzxx() {
		return dzxx;
	}
	public void setDzxx(String dzxx) {
		this.dzxx = dzxx;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getPracticeContent() {
		return practiceContent;
	}
	public void setPracticeContent(String practiceContent) {
		this.practiceContent = practiceContent;
	}
	public String getGraduationDesign() {
		return graduationDesign;
	}
	public void setGraduationDesign(String graduationDesign) {
		this.graduationDesign = graduationDesign;
	}
	public String getAwardRecord() {
		return awardRecord;
	}
	public void setAwardRecord(String awardRecord) {
		this.awardRecord = awardRecord;
	}
	public String getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}
	public List<EduLiveVo> getEduLiveList() {
		return eduLiveList;
	}
	public void setEduLiveList(List<EduLiveVo> eduLiveList) {
		this.eduLiveList = eduLiveList;
	}

	public int getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(int isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getSubmitType() {
		return submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getErpRegistrationNumber() {
		return erpRegistrationNumber;
	}
	public void setErpRegistrationNumber(String erpRegistrationNumber) {
		this.erpRegistrationNumber = erpRegistrationNumber;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getExpressNumber() {
		return expressNumber;
	}

	public void setExpressNumber(String expressNumber) {
		this.expressNumber = expressNumber;
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

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
