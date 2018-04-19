/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtCourseOwnership;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtGradeSpecialty;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.TblSysData;
import com.gzedu.xlims.pojo.status.CourseCategory;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.TblSysDataService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyPlanService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyService;
import com.gzedu.xlims.service.edumanage.GjtSpecialtyPlanService;
import com.gzedu.xlims.service.edumanage.GjtStudyYearService;
import com.gzedu.xlims.service.edumanage.GjtTeachCourseService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.exam.GjtExamSubjectNewService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.pcourse.PCourseServer;
import com.gzedu.xlims.service.share.GjtShareService;
import com.gzedu.xlims.service.textbook.GjtTextbookService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.common.UploadTmpFile;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/edumanage/teachCourse")
public class GjtTeachCourseController {
	private static final Logger log = LoggerFactory.getLogger(GjtTeachCourseController.class);
	
	@Autowired
	CommonMapService commonMapService;
	
	@Autowired
	GjtTeachCourseService gjtTeachCourseService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		
		Page pageInfo = gjtTeachCourseService.getTeachCourseList(searchParams, pageRequst);
		
		Map studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());
		Map courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("courseMap", courseMap);
		
		return "edumanage/teachCourse/list";
	}

	/**
	 * 任教管理列表（统计）
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getTeachCourseCount")
	@ResponseBody
	public Map getTeachCourseCount(HttpServletRequest request) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		
		Map resultMap = new HashMap();
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		
		searchParams.put("STATE", "1");
		int stateYCount = gjtTeachCourseService.getTeachCourseCount(searchParams);
		
		searchParams.put("STATE", "2");
		int stateNCount = gjtTeachCourseService.getTeachCourseCount(searchParams);
		
		int chooseCountAll = stateYCount+stateNCount;
		resultMap.put("stateYCount", stateYCount);
		resultMap.put("stateNCount", stateNCount);
		resultMap.put("stateCountAll", chooseCountAll);
		
		return resultMap;
	}
	
	/**
	 * 任教管理跳转添加老师
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	@SysLog("任教管理跳转添加老师")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "toAddTeachCourse")
	public String toAddTeachCourse(HttpServletRequest request, Model model) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		
		Map studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());
		Map courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());
		
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("courseMap", courseMap);
		
		return "edumanage/teachCourse/alert_addTeach";
	}
	
	/**
	 * 任教管理教师列表
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	@SysLog("任教管理教师列表")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "teacherList")
	public String teacherList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize,
			HttpServletRequest request, Model model) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Page pageInfo = gjtTeachCourseService.getTeacherList(searchParams, pageRequst);
		
		model.addAttribute("pageInfo", pageInfo);
		return "edumanage/teachCourse/teacherList";
	}
	
	/**
	 * 新增任教
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@SysLog("新增任教")
	@RequestMapping(value = "addCourseTeacher")
	@ResponseBody
	public int addCourseTeacher(HttpServletRequest request) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		searchParams.put("CREATED_BY", user.getId());
		int num = gjtTeachCourseService.addCourseTeacher(searchParams);
		return num;
	}
	
	/**
	 * 删除任教
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@SysLog("删除任教")
	@RequestMapping(value = "delTeacherInfo")
	@ResponseBody
	public int delTeacherInfo(HttpServletRequest request) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("UPDATED_BY", user.getId());
		int num = gjtTeachCourseService.delTeacherInfo(searchParams);
		return num;
	}
	
	/**
	 * 更新任教信息
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@SysLog("更新任教信息")
	@RequestMapping(value = "uptTeacherInfo")
	@ResponseBody
	public int uptTeacherInfo(HttpServletRequest request) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("UPDATED_BY", user.getId());
		int num = gjtTeachCourseService.uptTeacherInfo(searchParams);
		return num;
	}
	
	/**
	 * 任教管理编辑详情
	 * @param 
	 * @return
	 * @throws IOException
	 */
	@SysLog("任教管理编辑详情")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "toViewInfo")
	public String toViewInfo(HttpServletRequest request, Model model) throws IOException {
		Map resultMap = new HashMap();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		
		List result = gjtTeachCourseService.getTeachCourseList(searchParams);
		if(EmptyUtils.isNotEmpty(result) && result.size() > 0){
			resultMap = (Map) result.get(0);
		}
		
		Map studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());
		Map courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());
		
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("courseMap", courseMap);
		model.addAttribute("resultMap", resultMap);
		
		return "edumanage/teachCourse/alert_addTeach";
	}
	
	/**
	 * 同步任教信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getSyncList")
	public String getSyncList(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map resultMap = new HashMap();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", ObjectUtils.toString(user.getGjtOrg().getId()));
		searchParams.put("IS_SYNCHRO", "N");
		try {
			int count = gjtTeachCourseService.getTeachCourseCount(searchParams);
			resultMap.put("ALL_COUNT", count);
			model.addAttribute("resultMap", resultMap);
		} catch (Exception e){
			e.printStackTrace();
		}
		return "edumanage/teachCourse/getSyncList";
	}
	
	/**
	 * 同步任教信息结果
	 * 
	 * @param request
	 * @param file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getSyncResult")
	@SysLog("同步任教信息到国开总部")
	public String getSyncRecResult(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<String> filePaths = UploadTmpFile.uploadSimple(request);
		Map formMap = Servlets.getParametersStartingWith(request, "");
		formMap.put("XX_ID", user.getGjtOrg().getId());
		formMap.put("USER_ID", user.getId());
		formMap.put("OrgCode", user.getGjtOrg().getVirtualXxzxCode());
		formMap.put("operatingUserName", user.getLoginAccount());
		
		Map resultMap = gjtTeachCourseService.getSyncResult(formMap, request.getSession().getServletContext().getRealPath(""));
		model.addAttribute("resultMap", resultMap);
		return "edumanage/teachCourse/getSyncResult";
	}

	
}
