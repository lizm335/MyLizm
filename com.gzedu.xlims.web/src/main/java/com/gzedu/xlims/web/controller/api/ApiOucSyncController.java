/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gzedu.xlims.service.api.ApiOucSyncService;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 
 * 功能说明：与学习网同步接口
 * 
 */
@Controller
@RequestMapping("/api/oucsync/")
public class ApiOucSyncController {
	
	@Autowired
	ApiOucSyncService apiOucSyncService;
	
	/**
	 * 同步学习网数据
	 * @param response
	 * @param request
	 * @return
	 */
	public Map addAPPDataSynch(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return apiOucSyncService.addAPPDataSynch(formMap);
	}
}
