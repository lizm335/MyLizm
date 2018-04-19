package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;

/**
 * The persistent class for the GJT_SET_ORG_COPYRIGHT database table.
 * 
 */
@Entity
@Table(name = "GJT_SET_ORG_COPYRIGHT")
// @NamedQuery(name = "GjtSetOrgCopyright.findAll", query = "SELECT g FROM GjtSetOrgCopyright g")
@Deprecated public class BzrGjtSetOrgCopyright implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "XX_ID")
	private String xxId;

	@Column(name = "HOME_FOOTER_COPYRIGHT")
	private String homeFooterCopyright;// 首页底部版权

	@Column(name = "LOGIN_FOOTER_COPYRIGHT")
	private String loginFooterCopyright;// 登录页面底部版权

	@Column(name = "LOGIN_HEAD_LOGO")
	private String loginHeadLogo;// 登录头部LOGO

	@Column(name = "HOME_HEAD_LOGO")
	private String homeHeadLogo;// 首页头部LOGO

	@Column(name = "IS_DELETED")
	@Where(clause = "isDeleted='N'")
	private String isDeleted;// 是否删除标识

	@Column(name = "LOGIN_BACKGROUND")
	private String loginBackground;// 登录背景

	@Column(name = "LOGIN_TITLE")
	private String loginTitle;// 登录标题

	@Column(name = "PLATFROM_TYPE")
	private String platfromType;// 平台类型

	@Column(name = "MEMO")
	private String memo;// 备注

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION")
	private Integer version;

	@Column(name = "SCHOOL_MODEL")
	private String schoolModel;

	@Column(name = "SCHOOL_REALM_NAME")
	private String schoolRealmName;

	@Column(name = "PLATFORM_NAME")
	private String platformName;

	@Column(name = "QCODE_PIC")
	private String qcodePic;

	public BzrGjtSetOrgCopyright(String xx_id, int platfromType) {
		this.id = UUIDUtils.random().toString();
		this.xxId = xx_id;
		this.platfromType = String.valueOf(platfromType);
		this.createdDt = DateUtils.getNowTime();
		this.createdBy = "admin";
		this.isDeleted = "N";
		this.version = 3;
		this.schoolModel = "0";
	}

	public String getQcodePic() {
		return qcodePic;
	}

	public void setQcodePic(String qcodePic) {
		this.qcodePic = qcodePic;
	}

	public String getSchoolRealmName() {
		return schoolRealmName;
	}

	public void setSchoolRealmName(String schoolRealmName) {
		this.schoolRealmName = schoolRealmName;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public BzrGjtSetOrgCopyright() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getHomeFooterCopyright() {
		return homeFooterCopyright;
	}

	public void setHomeFooterCopyright(String homeFooterCopyright) {
		this.homeFooterCopyright = homeFooterCopyright;
	}

	public String getLoginFooterCopyright() {
		return loginFooterCopyright;
	}

	public void setLoginFooterCopyright(String loginFooterCopyright) {
		this.loginFooterCopyright = loginFooterCopyright;
	}

	public String getLoginHeadLogo() {
		return loginHeadLogo;
	}

	public void setLoginHeadLogo(String loginHeadLogo) {
		this.loginHeadLogo = loginHeadLogo;
	}

	public String getHomeHeadLogo() {
		return homeHeadLogo;
	}

	public void setHomeHeadLogo(String homeHeadLogo) {
		this.homeHeadLogo = homeHeadLogo;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getLoginBackground() {
		return loginBackground;
	}

	public void setLoginBackground(String loginBackground) {
		this.loginBackground = loginBackground;
	}

	public String getLoginTitle() {
		return loginTitle;
	}

	public void setLoginTitle(String loginTitle) {
		this.loginTitle = loginTitle;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPlatfromType() {
		return platfromType;
	}

	public void setPlatfromType(String platfromType) {
		this.platfromType = platfromType;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getSchoolModel() {
		return schoolModel;
	}

	public void setSchoolModel(String schoolModel) {
		this.schoolModel = schoolModel;
	}
}