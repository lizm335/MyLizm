package com.ouchgzee.headTeacher.pojo.textbook;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * The persistent class for the GJT_TEXTBOOK_STOCK database table.
 * 
 */
@Entity
@Table(name="GJT_TEXTBOOK_STOCK")
// @NamedQuery(name="GjtTextbookStock.findAll", query="SELECT g FROM GjtTextbookStock g")
@Deprecated public class BzrGjtTextbookStock implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="STOCK_ID")
	private String stockId;

	@Column(name="IN_STOCK_NUM")
	private int inStockNum;

	@Column(name="OUT_STOCK_NUM")
	private int outStockNum;

	@Column(name="PLAN_OUT_STOCK_NUM")
	private int planOutStockNum;

	@Column(name="STOCK_NUM")
	private int stockNum;

	//bi-directional many-to-one association to GjtTextbook
	@OneToOne
	@JoinColumn(name="TEXTBOOK_ID")
	private BzrGjtTextbook gjtTextbook;

	public BzrGjtTextbookStock() {
	}

	public String getStockId() {
		return this.stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public int getInStockNum() {
		return this.inStockNum;
	}

	public void setInStockNum(int inStockNum) {
		this.inStockNum = inStockNum;
	}

	public int getOutStockNum() {
		return this.outStockNum;
	}

	public void setOutStockNum(int outStockNum) {
		this.outStockNum = outStockNum;
	}

	public int getPlanOutStockNum() {
		return this.planOutStockNum;
	}

	public void setPlanOutStockNum(int planOutStockNum) {
		this.planOutStockNum = planOutStockNum;
	}

	public int getStockNum() {
		return this.stockNum;
	}

	public void setStockNum(int stockNum) {
		this.stockNum = stockNum;
	}

	public BzrGjtTextbook getGjtTextbook() {
		return this.gjtTextbook;
	}

	public void setGjtTextbook(BzrGjtTextbook gjtTextbook) {
		this.gjtTextbook = gjtTextbook;
	}

}