/**
 * Copyright(c) 2013 版权归属广州远程教育中心 www.969300.com
 */
package com.ouchgzee.study.web.controller.mobile;

import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.ResponseModel;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.ouchgzee.study.web.common.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * H5登录控制层
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年12月07日
 * @version 2.5
 *
 */
@Controller("mobileLoginController")
@RequestMapping("/sso")
public class LoginController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtClassStudentService gjtClassStudentService;

	/**
	 * 新单点登录接口-使用学员ID<br>
	 * @param username 账号(学号/身份证号/手机号)
	 * @param password 密码
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel login(String username, String password,
			HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) throws Exception {
		// 进入登录页面销毁当前登录用户
		Subject account = SecurityUtils.getSubject();
		if (SecurityUtils.getSubject().getSession() != null) {
			account.logout();
		}
		GjtStudentInfo student = gjtStudentInfoService.querySSOByXhOrSfzhOrSjh(username);
		if (student == null || student.getGjtUserAccount() == null) {
			throw new CommonException(MessageCode.PERMISSION_DENIED, "账号不存在！");
		}
		UsernamePasswordToken token = new UsernamePasswordToken(student.getGjtUserAccount().getLoginAccount(),
				Md5Util.encrypt(password));
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
			throw new CommonException(MessageCode.PERMISSION_DENIED, "密码不正确！");
		}
		return new ResponseModel(MessageCode.RESP_OK);
	}
	
}
