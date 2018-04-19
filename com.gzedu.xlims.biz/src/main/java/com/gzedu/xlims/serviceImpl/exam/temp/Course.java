/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.exam.temp;

import java.util.Date;
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
public class Course implements Comparable<Course> {
	String id;
	String name;

	Set<Student> students = new HashSet<Student>();

	Date examEnd;

	Date examSt;

	/**
	 * @param id
	 * @param name
	 */
	public Course(String id, String name, Date examSt, Date examEnd) {
		super();
		this.id = id;
		this.name = name;
		this.examEnd = examEnd;
		this.examSt = examSt;
	}

	@Override
	public boolean equals(Object obj) {
		return this.id.equals(((Course) obj).id);
	}

	@Override
	public String toString() {
		return "科目:" + name + "  ";
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

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	public Date getExamEnd() {
		return examEnd;
	}

	public void setExamEnd(Date examEnd) {
		this.examEnd = examEnd;
	}

	public Date getExamSt() {
		return examSt;
	}

	public void setExamSt(Date examSt) {
		this.examSt = examSt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Course o) {
		return o.examSt.after(this.examSt) ? -1 : 1;
	}

}
