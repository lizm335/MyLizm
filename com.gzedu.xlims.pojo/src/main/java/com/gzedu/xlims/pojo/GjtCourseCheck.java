package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 * The persistent class for the GJT_COURSE_CHECK database table.
 * 
 */
@Entity
@Table(name = "GJT_COURSE_CHECK")
@NamedQuery(name = "GjtCourseCheck.findAll", query = "SELECT g FROM GjtCourseCheck g")
public class GjtCourseCheck implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "AUDIT_CONTENT") // 审批内容
	private String auditContent;

	@Temporal(TemporalType.TIMESTAMP) // 审批时间
	@Column(name = "AUDIT_DT")
	private Date auditDt;

	@Column(name = "AUDIT_OPERATOR", insertable = false, updatable = false) // 审批人
	private String auditOperator;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUDIT_OPERATOR")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtUserAccount auditUser;

	@Column(name = "SUBMIT_OPERATOR", insertable = false, updatable = false) // 提交验收人ID
	private String submitOperator;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBMIT_OPERATOR")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtUserAccount submitUser;

	@Column(name = "AUDIT_STATE") // 审批状态 0待审核，1通过，2不通过，3重新提交审核
	private String auditState;

	@Column(name = "COURSE_ID", insertable = false, updatable = false) // 课程ID
	private String courseId;

	@ManyToOne
	@JoinColumn(name = "COURSE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtCourse gjtCourse;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	private BigDecimal version;

	public GjtCourseCheck() {
	}

	public String getAuditContent() {
		return this.auditContent;
	}

	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}

	public Date getAuditDt() {
		return this.auditDt;
	}

	public void setAuditDt(Date auditDt) {
		this.auditDt = auditDt;
	}

	public String getAuditOperator() {
		return this.auditOperator;
	}

	public void setAuditOperator(String auditOperator) {
		this.auditOperator = auditOperator;
	}

	public String getAuditState() {
		return this.auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getCourseId() {
		return courseId;
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

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public GjtUserAccount getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(GjtUserAccount auditUser) {
		this.auditUser = auditUser;
	}

	public String getSubmitOperator() {
		return submitOperator;
	}

	public void setSubmitOperator(String submitOperator) {
		this.submitOperator = submitOperator;
	}

	public GjtUserAccount getSubmitUser() {
		return submitUser;
	}

	public void setSubmitUser(GjtUserAccount submitUser) {
		this.submitUser = submitUser;
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

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

}