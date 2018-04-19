/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.web.controller.systemManage;

import com.ouchgzee.headTeacher.pojo.BzrPriOperateInfo;
import com.ouchgzee.headTeacher.service.systemManage.BzrPriOperateInfoService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

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
public class OptionController extends BaseController {

	@Autowired
	BzrPriOperateInfoService operateInfoService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
						@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
					   	HttpServletRequest request) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<BzrPriOperateInfo> pageInfo = operateInfoService.queryAll(searchParams, pageRequst);

		model.addAttribute("pageInfo", pageInfo);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "systemManage/option/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", operateInfoService.queryBy(id));
		model.addAttribute("action", "view");
		return "systemManage/option/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("entity", new BzrPriOperateInfo());
		model.addAttribute("action", "create");
		return "systemManage/option/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid BzrPriOperateInfo entity, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			operateInfoService.insert(entity);
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/system/option/list";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", operateInfoService.queryBy(id));
		model.addAttribute("action", "update");
		return "systemManage/option/form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("info") BzrPriOperateInfo entity, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");

		BzrPriOperateInfo modifyInfo = operateInfoService.queryBy(entity.getOperateId());
		modifyInfo.setOperateCode(entity.getOperateCode());
		modifyInfo.setOperateName(entity.getOperateName());
		modifyInfo.setOperateDec(entity.getOperateDec());

		try {
			operateInfoService.update(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/system/option/list";
	}

	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids, ServletResponse response) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			try {
				operateInfoService.delete(Arrays.asList(ids.split(",")));
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}
}
