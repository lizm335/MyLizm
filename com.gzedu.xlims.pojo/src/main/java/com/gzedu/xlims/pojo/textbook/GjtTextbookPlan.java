package com.gzedu.xlims.pojo.textbook;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gzedu.xlims.pojo.GjtGrade;

/**
 * The persistent class for the GJT_TEXTBOOK_PLAN database table.
 * 
 */
@Entity
@Table(name = "GJT_TEXTBOOK_PLAN")
@NamedQuery(name = "GjtTextbookPlan.findAll", query = "SELECT g FROM GjtTextbookPlan g")
public class GjtTextbookPlan implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PLAN_ID")
	private String planId;

	@Temporal(TemporalType.DATE)
	@Column(name = "ARRANGE_EDATE")
	private Date arrangeEdate;

	@Temporal(TemporalType.DATE)
	@Column(name = "ARRANGE_SDATE")
	private Date arrangeSdate;

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

	@Column(name = "GRADE_ID")
	private String gradeId;

	@ManyToOne
	@JoinColumn(name = "GRADE_ID", insertable = false, updatable = false)
	private GjtGrade gjtGrade;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	/*
	 * @Column(name = "NADDRESS_CONFIRM_EDATE") private int
	 * naddressConfirmEdate;
	 * 
	 * @Column(name = "NADDRESS_CONFIRM_SDATE") private int
	 * naddressConfirmSdate;
	 * 
	 * @Column(name = "NDISTRIBUTE_EDATE") private int ndistributeEdate;
	 * 
	 * @Column(name = "NDISTRIBUTE_SDATE") private int ndistributeSdate;
	 * 
	 * @Column(name = "NFEEDBACK_EDATE") private int nfeedbackEdate;
	 * 
	 * @Column(name = "NFEEDBACK_SDATE") private int nfeedbackSdate;
	 */

	@Temporal(TemporalType.DATE)
	@Column(name = "OADDRESS_CONFIRM_EDATE")
	private Date oaddressConfirmEdate;

	@Temporal(TemporalType.DATE)
	@Column(name = "OADDRESS_CONFIRM_SDATE")
	private Date oaddressConfirmSdate;

	@Temporal(TemporalType.DATE)
	@Column(name = "ODISTRIBUTE_EDATE")
	private Date odistributeEdate;

	@Temporal(TemporalType.DATE)
	@Column(name = "ODISTRIBUTE_SDATE")
	private Date odistributeSdate;

	@Temporal(TemporalType.DATE)
	@Column(name = "OFEEDBACK_EDATE")
	private Date ofeedbackEdate;

	@Temporal(TemporalType.DATE)
	@Column(name = "OFEEDBACK_SDATE")
	private Date ofeedbackSdate;

	@Temporal(TemporalType.DATE)
	@Column(name = "ORDER_EDATE")
	private Date orderEdate;

	@Temporal(TemporalType.DATE)
	@Column(name = "ORDER_SDATE")
	private Date orderSdate;

	@Column(name = "ORG_ID")
	private String orgId;

	@Column(name = "PLAN_CODE")
	private String planCode;

	@Column(name = "PLAN_NAME")
	private String planName;

	private int status;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	// bi-directional many-to-one association to GjtTextbookArrange
	@OneToMany(mappedBy = "gjtTextbookPlan", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtTextbookArrange> gjtTextbookArranges;

	@OneToMany(mappedBy = "gjtTextbookPlan", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtTextbookGrade> gjtTextbookGradeList;

	// bi-directional many-to-one association to GjtTextbookOrder
	@OneToMany(mappedBy = "gjtTextbookPlan", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtTextbookOrder> gjtTextbookOrders;

	// bi-directional many-to-one association to GjtTextbookOrderDetail
	@OneToMany(mappedBy = "gjtTextbookPlan", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtTextbookOrderDetail> gjtTextbookOrderDetails;

	// bi-directional many-to-one association to GjtTextbookPlanApproval
	@OneToMany(mappedBy = "gjtTextbookPlan", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@OrderBy(value = "CREATED_DT")
	private List<GjtTextbookPlanApproval> gjtTextbookPlanApprovals;

	public GjtTextbookPlan() {
	}

	public String getPlanId() {
		return this.planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public Date getArrangeEdate() {
		return this.arrangeEdate;
	}

	public void setArrangeEdate(Date arrangeEdate) {
		this.arrangeEdate = arrangeEdate;
	}

	public Date getArrangeSdate() {
		return this.arrangeSdate;
	}

	public void setArrangeSdate(Date arrangeSdate) {
		this.arrangeSdate = arrangeSdate;
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

	public String getGradeId() {
		return this.gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public GjtGrade getGjtGrade() {
		return gjtGrade;
	}

	public void setGjtGrade(GjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	/*
	 * public int getNaddressConfirmEdate() { return this.naddressConfirmEdate;
	 * }
	 * 
	 * public void setNaddressConfirmEdate(int naddressConfirmEdate) {
	 * this.naddressConfirmEdate = naddressConfirmEdate; }
	 * 
	 * public int getNaddressConfirmSdate() { return this.naddressConfirmSdate;
	 * }
	 * 
	 * public void setNaddressConfirmSdate(int naddressConfirmSdate) {
	 * this.naddressConfirmSdate = naddressConfirmSdate; }
	 * 
	 * public int getNdistributeEdate() { return this.ndistributeEdate; }
	 * 
	 * public void setNdistributeEdate(int ndistributeEdate) {
	 * this.ndistributeEdate = ndistributeEdate; }
	 * 
	 * public int getNdistributeSdate() { return this.ndistributeSdate; }
	 * 
	 * public void setNdistributeSdate(int ndistributeSdate) {
	 * this.ndistributeSdate = ndistributeSdate; }
	 * 
	 * public int getNfeedbackEdate() { return this.nfeedbackEdate; }
	 * 
	 * public void setNfeedbackEdate(int nfeedbackEdate) { this.nfeedbackEdate =
	 * nfeedbackEdate; }
	 * 
	 * public int getNfeedbackSdate() { return this.nfeedbackSdate; }
	 * 
	 * public void setNfeedbackSdate(int nfeedbackSdate) { this.nfeedbackSdate =
	 * nfeedbackSdate; }
	 */

	public Date getOaddressConfirmEdate() {
		return this.oaddressConfirmEdate;
	}

	public List<GjtTextbookGrade> getGjtTextbookGradeList() {
		return gjtTextbookGradeList;
	}

	public void setGjtTextbookGradeList(List<GjtTextbookGrade> gjtTextbookGradeList) {
		this.gjtTextbookGradeList = gjtTextbookGradeList;
	}

	public void setOaddressConfirmEdate(Date oaddressConfirmEdate) {
		this.oaddressConfirmEdate = oaddressConfirmEdate;
	}

	public Date getOaddressConfirmSdate() {
		return this.oaddressConfirmSdate;
	}

	public void setOaddressConfirmSdate(Date oaddressConfirmSdate) {
		this.oaddressConfirmSdate = oaddressConfirmSdate;
	}

	public Date getOdistributeEdate() {
		return this.odistributeEdate;
	}

	public void setOdistributeEdate(Date odistributeEdate) {
		this.odistributeEdate = odistributeEdate;
	}

	public Date getOdistributeSdate() {
		return this.odistributeSdate;
	}

	public void setOdistributeSdate(Date odistributeSdate) {
		this.odistributeSdate = odistributeSdate;
	}

	public Date getOfeedbackEdate() {
		return this.ofeedbackEdate;
	}

	public void setOfeedbackEdate(Date ofeedbackEdate) {
		this.ofeedbackEdate = ofeedbackEdate;
	}

	public Date getOfeedbackSdate() {
		return this.ofeedbackSdate;
	}

	public void setOfeedbackSdate(Date ofeedbackSdate) {
		this.ofeedbackSdate = ofeedbackSdate;
	}

	public Date getOrderEdate() {
		return this.orderEdate;
	}

	public void setOrderEdate(Date orderEdate) {
		this.orderEdate = orderEdate;
	}

	public Date getOrderSdate() {
		return this.orderSdate;
	}

	public void setOrderSdate(Date orderSdate) {
		this.orderSdate = orderSdate;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPlanCode() {
		return this.planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getPlanName() {
		return this.planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
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

	public List<GjtTextbookArrange> getGjtTextbookArranges() {
		return this.gjtTextbookArranges;
	}

	public void setGjtTextbookArranges(List<GjtTextbookArrange> gjtTextbookArranges) {
		this.gjtTextbookArranges = gjtTextbookArranges;
	}

	public GjtTextbookArrange addGjtTextbookArrange(GjtTextbookArrange gjtTextbookArrange) {
		getGjtTextbookArranges().add(gjtTextbookArrange);
		gjtTextbookArrange.setGjtTextbookPlan(this);

		return gjtTextbookArrange;
	}

	public GjtTextbookArrange removeGjtTextbookArrange(GjtTextbookArrange gjtTextbookArrange) {
		getGjtTextbookArranges().remove(gjtTextbookArrange);
		gjtTextbookArrange.setGjtTextbookPlan(null);

		return gjtTextbookArrange;
	}

	public List<GjtTextbookOrder> getGjtTextbookOrders() {
		return this.gjtTextbookOrders;
	}

	public void setGjtTextbookOrders(List<GjtTextbookOrder> gjtTextbookOrders) {
		this.gjtTextbookOrders = gjtTextbookOrders;
	}

	public GjtTextbookOrder addGjtTextbookOrder(GjtTextbookOrder gjtTextbookOrder) {
		getGjtTextbookOrders().add(gjtTextbookOrder);
		gjtTextbookOrder.setGjtTextbookPlan(this);

		return gjtTextbookOrder;
	}

	public GjtTextbookOrder removeGjtTextbookOrder(GjtTextbookOrder gjtTextbookOrder) {
		getGjtTextbookOrders().remove(gjtTextbookOrder);
		gjtTextbookOrder.setGjtTextbookPlan(null);

		return gjtTextbookOrder;
	}

	public List<GjtTextbookOrderDetail> getGjtTextbookOrderDetails() {
		return this.gjtTextbookOrderDetails;
	}

	public void setGjtTextbookOrderDetails(List<GjtTextbookOrderDetail> gjtTextbookOrderDetails) {
		this.gjtTextbookOrderDetails = gjtTextbookOrderDetails;
	}

	public GjtTextbookOrderDetail addGjtTextbookOrderDetail(GjtTextbookOrderDetail gjtTextbookOrderDetail) {
		getGjtTextbookOrderDetails().add(gjtTextbookOrderDetail);
		gjtTextbookOrderDetail.setGjtTextbookPlan(this);

		return gjtTextbookOrderDetail;
	}

	public GjtTextbookOrderDetail removeGjtTextbookOrderDetail(GjtTextbookOrderDetail gjtTextbookOrderDetail) {
		getGjtTextbookOrderDetails().remove(gjtTextbookOrderDetail);
		gjtTextbookOrderDetail.setGjtTextbookPlan(null);

		return gjtTextbookOrderDetail;
	}

	public List<GjtTextbookPlanApproval> getGjtTextbookPlanApprovals() {
		return this.gjtTextbookPlanApprovals;
	}

	public void setGjtTextbookPlanApprovals(List<GjtTextbookPlanApproval> gjtTextbookPlanApprovals) {
		this.gjtTextbookPlanApprovals = gjtTextbookPlanApprovals;
	}

	public GjtTextbookPlanApproval addGjtTextbookPlanApproval(GjtTextbookPlanApproval gjtTextbookPlanApproval) {
		getGjtTextbookPlanApprovals().add(gjtTextbookPlanApproval);
		gjtTextbookPlanApproval.setGjtTextbookPlan(this);

		return gjtTextbookPlanApproval;
	}

	public GjtTextbookPlanApproval removeGjtTextbookPlanApproval(GjtTextbookPlanApproval gjtTextbookPlanApproval) {
		getGjtTextbookPlanApprovals().remove(gjtTextbookPlanApproval);
		gjtTextbookPlanApproval.setGjtTextbookPlan(null);

		return gjtTextbookPlanApproval;
	}

	@Override
	public String toString() {
		return "GjtTextbookPlan [planId=" + planId + ", arrangeEdate=" + arrangeEdate + ", arrangeSdate=" + arrangeSdate
				+ ", createdBy=" + createdBy + ", createdDt=" + createdDt + ", deletedBy=" + deletedBy + ", deletedDt="
				+ deletedDt + ", gradeId=" + gradeId + ", isDeleted=" + isDeleted + ", oaddressConfirmEdate="
				+ oaddressConfirmEdate + ", oaddressConfirmSdate=" + oaddressConfirmSdate + ", odistributeEdate="
				+ odistributeEdate + ", odistributeSdate=" + odistributeSdate + ", ofeedbackEdate=" + ofeedbackEdate
				+ ", ofeedbackSdate=" + ofeedbackSdate + ", orderEdate=" + orderEdate + ", orderSdate=" + orderSdate
				+ ", orgId=" + orgId + ", planCode=" + planCode + ", planName=" + planName + ", status=" + status
				+ ", updatedBy=" + updatedBy + ", updatedDt=" + updatedDt + "]";
	}

}