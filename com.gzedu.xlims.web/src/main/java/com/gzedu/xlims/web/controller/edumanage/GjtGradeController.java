/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.GjtYear;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtStudyYearService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.organization.GjtYearService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 
 * 功能说明： 年级管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月18日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Controller
@RequestMapping(value = "/edumanage/grade")
public class GjtGradeController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(GjtGradeController.class);

	@Autowired
	GjtGradeService gjtGradeService;

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtSpecialtyService gjtSpecialtyService;

	@Autowired
	GjtStudyYearService gjtStudyYearService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtYearService gjtYearService;

	@Autowired
	PriRoleInfoService priRoleInfoService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, "gradeCode", "desc");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GjtGrade> page = gjtGradeService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);
		Map<String, String> schoolMap = commonMapService.getXxmcAllDates();
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		model.addAttribute("pageInfo", page);
		model.addAttribute("schoolMap", schoolMap);
		model.addAttribute("gradeMap", gradeMap);
		Subject subject = SecurityUtils.getSubject();
		boolean permitted = subject.isPermitted("/edumanage/teachEmployee/list$schoolModel");
		model.addAttribute("permitted", permitted);
		return "edumanage/grade/edumanage_grade_list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtGrade itme = gjtGradeService.queryById(id);
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 学校
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("itme", itme);
		model.addAttribute("action", "view");
		return "edumanage/grade/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<GjtYear> years = gjtYearService.findByExistsEnableGrade(user.getGjtOrg().getId());
		model.addAttribute("years", years);
		model.addAttribute("roleMap", priRoleInfoService.queryRoleExcludeAdmin());
		model.addAttribute("grade", new GjtGrade());
		model.addAttribute("editable", true);
		return "edumanage/grade/form";
	}



	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtGrade grade = gjtGradeService.queryById(id);
		boolean editable = true;
		if(grade.getStartDate()!=null){
			editable = grade.getStartDate().getTime() > new Date().getTime();
		}
		model.addAttribute("roleMap", priRoleInfoService.queryRoleExcludeAdmin());
		model.addAttribute("grade", grade);
		model.addAttribute("editable", editable);
		model.addAttribute("action", "update");
		Subject subject = SecurityUtils.getSubject();
		boolean permitted = subject.isPermitted("/edumanage/teachEmployee/list$schoolModel");
		model.addAttribute("permitted", permitted);
		return "edumanage/grade/form";
	}

	@SysLog("学期管理-修改学期")
	@ResponseBody
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public Feedback update(GjtGrade item, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			item.setUpdatedBy(user.getId());
			gjtGradeService.updateAdd(item,user);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "系统异常 ");
		}
		return feedback;
	}

	// 单个删除和多个删除
	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				gjtGradeService.deleteById(selectedIds);
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	@RequestMapping(value = "querySpecialty", method = RequestMethod.GET)
	public String querySpecialty(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, String gradeId, @RequestParam(value = "type", defaultValue = "0") int type) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		if (StringUtils.isBlank(gradeId) && !gradeMap.isEmpty()) {
			gradeId = gradeMap.keySet().iterator().next();
		}

		Page<GjtSpecialty> page = gjtSpecialtyService.queryGradeSpecialtyAll(user.getGjtOrg().getId(), gradeId, type,
				searchParams, pageRequst);

		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次

		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("gradeId", gradeId);
		model.addAttribute("pageInfo", page);
		model.addAttribute("type", type);
		// String str = Servlets.encodeParameterStringWithPrefix(searchParams,
		// "search_");
		// model.addAttribute("searchParams", str + "&gradeId=" + gradeId);
		return "edumanage/grade/specialty";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "existsCode", method = RequestMethod.GET)
	public Map existsCode(@RequestParam(value = "gradeCode") String gradeCode, HttpServletRequest request,
			@RequestParam(value = "gradeId", required = false) String gradeId) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map map = new HashMap();
		try {
			GjtGrade grade = gjtGradeService.queryByGjtGradeCode(gradeCode);
			boolean result = false;
			if (grade == null || grade.getGradeId().equals(gradeId))
				result = true;
			map.put("valid", result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("valid", false);
		}
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "findGradeByYearId", method = RequestMethod.GET)
	public Map<String, String> findGradeByYearId(String yearId, HttpServletRequest request) {
		List<GjtGrade> grades = gjtGradeService.findGradeByYearId(yearId);
		Map<String, String> gradeMap = new HashMap<String, String>();
		for (GjtGrade g : grades) {
			gradeMap.put(g.getGradeId(), g.getGradeName());
		}
		return gradeMap;
	}

	@ResponseBody
	@RequestMapping(value = "getGradeCodeById", method = RequestMethod.GET)
	public String getGradeCodeById(String gradeId) {
		GjtGrade grade = gjtGradeService.queryById(gradeId);
		if (grade != null)
			return grade.getGradeCode();
		return null;
	}

}
