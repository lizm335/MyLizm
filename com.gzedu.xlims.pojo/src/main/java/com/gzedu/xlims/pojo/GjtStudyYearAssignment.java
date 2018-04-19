package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the GJT_STUDYYEAR_ASSIGNMENT database table.
 * 
 */
@Entity
@Table(name = "GJT_STUDYYEAR_ASSIGNMENT")
@NamedQuery(name = "GjtStudyYearAssignment.findAll", query = "SELECT g FROM GjtStudyYearAssignment g")
public class GjtStudyYearAssignment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ASSIGNMENT_ID")
	private String assignmentId;

	@OneToOne
	@JoinColumn(name = "STUDYYEAR_ID") // 学年度
	private GjtStudyYearInfo gjtStudyYearInfo;

	@Column(name = "ASSIGNMENT_DO") // 任务执行人
	private String assignmentDo;

	@Column(name = "ASSIGNMENT_ENACT") // 任务制定人
	private String assignmentEnact;

	@Column(name = "ASSIGNMENT_NAME") // 任务名称
	private String assignmentName;

	@Column(name = "CREATED_BY") // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建日期
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Temporal(TemporalType.DATE) // 结束时间
	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "IS_DELETED") // 是否删除
	private String isDeleted;

	private String memo;// 备注

	private String parallelism;// 对应功能

	private String sort;// 序号

	@Temporal(TemporalType.DATE) // 开始时间
	@Column(name = "START_DATE")
	private Date startDate;

	private String status;// 完成状态，0未完成，1完成

	@Column(name = "UPDATED_BY") // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改日期
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;// 版本

	public GjtStudyYearAssignment() {
	}

	public String getAssignmentId() {
		return this.assignmentId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getAssignmentDo() {
		return this.assignmentDo;
	}

	public void setAssignmentDo(String assignmentDo) {
		this.assignmentDo = assignmentDo;
	}

	public String getAssignmentEnact() {
		return this.assignmentEnact;
	}

	public void setAssignmentEnact(String assignmentEnact) {
		this.assignmentEnact = assignmentEnact;
	}

	public String getAssignmentName() {
		return this.assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
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

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public String getParallelism() {
		return this.parallelism;
	}

	public void setParallelism(String parallelism) {
		this.parallelism = parallelism;
	}

	public String getSort() {
		return this.sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public GjtStudyYearInfo getGjtStudyYearInfo() {
		return gjtStudyYearInfo;
	}

	public void setGjtStudyYearInfo(GjtStudyYearInfo gjtStudyYearInfo) {
		this.gjtStudyYearInfo = gjtStudyYearInfo;
	}

}