package com.ouchgzee.headTeacher.pojo.exam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.ouchgzee.headTeacher.pojo.BzrGjtGrade;

/**
 * The persistent class for the GJT_EXAM_BATCH_NEW database table.
 * 
 */
@Entity
@Table(name = "GJT_EXAM_BATCH_NEW")
// @NamedQuery(name = "GjtExamBatchNew.findAll", query = "SELECT g FROM GjtExamBatchNew g")
@Deprecated public class BzrGjtExamBatchNew implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EXAM_BATCH_ID")
	private String examBatchId;

	@Temporal(TemporalType.DATE)
	@Column(name = "BOOK_END")
	private Date bookEnd;  //预约结束时间

	@Temporal(TemporalType.DATE)
	@Column(name = "BOOK_ST")
	private Date bookSt;  //预约开始时间

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "EXAM_BATCH_CODE")
	private String examBatchCode;

	@Column(name = "STUDY_YEAR_ID")
	private String studyYearId;

	@Column(name = "IS_DELETED")
	@Where(clause = "is_deleted=0")
	private int isDeleted;
//	private String isDeleted;

	private String name;

	@Temporal(TemporalType.DATE)
	@Column(name = "OFFLINE_END")
	private Date offlineEnd;

	@Temporal(TemporalType.DATE)
	@Column(name = "OFFLINE_ST")
	private Date offlineSt;

	@Temporal(TemporalType.DATE)
	@Column(name = "ONLINE_END")
	private Date onlineEnd;  //网考结束时间

	@Temporal(TemporalType.DATE)
	@Column(name = "ONLINE_ST")
	private Date onlineSt;  //网考开始时间

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "XX_ID")
	private String xxId;
	
	@Column(name = "GRADE_ID")
	private String gradeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE_ID", insertable = false, updatable = false)
	private BzrGjtGrade gjtGrade;
	
	@Column(name = "PLAN_STATUS")
	private String planStatus;  //考试计划状态（1：待审核，2：审核不通过，3：已发布）

	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_ST")
	private Date planSt;  //开考科目设置开始时间

	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_END")
	private Date planEnd;  //开考科目设置结束时间

	@Temporal(TemporalType.DATE)
	@Column(name = "ARRANGE_ST")
	private Date arrangeSt;  //排考开始时间

	@Temporal(TemporalType.DATE)
	@Column(name = "ARRANGE_END")
	private Date arrangeEnd;  //排考结束时间

	@Temporal(TemporalType.DATE)
	@Column(name = "PROVINCE_ONLINE_ST")
	private Date provinceOnlineSt;  //省网考开始时间

	@Temporal(TemporalType.DATE)
	@Column(name = "PROVINCE_ONLINE_END")
	private Date provinceOnlineEnd;  //省网考结束时间

	@Temporal(TemporalType.DATE)
	@Column(name = "PAPER_ST")
	private Date paperSt;  //笔考开始时间

	@Temporal(TemporalType.DATE)
	@Column(name = "PAPER_END")
	private Date paperEnd;  //笔考结束时间

	@Temporal(TemporalType.DATE)
	@Column(name = "MACHINE_ST")
	private Date machineSt;  //机考开始时间

	@Temporal(TemporalType.DATE)
	@Column(name = "MACHINE_END")
	private Date machineEnd;  //机考结束时间

	@Temporal(TemporalType.DATE)
	@Column(name = "SHAPE_END")
	private Date shapeEnd;  //形考任务登记截止时间`

	@Temporal(TemporalType.DATE)
	@Column(name = "THESIS_END")
	private Date thesisEnd;  //论文截止时间

	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_END")
	private Date reportEnd;  //报告截止时间

	@Temporal(TemporalType.DATE)
	@Column(name = "RECORD_ST")
	private Date recordSt;  //成绩登记开始时间

	@Temporal(TemporalType.DATE)
	@Column(name = "RECORD_END")
	private Date recordEnd;  //成绩登记结束时间
	
	@OneToMany(mappedBy = "gjtExamBatchNew", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@OrderBy(value = "CREATED_DT")
	private List<BzrGjtExamBatchApproval> gjtExamBatchApprovals;

	public BzrGjtExamBatchNew() {
	}

	public String getExamBatchId() {
		return this.examBatchId;
	}

	public void setExamBatchId(String examBatchId) {
		this.examBatchId = examBatchId;
	}

	public Date getBookEnd() {
		return this.bookEnd;
	}

	public void setBookEnd(Date bookEnd) {
		this.bookEnd = bookEnd;
	}

	public Date getBookSt() {
		return this.bookSt;
	}

	public void setBookSt(Date bookSt) {
		this.bookSt = bookSt;
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

	public int getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getOfflineEnd() {
		return this.offlineEnd;
	}

	public void setOfflineEnd(Date offlineEnd) {
		this.offlineEnd = offlineEnd;
	}

	public Date getOfflineSt() {
		return this.offlineSt;
	}

	public void setOfflineSt(Date offlineSt) {
		this.offlineSt = offlineSt;
	}

	public Date getOnlineEnd() {
		return this.onlineEnd;
	}

	public void setOnlineEnd(Date onlineEnd) {
		this.onlineEnd = onlineEnd;
	}

	public Date getOnlineSt() {
		return this.onlineSt;
	}

	public void setOnlineSt(Date onlineSt) {
		this.onlineSt = onlineSt;
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

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getStudyYearId() {
		return studyYearId;
	}

	public void setStudyYearId(String studyYearId) {
		this.studyYearId = studyYearId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public BzrGjtGrade getGjtGrade() {
		return gjtGrade;
	}

	public void setGjtGrade(BzrGjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	public String getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}

	public Date getPlanSt() {
		return planSt;
	}

	public void setPlanSt(Date planSt) {
		this.planSt = planSt;
	}

	public Date getPlanEnd() {
		return planEnd;
	}

	public void setPlanEnd(Date planEnd) {
		this.planEnd = planEnd;
	}

	public Date getArrangeSt() {
		return arrangeSt;
	}

	public void setArrangeSt(Date arrangeSt) {
		this.arrangeSt = arrangeSt;
	}

	public Date getArrangeEnd() {
		return arrangeEnd;
	}

	public void setArrangeEnd(Date arrangeEnd) {
		this.arrangeEnd = arrangeEnd;
	}

	public Date getProvinceOnlineSt() {
		return provinceOnlineSt;
	}

	public void setProvinceOnlineSt(Date provinceOnlineSt) {
		this.provinceOnlineSt = provinceOnlineSt;
	}

	public Date getProvinceOnlineEnd() {
		return provinceOnlineEnd;
	}

	public void setProvinceOnlineEnd(Date provinceOnlineEnd) {
		this.provinceOnlineEnd = provinceOnlineEnd;
	}

	public Date getPaperSt() {
		return paperSt;
	}

	public void setPaperSt(Date paperSt) {
		this.paperSt = paperSt;
	}

	public Date getPaperEnd() {
		return paperEnd;
	}

	public void setPaperEnd(Date paperEnd) {
		this.paperEnd = paperEnd;
	}

	public Date getMachineSt() {
		return machineSt;
	}

	public void setMachineSt(Date machineSt) {
		this.machineSt = machineSt;
	}

	public Date getMachineEnd() {
		return machineEnd;
	}

	public void setMachineEnd(Date machineEnd) {
		this.machineEnd = machineEnd;
	}

	public Date getShapeEnd() {
		return shapeEnd;
	}

	public void setShapeEnd(Date shapeEnd) {
		this.shapeEnd = shapeEnd;
	}

	public Date getThesisEnd() {
		return thesisEnd;
	}

	public void setThesisEnd(Date thesisEnd) {
		this.thesisEnd = thesisEnd;
	}

	public Date getReportEnd() {
		return reportEnd;
	}

	public void setReportEnd(Date reportEnd) {
		this.reportEnd = reportEnd;
	}

	public Date getRecordSt() {
		return recordSt;
	}

	public void setRecordSt(Date recordSt) {
		this.recordSt = recordSt;
	}

	public Date getRecordEnd() {
		return recordEnd;
	}

	public void setRecordEnd(Date recordEnd) {
		this.recordEnd = recordEnd;
	}

	public List<BzrGjtExamBatchApproval> getGjtExamBatchApprovals() {
		return gjtExamBatchApprovals;
	}

	public void setGjtExamBatchApprovals(List<BzrGjtExamBatchApproval> gjtExamBatchApprovals) {
		this.gjtExamBatchApprovals = gjtExamBatchApprovals;
	}

}