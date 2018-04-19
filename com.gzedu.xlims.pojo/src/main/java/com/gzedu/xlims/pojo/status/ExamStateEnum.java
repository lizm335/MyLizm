/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 功能说明：Gjt_rec_result 考试状态(ExamState)
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年4月7日
 * @version 2.5
 *
 */
public enum ExamStateEnum {
	/**
	 * 初始状态
	 */
	unkonw(0),
	/**
	 * 未预约
	 */
	unappointment(1),

	/**
	 * 已预约
	 */
	appointment(2),

	/**
	 * 已通过
	 */
	pass(3),

	/**
	 * 未通过
	 */
	unpass(4),

	/**
	 * 补考未缴费
	 */
	examNoPayment(5),

	/**
	 * 补考已缴费
	 */
	examPayment(6);

	int num;

	private ExamStateEnum(int num) {
		this.num = num;
	}

	public static String getName(int number) {
		for (ExamStateEnum item : ExamStateEnum.values()) {
			if (item.getNum() == number) {
				return item.toString();
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
