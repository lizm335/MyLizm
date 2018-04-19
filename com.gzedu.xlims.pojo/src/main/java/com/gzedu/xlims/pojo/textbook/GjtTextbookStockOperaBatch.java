package com.gzedu.xlims.pojo.textbook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
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
@NamedQuery(name="GjtTextbookStockOperaBatch.findAll", query="SELECT g FROM GjtTextbookStockOperaBatch g")
public class GjtTextbookStockOperaBatch implements Serializable {
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
	private List<GjtTextbookDistribute> gjtTextbookDistributes;

	//bi-directional many-to-one association to GjtTextbookStockApproval
	@OneToMany(mappedBy="gjtTextbookStockOperaBatch")
	@OrderBy(value = "CREATED_DT")
	private List<GjtTextbookStockApproval> gjtTextbookStockApprovals;

	//bi-directional many-to-one association to GjtTextbookStockOpera
	@OneToMany(mappedBy="gjtTextbookStockOperaBatch")
	private List<GjtTextbookStockOpera> gjtTextbookStockOperas;
	
	/**
	 * 库存是否充足
	 */
	@Transient
	private boolean stockStatus = true;

	public GjtTextbookStockOperaBatch() {
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

	public List<GjtTextbookDistribute> getGjtTextbookDistributes() {
		return this.gjtTextbookDistributes;
	}

	public void setGjtTextbookDistributes(List<GjtTextbookDistribute> gjtTextbookDistributes) {
		this.gjtTextbookDistributes = gjtTextbookDistributes;
	}

	public GjtTextbookDistribute addGjtTextbookDistribute(GjtTextbookDistribute gjtTextbookDistribute) {
		getGjtTextbookDistributes().add(gjtTextbookDistribute);
		gjtTextbookDistribute.setGjtTextbookStockOperaBatch(this);

		return gjtTextbookDistribute;
	}

	public GjtTextbookDistribute removeGjtTextbookDistribute(GjtTextbookDistribute gjtTextbookDistribute) {
		getGjtTextbookDistributes().remove(gjtTextbookDistribute);
		gjtTextbookDistribute.setGjtTextbookStockOperaBatch(null);

		return gjtTextbookDistribute;
	}

	public List<GjtTextbookStockApproval> getGjtTextbookStockApprovals() {
		return this.gjtTextbookStockApprovals;
	}

	public void setGjtTextbookStockApprovals(List<GjtTextbookStockApproval> gjtTextbookStockApprovals) {
		this.gjtTextbookStockApprovals = gjtTextbookStockApprovals;
	}

	public GjtTextbookStockApproval addGjtTextbookStockApproval(GjtTextbookStockApproval gjtTextbookStockApproval) {
		if (getGjtTextbookStockApprovals() == null) {
			setGjtTextbookStockApprovals(new ArrayList<GjtTextbookStockApproval>());
		}
		
		getGjtTextbookStockApprovals().add(gjtTextbookStockApproval);
		gjtTextbookStockApproval.setGjtTextbookStockOperaBatch(this);

		return gjtTextbookStockApproval;
	}

	public GjtTextbookStockApproval removeGjtTextbookStockApproval(GjtTextbookStockApproval gjtTextbookStockApproval) {
		getGjtTextbookStockApprovals().remove(gjtTextbookStockApproval);
		gjtTextbookStockApproval.setGjtTextbookStockOperaBatch(null);

		return gjtTextbookStockApproval;
	}

	public List<GjtTextbookStockOpera> getGjtTextbookStockOperas() {
		return this.gjtTextbookStockOperas;
	}

	public void setGjtTextbookStockOperas(List<GjtTextbookStockOpera> gjtTextbookStockOperas) {
		this.gjtTextbookStockOperas = gjtTextbookStockOperas;
	}

	public GjtTextbookStockOpera addGjtTextbookStockOpera(GjtTextbookStockOpera gjtTextbookStockOpera) {
		getGjtTextbookStockOperas().add(gjtTextbookStockOpera);
		gjtTextbookStockOpera.setGjtTextbookStockOperaBatch(this);

		return gjtTextbookStockOpera;
	}

	public GjtTextbookStockOpera removeGjtTextbookStockOpera(GjtTextbookStockOpera gjtTextbookStockOpera) {
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