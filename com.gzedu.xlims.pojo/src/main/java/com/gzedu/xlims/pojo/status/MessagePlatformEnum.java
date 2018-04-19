package com.gzedu.xlims.pojo.status;

public enum MessagePlatformEnum {
	PC(0), APP(1), 公众号(2);
	private MessagePlatformEnum(int code) {
		this.code = code;
	}

	private int code;

	public int getCode() {
		return code;
	}

	public static String getName(Integer code) {
		for (MessagePlatformEnum entity : MessagePlatformEnum.values()) {
			if (entity.getCode() == code) {
				return entity.toString();
			}
		}
		return null;
	}
}