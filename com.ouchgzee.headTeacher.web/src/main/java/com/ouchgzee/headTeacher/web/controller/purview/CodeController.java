/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.purview;

import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.gzedu.xlims.common.imgcode.CreateImageCode;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * 验证码生成控制器<br/>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月13日
 * @version 2.5
 * @since JDK 1.7
 */
@Controller
public class CodeController extends BaseController {

	/**
	 * 生成验证码
	 * @throws IOException
     */
	@RequestMapping(value = "/vCode", method = RequestMethod.GET)
	public void vCode(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		int width = Integer.parseInt(request.getParameter("w")==null ? "158" :request.getParameter("w"));
		int height = Integer.parseInt(request.getParameter("h")==null ? "50" :request.getParameter("h"));

		// 设置响应的类型格式为图片格式
		response.setContentType("image/png");
		//禁止图像缓存。
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		CreateImageCode vCode = new CreateImageCode(width, height);
		session.setAttribute(Servlets.SESSION_VCODE_NAME, vCode.getCode());
		vCode.write(response.getOutputStream());
	}

	/**
	 * 导入导出通用获取验证码
	 * @param request
	 * @param feedback
	 * @return
	 */
	@RequestMapping(value = "/common/getMessageCode",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Feedback getMessageCode(HttpServletRequest request, Feedback feedback){
		BzrGjtEmployeeInfo user = getUser(request.getSession());
		String phone = ObjectUtils.toString(user.getSjh());
		Random rd = new Random();
		String code = ObjectUtils.toString(rd.nextInt(10))+
				ObjectUtils.toString(rd.nextInt(10))+
				ObjectUtils.toString(rd.nextInt(10))+
				ObjectUtils.toString(rd.nextInt(10))+
				ObjectUtils.toString(rd.nextInt(10))+
				ObjectUtils.toString(rd.nextInt(10));
		int smsResult = SMSUtil.sendTemplateMessageCode(phone, code, "gk");
		if(smsResult == 1){
			request.getSession().setAttribute(user.getSjh(),code);
			feedback.setSuccessful(true);
			feedback.setMessage("获取验证码成功！");
		}else {
			feedback.setSuccessful(false);
			feedback.setMessage("获取验证码失败！");
		}

		return feedback;
	}


	/**
	 * 导入导出通用校验验证码
	 * @param request
	 * @param userCode
	 * @param feedback
	 * @return
	 */
	@RequestMapping(value = "/common/getCheckCode",method = {RequestMethod.POST})
	@ResponseBody
	public Feedback getCheckCode(HttpServletRequest request, String userCode, Feedback feedback){
		BzrGjtEmployeeInfo user = getUser(request.getSession());
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
