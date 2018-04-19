package com.gzedu.xlims.pojo.textbook;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gzedu.xlims.pojo.GjtCourse;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the GJT_TEXTBOOK_ARRANGE database table.
 * 
 */
@Entity
@Table(name="GJT_TEXTBOOK_ARRANGE")
@NamedQuery(name="GjtTextbookArrange.findAll", query="SELECT g FROM GjtTextbookArrange g")
public class GjtTextbookArrange implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ARRANGE_ID")
	private String arrangeId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	//bi-directional many-to-one association to GjtTextbookPlan
	@ManyToOne
	@JoinColumn(name="PLAN_ID")
	private GjtTextbookPlan gjtTextbookPlan;

	@Column(name="PLAN_ID", insertable = false, updatable = false)
	private String planId;

	//bi-directional many-to-one association to GjtTextbookOrderDetail
	@OneToMany(mappedBy="gjtTextbookArrange", fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtTextbookOrderDetail> gjtTextbookOrderDetails;

	//bi-directional many-to-one association to GjtTextbook
	@ManyToOne
	@JoinColumn(name="TEXTBOOK_ID")
	private GjtTextbook gjtTextbook;

	@Column(name="TEXTBOOK_ID", insertable = false, updatable = false)
	private String textbookId;
	
	@ManyToMany
	@JoinTable(name = "GJT_TEXTBOOK_ARRANGE_COURSE", joinColumns = {
			@JoinColumn(name = "ARRANGE_ID") }, inverseJoinColumns = { @JoinColumn(name = "COURSE_ID") })
	private List<GjtCourse> gjtCourseList;
	
	@Transient
	private int status;

	public GjtTextbookArrange() {
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

	public GjtTextbookPlan getGjtTextbookPlan() {
		return this.gjtTextbookPlan;
	}

	public void setGjtTextbookPlan(GjtTextbookPlan gjtTextbookPlan) {
		this.gjtTextbookPlan = gjtTextbookPlan;
	}

	public List<GjtTextbookOrderDetail> getGjtTextbookOrderDetails() {
		return this.gjtTextbookOrderDetails;
	}

	public void setGjtTextbookOrderDetails(List<GjtTextbookOrderDetail> gjtTextbookOrderDetails) {
		this.gjtTextbookOrderDetails = gjtTextbookOrderDetails;
	}

	public GjtTextbookOrderDetail addGjtTextbookOrderDetail(GjtTextbookOrderDetail gjtTextbookOrderDetail) {
		getGjtTextbookOrderDetails().add(gjtTextbookOrderDetail);
		gjtTextbookOrderDetail.setGjtTextbookArrange(this);

		return gjtTextbookOrderDetail;
	}

	public GjtTextbookOrderDetail removeGjtTextbookOrderDetail(GjtTextbookOrderDetail gjtTextbookOrderDetail) {
		getGjtTextbookOrderDetails().remove(gjtTextbookOrderDetail);
		gjtTextbookOrderDetail.setGjtTextbookArrange(null);

		return gjtTextbookOrderDetail;
	}

	public GjtTextbook getGjtTextbook() {
		return this.gjtTextbook;
	}

	public void setGjtTextbook(GjtTextbook gjtTextbook) {
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

	public List<GjtCourse> getGjtCourseList() {
		return gjtCourseList;
	}

	public void setGjtCourseList(List<GjtCourse> gjtCourseList) {
		this.gjtCourseList = gjtCourseList;
	}

	@Override
	public String toString() {
		return "GjtTextbookArrange [arrangeId=" + arrangeId + ", createdBy=" + createdBy + ", createdDt=" + createdDt
				+ ", gjtTextbookPlan=" + gjtTextbookPlan + ", gjtTextbook=" + gjtTextbook + "]";
	}

}