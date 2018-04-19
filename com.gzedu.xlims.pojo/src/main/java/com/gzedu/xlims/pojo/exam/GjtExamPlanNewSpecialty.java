package com.gzedu.xlims.pojo.exam;

import com.gzedu.xlims.pojo.id.GjtExamPlanNewSpecialtyPK;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The persistent class for the GJT_EXAM_PLAN_NEW_SPECIALTY database table.
 * 
 */
@Entity
@Table(name = "GJT_EXAM_PLAN_NEW_SPECIALTY")
@NamedQuery(name = "GjtExamPlanNewSpecialty.findAll", query = "SELECT g FROM GjtExamPlanNewSpecialty g")
public class GjtExamPlanNewSpecialty implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	/**
	 * 复合主键
	 */
	@EmbeddedId
	private GjtExamPlanNewSpecialtyPK id;

	public GjtExamPlanNewSpecialtyPK getId() {
		return id;
	}

	public void setId(GjtExamPlanNewSpecialtyPK id) {
		this.id = id;
	}

}