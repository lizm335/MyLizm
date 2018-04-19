/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.signup;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.gzedu.xlims.pojo.GjtRollPlan;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.signup.GjtRollPlanService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 学籍计划控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年05月05日
 * @version 2.5
 */
@Controller
@RequestMapping("/edumanage/rollPlan")
public class RollPlanController extends BaseController {

	@Autowired
	private GjtRollPlanService gjtRollPlanService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private CommonMapService commonMapService;

	/**
	 * 学籍计划列表
	 * @param pageNumber
	 * @param pageSize
     * @return
     */
	@RequestMapping(value = "list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String xxId = searchParams.get("EQ_xxId") != null ? searchParams.get("EQ_xxId").toString() : null;
		if(StringUtils.isBlank(xxId)) {
			xxId = user.getGjtOrg().getId();
			searchParams.put("EQ_xxId", xxId);
			model.addAttribute("defaultXxId", xxId);
		}
		Object gradeId = searchParams.get("EQ_gjtGrade.gradeId");
		if(gradeId == null) {
			gradeId = gjtGradeService.getCurrentGradeId(xxId);
			searchParams.put("EQ_gjtGrade.gradeId", gradeId);
			model.addAttribute("defaultGradeId", gradeId);
		}
		Page<GjtRollPlan> page = gjtRollPlanService.queryByPage(searchParams, pageRequst);
		Object auditState = searchParams.remove("EQ_auditState");
		Map<String, BigDecimal> countAuditStateMap = gjtRollPlanService.countGroupbyAuditState(searchParams);
		searchParams.put("EQ_auditState", auditState);

		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级

		model.addAttribute("pageInfo", page);
		model.addAttribute("countAuditStateMap", countAuditStateMap);
		model.addAttribute("gradeMap", gradeMap);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "edumanage/rollPlan/roll_plan_list";
	}

	/**
	 * 查看学籍资料详情
	 * @param id
     * @return
     */
	@RequiresPermissions("/edumanage/rollPlan/list$view")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtRollPlan info = gjtRollPlanService.queryById(id);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级

		model.addAttribute("info", info);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("action", request.getParameter("action") != null ? request.getParameter("action") : "view");
		return "edumanage/rollPlan/roll_plan_form";
	}

	/**
	 * 加载学籍计划信息
	 * @return
	 */
	@RequiresPermissions("/edumanage/rollPlan/list$create")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		String gradeId = gjtGradeService.getCurrentGradeId(user.getGjtOrg().getId());

		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("defaultGradeId", gradeId);
		model.addAttribute("action", "create");
		return "edumanage/rollPlan/roll_plan_form";
	}

	/**
	 * 发布学籍计划
	 * @param info
	 * @return
	 */
	@SysLog("学籍计划-发布学籍计划")
	@RequiresPermissions("/edumanage/rollPlan/list$create")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtRollPlan info,
						 HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_gjtGrade.gradeId", info.getGjtGrade().getGradeId());
			long count = gjtRollPlanService.count(searchParams);
			if(count == 0) {
				String officialBeginDtStr = request.getParameter("officialBeginDtStr");
				String officialEndDtStr = request.getParameter("officialEndDtStr");
				String officialBeginDt2Str = request.getParameter("officialBeginDt2Str");
				String officialEndDt2Str = request.getParameter("officialEndDt2Str");
				String followBeginDtStr = request.getParameter("followBeginDtStr");
				String followEndDtStr = request.getParameter("followEndDtStr");
	
				if(StringUtils.isNotBlank(officialBeginDtStr)) {
					Date officialBeginDt = DateUtil.parseDate(officialBeginDtStr);
					info.setOfficialBeginDt(officialBeginDt);
				}
				if(StringUtils.isNotBlank(officialEndDtStr)) {
					Date officialEndDt = DateUtil.parseDate(officialEndDtStr);
					info.setOfficialEndDt(officialEndDt);
				}
				if(StringUtils.isNotBlank(officialBeginDt2Str)) {
					Date officialBeginDt2 = DateUtil.parseDate(officialBeginDt2Str);
					info.setOfficialBeginDt2(officialBeginDt2);
				}
				if(StringUtils.isNotBlank(officialEndDt2Str)) {
					Date officialEndDt2 = DateUtil.parseDate(officialEndDt2Str);
					info.setOfficialEndDt2(officialEndDt2);
				}
				if(StringUtils.isNotBlank(followBeginDtStr)) {
					Date followBeginDt = DateUtil.parseDate(followBeginDtStr);
					info.setFollowBeginDt(followBeginDt);
				}
				if(StringUtils.isNotBlank(followEndDtStr)) {
					Date followEndDt = DateUtil.parseDate(followEndDtStr);
					info.setFollowEndDt(followEndDt);
				}
				GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
				Date now = new Date();
				info.setPublishDt(now);
				info.setPublishOperator(user.getRealName());
				info.setId(UUIDUtils.random());
				info.setXxId(user.getGjtOrg().getId());
				info.setCreatedBy(user.getId());
				info.setCreatedDt(now);
				gjtRollPlanService.insert(info);
			} else {
				feedback = new Feedback(false, "发布失败，该学期已存在学籍计划！");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "发布失败，服务器异常！");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/rollPlan/list";
	}

	/**
	 * 加载学籍计划信息
	 * @param id
     * @return
     */
	@RequiresPermissions("/edumanage/rollPlan/list$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtRollPlan info = gjtRollPlanService.queryById(id);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级

		model.addAttribute("info", info);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("action", "update");
		return "edumanage/rollPlan/roll_plan_form";
	}

	/**
	 * 修改学籍计划
	 * @param info
     * @return
     */
	@SysLog("学籍计划-修改学籍计划")
	@RequiresPermissions("/edumanage/rollPlan/list$update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(GjtRollPlan info, Model model, HttpServletRequest request,
						 RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtRollPlan modifyInfo = gjtRollPlanService.queryById(info.getId());

		modifyInfo.setRollPlanNo(info.getRollPlanNo());
		modifyInfo.setRollPlanTitle(info.getRollPlanTitle());
		String officialBeginDtStr = request.getParameter("officialBeginDtStr");
		String officialEndDtStr = request.getParameter("officialEndDtStr");
		String officialBeginDt2Str = request.getParameter("officialBeginDt2Str");
		String officialEndDt2Str = request.getParameter("officialEndDt2Str");
		String followBeginDtStr = request.getParameter("followBeginDtStr");
		String followEndDtStr = request.getParameter("followEndDtStr");		
		String rollTransBeginDt = request.getParameter("rollTransBeginDt");
		String rollTransEndDt = request.getParameter("rollTransEndDt");
		String rollTransBeginDt2 = request.getParameter("rollTransBeginDt2");
		String rollTransEndDt2 = request.getParameter("rollTransEndDt2");

		if(StringUtils.isNotBlank(officialBeginDtStr)) {
			Date officialBeginDt = DateUtil.parseDate(officialBeginDtStr);
			modifyInfo.setOfficialBeginDt(officialBeginDt);
		}
		if(StringUtils.isNotBlank(officialEndDtStr)) {
			Date officialEndDt = DateUtil.parseDate(officialEndDtStr);
			modifyInfo.setOfficialEndDt(officialEndDt);
		}
		if(StringUtils.isNotBlank(officialBeginDt2Str)) {
			Date officialBeginDt2 = DateUtil.parseDate(officialBeginDt2Str);
			modifyInfo.setOfficialBeginDt2(officialBeginDt2);
		}
		if(StringUtils.isNotBlank(officialEndDt2Str)) {
			Date officialEndDt2 = DateUtil.parseDate(officialEndDt2Str);
			modifyInfo.setOfficialEndDt2(officialEndDt2);
		}
		if(StringUtils.isNotBlank(followBeginDtStr)) {
			Date followBeginDt = DateUtil.parseDate(followBeginDtStr);
			modifyInfo.setFollowBeginDt(followBeginDt);
		}
		if(StringUtils.isNotBlank(followEndDtStr)) {
			Date followEndDt = DateUtil.parseDate(followEndDtStr);
			modifyInfo.setFollowEndDt(followEndDt);
		}
		if(StringUtils.isNotBlank(rollTransBeginDt)) {
			Date rollTransBeginDtStr = DateUtil.parseDate(rollTransBeginDt);
			modifyInfo.setRollTransBeginDt(rollTransBeginDtStr);
		}
		if(StringUtils.isNotBlank(rollTransEndDt)) {
			Date rollTransEndDtStr = DateUtil.parseDate(rollTransEndDt);
			modifyInfo.setRollTransEndDt(rollTransEndDtStr);
		}
		if(StringUtils.isNotBlank(rollTransBeginDt2)) {
			Date rollTransBeginDt2Str = DateUtil.parseDate(rollTransBeginDt2);
			modifyInfo.setRollTransBeginDt2(rollTransBeginDt2Str);
		}
		if(StringUtils.isNotBlank(rollTransEndDt2)) {
			Date rollTransEndDt2Str = DateUtil.parseDate(rollTransEndDt2);
			modifyInfo.setRollTransEndDt2(rollTransEndDt2Str);
		}
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Date now = new Date();
		modifyInfo.setPublishDt(now);
		modifyInfo.setPublishOperator(user.getRealName());
		modifyInfo.setAuditState(new BigDecimal(0));
		modifyInfo.setUpdatedBy(user.getId());

		try {
			gjtRollPlanService.updateRollPlan(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/rollPlan/list";
	}

	/**
	 * 学籍计划审核
	 * @param id
	 * @param auditState
	 * @param auditContent
     * @return
     */
	@SysLog("学籍计划-学籍计划审核")
	@RequiresPermissions("/edumanage/rollPlan/list$approval")
	@RequestMapping(value = "audit", method = RequestMethod.POST)
	public String update(@RequestParam("id") String id,
						 @RequestParam("auditState") BigDecimal auditState,
						 String auditContent,
						 HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "审核成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		try {
			boolean flag = gjtRollPlanService.auditRollPlan(id, auditState, auditContent, user.getRealName(), user.getId());
			if(!flag) {
				feedback = new Feedback(false, "审核失败");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "审核失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/rollPlan/view/"+id+"?action=audit";
	}

}
