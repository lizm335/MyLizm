/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 功能说明：教职工类型
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月28日
 * @version 2.5
 * @since JDK1.7
 *
 */
public enum EmployeeTypeEnum {
	班主任(1, "headTeacher"), 辅导教师(2, "coachTeacher"), 其他职工(3, "otherTeacher"), 督导教师(4, "supervisorTeacher"), 
	论文教师(10,"paperTeacher"), 论文指导教师(11,"guideTeacher"), 论文答辩教师(12, "replyTeacher"), 
	社会实践教师(13, "practiceTeacher"), 任课教师(14, "classTeacher");

	int num;
	String code;

	EmployeeTypeEnum(int num, String code) {
		this.num = num;
		this.code = code;
	}

	public static String getName(int number) {
		for (EmployeeTypeEnum employeeInfo : EmployeeTypeEnum.values()) {
			if (employeeInfo.getNum() == number) {
				return employeeInfo.toString();
			}
		}
		return "";
	}

	public static String getName(String code) {
		for (EmployeeTypeEnum employeeInfo : EmployeeTypeEnum.values()) {
			if (employeeInfo.code.equals(code)) {
				return employeeInfo.toString();
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

	public String getCode() {
		return code;
	}
}
