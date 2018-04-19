package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the GJT_CHANGE_SPECIALTY database table. 学籍异动表
 */
@Entity
@Table(name = "GJT_CHANGE_SPECIALTY")
@NamedQuery(name = "GjtChangeSpecialty.findAll", query = "SELECT g FROM GjtChangeSpecialty g")
public class GjtChangeSpecialty implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;// id

	@Column(name = "A_GJTGRADEID") // 当前年级ID
	private String aGjtgradeid;

	@Column(name = "A_NAME") // 当前记录名称
	private String aName;

	@OneToOne
	@JoinColumn(name = "A_SPECIALTY_ID") // 为什么是对象，兼容以前
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSpecialty nowGjtSpecialty; // 当前专业ID

	@Column(name = "A_STUDYCENTERID") // 当前学习中心ID
	private String aStudycenterid;

	@Column(name = "B_GJTGRADEID") // 修改前的年级ID
	private String bGjtgradeid;

	@Column(name = "B_NAME") // 修改前的名称
	private String bName;

	@OneToOne
	@JoinColumn(name = "B_SPECIALTY_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSpecialty oldGjtSpecialty; // 修改前的专业ID

	@Column(name = "B_STUDYCENTERID") // 修改前的中心ID
	private String bStudycenterid;

	private String changetype;// 变更类型 101转专业,102转中心,103转年级

	@Column(name = "CREATED_BY") // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建日期
	@Column(name = "CREATED_DT")
	private Date createdDt;

	private String remark;// 备注异动原因

	private String state;// 状态 1审核通过

	@OneToOne
	@JoinColumn(name = "STUDENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtStudentInfo gjtStudentInfo; // 学生ID

	public GjtChangeSpecialty() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getaGjtgradeid() {
		return aGjtgradeid;
	}

	public void setaGjtgradeid(String aGjtgradeid) {
		this.aGjtgradeid = aGjtgradeid;
	}

	public String getaName() {
		return aName;
	}

	public void setaName(String aName) {
		this.aName = aName;
	}

	public String getaStudycenterid() {
		return aStudycenterid;
	}

	public void setaStudycenterid(String aStudycenterid) {
		this.aStudycenterid = aStudycenterid;
	}

	public String getbGjtgradeid() {
		return bGjtgradeid;
	}

	public void setbGjtgradeid(String bGjtgradeid) {
		this.bGjtgradeid = bGjtgradeid;
	}

	public String getbName() {
		return bName;
	}

	public void setbName(String bName) {
		this.bName = bName;
	}

	/**
	 * @return the nowGjtSpecialty
	 */
	public GjtSpecialty getNowGjtSpecialty() {
		return nowGjtSpecialty;
	}

	/**
	 * @param nowGjtSpecialty
	 *            the nowGjtSpecialty to set
	 */
	public void setNowGjtSpecialty(GjtSpecialty nowGjtSpecialty) {
		this.nowGjtSpecialty = nowGjtSpecialty;
	}

	/**
	 * @return the oldGjtSpecialty
	 */
	public GjtSpecialty getOldGjtSpecialty() {
		return oldGjtSpecialty;
	}

	/**
	 * @param oldGjtSpecialty
	 *            the oldGjtSpecialty to set
	 */
	public void setOldGjtSpecialty(GjtSpecialty oldGjtSpecialty) {
		this.oldGjtSpecialty = oldGjtSpecialty;
	}

	public String getbStudycenterid() {
		return bStudycenterid;
	}

	public void setbStudycenterid(String bStudycenterid) {
		this.bStudycenterid = bStudycenterid;
	}

	public String getChangetype() {
		return changetype;
	}

	public void setChangetype(String changetype) {
		this.changetype = changetype;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

}