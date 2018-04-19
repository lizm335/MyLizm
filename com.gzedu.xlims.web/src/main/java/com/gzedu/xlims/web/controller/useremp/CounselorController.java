/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.useremp;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtOrg;
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

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
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

/**
 * 
 * 功能说明：辅导教师管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月18日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Controller
@RequestMapping("/usermanage/counselor")
public class CounselorController {

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
		GjtUserAccount userAccount = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GjtEmployeeInfo> list = gjtEmployeeInfoService.queryAll(userAccount.getGjtOrg().getId(), searchParams,
				pageRequst, EmployeeTypeEnum.辅导教师.getNum());

		Map<String, String> schoolInfoMap = commonMapService.getOrgMapByOrgId(userAccount.getGjtOrg().getId()); // 学校
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(userAccount.getGjtOrg().getId());// 中心

		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("pageInfo", list);
		
		
		return "usermanage/counselor/list";
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
		return "usermanage/counselor/form";
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
		return "usermanage/counselor/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtEmployeeInfo item, RedirectAttributes redirectAttributes, String loginAccount) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			GjtOrg org = gjtOrgService.queryById(item.getXxzxId());
			item.setOrgId(org.getId());
			item.setOrgCode(org.getCode());
			GjtUserAccount gjtUserAccount = gjtUserAccountService.saveEntity(item.getXm(), loginAccount, item.getOrgId(),
					UserTypeEnum.教师.getNum());
			gjtEmployeeInfoService.saveEntity(item, gjtUserAccount, String.valueOf(EmployeeTypeEnum.辅导教师.getNum()));
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/usermanage/counselor/list";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryById(id);
		String userId = request.getSession().getAttribute("userId").toString();
		GjtUserAccount userAccount = gjtUserAccountService.findOne(userId);
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(userId); // 学校
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(userAccount.getGjtOrg().getId());// 中心

		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("item", employeeInfo);
		model.addAttribute("action", "update");
		return "usermanage/counselor/form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(GjtEmployeeInfo item, RedirectAttributes redirectAttributes, String gjtSchoolInfoId,
			String gjtStudyCenterId) {
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
		return "redirect:/usermanage/counselor/list";
	}

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

	@RequestMapping(value = "checkLogin")
	@ResponseBody
	public Feedback checkLogin(String loginAccount) throws IOException {
		Boolean boolean1 = gjtUserAccountService.existsByLoginAccount(loginAccount);
		Feedback fe = new Feedback(boolean1, "");
		fe.setSuccessful(boolean1);
		return fe;
	}
}
