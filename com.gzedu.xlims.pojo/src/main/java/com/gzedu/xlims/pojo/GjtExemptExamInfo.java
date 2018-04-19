package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


/**
 * 学员申请免修免考
 * The persistent class for the GJT_EXEMPT_EXAM_INFO database table.
 * 
 */
@Entity
@Table(name="GJT_EXEMPT_EXAM_INFO")
@NamedQuery(name="GjtExemptExamInfo.findAll", query="SELECT g FROM GjtExemptExamInfo g")
public class GjtExemptExamInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EXEMPT_EXAM_ID")
	private String exemptExamId;

	@Column(name="AUDIT_STATUS")
	private String auditStatus;
	
	@Column(name="AUDIT_OPERATOR_ROLE")
	private String auditOperatorRole;

	@Column(name="COURSE_ID")
	private String courseId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtCourse gjtCourse;// 课程

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT",insertable = false, updatable = false)
	private Date createdDt;

	@Column(name="IS_DELETED",insertable = false, updatable = false)
	private String isDeleted;

	private String memo;

	@Column(name="SIGN")
	private String sign;

	@Column(name="STUDENT_ID")
	private String studentId;
	
	@Column(name="IS_APPLY")
	private String isApplay;//是否已申请过免修免考状态 0-未申请  1-已申请
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID" ,insertable=false,updatable=false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtStudentInfo gjtStudentInfo;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name="VERSION",insertable = false, updatable = false)
	private BigDecimal version;
	
	@OneToMany(mappedBy="gjtExemptExamInfo",fetch = FetchType.LAZY)
	private List<GjtExemptExamProve> gjtExemptExamProves;
	
	@OneToMany(mappedBy="gjtExemptExamInfo",fetch = FetchType.LAZY)
	private List<GjtExemptExamInfoAudit> gjtExemptExamInfoAudits;

	public GjtExemptExamInfo() {
	}

	public String getExemptExamId() {
		return this.exemptExamId;
	}

	public void setExemptExamId(String exemptExamId) {
		this.exemptExamId = exemptExamId;
	}

	public String getAuditStatus() {
		return this.auditStatus;
	}
	
	public String getAuditOperatorRole() {
		return auditOperatorRole;
	}

	public void setAuditOperatorRole(String auditOperatorRole) {
		this.auditOperatorRole = auditOperatorRole;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
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

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
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
	
	public String getIsApplay() {
		return isApplay;
	}

	public void setIsApplay(String isApplay) {
		this.isApplay = isApplay;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public GjtCourse getGjtCourse() {
		return gjtCourse;
	}

	public void setGjtCourse(GjtCourse gjtCourse) {
		this.gjtCourse = gjtCourse;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public List<GjtExemptExamProve> getGjtExemptExamProves() {
		return gjtExemptExamProves;
	}

	public void setGjtExemptExamProves(List<GjtExemptExamProve> gjtExemptExamProves) {
		this.gjtExemptExamProves = gjtExemptExamProves;
	}

	public List<GjtExemptExamInfoAudit> getGjtExemptExamInfoAudits() {
		return gjtExemptExamInfoAudits;
	}

	public void setGjtExemptExamInfoAudits(List<GjtExemptExamInfoAudit> gjtExemptExamInfoAudits) {
		this.gjtExemptExamInfoAudits = gjtExemptExamInfoAudits;
	}
	
}