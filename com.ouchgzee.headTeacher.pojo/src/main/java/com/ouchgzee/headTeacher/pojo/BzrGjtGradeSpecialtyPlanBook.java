package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbook;

/**
 * 年级专业教学计划和教材关联 The persistent class for the GJT_GRADE_SPECIALTY_PLAN_BOOK
 * database table.
 * 
 */
@Entity
@Table(name = "GJT_GRADE_SPECIALTY_PLAN_BOOK")
// @NamedQuery(name = "GjtGradeSpecialtyPlanBook.findAll", query = "SELECT g FROM GjtGradeSpecialtyPlanBook g")
@Deprecated public class BzrGjtGradeSpecialtyPlanBook implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE_SPECIALTY_PLAN_ID", insertable = false, updatable = false)
	private BzrGjtTeachPlan gjtGradeSpecialtyPlanBook;

	@Column(name = "GRADE_SPECIALTY_PLAN_ID")
	private String gradeSpecialtyPlanId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEXTBOOK_ID", insertable = false, updatable = false)
	private BzrGjtTextbook gjtTextbook;

	@Column(name = "TEXTBOOK_ID")
	private String textbookId;

	// @Column(name = "TEXTBOOK_TYPE")
	// private int textbookType;

	public BzrGjtGradeSpecialtyPlanBook() {
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

	// public int getTextbookType() {
	// return this.textbookType;
	// }
	//
	// public void setTextbookType(int textbookType) {
	// this.textbookType = textbookType;
	// }

	public BzrGjtTeachPlan getGjtGradeSpecialtyPlanBook() {
		return gjtGradeSpecialtyPlanBook;
	}

	public void setGjtGradeSpecialtyPlanBook(BzrGjtTeachPlan gjtGradeSpecialtyPlanBook) {
		this.gjtGradeSpecialtyPlanBook = gjtGradeSpecialtyPlanBook;
	}

	public BzrGjtTextbook getGjtTextbook() {
		return gjtTextbook;
	}

	public void setGjtTextbook(BzrGjtTextbook gjtTextbook) {
		this.gjtTextbook = gjtTextbook;
	}

}