/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtStudyYearAssignment;
import com.gzedu.xlims.pojo.GjtStudyYearInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtStudyAssignService;
import com.gzedu.xlims.service.edumanage.GjtStudyYearService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/edumanage/studyassign")
public class GjtStudyAssignController {

	@Autowired
	GjtStudyYearService gjtStudyYearService;

	@Autowired
	GjtStudyAssignService gjtStudyAssignService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	CommonMapService commonMapService;

	@RequestMapping(value = "list/{id}", method = RequestMethod.GET)
	public String list(Model model, HttpServletRequest request, @PathVariable("id") String id) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		List<GjtStudyYearAssignment> list = null;
		if (id != null) {
			searchParams.put("EQ_gjtStudyYearInfo.id", id);
			list = gjtStudyAssignService.queryAlls(searchParams);
		}
		model.addAttribute("id", id);
		model.addAttribute("list", list);
		return "edumanage/studyassign/list";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

	@RequestMapping(value = "create/{id}", method = RequestMethod.GET)
	public String createForm(Model model, @PathVariable("id") String id) {
		model.addAttribute("item", new GjtStudyYearAssignment());
		model.addAttribute("id", id);
		model.addAttribute("action", "create");
		return "edumanage/studyassign/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(GjtStudyYearAssignment item, RedirectAttributes redirectAttributes, String id,
			HttpServletRequest request) {
		GjtStudyYearInfo yearInfo = gjtStudyYearService.queryById(id);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			item.setAssignmentId(UUIDUtils.random());
			item.setCreatedBy(user.getRealName());
			item.setIsDeleted("N");
			item.setStatus("0");
			item.setCreatedDt(DateUtils.getNowTime());
			item.setVersion(new BigDecimal(2.5));
			item.setGjtStudyYearInfo(yearInfo);
			gjtStudyAssignService.saveEntity(item);
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/studyassign/list/" + id;
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		GjtStudyYearAssignment yearInfo = gjtStudyAssignService.queryById(id);

		model.addAttribute("item", yearInfo);
		model.addAttribute("action", "update");
		return "edumanage/studyassign/form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(GjtStudyYearAssignment item, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtStudyYearAssignment yearAssignment = gjtStudyAssignService.queryById(item.getAssignmentId());
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			yearAssignment.setAssignmentDo(item.getAssignmentDo());
			yearAssignment.setAssignmentEnact(item.getAssignmentEnact());
			yearAssignment.setAssignmentName(item.getAssignmentName());
			yearAssignment.setEndDate(item.getEndDate());
			yearAssignment.setMemo(item.getMemo());
			yearAssignment.setParallelism(item.getParallelism());
			yearAssignment.setSort(item.getSort());
			yearAssignment.setStartDate(item.getStartDate());
			yearAssignment.setUpdatedBy(user.getRealName());
			yearAssignment.setUpdatedDt(DateUtils.getNowTime());

			gjtStudyAssignService.updateEntity(yearAssignment);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/studyassign/list";
	}

	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				gjtStudyAssignService.deleteById(selectedIds);
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	@RequestMapping(value = "updateStutas")
	public @ResponseBody Feedback updateStutas(String id) throws IOException {
		try {
			gjtStudyAssignService.updateStutas(id, "1");
			return new Feedback(true, "修改成功");
		} catch (Exception e) {
			return new Feedback(false, "修改失败");
		}
	}

}
