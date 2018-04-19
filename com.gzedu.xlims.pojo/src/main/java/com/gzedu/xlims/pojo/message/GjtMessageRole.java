package com.gzedu.xlims.pojo.message;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the GJT_MESSAGE_ROLE database table.
 * 
 */
@Entity
@Table(name = "GJT_MESSAGE_ROLE")
@NamedQuery(name = "GjtMessageRole.findAll", query = "SELECT g FROM GjtMessageRole g")
public class GjtMessageRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "IS_COPY")
	private String isCopy;

	@Column(name = "ORDER_SORT")
	private BigDecimal orderSort;

	@Column(name = "PUT_OBJECT_ID")
	private String putObjectId;

	@Column(name = "ROLE_ID")
	private String roleId;

	@Column(name = "ROLE_NAME")
	private String roleName;

	public GjtMessageRole() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsCopy() {
		return this.isCopy;
	}

	public void setIsCopy(String isCopy) {
		this.isCopy = isCopy;
	}

	public BigDecimal getOrderSort() {
		return this.orderSort;
	}

	public void setOrderSort(BigDecimal orderSort) {
		this.orderSort = orderSort;
	}

	public String getPutObjectId() {
		return this.putObjectId;
	}

	public void setPutObjectId(String putObjectId) {
		this.putObjectId = putObjectId;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}