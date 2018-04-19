package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


/**
 * The persistent class for the GJT_SCHOOL_ROLL_TRANS_COST database table.
 * 
 */
@Entity
@Table(name="GJT_SCHOOL_ROLL_TRANS_COST")
@NamedQuery(name="GjtSchoolRollTransCost.findAll", query="SELECT g FROM GjtSchoolRollTransCost g")
public class GjtSchoolRollTransCost implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	@Column(name="TRANSACTION_ID")
	private String transactionId;
	
	@Column(name="BACK_PRICE_VOUCHER")
	private String backPriceVoucher;//退费凭证

	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DT",insertable = false,updatable = false)
	private Date createdDt;//创建时间

	@Column(name="NETWORK_MESSAGE_PRICE")
	private double networkMessagePrice;//网络通信费

	@Column(name="ORIGINAL_PRICE")
	private double originalPrice;//学费原价

	@Column(name="PAIDIN_PRICE")
	private double paidinPrice;//学费已缴纳金额

	@Column(name="PAYABLE_PRICE")
	private double payablePrice;//学费应缴纳金额

	@Column(name="RECEIVED_PRICE")
	private double receivedPrice;//实收费用

	@Column(name="REDUCED_PRICE")
	private double reducedPrice;//学费优惠价

	@Column(name="ROLL_REGISTER_PRICE")
	private double rollRegisterPrice;//学籍注册费

	@Column(name="SHOULD_BACK_PRICE")
	private double shouldBackPrice;//应退费用
	
	@Column(name="REAL_BACK_PRICE")
	private double realBackPrice;//实际退款费用

	@Column(name="STUDY_PRICE")
	private double studyPrice;//学习费

	@Column(name="TOTAL_PRICE")
	private double totalPrice;//应扣费用总计
	
	@Column(name="SHOULD_TEXTBOOK_PRICE")
	private double shouldTextbookPrice;//应退教材费
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRANSACTION_ID",insertable = false,updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSchoolRollTran gjtSchoolRollTran;//学籍异动表

	public GjtSchoolRollTransCost() {
	}
	
	public String getTransactionId() {
		return transactionId;
	}


	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBackPriceVoucher() {
		return this.backPriceVoucher;
	}

	public void setBackPriceVoucher(String backPriceVoucher) {
		this.backPriceVoucher = backPriceVoucher;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public double getNetworkMessagePrice() {
		return networkMessagePrice;
	}

	public void setNetworkMessagePrice(double networkMessagePrice) {
		this.networkMessagePrice = networkMessagePrice;
	}

	public double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public double getPaidinPrice() {
		return paidinPrice;
	}

	public void setPaidinPrice(double paidinPrice) {
		this.paidinPrice = paidinPrice;
	}

	public double getPayablePrice() {
		return payablePrice;
	}

	public void setPayablePrice(double payablePrice) {
		this.payablePrice = payablePrice;
	}

	public double getReceivedPrice() {
		return receivedPrice;
	}

	public void setReceivedPrice(double receivedPrice) {
		this.receivedPrice = receivedPrice;
	}

	public double getReducedPrice() {
		return reducedPrice;
	}

	public void setReducedPrice(double reducedPrice) {
		this.reducedPrice = reducedPrice;
	}

	public double getRollRegisterPrice() {
		return rollRegisterPrice;
	}

	public void setRollRegisterPrice(double rollRegisterPrice) {
		this.rollRegisterPrice = rollRegisterPrice;
	}

	public double getShouldBackPrice() {
		return shouldBackPrice;
	}

	public void setShouldBackPrice(double shouldBackPrice) {
		this.shouldBackPrice = shouldBackPrice;
	}

	public double getRealBackPrice() {
		return realBackPrice;
	}

	public void setRealBackPrice(double realBackPrice) {
		this.realBackPrice = realBackPrice;
	}

	public double getStudyPrice() {
		return studyPrice;
	}

	public void setStudyPrice(double studyPrice) {
		this.studyPrice = studyPrice;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public GjtSchoolRollTran getGjtSchoolRollTran() {
		return gjtSchoolRollTran;
	}

	public void setGjtSchoolRollTran(GjtSchoolRollTran gjtSchoolRollTran) {
		this.gjtSchoolRollTran = gjtSchoolRollTran;
	}

	public double getShouldTextbookPrice() {
		return shouldTextbookPrice;
	}

	public void setShouldTextbookPrice(double shouldTextbookPrice) {
		this.shouldTextbookPrice = shouldTextbookPrice;
	}
}