package com.gzedu.xlims.pojo.graduation;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the GJT_GRADUATION_SPECIALTY database table.
 * 毕业专业设置表
 */
@Entity
@Table(name="GJT_GRADUATION_SPECIALTY")
@NamedQuery(name="GjtGraduationSpecialty.findAll", query="SELECT g FROM GjtGraduationSpecialty g")
public class GjtGraduationSpecialty implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SETTING_ID")
	private String settingId;

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

	@Column(name="EXAMPLE")
	private String example;

	@Column(name="EXAMPLE_NAME")
	private String exampleName;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="SPECIALTY_ID")
	private String specialtyId;

	@Column(name="TOPIC")
	private String topic;

	@Column(name="TRAINING_LEVEL")
	private String trainingLevel;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	//bi-directional many-to-one association to GjtGraduationAdviser
	@OneToMany(cascade={CascadeType.ALL}, mappedBy="gjtGraduationSpecialty")
	@NotFound(action=NotFoundAction.IGNORE)
	private List<GjtGraduationAdviser> gjtGraduationAdvisers;

	//bi-directional many-to-one association to GjtGraduationAdviser
	@OneToMany(cascade={CascadeType.ALL}, mappedBy="gjtGraduationSpecialty")
	@Where(clause="ADVISER_TYPE = 1")
	@NotFound(action=NotFoundAction.IGNORE)
	private List<GjtGraduationAdviser> gjtGraduationAdvisers1;

	//bi-directional many-to-one association to GjtGraduationAdviser
	@OneToMany(cascade={CascadeType.ALL}, mappedBy="gjtGraduationSpecialty")
	@Where(clause="ADVISER_TYPE = 2")
	@NotFound(action=NotFoundAction.IGNORE)
	private List<GjtGraduationAdviser> gjtGraduationAdvisers2;


	//bi-directional many-to-one association to GjtGraduationAdviser
	@OneToMany(cascade={CascadeType.ALL}, mappedBy="gjtGraduationSpecialty")
	@Where(clause="ADVISER_TYPE = 3")
	@NotFound(action=NotFoundAction.IGNORE)
	private List<GjtGraduationAdviser> gjtGraduationAdvisers3;

	//bi-directional many-to-one association to GjtGraduationBatch
	@ManyToOne
	@JoinColumn(name="BATCH_ID")
	private GjtGraduationBatch gjtGraduationBatch;

	@Transient
	private String specialtyName;

	@Transient
	private String trainingName;

	@Transient
	private String gradeId;

	@Transient
	private String gradeName;

	@Transient
	private String[] advisers1;

	@Transient
	private String[] advisers2;

	@Transient
	private String[] advisers3;
	
	@OneToMany(mappedBy="gjtGraduationSpecialty")
	@Where(clause="IS_DELETED = 'N'")
	@OrderBy(clause="BEGIN_TIME")
	@NotFound(action=NotFoundAction.IGNORE)
	private List<GjtGraduationDefencePlan> gjtGraduationDefencePlans;
	
	@OneToMany(mappedBy="gjtGraduationSpecialty")
	@Where(clause="IS_DELETED = 'N' and DEFENCE_TYPE = 1")
	@OrderBy(clause="BEGIN_TIME")
	@NotFound(action=NotFoundAction.IGNORE)
	private List<GjtGraduationDefencePlan> gjtGraduationDefencePlans1;
	
	@OneToMany(mappedBy="gjtGraduationSpecialty")
	@Where(clause="IS_DELETED = 'N' and DEFENCE_TYPE = 2")
	@OrderBy(clause="BEGIN_TIME")
	@NotFound(action=NotFoundAction.IGNORE)
	private List<GjtGraduationDefencePlan> gjtGraduationDefencePlans2;
	
	public GjtGraduationSpecialty() {
	}

	public String getSettingId() {
		return this.settingId;
	}

	public void setSettingId(String settingId) {
		this.settingId = settingId;
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

	public String getExample() {
		return this.example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String getExampleName() {
		return exampleName;
	}

	public void setExampleName(String exampleName) {
		this.exampleName = exampleName;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getSpecialtyId() {
		return this.specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public String getTopic() {
		return this.topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTrainingLevel() {
		return this.trainingLevel;
	}

	public void setTrainingLevel(String trainingLevel) {
		this.trainingLevel = trainingLevel;
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

	public List<GjtGraduationAdviser> getGjtGraduationAdvisers() {
		return gjtGraduationAdvisers;
	}

	public void setGjtGraduationAdvisers(List<GjtGraduationAdviser> gjtGraduationAdvisers) {
		this.gjtGraduationAdvisers = gjtGraduationAdvisers;
	}

	public List<GjtGraduationAdviser> getGjtGraduationAdvisers1() {
		return gjtGraduationAdvisers1;
	}

	public void setGjtGraduationAdvisers1(List<GjtGraduationAdviser> gjtGraduationAdvisers1) {
		this.gjtGraduationAdvisers1 = gjtGraduationAdvisers1;
	}

	public List<GjtGraduationAdviser> getGjtGraduationAdvisers2() {
		return gjtGraduationAdvisers2;
	}

	public void setGjtGraduationAdvisers2(List<GjtGraduationAdviser> gjtGraduationAdvisers2) {
		this.gjtGraduationAdvisers2 = gjtGraduationAdvisers2;
	}

	public List<GjtGraduationAdviser> getGjtGraduationAdvisers3() {
		return gjtGraduationAdvisers3;
	}

	public void setGjtGraduationAdvisers3(List<GjtGraduationAdviser> gjtGraduationAdvisers3) {
		this.gjtGraduationAdvisers3 = gjtGraduationAdvisers3;
	}

	public GjtGraduationBatch getGjtGraduationBatch() {
		return this.gjtGraduationBatch;
	}

	public void setGjtGraduationBatch(GjtGraduationBatch gjtGraduationBatch) {
		this.gjtGraduationBatch = gjtGraduationBatch;
	}

	public String getSpecialtyName() {
		return specialtyName;
	}

	public void setSpecialtyName(String specialtyName) {
		this.specialtyName = specialtyName;
	}

	public String getTrainingName() {
		return trainingName;
	}

	public void setTrainingName(String trainingName) {
		this.trainingName = trainingName;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String[] getAdvisers1() {
		return advisers1;
	}

	public void setAdvisers1(String[] advisers1) {
		this.advisers1 = advisers1;
	}

	public String[] getAdvisers2() {
		return advisers2;
	}

	public void setAdvisers2(String[] advisers2) {
		this.advisers2 = advisers2;
	}

	public String[] getAdvisers3() {
		return advisers3;
	}

	public void setAdvisers3(String[] advisers3) {
		this.advisers3 = advisers3;
	}

	public List<GjtGraduationDefencePlan> getGjtGraduationDefencePlans() {
		return gjtGraduationDefencePlans;
	}

	public void setGjtGraduationDefencePlans(List<GjtGraduationDefencePlan> gjtGraduationDefencePlans) {
		this.gjtGraduationDefencePlans = gjtGraduationDefencePlans;
	}

	public List<GjtGraduationDefencePlan> getGjtGraduationDefencePlans1() {
		return gjtGraduationDefencePlans1;
	}

	public void setGjtGraduationDefencePlans1(List<GjtGraduationDefencePlan> gjtGraduationDefencePlans1) {
		this.gjtGraduationDefencePlans1 = gjtGraduationDefencePlans1;
	}

	public List<GjtGraduationDefencePlan> getGjtGraduationDefencePlans2() {
		return gjtGraduationDefencePlans2;
	}

	public void setGjtGraduationDefencePlans2(List<GjtGraduationDefencePlan> gjtGraduationDefencePlans2) {
		this.gjtGraduationDefencePlans2 = gjtGraduationDefencePlans2;
	}

	@Override
	public String toString() {
		return "GjtGraduationSpecialty [settingId=" + settingId + ", createdBy=" + createdBy + ", createdDt="
				+ createdDt + ", deletedBy=" + deletedBy + ", deletedDt=" + deletedDt + ", example=" + example
				+ ", isDeleted=" + isDeleted + ", specialtyId=" + specialtyId + ", topic=" + topic + ", trainingLevel="
				+ trainingLevel + ", updatedBy=" + updatedBy + ", updatedDt=" + updatedDt + ", gjtGraduationAdvisers="
				+ gjtGraduationAdvisers + ", gjtGraduationAdvisers1=" + gjtGraduationAdvisers1
				+ ", gjtGraduationAdvisers2=" + gjtGraduationAdvisers2 + ", gjtGraduationAdvisers3="
				+ gjtGraduationAdvisers3 + ", gjtGraduationBatch=" + gjtGraduationBatch + ", specialtyName="
				+ specialtyName + ", trainingName=" + trainingName + "]";
	}

}