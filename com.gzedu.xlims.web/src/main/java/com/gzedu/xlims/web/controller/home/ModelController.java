/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.home;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
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

import com.gzedu.xlims.common.menu.SystemName;
import com.gzedu.xlims.constants.ShiroDbRealm;
import com.gzedu.xlims.pojo.PriModelInfo;
import com.gzedu.xlims.pojo.PriOperateInfo;
import com.gzedu.xlims.pojo.TreeModel;
import com.gzedu.xlims.service.model.ModelService;
import com.gzedu.xlims.service.systemManage.PriOperateInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月10日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/admin/model")
public class ModelController {

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	ModelService modelService;

	@Autowired
	PriOperateInfoService priOperateInfoService;
	
	@Autowired
	ShiroDbRealm shiroDbRealm;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "3") int pageSize, Model model, ServletRequest request) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<PriModelInfo> infos = modelService.queryMainModel(SystemName.办学组织管理平台.name(), searchParams, pageRequst);

		model.addAttribute("infos", infos);

		return "model/list";
	}

	@RequestMapping(value = "tree", method = RequestMethod.GET)
	public String tree(Model model, ServletRequest request) {
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE, "operateName", "ASC");
		Map<String, Object> searchParams = new HashMap<String, Object>();
		Page<PriOperateInfo> page = priOperateInfoService.queryAll(searchParams, pageRequst);
		List<PriOperateInfo> operateList = page.getContent();
		model.addAttribute("operateList", operateList);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnCreate", subject.isPermitted("/admin/model/tree$create"));
		model.addAttribute("isBtnUpdate", subject.isPermitted("/admin/model/tree$update"));
		model.addAttribute("isBtnDelete", subject.isPermitted("/admin/model/tree$delete"));

		return "model/tree";
	}

	@RequestMapping(value = "getTree", method = RequestMethod.GET)
	@ResponseBody
	public List<TreeModel> getTree() {
		return modelService.queryModelTree();
	}

	@RequiresPermissions("/admin/model/tree$create")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("info", new PriModelInfo());
		model.addAttribute("action", "create");
		return "model/form";
	}

	@RequestMapping(value = "get/{id}", method = RequestMethod.GET)
	@ResponseBody
	public PriModelInfo getForm(@PathVariable("id") String id) {
		PriModelInfo modelInfo = modelService.getModelInfo(id);

		// 无视下列属性
		modelInfo.setChildModelList(null);
		// modelInfo.setPriModelOperateList(null);
		modelInfo.setPriRoleInfos(null);
		modelInfo.setParentModel(null);

		return modelInfo;
	}

	@RequiresPermissions("/admin/model/tree$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("info", modelService.getModelInfo(id));
		model.addAttribute("action", "update");
		return "model/form";
	}

	@RequiresPermissions("/admin/model/tree$update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("info") PriModelInfo info, String[] operateId,
			RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");
		try {
			PriModelInfo modelInfo = modelService.getOne(info.getModelId());

			modelInfo.setModelName(info.getModelName());
			modelInfo.setIsLeaf(info.getIsLeaf());
			modelInfo.setModelCode(info.getModelCode());
			modelInfo.setOrderNo(info.getOrderNo());
			modelInfo.setModelAddress(info.getModelAddress());

			if (modelInfo.getIsLeaf()) {
				if (operateId != null) {
					List<PriOperateInfo> newOperateInfos = priOperateInfoService.queryAll(Arrays.asList(operateId));
					modelInfo.setPriOperateInfos(newOperateInfos);
				}
			} else {
				modelInfo.setModelAddress("#");
				modelInfo.setPriOperateInfos(new ArrayList<PriOperateInfo>());
			}

			modelService.updatePriModelInfo(modelInfo);
			//清除缓存
			shiroDbRealm.clearAllCachedAuthorizationInfo();
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/admin/model/tree";
	}

	@RequiresPermissions("/admin/model/tree$create")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid PriModelInfo info, String[] operateId, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "新增成功!!");
		try {
			if (info.getIsLeaf()) {
				if (operateId != null) {
					List<PriOperateInfo> newOperateInfos = priOperateInfoService.queryAll(Arrays.asList(operateId));
					info.setPriOperateInfos(newOperateInfos);
				}
			} else {
				info.setModelAddress("#");
				info.setPriOperateInfos(new ArrayList<PriOperateInfo>());
			}

			modelService.createPriModelInfo(info);
			//清除缓存
			shiroDbRealm.clearAllCachedAuthorizationInfo();
		} catch (Exception e) {
			feedback = new Feedback(false, "新增失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/admin/model/tree";
	}

	@RequiresPermissions("/admin/model/tree$delete")
	@RequestMapping(value = "delete")
	public String delete(PriModelInfo info, RedirectAttributes redirectAttributes) throws IOException {

		boolean b = modelService.deletePriModelInfo(info.getModelId());
		//清除缓存
		shiroDbRealm.clearAllCachedAuthorizationInfo();
		Feedback feedback = new Feedback(true, "删除成功");
		if (!b) {
			feedback = new Feedback(false, "删除失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/admin/model/tree";

	}
}
