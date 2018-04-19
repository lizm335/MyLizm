package com.gzedu.xlims.web.controller.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.dao.ViewStudentInfoDao;
import com.gzedu.xlims.pojo.GjtStudyYearInfo;
import com.gzedu.xlims.pojo.ViewStudentInfo;
import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.pojo.exam.GjtExamSubjectNew;
import com.gzedu.xlims.service.exam.GjtExamAppointmentNewService;
import com.gzedu.xlims.service.exam.GjtExamBatchNewService;
import com.gzedu.xlims.serviceImpl.exam.wk.GjtExamWkService;
import com.gzedu.xlims.web.common.Feedback;

@Controller
@RequestMapping("/api/exam/new/wk")
public class ApiExamWKController {
	private static final Log log = LogFactory.getLog(ApiExamWKController.class);

	@Autowired
	private GjtExamAppointmentNewService gjtExamAppointmentNewService;

	@Autowired
	private GjtExamBatchNewService gjtExamBatchNewService;

	@Autowired
	private ViewStudentInfoDao viewStudentInfoDao;
	@Autowired
	private GjtExamWkService gjtExamWkService;

	/**
	 * 
	 * @param studentId
	 *            学员ＩＤ
	 * @param chooseId
	 *            选课ＩＤ
	 * @param teachPlanId
	 *            教学计划ＩＤ
	 */
	@RequestMapping(value = "synWXInfo", method = RequestMethod.GET)
	@ResponseBody
	public Feedback synWXInfo(String studentId, String chooseId, String teachPlanId) {
		try {
			GjtExamAppointmentNew appointmentNew = gjtExamAppointmentNewService.queryGjtExamAppointmentNew(studentId,
					teachPlanId);
			if (appointmentNew == null) {
				return new Feedback(false, "该课程未进行预约");
			}
			GjtExamPlanNew examPlanNew = appointmentNew.getExamPlanNew();
			GjtExamSubjectNew examSubjectNew = examPlanNew.getExamSubjectNew();
			String examNo = examSubjectNew.getExamNo();
			String xxId = examSubjectNew.getXxId();

			// 当前学年度
			GjtStudyYearInfo gjtStudyYearInfo = gjtExamAppointmentNewService.getCurrentStudyYear(xxId);
			if (gjtStudyYearInfo == null) {
				return new Feedback(false, "院校当前学年度未找到;xxId:" + xxId);
			}
			GjtExamBatchNew examBatchNew = gjtExamBatchNewService
					.findByStudyYearIdAndXxIdAndIsDeleted(gjtStudyYearInfo.getId(), xxId);
			if (examBatchNew == null) {
				return new Feedback(false, "批次未找到;GjtStudyYearid:", gjtStudyYearInfo.getId());
			}
			ViewStudentInfo studentInfo = viewStudentInfoDao.findOne(studentId);

			gjtExamWkService.initWk(examNo, examBatchNew.getExamBatchCode(), studentInfo.getStudentId(),
					studentInfo.getXm(), chooseId);
		} catch (RuntimeException e) {
			return new Feedback(false, "错误,同步考试平台失败：" + e.getMessage());
		} catch (Exception e) {
			return new Feedback(false, "异常,同步考试平台失败：" + e.getMessage());
		}
		return new Feedback(true, "成功");
	}

}
