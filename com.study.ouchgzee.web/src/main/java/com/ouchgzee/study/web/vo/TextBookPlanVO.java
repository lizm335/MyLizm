/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.study.web.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年4月26日
 * @version 3.0
 *
 */
public class TextBookPlanVO {

	private String gradeName;
	private String termName;
	private String planCode;
	private String planName;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date arrangeSdate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date arrangeEdate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date orderEdate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date orderSdate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date addressConfirmSdate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date addressConfirmEdate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date distributeSdate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date distributeEdate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date feedbackSdate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date feedbackEdate;

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getTermName() {
		return termName;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public Date getArrangeSdate() {
		return arrangeSdate;
	}

	public void setArrangeSdate(Date arrangeSdate) {
		this.arrangeSdate = arrangeSdate;
	}

	public Date getArrangeEdate() {
		return arrangeEdate;
	}

	public void setArrangeEdate(Date arrangeEdate) {
		this.arrangeEdate = arrangeEdate;
	}

	public Date getOrderEdate() {
		return orderEdate;
	}

	public void setOrderEdate(Date orderEdate) {
		this.orderEdate = orderEdate;
	}

	public Date getOrderSdate() {
		return orderSdate;
	}

	public void setOrderSdate(Date orderSdate) {
		this.orderSdate = orderSdate;
	}

	public Date getAddressConfirmSdate() {
		return addressConfirmSdate;
	}

	public void setAddressConfirmSdate(Date addressConfirmSdate) {
		this.addressConfirmSdate = addressConfirmSdate;
	}

	public Date getAddressConfirmEdate() {
		return addressConfirmEdate;
	}

	public void setAddressConfirmEdate(Date addressConfirmEdate) {
		this.addressConfirmEdate = addressConfirmEdate;
	}

	public Date getDistributeSdate() {
		return distributeSdate;
	}

	public void setDistributeSdate(Date distributeSdate) {
		this.distributeSdate = distributeSdate;
	}

	public Date getDistributeEdate() {
		return distributeEdate;
	}

	public void setDistributeEdate(Date distributeEdate) {
		this.distributeEdate = distributeEdate;
	}

	public Date getFeedbackSdate() {
		return feedbackSdate;
	}

	public void setFeedbackSdate(Date feedbackSdate) {
		this.feedbackSdate = feedbackSdate;
	}

	public Date getFeedbackEdate() {
		return feedbackEdate;
	}

	public void setFeedbackEdate(Date feedbackEdate) {
		this.feedbackEdate = feedbackEdate;
	}

}
