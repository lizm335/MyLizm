/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.pojo.status;

/**
 * 邮箱类型<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月9日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public enum BoxType implements BaseEnum {

	/**
	 * 收件箱
	 */
	INBOX(1, "收件箱"),
	/**
	 * 发件箱
	 */
	SENTBOX(2, "发件箱"),
	/**
	 * 草稿箱
	 */
	DRAFTBOX(3, "草稿箱");

	private int value;

	private String label;

	/**
	 * @param value
	 * @param label
	 */
	private BoxType(int value, String label) {
		this.value = value;
		this.label = label;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ouchgzee.headTeacher.pojo.status.BaseEnum#getLabel()
	 */
	public String getLabel() {
		return label;
	}

	public static BoxType getItem(int value) {
		for (BoxType s : values()) {
			if (s.getValue() == value)
				return s;
		}
		return null;
	}

}
