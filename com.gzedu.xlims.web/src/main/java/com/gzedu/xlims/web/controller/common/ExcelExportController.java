package com.gzedu.xlims.web.controller.common;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 导出文件
 */
@Controller
@RequestMapping("/excelExport")
public class ExcelExportController extends BaseController {

	/**
	 * 导出文件前验证
	 * @param totalNum 查询结果条数
	 * @param formAction 导出表单路径
     * @return
     */
	@RequestMapping(value = "/validateSmsCode/{totalNum}",method = {RequestMethod.GET,RequestMethod.POST})
	public String scoreListExport(@PathVariable("totalNum") String totalNum,
								  @RequestParam("formAction") String formAction,
								  HttpServletRequest request, Model model){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		String phone = ObjectUtils.toString(user.getSjh());
		if(EmptyUtils.isNotEmpty(phone)){
			model.addAttribute("mobileNumber",phone.substring(phone.length()-4,phone.length()));
		}
		model.addAttribute("totalNum",totalNum);
		model.addAttribute("formAction",formAction);
		model.addAttribute("hasPermission", request.getSession().getAttribute("hasPermission"));
		return "excelExport/excel_export_validate_sms_code";
	}

	/**
	 * 获取验证码
	 * @param request
	 * @param feedback
	 * @return
	 */
	@SysLog("Excel导出-获取验证码")
	@RequestMapping(value = "getMessageCode",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Feedback getMessageCode(HttpServletRequest request, Feedback feedback){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String phone = ObjectUtils.toString(user.getSjh());
		if(EmptyUtils.isNotEmpty(phone)){
			Random rd = new Random();
			String code = ObjectUtils.toString(rd.nextInt(900000) + 100000);
			request.getSession().setAttribute(user.getSjh(), code); // 防止发送超时已经发送了而认为发送失败了，所以提前存储一份

			int smsResult = SMSUtil.sendTemplateMessageCode(phone, code, "gk");
			if(smsResult == 1){
				feedback.setSuccessful(true);
				feedback.setMessage("获取验证码成功！");
				feedback.setObj(code);
			} else {
				feedback.setSuccessful(false);
				feedback.setMessage("获取验证码失败！");
				feedback.setObj("");
			}
		} else {
			feedback.setSuccessful(false);
			feedback.setMessage("获取验证码失败！");
			feedback.setObj("");
		}
		return feedback;
	}

	/**
	 * 校验验证码
	 * @param request
	 * @param userCode
	 * @param feedback
	 * @return
	 */
	@RequestMapping(value = "getCheckCode",method = {RequestMethod.POST})
	@ResponseBody
	public Feedback getCheckCode(HttpServletRequest request,String userCode,Feedback feedback){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String code = ObjectUtils.toString(request.getSession().getAttribute(user.getSjh()),"");
		if(code.equals(userCode)){
			request.getSession().setAttribute("hasPermission",true);
			feedback.setSuccessful(true);
		}else {
			request.getSession().setAttribute("hasPermission",false);
			feedback.setSuccessful(false);
		}
		return feedback;
	}

}
