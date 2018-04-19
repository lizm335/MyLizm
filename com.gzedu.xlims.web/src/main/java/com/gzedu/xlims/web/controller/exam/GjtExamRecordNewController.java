package com.gzedu.xlims.web.controller.exam;

import com.gzedu.xlims.common.CSVUtils;
import com.gzedu.xlims.common.DateUtil;
import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.ToolUtil;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.exam.GjtExamBatchNewDao;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.exam.GjtExamRecordNewService;
import com.gzedu.xlims.service.studymanage.StudyManageService;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.common.UploadTmpFile;
import com.gzedu.xlims.web.controller.base.BaseController;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考试成绩管理
 */
@Controller
@RequestMapping("/exam/new/record")
public class GjtExamRecordNewController extends BaseController {
	
	@Autowired
	CommonMapService commonMapService;
	
	@Autowired
	GjtExamRecordNewService gjtExamRecordNewService;

	@Autowired
	StudyManageService studyManageService;
	
	@Autowired
	GjtExamBatchNewDao gjtExamBatchNewDao;
	
	/**
	 * 考试管理=》考试成绩
	 * @return
	 */
	@RequestMapping(value = "getExamRecordList")
	public String getExamRecordList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业名称
		Map pyccMap = commonMapService.getPyccMap();// 层次
		Map examTypeMap = commonMapService.getExamTypeMap();// 考试方式
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 入学学期
		Map yearMap = commonMapService.getYearMap(user.getGjtOrg().getId());// 入学年级
		Map courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());// 课程列表
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 学习中心

		Map searchParams = Servlets.getParametersStartingWith(request, "");

		if (EmptyUtils.isNotEmpty(user.getGjtOrg().getGjtStudyCenter())) {
			searchParams.put("EQ_studyId", user.getGjtOrg().getGjtStudyCenter().getId());
		} else {
			searchParams.put("XX_ID", user.getGjtOrg().getId());
		}

		Map<String, String> currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(currentGradeMap)) {
			String currentGradeId = currentGradeMap.keySet().iterator().next();
			model.addAttribute("currentGradeId", currentGradeId);
			if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("NJ")))) {
				searchParams.put("NJ", currentGradeId);
			} else if ("all".equals(ObjectUtils.toString(searchParams.get("NJ")))) {
				searchParams.remove("NJ");
			}
		}
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Page pageInfo = studyManageService.getScoreList(searchParams, pageRequst);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("examTypeMap", examTypeMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("yearMap", yearMap);
		model.addAttribute("courseMap", courseMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		StringBuffer prefixName = new StringBuffer();// 导出文件名字根据搜索条件命名前缀
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			prefixName.append(
					ObjectUtils.toString(studyCenterMap.get(ObjectUtils.toString(searchParams.get("XXZX_ID")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("NJ")))) {
			prefixName.append(ObjectUtils.toString(gradeMap.get(ObjectUtils.toString(searchParams.get("NJ")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			prefixName.append(
					ObjectUtils.toString(specialtyMap.get(ObjectUtils.toString(searchParams.get("SPECIALTY_ID"))))
							+ "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))) {
			prefixName.append(
					ObjectUtils.toString(courseMap.get(ObjectUtils.toString(searchParams.get("COURSE_ID")))) + "-");
		}
		searchParams.put("prefixName", ObjectUtils.toString(prefixName.toString(), ""));
		request.getSession().setAttribute("downLoadScoreListExportXls", searchParams);// 导出数据的查询条件
		return "edumanage/exam/exam_record_list";
	}
	
	/**
	 * 考试管理=》登记成绩查询列表
	 * @return
	 */
	@RequestMapping(value = "getExamRegisterList")
	public String getExamRegisterList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String schoolId= user.getGjtOrg().getId();
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());	// 专业名称
		Map pyccMap = commonMapService.getPyccMap();									// 层次
		Map examTypeMap = commonMapService.getExamTypeMap();							// 考试方式
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());			// 入学学期
		Map yearMap = commonMapService.getYearMap(user.getGjtOrg().getId());			// 入学年级
		Map courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());		// 课程列表
		Map batchMap = commonMapService.getGjtExamBatchNewIdNameMap(schoolId);			// 考试批次
		// Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId()); // 学习中心

		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", schoolId);

		if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("NJ")))) {
			String code = commonMapService.getCurrentGjtExamBatchNew(schoolId);
			searchParams.put("EXAM_BATCH_CODE", code);
			model.addAttribute("EXAM_BATCH_CODE", code);
		} else {
			model.addAttribute("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		if ("all".equals(ObjectUtils.toString(searchParams.get("NJ")))) {
			searchParams.put("NJ", "");
		}
		if ("all".equals(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))) {
			searchParams.put("EXAM_BATCH_CODE", "");
		}

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Page pageInfo = gjtExamRecordNewService.getExamRegisterList(searchParams, pageRequst);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("examTypeMap", examTypeMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("yearMap", yearMap);
		model.addAttribute("courseMap", courseMap);
		// model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("batchMap", batchMap);

		return "edumanage/exam/exam_register_list";
	}
	
	/**
	 * 考试管理=》考试成绩(统计)
	 * @return
	 */
	@RequestMapping(value = "getRecordCount")
	@ResponseBody
	public Map getRecordCount(HttpServletRequest request) {
		Map resultMap = new HashMap();
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map searchParams = Servlets.getParametersStartingWith(request, "");

			if (EmptyUtils.isNotEmpty(user.getGjtOrg().getGjtStudyCenter())) {
				searchParams.put("EQ_studyId", user.getGjtOrg().getGjtStudyCenter().getId());
			} else {
				searchParams.put("XX_ID", user.getGjtOrg().getId());
			}

			// 查询条件统计项
			searchParams.put("EXAM_STATE", searchParams.get("EXAM_STATE_TEMP"));
			long score_state_count = studyManageService.getScoreCount(searchParams);
			resultMap.put("EXAM_STATE_COUNT", score_state_count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 考试管理=》登记成绩(统计)
	 * @return
	 */
	@RequestMapping(value = "getRegisterCount")
	@ResponseBody
	public Map getRegisterCount(HttpServletRequest request) {
		Map resultMap = new HashMap();
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map searchParams = Servlets.getParametersStartingWith(request, "");
			String xxId = user.getGjtOrg().getId();
			searchParams.put("XX_ID", xxId);
			
			if ("all".equals(ObjectUtils.toString(searchParams.get("NJ")))) {
				searchParams.put("NJ", "");
			}
			
			// 查询条件统计项
			int score_state_count = gjtExamRecordNewService.getRegisterCount(searchParams);
			resultMap.put("EXAM_STATE_COUNT", score_state_count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 管理后台--考试管理=》考试成绩查看详情
	 * @return
	 */
	@RequestMapping(value = "getExamRecordDetail")
	public String getExamRecordDetail(Model model, HttpServletRequest request) {
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("student_id",ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		Map resultMap = gjtExamRecordNewService.getExamRecordDetail(searchParams);
		model.addAttribute("resultMap", resultMap);
		return "edumanage/exam/exam_record_detail";
	}


	@RequestMapping(value = "getHistoryView/{teachPlanId}/{studentId}",method = {RequestMethod.POST,RequestMethod.GET})
	public String getHistoryView(@PathVariable("teachPlanId") String teachPlanId,@PathVariable("studentId") String studentId,Model model){

		Map<String,Object> formMap = new HashMap<String, Object>();
		formMap.put("teachPlanId",teachPlanId);
		formMap.put("studentId", studentId);
		List<Map<String,Object>> resultList = null;
		try {
			resultList = gjtExamRecordNewService.getHistoryScore(formMap);
		}catch (Exception e){
			e.printStackTrace();
		}

		model.addAttribute("resultList",resultList);
		return "edumanage/exam/get_historyscore_view";
	}
	
	
	/**
	 *  导入登记成绩
	 * @return
	 */
	@RequestMapping(value = "getRegisterRecordImport")
	public String getRegisterRecordImport(HttpServletRequest request, HttpServletResponse response, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String phone = ObjectUtils.toString(user.getSjh());
		String schoolId= user.getGjtOrg().getId();
		try {
			if(EmptyUtils.isNotEmpty(phone)){
				phone = phone.substring(phone.length()-4,phone.length());
			}
			Map formMap = Servlets.getParametersStartingWith(request, "");
			
			model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(schoolId));
			model.addAttribute("resultMap", formMap);
			model.addAttribute("sjh", user.getSjh());
			model.addAttribute("sjhs", phone); 
		} catch (Exception e){
			e.printStackTrace();
		}
		return "edumanage/exam/record/exam_register_import";
	}
	
	/**
	 *  导入登记成绩（统考/学位英语）
	 * @return
	 */
	@RequestMapping(value = "getRegisterTkXwImport")
	public String getRegisterTkXwImport(HttpServletRequest request, HttpServletResponse response, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String phone = ObjectUtils.toString(user.getSjh());
		String schoolId= user.getGjtOrg().getId();
		try {
			if(EmptyUtils.isNotEmpty(phone)){
				phone = phone.substring(phone.length()-4,phone.length());
			}
			Map formMap = Servlets.getParametersStartingWith(request, "");
			model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(schoolId));
			model.addAttribute("resultMap", formMap);
			model.addAttribute("sjh", user.getSjh());
			model.addAttribute("sjhs", phone); 
		} catch (Exception e){
			e.printStackTrace();
		}
		return "edumanage/exam/record/exam_register_tkxw_import";
	}
	
	/**
	 *  导出成绩
	 * @return
	 */
	@RequestMapping(value = "getExpRecord")
	public String getExpRecord(HttpServletRequest request, HttpServletResponse response, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String phone = ObjectUtils.toString(user.getSjh());
		try {
			if(EmptyUtils.isNotEmpty(phone)){
				phone = phone.substring(phone.length()-4,phone.length());
			}
			Map formMap = Servlets.getParametersStartingWith(request, "");
			model.addAttribute("resultMap", formMap);
			model.addAttribute("sjh", user.getSjh());
			model.addAttribute("sjhs", phone); 
		} catch (Exception e){
			e.printStackTrace();
		}
		return "edumanage/exam/record/exam_record_exp";
	}
		
	/**
	 * 导入登记成绩
	 * 
	 * @param request
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "importRegisterRecord")
	@SysLog("考试成绩-登记成绩")
	public String importRegisterRecord(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") CommonsMultipartFile file, Model model) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<String> filePaths = UploadTmpFile.uploadSimple(request);
		Map formMap = Servlets.getParametersStartingWith(request, "");
		formMap.put("XX_ID", user.getGjtOrg().getId());
		formMap.put("USER_ID", user.getId());
		Map resultMap = gjtExamRecordNewService.importRegisterRecord(filePaths.get(0), formMap, request.getSession().getServletContext().getRealPath(""));
//		Map resultMap = gjtExamRecordNewService.importRegisterRecordTeshuchuli(filePaths.get(0), formMap, request.getSession().getServletContext().getRealPath("")); // 导入登记成绩-特殊处理  按照学号课程代码对应得上就是干
		model.addAttribute("resultMap", resultMap);
		return "edumanage/exam/record/exam_register_import_result";
	}
	
	/**
	 * 导入登记成绩统考学位英语
	 * 
	 * @param request
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "importRegisterTkXwRecord")
	@SysLog("考试成绩-登记成绩(统考学位英语)")
	public String importRegisterTkXwRecord(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") CommonsMultipartFile file, Model model) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<String> filePaths = UploadTmpFile.uploadSimple(request);
		Map formMap = Servlets.getParametersStartingWith(request, "");
		formMap.put("XX_ID", user.getGjtOrg().getId());
		formMap.put("USER_ID", user.getId());
		Map resultMap = gjtExamRecordNewService.importRegisterTkXwRecord(filePaths.get(0), formMap, request.getSession().getServletContext().getRealPath(""));
		model.addAttribute("resultMap", resultMap);
		return "edumanage/exam/record/exam_register_tkxw_result";
	}
	
	/**
	 * 导出成绩
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "expExamRecord")
	@SysLog("考试成绩-导出成绩")
	@ResponseBody
	public void expExamRecord(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map formMap = Servlets.getParametersStartingWith(request, "");
		try {
			if (EmptyUtils.isNotEmpty(user.getGjtOrg().getGjtStudyCenter())) {
				formMap.put("EQ_studyId", user.getGjtOrg().getGjtStudyCenter().getId());
			} else {
				formMap.put("XX_ID", user.getGjtOrg().getId());
			}

			if ("all".equals(ObjectUtils.toString(formMap.get("NJ")))) {
				formMap.put("NJ", "");
			}
			String content = gjtExamRecordNewService.expExamRecordToCsvContent(formMap);
			request.getSession().setAttribute(user.getSjh(),"");

			try {
				String fileName = "导出成绩表.csv";
				CSVUtils.exportCsv(fileName, content, request, response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出登记成绩
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "expExamRegisterRecord")
	@SysLog("登记成绩-导出成绩")
	@ResponseBody
	public void expExamRegisterRecord(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map formMap = Servlets.getParametersStartingWith(request, "");
		formMap.put("XX_ID", ObjectUtils.toString(user.getGjtOrg().getId()));
		try {
			if ("all".equals(ObjectUtils.toString(formMap.get("NJ")))) {
				formMap.put("NJ", "");
			}
			if ("all".equals(ObjectUtils.toString(formMap.get("EXAM_BATCH_CODE")))) {
				formMap.put("EXAM_BATCH_CODE", "");
			}
			Workbook wb = gjtExamRecordNewService.expExamRegisterRecord(formMap);
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setHeader("Content-Disposition","attachment; filename=" + new String(("导出成绩表.xls").getBytes("UTF-8"), "ISO8859-1"));
			wb.write(response.getOutputStream());
			request.getSession().setAttribute(user.getSjh(),"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 下载成功失败记录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "downRecordResult")
	@ResponseBody
	public void downRecordResult(HttpServletRequest request, HttpServletResponse response, String execleFileName) {
		if ("getExemptRecordImport".equals(execleFileName)) {
			execleFileName = GjtExamRecordNewController.class.getClassLoader().getResource("").getPath()+"/excel/exam/免修免考或统考成绩导入模版.xls";
		} else if ("getRegisterRecordImport".equals(execleFileName)) {
			execleFileName = GjtExamRecordNewController.class.getClassLoader().getResource("").getPath()+"/excel/exam/成绩登记导入模板.xls";
		} else if ("getRegisterTkXwImport".equals(execleFileName)) {
			execleFileName = GjtExamRecordNewController.class.getClassLoader().getResource("").getPath()+"/excel/exam/统考学位英语成绩登记导入模板.xls";
		}
		ToolUtil.download(execleFileName, response);
	}
	
	/**
	 * 考试管理=》考情分析(考试预约明细)
	 * @return
	 */
	@RequestMapping(value = "getExamAppointmentList")
	public String getRecordAppointmentList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String schoolId= user.getGjtOrg().getId();
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());	// 专业名称
		Map pyccMap = commonMapService.getPyccMap();									// 层次
		Map examTypeMap = commonMapService.getExamTypeMap();							// 考试方式
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());			// 入学学期
		Map courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());		// 课程列表
		Map batchMap = commonMapService.getGjtExamBatchNewIdNameMap(schoolId);			// 考试计划

		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", schoolId);
		
		if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))) {
			String code = commonMapService.getCurrentGjtExamBatchNew(schoolId);
			searchParams.put("EXAM_BATCH_CODE", code);
			model.addAttribute("EXAM_BATCH_CODE", code);
		} else {
			model.addAttribute("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}
	
		
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Page pageInfo = gjtExamRecordNewService.getRecordAppointmentList(searchParams, pageRequst);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("examTypeMap", examTypeMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("courseMap", courseMap);
		model.addAttribute("batchMap", batchMap);
		
		return "edumanage/exam/record/exam_record_appointment";
	}
	
	/**
	 * 考试管理=》考情分析(考试预约统计)
	 * @return
	 */
	@RequestMapping(value = "getExamAppointmentCount")
	@ResponseBody
	public Map getExamAppointmentCount(HttpServletRequest request) {
		Map resultMap = new HashMap();
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map searchParams = Servlets.getParametersStartingWith(request, "");
			String xxId = user.getGjtOrg().getId();
			searchParams.put("XX_ID", xxId);
			
			
			// 查询条件统计项
			searchParams.put("EXAM_STATE", "");
			int score_state_count = gjtExamRecordNewService.getExamAppointmentCount(searchParams);
			resultMap.put("EXAM_STATE_COUNT", score_state_count);
			int tempRate = 0;
			
			// 已达标，未预约
			searchParams.put("EXAM_STATE", "1");
			int score_state_count1 = gjtExamRecordNewService.getExamAppointmentCount(searchParams);
			if (score_state_count>0 && score_state_count1>0) {
				tempRate = ToolUtil.getIntRound(((double)score_state_count1/(double)score_state_count)*100);
			}
			resultMap.put("EXAM_STATE_COUNT1", score_state_count1+"|"+tempRate+"%");
			
			// 未达标，未预约
			searchParams.put("EXAM_STATE", "2");
			int score_state_count2 = gjtExamRecordNewService.getExamAppointmentCount(searchParams);
			tempRate = 0;
			if (score_state_count>0 && score_state_count2>0) {
				tempRate = ToolUtil.getIntRound(((double)score_state_count2/(double)score_state_count)*100);
			}
			resultMap.put("EXAM_STATE_COUNT2", score_state_count2+"|"+tempRate+"%");
			
			// 已达标，已预约
			searchParams.put("EXAM_STATE", "3");
			int score_state_count3 = gjtExamRecordNewService.getExamAppointmentCount(searchParams);
			tempRate = 0;
			if (score_state_count>0 && score_state_count3>0) {
				tempRate = ToolUtil.getIntRound(((double)score_state_count3/(double)score_state_count)*100);
			}
			resultMap.put("EXAM_STATE_COUNT3", score_state_count3+"|"+tempRate+"%");
			
			// 未达标，已预约
			searchParams.put("EXAM_STATE", "4");
			int score_state_count4 = gjtExamRecordNewService.getExamAppointmentCount(searchParams);
			tempRate = 0;
			if (score_state_count>0 && score_state_count4>0) {
				tempRate = ToolUtil.getIntRound(((double)score_state_count4/(double)score_state_count)*100);
			}
			resultMap.put("EXAM_STATE_COUNT4", score_state_count4+"|"+tempRate+"%");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 导出考试情况数据
	 */
	@SysLog("导出考试情况数据")
	@RequestMapping(value = "exportExamAppointment")
	public void exportExamAppointment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		String xxId = user.getGjtOrg().getId();
		searchParams.put("XX_ID", xxId);
		
		String path = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL + "exam" + File.separator;
		String outFile = gjtExamRecordNewService.exportExamAppointment(searchParams, path);

		super.downloadFile(request, response, path + outFile);
		// 复制文件以便好手动下载
		FileUtils.copyFile(new File(path + outFile), new File(request.getSession().getServletContext().getRealPath("") + File.separator + "static" + File.separator + outFile));
		FileKit.delFile(path + outFile);
	}
	
	/**
	 * 考试情况明细查询列表
	 * @return
	 */
	@RequestMapping(value = "getExamDetailList")
	public String getExamDetailList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String schoolId= user.getGjtOrg().getId();
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());	// 专业名称
		Map pyccMap = commonMapService.getPyccMap();									// 层次
		Map examTypeMap = commonMapService.getExamTypeMap();							// 考试方式
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());			// 入学学期
		Map batchMap = commonMapService.getGjtExamBatchNewIdNameMap(schoolId);			// 考试计划

		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", schoolId);
		
		if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))) {
			String code = commonMapService.getCurrentGjtExamBatchNew(schoolId);
			searchParams.put("EXAM_BATCH_CODE", code);
			model.addAttribute("EXAM_BATCH_CODE", code);
		} else {
			model.addAttribute("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}
		
		int num = 0;
		GjtExamBatchNew gjtExamBatchNew = gjtExamBatchNewDao.queryByExamBatchCodeAndXxId(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")), schoolId);
		if (EmptyUtils.isNotEmpty(gjtExamBatchNew) && EmptyUtils.isNotEmpty(gjtExamBatchNew.getRecordEnd())) {
			num = DateUtil.compareDate(gjtExamBatchNew.getRecordEnd());
		}
		String dateFlg = "N";
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Page pageInfo = null;
		if (num>=0) {
			pageInfo = gjtExamRecordNewService.getExamDetailList(searchParams, pageRequst);
			dateFlg = "Y";
		}

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("examTypeMap", examTypeMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("batchMap", batchMap);
		model.addAttribute("dateFlg", dateFlg);
		
		return "edumanage/exam/record/exam_detail_list";
	}
	
	/**
	 * 导出考试情况明细
	 */
	@SysLog("导出考试情况明细")
	@RequestMapping(value = "exportExamDetail")
	public void exportExamDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		String xxId = user.getGjtOrg().getId();
		searchParams.put("XX_ID", xxId);
		
		String path = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL + "exam" + File.separator;
		String outFile = gjtExamRecordNewService.exportExamDetail(searchParams, path);

		super.downloadFile(request, response, path + outFile);
		// 复制文件以便好手动下载
		FileUtils.copyFile(new File(path + outFile), new File(request.getSession().getServletContext().getRealPath("") + File.separator + "static" + File.separator + outFile));
		FileKit.delFile(path + outFile);
	}
	
	/**
	 * 考试情况明细查询列表(统计数字)
	 * @return
	 */
	@RequestMapping(value = "getExamDetailCount")
	@ResponseBody
	public Map getExamDetailCount(HttpServletRequest request) {
		Map resultMap = new HashMap();
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map searchParams = Servlets.getParametersStartingWith(request, "");
			String xxId = user.getGjtOrg().getId();
			searchParams.put("XX_ID", xxId);
			
			
			// 查询条件统计项
			searchParams.put("EXAM_STATE", "");
			int score_state_count = gjtExamRecordNewService.getExamDetailCount(searchParams);
			resultMap.put("EXAM_STATE_COUNT", score_state_count);
			
			// 登记中
			searchParams.put("EXAM_STATE", "1");
			int score_state_count1 = gjtExamRecordNewService.getExamDetailCount(searchParams);
			resultMap.put("EXAM_STATE_COUNT1", score_state_count1);
			
			// 未考试
			searchParams.put("EXAM_STATE", "2");
			int score_state_count2 = gjtExamRecordNewService.getExamDetailCount(searchParams);
			resultMap.put("EXAM_STATE_COUNT2", score_state_count2);
			
			// 作弊
			searchParams.put("EXAM_STATE", "3");
			int score_state_count3 = gjtExamRecordNewService.getExamDetailCount(searchParams);
			resultMap.put("EXAM_STATE_COUNT3", score_state_count3);
			
			// 已达标，已通过
			searchParams.put("EXAM_STATE", "4");
			int score_state_count4 = gjtExamRecordNewService.getExamDetailCount(searchParams);
			resultMap.put("EXAM_STATE_COUNT4", score_state_count4);
			
			// 已达标，未通过
			searchParams.put("EXAM_STATE", "5");
			int score_state_count5 = gjtExamRecordNewService.getExamDetailCount(searchParams);
			resultMap.put("EXAM_STATE_COUNT5", score_state_count5);
						
			// 未达标，未通过
			searchParams.put("EXAM_STATE", "6");
			int score_state_count6 = gjtExamRecordNewService.getExamDetailCount(searchParams);
			resultMap.put("EXAM_STATE_COUNT6", score_state_count6);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
}
