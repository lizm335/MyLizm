/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 功能说明：GJT_USER_ACCOUNT 角色
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月29日
 * @version 2.5
 * @since JDK1.7
 *
 */
public enum GrantTypeEnum {
	超级管理员(0), 院校管理员(1), 教学点管理员(2);
	int num;

	private GrantTypeEnum(int num) {
		this.num = num;
	}

	public static String getName(int number) {
		for (GrantTypeEnum entity : GrantTypeEnum.values()) {
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

	public static void main(String[] args) {
		System.out.println(GrantTypeEnum.院校管理员.getNum());
	}
}
