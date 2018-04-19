package com.gzedu.xlims.pojo.textbook;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the GJT_TEXTBOOK_FEEDBACK_DETAIL database table.
 * 
 */
@Entity
@Table(name="GJT_TEXTBOOK_FEEDBACK_DETAIL")
@NamedQuery(name="GjtTextbookFeedbackDetail.findAll", query="SELECT g FROM GjtTextbookFeedbackDetail g")
public class GjtTextbookFeedbackDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DETAIL_ID")
	private String detailId;

	@Column(name="TEXTBOOK_ID")
	private String textbookId;

	//bi-directional many-to-one association to GjtTextbook
	@ManyToOne
	@JoinColumn(name="TEXTBOOK_ID", insertable=false, updatable=false)
	private GjtTextbook gjtTextbook;

	//bi-directional many-to-one association to GjtTextbookFeedback
	@ManyToOne
	@JoinColumn(name="FEEDBACK_ID")
	private GjtTextbookFeedback gjtTextbookFeedback;

	public GjtTextbookFeedbackDetail() {
	}

	public String getDetailId() {
		return this.detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getTextbookId() {
		return textbookId;
	}

	public void setTextbookId(String textbookId) {
		this.textbookId = textbookId;
	}

	public GjtTextbook getGjtTextbook() {
		return this.gjtTextbook;
	}

	public void setGjtTextbook(GjtTextbook gjtTextbook) {
		this.gjtTextbook = gjtTextbook;
	}

	public GjtTextbookFeedback getGjtTextbookFeedback() {
		return this.gjtTextbookFeedback;
	}

	public void setGjtTextbookFeedback(GjtTextbookFeedback gjtTextbookFeedback) {
		this.gjtTextbookFeedback = gjtTextbookFeedback;
	}

}