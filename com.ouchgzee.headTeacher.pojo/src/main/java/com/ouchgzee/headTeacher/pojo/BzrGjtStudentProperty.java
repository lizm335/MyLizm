package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 学员扩展参数实体类<br>
 * The persistent class for the GJT_STUDENT_PROPERTY database table.
 * 
 */
@Entity
@Table(name = "GJT_STUDENT_PROPERTY")
// @NamedQuery(name = "GjtStudentProperty.findAll", query = "SELECT g FROM GjtStudentProperty g")
@Deprecated public class BzrGjtStudentProperty implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "DESC_")
	private String desc;

	@Id
	@Column(name = "ID_")
	private String id;

	@Column(name = "KEY_")
	private String key;

	@Column(name = "STUDENT_ID")
	private String studentId;

	@Column(name = "VALUE_")
	private String value;

	public BzrGjtStudentProperty() {
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}