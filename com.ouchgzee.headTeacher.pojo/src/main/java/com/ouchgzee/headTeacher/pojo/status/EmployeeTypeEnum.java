/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.pojo.status;

/**
 * 功能说明：教职工类型
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月28日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Deprecated public enum EmployeeTypeEnum {
	班主任(1), 辅导教师(2), 其他职工(3), 督导教师(4), 论文教师(10);

	/**
	 * 论文教师分类
	 */
	@Deprecated public enum Thesis {
		论文指导教师(11), 论文答辩教师(12), 社会实践教师(13);
		int num;

		Thesis(int num) {
			this.num = num;
		}

		public static String getName(int number) {
			for (Thesis employeeInfo : Thesis.values()) {
				if (employeeInfo.getNum() == number) {
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
	}

	int num;

	EmployeeTypeEnum(int num) {
		this.num = num;
	}

	public static String getName(int number) {
		for (EmployeeTypeEnum employeeInfo : EmployeeTypeEnum.values()) {
			if (employeeInfo.getNum() == number) {
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

}
