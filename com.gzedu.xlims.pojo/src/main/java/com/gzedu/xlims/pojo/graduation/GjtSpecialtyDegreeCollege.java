package com.gzedu.xlims.pojo.graduation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtSpecialtyBase;


/**
 * 学位院校与专业关联表
 * 
 */
@Entity
@Table(name="GJT_SPECIALTY_DEGREE_COLLEGE")
@NamedQuery(name="GjtSpecialtyDegreeCollege.findAll", query="SELECT g FROM GjtSpecialtyDegreeCollege g")
public class GjtSpecialtyDegreeCollege implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	//bi-directional many-to-one association to GjtSpecialty
	@ManyToOne
	@JoinColumn(name="SPECIALTY_ID")
	private GjtSpecialtyBase gjtSpecialty;

	//bi-directional many-to-one association to GjtDegreeCollege
	@ManyToOne
	@JoinColumn(name="COLLEGE_ID")
	private GjtDegreeCollege gjtDegreeCollege;

	private int isEnabled;

	@Column(name = "GRADE_ID")
	private String gradeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE_ID", insertable = false, updatable = false)
	private GjtGrade gjtGrade;

	private String degreeName;

	private String isDeleted;

	private String orgId;

	public GjtSpecialtyDegreeCollege() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GjtSpecialtyBase getGjtSpecialty() {
		return gjtSpecialty;
	}

	public void setGjtSpecialty(GjtSpecialtyBase gjtSpecialty) {
		this.gjtSpecialty = gjtSpecialty;
	}

	public GjtDegreeCollege getGjtDegreeCollege() {
		return this.gjtDegreeCollege;
	}

	public void setGjtDegreeCollege(GjtDegreeCollege gjtDegreeCollege) {
		this.gjtDegreeCollege = gjtDegreeCollege;
	}

	public int getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getDegreeName() {
		return degreeName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@Override
	public String toString() {
		return "GjtSpecialtyDegreeCollege [id=" + id + ", gjtSpecialty=" + gjtSpecialty + ", gjtDegreeCollege="
				+ gjtDegreeCollege + "]";
	}

}