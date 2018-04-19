package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the GJT_GRADE_SPECIALTY database table.
 * 
 */
@Entity
@Table(name = "GJT_GRADE_SPECIALTY")
@NamedQuery(name = "GjtGradeSpecialty.findAll", query = "SELECT g FROM GjtGradeSpecialty g")
public class GjtGradeSpecialty implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@ManyToOne // 年级
	@JoinColumn(name = "GRADE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtGrade gjtGrade;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@ManyToOne // 专业
	@JoinColumn(name = "SPECIALTY_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSpecialty gjtSpecialty;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	private int status; // 0未开设 1已开设

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;


	@ManyToMany
	@JoinTable(name = "GJT_GS_STUDY_CENTER", joinColumns = {
			@JoinColumn(name = "GRADE_SPECIALTY_ID") }, inverseJoinColumns = { @JoinColumn(name = "STUDY_CENTER_ID") })
	private List<GjtStudyCenter> gjtStudyCenters;

	@Transient
	public List<String> getStudyCenterIds() {
		List<String> list = new ArrayList<String>();
		for (GjtStudyCenter gsc : gjtStudyCenters) {
			list.add(gsc.getId());
		}
		return list;
	}

	public GjtGradeSpecialty() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public GjtGrade getGjtGrade() {
		return gjtGrade;
	}

	public void setGjtGrade(GjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	public GjtSpecialty getGjtSpecialty() {
		return gjtSpecialty;
	}

	public void setGjtSpecialty(GjtSpecialty gjtSpecialty) {
		this.gjtSpecialty = gjtSpecialty;
	}


	public List<GjtStudyCenter> getGjtStudyCenters() {
		return gjtStudyCenters;
	}

	public void setGjtStudyCenters(List<GjtStudyCenter> gjtStudyCenters) {
		this.gjtStudyCenters = gjtStudyCenters;
	}

}