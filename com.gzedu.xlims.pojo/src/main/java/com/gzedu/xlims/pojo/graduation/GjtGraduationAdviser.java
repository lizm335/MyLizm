package com.gzedu.xlims.pojo.graduation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gzedu.xlims.pojo.GjtEmployeeInfo;


/**
 * The persistent class for the GJT_GRADUATION_ADVISER database table.
 * 毕业指导老师表
 */
@Entity
@Table(name="GJT_GRADUATION_ADVISER")
@NamedQuery(name="GjtGraduationAdviser.findAll", query="SELECT g FROM GjtGraduationAdviser g")
public class GjtGraduationAdviser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ADVISER_ID")
	private String adviserId;

	@Column(name="ADVISER_TYPE")
	private int adviserType;

	@ManyToOne
	@JoinColumn(name="TEACHER_ID")
	private GjtEmployeeInfo teacher;

	//bi-directional many-to-one association to GjtGraduationSpecialty
	@ManyToOne
	@JoinColumn(name="SETTING_ID")
	@NotFound(action=NotFoundAction.IGNORE)
	private GjtGraduationSpecialty gjtGraduationSpecialty;

	public GjtGraduationAdviser() {
	}

	public String getAdviserId() {
		return this.adviserId;
	}

	public void setAdviserId(String adviserId) {
		this.adviserId = adviserId;
	}

	public int getAdviserType() {
		return this.adviserType;
	}

	public void setAdviserType(int adviserType) {
		this.adviserType = adviserType;
	}

	public GjtEmployeeInfo getTeacher() {
		return teacher;
	}

	public void setTeacher(GjtEmployeeInfo teacher) {
		this.teacher = teacher;
	}

	public GjtGraduationSpecialty getGjtGraduationSpecialty() {
		return this.gjtGraduationSpecialty;
	}

	public void setGjtGraduationSpecialty(GjtGraduationSpecialty gjtGraduationSpecialty) {
		this.gjtGraduationSpecialty = gjtGraduationSpecialty;
	}

}