/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.record;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.service.record.RecordServer;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 统计记录
 * 
 */
@Controller
@RequestMapping("/api/record")
public class RecordController {
	
	@Autowired
	RecordServer recordServer;
	
	/**
	 * 初始化考试预约数据
	 */
	@RequestMapping(value = "initRecordAppointment")
	@ResponseBody
	public Map initRecordAppointment(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return recordServer.initRecordAppointment(formMap);
	}
}
