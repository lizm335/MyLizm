package com.ouchgzee.headTeacher.pojo.textbook;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;


/**
 * The persistent class for the GJT_TEXTBOOK_DISTRIBUTE database table.
 * 
 */
@Entity
@Table(name="GJT_TEXTBOOK_DISTRIBUTE")
// @NamedQuery(name="GjtTextbookDistribute.findAll", query="SELECT g FROM GjtTextbookDistribute g")
@Deprecated public class BzrGjtTextbookDistribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DISTRIBUTE_ID")
	private String distributeId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="DELETED_BY")
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED_DT")
	private Date deletedDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DISTRIBUTION_DT")
	private Date distributionDt;

	private Float freight;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="ORDER_CODE")
	private String orderCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SIGN_DT")
	private Date signDt;

	private int status;

	@Column(name="STUDENT_ID")
	private String studentId;
	
	@ManyToOne
	@JoinColumn(name="STUDENT_ID", insertable=false, updatable=false)
	private BzrGjtStudentInfo gjtStudentInfo;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name="WAYBILL_CODE")
	private String waybillCode;

	@Column(name="LOGISTICS_COMP")
	private String logisticsComp;

	//bi-directional many-to-one association to GjtTextbookStockOperaBatch
	@ManyToOne
	@JoinColumn(name="BATCH_ID")
	private BzrGjtTextbookStockOperaBatch gjtTextbookStockOperaBatch;

	//bi-directional many-to-one association to GjtTextbookDistributeDetail
	@OneToMany(mappedBy="gjtTextbookDistribute")
	private List<BzrGjtTextbookDistributeDetail> gjtTextbookDistributeDetails;

	public BzrGjtTextbookDistribute() {
	}

	public String getDistributeId() {
		return this.distributeId;
	}

	public void setDistributeId(String distributeId) {
		this.distributeId = distributeId;
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

	public String getDeletedBy() {
		return this.deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedDt() {
		return this.deletedDt;
	}

	public void setDeletedDt(Date deletedDt) {
		this.deletedDt = deletedDt;
	}

	public Date getDistributionDt() {
		return this.distributionDt;
	}

	public void setDistributionDt(Date distributionDt) {
		this.distributionDt = distributionDt;
	}

	public Float getFreight() {
		return this.freight;
	}

	public void setFreight(Float freight) {
		this.freight = freight;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOrderCode() {
		return this.orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Date getSignDt() {
		return this.signDt;
	}

	public void setSignDt(Date signDt) {
		this.signDt = signDt;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public BzrGjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(BzrGjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
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

	public String getWaybillCode() {
		return this.waybillCode;
	}

	public void setWaybillCode(String waybillCode) {
		this.waybillCode = waybillCode;
	}

	public String getLogisticsComp() {
		return logisticsComp;
	}

	public void setLogisticsComp(String logisticsComp) {
		this.logisticsComp = logisticsComp;
	}

	public BzrGjtTextbookStockOperaBatch getGjtTextbookStockOperaBatch() {
		return this.gjtTextbookStockOperaBatch;
	}

	public void setGjtTextbookStockOperaBatch(BzrGjtTextbookStockOperaBatch gjtTextbookStockOperaBatch) {
		this.gjtTextbookStockOperaBatch = gjtTextbookStockOperaBatch;
	}

	public List<BzrGjtTextbookDistributeDetail> getGjtTextbookDistributeDetails() {
		return this.gjtTextbookDistributeDetails;
	}

	public void setGjtTextbookDistributeDetails(List<BzrGjtTextbookDistributeDetail> gjtTextbookDistributeDetails) {
		this.gjtTextbookDistributeDetails = gjtTextbookDistributeDetails;
	}

	public BzrGjtTextbookDistributeDetail addGjtTextbookDistributeDetail(BzrGjtTextbookDistributeDetail gjtTextbookDistributeDetail) {
		getGjtTextbookDistributeDetails().add(gjtTextbookDistributeDetail);
		gjtTextbookDistributeDetail.setGjtTextbookDistribute(this);

		return gjtTextbookDistributeDetail;
	}

	public BzrGjtTextbookDistributeDetail removeGjtTextbookDistributeDetail(BzrGjtTextbookDistributeDetail gjtTextbookDistributeDetail) {
		getGjtTextbookDistributeDetails().remove(gjtTextbookDistributeDetail);
		gjtTextbookDistributeDetail.setGjtTextbookDistribute(null);

		return gjtTextbookDistributeDetail;
	}

}