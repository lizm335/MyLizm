package com.gzedu.xlims.web.controller.thesis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.pojo.status.ThesisProgressCodeEnum;
import com.gzedu.xlims.pojo.status.ThesisStatusEnum;
import com.gzedu.xlims.pojo.thesis.GjtThesisApply;
import com.gzedu.xlims.pojo.thesis.GjtThesisDefencePlan;
import com.gzedu.xlims.pojo.thesis.GjtThesisPlan;
import com.gzedu.xlims.pojo.thesis.GjtThesisStudentProg;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.thesis.GjtThesisApplyService;
import com.gzedu.xlims.service.thesis.GjtThesisDefencePlanService;
import com.gzedu.xlims.service.thesis.GjtThesisPlanService;
import com.gzedu.xlims.service.thesis.GjtThesisStudentProgService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/thesisDefencePlan")
public class GjtThesisDefencePlanController {

	private final static Logger log = LoggerFactory.getLogger(GjtThesisDefencePlanController.class);

	@Autowired
	private GjtThesisPlanService gjtThesisPlanService;

	@Autowired
	private GjtThesisApplyService gjtThesisApplyService;

	@Autowired
	private GjtThesisDefencePlanService gjtThesisDefencePlanService;

	@Autowired
	private GjtThesisStudentProgService gjtThesisStudentProgService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	private GjtOrgService gjtOrgService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private CommonMapService commonMapService;

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

		// 只查询“答辩安排中”以及之后的状态
		if (EmptyUtils.isEmpty(searchParams.get("GTE_status"))) {
			searchParams.put("GTE_status", ThesisStatusEnum.THESIS_STAY_DEFENCE.getValue());
		}
		// 本科的学生才需要答辩
		searchParams.put("GTE_gjtStudentInfo.pycc", "2");

		Page<GjtThesisApply> pageInfo = gjtThesisApplyService.findAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("thesisPlanMap", gjtThesisPlanService.getThesisPlanMap(orgId));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(orgId));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(orgId));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());

		List<GjtEmployeeInfo> guideTeachers = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(),
				EmployeeTypeEnum.论文指导教师.getNum(), orgId);
		model.addAttribute("guideTeachers", guideTeachers);

		List<GjtEmployeeInfo> defenceTeachers = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(),
				EmployeeTypeEnum.论文答辩教师.getNum(), orgId);
		model.addAttribute("defenceTeachers", defenceTeachers);

		// 统计"待安排"、“已安排”和“已结束”的状态数量
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.remove("GTE_status");

		map.put("EQ_status", ThesisStatusEnum.THESIS_STAY_DEFENCE.getValue());
		model.addAttribute("stayDefence", gjtThesisApplyService.count(map));

		map.put("EQ_status", ThesisStatusEnum.THESIS_DEFENCE.getValue());
		model.addAttribute("defence", gjtThesisApplyService.count(map));

		map.remove("EQ_status");
		map.put("GTE_status", ThesisStatusEnum.THESIS_DEFENCE_FAILED.getValue());
		model.addAttribute("hasDefence", gjtThesisApplyService.count(map));

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnImport", subject.isPermitted("/thesisDefencePlan/list$import"));
		model.addAttribute("isBtnExport", subject.isPermitted("/thesisDefencePlan/list$export"));
		model.addAttribute("isBtnImportScore", subject.isPermitted("/thesisDefencePlan/list$importScore"));

		return "thesis/thesisDefencePlan_list";
	}

	@RequestMapping(value = "export", method = RequestMethod.GET)
	public void export(@RequestParam String thesisPlanId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_thesisPlanId", thesisPlanId);

		// 只查询“答辩安排中”以及之后的状态
		searchParams.put("GTE_status", ThesisStatusEnum.THESIS_STAY_DEFENCE.getValue());
		// 本科的学生才需要答辩
		searchParams.put("GTE_gjtStudentInfo.pycc", "2");

		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}

		Page<GjtThesisApply> pageInfo = gjtThesisApplyService.findAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		HSSFWorkbook workbook = this.getWorkbook(pageInfo.getContent(), orgId);
		ExcelUtil.downloadExcelFile(response, workbook, "毕业论文答辩安排表.xls");
	}

	private HSSFWorkbook getWorkbook(List<GjtThesisApply> list, String orgId) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("毕业论文答辩安排表");
			String[] heads = { "论文计划名称", "论文计划编号", "姓名", "学号", "手机", "层次", "学期", "专业", "指导老师", "初评成绩", "答辩老师", "答辩形式",
					"答辩时间", "答辩地点", "答辩成绩", "状态" };

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
				if (apply.getGjtThesisDefencePlan() != null) {
					cell.setCellValue(apply.getGjtThesisDefencePlan().getDefenceTime().toString());
				} else {
					cell.setCellValue("--");
				}

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (apply.getGjtThesisDefencePlan() != null) {
					cell.setCellValue(apply.getGjtThesisDefencePlan().getDefenceAddress().toString());
				} else {
					cell.setCellValue("--");
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
				if (apply.getStatus() == ThesisStatusEnum.THESIS_STAY_DEFENCE.getValue()) {
					cell.setCellValue("待安排");
				} else if (apply.getStatus() == ThesisStatusEnum.THESIS_DEFENCE.getValue()) {
					cell.setCellValue("已安排");
				} else if (apply.getStatus() > ThesisStatusEnum.THESIS_DEFENCE.getValue()) {
					cell.setCellValue("已结束");
				}
			}

			return wb;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "importForm", method = RequestMethod.GET)
	public String importForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}
		model.addAttribute("thesisPlanMap", gjtThesisPlanService.getThesisPlanMap(orgId));

		return "thesis/thesisDefencePlan_import";
	}

	@RequestMapping(value = "import")
	@ResponseBody
	public ImportFeedback importDefencePlan(@RequestParam String thesisPlanId, @RequestParam("file") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			GjtThesisPlan thesisPlan = gjtThesisPlanService.findOne(thesisPlanId);
			if (thesisPlan == null) {
				return new ImportFeedback(false, "找不到论文计划！");
			}

			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "姓名", "学号", "手机号码", "层次", "学期", "专业", "答辩形式", "答辩时间", "答辩地点", "导入结果" };
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

					if (datas.length < heads.length - 1) {
						result[heads.length - 1] = "数据不全";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[0])) { // 姓名
						result[heads.length - 1] = "姓名不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[1])) { // 学号
						result[heads.length - 1] = "学号不能为空";
						failedList.add(result);
						continue;
					}

					if (!"现场答辩".equals(datas[6]) && !"远程答辩".equals(datas[6])) { // 答辩形式
						result[heads.length - 1] = "答辩形式有误";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[7])) { // 答辩时间
						result[heads.length - 1] = "答辩时间不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[8])) { // 答辩地点
						result[heads.length - 1] = "答辩地点不能为空";
						failedList.add(result);
						continue;
					}

					GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(datas[1]);
					if (studentInfo == null) {
						result[heads.length - 1] = "找不到学号为【" + datas[1] + "】的学生";
						failedList.add(result);
						continue;
					}

					if (!datas[0].equals(studentInfo.getXm())) {
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
					if (thesisApply.getStatus() != ThesisStatusEnum.THESIS_STAY_DEFENCE.getValue()
							&& thesisApply.getStatus() != ThesisStatusEnum.THESIS_DEFENCE.getValue()) {
						result[heads.length - 1] = "论文进度不符";
						failedList.add(result);
						continue;
					}

					if (thesisApply.getGjtThesisDefencePlan() != null) { // 更新
						GjtThesisDefencePlan thesisDefencePlan = thesisApply.getGjtThesisDefencePlan();
						if ("现场答辩".equals(datas[6])) {
							thesisDefencePlan.setDefenceType(1);
						} else {
							thesisDefencePlan.setDefenceType(2);
						}
						thesisDefencePlan.setDefenceTime(datas[7]);
						thesisDefencePlan.setDefenceAddress(datas[8]);
						thesisDefencePlan.setUpdatedBy(user.getId());
						gjtThesisDefencePlanService.update(thesisDefencePlan);

						if ("现场答辩".equals(datas[6])) {
							thesisApply.setDefenceType(1);
						} else {
							thesisApply.setDefenceType(2);
						}
						gjtThesisApplyService.update(thesisApply);
					} else { // 新增
						GjtThesisDefencePlan thesisDefencePlan = new GjtThesisDefencePlan();
						thesisDefencePlan.setThesisPlanId(thesisPlanId);
						thesisDefencePlan.setApplyId(thesisApply.getApplyId());
						if ("现场答辩".equals(datas[6])) {
							thesisDefencePlan.setDefenceType(1);
						} else {
							thesisDefencePlan.setDefenceType(2);
						}
						thesisDefencePlan.setDefenceTime(datas[7]);
						thesisDefencePlan.setDefenceAddress(datas[8]);
						thesisDefencePlan.setCreatedBy(user.getId());
						gjtThesisDefencePlanService.insert(thesisDefencePlan);

						// 更新申请的状态
						thesisApply.setDefencePlanId(thesisDefencePlan.getPlanId());
						if ("现场答辩".equals(datas[6])) {
							thesisApply.setDefenceType(1);
						} else {
							thesisApply.setDefenceType(2);
						}
						thesisApply.setStatus(ThesisStatusEnum.THESIS_DEFENCE.getValue());
						thesisApply.setUpdatedBy(user.getId());
						gjtThesisApplyService.update(thesisApply);

						// 添加进度
						GjtThesisStudentProg prog = new GjtThesisStudentProg();
						prog.setStudentId(thesisApply.getStudentId());
						prog.setThesisPlanId(thesisPlanId);
						prog.setProgressCode(ThesisProgressCodeEnum.THESIS_DEFENCE.getCode());
						prog.setCreatedBy(user.getId());
						gjtThesisStudentProgService.insert(prog);
					}

					result[heads.length - 1] = "成功";
					successList.add(result);
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "thesisDefencePlan_success_" + currentTimeMillis + ".xls";
			String failedFileName = "thesisDefencePlan_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "导入答辩安排成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "导入答辩安排失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
					+ "thesisDefencePlan" + File.separator;
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

	@RequestMapping(value = "importScoreForm", method = RequestMethod.GET)
	public String importScoreForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}
		model.addAttribute("thesisPlanMap", gjtThesisPlanService.getThesisPlanMap(orgId));

		return "thesis/thesisDefencePlan_importScore";
	}

	@RequestMapping(value = "importScore")
	@ResponseBody
	public ImportFeedback importScore(@RequestParam String thesisPlanId, @RequestParam("file") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			GjtThesisPlan thesisPlan = gjtThesisPlanService.findOne(thesisPlanId);
			if (thesisPlan == null) {
				return new ImportFeedback(false, "找不到论文计划！");
			}

			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "姓名", "学号", "手机号码", "层次", "学期", "专业", "答辩形式", "答辩时间", "答辩地点", "答辩成绩", "导入结果" };
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

					if (datas.length < heads.length - 1) {
						result[heads.length - 1] = "数据不全";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[0])) { // 姓名
						result[heads.length - 1] = "姓名不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[1])) { // 学号
						result[heads.length - 1] = "学号不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[9])) { // 答辩成绩
						result[heads.length - 1] = "答辩成绩不能为空";
						failedList.add(result);
						continue;
					}

					float defenceScore = 0f;
					try {
						defenceScore = Float.parseFloat(datas[9]);
					} catch (Exception e) {
						result[heads.length - 1] = "答辩成绩有误";
						failedList.add(result);
						continue;
					}

					GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(datas[1]);
					if (studentInfo == null) {
						result[heads.length - 1] = "找不到学号为【" + datas[1] + "】的学生";
						failedList.add(result);
						continue;
					}

					if (!datas[0].equals(studentInfo.getXm())) {
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
					if (thesisApply.getStatus() != ThesisStatusEnum.THESIS_DEFENCE.getValue()
							&& thesisApply.getStatus() != ThesisStatusEnum.THESIS_DEFENCE_FAILED.getValue()
							&& thesisApply.getStatus() != ThesisStatusEnum.THESIS_COMPLETED.getValue()) {
						result[heads.length - 1] = "论文进度不符";
						failedList.add(result);
						continue;
					}

					// 以“答辩成绩”作为课程的成绩，更新到课程中
					gjtThesisApplyService.updateScore(thesisApply.getStudentId(), defenceScore);

					if (thesisApply.getStatus() == ThesisStatusEnum.THESIS_DEFENCE.getValue()) {
						// 添加“已完成”进度
						GjtThesisStudentProg prog = new GjtThesisStudentProg();
						prog.setStudentId(thesisApply.getStudentId());
						prog.setThesisPlanId(thesisApply.getThesisPlanId());
						prog.setProgressCode(ThesisProgressCodeEnum.THESIS_COMPLETED.getCode());
						prog.setCreatedBy(user.getId());
						gjtThesisStudentProgService.insert(prog);
					}

					if (defenceScore >= 60) {
						thesisApply.setStatus(ThesisStatusEnum.THESIS_COMPLETED.getValue());
					} else {
						thesisApply.setStatus(ThesisStatusEnum.THESIS_DEFENCE_FAILED.getValue());
					}
					thesisApply.setDefenceScore(defenceScore);
					thesisApply.setUpdatedBy(user.getId());
					gjtThesisApplyService.update(thesisApply);

					result[heads.length - 1] = "成功";
					successList.add(result);
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "thesisDefenceScore_success_" + currentTimeMillis + ".xls";
			String failedFileName = "thesisDefenceScore_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "导入答辩成绩成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "导入答辩成绩失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
					+ "thesisDefencePlan" + File.separator;
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
