package com.gzedu.xlims.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The persistent class for the GJT_CLASS_TEACHER database table.
 * 
 */
@Entity
@Table(name = "GJT_CLASS_TEACHER")
@NamedQuery(name = "GjtClassTeacher.findAll", query = "SELECT g FROM GjtClassTeacher g")
public class GjtClassTeacher implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String classTeacherId;

	@Column(name = "CLASS_ID")
	private String classId;

	@Column(name = "EMPLOYEE_ID")
	private String employeeId;

	@Column(name = "EMPLOYEE_TYPE")
	private String employeeType;

	@OneToOne
	@JoinColumn(name = "EMPLOYEE_ID", insertable = false, updatable = false)
	private GjtEmployeeInfo gjtEmployeeInfo;

	public GjtEmployeeInfo getGjtEmployeeInfo() {
		return gjtEmployeeInfo;
	}

	public void setGjtEmployeeInfo(GjtEmployeeInfo gjtEmployeeInfo) {
		this.gjtEmployeeInfo = gjtEmployeeInfo;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public GjtClassTeacher() {
	}

	public String getClassTeacherId() {
		return classTeacherId;
	}

	public void setClassTeacherId(String classTeacherId) {
		this.classTeacherId = classTeacherId;
	}

	public String getClassId() {
		return this.classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

}