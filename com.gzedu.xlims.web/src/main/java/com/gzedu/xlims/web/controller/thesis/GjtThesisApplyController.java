package com.gzedu.xlims.web.controller.thesis;

import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.json.JsonUtils;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.pojo.status.ThesisProgressCodeEnum;
import com.gzedu.xlims.pojo.status.ThesisStatusEnum;
import com.gzedu.xlims.pojo.thesis.GjtThesisApply;
import com.gzedu.xlims.pojo.thesis.GjtThesisGuideRecord;
import com.gzedu.xlims.pojo.thesis.GjtThesisPlan;
import com.gzedu.xlims.pojo.thesis.GjtThesisStudentProg;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.thesis.GjtThesisApplyService;
import com.gzedu.xlims.service.thesis.GjtThesisGuideRecordService;
import com.gzedu.xlims.service.thesis.GjtThesisPlanService;
import com.gzedu.xlims.service.thesis.GjtThesisStudentProgService;
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
@RequestMapping("/thesisApply")
public class GjtThesisApplyController {

	private final static Logger log = LoggerFactory.getLogger(GjtThesisApplyController.class);

	@Autowired
	private GjtThesisPlanService gjtThesisPlanService;

	@Autowired
	private GjtThesisApplyService gjtThesisApplyService;

	@Autowired
	private GjtThesisStudentProgService gjtThesisStudentProgService;

	@Autowired
	private GjtThesisGuideRecordService gjtThesisGuideRecordService;

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

		searchParams.put("EQ_gjtThesisPlan.orgId", orgId);

		// 默认选择当前论文计划
		if (EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EQ_thesisPlanId"))) {
			String gradeId = gjtGradeService.getCurrentGradeId(orgId);
			GjtThesisPlan thesisPlan = gjtThesisPlanService.findByGradeIdAndOrgIdAndStatus(gradeId, orgId, 3);
			if (thesisPlan != null) {
				searchParams.put("EQ_thesisPlanId", thesisPlan.getThesisPlanId());
				model.addAttribute("thesisPlanId", thesisPlan.getThesisPlanId());
			}
		} else if (EmptyUtils.isNotEmpty(searchParams) && EmptyUtils.isNotEmpty(searchParams.get("EQ_thesisPlanId"))) {
			model.addAttribute("thesisPlanId", ObjectUtils.toString(searchParams.get("EQ_thesisPlanId")));
		}

		Page<GjtThesisApply> pageInfo = gjtThesisApplyService.findAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("thesisPlanMap", gjtThesisPlanService.getThesisPlanMap(orgId));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(orgId));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyBaseMap(orgId));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("thesisStatusMap", EnumUtil.getThesisApplyStatusMap());

		List<GjtEmployeeInfo> defenceTeachers = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(),
				EmployeeTypeEnum.论文答辩教师.getNum(), orgId);
		model.addAttribute("defenceTeachers", defenceTeachers);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnView", subject.isPermitted("/thesisApply/list$view"));
		model.addAttribute("isBtnExport", subject.isPermitted("/thesisApply/list$export"));
		model.addAttribute("isBtnConfirmExpress", subject.isPermitted("/thesisApply/list$confirmExpress"));

		return "thesis/thesisApply_list";
	}

	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(@RequestParam("thesisPlanId") String thesisPlanId, @RequestParam("studentId") String studentId,
			Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}

		GjtThesisApply thesisApply = gjtThesisApplyService.findByThesisPlanIdAndStudentId(thesisPlanId, studentId);
		List<GjtThesisStudentProg> studentProgs = gjtThesisStudentProgService
				.findByThesisPlanIdAndStudentId(thesisPlanId, studentId);
		List<GjtThesisGuideRecord> guideRecords = gjtThesisGuideRecordService
				.findByThesisPlanIdAndStudentId(thesisPlanId, studentId);

		// 构建进度和进度对应的指导记录
		Map<String, List<GjtThesisGuideRecord>> studentProgRecord = new HashMap<String, List<GjtThesisGuideRecord>>();
		if (studentProgs != null && studentProgs.size() > 0 && guideRecords != null && guideRecords.size() > 0) {
			for (GjtThesisStudentProg studentProg : studentProgs) {
				for (GjtThesisGuideRecord guideRecord : guideRecords) {
					if (guideRecord.getProgressCode().equals(studentProg.getProgressCode())) {
						List<GjtThesisGuideRecord> records = studentProgRecord.get(studentProg.getProgressId());
						if (records == null) {
							records = new ArrayList<GjtThesisGuideRecord>();
							records.add(0, guideRecord);
							studentProgRecord.put(studentProg.getProgressId(), records);
						} else {
							records.add(0, guideRecord);
						}
					}
				}
			}
		}

		model.addAttribute("thesisApply", thesisApply);
		model.addAttribute("studentProgs", studentProgs);
		model.addAttribute("studentProgRecord", studentProgRecord);
		model.addAttribute("thesisStatusMap", EnumUtil.getThesisApplyStatusMap());
		model.addAttribute("thesisProgressCodeMap", EnumUtil.getThesisProgressCodeMap());
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("expressMap", EnumUtil.getExpressMap());

		List<GjtEmployeeInfo> guideTeachers = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(),
				EmployeeTypeEnum.论文指导教师.getNum(), orgId);
		model.addAttribute("guideTeachers", guideTeachers);

		List<GjtEmployeeInfo> defenceTeachers = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(),
				EmployeeTypeEnum.论文答辩教师.getNum(), orgId);
		model.addAttribute("defenceTeachers", defenceTeachers);

		return "thesis/thesisApply_detail";
	}

	@RequestMapping(value = "updateGuideTeacher", method = RequestMethod.POST)
	public String updateGuideTeacher(@RequestParam("applyId") String applyId,
			@RequestParam("teacherId") String teacherId, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);
		thesisApply.setGuideTeacher(teacherId);
		thesisApply.setUpdatedBy(user.getId());

		// 当状态等于“已申请”的时候，表示为分配指导老师操作
		if (thesisApply.getStatus() == ThesisStatusEnum.THESIS_APPLY.getValue()) {
			thesisApply.setStatus(ThesisStatusEnum.THESIS_STAY_OPEN.getValue());

			// 添加进度
			GjtThesisStudentProg prog = new GjtThesisStudentProg();
			prog.setStudentId(thesisApply.getStudentId());
			prog.setThesisPlanId(thesisApply.getThesisPlanId());
			prog.setProgressCode(ThesisProgressCodeEnum.THESIS_STAY_OPEN.getCode());
			prog.setCreatedBy(user.getId());
			gjtThesisStudentProgService.insert(prog);
		}

		gjtThesisApplyService.update(thesisApply);

		return "redirect:/thesisApply/view?thesisPlanId=" + thesisApply.getThesisPlanId() + "&studentId="
				+ thesisApply.getStudentId();
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

		searchParams.put("EQ_gjtThesisPlan.orgId", orgId);

		Page<GjtThesisApply> pageInfo = gjtThesisApplyService.findAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		HSSFWorkbook workbook = this.getWorkbook(pageInfo.getContent(), orgId);
		ExcelUtil.downloadExcelFile(response, workbook, "毕业论文记录明细表.xls");
	}

	private HSSFWorkbook getWorkbook(List<GjtThesisApply> list, String orgId) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("毕业论文记录明细表");
			String[] heads = { "论文计划名称", "论文计划编号", "姓名", "学号", "手机", "邮箱", "层次", "学期", "专业", "指导老师", "初评成绩", "答辩老师",
					"答辩形式", "答辩成绩", "是否申请学位", "状态" };

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

			Map<Integer, String> statusMap = EnumUtil.getThesisApplyStatusMap();
			Map<String, String> pyccMap = commonMapService.getPyccMap();
			List<GjtEmployeeInfo> defenceTeachers = gjtEmployeeInfoService
					.findListByType(EmployeeTypeEnum.论文教师.getNum(), EmployeeTypeEnum.论文答辩教师.getNum(), orgId);

			for (GjtThesisApply apply : list) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtThesisPlan().getThesisPlanName());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtThesisPlan().getThesisPlanCode());

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

				String defenceTeacherStr = "--";
				if (apply.getDefenceTeacher1() != null || apply.getDefenceTeacher2() != null) {
					if (apply.getDefenceTeacher1() != null) {
						String[] ids = apply.getDefenceTeacher1().split(",");
						for (String id : ids) {
							for (GjtEmployeeInfo defenceTeacher : defenceTeachers) {
								if (defenceTeacher.getEmployeeId().equals(id)) {
									defenceTeacherStr += defenceTeacher.getXm() + ",";
								}
							}
						}
					}

					if (apply.getDefenceTeacher2() != null) {
						String[] ids = apply.getDefenceTeacher2().split(",");
						for (String id : ids) {
							for (GjtEmployeeInfo defenceTeacher : defenceTeachers) {
								if (defenceTeacher.getEmployeeId().equals(id)) {
									defenceTeacherStr += defenceTeacher.getXm() + ",";
								}
							}
						}
					}
				}
				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(defenceTeacherStr);

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (apply.getNeedDefence() == 1) {
					if (apply.getDefenceType() == 1) {
						cell.setCellValue("现场答辩");
					} else if (apply.getDefenceType() == 2) {
						cell.setCellValue("远程答辩");
					} else {
						cell.setCellValue("--");
					}
				} else {
					cell.setCellValue("无需答辩");
				}

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (apply.getDefenceScore() != null) {
					cell.setCellValue(apply.getDefenceScore().toString());
				} else {
					cell.setCellValue("--");
				}

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (apply.getApplyDegree() == 1) {
					cell.setCellValue("是");
				} else {
					cell.setCellValue("否");
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
		searchParams.put("EQ_gjtThesisPlan.orgId", orgId);

		String teacherId = "0";
		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.findOneByAccountId(user.getId());
		if (employeeInfo != null) {
			teacherId = employeeInfo.getEmployeeId();
		}
		searchParams.put("EQ_guideTeacher", teacherId);

		// 只查询“已开题”和“论文写作中”的状态
		Set<Integer> set = new HashSet<Integer>();
		set.add(ThesisStatusEnum.THESIS_SUBMIT_PROPOSE.getValue());
		set.add(ThesisStatusEnum.THESIS_SUBMIT_THESIS.getValue());
		searchParams.put("IN_status", set);

		// 默认选择当前论文计划
		if (EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EQ_thesisPlanId"))) {
			String gradeId = gjtGradeService.getCurrentGradeId(orgId);
			GjtThesisPlan thesisPlan = gjtThesisPlanService.findByGradeIdAndOrgIdAndStatus(gradeId, orgId, 3);
			if (thesisPlan != null) {
				searchParams.put("EQ_thesisPlanId", thesisPlan.getThesisPlanId());
				model.addAttribute("thesisPlanId", thesisPlan.getThesisPlanId());
			}
		} else if (EmptyUtils.isNotEmpty(searchParams) && EmptyUtils.isNotEmpty(searchParams.get("EQ_thesisPlanId"))) {
			model.addAttribute("thesisPlanId", ObjectUtils.toString(searchParams.get("EQ_thesisPlanId")));
		}

		Page<GjtThesisApply> pageInfo = gjtThesisApplyService.queryMyGuideList(searchParams, pageRequst);
		for (GjtThesisApply apply : pageInfo.getContent()) {
			if (apply.getStatus() == ThesisStatusEnum.THESIS_SUBMIT_PROPOSE.getValue()) {
				// 如果是在开题报告阶段，查询最后一次提交的开题报告
				List<GjtThesisGuideRecord> guideRecords = gjtThesisGuideRecordService.findStudentSubmitRecordByCode(
						apply.getThesisPlanId(), apply.getStudentId(),
						ThesisProgressCodeEnum.THESIS_SUBMIT_PROPOSE.getCode());
				if (guideRecords != null && guideRecords.size() > 0) {
					apply.setGuideRecord(guideRecords.get(guideRecords.size() - 1));
				}
				// 查询老师所发的指导
				List<GjtThesisGuideRecord> guideRecords2 = gjtThesisGuideRecordService.findTeacherSubmitRecordByCode(
						apply.getThesisPlanId(), apply.getStudentId(),
						ThesisProgressCodeEnum.THESIS_SUBMIT_PROPOSE.getCode());
				if (guideRecords2 != null && guideRecords2.size() > 0) {
					apply.setGuideTimes(guideRecords2.size());
				} else {
					apply.setGuideTimes(0);
				}
			} else if (apply.getStatus() == ThesisStatusEnum.THESIS_SUBMIT_THESIS.getValue()) {
				// 如果是在论文写作阶段，查询最后一次提交的论文
				List<GjtThesisGuideRecord> guideRecords = gjtThesisGuideRecordService.findStudentSubmitRecordByCode(
						apply.getThesisPlanId(), apply.getStudentId(),
						ThesisProgressCodeEnum.THESIS_SUBMIT_THESIS.getCode());
				if (guideRecords != null && guideRecords.size() > 0) {
					apply.setGuideRecord(guideRecords.get(guideRecords.size() - 1));
				}
				// 查询老师所发的指导
				List<GjtThesisGuideRecord> guideRecords2 = gjtThesisGuideRecordService.findTeacherSubmitRecordByCode(
						apply.getThesisPlanId(), apply.getStudentId(),
						ThesisProgressCodeEnum.THESIS_SUBMIT_THESIS.getCode());
				if (guideRecords2 != null && guideRecords2.size() > 0) {
					apply.setGuideTimes(guideRecords2.size());
				} else {
					apply.setGuideTimes(0);
				}
			}
		}

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("thesisPlanMap", gjtThesisPlanService.getThesisPlanMap(orgId));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(orgId));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(orgId));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("thesisStatusMap", EnumUtil.getThesisApplyStatusMap());

		// 统计“已开题”和“论文写作中”的状态数量
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.remove("IN_status");

		map.put("EQ_status", ThesisStatusEnum.THESIS_SUBMIT_PROPOSE.getValue());
		model.addAttribute("submitPropose", gjtThesisApplyService.queryMyGuideList(map, pageRequst).getTotalElements());

		map.put("EQ_status", ThesisStatusEnum.THESIS_SUBMIT_THESIS.getValue());
		model.addAttribute("submitThesis", gjtThesisApplyService.queryMyGuideList(map, pageRequst).getTotalElements());

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnView", subject.isPermitted("/thesisApply/listMyGuide$view"));

		return "thesis/thesisApply_myGuideList";
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
		searchParams.put("EQ_gjtThesisPlan.orgId", orgId);

		String teacherId = "0";
		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.findOneByAccountId(user.getId());
		if (employeeInfo != null) {
			teacherId = employeeInfo.getEmployeeId();
		}
		searchParams.put("EQ_guideTeacher", teacherId);

		// 默认选择当前论文计划
		if (EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EQ_thesisPlanId"))) {
			String gradeId = gjtGradeService.getCurrentGradeId(orgId);
			GjtThesisPlan thesisPlan = gjtThesisPlanService.findByGradeIdAndOrgIdAndStatus(gradeId, orgId, 3);
			if (thesisPlan != null) {
				searchParams.put("EQ_thesisPlanId", thesisPlan.getThesisPlanId());
				model.addAttribute("thesisPlanId", thesisPlan.getThesisPlanId());
			}
		} else if (EmptyUtils.isNotEmpty(searchParams) && EmptyUtils.isNotEmpty(searchParams.get("EQ_thesisPlanId"))) {
			model.addAttribute("thesisPlanId", ObjectUtils.toString(searchParams.get("EQ_thesisPlanId")));
		}

		Page<GjtThesisApply> pageInfo = gjtThesisApplyService.findAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("thesisPlanMap", gjtThesisPlanService.getThesisPlanMap(orgId));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(orgId));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(orgId));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("thesisStatusMap", EnumUtil.getThesisApplyStatusMap());

		List<GjtEmployeeInfo> defenceTeachers = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(),
				EmployeeTypeEnum.论文答辩教师.getNum(), orgId);
		model.addAttribute("defenceTeachers", defenceTeachers);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnView", subject.isPermitted("/thesisApply/listMyStudent$view"));

		return "thesis/thesisApply_myStudentList";
	}

	/**
	 * 查看老师指导汇总
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
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

		// 默认选择当前论文计划
		if (EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EQ_thesisPlanId"))) {
			String gradeId = gjtGradeService.getCurrentGradeId(orgId);
			GjtThesisPlan thesisPlan = gjtThesisPlanService.findByGradeIdAndOrgIdAndStatus(gradeId, orgId, 3);
			if (thesisPlan != null) {
				searchParams.put("EQ_thesisPlanId", thesisPlan.getThesisPlanId());
				model.addAttribute("thesisPlanId", thesisPlan.getThesisPlanId());
			}
		} else if (EmptyUtils.isNotEmpty(searchParams) && EmptyUtils.isNotEmpty(searchParams.get("EQ_thesisPlanId"))) {
			model.addAttribute("thesisPlanId", ObjectUtils.toString(searchParams.get("EQ_thesisPlanId")));
		}

		Page<Map<String, Object>> pageInfo = gjtThesisApplyService.findTeacherGuideList(orgId, searchParams,
				pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("thesisPlanMap", gjtThesisPlanService.getThesisPlanMap(orgId));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(orgId));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(orgId));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());

		List<GjtEmployeeInfo> guideTeachers = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(),
				EmployeeTypeEnum.论文指导教师.getNum(), orgId);
		model.addAttribute("guideTeachers", guideTeachers);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnView", subject.isPermitted("/thesisApply/listTeacherGuide$view"));
		model.addAttribute("isBtnExport", subject.isPermitted("/thesisApply/listTeacherGuide$export"));

		return "thesis/thesisApply_teacherGuideList";
	}

	/**
	 * 查看老师指导明细
	 * 
	 * @param thesisPlanId
	 * @param gradeSpecialtyId
	 * @param teacherId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "viewTeacherGuide", method = RequestMethod.GET)
	public String viewTeacherGuide(@RequestParam("thesisPlanId") String thesisPlanId,
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
		map.put("EQ_thesisPlanId", thesisPlanId);
		map.put("EQ_gradeSpecialtyId", gradeSpecialtyId);
		map.put("EQ_teacherId", teacherId);
		Page<Map<String, Object>> teacherGuides = gjtThesisApplyService.findTeacherGuideList(orgId, map, page);
		Map<String, Object> teacherGuide = teacherGuides.getContent().get(0);
		model.addAttribute("teacherGuide", teacherGuide);

		// 查询指导学生列表
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_gjtThesisPlan.orgId", orgId);
		searchParams.put("EQ_thesisPlanId", thesisPlanId);
		searchParams.put("EQ_gjtStudentInfo.gradeSpecialtyId", gradeSpecialtyId);
		searchParams.put("EQ_guideTeacher", teacherId);

		// 只查询“已开题”、"论文写作中"、"指导老师定稿"、"初评成绩未达标"、和“学院定稿”的状态
		Set<Integer> set = new HashSet<Integer>();
		set.add(ThesisStatusEnum.THESIS_SUBMIT_PROPOSE.getValue());
		set.add(ThesisStatusEnum.THESIS_SUBMIT_THESIS.getValue());
		set.add(ThesisStatusEnum.THESIS_TEACHER_CONFIRM_THESIS.getValue());
		set.add(ThesisStatusEnum.THESIS_REVIEW_FAILED.getValue());
		set.add(ThesisStatusEnum.THESIS_COLLEGE_CONFIRM_THESIS.getValue());
		searchParams.put("IN_status", set);

		Page<GjtThesisApply> pageInfo = gjtThesisApplyService.findAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("gradeMap", commonMapService.getGradeMap(orgId));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(orgId));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("thesisStatusMap", EnumUtil.getThesisApplyStatusMap());

		// 统计“已开题”、"论文写作中"、"指导老师定稿"、"初评成绩未达标"、和“学院定稿”的状态数量
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.putAll(searchParams);
		map2.remove("EQ_status");
		model.addAttribute("all", gjtThesisApplyService.count(map2));

		map2.put("EQ_status", ThesisStatusEnum.THESIS_SUBMIT_PROPOSE.getValue());
		model.addAttribute("submitPropose", gjtThesisApplyService.count(map2));

		map2.put("EQ_status", ThesisStatusEnum.THESIS_SUBMIT_THESIS.getValue());
		model.addAttribute("submitThesis", gjtThesisApplyService.count(map2));

		map2.put("EQ_status", ThesisStatusEnum.THESIS_TEACHER_CONFIRM_THESIS.getValue());
		model.addAttribute("teacherConfirm", gjtThesisApplyService.count(map2));

		map2.put("EQ_status", ThesisStatusEnum.THESIS_REVIEW_FAILED.getValue());
		model.addAttribute("reviewFailed", gjtThesisApplyService.count(map2));

		map2.put("EQ_status", ThesisStatusEnum.THESIS_COLLEGE_CONFIRM_THESIS.getValue());
		model.addAttribute("collegeConfirm", gjtThesisApplyService.count(map2));

		return "thesis/thesisApply_teacherGuideDetail";
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

		Page<Map<String, Object>> pageInfo = gjtThesisApplyService.findTeacherGuideList(orgId, searchParams,
				pageRequst);
		HSSFWorkbook workbook = this.getTeacherGuideWorkbook(pageInfo.getContent(), orgId);
		ExcelUtil.downloadExcelFile(response, workbook, "论文指导记录统计表.xls");
	}

	private HSSFWorkbook getTeacherGuideWorkbook(List<Map<String, Object>> list, String orgId) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("论文指导记录统计表");
			String[] heads = { "论文计划名称", "论文计划编号", "指导老师", "学期", "层次", "专业", "指导学员总数", "开题报告待定稿", "初稿待定稿", "定稿待评分",
					"定稿初评已通过", "定稿初评未通过" };

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
				cell.setCellValue(map.get("thesisPlanName").toString());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(map.get("thesisPlanCode").toString());

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
				cell.setCellValue(map.get("submitPropose").toString());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(map.get("submitThesis").toString());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(map.get("teacherConfirm").toString());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(map.get("collegeConfirm").toString());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(map.get("reviewFailed").toString());
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
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "submitRecord", method = RequestMethod.GET)
	public String submitRecordForm(@RequestParam String applyId, Model model, @RequestParam String type) {
		model.addAttribute("entity", gjtThesisApplyService.findOne(applyId));

		return "thesis/thesisApply_guide";
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
			GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);
			GjtThesisGuideRecord guideRecord = new GjtThesisGuideRecord();
			guideRecord.setThesisPlanId(thesisApply.getThesisPlanId());
			guideRecord.setStudentId(thesisApply.getStudentId());
			guideRecord.setTeacherId(thesisApply.getGuideTeacher());
			guideRecord.setAttachment(attachment);
			guideRecord.setAttachmentName(attachmentName);
			guideRecord.setContent(content);
			guideRecord.setIsStudent(0);
			if (thesisApply.getStatus() == ThesisStatusEnum.THESIS_SUBMIT_PROPOSE.getValue()) {
				guideRecord.setProgressCode(ThesisProgressCodeEnum.THESIS_SUBMIT_PROPOSE.getCode());
			} else if (thesisApply.getStatus() == ThesisStatusEnum.THESIS_SUBMIT_THESIS.getValue()) {
				guideRecord.setProgressCode(ThesisProgressCodeEnum.THESIS_SUBMIT_THESIS.getCode());
			}
			guideRecord.setCreatedBy(user.getId());
			gjtThesisGuideRecordService.insert(guideRecord);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "操作失败");
		}

		return feedback;
	}

	/**
	 * 开题定稿
	 * 
	 * @param applyId
	 * @return
	 */
	@RequestMapping(value = "confirmPropose", method = RequestMethod.GET)
	public String confirmProposeForm(@RequestParam String applyId) {
		return "thesis/thesisApply_confirmPropose";
	}

	/**
	 * 开题定稿
	 * 
	 * @param applyId
	 * @param content
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "confirmPropose", method = RequestMethod.POST)
	public Feedback confirmPropose(@RequestParam String applyId, @RequestParam String content,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "操作成功");

		try {
			GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);

			// 添加进度
			GjtThesisStudentProg prog = new GjtThesisStudentProg();
			prog.setStudentId(thesisApply.getStudentId());
			prog.setThesisPlanId(thesisApply.getThesisPlanId());
			prog.setProgressCode(ThesisProgressCodeEnum.THESIS_CONFIRM_PROPOSE.getCode());
			prog.setCreatedBy(user.getId());
			gjtThesisStudentProgService.insert(prog);

			// 添加指导记录，需要查询出学员最后一次提交的附件
			List<GjtThesisGuideRecord> thesisGuideRecords = gjtThesisGuideRecordService.findStudentSubmitRecordByCode(
					thesisApply.getThesisPlanId(), thesisApply.getStudentId(),
					ThesisProgressCodeEnum.THESIS_SUBMIT_PROPOSE.getCode());
			GjtThesisGuideRecord thesisGuideRecord = thesisGuideRecords.get(thesisGuideRecords.size() - 1);

			GjtThesisGuideRecord guideRecord = new GjtThesisGuideRecord();
			guideRecord.setThesisPlanId(thesisApply.getThesisPlanId());
			guideRecord.setStudentId(thesisApply.getStudentId());
			guideRecord.setTeacherId(thesisApply.getGuideTeacher());
			guideRecord.setAttachment(thesisGuideRecord.getAttachment());
			guideRecord.setAttachmentName(thesisGuideRecord.getAttachmentName());
			guideRecord.setContent(content);
			guideRecord.setIsStudent(0);
			guideRecord.setProgressCode(ThesisProgressCodeEnum.THESIS_CONFIRM_PROPOSE.getCode());
			guideRecord.setCreatedBy(user.getId());
			gjtThesisGuideRecordService.insert(guideRecord);

			// 更新状态
			thesisApply.setStatus(ThesisStatusEnum.THESIS_CONFIRM_PROPOSE.getValue());
			thesisApply.setUpdatedBy(user.getId());
			gjtThesisApplyService.update(thesisApply);
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
	@RequestMapping(value = "confirmThesis", method = RequestMethod.GET)
	public String confirmThesisForm(@RequestParam String applyId, Model model, HttpServletRequest request) {
		String userId = (String) request.getSession().getAttribute("userId");
		GjtUserAccount user = gjtUserAccountService.findOne(userId);
		model.addAttribute("signPhoto", user.getSignPhoto());
		return "thesis/thesisApply_confirmThesis";
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
	@RequestMapping(value = "confirmThesis", method = RequestMethod.POST)
	public Feedback confirmThesis(@RequestParam String applyId, HttpServletRequest request) {
		// @RequestParam String content,
		// @RequestParam float reviewScore,

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "操作成功");

		try {
			GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);

			// 添加进度
			GjtThesisStudentProg prog = new GjtThesisStudentProg();
			prog.setStudentId(thesisApply.getStudentId());
			prog.setThesisPlanId(thesisApply.getThesisPlanId());
			prog.setProgressCode(ThesisProgressCodeEnum.THESIS_TEACHER_CONFIRM_THESIS.getCode());
			prog.setCreatedBy(user.getId());
			gjtThesisStudentProgService.insert(prog);

			// 添加指导记录，需要查询出学员最后一次提交的附件
			List<GjtThesisGuideRecord> thesisGuideRecords = gjtThesisGuideRecordService.findStudentSubmitRecordByCode(
					thesisApply.getThesisPlanId(), thesisApply.getStudentId(),
					ThesisProgressCodeEnum.THESIS_SUBMIT_THESIS.getCode());
			GjtThesisGuideRecord thesisGuideRecord = thesisGuideRecords.get(thesisGuideRecords.size() - 1);

			GjtThesisGuideRecord guideRecord = new GjtThesisGuideRecord();
			guideRecord.setThesisPlanId(thesisApply.getThesisPlanId());
			guideRecord.setStudentId(thesisApply.getStudentId());
			guideRecord.setTeacherId(thesisApply.getGuideTeacher());
			guideRecord.setAttachment(thesisGuideRecord.getAttachment());
			guideRecord.setAttachmentName(thesisGuideRecord.getAttachmentName());
			guideRecord.setContent("已定稿");
			guideRecord.setIsStudent(0);
			guideRecord.setProgressCode(ThesisProgressCodeEnum.THESIS_TEACHER_CONFIRM_THESIS.getCode());
			guideRecord.setCreatedBy(user.getId());
			gjtThesisGuideRecordService.insert(guideRecord);

			// 更新状态
			// thesisApply.setReviewScore(reviewScore);//不要分数
			// thesisApply.setTeacherAutograph(autograph);
			thesisApply.setStatus(ThesisStatusEnum.THESIS_TEACHER_CONFIRM_THESIS.getValue());
			// thesisApply.setStatus(ThesisStatusEnum.THESIS_COLLEGE_CONFIRM_THESIS.getValue());//
			// 直接学院定稿
			thesisApply.setUpdatedBy(user.getId());
			gjtThesisApplyService.update(thesisApply);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "操作失败");
		}

		return feedback;

	}

	/**
	 * 学院定稿
	 * 
	 * @param applyId
	 * @param content
	 * @param type
	 *            类型：1-通过，2-不通过
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "collegeConfirmThesis", method = RequestMethod.POST)
	public Feedback collegeConfirmThesis(@RequestParam String applyId, @RequestParam String content,
			@RequestParam int type, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "操作成功");

		try {
			GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);

			// 添加进度
			GjtThesisStudentProg prog = new GjtThesisStudentProg();
			prog.setStudentId(thesisApply.getStudentId());
			prog.setThesisPlanId(thesisApply.getThesisPlanId());
			prog.setProgressCode(ThesisProgressCodeEnum.THESIS_COLLEGE_CONFIRM_THESIS.getCode());
			prog.setCreatedBy(user.getId());
			gjtThesisStudentProgService.insert(prog);

			// 添加指导记录，需要查询出学员最后一次提交的附件
			List<GjtThesisGuideRecord> thesisGuideRecords = gjtThesisGuideRecordService.findStudentSubmitRecordByCode(
					thesisApply.getThesisPlanId(), thesisApply.getStudentId(),
					ThesisProgressCodeEnum.THESIS_SUBMIT_THESIS.getCode());
			GjtThesisGuideRecord thesisGuideRecord = thesisGuideRecords.get(thesisGuideRecords.size() - 1);

			GjtThesisGuideRecord guideRecord = new GjtThesisGuideRecord();
			guideRecord.setThesisPlanId(thesisApply.getThesisPlanId());
			guideRecord.setStudentId(thesisApply.getStudentId());
			// guideRecord.setTeacherId(thesisApply.getGuideTeacher());
			guideRecord.setAttachment(thesisGuideRecord.getAttachment());
			guideRecord.setAttachmentName(thesisGuideRecord.getAttachmentName());
			guideRecord.setContent(content);
			guideRecord.setIsStudent(0);
			guideRecord.setProgressCode(ThesisProgressCodeEnum.THESIS_COLLEGE_CONFIRM_THESIS.getCode());
			guideRecord.setCreatedBy(user.getId());
			gjtThesisGuideRecordService.insert(guideRecord);

			// 更新状态
			if (type == 1) {
				// thesisApply.setStatus(ThesisStatusEnum.THESIS_COLLEGE_CONFIRM_THESIS.getValue());//就不到院校定稿了
				// 自动寄送定稿
				thesisApply.setStatus(ThesisStatusEnum.THESIS_SEND.getValue());
			} else {
				thesisApply.setStatus(ThesisStatusEnum.THESIS_SUBMIT_THESIS.getValue());
			}
			thesisApply.setUpdatedBy(user.getId());
			gjtThesisApplyService.update(thesisApply);
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
		GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", thesisApply.getExpressCompany());
		params.put("postid", thesisApply.getExpressNumber());
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
		model.addAttribute("expressCompany", expressMap.get(thesisApply.getExpressCompany()));
		model.addAttribute("expressNumber", thesisApply.getExpressNumber());
		model.addAttribute("list", list);

		return "thesis/thesisApply_logistics";
	}

	/**
	 * 确认收到定稿
	 * 
	 * @param applyId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "confirmExpress")
	@ResponseBody
	public Feedback confirmExpress(@RequestParam String applyId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "操作成功");

		try {
			GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);

			if (thesisApply.getStatus() != ThesisStatusEnum.THESIS_SEND.getValue()) {
				feedback = new Feedback(true, "进度不符");
			} else {
				// 添加进度
				GjtThesisStudentProg prog = new GjtThesisStudentProg();
				prog.setStudentId(thesisApply.getStudentId());
				prog.setThesisPlanId(thesisApply.getThesisPlanId());
				prog.setProgressCode(ThesisProgressCodeEnum.THESIS_SIGN.getCode());
				prog.setCreatedBy(user.getId());
				gjtThesisStudentProgService.insert(prog);

				// 更新状态，如果是专科，无需答辩，直接到完成状态
				if ("2".equals(thesisApply.getGjtStudentInfo().getPycc())) {
					thesisApply.setStatus(ThesisStatusEnum.THESIS_STAY_DEFENCE.getValue());
					thesisApply.setUpdatedBy(user.getId());
					gjtThesisApplyService.update(thesisApply);
				} else {
					Thread.sleep(100); // 休眠防止进度乱了

					// 以“初评成绩”作为课程的成绩，更新到课程中
					gjtThesisApplyService.updateScore(thesisApply.getStudentId(), thesisApply.getReviewScore());

					// 添加“已完成”进度
					GjtThesisStudentProg prog2 = new GjtThesisStudentProg();
					prog2.setStudentId(thesisApply.getStudentId());
					prog2.setThesisPlanId(thesisApply.getThesisPlanId());
					prog2.setProgressCode(ThesisProgressCodeEnum.THESIS_COMPLETED.getCode());
					prog2.setCreatedBy(user.getId());
					gjtThesisStudentProgService.insert(prog2);

					thesisApply.setStatus(ThesisStatusEnum.THESIS_COMPLETED.getValue());
					thesisApply.setUpdatedBy(user.getId());
					gjtThesisApplyService.update(thesisApply);
				}

			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "操作失败");
		}
		return feedback;
	}

	@RequestMapping(value = "importConfirmExpressForm", method = RequestMethod.GET)
	public String importConfirmExpressForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}
		model.addAttribute("thesisPlanMap", gjtThesisPlanService.getThesisPlanMap(orgId));

		return "thesis/thesisApply_importConfirmExpress";
	}

	@RequestMapping(value = "importConfirmExpress")
	@ResponseBody
	public ImportFeedback importConfirmExpress(@RequestParam String thesisPlanId,
			@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			GjtThesisPlan thesisPlan = gjtThesisPlanService.findOne(thesisPlanId);
			if (thesisPlan == null) {
				return new ImportFeedback(false, "找不到论文计划！");
			}

			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "论文计划名称", "论文计划编号", "姓名", "学号", "手机", "邮箱", "层次", "学期", "专业", "指导老师", "初评成绩", "答辩老师",
					"答辩形式", "答辩成绩", "状态", "导入结果" };
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

					GjtThesisApply thesisApply = gjtThesisApplyService.findByThesisPlanIdAndStudentId(thesisPlanId,
							studentInfo.getStudentId());
					if (thesisApply == null) {
						result[heads.length - 1] = "未找到申请";
						failedList.add(result);
						continue;
					}

					if (thesisApply.getStatus() != ThesisStatusEnum.THESIS_SEND.getValue()) {
						result[heads.length - 1] = "论文进度不符";
						failedList.add(result);
						continue;
					} else {
						// 添加进度
						GjtThesisStudentProg prog = new GjtThesisStudentProg();
						prog.setStudentId(thesisApply.getStudentId());
						prog.setThesisPlanId(thesisApply.getThesisPlanId());
						prog.setProgressCode(ThesisProgressCodeEnum.THESIS_SIGN.getCode());
						prog.setCreatedBy(user.getId());
						gjtThesisStudentProgService.insert(prog);

						// 更新状态，如果是专科，无需答辩，直接到完成状态
						if ("2".equals(thesisApply.getGjtStudentInfo().getPycc())) {
							thesisApply.setStatus(ThesisStatusEnum.THESIS_STAY_DEFENCE.getValue());
							thesisApply.setUpdatedBy(user.getId());
							gjtThesisApplyService.update(thesisApply);
						} else {
							Thread.sleep(100); // 休眠防止进度乱了

							// 以“初评成绩”作为课程的成绩，更新到课程中
							gjtThesisApplyService.updateScore(thesisApply.getStudentId(), thesisApply.getReviewScore());

							// 添加“已完成”进度
							GjtThesisStudentProg prog2 = new GjtThesisStudentProg();
							prog2.setStudentId(thesisApply.getStudentId());
							prog2.setThesisPlanId(thesisApply.getThesisPlanId());
							prog2.setProgressCode(ThesisProgressCodeEnum.THESIS_COMPLETED.getCode());
							prog2.setCreatedBy(user.getId());
							gjtThesisStudentProgService.insert(prog2);

							thesisApply.setStatus(ThesisStatusEnum.THESIS_COMPLETED.getValue());
							thesisApply.setUpdatedBy(user.getId());
							gjtThesisApplyService.update(thesisApply);
						}

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
					+ "thesisApply" + File.separator;
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
