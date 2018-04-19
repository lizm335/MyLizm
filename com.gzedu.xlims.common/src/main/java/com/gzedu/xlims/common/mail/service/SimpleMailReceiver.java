package com.gzedu.xlims.common.mail.service;

import com.gzedu.xlims.common.mail.domain.MailSenderInfo;
import com.gzedu.xlims.common.mail.domain.MyAuthenticator;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 简单邮件（不带附件的邮件）接收器
 * 
 * @author fly
 */
public class SimpleMailReceiver {
	/**
	 * 接收邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 */
	public static List<MailSenderInfo> receiveMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session session = Session.getDefaultInstance(pro, authenticator);
		try {
			// protocol(协议) pop3 imap gimap
			Store store = session.getStore("pop3");
			store.connect(mailInfo.getMailServerHost(), mailInfo.getUserName(),
					mailInfo.getPassword());
			// 获取默认文件夹
			Folder folder = store.getDefaultFolder();
			if (folder == null)
				throw new Exception("No POP3 INBOX");
			// 如果是收件箱
			folder = folder.getFolder("INBOX");
			if (folder == null)
				throw new Exception("No POP3 INBOX");
			// 使用只读方式打开收件箱
			folder.open(Folder.READ_WRITE);
			// 得到文件夹信息，获取邮件列表
			Message[] msgs = folder.getMessages();
			// 获取所有邮件信息
			List<MailSenderInfo> mailInfos = new ArrayList<MailSenderInfo>();
			for (int msgNum = 0; msgNum < msgs.length; msgNum++) {
				mailInfos.add(getMessage(msgs[msgNum]));
			}
			// 参数表示是否在删除操作邮件后更新Folder
			folder.close(true);
			// 关闭store
			store.close();
			return mailInfos;
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		return null;
	}

	/**
	 * 封装邮件信息
	 * 
	 * @param message
	 */
	private static MailSenderInfo getMessage(Message message) {
		MailSenderInfo mailInfo = new MailSenderInfo();
		try {
			// 获得发送邮件地址
			String from = ((InternetAddress) message.getFrom()[0])
					.getPersonal();
			if (from == null)
				from = ((InternetAddress) message.getFrom()[0]).getAddress();
			mailInfo.setFromAddress(from);
			// 获取主题
			String subject = MimeUtility.decodeText(message.getSubject());
			mailInfo.setSubject(subject);
			// 获取内容
			StringBuffer bodytext = new StringBuffer();
			mailInfo.setContent(getMailContent(message, bodytext).toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mailInfo;
	}

	/**
	 * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
	 */
	private static StringBuffer getMailContent(Part part, StringBuffer bodytext) throws Exception {
		String contenttype = part.getContentType();
		int nameindex = contenttype.indexOf("name");
		boolean conname = false;
		if (nameindex != -1)
			conname = true;
		if (part.isMimeType("text/plain") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("text/html") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				getMailContent(multipart.getBodyPart(i), bodytext);
			}
		} else if (part.isMimeType("message/rfc822")) {
			getMailContent((Part) part.getContent(), bodytext);
		} else {
		}
		return bodytext;
	}
}