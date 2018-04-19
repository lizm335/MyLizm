package com.gzedu.xlims.web.controller.practice;

import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.json.JsonUtils;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.practice.GjtPracticeApply;
import com.gzedu.xlims.pojo.practice.GjtPracticeGuideRecord;
import com.gzedu.xlims.pojo.practice.GjtPracticePlan;
import com.gzedu.xlims.pojo.practice.GjtPracticeStudentProg;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.pojo.status.PracticeProgressCodeEnum;
import com.gzedu.xlims.pojo.status.PracticeStatusEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.practice.GjtPracticeApplyService;
import com.gzedu.xlims.service.practice.GjtPracticeGuideRecordService;
import com.gzedu.xlims.service.practice.GjtPracticePlanService;
import com.gzedu.xlims.service.practice.GjtPracticeStudentProgService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/practiceApply")
public class GjtPracticeApplyController {

	private final static Logger log = LoggerFactory.getLogger(GjtPracticeApplyController.class);

	@Autowired
	private GjtPracticePlanService gjtPracticePlanService;

	@Autowired
	private GjtPracticeApplyService gjtPracticeApplyService;

	@Autowired
	private GjtPracticeStudentProgService gjtPracticeStudentProgService;

	@Autowired
	private GjtPracticeGuideRecordService gjtPracticeGuideRecordService;

	@Autowired
	private GjtOrgService gjtOrgService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}

		searchParams.put("EQ_gjtPracticePlan.orgId", orgId);

		// 默认选择当前社会实践计划
		if (EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EQ_practicePlanId"))) {
			String gradeId = gjtGradeService.getCurrentGradeId(orgId);
			GjtPracticePlan practicePlan = gjtPracticePlanService.findByGradeIdAndOrgIdAndStatus(gradeId, orgId, 3);
			if (practicePlan != null) {
				searchParams.put("EQ_practicePlanId", practicePlan.getPracticePlanId());
				model.addAttribute("practicePlanId", practicePlan.getPracticePlanId());
			}
		} else if (EmptyUtils.isNotEmpty(searchParams)
				&& EmptyUtils.isNotEmpty(searchParams.get("EQ_practicePlanId"))) {
			model.addAttribute("practicePlanId", ObjectUtils.toString(searchParams.get("EQ_practicePlanId")));
		}

		Page<GjtPracticeApply> pageInfo = gjtPracticeApplyService.findAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("practicePlanMap", gjtPracticePlanService.getPracticePlanMap(orgId));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(orgId));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyBaseMap(orgId));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("practiceStatusMap", EnumUtil.getPracticeApplyStatusMap());

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnView", subject.isPermitted("/practiceApply/list$view"));
		model.addAttribute("isBtnExport", subject.isPermitted("/practiceApply/list$export"));
		model.addAttribute("isBtnConfirmExpress", subject.isPermitted("/practiceApply/list$confirmExpress"));

		return "practice/practiceApply_list";
	}

	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(@RequestParam("practicePlanId") String practicePlanId,
			@RequestParam("studentId") String studentId, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}

		GjtPracticeApply practiceApply = gjtPracticeApplyService.findByPracticePlanIdAndStudentId(practicePlanId,
				studentId);
		List<GjtPracticeStudentProg> studentProgs = gjtPracticeStudentProgService
				.findByPracticePlanIdAndStudentId(practicePlanId, studentId);
		List<GjtPracticeGuideRecord> guideRecords = gjtPracticeGuideRecordService
				.findByPracticePlanIdAndStudentId(practicePlanId, studentId, "Asc");

		// 构建进度和进度对应的指导记录
		Map<String, List<GjtPracticeGuideRecord>> studentProgRecord = new HashMap<String, List<GjtPracticeGuideRecord>>();
		if (studentProgs != null && studentProgs.size() > 0 && guideRecords != null && guideRecords.size() > 0) {
			for (GjtPracticeStudentProg studentProg : studentProgs) {
				for (GjtPracticeGuideRecord guideRecord : guideRecords) {
					if (guideRecord.getProgressCode().equals(studentProg.getProgressCode())) {
						List<GjtPracticeGuideRecord> records = studentProgRecord.get(studentProg.getProgressId());
						if (records == null) {
							records = new ArrayList<GjtPracticeGuideRecord>();
							records.add(guideRecord);
							studentProgRecord.put(studentProg.getProgressId(), records);
						} else {
							records.add(guideRecord);
						}
					}
				}
			}
		}

		model.addAttribute("practiceApply", practiceApply);
		model.addAttribute("studentProgs", studentProgs);
		model.addAttribute("studentProgRecord", studentProgRecord);
		model.addAttribute("practiceStatusMap", EnumUtil.getPracticeApplyStatusMap());
		model.addAttribute("practiceProgressCodeMap", EnumUtil.getPracticeProgressCodeMap());
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("expressMap", EnumUtil.getExpressMap());

		List<GjtEmployeeInfo> guideTeachers = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(),
				EmployeeTypeEnum.社会实践教师.getNum(), orgId);
		model.addAttribute("guideTeachers", guideTeachers);

		return "practice/practiceApply_detail";
	}

	@RequestMapping(value = "updateGuideTeacher", method = RequestMethod.POST)
	public String updateGuideTeacher(@RequestParam("applyId") String applyId,
			@RequestParam("teacherId") String teacherId, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		GjtPracticeApply practiceApply = gjtPracticeApplyService.findOne(applyId);
		practiceApply.setGuideTeacher(teacherId);
		practiceApply.setUpdatedBy(user.getId());

		// 当状态等于“已申请”的时候，表示为分配指导老师操作
		if (practiceApply.getStatus() == PracticeStatusEnum.PRACTICE_APPLY.getValue()) {
			practiceApply.setStatus(PracticeStatusEnum.PRACTICE_STAY_OPEN.getValue());

			// 添加进度
			GjtPracticeStudentProg prog = new GjtPracticeStudentProg();
			prog.setStudentId(practiceApply.getStudentId());
			prog.setPracticePlanId(practiceApply.getPracticePlanId());
			prog.setProgressCode(PracticeProgressCodeEnum.PRACTICE_STAY_OPEN.getCode());
			prog.setCreatedBy(user.getId());
			gjtPracticeStudentProgService.insert(prog);
		}

		gjtPracticeApplyService.update(practiceApply);

		return "redirect:/practiceApply/view?practicePlanId=" + practiceApply.getPracticePlanId() + "&studentId="
				+ practiceApply.getStudentId();
	}

	@RequestMapping(value = "export", method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}

		searchParams.put("EQ_gjtPracticePlan.orgId", orgId);

		Page<GjtPracticeApply> pageInfo = gjtPracticeApplyService.findAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		HSSFWorkbook workbook = this.getWorkbook(pageInfo.getContent(), user);
		ExcelUtil.downloadExcelFile(response, workbook, "社会实践记录明细表.xls");
	}

	private HSSFWorkbook getWorkbook(List<GjtPracticeApply> list, GjtUserAccount user) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("社会实践记录明细表");
			String[] heads = { "实践计划名称", "实践计划编号", "姓名", "学号", "手机", "邮箱", "层次", "学期", "专业", "指导老师", "实践成绩", "状态" };

			HSSFRow row = sheet.createRow(0);
			for (int i = 0; i < heads.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(heads[i]);
			}

			int rowIdx = 1;
			int colIdx = 0;
			HSSFCell cell;

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行

			// 为了能够使用换行，需要设置单元格的样式 wrap=true
			HSSFCellStyle s = wb.createCellStyle();
			s.setWrapText(true);

			Map<Integer, String> statusMap = EnumUtil.getPracticeApplyStatusMap();
			Map<String, String> pyccMap = commonMapService.getPyccMap();

			for (GjtPracticeApply apply : list) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtPracticePlan().getPracticePlanName());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtPracticePlan().getPracticePlanCode());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtStudentInfo().getXm());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtStudentInfo().getXh());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtStudentInfo().getSjh());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtStudentInfo().getDzxx());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(pyccMap.get(apply.getGjtStudentInfo().getGjtSpecialty().getPycc()));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtStudentInfo().getGjtGrade().getGradeName());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtStudentInfo().getGjtSpecialty().getZymc());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (apply.getGuideTeacher1() != null) {
					cell.setCellValue(apply.getGuideTeacher1().getXm());
				} else {
					cell.setCellValue("--");
				}

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (apply.getReviewScore() != null) {
					cell.setCellValue(apply.getReviewScore().toString());
				} else {
					cell.setCellValue("--");
				}

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(statusMap.get(apply.getStatus()));
			}

			return wb;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 指导老师查看我的指导
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "listMyGuide", method = RequestMethod.GET)
	public String listMyGuide(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}
		searchParams.put("EQ_gjtPracticePlan.orgId", orgId);

		String teacherId = "0";
		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.findOneByAccountId(user.getId());
		if (employeeInfo != null) {
			teacherId = employeeInfo.getEmployeeId();
		}
		searchParams.put("EQ_guideTeacher", teacherId);

		// 只查询“待定稿”的状态
		Set<Integer> set = new HashSet<Integer>();
		set.add(PracticeStatusEnum.PRACTICE_SUBMIT_PRACTICE.getValue());
		searchParams.put("IN_status", set);

		// 默认选择当前社会实践计划
		if (EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EQ_practicePlanId"))) {
			String gradeId = gjtGradeService.getCurrentGradeId(orgId);
			GjtPracticePlan practicePlan = gjtPracticePlanService.findByGradeIdAndOrgIdAndStatus(gradeId, orgId, 3);
			if (practicePlan != null) {
				searchParams.put("EQ_practicePlanId", practicePlan.getPracticePlanId());
				model.addAttribute("practicePlanId", practicePlan.getPracticePlanId());
			}
		} else if (EmptyUtils.isNotEmpty(searchParams)
				&& EmptyUtils.isNotEmpty(searchParams.get("EQ_practicePlanId"))) {
			model.addAttribute("practicePlanId", ObjectUtils.toString(searchParams.get("EQ_practicePlanId")));
		}

		Page<GjtPracticeApply> pageInfo = gjtPracticeApplyService.queryMyGuideList(searchParams, pageRequst);
		for (GjtPracticeApply apply : pageInfo.getContent()) {
			List<GjtPracticeGuideRecord> guideRecords = gjtPracticeGuideRecordService.findStudentSubmitRecordByCode(
					apply.getPracticePlanId(), apply.getStudentId(),
					PracticeProgressCodeEnum.PRACTICE_SUBMIT_PRACTICE.getCode());
			if (guideRecords != null && guideRecords.size() > 0) {
				apply.setGuideRecord(guideRecords.get(guideRecords.size() - 1));
			}

			List<GjtPracticeGuideRecord> guideRecords2 = gjtPracticeGuideRecordService.findTeacherSubmitRecordByCode(
					apply.getPracticePlanId(), apply.getStudentId(),
					PracticeProgressCodeEnum.PRACTICE_SUBMIT_PRACTICE.getCode());
			if (guideRecords2 != null && guideRecords2.size() > 0) {
				apply.setGuideTimes(guideRecords2.size());
			} else {
				apply.setGuideTimes(0);
			}
		}

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("practicePlanMap", gjtPracticePlanService.getPracticePlanMap(orgId));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(orgId));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(orgId));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("practiceStatusMap", EnumUtil.getPracticeApplyStatusMap());

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnView", subject.isPermitted("/practiceApply/listMyGuide$view"));

		return "practice/practiceApply_myGuideList";
	}

	/**
	 * 指导老师查看我的学员
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "listMyStudent", method = RequestMethod.GET)
	public String listMyStudent(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}
		searchParams.put("EQ_gjtPracticePlan.orgId", orgId);

		String teacherId = "0";
		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.findOneByAccountId(user.getId());
		if (employeeInfo != null) {
			teacherId = employeeInfo.getEmployeeId();
		}
		searchParams.put("EQ_guideTeacher", teacherId);

		// 默认选择当前社会实践计划
		if (EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EQ_practicePlanId"))) {
			String gradeId = gjtGradeService.getCurrentGradeId(orgId);
			GjtPracticePlan practicePlan = gjtPracticePlanService.findByGradeIdAndOrgIdAndStatus(gradeId, orgId, 3);
			if (practicePlan != null) {
				searchParams.put("EQ_practicePlanId", practicePlan.getPracticePlanId());
				model.addAttribute("practicePlanId", practicePlan.getPracticePlanId());
			}
		} else if (EmptyUtils.isNotEmpty(searchParams)
				&& EmptyUtils.isNotEmpty(searchParams.get("EQ_practicePlanId"))) {
			model.addAttribute("practicePlanId", ObjectUtils.toString(searchParams.get("EQ_practicePlanId")));
		}

		Page<GjtPracticeApply> pageInfo = gjtPracticeApplyService.findAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("practicePlanMap", gjtPracticePlanService.getPracticePlanMap(orgId));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(orgId));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(orgId));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("practiceStatusMap", EnumUtil.getPracticeApplyStatusMap());

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnView", subject.isPermitted("/practiceApply/listMyStudent$view"));

		return "practice/practiceApply_myStudentList";
	}

	@RequestMapping(value = "listTeacherGuide", method = RequestMethod.GET)
	public String listTeacherGuide(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}

		// 默认选择当前社会实践计划
		if (EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EQ_practicePlanId"))) {
			String gradeId = gjtGradeService.getCurrentGradeId(orgId);
			GjtPracticePlan practicePlan = gjtPracticePlanService.findByGradeIdAndOrgIdAndStatus(gradeId, orgId, 3);
			if (practicePlan != null) {
				searchParams.put("EQ_practicePlanId", practicePlan.getPracticePlanId());
				model.addAttribute("practicePlanId", practicePlan.getPracticePlanId());
			}
		} else if (EmptyUtils.isNotEmpty(searchParams)
				&& EmptyUtils.isNotEmpty(searchParams.get("EQ_practicePlanId"))) {
			model.addAttribute("practicePlanId", ObjectUtils.toString(searchParams.get("EQ_practicePlanId")));
		}

		Page<Map<String, Object>> pageInfo = gjtPracticeApplyService.findTeacherGuideList(orgId, searchParams,
				pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("practicePlanMap", gjtPracticePlanService.getPracticePlanMap(orgId));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(orgId));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(orgId));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());

		List<GjtEmployeeInfo> guideTeachers = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(),
				EmployeeTypeEnum.论文答辩教师.getNum(), orgId);
		model.addAttribute("guideTeachers", guideTeachers);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnView", subject.isPermitted("/practiceApply/listTeacherGuide$view"));
		model.addAttribute("isBtnExport", subject.isPermitted("/practiceApply/listTeacherGuide$export"));

		return "practice/practiceApply_teacherGuideList";
	}

	@RequestMapping(value = "viewTeacherGuide", method = RequestMethod.GET)
	public String viewTeacherGuide(@RequestParam("practicePlanId") String practicePlanId,
			@RequestParam("gradeSpecialtyId") String gradeSpecialtyId, @RequestParam("teacherId") String teacherId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}

		// 查询老师指导汇总
		PageRequest page = Servlets.buildPageRequest(1, 1);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("EQ_practicePlanId", practicePlanId);
		map.put("EQ_gradeSpecialtyId", gradeSpecialtyId);
		map.put("EQ_teacherId", teacherId);
		Page<Map<String, Object>> teacherGuides = gjtPracticeApplyService.findTeacherGuideList(orgId, map, page);
		Map<String, Object> teacherGuide = teacherGuides.getContent().get(0);
		model.addAttribute("teacherGuide", teacherGuide);

		// 查询指导学生列表
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_gjtPracticePlan.orgId", orgId);
		searchParams.put("EQ_practicePlanId", practicePlanId);
		searchParams.put("EQ_gjtStudentInfo.gradeSpecialtyId", gradeSpecialtyId);
		searchParams.put("EQ_guideTeacher", teacherId);

		// 只查询“待定稿”和“已完成”的状态
		Set<Integer> set = new HashSet<Integer>();
		set.add(PracticeStatusEnum.PRACTICE_SUBMIT_PRACTICE.getValue());
		set.add(PracticeStatusEnum.PRACTICE_COMPLETED.getValue());
		searchParams.put("IN_status", set);

		Page<GjtPracticeApply> pageInfo = gjtPracticeApplyService.findAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("gradeMap", commonMapService.getGradeMap(orgId));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(orgId));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("practiceStatusMap", EnumUtil.getPracticeApplyStatusMap());

		// 统计“待定稿”和“已完成”的状态数量
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.putAll(searchParams);
		map2.remove("EQ_status");
		model.addAttribute("all", gjtPracticeApplyService.count(map2));

		map2.put("EQ_status", PracticeStatusEnum.PRACTICE_SUBMIT_PRACTICE.getValue());
		model.addAttribute("submitPractice", gjtPracticeApplyService.count(map2));

		map2.put("EQ_status", PracticeStatusEnum.PRACTICE_COMPLETED.getValue());
		model.addAttribute("completed", gjtPracticeApplyService.count(map2));

		return "practice/practiceApply_teacherGuideDetail";
	}

	@RequestMapping(value = "exportTeacherGuide", method = RequestMethod.GET)
	public void exportTeacherGuide(HttpServletRequest request, HttpServletResponse response) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}

		Page<Map<String, Object>> pageInfo = gjtPracticeApplyService.findTeacherGuideList(orgId, searchParams,
				pageRequst);
		HSSFWorkbook workbook = this.getTeacherGuideWorkbook(pageInfo.getContent(), orgId);
		ExcelUtil.downloadExcelFile(response, workbook, "社会实践指导记录统计表.xls");
	}

	private HSSFWorkbook getTeacherGuideWorkbook(List<Map<String, Object>> list, String orgId) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("社会实践指导记录统计表");
			String[] heads = { "实践计划名称", "实践计划编号", "指导老师", "学期", "层次", "专业", "指导学员总数", "初稿待定稿", "定稿评分已通过", "定稿评分未通过" };

			HSSFRow row = sheet.createRow(0);
			for (int i = 0; i < heads.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(heads[i]);
			}

			int rowIdx = 1;
			int colIdx = 0;
			HSSFCell cell;

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
				cell.setCellValue(map.get("practicePlanName").toString());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(map.get("practicePlanCode").toString());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(map.get("teacherName").toString());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(map.get("gradeName").toString());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(map.get("trainingLevel").toString());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(map.get("specialtyName").toString());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(map.get("all").toString());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(map.get("submitPractice").toString());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(map.get("completed").toString());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(map.get("failed").toString());
			}

			return wb;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 提交指导记录
	 * 
	 * @param applyId
	 * @return
	 */
	@RequestMapping(value = "submitRecord", method = RequestMethod.GET)
	public String submitRecordForm(@RequestParam String applyId, @RequestParam String type) {
		return "practice/practiceApply_guide";
	}

	/**
	 * 提交指导记录
	 * 
	 * @param applyId
	 * @param content
	 * @param attachment
	 * @param attachmentName
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "submitRecord", method = RequestMethod.POST)
	public Feedback submitRecord(@RequestParam String applyId, @RequestParam String content, String attachment,
			String attachmentName, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "操作成功");

		try {
			GjtPracticeApply practiceApply = gjtPracticeApplyService.findOne(applyId);
			GjtPracticeGuideRecord guideRecord = new GjtPracticeGuideRecord();
			guideRecord.setPracticePlanId(practiceApply.getPracticePlanId());
			guideRecord.setStudentId(practiceApply.getStudentId());
			guideRecord.setTeacherId(practiceApply.getGuideTeacher());
			guideRecord.setAttachment(attachment);
			guideRecord.setAttachmentName(attachmentName);
			guideRecord.setContent(content);
			guideRecord.setIsStudent(0);
			guideRecord.setProgressCode(PracticeProgressCodeEnum.PRACTICE_SUBMIT_PRACTICE.getCode());
			guideRecord.setCreatedBy(user.getId());
			gjtPracticeGuideRecordService.insert(guideRecord);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "操作失败");
		}

		return feedback;
	}

	/**
	 * 定稿
	 * 
	 * @param applyId
	 * @return
	 */
	@RequestMapping(value = "confirmPractice", method = RequestMethod.GET)
	public String confirmPracticeForm(@RequestParam String applyId, Model model, HttpServletRequest request) {
		String userId = (String) request.getSession().getAttribute("userId");
		GjtUserAccount user = gjtUserAccountService.findOne(userId);
		model.addAttribute("signPhoto", user.getSignPhoto());
		return "practice/practiceApply_confirmPractice";
	}

	/**
	 * 定稿
	 * 
	 * @param applyId
	 * @param autograph
	 * @param content
	 * @param reviewScore
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "confirmPractice", method = RequestMethod.POST)
	public Feedback confirmPractice(@RequestParam String applyId, HttpServletRequest request) {
		// @RequestParam String content,
		// @RequestParam float reviewScore,
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "操作成功");

		try {
			GjtPracticeApply practiceApply = gjtPracticeApplyService.findOne(applyId);

			// 添加进度
			GjtPracticeStudentProg prog = new GjtPracticeStudentProg();
			prog.setStudentId(practiceApply.getStudentId());
			prog.setPracticePlanId(practiceApply.getPracticePlanId());
			prog.setProgressCode(PracticeProgressCodeEnum.PRACTICE_CONFIRM_PRACTICE.getCode());
			prog.setCreatedBy(user.getId());
			gjtPracticeStudentProgService.insert(prog);

			// 添加指导记录，需要查询出学员最后一次提交的附件
			List<GjtPracticeGuideRecord> practiceGuideRecords = gjtPracticeGuideRecordService
					.findStudentSubmitRecordByCode(practiceApply.getPracticePlanId(), practiceApply.getStudentId(),
							PracticeProgressCodeEnum.PRACTICE_SUBMIT_PRACTICE.getCode());
			GjtPracticeGuideRecord practiceGuideRecord = practiceGuideRecords.get(practiceGuideRecords.size() - 1);

			GjtPracticeGuideRecord guideRecord = new GjtPracticeGuideRecord();
			guideRecord.setPracticePlanId(practiceApply.getPracticePlanId());
			guideRecord.setStudentId(practiceApply.getStudentId());
			guideRecord.setTeacherId(practiceApply.getGuideTeacher());
			guideRecord.setAttachment(practiceGuideRecord.getAttachment());
			guideRecord.setAttachmentName(practiceGuideRecord.getAttachmentName());
			guideRecord.setContent("已定稿");
			guideRecord.setIsStudent(0);
			guideRecord.setProgressCode(PracticeProgressCodeEnum.PRACTICE_CONFIRM_PRACTICE.getCode());
			guideRecord.setCreatedBy(user.getId());
			gjtPracticeGuideRecordService.insert(guideRecord);

			// 更新状态
			// practiceApply.setReviewScore(reviewScore);
			// practiceApply.setTeacherAutograph(autograph);
			// practiceApply.setStatus(PracticeStatusEnum.PRACTICE_CONFIRM_PRACTICE.getValue());
			practiceApply.setStatus(PracticeStatusEnum.PRACTICE_SEND.getValue());// 定稿已经直接寄送
			practiceApply.setUpdatedBy(user.getId());
			gjtPracticeApplyService.update(practiceApply);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "操作失败");
		}

		return feedback;

	}

	/**
	 * 查询物流信息
	 * 
	 * @param applyId
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "queryLogistics")
	@SuppressWarnings("unchecked")
	public String queryLogistics(@RequestParam String applyId, Model model) {
		GjtPracticeApply practiceApply = gjtPracticeApplyService.findOne(applyId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", practiceApply.getExpressCompany());
		params.put("postid", practiceApply.getExpressNumber());
		String returnStr = HttpClientUtils.doHttpPost("https://www.kuaidi100.com/query", params, 3000, "utf-8");
		Map<String, Object> returnMap = JsonUtils.toObject(returnStr, HashMap.class);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (returnMap.get("status") != null && "200".equals(returnMap.get("status").toString())) {
			list = (List<Map<String, Object>>) returnMap.get("data");
		} else {
			if (returnMap.get("message") != null) {
				model.addAttribute("errorMsg", returnMap.get("message").toString());
			} else {
				model.addAttribute("errorMsg", "查询物流信息异常");
			}
		}

		Map<String, String> expressMap = EnumUtil.getExpressMap();
		model.addAttribute("expressCompany", expressMap.get(practiceApply.getExpressCompany()));
		model.addAttribute("expressNumber", practiceApply.getExpressNumber());
		model.addAttribute("list", list);

		return "practice/practiceApply_logistics";
	}

	/**
	 * 确认收到定稿
	 * 
	 * @param applyId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "confirmExpress")
	public String confirmExpress(@RequestParam String applyId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "操作成功");

		try {
			GjtPracticeApply practiceApply = gjtPracticeApplyService.findOne(applyId);

			if (practiceApply.getStatus() != PracticeStatusEnum.PRACTICE_SEND.getValue()) {
				feedback = new Feedback(true, "进度不符");
			} else {
				// 以“初评成绩”作为课程的成绩，更新到课程中
				gjtPracticeApplyService.updateScore(practiceApply.getStudentId(), practiceApply.getReviewScore());

				// 添加进度
				GjtPracticeStudentProg prog = new GjtPracticeStudentProg();
				prog.setStudentId(practiceApply.getStudentId());
				prog.setPracticePlanId(practiceApply.getPracticePlanId());
				prog.setProgressCode(PracticeProgressCodeEnum.PRACTICE_COMPLETED.getCode());
				prog.setCreatedBy(user.getId());
				gjtPracticeStudentProgService.insert(prog);

				// 更新状态
				practiceApply.setStatus(PracticeStatusEnum.PRACTICE_COMPLETED.getValue());
				practiceApply.setUpdatedBy(user.getId());
				gjtPracticeApplyService.update(practiceApply);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "操作失败");
		}

		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/practiceApply/list";
	}

	@RequestMapping(value = "importConfirmExpressForm", method = RequestMethod.GET)
	public String importConfirmExpressForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}
		model.addAttribute("practicePlanMap", gjtPracticePlanService.getPracticePlanMap(orgId));

		return "practice/practiceApply_importConfirmExpress";
	}

	@RequestMapping(value = "importConfirmExpress")
	@ResponseBody
	public ImportFeedback importConfirmExpress(@RequestParam String practicePlanId,
			@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			GjtPracticePlan practicePlan = gjtPracticePlanService.findOne(practicePlanId);
			if (practicePlan == null) {
				return new ImportFeedback(false, "找不到实践计划！");
			}

			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "实践计划名称", "实践计划编号", "姓名", "学号", "手机", "邮箱", "层次", "学期", "专业", "指导老师", "实践成绩", "状态",
					"导入结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 1, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}

			if (dataList != null && dataList.size() > 0) {
				for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据

					if (datas.length < heads.length - 1) {
						result[heads.length - 1] = "数据不全";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[2])) { // 姓名
						result[heads.length - 1] = "姓名不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[3])) { // 学号
						result[heads.length - 1] = "学号不能为空";
						failedList.add(result);
						continue;
					}

					GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(datas[3]);
					if (studentInfo == null) {
						result[heads.length - 1] = "找不到学号为【" + datas[3] + "】的学生";
						failedList.add(result);
						continue;
					}

					if (!datas[2].equals(studentInfo.getXm())) {
						result[heads.length - 1] = "学号和姓名对应不上";
						failedList.add(result);
						continue;
					}

					GjtPracticeApply practiceApply = gjtPracticeApplyService
							.findByPracticePlanIdAndStudentId(practicePlanId, studentInfo.getStudentId());
					if (practiceApply == null) {
						result[heads.length - 1] = "未找到申请";
						failedList.add(result);
						continue;
					}

					if (practiceApply.getStatus() != PracticeStatusEnum.PRACTICE_SEND.getValue()) {
						result[heads.length - 1] = "进度不符";
						failedList.add(result);
						continue;
					} else {
						// 以“初评成绩”作为课程的成绩，更新到课程中
						gjtPracticeApplyService.updateScore(practiceApply.getStudentId(),
								practiceApply.getReviewScore());

						// 添加进度
						GjtPracticeStudentProg prog = new GjtPracticeStudentProg();
						prog.setStudentId(practiceApply.getStudentId());
						prog.setPracticePlanId(practiceApply.getPracticePlanId());
						prog.setProgressCode(PracticeProgressCodeEnum.PRACTICE_COMPLETED.getCode());
						prog.setCreatedBy(user.getId());
						gjtPracticeStudentProgService.insert(prog);

						// 更新状态
						practiceApply.setStatus(PracticeStatusEnum.PRACTICE_COMPLETED.getValue());
						practiceApply.setUpdatedBy(user.getId());
						gjtPracticeApplyService.update(practiceApply);
					}

					result[heads.length - 1] = "成功";
					successList.add(result);

				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "importConfirmExpress_success_" + currentTimeMillis + ".xls";
			String failedFileName = "importConfirmExpress_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "导入确认收到定稿成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "导入确认收到定稿失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
					+ "practiceApply" + File.separator;
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

}
