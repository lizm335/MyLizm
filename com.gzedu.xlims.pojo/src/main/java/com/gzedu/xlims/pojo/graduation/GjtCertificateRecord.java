package com.gzedu.xlims.pojo.graduation;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtStudentInfo;

/**
 * The persistent class for the GJT_CERTIFICATE_RECORD database table.
 * 毕业记录表
 */
@Entity
@Table(name = "GJT_CERTIFICATE_RECORD")
@NamedQuery(name = "GjtCertificateRecord.findAll", query = "SELECT g FROM GjtCertificateRecord g")
public class GjtCertificateRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "CERTIFICATE_TYPE")
	private int certificateType;// 证书类型： 0 毕业证 1学位证

	@Column(name = "CREATE_BY")
	private String createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DT")
	private Date createDt;

	@Column(name = "IS_AGENT")
	private int isAgent;// 是否代收： 0否 1是

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "ORG_ID")
	private String orgId;

	@Temporal(TemporalType.DATE)
	@Column(name = "SEND_DT")
	private Date sendDt;// 发放日期

	@Column(name = "SEND_ORG_ID")
	private String sendOrgId;// 发放机构

	@ManyToOne
	@JoinColumn(name = "SEND_ORG_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtOrg sendOrg;

	@Column(name = "SEND_PERSON")
	private String sendPerson;// 发放人

	@Column(name = "SEND_TYPE")
	private int sendType;// 发放类型：0现场认领 1快递发放 目前只有线程认领

	@Column(name = "SIGN_PERSON")
	private String signPerson;// 签收人

	private int status;// 状态：0未发放 1发放中 2已发放

	@Column(name = "STUDENT_ID")
	private String studentId;

	@ManyToOne
	@JoinColumn(name = "STUDENT_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtStudentInfo gjtStudentInfo;


	public GjtCertificateRecord() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCertificateType() {
		return this.certificateType;
	}

	public void setCertificateType(int certificateType) {
		this.certificateType = certificateType;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDt() {
		return this.createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public int getIsAgent() {
		return this.isAgent;
	}

	public void setIsAgent(int isAgent) {
		this.isAgent = isAgent;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Date getSendDt() {
		return this.sendDt;
	}

	public void setSendDt(Date sendDt) {
		this.sendDt = sendDt;
	}

	public String getSendOrgId() {
		return this.sendOrgId;
	}

	public void setSendOrgId(String sendOrgId) {
		this.sendOrgId = sendOrgId;
	}

	public String getSendPerson() {
		return this.sendPerson;
	}

	public void setSendPerson(String sendPerson) {
		this.sendPerson = sendPerson;
	}

	public int getSendType() {
		return this.sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public String getSignPerson() {
		return this.signPerson;
	}

	public void setSignPerson(String signPerson) {
		this.signPerson = signPerson;
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

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public GjtOrg getSendOrg() {
		return sendOrg;
	}

	public void setSendOrg(GjtOrg sendOrg) {
		this.sendOrg = sendOrg;
	}

	@Transient
	public String getStatusName() {
		switch (this.status) {
			case 0:
				return "未发放";
			case 1:
				return "发放中";
			case 2:
				return "已发放";
			default:
				return null;
		}
	}
}