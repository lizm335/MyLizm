package com.gzedu.xlims.pojo.status;

public enum ExpressEnum {

	shunfeng("顺丰", "shunfeng"),
	shentong("申通", "shentong"),
	yuantong("圆通", "yuantong"),
	zhongtong("中通", "zhongtong"),
	yunda("韵达", "yunda"),
	ems("EMS", "ems"),
	huitongkuaidi("百世快递", "huitongkuaidi"),
	rufengda("如风达", "rufengda"),
	tiantian("天天快递", "tiantian"),
	debangwuliu("德邦物流", "debangwuliu"),
	xinbangwuliu("新邦物流", "xinbangwuliu"),
	zhaijisong("宅急送", "zhaijisong"),
	other("其它", "other");
	
	private String name;
	
	private String value;

	private ExpressEnum(String name, String value) {
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
