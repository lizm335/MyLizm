package com.gzedu.xlims.pojo.textbook;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the GJT_STUDENT_TEXTBOOK_ORDER database table.
 * 
 */
@Entity
@Table(name="GJT_STUDENT_TEXTBOOK_ORDER")
@NamedQuery(name="GjtStudentTextbookOrder.findAll", query="SELECT g FROM GjtStudentTextbookOrder g")
public class GjtStudentTextbookOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="GRADE_ID")
	private String gradeId;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="ORDER_NO")
	private String orderNo;

	@Column(name="STUDENT_ID")
	private String studentId;

	@Column(name="TEXTBOOK_CODES")
	private String textbookCodes;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	public GjtStudentTextbookOrder() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGradeId() {
		return this.gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getTextbookCodes() {
		return textbookCodes;
	}

	public void setTextbookCodes(String textbookCodes) {
		this.textbookCodes = textbookCodes;
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

}