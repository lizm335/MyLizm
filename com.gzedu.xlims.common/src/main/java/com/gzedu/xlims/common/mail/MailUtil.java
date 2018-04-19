//package com.gzedu.xlims.common.mail;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.time.DateFormatUtils;
//
//import javax.activation.DataHandler;
//import javax.activation.FileDataSource;
//import javax.mail.*;
//import javax.mail.internet.*;
//import java.io.File;
//import java.io.UnsupportedEncodingException;
//import java.net.URL;
//import java.util.Date;
//import java.util.Properties;
//import java.util.Vector;
//
///**
// * <p>
// * 邮件发送类
// * </p>
// * <p>
// * 支持普通模式和HTML模式，可发送多个附件，支持SMTP服务器认证。<br>
// * 基于javamail开发，使用时请将javamail包含在classpath系统变量中。
// * </p>
// * <p>
// * <br>
// * 使用说明：
// * </p>
// * <p>
// * Mail mail=new Mail();
// * </p>
// * <p>
// * mail.setXXX ....
// * </p>
// * <p>
// * mail.send();<br>
// * </p>
// *
// * @author
// * @version 1.0
// */
//public class MailUtil {
//
//	private Address[] to = null;
//
//	private Address[] cc = null;
//
//	private Address[] bcc = null;
//
//	private String from = "";
//
//	private String title = "";
//
//	private String content = "";
//
//	private String smtpHost = "";
//
//	private int smtpPort = 25;
//
//	private String content_type = MODE_TEXT;
//
//	private String htmlMailDesc = "";
//
//	private String smtpUser = "";
//
//	private String smtpPassword = "";
//
//	private boolean isAuthenticationSMTP = false;
//
//	private Vector vFiles = new Vector();
//
//	private Vector vURLs = new Vector();
//
//	/**
//	 * 普通模式
//	 */
//	public static final String MODE_TEXT = "text/plain;charset=gb2312";
//
//	/**
//	 * HTML模式
//	 */
//	public static final String MODE_HTML = "text/html;charset=gb2312";
//
//	final static String mailSMTP = "smtp.126.com";
//	final static public String teacherMailUsername = "gulaoshieezyjy";
//	final static public String teacherMailPassword = "eezyjy";
//	final static public String assistantMailUsername = "zhanglaoshieezyjy";
//	final static public String assistantMailPassword = "eezyjy";
//
//	public MailUtil() {
//	}
//
//	/**
//	 * 设置SMTP服务器，使用默认端口
//	 *
//	 * @param server
//	 *            SMTP服务器IP
//	 */
//	public void setSmtpHost(String server) {
//		this.smtpHost = server;
//	}
//
//	/**
//	 * 设置SMTP服务器
//	 *
//	 * @param server
//	 *            SMTP服务器IP
//	 * @param port
//	 *            端口
//	 */
//	public void setSmtpHost(String server, int port) {
//		this.smtpHost = server;
//		this.smtpPort = port;
//	}
//
//	/**
//	 * 设置收件人地址
//	 *
//	 * @param aEmail
//	 *            收件人Email地址
//	 */
//	public void setTo(String aEmail) {
//		String[] s = new String[1];
//		s[0] = aEmail;
//		this.to = getAddress(s);
//	}
//
//	/**
//	 * 设置多个收件人地址
//	 *
//	 * @param Emails
//	 *            收件人Email地址
//	 */
//	public void setTo(String[] Emails) {
//		this.to = getAddress(Emails);
//	}
//
//	/**
//	 * 设置抄送地址
//	 *
//	 * @param aEmail
//	 *            抄送地址
//	 */
//	public void setCC(String aEmail) {
//		String[] s = new String[1];
//		s[0] = aEmail;
//		this.cc = getAddress(s);
//	}
//
//	/**
//	 * 设置多个抄送地址
//	 *
//	 * @param Emails
//	 *            抄送地址
//	 */
//	public void setCC(String[] Emails) {
//		this.cc = getAddress(Emails);
//	}
//
//	/**
//	 * 设置暗送地址
//	 *
//	 * @param aEmail
//	 *            暗送地址
//	 */
//
//	public void setBCC(String aEmail) {
//		String[] s = new String[1];
//		s[0] = aEmail;
//		this.bcc = getAddress(s);
//	}
//
//	/**
//	 * 设置多个暗送地址
//	 *
//	 * @param Emails
//	 *            暗送地址
//	 */
//	public void setBCC(String[] Emails) {
//		this.bcc = getAddress(Emails);
//	}
//
//	/**
//	 * 设置发件人地址
//	 *
//	 * @param aEmail
//	 *            发件人地址
//	 */
//	public void setFrom(String aEmail) {
//		// if(!isValidEmailAddress(aEmail)){
//		// throw new MyException("Invalid Email Address");
//		// }
//		this.from = aEmail;
//	}
//
//	/**
//	 * 设置邮件主题
//	 *
//	 * @param mailTitle
//	 *            邮件主题
//	 */
//	public void setSubject(String mailTitle) {
//		this.title = mailTitle;
//	}
//
//	/**
//	 * 设置邮件文字内容
//	 *
//	 * @param mailContent
//	 *            邮件文字内容
//	 */
//	public void setBody(String mailContent) {
//		this.content = mailContent;
//	}
//
//	/**
//	 * 设置邮件字符类型
//	 *
//	 * @param contentType
//	 *            请从静态变量TEXT和HTML中选择
//	 */
//	public void setContentType(String contentType) {
//		this.content_type = contentType;
//	}
//
//	/**
//	 * 设置HTML格式邮件在一般模式下显示的说明
//	 *
//	 * @param desc
//	 *            说明文字
//	 */
//	public void setHtmlMailDesc(String desc) {
//		this.htmlMailDesc = desc;
//	}
//
//	/**
//	 * 设置SMTP服务器用户认证
//	 *
//	 * @param username
//	 *            用户名
//	 * @param password
//	 *            密码
//	 */
//	public void setSmtpAuthentication(String username, String password) {
//		this.smtpUser = username;
//		this.smtpPassword = password;
//		this.isAuthenticationSMTP = true;
//	}
//
//	/**
//	 * 添加附件
//	 *
//	 * @param afile
//	 *            本地文件
//	 */
//	public void addAttachment(File afile) {
//		vFiles.add(afile);
//	}
//
//	/**
//	 * 添加附件
//	 *
//	 * @param fileURL
//	 *            文件URL
//	 */
//	public void addAttachment(URL fileURL) {
//		vURLs.add(fileURL);
//	}
//
//	/**
//	 * 标示邮件是否附带附件
//	 *
//	 * @return
//	 */
//	public boolean hasAttachment() {
//		return vFiles.size() + vURLs.size() > 0;
//	}
//
//	/**
//	 * 发送邮件
//	 */
//	public int send() {
//
//		int ret = 1;
//		try {
//			Properties server = new Properties();
//			if (StringUtils.isEmpty(this.smtpHost)) {
//				throw new NullPointerException("Please set SMTP host");
//			} else {
//				server.put("mail.smtp.host", this.smtpHost);
//			}
//			server.put("mail.smtp.port", String.valueOf(this.smtpPort));
//			if (this.isAuthenticationSMTP) {
//				server.put("mail.smtp.auth", "true");
//			}
//
//			Session conn = Session.getInstance(server, null);
//
//			MimeMessage msg = new MimeMessage(conn);
//			if (StringUtils.isEmpty(this.from)) {
//				throw new NullPointerException("Please set FROM address");
//			} else {
//				msg.setFrom(new InternetAddress(this.to_ISO8859_1(this.from)));
//			}
//			if (this.to != null) {
//				msg.setRecipients(Message.RecipientType.TO, this.to);
//			}
//			// else {
//			// throw new NullPointerException("Please set TO address");
//			// }
//			if (this.cc != null) {
//				msg.setRecipients(Message.RecipientType.CC, this.cc);
//			}
//			if (this.bcc != null) {
//				msg.setRecipients(Message.RecipientType.BCC, this.bcc);
//			}
//
//			sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
////			msg.setSubject("=?GB2312?B?" + enc.encode(this.title.getBytes())
////					+ "?=");
//			msg.setSubject(this.title);
//
//			if (!hasAttachment()) {
//				// 如果没有带附件
//				if (this.isHtmlModeMail()) {
//					// 是HTML格式的邮件
//					if (!this.hasHtmlDesc()) {
//						msg.setContent(this.content, this.content_type);
//					} else {
//						Multipart mp = new MimeMultipart();
//						MimeBodyPart mbp = null;
//
//						mbp = new MimeBodyPart();
//						mbp.setContent(this.content, this.content_type);
//						mp.addBodyPart(mbp);
//
//						mbp = new MimeBodyPart();
//						mbp.setContent(this.htmlMailDesc, this.MODE_TEXT);
//						mp.addBodyPart(mbp);
//
//						msg.setContent(mp);
//					}
//				} else {
//					// 是文本格式的邮件
//					msg.setText(this.content);
//				}
//
//			} else {
//				// 有附件
//				Multipart mp = new MimeMultipart();
//				MimeBodyPart mbp = null;
//				// 邮件正文
//				for (int i = 0; i < vFiles.size(); i++) {
//					mbp = new MimeBodyPart();
//					File file = (File) vFiles.get(i);
//					FileDataSource fds = new FileDataSource(file);
//					mbp.setDataHandler(new DataHandler(fds));
//					mbp.setFileName(to_ISO8859_1(file.getName()));
//					mp.addBodyPart(mbp);
//				}
//				for (int i = 0; i < vURLs.size(); i++) {
//					mbp = new MimeBodyPart();
//					URL url = (URL) vURLs.get(i);
//					// URLDataSource uds=new URLDataSource(url);
//					mbp.setDataHandler(new DataHandler(url));
//					mbp.setFileName(to_ISO8859_1(url.getFile()));
//					mp.addBodyPart(mbp);
//				}
//
//				mbp = new MimeBodyPart();
//				mbp.setContent(this.content, this.content_type);
//				mp.addBodyPart(mbp);
//
//				if (this.isHtmlModeMail() && this.hasHtmlDesc()) {
//					mbp = new MimeBodyPart();
//					mbp.setContent(this.htmlMailDesc, this.MODE_TEXT);
//					mp.addBodyPart(mbp);
//				}
//
//				msg.setContent(mp);
//			}
//			msg.saveChanges();
//			if (this.isAuthenticationSMTP) {
//				Transport transport = conn.getTransport("smtp");
//				transport.connect(this.smtpHost, this.smtpUser,
//						this.smtpPassword);
//				transport.sendMessage(msg, msg.getAllRecipients());
//				transport.close();
//			} else {
//				Transport.send(msg, msg.getAllRecipients());
//			}
//
//		} catch (javax.mail.internet.AddressException e) {
//			e.printStackTrace();
//			ret = 0;
//		} catch (javax.mail.MessagingException e) {
//			e.printStackTrace();
//			ret = 0;
//		}
//		return ret;
//	}
//
//	public boolean isValidEmailAddress(String email) {
//		if (StringUtils.isEmpty(email))
//			return false;
//		if (email.indexOf("@") > 0)
//			return !email.endsWith("@");
//		return false;
//	}
//
//	private Address[] getAddress(String[] add) {
//		Address[] a = new Address[add.length];
//		for (int i = 0; i < add.length; i++) {
//			try {
//				a[i] = new InternetAddress(add[i]);
//			} catch (AddressException ex) {
//				ex.printStackTrace();
//			}
//		}
//		return a;
//	}
//
//	public boolean isHtmlModeMail() {
//		return this.content_type.equals(this.MODE_HTML);
//	}
//
//	public boolean hasHtmlDesc() {
//		if (!this.isHtmlModeMail())
//			return false;
//		return !StringUtils.isEmpty(this.htmlMailDesc);
//	}
//
//	public String to_ISO8859_1(String name) {
//		if (name != null && !"".equals(name)) {
//			try {
//				return new String(name.getBytes("GBK"), "iso-8859-1");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return "";
//	}
//
//	/*public static int sendMail(String[] tomail, String content, int type)
//			throws Exception {
//		MailUtil mail = new MailUtil();
//		try {
//			mail.setSmtpHost(AppConfig.getProperty("mailSMTP"));
//			if (type == 1) {
//				mail.setSmtpAuthentication(AppConfig.getProperty("fdMail")
//						.split("@")[0], AppConfig.getProperty("fdPSD"));
//			} else if (type == 2) {
//				mail.setSmtpAuthentication(AppConfig.getProperty("bzrMail")
//						.split("@")[0], AppConfig.getProperty("bzrPSD"));
//			}
//			// mail.setCC(tomail);
//			if (type == 1) {
//				mail.setFrom(AppConfig.getProperty("fdMail"));
//			} else if (type == 2) {
//				mail.setFrom(AppConfig.getProperty("bzrMail"));
//			}
//
//			mail.setBCC(tomail);
//
//			mail.setContentType("TEXT");
//
//			if (type == 1) {
//				mail.setSubject("辅导老师发送邮件提醒");
//			} else if (type == 2) {
//				mail.setSubject("班主任老师发送邮件提醒");
//			}
//			// mail.setSubject(subject);
//			mail.setBody(content);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		int ret = mail.send();
//
//		System.out.println("发送邮件返回值：" + ret);
//
//		return ret;
//	}*/
//
//    public static boolean sendMailInter(String[] Emails,String mailContent,String mailSubject,
//    		  String sendMailUser,String sendUserMailPW){
//    	if(StringUtils.isBlank(sendMailUser)
//    			||StringUtils.isBlank(sendUserMailPW)
//    			||sendMailUser.indexOf("@")==-1){
//    		return false;
//    	}
//    	MailUtil mail = new MailUtil();
//		mail.setSmtpHost("smtp.gzedu.net");
//		mail.setSmtpAuthentication(sendMailUser.split("@")[0], sendUserMailPW);
//		mail.setFrom("国开管理员"+"<"+sendMailUser+">");
//		mail.setBCC(Emails);
//		mail.setContentType("TEXT");
//		mail.setSubject(mailSubject);
//		mail.setBody(mailContent);
//		int ret = mail.send();
//		return ret == 1;
//    }
//
//    public static boolean sendMailInter(String[] Emails,String mailContent,String mailSubject,
//  		  String sendMailUser,String sendUserMailPW,String contentType){
//	  	if(StringUtils.isBlank(sendMailUser)
//	  			||StringUtils.isBlank(sendUserMailPW)
//	  			||sendMailUser.indexOf("@")==-1){
//	  		return false;
//	  	}
//	  	MailUtil mail = new MailUtil();
//		mail.setSmtpHost("smtp.gzedu.net");
//		mail.setSmtpAuthentication(sendMailUser.split("@")[0], sendUserMailPW);
//		mail.setFrom("国开管理员"+"<"+sendMailUser+">");
//		mail.setBCC(Emails);
//		mail.setContentType(contentType);
//		mail.setSubject(mailSubject);
//		mail.setBody(mailContent);
//		int ret = mail.send();
//		return ret == 1;
//    }
//
//    /*邮件主机地址*/
//	public static final String EMAILHOST = "smtp.gzedu.net";
//
//	/*广州国开在线邮件账号*/
//	public static final String EMAILUSER = "gkzx@gzedu.net";
//
//	/*广州国开在线邮件账号密码*/
//	public static final String EMAILPASS = "888888";
//
//    public static boolean sendMailSignleAttachment(String[] Emails, File afile) {
//    	MailUtil mail = new MailUtil();
//		mail.setSmtpHost(EMAILHOST);
//		mail.setSmtpAuthentication(EMAILUSER.split("@")[0], EMAILPASS);
//		mail.setFrom("广州国开在线<"+EMAILUSER+">");
//		mail.setBCC(Emails);
//		mail.setSubject("");
//		mail.addAttachment(afile);
//		int ret = mail.send();
//		return ret == 1;
//    }
//
//	public static void main(String[] args) {
//		System.out.println(MailUtil.sendMailInter(new String[]{"1127685169@qq.com"},
//				String.format("<table style='border: solid 1px;'><tbody><tr><td>时间：</td><td>%s</td></tr><tr><td>账号：</td><td>%s</td></tr><tr><td>失败原因：</td><td>%s</td></tr></tbody></table><br/>请使用管理员账号登录教学教务后台手动同步！",
//						DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"),
//						"43102111111111",
//						"{code:500}"
//				), "账号同步至运营平台失败通知",
//				MailUtil.EMAILUSER, MailUtil.EMAILPASS, MailUtil.MODE_HTML));
//	}
//
//}
