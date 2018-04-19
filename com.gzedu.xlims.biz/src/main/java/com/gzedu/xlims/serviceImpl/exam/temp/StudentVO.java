/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.exam.temp;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年12月9日
 * @version 2.5
 *
 */
public class StudentVO implements Comparable<StudentVO> {
	public String studentId;// 学生ID
	public int seatNo;// 座位号
	public String xh;// 学号
	public String name;// 姓名
	public String sex;// 性别
	public String cardNo;// 身份证号码
	public String isRepeat;// 是否留级
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */

	@Override
	public int compareTo(StudentVO o) {
		return o.seatNo > this.seatNo ? -1 : 1;
	}

}
