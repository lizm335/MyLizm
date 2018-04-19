package com.gzedu.xlims.pojo.exam;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gzedu.xlims.pojo.GjtStudentInfo;


/**
 * The persistent class for the GJT_EXAM_COST database table.
 * 
 */
@Entity
@Table(name="GJT_EXAM_COST")
@NamedQuery(name="GjtExamCost.findAll", query="SELECT g FROM GjtExamCost g")
public class GjtExamCost implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COST_ID")
	private String costId;

	/**
	 * 缴费单号
	 */
	@Column(name="PAY_SN")
	private String paySn;

	@Column(name="COURSE_CODE")
	private String courseCode;

	@Column(name="COURSE_COST")
	private String courseCost;

	@Column(name="COURSE_ID")
	private String courseId;

	@Column(name="EXAM_BATCH_CODE")
	private String examBatchCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAM_BATCH_CODE", referencedColumnName = "EXAM_BATCH_CODE", insertable = false, updatable = false)
	private GjtExamBatchNew examBatchNew;

	@Column(name="EXAM_PLAN_ID")
	private String examPlanId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAM_PLAN_ID", referencedColumnName = "EXAM_PLAN_ID", insertable = false, updatable = false)
	private GjtExamPlanNew examPlanNew;

	@Column(name="GRADE_ID")
	private String gradeId;

	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	private String ksfs;

	private String makeup;

	@Column(name="PAY_STATUS")
	private String payStatus;

	@Column(name="STUDENT_ID")
	private String studentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID", referencedColumnName = "STUDENT_ID", insertable = false, updatable = false)
	private GjtStudentInfo student;

	@Column(name="TEACH_PLAN_ID")
	private String teachPlanId;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private Integer version;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PAY_DATE")
	private Date payDate;

	public GjtExamCost() {
	}

	public String getCostId() {
		return this.costId;
	}

	public void setCostId(String costId) {
		this.costId = costId;
	}

	public String getPaySn() {
		return paySn;
	}

	public void setPaySn(String paySn) {
		this.paySn = paySn;
	}

	public String getCourseCode() {
		return this.courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseCost() {
		return this.courseCost;
	}

	public void setCourseCost(String courseCost) {
		this.courseCost = courseCost;
	}

	public String getCourseId() {
		return this.courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
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

	public String getExamBatchCode() {
		return this.examBatchCode;
	}

	public void setExamBatchCode(String examBatchCode) {
		this.examBatchCode = examBatchCode;
	}

	public String getExamPlanId() {
		return this.examPlanId;
	}

	public void setExamPlanId(String examPlanId) {
		this.examPlanId = examPlanId;
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

	public String getKsfs() {
		return this.ksfs;
	}

	public void setKsfs(String ksfs) {
		this.ksfs = ksfs;
	}

	public String getMakeup() {
		return this.makeup;
	}

	public void setMakeup(String makeup) {
		this.makeup = makeup;
	}

	public String getPayStatus() {
		return this.payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getTeachPlanId() {
		return this.teachPlanId;
	}

	public void setTeachPlanId(String teachPlanId) {
		this.teachPlanId = teachPlanId;
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

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public GjtExamBatchNew getExamBatchNew() {
		return examBatchNew;
	}

	public void setExamBatchNew(GjtExamBatchNew examBatchNew) {
		this.examBatchNew = examBatchNew;
	}

	public GjtExamPlanNew getExamPlanNew() {
		return examPlanNew;
	}

	public void setExamPlanNew(GjtExamPlanNew examPlanNew) {
		this.examPlanNew = examPlanNew;
	}

	public GjtStudentInfo getStudent() {
		return student;
	}

	public void setStudent(GjtStudentInfo student) {
		this.student = student;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
}