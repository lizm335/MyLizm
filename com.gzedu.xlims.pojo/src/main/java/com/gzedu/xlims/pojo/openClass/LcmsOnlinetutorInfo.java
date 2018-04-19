package com.gzedu.xlims.pojo.openClass;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the LCMS_ONLINETUTOR_INFO database table.
 * 
 */
@Entity
@Table(name = "LCMS_ONLINETUTOR_INFO")
@NamedQuery(name="LcmsOnlinetutorInfo.findAll", query="SELECT l FROM LcmsOnlinetutorInfo l")
public class LcmsOnlinetutorInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ONLINETUTOR_ID")
	private String onlinetutorId;

	@Column(name="CLASS_ID")
	private String classId;

	@Column(name="CLASSROOM_ID")
	private String classroomId;

	@Column(name="CLASSROOM_PASSWORD")
	private String classroomPassword;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	private String isdeleted;

	@Column(name="ONLINETUTOR_DESC")
	private String onlinetutorDesc;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ONLINETUTOR_FINISH")
	private Date onlinetutorFinish;

	@Column(name="ONLINETUTOR_NAME")
	private String onlinetutorName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ONLINETUTOR_START")
	private Date onlinetutorStart;

	@Column(name="ONLINETUTOR_TYPE")
	private String onlinetutorType;

	@Column(name="PARTICIPATE_NUM")
	private String participateNum;

	@Column(name="PARTICIPATE_REAL_NUM")
	private String participateRealNum;

	private String remark;

	@Column(name="TERMCOURSE_ID")
	private String termcourseId;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	private String chooseType;

	private String imgUrl;

	public LcmsOnlinetutorInfo() {
	}

	public String getOnlinetutorId() {
		return this.onlinetutorId;
	}

	public void setOnlinetutorId(String onlinetutorId) {
		this.onlinetutorId = onlinetutorId;
	}

	public String getClassId() {
		return this.classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassroomId() {
		return this.classroomId;
	}

	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}

	public String getClassroomPassword() {
		return this.classroomPassword;
	}

	public void setClassroomPassword(String classroomPassword) {
		this.classroomPassword = classroomPassword;
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

	public String getIsdeleted() {
		return this.isdeleted;
	}

	public void setIsdeleted(String isdeleted) {
		this.isdeleted = isdeleted;
	}

	public String getOnlinetutorDesc() {
		return this.onlinetutorDesc;
	}

	public void setOnlinetutorDesc(String onlinetutorDesc) {
		this.onlinetutorDesc = onlinetutorDesc;
	}

	public Date getOnlinetutorFinish() {
		return this.onlinetutorFinish;
	}

	public void setOnlinetutorFinish(Date onlinetutorFinish) {
		this.onlinetutorFinish = onlinetutorFinish;
	}

	public String getOnlinetutorName() {
		return this.onlinetutorName;
	}

	public void setOnlinetutorName(String onlinetutorName) {
		this.onlinetutorName = onlinetutorName;
	}

	public Date getOnlinetutorStart() {
		return this.onlinetutorStart;
	}

	public void setOnlinetutorStart(Date onlinetutorStart) {
		this.onlinetutorStart = onlinetutorStart;
	}

	public String getOnlinetutorType() {
		return this.onlinetutorType;
	}

	public void setOnlinetutorType(String onlinetutorType) {
		this.onlinetutorType = onlinetutorType;
	}

	public String getParticipateNum() {
		return this.participateNum;
	}

	public void setParticipateNum(String participateNum) {
		this.participateNum = participateNum;
	}

	public String getParticipateRealNum() {
		return this.participateRealNum;
	}

	public void setParticipateRealNum(String participateRealNum) {
		this.participateRealNum = participateRealNum;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTermcourseId() {
		return this.termcourseId;
	}

	public void setTermcourseId(String termcourseId) {
		this.termcourseId = termcourseId;
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

	public String getChooseType() {
		return chooseType;
	}

	public void setChooseType(String chooseType) {
		this.chooseType = chooseType;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}