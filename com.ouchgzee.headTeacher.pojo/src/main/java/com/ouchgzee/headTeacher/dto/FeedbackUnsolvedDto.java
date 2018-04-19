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
public class FeedbackUnsolvedDto {

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

	/**
	 * 转发给辅导老师的ID
	 */
	private String shareEmployeeId;

	/**
	 * 转发给辅导老师的姓名
	 */
	private String shareEmployeeXm;

	private List<String> pictures;

	private List<FeedbackSolvedDto> gjtFeedbackList;

	public List<FeedbackSolvedDto> getGjtFeedbackList() {
		return gjtFeedbackList;
	}

	public void setGjtFeedbackList(List<FeedbackSolvedDto> gjtFeedbackList) {
		this.gjtFeedbackList = gjtFeedbackList;
	}

	public FeedbackUnsolvedDto() {
	}

	public List<String> getPictures() {
		return pictures;
	}

	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
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

	public String getShareEmployeeId() {
		return shareEmployeeId;
	}

	public void setShareEmployeeId(String shareEmployeeId) {
		this.shareEmployeeId = shareEmployeeId;
	}

	public String getShareEmployeeXm() {
		return shareEmployeeXm;
	}

	public void setShareEmployeeXm(String shareEmployeeXm) {
		this.shareEmployeeXm = shareEmployeeXm;
	}
}
