package com.gzedu.xlims.pojo.mobileMessage;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the GJT_MOBLIE_MESAAGE_SEARCH database table.
 * 
 */
@Entity
@Table(name = "GJT_MOBLIE_MESSAGE_SEARCH")
@NamedQuery(name = "GjtMoblieMessageSearch.findAll", query = "SELECT g FROM GjtMoblieMessageSearch g")
public class GjtMoblieMessageSearch implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "CONDITION_CODE")
	private String conditionCode;

	@Column(name = "CONDITION_CONTENT")
	private String conditionContent;

	@Column(name = "CONDITION_NAME")
	private String conditionName;

	@Column(name = "MOBILE_MESSAGE_ID")
	private String mobileMessageId;

	@Column(name = "ORDER_SORT")
	private int orderSort;

	public GjtMoblieMessageSearch() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConditionCode() {
		return this.conditionCode;
	}

	public void setConditionCode(String conditionCode) {
		this.conditionCode = conditionCode;
	}

	public String getConditionContent() {
		return this.conditionContent;
	}

	public void setConditionContent(String conditionContent) {
		this.conditionContent = conditionContent;
	}

	public String getConditionName() {
		return this.conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public String getMobileMessageId() {
		return this.mobileMessageId;
	}

	public void setMobileMessageId(String mobileMessageId) {
		this.mobileMessageId = mobileMessageId;
	}

	public int getOrderSort() {
		return orderSort;
	}

	public void setOrderSort(int orderSort) {
		this.orderSort = orderSort;
	}

}