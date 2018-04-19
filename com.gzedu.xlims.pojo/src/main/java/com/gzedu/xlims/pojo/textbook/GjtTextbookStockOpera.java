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
 * The persistent class for the GJT_TEXTBOOK_STOCK_OPERA database table.
 * 
 */
@Entity
@Table(name="GJT_TEXTBOOK_STOCK_OPERA")
@NamedQuery(name="GjtTextbookStockOpera.findAll", query="SELECT g FROM GjtTextbookStockOpera g")
public class GjtTextbookStockOpera implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="OPERA_ID")
	private String operaId;

	@Column(name="ACTUAL_QUANTITY")
	private int actualQuantity;

	@Column(name="APPLY_QUANTITY")
	private int applyQuantity;

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

	private int status;

	@Column(name="STOCK_QUANTITY")
	private int stockQuantity;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name="TEXTBOOK_ID")
	private String textbookId;

	//bi-directional many-to-one association to GjtTextbook
	@ManyToOne
	@JoinColumn(name="TEXTBOOK_ID", insertable=false, updatable=false)
	private GjtTextbook gjtTextbook;

	//bi-directional many-to-one association to GjtTextbookStockOperaBatch
	@ManyToOne
	@JoinColumn(name="BATCH_ID")
	private GjtTextbookStockOperaBatch gjtTextbookStockOperaBatch;

	public GjtTextbookStockOpera() {
	}

	public String getOperaId() {
		return this.operaId;
	}

	public void setOperaId(String operaId) {
		this.operaId = operaId;
	}

	public int getActualQuantity() {
		return this.actualQuantity;
	}

	public void setActualQuantity(int actualQuantity) {
		this.actualQuantity = actualQuantity;
	}

	public int getApplyQuantity() {
		return this.applyQuantity;
	}

	public void setApplyQuantity(int applyQuantity) {
		this.applyQuantity = applyQuantity;
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

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStockQuantity() {
		return this.stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
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

	public String getTextbookId() {
		return textbookId;
	}

	public void setTextbookId(String textbookId) {
		this.textbookId = textbookId;
	}

	public GjtTextbook getGjtTextbook() {
		return this.gjtTextbook;
	}

	public void setGjtTextbook(GjtTextbook gjtTextbook) {
		this.gjtTextbook = gjtTextbook;
	}

	public GjtTextbookStockOperaBatch getGjtTextbookStockOperaBatch() {
		return this.gjtTextbookStockOperaBatch;
	}

	public void setGjtTextbookStockOperaBatch(GjtTextbookStockOperaBatch gjtTextbookStockOperaBatch) {
		this.gjtTextbookStockOperaBatch = gjtTextbookStockOperaBatch;
	}

}