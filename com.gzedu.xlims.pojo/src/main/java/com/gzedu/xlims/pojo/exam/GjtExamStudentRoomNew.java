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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.pojo.GjtStudentInfo;

/**
 * The persistent class for the GJT_EXAM_STUDENT_ROOM_NEW database table.
 * 
 */
@Entity
@Table(name = "GJT_EXAM_STUDENT_ROOM_NEW")
@NamedQuery(name = "GjtExamStudentRoomNew.findAll", query = "SELECT g FROM GjtExamStudentRoomNew g")
public class GjtExamStudentRoomNew implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPOINTMENT_ID", referencedColumnName = "APPOINTMENT_ID", insertable = false, updatable = false)
	private GjtExamAppointmentNew gjtExamAppointmentNew;// 考试预约记录

	@Column(name = "APPOINTMENT_ID")
	private String appointmentId;

	@Column(name = "EXAM_BATCH_ID")
	private String examBatchId;

	@Column(name = "EXAM_BATCH_CODE")
	private String examBatchCode;

	@Column(name = "EXAM_PLAN_ID")
	private String examPlanId;

	@Column(name = "EXAM_POINT_ID")
	private String examPointId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAM_POINT_ID", referencedColumnName = "EXAM_POINT_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtExamPointNew gjtExamPointNew;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAM_PLAN_ID", referencedColumnName = "EXAM_PLAN_ID", insertable = false, updatable = false)
	private GjtExamPlanNew gjtExamPlanNew;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAM_ROOM_ID", referencedColumnName = "EXAM_ROOM_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtExamRoomNew gjtExamRoomNew;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID", referencedColumnName = "STUDENT_ID", insertable = false, updatable = false)
	private GjtStudentInfo gjtStudentInfo;

	@Column(name = "EXAM_ROOM_ID")
	private String examRoomId;

	@Column(name = "SEAT_NO")
	private int seatNo;

	@Column(name = "EXAM_TYPE")
	private int examType;

	@Column(name = "STUDENT_ID")
	private String studentId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	private String memo;

	public GjtExamStudentRoomNew() {
		super();
	}

	public GjtExamStudentRoomNew(String examBatchCode, String examPlanId, String examPointId, String examRoomId,
			int seatNo, int examType) {
		super();
		this.id = UUIDUtils.random().toString();
		this.examPlanId = examPlanId;
		this.examPointId = examPointId;
		this.examRoomId = examRoomId;
		this.seatNo = seatNo;
		this.examType = examType;
		this.examBatchCode = examBatchCode;
		this.createdBy = "排考";
		this.createdDt = new Date();
		this.memo = "排考";
	}

	@Override
	public boolean equals(Object obj) {
		GjtExamStudentRoomNew o = (GjtExamStudentRoomNew) obj;
		return this.examPlanId.equals(o.getExamPlanId()) && examRoomId.equals(o.getExamRoomId())
				&& seatNo == o.getSeatNo();
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

	public String getExamBatchId() {
		return this.examBatchId;
	}

	public void setExamBatchId(String examBatchId) {
		this.examBatchId = examBatchId;
	}

	public String getExamPlanId() {
		return this.examPlanId;
	}

	public void setExamPlanId(String examPlanId) {
		this.examPlanId = examPlanId;
	}

	public String getExamPointId() {
		return this.examPointId;
	}

	public void setExamPointId(String examPointId) {
		this.examPointId = examPointId;
	}

	public GjtExamAppointmentNew getGjtExamAppointmentNew() {
		return gjtExamAppointmentNew;
	}

	public void setGjtExamAppointmentNew(GjtExamAppointmentNew gjtExamAppointmentNew) {
		this.gjtExamAppointmentNew = gjtExamAppointmentNew;
	}

	public GjtExamPointNew getGjtExamPointNew() {
		return gjtExamPointNew;
	}

	public void setGjtExamPointNew(GjtExamPointNew gjtExamPointNew) {
		this.gjtExamPointNew = gjtExamPointNew;
	}

	public String getExamRoomId() {
		return this.examRoomId;
	}

	public void setExamRoomId(String examRoomId) {
		this.examRoomId = examRoomId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getSeatNo() {
		return this.seatNo;
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

	public String getExamBatchCode() {
		return examBatchCode;
	}

	public void setExamBatchCode(String examBatchCode) {
		this.examBatchCode = examBatchCode;
	}

	public int getExamType() {
		return examType;
	}

	public void setExamType(int examType) {
		this.examType = examType;
	}

	public GjtExamRoomNew getGjtExamRoomNew() {
		return gjtExamRoomNew;
	}

	public void setGjtExamRoomNew(GjtExamRoomNew gjtExamRoomNew) {
		this.gjtExamRoomNew = gjtExamRoomNew;
	}

	public GjtExamPlanNew getGjtExamPlanNew() {
		return gjtExamPlanNew;
	}

	public void setGjtExamPlanNew(GjtExamPlanNew gjtExamPlanNew) {
		this.gjtExamPlanNew = gjtExamPlanNew;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

}