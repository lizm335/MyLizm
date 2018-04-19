package com.ouchgzee.study.web.controller.educational;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtDegreeCollege;
import com.gzedu.xlims.pojo.graduation.GjtDegreeRequirement;
import com.gzedu.xlims.pojo.graduation.GjtGraApplyFlowRecord;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyDegree;
import com.gzedu.xlims.pojo.graduation.GjtGraduationPlan;
import com.gzedu.xlims.pojo.graduation.GjtSpecialtyDegreeCollege;
import com.gzedu.xlims.pojo.status.DegreeRequirementTypeEnum;
import com.gzedu.xlims.pojo.thesis.GjtThesisGuideRecord;
import com.gzedu.xlims.pojo.thesis.GjtThesisPlan;
import com.gzedu.xlims.service.graduation.GjtDegreeCollegeService;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyCertifService;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyDegreeService;
import com.gzedu.xlims.service.graduation.GjtGraduationPlanService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.practice.GjtPracticeApplyService;
import com.gzedu.xlims.service.thesis.GjtThesisApplyService;
import com.gzedu.xlims.service.thesis.GjtThesisGuideRecordService;
import com.gzedu.xlims.service.thesis.GjtThesisPlanService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.vo.graduation.DegreeCollegeVO;
import com.ouchgzee.study.web.vo.graduation.DegreeCollegeVO.DegreeRequirement;

@Controller
@RequestMapping("/pcenter/degree")
public class DegreeController extends BaseController {

	private static final Log log = LogFactory.getLog(DegreeController.class);

	@Autowired
	private GjtDegreeCollegeService gjtDegreeCollegeService;

	@Autowired
	private GjtGraduationApplyCertifService gjtGraduationApplyCertifService;

	@Autowired
	private GjtGraduationApplyDegreeService gjtGraduationApplyDegreeService;
	
	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtGraduationPlanService gjtGraduationPlanService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtThesisPlanService gjtThesisPlanService;

	@Autowired
	private GjtPracticeApplyService gjtPracticeApplyService;

	@Autowired
	private GjtThesisGuideRecordService gjtThesisGuideRecordService;

	@Autowired
	private GjtThesisApplyService gjtThesisApplyService;
	/**
	 * 查询学位院校列表
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月27日 下午4:28:05
	 * @param request
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryDegreeCollegeList", method = RequestMethod.GET)
	public Map<String, Object> queryDegreeCollegeList(HttpServletRequest request, HttpSession session) {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);

		List<GjtDegreeCollege> degreeColleges = gjtDegreeCollegeService.queryByStudentInfo(student);
		List<DegreeCollegeVO> degreeCollegeVOs = new ArrayList<DegreeCollegeVO>();
		for(GjtDegreeCollege college:degreeColleges){
			for (GjtSpecialtyDegreeCollege degree : college.getGjtSpecialtyDegreeColleges()) {
				if (!degree.getGjtSpecialty().getSpecialtyBaseId().equals(student.getGjtSpecialty().getSpecialtyBaseId()))
					continue;
				DegreeCollegeVO degreeCollegeVO = new DegreeCollegeVO();
				degreeCollegeVO.setCollegeName(college.getCollegeName());
				degreeCollegeVO.setCollegeCover(college.getCover());
				degreeCollegeVO.setDegreeId(degree.getId());
				degreeCollegeVO.setDegreeName(degree.getDegreeName());

				List<GjtDegreeRequirement> requirements = gjtDegreeCollegeService.queryReqByCollegeSpecialtyId(degree.getId());
				for (GjtDegreeRequirement req : requirements) {
					degreeCollegeVO.addDegreeRequirement(DegreeRequirementTypeEnum.getName(req.getRequirementType()), req.getRequirementDesc());
				}
				degreeCollegeVOs.add(degreeCollegeVO);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		GjtGraduationPlan gjtGraduationPlan = gjtGraduationPlanService.findByTermId(student.getGjtGrade().getGradeId(), student.getXxId());
		if (gjtGraduationPlan != null) {
			Date startDate = gjtGraduationPlan.getDegreeApplyBeginDt();
			Date endDate = gjtGraduationPlan.getDegreeApplyEndDt();
			Date now = new Date();

			result.put("applyStartDate", startDate);
			result.put("applyEndDate", endDate);
			result.put("allowApply", now.getTime() >= startDate.getTime() && now.getTime() <= endDate.getTime());
		}
		result.put("collegeList", degreeCollegeVOs);
		return result;
	}

	/**
	 * 查询学位院校详情
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月28日 下午3:58:53
	 * @param request
	 * @param session
	 * @param degreeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryDegreeCollegeDetail", method = RequestMethod.GET)
	public DegreeCollegeVO queryDegreeCollegeDetail(HttpServletRequest request, HttpSession session, String degreeId) {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtUserAccount user = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);
		GjtSpecialtyDegreeCollege degree = gjtDegreeCollegeService.querySpecialtyDegreeCollegeById(degreeId);
		DegreeCollegeVO degreeCollegeVO = new DegreeCollegeVO();
		degreeCollegeVO.setCollegeName(degree.getGjtDegreeCollege().getCollegeName());
		degreeCollegeVO.setCollegeCover(degree.getGjtDegreeCollege().getCover());
		degreeCollegeVO.setDegreeName(degree.getDegreeName());
		degreeCollegeVO.setPass(true);
		List<GjtDegreeRequirement> requirements = gjtDegreeCollegeService.queryReqByCollegeSpecialtyId(degree.getId());
		for (GjtDegreeRequirement req : requirements) {
			DegreeRequirement reqVO=degreeCollegeVO.addDegreeRequirement(DegreeRequirementTypeEnum.getName(req.getRequirementType()), req.getRequirementDesc());
			if (DegreeRequirementTypeEnum.COMPULSORY_AVG.getValue() == req.getRequirementType()) {
				float compulsoryScore = gjtGraduationApplyCertifService.queryCompulsorySumScore(student.getStudentId());
				req.setActualValue(compulsoryScore);
			} else if (DegreeRequirementTypeEnum.OTHER_AVG.getValue() == req.getRequirementType()) {
				float otherScore = gjtGraduationApplyCertifService.queryOtherSumScore(student.getStudentId());
				req.setActualValue(otherScore);
			} else if (DegreeRequirementTypeEnum.DEGREE_SCORE.getValue() == req.getRequirementType()) {
				float degreeScore = gjtGraduationApplyCertifService.queryDegreeScore(student.getStudentId());
				req.setActualValue(degreeScore);
			} else if (DegreeRequirementTypeEnum.DESIGN_SCORE.getValue() == req.getRequirementType()) {
				float designScore = gjtGraduationApplyCertifService.queryDesignScore(student.getStudentId());
				req.setActualValue(designScore);
				String gradeId = gjtGradeService.getCurrentGradeId(student.getXxId());
				GjtThesisPlan thesisPlan = gjtThesisPlanService.findByGradeIdAndOrgIdAndStatus(gradeId, student.getXxId(), 3);
				List<GjtThesisGuideRecord> guideRecords = gjtThesisGuideRecordService.findByThesisPlanIdAndStudentId(thesisPlan.getThesisPlanId(),
						student.getStudentId());
				reqVO.setAttachment(guideRecords.get(guideRecords.size() - 1).getAttachment());
				// GjtThesisApply thesisApply =
				// gjtThesisApplyService.findByThesisPlanIdAndStudentId(thesisPlan.getThesisPlanId(),
				// student.getStudentId());
				// reqVO.setExamScore(thesisApply.getReviewScore());
				// reqVO.setIsPass(thesisApply.getReviewScore() != null &&
				// thesisApply.getReviewScore() > 60);
			}
			reqVO.setRequirementScore(req.getRequirementParam());
			reqVO.setExamScore(req.getActualValue());
			if (reqVO.getIsPass() == null) {
				reqVO.setIsPass(req.getIsPass());
			}
			reqVO.setType(req.getRequirementType());
			if(!req.getIsPass()){
				degreeCollegeVO.setPass(req.getIsPass());
			}
		}
		return degreeCollegeVO;
	}

	/**
	 * 新增学位申请
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月28日 下午3:59:21
	 * @param request
	 * @param session
	 * @param degreeApply
	 */
	@ResponseBody
	@RequestMapping(value = "createDegreeApply", method = RequestMethod.POST)
	public void createDegreeApply(HttpServletRequest request, HttpSession session, @Valid GjtGraduationApplyDegree degreeApply) {
		GjtUserAccount user = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);
		GjtGraduationApplyDegree apply = new GjtGraduationApplyDegree();
		GjtSpecialtyDegreeCollege degree = gjtDegreeCollegeService.querySpecialtyDegreeCollegeById(degreeApply.getDegreeId());
		apply.setApplyId(UUIDUtils.random());
		apply.setApplyContent(degreeApply.getApplyContent());
		apply.setCollegeId(degree.getGjtDegreeCollege().getCollegeId());
		apply.setCreatedBy(user.getId());
		apply.setCreatedDt(new Date());
		apply.setDegreeCondition(1);
		apply.setDegreeId(degreeApply.getDegreeId());
		apply.setEnglishCertificateUrl(degreeApply.getEnglishCertificateUrl());
		apply.setIdcardFrontUrl(degreeApply.getIdcardFrontUrl());
		apply.setIdcardBackUrl(degreeApply.getIdcardBackUrl());
		apply.setPaperUrl(degreeApply.getPaperUrl());
		apply.setPaperCheckUrl(degreeApply.getPaperCheckUrl());
		apply.setIsDeleted(Constants.BOOLEAN_NO);
		gjtGraduationApplyDegreeService.save(apply);
		GjtGraApplyFlowRecord record = new GjtGraApplyFlowRecord();
		record.setApplyId(apply.getApplyId());
		record.setAuditState(0);
		record.setAuditOperatorRole(2);
		record.setCreatedBy(user.getId());
		record.setCreatedDt(new Date());
		record.setIsDeleted(Constants.NODELETED);
		gjtGraduationApplyDegreeService.saveFlowRecord(record);
	}

	@ResponseBody
	@RequestMapping(value = "queryCourseScore", method = RequestMethod.GET)
	public Map<String, Object> queryCourseScore(HttpServletRequest request, HttpSession session) {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		student = gjtStudentInfoService.queryById(student.getStudentId());
		resultMap.put("minCredits", student.getGjtSpecialty().getZdbyxf());
		resultMap.put("minCenterCredits", student.getGjtSpecialty().getZyddksxf());
		List<Map<String, Object>> achieveList = gjtGraduationApplyDegreeService.queryAchievementByStudentId(student.getStudentId());
		Set<String> modelSet = new HashSet<String>();
		for (Map<String, Object> map : achieveList) {
			modelSet.add((String) map.get("modelId"));
		}
		List<Map<String, Object>> modelList = new ArrayList<Map<String, Object>>();
		BigDecimal totalCredits = new BigDecimal(0);
		BigDecimal centerCredits = new BigDecimal(0);// 中央电大考试学分
		for (String modelId : modelSet) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			modelMap.put("modelId", modelId);
			List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
			BigDecimal getCredits = new BigDecimal(0);
			for (Map<String, Object> map : achieveList) {
				if (modelId.equals(map.get("modelId"))) {
					temp.add(map);
					getCredits = getCredits.add((BigDecimal) map.get("getCredits"));
					if ("2".equals(map.get("examUnit"))) {
						centerCredits = centerCredits.add((BigDecimal) map.get("getCredits"));
					}
				}
			}
			modelMap.put("achieveList", temp);
			modelMap.put("modelName", temp.get(0).get("modelName"));
			modelMap.put("totalscore", temp.get(0).get("totalscore"));
			modelMap.put("crtvuScore", temp.get(0).get("crtvuScore"));
			modelMap.put("getCredits", getCredits);
			totalCredits = totalCredits.add(getCredits);
			modelList.add(modelMap);
		}
		resultMap.put("modelList", modelList);
		resultMap.put("totalCredits", totalCredits);
		resultMap.put("centerCredits", centerCredits);
		return resultMap;
	}

	public Map<String, Object> queryDegreeResult(HttpServletRequest request, HttpSession session) {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtGraduationApplyDegree apply = gjtGraduationApplyDegreeService.queryDegreeApplyByStudentId(student.getStudentId());
		Map<String, Object> result = new HashMap<String, Object>();
		if (apply == null) {
			result.put("status", -1);
			return result;
		}
		result.put("status", apply.getAuditState());
		if (apply.getAuditState() == 1) {
			GjtGraduationPlan gjtGraduationPlan = gjtGraduationPlanService.findByTermId(student.getGjtGrade().getGradeId(), student.getXxId());
			result.put("graCertReceiveStart", gjtGraduationPlan.getGraCertReceiveBeginDt());
			result.put("graCertReceiveEnd", gjtGraduationPlan.getGraCertReceiveEndDt());
			result.put("graArchivesReceiveStart", gjtGraduationPlan.getGraArchivesReceiveBeginDt());
			result.put("graArchivesReceiveEnd", gjtGraduationPlan.getGraArchivesReceiveEndDt());
			result.put("degreeCertReceiveStart", gjtGraduationPlan.getDegreeCertReceiveBeginDt());
			result.put("degreeCertReceiveEnd", gjtGraduationPlan.getDegreeCertReceiveEndDt());

		} else if (apply.getAuditState() == 2) {
			GjtGraApplyFlowRecord record = gjtGraduationApplyDegreeService.queryFlowRecordByApplyId(apply.getApplyId(), 4);
			result.put("content", record.getAuditContent());
		}
		return result;
	}


}
