package com.gzedu.xlims.pojo.id;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the GjtEmployeePosition database table.
 * 
 */
@Embeddable
public class GjtEmployeePositionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="EMPLOYEE_ID")
	private String employeeId;

	@Column(name="TYPE")
	private Integer type;

	public GjtEmployeePositionPK() {
	}

	public GjtEmployeePositionPK(String employeeId, Integer type) {
		this.employeeId = employeeId;
		this.type = type;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof GjtEmployeePositionPK)) {
			return false;
		}
		GjtEmployeePositionPK castOther = (GjtEmployeePositionPK)other;
		return 
			this.employeeId.equals(castOther.employeeId)
			&& this.type.equals(castOther.type);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.employeeId.hashCode();
		hash = hash * prime + this.type.hashCode();
		
		return hash;
	}
}