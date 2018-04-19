package com.gzedu.xlims.pojo.status;

/**
 * 论文状态码
 * @author eenet09
 *
 */
public enum ThesisStatusEnum {
	
	THESIS_NO_APPLY("未申请", -1),
	THESIS_APPLY("已申请", 0),
	THESIS_STAY_OPEN("待开题", 1),
	THESIS_SUBMIT_PROPOSE("已开题", 2),
	THESIS_CONFIRM_PROPOSE("开题定稿", 3),
	THESIS_SUBMIT_THESIS("论文写作中", 4),
	THESIS_TEACHER_CONFIRM_THESIS("指导老师定稿", 5),
	THESIS_REVIEW_FAILED("初评成绩未达标", 6),
	THESIS_COLLEGE_CONFIRM_THESIS("学院定稿", 7),
	THESIS_SEND("定稿已寄送", 8),
	THESIS_STAY_DEFENCE("答辩安排中", 9),
	THESIS_DEFENCE("答辩已安排", 10),
	//THESIS_HAS_DEFENCE("已答辩", 11),
	THESIS_DEFENCE_FAILED("答辩成绩未达标", 12),
	THESIS_COMPLETED("已完成", 13);
	
	private String name;
	
	private int value;
	
	private ThesisStatusEnum(String name, int value) {
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
