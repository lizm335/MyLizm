package com.gzedu.xlims.service.vo;

public class Todo {

	/**
	 * 编号 10-答疑服务 20-学期计划 30-学籍计划制定 40-学籍计划审核 50-学籍计划审核不通过 60-学籍待注册 70-考试计划制定
	 * 80-考试计划审核不通过 90-考试计划审核 100-开考科目 110-排考安排 120-教材计划制定 130-教材计划审核不通过
	 * 140-教材计划审核 150-教材库存不足 160-教材订购审核不通过 170-教材订购审核 180-学员反馈 190-开设专业
	 * 200-分配班主任 210-分配辅导老师 220-分配督导老师 230-毕业计划制定 240-分配论文指导老师 250-分配论文答辩老师
	 * 260-分配社会实践老师 270-我的工单 280-我的计划 290- 300-直播  310-学籍异动(退学)
	 */
	private Integer code;

	private long total;

	private String title;

	private String content;

	private String link;

	public Todo(String title, String content, String link) {
		this.title = title;
		this.content = content;
		this.link = link;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
