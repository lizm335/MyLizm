
package com.ouchgzee.headTeacher.web.controller.classManager.student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
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

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.studymanage.StudyManageForTeachClassService;
import com.ouchgzee.headTeacher.pojo.BzrGjtClassInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.service.student.BzrScorePointService;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.common.view.CommonExcelView;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * Created by llx on 2017/02/27. 学习管理-成绩与学分
 */
@Controller
@RequestMapping({"/home/student/scorespoint","/home/student/collegeModelClockPoint","/home/student/noExamModelClockPoint"})
public class ScoresAndCreditsController extends BaseController {

	@Autowired
	BzrScorePointService scorePointService;

//	@Autowired
//	BzrCommonMapService commonMapService;
	
	@Autowired
	CommonMapService commonMapService;
	
	@Autowired
	StudyManageForTeachClassService studyManageForTeachClassService;
	
//	@Autowired
//	BzrGjtRecResultService gjtRecResultService;

	/**
	 * 学习管理--成绩与学分--成绩查询列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"scoreList", "/list"}, method = { RequestMethod.GET, RequestMethod.POST })
	public String scoreList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {

		BzrGjtClassInfo classInfo = getCurrentClass(request.getSession());
		BzrGjtEmployeeInfo user = getUser(request.getSession());

		Map specialtyMap = commonMapService.getTeachClassMajor(classInfo.getClassId());// 专业名称
		Map pyccMap = commonMapService.getPyccMap();// 层次
		Map examTypeMap = commonMapService.getExamTypeMap();// 考试方式
		Map<String, String> courseMap = commonMapService.getTeachClassCourse(classInfo.getClassId());//课程列表
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 入学学期
		Map yearMap = commonMapService.getYearMap(user.getGjtOrg().getId());

		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("TEACH_CLASS_ID", classInfo.getClassId());//教务班级限制
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		if("all".equals(ObjectUtils.toString(searchParams.get("NJ")))){
			searchParams.remove("NJ");
		}
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Page pageInfo = studyManageForTeachClassService.getScoreList(searchParams, pageRequst);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("courseMap", courseMap);
		model.addAttribute("yearMap", yearMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("examTypeMap", examTypeMap);
		request.getSession().setAttribute("downLoadScoreListExportXls",searchParams);
		return "/new/class/scorespoint/score_list";
	}

	/**
	 * 学习管理=》成绩查询异步查询统计项
	 *
	 * @return
	 */
	@RequestMapping(value = "/scoreListCounts")
	@ResponseBody
	public Map<String,Object> scoreListCounts(HttpServletRequest request) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		BzrGjtEmployeeInfo user = getUser(request.getSession());
		Map<String,Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		searchParams.put("TEACH_CLASS_ID", getCurrentClassId(request.getSession()));
		if("all".equals(ObjectUtils.toString(searchParams.get("NJ")))){
			searchParams.remove("NJ");
		}
		
		//参考状态  GjtRecResult examState
		searchParams.put("EXAM_STATE", "");
		// 查询条件统计项
		long score_state_count = studyManageForTeachClassService.getScoreCount(searchParams);
		
		searchParams.put("EXAM_STATE", "0");
		// 查询条件统计项
		long score_state_count0 = studyManageForTeachClassService.getScoreCount(searchParams);
		
		searchParams.put("EXAM_STATE", "1");
		// 查询条件统计项
		long score_state_count1 = studyManageForTeachClassService.getScoreCount(searchParams);
		
		searchParams.put("EXAM_STATE", "2");
		// 查询条件统计项
		long score_state_count2 = studyManageForTeachClassService.getScoreCount(searchParams);
		
		searchParams.put("EXAM_STATE", "3");
		// 查询条件统计项
		long score_state_count3 = studyManageForTeachClassService.getScoreCount(searchParams);
		
		searchParams.put("EXAM_STATE", "4");
		// 查询条件统计项
		long score_state_count4 = studyManageForTeachClassService.getScoreCount(searchParams);
		
		resultMap.put("EXAM_STATE_COUNT", score_state_count);
		resultMap.put("EXAM_STATE_COUNT0", score_state_count0);
		resultMap.put("EXAM_STATE_COUNT1", score_state_count1);
		resultMap.put("EXAM_STATE_COUNT2", score_state_count2);
		resultMap.put("EXAM_STATE_COUNT3", score_state_count3);
		resultMap.put("EXAM_STATE_COUNT4", score_state_count4);
		return resultMap;
	}

	/**
	 * 学习管理--成绩与学分--学分查询列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/creditList", method = { RequestMethod.POST, RequestMethod.GET })
	public String creditList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {

		BzrGjtEmployeeInfo user = getUser(request.getSession());
		BzrGjtClassInfo classInfo = getCurrentClass(request.getSession());

		Map specialtyMap = commonMapService.getSpecialtyMap(user.getXxId());// 专业名称
		Map pyccMap = commonMapService.getPyccMap();// 层次
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID",classInfo.getXxId());
		searchParams.put("TEACH_CLASS_ID", classInfo.getClassId());
		if(EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("NJ")))){
			searchParams.put("NJ", classInfo.getGjtGrade().getGradeId());
		}

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = studyManageForTeachClassService.getCreditsList(searchParams, pageRequst);


		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("gradeMap", gradeMap);

		return "/new/class/scorespoint/creditList";
	}

	/**
	 * 查询学分列表相关类型数量
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("creditListCounts")
	@ResponseBody
	public Map<String,Object> creditListCounts(){
		
		Map<String,Object> dataMap = new HashMap<String,Object>();
		
		BzrGjtClassInfo classInfo = currentClass();
		Map<String, Object> searchParams = Servlets.getParametersStartingWith();
		searchParams.put("XX_ID",classInfo.getXxId());
		searchParams.put("TEACH_CLASS_ID", classInfo.getClassId());
		if(EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("NJ")))){
			searchParams.put("NJ", classInfo.getGjtGrade().getGradeId());
		}
		// 查询条件统计项
		searchParams.put("SCORE_TYPE_TEMP", "");
		long score_count = studyManageForTeachClassService.getCreditsCount(searchParams);
		searchParams.put("SCORE_TYPE_TEMP", "2");
		long score_count2 = studyManageForTeachClassService.getCreditsCount(searchParams);

		dataMap.put("SCORE_COUNT", score_count);//全部
		dataMap.put("SCORE_COUNT1", (score_count - score_count2));
		dataMap.put("SCORE_COUNT2", score_count2);
		
		return dataMap;
	}
	
	
	/**
	 * 学支平台--成绩与学分详情页
	 * 
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/scoresPointDetail", method = { RequestMethod.POST, RequestMethod.GET })
	public String scoresPointDetail(@RequestParam String id, Model model, HttpServletRequest request) {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("studentId", id);
		//TODO 尚未未优化
		Map resultMap = scorePointService.getScorePointDetail(searchParams);
		model.addAttribute("resultMap", resultMap);
		return "/new/class/scorespoint/scoresPointDetail";
	}


	/**
	 * 学支平台--查看历史成绩
	 * @param teachPlanId
	 * @param studentId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getHistoryView/{teachPlanId}/{studentId}",method = {RequestMethod.POST,RequestMethod.GET})
	public String getHistoryView(@PathVariable("teachPlanId") String teachPlanId, @PathVariable("studentId") String studentId, Model model){

		Map<String,Object> formMap = new HashMap<String, Object>();
		formMap.put("teachPlanId",teachPlanId);
		formMap.put("studentId", studentId);
		List<Map<String,Object>> resultList = null;
		try {
			//TODO 尚未未优化
			resultList = scorePointService.getHistoryScore(formMap);
		}catch (Exception e){
			e.printStackTrace();
		}

		model.addAttribute("resultList",resultList);
		return "/new/class/scorespoint/get_historyscore_view";
	}


	/**
	 * 学支平台--成绩查询导出页面
	 * @param totalNum
	 * @return
	 */
	@RequestMapping(value = "/scoreListExport/{totalNum}",method = {RequestMethod.GET,RequestMethod.POST})
	public String scoreListExport(@PathVariable("totalNum") String totalNum,HttpServletRequest request,Model model){
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
		return "/new/class/scorespoint/score_list_export";
	}


	/**
	 * 学支平台--学情分析--》学员学情分析列表下载
	 */
	@RequestMapping(value = "downLoadScoreListExportXls",method = {RequestMethod.POST})
	public View downLoadScoreListExportXls(HttpServletRequest request, HttpServletResponse response){
		BzrGjtEmployeeInfo user = getUser(request.getSession());
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if(flag!=null&&flag==true){
			Map searchParams = (Map) request.getSession().getAttribute("downLoadScoreListExportXls");
			Workbook wb = studyManageForTeachClassService.downLoadScoreListExportXls(searchParams);
			
			String fileName = ObjectUtils.toString(searchParams.get("prefixName"),"")+"班级学员成绩列表.xls";
			request.getSession().setAttribute(user.getSjh(),"");
			
			return new CommonExcelView(wb, fileName);
		}else {
			throw new RuntimeException("您没有权限");
		}
	}


}

