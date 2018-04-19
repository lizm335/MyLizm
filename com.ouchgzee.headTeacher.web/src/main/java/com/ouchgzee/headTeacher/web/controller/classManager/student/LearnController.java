/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.classManager.student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.studymanage.StudyManageForTeachClassService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.ouchgzee.headTeacher.pojo.BzrGjtClassInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.common.view.CommonExcelView;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * 学员学情控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月27日
 * @version 2.5
 */
@Controller
@RequestMapping({"/home/class/learn","/home/class/collegeModelClockLearn","/home/class/noExamModelClockLearn"})
public class LearnController extends BaseController {

//	@Autowired
//	private BzrGjtRecResultService gjtRecResultService;
	
	@Autowired
	private GjtRecResultService gjtRecResultService;
	

//	@Autowired
//	private BzrGjtStudentService gjtStudentService;
	
	@Autowired
	private GjtStudentInfoService gjtStudentService;
	
	/**
     * 新的学习业务对象
     */
    @Autowired
    private StudyManageForTeachClassService studyManageForTeachClassService;
    

//	@Autowired
//	private BzrGjtTermInfoService gjtTermInfoService;

//	@Autowired
//	private BzrCommonMapService commonMapService;
	
	@Autowired
	private CommonMapService commonMapService;
	

	/**
	 * 学支平台--学员学情信息列表
	 * 
	 * 兼容以前的url -- list, 具体url设置为studentStudyList
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = {"studentStudyList", "list" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String studentStudyList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request,
			HttpSession session) {
		BzrGjtEmployeeInfo user = getUser(session);
		
		String classId = super.getCurrentClassId(session);//当前教学班
		
		String gradeId = getCurrentClass(session).getGjtGrade().getGradeId();//当前班级的入学学期

		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());//获取学期列表
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());//学习中心
		Map specialtyMap = commonMapService.getTeachClassMajor(classId);// 专业名称
		Map pyccMap = commonMapService.getPyccMap(user.getXxId());// 层次

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("CLASS_ID", classId);//限制为当前教务班级
		
		if("1".equals(user.getGjtOrg().getOrgType())) {
			searchParams.put("XX_ID", user.getGjtOrg().getId());
		} else {
			searchParams.put("EQ_studyId", user.getGjtOrg().getId());
		}
		if(EmptyUtils.isNotEmpty(gradeId)){
			model.addAttribute("currentGradeId",gradeId);
			if(EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))){//如果没有选择，则默认查当前学期
				searchParams.put("GRADE_ID",gradeId);
			}else if("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))){
				searchParams.remove("GRADE_ID");
			}
		}
		
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Page pageInfo = studyManageForTeachClassService.getStudentCourseList(searchParams, pageRequst);

		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageInfoTotalElements", pageInfo.getTotalElements());
		session.setAttribute("exportStudentConditionList",searchParams);
		return "new/class/learn/student_study_list";
	}


	/**
	 * 学支平台--学员学情导出
	 * @param totalNum
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "exportStudentConditionList/{totalNum}",method = RequestMethod.GET)
	public String exportStudentConditionList(@PathVariable("totalNum") String totalNum,HttpServletRequest request,Model model){
		BzrGjtEmployeeInfo user = getUser(request.getSession());
		try {
			String phone = ObjectUtils.toString(user.getSjh());
			if(EmptyUtils.isNotEmpty(phone)){
				model.addAttribute("mobileNumber",phone.substring(phone.length()-4,phone.length()));
			}
			model.addAttribute("totalNum",totalNum);
		}catch (Exception e){
			e.printStackTrace();
		}
		return "/new/class/learn/export_student_condition_list_page";
	}

	/**
	 *  学支平台--学员学情列表下载
	 */
	@RequestMapping(value = "downLoadStudentConditionListXls",method = {RequestMethod.POST})
	public View downLoadStudentConditionListXls(HttpServletRequest request, HttpServletResponse response){
		BzrGjtEmployeeInfo user = getUser(request.getSession());
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if(flag!=null&&flag==true){
			Map searchParams = (Map) request.getSession().getAttribute("exportStudentConditionList");
			searchParams.put("classId", getCurrentClassId(request.getSession()));
			
			
			Workbook workbook = studyManageForTeachClassService.downLoadStudentCourseListExportXls(searchParams);
			request.getSession().setAttribute(user.getSjh(),"");
			
			return new CommonExcelView(workbook, "学员学情列表");
		}else {
			throw new RuntimeException("您没有权限");
		}
	}

	/**
	 * 浏览学员学情详情
	 * 
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "studentStudyDetail/{studentId}", method = RequestMethod.GET)
	public String studentStudyDetail(@PathVariable("studentId") String studentId, HttpServletRequest request, Model model) {
		GjtStudentInfo studentInfo = gjtStudentService.queryById(studentId);
		BzrGjtClassInfo classInfo = getCurrentClass(request.getSession());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("studentId", studentInfo.getStudentId());
		params.put("xxId", studentInfo.getXxId());
		params.put("classId", classInfo.getClassId());//学员所在班级
		Map<String, Object> result = gjtRecResultService.queryStuStudyCondition(params);//学情信息总览
		// 学员学情详情
//		List<Map> infos = gjtRecResultService.queryStudentRecResultLearningDetail(studentId);
		
		List<Map<String, String>> infos = gjtRecResultService.queryStudentRecResultLearningDetail(params);
		
		// 模块学分详情
		params.put("specialty_id",studentInfo.getGjtSpecialty().getSpecialtyId());
		params.put("student_id",studentInfo.getStudentId());
		List studentand = gjtRecResultService.getCreditInfoAnd(params);// 学分详情

		model.addAttribute("infos", infos);
		model.addAttribute("action", "view");
		model.addAttribute("result", result);
		model.addAttribute("studentand", studentand);
		return "new/class/learn/student_study_detail";
	}


	/**
	 * 教学班级学员课程学情
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "courseList", method = { RequestMethod.GET, RequestMethod.POST })
	public String courseList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
							 @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,HttpServletRequest request) {
		BzrGjtEmployeeInfo user = getUser(request.getSession());
		BzrGjtClassInfo classInfo = getCurrentClass(request.getSession());
		Map gradeMap = commonMapService.getGradeMap(user.getXxId());//学期
		Map courseMap = commonMapService.getTeachClassCourse(classInfo.getClassId());//课程

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("TEACH_CLASS_ID", classInfo.getClassId());//限制教学班范围
		
		searchParams.put("XX_ID",user.getXxId());
		searchParams.put("orgType",user.getGjtOrg().getOrgType());
		Map currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if(EmptyUtils.isNotEmpty(currentGradeMap)){
			String currentGradeId = (String) currentGradeMap.keySet().iterator().next();
			model.addAttribute("currentGradeId",currentGradeId);
			if(EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))){//如果没有选择，则默认查当前学期
				searchParams.put("GRADE_ID",currentGradeId);
			}else if("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))){
				searchParams.remove("GRADE_ID");
			}
		}

		PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize);
		
//		Page<Map<String, Object>> pageInfo = gjtRecResultService.getCourseListPage(searchParams, pageRequest);
		
		Page pageInfo = studyManageForTeachClassService.getCourseStudyList(searchParams, pageRequest);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("gradeMap",gradeMap);
		model.addAttribute("courseMap",courseMap);
		model.addAttribute("pageInfoTotalElements",pageInfo.getTotalElements());
		request.getSession().setAttribute("downLoadCourseConditionListXls",searchParams);
		return "new/class/learn/courseList";
	}


	/**
	 * 学情分析--课程学情列表导出页面
	 * @return
	 */
	@RequestMapping(value = "exportCourseConditionList/{totalNum}",method = {RequestMethod.GET,RequestMethod.POST})
	public String exportCourseConditionList(@PathVariable("totalNum") String totalNum,HttpServletRequest request,Model model){
		BzrGjtEmployeeInfo user = getUser(request.getSession());
		try {
			String phone = ObjectUtils.toString(user.getSjh());
			if(EmptyUtils.isNotEmpty(phone)){
				model.addAttribute("mobileNumber",phone.substring(phone.length()-4,phone.length()));
			}
			model.addAttribute("totalNum",totalNum);
		}catch (Exception e){
			e.printStackTrace();
		}
		return "/new/class/learn/export_course_condition_list_page";
	}

	/**
	 *  课程学情列表下载
	 */
	@RequestMapping(value = "downLoadCourseConditionListXls",method = {RequestMethod.POST})
	public View downLoadCourseConditionListXls(HttpServletRequest request, HttpServletResponse response){
		BzrGjtEmployeeInfo user = getUser(request.getSession());
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if(flag!=null&&flag==true){
			Map searchParams = (Map) request.getSession().getAttribute("downLoadCourseConditionListXls");
//				Workbook wb = gjtStudentService.getCourseList(searchParams);
			
			Workbook wb = studyManageForTeachClassService.downloadCourseStudyListExportXls(searchParams);
			request.getSession().setAttribute(user.getSjh(),"");
			
			return new CommonExcelView(wb, "课程学情列表");
		}else {
			throw new RuntimeException("您没有权限");
		}
	}

	/**
	 * 课程学情详情页
	 * @param courseId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "courseDetails/{courseId}", method = {RequestMethod.GET,RequestMethod.POST})
	public String courseDetails(@PathVariable("courseId") String courseId,String initGradeId,
											  HttpServletRequest request,@RequestParam(value = "page", defaultValue = "1") int pageNumber,
											  @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model){

		BzrGjtEmployeeInfo user = getUser(request.getSession());
		BzrGjtClassInfo classInfo = getCurrentClass(request.getSession());
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("TEACH_CLASS_ID", classInfo.getClassId());//限制教学班学员范围
		searchParams.put("XX_ID", user.getXxId());
		searchParams.put("courseId",courseId);

		Map gradeMap = commonMapService.getGradeMap(user.getXxId());//学期
		Map courseMap = commonMapService.getTeachClassCourse(classInfo.getClassId());//课程
		Map specialtyMap = commonMapService.getTeachClassMajor(classInfo.getClassId());//专业
		Map pyccMap = commonMapService.getPyccMap();// 层次

		
		String gradeId = ObjectUtils.toString(searchParams.get("gradeId"));
		if(StringUtils.isEmpty(gradeId)) {
			gradeId = initGradeId;
			searchParams.put("gradeId", initGradeId);
			model.addAttribute("currentGradeId", initGradeId);
		}
		
		if(StringUtils.isEmpty(gradeId)) {
			Map currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
			if(EmptyUtils.isNotEmpty(currentGradeMap)){
				String currentGradeId = (String) currentGradeMap.keySet().iterator().next();
				model.addAttribute("currentGradeId",currentGradeId);
				searchParams.put("gradeId",currentGradeId);
			}
		}
		
		if("all".equals(gradeId)){
			searchParams.remove("gradeId");
		}
		

		PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize);
//		Page<Map<String, Object>> pageInfo = gjtRecResultService.courseLearnConditionDetails(searchParams,pageRequest);
		
		Page pageInfo = studyManageForTeachClassService.getCourseStudyDetails(searchParams, pageRequest);
		
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("gradeMap",gradeMap);
		model.addAttribute("courseMap",courseMap);
		model.addAttribute("specialtyMap",specialtyMap);
		model.addAttribute("pyccMap",pyccMap);
		request.getSession().setAttribute("courseLearnConditionDetails",searchParams);
		return "new/class/learn/course_study_details";
	}

	/**
	 * 课程学情详情页导出页面
	 * @return
	 */
	@RequestMapping(value = "exportCourseLearnConditionDetails/{totalNum}", 
			method = {RequestMethod.GET,RequestMethod.POST})
	public String exportCourseLearnConditionDetails(@PathVariable("totalNum") String totalNum,HttpServletRequest request,Model model){
		BzrGjtEmployeeInfo user = getUser(request.getSession());
		try {
			String phone = ObjectUtils.toString(user.getSjh());
			if(EmptyUtils.isNotEmpty(phone)){
				model.addAttribute("mobileNumber",phone.substring(phone.length()-4,phone.length()));
			}
			model.addAttribute("totalNum",totalNum);
		}catch (Exception e){
			e.printStackTrace();
		}

		return "/new/class/learn/export_courseLearn_condition_details";
	}

	/**
	 * 课程学情详情列表下载
	 */
	@RequestMapping(value = "downLoadCourseLearnConditionDetailsXls",method = {RequestMethod.POST})
	public View downLoadCourseLearnConditionDetailsXls(HttpServletRequest request, HttpServletResponse response){
		BzrGjtEmployeeInfo user = getUser(request.getSession());
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if(flag!=null&&flag==true){
			Map searchParams = (Map) request.getSession().getAttribute("courseLearnConditionDetails");
			Workbook wb = studyManageForTeachClassService.downLoadCommonCourseDetailExportXls(searchParams);
			request.getSession().setAttribute(user.getSjh(),"");
			return new CommonExcelView(wb, "课程学情详情列表");
		}else {
			throw new RuntimeException("您没有权限");
		}
	}

	/**
	 * 学员课程学情详情   课程学情列表 -- 课程学情详情 -- 课程学情详情下的学员详情信息
	 * @param termcourseId
	 * @param studentId
	 * @param courseId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "courseCondition/{termcourseId}/{studentId}/{courseId}", 
			method = { RequestMethod.GET, RequestMethod.POST })
	public String courseConditionDetials(@PathVariable("termcourseId") String termcourseId,
			@PathVariable("studentId") String studentId, @PathVariable("courseId") String courseId,
			HttpServletRequest request, Model model) {
		GjtStudentInfo studentInfo = gjtStudentService.queryById(studentId);
		BzrGjtClassInfo classInfo = getCurrentClass(request.getSession());

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("studentId", studentInfo.getStudentId());
		param.put("xxId", studentInfo.getXxId());
		param.put("classId", classInfo.getClassId());

		Map<String, Object> userInfo = gjtRecResultService.queryStuStudyCondition(param);//学员总体信息

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("termcourseId", termcourseId);
		params.put("studentId", studentId);
		params.put("courseId", courseId);
		Map<String, Object> result = gjtRecResultService.courseConditionDetials(params);//课程学情总览

		userInfo.putAll(result);
		userInfo.put("URL","formMap.USER_INFO=" + EncryptUtils.encrypt(ObjectUtils.toString(result.get("CHOOSE_ID")) + "," + ObjectUtils.toString(result.get("STUDENT_ID"))) + "&formMap.IS_MANAGER=Y&&formMap.IS_TEST=Y");

		model.addAttribute("result", userInfo);
		return "new/class/learn/courseConditionDetials";
	}

	/**
	 * 课程学情导出
	 * 
	 * @param session
	 * @return
	 */
	/*@RequestMapping(value = "exportStuCourse", method = RequestMethod.POST)
	public void exportStuCourse(HttpSession session, HttpServletResponse response) {

		String outputUrl = "班级课程学情-" + Calendar.getInstance().getTimeInMillis() + ".xls";
		BzrGjtClassInfo classInfo = getCurrentClass(session);
		HSSFWorkbook workbook = null;
		try {
			workbook = gjtRecResultService.exportStuCourse(ObjectUtils.toString(classInfo.getClassId()));
			super.downloadExcelFile(response, workbook, outputUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/
	
	
	
	
}
