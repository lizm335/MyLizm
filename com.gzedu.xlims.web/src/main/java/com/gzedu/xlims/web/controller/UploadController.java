/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gzedu.xlims.common.AppConfig;

/**
 * 
 * 功能说明：远程文件上传回调控制器
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月13日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Controller
@RequestMapping("/upload")
public class UploadController {

	@RequestMapping(value = "head", method = RequestMethod.GET)
	public String updateHeadPortraitForm(Model model, HttpServletRequest request, HttpSession session) {
		String eforgeServer = AppConfig.getProperty("file.upload.url.server");
		String rtpath = ObjectUtils.toString(request.getParameter("rtpath"));
		String rtid = ObjectUtils.toString(request.getParameter("rtid"));
		String fileName = ObjectUtils.toString(request.getParameter("fileName"));
		String opentype = ObjectUtils.toString(request.getParameter("opentype"));
		String picPath = eforgeServer + rtpath;

		model.addAttribute("rtpath", rtpath);
		model.addAttribute("rtid", rtid);
		model.addAttribute("fileName", fileName);
		model.addAttribute("picPath", picPath);
		return "upload/uploadHead";
	}

	@RequestMapping(value = "thesis", method = RequestMethod.GET)
	public String updateThesisPortraitForm(Model model, HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException {
		String eforgeServer = AppConfig.getProperty("file.upload.url.server");
		String rtpath = ObjectUtils.toString(request.getParameter("rtpath"));
		String rtid = ObjectUtils.toString(request.getParameter("rtid"));
		String fileName = ObjectUtils.toString(request.getParameter("fileName"));
		String opentype = ObjectUtils.toString(request.getParameter("opentype"));
		String picPath = eforgeServer + rtpath;

		model.addAttribute("rtpath", rtpath);
		model.addAttribute("rtid", rtid);
		model.addAttribute("fileName", URLEncoder.encode(fileName, "UTF-8"));
		model.addAttribute("picPath", picPath);
		return "upload/uploadThesis";
	}

}
