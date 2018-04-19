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
public class OrderInfo {

	private String user_name; // 姓名
	private String identity_card; // 身份证号
	private String product_name; // 产品名称
	private String order_sn; // 订单号

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
}
