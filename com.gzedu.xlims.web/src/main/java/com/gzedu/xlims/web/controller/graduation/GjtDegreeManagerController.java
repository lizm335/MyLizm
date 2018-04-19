/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.graduation;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtDegreeRequirement;
import com.gzedu.xlims.pojo.graduation.GjtGraApplyFlowRecord;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyDegree;
import com.gzedu.xlims.pojo.status.DegreeAuditRoleEnum;
import com.gzedu.xlims.pojo.status.DegreeRequirementTypeEnum;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.graduation.GjtDegreeCollegeService;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyCertifService;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyDegreeService;
import com.gzedu.xlims.service.graduation.GjtGraduationRegisterService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年8月29日
 * @version 3.0
 *
 */
@Controller
@RequestMapping("/graduation/degreemanager")
public class GjtDegreeManagerController extends BaseController {

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtGraduationApplyDegreeService gjtGraduationApplyDegreeService;

	@Autowired
	private GjtGraduationRegisterService gjtGraduationRegisterService;

	@Autowired
	private GjtDegreeCollegeService gjtDegreeCollegeService;

	@Autowired
	private GjtGraduationApplyCertifService gjtGraduationApplyCertifService;

	@Autowired
	private GjtGraduationApplyCertifService gjtGraduationApplyCardService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	private static final Log log = LogFactory.getLog(GjtDegreeManagerController.class);

	/**
	 * 学位申请列表
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月21日 下午5:18:52
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "degreeApplyList", method = RequestMethod.GET)
	public String degreeApplyList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		Page<GjtGraduationApplyDegree> pageInfo = gjtGraduationApplyDegreeService.queryGraduationApplyCardByPage(searchParams, pageRequest);
		model.addAttribute("pageInfo", pageInfo);

		searchParams.remove("EQ_auditState");
		long all = gjtGraduationApplyDegreeService.countGraduationApplyCardByPage(searchParams);
		model.addAttribute("all", all);

		searchParams.remove("EQ_isReceive");
		searchParams.put("EQ_auditState", 0);
		long audit = gjtGraduationApplyDegreeService.count(searchParams);// 待审核
		model.addAttribute("audit", audit);

		searchParams.put("EQ_auditState", 1);
		long pass = gjtGraduationApplyDegreeService.count(searchParams);// 已通过
		model.addAttribute("pass", pass);

		searchParams.put("EQ_auditState", 2);
		long notpass = gjtGraduationApplyDegreeService.count(searchParams);// 未通过
		model.addAttribute("notpass", notpass);

		searchParams.put("EQ_auditState", 3);// 已发放
		long receive = gjtGraduationApplyDegreeService.count(searchParams);
		model.addAttribute("receive", receive);

		Map<String, String> graduationPlanMap = commonMapService.getGraduationPlanMap(user.getGjtOrg().getId());// 毕业计划
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心
		Map<String, String> specialtyMap = commonMapService.getSpecialtyBaseMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 学期
		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("graduationPlanMap", graduationPlanMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		return "graduation/degree/degree_apply_list";
	}

	/**
	 * 学位申请详情
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月21日 下午5:19:09
	 * @param request
	 * @param model
	 * @param applyId
	 * @return
	 */
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(HttpServletRequest request, Model model, String applyId) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtGraduationApplyDegree info = gjtGraduationApplyDegreeService.queryById(applyId);
		info.setGjtGraduationRegister(gjtGraduationRegisterService.queryByStudentId(info.getGraduationPlanId(), info.getGjtStudentInfo().getStudentId()));
		model.addAttribute("info", info);
		List<GjtDegreeRequirement> requirements = gjtDegreeCollegeService.queryReqByCollegeSpecialtyId(info.getDegreeId());
		model.addAttribute("requirements", requirements);

		float compulsoryScore = gjtGraduationApplyCertifService.queryCompulsorySumScore(info.getStudentId());
		float otherScore = gjtGraduationApplyCertifService.queryOtherSumScore(info.getStudentId());
		float designScore = gjtGraduationApplyCertifService.queryDesignScore(info.getStudentId());
		float degreeScore = gjtGraduationApplyCertifService.queryDegreeScore(info.getStudentId());
		for (GjtDegreeRequirement req : requirements) {
			if (DegreeRequirementTypeEnum.COMPULSORY_AVG.getValue() == req.getRequirementType()) {
				req.setActualValue(compulsoryScore);
			} else if (DegreeRequirementTypeEnum.OTHER_AVG.getValue() == req.getRequirementType()) {
				req.setActualValue(otherScore);
			} else if (DegreeRequirementTypeEnum.ENGLISH_SCORE.getValue() == req.getRequirementType()) {
				req.setActualValue(degreeScore);
			} else if (DegreeRequirementTypeEnum.DESIGN_SCORE.getValue() == req.getRequirementType()) {
				req.setActualValue(designScore);
			}
		}

		// 审核记录
		List<GjtGraApplyFlowRecord> flowRecordList = gjtGraduationApplyCardService.queryGraApplyFlowRecordByApplyId(applyId);
		model.addAttribute("flowRecordList", flowRecordList);
		if (CollectionUtils.isNotEmpty(flowRecordList)) {
			model.addAttribute("lastRecord", flowRecordList.get(flowRecordList.size() - 1));
		}
		// 获取用户对应的审核角色
		Integer auditRole = DegreeAuditRoleEnum.getCode(user.getPriRoleInfo().getRoleId());
		model.addAttribute("auditRole", auditRole);
		model.addAttribute("baseTypeMap", EnumUtil.getDegreeRequirementTypeMap());
		return "graduation/degree/degree_apply_form";
	}

	/**
	 * 新增一个审核
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月28日 下午5:58:20
	 * @param request
	 * @param applyId
	 * @param recordId
	 * @param state
	 * @param content
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveFlowRecord", method = RequestMethod.POST)
	public Feedback saveFlowRecord(HttpServletRequest request, String applyId, String recordId, int state, String content) {
		Feedback feedback = new Feedback(true, null);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Date now = new Date();
		try {
			GjtGraApplyFlowRecord record = gjtGraduationApplyDegreeService.queryFlowRecordById(recordId);
			record.setAuditContent(content);
			record.setAuditOperator(user.getRealName());
			record.setAuditState(state);
			record.setAuditDt(now);
			record.setUpdatedBy(user.getId());
			record.setUpdatedDt(now);
			record = gjtGraduationApplyDegreeService.saveFlowRecord(record);
			GjtGraduationApplyDegree apply = gjtGraduationApplyDegreeService.queryById(applyId);
			if (state == 2) {// 审核不通过
				apply.setAuditState(2);
				apply.setUpdatedBy(user.getId());
				apply.setUpdatedDt(now);
				gjtGraduationApplyDegreeService.save(apply);
				return feedback;
			} else if (state == 1 && record.getAuditOperatorRole() == 4) {// 最后一个审核流程
				apply.setAuditState(1);
				apply.setUpdatedBy(user.getId());
				apply.setUpdatedDt(now);
				gjtGraduationApplyDegreeService.save(apply);
				return feedback;
			}
			// 添加下一个待审核流程
			GjtGraApplyFlowRecord nextRecord = new GjtGraApplyFlowRecord();
			nextRecord.setApplyId(record.getApplyId());
			nextRecord.setAuditOperatorRole(record.getAuditOperatorRole() + 1);
			nextRecord.setAuditState(0);
			nextRecord.setCreatedBy(user.getId());
			nextRecord.setCreatedDt(now);
			nextRecord.setFlowRecordId(UUIDUtils.random());
			nextRecord.setIsDeleted("N");
			gjtGraduationApplyDegreeService.saveFlowRecord(nextRecord);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback.setMessage("系统异常");
			feedback.setSuccessful(false);
		}
		return feedback;
	}



	@RequestMapping(value = "queryCourseScore", method = RequestMethod.GET)
	public String queryCourseScore(Model model, String studentId) {

		GjtStudentInfo student = gjtStudentInfoService.queryById(studentId);
		model.addAttribute("specialty", student.getGjtSpecialty());
		List<Map<String, Object>> achieveList = gjtGraduationApplyDegreeService.queryAchievementByStudentId(studentId);
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
		model.addAttribute("modelList", modelList);
		model.addAttribute("totalCredits", totalCredits);
		model.addAttribute("centerCredits", centerCredits);
		return "/graduation/degree/course_score_form";
	}

	@RequestMapping(value = "downloadReqFile", method = RequestMethod.GET)
	public String downloadReqFile(HttpServletRequest request, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String phone = ObjectUtils.toString(user.getSjh());
		if (EmptyUtils.isNotEmpty(phone)) {
			model.addAttribute("mobileNumber", phone.substring(phone.length() - 4, phone.length()));
		}
		return "/graduation/degree/download_form";
	}

	@RequestMapping(value = "download", method = RequestMethod.GET)
	public void downloadReqFile(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
			searchParams.put("EQ_orgId", user.getGjtOrg().getId());
			String outputFilePath = gjtGraduationApplyDegreeService.downloadReqFile(searchParams,
					request.getSession().getServletContext().getRealPath(""));
			super.downloadFile(request, response, outputFilePath);
			FileKit.delFile(outputFilePath);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@RequestMapping(value = "importApplyFlowRecord", method = RequestMethod.GET)
	public String importApplyFlowRecord(HttpServletRequest request, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String phone = ObjectUtils.toString(user.getSjh());
		if (EmptyUtils.isNotEmpty(phone)) {
			model.addAttribute("mobileNumber", phone.substring(phone.length() - 4, phone.length()));
		}
		return "/graduation/degree/import_form";
	}

	/**
	 * 导入审核记录
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月28日 下午6:01:03
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "importApplyFlowRecord", method = RequestMethod.POST)
	@ResponseBody
	public ImportFeedback importApplyFlowRecord(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "姓名", "学号", "分部审核人", "分部审核结果", "分部审核备注", "总部审核人", "总部审核结果", "总部审核备注", "结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}
			Date now = new Date();
			if (dataList != null && dataList.size() > 0) {
				for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据

					if (datas.length < heads.length - 1) {
						result[heads.length - 1] = "数据不全";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[1])) { // 学号
						result[heads.length - 1] = "学号不能为空";
						failedList.add(result);
						continue;
					}
					GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(datas[1]);
					if (studentInfo == null) {
						result[heads.length - 1] = "学号不存在";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[0])) { // 姓名
						result[heads.length - 1] = "姓名不能为空";
						failedList.add(result);
						continue;
					}

					if (!datas[0].equals(studentInfo.getXm())) {
						result[heads.length - 1] = "姓名和学号不对应";
						failedList.add(result);
						continue;
					}
					GjtGraduationApplyDegree apply = gjtGraduationApplyDegreeService.queryDegreeApplyByStudentId(studentInfo.getStudentId());
					if (apply == null) {
						result[heads.length - 1] = "该学生没有学位申请记录";
						failedList.add(result);
						continue;
					}
					if ("".equals(datas[2])) {
						result[heads.length - 1] = "分部审核人不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[3])) {
						result[heads.length - 1] = "分部审核结果不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[4])) {
						result[heads.length - 1] = "分部审核备注不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[5])) {
						result[heads.length - 1] = "总部审核结果不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[6])) {
						result[heads.length - 1] = "总部审核备注不能为空";
						failedList.add(result);
						continue;
					}

					result[heads.length - 1] = "新增成功";
					successList.add(result);

					GjtGraApplyFlowRecord record = gjtGraduationApplyDegreeService.queryFlowRecordByApplyId(apply.getApplyId(), 3);
					if (record == null) {
						record = new GjtGraApplyFlowRecord();
						record.setFlowRecordId(UUIDUtils.random());
						record.setApplyId(apply.getApplyId());
						record.setAuditOperatorRole(3);
						record.setCreatedBy(user.getId());
						record.setCreatedDt(now);
					}
					record.setAuditOperator(datas[2]);
					record.setAuditState(datas[3].equals("通过") ? 1 : 2);
					record.setAuditContent(datas[4]);
					record.setUpdatedBy(user.getId());
					record.setUpdatedDt(now);
					gjtGraduationApplyDegreeService.saveFlowRecord(record);

					if (record.getAuditState() == 2) {
						apply.setAuditState(2);
						gjtGraduationApplyDegreeService.save(apply);
					}else if(record.getAuditState()==1){
						record = gjtGraduationApplyDegreeService.queryFlowRecordByApplyId(apply.getApplyId(), 4);
						if (record == null) {
							record = new GjtGraApplyFlowRecord();
							record.setFlowRecordId(UUIDUtils.random());
							record.setApplyId(apply.getApplyId());
							record.setAuditOperatorRole(4);
							record.setCreatedBy(user.getId());
							record.setCreatedDt(now);
						}
						record.setAuditOperator(datas[5]);
						record.setAuditState(datas[6].equals("通过") ? 1 : 2);
						record.setAuditContent(datas[7]);
						record.setUpdatedBy(user.getId());
						record.setUpdatedDt(now);
						gjtGraduationApplyDegreeService.saveFlowRecord(record);
						apply.setAuditState(record.getAuditState());
						gjtGraduationApplyDegreeService.save(apply);
					}
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "degreeApplyAuditRecord_success_" + currentTimeMillis + ".xls";
			String failedFileName = "degreeApplyAuditRecord_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "教材导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "教材导入失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL + "degreeApplyAuditRecord"
					+ File.separator;
			File f = new File(filePath);
			if (!f.exists()) {
				f.mkdirs();
			}

			File successFile = new File(filePath, successFileName);
			successFile.createNewFile();
			ExcelUtil.writeWorkbook(workbook1, successFile);

			File failedFile = new File(filePath, failedFileName);
			failedFile.createNewFile();
			ExcelUtil.writeWorkbook(workbook2, failedFile);

			return new ImportFeedback(true, dataList.size(), successList.size(), failedList.size(), successFileName, failedFileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ImportFeedback(false, "系统异常！");
		}
	}

	@RequestMapping(value = "exportApplyFlowRecord", method = RequestMethod.GET)
	public String exportApplyFlowRecord(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String phone = ObjectUtils.toString(user.getSjh());
		if (EmptyUtils.isNotEmpty(phone)) {
			model.addAttribute("mobileNumber", phone.substring(phone.length() - 4, phone.length()));
		}
		return "/graduation/degree/export_form";
	}

	@RequestMapping(value = "exportApplyFlowRecord", method = RequestMethod.POST)
	public void exportApplyFlowRecord(HttpServletRequest request, HttpServletResponse response) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		PageRequest pageRequest = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Page<GjtGraduationApplyDegree> pageInfo = gjtGraduationApplyDegreeService.queryGraduationApplyCardByPage(searchParams, pageRequest);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", pageInfo.getContent());
		map.put("pyccMap", commonMapService.getPyccMap());
		String path = getClass().getResource(WebConstants.EXCEL_MODEL_URL).getPath() + "学位申请记录导出模板.xls";
		com.gzedu.xlims.common.ExcelUtil.exportExcel(map, path, response, "学位申请记录.xls");
	}

	@RequestMapping(value = "degreeCollegeList", method = RequestMethod.GET)
	public String degreeCollegeList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<Object> pageInfo = new PageImpl<Object>(new ArrayList<Object>(), pageRequst, 0);
		model.addAttribute("pageInfo", pageInfo);
		return "graduation/degree/degree_college_list";
	}

	@RequestMapping(value = "degreeSpecialtyList", method = RequestMethod.GET)
	public String degreeSpecialtyList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<Object> pageInfo = new PageImpl<Object>(new ArrayList<Object>(), pageRequst, 0);
		model.addAttribute("pageInfo", pageInfo);
		return "graduation/degree/degree_specialty_list";
	}

}
