package com.gzedu.xlims.pojo.textbook;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the GJT_TEXTBOOK_STOCK_APPROVAL database table.
 * 
 */
@Entity
@Table(name="GJT_TEXTBOOK_STOCK_APPROVAL")
@NamedQuery(name="GjtTextbookStockApproval.findAll", query="SELECT g FROM GjtTextbookStockApproval g")
public class GjtTextbookStockApproval implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="APPROVAL_ID")
	private String approvalId;

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

	@Column(name="OPERA_ROLE")
	private int operaRole;

	@Column(name="OPERA_TYPE")
	private int operaType;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	//bi-directional many-to-one association to GjtTextbookStockOperaBatch
	@ManyToOne
	@JoinColumn(name="BATCH_ID")
	private GjtTextbookStockOperaBatch gjtTextbookStockOperaBatch;

	public GjtTextbookStockApproval() {
	}

	public String getApprovalId() {
		return this.approvalId;
	}

	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
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

	public int getOperaRole() {
		return this.operaRole;
	}

	public void setOperaRole(int operaRole) {
		this.operaRole = operaRole;
	}

	public int getOperaType() {
		return this.operaType;
	}

	public void setOperaType(int operaType) {
		this.operaType = operaType;
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

	public GjtTextbookStockOperaBatch getGjtTextbookStockOperaBatch() {
		return this.gjtTextbookStockOperaBatch;
	}

	public void setGjtTextbookStockOperaBatch(GjtTextbookStockOperaBatch gjtTextbookStockOperaBatch) {
		this.gjtTextbookStockOperaBatch = gjtTextbookStockOperaBatch;
	}

}