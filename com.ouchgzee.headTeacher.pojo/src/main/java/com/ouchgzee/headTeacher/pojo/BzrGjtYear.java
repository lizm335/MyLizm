package com.ouchgzee.headTeacher.pojo;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the GJT_YEAR database table.年级表
 * 
 */
@Entity
@Table(name = "GJT_YEAR")
// @NamedQuery(name = "GjtYear.findAll", query = "SELECT g FROM GjtYear g")
@Deprecated public class BzrGjtYear implements Serializable {
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

	private BigDecimal version;// 版本好

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtOrg gjtOrg; // 所属机构

	@OneToMany(mappedBy = "gjtYear")
	@Where(clause = "IS_DELETED='N'")
	private List<BzrGjtGrade> gjtGrades;

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

	public BzrGjtOrg getGjtOrg() {
		return gjtOrg;
	}

	public void setGjtOrg(BzrGjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}

	public List<BzrGjtGrade> getGjtGrades() {
		return gjtGrades;
	}

	public void setGjtGrades(List<BzrGjtGrade> gjtGrades) {
		this.gjtGrades = gjtGrades;
	}

}