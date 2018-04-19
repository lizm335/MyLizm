/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.systemManage;

import com.gzedu.xlims.pojo.SysLogEntity;
import com.gzedu.xlims.service.SysLogService;
import com.gzedu.xlims.web.common.Servlets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * 功能说明： 系统日志管理，使用Restful风格的Urls
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年07月24日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/system/log")
public class SysLogController {

	public final static Logger log = LoggerFactory.getLogger(OptionController.class);

	@Autowired
	private SysLogService sysLogService;

	@RequestMapping(value = "list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
					   @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
					   HttpServletRequest request) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, "createDate", "DESC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<SysLogEntity> pageInfo = sysLogService.queryByPage(searchParams, pageRequst);

		model.addAttribute("pageInfo", pageInfo);
		return "systemManage/log/list";
	}
}
