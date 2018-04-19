package com.gzedu.xlims.web.controller.thesis;

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
import com.gzedu.xlims.pojo.thesis.GjtThesisPlan;
import com.gzedu.xlims.pojo.thesis.GjtThesisPlanApproval;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.thesis.GjtThesisPlanApprovalService;
import com.gzedu.xlims.service.thesis.GjtThesisPlanService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/thesisPlan")
public class GjtThesisPlanController {

	private final static Logger log = LoggerFactory.getLogger(GjtThesisPlanController.class);

	@Autowired
	private GjtThesisPlanService gjtThesisPlanService;

	@Autowired
	private GjtThesisPlanApprovalService gjtThesisPlanApprovalService;

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
		model.addAttribute("preAudit", gjtThesisPlanService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_status", 2);
		model.addAttribute("auditNotPass", gjtThesisPlanService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_status", 3);
		map.put("GT_applyBeginDt", today); //未开始
		model.addAttribute("notStart", gjtThesisPlanService.findAll(map, pageRequst).getTotalElements());
		map.remove("GT_applyBeginDt");
		map.put("LTE_applyBeginDt", today);
		map.put("GTE_defenceDt", today);
		model.addAttribute("starting", gjtThesisPlanService.findAll(map, pageRequst).getTotalElements());
		map.remove("LTE_applyBeginDt");
		map.remove("GTE_defenceDt");
		map.put("LT_defenceDt", today); //已结束
		model.addAttribute("end", gjtThesisPlanService.findAll(map, pageRequst).getTotalElements());
		
		//状态判断
		if (request.getParameter("status") != null) {
			int status = Integer.parseInt(request.getParameter("status").toString());
			if (status == 1) {  //未开始
				searchParams.put("EQ_status", 3);
				searchParams.put("GT_applyBeginDt", today);
			} else if (status == 2) {  //进行中
				searchParams.put("EQ_status", 3);
				searchParams.put("LTE_applyBeginDt", today);
				searchParams.put("GTE_defenceDt", today);
			} else if (status == 3) {  //已结束
				searchParams.put("EQ_status", 3);
				searchParams.put("LT_defenceDt", today);
			} else if (status == 4) {  //待审核
				searchParams.put("EQ_status", 1);
			} else if (status == 5) {  //审核不通过
				searchParams.put("EQ_status", 2);
			}
		}

		Page<GjtThesisPlan> pageInfo = gjtThesisPlanService.findAll(searchParams, pageRequst);
		Date now = new Date();
		for (GjtThesisPlan thesisPlan : pageInfo) {
			if (thesisPlan.getStatus() == 1) {
				thesisPlan.setStatus2(4);
			} else if (thesisPlan.getStatus() == 2) {
				thesisPlan.setStatus2(5);
			} else if (thesisPlan.getStatus() == 3) {
				if (thesisPlan.getApplyBeginDt().compareTo(now) > 0) {
					thesisPlan.setStatus2(1);
				} else if (thesisPlan.getApplyBeginDt().compareTo(now) <= 0
						&& thesisPlan.getDefenceDt().compareTo(now) >= 0) {
					thesisPlan.setStatus2(2);
				} else if (thesisPlan.getDefenceDt().compareTo(now) < 0) {
					thesisPlan.setStatus2(3);
				}
			}
		}
		model.addAttribute("pageInfo", pageInfo);
		
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);
		
		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("canApproval", subject.isPermitted("/thesisPlan/list$approval"));
		model.addAttribute("isBtnUpdate", subject.isPermitted("/thesisPlan/list$update"));
		model.addAttribute("isBtnCreate", subject.isPermitted("/thesisPlan/list$create"));
		model.addAttribute("isBtnDelete", subject.isPermitted("/thesisPlan/list$delete"));
		model.addAttribute("isBtnView", subject.isPermitted("/thesisPlan/list$view"));

		return "thesis/thesisPlan_list";
	}

	/**
	 * 返回新增页面
	 * 
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/thesisPlan/list$create")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		model.addAttribute("entity", new GjtThesisPlan());
		model.addAttribute("action", "create");

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);

		return "thesis/thesisPlan_form";
	}

	/**
	 * 新增
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequiresPermissions("/thesisPlan/list$create")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(@Valid GjtThesisPlan entity, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			GjtThesisPlan gjtThesisPlan = gjtThesisPlanService.findByGradeIdAndOrgId(entity.getGradeId(), user.getGjtOrg().getId());
			if (gjtThesisPlan == null) {
				entity.setCreatedBy(user.getId());
				entity.setOrgId(user.getGjtOrg().getId());
				entity.setThesisPlanCode("LW" + codeGeneratorService.codeGenerator(CacheConstants.THESIS_PLAN_CODE));
				gjtThesisPlanService.insert(entity);
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
	@RequiresPermissions("/thesisPlan/list$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		model.addAttribute("entity", gjtThesisPlanService.findOne(id));
		model.addAttribute("action", "update");

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);

		return "thesis/thesisPlan_form";
	}

	/**
	 * 更新
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequiresPermissions("/thesisPlan/list$update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(@Valid @ModelAttribute("entity") GjtThesisPlan entity, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtThesisPlan modifyInfo = gjtThesisPlanService.findOne(entity.getThesisPlanId());
			modifyInfo.setThesisPlanName(entity.getThesisPlanName());
			modifyInfo.setApplyBeginDt(entity.getApplyBeginDt());
			modifyInfo.setApplyEndDt(entity.getApplyEndDt());
			modifyInfo.setSubmitProposeEndDt(entity.getSubmitProposeEndDt());
			modifyInfo.setConfirmProposeEndDt(entity.getConfirmProposeEndDt());
			modifyInfo.setSubmitThesisEndDt(entity.getSubmitThesisEndDt());
			modifyInfo.setConfirmThesisEndDt(entity.getConfirmThesisEndDt());
			modifyInfo.setReviewDt(entity.getReviewDt());
			modifyInfo.setDefenceDt(entity.getDefenceDt());
			modifyInfo.setUpdatedBy(user.getId());

			if (modifyInfo.getStatus() == 2) { // 审核不通过，重新发布
				// 增加审批记录
				GjtThesisPlanApproval approval = new GjtThesisPlanApproval();
				approval.setThesisPlanId(entity.getThesisPlanId());
				approval.setOperaRole(1);
				approval.setOperaType(3);
				approval.setCreatedBy(user.getId());
				gjtThesisPlanApprovalService.insert(approval);

				modifyInfo.setStatus(1);
			}

			gjtThesisPlanService.update(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
			log.error(e.getMessage(), e);
		}
		
		return feedback;
	}

	@RequiresPermissions("/thesisPlan/list$view")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		model.addAttribute("entity", gjtThesisPlanService.findOne(id));
		model.addAttribute("action", "view");

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);
		
		return "thesis/thesisPlan_form";
	}

	@RequiresPermissions("/thesisPlan/list$delete")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, HttpServletRequest request) {
		if (StringUtils.isNotBlank(ids)) {
			try {
				GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
				GjtThesisPlan thesisPlan = gjtThesisPlanService.findOne(ids);
				thesisPlan.setIsDeleted("Y");
				thesisPlan.setDeletedBy(user.getId());
				thesisPlan.setDeletedDt(new Date());
				gjtThesisPlanService.update(thesisPlan);

				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	@RequiresPermissions("/thesisPlan/list$approval")
	@RequestMapping(value = "approval")
	public String approval(String id, int operaType, String description, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "审批成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

			// 增加审批记录
			GjtThesisPlanApproval approval = new GjtThesisPlanApproval();
			approval.setThesisPlanId(id);
			approval.setOperaRole(2);
			approval.setOperaType(2);
			approval.setDescription(description);
			approval.setCreatedBy(user.getId());
			gjtThesisPlanApprovalService.insert(approval);
			
			GjtThesisPlan thesisPlan = gjtThesisPlanService.findOne(id);
			if (operaType == 1) {
				thesisPlan.setStatus(3); // 通过
			} else {
				thesisPlan.setStatus(2); // 不通过
			}
			thesisPlan.setUpdatedBy(user.getId());
			gjtThesisPlanService.update(thesisPlan);
		} catch (Exception e) {
			feedback = new Feedback(false, "审批失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/thesisPlan/list";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

}
