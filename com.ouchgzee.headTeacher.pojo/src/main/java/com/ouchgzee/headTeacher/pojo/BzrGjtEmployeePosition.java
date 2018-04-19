package com.ouchgzee.headTeacher.pojo;

import com.ouchgzee.headTeacher.id.BzrGjtEmployeePositionPK;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The persistent class for the Gjt_Employee_Position database table.
 * 
 */
@Entity
@Table(name = "Gjt_Employee_Position")
// @NamedQuery(name = "GjtEmployeePosition.findAll", query = "SELECT g FROM GjtEmployeePosition g")
@Deprecated public class BzrGjtEmployeePosition implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 复合主键
	 */
	@EmbeddedId
	private BzrGjtEmployeePositionPK id;

	public BzrGjtEmployeePositionPK getId() {
		return id;
	}

	public void setId(BzrGjtEmployeePositionPK id) {
		this.id = id;
	}
}