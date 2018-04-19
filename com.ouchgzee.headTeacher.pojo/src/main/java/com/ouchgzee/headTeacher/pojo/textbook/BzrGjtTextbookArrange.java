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
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the GJT_TEXTBOOK_ARRANGE database table.
 * 
 */
@Entity
@Table(name = "GJT_TEXTBOOK_ARRANGE")
// @NamedQuery(name = "GjtTextbookArrange.findAll", query = "SELECT g FROM GjtTextbookArrange g")
@Deprecated public class BzrGjtTextbookArrange implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ARRANGE_ID")
	private String arrangeId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	// bi-directional many-to-one association to GjtTextbookPlan
	@ManyToOne
	@JoinColumn(name = "PLAN_ID")
	private BzrGjtTextbookPlan gjtTextbookPlan;

	@Column(name = "PLAN_ID", insertable = false, updatable = false)
	private String planId;

	// bi-directional many-to-one association to GjtTextbookOrderDetail
	@OneToMany(mappedBy = "gjtTextbookArrange", fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<BzrGjtTextbookOrderDetail> gjtTextbookOrderDetails;

	// bi-directional many-to-one association to GjtTextbook
	@ManyToOne
	@JoinColumn(name = "TEXTBOOK_ID")
	private BzrGjtTextbook gjtTextbook;

	@Column(name = "TEXTBOOK_ID", insertable = false, updatable = false)
	private String textbookId;

	@Transient
	private int status;

	public BzrGjtTextbookArrange() {
	}

	public String getArrangeId() {
		return this.arrangeId;
	}

	public void setArrangeId(String arrangeId) {
		this.arrangeId = arrangeId;
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
		gjtTextbookOrderDetail.setGjtTextbookArrange(this);

		return gjtTextbookOrderDetail;
	}

	public BzrGjtTextbookOrderDetail removeGjtTextbookOrderDetail(BzrGjtTextbookOrderDetail gjtTextbookOrderDetail) {
		getGjtTextbookOrderDetails().remove(gjtTextbookOrderDetail);
		gjtTextbookOrderDetail.setGjtTextbookArrange(null);

		return gjtTextbookOrderDetail;
	}

	public BzrGjtTextbook getGjtTextbook() {
		return this.gjtTextbook;
	}

	public void setGjtTextbook(BzrGjtTextbook gjtTextbook) {
		this.gjtTextbook = gjtTextbook;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getTextbookId() {
		return textbookId;
	}

	public void setTextbookId(String textbookId) {
		this.textbookId = textbookId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "GjtTextbookArrange [arrangeId=" + arrangeId + ", createdBy=" + createdBy + ", createdDt=" + createdDt
				+ ", gjtTextbookPlan=" + gjtTextbookPlan + ", gjtTextbook=" + gjtTextbook + "]";
	}

}