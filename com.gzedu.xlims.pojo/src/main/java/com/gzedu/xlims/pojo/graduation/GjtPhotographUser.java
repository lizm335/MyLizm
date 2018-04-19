package com.gzedu.xlims.pojo.graduation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.gzedu.xlims.pojo.GjtUserAccount;

/**
 * The persistent class for the GJT_PHOTOGRAPH_USER database table.
 * 
 */
@Entity
@Table(name = "GJT_PHOTOGRAPH_USER")
@NamedQuery(name = "GjtPhotographUser.findAll", query = "SELECT g FROM GjtPhotographUser g")
public class GjtPhotographUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "PHOTOGRAPH_ID")
	private String photographId;

	@OneToOne
	@JoinColumn(name = "PHOTOGRAPH_ID", insertable = false, updatable = false)
	private GjtPhotographAddress gjtPhotographAddress;

	@Column(name = "USER_ID")
	private String userId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", insertable = false, updatable = false)
	private GjtUserAccount gjtUserAccount;

	public GjtPhotographUser() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhotographId() {
		return this.photographId;
	}

	public void setPhotographId(String photographId) {
		this.photographId = photographId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public GjtPhotographAddress getGjtPhotographAddress() {
		return gjtPhotographAddress;
	}

	public void setGjtPhotographAddress(GjtPhotographAddress gjtPhotographAddress) {
		this.gjtPhotographAddress = gjtPhotographAddress;
	}

	public GjtUserAccount getGjtUserAccount() {
		return gjtUserAccount;
	}

	public void setGjtUserAccount(GjtUserAccount gjtUserAccount) {
		this.gjtUserAccount = gjtUserAccount;
	}

}