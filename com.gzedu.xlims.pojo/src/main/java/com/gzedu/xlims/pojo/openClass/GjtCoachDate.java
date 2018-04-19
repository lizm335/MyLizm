package com.gzedu.xlims.pojo.openClass;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the GJT_COACH_DATE database table. 辅导资料
 */
@Entity
@Table(name = "GJT_COACH_DATE")
@NamedQuery(name = "GjtCoachDate.findAll", query = "SELECT g FROM GjtCoachDate g")
public class GjtCoachDate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String coachDataId;

	private String dataName;// 资料名称

	private String dataType;// 资料类型

	private String dataPath;// 资料路径

	private String dataLabel;// 资料标签

	private int downloadNum;// 下载次数

	private String createdBy;// 共享人

	private Date createdDt;// 创建日期

	private String isDeleted;

	private String termCourseId;// 期课程id

	private String orgId;// 机构id

	public String getCoachDataId() {
		return coachDataId;
	}

	public void setCoachDataId(String coachDataId) {
		this.coachDataId = coachDataId;
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public int getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(int downloadNum) {
		this.downloadNum = downloadNum;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
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

	public String getTermCourseId() {
		return termCourseId;
	}

	public void setTermCourseId(String termCourseId) {
		this.termCourseId = termCourseId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getDataLabel() {
		return dataLabel;
	}

	public void setDataLabel(String dataLabel) {
		this.dataLabel = dataLabel;
	}

}