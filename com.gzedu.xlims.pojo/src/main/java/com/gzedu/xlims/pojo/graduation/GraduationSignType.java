package com.gzedu.xlims.pojo.graduation;

public enum GraduationSignType {
	
	SIGN_TOKEN("0","生成令牌"),SIGN_INPUT("1","签名输入"),SIGN_CONFIRM("2","签名确认");
	
	private String code;
	
	private String name;
	

	private GraduationSignType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
