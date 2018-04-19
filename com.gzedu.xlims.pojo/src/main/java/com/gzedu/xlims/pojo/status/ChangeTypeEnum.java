/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 
 * 功能说明：学籍异动类型
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月7日
 * @version 2.5
 * @since JDK1.7
 *
 */
public enum ChangeTypeEnum {

	转专业(101), 转学习中心(102), 转年级(103);
	int num;

	private ChangeTypeEnum(int num) {
		this.num = num;
	}

	public static String getName(int number) {
		for (ChangeTypeEnum typeEnum : ChangeTypeEnum.values()) {
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
