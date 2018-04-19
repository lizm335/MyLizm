/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.remote;

/**
 * 缴费单信息
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年10月25日
 * @version 3.0
 */
public class OrderBillInfo {

	private String order_bills_sn; // 缴费单号
	private Integer bill_type; // 缴费类型 转专业差价-4 考试费-6 教材费-7
	private String bill_type_remark; // 缴费标题
	private Integer pay_type; // 支付类型
	private Integer pay_status; // 支付状态 1已支付 0未支付
	private String order_amount; // 订单金额，单位：元
	private String pay_amount; // 支付金额，单位：元
	private String discount_price;
	private String charge_price;
	private String real_price;
	private long pay_time; // 支付时间，单位秒
	private String order_sn;

	public String getOrder_bills_sn() {
		return order_bills_sn;
	}

	public void setOrder_bills_sn(String order_bills_sn) {
		this.order_bills_sn = order_bills_sn;
	}

	public Integer getBill_type() {
		return bill_type;
	}

	public void setBill_type(Integer bill_type) {
		this.bill_type = bill_type;
	}

	public String getBill_type_remark() {
		return bill_type_remark;
	}

	public void setBill_type_remark(String bill_type_remark) {
		this.bill_type_remark = bill_type_remark;
	}

	public Integer getPay_type() {
		return pay_type;
	}

	public void setPay_type(Integer pay_type) {
		this.pay_type = pay_type;
	}

	public Integer getPay_status() {
		return pay_status;
	}

	public void setPay_status(Integer pay_status) {
		this.pay_status = pay_status;
	}

	public String getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}

	public String getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(String pay_amount) {
		this.pay_amount = pay_amount;
	}

	public String getDiscount_price() {
		return discount_price;
	}

	public void setDiscount_price(String discount_price) {
		this.discount_price = discount_price;
	}

	public String getCharge_price() {
		return charge_price;
	}

	public void setCharge_price(String charge_price) {
		this.charge_price = charge_price;
	}

	public String getReal_price() {
		return real_price;
	}

	public void setReal_price(String real_price) {
		this.real_price = real_price;
	}

	public long getPay_time() {
		return pay_time;
	}

	public void setPay_time(long pay_time) {
		this.pay_time = pay_time;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
}
