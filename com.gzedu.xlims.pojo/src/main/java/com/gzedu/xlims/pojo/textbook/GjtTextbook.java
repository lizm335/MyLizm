package com.gzedu.xlims.pojo.textbook;

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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gzedu.xlims.pojo.GjtCourse;

/**
 * The persistent class for the GJT_TEXTBOOK database table.
 * 
 */
@Entity
@Table(name = "GJT_TEXTBOOK")
@NamedQuery(name = "GjtTextbook.findAll", query = "SELECT g FROM GjtTextbook g")
public class GjtTextbook implements Serializable {
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

	private String cover;

	private String isbn; // isbn码

	@Column(name = "POSITION_NO")
	private String positionNo; // 货位号

	@OneToMany(mappedBy = "gjtTextbook", fetch = FetchType.LAZY)
	private List<GjtTextbookDistributeDetail> gjtTextbookDistributeDetails;

	@OneToMany(mappedBy = "gjtTextbook", fetch = FetchType.LAZY)
	private List<GjtTextbookFeedbackDetail> gjtTextbookFeedbackDetails;

	@OneToOne(mappedBy = "gjtTextbook")
	private GjtTextbookStock gjtTextbookStock;

	@OneToMany(mappedBy = "gjtTextbook", fetch = FetchType.LAZY)
	private List<GjtTextbookStockOpera> gjtTextbookStockOperas;

	@ManyToMany
	@JoinTable(name = "GJT_COURSE_TEXTBOOK", joinColumns = { @JoinColumn(name = "TEXTBOOK_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "COURSE_ID") })
	private List<GjtCourse> gjtCourseList;

	public GjtTextbook() {
	}

	public GjtTextbook(String textbookId) {
		this.textbookId = textbookId;
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

	public List<GjtTextbookDistributeDetail> getGjtTextbookDistributeDetails() {
		return this.gjtTextbookDistributeDetails;
	}

	public void setGjtTextbookDistributeDetails(List<GjtTextbookDistributeDetail> gjtTextbookDistributeDetails) {
		this.gjtTextbookDistributeDetails = gjtTextbookDistributeDetails;
	}

	public GjtTextbookDistributeDetail addGjtTextbookDistributeDetail(GjtTextbookDistributeDetail gjtTextbookDistributeDetail) {
		getGjtTextbookDistributeDetails().add(gjtTextbookDistributeDetail);
		gjtTextbookDistributeDetail.setGjtTextbook(this);

		return gjtTextbookDistributeDetail;
	}

	public GjtTextbookDistributeDetail removeGjtTextbookDistributeDetail(GjtTextbookDistributeDetail gjtTextbookDistributeDetail) {
		getGjtTextbookDistributeDetails().remove(gjtTextbookDistributeDetail);
		gjtTextbookDistributeDetail.setGjtTextbook(null);

		return gjtTextbookDistributeDetail;
	}

	public List<GjtTextbookFeedbackDetail> getGjtTextbookFeedbackDetails() {
		return this.gjtTextbookFeedbackDetails;
	}

	public void setGjtTextbookFeedbackDetails(List<GjtTextbookFeedbackDetail> gjtTextbookFeedbackDetails) {
		this.gjtTextbookFeedbackDetails = gjtTextbookFeedbackDetails;
	}

	public GjtTextbookFeedbackDetail addGjtTextbookFeedbackDetail(GjtTextbookFeedbackDetail gjtTextbookFeedbackDetail) {
		getGjtTextbookFeedbackDetails().add(gjtTextbookFeedbackDetail);
		gjtTextbookFeedbackDetail.setGjtTextbook(this);

		return gjtTextbookFeedbackDetail;
	}

	public GjtTextbookFeedbackDetail removeGjtTextbookFeedbackDetail(GjtTextbookFeedbackDetail gjtTextbookFeedbackDetail) {
		getGjtTextbookFeedbackDetails().remove(gjtTextbookFeedbackDetail);
		gjtTextbookFeedbackDetail.setGjtTextbook(null);

		return gjtTextbookFeedbackDetail;
	}

	public GjtTextbookStock getGjtTextbookStock() {
		return gjtTextbookStock;
	}

	public void setGjtTextbookStock(GjtTextbookStock gjtTextbookStock) {
		this.gjtTextbookStock = gjtTextbookStock;
	}

	public List<GjtTextbookStockOpera> getGjtTextbookStockOperas() {
		return this.gjtTextbookStockOperas;
	}

	public void setGjtTextbookStockOperas(List<GjtTextbookStockOpera> gjtTextbookStockOperas) {
		this.gjtTextbookStockOperas = gjtTextbookStockOperas;
	}

	public GjtTextbookStockOpera addGjtTextbookStockOpera(GjtTextbookStockOpera gjtTextbookStockOpera) {
		getGjtTextbookStockOperas().add(gjtTextbookStockOpera);
		gjtTextbookStockOpera.setGjtTextbook(this);

		return gjtTextbookStockOpera;
	}

	public GjtTextbookStockOpera removeGjtTextbookStockOpera(GjtTextbookStockOpera gjtTextbookStockOpera) {
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

	public List<GjtCourse> getGjtCourseList() {
		return gjtCourseList;
	}

	public void setGjtCourseList(List<GjtCourse> gjtCourseList) {
		this.gjtCourseList = gjtCourseList;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPositionNo() {
		return positionNo;
	}

	public void setPositionNo(String positionNo) {
		this.positionNo = positionNo;
	}
}