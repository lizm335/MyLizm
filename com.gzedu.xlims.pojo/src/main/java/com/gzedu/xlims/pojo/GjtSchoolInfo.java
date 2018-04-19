package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

/**
 * The persistent class for the GJT_SCHOOL_INFO database table. 学院信息表
 */
@Entity
@Table(name = "GJT_SCHOOL_INFO")
@NamedQuery(name = "GjtSchoolInfo.findAll", query = "SELECT g FROM GjtSchoolInfo g")
public class GjtSchoolInfo implements Serializable {
	// 用于关联关系表ID查询
	public GjtSchoolInfo(String id) {
		this.id = id;
	}

	@Id
	private String id;// id

	@OneToOne(optional = false, mappedBy = "gjtSchoolInfo")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtOrg gjtOrg;// 院校
	
	@OneToMany
	@JoinColumn(name = "XX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtSetOrgCopyright> gjtSetOrgCopyrights;// 院校版权设置
	
	@Column(name = "LINK_TEL") // 联系电话
	private String linkTel;

	@Column(name = "LINK_MAN")
	private String linkMan;// 联系人

	@Column(name = "LINK_MAIL")
	private String linkMail;// 联系邮箱

	private String appid;// appID- APP012

	@Column(name = "CHARGE_TYPE") // 收费类型
	private String chargeType;

	private String cjxyzk; // 附设成教学院状况,CJXYZK,1 有,0 否

	@Column(name = "CREATED_BY") // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建日期
	@Column(name = "CREATED_DT")
	private Date createdDt;

	private String czdh;// 传真电话

	private String dwfzr;// 党委负责人

	private String dzxx;// 电子信箱

	private String gcyxzk;// 211工程院校状况,GCYXZK,1 是,0 否

	@Column(name = "IS_DELETED") // 是否删除
	@Where(clause = "isDeleted='N'")
	private String isDeleted;

	@Column(name = "IS_ENABLED") // 是否启用
	private String isEnabled;

	private String jxny;// 建校年月

	private String logo1;// logo图标

	private String logo2;// logo图标

	private String logo3;// logo图标

	@Lob
	private String lsyg;// 历史沿革

	private String lxdh;// 联系电话

	private String memo;// 备注

	private String memo1;// 备注

	private String memo2;// 备注

	private String memo3;// 备注

	@Column(name = "ONE_FEE") // 单学分费用
	private BigDecimal oneFee;

	@Column(name = "UPDATED_BY") // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;// 版本

	private String wlxyzk;// 附设网络学院状况,WLXYZK,1 有,0 否

	private Double xkmls;// 学科门类数

	private String xqr;// 校庆日

	private String xxbxlxm;// 学校办学类型码

	private String xxdm;// 学校代码

	private String xxdz;// 学校地址

	private String xxjbzm;// 学校举办者码

	private String xxmc;// 学校名称

	private String xxqhm;// 所在地行政区

	private String xxxz;// 学校校长

	private String xxxzm;// 学校性质码

	private String xxywmc;// 学校英文名称

	private String xxzgbmm;// 学校主管部门码

	private String xxzgbmmc;// 学校主管部门名称

	private String yjsyzk;// 设立研究生院状况,YJSYZK,1 是,0 否

	private String yzbm;// 邮政编码

	private String zdyxzk;// 重点院校状况,ZDYXZK,1 是,0 否

	private String zydz;// 主页地址

	private String zzjgdm;// 组织机构代码

	public GjtSchoolInfo() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppid() {
		return this.appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getChargeType() {
		return this.chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getCjxyzk() {
		return this.cjxyzk;
	}

	public void setCjxyzk(String cjxyzk) {
		this.cjxyzk = cjxyzk;
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

	public String getCzdh() {
		return this.czdh;
	}

	public void setCzdh(String czdh) {
		this.czdh = czdh;
	}

	public String getDwfzr() {
		return this.dwfzr;
	}

	public void setDwfzr(String dwfzr) {
		this.dwfzr = dwfzr;
	}

	public String getDzxx() {
		return this.dzxx;
	}

	public void setDzxx(String dzxx) {
		this.dzxx = dzxx;
	}

	public String getGcyxzk() {
		return this.gcyxzk;
	}

	public void setGcyxzk(String gcyxzk) {
		this.gcyxzk = gcyxzk;
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

	public String getJxny() {
		return this.jxny;
	}

	public void setJxny(String jxny) {
		this.jxny = jxny;
	}

	public String getLogo1() {
		return this.logo1;
	}

	public void setLogo1(String logo1) {
		this.logo1 = logo1;
	}

	public String getLogo2() {
		return this.logo2;
	}

	public void setLogo2(String logo2) {
		this.logo2 = logo2;
	}

	public String getLogo3() {
		return this.logo3;
	}

	public void setLogo3(String logo3) {
		this.logo3 = logo3;
	}

	public String getLsyg() {
		return this.lsyg;
	}

	public void setLsyg(String lsyg) {
		this.lsyg = lsyg;
	}

	public String getLxdh() {
		return this.lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo1() {
		return this.memo1;
	}

	public void setMemo1(String memo1) {
		this.memo1 = memo1;
	}

	public String getMemo2() {
		return this.memo2;
	}

	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}

	public String getMemo3() {
		return this.memo3;
	}

	public void setMemo3(String memo3) {
		this.memo3 = memo3;
	}

	public BigDecimal getOneFee() {
		return this.oneFee;
	}

	public void setOneFee(BigDecimal oneFee) {
		this.oneFee = oneFee;
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

	public String getWlxyzk() {
		return this.wlxyzk;
	}

	public void setWlxyzk(String wlxyzk) {
		this.wlxyzk = wlxyzk;
	}

	public Double getXkmls() {
		return xkmls;
	}

	public void setXkmls(Double xkmls) {
		this.xkmls = xkmls;
	}

	public String getXqr() {
		return this.xqr;
	}

	public void setXqr(String xqr) {
		this.xqr = xqr;
	}

	public String getXxbxlxm() {
		return this.xxbxlxm;
	}

	public void setXxbxlxm(String xxbxlxm) {
		this.xxbxlxm = xxbxlxm;
	}

	public String getXxdm() {
		return this.xxdm;
	}

	public void setXxdm(String xxdm) {
		this.xxdm = xxdm;
	}

	public String getXxdz() {
		return this.xxdz;
	}

	public void setXxdz(String xxdz) {
		this.xxdz = xxdz;
	}

	public String getXxjbzm() {
		return this.xxjbzm;
	}

	public void setXxjbzm(String xxjbzm) {
		this.xxjbzm = xxjbzm;
	}

	public String getXxmc() {
		return this.xxmc;
	}

	public void setXxmc(String xxmc) {
		this.xxmc = xxmc;
	}

	public String getXxqhm() {
		return this.xxqhm;
	}

	public void setXxqhm(String xxqhm) {
		this.xxqhm = xxqhm;
	}

	public String getXxxz() {
		return this.xxxz;
	}

	public void setXxxz(String xxxz) {
		this.xxxz = xxxz;
	}

	public String getXxxzm() {
		return this.xxxzm;
	}

	public void setXxxzm(String xxxzm) {
		this.xxxzm = xxxzm;
	}

	public String getXxywmc() {
		return this.xxywmc;
	}

	public void setXxywmc(String xxywmc) {
		this.xxywmc = xxywmc;
	}

	public String getXxzgbmm() {
		return this.xxzgbmm;
	}

	public void setXxzgbmm(String xxzgbmm) {
		this.xxzgbmm = xxzgbmm;
	}

	public String getXxzgbmmc() {
		return this.xxzgbmmc;
	}

	public void setXxzgbmmc(String xxzgbmmc) {
		this.xxzgbmmc = xxzgbmmc;
	}

	public String getYjsyzk() {
		return this.yjsyzk;
	}

	public void setYjsyzk(String yjsyzk) {
		this.yjsyzk = yjsyzk;
	}

	public String getYzbm() {
		return this.yzbm;
	}

	public void setYzbm(String yzbm) {
		this.yzbm = yzbm;
	}

	public String getZdyxzk() {
		return this.zdyxzk;
	}

	public void setZdyxzk(String zdyxzk) {
		this.zdyxzk = zdyxzk;
	}

	public String getZydz() {
		return this.zydz;
	}

	public void setZydz(String zydz) {
		this.zydz = zydz;
	}

	public String getZzjgdm() {
		return this.zzjgdm;
	}

	public void setZzjgdm(String zzjgdm) {
		this.zzjgdm = zzjgdm;
	}

	public GjtOrg getGjtOrg() {
		return gjtOrg;
	}

	public void setGjtOrg(GjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}

	public String getLinkTel() {
		return linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getLinkMail() {
		return linkMail;
	}

	public void setLinkMail(String linkMail) {
		this.linkMail = linkMail;
	}

	public List<GjtSetOrgCopyright> getGjtSetOrgCopyrights() {
		return gjtSetOrgCopyrights;
	}

	public void setGjtSetOrgCopyrights(List<GjtSetOrgCopyright> gjtSetOrgCopyrights) {
		this.gjtSetOrgCopyrights = gjtSetOrgCopyrights;
	}

}