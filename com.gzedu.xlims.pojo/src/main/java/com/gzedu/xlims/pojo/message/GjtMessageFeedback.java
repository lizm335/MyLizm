package com.gzedu.xlims.pojo.message;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the GJT_MESSAGE_FEEDBACK database table.
 * 
 */
@Entity
@Table(name = "GJT_MESSAGE_FEEDBACK")
@NamedQuery(name = "GjtMessageFeedback.findAll", query = "SELECT g FROM GjtMessageFeedback g")
public class GjtMessageFeedback implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String code;

	@Column(name = "FEEDBACK_CONTENT")
	private String feedbackContent;

	@Column(name = "MESSAGE_ID")
	private String messageId;

	private String name;

	@Transient
	private long num;

	@Transient
	private boolean isCheck;

	@Transient
	private String percentage;

	public GjtMessageFeedback() {
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFeedbackContent() {
		return this.feedbackContent;
	}

	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}

	public String getMessageId() {
		return this.messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

}