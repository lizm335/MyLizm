/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.useremp;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.Objects;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.pojo.status.UserTypeEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtStudyCenterService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

/**
 * 
 * 功能说明：班主任管理(废弃)
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月16日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Controller
@RequestMapping("/usermanage/headteacher")
public class HeadTeacherController {

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
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GjtEmployeeInfo> list = gjtEmployeeInfoService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst,
				EmployeeTypeEnum.班主任.getNum());

		Map<String, String> schoolInfoMap = commonMapService.getOrgMapByOrgId(user.getGjtOrg().getId()); // 学校
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心

		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("studyCenterMap", studyCenterMap);

		model.addAttribute("pageInfo", list);

		return "usermanage/headteacher/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId()); // 学校
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心

		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("item", gjtEmployeeInfoService.queryById(id));

		model.addAttribute("action", "view");
		return "usermanage/headteacher/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId()); // 学校
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心

		model.addAttribute("item", new GjtEmployeeInfo());
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("action", "create");
		return "usermanage/headteacher/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtEmployeeInfo item, RedirectAttributes redirectAttributes, String loginAccount) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			GjtOrg org = gjtOrgService.queryById(item.getXxzxId());
			item.setOrgId(org.getId());
			item.setOrgCode(org.getCode());
			GjtUserAccount gjtUserAccount = gjtUserAccountService.saveEntity(item.getXm(), loginAccount,
					item.getOrgId(), UserTypeEnum.教师.getNum());
			gjtEmployeeInfoService.saveEntity(item, gjtUserAccount, String.valueOf(EmployeeTypeEnum.班主任.getNum()));
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/usermanage/headteacher/list";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId()); // 学校
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心

		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryById(id);
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("item", employeeInfo);
		model.addAttribute("action", "update");
		return "usermanage/headteacher/form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(GjtEmployeeInfo item, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtEmployeeInfo modifyInfo = gjtEmployeeInfoService.queryById(item.getEmployeeId());

		modifyInfo.setXbm(item.getXbm());
		modifyInfo.setWorkAddr(item.getWorkAddr());
		modifyInfo.setWorkTime(item.getWorkTime());
		modifyInfo.setSjh(item.getSjh());
		modifyInfo.setLxdh(item.getLxdh());
		modifyInfo.setDzxx(item.getDzxx());
		modifyInfo.setUpdatedDt(DateUtils.getNowTime());
		modifyInfo.setXxId(item.getXxId());
		modifyInfo.setXxzxId(item.getXxzxId());
		modifyInfo.setXm(item.getXm());
		modifyInfo.getGjtUserAccount().setRealName(item.getXm());

		try {
			gjtEmployeeInfoService.updateEntity(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/usermanage/headteacher/list";
	}

	// 单个删除和多个删除
	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				gjtEmployeeInfoService.deleteById(selectedIds);
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

	// 班主任模拟登陆
	@RequestMapping(value = "/simulation", method = RequestMethod.GET)
	public String simulation(@RequestParam String id,HttpServletRequest request) {
		String courseClassId = Objects.toString(request.getParameter("cid"), "");

		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryById(id);
		GjtUserAccount gjtUserAccount = employeeInfo.getGjtUserAccount();
		String url = "";
		if (gjtUserAccount != null) {
			url = gjtUserAccountService.headTeacherSimulation(gjtUserAccount.getLoginAccount(), id, courseClassId);
		}
		return "redirect:" + url; // 修改完重定向
	}

}
