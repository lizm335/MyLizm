package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * The persistent class for the GJT_ARTICLE database table.
 * 
 */
@Entity
@Table(name = "GJT_ARTICLE")
@NamedQuery(name = "GjtArticle.findAll", query = "SELECT g FROM GjtArticle g")
public class GjtArticle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "ARTICLE_CATEGORY_ID")
	private String articleCategoryId;

	@Lob
	private String content;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_ETIME")
	private Date effectiveEtime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_STIME")
	private Date effectiveStime;

	@Column(name = "GRADE_ID")
	private String gradeId;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "MENU_ID")
	private String menuId;

	@Column(name = "OWNER_TYPE")
	private BigDecimal ownerType;

	private String pycc;

	@Column(name = "SPECIALTY_ID")
	private String specialtyId;

	private String summary;

	private String title;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "XX_ID", updatable = false)
	private String xxId;
	// 上传附件名称
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name="FILE_URL")
	private String fileUrl;
	
	@Transient
	private String specialtyName;
	@Transient
	private String gradeName;
	@Transient
	private String pyccName;

	/**
	 * @return the specialtyName
	 */
	public String getSpecialtyName() {
		return specialtyName;
	}

	/**
	 * @param specialtyName
	 *            the specialtyName to set
	 */
	public void setSpecialtyName(String specialtyName) {
		this.specialtyName = specialtyName;
	}

	/**
	 * @return the gradeName
	 */
	public String getGradeName() {
		return gradeName;
	}

	/**
	 * @param gradeName
	 *            the gradeName to set
	 */
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	/**
	 * @return the pyccName
	 */
	public String getPyccName() {
		return pyccName;
	}

	/**
	 * @param pyccName
	 *            the pyccName to set
	 */
	public void setPyccName(String pyccName) {
		this.pyccName = pyccName;
	}

	public GjtArticle() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getArticleCategoryId() {
		return this.articleCategoryId;
	}

	public void setArticleCategoryId(String articleCategoryId) {
		this.articleCategoryId = articleCategoryId;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Date getEffectiveEtime() {
		return this.effectiveEtime;
	}

	public void setEffectiveEtime(Date effectiveEtime) {
		this.effectiveEtime = effectiveEtime;
	}

	public Date getEffectiveStime() {
		return this.effectiveStime;
	}

	public void setEffectiveStime(Date effectiveStime) {
		this.effectiveStime = effectiveStime;
	}

	public String getGradeId() {
		return this.gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getMenuId() {
		return this.menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public BigDecimal getOwnerType() {
		return this.ownerType;
	}

	public void setOwnerType(BigDecimal ownerType) {
		this.ownerType = ownerType;
	}

	public String getPycc() {
		return this.pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public String getSpecialtyId() {
		return this.specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

}