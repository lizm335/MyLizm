/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.systemManage;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.constants.ShiroDbRealm;
import com.gzedu.xlims.pojo.*;
import com.gzedu.xlims.pojo.status.PriRoleInfoEnum;
import com.gzedu.xlims.service.model.ModelService;
import com.gzedu.xlims.service.model.PriRoleOperateService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoRunService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoWorkService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
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
public class RoleController {

	public static Logger log = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	PriRoleInfoService roleInfoService;

	@Autowired
	ModelService modelService;

	@Autowired
	PriRoleOperateService priRoleOperateService;

	@Autowired
	private ShiroDbRealm shiroDbRealm;

	@Autowired
	PriRoleInfoRunService priRoleInfoRunService;

	@Autowired
	PriRoleInfoWorkService priRoleInfoWorkService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, "roleName", "ASC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<PriRoleInfo> pageInfo = roleInfoService.queryAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnCreate", subject.isPermitted("/system/role/list$create"));
		model.addAttribute("isBtnUpdate", subject.isPermitted("/system/role/list$update"));
		model.addAttribute("isBtnDelete", subject.isPermitted("/system/role/list$delete"));

		return "systemManage/role/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", roleInfoService.queryById(id));
		model.addAttribute("action", "view");
		return "systemManage/role/form";
	}

	@RequiresPermissions("/system/role/list$create")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		List<PriRoleInfo> roleList = roleInfoService.queryAll();

		model.addAttribute("roleList", roleList);
		model.addAttribute("entity", new PriRoleInfo());
		model.addAttribute("isUpdateCode", true);
		model.addAttribute("action", "create");
		return "systemManage/role/form";
	}

	@RequestMapping(value = "roleManage/{id}", method = RequestMethod.GET)
	public String roleManage(Model model, @PathVariable("id")String id) {
		PriRoleInfo roleInfo = roleInfoService.queryById(id);
		List<PriRoleInfo> roleList = roleInfoService.queryAll();
		List<PriRoleInfoRun> roleInfoList = roleInfo.getPriRoleInfoList();
		List<PriRoleInfoWork> workRoleInfoList = roleInfo.getWorkRoleInfoList();
		for (PriRoleInfo item : roleList) {
			for (PriRoleInfoRun priRoleInfoRun : roleInfoList) {
				if (item.getRoleId().equals(priRoleInfoRun.getPriRoleInfo().getRoleId())) {
					item.setIsCheck(true);
				}
			}

			for (PriRoleInfoWork priRoleInfoWork : workRoleInfoList) {
				if (item.getRoleId().equals(priRoleInfoWork.getPriRoleInfo().getRoleId())) {
					item.setIsWorkCheck(true);
				}
			}
		}

		model.addAttribute("roleList", roleList);
		model.addAttribute("entity", roleInfo);
		return "systemManage/role/roleManage";
	}

	@RequestMapping(value = "updateRoleManage", method = RequestMethod.POST)
	public String updateRoleManage(Model model, String id, String workRoleIds, String roleIds,
			RedirectAttributes redirectAttributes) {
		PriRoleInfo entity = new PriRoleInfo(id);
		Feedback feedback = new Feedback(true, "保存成功！");
		try {
			priRoleInfoRunService.delete(id);
			priRoleInfoWorkService.delete(id);

			if (StringUtils.isNotBlank(roleIds)) {
				List<PriRoleInfoRun> itemList = new ArrayList<PriRoleInfoRun>();
				String[] roles = roleIds.split(",");
				for (String roleId : roles) {
					PriRoleInfoRun item = new PriRoleInfoRun();
					item.setId(UUIDUtils.random().toString());
					item.setParentPriRoleInfo(entity);
					item.setPriRoleInfo(new PriRoleInfo(roleId));
					itemList.add(item);
				}
				priRoleInfoRunService.save(itemList);
			}

			if (StringUtils.isNotBlank(workRoleIds)) {
				List<PriRoleInfoWork> itemList = new ArrayList<PriRoleInfoWork>();
				String[] roles = workRoleIds.split(",");
				for (String roleId : roles) {
					PriRoleInfoWork item = new PriRoleInfoWork();
					item.setId(UUIDUtils.random().toString());
					item.setParentPriRoleInfo(entity);
					item.setPriRoleInfo(new PriRoleInfo(roleId));
					itemList.add(item);
				}
				priRoleInfoWorkService.save(itemList);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "保存失败，发生异常：" + e.getMessage());
		}

		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/system/role/list";
	}

	@SysLog("角色管理-新增角色")
	@RequiresPermissions("/system/role/list$create")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid PriRoleInfo entity, String modelIds, String modelOperateIds,
			RedirectAttributes redirectAttributes, HttpServletRequest request, String roleIds) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "更新成功");
		List<PriModelInfo> selectedModelList = new ArrayList<PriModelInfo>();

		PriRoleInfo roleInfo = roleInfoService.queryByName(entity.getRoleName());
		if (roleInfo != null) {// 存在
			feedback = new Feedback(false, "创建失败,角色已存在");
			redirectAttributes.addFlashAttribute("feedback", feedback);
			return "redirect:/system/role/list";
		}
		PriRoleInfo roleInfo2 = roleInfoService.queryByCode(entity.getRoleCode());
		if (roleInfo2 != null) {// 存在
			feedback = new Feedback(false, "创建失败,角色编号已存在");
			redirectAttributes.addFlashAttribute("feedback", feedback);
			return "redirect:/system/role/list";
		}
		// 被选中的菜单
		if (StringUtils.isNotBlank(modelIds)) {
			selectedModelList = modelService.queryAll(Arrays.asList(modelIds.split(",")));
		}
		entity.setPriModelInfos(selectedModelList);

		try {
			roleInfoService.insert(entity);

			// 下属角色-单独抽出来
			// if (StringUtils.isNotBlank(roleIds)) {
			// List<PriRoleInfoRun> itemList = new
			// ArrayList<PriRoleInfoRun>();
			// String[] roles = roleIds.split(",");
			// for (String roleId : roles) {
			// PriRoleInfoRun item = new PriRoleInfoRun();
			// item.setId(UUIDUtils.random().toString());
			// item.setParentPriRoleInfo(entity);
			// item.setPriRoleInfo(new PriRoleInfo(roleId));
			// itemList.add(item);
			// }
			// priRoleInfoRunService.save(itemList);
			// }

			// 被选中的菜单操作
			if (StringUtils.isNotBlank(modelOperateIds)) {
				List<PriRoleOperate> roleOperates = new ArrayList<PriRoleOperate>();
				for (String modelOperateId : modelOperateIds.split(",")) {
					if (StringUtils.isNotBlank(modelOperateId)) {
						String[] s = modelOperateId.split(":");
						PriRoleOperate roleOperate = new PriRoleOperate();
						roleOperate.setRoleId(entity.getRoleId());
						roleOperate.setModelId(s[0]);
						roleOperate.setOperateId(s[1]);
						roleOperate.setCreatedBy(user.getId());

						roleOperates.add(roleOperate);
					}
				}

				priRoleOperateService.insert(roleOperates);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/system/role/list";
	}

	@RequiresPermissions("/system/role/list$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		PriRoleInfo roleInfo = roleInfoService.queryById(id);
		// List<PriRoleInfo> roleList = roleInfoService.queryAll();//单独抽出来了
		// List<PriRoleInfoRun> roleInfoList = roleInfo.getPriRoleInfoList();
		// for (PriRoleInfo item : roleList) {
		// for (PriRoleInfoRun priRoleInfoRun : roleInfoList) {
		// if
		// (item.getRoleId().equals(priRoleInfoRun.getPriRoleInfo().getRoleId()))
		// {
		// item.setIsCheck(true);
		// }
		// }
		// }
		// model.addAttribute("roleList", roleList);
		model.addAttribute("entity", roleInfo);
		// 如果编号已经存在角色枚举里，则不能修改编码，反之不存在则可编辑
		model.addAttribute("isUpdateCode", PriRoleInfoEnum.getItem(roleInfo.getRoleCode()) == null);
		model.addAttribute("action", "update");
		return "systemManage/role/form";
	}

	@SysLog("角色管理-编辑角色")
	@RequiresPermissions("/system/role/list$update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity") PriRoleInfo entity, String modelIds, String modelOperateIds,
			RedirectAttributes redirectAttributes, HttpServletRequest request, String roleIds) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "更新成功");
		PriRoleInfo roleInfo = roleInfoService.queryByName(entity.getRoleName());
		if (roleInfo != null) {// 存在
			if (!roleInfo.getRoleId().equals(entity.getRoleId())) {// 修改的时候，避免自己跟自己相等
				feedback = new Feedback(false, "修改失败,角色名已存在");
				redirectAttributes.addFlashAttribute("feedback", feedback);
				return "redirect:/system/role/list";
			}
		}
		PriRoleInfo roleInfo2 = roleInfoService.queryByCode(entity.getRoleCode());
		if (roleInfo2 != null) {// 存在
			if (!roleInfo2.getRoleId().equals(entity.getRoleId())) {// 修改的时候，避免自己跟自己相等
				feedback = new Feedback(false, "修改失败,角色编号已存在");
				redirectAttributes.addFlashAttribute("feedback", feedback);
				return "redirect:/system/role/list";
			}
		}

		List<PriModelInfo> selectedModelList = new ArrayList<PriModelInfo>();
		PriRoleInfo modifyInfo = roleInfoService.queryById(entity.getRoleId());

		// 被选中的菜单
		if (StringUtils.isNotBlank(modelIds)) {
			selectedModelList = modelService.queryAll(Arrays.asList(modelIds.split(",")));
		}
		modifyInfo.setPriModelInfos(selectedModelList);

		// 如果编号已经存在角色枚举里，则不能修改编码，反之不存在则可编辑
		if (PriRoleInfoEnum.getItem(modifyInfo.getRoleCode()) == null) {
			modifyInfo.setRoleCode(entity.getRoleCode());
		}
		modifyInfo.setRoleName(entity.getRoleName());
		modifyInfo.setPlatformType(entity.getPlatformType());
		modifyInfo.setIsMng(entity.getIsMng());

		try {
			roleInfoService.update(modifyInfo);

			// 删除已经选定的下属，在新增下属角色//单独抽出来了
			// priRoleInfoRunService.delete(modifyInfo.getRoleId());
			// if (StringUtils.isNotBlank(roleIds)) {
			// List<PriRoleInfoRun> itemList = new
			// ArrayList<PriRoleInfoRun>();
			// String[] roles = roleIds.split(",");
			// for (String roleId : roles) {
			// PriRoleInfoRun item = new PriRoleInfoRun();
			// item.setId(UUIDUtils.random().toString());
			// item.setParentPriRoleInfo(entity);
			// item.setPriRoleInfo(new PriRoleInfo(roleId));
			// itemList.add(item);
			// }
			// priRoleInfoRunService.save(itemList);
			// }

			// 被选中的菜单操作
			priRoleOperateService.deleteByRoleId(modifyInfo.getRoleId());
			if (StringUtils.isNotBlank(modelOperateIds)) {
				List<PriRoleOperate> roleOperates = new ArrayList<PriRoleOperate>();
				for (String modelOperateId : modelOperateIds.split(",")) {
					if (StringUtils.isNotBlank(modelOperateId)) {
						String[] s = modelOperateId.split(":");
						PriRoleOperate roleOperate = new PriRoleOperate();
						roleOperate.setRoleId(modifyInfo.getRoleId());
						roleOperate.setModelId(s[0]);
						roleOperate.setOperateId(s[1]);
						roleOperate.setCreatedBy(user.getId());

						roleOperates.add(roleOperate);
					}
				}

				priRoleOperateService.insert(roleOperates);

				// 清空权限缓存。todo
				// 目前是清楚了全部角色的权限缓存，后期可以改成切换到需要修改的角色然后清除缓存，再切换回来当前用户的角色
				shiroDbRealm.clearAllCachedAuthorizationInfo();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/system/role/list";
	}

	@SysLog("角色管理-删除角色")
	@RequiresPermissions("/system/role/list$delete")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, ServletResponse response) throws IOException {
		Feedback feedback = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(ids)) {
			try {
				roleInfoService.delete(Arrays.asList(ids.split(",")));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				feedback = new Feedback(false, "删除失败");
			}
		}
		return feedback;
	}

	@RequestMapping(value = "getTree", method = RequestMethod.GET)
	@ResponseBody
	public List<TreeModel> getTree() {
		List<TreeModel> tree = modelService.queryModelAndOperateTree();
		return tree;
	}

	@RequestMapping(value = "getTree/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<TreeModel> getTree(@PathVariable("id") String id) {
		List<TreeModel> tree = null;
		try {
			PriRoleInfo roleInfo = roleInfoService.queryById(id);
			tree = modelService.queryModelAndOperateTree();
			List<PriModelInfo> infos = roleInfo.getPriModelInfos();

			log.info("返回集合>>roleInfo:" + roleInfo + "-----------tree:" + tree + "----------infos:" + infos);

			for (PriModelInfo model : infos) {
				walk(tree, model);
				if (model.getIsLeaf()) {
					List<PriRoleOperate> roleOperates = priRoleOperateService
							.findByRoleIdAndModelId(roleInfo.getRoleId(), model.getModelId());
					for (PriRoleOperate roleOperate : roleOperates) {
						walk(tree, model, roleOperate);
					}
				}
			}
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		}
		return tree;
	}

	private void walk(List<TreeModel> modelList, PriModelInfo model) {
		for (TreeModel treeModel : modelList) {
			if (treeModel.getId().equals(model.getModelId())) {
				treeModel.getState().setChecked(true);
				break;
			}
			if (treeModel.getNodes() != null) {
				walk(treeModel.getNodes(), model);
			}
		}
	}

	private void walk(List<TreeModel> modelList, PriModelInfo model, PriRoleOperate roleOperate) {
		for (TreeModel treeModel : modelList) {
			if ("##".equals(treeModel.getHref())
					&& treeModel.getId().equals(model.getModelId() + ":" + roleOperate.getOperateId())) {
				treeModel.getState().setChecked(true);
				break;
			}
			if (treeModel.getNodes() != null) {
				walk(treeModel.getNodes(), model, roleOperate);
			}
		}
	}

	@RequestMapping(value = "check", method = { RequestMethod.POST })
	@ResponseBody
	public Feedback checkLogin(String name, String id) throws IOException {
		PriRoleInfo roleInfo = roleInfoService.queryByName(name);
		boolean bool = false;
		if (roleInfo != null) {// 存在
			if (!roleInfo.getRoleId().equals(id)) {// 修改的时候，避免自己跟自己相等
				bool = true;
			}
		}
		Feedback fe = new Feedback(bool, "");
		fe.setSuccessful(bool);
		return fe;
	}

}
