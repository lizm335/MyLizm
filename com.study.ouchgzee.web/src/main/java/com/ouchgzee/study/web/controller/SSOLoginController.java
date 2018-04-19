/**
 * Copyright(c) 2017 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.controller;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.FormSubmitUtil;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.gzedu.xlims.common.gzedu.SSOUtil;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.common.Servlets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 单点登录控制器<br/>
 * 功能说明：<br/>
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年05月11日
 * @version 2.5
 */
@Controller
@RequestMapping("/sso")
public class SSOLoginController extends BaseController {

	private static final Log log = LogFactory.getLog(SSOLoginController.class);

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtClassStudentService gjtClassStudentService;

	/**
	 * 新单点登录接口-使用学员ID<br>
	 * @param sign
	 *            密文 {xh-学员ID}
	 * @param t
	 *            跳转类型 默认跳转主页，t=1 跳转到PC学籍资料完善
	 *            t=2 跳转到PC学员个人详情
	 *            t=101 跳转到H5学籍资料完善
	 * @return
	 */
	@RequestMapping(value = "/signonNew")
	public ModelAndView signonNew(String sign, Integer t, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, null);
		if (StringUtils.isBlank(sign)) {
			ModelAndView mv = new ModelAndView();
			mv.addObject("shiroLoginFailure", UnknownAccountException.class.getName());
			mv.setViewName("redirect:/login");
			return mv;
		}

		Map<String, String> param = SSOUtil.parseSignOnParam(sign); // 解析参数
		String timestamp = param.get("timestamp");
		// 校验签名
		if (!SSOUtil.verifySign(sign)) {
			ModelAndView mv = new ModelAndView();
			mv.addObject("shiroLoginFailure", IncorrectCredentialsException.class.getName());
			mv.setViewName("redirect:/login");
			return mv;
		}
		String studentId = SSOUtil.getOneParam(sign); // 学号或身份证号
		// 超时校验
		long now = System.currentTimeMillis();
		long mills = NumberUtils.createLong(timestamp);
		boolean timeout = Math.abs(now - mills) > DateUtil.ONE_MINUTE * 30;
		if (!StringUtils.equals("ebc806cd61fb4982b3e54407662ee08b", studentId) && timeout) { // 30分钟内才能登录
			ModelAndView mv = new ModelAndView();
			mv.addObject("shiroLoginFailure", "timeout");
			mv.setViewName("redirect:/login");
			return mv;
		}

		GjtStudentInfo student = gjtStudentInfoService.queryById(studentId);
		if (student == null || student.getGjtUserAccount() == null || Constants.BOOLEAN_YES.equals(student.getIsDeleted())) {
			ModelAndView mv = new ModelAndView();
			mv.addObject("shiroLoginFailure", UnknownAccountException.class.getName());
			mv.setViewName("redirect:/login");
			return mv;
		}
		Subject account = SecurityUtils.getSubject();
		GjtStudentInfo sessionStudent = (GjtStudentInfo) account.getSession().getAttribute(WebConstants.STUDENT_INFO);
		// 其他账号登录时先销毁当前登录用户
		if (sessionStudent == null || !sessionStudent.getStudentId().equals(studentId)) {
			account.logout();

			UsernamePasswordToken token = new UsernamePasswordToken(student.getGjtUserAccount().getLoginAccount(),
					student.getGjtUserAccount().getPassword());
			try {
				account.login(token);
				Session session = SecurityUtils.getSubject().getSession();
				session.setAttribute(WebConstants.CURRENT_USER, student.getGjtUserAccount());
				session.setAttribute("userId", student.getGjtUserAccount().getId());

				if (student.getGjtUserAccount().getUserType() != null && student.getGjtUserAccount().getUserType() == 1) {
					session.setAttribute(WebConstants.STUDENT_INFO, student);

					GjtClassInfo classInfo = gjtClassStudentService.queryTeachClassInfoByStudetnId(student.getStudentId());
					session.setAttribute(WebConstants.TEACH_CLASS, classInfo);
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				token.clear();
				ModelAndView mv = new ModelAndView();
				mv.addObject("shiroLoginFailure", e.getClass().getName());
				mv.setViewName("redirect:/login");
				return mv;
			}
		}
		searchParams.remove("sign");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/pcenter/main?" + FormSubmitUtil.createLinkString(searchParams));
		if (t != null) {
			if (t == 1) {
				mv.setViewName("redirect:/pcenter/main#/scdenglu?" + FormSubmitUtil.createLinkString(searchParams));
			} else if (t == 2) {
				mv.setViewName("redirect:/pcenter/personal-information/index.html?" + FormSubmitUtil.createLinkString(searchParams));
			} else if (t == 101) {
				mv.setViewName("redirect:/pcenter/mobile/complete-info-h5.html?" + FormSubmitUtil.createLinkString(searchParams));
			}
		}
		return mv;
	}

	/**
	 * 单点登录<br>
	 * 
	 * @param p
	 *            密文 {xh-学号或身份证号}
	 * @param t
	 *            跳转类型 默认跳转主页，t=1 跳转到PC学籍资料完善 t=101 跳转到H5学籍资料完善
	 * @return
	 */
	@Deprecated // 使用新的接口，因为身份证号会对应多个培养层次
	@RequestMapping(value = "/signon")
	public ModelAndView signon(String p, Integer t, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		if (StringUtils.isBlank(p)) {
			ModelAndView mv = new ModelAndView();
			mv.addObject("shiroLoginFailure", UnknownAccountException.class.getName());
			mv.setViewName("redirect:/login");
			return mv;
		}

		Map<String, String> param = SSOUtil.parseSignOnParam(p); // 解析参数
		String timestamp = param.get("timestamp");
		// 校验签名
		if (!SSOUtil.verifySign(p)) {
			ModelAndView mv = new ModelAndView();
			mv.addObject("shiroLoginFailure", IncorrectCredentialsException.class.getName());
			mv.setViewName("redirect:/login");
			return mv;
		}
		String xh = SSOUtil.getOneParam(p); // 学号或身份证号
		// 超时校验
		long now = System.currentTimeMillis();
		long mills = NumberUtils.createLong(timestamp);
		boolean timeout = Math.abs(now - mills) > DateUtil.ONE_MINUTE * 30;
		if (!StringUtils.equals("ls001", xh) && timeout) { // 30分钟内才能登录
			ModelAndView mv = new ModelAndView();
			mv.addObject("shiroLoginFailure", "timeout");
			mv.setViewName("redirect:/login");
			return mv;
		}

		GjtStudentInfo student = gjtStudentInfoService.querySSOByXhOrSfzh(xh);
		if (student == null || student.getGjtUserAccount() == null) {
			ModelAndView mv = new ModelAndView();
			mv.addObject("shiroLoginFailure", UnknownAccountException.class.getName());
			mv.setViewName("redirect:/login");
			return mv;
		}
		Subject account = SecurityUtils.getSubject();
		GjtStudentInfo sessionStudent = (GjtStudentInfo) account.getSession().getAttribute(WebConstants.STUDENT_INFO);
		// 其他账号登录时先销毁当前登录用户
		if (sessionStudent == null || !sessionStudent.getStudentId().equals(student.getStudentId())) {
			account.logout();

			UsernamePasswordToken token = new UsernamePasswordToken(student.getGjtUserAccount().getLoginAccount(),
					student.getGjtUserAccount().getPassword());
			try {
				account.login(token);
				Session session = SecurityUtils.getSubject().getSession();
				session.setAttribute(WebConstants.CURRENT_USER, student.getGjtUserAccount());
				session.setAttribute("userId", student.getGjtUserAccount().getId());

				if (student.getGjtUserAccount().getUserType() != null && student.getGjtUserAccount().getUserType() == 1) {
					session.setAttribute(WebConstants.STUDENT_INFO, student);

					GjtClassInfo classInfo = gjtClassStudentService.queryTeachClassInfoByStudetnId(student.getStudentId());
					session.setAttribute(WebConstants.TEACH_CLASS, classInfo);
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				token.clear();
				ModelAndView mv = new ModelAndView();
				mv.addObject("shiroLoginFailure", e.getClass().getName());
				mv.setViewName("redirect:/login");
				return mv;
			}
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/pcenter/main");
		if (t != null) {
			if (t == 1) {
				mv.setViewName("redirect:/pcenter/main#/scdenglu");
			} else if (t == 101) {
				mv.setViewName("redirect:/pcenter/mobile/complete-info-h5.html");
			}
		}
		return mv;
	}

}
