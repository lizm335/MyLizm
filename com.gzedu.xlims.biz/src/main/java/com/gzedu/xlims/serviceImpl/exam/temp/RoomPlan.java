/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.exam.temp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能说明：考场计划
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年11月18日
 * @version 2.5
 *
 */
public class RoomPlan {
	String id;
	String name;
	Course course;// 科目
	Room room;
	Date startTime;
	Date endTime;
	int index;// 场次NUM

	List<Student> students = new ArrayList<Student>();

	/**
	 * @param id
	 * @param name
	 * @param room
	 * @param startTime
	 * @param endTime
	 */
	public RoomPlan(String name, Room room, int index) {
		super();
		this.name = name;
		this.room = room;
		this.index = index;
	}

	@Override
	public String toString() {
		String s = "";
		for (Student student : students) {
			s += student.getName() + ",";
		}
		return name + index + "-" + room.getName() + "-科目" + course.name + "-总人数" + students.size() + "[" + s + "]";
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
