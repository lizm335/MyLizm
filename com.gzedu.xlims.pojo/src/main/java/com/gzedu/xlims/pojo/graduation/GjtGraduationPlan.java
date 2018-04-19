package com.gzedu.xlims.pojo.graduation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;

import com.gzedu.xlims.pojo.GjtGrade;

/**
 * 毕业计划实体类<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年08月31日
 * @version 3.0
 */
@Entity
@Table(name="GJT_GRADUATION_PLAN")
@NamedQuery(name="GjtGraduationPlan.findAll", query="SELECT g FROM GjtRollPlan g where g.isDeleted='N'")
public class GjtGraduationPlan implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY) // 学期ID
	@JoinColumn(name = "TERM_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtGrade gjtGrade;
	
	@Column(name="TERM_ID",insertable = false,updatable=false)
	private String termId;
	
	@Column(name="GRA_PLAN_NO")
	private String graPlanNo;

	@Column(name="GRA_PLAN_TITLE")
	private String graPlanTitle;
	
	/**
	 * 毕业登记开始时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="GRA_REGISTER_BEGIN_DT")
	private Date graRegisterBeginDt;
	
	/**
	 * 毕业登记结束时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="GRA_REGISTER_END_DT")
	private Date graRegisterEndDt;
	
	/**
	 * 毕业申请开始时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="GRA_APPLY_BEGIN_DT")
	private Date graApplyBeginDt;

	/**
	 * 毕业申请结束时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="GRA_APPLY_END_DT")
	private Date graApplyEndDt;

	/**
	 * 学位申请开始时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="DEGREE_APPLY_BEGIN_DT")
	private Date degreeApplyBeginDt;

	/**
	 * 学位申请结束时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="DEGREE_APPLY_END_DT")
	private Date degreeApplyEndDt;

	/**
	 * 毕业审核开始时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="GRA_AUDIT_BEGIN_DT")
	private Date graAuditBeginDt;

	/**
	 * 毕业审核结束时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="GRA_AUDIT_END_DT")
	private Date graAuditEndDt;

	/**
	 * 学位审核开始时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="DEGREE_AUDIT_BEGIN_DT")
	private Date degreeAuditBeginDt;

	/**
	 * 学位审核结束时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="DEGREE_AUDIT_END_DT")
	private Date degreeAuditEndDt;

	/**
	 * 学位意向退回开始时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="DEGREE_BACK_BEGIN_DT")
	private Date degreeBackBeginDt;

	/**
	 * 学位意向退回结束时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="DEGREE_BACK_END_DT")
	private Date degreeBackEndDt;

	/**
	 * 毕业证书领取开始时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="GRA_CERT_RECEIVE_BEGIN_DT")
	private Date graCertReceiveBeginDt;

	/**
	 * 毕业证书领取结束时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="GRA_CERT_RECEIVE_END_DT")
	private Date graCertReceiveEndDt;

	/**
	 * 毕业档案领取开始时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="GRA_ARCHIVES_RECEIVE_BEGIN_DT")
	private Date graArchivesReceiveBeginDt;

	/**
	 * 毕业档案领取结束时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="GRA_ARCHIVES_RECEIVE_END_DT")
	private Date graArchivesReceiveEndDt;

	/**
	 * 学位证书领取开始时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="DEGREE_CERT_RECEIVE_BEGIN_DT")
	private Date degreeCertReceiveBeginDt;

	/**
	 * 学位证书领取结束时间
	 */
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="DEGREE_CERT_RECEIVE_END_DT")
	private Date degreeCertReceiveEndDt;

	/**
	 * 审核状态 0-待审核 1-审核通过 2-审核不通过
	 */
	@Column(name="AUDIT_STATE", insertable = false)
	private Integer auditState;

	@Column(name="PUBLISH_OPERATOR")
	private String publishOperator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="PUBLISH_DT")
	private Date publishDt;

	@Column(name="XX_ID")
	private String xxId;

	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	@Transient
	private int status;  // 查询状态： 0-待审核 2-审核不通过 3-未开始，4-进行中，5-已结束

	public GjtGraduationPlan() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GjtGrade getGjtGrade() {
		return gjtGrade;
	}

	public void setGjtGrade(GjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	public String getGraPlanNo() {
		return graPlanNo;
	}

	public void setGraPlanNo(String graPlanNo) {
		this.graPlanNo = graPlanNo;
	}

	public String getGraPlanTitle() {
		return graPlanTitle;
	}

	public void setGraPlanTitle(String graPlanTitle) {
		this.graPlanTitle = graPlanTitle;
	}
		
	public Date getGraRegisterBeginDt() {
		return graRegisterBeginDt;
	}

	public void setGraRegisterBeginDt(Date graRegisterBeginDt) {
		this.graRegisterBeginDt = graRegisterBeginDt;
	}

	public Date getGraRegisterEndDt() {
		return graRegisterEndDt;
	}

	public void setGraRegisterEndDt(Date graRegisterEndDt) {
		this.graRegisterEndDt = graRegisterEndDt;
	}

	public Date getGraApplyBeginDt() {
		return graApplyBeginDt;
	}

	public void setGraApplyBeginDt(Date graApplyBeginDt) {
		this.graApplyBeginDt = graApplyBeginDt;
	}

	public Date getGraApplyEndDt() {
		return graApplyEndDt;
	}

	public void setGraApplyEndDt(Date graApplyEndDt) {
		this.graApplyEndDt = graApplyEndDt;
	}

	public Date getDegreeApplyBeginDt() {
		return degreeApplyBeginDt;
	}

	public void setDegreeApplyBeginDt(Date degreeApplyBeginDt) {
		this.degreeApplyBeginDt = degreeApplyBeginDt;
	}

	public Date getDegreeApplyEndDt() {
		return degreeApplyEndDt;
	}

	public void setDegreeApplyEndDt(Date degreeApplyEndDt) {
		this.degreeApplyEndDt = degreeApplyEndDt;
	}

	public Date getGraAuditBeginDt() {
		return graAuditBeginDt;
	}

	public void setGraAuditBeginDt(Date graAuditBeginDt) {
		this.graAuditBeginDt = graAuditBeginDt;
	}

	public Date getGraAuditEndDt() {
		return graAuditEndDt;
	}

	public void setGraAuditEndDt(Date graAuditEndDt) {
		this.graAuditEndDt = graAuditEndDt;
	}

	public Date getDegreeAuditBeginDt() {
		return degreeAuditBeginDt;
	}

	public void setDegreeAuditBeginDt(Date degreeAuditBeginDt) {
		this.degreeAuditBeginDt = degreeAuditBeginDt;
	}

	public Date getDegreeAuditEndDt() {
		return degreeAuditEndDt;
	}

	public void setDegreeAuditEndDt(Date degreeAuditEndDt) {
		this.degreeAuditEndDt = degreeAuditEndDt;
	}

	public Date getDegreeBackBeginDt() {
		return degreeBackBeginDt;
	}

	public void setDegreeBackBeginDt(Date degreeBackBeginDt) {
		this.degreeBackBeginDt = degreeBackBeginDt;
	}

	public Date getDegreeBackEndDt() {
		return degreeBackEndDt;
	}

	public void setDegreeBackEndDt(Date degreeBackEndDt) {
		this.degreeBackEndDt = degreeBackEndDt;
	}

	public Date getGraCertReceiveBeginDt() {
		return graCertReceiveBeginDt;
	}

	public void setGraCertReceiveBeginDt(Date graCertReceiveBeginDt) {
		this.graCertReceiveBeginDt = graCertReceiveBeginDt;
	}

	public Date getGraCertReceiveEndDt() {
		return graCertReceiveEndDt;
	}

	public void setGraCertReceiveEndDt(Date graCertReceiveEndDt) {
		this.graCertReceiveEndDt = graCertReceiveEndDt;
	}

	public Date getGraArchivesReceiveBeginDt() {
		return graArchivesReceiveBeginDt;
	}

	public void setGraArchivesReceiveBeginDt(Date graArchivesReceiveBeginDt) {
		this.graArchivesReceiveBeginDt = graArchivesReceiveBeginDt;
	}

	public Date getGraArchivesReceiveEndDt() {
		return graArchivesReceiveEndDt;
	}

	public void setGraArchivesReceiveEndDt(Date graArchivesReceiveEndDt) {
		this.graArchivesReceiveEndDt = graArchivesReceiveEndDt;
	}

	public Date getDegreeCertReceiveBeginDt() {
		return degreeCertReceiveBeginDt;
	}

	public void setDegreeCertReceiveBeginDt(Date degreeCertReceiveBeginDt) {
		this.degreeCertReceiveBeginDt = degreeCertReceiveBeginDt;
	}

	public Date getDegreeCertReceiveEndDt() {
		return degreeCertReceiveEndDt;
	}

	public void setDegreeCertReceiveEndDt(Date degreeCertReceiveEndDt) {
		this.degreeCertReceiveEndDt = degreeCertReceiveEndDt;
	}

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public String getPublishOperator() {
		return publishOperator;
	}

	public void setPublishOperator(String publishOperator) {
		this.publishOperator = publishOperator;
	}

	public Date getPublishDt() {
		return publishDt;
	}

	public void setPublishDt(Date publishDt) {
		this.publishDt = publishDt;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
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

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
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

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTermId() {
		return termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
	}
}