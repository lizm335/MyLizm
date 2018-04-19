/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gzedu.xlims.common.menu.SystemName;
import com.gzedu.xlims.pojo.PriModelInfo;
import com.gzedu.xlims.service.model.ModelService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月10日
 * @version 2.5
 *
 */
@Controller
@RequestMapping(value = "/index")
public class IndexController {

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	ModelService modelService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model, HttpServletResponse response, HttpSession session) {
		// 首页
		List<PriModelInfo> modelInfoList = modelService.queryMainModel(SystemName.办学组织管理平台.name());

		model.put("modelInfoList", modelInfoList);
		return "/main_index";
	}
}
