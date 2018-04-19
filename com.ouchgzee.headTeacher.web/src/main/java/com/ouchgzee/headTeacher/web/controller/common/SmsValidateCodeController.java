/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.common;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.ValidateUtil;
import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * 短信验证码验证控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年04月18日
 * @version 2.5
 */
@Controller
@RequestMapping("/home/common")
public class SmsValidateCodeController extends BaseController {
	private final static Logger log = LoggerFactory.getLogger(SmsValidateCodeController.class);

	/**
	 * 验证验证码
	 * 
	 * @return
	 */
	@RequestMapping(value = "toSmsValidateCode", method = RequestMethod.GET)
	public String updateHeadPortraitForm(Model model, HttpServletRequest request, HttpSession session) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		Integer smsCodeMark = (Integer) session.getAttribute(Servlets.SESSION_SMS_CODE_MARK_NAME);
		model.addAttribute("mobileNumber", employeeInfo.getSjh() != null && ValidateUtil.isMobile(employeeInfo.getSjh())
				? employeeInfo.getSjh().substring(7) : null);
		model.addAttribute("smsCodeMark", smsCodeMark);
		model.addAttribute("totalNum", request.getParameter("totalNum"));
		return "new/common/sms_validate_code";
	}

	/**
	 * 发送验证验证码
	 * 
	 * @return
	 */
	@RequestMapping(value = "sendSmsCode", method = RequestMethod.POST)
	@ResponseBody
	public Feedback sendSmsCode(Model model, HttpServletRequest request, HttpSession session) {
		Feedback feedback = new Feedback(true, "操作成功");
		try {
			BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
			if (employeeInfo.getSjh() != null && ValidateUtil.isMobile(employeeInfo.getSjh())) {
				String code = (100000 + new Random().nextInt(900000)) + "";
				String smsType = null;
				// 这个逻辑来源于旧的版本
				if (StringUtils.contains(employeeInfo.getGjtOrg().getTreeCode(), "00010011")) { // 深圳龙岗
					smsType = "lgzz";
				} else if (StringUtils.contains(employeeInfo.getGjtOrg().getTreeCode(), "00010007")) { // 国开广州
					smsType = "gk";
				}
				try {
					// 发送短信验证码
					SMSUtil.sendTemplateMessageCode(employeeInfo.getSjh(), code, smsType);
				} catch (Exception e) {
				}
				session.removeAttribute(Servlets.SESSION_SMS_CODE_NAME);
				session.setAttribute(Servlets.SESSION_SMS_CODE_NAME, code);
			} else {
				feedback = new Feedback(false, "手机号有误！");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "服务器异常！");
		}
		return feedback;
	}

	/**
	 * 验证验证码
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "doSmsValidateCode", method = RequestMethod.POST)
	@ResponseBody
	public Feedback doSmsValidateCode(@RequestParam("code") String code, Model model, HttpServletRequest request,
			HttpSession session) {
		Feedback feedback = new Feedback(true, "验证成功");
		try {
			Object scode = session.getAttribute(Servlets.SESSION_SMS_CODE_NAME);
			Assert.isTrue(code.equals(scode));
			session.setAttribute(Servlets.SESSION_SMS_CODE_MARK_NAME, 1);
		} catch (Exception e) {
			feedback = new Feedback(false, "验证码不正确！");
		}
		return feedback;
	}

	@RequestMapping(value = "doSmsNewValidateCode", method = RequestMethod.POST)
	@ResponseBody
	public Feedback doSmsNewValidateCode(@RequestParam("code") String code, Model model, HttpServletRequest request,
			HttpSession session) {
		Feedback feedback = new Feedback(true, "验证成功");
		try {
			Object scode = session.getAttribute(Servlets.SESSION_SMS_NEW_CODE_NAME);
			Assert.isTrue(code.equals(scode));
			session.setAttribute(Servlets.SESSION_SMS_NEW_CODE_MARK_NAME, 1);
		} catch (Exception e) {
			feedback = new Feedback(false, "验证码不正确！");
		}
		return feedback;
	}

	@RequestMapping(value = "sendMobilePhone", method = RequestMethod.POST)
	@ResponseBody
	public Feedback sendMobilePhone(String mobilePhone, String type, Model model, HttpServletRequest request,
			HttpSession session) {
		Feedback feedback = new Feedback(true, "发送短信成功");
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		if (StringUtils.isBlank(mobilePhone) || !ValidateUtil.isMobile(mobilePhone)) {
			feedback = new Feedback(false, "请填写正确的手机号码！");
		} else {
			if (!mobilePhone.equals(employeeInfo.getSjh()) && "1".equals(type)) {
				feedback = new Feedback(false, "原始手机号和传递的参数不一致，请联系管理员！");
			} else {
				String code = (100000 + new Random().nextInt(900000)) + "";
				String smsType = null;
				// 这个逻辑来源于旧的版本
				if (StringUtils.contains(employeeInfo.getGjtOrg().getTreeCode(), "00010011")) { // 深圳龙岗
					smsType = "lgzz";
				} else if (StringUtils.contains(employeeInfo.getGjtOrg().getTreeCode(), "00010007")) { // 国开广州
					smsType = "gk";
				}
				try {
					// 发送短信验证码
					int smsResult = SMSUtil.sendTemplateMessageCode(mobilePhone, code, smsType);
					log.info(" 发送短信验证码:mobilePhone={},code={},smsType={}", mobilePhone, code, smsType);
					if (smsResult != 1) {
						feedback = new Feedback(false, "发送短信失败，错误码：" + smsResult);
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					feedback = new Feedback(false, "服务器异常！");
				}
				if ("1".equals(type)) {
					session.removeAttribute(Servlets.SESSION_SMS_CODE_NAME);
					session.setAttribute(Servlets.SESSION_SMS_CODE_NAME, code);
				} else {
					session.removeAttribute(Servlets.SESSION_SMS_NEW_CODE_NAME);
					session.setAttribute(Servlets.SESSION_SMS_NEW_CODE_NAME, code);
				}
			}
		}

		return feedback;
	}

}
