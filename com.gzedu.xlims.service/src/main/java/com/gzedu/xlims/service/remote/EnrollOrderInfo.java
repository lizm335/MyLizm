/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.remote;

/**
 * 订单信息
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年11月03日
 * @version 3.0
 */
public class EnrollOrderInfo {

	private String order_id; // 订单ID
	private String user_name; // 姓名
	private String identity_card; // 身份证号
	private String product_name; // 产品名称
	private String order_sn; // 订单号
	private String order_amount; // 订单金额
	private Integer order_status; // 订单状态 1-已支付
	private String payment_way_code; // 缴费类型 XFY-学付易 XNXQ-按年缴费 AXQJF-按学期缴费 QEJF-全额缴费
	private String payment_way_name; // 缴费类型名称
	private String order_remark; // 备注
	private String create_time; // 订单时间

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getIdentity_card() {
		return identity_card;
	}

	public void setIdentity_card(String identity_card) {
		this.identity_card = identity_card;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}

	public Integer getOrder_status() {
		return order_status;
	}

	public void setOrder_status(Integer order_status) {
		this.order_status = order_status;
	}

	public String getPayment_way_code() {
		return payment_way_code;
	}

	public void setPayment_way_code(String payment_way_code) {
		this.payment_way_code = payment_way_code;
	}

	public String getPayment_way_name() {
		return payment_way_name;
	}

	public void setPayment_way_name(String payment_way_name) {
		this.payment_way_name = payment_way_name;
	}

	public String getOrder_remark() {
		return order_remark;
	}

	public void setOrder_remark(String order_remark) {
		this.order_remark = order_remark;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
}
