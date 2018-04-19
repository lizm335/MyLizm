/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月28日
 * @version 2.5
 * @since JDK1.7
 *
 */
public enum StudentTypeEnum {
	正式学员(11), 跟读学员(12), 体验学员(21), 测试学员(31);
	int num;

	private StudentTypeEnum(int num) {
		this.num = num;
	}

	public static String getName(int number) {
		for (StudentTypeEnum studentTypeEnum : StudentTypeEnum.values()) {
			if (studentTypeEnum.getNum() == number) {
				return studentTypeEnum.toString();
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
