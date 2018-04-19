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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 学期课程信息<br/>
 * The persistent class for the GJT_TERM_COURSEINFO database table.
 * 
 */
@Entity
@Table(name="GJT_TERM_COURSEINFO")
@NamedQuery(name="GjtTermCourseinfo.findAll", query="SELECT g FROM GjtTermCourseinfo g")
public class GjtTermCourseinfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TERMCOURSE_ID")
	private String termcourseId; // 学期课程ID

	@Column(name="COURSE_ID")
	private String courseId;

	@Column(name = "TERM_ID") // 学期ID
	private String termId;

	@Column(name="XX_ID")
	private String xxId;

	@Column(name="REMARK")
	private String remark;

	@Column(name="COPY_FLG", insertable = false)
	private String copyFlg; // 学习平台是否复制课程 1、未复制  2、已复制

	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@Column(name = "CLASS_TEACHER")
	private String teacherEmployeeId;// 任课教师

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASS_TEACHER", insertable = false, updatable = false)
	private GjtEmployeeInfo teacherEmployee;

	public GjtTermCourseinfo() {
	}

	public String getTermcourseId() {
		return termcourseId;
	}

	public void setTermcourseId(String termcourseId) {
		this.termcourseId = termcourseId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getTermId() {
		return termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCopyFlg() {
		return copyFlg;
	}

	public void setCopyFlg(String copyFlg) {
		this.copyFlg = copyFlg;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
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

	public BigDecimal getVersion() {
		return version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public String getTeacherEmployeeId() {
		return teacherEmployeeId;
	}

	public void setTeacherEmployeeId(String teacherEmployeeId) {
		this.teacherEmployeeId = teacherEmployeeId;
	}

	public GjtEmployeeInfo getTeacherEmployee() {
		return teacherEmployee;
	}

	public void setTeacherEmployee(GjtEmployeeInfo teacherEmployee) {
		this.teacherEmployee = teacherEmployee;
	}


}