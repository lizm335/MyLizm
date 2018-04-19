package com.gzedu.xlims.web.controller.personal;
/**
 * 个人资料管理-个人资料
 * @author lyj
 * @time 2017年5月5日 
 * TODO
 */
public class PersonalVo {
	// 帐号id
	private String id;
	// 平台帐号
	private String account;
	// 院校
	private String orgName;
	// 角色
	private String roleName;
	// 真实性名
	private String realName;
	// 手机
	private String mobile;
	// 联系电话
	private String phone;
	// email
	private String email;
	// 密码
	private String pwd;
	// 头像地址
	private String avatar;
	// 签名
	private String signPhoto;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getSignPhoto() {
		return signPhoto;
	}
	public void setSignPhoto(String signPhoto) {
		this.signPhoto = signPhoto;
	}

}
