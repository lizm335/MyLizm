/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.pageConfig;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.pojo.GjtPageDef;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.pageConfig.GjtPageConfigService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年8月12日
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/pageConfig")
public class PageConfigController {

	private final static Logger log = LoggerFactory.getLogger(PageConfigController.class);

	@Autowired
	GjtPageConfigService gjtPageConfigService;
	@Autowired
	private CommonMapService commonMapService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request,
			HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GjtPageDef> lists = gjtPageConfigService.queryPageList(searchParams, pageRequst);
		for (Iterator<GjtPageDef> iterator = lists.iterator(); iterator.hasNext();) {
			GjtPageDef gjtPageDef = iterator.next();
			gjtPageDef.setOrgName(commonMapService.getXxmcDates(gjtPageDef.getXxId()).get(gjtPageDef.getXxId()));
		}
		model.addAttribute("infos", lists);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnCreate", subject.isPermitted("/pageConfig/list$create"));
		model.addAttribute("isBtnUpdate", subject.isPermitted("/pageConfig/list$update"));
		// model.addAttribute("isBtnDelete",
		// subject.isPermitted("/pageConfig/list$delete"));

		return "/pageConfig/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewInfo(@PathVariable("id") String id, HttpServletRequest request, Model model) {
		Map<String, String> xxmcMap = commonMapService.getXxmcAllDates();
		model.addAttribute("xxmcMap", xxmcMap);
		model.addAttribute("gjtPageDef", gjtPageConfigService.queryById(id));
		model.addAttribute("action", "view");
		return "/pageConfig/form";
	}

	/**
	 * 加载新增页面配置
	 * 
	 * @return
	 */
	@RequiresPermissions("/pageConfig/list$create")
	@RequestMapping(value = "toCreatePageConfig", method = RequestMethod.GET)
	public String toCreatePageConfig(Model model, HttpServletRequest request) {
		Map<String, String> xxmcMap = commonMapService.getXxmcAllDates();
		model.addAttribute("xxmcMap", xxmcMap);
		model.addAttribute("gjtPageDef", new GjtPageDef());
		model.addAttribute("action", "doCreatePageConfig");
		return "/pageConfig/form";
	}

	/**
	 * 创建页面配置
	 * 
	 * @return
	 */
	@RequiresPermissions("/pageConfig/list$create")
	@RequestMapping(value = "doCreatePageConfig", method = RequestMethod.POST)
	public String doCreatePageConfig(ServletRequest request, RedirectAttributes redirectAttributes,
			@Valid GjtPageDef gjtPageDef) {
		Feedback feedback = new Feedback(true, "创建成功");
		gjtPageDef.setId(UUIDUtils.create());
		try {
			gjtPageConfigService.savePageConfig(gjtPageDef);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/pageConfig/list";
	}

	@RequiresPermissions("/pageConfig/list$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		Map<String, String> xxmcMap = commonMapService.getXxmcAllDates();
		model.addAttribute("xxmcMap", xxmcMap);
		model.addAttribute("gjtPageDef", gjtPageConfigService.queryById(id));
		model.addAttribute("action", "doUpdatePageConfig");
		return "/pageConfig/form";
	}

	@RequiresPermissions("/pageConfig/list$update")
	@RequestMapping(value = "doUpdatePageConfig", method = RequestMethod.POST)
	public String doUpdatePageConfig(ServletRequest request, RedirectAttributes redirectAttributes,
			@Valid GjtPageDef gjtPageDef) {
		Feedback feedback = new Feedback(true, "修改成功");
		try {
			gjtPageConfigService.savePageConfig(gjtPageDef);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "修改失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/pageConfig/list";
	}
}
