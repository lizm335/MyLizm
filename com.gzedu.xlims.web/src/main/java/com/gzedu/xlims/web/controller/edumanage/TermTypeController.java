/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtTermType;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtTermTypeService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 
 * 功能说明：成绩
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月12日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/edumanage/termType")
public class TermTypeController {

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtTermTypeService gjtTermTypeService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, new Sort(Direction.ASC, "ordNo"));
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<GjtTermType> pageInfo = gjtTermTypeService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);

		model.addAttribute("pageInfo", pageInfo);
		
		
		return "edumanage/termType/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", gjtTermTypeService.queryBy(id));
		model.addAttribute("action", "view");
		return "edumanage/termType/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		model.addAttribute("entity", new GjtTermType());
		model.addAttribute("action", "create");
		return "edumanage/termType/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtTermType entity, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		entity.setXxId(user.getGjtOrg().getId());
		try {
			gjtTermTypeService.insert(entity);
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/termType/list";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		model.addAttribute("entity", gjtTermTypeService.queryBy(id));
		model.addAttribute("action", "update");
		return "edumanage/termType/form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("info") GjtTermType entity, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");

		GjtTermType modifyInfo = gjtTermTypeService.queryBy(entity.getId());

		modifyInfo.setName(entity.getName());
		modifyInfo.setOrdNo(entity.getOrdNo());

		try {
			gjtTermTypeService.update(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/termType/list";
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, ServletResponse response) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			try {
				gjtTermTypeService.delete(Arrays.asList(ids.split(",")));
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

}
