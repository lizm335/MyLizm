package com.gzedu.xlims.pojo.graduation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.gzedu.xlims.pojo.GjtEmployeeInfo;


/**
 * 毕业专业答辩老师
 * 
 */
@Entity
@Table(name="GJT_GRADUATION_DEFENCE_TEACHER")
@NamedQuery(name="GjtGraduationDefenceTeacher.findAll", query="SELECT g FROM GjtGraduationDefenceTeacher g")
public class GjtGraduationDefenceTeacher implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@ManyToOne
	@JoinColumn(name="TEACHER_ID")
	private GjtEmployeeInfo teacher;

	@Column(name="TYPE")
	private int type;

	//bi-directional many-to-one association to GjtGraduationDefencePlan
	@ManyToOne
	@JoinColumn(name="PLAN_ID")
	private GjtGraduationDefencePlan gjtGraduationDefencePlan;

	public GjtGraduationDefenceTeacher() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GjtEmployeeInfo getTeacher() {
		return teacher;
	}

	public void setTeacher(GjtEmployeeInfo teacher) {
		this.teacher = teacher;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public GjtGraduationDefencePlan getGjtGraduationDefencePlan() {
		return this.gjtGraduationDefencePlan;
	}

	public void setGjtGraduationDefencePlan(GjtGraduationDefencePlan gjtGraduationDefencePlan) {
		this.gjtGraduationDefencePlan = gjtGraduationDefencePlan;
	}

}