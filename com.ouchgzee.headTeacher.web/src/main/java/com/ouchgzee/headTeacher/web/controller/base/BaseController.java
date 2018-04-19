/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.base;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gzedu.xlims.common.HttpContextUtils;
import com.ouchgzee.headTeacher.pojo.BzrGjtClassInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.common.vo.LoginNamespaceCopyright;

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

	private static final String LOGIN_NAMESPACE_COPYRIGHT_HEAD = "LOGIN_NAMESPACE_COPYRIGHT_HEAD";
	
	/**
	 * 通用日志记录
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	

	/**
	 * 版权信息保存到会话(Session)中
	 * 
	 * @param session
	 * @param copyright
	 */
	protected void setLoginNamespaceCopyright(HttpSession session, LoginNamespaceCopyright copyright) {
		session.setAttribute(LOGIN_NAMESPACE_COPYRIGHT_HEAD, copyright);
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

	/**
	 * 登录用户信息保存到会话(Session)中
	 * 
	 * @param session
	 * @param employeeInfo
	 */
	protected void setUser(HttpSession session, BzrGjtEmployeeInfo employeeInfo) {
		// <!-- 会话有效时长 12h 单位：秒 -->
		// session.setMaxInactiveInterval(43200);
		session.setAttribute(Servlets.SESSION_EMPLOYEE_NAME, employeeInfo);
	}

	/**
	 * 获取当前会话的登录用户信息
	 * 
	 * @param session
	 * @return
	 */
	protected BzrGjtEmployeeInfo getUser(HttpSession session) {
		BzrGjtEmployeeInfo employeeInfo = (BzrGjtEmployeeInfo) session.getAttribute(Servlets.SESSION_EMPLOYEE_NAME);
		return employeeInfo;
	}
	
	/**
	 * 新增 2018/04/10
	 * 获取当前用户信息
	 * @return
	 */
	protected BzrGjtEmployeeInfo currentUser() {
		HttpSession session = HttpContextUtils.getHttpServletRequest().getSession();
		BzrGjtEmployeeInfo employeeInfo = (BzrGjtEmployeeInfo) session.getAttribute(Servlets.SESSION_EMPLOYEE_NAME);
		return employeeInfo;
	}
	
	/**
	 * 当前班级信息保存到会话(Session)中
	 * 
	 * @param session
	 * @param classInfo
	 */
	protected void setCurrentClass(HttpSession session, BzrGjtClassInfo classInfo) {
		session.setAttribute(Servlets.SESSION_CURRENT_CLASS_NAME, classInfo);
	}

	/**
	 * 获取当前会话的当前班级信息
	 * 
	 * @param session
	 * @return
	 */
	protected BzrGjtClassInfo getCurrentClass(HttpSession session) {
		BzrGjtClassInfo classInfo = (BzrGjtClassInfo) session.getAttribute(Servlets.SESSION_CURRENT_CLASS_NAME);
		return classInfo;
	}

	/**
	 * 新增 2018/04/10
	 * 获取当前用户所在班级
	 * @return
	 */
	protected BzrGjtClassInfo currentClass() {
		HttpSession session = HttpContextUtils.getHttpServletRequest().getSession();
		BzrGjtClassInfo classInfo = (BzrGjtClassInfo) session.getAttribute(Servlets.SESSION_CURRENT_CLASS_NAME);
		return classInfo;
	}
	
	/**
	 * 获取当前会话的当前班级ID
	 * 
	 * @param session
	 * @return
	 */
	protected String getCurrentClassId(HttpSession session) {
		BzrGjtClassInfo classInfo = getCurrentClass(session);
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
	 * 设置让浏览器弹出下载[Excel文件]对话框的Header.
	 * 
	 * @param response
	 * @param workbook
	 * @param outputFileName
	 * @throws IOException
	 */
	protected void downloadExcelFile(HttpServletResponse response, Workbook workbook, String outputFileName)
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

}
