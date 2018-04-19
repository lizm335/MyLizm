package com.gzedu.xlims.common.mail.domain;

import java.io.File;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 发送邮件需要使用的基本信息
 */
public class MailSenderInfo {
	// 发送邮件的服务器的IP和端口
	private String mailServerHost;
	private String mailServerPort = "25";
	// 邮件发送者的地址
	private String fromAddress;
	// 邮件接收者的地址
	private String[] toAddress;
	// 邮件抄送者的地址
	private String[] ccAddress;
	// 邮件密送者的地址
	private String[] bccAddress;
	// 登陆邮件发送服务器的用户名和密码
	private String userName;
	private String password;
	// 是否需要身份验证
	private boolean validate = false;
	// 邮件主题
	private String subject = "";
	// 邮件的文本内容
	private String content = "";
	// 邮件附件的文件
	private CopyOnWriteArrayList<File> attachFiles = new CopyOnWriteArrayList<File>();
	// 邮件附件的文件
	private CopyOnWriteArrayList<URL> attachUrls = new CopyOnWriteArrayList<URL>();

	/**
	 * 获得邮件会话属性
	 */
	public Properties getProperties() {
		Properties p = new Properties();
		p.put("mail.smtp.host", this.mailServerHost);
		if("465".equals(this.mailServerPort)) {
			p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			p.setProperty("mail.smtp.socketFactory.fallback", "false");
			p.setProperty("mail.smtp.socketFactory.port", "465");
		}
		p.put("mail.smtp.port", this.mailServerPort);
		p.put("mail.smtp.auth", validate ? "true" : "false");
		return p;
	}

	public String getMailServerHost() {
		return mailServerHost;
	}

	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getToAddress() {
		return toAddress;
	}

	public void setToAddress(String[] toAddress) {
		this.toAddress = toAddress;
	}

	public String[] getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String[] ccAddress) {
		this.ccAddress = ccAddress;
	}

	public String[] getBccAddress() {
		return bccAddress;
	}

	public void setBccAddress(String[] bccAddress) {
		this.bccAddress = bccAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String textContent) {
		this.content = textContent;
	}

	public CopyOnWriteArrayList<File> getAttachFiles() {
		return attachFiles;
	}

	public void setAttachFiles(CopyOnWriteArrayList<File> attachFiles) {
		this.attachFiles = attachFiles;
	}

	public void addAttachFiles(File f) {
		this.attachFiles.add(f);
	}

	public CopyOnWriteArrayList<URL> getAttachUrls() {
		return attachUrls;
	}

	public void setAttachUrls(CopyOnWriteArrayList<URL> attachUrls) {
		this.attachUrls = attachUrls;
	}

	public void addAttachUrls(URL u) {
		this.attachUrls.add(u);
	}
}