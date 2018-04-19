/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月15日
 * @version 2.5
 *
 */
public enum ExamType {
	网考(0), 场考(1);
	int num;

	private ExamType(int num) {
		this.num = num;
	}

	public static String getName(int number) {
		for (ExamType typeEnum : ExamType.values()) {
			if (typeEnum.getNum() == number) {
				return typeEnum.toString();
			}
		}
		return "";
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
