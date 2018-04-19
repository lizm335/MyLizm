package com.ouchgzee.study.web.controller.educational;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.constants.BusinessCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.BeanConvertHandler;
import com.gzedu.xlims.common.gzdec.framework.util.BeanConvertUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApply;
import com.gzedu.xlims.pojo.practice.GjtPracticeApply;
import com.gzedu.xlims.pojo.practice.GjtPracticeGuideRecord;
import com.gzedu.xlims.pojo.practice.GjtPracticePlan;
import com.gzedu.xlims.pojo.practice.GjtPracticeStudentProg;
import com.gzedu.xlims.pojo.status.PracticeProgressCodeEnum;
import com.gzedu.xlims.pojo.status.PracticeStatusEnum;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.practice.GjtPracticeApplyService;
import com.gzedu.xlims.service.practice.GjtPracticeGuideRecordService;
import com.gzedu.xlims.service.practice.GjtPracticePlanService;
import com.gzedu.xlims.service.practice.GjtPracticeStudentProgService;
import com.ouchgzee.study.web.vo.practice.PracticeApplyVO;
import com.ouchgzee.study.web.vo.practice.PracticeGuideRecordVO;
import com.ouchgzee.study.web.vo.practice.PracticePlanVO;

/**
 * 社会实践写作
 * 
 * @author lzj
 *
 */
@Controller
@RequestMapping("/pcenter/practice")
public class PracticeController {

	private final static Logger log = LoggerFactory.getLogger(PracticeController.class);

	private final static int STATUS_COMPLETED = 5;

	@Autowired
	private GjtPracticePlanService gjtPracticePlanService;

	@Autowired
	private GjtPracticeApplyService gjtPracticeApplyService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtGraduationApplyService gjtGraduationApplyService;

	@Autowired
	private GjtPracticeStudentProgService gjtPracticeStudentProgService;

	@Autowired
	private GjtPracticeGuideRecordService gjtPracticeGuideRecordService;

	private int getPlanProgress(int status) {
		if (status == PracticeStatusEnum.PRACTICE_APPLY.getValue()) {
			return 0;
		} else if (status == PracticeStatusEnum.PRACTICE_STAY_OPEN.getValue()) {
			return 1;
		} else if (status == PracticeStatusEnum.PRACTICE_SUBMIT_PRACTICE.getValue()) {
			return 2;
		} else if (status == PracticeStatusEnum.PRACTICE_CONFIRM_PRACTICE.getValue()) {
			return 3;
		} else if (status == PracticeStatusEnum.PRACTICE_SEND.getValue()) {
			return 4;
		} else if (status == PracticeStatusEnum.PRACTICE_COMPLETED.getValue()) {
			return STATUS_COMPLETED;
		}

		return 0;
	}

	/**
	 * 查询当前实践计划
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "queryCurrentPlan")
	public PracticePlanVO queryCurrentPlan(HttpServletRequest request, HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtGrade grade = gjtGradeService.findCurrentGrade(student.getXxId());
		List<GjtPracticeApply> list = gjtPracticeApplyService.findPracticePlanIdByStudentId(student.getStudentId(),
				grade.getGradeId());
		PracticePlanVO practicePlanVO = null;

		if (EmptyUtils.isNotEmpty(list)) {// 申请过的人就算过了申请时间还是能走流程
			GjtPracticeApply practiceApply = list.get(0);
			GjtPracticePlan practicePlan = practiceApply.getGjtPracticePlan();
			// 先查询学生的课程是否已经通过
			Float score = gjtPracticeApplyService.getScore(practicePlan.getOrgId(), grade.getGradeId(),
					student.getGradeSpecialtyId(), student.getStudentId());
			practicePlanVO = BeanConvertUtils.convert(practicePlan, PracticePlanVO.class);
			if (score >= 60) {
				practicePlanVO.setStatus(STATUS_COMPLETED);
			} else {
				practicePlanVO.setStatus(this.getPlanProgress(practiceApply.getStatus()));
			}
		} else {
			GjtPracticePlan practicePlan = gjtPracticePlanService.findByGradeIdAndOrgIdAndStatus(grade.getGradeId(),
					student.getXxId(), 3);
			if (practicePlan == null) {
				throw new CommonException(BusinessCode.STUDY_PRACTICE_PLAN_NOT_FOUND);
			}
			log.info(practicePlan.toString());
			practicePlanVO = BeanConvertUtils.convert(practicePlan, PracticePlanVO.class);
			// 先查询学生的课程是否已经通过
			Float score = gjtPracticeApplyService.getScore(practicePlan.getOrgId(), grade.getGradeId(),
					student.getGradeSpecialtyId(), student.getStudentId());
			if (score >= 60) {
				practicePlanVO.setStatus(STATUS_COMPLETED);
			} else {
				// 再查询之前是否有通过记录
				GjtGraduationApply apply = gjtGraduationApplyService
						.findByStudentIdAndApplyTypeAndStatusGreaterThanEqual(student.getStudentId(), 2,
								PracticeStatusEnum.PRACTICE_CONFIRM_PRACTICE.getValue());
				if (apply != null) {
					practicePlanVO.setStatus(STATUS_COMPLETED);
				} else {
					GjtPracticeApply apply2 = gjtPracticeApplyService.findCompletedApply(student.getStudentId());
					if (apply2 != null) {
						practicePlanVO.setStatus(STATUS_COMPLETED);
					} else {
						// 查询当前是否有申请
						GjtPracticeApply practiceApply = gjtPracticeApplyService.findByPracticePlanIdAndStudentId(
								practicePlanVO.getPracticePlanId(), student.getStudentId());
						if (practiceApply != null) {
							practicePlanVO.setStatus(this.getPlanProgress(practiceApply.getStatus()));
						} else {
							practicePlanVO.setStatus(0);
						}
					}
				}
			}
		}
		practicePlanVO.setSystemDt(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return practicePlanVO;
	}

	/**
	 * 查询实践申请
	 * 
	 * @param practicePlanId
	 * @param request
	 * @param session
	 * @return
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "queryPracticeApply")
	public PracticeApplyVO queryPracticeApply(@RequestParam String practicePlanId, HttpServletRequest request,
			HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtGrade grade = gjtGradeService.findCurrentGrade(student.getXxId());
		PracticeApplyVO practiceApplyVO = null;

		// 先查询学生的课程是否已经通过
		Float score = gjtPracticeApplyService.getScore(student.getXxId(), grade.getGradeId(),
				student.getGradeSpecialtyId(), student.getStudentId());
		if (score > 0) {
			practiceApplyVO = new PracticeApplyVO();
			practiceApplyVO.setScore(score);
			practiceApplyVO.setStatus(PracticeStatusEnum.PRACTICE_COMPLETED.getValue());
		} else {
			// 再查询之前是否有通过记录
			GjtGraduationApply apply = gjtGraduationApplyService.findByStudentIdAndApplyTypeAndStatusGreaterThanEqual(
					student.getStudentId(), 2, PracticeStatusEnum.PRACTICE_CONFIRM_PRACTICE.getValue());
			if (apply != null) {
				practiceApplyVO = BeanConvertUtils.convert(apply, PracticeApplyVO.class);
				if (practiceApplyVO.getStatus() != PracticeStatusEnum.PRACTICE_APPLY.getValue()
						&& apply.getGuideTeacher1() != null) {
					practiceApplyVO.setGuideTeacherId(apply.getGuideTeacher1().getEmployeeId());
					practiceApplyVO.setGuideTeacherName(apply.getGuideTeacher1().getXm());
					practiceApplyVO.setGuideTeacherZp(apply.getGuideTeacher1().getZp());
				}
				// 旧的社会实践写作只到定稿步骤，不一定有成绩
				// 突然说，定稿的时候不打分，有些学员就变成了没分了，然后又说要按照gjt_result表的score2分为准，所以这里都返回0
				// if (apply.getReviewScore() >= 60) {
				// practiceApplyVO.setScore(apply.getReviewScore());
				// } else {
				// practiceApplyVO.setScore(60f);
				// }
				practiceApplyVO.setScore(0f);
				practiceApplyVO.setStatus(PracticeStatusEnum.PRACTICE_COMPLETED.getValue());
			} else {
				GjtPracticeApply apply2 = gjtPracticeApplyService.findCompletedApply(student.getStudentId());
				if (apply2 != null) {
					practiceApplyVO = BeanConvertUtils.convert(apply2, PracticeApplyVO.class);
					if (practiceApplyVO.getStatus() != PracticeStatusEnum.PRACTICE_APPLY.getValue()
							&& apply2.getGuideTeacher1() != null) {
						practiceApplyVO.setGuideTeacherId(apply2.getGuideTeacher1().getEmployeeId());
						practiceApplyVO.setGuideTeacherName(apply2.getGuideTeacher1().getXm());
						practiceApplyVO.setGuideTeacherZp(apply2.getGuideTeacher1().getZp());
					}
					// practiceApplyVO.setScore(apply2.getReviewScore());
					practiceApplyVO.setScore(0f);
					practiceApplyVO.setStatus(PracticeStatusEnum.PRACTICE_COMPLETED.getValue());
				} else {
					// 查询当前是否有申请
					GjtPracticeApply practiceApply = gjtPracticeApplyService
							.findByPracticePlanIdAndStudentId(practicePlanId, student.getStudentId());
					if (practiceApply == null) {
						throw new CommonException(BusinessCode.STUDY_PRACTICE_APPLY_NOT_FOUND);
					}

					practiceApplyVO = BeanConvertUtils.convert(practiceApply, PracticeApplyVO.class);
					if (practiceApplyVO.getStatus() != PracticeStatusEnum.PRACTICE_APPLY.getValue()
							&& practiceApply.getGuideTeacher1() != null) {
						practiceApplyVO.setGuideTeacherId(practiceApply.getGuideTeacher1().getEmployeeId());
						practiceApplyVO.setGuideTeacherName(practiceApply.getGuideTeacher1().getXm());
						practiceApplyVO.setGuideTeacherZp(practiceApply.getGuideTeacher1().getZp());
					}
					if (practiceApplyVO.getStatus() == PracticeStatusEnum.PRACTICE_COMPLETED.getValue()) {
						practiceApplyVO.setScore(0f);
					}
				}
			}
		}

		return practiceApplyVO;
	}

	/**
	 * 申请实践写作
	 * 
	 * @param practicePlanId
	 * @param session
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "apply")
	public PracticeApplyVO apply(@RequestParam String practicePlanId, HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtPracticePlan practicePlan = gjtPracticePlanService.findOne(practicePlanId);
		if (practicePlan == null) {
			throw new CommonException(BusinessCode.STUDY_PRACTICE_APPLY_PRACTICE_NOT_FOUND);
		}

		if (practicePlan.getApplyBeginDt().compareTo(new Date()) > 0) {
			throw new CommonException(BusinessCode.STUDY_PRACTICE_APPLY_NOT_IN_TIME);
		}

		GjtPracticeApply practiceApply = gjtPracticeApplyService.findByPracticePlanIdAndStudentId(practicePlanId,
				student.getStudentId());
		if (practiceApply != null) {
			throw new CommonException(BusinessCode.STUDY_PRACTICE_APPLY_HAD_APPLY);
		}

		GjtGrade grade = gjtGradeService.findCurrentGrade(student.getXxId());
		boolean canApply = gjtPracticeApplyService.getCanApply(practicePlan.getOrgId(), grade.getGradeId(),
				student.getGradeSpecialtyId(), student.getStudentId());
		if (!canApply) {
			throw new CommonException(BusinessCode.STUDY_PRACTICE_APPLY_COURSE_NOT_FOUND);
		}

		GjtPracticeApply newApply = new GjtPracticeApply();
		newApply.setStudentId(student.getStudentId());
		newApply.setPracticePlanId(practicePlanId);
		newApply.setReviewScore(0f);
		newApply.setCreatedBy(student.getGjtUserAccount().getId());

		gjtPracticeApplyService.insert(newApply);

		PracticeApplyVO practiceApplyVO = BeanConvertUtils.convert(newApply, PracticeApplyVO.class);

		return practiceApplyVO;
	}

	/**
	 * 取消申请实践写作
	 * 
	 * @param applyId
	 * @param session
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "cancelApply")
	public void cancelApply(@RequestParam String applyId, HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtPracticeApply practiceApply = gjtPracticeApplyService.findOne(applyId);
		if (practiceApply == null) {
			throw new CommonException(BusinessCode.STUDY_PRACTICE_CANCEL_APPLY_NOT_FOUND);
		}

		if ("Y".equals(practiceApply.getIsDeleted())) {
			throw new CommonException(BusinessCode.STUDY_PRACTICE_CANCEL_APPLY_NOT_FOUND);
		}

		if (!practiceApply.getStudentId().equals(student.getStudentId())) {
			throw new CommonException(BusinessCode.STUDY_PRACTICE_CANCEL_APPLY_NOT_FOUND);
		}

		practiceApply.setIsDeleted("Y");
		practiceApply.setDeletedBy(student.getGjtUserAccount().getId());
		practiceApply.setDeletedDt(new Date());
		gjtPracticeApplyService.update(practiceApply);

		gjtPracticeStudentProgService.deleteByPracticePlanIdAndStudentId(practiceApply.getPracticePlanId(),
				student.getStudentId());
	}

	/**
	 * 提交初稿
	 * 
	 * @param applyId
	 * @param recordId
	 * @param content
	 * @param attachment
	 * @param attachmentName
	 * @param session
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "submitPractice")
	public void submitPractice(@RequestParam String applyId, String recordId, String content, String attachment,
			String attachmentName, HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtPracticeApply practiceApply = gjtPracticeApplyService.findOne(applyId);
		if (practiceApply == null) {
			throw new CommonException(BusinessCode.STUDY_PRACTICE_SUBMIT_PRACTICE_APPLY_NOT_FOUND);
		}

		if (recordId != null && !"".equals(recordId)) {
			// 修改社会实践指导记录
			GjtPracticeGuideRecord guideRecord = gjtPracticeGuideRecordService.findOne(recordId);
			guideRecord.setAttachment(attachment);
			guideRecord.setAttachmentName(attachmentName);
			guideRecord.setContent(content);
			guideRecord.setUpdatedBy(student.getGjtUserAccount().getId());
			gjtPracticeGuideRecordService.update(guideRecord);
		} else {
			// 新增社会实践指导记录
			GjtPracticeGuideRecord guideRecord = new GjtPracticeGuideRecord();
			guideRecord.setPracticePlanId(practiceApply.getPracticePlanId());
			guideRecord.setStudentId(student.getStudentId());
			guideRecord.setTeacherId(practiceApply.getGuideTeacher());
			guideRecord.setAttachment(attachment);
			guideRecord.setAttachmentName(attachmentName);
			guideRecord.setContent(content);
			guideRecord.setIsStudent(1);
			guideRecord.setProgressCode(PracticeProgressCodeEnum.PRACTICE_SUBMIT_PRACTICE.getCode());
			guideRecord.setCreatedBy(student.getGjtUserAccount().getId());
			gjtPracticeGuideRecordService.insert(guideRecord);

			if (StringUtils.isNotBlank(attachment)) {// 只是评论，不是提交，不需要更改状态
				if (practiceApply.getStatus() == PracticeStatusEnum.PRACTICE_STAY_OPEN.getValue()) {
					// 新增学员个人进度
					GjtPracticeStudentProg prog = new GjtPracticeStudentProg();
					prog.setPracticePlanId(practiceApply.getPracticePlanId());
					prog.setStudentId(student.getStudentId());
					prog.setProgressCode(PracticeProgressCodeEnum.PRACTICE_SUBMIT_PRACTICE.getCode());
					prog.setCreatedBy(student.getGjtUserAccount().getId());
					gjtPracticeStudentProgService.insert(prog);

					// 更新论文申请状态
					practiceApply.setStatus(PracticeStatusEnum.PRACTICE_SUBMIT_PRACTICE.getValue());
					practiceApply.setUpdatedBy(student.getGjtUserAccount().getId());
					gjtPracticeApplyService.update(practiceApply);
				}
			}
		}

	}

	/**
	 * 查询指导记录
	 * 
	 * @param applyId
	 * @param session
	 * @return
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "queryGuideRecord")
	public List<PracticeGuideRecordVO> queryGuideRecord(@RequestParam String applyId, HttpSession session)
			throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtPracticeApply practiceApply = gjtPracticeApplyService.findOne(applyId);
		if (practiceApply == null) {
			throw new CommonException(BusinessCode.STUDY_PRACTICE_QUERY_GUIDERECORD_APPLY_NOT_FOUND);
		}

		final DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

		List<GjtPracticeGuideRecord> guideRecords = gjtPracticeGuideRecordService
				.findByPracticePlanIdAndStudentId(practiceApply.getPracticePlanId(), student.getStudentId(), "Desc");

		List<GjtPracticeGuideRecord> removeList = new ArrayList<GjtPracticeGuideRecord>();
		// 因为管理后台有些操作也会记录在此，这些记录的老师id是为空的，所以这里需要过滤掉
		for (GjtPracticeGuideRecord guideRecord : guideRecords) {
			if (guideRecord.getTeacherId() == null) {
				removeList.add(guideRecord);
			}
		}
		guideRecords.removeAll(removeList);

		List<PracticeGuideRecordVO> list = BeanConvertUtils.convertList(guideRecords, PracticeGuideRecordVO.class,
				new BeanConvertHandler<GjtPracticeGuideRecord, PracticeGuideRecordVO>() {

					@Override
					public void handle(GjtPracticeGuideRecord soruce, PracticeGuideRecordVO target) {
						target.setStudentName(soruce.getGjtStudentInfo().getXm());
						target.setStudentZp(soruce.getGjtStudentInfo().getAvatar());
						target.setTeacherName(soruce.getTeacher().getXm());
						target.setTeacherZp(soruce.getTeacher().getZp());
						target.setDate(df.format(soruce.getCreatedDt()));
					}

				});

		return list;
	}

	/**
	 * 提交快递信息
	 * 
	 * @param applyId
	 * @param expressCompany
	 * @param expressNumber
	 * @param session
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "submitExpress")
	public void submitExpress(@RequestParam String applyId, @RequestParam String expressCompany,
			@RequestParam String expressNumber, HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtPracticeApply practiceApply = gjtPracticeApplyService.findOne(applyId);
		if (practiceApply == null) {
			throw new CommonException(BusinessCode.STUDY_PRACTICE_SUBMIT_EXPRESS_APPLY_NOT_FOUND);
		}

		if (practiceApply.getExpressNumber() == null) { // 快递单号为空表示第一次提交
			// 新增学员个人进度
			GjtPracticeStudentProg prog = new GjtPracticeStudentProg();
			prog.setPracticePlanId(practiceApply.getPracticePlanId());
			prog.setStudentId(student.getStudentId());
			prog.setProgressCode(PracticeProgressCodeEnum.PRACTICE_SEND.getCode());
			prog.setCreatedBy(student.getGjtUserAccount().getId());
			gjtPracticeStudentProgService.insert(prog);
		}

		practiceApply.setExpressCompany(expressCompany);
		practiceApply.setExpressNumber(expressNumber);
		practiceApply.setUpdatedBy(student.getGjtUserAccount().getId());

		gjtPracticeApplyService.update(practiceApply);

	}

	/**
	 * 寄送定稿
	 * 
	 * @param applyId
	 * @param session
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "sendPractice")
	public void sendPractice(@RequestParam String applyId, HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtPracticeApply practiceApply = gjtPracticeApplyService.findOne(applyId);
		if (practiceApply == null) {
			throw new CommonException(BusinessCode.STUDY_PRACTICE_SEND_PRACTICE_APPLY_NOT_FOUND);
		}

		if (practiceApply.getStatus() == PracticeStatusEnum.PRACTICE_CONFIRM_PRACTICE.getValue()) {
			practiceApply.setStatus(PracticeStatusEnum.PRACTICE_SEND.getValue());
			practiceApply.setUpdatedBy(student.getGjtUserAccount().getId());
			gjtPracticeApplyService.update(practiceApply);
		}

	}

}
