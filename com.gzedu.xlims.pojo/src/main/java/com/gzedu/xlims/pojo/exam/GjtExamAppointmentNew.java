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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.gzedu.xlims.pojo.GjtStudentInfo;

/**
 * The persistent class for the GJT_EXAM_APPOINTMENT_NEW database table.
 * 预约数据如果在某批次考试过去之后仍然未排考则需要通过定时任务该数据其设为过期状态
 */
@Entity
@Table(name = "GJT_EXAM_APPOINTMENT_NEW")
@NamedQuery(name = "GjtExamAppointmentNew.findAll", query = "SELECT g FROM GjtExamAppointmentNew g")
public class GjtExamAppointmentNew implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "APPOINTMENT_ID")
	private String appointmentId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAM_PLAN_ID", referencedColumnName = "EXAM_PLAN_ID", insertable = false, updatable = false)
	private GjtExamPlanNew examPlanNew;

	@Column(name = "EXAM_PLAN_ID")
	private String examPlanId;
	
	@Column(name="EXAM_ROUND_ID")
	private String examRoundId;

	@OneToOne(mappedBy = "gjtExamAppointmentNew", fetch = FetchType.LAZY)
	private GjtExamStudentRoomNew gjtExamStudentRoomNew;// 考生考场座位详情

	@Column(name = "IS_DELETED")
	@Where(clause = "is_deleted=0")
	private int isDeleted;

	@Column(name = "SEAT_NO")
	private int seatNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID", referencedColumnName = "STUDENT_ID", insertable = false, updatable = false)
	private GjtStudentInfo student;

	@Column(name = "STUDENT_ID")
	private String studentId;

	@Column(name = "TYPE")
	private int type;

	@Column(name = "STATUS")
	private int status;// 0-待排考;1-已排考;2-已过期

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;
	
	@Column(name = "REC_ID")
	private String recId;

	@Column(name = "XX_ID")
	private String xxId;

	@Column(name = "EXAM_BATCH_CODE")
	private String examBatchCode;

	/**
	 * 考试状态，0为待开始，1为已通过，2为未通过，3为考试中，4为待批改，5为已截止
	 */
	@Column(name = "EXAM_STATUS")
	private String examStatus;

	/**
	 * 考试成绩
	 */
	@Column(name = "EXAM_SCORE")
	private String examScore;
	
	@Transient
	private String examPointCode;  //考点编号
	
	@Transient
	private String examPointName;  //考点名称

	@Transient
	private String address;  //考点地址

	@Transient
	private boolean isResit;  //是否补考

	@Transient
	private String resitCost;  //补考费用

	public GjtExamAppointmentNew() {
	}

	public String getAppointmentId() {
		return this.appointmentId;
	}

	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
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

	public String getExamPlanId() {
		return this.examPlanId;
	}

	public void setExamPlanId(String examPlanId) {
		this.examPlanId = examPlanId;
	}

	public int getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(int seatNo) {
		this.seatNo = seatNo;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
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

	public GjtExamStudentRoomNew getGjtExamStudentRoomNew() {
		return gjtExamStudentRoomNew;
	}

	public void setGjtExamStudentRoomNew(GjtExamStudentRoomNew gjtExamStudentRoomNew) {
		this.gjtExamStudentRoomNew = gjtExamStudentRoomNew;
	}

	public String getExamRoundId() {
		return examRoundId;
	}

	public void setExamRoundId(String examRoundId) {
		this.examRoundId = examRoundId;
	}

	public String getExamPointCode() {
		return examPointCode;
	}

	public void setExamPointCode(String examPointCode) {
		this.examPointCode = examPointCode;
	}

	public String getExamPointName() {
		return examPointName;
	}

	public void setExamPointName(String examPointName) {
		this.examPointName = examPointName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean getIsResit() {
		return isResit;
	}

	public void setIsResit(boolean isResit) {
		this.isResit = isResit;
	}

	public String getResitCost() {
		return resitCost;
	}

	public void setResitCost(String resitCost) {
		this.resitCost = resitCost;
	}

	public String getRecId() {
		return recId;
	}

	public void setRecId(String recId) {
		this.recId = recId;
	}

	public String getExamBatchCode() {
		return examBatchCode;
	}

	public void setExamBatchCode(String examBatchCode) {
		this.examBatchCode = examBatchCode;
	}

	public boolean isResit() {
		return isResit;
	}

	public void setResit(boolean resit) {
		isResit = resit;
	}

	public String getExamStatus() {
		return examStatus;
	}

	public void setExamStatus(String examStatus) {
		this.examStatus = examStatus;
	}

	public String getExamScore() {
		return examScore;
	}

	public void setExamScore(String examScore) {
		this.examScore = examScore;
	}
}