/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.web.controller.systemManage;

import com.ouchgzee.headTeacher.pojo.BzrPriModelInfo;
import com.ouchgzee.headTeacher.pojo.BzrPriRoleInfo;
import com.ouchgzee.headTeacher.pojo.BzrTreeModel;
import com.ouchgzee.headTeacher.service.model.BzrModelService;
import com.ouchgzee.headTeacher.service.usermanage.BzrPriRoleInfoService;
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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
@RequestMapping("/system/role")
public class RoleController extends BaseController {

	@Autowired
	BzrPriRoleInfoService roleInfoService;

	@Autowired
	BzrModelService modelService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, "roleCode", "ASC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<BzrPriRoleInfo> pageInfo = roleInfoService.queryAll(searchParams, pageRequst);

		model.addAttribute("pageInfo", pageInfo);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "systemManage/role/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", roleInfoService.getOne(id));
		model.addAttribute("action", "view");
		return "systemManage/role/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("entity", new BzrPriRoleInfo());
		model.addAttribute("action", "create");
		return "systemManage/role/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid BzrPriRoleInfo entity, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			roleInfoService.insert(entity);
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/system/role/list";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		BzrPriRoleInfo roleInfo = roleInfoService.getOne(id);

		model.addAttribute("entity", roleInfo);
		model.addAttribute("action", "update");
		return "systemManage/role/form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("info") BzrPriRoleInfo entity, String modelIds, ServletRequest request,
						 RedirectAttributes redirectAttributes) {

		Feedback feedback = new Feedback(true, "更新成功");
		List<BzrPriModelInfo> selectedModelList = new ArrayList();
		BzrPriRoleInfo modifyInfo = roleInfoService.getOne(entity.getRoleId());

		// 被选中的菜单
		if (StringUtils.isNotBlank(modelIds)) {
			selectedModelList = modelService.queryAll(Arrays.asList(modelIds.split(",")));
		}
		modifyInfo.setPriModelInfos(selectedModelList);

		modifyInfo.setRoleCode(entity.getRoleCode());
		modifyInfo.setRoleName(entity.getRoleName());

		try {
			roleInfoService.update(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/system/role/list";
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, ServletResponse response) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			try {
				roleInfoService.delete(Arrays.asList(ids.split(",")));
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	@RequestMapping(value = "getTree/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<BzrTreeModel> getTree(@PathVariable("id") String id) {
		BzrPriRoleInfo roleInfo = roleInfoService.getOne(id);
		List<BzrTreeModel> tree = modelService.queryModelTree();
		List<BzrPriModelInfo> infos = roleInfo.getPriModelInfos();
		for (BzrPriModelInfo model : infos) {
			walk(tree, model);
		}

		return tree;
	}

	private void walk(List<BzrTreeModel> modelList, BzrPriModelInfo model) {
		for (BzrTreeModel treeModel : modelList) {
			if (treeModel.getId().equals(model.getModelId())) {
				treeModel.getState().setChecked(true);
				break;
			}
			if (treeModel.getNodes() != null) {
				walk(treeModel.getNodes(), model);
			}
		}
	}

}
