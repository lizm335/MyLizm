/**
 * Copyright(c) 2017 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

/**
 * 微信公众号单点登录控制器<br/>
 * 功能说明：<br/>
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年11月21日
 * @version 3.0
 */
@Controller
@RequestMapping("/sso/wx")
public class SSOWxLoginController extends BaseController {

	private static final Log log = LogFactory.getLog(SSOWxLoginController.class);

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtClassStudentService gjtClassStudentService;

	/**
	 * 新单点登录接口-使用学员微信OpenId<br>
	 * @param sign
	 *            密文 {xh-微信OpenId}
	 * @param t
	 *            跳转类型 默认跳转主页，t=1 跳转到PC学籍资料完善
	 *            t=2 跳转到PC学员个人详情
	 *            t=101 跳转到H5学籍资料完善
	 *            t=102 跳转到微信版学员答疑
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
		String wxOpenId = SSOUtil.getOneParam(sign); // 微信OpenId
		// 超时校验
		long now = System.currentTimeMillis();
		long mills = NumberUtils.createLong(timestamp);
		boolean timeout = Math.abs(now - mills) > DateUtil.ONE_MINUTE * 30;
		if (!StringUtils.equals("0", wxOpenId) && timeout) { // 30分钟内才能登录
			ModelAndView mv = new ModelAndView();
			mv.addObject("shiroLoginFailure", "timeout");
			mv.setViewName("redirect:/login");
			return mv;
		}

		GjtStudentInfo student = gjtStudentInfoService.queryByWxOpenId(wxOpenId);
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
			} else if (t == 102) {
				mv.setViewName("redirect:/pcenter/personal-information/index.html?" + FormSubmitUtil.createLinkString(searchParams));// 微信版学员答疑
			}
		}
		return mv;
	}

}
