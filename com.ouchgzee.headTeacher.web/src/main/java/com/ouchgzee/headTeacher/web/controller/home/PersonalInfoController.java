/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.service.employee.BzrGjtEmployeeInfoService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * 个人管理控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月19日
 * @version 2.5
 */
@Controller
@RequestMapping("/home/personal")
public class PersonalInfoController extends BaseController {

	private final static Logger log = LoggerFactory.getLogger(PersonalInfoController.class);

	@Autowired
	private BzrGjtEmployeeInfoService gjtEmployeeInfoService;

	/**
	 * 加载教职工个人信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateInfo", method = RequestMethod.GET)
	public String updateInfoForm(Model model, HttpSession session) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		model.addAttribute("info", gjtEmployeeInfoService.queryById(employeeInfo.getEmployeeId()));
		model.addAttribute("action", "update");
		return "/new/personal/update_info";
	}

	/**
	 * 修改教职工个人信息
	 * 
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "updateInfo", method = RequestMethod.POST)
	public String updateInfo(BzrGjtEmployeeInfo info, HttpSession session, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");

		try {
			BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
			BzrGjtEmployeeInfo modifyInfo = gjtEmployeeInfoService.queryById(employeeInfo.getEmployeeId());
			modifyInfo.setXm(info.getXm());
			modifyInfo.setXbm(info.getXbm());
			// modifyInfo.setSjh(info.getSjh());
			modifyInfo.setDzxx(info.getDzxx());
			modifyInfo.setQq(info.getQq());
			modifyInfo.setLxdh(info.getLxdh());
			modifyInfo.setIndividualitySign(info.getIndividualitySign());
			modifyInfo.setUpdatedBy(employeeInfo.getGjtUserAccount().getId());

			gjtEmployeeInfoService.update(modifyInfo);
			// Session更新登录用户数据
			modifyInfo.setManageClassList(getUser(session).getManageClassList());
			session.setAttribute(Servlets.SESSION_EMPLOYEE_NAME, modifyInfo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/home/personal/updateInfo";
	}

	/**
	 * 加载修改密码
	 * 
	 * @return
	 */
	@RequestMapping(value = "updatePwd", method = RequestMethod.GET)
	public String updatePwdForm(Model model, HttpSession session) {
		model.addAttribute("action", "update");
		return "/new/personal/update_pwd";
	}

	/**
	 * 修改密码
	 * 
	 * @param newPassword
	 * @param oldPassword
	 * @return
	 */
	@RequestMapping(value = "updatePwd", method = RequestMethod.POST)
	public String updatePwd(@RequestParam(value = "password") String newPassword,
			@RequestParam(value = "oldPassword") String oldPassword, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "密码修改成功");

		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		try {
			int code = gjtEmployeeInfoService.updatePassword(employeeInfo.getGjtUserAccount().getId(), newPassword,
					oldPassword, employeeInfo.getGjtUserAccount().getId());
			if (code == 1) {

			} else if (code == -1) {
				feedback = new Feedback(false, "原密码不正确");
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/home/personal/updatePwd";
	}

	/**
	 * 修改手机号
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "updateMobilePhone", method = RequestMethod.GET)
	public String updateMobilePhone(Model model, HttpSession session) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		model.addAttribute("info", gjtEmployeeInfoService.queryById(employeeInfo.getEmployeeId()));
		model.addAttribute("action", "update");
		return "/new/personal/update_Phone";
	}

	@RequestMapping(value = "updateMobilePhone", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updateMobilePhone(String oldSjh, String oldPassword, HttpSession session, String oldVerifyCode,
			String newVerifyCode, String newSjh, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "手机更换修改成功！");

		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		try {
			String oldCode = (String) session.getAttribute(Servlets.SESSION_SMS_CODE_NAME);// 旧验证码
			String newCode = (String) session.getAttribute(Servlets.SESSION_SMS_NEW_CODE_NAME);// 新验证码

			if (oldCode.equals(oldVerifyCode) && newCode.equals(newVerifyCode)) {
				employeeInfo.setSjh(newSjh);
				boolean update = gjtEmployeeInfoService.update(employeeInfo);
				if (update) {
					// Session更新登录用户数据
					BzrGjtEmployeeInfo modifyInfo = gjtEmployeeInfoService.queryById(employeeInfo.getEmployeeId());
					modifyInfo.setManageClassList(getUser(session).getManageClassList());
					session.setAttribute(Servlets.SESSION_EMPLOYEE_NAME, modifyInfo);
				} else {
					feedback = new Feedback(false, "修改失败");
				}
			} else {
				feedback = new Feedback(false, "验证码不一致");
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "更新失败");
		}
		return feedback;
	}

	/**
	 * 加载头像
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateHeadPortrait", method = RequestMethod.GET)
	public String updateHeadPortraitForm(Model model, HttpServletRequest request, HttpSession session) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		model.addAttribute("info", gjtEmployeeInfoService.queryById(employeeInfo.getEmployeeId()));
		model.addAttribute("action", "update");
		return "/new/personal/update_headPortrait";
	}

	/**
	 * 修改头像
	 * 
	 * @param zp
	 * @return
	 */
	@RequestMapping(value = "updateHeadPortrait", method = RequestMethod.POST)
	public String updateHeadPortrait(@RequestParam(value = "zp", defaultValue = "") String zp, HttpSession session,
			RedirectAttributes redirectAttributes) {
		if (StringUtils.isNotBlank(zp)) {
			Feedback feedback = new Feedback(true, "头像更换成功");

			BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
			try {
				gjtEmployeeInfoService.updatePhoto(employeeInfo.getEmployeeId(), zp,
						employeeInfo.getGjtUserAccount().getId());
				// Session更新登录用户数据
				BzrGjtEmployeeInfo modifyInfo = gjtEmployeeInfoService.queryById(employeeInfo.getEmployeeId());
				modifyInfo.setManageClassList(getUser(session).getManageClassList());
				session.setAttribute(Servlets.SESSION_EMPLOYEE_NAME, modifyInfo);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				feedback = new Feedback(false, "更新失败");
			}
			redirectAttributes.addFlashAttribute("feedback", feedback);
		} else {
			// 头像未更新，则啥都不做
		}
		return "redirect:/home/personal/updateHeadPortrait";
	}

}
