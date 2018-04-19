package com.gzedu.xlims.pojo.graduation;

import com.gzedu.xlims.pojo.GjtSpecialty;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 示例文档实体类<br>
 * The persistent class for the GJT_SAMPLE_DOCUMENT database table.
 * 
 */
@Entity
@Table(name = "GJT_SAMPLE_DOCUMENT")
@NamedQuery(name = "GjtSampleDocument.findAll", query = "SELECT g FROM GjtSampleDocument g")
public class GjtSampleDocument implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DOCUMENT_ID")
	private String documentId;

	@ManyToOne
	@JoinColumn(name = "SPECIALTY_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSpecialty gjtSpecialty;

	@Column(name = "DOCUMENT_NAME")
	private String documentName;

	@Column(name = "DOCUMENT_URL")
	private String documentUrl;

	@Column(name = "DOCUMENT_TYPE")
	private Double documentType;

	@Column(name = "DOWNLOAD_NUM", insertable = false)
	private Double downloadNum;

	@Column(name = "ORG_ID")
	private String orgId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", updatable = false)
	private Date createdDt;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED_DT", insertable = false)
	private Date deletedDt;

	@Column(name = "DELETED_BY", insertable = false)
	private String deletedBy;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public Double getDocumentType() {
		return documentType;
	}

	public void setDocumentType(Double documentType) {
		this.documentType = documentType;
	}

	public Double getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(Double downloadNum) {
		this.downloadNum = downloadNum;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getDeletedDt() {
		return deletedDt;
	}

	public void setDeletedDt(Date deletedDt) {
		this.deletedDt = deletedDt;
	}

	public String getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public GjtSpecialty getGjtSpecialty() {
		return gjtSpecialty;
	}

	public void setGjtSpecialty(GjtSpecialty gjtSpecialty) {
		this.gjtSpecialty = gjtSpecialty;
	}
}