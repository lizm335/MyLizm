/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 功能说明：考试单位枚举
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年5月23日
 * @version 3.0
 *
 */
public enum ExamUnit {
	PROVINCE(1, "省"), CENTRE(2, "中央"), BRANCH(3, "分校");
	private ExamUnit(int code, String name) {
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
		for(ExamUnit eu:ExamUnit.values()){
			if (eu.getName().equals(name)) {
				return eu.getCode();
			}
		}

		return null;
	}

}
