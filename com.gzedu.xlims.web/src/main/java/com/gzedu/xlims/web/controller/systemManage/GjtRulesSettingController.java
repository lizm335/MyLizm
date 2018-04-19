/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.systemManage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtRulesSetting;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtRulesSettingService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 
 * 功能说明：规则设置
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月28日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Controller
@RequestMapping("/system/rulessetting")
public class GjtRulesSettingController {

	@Autowired
	GjtGradeService gjtGradeService;

	@Autowired
	GjtSpecialtyService gjtSpecialtyService;

	@Autowired
	GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtRulesSettingService gjtRulesSettingService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<GjtRulesSetting> page = gjtRulesSettingService.queryAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次

		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("pageInfo", page);

		return "systemManage/rulessetting/list";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("item", new GjtRulesSetting());
		String orgId = user.getGjtOrg().getId();
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 学校.
		Map<String, String> gradeMap = commonMapService.getGradeMap(orgId);// 年级
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(orgId);// 专业名称
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(orgId);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("action", "create");
		return "systemManage/rulessetting/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(GjtRulesSetting item, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			if (StringUtils.isBlank(item.getGjtSchoolInfo().getId())) {
				item.setGjtSchoolInfo(null);
			}
			if (StringUtils.isBlank(item.getGjtGrade().getGradeId())) {
				item.setGjtGrade(null);
			}
			if (StringUtils.isBlank(item.getGjtSpecialty().getSpecialtyId())) {
				item.setGjtSpecialty(null);
			}
			if (StringUtils.isBlank(item.getGjtStudyCenter().getId())) {
				item.setGjtSpecialty(null);
			}

			item.setId(UUIDUtils.random());
			item.setCreatedDt(DateUtils.getNowTime());
			item.setCreatedBy(user.getRealName());
			item.setVersion(new BigDecimal(2.5));
			item.setIsDeleted("N");
			gjtRulesSettingService.insert(item);
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/system/rulessetting/list";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 学校
		GjtRulesSetting entity = gjtRulesSettingService.queryBy(id);
		String orgId = user.getGjtOrg().getId();
		Map<String, String> gradeMap = commonMapService.getGradeMap(orgId);// 年级
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(orgId);// 专业名称
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(orgId);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("item", entity);
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("action", "update");
		return "systemManage/rulessetting/form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(GjtRulesSetting item, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "更新成功");
		GjtRulesSetting entity = gjtRulesSettingService.queryBy(item.getId());
		try {

			if (StringUtils.isBlank(item.getGjtSchoolInfo().getId())) {
				item.setGjtSchoolInfo(null);
			}
			if (StringUtils.isBlank(item.getGjtGrade().getGradeId())) {
				item.setGjtGrade(null);
			}
			if (StringUtils.isBlank(item.getGjtSpecialty().getSpecialtyId())) {
				item.setGjtSpecialty(null);
			}
			if (StringUtils.isBlank(item.getGjtStudyCenter().getId())) {
				item.setGjtSpecialty(null);
			}

			entity.setUpdatedBy(user.getRealName());
			entity.setUpdatedDt(DateUtils.getNowTime());
			entity.setGjtGrade(item.getGjtGrade());
			// entity.setGjtOrg(item.getGjtOrg());
			entity.setGjtSchoolInfo(item.getGjtSchoolInfo());
			entity.setGjtSpecialty(item.getGjtSpecialty());
			entity.setGjtStudyCenter(item.getGjtStudyCenter());
			entity.setPeopleNo(item.getPeopleNo());
			entity.setPycc(item.getPycc());
			gjtRulesSettingService.update(entity);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/system/rulessetting/list";
	}

	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				gjtRulesSettingService.deleteById(selectedIds);
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

}
