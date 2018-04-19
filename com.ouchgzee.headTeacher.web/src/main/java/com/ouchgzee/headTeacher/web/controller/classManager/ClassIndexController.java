/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.classManager;

import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.ouchgzee.headTeacher.dto.CourseClassInfoDto;
import com.ouchgzee.headTeacher.pojo.*;
import com.ouchgzee.headTeacher.pojo.exam.BzrGjtExamBatchNew;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.activity.BzrGjtActivityService;
import com.ouchgzee.headTeacher.service.exam.BzrGjtExamAppointmentService;
import com.ouchgzee.headTeacher.service.exam.BzrGjtExamBatchNewService;
import com.ouchgzee.headTeacher.service.exam.BzrGjtRecResultService;
import com.ouchgzee.headTeacher.service.feedback.BzrLcmsMutualSubjectService;
import com.ouchgzee.headTeacher.service.graduation.BzrGjtGraduationApplyService;
import com.ouchgzee.headTeacher.service.message.BzrGjtMessageUserService;
import com.ouchgzee.headTeacher.service.serviceManager.BzrGjtServiceManagerService;
import com.ouchgzee.headTeacher.service.student.BzrGjtClassService;
import com.ouchgzee.headTeacher.service.student.BzrGjtSpecialtyService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 班级首页控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月16日
 * @version 2.5
 */
@Controller
@RequestMapping("/home/class")
public class ClassIndexController extends BaseController {

	@Autowired
	private BzrGjtStudentService gjtStudentService;

	@Autowired
	private BzrGjtClassService gjtClassService;

	@Autowired
	private BzrGjtSpecialtyService gjtSpecialtyService;

	@Autowired
	private BzrGjtGraduationApplyService gjtGraduationApplyService;

	@Autowired
	private BzrGjtMessageUserService gjtMessageUserService;

	@Autowired
	private BzrGjtServiceManagerService gjtServiceManagerService;

	@Autowired
	private BzrGjtRecResultService gjtRecResultService;

	@Autowired
	private BzrLcmsMutualSubjectService lcmsMutualSubjectService;

	@Autowired
	private BzrGjtActivityService gjtActivityService;

	@Autowired
	private BzrGjtExamBatchNewService gjtExamBatchNewService;

	@Autowired
	private BzrGjtExamAppointmentService gjtExamAppointmentService;
	
	@Autowired
	private BzrCommonMapService commonMapService;

	/**
	 * 班级首页
	 * 
	 * @return
	 */
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(Model model, ServletRequest request, ServletResponse response, HttpSession session) {
		BzrGjtClassInfo currentClass = super.getCurrentClass(session);
		if (currentClass == null) {
			return "redirect:/home/class/list";
		}
		String classId = currentClass.getClassId();
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);

		PageRequest pageRequst = Servlets.buildPageRequest(1, 6);
		Map<String, Object> searchParams2 = new HashMap<String, Object>();
		searchParams2.put("EQ_gjtUserAccount.id", employeeInfo.getGjtUserAccount().getId());
//		searchParams2.put("EQ_gjtMessageInfo.infoType", "2");
		Page<BzrGjtMessageUser> infos = gjtMessageUserService.queryAll(searchParams2, pageRequst);

//		GjtMessageInfo messageInfo = new GjtMessageInfo();
//		messageInfo.setXxId(employeeInfo.getXxId());
//		messageInfo.setGetUser(employeeInfo.getGjtUserAccount().getLoginAccount());
//		Page<GjtMessageInfo> infos = gjtMessageService.queryHeadTeacherMessageInfoByPage(messageInfo, pageRequst);

		// 课程班
		BzrGjtSpecialty specialtyInfo = currentClass.getSpecialtyId() != null
				? gjtSpecialtyService.queryById(currentClass.getSpecialtyId()) : null;

		// 未确认入学学员数
		long notLearningCount = gjtStudentService.countNotLearningStudentByClassId(classId);
		// 未完善资料学员数
		long notPerfectCount = gjtStudentService.countNotPerfectStudentByClassId(classId);
		// 可申请毕业学员数
		long graduateCount = 0;// gjtStudentService.countGraduateStudentByClassId(classId);

		//查询“论文终稿已确认”状态和“论文终稿待确认”状态的论文
		Map<String, Object> mapgrad = new HashMap<String, Object>();
		mapgrad.put("EQ_classId", classId);
		mapgrad.put("EQ_isConfirm", "0");
		model.addAttribute("notConfirmGraduation", gjtGraduationApplyService.queryAll(mapgrad, pageRequst).getTotalElements());

		model.addAttribute("messageList", infos.getContent());
		model.addAttribute("specialtyInfo", specialtyInfo);

		model.addAttribute("notLearningCount", notLearningCount);
		model.addAttribute("notPerfectCount", notPerfectCount);
		model.addAttribute("graduateCount", graduateCount);
		// 查询“未预约科目”、“未预约考点”
		String orgId = "-1";
		if ("1".equals(employeeInfo.getGjtOrg().getOrgType())) {
			orgId = employeeInfo.getGjtOrg().getId();
		} else {
			orgId = commonMapService.getParentWithType(employeeInfo.getGjtOrg().getId(), "1");
		}
		BzrGjtExamBatchNew examBatch = gjtExamBatchNewService.findCurrentExamBatch(orgId);
		Map<String, Object> examMap = new HashMap<String, Object>();
		examMap.remove("EQ_pointStatus");

		examMap.put("EQ_appointmentStatus", "0");
		long waitExamCount = 0;
		if(examBatch != null) {
			waitExamCount = gjtExamAppointmentService.findCurrentAppointmentList(classId, examBatch.getExamBatchCode(), examMap, pageRequst).getTotalElements();
		}
		model.addAttribute("waitExamCount", waitExamCount);

		examMap.remove("EQ_appointmentStatus");
		examMap.put("EQ_pointStatus", "0");
		long waitExamPointCount = 0;
		if(examBatch != null) {
			waitExamPointCount = gjtExamAppointmentService.findCurrentAppointmentList(classId, examBatch.getExamBatchCode(), examMap, pageRequst).getTotalElements();
		}
		model.addAttribute("waitExamPointCount", waitExamPointCount);
//		model.addAttribute("waitExamCount", gjtRecResultService.countClassStudentWaitExam(classId));
//		model.addAttribute("waitExamPointCount", gjtRecResultService.countClassStudentWaitExamPoint(classId));
		model.addAttribute("unServiceCount", gjtServiceManagerService.countUnoverNum(classId)); // 待跟进服务
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bzrId", employeeInfo.getEmployeeId());
		map.put("classId", classId);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_forwardAccountId", employeeInfo.getGjtUserAccount().getId());
		searchParams.put("EQ_subjectStatus", "N");
		searchParams.put("EQ_isdeleted", "N");
		searchParams.put("EQ_classId", classId);
		model.addAttribute("unFeedbackCount", lcmsMutualSubjectService.queryAllCount(searchParams)); // 待处理答疑
		model.addAttribute("auditActivityNum", gjtActivityService.countWaitActivityStudentNum(map)); // 待审核活动报名

		List<BzrGjtGrade> termList = gjtStudentService.findCurrentTermAndBeforeTerm(employeeInfo.getXxId(), currentClass.getGjtGrade().getGradeId());
		String remainingDays = "-";
		if(termList != null && termList.size() > 0) {
			Date endDate = termList.get(termList.size() - 1).getEndDate();
			remainingDays = DateUtil.getIntervalDayAfter(endDate) + "";
		}
		model.addAttribute("termList", termList);
		model.addAttribute("remainingDays", remainingDays);
		return "/new/class/index";
	}

	/**
	 * 班级考勤
	 * @param termId
     * @return
     */
	@RequestMapping(value = "clockSituation", method = RequestMethod.GET)
	@ResponseBody
	public Feedback clockSituation(@RequestParam("termId") String termId,
								   Model model, ServletRequest request, ServletResponse response, HttpSession session) {
		Feedback feedback = new Feedback(true, "成功");
		try {
			String teachClassId = super.getCurrentClassId(session); // 教学班
			BzrGjtGrade grade = gjtStudentService.queryGradeById(termId);
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("startDate", grade.getStartDate() != null ? DateFormatUtils.ISO_DATE_FORMAT.format(grade.getStartDate()) + " 00:00:00" : "");
			queryParams.put("endDate", grade.getEndDate() != null ? DateFormatUtils.ISO_DATE_FORMAT.format(grade.getEndDate()) + " 23:59:59" : "");
			// 班级学员考勤情况
			Map clockingInSituation = gjtStudentService.countStudentClockingInSituationByClass(teachClassId, queryParams);
			// 班级学员学习情况
			Map studySituation = gjtStudentService.countStudySituationByClass(teachClassId, queryParams);

			Map result = new HashMap();
			result.put("clockingInSituation", clockingInSituation);
			result.put("studySituation", studySituation);
			feedback.setObj(result);
		} catch (Exception e) {
			feedback = new Feedback(false, "服务器异常");
		}
		return feedback;
	}

	/**
	 * 课程考勤
	 * @param courseId
     * @return
     */
	@RequestMapping(value = "clockSituationByCourseId", method = RequestMethod.GET)
	@ResponseBody
	public Feedback clockSituationByCourseId(@RequestParam("courseId") String courseId,
								   Model model, ServletRequest request, ServletResponse response, HttpSession session) {
		Feedback feedback = new Feedback(true, "成功");
		try {
			String teachClassId = super.getCurrentClassId(session); // 教学班
			// 班级学员考勤情况
			Map clockingInSituation = gjtStudentService.countClockInSituationByCourseClass(teachClassId, courseId);
			// 班级学员学习情况
			Map studySituation = new HashMap();
			studySituation.put("STUDYNUM", clockingInSituation.get("STUDYNUM"));
			studySituation.put("STUDYHOURNUM", clockingInSituation.get("STUDYHOURNUM"));

			Map result = new HashMap();
			result.put("clockingInSituation", clockingInSituation);
			result.put("studySituation", studySituation);
			feedback.setObj(result);
		} catch (Exception e) {
			feedback = new Feedback(false, "服务器异常");
		}
		return feedback;
	}

	/**
	 * 课程学情
	 * @param courseId
     * @return
     */
	@RequestMapping(value = "learnSituationByCourseId", method = RequestMethod.GET)
	@ResponseBody
	public Feedback learnSituationByCourseId(@RequestParam("termId") String termId,
											 @RequestParam("courseId") String courseId,
											 Model model, ServletRequest request, ServletResponse response, HttpSession session) {
		Feedback feedback = new Feedback(true, "成功");
		try {
			String teachClassId = super.getCurrentClassId(session); // 教学班
			BzrGjtGrade grade = gjtStudentService.queryGradeById(termId);
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("startDate", grade.getStartDate() != null ? DateFormatUtils.ISO_DATE_FORMAT.format(grade.getStartDate()) + " 00:00:00" : "");
			queryParams.put("endDate", grade.getEndDate() != null ? DateFormatUtils.ISO_DATE_FORMAT.format(grade.getEndDate()) + " 23:59:59" : "");
			// 班级学员学习情况
			Map learnSituation = gjtStudentService.countLearnSituationByCourseClass(teachClassId, courseId, queryParams);

			Map result = new HashMap();
			result.put("learnSituation", learnSituation);
			feedback.setObj(result);
		} catch (Exception e) {
			feedback = new Feedback(false, "服务器异常");
		}
		return feedback;
	}

	/**
	 * 学习排行TOP10
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value = "studyRankingByCourseId", method = RequestMethod.GET)
	@ResponseBody
	public Feedback studyRankingByCourseId(@RequestParam("termId") String termId,
										   @RequestParam("courseId") String courseId,
											 Model model, ServletRequest request, ServletResponse response, HttpSession session) {
		Feedback feedback = new Feedback(true, "成功");
		try {
			String teachClassId = super.getCurrentClassId(session); // 教学班
			BzrGjtGrade grade = gjtStudentService.queryGradeById(termId);
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("startDate", grade.getStartDate() != null ? DateFormatUtils.ISO_DATE_FORMAT.format(grade.getStartDate()) + " 00:00:00" : "");
			queryParams.put("endDate", grade.getEndDate() != null ? DateFormatUtils.ISO_DATE_FORMAT.format(grade.getEndDate()) + " 23:59:59" : "");
			// 班级学员学习进度
			List<Map> topTenSchedule = gjtStudentService.queryStudyRankingTopTenScheduleByCourseClass(teachClassId, courseId, queryParams);
			// 班级学员学习次数
			List<Map> topTenStudyNum = gjtStudentService.queryStudyRankingTopTenStudyNumByCourseClass(teachClassId, courseId, queryParams);
			// 班级学员学习时长
			List<Map> topTenStudyHour = gjtStudentService.queryStudyRankingTopTenStudyHourByCourseClass(teachClassId, courseId, queryParams);

			Map result = new HashMap();
			result.put("topTenSchedule", topTenSchedule);
			result.put("topTenStudyNum", topTenStudyNum);
			result.put("topTenStudyHour", topTenStudyHour);
			feedback.setObj(result);
		} catch (Exception e) {
			feedback = new Feedback(false, "服务器异常");
		}
		return feedback;
	}

	/**
	 * 课程班学习情况
	 * 
	 * @return
	 */
	/*@RequestMapping(value = "learnSituation", method = RequestMethod.GET)
	@ResponseBody
	public Feedback learnSituation(Model model, ServletRequest request, ServletResponse response, HttpSession session) {
		Feedback feedback = new Feedback(true, "成功");
		try {
			String teachClassId = super.getCurrentClassId(session); // 教学班
			String courseId = request.getParameter("courseId"); // 课程班
			if (courseId != null) {
				// 班级学员学习进度、成绩、考试概况-对课程
				Map<String, Object> learningSituation = new HashMap<String, Object>();
				learningSituation = gjtRecResultService.countClassStudentLearningSituation(teachClassId, courseId);
				// // 班级学员考试情况
				// Map examSituation =
				// gjtStudentService.countClassStudentExamSituation(teachClassId,
				// null);

				BigDecimal total = (BigDecimal) learningSituation.get("STUDENTNUM");
				BigDecimal pass = (BigDecimal) learningSituation.get("PASSNUM");

				// 总人数减去已经通过的，就是未通过的，（未通过的包括考试不通过的，正在学习的，正在登记的）
				int unPass = total.subtract(pass).intValue();

				learningSituation.put("UNPASSNUM", unPass);

				Map<String, Object> result = new HashMap<String, Object>();
				result.put("learningSituation", learningSituation);
				feedback.setObj(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			feedback = new Feedback(false, "服务器异常");
		}
		return feedback;
	}

	@RequestMapping(value = "studentSpecialty", method = RequestMethod.GET)
	@ResponseBody
	public Feedback studentSpecialty(Model model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		Feedback feedback = new Feedback(true, "成功");
		try {
			String teachClassId = super.getCurrentClassId(session); // 教学班
			GjtClassInfo gjtClassInfo = gjtClassService.queryById(teachClassId);

			int passCount = 0;
			int failCount = 0;
			if (gjtClassInfo != null) {
				List<String> list = gjtClassService.queryTeacherClassStudent(teachClassId);
				if (list != null) {
					List<String> ids = new ArrayList<String>();
					for (String id : list) {
						ids.add(id);
					}

					List<Object[]> studentSpecialtys = gjtClassService
							.getStudentSpecialty(gjtClassInfo.getSpecialtyId(), ids);// 学员学情

					for (Object[] objects : studentSpecialtys) {
						int zuidixuefen = Integer.parseInt(objects[1] == null ? "0" : objects[1].toString());
						int yixiuxuefen = Integer.parseInt(objects[2] == null ? "0" : objects[2].toString());
						if (yixiuxuefen > zuidixuefen) {
							passCount++;
						} else {
							failCount++;
						}
					}
				}
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("passCount", passCount);
			result.put("failCount", failCount);
			feedback.setObj(result);
		} catch (Exception e) {
			e.printStackTrace();
			feedback = new Feedback(false, "服务器异常");
		}
		return feedback;
	}*/

	/**
	 * 学期获取课程
	 * @param termId
     * @return
     */
	@RequestMapping(value = "getCourses", method = RequestMethod.GET)
	@ResponseBody
	public Feedback learnSituation(@RequestParam("termId") String termId,
			Model model, ServletRequest request, ServletResponse response, HttpSession session) {
		Feedback feedback = new Feedback(true, "成功");
		try {
			String teachClassId = super.getCurrentClassId(session); // 教学班
			List<CourseClassInfoDto> courseClassInfoDtoList = gjtClassService.queryCourseClassInfoByTeachClassIdAndTermId(teachClassId, termId);
			feedback.setObj(courseClassInfoDtoList);
		} catch (Exception e) {
			e.printStackTrace();
			feedback = new Feedback(false, "服务器异常");
		}
		return feedback;
	}

}
