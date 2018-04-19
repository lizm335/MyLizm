/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.classManager.student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ObjectUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;

import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.exception.ControllerException;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.TblPriLoginLog;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.studymanage.StudyManageForTeachClassService;
import com.gzedu.xlims.service.systemManage.TblPriLoginLogService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.ouchgzee.headTeacher.pojo.BzrGjtClassInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.common.view.CommonExcelView;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * 学员考勤控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年06月06日
 * @version 2.5
 */
@Controller
@RequestMapping({"/home/class/clock","/home/class/collegeModelClock","/home/class/noExamModelClock"})
public class ClockingInController extends BaseController {

//    @Autowired
//    private BzrGjtStudentService gjtStudentService;

//    @Autowired
//    private BzrGjtUserAccountService gjtUserAccountService;
	
	@Autowired
	private GjtUserAccountService gjtUserAccountService;
    
//     @Autowired
//    private BzrCommonMapService commonMapService;
    
	@Autowired
	private CommonMapService commonMapService;

//    @Autowired
//    private BzrGjtRecResultService gjtRecResultService;

	@Autowired
	private GjtRecResultService gjtRecResultService;
	
    /**
     * 新的学习业务对象
     */
    @Autowired
    private StudyManageForTeachClassService studyManageForTeachClassService;
    
    
    /**
     * 学生信息服务
     */
    @Autowired
    private GjtStudentInfoService gjtStudentInfoService;
    
    /**
     * 班级学生服务
     */
    @Autowired
    private GjtClassStudentService gjtClassStudentService;
    
    /**
     * 日志服务
     */
    @Autowired
    private TblPriLoginLogService tblPriLoginLogService;
    
    /**
     * 考勤分析--》课程考勤-列表 
     * 新方法 兼容以前的操作list-- 创建于 2018/04/10
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = {"courseAttendanceList", "list"}, method = RequestMethod.GET)
    public String courseAttendanceList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                       @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
                       HttpSession session) {
    	
    	
    	//构建pageable对象？
        PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
        
        BzrGjtEmployeeInfo employeeInfo = currentUser();//当前用户
        BzrGjtClassInfo classInfo = currentClass();//当前班级
        String classId = null;
        if(classInfo != null) {
        	classId = classInfo.getClassId();
        }
        
        Map<String, Object> searchParams = Servlets.getParametersStartingWith();
        searchParams.put("TEACH_CLASS_ID", classId);//强制规定教学班级
        searchParams.put("XX_ID", employeeInfo.getGjtOrg().getId());//机构id? 学院ID？
        
        String gradeId = ObjectUtils.toString(searchParams.get("GRADE_ID"));
        
        //是否默认当前学期
        if(StringUtils.isEmpty(gradeId)) {
       	 	 Map currentGradeMap = commonMapService.getCurrentGradeMap(employeeInfo.getGjtOrg().getId());
	  		 if(EmptyUtils.isNotEmpty(currentGradeMap)) {
	  			 String currentGradeId = (String) currentGradeMap.keySet().iterator().next();
	  		     searchParams.put("GRADE_ID", currentGradeId);//默认当前学期
	  		 }
        }
        
		if("all".equals(gradeId)) {
			searchParams.remove("GRADE_ID");
		}
       
		//设置当前学期
		model.addAttribute("currentGradeId", searchParams.get("GRADE_ID"));
        

        Map gradeMap = commonMapService.getGradeMap(employeeInfo.getXxId());//学期
        Map courseMap = commonMapService.getTeachClassCourse(classId);//课程
        
        //课程考勤列表
        Page pageInfo = studyManageForTeachClassService.getCourseLoginList(searchParams, pageRequst);
        
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pageInfoNum", pageInfo.getTotalElements());
        model.addAttribute("courseMap",courseMap);
        model.addAttribute("gradeMap",gradeMap);
        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, ""));
        session.setAttribute("courseAttendanceList",searchParams);//用于导出的条件
        return "new/class/clock/course_attendance_list";
    }

    
    /**
	 * 课程考勤列表查询统计项；
	 * 包含所有状态
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "courseAttendanceCounts")
	@ResponseBody
	public Map<String,Object> courseAttendanceCounts(HttpServletRequest request) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		BzrGjtEmployeeInfo employeeInfo =  currentUser();
		BzrGjtClassInfo classInfo = currentClass();
		Map<String,Object> searchParams = Servlets.getParametersStartingWith();
		String gradeId = ObjectUtils.toString(searchParams.get("GRADE_ID"));
		if ("all".equals(gradeId)) {
			searchParams.remove("GRADE_ID");
		}
		
		String classId = classInfo.getClassId();
		
		searchParams.put("XX_ID", employeeInfo.getGjtOrg().getId());
		searchParams.put("TEACH_CLASS_ID", classId);//强制规定教学班级
		
		// 查询条件统计项
		searchParams.remove("TIME_FLG");
		long count = studyManageForTeachClassService.getCourseLoginCount(searchParams);
		searchParams.put("TIME_FLG", "1");
		long count1 = studyManageForTeachClassService.getCourseLoginCount(searchParams);
		searchParams.put("TIME_FLG", "2");
		long count2 = studyManageForTeachClassService.getCourseLoginCount(searchParams);
		searchParams.put("TIME_FLG", "3");
		long count3 = studyManageForTeachClassService.getCourseLoginCount(searchParams);
		
		resultMap.put("LOGIN_STATE_COUNT", count);
		resultMap.put("LOGIN_STATE_COUNT1", count1);
		resultMap.put("LOGIN_STATE_COUNT2", count2);
		resultMap.put("LOGIN_STATE_COUNT3", count3);
		return resultMap;
	}
	
	
    /**
     * 课程考勤详情页
     * @return
     */
    @RequestMapping(value = "courseAttendanceDetails/{courseId}", method = {RequestMethod.GET,RequestMethod.POST})
    public String courseAttendanceDetails(@PathVariable String courseId, String initGradId,
    									@RequestParam(value = "page", defaultValue = "1") int pageNumber,
    									@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
                                          Model model, ServletRequest request, HttpSession session){
    	
        BzrGjtEmployeeInfo employeeInfo = getUser(session);
        String classId = super.getCurrentClassId(session);
        
        Map<String, String> specialtyMap = commonMapService.getTeachClassMajor(classId);//专业
        
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
        
        Map<String,String> pyccMap = commonMapService.getPyccMap();// 层次
        Map<String,String> courseClassMap = commonMapService.getCourseClassInfoMap(employeeInfo.getGjtOrg().getId(),courseId);// 课程班
        Map<String,String> gradeMap = commonMapService.getGradeMap(employeeInfo.getXxId());
        
        
        searchParams.put("XX_ID", employeeInfo.getGjtOrg().getId());
        searchParams.put("TEACH_CLASS_ID", classId);// 规定教学班级
        searchParams.put("COURSE_ID", courseId);
        

        if(!searchParams.containsKey("gradeId")) {
        	searchParams.put("gradeId", initGradId);//初始化默认值
        }
        
        //设置当前课程id
        model.addAttribute("courseId", courseId);
        //设置当前学期
      	model.addAttribute("currentGradeId", searchParams.get("gradeId"));
      	
        //考勤详情列表
        PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize);
        Page pageInfo  = studyManageForTeachClassService.getTeachClassCourseClockingDetail(searchParams, pageRequest);
        
        
        model.addAttribute("pageInfo", pageInfo);//显示指定名称
        model.addAttribute("specialtyMap",specialtyMap);
        model.addAttribute("gradeMap",gradeMap);
        model.addAttribute("pyccMap",pyccMap);
        model.addAttribute("courseClassMap",courseClassMap);
        model.addAttribute("searchParams", searchParams);
        
        session.setAttribute("courseAttendanceDetails", searchParams);//用于导出取此条件的参数
        return "new/class/clock/course_attendance_details";
    }

    
    /**
     * 课程详情的类型数量统计
     * @return
     */
    @RequestMapping("courseAttendanceDetailCounts")
    @ResponseBody
    public Map<String,Object> courseAttendanceDetailCounts(@RequestParam String courseId, HttpSession session){
    	 Map<String,Object> resultMap = new HashMap<String,Object>();//响应结果
    	 Map<String, Object> searchParams = Servlets.getParametersStartingWith();
    	 
         BzrGjtEmployeeInfo employeeInfo = getUser(session);
         String classId = super.getCurrentClassId(session);
         
    	 searchParams.put("XX_ID", employeeInfo.getGjtOrg().getId());
         searchParams.put("TEACH_CLASS_ID", classId);// 规定教学班级
         searchParams.put("COURSE_ID", courseId);// 必须参数，课程id
    	 
    	 searchParams.remove("MAIN_DEVICE");
    	 long allCount = studyManageForTeachClassService.getTeachClassCourseClockingDetailCount(searchParams);
    	 
    	 searchParams.put("MAIN_DEVICE","APP");
    	 long appCount = studyManageForTeachClassService.getTeachClassCourseClockingDetailCount(searchParams);
    	 
    	 searchParams.put("MAIN_DEVICE","PC");
    	 long pcCount = studyManageForTeachClassService.getTeachClassCourseClockingDetailCount(searchParams);
    	 
    	 
    	 resultMap.put("MAIN_DEVIECE_", allCount);
    	 resultMap.put("MAIN_DEVIECE_APP", appCount);
    	 resultMap.put("MAIN_DEVIECE_PC", pcCount);
         
         
         return resultMap;

    }
    
    
    /**
     * 课程考勤明细导出页面
     * @return
     */
    @RequestMapping(value = "exportCourseAttendanceDetails/{totalNum}",method = {RequestMethod.GET,RequestMethod.POST})
    public String exportCourseAttendanceDetails(@PathVariable("totalNum") String totalNum,HttpServletRequest request,Model model){
        BzrGjtEmployeeInfo user = getUser(request.getSession());
        try {
            String phone = ObjectUtils.toString(user.getSjh());
            if(EmptyUtils.isNotEmpty(phone)){
                model.addAttribute("mobileNumber",phone.substring(phone.length()-4,phone.length()));
            }
            model.addAttribute("totalNum",totalNum);
        }catch (Exception e){
            logger.error("导出失败", e);
        }

        return "/new/class/clock/export_course_attendance_details_page";
    }


    /**
     * 课程考勤详情列表下载
     */
    @RequestMapping(value = "downLoadCourseAttendanceDetailsXls",method = {RequestMethod.POST})
    public View downLoadCourseAttendanceDetailsXls(HttpSession session, HttpServletResponse response){
        BzrGjtEmployeeInfo user = currentUser();
        Boolean flag = (Boolean) session.getAttribute("hasPermission");
        if(flag!=null&&flag==true){
            Map searchParams = (Map) session.getAttribute("courseAttendanceDetails");
            Workbook wb = studyManageForTeachClassService.downloadCourseClockingDetailExportXls(searchParams);
            CommonExcelView excelView = new CommonExcelView(wb, "课程考勤详情列表");
            session.setAttribute(user.getSjh(),"");
            return excelView;
        }else {
            throw new ControllerException("您没有权限");
        }
    }

    /**
     * 学员考勤列表
     * @return
     */
    @RequestMapping(value = "studentAttendanceList",method = RequestMethod.GET)
    public String studentAttendanceList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                                        @RequestParam(value = "page.size", defaultValue = "10") int pageSize,
                                        Model model, ServletRequest request, HttpSession session){
        PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
        Map<String,Object> searchParams = Servlets.getParametersStartingWith(request, "");
        BzrGjtEmployeeInfo user = getUser(session);
        
        Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心
        Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业名称
        Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
        Map pyccMap = commonMapService.getPyccMap();// 层次
        
        String classId = getCurrentClassId(session);
        searchParams.put("CLASS_ID", classId);//当前选中班级
        if("1".equals(user.getGjtOrg().getOrgType())) {
            searchParams.put("XX_ID", user.getGjtOrg().getId());
        } else {
            searchParams.put("EQ_studyId", user.getGjtOrg().getId());
        }
        
        
        String gradeId = getCurrentClass(session).getGjtGrade().getGradeId();//当前班级的入学学期
        if(EmptyUtils.isNotEmpty(gradeId)){
            model.addAttribute("currentGradeId",gradeId);
            if(EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))){//如果没有选择，则默认查当前学期
                searchParams.put("GRADE_ID",gradeId);
            }else if("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))){
                searchParams.remove("GRADE_ID");
            }
        }
        
        Page pageInfo = studyManageForTeachClassService.getStudentLoginList(searchParams, pageRequst);

        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("specialtyMap", specialtyMap);
        model.addAttribute("gradeMap", gradeMap);
        model.addAttribute("pyccMap", pyccMap);
        model.addAttribute("studyCenterMap", studyCenterMap);
        model.addAttribute("pageInfoTotal",pageInfo.getTotalElements());
        
        
        StringBuffer prefixName = new StringBuffer();//导出文件名字根据搜索条件命名前缀
        if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))){
            prefixName.append(ObjectUtils.toString(studyCenterMap.get(ObjectUtils.toString(searchParams.get("XXZX_ID"))))+"-");
        }
        if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))){
            prefixName.append(ObjectUtils.toString(gradeMap.get(ObjectUtils.toString(searchParams.get("GRADE_ID"))))+"-");
        }
        if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))){
            prefixName.append(ObjectUtils.toString(specialtyMap.get(ObjectUtils.toString(searchParams.get("SPECIALTY_ID"))))+"-");
        }

        searchParams.put("prefixName", ObjectUtils.toString(prefixName.toString(),""));

        session.setAttribute("queryStudentClockingInByClassId",searchParams);//用于导出的条件
        return "new/class/clock/student_attendance_list";
    }


    /**
     * 学员考勤列表查询统计项
     * @param request
     * @return
     */
    @RequestMapping(value = "studentAttendanceCounts")
    @ResponseBody
    public Map<String,Object> studentAttendanceCounts(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        BzrGjtEmployeeInfo user = getUser(request.getSession());
        Map<String,Object> searchParams = Servlets.getParametersStartingWith(request, "");
        if("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
            searchParams.remove("GRADE_ID");
        }
        
        if("1".equals(user.getGjtOrg().getOrgType())) {
            searchParams.put("XX_ID", user.getGjtOrg().getId());
        } else {
            searchParams.put("EQ_studyId", user.getGjtOrg().getId());
        }
        
        //''=全部， 0=在线 ，1=离线（7天以上未学习），2=离线（3天以上未学习），3=离线（3天内未学习），4=从未登录
        searchParams.put("STUDY_STATUS", "");
        int studyStatusCountAll = studyManageForTeachClassService.getStudentLoginCount(searchParams);
        
        searchParams.put("STUDY_STATUS", "0");
        int studyStatusCount0 = studyManageForTeachClassService.getStudentLoginCount(searchParams);
        
        searchParams.put("STUDY_STATUS", "1");
        int studyStatusCount1 = studyManageForTeachClassService.getStudentLoginCount(searchParams);
        
        searchParams.put("STUDY_STATUS", "2");
        int studyStatusCount2 = studyManageForTeachClassService.getStudentLoginCount(searchParams);
        
        searchParams.put("STUDY_STATUS", "3");
        int studyStatusCount3 = studyManageForTeachClassService.getStudentLoginCount(searchParams);
        

        searchParams.put("STUDY_STATUS", "4");
        int studyStatusCount4 = studyManageForTeachClassService.getStudentLoginCount(searchParams);
        
        
        resultMap.put("STUDY_STATUS_COUNT",  studyStatusCountAll);
        resultMap.put("STUDY_STATUS_COUNT0", studyStatusCount0);
        resultMap.put("STUDY_STATUS_COUNT1", studyStatusCount1);
        resultMap.put("STUDY_STATUS_COUNT2", studyStatusCount2);
        resultMap.put("STUDY_STATUS_COUNT3", studyStatusCount3);
        resultMap.put("STUDY_STATUS_COUNT4", studyStatusCount4);
        
        return resultMap;
    }

    /**
     * 考勤详情
     * @param studentId
     * @return
     */
    @RequestMapping(value = "studentLoginDetail/{studentId}", method = RequestMethod.GET)
    public String studentLoginDetail(@PathVariable("studentId") String studentId,
    					   @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "page.size", defaultValue = "10") int pageSize,
                           Model model, ServletRequest request, HttpSession session,String type) {
        BzrGjtEmployeeInfo user = getUser(session);
        Map searchParams = Servlets.getParametersStartingWith(request, "");

        if("1".equals(user.getGjtOrg().getOrgType())) {
            searchParams.put("XX_ID", user.getGjtOrg().getId());
        } else {
            searchParams.put("EQ_studyId", user.getGjtOrg().getId());
        }
        searchParams.put("STUDENT_ID",studentId);
        Map resultMap = studyManageForTeachClassService.getStudentLoginDetail(searchParams);
        model.addAttribute("resultMap", resultMap);
        session.setAttribute("getStudentLoginDetail",searchParams);
        return "/new/class/clock/studentlogin_detail";

    }

    /**
     * 考勤分析--》学员课程考勤明细
     * @param teachPlanId
     * @param studentId
     * @param courseId
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "courseCondition/{teachPlanId}/{studentId}/{courseId}", method = { RequestMethod.GET,RequestMethod.POST })
    public String courseConditionDetials(@PathVariable("teachPlanId") String teachPlanId,
                                         @PathVariable("studentId") String studentId,@PathVariable("courseId") String courseId,
                                         HttpServletRequest request, Model model) {
        Map<String, Object> param = new HashMap<String, Object>();
        GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);
        GjtClassInfo classInfo = gjtClassStudentService.queryTeachClassInfoByStudetnId(studentId);

        param.put("classId", classInfo.getClassId());
        param.put("studentId", studentInfo.getStudentId());
        param.put("xxId", studentInfo.getXxId());

        Map<String, Object> userInfo = gjtRecResultService.queryStuStudyCondition(param);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("teachPlanId", teachPlanId);
        params.put("studentId", studentId);
        params.put("courseId", courseId);
        Map<String, Object> result = gjtRecResultService.courseConditionDetials(params);

        userInfo.putAll(result);
        userInfo.put("URL",
                "formMap.USER_INFO=" + EncryptUtils.encrypt(ObjectUtils.toString(result.get("CHOOSE_ID")) + "," + ObjectUtils.toString(result.get("STUDENT_ID"))) + "&formMap.IS_MANAGER=Y&&formMap.IS_TEST=Y");

        model.addAttribute("result", userInfo);
        return "/new/class/learn/courseConditionDetials";
    }

    /**
     * 学员课程考勤列表导出页面
     * @return
     */
    @RequestMapping(value = "exportAttendanceList/{totalNum}",method = {RequestMethod.GET,RequestMethod.POST})
    public String exportAttendanceList(@PathVariable("totalNum") String totalNum,HttpServletRequest request,Model model){
        BzrGjtEmployeeInfo user = getUser(request.getSession());
        try {
            String phone = ObjectUtils.toString(user.getSjh());
            if(EmptyUtils.isNotEmpty(phone)){
                model.addAttribute("mobileNumber",phone.substring(phone.length()-4,phone.length()));
            }
            model.addAttribute("totalNum",totalNum);
        }catch (Exception e){
        	logger.error("导出失败", e);
        }

        return "/new/class/clock/attendance_export_page";
    }

    /**
     * 学员课程考勤列表下载
     * ----学员的课程
     * 
     */
    @RequestMapping(value = "downLoadAttendanceListXls",method = {RequestMethod.POST})
    public View downLoadAttendanceListXls(HttpServletRequest request, HttpServletResponse response){
        BzrGjtEmployeeInfo user = getUser(request.getSession());
        Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
        if(flag!=null&&flag==true){
            Map searchParams = (Map) request.getSession().getAttribute("queryStudentClockingInByClassId");
            Workbook wb = studyManageForTeachClassService.downLoadStudentLoginListExportXls(searchParams);
            String fileName = ObjectUtils.toString(searchParams.get("prefixName"),"")+"学员考勤统计表";
            request.getSession().setAttribute(user.getSjh(),"");
            return new CommonExcelView(wb, fileName);
        }else {
        	throw new ControllerException("您没有权限");
        }
    }

    /**
     * 课程考勤列表导出页面
     * @param totalNum
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "exportCourseAttendancePage/{totalNum}",method = {RequestMethod.GET,RequestMethod.POST})
    public String exportCourseAttendancePage(@PathVariable("totalNum") String totalNum,HttpServletRequest request,Model model){
        BzrGjtEmployeeInfo user = getUser(request.getSession());
        try {
            String phone = ObjectUtils.toString(user.getSjh());
            if(EmptyUtils.isNotEmpty(phone)){
                model.addAttribute("mobileNumber",phone.substring(phone.length()-4,phone.length()));
            }
            model.addAttribute("totalNum",totalNum);
        }catch (Exception e){
        	logger.error("导出失败", e);
        }
        return "/new/class/clock/export_course_attendance_page";
    }

    /**
     * 课程考勤列表下载
     */
    @RequestMapping(value = "downLoadCourseAttendanceListXls",method = {RequestMethod.POST})
    public View downLoadCourseAttendanceListXls(HttpSession session , HttpServletResponse response){
        BzrGjtEmployeeInfo user = currentUser();
        Boolean flag = (Boolean) session.getAttribute("hasPermission");
        if(flag!=null&&flag==true){
            Map searchParams = (Map)session.getAttribute("courseAttendanceList");
            Workbook wb = studyManageForTeachClassService.downLoadCourseLoginListExportXls(searchParams);
            session.setAttribute(user.getSjh(),"");
            return new CommonExcelView(wb, "课程考勤列表");
        }else {
            throw new ControllerException("您没有权限");
        }
    }


    /**
     * 学员课程考勤列表导出页面
     * @return
     */
    @RequestMapping(value = "exportAttendanceDetail/{totalNum}",method = {RequestMethod.GET,RequestMethod.POST})
    public String exportAttendanceDetail(@PathVariable("totalNum") String totalNum,HttpServletRequest request,Model model){
        BzrGjtEmployeeInfo user = getUser(request.getSession());
        try {
            String phone = ObjectUtils.toString(user.getSjh());
            if(EmptyUtils.isNotEmpty(phone)){
                model.addAttribute("mobileNumber",phone.substring(phone.length()-4,phone.length()));
            }
            model.addAttribute("totalNum",totalNum);
        }catch (Exception e){
        	logger.error("导出失败", e);
        }

        return "/new/class/clock/attendance_export_detail_page";
    }


    /**
     * 获取验证码
     * @param request
     * @param feedback
     * @return
     */
    @RequestMapping(value = "getMessageCode",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Feedback getMessageCode(HttpServletRequest request,Feedback feedback){
        BzrGjtEmployeeInfo user = getUser(request.getSession());
        String phone = ObjectUtils.toString(user.getSjh());
        Random rd = new Random();
        String code = ObjectUtils.toString(rd.nextInt(10))+
                ObjectUtils.toString(rd.nextInt(10))+
                ObjectUtils.toString(rd.nextInt(10))+
                ObjectUtils.toString(rd.nextInt(10))+
                ObjectUtils.toString(rd.nextInt(10))+
                ObjectUtils.toString(rd.nextInt(10));
        int smsResult = SMSUtil.sendTemplateMessageCode(phone, code, "gk");
        if(smsResult == 1){
            request.getSession().setAttribute(user.getSjh(),code);
            feedback.setSuccessful(true);
            feedback.setMessage("获取验证码成功！");
        }else {
            feedback.setSuccessful(false);
            feedback.setMessage("获取验证码失败！");
        }

        return feedback;
    }

    /**
     * 校验验证码
     * @param request
     * @param userCode
     * @param feedback
     * @return
     */
    @RequestMapping(value = "getCheckCode",method = {RequestMethod.POST})
    @ResponseBody
    public Feedback getCheckCode(HttpServletRequest request, String userCode, Feedback feedback){
        BzrGjtEmployeeInfo user = getUser(request.getSession());
        String code = ObjectUtils.toString(request.getSession().getAttribute(user.getSjh()),"");
        if(code.equals(userCode)){
            request.getSession().setAttribute("hasPermission",true);
            feedback.setSuccessful(true);
        }else {
            request.getSession().setAttribute("hasPermission",false);
            feedback.setSuccessful(false);
        }
        return feedback;
    }



    /**
     * 学员学习记录明细表
     */
    @RequestMapping(value = "downLoadAttendanceDetailXls",method = {RequestMethod.POST})
    public View downLoadAttendanceDetailXls(HttpServletRequest request, HttpServletResponse response){
        BzrGjtEmployeeInfo user = getUser(request.getSession());
        Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
        if(flag!=null&&flag==true){
            Map searchParams = (Map) request.getSession().getAttribute("getStudentLoginDetail");
            Workbook wb = studyManageForTeachClassService.downLoadStudentDetailExportXls(searchParams);
            request.getSession().setAttribute(user.getSjh(),"");
            return new CommonExcelView(wb, "学员学习记录明细表");
        }else {
            throw new ControllerException("您没有权限");
        }

    }

    /**
     * 考勤详情
     * @param studentId
     * @return
     */
    @RequestMapping(value = "studyView/{studentId}", method = RequestMethod.GET)
    public String studyViewForm(@PathVariable("studentId") String studentId, @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "page.size", defaultValue = "10") int pageSize,
                           Model model, ServletRequest request, HttpSession session,String type) {

        BzrGjtClassInfo classInfo = getCurrentClass(session);
        PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        if("1".equals(type)){
            searchParams.put("EQ_loginType","1");
        }else if("2".equals(type)){
            searchParams.put("NE_loginType","1");
        }

        GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);

        searchParams.put("EQ_username", studentInfo.getGjtUserAccount().getLoginAccount());
        Page<TblPriLoginLog> infos = tblPriLoginLogService.queryLoginLogByPage(searchParams, pageRequst);

        searchParams.put("EQ_loginType","1");
        Page<TblPriLoginLog> infosPc = tblPriLoginLogService.queryLoginLogByPage(searchParams, pageRequst);
        searchParams.put("NE_loginType","1");
        Page<TblPriLoginLog> infosApp = tblPriLoginLogService.queryLoginLogByPage(searchParams, pageRequst);

        
        model.addAttribute("type",type);
        model.addAttribute("pcCount",ObjectUtils.toString(infosPc.getTotalElements(),"0"));
        model.addAttribute("appCount", Integer.parseInt(ObjectUtils.toString(infosApp.getTotalElements(),"0")));
        model.addAttribute("info", studentInfo);
        model.addAttribute("infos", infos);
        model.addAttribute("studentId",studentId);
        
        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
        model.addAttribute("action", "view");
        return "new/class/clock/studyView";
    }


    /**
     * 导出学员登录明细
     * @param studentId
     * @return
     */
    @RequestMapping(value = "/exportInfoDetails",method = RequestMethod.POST)
    public View exportInfoDetails(@RequestParam("studentId") String studentId,HttpServletResponse response){
        GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);
        Map<String, Object> searchParams = new HashMap<String,Object>();
        searchParams.put("EQ_username", studentInfo.getGjtUserAccount().getLoginAccount());

        List<TblPriLoginLog> tblPriLoginLogs = tblPriLoginLogService.queryAllLoginLog(searchParams);
        Workbook workbook = studyManageForTeachClassService.exportInfoDetails(tblPriLoginLogs);
        
        return new CommonExcelView(workbook, "学员考勤详情");
    }

}
