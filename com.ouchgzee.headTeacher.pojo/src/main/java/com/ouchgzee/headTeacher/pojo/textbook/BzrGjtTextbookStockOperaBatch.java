package com.ouchgzee.headTeacher.pojo.textbook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * The persistent class for the GJT_TEXTBOOK_STOCK_OPERA_BATCH database table.
 * 
 */
@Entity
@Table(name="GJT_TEXTBOOK_STOCK_OPERA_BATCH")
// @NamedQuery(name="GjtTextbookStockOperaBatch.findAll", query="SELECT g FROM GjtTextbookStockOperaBatch g")
@Deprecated public class BzrGjtTextbookStockOperaBatch implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BATCH_ID")
	private String batchId;

	@Column(name="BATCH_CODE")
	private String batchCode;

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

	private String description;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="OPERA_TYPE")
	private int operaType;

	private int quantity;

	private int status;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	//bi-directional many-to-one association to GjtTextbookDistribute
	@OneToMany(mappedBy="gjtTextbookStockOperaBatch")
	private List<BzrGjtTextbookDistribute> gjtTextbookDistributes;

	//bi-directional many-to-one association to GjtTextbookStockApproval
	@OneToMany(mappedBy="gjtTextbookStockOperaBatch")
	@OrderBy(value = "CREATED_DT")
	private List<BzrGjtTextbookStockApproval> gjtTextbookStockApprovals;

	//bi-directional many-to-one association to GjtTextbookStockOpera
	@OneToMany(mappedBy="gjtTextbookStockOperaBatch")
	private List<BzrGjtTextbookStockOpera> gjtTextbookStockOperas;
	
	/**
	 * 库存是否充足
	 */
	@Transient
	private boolean stockStatus = true;

	public BzrGjtTextbookStockOperaBatch() {
	}

	public String getBatchId() {
		return this.batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getBatchCode() {
		return this.batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getOperaType() {
		return this.operaType;
	}

	public void setOperaType(int operaType) {
		this.operaType = operaType;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getStatus() {
		return this.status;
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

	public List<BzrGjtTextbookDistribute> getGjtTextbookDistributes() {
		return this.gjtTextbookDistributes;
	}

	public void setGjtTextbookDistributes(List<BzrGjtTextbookDistribute> gjtTextbookDistributes) {
		this.gjtTextbookDistributes = gjtTextbookDistributes;
	}

	public BzrGjtTextbookDistribute addGjtTextbookDistribute(BzrGjtTextbookDistribute gjtTextbookDistribute) {
		getGjtTextbookDistributes().add(gjtTextbookDistribute);
		gjtTextbookDistribute.setGjtTextbookStockOperaBatch(this);

		return gjtTextbookDistribute;
	}

	public BzrGjtTextbookDistribute removeGjtTextbookDistribute(BzrGjtTextbookDistribute gjtTextbookDistribute) {
		getGjtTextbookDistributes().remove(gjtTextbookDistribute);
		gjtTextbookDistribute.setGjtTextbookStockOperaBatch(null);

		return gjtTextbookDistribute;
	}

	public List<BzrGjtTextbookStockApproval> getGjtTextbookStockApprovals() {
		return this.gjtTextbookStockApprovals;
	}

	public void setGjtTextbookStockApprovals(List<BzrGjtTextbookStockApproval> gjtTextbookStockApprovals) {
		this.gjtTextbookStockApprovals = gjtTextbookStockApprovals;
	}

	public BzrGjtTextbookStockApproval addGjtTextbookStockApproval(BzrGjtTextbookStockApproval gjtTextbookStockApproval) {
		if (getGjtTextbookStockApprovals() == null) {
			setGjtTextbookStockApprovals(new ArrayList<BzrGjtTextbookStockApproval>());
		}
		
		getGjtTextbookStockApprovals().add(gjtTextbookStockApproval);
		gjtTextbookStockApproval.setGjtTextbookStockOperaBatch(this);

		return gjtTextbookStockApproval;
	}

	public BzrGjtTextbookStockApproval removeGjtTextbookStockApproval(BzrGjtTextbookStockApproval gjtTextbookStockApproval) {
		getGjtTextbookStockApprovals().remove(gjtTextbookStockApproval);
		gjtTextbookStockApproval.setGjtTextbookStockOperaBatch(null);

		return gjtTextbookStockApproval;
	}

	public List<BzrGjtTextbookStockOpera> getGjtTextbookStockOperas() {
		return this.gjtTextbookStockOperas;
	}

	public void setGjtTextbookStockOperas(List<BzrGjtTextbookStockOpera> gjtTextbookStockOperas) {
		this.gjtTextbookStockOperas = gjtTextbookStockOperas;
	}

	public BzrGjtTextbookStockOpera addGjtTextbookStockOpera(BzrGjtTextbookStockOpera gjtTextbookStockOpera) {
		getGjtTextbookStockOperas().add(gjtTextbookStockOpera);
		gjtTextbookStockOpera.setGjtTextbookStockOperaBatch(this);

		return gjtTextbookStockOpera;
	}

	public BzrGjtTextbookStockOpera removeGjtTextbookStockOpera(BzrGjtTextbookStockOpera gjtTextbookStockOpera) {
		getGjtTextbookStockOperas().remove(gjtTextbookStockOpera);
		gjtTextbookStockOpera.setGjtTextbookStockOperaBatch(null);

		return gjtTextbookStockOpera;
	}

	public boolean isStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(boolean stockStatus) {
		this.stockStatus = stockStatus;
	}

}