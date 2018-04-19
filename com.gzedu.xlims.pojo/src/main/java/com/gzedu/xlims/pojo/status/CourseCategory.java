/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 功能说明：课程属性,0必修，１选修
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月15日
 * @version 2.5
 *
 */
public enum CourseCategory {
	必修(0), 选修(1), 补修(2);
	int num;

	private CourseCategory(int num) {
		this.num = num;
	}

	public static String getName(int number) {
		for (CourseCategory typeEnum : CourseCategory.values()) {
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
