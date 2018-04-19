package com.gzedu.xlims.pojo.graduation;

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
 * 毕业指导老师接收信息权限表
 * @author eenet09
 *
 */
@Entity
@Table(name="GJT_GRADUATION_ADVISER_MSG")
@NamedQuery(name="GjtGraduationAdviserMsg.findAll", query="SELECT g FROM GjtGraduationAdviserMsg g")
public class GjtGraduationAdviserMsg implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private String id;

	@ManyToOne
	@JoinColumn(name="TEACHER_ID")
	private GjtEmployeeInfo teacher;

	@Column(name="ADVISER_TYPE")
	private int adviserType;

	@Column(name="MSG_TYPE")
	private int msgType;

	@Transient
	private String[] advisers;
	
	public GjtGraduationAdviserMsg(){
	}

	public String getId() {
		return id;
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

	public int getAdviserType() {
		return adviserType;
	}

	public void setAdviserType(int adviserType) {
		this.adviserType = adviserType;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String[] getAdvisers() {
		return advisers;
	}

	public void setAdvisers(String[] advisers) {
		this.advisers = advisers;
	}

	@Override
	public String toString() {
		return "GjtGraduationAdviserMsg [id=" + id + ", teacher=" + teacher + ", adviserType=" + adviserType
				+ ", msgType=" + msgType + "]";
	}

}
