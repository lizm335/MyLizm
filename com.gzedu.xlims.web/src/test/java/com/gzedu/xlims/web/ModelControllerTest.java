///**
// * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
// */
//package com.gzedu.xlims.web;
//
//import java.io.IOException;
//import java.util.Map;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.validation.Valid;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.gzedu.xlims.common.menu.SystemName;
//import com.gzedu.xlims.pojo.PriModelInfo;
//import com.gzedu.xlims.service.model.ModelService;
//import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
//import com.gzedu.xlims.web.common.Feedback;
//import com.gzedu.xlims.web.common.Servlets;
//
///**
// * 功能说明：方便复制的版本
// *
// * @author 李明 liming@eenet.com
// * @Date 2016年5月10日
// * @version 2.5
// *
// */
//@Controller
//@RequestMapping("/admin/test")
//public class ModelControllerTest {
//
//	@Autowired
//	GjtUserAccountService gjtUserAccountService;
//
//	@Autowired
//	ModelService modelService;
//
//	@RequestMapping(value = "list", method = RequestMethod.GET)
//	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
//			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request) {
//		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
//		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
//		Page<PriModelInfo> infos = modelService.queryMainModel(SystemName.办学组织管理平台.name(), searchParams, pageRequst);
//		model.addAttribute("infos", infos);
//
//
//		return "model/list";
//	}
//
//	@RequestMapping(value = "create", method = RequestMethod.GET)
//	public String createForm(Model model) {
//		model.addAttribute("info", new PriModelInfo());
//		model.addAttribute("action", "create");
//		return "model/form";
//	}
//
//	@RequestMapping(value = "create", method = RequestMethod.POST)
//	public String create(@Valid PriModelInfo newModelInfo, RedirectAttributes redirectAttributes) {
//
//		Feedback feedback = new Feedback(true, "成功");
//		redirectAttributes.addFlashAttribute("feedback", feedback);
//		return "redirect:/admin/model/list";
//	}
//
//	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
//	public String updateForm(@PathVariable("id") String id, Model model) {
//		model.addAttribute("info", modelService.getModelInfo(id));
//		model.addAttribute("action", "update");
//		return "model/form";
//	}
//
//	@RequestMapping(value = "update", method = RequestMethod.POST)
//	public String update(@Valid @ModelAttribute("info") PriModelInfo info, RedirectAttributes redirectAttributes) {
//		// service.add(info);
//		Feedback feedback = new Feedback(false, "更新失败");
//		redirectAttributes.addFlashAttribute("feedback", feedback);
//		return "redirect:/admin/model/list";
//	}
//
//	@RequestMapping(value = "delete")
//	public @ResponseBody Feedback delete(String ids, ServletResponse response) throws IOException {
//		String[] selectedIds = null;
//		if (StringUtils.isNotBlank(ids)) {
//			selectedIds = ids.split(",");
//		}
//		Feedback fb = new Feedback(true, "成功");
//		return fb;
//	}
//}
