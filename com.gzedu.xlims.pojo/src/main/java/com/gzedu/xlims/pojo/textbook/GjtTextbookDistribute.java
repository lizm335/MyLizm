package com.gzedu.xlims.pojo.textbook;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.gzedu.xlims.pojo.GjtStudentInfo;

/**
 * The persistent class for the GJT_TEXTBOOK_DISTRIBUTE database table.
 * 
 */
@Entity
@Table(name = "GJT_TEXTBOOK_DISTRIBUTE")
@NamedQuery(name = "GjtTextbookDistribute.findAll", query = "SELECT g FROM GjtTextbookDistribute g")
public class GjtTextbookDistribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DISTRIBUTE_ID")
	private String distributeId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "DELETED_BY")
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED_DT")
	private Date deletedDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DISTRIBUTION_DT") // 配送时间
	private Date distributionDt;

	private BigDecimal freight;// 运费

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "ORDER_CODE")
	private String orderCode;// 订单号

	@Temporal(TemporalType.TIMESTAMP) // 签收时间
	@Column(name = "SIGN_DT")
	private Date signDt;

	private int status;// 0-未就绪 1-待配送 2-配送中 3-已签收

	@Column(name = "STUDENT_ID")
	private String studentId;

	@ManyToOne
	@JoinColumn(name = "STUDENT_ID", insertable = false, updatable = false)
	private GjtStudentInfo gjtStudentInfo;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "WAYBILL_CODE")
	private String waybillCode;// 运单号

	@Column(name = "LOGISTICS_COMP")
	private String logisticsComp;// 物流公司

	@Column(name = "PLAN_ID")
	private String planId;// 教材计划ID

	private String address;// 收货地址

	private String gradeId;// 学期id

	private String mobile;

	private String receiver;// 收货人名字

	private BigDecimal price;

	private int belong;

	private BigDecimal payPrice;

	@ManyToOne
	@JoinColumn(name = "PLAN_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtTextbookPlan gjtTextbookPlan;

	// bi-directional many-to-one association to GjtTextbookStockOperaBatch
	@ManyToOne
	@JoinColumn(name = "BATCH_ID")
	private GjtTextbookStockOperaBatch gjtTextbookStockOperaBatch;

	// bi-directional many-to-one association to GjtTextbookDistributeDetail
	@OneToMany(mappedBy = "gjtTextbookDistribute")
	private List<GjtTextbookDistributeDetail> gjtTextbookDistributeDetails;

	@Where(clause = "isDeleted='N'")
	@OneToOne(mappedBy = "gjtTextbookDistribute")
	private GjtTextbookComment gjtTextbookComment;

	public GjtTextbookDistribute() {
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

	public BigDecimal getFreight() {
		return this.freight;
	}

	public void setFreight(BigDecimal feightPrice) {
		this.freight = feightPrice;
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

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
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

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public GjtTextbookPlan getGjtTextbookPlan() {
		return gjtTextbookPlan;
	}

	public void setGjtTextbookPlan(GjtTextbookPlan gjtTextbookPlan) {
		this.gjtTextbookPlan = gjtTextbookPlan;
	}

	public GjtTextbookStockOperaBatch getGjtTextbookStockOperaBatch() {
		return this.gjtTextbookStockOperaBatch;
	}

	public void setGjtTextbookStockOperaBatch(GjtTextbookStockOperaBatch gjtTextbookStockOperaBatch) {
		this.gjtTextbookStockOperaBatch = gjtTextbookStockOperaBatch;
	}

	public List<GjtTextbookDistributeDetail> getGjtTextbookDistributeDetails() {
		return this.gjtTextbookDistributeDetails;
	}

	public void setGjtTextbookDistributeDetails(List<GjtTextbookDistributeDetail> gjtTextbookDistributeDetails) {
		this.gjtTextbookDistributeDetails = gjtTextbookDistributeDetails;
	}

	public GjtTextbookDistributeDetail addGjtTextbookDistributeDetail(
			GjtTextbookDistributeDetail gjtTextbookDistributeDetail) {
		getGjtTextbookDistributeDetails().add(gjtTextbookDistributeDetail);
		gjtTextbookDistributeDetail.setGjtTextbookDistribute(this);

		return gjtTextbookDistributeDetail;
	}

	public GjtTextbookDistributeDetail removeGjtTextbookDistributeDetail(
			GjtTextbookDistributeDetail gjtTextbookDistributeDetail) {
		getGjtTextbookDistributeDetails().remove(gjtTextbookDistributeDetail);
		gjtTextbookDistributeDetail.setGjtTextbookDistribute(null);

		return gjtTextbookDistributeDetail;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public GjtTextbookComment getGjtTextbookComment() {
		return gjtTextbookComment;
	}

	public void setGjtTextbookComment(GjtTextbookComment gjtTextbookComment) {
		this.gjtTextbookComment = gjtTextbookComment;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getBelong() {
		return belong;
	}

	public void setBelong(int belong) {
		this.belong = belong;
	}

	public BigDecimal getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}

	/**
	 * 获取订单总价
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年3月6日 上午10:37:47
	 * @return
	 */
	@Transient
	public BigDecimal getToTalPrice() {
		if (this.payPrice != null && this.payPrice.compareTo(BigDecimal.ZERO) != 0) {
			return this.payPrice;
		} else if (this.price != null && this.price.compareTo(BigDecimal.ZERO) != 0) {
			if (this.freight != null && this.freight.compareTo(BigDecimal.ZERO) != 0) {
				return this.price.add(this.freight);
			}
			return this.price;
		} else if (CollectionUtils.isNotEmpty(this.gjtTextbookDistributeDetails)) {
			BigDecimal tPrice = new BigDecimal(0);
			for (GjtTextbookDistributeDetail detail : gjtTextbookDistributeDetails) {
				tPrice = tPrice.add(new BigDecimal(detail.getPrice()));
			}
			if (this.freight != null && this.freight.compareTo(BigDecimal.ZERO) != 0) {
				return tPrice.add(this.freight).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			return tPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return null;
	}


}