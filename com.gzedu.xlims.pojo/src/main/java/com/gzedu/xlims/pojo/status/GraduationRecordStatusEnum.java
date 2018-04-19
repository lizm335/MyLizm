package com.gzedu.xlims.pojo.status;

/**
 * 毕业记录的状态
 * @author eenet09
 *
 */
public enum GraduationRecordStatusEnum {
	
	APPLY("已申请", 0),
	ACCEPT("已受理", 1),
	FIRST_TRIAL_FAILED("初审未通过", 2),
	FINAL_TRIAL_FAILED("终审未通过", 3),
	FINAL_TRIAL_SUCCESS("终审已通过", 4),
	COMPLETED("已领取毕业证", 5);

	private String name;
	
	private int value;
	
	private GraduationRecordStatusEnum(String name, int value) {
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
