/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 
 * 功能说明： 用户类型
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月4日
 * @version 2.5
 * @since JDK1.7
 *
 */
public enum UserTypeEnum {
	管理员(0), 学生(1), 教师(2), 职工(3);
	int num;

	private UserTypeEnum(int num) {
		this.num = num;
	}

	public static String getName(int number) {
		for (UserTypeEnum entity : UserTypeEnum.values()) {
			if (entity.getNum() == number) {
				return entity.toString();
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
