/**
 * Copyright(c) 2013 版权归属广州远程教育中心 www.969300.com
 */
package com.ouchgzee.study.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gzedu.xlims.common.PlatfromTypeEnum;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.ResponseModel;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtSetOrgCopyright;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.organization.GjtSetOrgCopyrightService;
import com.gzedu.xlims.service.systemManage.TblPriLoginLogService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.vo.LoginNamespaceCopyright;

/**
 * 
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2017年2月15日
 * @version 2.5
 *
 */
@Controller
public class LoginController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	TblPriLoginLogService tblPriLoginLogService;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	private GjtClassInfoService gjtClassInfoService;

	@Autowired
	private GjtSetOrgCopyrightService gjtSetOrgCopyrightService;

	@Autowired
	private GjtTeachPlanService gjtTeachPlanService;

	// 学生登陆页面版权配置
	@RequestMapping(value = "login")
	public String login(ModelMap model, HttpServletResponse response, HttpServletRequest request)
			throws ServletException, IOException {
		String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
		if (exceptionClassName == null)
			exceptionClassName = request.getParameter("shiroLoginFailure");
		if (exceptionClassName != null) {
			log.info("登录异常:" + exceptionClassName);
			if ("randomCodeError".equals(exceptionClassName)) {
				request.setAttribute("loginErrorMessage", "验证码错误!");
			} else if ("timeout".equals(exceptionClassName)) {
				request.setAttribute("loginErrorMessage", "登录超时!");
			} else if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
				request.setAttribute("loginErrorMessage", "帐号不存在!");
			} else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
				request.setAttribute("loginErrorMessage", "密码错误");
			} else if (AuthenticationException.class.getName().equals(exceptionClassName)) {
				request.setAttribute("loginErrorMessage", "认证发生异常");
			} else if ("quitSchool".equals(exceptionClassName)) {
				request.setAttribute("loginErrorMessage", "您目前是休学状态!");
			} else if ("dropOutOfSchool".equals(exceptionClassName)) {
				request.setAttribute("loginErrorMessage", "您已退学无法登录!");
			} else {
				request.setAttribute("loginErrorMessage", "未知错误");
			}
		}

		/*
		 * // 进入登录页面销毁当前登录用户 Subject currentUser = SecurityUtils.getSubject();
		 * if (SecurityUtils.getSubject().getSession() != null) {
		 * currentUser.logout(); }
		 */

		LoginNamespaceCopyright copyright = new LoginNamespaceCopyright();
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolRealmName(request,
				PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		if (item != null) {
			copyright.setLoginHeadLogo(item.getLoginHeadLogo());
			copyright.setLoginFooterCopyright(item.getLoginFooterCopyright());
			copyright.setHomeFooterCopyright(item.getHomeFooterCopyright());
			copyright.setLoginBackground(item.getLoginBackground());
			copyright.setLoginTitle(item.getLoginTitle());
			copyright.setQcodePic(item.getQcodePic());
			super.setLoginNamespaceCopyrightStudy(request.getSession(), copyright);
		}

		return "login";
	}

	@ResponseBody
	@RequestMapping(value = "loginForNotLogged")
	public ResponseModel loginForNotLogged(HttpServletResponse response, HttpServletRequest request) {
		ResponseModel rm = new ResponseModel(MessageCode.NOT_LOGGED_IN.getMsgCode(), 0,
				MessageCode.NOT_LOGGED_IN.getMessage());
		return rm;
	}

	@ResponseBody
	@RequestMapping(value = "loginForSessionTimeOut")
	public ResponseModel loginForSessionTimeOut(HttpServletResponse response, HttpServletRequest request) {
		ResponseModel rm = new ResponseModel(MessageCode.TOKEN_INVALID.getMsgCode(), 0,
				MessageCode.TOKEN_INVALID.getMessage());
		return rm;
	}

	@RequestMapping(value = "/pcenter/main")
	public ModelAndView main(ModelMap model, HttpServletResponse response, HttpSession session,
			HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		GjtUserAccount user = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);
		if ("4".equals(user.getGjtStudentInfo().getXjzt())) { // 休学
			Subject currentUser = SecurityUtils.getSubject();
			if (SecurityUtils.getSubject().getSession() != null) {
				currentUser.logout();
			}
			mv.addObject("shiroLoginFailure", "quitSchool");
			mv.setViewName("redirect:/login");
			return mv;
		} else if ("5".equals(user.getGjtStudentInfo().getXjzt())) { // 退学
			Subject currentUser = SecurityUtils.getSubject();
			if (SecurityUtils.getSubject().getSession() != null) {
				currentUser.logout();
			}
			mv.addObject("shiroLoginFailure", "dropOutOfSchool");
			mv.setViewName("redirect:/login");
			return mv;
		}

		boolean bool = false;
		try {
			log.info("==================>当前登录会话ID：{}", session.getId());
			log.info("==================>用户是否在线：{};上次登录会话ID：{}", user.getIsOnline(), user.getCurrentLoginIp());
			if (!session.getId().equals(user.getCurrentLoginIp()) && "Y".equals(user.getIsOnline())) {
				// 如果浏览器关闭，日志表里面旧的sessionId更改为新的
				tblPriLoginLogService.updateNewSessionByOldSession(user.getCurrentLoginIp(), session.getId());
				// 更改旧的sessionId为新的，要改user表
				bool = gjtUserAccountService.updateSessionId(user.getId(), user.getLoginCount(), session.getId());
			} else {
				if ("N".equals(user.getIsOnline()) || StringUtils.isBlank(user.getIsOnline())) {// Y是刷新,N是离线
					bool = gjtUserAccountService.updateLoginState(user.getId(), user.getLoginCount(), session.getId());
					// 插入日志表
					tblPriLoginLogService.save(user, request, session.getId());
				}
			}
			if (bool) {
				user = gjtUserAccountService.findOne(user.getId());
				session.setAttribute(WebConstants.CURRENT_USER, user);// 更新缓存
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		mv.addObject("result", 0);
		mv.addObject("msg", "");
		mv.addObject("sessionId", session.getId());
		mv.setViewName("en/index");
		return mv;
	}

	// 辅导老师登陆页面版权配置
	@RequestMapping(value = "/coach/teacher/login", method = RequestMethod.GET)
	public String toCoachTeacherLogin(HttpServletRequest request) {
		request.setAttribute("loginTitle", "辅导老师登录");
		request.setAttribute("loginUrl", "/coach/teacher/login");

		LoginNamespaceCopyright copyright = new LoginNamespaceCopyright();
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolRealmName(request,
				PlatfromTypeEnum.COACHTEACHERPLATFORM.getNum());
		if (item != null) {
			copyright.setLoginHeadLogo(item.getLoginHeadLogo());
			copyright.setLoginFooterCopyright(item.getLoginFooterCopyright());
			copyright.setHomeFooterCopyright(item.getHomeFooterCopyright());
			copyright.setLoginBackground(item.getLoginBackground());
			copyright.setLoginTitle(item.getLoginTitle());
			copyright.setQcodePic(item.getQcodePic());
			super.setLoginNamespaceCopyrightTeacher(request.getSession(), copyright);
		}

		return "/teacher-login";
	}

	@RequestMapping(value = "/coach/teacher/login", method = RequestMethod.POST)
	public String coachTeacherLogin(HttpServletRequest request, HttpServletResponse response) {
		String account = request.getParameter("username");
		String password = request.getParameter("password");
		String url = "/coach/teacher/login";
		try {
			GjtUserAccount userAccount = gjtUserAccountService.queryByLoginAccount(account);
			if (userAccount == null) {
				request.setAttribute("loginErrorMessage", "帐号不存在.");
				return toCoachTeacherLogin(request);
			} else {
				if (!userAccount.getPassword2().equals(password)) {
					request.setAttribute("loginErrorMessage", "密码错误.");
					return toCoachTeacherLogin(request);
				} else {
					GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryByAccountId(userAccount.getId());
					url = gjtUserAccountService.coachTeacherSimulation(employeeInfo.getEmployeeId());
					// response.sendRedirect(url);
				}
			}
		} catch (Exception e) {
			request.setAttribute("loginErrorMessage", "未知错误.");
			e.printStackTrace();
		}
		return "redirect:" + url; // 修改完重定向
	}

	// 督导老师登陆页面版权配置
	@RequestMapping(value = "/supervisor/teacher/login", method = RequestMethod.GET)
	public String toSupervisorTeacherLogin(HttpServletRequest request) {
		request.setAttribute("loginTitle", "督导老师登录");
		request.setAttribute("loginUrl", "/supervisor/teacher/login");

		LoginNamespaceCopyright copyright = new LoginNamespaceCopyright();
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolRealmName(request,
				PlatfromTypeEnum.SUPERVISORTEACHERPLATFORM.getNum());
		if (item != null) {
			copyright.setLoginHeadLogo(item.getLoginHeadLogo());
			copyright.setLoginFooterCopyright(item.getLoginFooterCopyright());
			copyright.setHomeFooterCopyright(item.getHomeFooterCopyright());
			copyright.setLoginBackground(item.getLoginBackground());
			copyright.setLoginTitle(item.getLoginTitle());
			copyright.setQcodePic(item.getQcodePic());
			super.setLoginNamespaceCopyrightTeacher(request.getSession(), copyright);
		}

		return "/teacher-login";
	}

	@RequestMapping(value = "/supervisor/teacher/login", method = RequestMethod.POST)
	public String supervisorTeacherLogin(HttpServletRequest request, HttpServletResponse response) {
		String account = request.getParameter("username");
		String password = request.getParameter("password");
		// String url = "/supervisor/teacher/login";
		String url = null;
		try {
			GjtUserAccount userAccount = gjtUserAccountService.queryByLoginAccount(account);
			if (userAccount == null) {
				request.setAttribute("loginErrorMessage", "帐号不存在.");
				return toSupervisorTeacherLogin(request);
			} else {
				if (!userAccount.getPassword2().equals(password)) {
					request.setAttribute("loginErrorMessage", "密码错误.");
					return toSupervisorTeacherLogin(request);
				} else {
					GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryByAccountId(userAccount.getId());
					List<Object[]> classInfoList = gjtClassInfoService
							.findClassIdANDTermcourseId(employeeInfo.getEmployeeId(), "course");
					if (classInfoList == null || classInfoList.size() == 0) {
						request.setAttribute("loginErrorMessage", "该帐号没有分配班级.");
						return toSupervisorTeacherLogin(request);
					}
					for (Object[] object : classInfoList) {
						String termcourseId = ObjectUtils.toString(object[1]);
						String classId = ObjectUtils.toString(object[0]);
						if (StringUtils.isNotEmpty(termcourseId)) {
							url = gjtUserAccountService.supervisorTeacherSimulation(termcourseId, classId,
									employeeInfo.getEmployeeId());
							log.info("{}用户登录督导平台url:{}", userAccount, url);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			request.setAttribute("loginErrorMessage", "未知错误.");
			e.printStackTrace();
		}

		if (StringUtils.isEmpty(url)) {
			request.setAttribute("loginErrorMessage", "该帐号分配的班级没有教学计划");
			return toSupervisorTeacherLogin(request);
		} else {
			url = "redirect:" + url;
		}
		log.info("url:{}", url);
		return url;// 修改完重定向
	}
}
