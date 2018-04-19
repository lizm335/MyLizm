
/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.purview;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.PlatfromTypeEnum;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtSetOrgCopyright;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.service.account.BzrGjtUserAccountService;
import com.ouchgzee.headTeacher.service.employee.BzrGjtEmployeeInfoService;
import com.ouchgzee.headTeacher.service.student.BzrGjtClassService;
import com.ouchgzee.headTeacher.service.systemManage.BzrGjtSetOrgCopyrightService;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.common.vo.LoginNamespaceCopyright;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

import net.spy.memcached.MemcachedClient;

/**
 * 班主任登录控制器<br/>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月12日
 * @version 2.5
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
	private final static Logger log = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private BzrGjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	BzrGjtUserAccountService gjtUserAccountService;

	@Autowired
	private BzrGjtClassService gjtClassService;

	@Autowired
	MemcachedClient memcachedClient;

	@Autowired
	BzrGjtSetOrgCopyrightService gjtSetOrgCopyrightService;

	/**
	 * 登录页
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String login(ModelMap model, HttpServletRequest request, HttpSession session) {
		BzrGjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolRealmName(request,
				PlatfromTypeEnum.HEADTEACHERPLATFORM.getNum());
		if (item != null) {
			LoginNamespaceCopyright copyright = new LoginNamespaceCopyright(null, item.getPlatformName());
			copyright.setLoginHeadLogo(item.getLoginHeadLogo());
			copyright.setLoginFooterCopyright(item.getLoginFooterCopyright());
			copyright.setHomeFooterCopyright(item.getHomeFooterCopyright());
			copyright.setLoginBackground(item.getLoginBackground());
			copyright.setQcodePic(item.getQcodePic());
			copyright.setLoginTitle(item.getLoginTitle());
			super.setLoginNamespaceCopyright(session, copyright);
		}

		return "new/purview/teacher_login";
	}

	/**
	 * 账号登录
	 * 
	 * @param gjtUserAccount
	 * @param username
	 * @param password
	 * @param vCode
	 * @param userType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String checkLogin(@Valid BzrGjtUserAccount gjtUserAccount, String username, String password, String userType,
			HttpServletRequest request, HttpSession session, ModelMap model, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) throws Exception {
		// 1为班主任，2为辅导教师，4为督导老师
		String employeeType = "1";
		if ("133".equals(userType)) {
			employeeType = "1";
		} else if ("122".equals(userType)) {
			employeeType = "2";
		} else if ("144".equals(userType)) {
			employeeType = "4";
		}
		// 验证码校验
		String vCode = (String) session.getAttribute("vCode");
		String rCode = request.getParameter("rCode");
		log.info("----------------->>>>>>>>>>>>>>>vCode:" + vCode + ",rCode:" + rCode);
		if (vCode != null && rCode != null && !vCode.equals(rCode)) {
			model.put("msg", "验证码错误！");
			return "new/purview/teacher_login";
		}

		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			model.put("msg", "用户名/密码不能为空！");
			return "new/purview/teacher_login";
		}
		model.put("username", username);

		/*
		 * Object scode = session.getAttribute(Servlets.SESSION_VCODE_NAME); if
		 * (!vCode.equals(scode)) { model.put("msg", "验证码不正确！");
		 * session.removeAttribute(Servlets.SESSION_VCODE_NAME); return
		 * "new/purview/teacher_login"; }
		 */
		session.removeAttribute(Servlets.SESSION_VCODE_NAME);

		Subject account = SecurityUtils.getSubject();
		String host = request.getRemoteHost();
		UsernamePasswordToken token = new UsernamePasswordToken(username, Md5Util.encrypt(password));
		try {
			account.login(token);
			// 登陆成功
			BzrGjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryByLoginAccount(username);
			if ("1".equals(employeeType)) {
				long classNum = gjtClassService.countClassByBzr(employeeInfo.getEmployeeId());
				if (classNum == 0) {
					model.put("msg", "登录失败，没有分配班级！");
					return "new/purview/teacher_login";
				}
			} else if ("4".equals(employeeType)) {
				List<Object[]> tpcList = gjtEmployeeInfoService.queryTachPlanClassByEmpId(employeeInfo.getEmployeeId());
				if (tpcList == null || tpcList.size() == 0) {
					model.put("msg", "登录失败，没有分配班级！");
					return "new/purview/teacher_login";
				}
				// String teachPlanId = tpcList.get(0)[0].toString();
				// String classId = tpcList.get(0)[1].toString();
				// String url = EncryptUtils.encrypt(teachPlanId + "," + classId
				// + "," + employeeInfo.getEmployeeId());
				// session.setAttribute("url", AppConfig.getProperty("xlHost") +
				// "/book/index/ddteacher/urlLogin.do?formMap.USER_INFO=" +
				// url);
			}
			super.setUser(session, employeeInfo);
		} catch (UnknownAccountException e) {
			log.error(e.getMessage(), e);
			token.clear();
			/*
			 * 如果身份验证失败请捕获 AuthenticationException 或其子类， 常见的如：
			 * DisabledAccountException（禁用的帐号）、 LockedAccountException（锁定的帐号）、
			 * UnknownAccountException（错误的帐号）、
			 * ExcessiveAttemptsException（登录失败次数过多）、
			 * IncorrectCredentialsException （错误的凭证）、
			 * ExpiredCredentialsException（过期的凭证）等，具体请查看其继承关系； 对于页面的错误消息展示，
			 * 最好使用如 “用户名 / 密码错误” 而不是 “用户名错误”/“密码错误”， 防止一些恶意用户非法扫描帐号库；
			 */
			model.put("msg", "帐号不存在！");
			return "new/purview/teacher_login";
		} catch (AuthenticationException e) {
			log.error(e.getMessage(), e);
			token.clear();
			model.put("msg", "用户名/密码不正确！");
			return "new/purview/teacher_login";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			token.clear();
			model.put("msg", "未知错误!");
			return "new/purview/teacher_login";
		}

		return "redirect:/home/manyClass";
	}

	/**
	 * 账号登出
	 * 
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		Subject currentUser = SecurityUtils.getSubject();
		if (SecurityUtils.getSubject().getSession() != null) {
			currentUser.logout();
		}
		session.invalidate();
		return "redirect:/login";
	}

}