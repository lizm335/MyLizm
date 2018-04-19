/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.GjtYear;
import com.gzedu.xlims.service.organization.GjtYearService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 功能说明：年级管理
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年4月7日
 * @version 3.0
 *
 */
@Controller
@RequestMapping(value = "/edumanage/year")
public class GjtYearController extends BaseController {

	@Autowired
	private GjtYearService gjtYearService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, "startYear", "DESC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<GjtYear> page = gjtYearService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);
		model.addAttribute("pageInfo", page);
		return "edumanage/year/list";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<GjtYear> years = gjtYearService.findByOrgIdAndIsEnabled(user.getGjtOrg().getId(), 0);
		model.addAttribute("years", years);
		model.addAttribute("action", "create");
		return "edumanage/year/form";
	}

	@ResponseBody
	@RequestMapping(value = "getGjtYear", method = RequestMethod.GET)
	public GjtYear getGjtYear(String gradeId) {
		GjtYear gjtYear = gjtYearService.findOne(gradeId);
		GjtYear year = new GjtYear();
		year.setGradeId(gjtYear.getGradeId());
		year.setName(gjtYear.getName());
		year.setCode(gjtYear.getCode());
		year.setStartYear(gjtYear.getStartYear());
		return year;
	}

	@SysLog("年级管理-修改年级")
	@ResponseBody
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public Feedback update(String gradeId, int isEnabled, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			GjtYear gjtYear = gjtYearService.findOne(gradeId);
			gjtYear.setIsEnabled(isEnabled);
			gjtYear.setUpdatedBy(user.getId());
			gjtYear.setUpdatedDt(new Date());
			gjtYearService.save(gjtYear);
		} catch (Exception e) {
			e.printStackTrace();
			feedback = new Feedback(false, "更新失败");
		}
		return feedback;
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public Feedback create(GjtYear item, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			item.setCreatedBy(user.getId());
			item.setCreatedDt(new Date());
			item.setOrgId(user.getGjtOrg().getId());
			item.setVersion(new BigDecimal(3));
			item.setUpdatedBy(user.getId());
			gjtYearService.save(item);
		} catch (Exception e) {
			e.printStackTrace();
			feedback = new Feedback(false, "更新失败");
		}
		return feedback;
	}

	@RequiresPermissions("/edumanage/year/list$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtYear gjtYear = gjtYearService.queryById(id);
		model.addAttribute("action", "update");
		model.addAttribute("item", gjtYear);
		return "edumanage/year/form";
	}

//	@RequiresPermissions("/edumanage/year/list$update")
	// @RequestMapping(value = "update", method = RequestMethod.POST)
	// public Feedback update(GjtYear item, RedirectAttributes
	// redirectAttributes, HttpServletRequest request) {
	// Feedback feedback = new Feedback(true, "更新成功");
	// GjtUserAccount user = (GjtUserAccount)
	// request.getSession().getAttribute(WebConstants.CURRENT_USER);
	// try {
	// item.setUpdatedBy(user.getId());
	// gjtYearService.updateGjtYear(item);
	// } catch (Exception e) {
	// e.printStackTrace();
	// feedback = new Feedback(false, "更新失败");
	// }
	// return feedback;
//	}

}
