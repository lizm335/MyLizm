package com.gzedu.xlims.pojo.textbook;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the GJT_TEXTBOOK_DISTRIBUTE_DETAIL database table.
 * 
 */
@Entity
@Table(name = "GJT_TEXTBOOK_DISTRIBUTE_DETAIL")
@NamedQuery(name = "GjtTextbookDistributeDetail.findAll", query = "SELECT g FROM GjtTextbookDistributeDetail g")
public class GjtTextbookDistributeDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DETAIL_ID")
	private String detailId;

	private Float price;

	private Integer quantity;

	private Integer status;// 状态：0-未就绪，1-待配送，2-配送中，3-已签收

	@Column(name = "TEXTBOOK_ID")
	private String textbookId;

	// bi-directional many-to-one association to GjtTextbook
	@ManyToOne
	@JoinColumn(name = "TEXTBOOK_ID", insertable = false, updatable = false)
	private GjtTextbook gjtTextbook;

	@Column(name = "DISTRIBUTE_ID", insertable = false, updatable = false)
	private String distributeId;

	// bi-directional many-to-one association to GjtTextbookDistribute
	@ManyToOne
	@JoinColumn(name = "DISTRIBUTE_ID")
	private GjtTextbookDistribute gjtTextbookDistribute;

	public GjtTextbookDistributeDetail() {
	}

	public String getDetailId() {
		return this.detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public Float getPrice() {
		return this.price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public GjtTextbookDistribute getGjtTextbookDistribute() {
		return this.gjtTextbookDistribute;
	}

	public void setGjtTextbookDistribute(GjtTextbookDistribute gjtTextbookDistribute) {
		this.gjtTextbookDistribute = gjtTextbookDistribute;
	}

	public String getDistributeId() {
		return distributeId;
	}

	public void setDistributeId(String distributeId) {
		this.distributeId = distributeId;
	}

}