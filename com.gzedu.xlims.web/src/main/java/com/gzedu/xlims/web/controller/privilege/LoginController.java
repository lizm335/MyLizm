/**
 * Copyright(c) 2013 版权归属广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.privilege;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.PlatfromTypeEnum;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.ResponseModel;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtSetOrgCopyright;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.service.organization.GjtSetOrgCopyrightService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.login.vo.LoginNamespaceCopyright;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月10日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("")
public class LoginController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@Autowired
	private GjtSetOrgCopyrightService gjtSetOrgCopyrightService;

	@RequestMapping(value = "/{namespace}/login", method = RequestMethod.GET)
	public String login(@PathVariable("namespace") String namespace, ModelMap model, HttpServletRequest request,
			HttpSession session) {
		LoginNamespaceCopyright copyright = new LoginNamespaceCopyright(namespace);
		if ("yz".equals(copyright.getLoginNamespace())) {
			copyright.setLoginTitle("院长管理平台");
		} else if ("jw".equals(copyright.getLoginNamespace())) {
			copyright.setLoginTitle("教务管理平台");
		} else if ("jx".equals(copyright.getLoginNamespace())) {
			copyright.setLoginTitle("教学管理平台");
		} else if ("xj".equals(copyright.getLoginNamespace())) {
			copyright.setLoginTitle("学籍管理平台");
		} else if ("kw".equals(copyright.getLoginNamespace())) {
			copyright.setLoginTitle("考务管理平台");
		} else if ("jc".equals(copyright.getLoginNamespace())) {
			copyright.setLoginTitle("教材管理平台");
		} else if ("by".equals(copyright.getLoginNamespace())) {
			copyright.setLoginTitle("毕业管理平台");
		} else if ("xz".equals(copyright.getLoginNamespace())) {
			copyright.setLoginTitle("学院学支管理平台");
		} else if ("xxzx".equals(copyright.getLoginNamespace())) {
			copyright.setLoginTitle("学习中心管理平台");
		} else if ("xxzxxz".equals(copyright.getLoginNamespace())) {
			copyright.setLoginTitle("学习中心学支管理平台");
		} else if ("zsdxz".equals(copyright.getLoginNamespace())) {
			copyright.setLoginTitle("招生点学支管理平台");
		} else if ("yy".equals(copyright.getLoginNamespace())) {
			copyright.setLoginTitle("运营管理平台");
		}
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolRealmName(request,
				PlatfromTypeEnum.MANAGEPLATFORM.getNum());
		if (item != null) {
			copyright.setLoginHeadLogo(item.getLoginHeadLogo());
			copyright.setLoginFooterCopyright(item.getLoginFooterCopyright());
			copyright.setHomeFooterCopyright(item.getHomeFooterCopyright());
			copyright.setLoginBackground(item.getLoginBackground());
		}
		super.setLoginNamespaceCopyright(session, copyright);
		return "/purview/login/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(ModelMap model, HttpServletRequest request, HttpSession session) {

		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolRealmName(request,
				PlatfromTypeEnum.MANAGEPLATFORM.getNum());
		if (item != null) {
			LoginNamespaceCopyright copyright = new LoginNamespaceCopyright(null, item.getPlatformName());
			copyright.setLoginHeadLogo(item.getLoginHeadLogo());
			copyright.setLoginFooterCopyright(item.getLoginFooterCopyright());
			copyright.setHomeFooterCopyright(item.getHomeFooterCopyright());
			copyright.setLoginBackground(item.getLoginBackground());
			super.setLoginNamespaceCopyright(session, copyright);
		}
		return "/purview/login/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String checkLogin(@Valid GjtUserAccount gjtUserAccount, String username, String password, String namespace,
			ModelMap model, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) throws Exception {
		Subject account = SecurityUtils.getSubject();
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			model.put("msg", "用户名或密码不能为空！");
			return "/purview/login/login";
		}

		UsernamePasswordToken token = new UsernamePasswordToken(username, Md5Util.encrypt(password));
		try {
			model.put("username", username);
			account.login(token);
			// 设置session
			GjtUserAccount user = gjtUserAccountService.queryByLoginAccount(username);
			Session session = SecurityUtils.getSubject().getSession();
			session.setAttribute(WebConstants.CURRENT_USER, user);
			session.setAttribute("userId", user.getId());
		} catch (UnknownAccountException e) {
			log.error(e.getMessage(), e);
			token.clear();
			model.put("msg", "账号不存在!");
			return "/purview/login/login";
		} catch (DisabledAccountException e) {
			log.error(e.getMessage(), e);
			token.clear();
			model.put("msg", "账号被停用");
			return "/purview/login/login";
		} catch (AuthenticationException e) {
			log.error(e.getMessage(), e);
			token.clear();
			model.put("msg", "用户或密码不正确！");
			return "/purview/login/login";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			token.clear();
			model.put("msg", "未知错误!");
			return "/purview/login/login";
		}

		return "redirect:/admin/home/main";
	}

	@ResponseBody
	@RequestMapping(value = "ajax/login", method = RequestMethod.POST)
	public Feedback checkAjaxLogin(@Valid GjtUserAccount gjtUserAccount, String username, String password,
			String namespace, ModelMap model, BindingResult bindingResult, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
		Feedback feedback = new Feedback(false, null);
		Subject account = SecurityUtils.getSubject();
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			feedback.setMessage("用户名或密码不能为空！");
			return feedback;
		}

		UsernamePasswordToken token = new UsernamePasswordToken(username, Md5Util.encrypt(password));
		try {
			model.put("username", username);
			account.login(token);
			// 设置session
			GjtUserAccount user = gjtUserAccountService.queryByLoginAccount(username);
			Session session = SecurityUtils.getSubject().getSession();
			session.setAttribute(WebConstants.CURRENT_USER, user);
			session.setAttribute("userId", user.getId());
			feedback.setSuccessful(true);
		} catch (UnknownAccountException e) {
			log.error(e.getMessage(), e);
			token.clear();
			feedback.setMessage("账号不存在!");
		} catch (AuthenticationException e) {
			log.error(e.getMessage(), e);
			token.clear();
			feedback.setMessage("用户或密码不正确！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			token.clear();
			feedback.setMessage("未知错误!");
		}

		return feedback;
	}

	@RequestMapping(value = "signIn", method = RequestMethod.POST)
	public String signIn(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {

		return "/purview/login/login";
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		Subject currentUser = SecurityUtils.getSubject();
		if (SecurityUtils.getSubject().getSession() != null) {
			currentUser.logout();
		}
		LoginNamespaceCopyright copyright = super.getLoginNamespaceCopyright(session);
		session.invalidate();
		return "redirect:" + copyright.getLoginNamespace() + "/login";
	}

	@RequestMapping(value = "/static/platform.html", method = RequestMethod.GET)
	public String platform(ModelMap model, HttpServletRequest request, HttpSession session) {

		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolRealmName(request,
				PlatfromTypeEnum.MANAGEPLATFORM.getNum());
		if (item != null) {
			LoginNamespaceCopyright copyright = new LoginNamespaceCopyright(null, item.getPlatformName());
			copyright.setLoginHeadLogo(item.getLoginHeadLogo());
			copyright.setLoginFooterCopyright(item.getLoginFooterCopyright());
			copyright.setHomeFooterCopyright(item.getHomeFooterCopyright());
			copyright.setLoginBackground(item.getLoginBackground());
			super.setLoginNamespaceCopyright(session, copyright);
		}
		return "/purview/login/platform";
	}

	/**
	 * 登录超时
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月5日 下午7:01:08
	 * @param response
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "loginForSessionTimeOut")
	public ResponseModel loginForSessionTimeOut(HttpServletResponse response, HttpServletRequest request) {
		response.setStatus(MessageCode.TOKEN_INVALID.getMsgCode());
		ResponseModel rm = new ResponseModel(MessageCode.TOKEN_INVALID.getMsgCode(), 0,
				MessageCode.TOKEN_INVALID.getMessage());
		return rm;
	}

	/**
	 * 跳转任课教师登录页面
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月30日 上午11:13:19
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "classTeacher/login", method = RequestMethod.GET)
	public String classTecherLogin(ModelMap model, HttpServletRequest request, HttpSession session) {
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolRealmName(request,
				PlatfromTypeEnum.CLASSTEACHERPLATFORM.getNum());
		if (item != null) {
			LoginNamespaceCopyright copyright = new LoginNamespaceCopyright(null, item.getPlatformName());
			copyright.setLoginHeadLogo(item.getLoginHeadLogo());
			copyright.setLoginFooterCopyright(item.getLoginFooterCopyright());
			copyright.setHomeFooterCopyright(item.getHomeFooterCopyright());
			copyright.setLoginBackground(item.getLoginBackground());
			super.setLoginNamespaceCopyright(session, copyright);
		}
		request.setAttribute("loginTitle", "任课教师登录");
		request.setAttribute("loginUrl", "/classTeacher/login");
		return "/purview/login/teahcer_login";
	}

	/**
	 * 任课教师登录
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月30日 上午11:13:39
	 * @param model
	 * @param request
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "classTeacher/login", method = RequestMethod.POST)
	public String classTecherCheckLogin(ModelMap model, HttpServletRequest request, String username, String password)
			throws Exception {
		Subject account = SecurityUtils.getSubject();
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			model.put("msg", "用户名或密码不能为空！");
			return "/purview/login/teahcer_login";
		}

		UsernamePasswordToken token = new UsernamePasswordToken(username, Md5Util.encrypt(password));
		try {
			model.put("username", username);
			account.login(token);
			GjtUserAccount user = gjtUserAccountService.queryByLoginAccount(username);
			if (!EmployeeTypeEnum.任课教师.name().equals(user.getPriRoleInfo().getRoleName())) {
				token.clear();
				model.put("msg", "用户或密码不正确！");
				return "/purview/login/teahcer_login";
			}
			// 设置session
			Session session = SecurityUtils.getSubject().getSession();
			session.setAttribute(WebConstants.CURRENT_USER, user);
			session.setAttribute("userId", user.getId());
		} catch (UnknownAccountException e) {
			log.error(e.getMessage(), e);
			token.clear();
			model.put("msg", "账号不存在!");
			return "/purview/login/teahcer_login";
		} catch (AuthenticationException e) {
			log.error(e.getMessage(), e);
			token.clear();
			model.put("msg", "用户或密码不正确！");
			return "/purview/login/teahcer_login";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			token.clear();
			model.put("msg", "未知错误!");
			return "/purview/login/teahcer_login";
		}

		return "redirect:/admin/home/main";
	}

}
