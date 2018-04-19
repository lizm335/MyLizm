/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.common;

import com.gzedu.xlims.common.HttpContextUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.ouchgzee.study.web.vo.LoginNamespaceCopyright;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

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

	private static final String LOGIN_NAMESPACE_COPYRIGHT_STUDY = "LOGIN_NAMESPACE_COPYRIGHT_STUDY";
	private static final String LOGIN_NAMESPACE_COPYRIGHT_TEACHER = "LOGIN_NAMESPACE_COPYRIGHT_TEACHER";

	/**
	 * 版权信息保存到会话(Session)中
	 * 
	 * @param session
	 * @param copyright
	 */
	protected void setLoginNamespaceCopyrightStudy(HttpSession session, LoginNamespaceCopyright copyright) {
		session.setAttribute(LOGIN_NAMESPACE_COPYRIGHT_STUDY, copyright);
	}

	protected void setLoginNamespaceCopyrightTeacher(HttpSession session, LoginNamespaceCopyright copyright) {
		session.setAttribute(LOGIN_NAMESPACE_COPYRIGHT_TEACHER, copyright);
	}

	/**
	 * 判断是否为本科
	 * 
	 * @param pycc
	 * @return
	 */
	protected boolean isUndergraduateCourse(String pycc) {
		return "2".equals(pycc) || "8".equals(pycc);
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

	 /**
	 * 教学班级信息保存到会话(Session)中
	 * @param session
	 * @param classInfo
	 */
	 protected void setTeachClass(HttpSession session, GjtClassInfo classInfo) {
	 	session.setAttribute(WebConstants.TEACH_CLASS, classInfo);
	 }

	 /**
	 * 获取当前会话的教学班级信息
	 * @param session
	 * @return
	 */
	 protected GjtClassInfo getTeachClass(HttpSession session) {
		 GjtClassInfo classInfo = (GjtClassInfo)
		 session.getAttribute(WebConstants.TEACH_CLASS);
		 return classInfo;
	 }

	 /**
	 * 获取当前会话的教学班级ID
	 * @param session
	 * @return
	 */
	 protected String getTeachClassId(HttpSession session) {
		 GjtClassInfo classInfo = getTeachClass(session);
		 return classInfo != null ? classInfo.getClassId() : null;
	 }

	/**
	 * 获取当前项目域名路径
	 * @param request
	 * @return
	 */
	protected String getBasePath(HttpServletRequest request) {
		String serverName = request.getServerName();
		int port = request.getServerPort();
		String ctx = request.getContextPath();
		return "http://" + serverName + ":" + port + ctx;
	}

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
			// + new String(outputFileName.getBytes("UTF-8"), "ISO8859-
			HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
			String fileName = com.gzedu.xlims.common.StringUtils.getBrowserStr(request, outputFileName); // 解决下载文件名乱码问题（兼容性）
			// 解决IE下载文件名乱码问题
			response.setHeader("Content-Disposition",
					"attachment; filename=" + fileName);

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
	protected void downloadExcelFile(HttpServletResponse response, HSSFWorkbook workbook, String outputFileName)
			throws IOException {
		if (workbook != null) {
			response.setContentType("application/x-msdownload;charset=utf-8");
			// response.setHeader("Content-Disposition", "attachment; filename="
			// + new String(outputFileName.getBytes("UTF-8"), "ISO8859-1"));
			// 解决IE下载文件名乱码问题
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(outputFileName.getBytes("GB2312"), "ISO8859-1"));
			workbook.write(response.getOutputStream());
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("<script type='application/javascript'>/*自动关闭当前窗口*/window.close();</script>");
			response.getWriter().flush();
		}
	}

	/**
	 * 插入数据判断成功与否
	 * 
	 * @param insert
	 * @return Feedback
	 */
	protected Feedback returnFeedback(Boolean insert) {
		Feedback feedback = new Feedback("0", "接口调用成功");
		if (insert) {
			feedback = new Feedback("0", "接口调用成功,数据插入成功");
		} else {
			feedback = new Feedback("0", "接口调用成功,插入数据失败");
		}
		return feedback;
	}


}
