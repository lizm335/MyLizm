package com.ouchgzee.study.web.vo.thesis;

import java.util.Date;

public class ThesisPlanVO {
	
	private String thesisPlanId;
	
	private String thesisPlanCode;
	
	private String thesisPlanName;
	
	private Date applyBeginDt;
	
	private Date applyEndDt;

	private Date submitProposeEndDt;

	private Date confirmProposeEndDt;

	private Date submitThesisEndDt;

	private Date confirmThesisEndDt;

	private Date reviewDt;

	private Date defenceDt;
	
	private String systemDt;
	
	private int status = 0;

	public String getThesisPlanId() {
		return thesisPlanId;
	}

	public void setThesisPlanId(String thesisPlanId) {
		this.thesisPlanId = thesisPlanId;
	}

	public String getThesisPlanCode() {
		return thesisPlanCode;
	}

	public void setThesisPlanCode(String thesisPlanCode) {
		this.thesisPlanCode = thesisPlanCode;
	}

	public String getThesisPlanName() {
		return thesisPlanName;
	}

	public void setThesisPlanName(String thesisPlanName) {
		this.thesisPlanName = thesisPlanName;
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

	public Date getSubmitProposeEndDt() {
		return submitProposeEndDt;
	}

	public void setSubmitProposeEndDt(Date submitProposeEndDt) {
		this.submitProposeEndDt = submitProposeEndDt;
	}

	public Date getConfirmProposeEndDt() {
		return confirmProposeEndDt;
	}

	public void setConfirmProposeEndDt(Date confirmProposeEndDt) {
		this.confirmProposeEndDt = confirmProposeEndDt;
	}

	public Date getSubmitThesisEndDt() {
		return submitThesisEndDt;
	}

	public void setSubmitThesisEndDt(Date submitThesisEndDt) {
		this.submitThesisEndDt = submitThesisEndDt;
	}

	public Date getConfirmThesisEndDt() {
		return confirmThesisEndDt;
	}

	public void setConfirmThesisEndDt(Date confirmThesisEndDt) {
		this.confirmThesisEndDt = confirmThesisEndDt;
	}

	public Date getReviewDt() {
		return reviewDt;
	}

	public void setReviewDt(Date reviewDt) {
		this.reviewDt = reviewDt;
	}

	public Date getDefenceDt() {
		return defenceDt;
	}

	public void setDefenceDt(Date defenceDt) {
		this.defenceDt = defenceDt;
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
