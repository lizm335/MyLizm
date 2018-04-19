package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the GJT_WORK_ORDER_USER database table. 工单任务指定人员，一对多
 */
@Entity
@Table(name = "GJT_WORK_ORDER_ASSIGN_PERSON")
@NamedQuery(name = "GjtWorkOrderAssignPerson.findAll", query = "SELECT g FROM GjtWorkOrderAssignPerson g")
public class GjtWorkOrderAssignPerson implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private String id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtUserAccount assignPerson;

	@Column(name = "USER_ID", insertable = false, updatable = false)
	private String userId;

	@ManyToOne
	@JoinColumn(name = "WORK_ORDER_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtWorkOrder gjtWorkOrder;

	@Column(name = "WORK_ORDER_ID", insertable = false, updatable = false)
	private String workOrderId;

	@Column(name = "CREATED_BY") // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "IS_DELETED") // 是否删除
	private String isDeleted;

	@Column(name = "UPDATED_BY") // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	private BigDecimal version;// 版本号，乐观锁

	public GjtWorkOrderAssignPerson() {

	}

	public GjtUserAccount getAssignPerson() {
		return assignPerson;
	}

	public void setAssignPerson(GjtUserAccount assignPerson) {
		this.assignPerson = assignPerson;
	}

	public GjtWorkOrder getGjtWorkOrder() {
		return gjtWorkOrder;
	}

	public void setGjtWorkOrder(GjtWorkOrder gjtWorkOrder) {
		this.gjtWorkOrder = gjtWorkOrder;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
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

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public String getWorkOrderId() {
		return this.workOrderId;
	}

	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}

}