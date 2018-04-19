package com.gzedu.xlims.pojo;

import com.gzedu.xlims.pojo.id.GjtEmployeePositionPK;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The persistent class for the Gjt_Employee_Position database table.
 * 
 */
@Entity
@Table(name = "Gjt_Employee_Position")
@NamedQuery(name = "GjtEmployeePosition.findAll", query = "SELECT g FROM GjtEmployeePosition g")
public class GjtEmployeePosition implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 复合主键
	 */
	@EmbeddedId
	private GjtEmployeePositionPK id;

	public GjtEmployeePositionPK getId() {
		return id;
	}

	public void setId(GjtEmployeePositionPK id) {
		this.id = id;
	}
}