/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 功能说明：
 * @author ouguohao@eenet.com
 * @Date 2017年6月30日
 * @version 3.0
 *
 */
public enum CourseType {
	UNIFIED(0, "统设"), NOT_UNIFIED(1, "非统设");

	private CourseType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	private int code;
	private String name;

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static Integer getCodeByName(String name) {
		for (CourseType type : CourseType.values()) {
			if (type.getName().equals(name)) {
				return type.getCode();
			}
		}
		return null;
	}

}
