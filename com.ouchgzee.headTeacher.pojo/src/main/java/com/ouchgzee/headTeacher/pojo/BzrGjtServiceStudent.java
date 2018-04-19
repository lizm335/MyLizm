package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the GJT_SERVICE_STUDENT database table.
 * 
 */
@Entity
@Table(name="GJT_SERVICE_STUDENT")
// @NamedQuery(name="GjtServiceStudent.findAll", query="SELECT g FROM GjtServiceStudent g")
@Deprecated public class BzrGjtServiceStudent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SERVICE_STUDENT_ID")
	private String serviceStudentId;

	@Column(name="CREATED_BY",updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT",insertable = false,updatable = false)
	private Date createdDt;

	private String serviceid;

	@Column(name="STUDENT_ID")
	private String studentId;

	public BzrGjtServiceStudent() {
	}

	public String getServiceStudentId() {
		return this.serviceStudentId;
	}

	public void setServiceStudentId(String serviceStudentId) {
		this.serviceStudentId = serviceStudentId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getServiceid() {
		return this.serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

}