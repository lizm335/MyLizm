/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.web.controller.systemManage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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

import com.gzedu.xlims.common.Md5Util;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.pojo.BzrPriRoleInfo;
import com.ouchgzee.headTeacher.service.account.BzrGjtUserAccountService;
import com.ouchgzee.headTeacher.service.usermanage.BzrPriRoleInfoService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

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
@RequestMapping("/system/user")
public class UserAccountController extends BaseController {

	@Autowired
	BzrGjtUserAccountService gjtUserAccountService;

	@Autowired
	BzrPriRoleInfoService roleInfoService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
					   @RequestParam(value = "page.size", defaultValue = "10") int pageSize,
					   @RequestParam(value = "orderProperty", defaultValue = "createdDt") String orderProperty,
					   @RequestParam(value = "orderDirection", defaultValue = "desc") String orderDirection, Model model,
					   ServletRequest request) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, orderProperty, orderDirection);

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<BzrGjtUserAccount> pageInfo = gjtUserAccountService.queryAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "systemManage/user/list";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		List<BzrPriRoleInfo> roles = roleInfoService.queryAll();
		model.addAttribute("roles", roles);
		model.addAttribute("entity", new BzrGjtUserAccount());
		model.addAttribute("action", "create");
		return "systemManage/user/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid BzrGjtUserAccount entity, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			gjtUserAccountService.insert(entity);
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/system/user/list";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		List<BzrPriRoleInfo> roles = roleInfoService.queryAll();
		model.addAttribute("roles", roles);
		model.addAttribute("entity", gjtUserAccountService.findOne(id));
		model.addAttribute("action", "update");
		return "systemManage/user/form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity") BzrGjtUserAccount entity,
						 RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");

		BzrGjtUserAccount modifyInfo = gjtUserAccountService.findOne(entity.getId());
		modifyInfo.setLoginAccount(entity.getLoginAccount());
		modifyInfo.setPassword(Md5Util.encode(entity.getPassword2()));
		modifyInfo.setPassword2(entity.getPassword2());
		modifyInfo.setUserType(entity.getUserType());
		modifyInfo.setPriRoleInfo(entity.getPriRoleInfo());
		modifyInfo.setRealName(entity.getRealName());
		modifyInfo.setIsEnabled(entity.getIsEnabled());

		try {
			gjtUserAccountService.update(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/system/user/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		List<BzrPriRoleInfo> roles = roleInfoService.queryAll();
		model.addAttribute("roles", roles);
		model.addAttribute("info", gjtUserAccountService.findOne(id));
		model.addAttribute("action", "view");
		return "systemManage/user/form";
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, ServletResponse response) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			List<String> selectedIds = new ArrayList(Arrays.asList(ids.split(",")));
			try {
				gjtUserAccountService.delete(selectedIds);
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	// 校验帐号是否存在
	@RequestMapping(value = "checkloginAccount")
	@ResponseBody
	public Feedback checkLogin(String loginAccount) throws IOException {
		BzrGjtUserAccount userAccount = gjtUserAccountService.queryByLoginAccount(loginAccount);
		Boolean boolean1 = userAccount==null?false:true;
		Feedback fe = new Feedback(boolean1, "");
		fe.setSuccessful(boolean1);
		return fe;
	}
}
