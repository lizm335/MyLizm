package com.ouchgzee.study.web.controller.educational;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.constants.BusinessCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.BeanConvertHandler;
import com.gzedu.xlims.common.gzdec.framework.util.BeanConvertUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApply;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.pojo.status.ExpressEnum;
import com.gzedu.xlims.pojo.status.ThesisProgressCodeEnum;
import com.gzedu.xlims.pojo.status.ThesisStatusEnum;
import com.gzedu.xlims.pojo.thesis.GjtThesisApply;
import com.gzedu.xlims.pojo.thesis.GjtThesisArrange;
import com.gzedu.xlims.pojo.thesis.GjtThesisDefencePlan;
import com.gzedu.xlims.pojo.thesis.GjtThesisGuideRecord;
import com.gzedu.xlims.pojo.thesis.GjtThesisPlan;
import com.gzedu.xlims.pojo.thesis.GjtThesisStudentProg;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.thesis.GjtThesisApplyService;
import com.gzedu.xlims.service.thesis.GjtThesisArrangeService;
import com.gzedu.xlims.service.thesis.GjtThesisGuideRecordService;
import com.gzedu.xlims.service.thesis.GjtThesisPlanService;
import com.gzedu.xlims.service.thesis.GjtThesisStudentProgService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.ouchgzee.study.web.vo.thesis.ThesisApplyVO;
import com.ouchgzee.study.web.vo.thesis.ThesisArrangeVO;
import com.ouchgzee.study.web.vo.thesis.ThesisDefencePlanVO;
import com.ouchgzee.study.web.vo.thesis.ThesisGuideRecordVO;
import com.ouchgzee.study.web.vo.thesis.ThesisPlanVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 论文写作
 * 
 * @author lzj
 *
 */
@Controller
@RequestMapping("/pcenter/thesis")
public class ThesisController {

	private final static Logger log = LoggerFactory.getLogger(ThesisController.class);

	private final static int STATUS_COMPLETED = 8;

	@Autowired
	private GjtThesisPlanService gjtThesisPlanService;

	@Autowired
	private GjtThesisArrangeService gjtThesisArrangeService;

	@Autowired
	private GjtThesisApplyService gjtThesisApplyService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtGraduationApplyService gjtGraduationApplyService;

	@Autowired
	private GjtThesisStudentProgService gjtThesisStudentProgService;

	@Autowired
	private GjtThesisGuideRecordService gjtThesisGuideRecordService;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;

	private int getPlanProgress(int status) {
		if (status == ThesisStatusEnum.THESIS_APPLY.getValue()) {
			return 0;
		} else if (status == ThesisStatusEnum.THESIS_STAY_OPEN.getValue()) {
			return 1;
		} else if (status == ThesisStatusEnum.THESIS_SUBMIT_PROPOSE.getValue()) {
			return 2;
		} else if (status == ThesisStatusEnum.THESIS_CONFIRM_PROPOSE.getValue()) {
			return 3;
		} else if (status == ThesisStatusEnum.THESIS_SUBMIT_THESIS.getValue()
				|| status == ThesisStatusEnum.THESIS_TEACHER_CONFIRM_THESIS.getValue()) {
			return 4;
		} else if (status == ThesisStatusEnum.THESIS_COLLEGE_CONFIRM_THESIS.getValue()
				|| status == ThesisStatusEnum.THESIS_REVIEW_FAILED.getValue()) {
			return 5;
		} else if (status == ThesisStatusEnum.THESIS_SEND.getValue()) {
			return 6;
		} else if (status == ThesisStatusEnum.THESIS_STAY_DEFENCE.getValue()
				|| status == ThesisStatusEnum.THESIS_DEFENCE.getValue()) {
			return 7;
		} else if (status == ThesisStatusEnum.THESIS_DEFENCE_FAILED.getValue()
				|| status == ThesisStatusEnum.THESIS_COMPLETED.getValue()) {
			return STATUS_COMPLETED;
		}

		return 0;
	}

	/**
	 * 查询当前论文计划
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "queryCurrentPlan")
	public ThesisPlanVO queryCurrentPlan(HttpServletRequest request, HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtGrade grade = gjtGradeService.findCurrentGrade(student.getXxId());
		List<GjtThesisApply> list = gjtThesisApplyService.findIsApplyByStudentId(student.getStudentId(),
				grade.getGradeId());
		ThesisPlanVO thesisPlanVO = null;
		if (EmptyUtils.isNotEmpty(list)) {// 申请过的人就算过了申请时间还是能走流程
			GjtThesisApply thesisApply = list.get(0);
			GjtThesisPlan thesisPlan = thesisApply.getGjtThesisPlan();
			// 先查询学生的课程是否已经通过
			thesisPlanVO = BeanConvertUtils.convert(thesisPlan, ThesisPlanVO.class);
			Float score = gjtThesisApplyService.getScore(thesisPlan.getOrgId(), grade.getGradeId(),
					student.getGradeSpecialtyId(), student.getStudentId());
			if (score >= 60) {
				thesisPlanVO.setStatus(STATUS_COMPLETED);
			} else {
				thesisPlanVO.setStatus(this.getPlanProgress(thesisApply.getStatus()));
			}
		} else {
			GjtThesisPlan thesisPlan = gjtThesisPlanService.findByGradeIdAndOrgIdAndStatus(grade.getGradeId(),
					student.getXxId(), 3);
			if (thesisPlan == null) {
				throw new CommonException(BusinessCode.STUDY_THESIS_PLAN_NOT_FOUND);
			}
			log.info(thesisPlan.toString());
			thesisPlanVO = BeanConvertUtils.convert(thesisPlan, ThesisPlanVO.class);
			// 先查询学生的课程是否已经通过
			Float score = gjtThesisApplyService.getScore(thesisPlan.getOrgId(), grade.getGradeId(),
					student.getGradeSpecialtyId(), student.getStudentId());
			if (score >= 60) {
				thesisPlanVO.setStatus(STATUS_COMPLETED);
			} else {
				// 再查询之前是否有通过记录
				GjtGraduationApply apply = gjtGraduationApplyService
						.findByStudentIdAndApplyTypeAndStatusGreaterThanEqual(student.getStudentId(), 1,
								ThesisStatusEnum.THESIS_COLLEGE_CONFIRM_THESIS.getValue());
				if (apply != null) {
					thesisPlanVO.setStatus(STATUS_COMPLETED);
				} else {
					GjtThesisApply apply2 = gjtThesisApplyService.findCompletedApply(student.getStudentId());
					if (apply2 != null) {
						thesisPlanVO.setStatus(STATUS_COMPLETED);
					} else {
						// 查询当前是否有申请
						GjtThesisApply thesisApply = gjtThesisApplyService
								.findByThesisPlanIdAndStudentId(thesisPlanVO.getThesisPlanId(), student.getStudentId());
						if (thesisApply != null) {
							thesisPlanVO.setStatus(this.getPlanProgress(thesisApply.getStatus()));
						} else {
							thesisPlanVO.setStatus(0);
						}
					}
				}
			}
		}
		thesisPlanVO.setSystemDt(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		return thesisPlanVO;
	}

	/**
	 * 查询论文申请
	 * 
	 * @param thesisPlanId
	 * @param request
	 * @param session
	 * @return
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "queryThesisApply")
	public ThesisApplyVO queryThesisApply(@RequestParam String thesisPlanId, HttpServletRequest request,
			HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtGrade grade = gjtGradeService.findCurrentGrade(student.getXxId());
		ThesisApplyVO thesisApplyVO = null;

		// 先查询学生的课程是否已经通过
		Float score = gjtThesisApplyService.getScore(student.getXxId(), grade.getGradeId(),
				student.getGradeSpecialtyId(), student.getStudentId());
		if (score > 0) {
			thesisApplyVO = new ThesisApplyVO();
			thesisApplyVO.setScore(score);
			thesisApplyVO.setStatus(ThesisStatusEnum.THESIS_COMPLETED.getValue());
		} else {
			// 再查询之前是否有通过记录
			GjtGraduationApply apply = gjtGraduationApplyService.findByStudentIdAndApplyTypeAndStatusGreaterThanEqual(
					student.getStudentId(), 1, ThesisStatusEnum.THESIS_COLLEGE_CONFIRM_THESIS.getValue());
			if (apply != null) {
				thesisApplyVO = BeanConvertUtils.convert(apply, ThesisApplyVO.class);
				if (thesisApplyVO.getStatus() != ThesisStatusEnum.THESIS_APPLY.getValue()
						&& apply.getGuideTeacher1() != null) {
					thesisApplyVO.setGuideTeacherId(apply.getGuideTeacher1().getEmployeeId());
					thesisApplyVO.setGuideTeacherName(apply.getGuideTeacher1().getXm());
					thesisApplyVO.setGuideTeacherZp(apply.getGuideTeacher1().getZp());
				}
				// 旧的论文写作只到定稿步骤，不一定有成绩
				// if (apply.getReviewScore() >= 60) {
				// thesisApplyVO.setScore(apply.getReviewScore());
				// } else {
				// thesisApplyVO.setScore(60f);
				// }
				thesisApplyVO.setScore(0f);
				thesisApplyVO.setStatus(ThesisStatusEnum.THESIS_COMPLETED.getValue());
			} else {
				GjtThesisApply apply2 = gjtThesisApplyService.findCompletedApply(student.getStudentId());
				if (apply2 != null) {
					thesisApplyVO = BeanConvertUtils.convert(apply2, ThesisApplyVO.class);
					if (thesisApplyVO.getStatus() != ThesisStatusEnum.THESIS_APPLY.getValue()
							&& apply2.getGuideTeacher1() != null) {
						thesisApplyVO.setGuideTeacherId(apply2.getGuideTeacher1().getEmployeeId());
						thesisApplyVO.setGuideTeacherName(apply2.getGuideTeacher1().getXm());
						thesisApplyVO.setGuideTeacherZp(apply2.getGuideTeacher1().getZp());
					}
					// 本科取答辩成绩，专科取初评成绩
					// 突然说，定稿的时候不打分，有些学员就变成了没分了，然后又说要按照gjt_result表的score2分为准，所以这里都返回0
					// if ("2".equals(student.getPycc())) {
					// thesisApplyVO.setScore(apply2.getDefenceScore());
					// } else {
					// thesisApplyVO.setScore(apply2.getReviewScore());
					// }
					thesisApplyVO.setScore(0f);
					thesisApplyVO.setStatus(ThesisStatusEnum.THESIS_COMPLETED.getValue());
				} else {
					// 查询当前是否有申请
					GjtThesisApply thesisApply = gjtThesisApplyService.findByThesisPlanIdAndStudentId(thesisPlanId,
							student.getStudentId());
					if (thesisApply == null) {
						throw new CommonException(BusinessCode.STUDY_THESIS_APPLY_NOT_FOUND);
					}

					thesisApplyVO = BeanConvertUtils.convert(thesisApply, ThesisApplyVO.class);
					if (thesisApplyVO.getStatus() != ThesisStatusEnum.THESIS_APPLY.getValue()
							&& thesisApply.getGuideTeacher1() != null) {
						thesisApplyVO.setGuideTeacherId(thesisApply.getGuideTeacher1().getEmployeeId());
						thesisApplyVO.setGuideTeacherName(thesisApply.getGuideTeacher1().getXm());
						thesisApplyVO.setGuideTeacherZp(thesisApply.getGuideTeacher1().getZp());
					}
					if (thesisApplyVO.getStatus() > ThesisStatusEnum.THESIS_DEFENCE.getValue()) {
						// 本科取答辩成绩，专科取初评成绩
						// if ("2".equals(student.getPycc())) {
						// thesisApplyVO.setScore(thesisApply.getDefenceScore());
						// } else {
						// thesisApplyVO.setScore(thesisApply.getReviewScore());
						// }
						thesisApplyVO.setScore(0f);
					}
				}

			}
		}

		return thesisApplyVO;
	}

	/**
	 * 申请论文写作
	 * 
	 * @param thesisPlanId
	 * @param applyDegree
	 * @param session
	 * @return
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "apply")
	public ThesisApplyVO apply(@RequestParam String thesisPlanId, @RequestParam int applyDegree, HttpSession session)
			throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtThesisPlan thesisPlan = gjtThesisPlanService.findOne(thesisPlanId);
		if (thesisPlan == null) {
			throw new CommonException(BusinessCode.STUDY_THESIS_APPLY_THESIS_NOT_FOUND);
		}

		if (thesisPlan.getApplyBeginDt().compareTo(new Date()) > 0) {
			throw new CommonException(BusinessCode.STUDY_THESIS_APPLY_NOT_IN_TIME);
		}

		GjtThesisApply thesisApply = gjtThesisApplyService.findByThesisPlanIdAndStudentId(thesisPlanId,
				student.getStudentId());
		if (thesisApply != null) {
			throw new CommonException(BusinessCode.STUDY_THESIS_APPLY_HAD_APPLY);
		}

		GjtGrade grade = gjtGradeService.findCurrentGrade(student.getXxId());
		boolean canApply = gjtThesisApplyService.getCanApply(thesisPlan.getOrgId(), grade.getGradeId(),
				student.getGradeSpecialtyId(), student.getStudentId());
		if (!canApply) {
			throw new CommonException(BusinessCode.STUDY_THESIS_APPLY_COURSE_NOT_FOUND);
		}

		GjtThesisApply newApply = new GjtThesisApply();
		newApply.setStudentId(student.getStudentId());
		newApply.setThesisPlanId(thesisPlanId);
		newApply.setApplyDegree(applyDegree);
		newApply.setReviewScore(0f);
		newApply.setDefenceScore(0f);
		newApply.setCreatedBy(student.getGjtUserAccount().getId());

		if ("2".equals(student.getGjtSpecialty().getPycc())) { // 本科需要答辩
			newApply.setNeedDefence(1);
		} else {
			newApply.setNeedDefence(0);
		}

		gjtThesisApplyService.insert(newApply);

		ThesisApplyVO thesisApplyVO = BeanConvertUtils.convert(newApply, ThesisApplyVO.class);

		return thesisApplyVO;
	}

	/**
	 * 取消申请论文写作
	 * 
	 * @param applyId
	 * @param session
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "cancelApply")
	public void cancelApply(@RequestParam String applyId, HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);
		if (thesisApply == null) {
			throw new CommonException(BusinessCode.STUDY_THESIS_CANCEL_APPLY_NOT_FOUND);
		}

		if ("Y".equals(thesisApply.getIsDeleted())) {
			throw new CommonException(BusinessCode.STUDY_THESIS_CANCEL_APPLY_NOT_FOUND);
		}

		if (!thesisApply.getStudentId().equals(student.getStudentId())) {
			throw new CommonException(BusinessCode.STUDY_THESIS_CANCEL_APPLY_NOT_FOUND);
		}

		thesisApply.setIsDeleted("Y");
		thesisApply.setDeletedBy(student.getGjtUserAccount().getId());
		thesisApply.setDeletedDt(new Date());
		gjtThesisApplyService.update(thesisApply);

		gjtThesisStudentProgService.deleteByThesisPlanIdAndStudentId(thesisApply.getThesisPlanId(),
				student.getStudentId());
	}

	/**
	 * 修改学位申请意向
	 * 
	 * @param applyId
	 * @param applyDegree
	 * @param session
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "updateApplyDegree")
	public void updateApplyDegree(@RequestParam String applyId, @RequestParam int applyDegree, HttpSession session)
			throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);
		if (thesisApply == null) {
			throw new CommonException(BusinessCode.STUDY_THESIS_UPDATE_DEGREE_APPLY_NOT_FOUND);
		}

		if (!thesisApply.getStudentId().equals(student.getStudentId())) {
			throw new CommonException(BusinessCode.STUDY_THESIS_UPDATE_DEGREE_APPLY_NOT_FOUND);
		}

		thesisApply.setApplyDegree(applyDegree);
		thesisApply.setUpdatedBy(student.getGjtUserAccount().getId());
		gjtThesisApplyService.update(thesisApply);
	}

	/**
	 * 查询开题报告示例
	 * 
	 * @param thesisPlanId
	 * @param request
	 * @param session
	 * @return
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "queryExample")
	public ThesisArrangeVO queryExample(@RequestParam String thesisPlanId, HttpServletRequest request,
			HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtThesisPlan thesisPlan = gjtThesisPlanService.findOne(thesisPlanId);
		if (thesisPlan == null) {
			throw new CommonException(BusinessCode.STUDY_THESIS_QUERY_EXAMPLE_THESIS_NOT_FOUND);
		}

		GjtThesisArrange thesisArrange = gjtThesisArrangeService.findByThesisPlanIdAndSpecialtyBaseId(thesisPlanId,
				student.getGjtSpecialty().getSpecialtyBaseId());
		ThesisArrangeVO thesisArrangeVO = BeanConvertUtils.convert(thesisArrange, ThesisArrangeVO.class);

		return thesisArrangeVO;
	}

	/**
	 * 提交开题报告
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
	@RequestMapping(value = "submitPropose")
	public void submitPropose(@RequestParam String applyId, String recordId, String content, String attachment,
			String attachmentName, HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);
		if (thesisApply == null) {
			throw new CommonException(BusinessCode.STUDY_THESIS_SUBMIT_PROPOSE_APPLY_NOT_FOUND);
		}

		// 本科才有此步骤
		/*
		 * if (!"2".equals(student.getGjtSpecialty().getPycc())) { throw new
		 * CommonException(BusinessCode.STUDY_THESIS_SUBMIT_PROPOSE_NO_OPERATION
		 * ); }
		 */

		if (recordId != null && !"".equals(recordId)) {
			// 修改论文指导记录
			GjtThesisGuideRecord guideRecord = gjtThesisGuideRecordService.findOne(recordId);
			guideRecord.setAttachment(attachment);
			guideRecord.setAttachmentName(attachmentName);
			guideRecord.setContent(content);
			guideRecord.setUpdatedBy(student.getGjtUserAccount().getId());
			gjtThesisGuideRecordService.update(guideRecord);
		} else {
			// 新增论文指导记录
			GjtThesisGuideRecord guideRecord = new GjtThesisGuideRecord();
			guideRecord.setThesisPlanId(thesisApply.getThesisPlanId());
			guideRecord.setStudentId(student.getStudentId());
			guideRecord.setTeacherId(thesisApply.getGuideTeacher());
			guideRecord.setAttachment(attachment);
			guideRecord.setAttachmentName(attachmentName);
			guideRecord.setContent(content);
			guideRecord.setIsStudent(1);
			guideRecord.setProgressCode(ThesisProgressCodeEnum.THESIS_SUBMIT_PROPOSE.getCode());
			guideRecord.setCreatedBy(student.getGjtUserAccount().getId());
			gjtThesisGuideRecordService.insert(guideRecord);

			if (StringUtils.isNotBlank(attachment)) {// 只是评论，不是提交，不需要更改状态
				if (thesisApply.getStatus() == ThesisStatusEnum.THESIS_STAY_OPEN.getValue()) {
					// 新增学员个人进度
					GjtThesisStudentProg prog = new GjtThesisStudentProg();
					prog.setThesisPlanId(thesisApply.getThesisPlanId());
					prog.setStudentId(student.getStudentId());
					prog.setProgressCode(ThesisProgressCodeEnum.THESIS_SUBMIT_PROPOSE.getCode());
					prog.setCreatedBy(student.getGjtUserAccount().getId());
					gjtThesisStudentProgService.insert(prog);

					// 更新论文申请状态
					thesisApply.setStatus(ThesisStatusEnum.THESIS_SUBMIT_PROPOSE.getValue());
					thesisApply.setUpdatedBy(student.getGjtUserAccount().getId());
					gjtThesisApplyService.update(thesisApply);
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
	public List<ThesisGuideRecordVO> queryGuideRecord(@RequestParam String applyId, HttpSession session)
			throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);
		if (thesisApply == null) {
			throw new CommonException(BusinessCode.STUDY_THESIS_QUERY_GUIDERECORD_APPLY_NOT_FOUND);
		}

		final DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

		List<GjtThesisGuideRecord> guideRecords = gjtThesisGuideRecordService
				.findByThesisPlanIdAndStudentId(thesisApply.getThesisPlanId(), student.getStudentId());

		List<GjtThesisGuideRecord> removeList = new ArrayList<GjtThesisGuideRecord>();
		// 因为管理后台有些操作也会记录在此，这些记录的老师id是为空的，所以这里需要过滤掉
		for (GjtThesisGuideRecord guideRecord : guideRecords) {
			if (guideRecord.getTeacherId() == null) {
				removeList.add(guideRecord);
			}
		}
		guideRecords.removeAll(removeList);

		List<ThesisGuideRecordVO> list = BeanConvertUtils.convertList(guideRecords, ThesisGuideRecordVO.class,
				new BeanConvertHandler<GjtThesisGuideRecord, ThesisGuideRecordVO>() {

					@Override
					public void handle(GjtThesisGuideRecord soruce, ThesisGuideRecordVO target) {
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
	@RequestMapping(value = "submitThesis")
	public void submitThesis(@RequestParam String applyId, String recordId, String content, String attachment,
			String attachmentName, HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		// String pycc = student.getGjtSpecialty().getPycc();
		GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);
		if (thesisApply == null) {
			throw new CommonException(BusinessCode.STUDY_THESIS_SUBMIT_THESIS_APPLY_NOT_FOUND);
		}

		if (recordId != null && !"".equals(recordId)) {
			// 修改论文指导记录
			GjtThesisGuideRecord guideRecord = gjtThesisGuideRecordService.findOne(recordId);
			guideRecord.setAttachment(attachment);
			guideRecord.setAttachmentName(attachmentName);
			guideRecord.setContent(content);
			guideRecord.setUpdatedBy(student.getGjtUserAccount().getId());
			gjtThesisGuideRecordService.update(guideRecord);
		} else {
			// 新增论文指导记录
			GjtThesisGuideRecord guideRecord = new GjtThesisGuideRecord();
			guideRecord.setThesisPlanId(thesisApply.getThesisPlanId());
			guideRecord.setStudentId(student.getStudentId());
			guideRecord.setTeacherId(thesisApply.getGuideTeacher());
			guideRecord.setAttachment(attachment);
			guideRecord.setAttachmentName(attachmentName);
			guideRecord.setContent(content);
			guideRecord.setIsStudent(1);
			guideRecord.setProgressCode(ThesisProgressCodeEnum.THESIS_SUBMIT_THESIS.getCode());
			guideRecord.setCreatedBy(student.getGjtUserAccount().getId());
			gjtThesisGuideRecordService.insert(guideRecord);

			/*
			 * if (("2".equals(pycc) && thesisApply.getStatus() ==
			 * ThesisStatusEnum.THESIS_CONFIRM_PROPOSE.getValue()) ||
			 * ("0".equals(pycc) && thesisApply.getStatus() ==
			 * ThesisStatusEnum.THESIS_STAY_OPEN.getValue())) {
			 */

			if (StringUtils.isNotBlank(attachment)) {// 只是评论，不是提交，不需要更改状态
				if (thesisApply.getStatus() == ThesisStatusEnum.THESIS_CONFIRM_PROPOSE.getValue()) {
					// 新增学员个人进度
					GjtThesisStudentProg prog = new GjtThesisStudentProg();
					prog.setThesisPlanId(thesisApply.getThesisPlanId());
					prog.setStudentId(student.getStudentId());
					prog.setProgressCode(ThesisProgressCodeEnum.THESIS_SUBMIT_THESIS.getCode());
					prog.setCreatedBy(student.getGjtUserAccount().getId());
					gjtThesisStudentProgService.insert(prog);

					// 更新论文申请状态
					thesisApply.setStatus(ThesisStatusEnum.THESIS_SUBMIT_THESIS.getValue());
					thesisApply.setUpdatedBy(student.getGjtUserAccount().getId());
					gjtThesisApplyService.update(thesisApply);
				}
			}
		}

	}

	/**
	 * 查询快递公司列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryExpress")
	public List<Map<String, String>> queryExpress() {
		return EnumUtil.getExpressList();
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
		GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);
		if (thesisApply == null) {
			throw new CommonException(BusinessCode.STUDY_THESIS_SUBMIT_EXPRESS_APPLY_NOT_FOUND);
		}

		if (thesisApply.getExpressNumber() == null) { // 快递单号为空表示第一次提交
			// 新增学员个人进度
			GjtThesisStudentProg prog = new GjtThesisStudentProg();
			prog.setThesisPlanId(thesisApply.getThesisPlanId());
			prog.setStudentId(student.getStudentId());
			prog.setProgressCode(ThesisProgressCodeEnum.THESIS_SEND.getCode());
			prog.setCreatedBy(student.getGjtUserAccount().getId());
			gjtThesisStudentProgService.insert(prog);
		}

		thesisApply.setExpressCompany(expressCompany);
		thesisApply.setExpressNumber(expressNumber);
		thesisApply.setUpdatedBy(student.getGjtUserAccount().getId());

		gjtThesisApplyService.update(thesisApply);

	}

	/**
	 * 查询物流信息
	 * 
	 * @param expressCompany
	 * @param expressNumber
	 * @return
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "queryLogistics")
	@SuppressWarnings("unchecked")
	public List<JSONObject> queryLogistics(@RequestParam String expressCompany, @RequestParam String expressNumber)
			throws CommonException, UnsupportedEncodingException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", expressCompany);
		params.put("postid", expressNumber);
		String s = HttpClientUtils.doHttpPost("https://www.kuaidi100.com/query", params, 10000, "utf-8");
		log.info("kuaidi100 result ======== " + URLEncoder.encode(s, Constants.CHARSET));
		JSONObject result = JSONObject.fromObject(s);
		List<JSONObject> list = new ArrayList<JSONObject>();
		if (result.get("status") != null && "200".equals(result.getString("status"))) {
			if (result != null && result.get("data") != null) {
				JSONArray arr = result.getJSONArray("data");
				for (int i = 0; i < arr.size(); i++) {
					JSONObject json = arr.getJSONObject(i);
					JSONObject obj = new JSONObject();
					obj.put("time", json.get("time"));
					obj.put("context", json.get("context"));
					list.add(obj);
				}
			}
		} else if (ExpressEnum.shunfeng.getValue().equals(expressCompany)) { // 由于快递100接口不支持顺丰快递查询
			JSONObject obj = new JSONObject();
			obj.put("context",
					"请前往 <a href=\"http://www.sf-express.com/cn/sc/dynamic_function/waybill/#search/bill-number/"
							+ expressNumber + "\" target=\"_blank\">顺丰快递官网</a> 查询");
			list.add(obj);
		} else {
			if (result.get("message") != null) {
				throw new CommonException(BusinessCode.STUDY_THESIS_QUERY_LOGISTICS_ERROR.getBusCode(),
						result.getString("message"));
			} else {
				throw new CommonException(BusinessCode.STUDY_THESIS_QUERY_LOGISTICS_ERROR);
			}
		}

		return list;
	}

	/**
	 * 查询答辩安排
	 * 
	 * @param applyId
	 * @param session
	 * @return
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "queryDefencePlan")
	public ThesisDefencePlanVO queryDefencePlan(@RequestParam String applyId, HttpSession session)
			throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		final GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);
		if (thesisApply == null) {
			throw new CommonException(BusinessCode.STUDY_THESIS_QUERY_DEFENCEPLAN_APPLY_NOT_FOUND);
		}

		GjtThesisDefencePlan thesisDefencePlan = thesisApply.getGjtThesisDefencePlan();
		if (thesisDefencePlan == null) {
			throw new CommonException(BusinessCode.STUDY_THESIS_QUERY_DEFENCEPLAN_NO_PLAN);
		}

		final List<GjtEmployeeInfo> defenceTeachers = gjtEmployeeInfoService
				.findListByType(EmployeeTypeEnum.论文教师.getNum(), EmployeeTypeEnum.论文答辩教师.getNum(), student.getXxId());

		ThesisDefencePlanVO thesisDefencePlanVO = BeanConvertUtils.convert(thesisDefencePlan, ThesisDefencePlanVO.class,
				new BeanConvertHandler<GjtThesisDefencePlan, ThesisDefencePlanVO>() {

					@Override
					public void handle(GjtThesisDefencePlan soruce, ThesisDefencePlanVO target) {
						if (soruce.getDefenceType() == 1) {
							target.setDefenceTypeStr("现场答辩");
						} else {
							target.setDefenceTypeStr("远程答辩");
						}

						String defenceTeacherStr = "";
						if (thesisApply.getDefenceTeacher1() != null || thesisApply.getDefenceTeacher2() != null) {
							if (thesisApply.getDefenceTeacher1() != null) {
								String[] ids = thesisApply.getDefenceTeacher1().split(",");
								for (String id : ids) {
									for (GjtEmployeeInfo defenceTeacher : defenceTeachers) {
										if (defenceTeacher.getEmployeeId().equals(id)) {
											defenceTeacherStr += defenceTeacher.getXm() + ",";
										}
									}
								}
							}

							if (thesisApply.getDefenceTeacher2() != null) {
								String[] ids = thesisApply.getDefenceTeacher2().split(",");
								for (String id : ids) {
									for (GjtEmployeeInfo defenceTeacher : defenceTeachers) {
										if (defenceTeacher.getEmployeeId().equals(id)) {
											defenceTeacherStr += defenceTeacher.getXm() + ",";
										}
									}
								}
							}
						}
						target.setDefenceTeachers(defenceTeacherStr);
					}

				});

		return thesisDefencePlanVO;
	}

	/**
	 * 寄送定稿
	 * 
	 * @param applyId
	 * @param session
	 * @throws CommonException
	 */
	@ResponseBody
	@RequestMapping(value = "sendThesis")
	public void sendThesis(@RequestParam String applyId, HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);
		if (thesisApply == null) {
			throw new CommonException(BusinessCode.STUDY_THESIS_SEND_THESIS_APPLY_NOT_FOUND);
		}

		if (thesisApply.getStatus() == ThesisStatusEnum.THESIS_COLLEGE_CONFIRM_THESIS.getValue()) {
			thesisApply.setStatus(ThesisStatusEnum.THESIS_SEND.getValue());
			thesisApply.setUpdatedBy(student.getGjtUserAccount().getId());
			gjtThesisApplyService.update(thesisApply);
		}

	}

}
