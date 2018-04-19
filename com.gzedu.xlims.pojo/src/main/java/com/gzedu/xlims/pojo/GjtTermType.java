package com.gzedu.xlims.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the GJT_TERM_TYPE database table. 学校学期类别
 */
@Entity
@Table(name = "GJT_TERM_TYPE")
@NamedQuery(name = "GjtTermType.findAll", query = "SELECT g FROM GjtTermType g")
public class GjtTermType implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;

	private String name;

	@Column(name = "ORD_NO")
	private String ordNo;// 序号

	@Column(name = "XX_ID")
	private String xxId;

	public GjtTermType() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getOrdNo() {
		return ordNo;
	}

	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}

}