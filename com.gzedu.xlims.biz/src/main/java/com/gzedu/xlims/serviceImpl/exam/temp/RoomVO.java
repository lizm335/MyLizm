/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.exam.temp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年12月9日
 * @version 2.5
 *
 */
public class RoomVO implements Comparable<RoomVO> {
	public String roomId;
	public String title;// 标题
	public String pointName;// 考点名称
	public String pointArea;// 省市区
	public String pointAddress;// 考点地址
	public String planId;// 考试计划ID（内含考试科目）
//	public String subjectName;// 科目名称
	public String courseName;// 课程名称
	public String examType;// 考试方式
	public String examNo;// 试卷号
	public String roomName;// 考场名称
	public String examDay;// 考试日期
	public String examTime;// 考试范围
	public int seatTotal;// 座位总数量

	public Date startDate;
	public Date endDate;

	public List<StudentVO> students = new ArrayList<StudentVO>();

	// 学号长度为9位是跟读学员
	public List<StudentVO> getEqual9Students() {
		List<StudentVO> list = new ArrayList<StudentVO>();
		for (StudentVO studentVO : students) {
			if (studentVO.xh.length() == 9) {
				list.add(studentVO);
			}
		}
		return list;
	}

	// 学号长度为不是9位是非跟读学员
	public List<StudentVO> getThan9Students() {
		List<StudentVO> list = new ArrayList<StudentVO>();
		for (StudentVO studentVO : students) {
			if (studentVO.xh.length() != 9) {
				list.add(studentVO);
			}
		}
		return list;
	}

	public int getMaxStudentSeatNo() {
		int i = 0;
		for (StudentVO studentVO : students) {
			i = studentVO.seatNo > i ? studentVO.seatNo : i;
		}
		return i;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RoomVO o) {
		// try {
		// // 相同的时间同一考场，位置编号靠前的排第一
		// if (o.endDate.compareTo(this.endDate) == 0) {
		// if (o.students.size() > 0 && this.students.size() > 0 &&
		// o.roomName.equals(this.roomName)) {
		// Collections.sort(this.students);
		// Collections.sort(o.students);
		// return o.students.get(0).seatNo > this.students.get(0).seatNo ? -1 :
		// 1;
		// }
		// }
		// } catch (Exception e) {
		// return 0;
		// }
		// return o.endDate.after(this.endDate) ? -1 : 1;

		if (o.endDate.after(this.endDate)) {
			return -1;
		} else if (o.endDate.equals(this.endDate)) {
			if (o.roomName.equals(this.roomName)) {
				return -1;
			} else {
				return 1;
			}
		} else {
			System.out.println("相同了3" + this.roomName + ":" + o.roomName);
			return 1;
		}

		// if (o != null) {
		// if (country < o.country) {
		// return -1;
		// } else if (country == o.country) {
		// if (province < o.province) {
		// return -1;
		// } else if (province == o.province) {
		// if (city <= o.city) {
		// return -1;
		// } else {
		// return 1;
		// }
		// } else {
		// return 1;
		// }
		// } else {
		// return 1;
		// }
		// } else {
		// return -1;
		// }
	}

	public static void main(String[] args) {
		System.out.println(new Date().after(new Date()));
	}

}
