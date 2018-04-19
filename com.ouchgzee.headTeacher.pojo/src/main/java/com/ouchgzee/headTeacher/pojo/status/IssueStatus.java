/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.pojo.status;

/**
 * 发放状态<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月4日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public enum IssueStatus implements BaseEnum {

	WAIT(0, "待发放"), FINISH(1, "已发放");

	private int value;

	private String label;

	/**
	 * @param value
	 * @param label
	 */
	private IssueStatus(int value, String label) {
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

	public static IssueStatus getItem(int value) {
		for (IssueStatus s : values()) {
			if (s.getValue() == value)
				return s;
		}
		return null;
	}

}
