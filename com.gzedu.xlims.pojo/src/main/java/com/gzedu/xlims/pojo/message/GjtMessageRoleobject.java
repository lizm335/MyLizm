package com.gzedu.xlims.pojo.message;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the GJT_MESSAGE_ROLEOBJECT database table.
 * 
 */
@Entity
@Table(name = "GJT_MESSAGE_ROLEOBJECT")
@NamedQuery(name = "GjtMessageRoleobject.findAll", query = "SELECT g FROM GjtMessageRoleobject g")
public class GjtMessageRoleobject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "MESSAGE_ROLE_ID")
	private String messageRoleId;

	@Column(name = "ORDER_SORT")
	private BigDecimal orderSort;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "USER_NAME")
	private String userName;

	public GjtMessageRoleobject() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessageRoleId() {
		return this.messageRoleId;
	}

	public void setMessageRoleId(String messageRoleId) {
		this.messageRoleId = messageRoleId;
	}

	public BigDecimal getOrderSort() {
		return this.orderSort;
	}

	public void setOrderSort(BigDecimal orderSort) {
		this.orderSort = orderSort;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}