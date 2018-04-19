package com.gzedu.xlims.web.controller.graduation;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzdec.framework.util.WordTemplateUtil;
import com.gzedu.xlims.pojo.GjtRecResult;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtDegreeRequirement;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyCertif;
import com.gzedu.xlims.pojo.graduation.GjtGraduationRegisterEdu;
import com.gzedu.xlims.pojo.status.DegreeRequirementTypeEnum;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.pojo.status.GraduationRecordStatusEnum;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.graduation.GjtDegreeCollegeService;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyCertifService;
import com.gzedu.xlims.service.graduation.GjtGraduationRegisterService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 旧版的了，弃用 迁移到com.gzedu.xlims.web.controller.graduation.GjtGraduationApplyCertifController
 */
@Controller
@RequestMapping("/graduation/record")
public class GjtGraduationRecordController {

	private static final Log log = LogFactory.getLog(GjtGraduationApplyCertifController.class);

	@Autowired
	private GjtGraduationApplyCertifService gjtGraduationRecordService;

	@Autowired
	private GjtRecResultService gjtRecResultService;

	@Autowired
	private GjtDegreeCollegeService gjtDegreeCollegeService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtGraduationRegisterService gjtGraduationRegisterService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private CacheService cacheService;

	/**
	 * 查询毕业申请列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_gjtStudentInfo.gjtSchoolInfo.id", user.getGjtOrg().getId());
		Page<GjtGraduationApplyCertif> pageInfo = gjtGraduationRecordService.queryPage(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);

		model.addAttribute("recordStatusMap", EnumUtil.getGraduationRecordStatusMap());
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId()));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(user.getGjtOrg().getId()));
		model.addAttribute("trainingLevelMap", commonMapService.getPyccMap());

		return "graduation/record/list";
	}

	/**
	 * 详情
	 * @param recordId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(@RequestParam("recordId") String recordId, Model model, HttpServletRequest request) {
		log.info("recordId:" + recordId);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtGraduationApplyCertif graduationRecord = gjtGraduationRecordService.queryById(recordId);

		//查询学生的模块学分获得详情，并按模块归类
		List<Object[]> moduleScoreList = gjtGraduationRecordService.queryModuleScore(graduationRecord.getGjtStudentInfo().getStudentId());
		Map<String, List<Object[]>> moduleScoreMap = new LinkedHashMap<String, List<Object[]>>();
		if (moduleScoreList != null && moduleScoreList.size() > 0) {
			for (Object moduleScore[] : moduleScoreList) {
				String moduleName = (String)moduleScore[0];
				List<Object[]> list = moduleScoreMap.get(moduleName);
				if (list == null) {
					list = new ArrayList<Object[]>();
					list.add(moduleScore);
					moduleScoreMap.put(moduleName, list);
				} else {
					list.add(moduleScore);
				}
			}
		}

		//如果是本科专业的学生，还需要查询“大学英语B”和“计算机应用基础（本）”这两门课程的分数
		if ("2".equals(graduationRecord.getGjtStudentInfo().getPycc()) || "8".equals(graduationRecord.getGjtStudentInfo().getPycc())) {
			GjtRecResult score1 = gjtRecResultService.queryByStudentIdAndCourseName(graduationRecord.getGjtStudentInfo().getStudentId(), "大学英语B");
			GjtRecResult score2 = gjtRecResultService.queryByStudentIdAndCourseName(graduationRecord.getGjtStudentInfo().getStudentId(), "计算机应用基础（本）");
			model.addAttribute("score1", score1);
			model.addAttribute("score2", score2);
		}

		//申请学位
		if (graduationRecord.getApplyDegree() == 1) {
			List<GjtDegreeRequirement> degreeRequirements = null;/*gjtDegreeCollegeService.queryGjtDegreeRequirementBySpecialty(graduationRecord.getGjtDegreeCollege().getCollegeId(),
					graduationRecord.getGjtStudentInfo().getGjtSpecialty().getSpecialtyId());
			for (GjtDegreeRequirement requirement : degreeRequirements) {
				if (requirement.getRequirementType() == DegreeRequirementTypeEnum.COMPULSORY_AVG.getValue()) {  //必修课平均分
					requirement.setActualValue(gjtGraduationRecordService.queryCompulsorySumScore(graduationRecord.getGjtStudentInfo().getStudentId()));
				} else if (requirement.getRequirementType() == DegreeRequirementTypeEnum.OTHER_AVG.getValue()) {  //其他课程平均分
					requirement.setActualValue(gjtGraduationRecordService.queryOtherSumScore(graduationRecord.getGjtStudentInfo().getStudentId()));
				} else if (requirement.getRequirementType() == DegreeRequirementTypeEnum.DEGREE_SCORE.getValue()) {  //学位课程成绩
					requirement.setActualValue(gjtGraduationRecordService.queryDegreeScore(graduationRecord.getGjtStudentInfo().getStudentId()));
				} else if (requirement.getRequirementType() == DegreeRequirementTypeEnum.DESIGN_SCORE.getValue()) {  //毕业设计成绩
					requirement.setActualValue(gjtGraduationRecordService.queryDesignScore(graduationRecord.getGjtStudentInfo().getStudentId()));
				}
			}*/

			model.addAttribute("degreeRequirements", degreeRequirements);
		}

		model.addAttribute("graduationRecord", graduationRecord);
		model.addAttribute("moduleScoreMap", moduleScoreMap);
		model.addAttribute("recordStatusMap", EnumUtil.getGraduationRecordStatusMap());
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId()));
		model.addAttribute("trainingLevelMap", commonMapService.getPyccMap());

		return "graduation/record/view";
	}

//	/**
//	 * 受理
//	 * @param recordId
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "accept", method = RequestMethod.GET)
//	@ResponseBody
//	public Feedback accept(@RequestParam("recordId") String recordId, HttpServletRequest request) {
//		log.info("recordId:" + recordId);
//		try {
//			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
//			GjtGraduationApplyCertif graduationRecord = gjtGraduationRecordService.queryById(recordId);
//			if (graduationRecord.getStatus() == GraduationRecordStatusEnum.APPLY.getValue()) {
//				graduationRecord.setStatus(GraduationRecordStatusEnum.ACCEPT.getValue());
//				graduationRecord.setUpdatedBy(user.getId());
//				gjtGraduationRecordService.update(graduationRecord);
//
//				return new Feedback(true, "受理成功");
//			} else {
//				return new Feedback(true, "受理失败，原因:状态不符");
//			}
//
//		} catch (Exception e) {
//			return new Feedback(false, "受理失败，原因:" + e.getMessage());
//		}
//	}
//
//	/**
//	 * 初审
//	 * @param recordId
//	 * @param graduationCondition
//	 * @param firstTrial
//	 * @param degreeCondition
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "firstTrial", method = RequestMethod.POST)
//	@ResponseBody
//	public Feedback firstTrial(@RequestParam("recordId") String recordId, @RequestParam("graduationCondition") int graduationCondition,
//			@RequestParam("firstTrial") int firstTrial, Integer degreeCondition, HttpServletRequest request) {
//		log.info("recordId:" + recordId + ", graduationCondition:" + graduationCondition + ", firstTrial:" + firstTrial + ", degreeCondition" + degreeCondition);
//		try {
//			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
//			GjtGraduationApplyCertif graduationRecord = gjtGraduationRecordService.queryById(recordId);
//			if (graduationRecord.getStatus() == GraduationRecordStatusEnum.ACCEPT.getValue()) {
//				graduationRecord.setGraduationCondition(graduationCondition);
//				if (graduationRecord.getApplyDegree() == 1) {  //有申请学位才能对学位条件进行审核
//					graduationRecord.setDegreeCondition(degreeCondition);
//				}
//				if (firstTrial == 0) {  //未通过
//					graduationRecord.setStatus(GraduationRecordStatusEnum.FIRST_TRIAL_FAILED.getValue());
//				}
//				graduationRecord.setUpdatedBy(user.getId());
//				gjtGraduationRecordService.update(graduationRecord);
//
//				return new Feedback(true, "审核成功");
//			} else {
//				return new Feedback(true, "审核失败，原因:状态不符");
//			}
//		} catch (Exception e) {
//			return new Feedback(false, "审核失败，原因:" + e.getMessage());
//		}
//	}
//
//	/**
//	 * 终审
//	 * @param recordId
//	 * @param finalTrial
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "finalTrial", method = RequestMethod.POST)
//	@ResponseBody
//	public Feedback finalTrial(@RequestParam("recordId") String recordId,
//			@RequestParam("finalTrial") int finalTrial, HttpServletRequest request) {
//		log.info("recordId:" + recordId + ", finalTrial:" + finalTrial);
//		try {
//			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
//			GjtGraduationApplyCertif graduationRecord = gjtGraduationRecordService.queryById(recordId);
//			if (graduationRecord.getStatus() > GraduationRecordStatusEnum.APPLY.getValue()) {
//				if (finalTrial == 0) {  //未通过
//					graduationRecord.setStatus(GraduationRecordStatusEnum.FINAL_TRIAL_FAILED.getValue());
//				} else {
//					graduationRecord.setStatus(GraduationRecordStatusEnum.FINAL_TRIAL_SUCCESS.getValue());
//				}
//				graduationRecord.setUpdatedBy(user.getId());
//				gjtGraduationRecordService.update(graduationRecord);
//
//				return new Feedback(true, "审核成功");
//			} else {
//				return new Feedback(true, "审核失败，原因:状态不符,请先受理后再审核!");
//			}
//		} catch (Exception e) {
//			return new Feedback(false, "审核失败，原因:" + e.getMessage());
//		}
//	}
//
//	/**
//	 * 批量终审
//	 * @param file
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "/importFinalTrial")
//	@ResponseBody
//	public ImportFeedback importFinalTrial(@RequestParam("file") MultipartFile file,
//			HttpServletRequest request, HttpServletResponse response) {
//		try {
//			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
//			String[] heads = {"学号", "姓名", "专业", "年级", "层次", "毕业状态", "毕业条件", "学位状态", "学位条件", "终审结果", "导入结果"};
//			List<String[]> successList = new ArrayList<String[]>();
//			List<String[]> failedList = new ArrayList<String[]>();
//			List<String[]> dataList = null;
//			try {
//				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
//			} catch (Exception e) {
//				return new ImportFeedback(false, "请下载正确表格模版填写");
//			}
//
//			if (dataList != null && dataList.size() > 0) {
//				for (String[] datas : dataList) {
//					String[] result = new String[heads.length];  //记录导入结果
//					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1));  //先拷贝数据
//
//					if ("".equals(datas[0])) {
//						result[heads.length - 1] = "学号为空";
//						failedList.add(result);
//					} else {
//						GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(datas[0]);
//						if (studentInfo == null) {
//							result[heads.length - 1] = "找不到对应学生";
//							failedList.add(result);
//						} else {
//							GjtGraduationApplyCertif graduationRecord = gjtGraduationRecordService.queryByStudentId(studentInfo.getStudentId());
//							if (graduationRecord == null) {
//								result[heads.length - 1] = "找不到对应申请记录";
//								failedList.add(result);
//							} else {
//								if (graduationRecord.getStatus() > GraduationRecordStatusEnum.APPLY.getValue()
//										&& graduationRecord.getStatus() < GraduationRecordStatusEnum.COMPLETED.getValue()) {
//									if ("未通过".equals(datas[9])) {  //未通过
//										graduationRecord.setStatus(GraduationRecordStatusEnum.FINAL_TRIAL_FAILED.getValue());
//										graduationRecord.setUpdatedBy(user.getId());
//										gjtGraduationRecordService.update(graduationRecord);
//
//										result[heads.length - 1] = "成功";
//										successList.add(result);
//									} else if ("通过".equals(datas[9])) {
//										graduationRecord.setStatus(GraduationRecordStatusEnum.FINAL_TRIAL_SUCCESS.getValue());
//										graduationRecord.setUpdatedBy(user.getId());
//										gjtGraduationRecordService.update(graduationRecord);
//
//										result[heads.length - 1] = "成功";
//										successList.add(result);
//									} else {
//										result[heads.length - 1] = "审批结果有误，应为\"通过\"或者\"未通过\"";
//										failedList.add(result);
//									}
//								} else {
//									result[heads.length - 1] = "学生毕业进度不符";
//									failedList.add(result);
//								}
//							}
//						}
//					}
//				}
//			}
//
//			/* 创建记录成功和失败记录的文件  */
//			long currentTimeMillis = System.currentTimeMillis();
//			String successFileName = "importFinalTrial_success_" + currentTimeMillis + ".xls";
//			String failedFileName = "importFinalTrial_failed_" + currentTimeMillis + ".xls";
//
//			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "终审结果导入成功记录");
//			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "终审结果导入失败记录");
//
//			String filePath = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL + "graduationRecord" + File.separator;
//			File f = new File(filePath);
//			if (!f.exists()) {
//				f.mkdirs();
//			}
//
//			File successFile = new File(filePath, successFileName);
//			successFile.createNewFile();
//			ExcelUtil.writeWorkbook(workbook1, successFile);
//
//			File failedFile = new File(filePath, failedFileName);
//			failedFile.createNewFile();
//			ExcelUtil.writeWorkbook(workbook2, failedFile);
//
//			return new ImportFeedback(true, dataList.size(), successList.size(), failedList.size(), successFileName, failedFileName);
//		} catch (Exception e) {
//			log.error(e.getMessage(), e);
//			return new ImportFeedback(false, "系统异常！");
//		}
//	}
//
//	/**
//	 * 上传电子证件
//	 * @param recordId
//	 * @param graduationCertificateNo
//	 * @param graduationCertificateUrl
//	 * @param degreeCertificateNo
//	 * @param degreeCertificateUrl
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "uploadCertificate", method = RequestMethod.POST)
//	@ResponseBody
//	public Feedback uploadCertificate(@RequestParam("recordId") String recordId, String graduationCertificateNo, String graduationCertificateUrl,
//			String degreeCertificateNo, String degreeCertificateUrl, HttpServletRequest request) {
//		log.info("graduationCertificateNo:" + graduationCertificateNo + ", graduationCertificateUrl:" + graduationCertificateUrl
//				+ ", degreeCertificateNo:" + degreeCertificateNo + ", degreeCertificateUrl:" + degreeCertificateUrl);
//		try {
//			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
//			GjtGraduationApplyCertif graduationRecord = gjtGraduationRecordService.queryById(recordId);
//			if (graduationRecord.getStatus() > GraduationRecordStatusEnum.FINAL_TRIAL_FAILED.getValue()) {
//				graduationRecord.setGraduationCertificateNo(graduationCertificateNo);
//				graduationRecord.setGraduationCertificateUrl(graduationCertificateUrl);
//				graduationRecord.setDegreeCertificateNo(degreeCertificateNo);
//				graduationRecord.setDegreeCertificateUrl(degreeCertificateUrl);
//				graduationRecord.setUpdatedBy(user.getId());
//				gjtGraduationRecordService.update(graduationRecord);
//
//				return new Feedback(true, "上传成功");
//			} else {
//				return new Feedback(true, "上传失败，原因:状态不符");
//			}
//		} catch (Exception e) {
//			return new Feedback(false, "上传失败，原因:" + e.getMessage());
//		}
//	}
//
//	/**
//	 * 确认学员领取毕业证原件
//	 * @param recordId
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "completed", method = RequestMethod.GET)
//	@ResponseBody
//	public Feedback completed(@RequestParam("recordId") String recordId, HttpServletRequest request) {
//		log.info("recordId:" + recordId);
//		try {
//			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
//			GjtGraduationApplyCertif graduationRecord = gjtGraduationRecordService.queryById(recordId);
//			if (graduationRecord.getStatus() == GraduationRecordStatusEnum.FINAL_TRIAL_SUCCESS.getValue()) {
//				graduationRecord.setStatus(GraduationRecordStatusEnum.COMPLETED.getValue());
//				graduationRecord.setUpdatedBy(user.getId());
//				gjtGraduationRecordService.update(graduationRecord);
//
//				return new Feedback(true, "确认成功");
//			} else {
//				return new Feedback(true, "确认失败，原因:状态不符");
//			}
//
//		} catch (Exception e) {
//			return new Feedback(false, "确认失败，原因:" + e.getMessage());
//		}
//	}
//
//	/**
//	 * 下载登记表
//	 * @param studentId
//	 * @param response
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "download", method = RequestMethod.GET)
//	public void download(@RequestParam("studentId") String studentId, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		log.info("studentId:" + studentId);
//		Map<String, Object> registerMsg = gjtGraduationRecordService.queryStudentRegisterMsg(studentId);
//		String studentName = registerMsg.get("studentName").toString();
//		String fileName = "国家开放大学毕业生登记表——" + studentName + ".doc";
//
//		DateFormat df = new SimpleDateFormat("yyyy.MM");
//		List<Map<String, String>> eduList = new ArrayList<Map<String,String>>();
//		List<GjtGraduationRegisterEdu> registerEduList = gjtGraduationRegisterService.queryEduByStudentId(studentId);
//		if (registerEduList != null && registerEduList.size() > 0) {
//			int size = registerEduList.size();
//			if (size < 3) {
//				int n = 0;
//				for (GjtGraduationRegisterEdu registerEdu : registerEduList) {
//					Map<String, String> edu = new HashMap<String, String>();
//					String time = "";
//					if (registerEdu.getBeginTime() != null) {
//						time += df.format(registerEdu.getBeginTime());
//					}
//					if (registerEdu.getEndTime() != null) {
//						time += "--" + df.format(registerEdu.getEndTime());
//					}
//					time += " ";
//					edu.put("time", time);
//					if (registerEdu.getRegion() != null) {
//						edu.put("region", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.ALLAREA, registerEdu.getRegion()));
//					} else {
//						edu.put("region", " ");
//					}
//					if (registerEdu.getSchool() != null) {
//						edu.put("school", registerEdu.getSchool());
//					} else {
//						edu.put("school", " ");
//					}
//
//					eduList.add(edu);
//					n++;
//				}
//				for ( ; n < 4; n++) {  //凑够4行
//					Map<String, String> edu = new HashMap<String, String>();
//					edu.put("time", " ");
//					edu.put("region", " ");
//					edu.put("school", " ");
//
//					eduList.add(edu);
//				}
//			} else {   //只取前3行
//				for (int i = 0; i < 3; i++) {
//					GjtGraduationRegisterEdu registerEdu = registerEduList.get(i);
//					Map<String, String> edu = new HashMap<String, String>();
//					String time = "";
//					if (registerEdu.getBeginTime() != null) {
//						time += df.format(registerEdu.getBeginTime());
//					}
//					if (registerEdu.getEndTime() != null) {
//						time += "--" + df.format(registerEdu.getEndTime());
//					}
//					time += " ";
//					edu.put("time", time);
//					if (registerEdu.getRegion() != null) {
//						edu.put("region", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.ALLAREA, registerEdu.getRegion()));
//					} else {
//						edu.put("region", " ");
//					}
//					if (registerEdu.getSchool() != null) {
//						edu.put("school", registerEdu.getSchool());
//					} else {
//						edu.put("school", " ");
//					}
//
//					eduList.add(edu);
//				}
//			}
//		} else {  //放4个空行
//			for (int i = 0; i < 4; i++) {
//				Map<String, String> edu = new HashMap<String, String>();
//				edu.put("time", " ");
//				edu.put("region", " ");
//				edu.put("school", " ");
//
//				eduList.add(edu);
//			}
//		}
//		registerMsg.put("eduList", eduList);
//
//		Object photo = registerMsg.get("photo");
//		if (photo != null && !"".equals(photo.toString().trim())) {
//			String realPath = request.getSession().getServletContext().getRealPath("");
//			try {
//				String encode = WordTemplateUtil.getRemoteSourceEncode(photo.toString().trim(), realPath);
//				registerMsg.put("photo", encode);
//			} catch (Exception e) {
//				registerMsg.put("photo", " ");
//				log.error(e.getMessage(), e);
//			}
//		} else {
//			registerMsg.put("photo", " ");
//		}
//
//		response.setContentType("application/msword;charset=utf-8");
//		response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
//		WordTemplateUtil.createWord(registerMsg, "国家开放大学毕业生登记表.ftl", response.getOutputStream());
//	}
//
//	@RequestMapping(value = "export", method = RequestMethod.GET)
//	public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		String fileName = "毕业学员列表.xls";
//		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
//		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
//
//		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
//		searchParams.put("EQ_gjtStudentInfo.gjtSchoolInfo.id", user.getGjtOrg().getId());
//		Page<GjtGraduationApplyCertif> pageInfo = gjtGraduationRecordService.queryPage(searchParams, pageRequst);
//		List<GjtGraduationApplyCertif> list = (List<GjtGraduationApplyCertif>)pageInfo.getContent();
//
//		HSSFWorkbook workbook = this.getWorkbook(list, user);
//		this.downloadExcelFile(response, workbook, fileName);
//	}
//
//	private HSSFWorkbook getWorkbook(List<GjtGraduationApplyCertif> list, GjtUserAccount user) {
//		try {
//			HSSFWorkbook wb = new HSSFWorkbook();
//		    HSSFSheet sheet = wb.createSheet("毕业学员列表");
//		    String[] heads = {"学号", "个人信息", "报读信息", "毕业状态", "学位状态", "电子证书状态", "证书编号"};
//
//			Map<Integer, String> recordStatusMap = EnumUtil.getGraduationRecordStatusMap();
//			Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
//			Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
//			Map<String, String> trainingLevelMap = commonMapService.getPyccMap();
//
//	    	HSSFRow row = sheet.createRow(0);
//	    	for (int i = 0; i < heads.length; i++) {
//	    		HSSFCell cell = row.createCell(i);
//	    		cell.setCellType(Cell.CELL_TYPE_STRING);
//	    		cell.setCellValue(heads[i]);
//	    	}
//
//			int rowIdx = 1;
//			int colIdx = 0;
//			HSSFCell cell;
//			StringBuffer sb = new StringBuffer();
//
//			sheet.createFreezePane(0, 1); //冻结列：冻结0列1行
//
//			//为了能够使用换行，需要设置单元格的样式 wrap=true
//	        HSSFCellStyle s = wb.createCellStyle();
//	        s.setWrapText(true);
//
//			for (GjtGraduationApplyCertif record : list) {
//				row = sheet.createRow(rowIdx++);
//				colIdx = 0;
//
//				cell = row.createCell(colIdx++);
//				cell.setCellType(Cell.CELL_TYPE_STRING);
//				cell.setCellStyle(s);
//				sb.append((record.getGjtStudentInfo().getXh() == null ? "--" : record.getGjtStudentInfo().getXh()));
//				cell.setCellValue(sb.toString());
//				sb.delete(0, sb.length());
//
//				cell = row.createCell(colIdx++);
//				cell.setCellType(Cell.CELL_TYPE_STRING);
//				cell.setCellStyle(s);
//				sb.append("姓名：" + (record.getGjtStudentInfo().getXm() == null ? "--" : record.getGjtStudentInfo().getXm()));
//				sb.append("\n手机：" + (record.getGjtStudentInfo().getSjh() == null ? "--" : record.getGjtStudentInfo().getSjh()));
//				cell.setCellValue(sb.toString());
//				sb.delete(0, sb.length());
//
//				cell = row.createCell(colIdx++);
//				cell.setCellType(Cell.CELL_TYPE_STRING);
//				cell.setCellStyle(s);
//				sb.append("层次：" + (record.getGjtStudentInfo().getPycc() == null ? "--" : trainingLevelMap.get(record.getGjtStudentInfo().getPycc())));
//				sb.append("\n年级：" + gradeMap.get(record.getGjtStudentInfo().getGjtGrade().getGradeId()));
//				sb.append("\n专业：" + specialtyMap.get(record.getGjtStudentInfo().getGjtSpecialty().getSpecialtyId()));
//				cell.setCellValue(sb.toString());
//				sb.delete(0, sb.length());
//
//				cell = row.createCell(colIdx++);
//				cell.setCellType(Cell.CELL_TYPE_STRING);
//				cell.setCellStyle(s);
//				sb.append(recordStatusMap.get(record.getStatus()));
//				Integer graduationCondition = record.getGraduationCondition();
//				if (graduationCondition == null) {
//					sb.append("\n毕业条件：--");
//				} else if (graduationCondition == 0) {
//					sb.append("\n毕业条件：未达标");
//				} else {
//					sb.append("\n毕业条件：已达标");
//				}
//				cell.setCellValue(sb.toString());
//				sb.delete(0, sb.length());
//
//				cell = row.createCell(colIdx++);
//				cell.setCellType(Cell.CELL_TYPE_STRING);
//				cell.setCellStyle(s);
//				if (record.getApplyDegree() == 0) {
//					sb.append("未申请学位");
//				} else {
//					sb.append("申请学位");
//				}
//				Integer degreeCondition = record.getDegreeCondition();
//				if (degreeCondition == null) {
//					sb.append("\n学位条件：--");
//				} else if (degreeCondition == 0) {
//					sb.append("\n学位条件：未达标");
//				} else {
//					sb.append("\n学位条件：已达标");
//				}
//				cell.setCellValue(sb.toString());
//				sb.delete(0, sb.length());
//
//				cell = row.createCell(colIdx++);
//				cell.setCellType(Cell.CELL_TYPE_STRING);
//				cell.setCellStyle(s);
//				if (record.getStatus() > GraduationRecordStatusEnum.FINAL_TRIAL_FAILED.getValue()) {
//					if (record.getGraduationCertificateUrl() == null) {
//						sb.append("待上传");
//					} else {
//						sb.append("已上传");
//					}
//				} else {
//					sb.append("--");
//				}
//				cell.setCellValue(sb.toString());
//				sb.delete(0, sb.length());
//
//				cell = row.createCell(colIdx++);
//				cell.setCellType(Cell.CELL_TYPE_STRING);
//				cell.setCellStyle(s);
//				if (record.getStatus() > GraduationRecordStatusEnum.FINAL_TRIAL_FAILED.getValue()) {
//					if (record.getGraduationCertificateNo() == null) {
//						sb.append("毕业证书：待上传");
//					} else {
//						sb.append("毕业证书：" + record.getGraduationCertificateNo());
//					}
//					if (record.getDegreeCertificateNo() == null) {
//						sb.append("\n学位证书：待上传");
//					} else {
//						sb.append("\n学位证书：" + record.getDegreeCertificateNo());
//					}
//				} else {
//					sb.append("--");
//				}
//				cell.setCellValue(sb.toString());
//				sb.delete(0, sb.length());
//
//				//增加单元格的高度 以能够容纳6行字
//		        row.setHeightInPoints(6 * sheet.getDefaultRowHeightInPoints());
//			}
//
//			sheet.setColumnWidth(0, 10000);
//			sheet.setColumnWidth(1, 10000);
//			sheet.setColumnWidth(2, 10000);
//			sheet.setColumnWidth(3, 10000);
//			sheet.setColumnWidth(4, 10000);
//			sheet.setColumnWidth(5, 10000);
//			sheet.setColumnWidth(6, 10000);
//
//			return wb;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * 设置让浏览器弹出下载[Excel文件]对话框的Header.
//	 * @param response
//	 * @param workbook
//	 * @param outputFileName
//	 * @throws IOException
//     */
//	protected void downloadExcelFile(HttpServletResponse response,
//									 HSSFWorkbook workbook, String outputFileName) throws IOException {
//		if(workbook != null) {
//			response.setContentType("application/x-msdownload;charset=utf-8");
//			response.setHeader("Content-Disposition", "attachment; filename=" + new String(outputFileName.getBytes("UTF-8"), "ISO8859-1"));
//			workbook.write(response.getOutputStream());
//		} else {
//			response.setContentType("text/html;charset=UTF-8");
//			response.getWriter().write("<script type='application/javascript'>/*自动关闭当前窗口*/window.close();</script>");
//			response.getWriter().flush();
//		}
//	}

}
