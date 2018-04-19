package com.gzedu.xlims.pojo.id;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the GjtEmployeePosition database table.
 * 
 */
@Embeddable
public class GjtExamPlanNewSpecialtyPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "EXAM_PLAN_ID")
	private String examPlanId;

	@Column(name = "SPECIALTY_ID")
	private String specialtyId;

	public GjtExamPlanNewSpecialtyPK() {
	}

	public GjtExamPlanNewSpecialtyPK(String examPlanId, String specialtyId) {
		this.examPlanId = examPlanId;
		this.specialtyId = specialtyId;
	}

	public String getExamPlanId() {
		return examPlanId;
	}

	public void setExamPlanId(String examPlanId) {
		this.examPlanId = examPlanId;
	}

	public String getSpecialtyId() {
		return specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof GjtExamPlanNewSpecialtyPK)) {
			return false;
		}
		GjtExamPlanNewSpecialtyPK castOther = (GjtExamPlanNewSpecialtyPK)other;
		return 
			this.examPlanId.equals(castOther.examPlanId)
			&& this.specialtyId.equals(castOther.specialtyId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.examPlanId.hashCode();
		hash = hash * prime + this.specialtyId.hashCode();
		
		return hash;
	}
}