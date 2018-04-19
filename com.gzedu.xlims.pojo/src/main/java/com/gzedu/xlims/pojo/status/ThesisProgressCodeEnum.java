package com.gzedu.xlims.pojo.status;

/**
 * 论文进度码
 * @author eenet09
 *
 */
public enum ThesisProgressCodeEnum {
	
	THESIS_APPLY("申请毕业论文", "1001"),
	THESIS_STAY_OPEN("分配指导老师", "2001"),
	THESIS_SUBMIT_PROPOSE("提交开题报告", "3001"),
	THESIS_CONFIRM_PROPOSE("开题报告定稿", "3002"),
	THESIS_SUBMIT_THESIS("提交初稿", "4001"),
	THESIS_TEACHER_CONFIRM_THESIS("指导老师定稿", "4002"),
	THESIS_COLLEGE_CONFIRM_THESIS("学院定稿", "4003"),
	THESIS_SEND("定稿已寄送", "5001"),
	THESIS_SIGN("定稿已签收", "5002"),
	THESIS_DEFENCE("答辩已安排", "6001"),
	//THESIS_HAS_DEFENCE("已答辩", "6002"),
	THESIS_COMPLETED("完成", "0000");
	
	private String name;
	
	private String code;
	
	private ThesisProgressCodeEnum(String name, String code) {
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
