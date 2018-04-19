package com.ouchgzee.study.web.vo.practice;

public class PracticeApplyVO {

	private String applyId;

	private String guideTeacherId = "";

	private String guideTeacherName = "";

	private String guideTeacherZp = "";

	private Float reviewScore = 0f;

	private Float score = 0f;

	private String expressCompany = "";

	private String expressNumber = "";

	private int status;

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getGuideTeacherId() {
		return guideTeacherId;
	}

	public void setGuideTeacherId(String guideTeacherId) {
		this.guideTeacherId = guideTeacherId;
	}

	public String getGuideTeacherName() {
		return guideTeacherName;
	}

	public void setGuideTeacherName(String guideTeacherName) {
		this.guideTeacherName = guideTeacherName;
	}

	public String getGuideTeacherZp() {
		return guideTeacherZp;
	}

	public void setGuideTeacherZp(String guideTeacherZp) {
		this.guideTeacherZp = guideTeacherZp;
	}

	public Float getReviewScore() {
		return reviewScore;
	}

	public void setReviewScore(Float reviewScore) {
		this.reviewScore = reviewScore;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getExpressNumber() {
		return expressNumber;
	}

	public void setExpressNumber(String expressNumber) {
		this.expressNumber = expressNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
