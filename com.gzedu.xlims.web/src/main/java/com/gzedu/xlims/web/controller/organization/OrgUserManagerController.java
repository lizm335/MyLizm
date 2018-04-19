
package com.gzedu.xlims.web.controller.organization;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtStudyCenterService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/organization/userManager")
public class OrgUserManagerController {

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	GjtStudyCenterService gjtStudyCenterService;

	@Autowired
	GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	CommonMapService commonMapService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, String orgId, Model model,
			ServletRequest request, HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<GjtUserAccount> pageInfo = gjtOrgService.queryUserManagers(orgId, searchParams, pageRequst);

		GjtOrg gjtOrg = gjtOrgService.queryById(orgId);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("gjtOrg", gjtOrg);
		
		
		return "organization/userManager/list";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, String orgId, HttpSession session) {
		Map<String, String> orgMap = new HashMap<String, String>();
		GjtOrg gjtOrg = gjtOrgService.queryById(orgId);
		orgMap.put(gjtOrg.getId(), gjtOrg.getOrgName());

		Map<String, String> orgMagagerRoleMap = commonMapService.getOrgMagagerRoleMap();

		model.addAttribute("orgMap", orgMap);
		model.addAttribute("orgMagagerRoleMap", orgMagagerRoleMap);
		model.addAttribute("action", "create");
		model.addAttribute("orgId", orgId);
		model.addAttribute("item", new GjtUserAccount());
		return "organization/userManager/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtUserAccount item, String orgId, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			gjtUserAccountService.insert(item);
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		redirectAttributes.addAttribute("orgId", orgId);
		return "redirect:/organization/userManager/list";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, String orgId, Model model, HttpSession session) {

		Map<String, String> orgMagagerRoleMap = commonMapService.getOrgMagagerRoleMap();
		GjtUserAccount gjtUserAccount = gjtUserAccountService.findOne(id);
		Map<String, String> orgMap = new HashMap<String, String>();
		GjtOrg gjtOrg = gjtUserAccount.getGjtOrg();
		orgMap.put(gjtOrg.getId(), gjtOrg.getOrgName());

		model.addAttribute("item", gjtUserAccount);
		model.addAttribute("orgMagagerRoleMap", orgMagagerRoleMap);
		model.addAttribute("orgMap", orgMap);
		model.addAttribute("action", "update");
		model.addAttribute("orgId", orgId);
		return "organization/userManager/form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(GjtUserAccount item, String orgId, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");

		GjtUserAccount modifyInfo = gjtUserAccountService.findOne(item.getId());

		modifyInfo.setLoginAccount(item.getLoginAccount());
		modifyInfo.setPassword(Md5Util.encode(item.getPassword2()));
		modifyInfo.setPassword2(item.getPassword2());
		modifyInfo.setUserType(item.getUserType());
		modifyInfo.setRealName(item.getRealName());
		modifyInfo.setGjtOrg(item.getGjtOrg());

		modifyInfo.setPriRoleInfo(item.getPriRoleInfo());
		try {
			gjtUserAccountService.update(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		redirectAttributes.addAttribute("search_EQ_gjtOrg.id", orgId);
		return "redirect:/organization/userManager/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		Map<String, String> orgMagagerRoleMap = commonMapService.getOrgMagagerRoleMap();
		GjtUserAccount gjtUserAccount = gjtUserAccountService.findOne(id);
		Map<String, String> orgMap = new HashMap<String, String>();
		GjtOrg gjtOrg = gjtUserAccount.getGjtOrg();
		orgMap.put(gjtOrg.getId(), gjtOrg.getOrgName());

		model.addAttribute("item", gjtUserAccount);
		model.addAttribute("orgMagagerRoleMap", orgMagagerRoleMap);
		model.addAttribute("orgMap", orgMap);

		model.addAttribute("action", "view");
		return "organization/userManager/form";
	}

	// 单个删除和多个删除
	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(HttpServletRequest request, String ids) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				for (String id : selectedIds) {
					if (user.getId().equals(id)) {
						return new Feedback(false, "不能删除自己");
					}
				}
				gjtUserAccountService.deleteById(selectedIds);
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	// 校验帐号是否存在
	@RequestMapping(value = "checkLogin")
	@ResponseBody
	public Feedback checkLogin(String loginAccount) throws IOException {
		Boolean boolean1 = gjtUserAccountService.existsByLoginAccount(loginAccount);
		Feedback fe = new Feedback(boolean1, "");
		fe.setSuccessful(boolean1);
		return fe;
	}
}
