package com.gzedu.xlims.pojo.graduation;

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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * 毕业专业答辩安排
 * 
 */
@Entity
@Table(name="GJT_GRADUATION_DEFENCE_PLAN")
@NamedQuery(name="GjtGraduationDefencePlan.findAll", query="SELECT g FROM GjtGraduationDefencePlan g")
public class GjtGraduationDefencePlan implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PLAN_ID")
	private String planId;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	@Column(name="BEGIN_TIME")
	private Date beginTime;

	@Column(name="DEFENCE_NUM")
	private int defenceNum;

	@Column(name="DEFENCE_PLACE")
	private String defencePlace;

	@Column(name="DEFENCE_TYPE")
	private int defenceType;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	@Column(name="END_TIME")
	private Date endTime;

	@Column(name="SETTING_ID")
	private String settingId;

	@ManyToOne
	@JoinColumn(name="SETTING_ID", insertable=false, updatable=false)
	private GjtGraduationSpecialty gjtGraduationSpecialty;

	@Column(name="TRAFFIC_GUIDANCE")
	private String trafficGuidance;

	//bi-directional many-to-one association to GjtGraduationDefenceTeacher
	@OneToMany(cascade={CascadeType.ALL}, mappedBy="gjtGraduationDefencePlan")
	@NotFound(action=NotFoundAction.IGNORE)
	private List<GjtGraduationDefenceTeacher> gjtGraduationDefenceTeachers;

	//bi-directional many-to-one association to GjtGraduationDefenceTeacher
	@OneToMany(cascade={CascadeType.ALL}, mappedBy="gjtGraduationDefencePlan")
	@Where(clause="\"TYPE\" = 1")
	@NotFound(action=NotFoundAction.IGNORE)
	private List<GjtGraduationDefenceTeacher> gjtGraduationDefenceTeachers1;

	//bi-directional many-to-one association to GjtGraduationDefenceTeacher
	@OneToMany(cascade={CascadeType.ALL}, mappedBy="gjtGraduationDefencePlan")
	@Where(clause="\"TYPE\" = 2")
	@NotFound(action=NotFoundAction.IGNORE)
	private List<GjtGraduationDefenceTeacher> gjtGraduationDefenceTeachers2;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name="DELETED_BY")
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED_DT")
	private Date deletedDt;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Transient
	private String[] defenceTeachers1;

	@Transient
	private String[] defenceTeachers2;

	public GjtGraduationDefencePlan() {
	}

	public String getPlanId() {
		return this.planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public int getDefenceNum() {
		return this.defenceNum;
	}

	public void setDefenceNum(int defenceNum) {
		this.defenceNum = defenceNum;
	}

	public String getDefencePlace() {
		return this.defencePlace;
	}

	public void setDefencePlace(String defencePlace) {
		this.defencePlace = defencePlace;
	}

	public int getDefenceType() {
		return this.defenceType;
	}

	public void setDefenceType(int defenceType) {
		this.defenceType = defenceType;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getSettingId() {
		return this.settingId;
	}

	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}

	public GjtGraduationSpecialty getGjtGraduationSpecialty() {
		return gjtGraduationSpecialty;
	}

	public void setGjtGraduationSpecialty(GjtGraduationSpecialty gjtGraduationSpecialty) {
		this.gjtGraduationSpecialty = gjtGraduationSpecialty;
	}

	public String getTrafficGuidance() {
		return this.trafficGuidance;
	}

	public void setTrafficGuidance(String trafficGuidance) {
		this.trafficGuidance = trafficGuidance;
	}

	public List<GjtGraduationDefenceTeacher> getGjtGraduationDefenceTeachers() {
		return this.gjtGraduationDefenceTeachers;
	}

	public void setGjtGraduationDefenceTeachers(List<GjtGraduationDefenceTeacher> gjtGraduationDefenceTeachers) {
		this.gjtGraduationDefenceTeachers = gjtGraduationDefenceTeachers;
	}

	public List<GjtGraduationDefenceTeacher> getGjtGraduationDefenceTeachers1() {
		return gjtGraduationDefenceTeachers1;
	}

	public void setGjtGraduationDefenceTeachers1(List<GjtGraduationDefenceTeacher> gjtGraduationDefenceTeachers1) {
		this.gjtGraduationDefenceTeachers1 = gjtGraduationDefenceTeachers1;
	}

	public List<GjtGraduationDefenceTeacher> getGjtGraduationDefenceTeachers2() {
		return gjtGraduationDefenceTeachers2;
	}

	public void setGjtGraduationDefenceTeachers2(List<GjtGraduationDefenceTeacher> gjtGraduationDefenceTeachers2) {
		this.gjtGraduationDefenceTeachers2 = gjtGraduationDefenceTeachers2;
	}

	public String[] getDefenceTeachers1() {
		return defenceTeachers1;
	}

	public void setDefenceTeachers1(String[] defenceTeachers1) {
		this.defenceTeachers1 = defenceTeachers1;
	}

	public String[] getDefenceTeachers2() {
		return defenceTeachers2;
	}

	public void setDefenceTeachers2(String[] defenceTeachers2) {
		this.defenceTeachers2 = defenceTeachers2;
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

	public String getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedDt() {
		return deletedDt;
	}

	public void setDeletedDt(Date deletedDt) {
		this.deletedDt = deletedDt;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "GjtGraduationDefencePlan [planId=" + planId + ", beginTime=" + beginTime + ", defenceNum=" + defenceNum
				+ ", defencePlace=" + defencePlace + ", defenceType=" + defenceType + ", endTime=" + endTime
				+ ", settingId=" + settingId + ", gjtGraduationSpecialty=" + gjtGraduationSpecialty
				+ ", trafficGuidance=" + trafficGuidance + ", gjtGraduationDefenceTeachers="
				+ gjtGraduationDefenceTeachers + ", gjtGraduationDefenceTeachers1=" + gjtGraduationDefenceTeachers1
				+ ", gjtGraduationDefenceTeachers2=" + gjtGraduationDefenceTeachers2 + "]";
	}

}