package com.ouchgzee.headTeacher.pojo.textbook;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ouchgzee.headTeacher.pojo.BzrGjtCourse;

/**
 * The persistent class for the GJT_TEXTBOOK database table.
 * 
 */
@Entity
@Table(name = "GJT_TEXTBOOK")
// @NamedQuery(name = "GjtTextbook.findAll", query = "SELECT g FROM GjtTextbook g")
@Deprecated public class BzrGjtTextbook implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TEXTBOOK_ID")
	private String textbookId;

	private String author;

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

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "ORG_ID")
	private String orgId;

	private Float price;

	@Column(name = "DISCOUNT_PRICE")
	private Float discountPrice;

	private String publishing;

	private String revision;

	@Column(name = "TEXTBOOK_CODE")
	private String textbookCode;

	@Column(name = "TEXTBOOK_NAME")
	private String textbookName;

	@Column(name = "TEXTBOOK_TYPE")
	private int textbookType;

	private int status;

	@Column(name = "REASON_TYPE")
	private Integer reasonType;

	private String reason;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@OneToMany(mappedBy = "gjtTextbook", fetch = FetchType.LAZY)
	private List<BzrGjtTextbookDistributeDetail> gjtTextbookDistributeDetails;

	@OneToMany(mappedBy = "gjtTextbook", fetch = FetchType.LAZY)
	private List<BzrGjtTextbookFeedbackDetail> gjtTextbookFeedbackDetails;

	@OneToOne(mappedBy = "gjtTextbook")
	private BzrGjtTextbookStock gjtTextbookStock;

	@OneToMany(mappedBy = "gjtTextbook", fetch = FetchType.LAZY)
	private List<BzrGjtTextbookStockOpera> gjtTextbookStockOperas;

	@ManyToMany
	@JoinTable(name = "GJT_COURSE_TEXTBOOK", joinColumns = { @JoinColumn(name = "TEXTBOOK_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "COURSE_ID") })
	private List<BzrGjtCourse> gjtCourseList;

	public BzrGjtTextbook() {
	}

	public String getTextbookId() {
		return textbookId;
	}

	public void setTextbookId(String textbookId) {
		this.textbookId = textbookId;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
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

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Float getPrice() {
		return this.price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getPublishing() {
		return this.publishing;
	}

	public void setPublishing(String publishing) {
		this.publishing = publishing;
	}

	public String getRevision() {
		return this.revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getTextbookCode() {
		return this.textbookCode;
	}

	public void setTextbookCode(String textbookCode) {
		this.textbookCode = textbookCode;
	}

	public String getTextbookName() {
		return this.textbookName;
	}

	public void setTextbookName(String textbookName) {
		this.textbookName = textbookName;
	}

	public int getTextbookType() {
		return this.textbookType;
	}

	public void setTextbookType(int textbookType) {
		this.textbookType = textbookType;
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

	public List<BzrGjtTextbookDistributeDetail> getGjtTextbookDistributeDetails() {
		return this.gjtTextbookDistributeDetails;
	}

	public void setGjtTextbookDistributeDetails(List<BzrGjtTextbookDistributeDetail> gjtTextbookDistributeDetails) {
		this.gjtTextbookDistributeDetails = gjtTextbookDistributeDetails;
	}

	public BzrGjtTextbookDistributeDetail addGjtTextbookDistributeDetail(
			BzrGjtTextbookDistributeDetail gjtTextbookDistributeDetail) {
		getGjtTextbookDistributeDetails().add(gjtTextbookDistributeDetail);
		gjtTextbookDistributeDetail.setGjtTextbook(this);

		return gjtTextbookDistributeDetail;
	}

	public BzrGjtTextbookDistributeDetail removeGjtTextbookDistributeDetail(
			BzrGjtTextbookDistributeDetail gjtTextbookDistributeDetail) {
		getGjtTextbookDistributeDetails().remove(gjtTextbookDistributeDetail);
		gjtTextbookDistributeDetail.setGjtTextbook(null);

		return gjtTextbookDistributeDetail;
	}

	public List<BzrGjtTextbookFeedbackDetail> getGjtTextbookFeedbackDetails() {
		return this.gjtTextbookFeedbackDetails;
	}

	public void setGjtTextbookFeedbackDetails(List<BzrGjtTextbookFeedbackDetail> gjtTextbookFeedbackDetails) {
		this.gjtTextbookFeedbackDetails = gjtTextbookFeedbackDetails;
	}

	public BzrGjtTextbookFeedbackDetail addGjtTextbookFeedbackDetail(BzrGjtTextbookFeedbackDetail gjtTextbookFeedbackDetail) {
		getGjtTextbookFeedbackDetails().add(gjtTextbookFeedbackDetail);
		gjtTextbookFeedbackDetail.setGjtTextbook(this);

		return gjtTextbookFeedbackDetail;
	}

	public BzrGjtTextbookFeedbackDetail removeGjtTextbookFeedbackDetail(
			BzrGjtTextbookFeedbackDetail gjtTextbookFeedbackDetail) {
		getGjtTextbookFeedbackDetails().remove(gjtTextbookFeedbackDetail);
		gjtTextbookFeedbackDetail.setGjtTextbook(null);

		return gjtTextbookFeedbackDetail;
	}

	public BzrGjtTextbookStock getGjtTextbookStock() {
		return gjtTextbookStock;
	}

	public void setGjtTextbookStock(BzrGjtTextbookStock gjtTextbookStock) {
		this.gjtTextbookStock = gjtTextbookStock;
	}

	public List<BzrGjtTextbookStockOpera> getGjtTextbookStockOperas() {
		return this.gjtTextbookStockOperas;
	}

	public void setGjtTextbookStockOperas(List<BzrGjtTextbookStockOpera> gjtTextbookStockOperas) {
		this.gjtTextbookStockOperas = gjtTextbookStockOperas;
	}

	public BzrGjtTextbookStockOpera addGjtTextbookStockOpera(BzrGjtTextbookStockOpera gjtTextbookStockOpera) {
		getGjtTextbookStockOperas().add(gjtTextbookStockOpera);
		gjtTextbookStockOpera.setGjtTextbook(this);

		return gjtTextbookStockOpera;
	}

	public BzrGjtTextbookStockOpera removeGjtTextbookStockOpera(BzrGjtTextbookStockOpera gjtTextbookStockOpera) {
		getGjtTextbookStockOperas().remove(gjtTextbookStockOpera);
		gjtTextbookStockOpera.setGjtTextbook(null);

		return gjtTextbookStockOpera;
	}

	public Float getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Float discountPrice) {
		this.discountPrice = discountPrice;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Integer getReasonType() {
		return reasonType;
	}

	public void setReasonType(Integer reasonType) {
		this.reasonType = reasonType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<BzrGjtCourse> getGjtCourseList() {
		return gjtCourseList;
	}

	public void setGjtCourseList(List<BzrGjtCourse> gjtCourseList) {
		this.gjtCourseList = gjtCourseList;
	}

}