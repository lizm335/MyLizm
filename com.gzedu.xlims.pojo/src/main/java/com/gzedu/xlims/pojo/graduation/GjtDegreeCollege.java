package com.gzedu.xlims.pojo.graduation;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 学位院校表
 * 
 */
@Entity
@Table(name = "GJT_DEGREE_COLLEGE")
@NamedQuery(name = "GjtDegreeCollege.findAll", query = "SELECT g FROM GjtDegreeCollege g")
public class GjtDegreeCollege implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COLLEGE_ID")
	private String collegeId;

	@Column(name = "COLLEGE_NAME")
	private String collegeName;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", updatable = false)
	private Date createdDt;

	@Column(name = "DELETED_BY", insertable = false)
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED_DT", insertable = false)
	private Date deletedDt;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "ORG_ID")
	private String orgId;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	private int status;

	private String cover;

	// bi-directional many-to-one association to GjtSpecialtyDegreeCollege
	@OneToMany(mappedBy = "gjtDegreeCollege")
	private List<GjtSpecialtyDegreeCollege> gjtSpecialtyDegreeColleges;

	public GjtDegreeCollege() {
	}

	public GjtDegreeCollege(String collegeId) {
		this.collegeId = collegeId;
	}

	public String getCollegeId() {
		return this.collegeId;
	}

	public void setCollegeId(String collegeId) {
		this.collegeId = collegeId;
	}

	public String getCollegeName() {
		return this.collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getDeletedBy() {
		return this.deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedDt() {
		return this.deletedDt;
	}

	public void setDeletedDt(Date deletedDt) {
		this.deletedDt = deletedDt;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public List<GjtSpecialtyDegreeCollege> getGjtSpecialtyDegreeColleges() {
		return this.gjtSpecialtyDegreeColleges;
	}

	public void setGjtSpecialtyDegreeColleges(List<GjtSpecialtyDegreeCollege> gjtSpecialtyDegreeColleges) {
		this.gjtSpecialtyDegreeColleges = gjtSpecialtyDegreeColleges;
	}

	public GjtSpecialtyDegreeCollege addGjtSpecialtyDegreeCollege(GjtSpecialtyDegreeCollege gjtSpecialtyDegreeCollege) {
		getGjtSpecialtyDegreeColleges().add(gjtSpecialtyDegreeCollege);
		gjtSpecialtyDegreeCollege.setGjtDegreeCollege(this);

		return gjtSpecialtyDegreeCollege;
	}

	public GjtSpecialtyDegreeCollege removeGjtSpecialtyDegreeCollege(GjtSpecialtyDegreeCollege gjtSpecialtyDegreeCollege) {
		getGjtSpecialtyDegreeColleges().remove(gjtSpecialtyDegreeCollege);
		gjtSpecialtyDegreeCollege.setGjtDegreeCollege(null);

		return gjtSpecialtyDegreeCollege;
	}

	@Override
	public String toString() {
		return "GjtDegreeCollege [collegeId=" + collegeId + ", collegeName=" + collegeName + ", createdBy=" + createdBy + ", createdDt=" + createdDt
				+ ", deletedBy=" + deletedBy + ", deletedDt=" + deletedDt + ", isDeleted=" + isDeleted + ", orgId=" + orgId + ", updatedBy="
				+ updatedBy + ", updatedDt=" + updatedDt + "]";
	}

}