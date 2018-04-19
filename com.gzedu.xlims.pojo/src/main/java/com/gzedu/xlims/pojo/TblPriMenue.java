package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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

/**
 * The persistent class for the TBL_PRI_MENUE database table. 菜单表
 */
@Entity
@Table(name = "TBL_PRI_MENUE")
@NamedQuery(name = "TblPriMenue.findAll", query = "SELECT t FROM TblPriMenue t")
public class TblPriMenue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MENU_ID")
	private String menuId;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	@NotFound(action = NotFoundAction.IGNORE)
	TblPriMenue parentMenue;// 父菜单

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "parent_id")
	@OrderBy("menuOrd asc")
	@NotFound(action = NotFoundAction.IGNORE)
	List<TblPriMenue> childMenueList;// 子菜单列表

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = true, updatable = false)
	private Date createdDt;

	private String description;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "IS_LEAF")
	private Boolean isLeaf;

	@Column(name = "MENU_CODE")
	private String menuCode;

	@Column(name = "MENU_LEVEL")
	private BigDecimal menuLevel;

	@Column(name = "MENU_LINK")
	private String menuLink;

	@Column(name = "MENU_NAME")
	private String menuName;

	@Column(name = "MENU_ORD")
	private BigDecimal menuOrd;

	private String remark;

	private String status;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	public TblPriMenue() {
	}

	public String getMenuId() {
		return this.menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getMenuCode() {
		return this.menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public BigDecimal getMenuLevel() {
		return this.menuLevel;
	}

	public void setMenuLevel(BigDecimal menuLevel) {
		this.menuLevel = menuLevel;
	}

	public String getMenuLink() {
		return this.menuLink;
	}

	public void setMenuLink(String menuLink) {
		this.menuLink = menuLink;
	}

	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public BigDecimal getMenuOrd() {
		return this.menuOrd;
	}

	public void setMenuOrd(BigDecimal menuOrd) {
		this.menuOrd = menuOrd;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
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

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public TblPriMenue getParentMenue() {
		return parentMenue;
	}

	public void setParentMenue(TblPriMenue parentMenue) {
		this.parentMenue = parentMenue;
	}

	public List<TblPriMenue> getChildMenueList() {
		return childMenueList;
	}

	public void setChildMenueList(List<TblPriMenue> childMenueList) {
		this.childMenueList = childMenueList;
	}

}