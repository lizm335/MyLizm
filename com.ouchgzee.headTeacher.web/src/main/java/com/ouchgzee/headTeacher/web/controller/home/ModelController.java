/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.web.controller.home;

import com.gzedu.xlims.common.menu.SystemName;
import com.ouchgzee.headTeacher.pojo.BzrPriModelInfo;
import com.ouchgzee.headTeacher.pojo.BzrPriOperateInfo;
import com.ouchgzee.headTeacher.pojo.BzrTreeModel;
import com.ouchgzee.headTeacher.service.account.BzrGjtUserAccountService;
import com.ouchgzee.headTeacher.service.model.BzrModelService;
import com.ouchgzee.headTeacher.service.systemManage.BzrPriOperateInfoService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月10日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/home/model")
public class ModelController extends BaseController {

	@Autowired
	BzrGjtUserAccountService userAccountService;

	@Autowired
	BzrModelService modelService;

	@Autowired
	BzrPriOperateInfoService priOperateInfoService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
					   @RequestParam(value = "page.size", defaultValue = "3") int pageSize, Model model, ServletRequest request) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<BzrPriModelInfo> infos = modelService.queryMainModel(SystemName.办学组织管理平台.name(), searchParams, pageRequst);

		model.addAttribute("infos", infos);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "model/list";
	}

	@RequestMapping(value = "tree", method = RequestMethod.GET)
	public String tree(Model model, ServletRequest request) {
		List<BzrPriOperateInfo> operateList = priOperateInfoService.queryAll();
		model.addAttribute("operateList", operateList);
		return "model/tree";
	}

	@RequestMapping(value = "getTree", method = RequestMethod.GET)
	@ResponseBody
	public List<BzrTreeModel> getTree() {
		return modelService.queryModelTree();
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("info", new BzrPriModelInfo());
		model.addAttribute("action", "create");
		return "model/form";
	}

	@RequestMapping(value = "get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public BzrPriModelInfo getForm(@PathVariable("id") String id) {
		BzrPriModelInfo modelInfo = modelService.getModelInfo(id);

		// 无视下列属性
		modelInfo.setChildModelList(null);
		// modelInfo.setPriModelOperateList(null);
		modelInfo.setPriRoleInfos(null);
		modelInfo.setParentModel(null);

		return modelInfo;
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("info", modelService.getModelInfo(id));
		model.addAttribute("action", "update");
		return "model/form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("info") BzrPriModelInfo info, String[] operateId,
						 RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");
		try {
			BzrPriModelInfo modelInfo = modelService.getOne(info.getModelId());

			modelInfo.setModelName(info.getModelName());
			modelInfo.setIsLeaf(info.getIsLeaf());
			modelInfo.setModelCode(info.getModelCode());
			modelInfo.setOrderNo(info.getOrderNo());
			modelInfo.setModelAddress(info.getModelAddress());

			if (modelInfo.getIsLeaf()) {
				if (operateId != null) {
					List<BzrPriOperateInfo> newOperateInfos = priOperateInfoService.queryAll(Arrays.asList(operateId));
					modelInfo.setPriOperateInfos(newOperateInfos);
				}
			} else {
				modelInfo.setModelAddress("#");
				modelInfo.setPriOperateInfos(new ArrayList<BzrPriOperateInfo>());
			}

			modelService.updatePriModelInfo(modelInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/admin/model/tree";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid BzrPriModelInfo info, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "新增成功!!");
		try {
			modelService.createPriModelInfo(info);
		} catch (Exception e) {
			feedback = new Feedback(false, "新增失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/admin/model/tree";
	}

	@RequestMapping(value = "delete")
	public String delete(BzrPriModelInfo info, RedirectAttributes redirectAttributes) throws IOException {

		boolean b = modelService.deletePriModelInfo(info.getModelId());
		Feedback feedback = new Feedback(true, "删除成功");
		if (!b) {
			feedback = new Feedback(false, "删除失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/admin/model/tree";

	}
}
