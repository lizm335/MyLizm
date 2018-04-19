package com.gzedu.xlims.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the GJT_TEXTBOOK_PLAN_OWNERSHIP database table.
 * 教材学习计划中间表
 * 
 */
@Entity
@Table(name = "GJT_TEXTBOOK_PLAN_OWNERSHIP")
@NamedQuery(name = "GjtTextbookPlanOwnership.findAll", query = "SELECT g FROM GjtTextbookPlanOwnership g")
public class GjtTextbookPlanOwnership implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "SPECIALTY_PLAN_ID")
	private String specialtyPlanId;

	@Column(name = "TEXTBOOK_ID")
	private String textbookId;

	@Column(name = "TEXTBOOK_TYPE")
	private String textbookType;

	public GjtTextbookPlanOwnership() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSpecialtyPlanId() {
		return this.specialtyPlanId;
	}

	public void setSpecialtyPlanId(String specialtyPlanId) {
		this.specialtyPlanId = specialtyPlanId;
	}

	public String getTextbookId() {
		return this.textbookId;
	}

	public void setTextbookId(String textbookId) {
		this.textbookId = textbookId;
	}

	public String getTextbookType() {
		return this.textbookType;
	}

	public void setTextbookType(String textbookType) {
		this.textbookType = textbookType;
	}

}