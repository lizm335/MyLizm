/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.pojo.status;

/**
 * 考试状态<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月15日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public enum ExamState implements BaseEnum {

	/**
	 * 初始状态
	 */
	UNKONW(0, "初始状态"),
	/**
	 * 未预约
	 */
	UNAPPOINTMENT(1, "未预约"),
	/**
	 * 已预约
	 */
	APPOINTMENT(2, "已预约"),
	/**
	 * 已通过
	 */
	PASS(3, "已通过"),
	/**
	 * 未通过
	 */
	UNPASS(4, "未通过"),
	/**
	 * 补考未缴费
	 */
	EXAMNOPAYMENT(5, "补考未缴费"),
	/**
	 * 补考已缴费
	 */
	EXAMPAYMENT(6, "补考已缴费");

	private int value;

	private String label;

	private ExamState(int value, String label) {
		this.value = value;
		this.label = label;
	}

	public int getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public static ExamState getItem(int value) {
		for (ExamState s : values()) {
			if (s.getValue() == value)
				return s;
		}
		return null;
	}

}
