/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtStudyYearInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.system.StudyYear;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtStudyYearService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/edumanage/studyyear")
public class GjtStudyYearController {

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	GjtStudyYearService gjtStudyYearService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtGradeService gjtGradeService;

	@Autowired
	PriRoleInfoService priRoleInfoService;
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, new Sort(Direction.DESC, "startDate"));
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GjtGrade> page = gjtGradeService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);
		model.addAttribute("pageInfo", page);

		model.addAttribute("responsibleMap", commonMapService.getResponsibleMap());

		return "edumanage/studyyear/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtGrade grade = gjtGradeService.queryById(id);
		model.addAttribute("item", grade);
		model.addAttribute("action", "view");
		model.addAttribute("responsibleMap", commonMapService.getOrgMagagerRoleMap());

		return "edumanage/studyyear/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("item", new GjtStudyYearInfo());

		Map<Integer, String> studyYearMap = StudyYear.getTenYearList();

		List<GjtStudyYearInfo> list = gjtStudyYearService.queryAll(user.getGjtOrg().getId());
		for (GjtStudyYearInfo gjtStudyYearInfo : list) {
			studyYearMap.remove(gjtStudyYearInfo.getStudyYearCode());
		}

		model.addAttribute("studyYearMap", studyYearMap);

		model.addAttribute("responsibleMap", commonMapService.getResponsibleMap());

		model.addAttribute("action", "create");
		return "edumanage/studyyear/form";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(GjtStudyYearInfo item, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			GjtStudyYearInfo yearInfo = gjtStudyYearService.queryByStudyYearCodeAndXxId2(item.getStudyYearCode(),
					user.getGjtOrg().getId());
			if (yearInfo != null) {
				yearInfo.setUpdatedDt(DateUtils.getNowTime());
				yearInfo.setUpdatedBy(user.getId());
				yearInfo.setStudyYearCode(item.getStudyYearCode());
				yearInfo.setStudyYearName(item.getStudyYearName());
				yearInfo.setStudyyearStartDate(item.getStudyyearStartDate());
				yearInfo.setStudyyearEndDate(item.getStudyyearEndDate());
				yearInfo.setEnrollStartDate(item.getEnrollStartDate());
				yearInfo.setEnrollEndDate(item.getEnrollEndDate());
				yearInfo.setEnrollResponsible(item.getEnrollResponsible());
				yearInfo.setSchoolrollStartDate(item.getSchoolrollStartDate());
				yearInfo.setSchoolrollEndDate(item.getSchoolrollEndDate());
				yearInfo.setSchoolrollResponsible(item.getSchoolrollResponsible());
				yearInfo.setEducationalStartDate(item.getEducationalStartDate());
				yearInfo.setEducationalEndDate(item.getEducationalEndDate());
				yearInfo.setEducationalResponsible(item.getEducationalResponsible());
				yearInfo.setTeachingStartDate(item.getTeachingStartDate());
				yearInfo.setTeachingEndDate(item.getTeachingEndDate());
				yearInfo.setTeachingResponsible(item.getTeachingResponsible());
				yearInfo.setExamStartDate(item.getExamStartDate());
				yearInfo.setExamEndDate(item.getExamEndDate());
				yearInfo.setExamResponsible(item.getExamResponsible());
				yearInfo.setGraduationStartDate(item.getGraduationStartDate());
				yearInfo.setGraduationEndDate(item.getGraduationEndDate());
				yearInfo.setGraduationResponsible(item.getGraduationResponsible());
				yearInfo.setCourseStartDate(item.getCourseStartDate());
				yearInfo.setCourseEndDate(item.getCourseEndDate());
				yearInfo.setCourseResponsible(item.getCourseResponsible());
				yearInfo.setIsDeleted("N");
				gjtStudyYearService.updateEntity(yearInfo);
			} else {
				item.setId(UUIDUtils.random());
				item.setCreatedDt(DateUtils.getNowTime());
				item.setCreatedBy(user.getId());
				item.setXxId(user.getGjtOrg().getId());
				item.setIsDeleted("N");
				gjtStudyYearService.saveEntity(item);
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/studyyear/list";
	}

	@RequestMapping(value = "autoCreate/{num}", method = RequestMethod.POST)
	public String autoCreate(@PathVariable("num") String num, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		// GjtUserAccount user = (GjtUserAccount)
		// request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {

		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/studyyear/list";
	}

	@RequiresPermissions("/edumanage/studyyear/list$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtGrade grade = gjtGradeService.queryById(id);
		model.addAttribute("item", grade);
		model.addAttribute("action", "update");
		model.addAttribute("responsibleMap", priRoleInfoService.queryRoleExcludeAdmin());
		return "edumanage/studyyear/form";
	}

	@RequiresPermissions("/edumanage/studyyear/list$update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(GjtGrade item, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		item.setUpdatedBy(user.getId());
		try {
			gjtGradeService.updateGjtGrade(item);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/studyyear/list";
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") String id, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudyYearInfo yearInfo = gjtStudyYearService.queryById(id);
		yearInfo.setIsDeleted("Y");
		yearInfo.setUpdatedDt(DateUtils.getNowTime());
		yearInfo.setUpdatedBy(user.getId());
		gjtStudyYearService.updateEntity(yearInfo);

		return "redirect:/edumanage/studyyear/list";
	}
	// @RequestMapping(value = "delete")
	// public @ResponseBody Feedback delete(String ids) throws IOException {
	// if (StringUtils.isNotBlank(ids)) {
	// String[] selectedIds = ids.split(",");
	// try {
	// gjtStudyYearService.delete(Arrays.asList(selectedIds));
	// return new Feedback(true, "删除成功");
	// } catch (Exception e) {
	// return new Feedback(false, "删除失败");
	// }
	// }
	// return new Feedback(false, "删除失败");
	// }


}
