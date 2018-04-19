package com.gzedu.xlims.web.controller.edumanage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.dao.Collections3;
import com.gzedu.xlims.pojo.GjtAuditOperateLine;
import com.gzedu.xlims.pojo.GjtCustomCourse;
import com.gzedu.xlims.pojo.GjtGrantCoursePlan;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.comm.AuditStatus;
import com.gzedu.xlims.pojo.comm.OperateType;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtCustomCourseService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyService;
import com.gzedu.xlims.service.edumanage.GjtGrantCoursePlanService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 定制课程审核
 * @author lyj
 * @time 2017年7月24日 
 */
@Controller
@RequestMapping("/edumanage/custom/audit")
public class GjtCustomAuditController {
	
	@Autowired
	private GjtGradeSpecialtyService gjtGradeSpecialtyService;
	@Autowired
	private GjtOrgService gjtOrgService;
	@Autowired
	private GjtTeachPlanService gjtTeachPlanService;
	@Autowired
	private CommonMapService commonMapService;
	@Autowired
	private GjtCustomCourseService gjtCustomCourseService;
	@Autowired
	private GjtGrantCoursePlanService gjtGrantCoursePlanService;
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize) {
		String gradeId = request.getParameter("gradeId");// 开课学期
		String courseName = request.getParameter("courseName");// 学院课程
		String replaceCourseName = request.getParameter("replaceCourseName");// 替换课程
		String planStatus = request.getParameter("planStatus");// 授课计划状态
		String certificateStatus = request.getParameter("certificateStatus");// 授课凭证状态
		String scoreStatus = request.getParameter("scoreStatus");// 授课成绩状态
		String orgId = request.getParameter("orgId");// 学习中心
		
		Map<String, Object> params = Maps.newHashMap();
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		// 1. 查询机构
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<String> orgIds = Lists.newArrayList();
		if(StringUtils.isNotEmpty(orgId)) {
			//orgIds = gjtOrgService.queryByParentId(orgId,"6");
			orgIds.add(orgId);
		} else {
			orgIds = gjtOrgService.queryByParentId(user.getGjtOrg().getId());
		}
		// 2.查询教学计划
		Page<Map<String, Object>> pageInfo = null;
		if(Collections3.isNotEmpty(orgIds)) {
			// 3. 查询定制课程
			params = Maps.newHashMap();
			params.put("orgIds", orgIds);
			if(StringUtils.isNotEmpty(gradeId)) {
				params.put("gradeId", gradeId);
			}
			if(StringUtils.isNotEmpty(courseName)) {
				params.put("courseName", courseName);
			}
			if(StringUtils.isNotEmpty(replaceCourseName)) {
				params.put("replaceCourseName", replaceCourseName);
			}
			if(StringUtils.isNotEmpty(planStatus)) {
				params.put("planStatus", planStatus);
			}
			if(StringUtils.isNotEmpty(certificateStatus)) {
				params.put("certificateStatus", certificateStatus);
			}
			if(StringUtils.isNotEmpty(scoreStatus)) {
				params.put("scoreStatus", scoreStatus);
			}
			pageInfo = gjtCustomCourseService.queryAuditAll(params,pageRequst);
		}
		request.setAttribute("pageInfo", pageInfo);
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		request.setAttribute("gradeMap", gradeMap);
		Map<String,String> studyCenterMap = commonMapService.getOrgTree(user.getGjtOrg().getId(), false);// 学习中心map
		request.setAttribute("studyCenterMap", studyCenterMap);
		return "/edumanage/customAudit/audit-list";
	}
	
	@RequestMapping("/toAuditPlan/{customCourseId}")
	public String toAuditPlan(@PathVariable("customCourseId") String customCourseId,HttpServletRequest request) {
		setCustomCourse(customCourseId, request);
		return "/edumanage/customAudit/grant-course-plan";
	}
	
	@RequestMapping("/toCertificate/{customCourseId}")
	public String toCertificate(@PathVariable("customCourseId") String customCourseId,HttpServletRequest request) {
		setCustomCourse(customCourseId, request);
		return "/edumanage/customAudit/grant-course-certificate";
	}
	
	@RequestMapping("/toAuditScore/{customCourseId}")
	public String toAuditScore(@PathVariable("customCourseId") String customCourseId,HttpServletRequest request) {
		setCustomCourse(customCourseId, request);
		return "/edumanage/customAudit/grant-course-score";
	}
	/**
	 * 进入上传授课凭证
	 * @return
	 */
	@RequestMapping("/toUploadCertificate/{grantCoursePlanId}")
	public String toUploadCertificate(@PathVariable("grantCoursePlanId")String grantCoursePlanId,HttpServletRequest request) {
		GjtGrantCoursePlan plan = gjtGrantCoursePlanService.findOne(grantCoursePlanId);
		GjtCustomCourse customCourse = gjtCustomCourseService.findOne(plan.getCustomCourseId());
		request.setAttribute("plan", plan);
		request.setAttribute("customCourse", customCourse);
		return "/edumanage/customAudit/certificate-info";
	}
	private void setCustomCourse(String customCourseId,HttpServletRequest request) {
		GjtCustomCourse customCourse = gjtCustomCourseService.findOne(customCourseId);
		GjtTeachPlan teachPlan = gjtTeachPlanService.findOne(customCourse.getTeachPlanId());
		request.setAttribute("teachPlan", teachPlan);
		request.setAttribute("customCourse", customCourse);
	}
	
	@RequestMapping("/audit")
	@ResponseBody
	public Map<String,Object> audit(String customCourseId,String auditContent, boolean pass, Integer operateType, HttpServletRequest request) {
		Map<String,Object> resultMap = Maps.newHashMap();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtCustomCourse customCourse = gjtCustomCourseService.findOne(customCourseId);
		GjtAuditOperateLine line = new GjtAuditOperateLine();
		line.setAuditOperateLineId(UUIDUtils.random());
		line.setAuditContent(auditContent);
		line.setAuditStatus(pass ? AuditStatus.AUDIT_PASS : AuditStatus.AUDIT_NOT_PASS);
		
		line.setOperate(user.getPriRoleInfo().getRoleName()+"审核");
		line.setOperateType(OperateType.getItem(operateType));
		line.setOperatorRole(user.getPriRoleInfo().getRoleName());
		line.setOperator(user.getLoginAccount());
		line.setOperatorName(user.getRealName());
		line.setOperateTime(new Date());
		if(operateType == OperateType.GRANT_COURSE_PLAN_AUDIT.getType()) {
			if(Collections3.isEmpty(customCourse.getPlanAuditLines())){
				customCourse.setPlanAuditLines(new ArrayList<GjtAuditOperateLine>());
			}
			customCourse.getPlanAuditLines().add(line);
			customCourse.setPlanStatus(pass ? GjtCustomCourse.AUDIT_PASS : GjtCustomCourse.AUDIT_NOT_PASS);
		}
		if(operateType == OperateType.GRANT_COURSE_SCORE_AUDIT.getType()) {
			if(Collections3.isEmpty(customCourse.getScoreAuditLines())){
				customCourse.setScoreAuditLines(new ArrayList<GjtAuditOperateLine>());
			}
			customCourse.getScoreAuditLines().add(line);
			customCourse.setScoreStatus(pass ? GjtCustomCourse.AUDIT_PASS : GjtCustomCourse.AUDIT_NOT_PASS);
		}
		gjtCustomCourseService.save(customCourse);
//		return "redirect:/toAuditPlan/"+teachPlanId;
		resultMap.put("result", "success");
		resultMap.put("message", "提交成功");
		return resultMap;
	}
	
}
