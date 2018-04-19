package com.ouchgzee.study.web.vo;

import java.io.Serializable;

/**
 * 登录的命名空间版权信息 Created by Hyf on 2017/6/29.
 */
public class LoginNamespaceCopyright implements Serializable {

	private static final long serialVersionUID = 1L;

	private String loginNamespace; // 命名空间

	private String loginTitle; // 标题

	private String loginHeadLogo; // 登录logo

	private String homeHeadLogo; // 主页LOGO

	private String loginBackground;// 登录背景

	private String homeFooterCopyright; // 首页底部版权

	private String loginFooterCopyright; // 登录页面底部版权

	private String qcodePic;// 二维码下载地址

	public LoginNamespaceCopyright() {
	}

	public LoginNamespaceCopyright(String loginNamespace) {
		this.loginNamespace = loginNamespace;
	}

	public LoginNamespaceCopyright(String loginNamespace, String namespaceName) {
		this.loginNamespace = loginNamespace;
		this.loginTitle = namespaceName;
	}

	public String getQcodePic() {
		return qcodePic;
	}

	public void setQcodePic(String qcodePic) {
		this.qcodePic = qcodePic;
	}

	public String getLoginNamespace() {
		return loginNamespace;
	}

	public void setLoginNamespace(String loginNamespace) {
		this.loginNamespace = loginNamespace;
	}

	public String getLoginTitle() {
		return loginTitle;
	}

	public void setLoginTitle(String loginTitle) {
		this.loginTitle = loginTitle;
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

	public String getLoginBackground() {
		return loginBackground;
	}

	public void setLoginBackground(String loginBackground) {
		this.loginBackground = loginBackground;
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

}
