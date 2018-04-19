package com.gzedu.xlims.pojo.status;

public enum MessageType {
	
	SYS_MESSAGE("1","系统消息"),
	CLASS_NOTICE("11","班级公告"),
	EXANINATION_MESSAGE("12","考试通知"),
	STUDY_REMIND("13","学习提醒"),
	EDUCATIONAL_NOTICE("2","教务通知"),
	URGE_REMIND("21","督促提醒"),
	;
	
	private MessageType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	private String code;
	
	private String name;

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
