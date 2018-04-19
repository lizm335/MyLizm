/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.controller.purview;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gzedu.xlims.common.imgcode.CreateImageCode;

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
public class CodeController  {

	/**
	 * 生成验证码
	 * @throws IOException
     */
	@RequestMapping(value = "/vCode", method = RequestMethod.GET)
	public void vCode(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		int width = Integer.parseInt(request.getParameter("w")==null ? "158" :request.getParameter("w"));
		int height = Integer.parseInt(request.getParameter("h")==null ? "50" :request.getParameter("h"));

		// 设置响应的类型格式为图片格式
		response.setContentType("image/jpeg");
		//禁止图像缓存。
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		CreateImageCode vCode = new CreateImageCode(width, height);
		session.setAttribute("vCode", vCode.getCode());
		vCode.write(response.getOutputStream());
	}


}
