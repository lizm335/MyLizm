package com.ouchgzee.headTeacher.pojo.textbook;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


/**
 * The persistent class for the GJT_TEXTBOOK_ORDER database table.
 * 
 */
@Entity
@Table(name="GJT_TEXTBOOK_ORDER")
// @NamedQuery(name="GjtTextbookOrder.findAll", query="SELECT g FROM GjtTextbookOrder g")
@Deprecated public class BzrGjtTextbookOrder implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ORDER_ID")
	private String orderId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="DISTRIBUTE_NUM")
	private Integer distributeNum;

	@Column(name="ORDER_NUM")
	private Integer orderNum;

	@Column(name="ORDER_PRICE")
	private Float orderPrice;

	private String reason;

	private int status;

	@Column(name="STOCK_NUM")
	private Integer stockNum;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name="PLAN_ID")
	private String planId;

	//bi-directional many-to-one association to GjtTextbookPlan
	@ManyToOne
	@JoinColumn(name="PLAN_ID", insertable = false, updatable = false)
	private BzrGjtTextbookPlan gjtTextbookPlan;

	//bi-directional many-to-one association to GjtTextbookOrderDetail
	@OneToMany(mappedBy="gjtTextbookOrder", fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<BzrGjtTextbookOrderDetail> gjtTextbookOrderDetails;

	public BzrGjtTextbookOrder() {
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public Integer getDistributeNum() {
		return this.distributeNum;
	}

	public void setDistributeNum(Integer distributeNum) {
		this.distributeNum = distributeNum;
	}

	public Integer getOrderNum() {
		return this.orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Float getOrderPrice() {
		return this.orderPrice;
	}

	public void setOrderPrice(Float orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Integer getStockNum() {
		return this.stockNum;
	}

	public void setStockNum(Integer stockNum) {
		this.stockNum = stockNum;
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

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public BzrGjtTextbookPlan getGjtTextbookPlan() {
		return this.gjtTextbookPlan;
	}

	public void setGjtTextbookPlan(BzrGjtTextbookPlan gjtTextbookPlan) {
		this.gjtTextbookPlan = gjtTextbookPlan;
	}

	public List<BzrGjtTextbookOrderDetail> getGjtTextbookOrderDetails() {
		return this.gjtTextbookOrderDetails;
	}

	public void setGjtTextbookOrderDetails(List<BzrGjtTextbookOrderDetail> gjtTextbookOrderDetails) {
		this.gjtTextbookOrderDetails = gjtTextbookOrderDetails;
	}

	public BzrGjtTextbookOrderDetail addGjtTextbookOrderDetail(BzrGjtTextbookOrderDetail gjtTextbookOrderDetail) {
		getGjtTextbookOrderDetails().add(gjtTextbookOrderDetail);
		gjtTextbookOrderDetail.setGjtTextbookOrder(this);

		return gjtTextbookOrderDetail;
	}

	public BzrGjtTextbookOrderDetail removeGjtTextbookOrderDetail(BzrGjtTextbookOrderDetail gjtTextbookOrderDetail) {
		getGjtTextbookOrderDetails().remove(gjtTextbookOrderDetail);
		gjtTextbookOrderDetail.setGjtTextbookOrder(null);

		return gjtTextbookOrderDetail;
	}

	@Override
	public String toString() {
		return "GjtTextbookOrder [orderId=" + orderId + ", createdBy=" + createdBy + ", createdDt=" + createdDt
				+ ", distributeNum=" + distributeNum + ", orderNum=" + orderNum + ", orderPrice=" + orderPrice
				+ ", reason=" + reason + ", status=" + status + ", stockNum=" + stockNum + ", updatedBy=" + updatedBy
				+ ", updatedDt=" + updatedDt + ", gjtTextbookPlan=" + gjtTextbookPlan + "]";
	}

}