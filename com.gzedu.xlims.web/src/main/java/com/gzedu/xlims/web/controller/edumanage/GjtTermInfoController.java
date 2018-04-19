/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.GjtTermInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtTermInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/edumanage/termInfo")
public class GjtTermInfoController {

	@Autowired
	CommonMapService commonMapService;
	@Autowired
	GjtTermInfoService gjtTermInfoService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtGradeService gjtGradeService;

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GjtTermInfo> page = gjtTermInfoService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);

		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 院校

		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pageInfo", page);
		
		
		return "edumanage/termInfo/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 院校

		GjtTermInfo entity = gjtTermInfoService.queryById(id);
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("gradeMap", gradeMap);

		model.addAttribute("item", entity);
		model.addAttribute("action", "view");
		return "edumanage/termInfo/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 院校

		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("item", new GjtTermInfo());
		model.addAttribute("action", "create");
		return "edumanage/termInfo/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(GjtTermInfo item, RedirectAttributes redirectAttributes, String gjtSchoolInfoId,
			String startDates, String endDates, String gradeId) {
		GjtSchoolInfo gjtSchoolInfo = gjtSchoolInfoService.queryById(gjtSchoolInfoId);
		GjtOrg gjtOrg = gjtOrgService.queryById(gjtSchoolInfoId);
		GjtGrade gjtGrade = gjtGradeService.queryById(gradeId);
		Feedback feedback = new Feedback(true, "创建成功");

		try {
			item.setTermId(UUIDUtils.random());
			item.setGjtSchoolInfo(gjtSchoolInfo);
			item.setGjtOrg(gjtOrg);
			item.setGjtGrade(gjtGrade);
			item.setOrgCode(gjtOrg.getCode());
			item.setCreatedDt(DateUtils.getNowTime());
			item.setIsDeleted("N");
			item.setIsEnabled("1");
			item.setTermYear(startDates.split("-")[0]);
			item.setStartDate(DateUtils.getNowTime(startDates));
			item.setEndDate(DateUtils.getNowTime(endDates));
			item.setVersion(BigDecimal.valueOf(2.5));
			gjtTermInfoService.saveEntity(item);
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/termInfo/list";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 院校

		GjtTermInfo entity = gjtTermInfoService.queryById(id);
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("item", entity);
		model.addAttribute("action", "update");
		return "edumanage/termInfo/form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(GjtTermInfo item, RedirectAttributes redirectAttributes, String gjtSchoolInfoId,
			String startDates, String endDates, String gradeId) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtSchoolInfo gjtSchoolInfo = gjtSchoolInfoService.queryById(gjtSchoolInfoId);
		GjtOrg gjtOrg = gjtOrgService.queryById(gjtSchoolInfoId);
		GjtTermInfo entity = gjtTermInfoService.queryById(item.getTermId());
		GjtGrade gjtGrade = gjtGradeService.queryById(gradeId);
		try {
			entity.setStartDate(DateUtils.getNowTime(startDates));
			entity.setEndDate(DateUtils.getNowTime(endDates));
			entity.setGjtSchoolInfo(gjtSchoolInfo);
			entity.setGjtOrg(gjtOrg);
			entity.setUpdatedDt(DateUtils.getNowTime());
			entity.setGjtGrade(gjtGrade);
			entity.setTermCode(item.getTermCode());
			entity.setMemo(item.getMemo());
			entity.setTermName(item.getTermName());
			gjtTermInfoService.updateEntity(entity);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/termInfo/list";
	}

	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				gjtTermInfoService.deleteById(selectedIds); // 删除专业
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

}
