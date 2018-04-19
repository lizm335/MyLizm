package com.gzedu.xlims.pojo.status;

public enum PracticeStatusEnum {
	
	PRACTICE_NO_APPLY("未申请" , -1),
	PRACTICE_APPLY("已申请", 0),
	PRACTICE_STAY_OPEN("待提交初稿", 1),
	PRACTICE_SUBMIT_PRACTICE("待定稿", 2),
	PRACTICE_CONFIRM_PRACTICE("已定稿", 3),
	PRACTICE_SEND("定稿已寄送", 8),
	PRACTICE_COMPLETED("已完成", 13);
	
	private String name;
	
	private int value;
	
	private PracticeStatusEnum(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
