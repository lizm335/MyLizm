package com.gzedu.xlims.pojo.exam;

import com.gzedu.xlims.pojo.GjtStudyCenter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the GJT_EXAM_POINT_NEW database table.
 * 
 */
@Entity
@Table(name = "GJT_EXAM_POINT_NEW")
@NamedQuery(name = "GjtExamPointNew.findAll", query = "SELECT g FROM GjtExamPointNew g")
public class GjtExamPointNew implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EXAM_POINT_ID")
	private String examPointId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAM_BATCH_ID", referencedColumnName = "EXAM_BATCH_ID", insertable = false, updatable = false)
	private GjtExamBatchNew examBatchNew;

	@Column(name = "EXAM_BATCH_ID")
	private String examBatchId;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAM_POINT_ID")
	@OrderBy("orderNo asc")
	private List<GjtExamRoomNew> gjtExamRoomNews;

	@ManyToMany
	@JoinTable(name = "GJT_EXAM_POINT_NEW_STUDYCENTER", joinColumns = { @JoinColumn(name = "EXAM_POINT_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "STUDY_CENTER_ID") })
	private List<GjtStudyCenter> gjtStudyCenters;

	@Column(name = "EXAM_BATCH_CODE")
	private String examBatchCode;

	@Column(name = "AREA_ID")
	private String areaId;// 省市区

	@Transient
	private String provinceCode;// 省
	@Transient
	private String provinceName;// 省
	@Transient
	private String cityCode;// 市
	@Transient
	private String cityName;// 市
	@Transient
	private String districtCode;// 区
	@Transient
	private String districtName;// 区
	@Transient
	private int seatCount;// 该考点的座位数量
	@Transient
	private int studentAppointCount2;// 该考点笔考的预约记录数
	@Transient
	private int studentAppointCount3;// 该考点机考的预约记录数
	@Transient
	private int roomCount;// 该考点考场数量

	private String address;// 详细地址
	
	private String examType;// 考试类型

	private String code;// 考点编号

	@Column(name = "IS_ENABLED")
	private boolean isEnabled = true;

	@Column(name = "LINK_MAN")
	private String linkMan;

	@Column(name = "LINK_TEL")
	private String linkTel;

	private String name;

	@Column(name = "XX_ID")
	private String xxId;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	public GjtExamPointNew() {
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getExamBatchCode() {
		return examBatchCode;
	}

	public void setExamBatchCode(String examBatchCode) {
		this.examBatchCode = examBatchCode;
	}

	public String getExamPointId() {
		return examPointId;
	}

	public void setExamPointId(String examPointId) {
		this.examPointId = examPointId;
	}

	public boolean getIsEnabled() {
		return isEnabled;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getLinkMan() {
		return this.linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getLinkTel() {
		return this.linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
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

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public List<GjtExamRoomNew> getGjtExamRoomNews() {
		return gjtExamRoomNews;
	}

	public void setGjtExamRoomNews(List<GjtExamRoomNew> gjtExamRoomNews) {
		this.gjtExamRoomNews = gjtExamRoomNews;
	}

	public List<GjtStudyCenter> getGjtStudyCenters() {
		return gjtStudyCenters;
	}

	public void setGjtStudyCenters(List<GjtStudyCenter> gjtStudyCenters) {
		this.gjtStudyCenters = gjtStudyCenters;
	}

	public GjtExamBatchNew getExamBatchNew() {
		return examBatchNew;
	}

	public void setExamBatchNew(GjtExamBatchNew examBatchNew) {
		this.examBatchNew = examBatchNew;
	}

	public String getExamBatchId() {
		return examBatchId;
	}

	public void setExamBatchId(String examBatchId) {
		this.examBatchId = examBatchId;
	}

	public int getSeatCount() {
		seatCount = 0;
		if (this.getGjtExamRoomNews() != null) {
			for (GjtExamRoomNew gjtExamRoomNew : this.getGjtExamRoomNews()) {
				if (gjtExamRoomNew.getIsDeleted() == 0) {
//				if ("N".equals(gjtExamRoomNew.getIsDeleted())) {
					seatCount += gjtExamRoomNew.getSeats();
				}
			}
		}
		return seatCount;
	}

	public int getStudentAppointCount2() {
		return studentAppointCount2;
	}

	public void setStudentAppointCount2(int studentAppointCount2) {
		this.studentAppointCount2 = studentAppointCount2;
	}

	public int getStudentAppointCount3() {
		return studentAppointCount3;
	}

	public void setStudentAppointCount3(int studentAppointCount3) {
		this.studentAppointCount3 = studentAppointCount3;
	}

	public int getRoomCount() {
		return gjtExamRoomNews.size();
	}

	public void setRoomCount(int roomCount) {
		this.roomCount = roomCount;
	}

	public void setSeatCount(int seatCount) {
		this.seatCount = seatCount;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
}