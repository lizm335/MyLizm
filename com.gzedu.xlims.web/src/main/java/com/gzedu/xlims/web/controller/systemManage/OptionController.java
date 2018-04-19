/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.systemManage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.pojo.PriOperateInfo;
import com.gzedu.xlims.service.systemManage.PriOperateInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 
 * 功能说明： 操作管理，使用Restful风格的Urls
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月16日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/system/option")
public class OptionController {

	public final static Logger log = LoggerFactory.getLogger(OptionController.class);

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	PriOperateInfoService operateInfoService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, "createdDt", "DESC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<PriOperateInfo> pageInfo = operateInfoService.queryAll(searchParams, pageRequst);

		model.addAttribute("pageInfo", pageInfo);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnCreate", subject.isPermitted("/system/option/list$create"));
		model.addAttribute("isBtnUpdate", subject.isPermitted("/system/option/list$update"));
		model.addAttribute("isBtnDelete", subject.isPermitted("/system/option/list$delete"));

		return "systemManage/option/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", operateInfoService.queryBy(id));
		model.addAttribute("action", "view");
		return "systemManage/option/form";
	}

	@RequiresPermissions("/system/option/list$create")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("entity", new PriOperateInfo());
		model.addAttribute("action", "create");
		return "systemManage/option/form";
	}

	@RequiresPermissions("/system/option/list$create")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid PriOperateInfo entity, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			operateInfoService.insert(entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/system/option/list";
	}

	@RequiresPermissions("/system/option/list$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", operateInfoService.queryBy(id));
		model.addAttribute("action", "update");
		return "systemManage/option/form";
	}

	@RequiresPermissions("/system/option/list$update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("info") PriOperateInfo entity, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");

		PriOperateInfo modifyInfo = operateInfoService.queryBy(entity.getOperateId());
		modifyInfo.setOperateCode(entity.getOperateCode());
		modifyInfo.setOperateName(entity.getOperateName());
		modifyInfo.setOperateDec(entity.getOperateDec());

		try {
			operateInfoService.update(modifyInfo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/system/option/list";
	}

	@RequiresPermissions("/system/option/list$delete")
	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids, ServletResponse response) throws IOException {
		Feedback fb = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(ids)) {
			try {
				operateInfoService.delete(Arrays.asList(ids.split(",")));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				fb = new Feedback(false, "删除失败");
			}
		}
		return fb;
	}
}
