/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.pojo.status;

/**
 * 培养层次<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月7日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public enum TrainingLevel implements BaseEnum {

	/**
	 * 高起专
	 */
	GQZ(0, "高起专"),
	/**
	 * 专起本
	 */
	ZQB(2, "专起本"),
	/**
	 * 中专
	 */
	ZZ(4, "中专"),
	/**
	 * 高起专_助力计划
	 */
	GQZZ(6, "高起专_助力计划"),
	/**
	 * 专升本_助力计划
	 */
	ZQBZ(8, "专升本_助力计划");

	private int value;

	private String label;

	/**
	 * @param value
	 * @param label
	 */
	private TrainingLevel(int value, String label) {
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

	public static TrainingLevel getItem(int value) {
		for (TrainingLevel s : values()) {
			if (s.getValue() == value)
				return s;
		}
		return null;
	}

}
