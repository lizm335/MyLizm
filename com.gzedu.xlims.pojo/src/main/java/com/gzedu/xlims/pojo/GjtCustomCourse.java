package com.gzedu.xlims.pojo;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.google.common.collect.Sets;
import com.gzedu.xlims.pojo.comm.OperateType;

@Entity
@Table(name = "GJT_CUSTOM_COURSE")
@NamedQuery(name = "GjtCustomCourse.findAll", query = "SELECT c FROM GjtCustomCourse c")
public class GjtCustomCourse {

	/**
	 * 未上传
	 */
	@Transient
	public static int NOT_UPLOAD = 0;
	
	/**
	 *  (审核)上传中
	 */
	@Transient
	public static int AUDIT_UPLOADING = 6;
	
	/**
	 * 待审核
	 */
	@Transient
	public static int AUDIT_WAIT = 1;
	/**
	 * 审核通过
	 */
	@Transient
	public static int AUDIT_PASS = 2;
	/**
	 * 审核不通过
	 */
	@Transient
	public static int AUDIT_NOT_PASS = 3;
	/**
	 * 上传中
	 */
	@Transient
	public static int UPLOADING = 1;
	/**
	 * 已上传
	 */
	@Transient
	public static int ALREADY_UPLOADED = 2;
	

	@Id
	@Column(name = "CUSTOM_COURSE_ID")
	private String customCourseId;
	// 教学计划ID
	@Column(name = "TEACH_PLAN_ID", unique = true)
	private String teachPlanId;

	// 机构ID
	@Column(name = "ORG_ID", unique = true)
	private String orgId;

	// 授课计划
	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "CUSTOM_COURSE_ID")
	private List<GjtGrantCoursePlan> plans;
	// 授课计划审核记录
	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "SOURCE_ID")
	@Where(clause = " OPERATE_TYPE = 0 ")
	@OrderBy("operateTime ASC")
	private List<GjtAuditOperateLine> planAuditLines;

	// 授课成绩
	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "CUSTOM_COURSE_ID")
	private Set<GjtGrantCourseScore> scores;
	// 授课成绩审核记录
	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "SOURCE_ID")
	@Where(clause = " OPERATE_TYPE = 1 ")
	@OrderBy("operateTime ASC")
	private List<GjtAuditOperateLine> scoreAuditLines;

	// 成绩扫描件url
	@ElementCollection(targetClass = java.lang.String.class)
	@CollectionTable(name = "GJT_GRANT_COURSE_SCORE_IMAGE", joinColumns = @JoinColumn(name = "CUSTOM_COURSE_ID"))
	@Column(name = "IMAGE")
	private Set<String> images = Sets.newHashSet();
	// 成绩excel文件url
	@Column(name = "SCORE_FILE_URL")
	private String scoreFileUrl;

	// 授课计划状态(0：未上传,1:待审核,2:审核通过,3:审核不通过)
	@Column(name = "PLAN_STATUS")
	private Integer planStatus;

	// 授课凭证状态(0:未上传,1:上传中, 2:已上传)
	@Column(name = "CERTIFICATE_STATUS")
	private Integer certificateStatus;

	// 授课成绩状态(0：未上传,1:待审核,2:审核通过,3:审核不通过)
	@Column(name = "SCORE_STATUS")
	private Integer scoreStatus;

	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "UPDATED_BY", insertable = false) // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "IS_DELETED", insertable = false) // 是否删除
	private String isDeleted;

	public String getCustomCourseId() {
		return customCourseId;
	}

	public void setCustomCourseId(String customCourseId) {
		this.customCourseId = customCourseId;
	}

	public String getTeachPlanId() {
		return teachPlanId;
	}

	public void setTeachPlanId(String teachPlanId) {
		this.teachPlanId = teachPlanId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public List<GjtGrantCoursePlan> getPlans() {
		return plans;
	}

	public void setPlans(List<GjtGrantCoursePlan> plans) {
		this.plans = plans;
	}

	public List<GjtAuditOperateLine> getPlanAuditLines() {
		return planAuditLines;
	}

	public void setPlanAuditLines(List<GjtAuditOperateLine> planAuditLines) {
		this.planAuditLines = planAuditLines;
	}

	public Set<GjtGrantCourseScore> getScores() {
		return scores;
	}

	public void setScores(Set<GjtGrantCourseScore> scores) {
		this.scores = scores;
	}

	public List<GjtAuditOperateLine> getScoreAuditLines() {
		return scoreAuditLines;
	}

	public void setScoreAuditLines(List<GjtAuditOperateLine> scoreAuditLines) {
		this.scoreAuditLines = scoreAuditLines;
	}

	public Set<String> getImages() {
		return images;
	}

	public void setImages(Set<String> images) {
		this.images = images;
	}

	public String getScoreFileUrl() {
		return scoreFileUrl;
	}

	public void setScoreFileUrl(String scoreFileUrl) {
		this.scoreFileUrl = scoreFileUrl;
	}

	public Integer getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(Integer planStatus) {
		this.planStatus = planStatus;
	}

	public Integer getCertificateStatus() {
		return certificateStatus;
	}

	public void setCertificateStatus(Integer certificateStatus) {
		this.certificateStatus = certificateStatus;
	}

	public Integer getScoreStatus() {
		return scoreStatus;
	}

	public void setScoreStatus(Integer scoreStatus) {
		this.scoreStatus = scoreStatus;
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

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
