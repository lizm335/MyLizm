package com.gzedu.xlims.pojo.message;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * The persistent class for the GJT_MESSAGE_PUT_OBJECT database table.
 * 
 */
@Entity
@Table(name = "GJT_MESSAGE_PUT_OBJECT")
@NamedQuery(name = "GjtMessagePutObject.findAll", query = "SELECT g FROM GjtMessagePutObject g")
public class GjtMessagePutObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "PUT_OBJECT_ID")
	@OrderBy("orderSort asc")
	private List<GjtMessageSearch> gjtMessageSearchs;

	private String code;

	@Column(name = "MESSAGE_ID")
	private String messageId;

	private String name;

	@Column(name = "ORDER_SORT")
	private int orderSort;

	public GjtMessagePutObject() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessageId() {
		return this.messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrderSort() {
		return orderSort;
	}

	public void setOrderSort(int orderSort) {
		this.orderSort = orderSort;
	}

	public List<GjtMessageSearch> getGjtMessageSearchs() {
		return gjtMessageSearchs;
	}

	public void setGjtMessageSearchs(List<GjtMessageSearch> gjtMessageSearchs) {
		this.gjtMessageSearchs = gjtMessageSearchs;
	}

}