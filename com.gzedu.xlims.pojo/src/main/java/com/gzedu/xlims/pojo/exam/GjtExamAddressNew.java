package com.gzedu.xlims.pojo.exam;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Where;

import java.util.Date;


/**
 * The persistent class for the GJT_EXAM_ADDRESS_NEW database table.
 * 
 */
@Entity
@Table(name="GJT_EXAM_ADDRESS_NEW")
@NamedQuery(name="GjtExamAddressNew.findAll", query="SELECT g FROM GjtExamAddressNew g")
public class GjtExamAddressNew implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EXAM_ADDRESS_ID")
	private String examAddressId;

	private String address;

	@Column(name="CREATED_BY")
	private String createdBy;

	
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="EXAM_ADDRESS_CODE")
	private String examAddressCode;

	@Column(name="EXAM_BATCH_ID")
	private String examBatchId;

	@Column(name="IS_DELETED")
	@Where(clause = "is_deleted=0")
	private int isDeleted;

	@Column(name="LINK_PHONE")
	private String linkPhone;

	private String linkman;

	private String location;

	private String name;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name="XX_ID")
	private String xxId;

	public GjtExamAddressNew() {
	}

	public String getExamAddressId() {
		return this.examAddressId;
	}

	public void setExamAddressId(String examAddressId) {
		this.examAddressId = examAddressId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getExamAddressCode() {
		return this.examAddressCode;
	}

	public void setExamAddressCode(String examAddressCode) {
		this.examAddressCode = examAddressCode;
	}

	public String getExamBatchId() {
		return this.examBatchId;
	}

	public void setExamBatchId(String examBatchId) {
		this.examBatchId = examBatchId;
	}

	public int getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getLinkPhone() {
		return this.linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getLinkman() {
		return this.linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

}