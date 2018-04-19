/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.classManager.student;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.criterion.MatchMode;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtExamPoint;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtTermInfo;
import com.ouchgzee.headTeacher.service.BzrCacheService;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.exam.BzrGjtRecResultService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.service.student.BzrGjtTermInfoService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * 考试管理控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月13日
 * @version 2.5
 */
@Controller
@RequestMapping("/home/class/exam")
public class ExamController extends BaseController {

	@Autowired
	private BzrGjtRecResultService gjtRecResultService;

	@Autowired
	private BzrGjtStudentService gjtStudentService;

	@Autowired
	private BzrGjtTermInfoService gjtTermInfoService;

	@Autowired
	private BzrCacheService cacheService;

	@Autowired
	private BzrCommonMapService commonMapService;

	/**
	 * 考试预约信息列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, HttpSession session) {
		BzrGjtEmployeeInfo user = super.getUser(session);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");

		Map<String, String> pyccMap = commonMapService.getPyccMap();
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());

		String classId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID))
				? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(session);
		// Page<StudentRecResultDto> infos =
		// gjtRecResultService.queryStudentRecResultByPage(classId,
		// searchParams, pageRequst);
		// 获取此班级所有学员所有科目的考试预约情况列表
		searchParams.put("classId", classId);
		Page<Map<String, Object>> infos = gjtRecResultService.queryStudentRecResultPage(searchParams, pageRequst);// 根据条件的查询方法

		searchParams.remove("chooseType");
		Page<Map<String, Object>> infos0 = gjtRecResultService.queryStudentRecResultPage(searchParams, pageRequst);// 全部
		//
		List<Map<String, Object>> examType = gjtRecResultService.queryExamTypeByTeachplan();// 根据教学计划获取此教学计划里面拥有的考试方式

		Map<String, List<Object>> ksfs = new HashMap<String, List<Object>>();
		if (EmptyUtils.isNotEmpty(examType)) {
			for (Map map : examType) {
				searchParams.put("ksfs", map.get("CODE"));
				Page<Map<String, Object>> infosInclude = gjtRecResultService.queryStudentRecResultPage(searchParams,
						pageRequst);
				List<Object> temp = new LinkedList<Object>();
				temp.add(map.get("CODE"));
				temp.add(infosInclude.getTotalElements());
				ksfs.put(ObjectUtils.toString(map.get("NAME")), temp);
			}
		}

		searchParams.remove("ksfs");
		searchParams.put("chooseType", "1");
		Page<Map<String, Object>> infos1 = gjtRecResultService.queryStudentRecResultPage(searchParams, pageRequst);
		searchParams.put("chooseType", "2");
		Page<Map<String, Object>> infos2 = gjtRecResultService.queryStudentRecResultPage(searchParams, pageRequst);
		searchParams.put("chooseType", "3");
		Page<Map<String, Object>> infos3 = gjtRecResultService.queryStudentRecResultPage(searchParams, pageRequst);
		searchParams.put("chooseType", "4");
		Page<Map<String, Object>> infos4 = gjtRecResultService.queryStudentRecResultPage(searchParams, pageRequst);

		Map<String, Object> infoNum = new HashMap<String, Object>();

		infoNum.put("ksfs", ksfs);
		infoNum.put("num0", infos0.getTotalElements());
		infoNum.put("num1", infos1.getTotalElements());
		infoNum.put("num2", infos2.getTotalElements());
		infoNum.put("num3", infos3.getTotalElements());
		infoNum.put("num4", infos4.getTotalElements());
		model.addAttribute("infoNum", infoNum);// 导航条数据统计
		model.addAttribute("pyccMap", pyccMap);// 培养层次
		model.addAttribute("gradeMap", gradeMap);// 年级
		model.addAttribute("specialtyMap", specialtyMap);// 专业
		model.addAttribute("infos", infos);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		searchParams.remove("page");
		searchParams.remove("sortType");
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, ""));
		return "new/class/exam/list";
	}

	/**
	 * 加载预约考试信息
	 * 
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "update/{studentId}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("studentId") String studentId, HttpServletRequest request, Model model) {
		BzrGjtStudentInfo studentInfo = gjtStudentService.queryById(studentId);
		Object[] studentReserveSituation = gjtRecResultService.countStudentReserveSituation(studentId);

		// 课程预约情况
		List<Map> infos = gjtRecResultService.queryStudentRecResultDetail(studentId);

		// 当前学期和考点
		String currentTermId = gjtTermInfoService.getStudentCurrentTerm(studentId);
		BzrGjtTermInfo currentTermInfo = gjtTermInfoService.queryById(currentTermId);
		Map searchParams = new HashMap();
		searchParams.put("EQ_gjtStudentExamPointList.studentId", studentId);
		searchParams.put("EQ_gjtStudentExamPointList.termId", currentTermId);
		searchParams.put("EQ_gjtStudentExamPointList.isDeleted", Constants.BOOLEAN_NO);
		List<BzrGjtExamPoint> currentExamPointList = gjtRecResultService.queryExamPointBy(searchParams, null);

		model.addAttribute("info", studentInfo);
		model.addAttribute("studentReserveSituation", studentReserveSituation);
		model.addAttribute("infos", infos);
		model.addAttribute("currentTermId", currentTermId);
		model.addAttribute("currentTermInfo", currentTermInfo);
		model.addAttribute("currentExamPoint",
				(currentExamPointList != null && currentExamPointList.size() > 0) ? currentExamPointList.get(0) : null);
		model.addAttribute("action", "update");
		return "new/class/exam/subscribe";
	}

	/**
	 * 预约考试
	 * 
	 * @param recId
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "appoint", method = RequestMethod.GET)
	public String appoint(@RequestParam String recId, @RequestParam String studentId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "预约成功");

		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		try {
			String[] recIds = recId.split(",");
			gjtRecResultService.updateRecExamCourse(studentId, recIds, employeeInfo.getGjtUserAccount().getId());
		} catch (Exception e) {
			feedback = new Feedback(false, "预约失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/home/class/exam/update/" + studentId;
	}

	/**
	 * 取消考试
	 * 
	 * @param recId
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "cancel", method = RequestMethod.GET)
	public String cancel(@RequestParam String recId, @RequestParam String studentId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "取消成功");

		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		try {
			gjtRecResultService.updateCancelRecExamCourse(studentId, recId, employeeInfo.getGjtUserAccount().getId());
		} catch (Exception e) {
			feedback = new Feedback(false, "取消失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/home/class/exam/update/" + studentId;
	}

	/**
	 * 考点信息列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "pointList", method = RequestMethod.GET)
	@ResponseBody
	public Feedback pointList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request,
			HttpSession session) {
		Feedback feedback = new Feedback(true, "成功");
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		if (com.gzedu.xlims.common.StringUtils.isNotBlank(searchParams.get("area"))) {
			searchParams.put("EQ_areaId", searchParams.get("area"));
		} else if (com.gzedu.xlims.common.StringUtils.isNotBlank(searchParams.get("city"))) {
			searchParams.put("LIKE_areaId",
					MatchMode.START.toMatchString(searchParams.get("city").toString().substring(0, 4)));
		} else if (com.gzedu.xlims.common.StringUtils.isNotBlank(searchParams.get("province"))) {
			searchParams.put("LIKE_areaId",
					MatchMode.START.toMatchString(searchParams.get("province").toString().substring(0, 2)));
		}
		searchParams.remove("area");
		searchParams.remove("city");
		searchParams.remove("province");
		List<BzrGjtExamPoint> infos = gjtRecResultService.queryExamPointBy(searchParams, null);
		for (Iterator<BzrGjtExamPoint> iterator = infos.iterator(); iterator.hasNext();) {
			BzrGjtExamPoint ep = iterator.next();
			if (StringUtils.isNotBlank(ep.getAreaId())) {
				List<BzrCacheService.Value> plist = cacheService.getCachedDictionary(BzrCacheService.DictionaryKey.PROVINCE);
				List<BzrCacheService.Value> clist = cacheService.getCachedDictionary(BzrCacheService.DictionaryKey.CITY
						.replace("${Province}", ep.getAreaId().substring(0, 2) + "0000"));
				List<BzrCacheService.Value> alist = cacheService.getCachedDictionary(
						BzrCacheService.DictionaryKey.AREA.replace("${Province}", ep.getAreaId().substring(0, 2) + "0000")
								.replace("${City}", ep.getAreaId().substring(0, 4) + "00"));
				String areaAllName = "";
				for (BzrCacheService.Value v : plist) {
					if (v.getCode().equals(ep.getAreaId().substring(0, 2) + "0000"))
						areaAllName += v.getName();
				}
				for (BzrCacheService.Value v : clist) {
					if (v.getCode().equals(ep.getAreaId().substring(0, 4) + "00"))
						areaAllName += v.getName();
				}
				for (BzrCacheService.Value v : alist) {
					if (v.getCode().equals(ep.getAreaId()))
						areaAllName += v.getName();
				}
				ep.setColAreaAllName(areaAllName);
			}
		}

		feedback.setObj(infos);
		return feedback;
	}

	/**
	 * 预约考点
	 * 
	 * @param examPointId
	 * @param studentId
	 * @param termId
	 * @return
	 */
	@RequestMapping(value = "saveExamPoint", method = RequestMethod.GET)
	public String saveExamPoint(@RequestParam String examPointId, @RequestParam String studentId,
			@RequestParam String termId, HttpSession session, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "预约考点成功");

		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		try {
			gjtRecResultService.updateRecExamPoint(studentId, termId, examPointId,
					employeeInfo.getGjtUserAccount().getId());
		} catch (Exception e) {
			feedback = new Feedback(false, "预约考点失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/home/class/exam/update/" + studentId;
	}

	/**
	 * 导出考试预约信息功能
	 * 
	 * @param studentId
	 */
	@RequestMapping(value = "exportInfo_stuResExam", method = RequestMethod.POST)
	public void exportInfoDetails(@RequestParam String studentId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {
		String xm = gjtStudentService.queryXmById(studentId);
		String outputUrl = "考试预约详情_" + xm + "_" + Calendar.getInstance().getTimeInMillis() + ".xls";

		HSSFWorkbook workbook = gjtRecResultService.exportStudentRecResultDetailToExcel(studentId);

		super.downloadExcelFile(response, workbook, outputUrl);
	}

	@RequestMapping(value = "exportInfo", method = RequestMethod.POST)
	public void exportInfo(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		String outputUrl = "考试预约信息-" + Calendar.getInstance().getTimeInMillis() + ".xls";
		String classId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID))
				? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(request.getSession());
		// Page<StudentRecResultDto> infos =
		// gjtRecResultService.queryStudentRecResultByPage(classId,
		// searchParams, pageRequst);
		// 获取此班级所有学员所有科目的考试预约情况列表
		searchParams.put("classId", classId);

		HSSFWorkbook workbook = gjtRecResultService.exportStudentRecResultsToExcel(searchParams);
		try {
			super.downloadExcelFile(response, workbook, outputUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
