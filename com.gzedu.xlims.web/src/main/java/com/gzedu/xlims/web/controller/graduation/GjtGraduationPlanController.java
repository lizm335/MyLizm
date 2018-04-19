package com.gzedu.xlims.web.controller.graduation;

import com.gzedu.xlims.common.ResultFeedback;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtGraPlanFlowRecord;
import com.gzedu.xlims.pojo.graduation.GjtGraduationPlan;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.graduation.GjtGraduationPlanService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 毕业计划控制层<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年08月29日
 * @version 3.0
 */
@Controller
@RequestMapping("/graduation/plan")
public class GjtGraduationPlanController extends BaseController {
	
	private static final Log log = LogFactory.getLog(GjtGraduationPlanController.class);

	@Autowired
	private GjtGraduationPlanService gjtGraduationPlanService;

	@Autowired
	private CodeGeneratorService codeGeneratorService;

	@Autowired
	private CommonMapService commonMapService;

	/**
	 * 查询毕业计划列表
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_xxId", user.getGjtOrg().getId());
		Page<GjtGraduationPlan> pageInfo = gjtGraduationPlanService.queryGraduationPlanListByPage(searchParams, pageRequst);
		Map<String, Long> countAuditStateMap = gjtGraduationPlanService.countGroupbyAuditState(searchParams);

		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 学期

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("countAuditStateMap", countAuditStateMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "graduation/plan/graduation_plan_list";
	}

	/**
	 * 返回详情页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/graduation/plan/list$view")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("entity", gjtGraduationPlanService.queryById(id));

		List<GjtGraPlanFlowRecord> flowRecordList = gjtGraduationPlanService.queryGraPlanFlowRecordByPlanId(id);
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());

		model.addAttribute("flowRecordList", flowRecordList);
		model.addAttribute("termMap", termMap);
		model.addAttribute("action", request.getParameter("action") != null ? request.getParameter("action") : "view");
		return "graduation/plan/graduation_plan_form";
	}

	/**
	 * 返回新增页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("entity", new GjtGraduationPlan());
		model.addAttribute("action", "create");
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);
		return "graduation/plan/graduation_plan_form";
	}

	/**
	 * 新增
	 * @param entity
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@SysLog("毕业管理-毕业计划-新增")
	@RequiresPermissions("/graduation/plan/list$create")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtGraduationPlan entity,
						 RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			GjtGraduationPlan plan = gjtGraduationPlanService.findByTermId(entity.getGjtGrade().getGradeId(), user.getGjtOrg().getId());
			if (plan == null) {
				entity.setId(UUIDUtils.random());
				entity.setCreatedBy(user.getId());
				entity.setXxId(user.getGjtOrg().getId());
				entity.setGraPlanNo(codeGeneratorService.codeGenerator(CacheConstants.GRADUATION_PLAN_CODE));
				gjtGraduationPlanService.insert(entity);
				gjtGraduationPlanService.initAuditGraduationPlan(entity.getId()); // 提交计划
			} else {
				feedback = new Feedback(false, "该学期已创建计划！" );
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/graduation/plan/list";
	}

	/**
	 * 返回编辑页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("entity", gjtGraduationPlanService.queryById(id));
		model.addAttribute("action", "update");
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);
		return "graduation/plan/graduation_plan_form";
	}

	/**
	 * 更新
	 * @param entity
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@SysLog("毕业管理-毕业计划-修改")
	@RequiresPermissions("/graduation/plan/list$update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity") GjtGraduationPlan entity,
						 RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");

		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtGraduationPlan plan = gjtGraduationPlanService.findByTermId(entity.getGjtGrade().getGradeId(), user.getGjtOrg().getId());
			if (plan == null || plan.getId().equals(entity.getId())) {
				GjtGraduationPlan modifyInfo = gjtGraduationPlanService.queryById(entity.getId());

				modifyInfo.setGraPlanTitle(entity.getGraPlanTitle());
				modifyInfo.setGraApplyBeginDt(entity.getGraApplyBeginDt());
				modifyInfo.setGraApplyEndDt(entity.getGraApplyEndDt());
				modifyInfo.setDegreeApplyBeginDt(entity.getDegreeApplyBeginDt());
				modifyInfo.setDegreeApplyEndDt(entity.getDegreeApplyEndDt());
				modifyInfo.setGraAuditBeginDt(entity.getGraAuditBeginDt());
				modifyInfo.setGraAuditEndDt(entity.getGraAuditEndDt());
				modifyInfo.setDegreeAuditBeginDt(entity.getDegreeAuditBeginDt());
				modifyInfo.setDegreeAuditEndDt(entity.getDegreeAuditEndDt());
				modifyInfo.setDegreeBackBeginDt(entity.getDegreeBackBeginDt());
				modifyInfo.setDegreeBackEndDt(entity.getDegreeBackEndDt());
				modifyInfo.setGraCertReceiveBeginDt(entity.getGraCertReceiveBeginDt());
				modifyInfo.setGraCertReceiveEndDt(entity.getGraCertReceiveEndDt());
				modifyInfo.setGraArchivesReceiveBeginDt(entity.getGraArchivesReceiveBeginDt());
				modifyInfo.setGraArchivesReceiveEndDt(entity.getGraArchivesReceiveEndDt());
				modifyInfo.setDegreeCertReceiveBeginDt(entity.getDegreeCertReceiveBeginDt());
				modifyInfo.setDegreeCertReceiveEndDt(entity.getDegreeCertReceiveEndDt());
				modifyInfo.setUpdatedBy(user.getId());

				gjtGraduationPlanService.updateGraduationPlan(modifyInfo);
				gjtGraduationPlanService.initAuditGraduationPlan(entity.getId()); // 提交计划
			} else {
				feedback = new Feedback(false, "该学期已创建计划！" );
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/graduation/plan/list";
	}

	/**
	 * 删除
	 * @param ids
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@SysLog("毕业管理-毕业计划-删除")
	@RequiresPermissions("/graduation/plan/list$delete")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, HttpServletRequest request) throws IOException {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			gjtGraduationPlanService.delete(ids, user.getId());
			return new Feedback(true, "删除成功");
		} catch (Exception e) {
			return new Feedback(false, "删除失败，原因:" + e.getMessage());
		}
	}

	/**
	 * 资料审核
	 * @param id
	 * @param auditState
	 * @param auditContent
	 * @return
	 */
	@SysLog("毕业管理-毕业计划-审核")
	@RequiresPermissions("/graduation/plan/list$approval")
	@RequestMapping(value = "audit", method = RequestMethod.POST)
	public String update(@RequestParam("id") String id,
						 @RequestParam("auditState") Integer auditState,
						 String auditContent,
						 HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "审核成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		try {
			ResultFeedback result = gjtGraduationPlanService.auditGraduationPlan(id, auditState, auditContent, 5, user.getRealName());
			if(!result.isSuccessful()) {
				feedback = new Feedback(false, result.getMessage());
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "审核失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/graduation/plan/view/"+id+"?action=audit";
	}

	/**
	 * 前后端日期格式转换
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   //true:允许输入空值，false:不能为空值
	}
	
}
