package com.ouchgzee.study.web.vo.practice;

import java.util.Date;

public class PracticePlanVO {
	
	private String practicePlanId;
	
	private String practicePlanCode;
	
	private String practicePlanName;
	
	private Date applyBeginDt;
	
	private Date applyEndDt;

	private Date submitPracticeEndDt;

	private Date confirmPracticeEndDt;

	private Date reviewDt;
	
	private String systemDt;

	private int status = 0;

	public String getPracticePlanId() {
		return practicePlanId;
	}

	public void setPracticePlanId(String practicePlanId) {
		this.practicePlanId = practicePlanId;
	}

	public String getPracticePlanCode() {
		return practicePlanCode;
	}

	public void setPracticePlanCode(String practicePlanCode) {
		this.practicePlanCode = practicePlanCode;
	}

	public String getPracticePlanName() {
		return practicePlanName;
	}

	public void setPracticePlanName(String practicePlanName) {
		this.practicePlanName = practicePlanName;
	}

	public Date getApplyBeginDt() {
		return applyBeginDt;
	}

	public void setApplyBeginDt(Date applyBeginDt) {
		this.applyBeginDt = applyBeginDt;
	}

	public Date getApplyEndDt() {
		return applyEndDt;
	}

	public void setApplyEndDt(Date applyEndDt) {
		this.applyEndDt = applyEndDt;
	}

	public Date getSubmitPracticeEndDt() {
		return submitPracticeEndDt;
	}

	public void setSubmitPracticeEndDt(Date submitPracticeEndDt) {
		this.submitPracticeEndDt = submitPracticeEndDt;
	}

	public Date getConfirmPracticeEndDt() {
		return confirmPracticeEndDt;
	}

	public void setConfirmPracticeEndDt(Date confirmPracticeEndDt) {
		this.confirmPracticeEndDt = confirmPracticeEndDt;
	}

	public Date getReviewDt() {
		return reviewDt;
	}

	public void setReviewDt(Date reviewDt) {
		this.reviewDt = reviewDt;
	}

	public String getSystemDt() {
		return systemDt;
	}

	public void setSystemDt(String systemDt) {
		this.systemDt = systemDt;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
