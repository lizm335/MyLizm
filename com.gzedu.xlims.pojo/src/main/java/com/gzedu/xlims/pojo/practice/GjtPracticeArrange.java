package com.gzedu.xlims.pojo.practice;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.gzedu.xlims.pojo.GjtSpecialtyBase;


/**
 * The persistent class for the GJT_PRACTICE_ARRANGE database table.
 * 
 */
@Entity
@Table(name="GJT_PRACTICE_ARRANGE")
@NamedQuery(name="GjtPracticeArrange.findAll", query="SELECT g FROM GjtPracticeArrange g")
public class GjtPracticeArrange implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ARRANGE_ID")
	private String arrangeId;

	@Column(name="CAN_APPLY_NUM")
	private int canApplyNum;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="DELETED_BY")
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED_DT")
	private Date deletedDt;

	@Column(name="SPECIALTY_BASE_ID")
	private String specialtyBaseId;

	@ManyToOne
	@JoinColumn(name="SPECIALTY_BASE_ID", insertable=false, updatable=false)
	private GjtSpecialtyBase gjtSpecialtyBase;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@OneToMany(cascade={CascadeType.ALL}, mappedBy="gjtPracticeArrange")
	private List<GjtPracticeAdviser> gjtPracticeAdvisers;

	@Column(name="PRACTICE_PLAN_ID")
	private String practicePlanId;

	@ManyToOne
	@JoinColumn(name="PRACTICE_PLAN_ID", insertable=false, updatable=false)
	private GjtPracticePlan gjtPracticePlan;

	@Transient
	private int status;

	@Transient
	private int applyNum;

	@Transient
	private int passNum;

	@Transient
	private boolean canUpdate;

	public GjtPracticeArrange() {
	}

	public String getArrangeId() {
		return this.arrangeId;
	}

	public void setArrangeId(String arrangeId) {
		this.arrangeId = arrangeId;
	}

	public int getCanApplyNum() {
		return this.canApplyNum;
	}

	public void setCanApplyNum(int canApplyNum) {
		this.canApplyNum = canApplyNum;
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

	public String getSpecialtyBaseId() {
		return specialtyBaseId;
	}

	public void setSpecialtyBaseId(String specialtyBaseId) {
		this.specialtyBaseId = specialtyBaseId;
	}

	public GjtSpecialtyBase getGjtSpecialtyBase() {
		return gjtSpecialtyBase;
	}

	public void setGjtSpecialtyBase(GjtSpecialtyBase gjtSpecialtyBase) {
		this.gjtSpecialtyBase = gjtSpecialtyBase;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
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

	public List<GjtPracticeAdviser> getGjtPracticeAdvisers() {
		return this.gjtPracticeAdvisers;
	}

	public void setGjtPracticeAdvisers(List<GjtPracticeAdviser> gjtPracticeAdvisers) {
		this.gjtPracticeAdvisers = gjtPracticeAdvisers;
	}

	public GjtPracticeAdviser addGjtPracticeAdviser(GjtPracticeAdviser gjtPracticeAdviser) {
		getGjtPracticeAdvisers().add(gjtPracticeAdviser);
		gjtPracticeAdviser.setGjtPracticeArrange(this);

		return gjtPracticeAdviser;
	}

	public GjtPracticeAdviser removeGjtPracticeAdviser(GjtPracticeAdviser gjtPracticeAdviser) {
		getGjtPracticeAdvisers().remove(gjtPracticeAdviser);
		gjtPracticeAdviser.setGjtPracticeArrange(null);

		return gjtPracticeAdviser;
	}

	public GjtPracticePlan getGjtPracticePlan() {
		return this.gjtPracticePlan;
	}

	public void setGjtPracticePlan(GjtPracticePlan gjtPracticePlan) {
		this.gjtPracticePlan = gjtPracticePlan;
	}

	public String getPracticePlanId() {
		return practicePlanId;
	}

	public void setPracticePlanId(String practicePlanId) {
		this.practicePlanId = practicePlanId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(int applyNum) {
		this.applyNum = applyNum;
	}

	public int getPassNum() {
		return passNum;
	}

	public void setPassNum(int passNum) {
		this.passNum = passNum;
	}

	public boolean isCanUpdate() {
		return canUpdate;
	}

	public void setCanUpdate(boolean canUpdate) {
		this.canUpdate = canUpdate;
	}

	@Override
	public String toString() {
		return "GjtPracticeArrange [arrangeId=" + arrangeId + ", canApplyNum=" + canApplyNum + ", createdBy="
				+ createdBy + ", createdDt=" + createdDt + ", deletedBy=" + deletedBy + ", deletedDt=" + deletedDt
				+ ", specialtyBaseId=" + specialtyBaseId + ", isDeleted=" + isDeleted + ", updatedBy=" + updatedBy
				+ ", updatedDt=" + updatedDt + ", practicePlanId=" + practicePlanId + "]";
	}

}