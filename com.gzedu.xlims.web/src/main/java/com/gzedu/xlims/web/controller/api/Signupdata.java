/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.api;

import java.lang.reflect.Field;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年7月22日
 * @version 2.5
 *
 */
public class Signupdata {
	String studentId;//
	String name;//
	String studentNo;// 报读学员的身份证号码
	String sex = "1";// 0:未知 1：男 2：女
	String idNo;//
	String mobile;//
	String batchId;// 参考“读取批次年级专业层次”接口返回
	String gradeId;// 参考“读取批次年级专业层次”接口
	String majorId;// 请参考“读取批次年级专业层次”接口返回
	String levelId;// 请参考“读取批次年级专业层次”接口返回
	String orderSn;//
	String charge;// Y：已缴费 N：未缴费
	String auditSource;// 订单来源有如下项： 互联网OAO 电话中心 学习中心 招生点 微信 其它
	String classType;// A： 精英班 B： 进取班 C：默认班
	String collegeCode;//
	String learncenterCode;//
	String co;//
	String scNo;//
	String className;//
	String preMajor;//
	String preOrg;//

	public Signupdata() {
		super();
	}

	public Signupdata(String studentId, String name, String studentNo, String gradeId, String majorId) {
		super();
		this.studentId = studentId;
		this.name = name;
		this.studentNo = studentNo;
		this.gradeId = gradeId;
		this.majorId = majorId;
	}

	@Override
	public String toString() {
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				System.out.println(field.getName() + ":" + field.get(this));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return super.toString();
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getMajorId() {
		return majorId;
	}

	public void setMajorId(String majorId) {
		this.majorId = majorId;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getAuditSource() {
		return auditSource;
	}

	public void setAuditSource(String auditSource) {
		this.auditSource = auditSource;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getCollegeCode() {
		return collegeCode;
	}

	public void setCollegeCode(String collegeCode) {
		this.collegeCode = collegeCode;
	}

	public String getLearncenterCode() {
		return learncenterCode;
	}

	public void setLearncenterCode(String learncenterCode) {
		this.learncenterCode = learncenterCode;
	}

	public String getCo() {
		return co;
	}

	public void setCo(String co) {
		this.co = co;
	}

	public String getScNo() {
		return scNo;
	}

	public void setScNo(String scNo) {
		this.scNo = scNo;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPreMajor() {
		return preMajor;
	}

	public void setPreMajor(String preMajor) {
		this.preMajor = preMajor;
	}

	public String getPreOrg() {
		return preOrg;
	}

	public void setPreOrg(String preOrg) {
		this.preOrg = preOrg;
	}

}
