package com.gzedu.xlims.pojo.status;

/**
 * 社会实践进度码
 * @author eenet09
 *
 */
public enum PracticeProgressCodeEnum {
	
	PRACTICE_APPLY("申请社会实践", "1001"),
	PRACTICE_STAY_OPEN("分配指导老师", "2001"),
	PRACTICE_SUBMIT_PRACTICE("提交初稿", "4001"),
	PRACTICE_CONFIRM_PRACTICE("定稿", "4002"),
	PRACTICE_SEND("定稿已寄送", "5001"),
	PRACTICE_COMPLETED("完成", "0000");
	
	private String name;
	
	private String code;
	
	private PracticeProgressCodeEnum(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
