package com.gzedu.xlims.pojo.textbook;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gzedu.xlims.pojo.GjtGrade;

/**
 * The persistent class for the GJT_TEXTBOOK_ARRANGE database table.
 * 
 */
@Entity
@Table(name = "GJT_TEXTBOOK_GRADE")
@NamedQuery(name = "GjtTextbookGrade.findAll", query = "SELECT g FROM GjtTextbookGrade g")
public class GjtTextbookGrade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String Id;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	// bi-directional many-to-one association to GjtTextbookPlan
	@ManyToOne
	@JoinColumn(name = "PLAN_ID")
	private GjtTextbookPlan gjtTextbookPlan;

	@Column(name = "PLAN_ID", insertable = false, updatable = false)
	private String planId;

	@Column(name = "GRADE_ID", insertable = false, updatable = false)
	private String gradeId;

	@OneToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "GRADE_ID")
	private GjtGrade gjtGrade;

	public GjtTextbookGrade() {
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public GjtGrade getGjtGrade() {
		return gjtGrade;
	}

	public void setGjtGrade(GjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
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

	public GjtTextbookPlan getGjtTextbookPlan() {
		return this.gjtTextbookPlan;
	}

	public void setGjtTextbookPlan(GjtTextbookPlan gjtTextbookPlan) {
		this.gjtTextbookPlan = gjtTextbookPlan;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

}