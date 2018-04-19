package com.gzedu.xlims.web.controller.common.vo;

import java.util.List;

public class LcmsMutualReplyVO {
	private String title;
	private String content;
	private List<String> imgUrls;
	private String id;
	private String pid;
	private String createDt;
	private String teacherName;
	private String teacherType;
	private String teacherPhotoUrl;
	private String studentXm;
	private String type;

	public LcmsMutualReplyVO() {
	};

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStudentXm() {
		return studentXm;
	}

	public void setStudentXm(String studentXm) {
		this.studentXm = studentXm;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherType() {
		return teacherType;
	}

	public void setTeacherType(String teacherType) {
		this.teacherType = teacherType;
	}

	public String getCreateDt() {
		return createDt;
	}

	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(List<String> imgUrls) {
		this.imgUrls = imgUrls;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTeacherPhotoUrl() {
		return teacherPhotoUrl;
	}

	public void setTeacherPhotoUrl(String teacherPhotoUrl) {
		this.teacherPhotoUrl = teacherPhotoUrl;
	}

}
