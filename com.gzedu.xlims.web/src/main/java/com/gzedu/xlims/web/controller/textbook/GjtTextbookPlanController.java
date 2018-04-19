package com.gzedu.xlims.web.controller.textbook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.textbook.GjtTextbookGrade;
import com.gzedu.xlims.pojo.textbook.GjtTextbookPlan;
import com.gzedu.xlims.pojo.textbook.GjtTextbookPlanApproval;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.textbook.GjtTextbookPlanApprovalService;
import com.gzedu.xlims.service.textbook.GjtTextbookPlanService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/textbookPlan")
public class GjtTextbookPlanController {

	private final static Logger log = LoggerFactory.getLogger(GjtTextbookPlanController.class);

	@Autowired
	private GjtTextbookPlanService gjtTextbookPlanService;

	@Autowired
	private GjtTextbookPlanApprovalService gjtTextbookPlanApprovalService;

	@Autowired
	private GjtGradeService gjtGradeService;


	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtOrgService gjtOrgService;

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

		Page<GjtTextbookPlan> pageInfo = gjtTextbookPlanService.findAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);

		// 查询“待审核”、“审核不通过”和“已发布”
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.put("EQ_status", 1);
		model.addAttribute("preAudit", gjtTextbookPlanService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_status", 2);
		model.addAttribute("auditNotPass", gjtTextbookPlanService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_status", 3);
		model.addAttribute("auditPass", gjtTextbookPlanService.findAll(map, pageRequst).getTotalElements());

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("canApproval", subject.isPermitted("/textbookPlan/list$approval"));
		model.addAttribute("isBtnUpdate", subject.isPermitted("/textbookPlan/list$update"));
		model.addAttribute("isBtnCreate", subject.isPermitted("/textbookPlan/list$createTextbookPlan"));
		model.addAttribute("isBtnDelete", subject.isPermitted("/textbookPlan/list$delete"));
		model.addAttribute("isBtnView", subject.isPermitted("/textbookPlan/list$view"));

		return "textbook/textbookPlan_list";
	}

	/**
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月18日 上午11:55:23
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "orderPlanList", method = RequestMethod.GET)
	public String orderPlanList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize, "startDate", "DESC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		String orgId = null;
		if ("1".equals(user.getGjtOrg().getOrgType())) {
			orgId = user.getGjtOrg().getId();
		} else {
			GjtOrg gjtOrg = gjtOrgService.queryParentBySonId(user.getGjtOrg().getOrgType());
			if (gjtOrg != null) {
				orgId = gjtOrg.getId();
			} else {
				orgId = user.getGjtOrg().getId();
			}
		}
		Page<GjtGrade> page = gjtGradeService.queryAll(orgId, searchParams, pageRequest);
		model.addAttribute("pageInfo", page);
		model.addAttribute("gradeMap", commonMapService.getGradeMap(orgId));
		return "textbook/orderPlan_list";
	}

	/**
	 * 修改教材订购时间
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月18日 下午4:51:15
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "updateOrderPlan/{id}", method = RequestMethod.GET)
	public String updateOrderPlan(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		model.addAttribute("entity", gjtGradeService.queryById(id));
		return "textbook/orderPlan_form";
	}

	@ResponseBody
	@RequestMapping(value = "updateOrderPlan", method = RequestMethod.POST)
	public Feedback updateOrderPlan(String gradeId, Date textbookStartDate, Date textbookEndDate) {
		Feedback feedback = new Feedback(true, "更新成功");
		try {
			GjtGrade gjtGrade = gjtGradeService.queryById(gradeId);
			gjtGrade.setTextbookStartDate(textbookStartDate);
			gjtGrade.setTextbookEndDate(textbookEndDate);
			gjtGradeService.saveEntity(gjtGrade);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback.setSuccessful(false);
			feedback.setMessage("系统异常");
		}
		return feedback;
	}

	/**
	 * 返回新增页面
	 * 
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/textbookPlan/list$createTextbookPlan")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		model.addAttribute("entity", new GjtTextbookPlan());
		model.addAttribute("action", "create");

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);

		return "textbook/textbookPlan_form";
	}

	/**
	 * 新增
	 * 
	 * @param entity
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequiresPermissions("/textbookPlan/list$createTextbookPlan")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(@Valid GjtTextbookPlan entity, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		try {
			// 校验所属学期唯一性
			/*
			 * GjtTextbookPlan plan1 =
			 * gjtTextbookPlanService.findByGradeIdAndOrgId(entity.getGradeId(),
			 * user.getGjtOrg().getId()); if (plan1 == null) { //校验编号唯一性
			 * GjtTextbookPlan plan2 =
			 * gjtTextbookPlanService.findByPlanCodeAndOrgId(entity.getPlanCode(
			 * ), user.getGjtOrg().getId()); if (plan2 == null) {
			 * entity.setCreatedBy(user.getId());
			 * entity.setOrgId(user.getGjtOrg().getId());
			 * gjtTextbookPlanService.insert(entity); } else { feedback = new
			 * Feedback(false, "计划编号【" + entity.getPlanCode() + "】已存在！" ); } }
			 * else { feedback = new Feedback(false, "所属学期已存在！" ); }
			 */

			String[] values = request.getParameterValues("gradeIds");
			List<GjtTextbookGrade> gjtTextbookGradeList = new ArrayList<GjtTextbookGrade>();
			for (String gradeId : values) {
				GjtTextbookGrade bookGrade = new GjtTextbookGrade();
				bookGrade.setId(UUIDUtils.random().toString());
				bookGrade.setGjtTextbookPlan(entity);
				bookGrade.setGjtGrade(new GjtGrade(gradeId));
				bookGrade.setCreatedBy(user.getId());
				bookGrade.setCreatedDt(DateUtils.getNowTime());
				gjtTextbookGradeList.add(bookGrade);
			}
			entity.setGjtTextbookGradeList(gjtTextbookGradeList);

			// 校验编号唯一性
			GjtTextbookPlan plan1 = gjtTextbookPlanService.findByPlanCodeAndOrgId(entity.getPlanCode(),
					user.getGjtOrg().getId());
			if (plan1 == null) {
				// 查询相同学期的计划列表
				PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE, "createdDt", "DESC");
				Map<String, Object> searchParams = new HashMap<String, Object>();
				searchParams.put("EQ_orgId", user.getGjtOrg().getId());
				searchParams.put("EQ_gradeId", entity.getGradeId());
				Page<GjtTextbookPlan> pageInfo = gjtTextbookPlanService.findAll(searchParams, pageRequst);
				if (pageInfo != null && pageInfo.getContent() != null && pageInfo.getContent().size() > 0) {
					// 检查是否存在未发布的计划
					boolean noPublish = false;
					for (GjtTextbookPlan plan : pageInfo) {
						if (plan.getStatus() != 3) {
							noPublish = true;
							break;
						}
					}
					if (noPublish) {
						feedback = new Feedback(false, "当前学期存在未发布的计划，请先提交审核发布！");
					} else {
						// 检查教材发放编排时间是否在上一个计划的教材订购时间之后
						GjtTextbookPlan plan2 = pageInfo.getContent().get(0);
						if (entity.getArrangeSdate().compareTo(plan2.getOrderEdate()) > 0) {
							entity.setCreatedBy(user.getId());
							entity.setOrgId(user.getGjtOrg().getId());
							gjtTextbookPlanService.insert(entity);
						} else {
							feedback = new Feedback(false, "教材发放编排时间设置必须要在上个次计划中的教材订购时间之后！");
						}
					}
				} else {
					entity.setCreatedBy(user.getId());
					entity.setOrgId(user.getGjtOrg().getId());
					gjtTextbookPlanService.insert(entity);
				}

			} else {
				feedback = new Feedback(false, "计划编号【" + entity.getPlanCode() + "】已存在！");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
			log.error(e.getMessage(), e);
		}

		// redirectAttributes.addFlashAttribute("feedback", feedback);
		return feedback;
	}

	/**
	 * 返回编辑页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/textbookPlan/list$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		model.addAttribute("entity", gjtTextbookPlanService.findOne(id));
		model.addAttribute("action", "update");

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);

		return "textbook/textbookPlan_form";
	}

	/**
	 * 更新
	 * 
	 * @param entity
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequiresPermissions("/textbookPlan/list$update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(@Valid @ModelAttribute("entity") GjtTextbookPlan entity,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtTextbookPlan modifyInfo = gjtTextbookPlanService.findOne(entity.getPlanId());
			modifyInfo.setPlanName(entity.getPlanName());
			modifyInfo.setArrangeSdate(entity.getArrangeSdate());
			modifyInfo.setArrangeEdate(entity.getArrangeEdate());
			modifyInfo.setOrderSdate(entity.getOrderSdate());
			modifyInfo.setOrderEdate(entity.getOrderEdate());
			/*
			 * modifyInfo.setNaddressConfirmSdate(entity.getNaddressConfirmSdate
			 * ());
			 * modifyInfo.setNaddressConfirmEdate(entity.getNaddressConfirmEdate
			 * ());
			 * modifyInfo.setNdistributeSdate(entity.getNdistributeSdate());
			 * modifyInfo.setNdistributeEdate(entity.getNdistributeEdate());
			 * modifyInfo.setNfeedbackSdate(entity.getNfeedbackSdate());
			 * modifyInfo.setNfeedbackEdate(entity.getNfeedbackEdate());
			 */
			modifyInfo.setOaddressConfirmSdate(entity.getOaddressConfirmSdate());
			modifyInfo.setOaddressConfirmEdate(entity.getOaddressConfirmEdate());
			modifyInfo.setOdistributeSdate(entity.getOdistributeSdate());
			modifyInfo.setOdistributeEdate(entity.getOdistributeEdate());
			modifyInfo.setOfeedbackSdate(entity.getOfeedbackSdate());
			modifyInfo.setOfeedbackEdate(entity.getOfeedbackEdate());
			modifyInfo.setUpdatedBy(user.getId());

			// 检查教材发放编排时间是否在上一个计划的教材订购时间之后
			PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE, "createdDt", "DESC");
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_orgId", user.getGjtOrg().getId());
			searchParams.put("EQ_gradeId", modifyInfo.getGradeId());
			Page<GjtTextbookPlan> pageInfo = gjtTextbookPlanService.findAll(searchParams, pageRequst);
			if (pageInfo != null && pageInfo.getContent() != null && pageInfo.getContent().size() > 1) {
				GjtTextbookPlan plan = pageInfo.getContent().get(1);
				if (modifyInfo.getArrangeSdate().compareTo(plan.getOrderEdate()) > 0) {
					if (modifyInfo.getStatus() == 2) { // 审核不通过，重新发布
						// 增加审批记录
						GjtTextbookPlanApproval approval = new GjtTextbookPlanApproval();
						approval.setPlanId(modifyInfo.getPlanId());
						approval.setOperaRole(1);
						approval.setOperaType(3);
						approval.setCreatedBy(user.getId());
						gjtTextbookPlanApprovalService.insert(approval);

						modifyInfo.setStatus(1);
					}

					gjtTextbookPlanService.update(modifyInfo);
				} else {
					feedback = new Feedback(false, "教材发放编排时间设置必须要在上个次计划中的教材订购时间之后！");
				}
			} else {
				if (modifyInfo.getStatus() == 2) { // 审核不通过，重新发布
					// 增加审批记录
					GjtTextbookPlanApproval approval = new GjtTextbookPlanApproval();
					approval.setPlanId(modifyInfo.getPlanId());
					approval.setOperaRole(1);
					approval.setOperaType(3);
					approval.setCreatedBy(user.getId());
					gjtTextbookPlanApprovalService.insert(approval);

					modifyInfo.setStatus(1);
				}

				gjtTextbookPlanService.update(modifyInfo);
			}

		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
			log.error(e.getMessage(), e);
		}
		// redirectAttributes.addFlashAttribute("feedback", feedback);
		return feedback;
	}

	@RequiresPermissions("/textbookPlan/list$view")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", gjtTextbookPlanService.findOne(id));
		model.addAttribute("action", "view");
		return "textbook/textbookPlan_form";
	}

	@RequiresPermissions("/textbookPlan/list$delete")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, HttpServletRequest request) {
		if (StringUtils.isNotBlank(ids)) {
			try {
				GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
				GjtTextbookPlan textbookPlan = gjtTextbookPlanService.findOne(ids);
				textbookPlan.setIsDeleted("Y");
				textbookPlan.setDeletedBy(user.getId());
				textbookPlan.setDeletedDt(new Date());
				gjtTextbookPlanService.update(textbookPlan);

				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	@RequiresPermissions("/textbookPlan/list$approval")
	@RequestMapping(value = "approval")
	public String approval(String id, int operaType, String description, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "审批成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

			// 增加审批记录
			GjtTextbookPlanApproval approval = new GjtTextbookPlanApproval();
			approval.setPlanId(id);
			approval.setOperaRole(2);
			approval.setOperaType(2);
			approval.setDescription(description);
			approval.setCreatedBy(user.getId());
			gjtTextbookPlanApprovalService.insert(approval);

			GjtTextbookPlan textbookPlan = gjtTextbookPlanService.findOne(id);
			if (operaType == 1) {
				textbookPlan.setStatus(3); // 通过
			} else {
				textbookPlan.setStatus(2); // 不通过
			}
			textbookPlan.setUpdatedBy(user.getId());
			gjtTextbookPlanService.update(textbookPlan);

			/*
			 * if (textbookPlan.getStatus() == 3) { // 审批通过，生成发放编排 try { //
			 * 查询可生成安排的主教材 List<GjtTextbook> textbookList = gjtTextbookService
			 * .findCurrentArrangeTextbook(textbookPlan.getPlanId(),
			 * user.getGjtOrg().getId()); if (textbookList != null &&
			 * textbookList.size() > 0) { List<GjtTextbookArrange>
			 * textbookArrangeList = new ArrayList<GjtTextbookArrange>(); for
			 * (GjtTextbook textbook : textbookList) { GjtTextbookArrange
			 * textbookArrange = new GjtTextbookArrange();
			 * textbookArrange.setGjtTextbookPlan(textbookPlan);
			 * textbookArrange.setGjtTextbook(textbook);
			 * textbookArrange.setCreatedBy(user.getId());
			 * 
			 * textbookArrangeList.add(textbookArrange); }
			 * 
			 * gjtTextbookArrangeService.insert(textbookArrangeList); } } catch
			 * (Exception e) {
			 * 
			 * } }
			 */
		} catch (Exception e) {
			feedback = new Feedback(false, "审批失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/textbookPlan/list";
	}

	// 校验编码是否存在
	@RequestMapping(value = "checkCode")
	@ResponseBody
	public Feedback checkCode(String planCode, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtTextbookPlan plan = gjtTextbookPlanService.findByPlanCodeAndOrgId(planCode, user.getGjtOrg().getId());
		if (plan != null) {
			return new Feedback(true, "");
		} else {
			return new Feedback(false, "");
		}
	}

	// 校验学期是否已添加
	@RequestMapping(value = "checkGradeId")
	@ResponseBody
	public Feedback checkGradeId(String gradeId, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtTextbookPlan plan = gjtTextbookPlanService.findByGradeIdAndOrgId(gradeId, user.getGjtOrg().getId());
		if (plan != null) {
			return new Feedback(true, "");
		} else {
			return new Feedback(false, "");
		}
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

}
