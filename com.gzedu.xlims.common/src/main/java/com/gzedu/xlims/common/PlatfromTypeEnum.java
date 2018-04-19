/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.common;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年5月15日
 * @version 2.5
 *
 */
public enum PlatfromTypeEnum {
	/**
	 * 管理平台
	 */
	MANAGEPLATFORM(1, "管理平台"),
	/**
	 * 辅导教师平台
	 */
	COACHTEACHERPLATFORM(2, "辅导教师平台"),
	/**
	 * 班主任平台
	 */
	HEADTEACHERPLATFORM(3, "班主任平台"),
	/**
	 * 督导教师平台
	 */
	SUPERVISORTEACHERPLATFORM(4, "督导教师平台"),
	/**
	 * 个人中心学习平台
	 */
	PERCENTPLATFORM(5, "个人中心学习平台"),
	/**
	 * 任课教师平台
	 */
	CLASSTEACHERPLATFORM(6, "任课教师平台");

	int num;
	String name;

	private PlatfromTypeEnum(int num, String name) {
		this.num = num;
		this.name = name;
	}

	public static String getName(int number) {
		for (PlatfromTypeEnum typeEnum : PlatfromTypeEnum.values()) {
			if (typeEnum.getNum() == number) {
				return typeEnum.toString();
			}
		}
		return "";
	}
	public static PlatfromTypeEnum getItem(int number) {
		for (PlatfromTypeEnum typeEnum : PlatfromTypeEnum.values()) {
			if (typeEnum.getNum() == number) {
				return typeEnum;
			}
		}
		return null;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
