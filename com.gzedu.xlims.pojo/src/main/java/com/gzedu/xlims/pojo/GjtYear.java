package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

/**
 * The persistent class for the GJT_YEAR database table.年级表
 * 
 */
@Entity
@Table(name = "GJT_YEAR")
@NamedQuery(name = "GjtYear.findAll", query = "SELECT g FROM GjtYear g")
public class GjtYear implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String gradeId;// 年级ID

	private String name; // 年級名称

	private String code; // 年级编号

	@Column(name = "CREATED_BY") // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "START_YEAR")
	private Integer startYear; // 入学年份

	@Column(name = "UPDATED_BY") // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;// 版本好

	@Column(name = "ORG_ID")
	private String orgId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtOrg gjtOrg; // 所属机构

	@Column(name = "IS_ENABLED")
	private int isEnabled;// 0未启用，1启用 页面只显示已启用的数据

	@OneToMany(mappedBy = "gjtYear")
	@Where(clause = "IS_DELETED='N'")
	private List<GjtGrade> gjtGrades;

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public Integer getStartYear() {
		return startYear;
	}

	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public BigDecimal getVersion() {
		return version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public GjtOrg getGjtOrg() {
		return gjtOrg;
	}

	public void setGjtOrg(GjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}

	public List<GjtGrade> getGjtGrades() {
		return gjtGrades;
	}

	public void setGjtGrades(List<GjtGrade> gjtGrades) {
		this.gjtGrades = gjtGrades;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public int getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}

}