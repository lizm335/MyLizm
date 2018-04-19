package com.gzedu.xlims.pojo.exam;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

/**
 * The persistent class for the GJT_EXAM_ROOM_NEW database table.
 * 
 */
@Entity
@Table(name = "GJT_EXAM_ROOM_NEW")
@NamedQuery(name = "GjtExamRoomNew.findAll", query = "SELECT g FROM GjtExamRoomNew g")
public class GjtExamRoomNew implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EXAM_ROOM_ID")
	private String examRoomId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "EXAM_POINT_ID")
	private String examPointId;

	@ManyToOne
	@JoinColumn(name = "EXAM_POINT_ID", referencedColumnName = "EXAM_POINT_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtExamPointNew examPonitNew;

	@Column(name = "IS_DELETED")
	@Where(clause = "is_deleted=0")
	private int isDeleted;

	private String name;

	private int seats;

	private boolean status;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "ORDER_NO")
	private int orderNo;

	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "XX_ID")
	private String xxId;

	public GjtExamRoomNew() {
	}

	public String getExamRoomId() {
		return this.examRoomId;
	}

	public void setExamRoomId(String examRoomId) {
		this.examRoomId = examRoomId;
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

	public String getExamPointId() {
		return this.examPointId;
	}

	public void setExamPointId(String examPointId) {
		this.examPointId = examPointId;
	}

	public GjtExamPointNew getExamPonitNew() {
		return examPonitNew;
	}

	public void setExamPonitNew(GjtExamPointNew examPonitNew) {
		this.examPonitNew = examPonitNew;
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

	public int getSeats() {
		return this.seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
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

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

}