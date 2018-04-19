package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.NotBlank;

/**
 * The persistent class for the PRI_MODEL_INFO database table.
 * 
 */
@Entity
@Table(name = "PRI_MODEL_INFO")
@NamedQuery(name = "PriModelInfo.findAll", query = "SELECT p FROM PriModelInfo p")
public class PriModelInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MODEL_ID")
	private String modelId;

	@ManyToMany
	@JoinTable(name = "PRI_MODEL_OPERATE", joinColumns = { @JoinColumn(name = "MODEL_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "OPERATE_ID") })
	List<PriOperateInfo> priOperateInfos;

	@ManyToMany
	@JoinTable(name = "PRI_ROLE_MODEL", joinColumns = { @JoinColumn(name = "MODEL_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "ROLE_ID") })
	List<PriRoleInfo> priRoleInfos;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "PARENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	PriModelInfo parentModel;// 父菜单

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "PARENT_ID")
	@Where(clause = "ISDELETED='N'")
	@OrderBy("orderNo asc")
	@NotFound(action = NotFoundAction.IGNORE)
	List<PriModelInfo> childModelList;// 子菜单列表

	//@Where(clause = "isdeleted='N'")
	private String isdeleted;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "MODEL_ADDRESS")
	private String modelAddress;

	@Column(name = "MODEL_CODE")
	private String modelCode;

	@Column(name = "MODEL_DEC")
	private String modelDec;

	@Column(name = "MODEL_NAME")
	@NotBlank
	private String modelName;

	@Column(name = "MODEL_RANK")
	private Integer modelRank;

	@OrderBy(value = "orderNo asc")
	@Column(name = "ORDER_NO")
	private Integer orderNo;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "is_leaf")
	private boolean isLeaf;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Transient
	private boolean isShow;

	public PriModelInfo() {
	}

	public List<PriOperateInfo> getPriOperateInfos() {
		return priOperateInfos;
	}

	public void setPriOperateInfos(List<PriOperateInfo> priOperateInfos) {
		this.priOperateInfos = priOperateInfos;
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

	public String getIsdeleted() {
		return this.isdeleted;
	}

	public void setIsdeleted(String isdeleted) {
		this.isdeleted = isdeleted;
	}

	public String getModelAddress() {
		if (StringUtils.isBlank(this.modelAddress)) {
			this.modelAddress = "/";
		}
		return this.modelAddress;
	}

	public void setModelAddress(String modelAddress) {
		this.modelAddress = modelAddress;
	}

	public String getModelCode() {
		return this.modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelDec() {
		return this.modelDec;
	}

	public void setModelDec(String modelDec) {
		this.modelDec = modelDec;
	}

	public String getModelId() {
		return this.modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return this.modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Integer getModelRank() {
		return modelRank;
	}

	public void setModelRank(Integer modelRank) {
		this.modelRank = modelRank;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public Integer getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public PriModelInfo getParentModel() {
		return parentModel;
	}

	public void setParentModel(PriModelInfo parentModel) {
		this.parentModel = parentModel;
	}

	public List<PriModelInfo> getChildModelList() {
		return childModelList;
	}

	public void setChildModelList(List<PriModelInfo> childModelList) {
		this.childModelList = childModelList;
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

	// public List<PriModelOperate> getPriModelOperateList() {
	// return priModelOperateList;
	// }
	//
	// public void setPriModelOperateList(List<PriModelOperate>
	// priModelOperateList) {
	// this.priModelOperateList = priModelOperateList;
	// }

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public List<PriRoleInfo> getPriRoleInfos() {
		return priRoleInfos;
	}

	public void setPriRoleInfos(List<PriRoleInfo> priRoleInfos) {
		this.priRoleInfos = priRoleInfos;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(boolean isShow) {
		this.isShow = isShow;
	}

}