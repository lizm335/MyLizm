/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.classManager.edu;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.annotation.SysLog;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;
import com.ouchgzee.headTeacher.service.account.BzrGjtUserAccountService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * 学员学籍控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年10月08日
 * @version 2.5
 */
@Controller
@RequestMapping("/home/class/edu/roll")
public class RollController extends BaseController {

	@Autowired
	private BzrGjtStudentService gjtStudentService;
	@Autowired
	private BzrGjtUserAccountService gjtUserAccountService;

	/**
	 * 重置密码
	 * 
	 * @param studentId
	 * @return
	 */
	@SysLog("教学教务服务-学员信息-重置密码")
	@RequestMapping(value = "resetPwd", method = RequestMethod.POST)
	@ResponseBody
	public Feedback resetPwd(@RequestParam String studentId, @RequestParam String type, HttpSession session) {
		Feedback feedback = new Feedback(true, "重置成功，重置后的密码为" + Constants.STUDENT_ACCOUNT_PWD_DEFAULT);
		try {
			BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
			if ("1".equals(type)) {
				boolean flag = gjtStudentService.updateStudentResetPwd(studentId,
						employeeInfo.getGjtUserAccount().getId());
				Assert.isTrue(flag);
			} else if ("2".equals(type)) {
				boolean flag = gjtStudentService.updateStudentResetPwdNew(studentId,
						employeeInfo.getGjtUserAccount().getId());
				Assert.isTrue(flag);
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "重置失败");
		}
		return feedback;
	}

	/**
	 * 模拟登录
	 * 
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "simulationLogin", method = RequestMethod.GET)
	public void simulationLogin(@RequestParam String studentId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, RedirectAttributes redirectAttributes)
			throws IOException {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);

		BzrGjtStudentInfo studentInfo = gjtStudentService.queryById(studentId);

		try {
			if ("696c75a310b44a7f9dd61516ea84fbe3".equals(employeeInfo.getXxId())) { // 广州电大
				/*
				 * SSOUtil sso = new SSOUtil(); String p =
				 * sso.encrypt(SSOUtil.GZDD_APP_ID, studentInfo.getXh()); String
				 * url = AppConfig.getProperty("gzddServer") +
				 * AppConfig.getProperty("sso.login.url"); url += "?p=" + p +
				 * "&s=" + studentInfo.getStudentId();
				 * response.sendRedirect(url);
				 */
			} else if ("2f5bfcce71fa462b8e1f65bcd0f4c632".equals(employeeInfo.getXxId())) { // 国开
				String url = gjtUserAccountService.studentSimulation(studentInfo.getStudentId(), studentInfo.getXh());
				response.sendRedirect(url);
			} else {
				super.outputJsAlertCloseWindow(response, "非国开和电大的班主任，无权限模拟登录！");
			}
		} catch (Exception e) {
			super.outputJsAlertCloseWindow(response, "服务器异常，请稍后再试！");
		}
	}

}
