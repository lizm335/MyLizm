package com.gzedu.xlims.web.controller.exam;

import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamBatchApproval;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.exam.GjtExamBatchApprovalService;
import com.gzedu.xlims.service.exam.GjtExamBatchNewService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.common.vo.CommonSelect;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
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
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Controller
@RequestMapping("/exam/new/batch")
public class GjtExamBatchNewController {

	private static final Log log = LogFactory.getLog(GjtExamBatchNewController.class);

	@Autowired
	private GjtExamBatchNewService gjtExamBatchNewService;
	@Autowired
	private CodeGeneratorService codeGeneratorService;
	@Autowired
	private CommonMapService commonMapService;
	
	@Autowired
	private GjtExamBatchApprovalService gjtExamBatchApprovalService;

	@Autowired
	private GjtOrgService gjtOrgService;

	/**
	 * 列表 
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		if ("1".equals(user.getGjtOrg().getOrgType())) {
			searchParams.put("EQ_xxId", user.getGjtOrg().getId());
		} else {
			List<String> parents = gjtOrgService.queryParents(user.getGjtOrg().getId());
			if (parents != null && parents.size() > 0) {
				searchParams.put("IN_xxId", parents);
			} else {
				searchParams.put("EQ_xxId", user.getGjtOrg().getId());
			}
		}
		
		// 查询“待审核”、“审核不通过”、“已发布”和“已过期”
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.put("GTE_recordEnd", today);
		map.put("EQ_planStatus", "1");
		model.addAttribute("preAudit", gjtExamBatchNewService.queryAll(map, pageRequst).getTotalElements());
		map.put("EQ_planStatus", "2");
		model.addAttribute("auditNotPass", gjtExamBatchNewService.queryAll(map, pageRequst).getTotalElements());
		map.put("EQ_planStatus", "3");
		model.addAttribute("auditPass", gjtExamBatchNewService.queryAll(map, pageRequst).getTotalElements());
		map.remove("EQ_planStatus");
		map.remove("GTE_recordEnd");
		map.put("LT_recordEnd", today);
		model.addAttribute("overdue", gjtExamBatchNewService.queryAll(map, pageRequst).getTotalElements());
		
		if (searchParams.get("EQ_planStatus") != null && !"".equals(searchParams.get("EQ_planStatus").toString())) {
			if ("4".equals(searchParams.get("EQ_planStatus").toString())) {
				searchParams.remove("EQ_planStatus");
				searchParams.put("LT_recordEnd", today);
			} else {
				searchParams.put("GTE_recordEnd", today);
			}
		}
		Page<GjtExamBatchNew> pageInfo = gjtExamBatchNewService.queryAll(searchParams, pageRequst);
		for (GjtExamBatchNew examBatch : pageInfo) {  //设置“已过期”
			if (examBatch.getRecordEnd() != null && examBatch.getRecordEnd().compareTo(new Date()) < 0) {
				examBatch.setPlanStatus("4");
			}
		}
		model.addAttribute("pageInfo", pageInfo);

		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("gradeMap", gradeMap);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("canApproval", subject.isPermitted("/exam/new/batch/list$approval"));
		model.addAttribute("isBtnUpdate", subject.isPermitted("/exam/new/batch/list$update"));
		model.addAttribute("isBtnCreate", subject.isPermitted("/exam/new/batch/list$create"));
		model.addAttribute("isBtnDelete", subject.isPermitted("/exam/new/batch/list$delete"));
		model.addAttribute("isBtnView", subject.isPermitted("/exam/new/batch/list$view"));
		
		return "edumanage/exam/exam_batch_list";
	}
	

	/**
	 * 新增
	 * 
	 * @param request
	 * @param examBatch
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback add(HttpServletRequest request, @ModelAttribute GjtExamBatchNew examBatch) {
		Feedback feedback = new Feedback();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		examBatch.setPlanStatus("1");
		examBatch.setXxId(user.getGjtOrg().getId());
		examBatch.setCreatedBy(user.getId());
		examBatch.setCreatedDt(new Date());

		// 支持一个年级添加多个考试计划
		examBatch = gjtExamBatchNewService.insert(examBatch);
		if (null != examBatch) {
			feedback.setSuccessful(true);
			feedback.setMessage("创建成功");
		} else {
			feedback.setSuccessful(false);
			feedback.setMessage("创建失败");
		}
		return feedback;
	}

	/**
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	@RequiresPermissions("/exam/new/batch/list$delete")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(@RequestParam String ids, HttpServletRequest request) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (StringUtils.isNotBlank(ids)) {
			try {
				GjtExamBatchNew examBatch = gjtExamBatchNewService.queryBy(ids);
				examBatch.setIsDeleted(1);
				examBatch.setUpdatedBy(user.getId());
				examBatch.setUpdatedDt(new Date());
				gjtExamBatchNewService.update(examBatch);

				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return new Feedback(false, "删除失败");
			}
		}
		
		return new Feedback(false, "删除失败");
	}

	@RequestMapping(value = "{op}/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable String op, @PathVariable String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtExamBatchNew examBatch = new GjtExamBatchNew();
		if ("create".equals(op)) {
			examBatch.setExamBatchCode(codeGeneratorService.codeGenerator(CacheConstants.EXAM_BATCH_CODE));
		} else {
			examBatch = gjtExamBatchNewService.queryBy(id);
			model.addAttribute("entity", examBatch);
		}

		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("entity", examBatch);
		model.addAttribute("action", op);
		return "edumanage/exam/exam_batch_form";
	}

	/**
	 * 更新
	 * 
	 * @param entity
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(@ModelAttribute GjtExamBatchNew entity, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtExamBatchNew gjtExamBatchNew = gjtExamBatchNewService.queryBy(entity.getExamBatchId());
		gjtExamBatchNew.setPlanSt(entity.getPlanSt());
		gjtExamBatchNew.setPlanEnd(entity.getPlanEnd());
		gjtExamBatchNew.setBookSt(entity.getBookSt());
		gjtExamBatchNew.setBookEnd(entity.getBookEnd());
		gjtExamBatchNew.setArrangeSt(entity.getArrangeSt());
		gjtExamBatchNew.setArrangeEnd(entity.getArrangeEnd());
		//gjtExamBatchNew.setRecordSt(entity.getRecordSt());
		gjtExamBatchNew.setRecordEnd(entity.getRecordEnd());
		gjtExamBatchNew.setOnlineSt(entity.getOnlineSt());
		gjtExamBatchNew.setOnlineEnd(entity.getOnlineEnd());
		gjtExamBatchNew.setProvinceOnlineSt(entity.getProvinceOnlineSt());
		gjtExamBatchNew.setProvinceOnlineEnd(entity.getProvinceOnlineEnd());
		gjtExamBatchNew.setPaperSt(entity.getPaperSt());
		gjtExamBatchNew.setPaperEnd(entity.getPaperEnd());
		gjtExamBatchNew.setMachineSt(entity.getMachineSt());
		gjtExamBatchNew.setMachineEnd(entity.getMachineEnd());
		gjtExamBatchNew.setShapeEnd(entity.getShapeEnd());
		gjtExamBatchNew.setThesisEnd(entity.getThesisEnd());
		gjtExamBatchNew.setReportEnd(entity.getReportEnd());
		gjtExamBatchNew.setUpdatedBy(user.getId());
		gjtExamBatchNew.setUpdatedDt(new Date());
		
		gjtExamBatchNew.setBooksSt(entity.getBooksSt());
		gjtExamBatchNew.setBooksEnd(entity.getBooksEnd());
		gjtExamBatchNew.setBktkBookSt(entity.getBktkBookSt());
		gjtExamBatchNew.setBktkBookEnd(entity.getBktkBookEnd());
		gjtExamBatchNew.setXwyyBookSt(entity.getXwyyBookSt());
		gjtExamBatchNew.setXwyyBookEnd(entity.getXwyyBookEnd());
		gjtExamBatchNew.setBktkExamSt(entity.getBktkExamSt());
		gjtExamBatchNew.setBktkExamEnd(entity.getBktkExamEnd());
		gjtExamBatchNew.setXwyyExamSt(entity.getXwyyExamSt());
		gjtExamBatchNew.setXwyyExamEnd(entity.getXwyyExamEnd());
		
		if ("2".equals(gjtExamBatchNew.getPlanStatus())) { // 审核不通过，重新发布
			GjtExamBatchApproval approval = new GjtExamBatchApproval();
			approval.setExamBatchId(gjtExamBatchNew.getExamBatchId());
			approval.setAuditOperatorRole("1");
			approval.setAuditState("3");
			approval.setUserId(user.getId());
			approval.setXxId(user.getGjtOrg().getId());
			approval.setCreatedBy(user.getId());
			gjtExamBatchApprovalService.insert(approval);
			
			gjtExamBatchNew.setPlanStatus("1");
		}

		gjtExamBatchNewService.update(gjtExamBatchNew);
		
		return feedback;
	}

	@RequestMapping(value = "queryExamPoint", method = RequestMethod.GET)
	@ResponseBody
	public List<CommonSelect> queryExamPoint(String examBatchCode, HttpServletRequest request) {

		List<CommonSelect> list = new ArrayList<CommonSelect>();
		if (StringUtils.isNotBlank(examBatchCode)) {
			Map<String, String> map = commonMapService.getExamPointMapByBatchCode(examBatchCode);
			for (Entry<String, String> en : map.entrySet()) {
				list.add(new CommonSelect(en.getKey(), en.getValue()));
			}
		}
		return list;
	}
	
	@RequiresPermissions("/exam/new/batch/list$approval")
	@RequestMapping(value = "approval")
	public String approval(String examBatchId, int auditState, String auditContent,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "审批成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			
			GjtExamBatchApproval approval = new GjtExamBatchApproval();
			approval.setExamBatchId(examBatchId);
			approval.setAuditOperatorRole("2");
			approval.setAuditState("2");
			approval.setAuditContent(auditContent);
			approval.setUserId(user.getId());
			approval.setXxId(user.getGjtOrg().getId());
			approval.setCreatedBy(user.getId());
			gjtExamBatchApprovalService.insert(approval);
			
			GjtExamBatchNew gjtExamBatchNew = gjtExamBatchNewService.queryBy(examBatchId);
			if (auditState == 1) {
				gjtExamBatchNew.setPlanStatus("3"); // 通过
			} else {
				gjtExamBatchNew.setPlanStatus("2"); // 不通过
			}
			gjtExamBatchNew.setUpdatedBy(user.getId());
			gjtExamBatchNew.setUpdatedDt(new Date());
			gjtExamBatchNewService.update(gjtExamBatchNew);
		} catch (Exception e) {
			feedback = new Feedback(false, "审批失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/exam/new/batch/list";
	}
	
	/**
	 * 前后端日期格式转换
	 * @param binder
	 */
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		dateFormat.setLenient(false);  
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   //true:允许输入空值，false:不能为空值  
	}

}
