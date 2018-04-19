/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.college;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 功能说明：院校模式专业表
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年5月20日
 * @version 3.0
 *
 */
@Entity
@Table(name = "GJT_SPECIALTY_COLLEGE")
public class GjtSpecialtyCollege implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String specialtyId;// 专业Id
	private String name;// 专业名称
	private String orgId;// 所属机构
	private String ruleCode;// 专业规则号
	private String specialtyLevel;// 专业层次
	private Date createdDt;// 创建时间
	private String createdBy;// 创建人

	public String getSpecialtyId() {
		return specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	public String getSpecialtyLevel() {
		return specialtyLevel;
	}

	public void setSpecialtyLevel(String specialtyLevel) {
		this.specialtyLevel = specialtyLevel;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


}
