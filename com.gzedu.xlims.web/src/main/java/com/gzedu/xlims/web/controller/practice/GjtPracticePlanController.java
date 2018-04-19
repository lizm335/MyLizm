package com.gzedu.xlims.web.controller.practice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.practice.GjtPracticePlan;
import com.gzedu.xlims.pojo.practice.GjtPracticePlanApproval;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.practice.GjtPracticePlanApprovalService;
import com.gzedu.xlims.service.practice.GjtPracticePlanService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/practicePlan")
public class GjtPracticePlanController {

	private final static Logger log = LoggerFactory.getLogger(GjtPracticePlanController.class);
	
	@Autowired
	private GjtPracticePlanService gjtPracticePlanService;
	
	@Autowired
	private GjtPracticePlanApprovalService gjtPracticePlanApprovalService;

	@Autowired
	private CodeGeneratorService codeGeneratorService;

	@Autowired
	private GjtOrgService gjtOrgService;
	
	@Autowired
	private CommonMapService commonMapService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		if ("1".equals(user.getGjtOrg().getOrgType())) {
			searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		} else {
			List<String> parents = gjtOrgService.queryParents(user.getGjtOrg().getId());
			if (parents != null && parents.size() > 0) {
				searchParams.put("IN_orgId", parents);
			} else {
				searchParams.put("EQ_orgId", user.getGjtOrg().getId());
			}
		}

		// 查询“待审核”、“审核不通过”、“未开始”、“进行中”和“已结束”
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.put("EQ_status", 1);
		model.addAttribute("preAudit", gjtPracticePlanService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_status", 2);
		model.addAttribute("auditNotPass", gjtPracticePlanService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_status", 3);
		map.put("GT_applyBeginDt", today); //未开始
		model.addAttribute("notStart", gjtPracticePlanService.findAll(map, pageRequst).getTotalElements());
		map.remove("GT_applyBeginDt");
		map.put("LTE_applyBeginDt", today);
		map.put("GTE_reviewDt", today);
		model.addAttribute("starting", gjtPracticePlanService.findAll(map, pageRequst).getTotalElements());
		map.remove("LTE_applyBeginDt");
		map.remove("GTE_reviewDt");
		map.put("LT_reviewDt", today); //已结束
		model.addAttribute("end", gjtPracticePlanService.findAll(map, pageRequst).getTotalElements());
		
		//状态判断
		if (request.getParameter("status") != null) {
			int status = Integer.parseInt(request.getParameter("status").toString());
			if (status == 1) {  //未开始
				searchParams.put("EQ_status", 3);
				searchParams.put("GT_applyBeginDt", today);
			} else if (status == 2) {  //进行中
				searchParams.put("EQ_status", 3);
				searchParams.put("LTE_applyBeginDt", today);
				searchParams.put("GTE_reviewDt", today);
			} else if (status == 3) {  //已结束
				searchParams.put("EQ_status", 3);
				searchParams.put("LT_reviewDt", today);
			} else if (status == 4) {  //待审核
				searchParams.put("EQ_status", 1);
			} else if (status == 5) {  //审核不通过
				searchParams.put("EQ_status", 2);
			}
		}

		Page<GjtPracticePlan> pageInfo = gjtPracticePlanService.findAll(searchParams, pageRequst);
		Date now = new Date();
		for (GjtPracticePlan practicePlan : pageInfo) {
			if (practicePlan.getStatus() == 1) {
				practicePlan.setStatus2(4);
			} else if (practicePlan.getStatus() == 2) {
				practicePlan.setStatus2(5);
			} else if (practicePlan.getStatus() == 3) {
				if (practicePlan.getApplyBeginDt().compareTo(now) > 0) {
					practicePlan.setStatus2(1);
				} else if (practicePlan.getApplyBeginDt().compareTo(now) <= 0
						&& practicePlan.getReviewDt().compareTo(now) >= 0) {
					practicePlan.setStatus2(2);
				} else if (practicePlan.getReviewDt().compareTo(now) < 0) {
					practicePlan.setStatus2(3);
				}
			}
		}
		model.addAttribute("pageInfo", pageInfo);
		
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);
		
		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("canApproval", subject.isPermitted("/practicePlan/list$approval"));
		model.addAttribute("isBtnUpdate", subject.isPermitted("/practicePlan/list$update"));
		model.addAttribute("isBtnCreate", subject.isPermitted("/practicePlan/list$create"));
		model.addAttribute("isBtnDelete", subject.isPermitted("/practicePlan/list$delete"));
		model.addAttribute("isBtnView", subject.isPermitted("/practicePlan/list$view"));

		return "practice/practicePlan_list";
	}

	/**
	 * 返回新增页面
	 * 
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/practicePlan/list$create")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		model.addAttribute("entity", new GjtPracticePlan());
		model.addAttribute("action", "create");

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);

		return "practice/practicePlan_form";
	}

	/**
	 * 新增
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequiresPermissions("/practicePlan/list$create")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(@Valid GjtPracticePlan entity, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			GjtPracticePlan gjtPracticePlan = gjtPracticePlanService.findByGradeIdAndOrgId(entity.getGradeId(), user.getGjtOrg().getId());
			if (gjtPracticePlan == null) {
				entity.setCreatedBy(user.getId());
				entity.setOrgId(user.getGjtOrg().getId());
				entity.setPracticePlanCode("SJ" + codeGeneratorService.codeGenerator(CacheConstants.PRACTICE_PLAN_CODE));
				gjtPracticePlanService.insert(entity);
			} else {
				feedback = new Feedback(false, "该学期已创建计划！" );
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
			log.error(e.getMessage(), e);
		}
		
		return feedback;
	}

	/**
	 * 返回编辑页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/practicePlan/list$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		model.addAttribute("entity", gjtPracticePlanService.findOne(id));
		model.addAttribute("action", "update");

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);

		return "practice/practicePlan_form";
	}

	/**
	 * 更新
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequiresPermissions("/practicePlan/list$update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(@Valid @ModelAttribute("entity") GjtPracticePlan entity, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtPracticePlan modifyInfo = gjtPracticePlanService.findOne(entity.getPracticePlanId());
			modifyInfo.setPracticePlanName(entity.getPracticePlanName());
			modifyInfo.setApplyBeginDt(entity.getApplyBeginDt());
			modifyInfo.setApplyEndDt(entity.getApplyEndDt());
			modifyInfo.setSubmitPracticeEndDt(entity.getSubmitPracticeEndDt());
			modifyInfo.setConfirmPracticeEndDt(entity.getConfirmPracticeEndDt());
			modifyInfo.setReviewDt(entity.getReviewDt());
			modifyInfo.setUpdatedBy(user.getId());

			if (modifyInfo.getStatus() == 2) { // 审核不通过，重新发布
				// 增加审批记录
				GjtPracticePlanApproval approval = new GjtPracticePlanApproval();
				approval.setPracticePlanId(entity.getPracticePlanId());
				approval.setOperaRole(1);
				approval.setOperaType(3);
				approval.setCreatedBy(user.getId());
				gjtPracticePlanApprovalService.insert(approval);

				modifyInfo.setStatus(1);
			}

			gjtPracticePlanService.update(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
			log.error(e.getMessage(), e);
		}
		
		return feedback;
	}

	@RequiresPermissions("/practicePlan/list$view")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		model.addAttribute("entity", gjtPracticePlanService.findOne(id));
		model.addAttribute("action", "view");

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);

		return "practice/practicePlan_form";
	}

	@RequiresPermissions("/practicePlan/list$delete")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, HttpServletRequest request) {
		if (StringUtils.isNotBlank(ids)) {
			try {
				GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
				GjtPracticePlan practicePlan = gjtPracticePlanService.findOne(ids);
				practicePlan.setIsDeleted("Y");
				practicePlan.setDeletedBy(user.getId());
				practicePlan.setDeletedDt(new Date());
				gjtPracticePlanService.update(practicePlan);

				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	@RequiresPermissions("/practicePlan/list$approval")
	@RequestMapping(value = "approval")
	public String approval(String id, int operaType, String description, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "审批成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

			// 增加审批记录
			GjtPracticePlanApproval approval = new GjtPracticePlanApproval();
			approval.setPracticePlanId(id);
			approval.setOperaRole(2);
			approval.setOperaType(2);
			approval.setDescription(description);
			approval.setCreatedBy(user.getId());
			gjtPracticePlanApprovalService.insert(approval);
			
			GjtPracticePlan practicePlan = gjtPracticePlanService.findOne(id);
			if (operaType == 1) {
				practicePlan.setStatus(3); // 通过
			} else {
				practicePlan.setStatus(2); // 不通过
			}
			practicePlan.setUpdatedBy(user.getId());
			gjtPracticePlanService.update(practicePlan);
		} catch (Exception e) {
			feedback = new Feedback(false, "审批失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/practicePlan/list";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

}
