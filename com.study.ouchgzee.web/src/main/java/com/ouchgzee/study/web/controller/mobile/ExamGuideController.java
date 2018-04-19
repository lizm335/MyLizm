package com.ouchgzee.study.web.controller.mobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.PlatfromTypeEnum;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtSetOrgCopyright;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.LcmsMutualSubject;
import com.gzedu.xlims.pojo.exam.GjtExamGuideFeedback;
import com.gzedu.xlims.pojo.status.SubjectSourceEnum;
import com.gzedu.xlims.service.exam.GjtExamGuideFeedbackService;
import com.gzedu.xlims.service.feedback.LcmsMutualSubjectService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.organization.GjtSetOrgCopyrightService;
import com.ouchgzee.study.service.exam.ExamServeService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.common.Servlets;

/**
 * 考试指引控制器
 */
@RestController
@RequestMapping("/pcenter/mobile/exam")
public class ExamGuideController extends BaseController {

	private static Log log = LogFactory.getLog(ExamGuideController.class);

	private static final String EXAM_BATCH_CODE = "201710110001";

	@Autowired
	ExamServeService examServeService;

	@Autowired
	GjtSetOrgCopyrightService gjtSetOrgCopyrightService;

	@Autowired
	GjtClassStudentService gjtClassStudentService;

	@Autowired
	GjtExamGuideFeedbackService gjtExamGuideFeedbackService;

	@Autowired
	LcmsMutualSubjectService lcmsMutualSubjectService;

	/**
	 * 待预约考试
	 * 
	 * @return
	 */
	@RequestMapping(value = "/appointmentExam", method = RequestMethod.GET)
	public Map<String, Object> appointmentExam(Model model, HttpServletRequest request, HttpSession session) {
		final GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		final GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(student.getXxId(),
				PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("EXAM_BATCH_CODE", EXAM_BATCH_CODE);
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("USER_TYPE", student.getUserType());
		searchParams.put("username", student.getSfzh());
		searchParams.put("XJZT", student.getXjzt());
		searchParams.put("KKZY", student.getMajor());
		// searchParams.put("PYCC", user.getGjtStudentInfo().getPycc());
		searchParams.put("XX_ID", ObjectUtils.toString(student.getXxId()));
		searchParams.put("SCHOOL_MODEL", ObjectUtils.toString(item.getSchoolModel(), "")); // 返回院校模式(0:非院校，1：院校)
		searchParams.put("condition", "1");

		Map resultMap = examServeService.queryAppointmentExam(searchParams);

		List<Map<String, Object>> appointmentNewList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> termList = (List<Map<String, Object>>) resultMap.get("LIST");
		for (Map<String, Object> term : termList) {
			List<Map<String, Object>> appointmentList = (List<Map<String, Object>>) term.get("APPOINTMENTLIST");
			for (Map<String, Object> appointment : appointmentList) {
				appointmentNewList.add(appointment);
			}
		}

		// 记录学员查看了考试指引
		Map searchParams2 = new HashMap();
		searchParams2.put("EQ_gjtStudentInfo.studentId", student.getStudentId());
		searchParams2.put("EQ_examBatchCode", EXAM_BATCH_CODE);
		long count = gjtExamGuideFeedbackService.count(searchParams2);
		if (count == 0) {
			GjtExamGuideFeedback examGuideFeedback = new GjtExamGuideFeedback();
			examGuideFeedback.setId(UUIDUtils.random());
			examGuideFeedback.setGjtStudentInfo(student);
			examGuideFeedback.setExamBatchCode(EXAM_BATCH_CODE);
			examGuideFeedback.setViewState(1);
			examGuideFeedback.setCreatedBy(user.getId());
			gjtExamGuideFeedbackService.insert(examGuideFeedback);
		}

		Map<String, Object> infos = new HashMap<String, Object>();
		infos.put("infos", appointmentNewList);
		return infos;
	}

	/**
	 * 学员提问<br/>
	 * 考试相关问题<br/>
	 */
	@RequestMapping(value = "addSubject", method = RequestMethod.POST)
	public void addSubject(HttpServletRequest request) throws CommonException {
		final GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		final GjtStudentInfo student = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String[] imgUrls = request.getParameterValues("imgUrls");

		if (StringUtils.isBlank(title) || StringUtils.isBlank(content)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "有必填参数为空！");
		}

		String adminUserId = "ef01341ecc304126a93422ed149edd83"; // gkfudaolaoshi
																	// 辅导老师解答

		GjtClassInfo classInfo = gjtClassStudentService.queryTeachClassInfoByStudetnId(student.getStudentId());
		String teacherClassId = "";
		String termId = "";
		if (classInfo != null) {
			teacherClassId = classInfo.getClassId();
			termId = classInfo.getTermcourseId();
		}

		StringBuffer imgUrl = new StringBuffer("");
		if (imgUrls != null && imgUrls.length > 0) {
			for (int i = 0; i < imgUrls.length; i++) {
				imgUrl.append(imgUrls[i]);
				if (i < imgUrls.length - 1) {
					imgUrl.append(",");
				}
			}
		}

		LcmsMutualSubject item = new LcmsMutualSubject();
		item.setClassId(teacherClassId);
		item.setCreateAccount(user);
		item.setForwardAccountId(adminUserId);// 发送给考务管理员
		item.setInitialAccountId(adminUserId);// 问题初始人
		item.setResPath(imgUrl.toString());
		item.setSubjectContent(content);
		item.setSubjectTitle(title);
		item.setUserId(student.getStudentId());// 兼容旧的userId字段和createBy字段，取studentId
		item.setCreatedBy(student.getStudentId());// 兼容旧的userId字段和createBy字段，取studentId
		item.setOrgId(student.getGjtSchoolInfo().getId());
		item.setTermcourseId(termId);
		item.setQuestionSource(SubjectSourceEnum.STUDY_SPACE.getNum());
		item.setQuestionSourcePath("考试指引：学员提问");
		item.setIsTranspond(Constants.BOOLEAN_0);// 非转发
		lcmsMutualSubjectService.save(item);
	}

	/**
	 * 学员反馈<br/>
	 */
	@RequestMapping(value = "addFeedback", method = RequestMethod.POST)
	public void addFeedback(HttpServletRequest request) throws CommonException {
		final GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String feedbackContent = request.getParameter("feedbackContent");

		if (StringUtils.isBlank(feedbackContent)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数！");
		}

		GjtExamGuideFeedback examGuideFeedback = gjtExamGuideFeedbackService
				.findByStudentIdAndExamBatchCode(user.getGjtStudentInfo().getStudentId(), EXAM_BATCH_CODE);
		examGuideFeedback.setFeedbackState(1);
		examGuideFeedback.setFeedbackContent(feedbackContent);
		examGuideFeedback.setFeedbackDt(new Date());
		examGuideFeedback.setUpdatedBy(user.getId());
		gjtExamGuideFeedbackService.update(examGuideFeedback);
	}

}
