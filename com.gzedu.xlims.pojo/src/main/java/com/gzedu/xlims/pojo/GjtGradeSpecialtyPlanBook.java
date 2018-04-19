package com.gzedu.xlims.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;

/**
 * 年级专业教学计划和教材关联 The persistent class for the GJT_GRADE_SPECIALTY_PLAN_BOOK
 * database table.
 * 
 */
@Entity
@Table(name = "GJT_GRADE_SPECIALTY_PLAN_BOOK")
@NamedQuery(name = "GjtGradeSpecialtyPlanBook.findAll", query = "SELECT g FROM GjtGradeSpecialtyPlanBook g")
public class GjtGradeSpecialtyPlanBook implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE_SPECIALTY_PLAN_ID", insertable = false, updatable = false)
	private GjtGradeSpecialtyPlan gjtGradeSpecialtyPlanBook;

	@Column(name = "GRADE_SPECIALTY_PLAN_ID")
	private String gradeSpecialtyPlanId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEXTBOOK_ID", insertable = false, updatable = false)
	private GjtTextbook gjtTextbook;

	@Column(name = "TEXTBOOK_ID")
	private String textbookId;

	public GjtGradeSpecialtyPlanBook() {
		super();
	}

	public GjtGradeSpecialtyPlanBook(String gradeSpecialtyPlanId, String textbookId, int textbookType) {
		this.id = UUIDUtils.random().toString();
		this.gradeSpecialtyPlanId = gradeSpecialtyPlanId;
		this.textbookId = textbookId;
	}

	public String getGradeSpecialtyPlanId() {
		return this.gradeSpecialtyPlanId;
	}

	public void setGradeSpecialtyPlanId(String gradeSpecialtyPlanId) {
		this.gradeSpecialtyPlanId = gradeSpecialtyPlanId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTextbookId() {
		return this.textbookId;
	}

	public void setTextbookId(String textbookId) {
		this.textbookId = textbookId;
	}

	public GjtGradeSpecialtyPlan getGjtGradeSpecialtyPlanBook() {
		return gjtGradeSpecialtyPlanBook;
	}

	public void setGjtGradeSpecialtyPlanBook(GjtGradeSpecialtyPlan gjtGradeSpecialtyPlanBook) {
		this.gjtGradeSpecialtyPlanBook = gjtGradeSpecialtyPlanBook;
	}

	public GjtTextbook getGjtTextbook() {
		return gjtTextbook;
	}

	public void setGjtTextbook(GjtTextbook gjtTextbook) {
		this.gjtTextbook = gjtTextbook;
	}

}