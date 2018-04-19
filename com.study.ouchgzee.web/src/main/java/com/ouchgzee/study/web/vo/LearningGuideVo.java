package com.ouchgzee.study.web.vo;

import com.gzedu.xlims.pojo.GjtArticle;

public class LearningGuideVo {
	private String title = ""; // 文章标题
	private String content = ""; // 文章内容
	private String fileName=""; // 附件名称
	private String fileUrl=""; // 附件url
	private int no;// 次序

	public LearningGuideVo(GjtArticle gjtArticle) {
		super();
		this.content = gjtArticle.getContent();
		this.title = gjtArticle.getTitle();
		this.fileName = gjtArticle.getFileName();
		this.fileUrl = gjtArticle.getFileUrl();
	}
	public LearningGuideVo() {
		super();
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

}
