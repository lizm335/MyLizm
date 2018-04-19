/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 
 * 功能说明： 银行列表
 * 
 * @author lulinlin@eenet.com
 * @Date 2017年12月08日
 * @version 2.5
 * @since JDK1.7
 *
 */
public enum BankEnum {
	
	gongshang("中国工商银行","gongshang"),
	zhongguo("中国银行","zhongguo"),
	jianshe("中国建设银行","jianshe"),
	nongye("中国农业银行","nongye"),
	jiaotong("交通银行","jiaotong"),
	zhaoshang("中国招商银行","zhaoshang"),
	zhongxin("中信银行","zhongxin"),
	mingsheng("中国民生银行","mingsheng"),
	xingye("兴业银行","xingye"),
	guangfa("广发银行","guangfa");
	
	private String name;
	
	private String value;
	
	
	private BankEnum(String name, String value) {
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
