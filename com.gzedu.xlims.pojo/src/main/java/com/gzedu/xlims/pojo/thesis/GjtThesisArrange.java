package com.gzedu.xlims.pojo.thesis;

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

import org.hibernate.annotations.Where;

import com.gzedu.xlims.pojo.GjtSpecialtyBase;


/**
 * The persistent class for the GJT_THESIS_ARRANGE database table.
 * 
 */
@Entity
@Table(name="GJT_THESIS_ARRANGE")
@NamedQuery(name="GjtThesisArrange.findAll", query="SELECT g FROM GjtThesisArrange g")
public class GjtThesisArrange implements Serializable {
	
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

	@Column(name="EXAMPLE_NAME")
	private String exampleName;

	@Column(name="EXAMPLE_URL")
	private String exampleUrl;

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

	@OneToMany(cascade={CascadeType.ALL}, mappedBy="gjtThesisArrange")
	private List<GjtThesisAdviser> gjtThesisAdvisers;

	@OneToMany(cascade={CascadeType.ALL}, mappedBy="gjtThesisArrange")
	@Where(clause="ADVISER_TYPE = 1")
	private List<GjtThesisAdviser> gjtThesisAdvisers1;

	@OneToMany(cascade={CascadeType.ALL}, mappedBy="gjtThesisArrange")
	@Where(clause="ADVISER_TYPE = 2")
	private List<GjtThesisAdviser> gjtThesisAdvisers2;

	@OneToMany(cascade={CascadeType.ALL}, mappedBy="gjtThesisArrange")
	@Where(clause="ADVISER_TYPE = 3")
	private List<GjtThesisAdviser> gjtThesisAdvisers3;

	@Column(name="THESIS_PLAN_ID")
	private String thesisPlanId;

	//bi-directional many-to-one association to GjtThesisPlan
	@ManyToOne
	@JoinColumn(name="THESIS_PLAN_ID", insertable=false, updatable=false)
	private GjtThesisPlan gjtThesisPlan;

	@Transient
	private int status;

	@Transient
	private int applyNum;

	@Transient
	private int defenceNum;

	@Transient
	private int passNum;

	@Transient
	private boolean canUpdate;

	public GjtThesisArrange() {
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

	public String getExampleName() {
		return this.exampleName;
	}

	public void setExampleName(String exampleName) {
		this.exampleName = exampleName;
	}

	public String getExampleUrl() {
		return this.exampleUrl;
	}

	public void setExampleUrl(String exampleUrl) {
		this.exampleUrl = exampleUrl;
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

	public List<GjtThesisAdviser> getGjtThesisAdvisers() {
		return gjtThesisAdvisers;
	}

	public void setGjtThesisAdvisers(List<GjtThesisAdviser> gjtThesisAdvisers) {
		this.gjtThesisAdvisers = gjtThesisAdvisers;
	}

	public List<GjtThesisAdviser> getGjtThesisAdvisers1() {
		return gjtThesisAdvisers1;
	}

	public void setGjtThesisAdvisers1(List<GjtThesisAdviser> gjtThesisAdvisers1) {
		this.gjtThesisAdvisers1 = gjtThesisAdvisers1;
	}

	public List<GjtThesisAdviser> getGjtThesisAdvisers2() {
		return gjtThesisAdvisers2;
	}

	public void setGjtThesisAdvisers2(List<GjtThesisAdviser> gjtThesisAdvisers2) {
		this.gjtThesisAdvisers2 = gjtThesisAdvisers2;
	}

	public List<GjtThesisAdviser> getGjtThesisAdvisers3() {
		return gjtThesisAdvisers3;
	}

	public void setGjtThesisAdvisers3(List<GjtThesisAdviser> gjtThesisAdvisers3) {
		this.gjtThesisAdvisers3 = gjtThesisAdvisers3;
	}

	public String getThesisPlanId() {
		return thesisPlanId;
	}

	public void setThesisPlanId(String thesisPlanId) {
		this.thesisPlanId = thesisPlanId;
	}

	public GjtThesisPlan getGjtThesisPlan() {
		return this.gjtThesisPlan;
	}

	public void setGjtThesisPlan(GjtThesisPlan gjtThesisPlan) {
		this.gjtThesisPlan = gjtThesisPlan;
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

	public int getDefenceNum() {
		return defenceNum;
	}

	public void setDefenceNum(int defenceNum) {
		this.defenceNum = defenceNum;
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
		return "GjtThesisArrange [arrangeId=" + arrangeId + ", canApplyNum=" + canApplyNum + ", createdBy=" + createdBy
				+ ", createdDt=" + createdDt + ", deletedBy=" + deletedBy + ", deletedDt=" + deletedDt
				+ ", exampleName=" + exampleName + ", exampleUrl=" + exampleUrl + ", specialtyBaseId="
				+ specialtyBaseId + ", isDeleted=" + isDeleted + ", updatedBy=" + updatedBy + ", updatedDt="
				+ updatedDt + ", thesisPlanId=" + thesisPlanId + "]";
	}

}