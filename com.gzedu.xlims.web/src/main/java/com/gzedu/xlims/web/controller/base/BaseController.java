/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.gzedu.xlims.web.common.login.vo.LoginNamespaceCopyright;

import net.sf.json.JSONObject;

/**
 * 基础控制器<br/>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年06月30日
 * @version 2.5
 * @since JDK 1.7
 */
public abstract class BaseController {

	private static final String LOGIN_NAMESPACE_COPYRIGHT = "LOGIN_NAMESPACE_COPYRIGHT";

	/**
	 * 判断是否为本科
	 * 
	 * @param pycc
	 * @return
	 */
	protected boolean isUndergraduateCourse(String pycc) {
		return "2".equals(pycc) || "8".equals(pycc);
	}

	/**
	 * 版权信息保存到会话(Session)中
	 * @param session
	 * @param copyright
	 */
	protected void setLoginNamespaceCopyright(HttpSession session, LoginNamespaceCopyright copyright) {
		Session shiroSession = SecurityUtils.getSubject().getSession();
		shiroSession.setAttribute(LOGIN_NAMESPACE_COPYRIGHT, copyright);
	}

	/**
	 * 获取当前会话的版权信息
	 * @param session
	 * @return
	 */
	protected LoginNamespaceCopyright getLoginNamespaceCopyright(HttpSession session) {
		Session shiroSession = SecurityUtils.getSubject().getSession();
		LoginNamespaceCopyright copyright = (LoginNamespaceCopyright) shiroSession.getAttribute(LOGIN_NAMESPACE_COPYRIGHT);
		return copyright;
	}

	// /**
	// * 登录用户信息保存到会话(Session)中
	// * @param session
	// * @param employeeInfo
	// */
	// protected void setUser(HttpSession session, GjtEmployeeInfo employeeInfo)
	// {
	// // <!-- 会话有效时长 12h 单位：秒 -->
	// session.setMaxInactiveInterval(43200);
	// session.setAttribute(Servlets.SESSION_EMPLOYEE_NAME, employeeInfo);
	// }
	//
	// /**
	// * 获取当前会话的登录用户信息
	// * @param session
	// * @return
	// */
	// protected GjtEmployeeInfo getUser(HttpSession session) {
	// GjtEmployeeInfo employeeInfo = (GjtEmployeeInfo)
	// session.getAttribute(Servlets.SESSION_EMPLOYEE_NAME);
	// return employeeInfo;
	// }
	//
	// /**
	// * 当前班级信息保存到会话(Session)中
	// * @param session
	// * @param classInfo
	// */
	// protected void setCurrentClass(HttpSession session, GjtClassInfo
	// classInfo) {
	// session.setAttribute(Servlets.SESSION_CURRENT_CLASS_NAME, classInfo);
	// }
	//
	// /**
	// * 获取当前会话的当前班级信息
	// * @param session
	// * @return
	// */
	// protected GjtClassInfo getCurrentClass(HttpSession session) {
	// GjtClassInfo classInfo = (GjtClassInfo)
	// session.getAttribute(Servlets.SESSION_CURRENT_CLASS_NAME);
	// return classInfo;
	// }
	//
	// /**
	// * 获取当前会话的当前班级ID
	// * @param session
	// * @return
	// */
	// protected String getCurrentClassId(HttpSession session) {
	// GjtClassInfo classInfo = getCurrentClass(session);
	// return classInfo != null ? classInfo.getClassId() : null;
	// }

	/**
	 * 结果输出为JSON格式数据
	 * 
	 * @param response
	 * @param obj
	 * @throws IOException
	 */
	protected void outputJsonData(HttpServletResponse response, Object obj) throws IOException {
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JSONObject.fromObject(obj).toString());
		response.getWriter().flush();
	}

	/**
	 * 结果输出为HTML代码
	 * 
	 * @param response
	 * @param html
	 * @throws IOException
	 */
	protected void outputHtml(HttpServletResponse response, String html) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(html);
		response.getWriter().flush();
	}

	/**
	 * 结果输出为JS代码
	 * 
	 * @param response
	 * @param jsCode
	 * @throws IOException
	 */
	protected void outputJs(HttpServletResponse response, String jsCode) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write("<script type='application/javascript'>" + jsCode + "</script>");
		response.getWriter().flush();
	}

	/**
	 * 结果输出为JS代码，先弹出提示框，然后自动关闭窗口
	 * 
	 * @param response
	 * @param msg
	 * @throws IOException
	 */
	protected void outputJsAlertCloseWindow(HttpServletResponse response, String... msg) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write("<script type='application/javascript'>"
				+ (msg.length > 0 ? "alert('" + msg[0] + "');" : "") + "/*自动关闭当前窗口*/window.close();</script>");
		response.getWriter().flush();
	}

	/**
	 * 设置让浏览器弹出下载文件对话框的Header.
	 *
	 * @param response
	 * @param outputFilePath
	 * @throws IOException
	 */
	protected void downloadFile(HttpServletRequest request, HttpServletResponse response, String outputFilePath) throws IOException {
		String outputFileName = null;
		int lastIndex = StringUtils.isNotBlank(outputFilePath) ? outputFilePath.lastIndexOf(File.separator) : -1;
		if (lastIndex != -1) {
			outputFileName = outputFilePath.substring(lastIndex + 1);
			String fileName = com.gzedu.xlims.common.StringUtils.getBrowserStr(request, outputFileName); // 解决下载文件名乱码问题（兼容性）
			response.setContentType("application/x-msdownload;charset=utf-8");
			// 解决IE下载文件名乱码问题
			// response.setHeader("Content-Disposition", "attachment; filename=" + new String(outputFileName.getBytes("GB2312"), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			InputStream in = new FileInputStream(outputFilePath);
			OutputStream out = response.getOutputStream();
			int length = 0;
			byte[] bts = new byte[1024];

			// in.read(bts) 每次读到的数据存放在bts数组中
			while ((length = in.read(bts)) != -1) {
				// 在 bts 数组中取出数据写到（输出流）磁盘上
				out.write(bts, 0, length);
			}
			out.flush();
			out.close();
			in.close();
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("<script type='application/javascript'>/*自动关闭当前窗口*/window.close();</script>");
			response.getWriter().flush();
		}
	}

	/**
	 * 设置让浏览器弹出下载文件对话框的Header.
	 * 
	 * @param response
	 * @param in
	 * @param outputFileName
	 * @throws IOException
	 */
	protected void downloadInputStream(HttpServletResponse response, InputStream in, String outputFileName) throws IOException {
		if (in != null) {
			response.setContentType("application/x-msdownload;charset=utf-8");
			// response.setHeader("Content-Disposition", "attachment; filename="
			// + new String(outputFileName.getBytes("UTF-8"), "ISO8859-1"));
			// 解决IE下载文件名乱码问题
			response.setHeader("Content-Disposition", "attachment; filename=" + new String(outputFileName.getBytes("GB2312"), "ISO8859-1"));

			OutputStream out = response.getOutputStream();
			int length = 0;
			byte[] bts = new byte[1024];

			// in.read(bts) 每次读到的数据存放在bts数组中
			while ((length = in.read(bts)) != -1) {
				// 在 bts 数组中取出数据写到（输出流）磁盘上
				out.write(bts, 0, length);
			}
			out.flush();
			out.close();
			in.close();
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("<script type='application/javascript'>/*自动关闭当前窗口*/window.close();</script>");
			response.getWriter().flush();
		}
	}

	/**
	 * 设置让浏览器弹出下载[Excel文件]对话框的Header.
	 * 
	 * @param response
	 * @param workbook
	 * @param outputFileName
	 * @throws IOException
	 */
	protected void downloadExcelFile(HttpServletRequest request, HttpServletResponse response, Workbook workbook, String outputFileName)
			throws IOException {
		if (workbook != null) {
			response.setContentType("application/x-msdownload;charset=utf-8");
			// response.setHeader("Content-Disposition", "attachment; filename=" + new String(outputFileName.getBytes("UTF-8"), "ISO8859-1"));
			// 解决IE下载文件名乱码问题
//			response.setHeader("Content-Disposition", "attachment; filename=" + new String(outputFileName.getBytes("GB2312"), "ISO8859-1"));
			String fileName = com.gzedu.xlims.common.StringUtils.getBrowserStr(request, outputFileName); // 解决下载文件名乱码问题（兼容性）
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			workbook.write(response.getOutputStream());
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("<script type='application/javascript'>/*自动关闭当前窗口*/window.close();</script>");
			response.getWriter().flush();
		}
	}

	@InitBinder
	protected void InitBinder(WebDataBinder dataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

}
