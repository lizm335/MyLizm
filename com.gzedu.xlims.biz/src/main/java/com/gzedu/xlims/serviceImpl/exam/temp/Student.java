/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.exam.temp;

import java.util.HashSet;
import java.util.Set;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年11月18日
 * @version 2.5
 *
 */
public class Student implements Comparable<Student> {
	String id;
	String name;

	Set<Course> courses = new HashSet<Course>();// 预约的科目列表

	int score = 0;

	@Override
	public String toString() {
		return "[学生:" + name + " 得分：" + score + "]";
	}

	@Override
	public boolean equals(Object obj) {
		return this.id.equals(((Student) obj).id);
	}

	/**
	 * @param id
	 * @param name
	 * @param course
	 */
	public Student(String id, String name) {
		this.id = id;
		this.name = name;
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

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public int compareTo(Student o) {
		return this.score < o.score ? 1 : -1;
	}

}
