/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dto;

import java.util.Date;
import java.util.List;

/**
 * 答疑未解决信息DTO<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月31日
 * @version 2.5
 * @since JDK 1.7
 */
public class FeedbackSolvedDto {

	private String id;

	private String title;

	/**
	 * 咨询的学员姓名
	 */
	private String studentXm;

	/**
	 * 头像
	 */
	private String avatar;

	private Date createdDt;

	private String content;

	private Date answerDealDt;

	/**
	 * 回复内容
	 */
	private String answerContent;

	/**
	 * 回复老师的ID
	 */
	private String answerEmployeeId;

	private String employeeType;

	/**
	 * 回复老师的姓名
	 */
	private String answerEmployeeXm;

	private List<String> studentPictures;
	private List<String> teacherPictures;

	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public FeedbackSolvedDto() {
	}

	public List<String> getStudentPictures() {
		return studentPictures;
	}

	public void setStudentPictures(List<String> studentPictures) {
		this.studentPictures = studentPictures;
	}

	public List<String> getTeacherPictures() {
		return teacherPictures;
	}

	public void setTeacherPictures(List<String> teacherPictures) {
		this.teacherPictures = teacherPictures;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStudentXm() {
		return studentXm;
	}

	public void setStudentXm(String studentXm) {
		this.studentXm = studentXm;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getAnswerDealDt() {
		return answerDealDt;
	}

	public void setAnswerDealDt(Date answerDealDt) {
		this.answerDealDt = answerDealDt;
	}

	public String getAnswerContent() {
		return answerContent;
	}

	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}

	public String getAnswerEmployeeId() {
		return answerEmployeeId;
	}

	public void setAnswerEmployeeId(String answerEmployeeId) {
		this.answerEmployeeId = answerEmployeeId;
	}

	public String getAnswerEmployeeXm() {
		return answerEmployeeXm;
	}

	public void setAnswerEmployeeXm(String answerEmployeeXm) {
		this.answerEmployeeXm = answerEmployeeXm;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

}
