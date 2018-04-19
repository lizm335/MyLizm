package com.gzedu.xlims.common.mail.service;

import com.gzedu.xlims.common.mail.domain.MailSenderInfo;
import com.gzedu.xlims.common.mail.domain.MyAuthenticator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

/**
 * 简单邮件（不带附件的邮件）发送器
 * @author fly
 */
public class SimpleMailSender {
	private static final Log log = LogFactory.getLog(SimpleMailSender.class);

	/**
	 * 检测邮箱服务器的连接
	 * @param mailInfo
	 * @return
	 */
	public static boolean validateConnect(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session

		Session session = Session.getDefaultInstance(pro, authenticator);
		try {
			// protocol(协议) pop3 imap gimap
			Store store = session.getStore("pop3");
			store.connect(mailInfo.getMailServerHost(), mailInfo.getUserName(), mailInfo.getPassword());
			return true;
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			log.error("function validateConnect fail ================== " + e.getMessage());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			log.error("function validateConnect fail ================== " + e.getMessage());
		}
		return false;
	}

	/**
	 * 以文本格式发送邮件
	 *
	 * @param mailInfo
	 *            待发送的邮件的信息
	 */
	public static boolean sendTextMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			// Message.RecipientType.TO属性表示接收者的类型为TO，CC为抄送，BCC为密送
			Address[] to = new InternetAddress[mailInfo.getToAddress().length];
			for (int i = 0; i < mailInfo.getToAddress().length; i++) {
				to[i] = new InternetAddress(mailInfo.getToAddress()[i]);
			}
			mailMessage.setRecipients(Message.RecipientType.TO, to);
			if(mailInfo.getCcAddress() != null && mailInfo.getCcAddress().length != 0) {
				Address[] cc = new InternetAddress[mailInfo.getCcAddress().length];
				for (int i = 0; i < mailInfo.getCcAddress().length; i++) {
					cc[i] = new InternetAddress(mailInfo.getCcAddress()[i]);
				}
				mailMessage.setRecipients(Message.RecipientType.CC, cc);
			}
			if(mailInfo.getBccAddress() != null && mailInfo.getBccAddress().length != 0) {
				Address[] bcc = new InternetAddress[mailInfo.getBccAddress().length];
				for (int i = 0; i < mailInfo.getBccAddress().length; i++) {
					bcc[i] = new InternetAddress(mailInfo.getBccAddress()[i]);
				}
				mailMessage.setRecipients(Message.RecipientType.BCC, bcc);
			}
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 发送复杂的邮件，以HTML格式发送邮件，可带附件
	 *
	 * @param mailInfo
	 *            待发送的邮件信息
	 * @return 是否发送成功
	 */
	public static boolean sendMultipartMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			// Message.RecipientType.TO属性表示接收者的类型为TO，CC为抄送，BCC为密送
			Address[] to = new InternetAddress[mailInfo.getToAddress().length];
			for (int i = 0; i < mailInfo.getToAddress().length; i++) {
				to[i] = new InternetAddress(mailInfo.getToAddress()[i]);
			}
			mailMessage.setRecipients(Message.RecipientType.TO, to);
			if(mailInfo.getCcAddress() != null && mailInfo.getCcAddress().length != 0) {
				Address[] cc = new InternetAddress[mailInfo.getCcAddress().length];
				for (int i = 0; i < mailInfo.getCcAddress().length; i++) {
					cc[i] = new InternetAddress(mailInfo.getCcAddress()[i]);
				}
				mailMessage.setRecipients(Message.RecipientType.CC, cc);
			}
			if(mailInfo.getBccAddress() != null && mailInfo.getBccAddress().length != 0) {
				Address[] bcc = new InternetAddress[mailInfo.getBccAddress().length];
				for (int i = 0; i < mailInfo.getBccAddress().length; i++) {
					bcc[i] = new InternetAddress(mailInfo.getBccAddress()[i]);
				}
				mailMessage.setRecipients(Message.RecipientType.BCC, bcc);
			}
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 附件
			for (File f : mailInfo.getAttachFiles()) {
				BodyPart mbp = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(f);
				mbp.setDataHandler(new DataHandler(fds));
				try {
					mbp.setFileName(new String(f.getName().getBytes("GBK"), "iso-8859-1"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				mainPart.addBodyPart(mbp);
			}
			for (URL u : mailInfo.getAttachUrls()) {
				BodyPart mbp = new MimeBodyPart();
				URLDataSource uds = new URLDataSource(u);
				mbp.setDataHandler(new DataHandler(uds));
				try {
					mbp.setFileName(new String(u.getFile().getBytes("GBK"), "iso-8859-1"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				mainPart.addBodyPart(mbp);
			}
			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			System.err.println(ex.getMessage());
		}
		return false;
	}
}