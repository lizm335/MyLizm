package com.gzedu.xlims.pojo.thesis;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;


/**
 * The persistent class for the GJT_THESIS_APPLY database table.
 * 
 */
@Entity
@Table(name="GJT_THESIS_APPLY")
@NamedQuery(name="GjtThesisApply.findAll", query="SELECT g FROM GjtThesisApply g")
public class GjtThesisApply implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="APPLY_ID")
	private String applyId;

	@Column(name="APPLY_DEGREE")
	private int applyDegree;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="DEFENCE_PLAN_ID")
	private String defencePlanId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFENCE_PLAN_ID", insertable=false, updatable=false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtThesisDefencePlan gjtThesisDefencePlan;

	@Column(name="DEFENCE_SCORE")
	private Float defenceScore;

	@Column(name="DEFENCE_TEACHER1")
	private String defenceTeacher1;

	@Column(name="DEFENCE_TEACHER2")
	private String defenceTeacher2;

	@Column(name="DELETED_BY")
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED_DT")
	private Date deletedDt;

	@Column(name="GUIDE_TEACHER")
	private String guideTeacher;
	
	@ManyToOne
	@JoinColumn(name="GUIDE_TEACHER", insertable=false, updatable=false)
	private GjtEmployeeInfo guideTeacher1;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="NEED_DEFENCE")
	private int needDefence;

	@Column(name="DEFENCE_TYPE")
	private int defenceType;

	@Column(name="REVIEW_SCORE")
	private Float reviewScore;

	@Column(name="TEACHER_AUTOGRAPH")
	private String teacherAutograph;

	@Column(name="EXPRESS_COMPANY")
	private String expressCompany;

	@Column(name="EXPRESS_NUMBER")
	private String expressNumber;

	private int status;

	@Column(name="STUDENT_ID")
	private String studentId;

	@ManyToOne
	@JoinColumn(name="STUDENT_ID", insertable=false, updatable=false)
	private GjtStudentInfo gjtStudentInfo;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name="THESIS_PLAN_ID")
	private String thesisPlanId;

	//bi-directional many-to-one association to GjtThesisPlan
	@ManyToOne
	@JoinColumn(name="THESIS_PLAN_ID", insertable=false, updatable=false)
	private GjtThesisPlan gjtThesisPlan;
	
	/**
	 * 记录学员最后一次提交的记录
	 */
	@Transient
	private GjtThesisGuideRecord guideRecord;
	
	/**
	 * 记录指导老师的指导次数
	 */
	@Transient
	private int guideTimes;

	public GjtThesisApply() {
	}

	public String getApplyId() {
		return this.applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public int getApplyDegree() {
		return this.applyDegree;
	}

	public void setApplyDegree(int applyDegree) {
		this.applyDegree = applyDegree;
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

	public String getDefencePlanId() {
		return this.defencePlanId;
	}

	public void setDefencePlanId(String defencePlanId) {
		this.defencePlanId = defencePlanId;
	}

	public GjtThesisDefencePlan getGjtThesisDefencePlan() {
		return gjtThesisDefencePlan;
	}

	public void setGjtThesisDefencePlan(GjtThesisDefencePlan gjtThesisDefencePlan) {
		this.gjtThesisDefencePlan = gjtThesisDefencePlan;
	}

	public Float getDefenceScore() {
		return this.defenceScore;
	}

	public void setDefenceScore(Float defenceScore) {
		this.defenceScore = defenceScore;
	}

	public String getDefenceTeacher1() {
		return this.defenceTeacher1;
	}

	public void setDefenceTeacher1(String defenceTeacher1) {
		this.defenceTeacher1 = defenceTeacher1;
	}

	public String getDefenceTeacher2() {
		return this.defenceTeacher2;
	}

	public void setDefenceTeacher2(String defenceTeacher2) {
		this.defenceTeacher2 = defenceTeacher2;
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

	public String getGuideTeacher() {
		return this.guideTeacher;
	}

	public void setGuideTeacher(String guideTeacher) {
		this.guideTeacher = guideTeacher;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getNeedDefence() {
		return this.needDefence;
	}

	public void setNeedDefence(int needDefence) {
		this.needDefence = needDefence;
	}

	public Float getReviewScore() {
		return this.reviewScore;
	}

	public void setReviewScore(Float reviewScore) {
		this.reviewScore = reviewScore;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
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

	public GjtThesisPlan getGjtThesisPlan() {
		return this.gjtThesisPlan;
	}

	public void setGjtThesisPlan(GjtThesisPlan gjtThesisPlan) {
		this.gjtThesisPlan = gjtThesisPlan;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public String getThesisPlanId() {
		return thesisPlanId;
	}

	public void setThesisPlanId(String thesisPlanId) {
		this.thesisPlanId = thesisPlanId;
	}

	public GjtEmployeeInfo getGuideTeacher1() {
		return guideTeacher1;
	}

	public void setGuideTeacher1(GjtEmployeeInfo guideTeacher1) {
		this.guideTeacher1 = guideTeacher1;
	}

	public int getDefenceType() {
		return defenceType;
	}

	public void setDefenceType(int defenceType) {
		this.defenceType = defenceType;
	}

	public String getTeacherAutograph() {
		return teacherAutograph;
	}

	public void setTeacherAutograph(String teacherAutograph) {
		this.teacherAutograph = teacherAutograph;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getExpressNumber() {
		return expressNumber;
	}

	public void setExpressNumber(String expressNumber) {
		this.expressNumber = expressNumber;
	}

	public GjtThesisGuideRecord getGuideRecord() {
		return guideRecord;
	}

	public void setGuideRecord(GjtThesisGuideRecord guideRecord) {
		this.guideRecord = guideRecord;
	}

	public int getGuideTimes() {
		return guideTimes;
	}

	public void setGuideTimes(int guideTimes) {
		this.guideTimes = guideTimes;
	}

	@Override
	public String toString() {
		return "GjtThesisApply [applyId=" + applyId + ", applyDegree=" + applyDegree + ", createdBy=" + createdBy
				+ ", createdDt=" + createdDt + ", defencePlanId=" + defencePlanId + ", defenceScore=" + defenceScore
				+ ", defenceTeacher1=" + defenceTeacher1 + ", defenceTeacher2=" + defenceTeacher2 + ", deletedBy="
				+ deletedBy + ", deletedDt=" + deletedDt + ", guideTeacher=" + guideTeacher + ", isDeleted=" + isDeleted
				+ ", needDefence=" + needDefence + ", reviewScore=" + reviewScore + ", status=" + status
				+ ", studentId=" + studentId + ", updatedBy=" + updatedBy + ", updatedDt=" + updatedDt
				+ ", thesisPlanId=" + thesisPlanId + "]";
	}

}
