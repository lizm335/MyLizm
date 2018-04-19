package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the TBL_PRI_MENU_OPT database table.菜单操作
 * 
 */
@Entity
@Table(name = "TBL_PRI_MENU_OPT")
@NamedQuery(name = "TblPriMenuOpt.findAll", query = "SELECT t FROM TblPriMenuOpt t")
public class TblPriMenuOpt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MENU_OPT_ID")
	private String menuOptId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "FUNC_OPT_CODE")
	private String funcOptCode;

	@Column(name = "FUNC_OPT_NAME")
	private String funcOptName;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "IS_VIEW")
	private String isView;

	@Column(name = "MENU_ID")
	private String menuId;

	private String remark;

	private String status;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "URL_PATTERN")
	private String urlPattern;

	@Column(name = "VERSION")
	private BigDecimal version;

	public TblPriMenuOpt() {
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

	public String getFuncOptCode() {
		return this.funcOptCode;
	}

	public void setFuncOptCode(String funcOptCode) {
		this.funcOptCode = funcOptCode;
	}

	public String getFuncOptName() {
		return this.funcOptName;
	}

	public void setFuncOptName(String funcOptName) {
		this.funcOptName = funcOptName;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsView() {
		return this.isView;
	}

	public void setIsView(String isView) {
		this.isView = isView;
	}

	public String getMenuId() {
		return this.menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuOptId() {
		return this.menuOptId;
	}

	public void setMenuOptId(String menuOptId) {
		this.menuOptId = menuOptId;
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

	public String getUrlPattern() {
		return this.urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

}