/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 
 * 功能说明：答疑数据来源类型
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月30日
 * @version 2.5
 *
 */
public enum SubjectSourceEnum {
	/**
	 * 学习空间
	 */
	STUDY_SPACE("1", "学习空间"),
	/**
	 * 课程学习平台
	 */
	COURSE_STUDY_PLATFORM("2", "课程学习平台"),
	/**
	 * iosApp
	 */
	IOS_APP("3", "iosApp"),
	/**
	 * androidApp
	 */
	ANDROID_APP("4", "androidApp"),
	/**
	 * 微信App
	 */
	WEIXIN_APP("5", "微信App"),

	/**
	 * 教务后台
	 */
	EDU_BACKSTAGE("6", "教务平台"),

	/**
	 * 班主任后台
	 */
	HEAD_TEACHER_BACKSTAGE("7", "班主任平台");

	String num;
	String code;

	private SubjectSourceEnum(String num, String code) {
		this.num = num;
		this.code = code;
	}

	public static String getName(String number) {
		for (SubjectSourceEnum studentTypeEnum : SubjectSourceEnum.values()) {
			if (number.equals(studentTypeEnum.getNum())) {
				return studentTypeEnum.toString();
			}
		}
		return "";
	}

	public static String getCode(String number) {
		for (SubjectSourceEnum studentTypeEnum : SubjectSourceEnum.values()) {
			if (number.equals(studentTypeEnum.getNum())) {
				return studentTypeEnum.getCode().toString();
			}
		}
		return "";
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
