/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 
 * 功能说明：通知公告发送角色
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月22日
 * @version 2.5
 *
 */
public enum MessageInfoRoleTypeEnum {

	student(1), headTeacher(2), coachTeacher(3), superviseTeacher(4);
	int num;

	private MessageInfoRoleTypeEnum(int num) {
		this.num = num;
	}

	public static String getName(int number) {
		for (MessageInfoRoleTypeEnum typeEnum : MessageInfoRoleTypeEnum.values()) {
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
