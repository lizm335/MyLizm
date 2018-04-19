package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the GJT_SIGNUP database table.
 * 
 */
@Entity
@Table(name = "GJT_SIGNUP")
@NamedQuery(name = "GjtSignup.findAll", query = "SELECT g FROM GjtSignup g")
public class GjtSignup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SIGNUP_ID")
	private String signupId;

	@OneToOne(fetch = FetchType.LAZY, optional = false, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH })
	@JoinColumn(name = "STUDENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtStudentInfo gjtStudentInfo;

//	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
//	@JoinColumn(name = "ENROLL_BATCH_ID")
//	private GjtEnrollBatchNew gjtEnrollBatchNew;
	
	@Column(name = "ADJUST_SPECIALTY_ID")
	private String adjustSpecialtyId;

	@Column(name = "ADMIT_STATUS")
	private String admitStatus;

	@Column(name = "APPLY_EXEMPTIOIN")
	private String applyExemptioin;

	@Column(name = "AUDIT_BY")
	private String auditBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AUDIT_DATE")
	private Date auditDate;

	@Column(name = "AUDIT_OPINION")
	private String auditOpinion;

	@Column(name = "AUDIT_SOURCE")
	private String auditSource;

	@Column(name = "AUDIT_STATE")
	private String auditState;

	@Column(name = "AUDIT_STATUS")
	private String auditStatus;

	private String charge; // 收费状态 0：已全额缴费，1：已部分缴费，2：待缴费，3：已欠费

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "DOCUMENT_NO")
	private String documentNo;

	@Column(name = "ENTER_TYPE")
	private String enterType;

	@Column(name = "EXAM_NUMBER")
	private String examNumber;

	@Column(name = "GRAD_COLLEGE")
	private String gradCollege;

	@Column(name = "GRAD_COLLEGE_CODE")
	private String gradCollegeCode;

	@Column(name = "GRAD_DATE")
	private String gradDate;

	@Column(name = "GRAD_ELEC_NO")
	private String gradElecNo;

	@Column(name = "GRAD_NO")
	private String gradNo;

	@Column(name = "GRAD_SPECIALTY")
	private String gradSpecialty;

	private String idcard;

	@Column(name = "INFO_FROM")
	private String infoFrom;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	@Column(name = "IS_MINORITY")
	private String isMinority;

	@Column(name = "IS_THIS_COLLEGE")
	private String isThisCollege;

	@Column(name = "JOB_COMPANY")
	private String jobCompany;

	@Column(name = "JOB_DATE")
	private String jobDate;

	@Column(name = "JOB_POST")
	private String jobPost;

	@Column(name = "JOB_STATUS")
	private String jobStatus;

	@Column(name = "LAST_GRADUATIONAL")
	private String lastGraduational;

	private String mail;

	private String memo;

	private String mobile;

	private String name;

	@Column(name = "ORDER_SN")
	private String orderSn;

	private String portty;

	private String productid;

	private String pycc;

	@Column(name = "SIGNUP_CODE")
	private String signupCode;

	@Column(name = "SIGNUP_SPECIALTY_ID")//专业ID
	private String signupSpecialtyId;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@Column(name = "XX_ID")
	private String xxId;

	@Column(name = "XXZX_ID")
	private String xxzxId;

	@Column(name = "YX_ID")
	private String yxId;

	@Column(name = "ZGXL_RADIO_TYPE", insertable = false)
	private Integer zgxlRadioType; // 专科-最高学历证明类型，默认0 0-毕业证复印件 1-学历证明+专科承诺书

	@Column(name = "SIGNUP_SFZ_TYPE", insertable = false)
	private Integer signupSfzType; // 所有-身份证件类型，默认0 0-中国居民身份证 1-其他身份证件（中国居民临时身份证、港澳台、外籍人士身份证）

	@Column(name = "SIGNUP_BYZ_TYPE", insertable = false)
	private Integer signupByzType; // 本科-毕业证件类型，默认0 0-毕业证 1-毕业电子注册号

	@Column(name = "SIGNUP_JZZ_TYPE", insertable = false)
	private Integer signupJzzType; // 非广东户口证件类型，默认0 0-居住证 1-在读年级证明

	@Transient
	private String zymc;
	@Transient
	private String xxzxmc;

	/**
	 * @return the xxzxmc
	 */
	public String getXxzxmc() {
		return xxzxmc;
	}

	/**
	 * @param xxzxmc
	 *            the xxzxmc to set
	 */
	public void setXxzxmc(String xxzxmc) {
		this.xxzxmc = xxzxmc;
	}

	/**
	 * @return the zymc
	 */
	public String getZymc() {
		return zymc;
	}

	/**
	 * @param zymc
	 *            the zymc to set
	 */
	public void setZymc(String zymc) {
		this.zymc = zymc;
	}

	public GjtSignup() {
	}

	public GjtSignup(String signupId) {
		this.signupId = signupId;
	}

	public String getSignupId() {
		return this.signupId;
	}

	public void setSignupId(String signupId) {
		this.signupId = signupId;
	}

	public String getAdjustSpecialtyId() {
		return this.adjustSpecialtyId;
	}

	public void setAdjustSpecialtyId(String adjustSpecialtyId) {
		this.adjustSpecialtyId = adjustSpecialtyId;
	}

	public String getAdmitStatus() {
		return this.admitStatus;
	}

	public void setAdmitStatus(String admitStatus) {
		this.admitStatus = admitStatus;
	}

	public String getApplyExemptioin() {
		return this.applyExemptioin;
	}

	public void setApplyExemptioin(String applyExemptioin) {
		this.applyExemptioin = applyExemptioin;
	}

	public String getAuditBy() {
		return this.auditBy;
	}

	public void setAuditBy(String auditBy) {
		this.auditBy = auditBy;
	}

	public Date getAuditDate() {
		return this.auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditOpinion() {
		return this.auditOpinion;
	}

	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}

	public String getAuditSource() {
		return this.auditSource;
	}

	public void setAuditSource(String auditSource) {
		this.auditSource = auditSource;
	}

	public String getAuditState() {
		return this.auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getAuditStatus() {
		return this.auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getCharge() {
		return this.charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
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

	public String getDocumentNo() {
		return this.documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

//	public GjtEnrollBatchNew getGjtEnrollBatchNew() {
//		return gjtEnrollBatchNew;
//	}
//
//	public void setGjtEnrollBatchNew(GjtEnrollBatchNew gjtEnrollBatchNew) {
//		this.gjtEnrollBatchNew = gjtEnrollBatchNew;
//	}

	public String getEnterType() {
		return this.enterType;
	}

	public void setEnterType(String enterType) {
		this.enterType = enterType;
	}

	public String getExamNumber() {
		return this.examNumber;
	}

	public void setExamNumber(String examNumber) {
		this.examNumber = examNumber;
	}

	public String getGradCollege() {
		return this.gradCollege;
	}

	public void setGradCollege(String gradCollege) {
		this.gradCollege = gradCollege;
	}

	public String getGradCollegeCode() {
		return this.gradCollegeCode;
	}

	public void setGradCollegeCode(String gradCollegeCode) {
		this.gradCollegeCode = gradCollegeCode;
	}

	public String getGradDate() {
		return this.gradDate;
	}

	public void setGradDate(String gradDate) {
		this.gradDate = gradDate;
	}

	public String getGradElecNo() {
		return this.gradElecNo;
	}

	public void setGradElecNo(String gradElecNo) {
		this.gradElecNo = gradElecNo;
	}

	public String getGradNo() {
		return this.gradNo;
	}

	public void setGradNo(String gradNo) {
		this.gradNo = gradNo;
	}

	public String getGradSpecialty() {
		return this.gradSpecialty;
	}

	public void setGradSpecialty(String gradSpecialty) {
		this.gradSpecialty = gradSpecialty;
	}

	public String getIdcard() {
		return this.idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getInfoFrom() {
		return this.infoFrom;
	}

	public void setInfoFrom(String infoFrom) {
		this.infoFrom = infoFrom;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getIsMinority() {
		return this.isMinority;
	}

	public void setIsMinority(String isMinority) {
		this.isMinority = isMinority;
	}

	public String getIsThisCollege() {
		return this.isThisCollege;
	}

	public void setIsThisCollege(String isThisCollege) {
		this.isThisCollege = isThisCollege;
	}

	public String getJobCompany() {
		return this.jobCompany;
	}

	public void setJobCompany(String jobCompany) {
		this.jobCompany = jobCompany;
	}

	public String getJobDate() {
		return this.jobDate;
	}

	public void setJobDate(String jobDate) {
		this.jobDate = jobDate;
	}

	public String getJobPost() {
		return this.jobPost;
	}

	public void setJobPost(String jobPost) {
		this.jobPost = jobPost;
	}

	public String getJobStatus() {
		return this.jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getLastGraduational() {
		return this.lastGraduational;
	}

	public void setLastGraduational(String lastGraduational) {
		this.lastGraduational = lastGraduational;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrderSn() {
		return this.orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getPortty() {
		return this.portty;
	}

	public void setPortty(String portty) {
		this.portty = portty;
	}

	public String getProductid() {
		return this.productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getPycc() {
		return this.pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public String getSignupCode() {
		return this.signupCode;
	}

	public void setSignupCode(String signupCode) {
		this.signupCode = signupCode;
	}

	public String getSignupSpecialtyId() {
		return this.signupSpecialtyId;
	}

	public void setSignupSpecialtyId(String signupSpecialtyId) {
		this.signupSpecialtyId = signupSpecialtyId;
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

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getXxzxId() {
		return this.xxzxId;
	}

	public void setXxzxId(String xxzxId) {
		this.xxzxId = xxzxId;
	}

	public String getYxId() {
		return this.yxId;
	}

	public void setYxId(String yxId) {
		this.yxId = yxId;
	}

	public Integer getZgxlRadioType() {
		return zgxlRadioType;
	}

	public void setZgxlRadioType(Integer zgxlRadioType) {
		this.zgxlRadioType = zgxlRadioType;
	}

	public Integer getSignupSfzType() {
		return signupSfzType;
	}

	public void setSignupSfzType(Integer signupSfzType) {
		this.signupSfzType = signupSfzType;
	}

	public Integer getSignupByzType() {
		return signupByzType;
	}

	public void setSignupByzType(Integer signupByzType) {
		this.signupByzType = signupByzType;
	}

	public Integer getSignupJzzType() {
		return signupJzzType;
	}

	public void setSignupJzzType(Integer signupJzzType) {
		this.signupJzzType = signupJzzType;
	}
}