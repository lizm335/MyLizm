package com.gzedu.xlims.pojo.practice;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.gzedu.xlims.pojo.GjtEmployeeInfo;


/**
 * The persistent class for the GJT_PRACTICE_ADVISER database table.
 * 
 */
@Entity
@Table(name="GJT_PRACTICE_ADVISER")
@NamedQuery(name="GjtPracticeAdviser.findAll", query="SELECT g FROM GjtPracticeAdviser g")
public class GjtPracticeAdviser implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ADVISER_ID")
	private String adviserId;

	@Column(name="ADVISER_NUM")
	private int adviserNum;

	@Column(name="ADVISER_TYPE")
	private int adviserType;

	@Column(name="TEACHER_ID")
	private String teacherId;

	@ManyToOne
	@JoinColumn(name="TEACHER_ID", insertable=false, updatable=false)
	private GjtEmployeeInfo teacher;

	@Column(name="ARRANGE_ID")
	private String arrangeId;

	@ManyToOne
	@JoinColumn(name="ARRANGE_ID", insertable=false, updatable=false)
	private GjtPracticeArrange gjtPracticeArrange;

	@Transient
	private int guideNum1;

	@Transient
	private int guideNum2;

	@Transient
	private int adviserNum2;

	public GjtPracticeAdviser() {
	}

	public String getAdviserId() {
		return this.adviserId;
	}

	public void setAdviserId(String adviserId) {
		this.adviserId = adviserId;
	}

	public int getAdviserNum() {
		return this.adviserNum;
	}

	public void setAdviserNum(int adviserNum) {
		this.adviserNum = adviserNum;
	}

	public int getAdviserType() {
		return this.adviserType;
	}

	public void setAdviserType(int adviserType) {
		this.adviserType = adviserType;
	}

	public String getTeacherId() {
		return this.teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public GjtPracticeArrange getGjtPracticeArrange() {
		return this.gjtPracticeArrange;
	}

	public void setGjtPracticeArrange(GjtPracticeArrange gjtPracticeArrange) {
		this.gjtPracticeArrange = gjtPracticeArrange;
	}

	public GjtEmployeeInfo getTeacher() {
		return teacher;
	}

	public void setTeacher(GjtEmployeeInfo teacher) {
		this.teacher = teacher;
	}

	public String getArrangeId() {
		return arrangeId;
	}

	public void setArrangeId(String arrangeId) {
		this.arrangeId = arrangeId;
	}

	public int getGuideNum1() {
		return guideNum1;
	}

	public void setGuideNum1(int guideNum1) {
		this.guideNum1 = guideNum1;
	}

	public int getGuideNum2() {
		return guideNum2;
	}

	public void setGuideNum2(int guideNum2) {
		this.guideNum2 = guideNum2;
	}

	public int getAdviserNum2() {
		return adviserNum2;
	}

	public void setAdviserNum2(int adviserNum2) {
		this.adviserNum2 = adviserNum2;
	}

	@Override
	public String toString() {
		return "GjtPracticeAdviser [adviserId=" + adviserId + ", adviserNum=" + adviserNum + ", adviserType="
				+ adviserType + ", teacherId=" + teacherId + ", arrangeId=" + arrangeId + "]";
	}

}