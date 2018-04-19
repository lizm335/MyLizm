package com.gzedu.xlims.pojo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the GJT_AREA database table.
 * 
 */
@Entity
@Table(name = "GJT_AREA")
@NamedQuery(name = "GjtArea.findAll", query = "SELECT g FROM GjtArea g")
public class GjtArea implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String cityname;

	private String district;

	public GjtArea() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

}