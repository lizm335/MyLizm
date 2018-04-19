package com.gzedu.xlims.web.controller.graduation;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.gzedu.xlims.pojo.graduation.GjtGraduationBatch;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.graduation.GjtGraduationBatchService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/graduation/graduationBatch")
public class GjtGraduationBatchController {
	
	private static final Log log = LogFactory.getLog(GjtGraduationBatchController.class);

	@Autowired
	private GjtGraduationBatchService gjtGraduationBatchService;

	@Autowired
	private CodeGeneratorService codeGeneratorService;

	@Autowired
	CommonMapService commonMapService;
	
	/**
	 * 查询毕业批次列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());

		//按状态统计
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.put("EQ_orgId", user.getGjtOrg().getId());
		map.put("GT_gjtGrade.startDate", today); //未开始
		model.addAttribute("notStart", gjtGraduationBatchService.queryAll(map, pageRequst).getTotalElements());
		map.remove("GT_gjtGrade.startDate");
		map.put("LTE_gjtGrade.startDate", today);
		map.put("GTE_gjtGrade.endDate", today);
		model.addAttribute("starting", gjtGraduationBatchService.queryAll(map, pageRequst).getTotalElements());
		map.remove("LTE_gjtGrade.startDate");
		map.remove("GTE_gjtGrade.endDate");
		map.put("LT_gjtGrade.endDate", today); //已结束
		model.addAttribute("end", gjtGraduationBatchService.queryAll(map, pageRequst).getTotalElements());
		
		//状态判断
		if (request.getParameter("status") != null) {
			int status = Integer.parseInt(request.getParameter("status").toString());
			if (status == 1) {  //未开始
				searchParams.put("GT_gjtGrade.startDate", today);
			} else if (status == 2) {  //进行中
				searchParams.put("LTE_gjtGrade.startDate", today);
				searchParams.put("GTE_gjtGrade.endDate", today);
			} else if (status == 3) {  //已结束
				searchParams.put("LT_gjtGrade.endDate", today);
			}
		}
		
		Page<GjtGraduationBatch> pageInfo = gjtGraduationBatchService.queryAll(searchParams, pageRequst);
		Date now = new Date();
		for (GjtGraduationBatch batch : pageInfo) {
			if (batch.getGjtGrade().getStartDate().compareTo(now) > 0) {
				batch.setStatus(1);
			} else if (batch.getGjtGrade().getStartDate().compareTo(now) <= 0
					&& batch.getGjtGrade().getEndDate().compareTo(now) >= 0) {
				batch.setStatus(2);
			} else if (batch.getGjtGrade().getEndDate().compareTo(now) < 0) {
				batch.setStatus(3);
			}
		}
		model.addAttribute("pageInfo", pageInfo);
		
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);
		
		return "graduation/graduationBatch/list";
	}

	/**
	 * 返回新增页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("entity", new GjtGraduationBatch());
		model.addAttribute("action", "create");
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);
		return "graduation/graduationBatch/form";
	}

	/**
	 * 新增
	 * @param entity
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtGraduationBatch entity,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			GjtGraduationBatch graduationBatch = gjtGraduationBatchService.findByGradeId(entity.getGradeId(), user.getGjtOrg().getId());
			if (graduationBatch == null) {
				entity.setCreatedBy(user.getId());
				entity.setOrgId(user.getGjtOrg().getId());
				entity.setBatchCode(codeGeneratorService.codeGenerator(CacheConstants.GRADUATION_BATCH_CODE));
				gjtGraduationBatchService.insert(entity);
			} else {
				feedback = new Feedback(false, "该学期已创建计划！" );
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/graduation/graduationBatch/list";
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
		model.addAttribute("entity", gjtGraduationBatchService.queryById(id));
		model.addAttribute("action", "update");
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);
		return "graduation/graduationBatch/form";
	}

	/**
	 * 更新
	 * @param entity
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity") GjtGraduationBatch entity,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");

		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtGraduationBatch graduationBatch = gjtGraduationBatchService.findByGradeId(entity.getGradeId(), user.getGjtOrg().getId());
			if (graduationBatch == null || graduationBatch.getBatchId().equals(entity.getBatchId())) {
				GjtGraduationBatch modifyInfo = gjtGraduationBatchService.queryById(entity.getBatchId());
				
				modifyInfo.setBatchName(entity.getBatchName());
				modifyInfo.setStudyYearCode(entity.getStudyYearCode());
				modifyInfo.setGuideLimitNum(entity.getGuideLimitNum());
				modifyInfo.setDefenceLimitNum(entity.getDefenceLimitNum());
				modifyInfo.setPracticeLimitNum(entity.getPracticeLimitNum());
				modifyInfo.setApplyThesisBeginDt(entity.getApplyThesisBeginDt());
				modifyInfo.setApplyThesisEndDt(entity.getApplyThesisEndDt());
				modifyInfo.setSubmitProposeEndDt(entity.getSubmitProposeEndDt());
				modifyInfo.setConfirmProposeEndDt(entity.getConfirmProposeEndDt());
				modifyInfo.setSubmitThesisEndDt(entity.getSubmitThesisEndDt());
				modifyInfo.setConfirmThesisEndDt(entity.getConfirmThesisEndDt());
				modifyInfo.setReviewThesisDt(entity.getReviewThesisDt());
				modifyInfo.setDefenceThesisDt(entity.getDefenceThesisDt());
				modifyInfo.setApplyPracticeBeginDt(entity.getApplyPracticeBeginDt());
				modifyInfo.setApplyPracticeEndDt(entity.getApplyPracticeEndDt());
				modifyInfo.setSubmitPracticeEndDt(entity.getSubmitPracticeEndDt());
				modifyInfo.setConfirmPracticeEndDt(entity.getConfirmPracticeEndDt());
				modifyInfo.setReviewPracticeDt(entity.getReviewPracticeDt());
				modifyInfo.setUpdatedBy(user.getId());
		
				gjtGraduationBatchService.update(modifyInfo);
			} else {
				feedback = new Feedback(false, "该学期已创建计划！" );
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/graduation/graduationBatch/list";
	}

	/**
	 * 删除
	 * @param id
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, HttpServletRequest request) throws IOException {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			gjtGraduationBatchService.delete(ids, user.getId());
			return new Feedback(true, "删除成功");
		} catch (Exception e) {
			return new Feedback(false, "删除失败，原因:" + e.getMessage());
		}
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
