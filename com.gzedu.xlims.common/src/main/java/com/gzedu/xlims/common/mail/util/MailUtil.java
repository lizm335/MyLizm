package com.gzedu.xlims.common.mail.util;

import com.gzedu.xlims.common.mail.domain.MailSenderInfo;
import com.gzedu.xlims.common.mail.service.SimpleMailSender;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class MailUtil {

	public static boolean sendTextMail(String[] to, String subject, String content) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.gzedu.net");
//		mailInfo.setMailServerPort("465"); // SSL加密端口
		mailInfo.setValidate(true);
		mailInfo.setUserName("gkzx@gzedu.net");
		mailInfo.setPassword("888888");// 您的邮箱密码
		try {
			mailInfo.setFromAddress(new String("国开管理员".getBytes("GBK"), "iso-8859-1") + " <gkzx@gzedu.net>");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		mailInfo.setToAddress(to);
		mailInfo.setSubject(subject);
		mailInfo.setContent(content);
		return SimpleMailSender.sendTextMail(mailInfo);// 发送文体格式
	}

	public static boolean sendHtmlMail(String[] to, String subject, String content) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.gzedu.net");
//		mailInfo.setMailServerPort("465"); // SSL加密端口
		mailInfo.setValidate(true);
		mailInfo.setUserName("gkzx@gzedu.net");
		mailInfo.setPassword("888888");// 您的邮箱密码
		try {
			mailInfo.setFromAddress(new String("国开管理员".getBytes("GBK"), "iso-8859-1") + " <gkzx@gzedu.net>");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		mailInfo.setToAddress(to);
		mailInfo.setSubject(subject);
		mailInfo.setContent(content);
		return SimpleMailSender.sendMultipartMail(mailInfo);// 发送html格式
	}

	public static boolean sendAttachMail(String[] to, String subject, File...files) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.gzedu.net");
//		mailInfo.setMailServerPort("465"); // SSL加密端口
		mailInfo.setValidate(true);
		mailInfo.setUserName("gkzx@gzedu.net");
		mailInfo.setPassword("888888");// 您的邮箱密码
		try {
			mailInfo.setFromAddress(new String("国开管理员".getBytes("GBK"), "iso-8859-1") + " <gkzx@gzedu.net>");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		mailInfo.setToAddress(to);
		mailInfo.setSubject(subject);
		for (File f: files) {
			mailInfo.addAttachFiles(f);
		}
		return SimpleMailSender.sendMultipartMail(mailInfo);// 发送html格式
	}

	public static void main(String[] args) {
//		boolean flag = MailUtil.sendMail(new String[]{"1127685169@qq.com"}, "Title", "Hi, Welcome to <font color=\"#FF0000\">Guangzhou，欢迎您！</font>");
//		System.out.println("邮件已发送完成" + flag);
//		boolean flag = MailUtil.sendHtmlMail(new String[]{"1127685169@qq.com"}, "Title", "Hi, Welcome to <font color=\"#FF0000\">Guangzhou，欢迎您！</font>");
//		System.out.println("邮件已发送完成" + flag);
		boolean flag = MailUtil.sendAttachMail(new String[]{"1127685169@qq.com"}, "模板文件", new File("D:\\logo.png"), new File("D:\\zxing - 副本.png"));
		System.out.println("邮件已发送完成" + flag);
	}

}
