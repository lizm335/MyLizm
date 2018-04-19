package com.gzedu.xlims.web.controller.graduation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApply;
import com.gzedu.xlims.pojo.graduation.GjtGraduationBatch;
import com.gzedu.xlims.pojo.graduation.GjtGraduationGuideRecord;
import com.gzedu.xlims.pojo.graduation.GjtGraduationStudentProg;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.pojo.status.GraduationApplyStatusEnum;
import com.gzedu.xlims.pojo.status.GraduationProgressCodeEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyService;
import com.gzedu.xlims.service.graduation.GjtGraduationBatchService;
import com.gzedu.xlims.service.graduation.GjtGraduationGuideRecordService;
import com.gzedu.xlims.service.graduation.GjtGraduationStudentProgService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/graduation/apply")
public class GjtGraduationApplyController {

	private static final Log log = LogFactory.getLog(GjtGraduationApplyController.class);

	@Autowired
	private GjtGraduationApplyService gjtGraduationApplyService;

	@Autowired
	private GjtGraduationBatchService gjtGraduationBatchService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtGraduationStudentProgService gjtGraduationStudentProgService;

	@Autowired
	private GjtGraduationGuideRecordService gjtGraduationGuideRecordService;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;
	
	@Autowired
	private GjtGradeService gjtGradeService;

	/**
	 * 查询毕业申请列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		Page<?> pageInfo = gjtGraduationApplyService.queryAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);

		model.addAttribute("thesisStatusMap", EnumUtil.getThesisApplyStatusMap());
		model.addAttribute("practiceStatusMap", EnumUtil.getPracticeApplyStatusMap());
		model.addAttribute("batchMap", gjtGraduationBatchService.getGraduationBatchMap(user.getGjtOrg().getId()));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId()));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(user.getGjtOrg().getId()));
		model.addAttribute("trainingLevelMap", commonMapService.getPyccMap());

		return "graduation/apply/list";
	}

	/**
	 * 查询学生申请详情
	 * 
	 * @param studentId
	 * @param batchId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(@RequestParam("studentId") String studentId, @RequestParam("batchId") String batchId,
			Model model, HttpServletRequest request) {
		log.info("studentId:" + studentId + ", batchId:" + batchId);

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(studentId);

		// 论文申请记录
		GjtGraduationApply thesisApply = gjtGraduationApplyService.queryOneByStudent(batchId, studentId, 1);
		List<GjtGraduationStudentProg> thesisProgList = gjtGraduationStudentProgService.queryListByStudent(batchId,
				studentId, 1);
		List<GjtGraduationGuideRecord> thesisRecordList = gjtGraduationGuideRecordService.queryListByStudent(batchId,
				studentId, 1);

		// 构建进度和进度对应的指导记录
		Map<String, List<GjtGraduationGuideRecord>> thesisProgRecord = new HashMap<String, List<GjtGraduationGuideRecord>>();
		if (thesisProgList != null && thesisProgList.size() > 0 && thesisRecordList != null
				&& thesisRecordList.size() > 0) {
			for (GjtGraduationStudentProg prog : thesisProgList) {
				for (GjtGraduationGuideRecord record : thesisRecordList) {
					if (record.getProgressCode().equals(prog.getProgressCode())) {
						List<GjtGraduationGuideRecord> records = thesisProgRecord.get(prog.getProgressId());
						if (records == null) {
							records = new ArrayList<GjtGraduationGuideRecord>();
							records.add(record);
							thesisProgRecord.put(prog.getProgressId(), records);
						} else {
							records.add(record);
						}
					}
				}
			}
		}

		// 社会实践申请记录
		GjtGraduationApply practiceApply = gjtGraduationApplyService.queryOneByStudent(batchId, studentId, 2);
		List<GjtGraduationStudentProg> practiceProgList = gjtGraduationStudentProgService.queryListByStudent(batchId,
				studentId, 2);
		List<GjtGraduationGuideRecord> practiceRecordList = gjtGraduationGuideRecordService.queryListByStudent(batchId,
				studentId, 2);

		// 构建进度和进度对应的指导记录
		Map<String, List<GjtGraduationGuideRecord>> practiceProgRecord = new HashMap<String, List<GjtGraduationGuideRecord>>();
		if (practiceProgList != null && practiceProgList.size() > 0 && practiceRecordList != null
				&& practiceRecordList.size() > 0) {
			for (GjtGraduationStudentProg prog : practiceProgList) {
				for (GjtGraduationGuideRecord record : practiceRecordList) {
					if (record.getProgressCode().equals(prog.getProgressCode())) {
						List<GjtGraduationGuideRecord> records = practiceProgRecord.get(prog.getProgressId());
						if (records == null) {
							records = new ArrayList<GjtGraduationGuideRecord>();
							records.add(record);
							practiceProgRecord.put(prog.getProgressId(), records);
						} else {
							records.add(record);
						}
					}
				}
			}
		}

		model.addAttribute("studentInfo", gjtStudentInfo);
		model.addAttribute("thesisApply", thesisApply);
		model.addAttribute("thesisProgList", thesisProgList);
		model.addAttribute("thesisProgRecord", thesisProgRecord);
		model.addAttribute("practiceApply", practiceApply);
		model.addAttribute("practiceProgList", practiceProgList);
		model.addAttribute("practiceProgRecord", practiceProgRecord);
		model.addAttribute("trainingLevelMap", commonMapService.getPyccMap());
		model.addAttribute("thesisProgressCodeMap", EnumUtil.getThesisProgressCodeMap());
		model.addAttribute("practiceProgressCodeMap", EnumUtil.getPracticeProgressCodeMap());

		// 指导老师列表
		List<GjtEmployeeInfo> thesisGuideTeacherList = gjtEmployeeInfoService.findListByType(
				EmployeeTypeEnum.论文教师.getNum(), EmployeeTypeEnum.论文指导教师.getNum(), user.getGjtOrg().getId());
		List<GjtEmployeeInfo> practiceGuideTeacherList = gjtEmployeeInfoService.findListByType(
				EmployeeTypeEnum.论文教师.getNum(), EmployeeTypeEnum.社会实践教师.getNum(), user.getGjtOrg().getId());
		model.addAttribute("thesisGuideTeacherList", thesisGuideTeacherList);
		model.addAttribute("practiceGuideTeacherList", practiceGuideTeacherList);

		return "graduation/apply/view";
	}

	/**
	 * 修改指导老师
	 * 
	 * @param applyId
	 * @param teacherId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "modifyGuideTeacher", method = RequestMethod.POST)
	public String modifyGuideTeacher(@RequestParam("applyId") String applyId,
			@RequestParam("teacherId") String teacherId, HttpServletRequest request) {
		log.info("applyId:" + applyId + ", teacherId:" + teacherId);

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtGraduationApply graduationApply = gjtGraduationApplyService.queryOne(applyId);
		graduationApply.setGuideTeacher(teacherId);
		graduationApply.setUpdatedBy(user.getId());

		// 当状态等于“已申请”的时候，表示为分配指导老师操作
		if (graduationApply.getApplyType() == 1
				&& graduationApply.getStatus() == GraduationApplyStatusEnum.THESIS_APPLY.getValue()) {
			graduationApply.setStatus(GraduationApplyStatusEnum.THESIS_STAY_OPEN.getValue());

			// 保存学生的进度
			GjtGraduationStudentProg progress = new GjtGraduationStudentProg();
			progress.setCreatedBy(user.getId());
			progress.setStudentId(graduationApply.getGjtStudentInfo().getStudentId());
			progress.setGjtGraduationBatch(graduationApply.getGjtGraduationBatch());
			progress.setProgressType(1);
			progress.setProgressCode(GraduationProgressCodeEnum.THESIS_STAY_OPEN.getCode());
			gjtGraduationStudentProgService.insert(progress);
		} else if (graduationApply.getApplyType() == 2
				&& graduationApply.getStatus() == GraduationApplyStatusEnum.PRACTICE_APPLY.getValue()) {
			graduationApply.setStatus(GraduationApplyStatusEnum.PRACTICE_STAY_OPEN.getValue());

			// 保存学生的进度
			GjtGraduationStudentProg progress = new GjtGraduationStudentProg();
			progress.setCreatedBy(user.getId());
			progress.setStudentId(graduationApply.getGjtStudentInfo().getStudentId());
			progress.setGjtGraduationBatch(graduationApply.getGjtGraduationBatch());
			progress.setProgressType(2);
			progress.setProgressCode(GraduationProgressCodeEnum.PRACTICE_STAY_OPEN.getCode());
			gjtGraduationStudentProgService.insert(progress);
		}

		// 更新申请记录
		gjtGraduationApplyService.update(graduationApply);

		return "redirect:/graduation/apply/view?studentId=" + graduationApply.getGjtStudentInfo().getStudentId()
				+ "&batchId=" + graduationApply.getGjtGraduationBatch().getBatchId();
	}

	/**
	 * 修改答辩方式
	 * 
	 * @param applyId
	 * @param defence
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "modifyDefence", method = RequestMethod.POST)
	public String modifyDefence(@RequestParam("applyId") String applyId, @RequestParam("defence") int defence,
			HttpServletRequest request) {
		log.info("applyId:" + applyId + ", defence:" + defence);

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtGraduationApply graduationApply = gjtGraduationApplyService.queryOne(applyId);
		graduationApply.setNeedDefence(defence);
		graduationApply.setUpdatedBy(user.getId());

		// 更新申请记录
		gjtGraduationApplyService.update(graduationApply);

		return "redirect:/graduation/apply/view?studentId=" + graduationApply.getGjtStudentInfo().getStudentId()
				+ "&batchId=" + graduationApply.getGjtGraduationBatch().getBatchId();
	}

	/**
	 * 批量修改答辩方式
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "importModifyDefence")
	@ResponseBody
	public ImportFeedback importModifyDefence(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "学号", "姓名", "专业", "年级", "层次", "论文批次", "状态", "答辩方式", "导入结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}

			if (dataList != null && dataList.size() > 0) {
				for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据

					if ("".equals(datas[0])) {
						result[heads.length - 1] = "学号为空";
						failedList.add(result);
					} else {
						int defence = 0;
						if ("现场答辩".equals(datas[7])) {
							defence = 1;
						} else if ("远程答辩".equals(datas[7])) {
							defence = 2;
						} else {
							result[heads.length - 1] = "答辩方式不正确";
							failedList.add(result);
							continue;
						}

						GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(datas[0]);
						if (studentInfo == null) {
							result[heads.length - 1] = "找不到对应学生";
							failedList.add(result);
						} else {
							if (!"2".equals(studentInfo.getPycc()) && !"8".equals(studentInfo.getPycc())) {
								result[heads.length - 1] = "该学生无需答辩";
								failedList.add(result);
								continue;
							}

							GjtGrade grade = gjtGradeService.findCurrentGrade(user.getGjtOrg().getId());
							GjtGraduationBatch graduationBatch = gjtGraduationBatchService.findByGradeId(grade.getGradeId(),
									user.getGjtOrg().getId());
							if (graduationBatch == null) {
								result[heads.length - 1] = "找不到对应毕业批次";
								failedList.add(result);
							} else {
								GjtGraduationApply graduationApply = gjtGraduationApplyService
										.queryOneByStudent(graduationBatch.getBatchId(), studentInfo.getStudentId(), 1);
								if (graduationApply == null) {
									result[heads.length - 1] = "找不到对应申请记录";
									failedList.add(result);
								} else {
									if (graduationApply.getStatus() < GraduationApplyStatusEnum.THESIS_STAY_DEFENCE
											.getValue()) {
										graduationApply.setNeedDefence(defence);
										graduationApply.setUpdatedBy(user.getId());
										gjtGraduationApplyService.update(graduationApply);

										result[heads.length - 1] = "成功";
										successList.add(result);
									} else {
										result[heads.length - 1] = "学生论文进度不符";
										failedList.add(result);
									}
								}
							}
						}
					}
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "importModifyDefence_success_" + currentTimeMillis + ".xls";
			String failedFileName = "importModifyDefence_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "答辩方式导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "答辩方式导入失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "graduationApply" + File.separator;
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

			return new ImportFeedback(true, dataList.size(), successList.size(), failedList.size(), successFileName,
					failedFileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ImportFeedback(false, "系统异常！");
		}
	}

	/**
	 * 批量导入初评成绩
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "importReviewScore")
	@ResponseBody
	public ImportFeedback importReviewScore(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "学号", "姓名", "专业", "年级", "层次", "论文批次", "状态", "初评成绩", "导入结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}

			if (dataList != null && dataList.size() > 0) {
				for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据

					if ("".equals(datas[0])) {
						result[heads.length - 1] = "学号为空";
						failedList.add(result);
					} else {
						float reviewScore = -1;
						try {
							reviewScore = Float.parseFloat(datas[7]);
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							result[heads.length - 1] = "初评成绩必须是数字";
							failedList.add(result);
							continue;
						}
						if (reviewScore < 0 || reviewScore > 100) {
							result[heads.length - 1] = "初评成绩必须为0到100之间的数字";
							failedList.add(result);
							continue;
						}

						GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(datas[0]);
						if (studentInfo == null) {
							result[heads.length - 1] = "找不到对应学生";
							failedList.add(result);
						} else {
							GjtGrade grade = gjtGradeService.findCurrentGrade(user.getGjtOrg().getId());
							GjtGraduationBatch graduationBatch = gjtGraduationBatchService.findByGradeId(grade.getGradeId(),
									user.getGjtOrg().getId());
							if (graduationBatch == null) {
								result[heads.length - 1] = "找不到对应毕业批次";
								failedList.add(result);
							} else {
								GjtGraduationApply graduationApply = gjtGraduationApplyService
										.queryOneByStudent(graduationBatch.getBatchId(), studentInfo.getStudentId(), 1);
								if (graduationApply == null) {
									result[heads.length - 1] = "找不到对应申请记录";
									failedList.add(result);
								} else {
									if (graduationApply.getStatus() == GraduationApplyStatusEnum.THESIS_REVIEW
											.getValue()) {
										graduationApply.setReviewScore(reviewScore);
										graduationApply
												.setStatus(GraduationApplyStatusEnum.THESIS_STAY_DEFENCE.getValue());
										graduationApply.setUpdatedBy(user.getId());
										gjtGraduationApplyService.update(graduationApply);

										GjtGraduationStudentProg prog = new GjtGraduationStudentProg();
										prog.setStudentId(graduationApply.getGjtStudentInfo().getStudentId());
										prog.setProgressType(graduationApply.getApplyType());
										prog.setGjtGraduationBatch(graduationApply.getGjtGraduationBatch());
										prog.setCreatedBy(user.getId());
										prog.setProgressCode(GraduationProgressCodeEnum.THESIS_REVIEW.getCode());
										gjtGraduationStudentProgService.insert(prog);

										result[heads.length - 1] = "成功";
										successList.add(result);
									} else {
										result[heads.length - 1] = "学生论文进度不符";
										failedList.add(result);
									}
								}
							}
						}
					}
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "importReviewScore_success_" + currentTimeMillis + ".xls";
			String failedFileName = "importReviewScore_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "初评成绩导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "初评成绩导入失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "graduationApply" + File.separator;
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

			return new ImportFeedback(true, dataList.size(), successList.size(), failedList.size(), successFileName,
					failedFileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ImportFeedback(false, "系统异常！");
		}
	}

	/**
	 * 学院定稿
	 * 
	 * @param applyId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "confirm", method = RequestMethod.GET)
	public String confirm(@RequestParam("applyId") String applyId, HttpServletRequest request) {
		log.info("applyId:" + applyId);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtGraduationApply graduationApply = gjtGraduationApplyService.queryOne(applyId);
		if (graduationApply.getStatus() == GraduationApplyStatusEnum.THESIS_TEACHER_CONFIRM_THESIS.getValue()) {
			graduationApply.setStatus(GraduationApplyStatusEnum.THESIS_COLLEGE_CONFIRM_THESIS.getValue());
			graduationApply.setUpdatedBy(user.getId());
			gjtGraduationApplyService.update(graduationApply);

			GjtGraduationStudentProg prog = new GjtGraduationStudentProg();
			prog.setStudentId(graduationApply.getGjtStudentInfo().getStudentId());
			prog.setProgressType(graduationApply.getApplyType());
			prog.setGjtGraduationBatch(graduationApply.getGjtGraduationBatch());
			prog.setCreatedBy(user.getId());
			prog.setProgressCode(GraduationProgressCodeEnum.THESIS_COLLEGE_CONFIRM_THESIS.getCode());
			gjtGraduationStudentProgService.insert(prog);
		}

		return "redirect:/graduation/apply/view?studentId=" + graduationApply.getGjtStudentInfo().getStudentId()
				+ "&batchId=" + graduationApply.getGjtGraduationBatch().getBatchId();
	}

	/**
	 * 批量定稿
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "importConfirm")
	@ResponseBody
	public ImportFeedback importConfirm(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "学号", "姓名", "专业", "年级", "层次", "论文批次", "状态", "导入结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}

			if (dataList != null && dataList.size() > 0) {
				for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据

					if ("".equals(datas[0])) {
						result[heads.length - 1] = "学号为空";
						failedList.add(result);
					} else {
						GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(datas[0]);
						if (studentInfo == null) {
							result[heads.length - 1] = "找不到对应学生";
							failedList.add(result);
						} else {
							GjtGrade grade = gjtGradeService.findCurrentGrade(user.getGjtOrg().getId());
							GjtGraduationBatch graduationBatch = gjtGraduationBatchService.findByGradeId(grade.getGradeId(),
									user.getGjtOrg().getId());
							if (graduationBatch == null) {
								result[heads.length - 1] = "找不到对应毕业批次";
								failedList.add(result);
							} else {
								GjtGraduationApply graduationApply = gjtGraduationApplyService
										.queryOneByStudent(graduationBatch.getBatchId(), studentInfo.getStudentId(), 1);
								if (graduationApply == null) {
									result[heads.length - 1] = "找不到对应申请记录";
									failedList.add(result);
								} else {
									if (graduationApply
											.getStatus() == GraduationApplyStatusEnum.THESIS_TEACHER_CONFIRM_THESIS
													.getValue()) {
										graduationApply.setStatus(
												GraduationApplyStatusEnum.THESIS_COLLEGE_CONFIRM_THESIS.getValue());
										graduationApply.setUpdatedBy(user.getId());
										gjtGraduationApplyService.update(graduationApply);

										GjtGraduationStudentProg prog = new GjtGraduationStudentProg();
										prog.setStudentId(graduationApply.getGjtStudentInfo().getStudentId());
										prog.setProgressType(graduationApply.getApplyType());
										prog.setGjtGraduationBatch(graduationApply.getGjtGraduationBatch());
										prog.setCreatedBy(user.getId());
										prog.setProgressCode(
												GraduationProgressCodeEnum.THESIS_COLLEGE_CONFIRM_THESIS.getCode());
										gjtGraduationStudentProgService.insert(prog);

										result[heads.length - 1] = "成功";
										successList.add(result);
									} else {
										result[heads.length - 1] = "学生论文进度不符";
										failedList.add(result);
									}
								}
							}
						}
					}
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "importConfirm_success_" + currentTimeMillis + ".xls";
			String failedFileName = "importConfirm_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "学院定稿导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "学院定稿导入失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "graduationApply" + File.separator;
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

			return new ImportFeedback(true, dataList.size(), successList.size(), failedList.size(), successFileName,
					failedFileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ImportFeedback(false, "系统异常！");
		}

	}

	/**
	 * 审核不通过
	 * 
	 * @param applyId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "reject", method = RequestMethod.GET)
	public String reject(@RequestParam("applyId") String applyId, HttpServletRequest request) {
		log.info("applyId:" + applyId);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtGraduationApply graduationApply = gjtGraduationApplyService.queryOne(applyId);
		if (graduationApply.getStatus() == GraduationApplyStatusEnum.THESIS_TEACHER_CONFIRM_THESIS.getValue()) {
			graduationApply.setStatus(GraduationApplyStatusEnum.THESIS_SUBMIT_THESIS.getValue());
			graduationApply.setUpdatedBy(user.getId());
			gjtGraduationApplyService.update(graduationApply);
		}

		return "redirect:/graduation/apply/view?studentId=" + graduationApply.getGjtStudentInfo().getStudentId()
				+ "&batchId=" + graduationApply.getGjtGraduationBatch().getBatchId();
	}

	/**
	 * 批量审核不通过
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "importReject")
	@ResponseBody
	public ImportFeedback importReject(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "学号", "姓名", "专业", "年级", "层次", "论文批次", "状态", "导入结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}

			if (dataList != null && dataList.size() > 0) {
				for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据

					if ("".equals(datas[0])) {
						result[heads.length - 1] = "学号为空";
						failedList.add(result);
					} else {
						GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(datas[0]);
						if (studentInfo == null) {
							result[heads.length - 1] = "找不到对应学生";
							failedList.add(result);
						} else {
							GjtGrade grade = gjtGradeService.findCurrentGrade(user.getGjtOrg().getId());
							GjtGraduationBatch graduationBatch = gjtGraduationBatchService.findByGradeId(grade.getGradeId(),
									user.getGjtOrg().getId());
							if (graduationBatch == null) {
								result[heads.length - 1] = "找不到对应毕业批次";
								failedList.add(result);
							} else {
								GjtGraduationApply graduationApply = gjtGraduationApplyService
										.queryOneByStudent(graduationBatch.getBatchId(), studentInfo.getStudentId(), 1);
								if (graduationApply == null) {
									result[heads.length - 1] = "找不到对应申请记录";
									failedList.add(result);
								} else {
									if (graduationApply
											.getStatus() == GraduationApplyStatusEnum.THESIS_TEACHER_CONFIRM_THESIS
													.getValue()) {
										graduationApply
												.setStatus(GraduationApplyStatusEnum.THESIS_SUBMIT_THESIS.getValue());
										graduationApply.setUpdatedBy(user.getId());
										gjtGraduationApplyService.update(graduationApply);

										result[heads.length - 1] = "成功";
										successList.add(result);
									} else {
										result[heads.length - 1] = "学生论文进度不符";
										failedList.add(result);
									}
								}
							}
						}
					}
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "importReject_success_" + currentTimeMillis + ".xls";
			String failedFileName = "importReject_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "审核不通过导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "审核不通过导入失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "graduationApply" + File.separator;
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

			return new ImportFeedback(true, dataList.size(), successList.size(), failedList.size(), successFileName,
					failedFileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ImportFeedback(false, "系统异常！");
		}
	}

	/**
	 * 导出明细
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileName = "毕业申请学生明细.xls";
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		Page<?> pageInfo = gjtGraduationApplyService.queryAll(searchParams, pageRequst);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) pageInfo.getContent();
		HSSFWorkbook workbook = this.getWorkbook(list);
		this.downloadExcelFile(response, workbook, fileName);
	}

	private HSSFWorkbook getWorkbook(List<Map<String, Object>> list) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("毕业申请学生明细");
			String[] heads = { "学号", "姓名", "手机号", "邮箱", "报读信息", "毕业批次", "毕业论文", "社会实践", "是否申请学位" };

			Map<Integer, String> thesisStatusMap = EnumUtil.getThesisApplyStatusMap();
			Map<Integer, String> practiceStatusMap = EnumUtil.getPracticeApplyStatusMap();

			HSSFRow row = sheet.createRow(0);
			for (int i = 0; i < heads.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(heads[i]);
			}

			int rowIdx = 1;
			int colIdx = 0;
			HSSFCell cell;
			StringBuffer sb = new StringBuffer();

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行

			// 为了能够使用换行，需要设置单元格的样式 wrap=true
			HSSFCellStyle s = wb.createCellStyle();
			s.setWrapText(true);

			for (Map<String, Object> map : list) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				sb.append((map.get("studentCode") == null ? "--" : map.get("studentCode").toString()));
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				sb.append(map.get("studentName") == null ? "--" : map.get("studentName").toString());
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				sb.append(map.get("phone") == null ? "--" : map.get("phone").toString());
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				sb.append(map.get("email") == null ? "--" : map.get("email").toString());
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				sb.append("层次：" + (map.get("trainingLevel") == null ? "--" : map.get("trainingLevel").toString()));
				sb.append("\n年级：" + (map.get("grade") == null ? "--" : map.get("grade").toString()));
				sb.append("\n专业：" + (map.get("specialtyName") == null ? "--" : map.get("specialtyName").toString()));
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				sb.append(map.get("batchName1") == null ? map.get("batchName2").toString()
						: map.get("batchName1").toString());
				sb.append("(");
				sb.append(map.get("batchCode1") == null ? map.get("batchCode2").toString()
						: map.get("batchCode1").toString());
				sb.append(")");
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (map.get("applyId1") == null) {
					sb.append("答辩形式：--");
					sb.append("\n指导老师：--");
					sb.append("\n答辩老师：--");
					sb.append("\n初评成绩：--");
					sb.append("\n答辩成绩：--");
					sb.append("\n状态：未申请");
				} else {
					if (Integer.parseInt(map.get("needDefence1").toString()) == 0) {
						sb.append("答辩形式：无需答辩");
					} else if (Integer.parseInt(map.get("needDefence1").toString()) == 1) {
						sb.append("答辩形式：现场答辩");
					} else if (Integer.parseInt(map.get("needDefence1").toString()) == 2) {
						sb.append("答辩形式：远程答辩");
					}
					sb.append("\n指导老师："
							+ (map.get("guideTeacherName1") == null ? "--" : map.get("guideTeacherName1").toString()));
					sb.append("\n答辩老师：" + (map.get("defenceTeacherName1") == null ? "--"
							: map.get("defenceTeacherName1").toString()));
					sb.append(
							"\n初评成绩：" + (map.get("reviewScore1") == null ? "--" : map.get("reviewScore1").toString()));
					sb.append("\n答辩成绩："
							+ (map.get("defenceScore1") == null ? "--" : map.get("defenceScore1").toString()));
					sb.append("\n状态：" + thesisStatusMap.get(Integer.parseInt(map.get("status1").toString())));
				}
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (map.get("applyId2") == null) {
					sb.append("指导老师：--");
					sb.append("\n实践成绩：--");
					sb.append("\n状态：未申请");
				} else {
					sb.append("指导老师："
							+ (map.get("guideTeacherName2") == null ? "--" : map.get("guideTeacherName2").toString()));
					sb.append(
							"\n实践成绩：" + (map.get("reviewScore2") == null ? "--" : map.get("reviewScore2").toString()));
					sb.append("\n状态：" + practiceStatusMap.get(Integer.parseInt(map.get("status2").toString())));
				}
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				Object applyDegree1 = map.get("applyDegree1");
				if (applyDegree1 == null) {
					sb.append("--");
				} else if (Integer.parseInt(applyDegree1.toString()) == 0) {
					sb.append("否");
				} else if (Integer.parseInt(applyDegree1.toString()) == 1) {
					sb.append("是");
				}
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());

				// 增加单元格的高度 以能够容纳6行字
				row.setHeightInPoints(6 * sheet.getDefaultRowHeightInPoints());
			}

			sheet.setColumnWidth(0, 10000);
			sheet.setColumnWidth(1, 10000);
			sheet.setColumnWidth(2, 10000);
			sheet.setColumnWidth(3, 10000);
			sheet.setColumnWidth(4, 10000);
			sheet.setColumnWidth(5, 10000);
			sheet.setColumnWidth(6, 10000);

			return wb;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 导出指导老师定稿状态的论文
	 * 
	 * @param fileName
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "exportTeacherConfirmThesis", method = RequestMethod.GET)
	public void exportTeacherConfirmThesis(@RequestParam String fileName, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		InputStream fis = GjtGraduationApplyController.class
				.getResourceAsStream(WebConstants.EXCEL_MODEL_URL + fileName);
		HSSFWorkbook workbook = new HSSFWorkbook(fis);

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtGrade grade = gjtGradeService.findCurrentGrade(user.getGjtOrg().getId());
		GjtGraduationBatch graduationBatch = gjtGraduationBatchService.findByGradeId(grade.getGradeId(),
				user.getGjtOrg().getId());

		if (graduationBatch != null) {
			PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_orgId", user.getGjtOrg().getId());
			searchParams.put("EQ_batchId", graduationBatch.getBatchId());
			searchParams.put("EQ_status1", "5");
			Page<?> pageInfo = gjtGraduationApplyService.queryAll(searchParams, pageRequst);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> list = (List<Map<String, Object>>) pageInfo.getContent();
			this.fillData(workbook, list);
		}

		this.downloadExcelFile(response, workbook, fileName);
		fis.close();
	}

	/**
	 * 导出初评中状态的论文
	 * 
	 * @param fileName
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "exportReviewThesis", method = RequestMethod.GET)
	public void exportReviewThesis(@RequestParam String fileName, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		InputStream fis = GjtGraduationApplyController.class
				.getResourceAsStream(WebConstants.EXCEL_MODEL_URL + fileName);
		HSSFWorkbook workbook = new HSSFWorkbook(fis);

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtGrade grade = gjtGradeService.findCurrentGrade(user.getGjtOrg().getId());
		GjtGraduationBatch graduationBatch = gjtGraduationBatchService.findByGradeId(grade.getGradeId(),
				user.getGjtOrg().getId());

		if (graduationBatch != null) {
			PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_orgId", user.getGjtOrg().getId());
			searchParams.put("EQ_batchId", graduationBatch.getBatchId());
			searchParams.put("EQ_status1", "7");
			Page<?> pageInfo = gjtGraduationApplyService.queryAll(searchParams, pageRequst);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> list = (List<Map<String, Object>>) pageInfo.getContent();
			this.fillData(workbook, list);
		}

		this.downloadExcelFile(response, workbook, fileName);
		fis.close();
	}

	private void fillData(HSSFWorkbook workbook, List<Map<String, Object>> list) {
		if (list != null && list.size() > 0) {
			Map<Integer, String> thesisStatusMap = EnumUtil.getThesisApplyStatusMap();
			HSSFSheet sheet = workbook.getSheetAt(0);
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> obj = list.get(i);
				HSSFRow row = sheet.createRow(i + 3);

				HSSFCell cell = row.createCell(0);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("studentCode") == null ? "--" : obj.get("studentCode").toString());

				cell = row.createCell(1);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("studentName") == null ? "--" : obj.get("studentName").toString());

				cell = row.createCell(2);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("specialtyName") == null ? "--" : obj.get("specialtyName").toString());

				cell = row.createCell(3);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("grade") == null ? "--" : obj.get("grade").toString());

				cell = row.createCell(4);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("trainingLevel") == null ? "--" : obj.get("trainingLevel").toString());

				String batchName = obj.get("batchName1") == null ? "--" : obj.get("batchName1").toString();
				String batchCode = obj.get("batchCode1") == null ? "--" : obj.get("batchCode1").toString();
				cell = row.createCell(5);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(batchName + "(" + batchCode + ")");

				String status = obj.get("status1") == null ? "--"
						: thesisStatusMap.get(Integer.parseInt(obj.get("status1").toString()));
				cell = row.createCell(6);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(status);
			}
		}
	}

	/**
	 * 设置让浏览器弹出下载[Excel文件]对话框的Header.
	 * 
	 * @param response
	 * @param workbook
	 * @param outputFileName
	 * @throws IOException
	 */
	protected void downloadExcelFile(HttpServletResponse response, HSSFWorkbook workbook, String outputFileName)
			throws IOException {
		if (workbook != null) {
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(outputFileName.getBytes("UTF-8"), "ISO8859-1"));
			workbook.write(response.getOutputStream());
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("<script type='application/javascript'>/*自动关闭当前窗口*/window.close();</script>");
			response.getWriter().flush();
		}
	}

}
