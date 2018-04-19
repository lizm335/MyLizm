package com.gzedu.xlims.web.controller.textbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistribute;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistributeDetail;
import com.gzedu.xlims.pojo.textbook.GjtTextbookFeedback;
import com.gzedu.xlims.pojo.textbook.GjtTextbookFeedbackDetail;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.textbook.GjtTextbookDistributeService;
import com.gzedu.xlims.service.textbook.GjtTextbookFeedbackService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/textbookFeedback")
public class GjtTextbookFeedbackController {

	private static final Logger log = LoggerFactory.getLogger(GjtTextbookFeedbackController.class);

	@Autowired
	private GjtTextbookFeedbackService gjtTextbookFeedbackService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtTeachPlanService gjtTeachPlanService;

	@Autowired
	private GjtTextbookDistributeService gjtTextbookDistributeService;

	@Autowired
	private CommonMapService commonMapService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_gjtStudentInfo.xxId", user.getGjtOrg().getId());

		Page<GjtTextbookFeedback> pageInfo = gjtTextbookFeedbackService.findAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);

		// 查询“待处理”状态和“已处理”状态的反馈总数
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.put("EQ_status", 1);
		model.addAttribute("pending", gjtTextbookFeedbackService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_status", 2);
		model.addAttribute("processed", gjtTextbookFeedbackService.findAll(map, pageRequst).getTotalElements());

		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnCreate", subject.isPermitted("/textbookFeedback/list$create"));
		model.addAttribute("isBtnView", subject.isPermitted("/textbookFeedback/list$view"));

		return "textbook/textbookFeedback_list";
	}

	/**
	 * 返回新增页面
	 * 
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/textbookFeedback/list$create")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("entity", new GjtTextbookFeedback());
		model.addAttribute("action", "create");
		return "textbook/textbookFeedback_form";
	}

	/**
	 * 新增
	 * 
	 * @param entity
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequiresPermissions("/textbookFeedback/list$create")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtTextbookFeedback entity, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(entity.getGjtStudentInfo().getXh());
			if (studentInfo != null) {
				String stuNameByForm = entity.getGjtStudentInfo().getXm();
				if (StringUtils.isNotEmpty(stuNameByForm) && !studentInfo.getXm().equals(stuNameByForm)) {
					feedback = new Feedback(false, "学号为【" + entity.getGjtStudentInfo().getXh() + "】的学生姓名不是【"
							+ entity.getGjtStudentInfo().getXm() + "】！");
				} else {
					entity.setGjtStudentInfo(studentInfo);
					entity.setStatus(1);
					entity.setCreatedBy(user.getId());

					List<GjtTextbookFeedbackDetail> textbookFeedbackDetails = new ArrayList<GjtTextbookFeedbackDetail>();
					for (String textbookId : entity.getTextbookIds()) {
						GjtTextbookFeedbackDetail detail = new GjtTextbookFeedbackDetail();
						detail.setTextbookId(textbookId);
						textbookFeedbackDetails.add(detail);
					}
					entity.setGjtTextbookFeedbackDetails(textbookFeedbackDetails);

					gjtTextbookFeedbackService.insert(entity);
				}
			} else {
				feedback = new Feedback(false, "未找到学号为【" + entity.getGjtStudentInfo().getXh() + "】的学生信息！");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/textbookFeedback/list";
	}

	/**
	 * 返回选择教材列表页面
	 * 
	 * @param studentCode
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "selectTextbook")
	public String selectTextbook(@RequestParam String studentCode, Model model) {
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(studentCode);
		if (studentInfo == null) {
			model.addAttribute("errorMsg", "未找到学号为[" + studentCode + "]的学生信息！");
		} else {
			// 查询学员的教学计划列表，并按照学期归并
			List<GjtTeachPlan> gjtTeachPlans = gjtTeachPlanService.queryGjtTeachPlan(studentInfo.getGradeSpecialtyId());
			Map<Integer, List<GjtTeachPlan>> gradeSpecialtyPlanMap = new LinkedHashMap<Integer, List<GjtTeachPlan>>();
			if (gjtTeachPlans != null && gjtTeachPlans.size() > 0) {
				for (GjtTeachPlan gjtTeachPlan : gjtTeachPlans) {
					int termTypeCode = gjtTeachPlan.getKkxq();
					if (gradeSpecialtyPlanMap.get(termTypeCode) == null) {
						List<GjtTeachPlan> list = new ArrayList<GjtTeachPlan>();
						list.add(gjtTeachPlan);
						gradeSpecialtyPlanMap.put(termTypeCode, list);
					} else {
						List<GjtTeachPlan> list = gradeSpecialtyPlanMap.get(termTypeCode);
						list.add(gjtTeachPlan);
					}
				}
			}

			List<Integer> statuses = new ArrayList<Integer>();
			statuses.add(1);
			statuses.add(2);
			statuses.add(3);
			List<GjtTextbookDistribute> textbookDistributes = gjtTextbookDistributeService
					.findByStudentIdAndIsDeletedAndStatusIn(studentInfo.getStudentId(), "N", statuses);

			// 构建学生发放教材的id与发放状态对应map
			Map<String, Integer> statusMap = new HashMap<String, Integer>();
			if (textbookDistributes != null && textbookDistributes.size() > 0) {
				for (GjtTextbookDistribute textbookDistribute : textbookDistributes) {
					List<GjtTextbookDistributeDetail> textbookDistributeDetails = textbookDistribute
							.getGjtTextbookDistributeDetails();
					for (GjtTextbookDistributeDetail detail : textbookDistributeDetails) {
						statusMap.put(detail.getTextbookId(), detail.getStatus());
					}
				}
			}

			model.addAttribute("statusMap", statusMap);
			model.addAttribute("gradeSpecialtyPlanMap", gradeSpecialtyPlanMap);
		}

		return "textbook/textbookFeedback_selectTextbook";
	}

	@RequestMapping(value = "view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		GjtTextbookFeedback textbookFeedback = gjtTextbookFeedbackService.findOne(id);

		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(1);
		statuses.add(2);
		statuses.add(3);
		List<GjtTextbookDistribute> textbookDistributes = gjtTextbookDistributeService
				.findByStudentIdAndIsDeletedAndStatusIn(textbookFeedback.getGjtStudentInfo().getStudentId(), "N",
						statuses);

		model.addAttribute("entity", textbookFeedback);
		model.addAttribute("textbookDistributes", textbookDistributes);
		model.addAttribute("trainingLevelMap", commonMapService.getPyccMap());
		
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnDeal", subject.isPermitted("/textbookFeedback/list$dealFeedback"));

		return "textbook/textbookFeedback_detail";
	}

	@RequiresPermissions("/textbookFeedback/list$update")
	@RequestMapping(value = "deal")
	public String dealFeedback(@RequestParam String feedbackId, @RequestParam String reply,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtTextbookFeedback textbookFeedback = gjtTextbookFeedbackService.findOne(feedbackId);
		textbookFeedback.setStatus(2);
		textbookFeedback.setReply(reply);
		textbookFeedback.setUpdatedBy(user.getId());

		gjtTextbookFeedbackService.update(textbookFeedback);

		return "redirect:/textbookFeedback/view/" + feedbackId;
	}

}
