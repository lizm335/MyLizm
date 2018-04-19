package com.gzedu.xlims.pojo.textbook;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtStudentInfo;

/**
 * The persistent class for the GJT_TEXTBOOK_ORDER_DETAIL database table.
 * 
 */
@Entity
@Table(name = "GJT_TEXTBOOK_ORDER_DETAIL")
@NamedQuery(name = "GjtTextbookOrderDetail.findAll", query = "SELECT g FROM GjtTextbookOrderDetail g")
public class GjtTextbookOrderDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DETAIL_ID")
	private String detailId;

	@Column(name = "COURSE_ID")
	private String courseId;

	@ManyToOne
	@JoinColumn(name = "COURSE_ID", insertable = false, updatable = false)
	private GjtCourse gjtCourse;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "GRADE_ID")
	private String gradeId;

	@ManyToOne
	@JoinColumn(name = "GRADE_ID", insertable = false, updatable = false)
	private GjtGrade gjtGrade;

	@Column(name = "NEED_DISTRIBUTE") // 是否需要发放：1-需要，0-不需要
	private int needDistribute;

	private int status;// 状态：0-待提交审核，1-待审核，2-审核不通过，3-审核通过

	@Column(name = "STUDENT_ID")
	private String studentId;

	@ManyToOne
	@JoinColumn(name = "STUDENT_ID", insertable = false, updatable = false)
	private GjtStudentInfo gjtStudentInfo;

	// bi-directional many-to-one association to GjtTextbookArrange
	@ManyToOne
	@JoinColumn(name = "ARRANGE_ID")
	private GjtTextbookArrange gjtTextbookArrange;

	// bi-directional many-to-one association to GjtTextbookOrder
	@ManyToOne
	@JoinColumn(name = "ORDER_ID")
	private GjtTextbookOrder gjtTextbookOrder;

	// bi-directional many-to-one association to GjtTextbookPlan
	@ManyToOne
	@JoinColumn(name = "PLAN_ID", insertable = false, updatable = false)
	private GjtTextbookPlan gjtTextbookPlan;

	@Column(name = "PLAN_ID")
	private String planId;

	// bi-directional many-to-one association to GjtTextbook
	@ManyToOne
	@JoinColumn(name = "TEXTBOOK_ID")
	private GjtTextbook gjtTextbook;

	@Column(name = "TEXTBOOK_ID", insertable = false, updatable = false)
	private String textbookId;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	public GjtTextbookOrderDetail() {
	}

	public String getDetailId() {
		return this.detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getCourseId() {
		return this.courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public GjtCourse getGjtCourse() {
		return gjtCourse;
	}

	public void setGjtCourse(GjtCourse gjtCourse) {
		this.gjtCourse = gjtCourse;
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

	public String getGradeId() {
		return this.gradeId;
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

	public int getNeedDistribute() {
		return this.needDistribute;
	}

	public void setNeedDistribute(int needDistribute) {
		this.needDistribute = needDistribute;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public GjtTextbookArrange getGjtTextbookArrange() {
		return this.gjtTextbookArrange;
	}

	public void setGjtTextbookArrange(GjtTextbookArrange gjtTextbookArrange) {
		this.gjtTextbookArrange = gjtTextbookArrange;
	}

	public GjtTextbookOrder getGjtTextbookOrder() {
		return this.gjtTextbookOrder;
	}

	public void setGjtTextbookOrder(GjtTextbookOrder gjtTextbookOrder) {
		this.gjtTextbookOrder = gjtTextbookOrder;
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

	public GjtTextbook getGjtTextbook() {
		return this.gjtTextbook;
	}

	public void setGjtTextbook(GjtTextbook gjtTextbook) {
		this.gjtTextbook = gjtTextbook;
	}

	public String getTextbookId() {
		return textbookId;
	}

	public void setTextbookId(String textbookId) {
		this.textbookId = textbookId;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	@Override
	public String toString() {
		return "GjtTextbookOrderDetail [detailId=" + detailId + ", courseId=" + courseId + ", createdBy=" + createdBy
				+ ", createdDt=" + createdDt + ", gradeId=" + gradeId + ", needDistribute=" + needDistribute
				+ ", status=" + status + ", studentId=" + studentId + ", gjtTextbookArrange=" + gjtTextbookArrange
				+ ", gjtTextbookOrder=" + gjtTextbookOrder + ", gjtTextbookPlan=" + gjtTextbookPlan + ", gjtTextbook="
				+ gjtTextbook + "]";
	}

}