/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.pojo.status;

/**
 * 教师类别<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月2日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public enum EmployeeType implements BaseEnum {

	/**
	 * 班主任（教学班）
	 */
	HEADTEACHER(1, "班主任"),
	/**
	 * 辅导教师(课程班)
	 */
	TUTORIALTEACHER(2, "辅导教师"),
	/**
	 * 其它(职工)
	 */
	OTHERTEACHER(3, "其它"),
	/**
	 * 督导教师
	 */
	SUPERVISORTEACHER(4, "督导教师");

	private int value;

	private String label;

	/**
	 * @param value
	 * @param label
	 */
	private EmployeeType(int value, String label) {
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

	public static EmployeeType getItem(int value) {
		for (EmployeeType s : values()) {
			if (s.getValue() == value)
				return s;
		}
		return null;
	}

}
