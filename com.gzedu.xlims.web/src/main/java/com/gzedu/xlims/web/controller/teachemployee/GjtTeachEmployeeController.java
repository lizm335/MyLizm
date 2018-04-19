package com.gzedu.xlims.web.controller.teachemployee;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtEmployeePosition;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.pojo.id.GjtEmployeePositionPK;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.api.ApiOucSyncService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtStudyCenterService;
import com.gzedu.xlims.service.teachemployee.GjtTeachEmployeeService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;
import com.gzedu.xlims.third.eechat.data.EEIMUpdateInfo;
import com.gzedu.xlims.third.eechat.data.EEIMUpdateInfoReturnData;
import com.gzedu.xlims.third.eechat.data.EEIMUserNew;
import com.gzedu.xlims.third.eechat.service.EEChatServiceImpl;
import com.gzedu.xlims.third.eechat.util.EEIMService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * Created by llx on 2017/2/17.
 */

@RequestMapping("/edumanage/teachEmployee")
@Controller
public class GjtTeachEmployeeController {

	private static final Logger log = LoggerFactory.getLogger(GjtTeachEmployeeController.class);

	@Autowired
	GjtTeachEmployeeService gjtTeachEmployeeService;

	@Autowired
	GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	PriRoleInfoService priRoleInfoService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	GjtStudyCenterService gjtStudyCenterService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	private EEChatServiceImpl eeChatService;

	@Autowired
	private EEIMService eeIMService;
	
	@Autowired
	ApiOucSyncService apiOucSyncService;
	
	/**
	 * 教职人员列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
	public String indexList(HttpServletRequest request, Model model, String action,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, String orgId) {
		// 1班主任(教学班),2辅导教师(课程班),3其它(职工),4督导教师

		Subject subject = SecurityUtils.getSubject();
		boolean headTeacher = subject.isPermitted("/edumanage/teachEmployee/list$headTeacher");
		boolean coachTeacher = subject.isPermitted("/edumanage/teachEmployee/list$coachTeacher");
		boolean supervisorTeacher = subject.isPermitted("/edumanage/teachEmployee/list$supervisorTeacher");
		// boolean guideTeacher =
		// subject.isPermitted("/edumanage/teachEmployee/list$guideTeacher");//指导
		// boolean replyTeacher =
		// subject.isPermitted("/edumanage/teachEmployee/list$replyTeacher");//答疑
		// boolean practiceTeacher =
		// subject.isPermitted("/edumanage/teachEmployee/list$practiceTeacher");//实践
		boolean classTeacher = subject.isPermitted("/edumanage/teachEmployee/list$classTeacher");

		boolean paperTeacher = subject.isPermitted("/edumanage/teachEmployee/list$paperTeacher");// 论文教师

		if (EmptyUtils.isEmpty(action)) {
			if (headTeacher) {
				action = "headTeacher";
			} else if (coachTeacher) {
				action = "coachTeacher";
			} else if (supervisorTeacher) {
				action = "supervisorTeacher";
				// } else if (guideTeacher) {
				// action = "guideTeacher";
				// } else if (replyTeacher) {
				// action = "replyTeacher";
				// } else if (practiceTeacher) {
				// action = "practiceTeacher";
			} else if (classTeacher) {
				action = "classTeacher";
			} else if (paperTeacher) {
				action = "paperTeacher";
			} else {
				action = "headTeacher";
			}
		}

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_employeeType", getType(action));
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgId", orgId);
		map.put("defaultOrgId", user.getGjtOrg().getId());

		Page<GjtEmployeeInfo> employeeInfos = gjtTeachEmployeeService.queryAll(map, searchParams, pageRequst);

		Map<String, String> orgMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());
		model.addAttribute("action", action);
		model.addAttribute("teacher", EmployeeTypeEnum.getName(action));
		model.addAttribute("pageInfo", employeeInfos);
		model.addAttribute("orgMap", orgMap);
		return "edumanage/teachEmployee/list";
	}

	/**
	 * 辅导、督导、班主任批量导入引导页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@SysLog("教职人员-导入教职人员")
	@RequestMapping(value = "importTeacher", method = { RequestMethod.POST, RequestMethod.GET })
	public String importTeacher(HttpServletRequest request, Model model, String type) {

		// supervisorTeacher,coachTeacher,headTeacher

		model.addAttribute("type", type);
		return "edumanage/teachEmployee/importTeacher";
	}

	@SysLog("教职人员-导出班主任休息")
	@RequestMapping(value = "exportTeacher", method = { RequestMethod.POST, RequestMethod.GET })
	public void exportTeacher(HttpServletResponse response, HttpServletRequest request, Model model) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<Map<String, String>> list = gjtTeachEmployeeService.queryHeardTeacher(user.getGjtOrg().getId(),
				EmployeeTypeEnum.班主任.getNum());

		try {
			HSSFWorkbook workbook = this.getHeardTeacherExcel(list);
			ExcelUtil.downloadExcelFile(response, workbook, "班主任信息.xls");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	private HSSFWorkbook getHeardTeacherExcel(List<Map<String, String>> list) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("班主任信息列表");
			String[] heads = { "帐号", "姓名", "性别", "手机号", "邮箱", "学习中心", "所属院校" };

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

			for (Map<String, String> obj : list) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;
				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("ZGH"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("XM"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				String xbm = "";
				if (StringUtils.isNotBlank(obj.get("XBM"))) {
					xbm = "1".equals(obj.get("XBM")) ? "男" : "女";
				}
				cell.setCellValue(xbm);

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("SJH"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("DZXX"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("SC_NAME"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("XXMC"));

			}

			return wb;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 保存导入的辅导、督导、班主任数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveImport", method = RequestMethod.POST)
	@ResponseBody
	public ImportFeedback saveImport(@RequestParam("file") MultipartFile file, HttpServletRequest request, Model model,
			String type) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String[] heads = null;
		if ("headTeacher".equals(type)) {
			heads = new String[11];
			heads[0] = "帐号";
			heads[1] = "姓名";
			heads[2] = "性别";
			heads[3] = "手机号";
			heads[4] = "E-Mail";
			heads[5] = "办公电话";
			heads[6] = "QQ";
			heads[7] = "备注";
			heads[8] = "院校名称";
			heads[9] = "学习中心名称";
			heads[10] = "导入结果";

		} else {
			heads = new String[9];
			heads[0] = "帐号";
			heads[1] = "姓名";
			heads[2] = "性别";
			heads[3] = "手机号";
			heads[4] = "E-Mail";
			heads[5] = "办公电话";
			heads[6] = "QQ";
			heads[7] = "备注";
			heads[8] = "导入结果";
		}
		List<String[]> successList = new ArrayList<String[]>();
		List<String[]> failedList = new ArrayList<String[]>();
		List<String[]> dataList = null;
		try {
			dataList = ExcelUtil.readAsStringList(file.getInputStream(), 2, heads.length - 1);
		} catch (Exception e) {
			return new ImportFeedback(false, "请下载正确表格模版填写");
		}

		try {
			if (dataList != null && dataList.size() > 0) {
				for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据

					if (datas.length < heads.length - 1) {
						result[heads.length - 1] = "数据不全";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[0])) { // 帐号
						result[heads.length - 1] = "帐号不能为空";
						failedList.add(result);
						continue;
					} else {
						Boolean boolean1 = gjtUserAccountService.queryByLoginAccount(datas[0]) == null ? false : true;
						if (boolean1) {
							result[heads.length - 1] = "帐号已经重复";
							failedList.add(result);
							continue;
						}
					}

					if ("".equals(datas[1])) { // 姓名
						result[heads.length - 1] = "姓名不能为空";
						failedList.add(result);
						continue;
					}

					// if ("".equals(datas[2])) { // 性别
					// result[heads.length - 1] = "性别不能为空";
					// failedList.add(result);
					// continue;
					// } else {
					if ("男".equals(datas[2])) {
						datas[2] = "1";
					} else if ("女".equals(datas[2])) {
						datas[2] = "2";
					} else {
						datas[2] = "";
					}
					// }

					if ("".equals(datas[3])) { // 手机号
						result[heads.length - 1] = "手机不能为空";
						failedList.add(result);
						continue;
					}

					GjtSchoolInfo gjtSchoolInfo = null;
					GjtStudyCenter gjtStudyCenter = null;
					GjtOrg gjtOrg = null;
					Subject subject = SecurityUtils.getSubject();
					boolean permitted = subject.isPermitted("/edumanage/teachEmployee/list$schoolModel");

					if ("headTeacher".equals(type)) {
						String orgType = user.getGjtOrg().getOrgType();
						if ("3".equals(orgType) || "6".equals(orgType)) {// 角色不属于院校，直接取当前所在的院校和学习中心ID
							gjtOrg = user.getGjtOrg();
							GjtOrg school = gjtOrgService.queryParentBySonId(gjtOrg.getId());
							gjtSchoolInfo = school.getGjtSchoolInfo();
							gjtStudyCenter = gjtOrg.getGjtStudyCenter();
						} else {
							if ("".equals(datas[8])) { // 院校名称
								result[heads.length - 1] = "院校名称不能为空";
								failedList.add(result);
								continue;
							} else {
								gjtSchoolInfo = gjtSchoolInfoService.queryByName(datas[8]);
								if (gjtSchoolInfo == null) {
									result[heads.length - 1] = "查询不到该院校";
									failedList.add(result);
									continue;
								}
							}

							if (!permitted) {// 不是教辅云，不是院校模式，才有学习中心或者招生点
								if ("".equals(datas[9])) { // 院校名称
									result[heads.length - 1] = "学习中心不能为空";
									failedList.add(result);
									continue;
								} else {
									gjtOrg = gjtOrgService.queryByschoolIdAndScName(gjtSchoolInfo.getId(), datas[9]);
									if (gjtOrg == null) {
										result[heads.length - 1] = "查询不到该院校";
										failedList.add(result);
										continue;
									}
									gjtStudyCenter = gjtOrg.getGjtStudyCenter();
								}
							}
						}
					}

					PriRoleInfo PriRoleInfo = priRoleInfoService.queryByName(EmployeeTypeEnum.getName(type));

					GjtUserAccount userAccount = new GjtUserAccount(UUIDUtils.random());
					userAccount.setLoginAccount(datas[0]);
					userAccount.setIsDeleted("N");
					userAccount.setRealName(datas[1]);
					userAccount.setEmail(datas[4]);
					userAccount.setSjh(datas[3]);
					userAccount.setIsEnabled(true);
					userAccount.setCreatedBy(user.getId());
					userAccount.setCreatedDt(DateUtils.getNowTime());
					userAccount.setPassword2("888888");
					userAccount.setPassword(Md5Util.encode("888888"));
					userAccount.setRemark(datas[7]);
					userAccount.setPriRoleInfo(PriRoleInfo);
					if ("classTeacher".equals(type) || "paperTeacher".endsWith(type)) {
						userAccount.setUserType(0);
					} else {
						userAccount.setUserType(2);
					}
					userAccount.setTelephone(datas[5]);

					GjtEmployeeInfo item = new GjtEmployeeInfo();
					String employeeId = UUIDUtils.random();
					item.setEmployeeId(employeeId);
					item.setIsDeleted("N");
					item.setIsEnabled("1");
					item.setCreatedDt(DateUtils.getNowTime());
					item.setCreatedBy(user.getId());
					item.setGjtUserAccount(userAccount);
					item.setZgh(datas[0]);
					item.setXm(datas[1]);
					item.setDzxx(datas[4]);
					item.setLxdh(datas[5]);
					item.setSjh(datas[3]);
					item.setQq(datas[6]);
					item.setWorkTime("09:00-17:00");

					if ("headTeacher".equals(type)) {
						item.setOrgId(gjtOrg.getId());
						item.setGjtSchoolInfo(gjtSchoolInfo);
						userAccount.setGjtOrg(gjtOrg);
						if (!permitted) {
							item.setGjtStudyCenter(gjtStudyCenter);
						}
					} else {
						item.setOrgId(user.getGjtOrg().getId());
						item.setGjtSchoolInfo(user.getGjtOrg().getGjtSchoolInfo());
						// item.setGjtStudyCenter(user.getGjtOrg().getGjtStudyCenter());
						userAccount.setGjtOrg(user.getGjtOrg());
					}
					item.setEmployeeType(getType(type));
					item.setXbm(datas[2]);
					// 同步
					EEIMUserNew eeimUser = null;
					String appId = null;
					try {
						// 同步班主任或辅导老师至EE平台
						if ("headTeacher".equals(type) || "coachTeacher".equals(type)) {
							if ("headTeacher".equals(type)) {// 班主任
								eeimUser = new EEIMUserNew(employeeId, item.getXm(), "2", "", "");
								appId = OrgUtil.getEEChatAppId(gjtSchoolInfo.getGjtOrg().getCode());
							}
							if ("coachTeacher".equals(type)) {// 辅导老师
								eeimUser = new EEIMUserNew(employeeId, item.getXm(), "1", "", "");
								appId = OrgUtil.getEEChatAppId(user.getGjtOrg().getCode());
							}
							String eeno = eeChatService.syncSingleStudents(appId, eeimUser);
							item.setEeno(eeno);
						}
					} catch (Exception e) {
						String objJson = GsonUtils.toJson(eeimUser);
						log.error("createHeadTeacherEENo fail ======== params:" + objJson);
						log.error("批量导入教师后同步至EE平台失败！ ");
					}
					gjtTeachEmployeeService.saveEmployee(item);
					result[heads.length - 1] = "新增成功";
					successList.add(result);
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "employeebook_success_" + currentTimeMillis + ".xls";
			String failedFileName = "employee_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "教职工导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "教职工导入失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
					+ "employee" + File.separator;
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
	 * 新增辅导、督导、班主任老师页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "addTeacher", method = { RequestMethod.GET })
	public String addTeacher(HttpServletRequest request, String type, Model model) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		model.addAttribute("type", type);
		model.addAttribute("isAdd", true);
		if ("headTeacher".equals(type)) {// 班主任
			String orgType = user.getGjtOrg().getOrgType();
			Map<String, String> schoolInfoMap = null;
			if ("3".equals(orgType) || "6".equals(orgType)) {// 角色不属于院校，直接取当前所在的院校和学习中心ID
				GjtOrg studyCenter = user.getGjtOrg();
				GjtOrg school = gjtOrgService.queryParentBySonId(studyCenter.getId());
				schoolInfoMap = new HashMap<String, String>();
				schoolInfoMap.put("id", school.getId());
				schoolInfoMap.put("name", school.getOrgName());

				model.addAttribute("orgType", "son");
				model.addAttribute("studyCenter", studyCenter);
			} else {
				schoolInfoMap = commonMapService.getOrgMap(user.getId()); // 学校
				model.addAttribute("orgType", "parent");
			}

			model.addAttribute("schoolInfoMap", schoolInfoMap);

			Subject subject = SecurityUtils.getSubject();
			boolean permitted = subject.isPermitted("/edumanage/teachEmployee/list$schoolModel");
			if (permitted) {
				return "edumanage/teachEmployee/addSchoolHeadTeacher";
			} else {
				return "edumanage/teachEmployee/addHeadTeacher";
			}
		} else {
			return "edumanage/teachEmployee/addCoachOrSupervisorTeacher";
		}
	}

	@RequestMapping(value = "updateTeacher", method = RequestMethod.GET)
	public String updateTeacher(String id, HttpServletRequest request, String type, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryById(id);

		model.addAttribute("gjtEmployeeInfo", employeeInfo);
		model.addAttribute("type", type);
		model.addAttribute("isAdd", false);
		if ("headTeacher".equals(type)) {// 班主任
			String orgType = user.getGjtOrg().getOrgType();
			Map<String, String> schoolInfoMap = null;
			if ("3".equals(orgType) || "6".equals(orgType)) {// 角色不属于院校，直接取当前所在的院校和学习中心ID
				GjtOrg studyCenter = user.getGjtOrg();
				GjtOrg school = gjtOrgService.queryParentBySonId(studyCenter.getId());
				schoolInfoMap = new HashMap<String, String>();
				schoolInfoMap.put("id", school.getId());
				schoolInfoMap.put("name", school.getOrgName());

				model.addAttribute("orgType", "son");
				model.addAttribute("studyCenter", studyCenter);
			} else {
				schoolInfoMap = commonMapService.getOrgMap(user.getId()); // 学校
				Map<String, String> studyCenterMap = commonMapService
						.getStudyCenterMap(employeeInfo.getGjtSchoolInfo().getId());
				model.addAttribute("orgType", "parent");
				model.addAttribute("studyCenterMap", studyCenterMap);
			}
			model.addAttribute("schoolInfoMap", schoolInfoMap);

			Subject subject = SecurityUtils.getSubject();
			boolean permitted = subject.isPermitted("/edumanage/teachEmployee/list$schoolModel");
			if (permitted) {
				return "edumanage/teachEmployee/addSchoolHeadTeacher";
			} else {
				return "edumanage/teachEmployee/addHeadTeacher";
			}
		}
		if ("paperTeacher".equals(type)) {
			List<GjtEmployeePosition> list = employeeInfo.getGjtEmployeePositionList();
			Map<String, Boolean> checkedMap = new HashMap<String, Boolean>();
			if (list != null && list.size() > 0) {
				for (GjtEmployeePosition position : list) {
					checkedMap.put(position.getId().getType().toString(), true);
				}
			}
			model.addAttribute("checkedMap", checkedMap);
		}

		return "edumanage/teachEmployee/addCoachOrSupervisorTeacher";
	}

	/**
	 * 保存新增的辅导、督导、班主任数据
	 * 
	 * @param request
	 * @param item
	 * @return
	 */
	@SysLog("教职人员-新增教职人员")
	@RequestMapping(value = "saveTeacher", method = { RequestMethod.POST })
	public String saveTeacher(HttpServletRequest request, RedirectAttributes redirectAttributes, @Valid GjtEmployeeInfo item, Model model) {
		Feedback feedback = new Feedback(true, "添加成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String employeeId = UUIDUtils.random().toString();
		String tempType = item.getEmployeeType();
		item.setEmployeeType(getType(tempType));

		String[] positions = request.getParameterValues("employeeTypes");

		if ("paperTeacher".equals(tempType)) {
			if (positions != null && positions.length > 0) {
				item.setGjtEmployeePositionList(new ArrayList<GjtEmployeePosition>());
				for (int i = 0; i < positions.length; i++) {
					GjtEmployeePosition position = new GjtEmployeePosition();
					GjtEmployeePositionPK pkId = new GjtEmployeePositionPK(employeeId, NumberUtils.toInt(positions[i]));
					position.setId(pkId);
					item.addGjtEmployeePositionList(position);
				}
			}
		}
		GjtSchoolInfo gjtSchoolInfo = null;
		GjtUserAccount userAccount = new GjtUserAccount(UUIDUtils.random());
		PriRoleInfo PriRoleInfo;
		if ("headTeacher".equals(tempType)) {
			gjtSchoolInfo = gjtSchoolInfoService.queryById(item.getXxId());
			item.setOrgId(gjtSchoolInfo.getId());
			item.setGjtSchoolInfo(gjtSchoolInfo);
			userAccount.setGjtOrg(gjtSchoolInfo.getGjtOrg());
			String schoolModel = gjtSchoolInfo.getGjtOrg().getSchoolModel();// 办学模式
			Subject subject = SecurityUtils.getSubject();
			boolean permitted = subject.isPermitted("/edumanage/teachEmployee/list$schoolModel");
			if (!permitted) {// 不是教辅云，不是院校模式，才有学习中心或者招生点
				GjtStudyCenter gjtStudyCenter = gjtStudyCenterService.queryById(item.getXxzxId());
				item.setGjtStudyCenter(gjtStudyCenter);
			}
			if ("3".equals(schoolModel)) {// TODO-- 班主任（有考试院校模式）班主任（无考试院校模式）
				PriRoleInfo = priRoleInfoService.queryByName("班主任（有考试院校模式）");
			} else if ("4".equals(schoolModel)) {
				PriRoleInfo = priRoleInfoService.queryByName("班主任（无考试院校模式）");
			} else {
				PriRoleInfo = priRoleInfoService.queryByName(EmployeeTypeEnum.getName(tempType));
			}
		} else {
			PriRoleInfo = priRoleInfoService.queryByName(EmployeeTypeEnum.getName(tempType));
		}

		userAccount.setLoginAccount(item.getZgh());
		userAccount.setIsDeleted("N");
		userAccount.setRealName(item.getXm());
		userAccount.setEmail(item.getDzxx());
		userAccount.setSjh(item.getSjh());
		userAccount.setIsEnabled(true);
		userAccount.setCreatedBy(user.getId());
		userAccount.setCreatedDt(DateUtils.getNowTime());
		userAccount.setPriRoleInfo(PriRoleInfo);
		userAccount.setPassword2("888888");
		userAccount.setPassword(Md5Util.encode("888888"));
		if ("classTeacher".equals(tempType) || "paperTeacher".endsWith(tempType)) {
			userAccount.setUserType(0);
		} else {
			userAccount.setUserType(2);
		}
		userAccount.setTelephone(item.getLxdh());

		item.setEmployeeId(employeeId);
		item.setIsDeleted("N");
		item.setCreatedDt(DateUtils.getNowTime());
		item.setCreatedBy(user.getId());
		item.setIsEnabled("1");
		item.setGjtUserAccount(userAccount);
		if (!"headTeacher".equals(tempType)) {
			item.setOrgId(user.getGjtOrg().getId());
			item.setGjtSchoolInfo(user.getGjtOrg().getGjtSchoolInfo());
			userAccount.setGjtOrg(user.getGjtOrg());
		}
		EEIMUserNew eeimUser = null;
		String appId = null;
		try {
			// 同步班主任或辅导老师至EE平台
			if ("headTeacher".equals(tempType) || "coachTeacher".equals(tempType)) {
				if ("headTeacher".equals(tempType)) {// 班主任
					eeimUser = new EEIMUserNew(employeeId, item.getXm(), "2", "", "");
					appId = OrgUtil.getEEChatAppId(gjtSchoolInfo.getGjtOrg().getCode());
				}
				if ("coachTeacher".equals(tempType)) {// 辅导老师
					eeimUser = new EEIMUserNew(employeeId, item.getXm(), "1", "", "");
					appId = OrgUtil.getEEChatAppId(user.getGjtOrg().getCode());
				}
				String eeno = eeChatService.syncSingleStudents(appId, eeimUser);
				item.setEeno(eeno);
			}
		} catch (Exception e) {
			String objJson = GsonUtils.toJson(eeimUser);
			log.error("createHeadTeacherEENo fail ======== params:" + objJson);
			log.error("同步老师至EE平台失败！ ");
		}
		
		
		// 国开在线模式把账号同步到国开学习网
		if ("5".equals(user.getGjtOrg().getSchoolModel())) {
			Map formMap = new HashMap();
			Map dataMap = new HashMap();
			dataMap.put("OrgCode", user.getGjtOrg().getVirtualXxzxCode());
			dataMap.put("UserName", userAccount.getLoginAccount());
			dataMap.put("RealName", userAccount.getRealName());
			dataMap.put("TeacherNO", userAccount.getLoginAccount());
			dataMap.put("Password", userAccount.getPassword2());
			dataMap.put("EMAIL", userAccount.getEmail());
			
			formMap.put("synchDATA", dataMap);
			formMap.put("operatingUserName", user.getLoginAccount());
			formMap.put("functionType", "SynchTeacher");
			Map jsonMap = apiOucSyncService.addAPPDataSynch(formMap);
			
			String code = ObjectUtils.toString(jsonMap.get("code"));
			
			if ("1".equals(code)) {
				gjtTeachEmployeeService.saveEmployee(item);
				feedback = new Feedback(true, "新增账号成功！");
			} else {
				String ErrorMessage = "同步数据到国开总部失败！";
				if (EmptyUtils.isNotEmpty(ObjectUtils.toString(jsonMap.get("data")))) {
					List dataList = (List)JSON.parse(ObjectUtils.toString(jsonMap.get("data")));
					Map dataMaps = (Map)dataList.get(0);
					ErrorMessage = ObjectUtils.toString(dataMaps.get("ErrorMessage"));
				}
				
				feedback = new Feedback(false, ErrorMessage+"，如有疑问请联系管理员！");
			}
		} else {
			gjtTeachEmployeeService.saveEmployee(item);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		model.addAttribute("tempType", tempType);
		return "redirect:/edumanage/teachEmployee/list?action="+tempType;
	}

	@SysLog("教职人员-修改教职人员")
	@RequestMapping(value = "update", method = { RequestMethod.POST })
	public String updateTeacher(HttpServletRequest request, @Valid GjtEmployeeInfo item, Model model) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		String tempType = item.getEmployeeType();
		item.setEmployeeType(getType(tempType));

		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryById(item.getEmployeeId());
		GjtUserAccount userAccount = employeeInfo.getGjtUserAccount();

		try {
			String[] positions = request.getParameterValues("employeeTypes");
			if ("paperTeacher".equals(tempType)) {
				gjtEmployeeInfoService.deletePositionByEmployeeId(item.getEmployeeId());
				if (positions != null && positions.length > 0) {
					for (String types : positions) {
						gjtEmployeeInfoService.addPosition(employeeInfo.getEmployeeId(), types);
					}
				}
			}

			if ("headTeacher".equals(tempType)) {
				GjtSchoolInfo gjtSchoolInfo = gjtSchoolInfoService.queryById(item.getXxId());
				employeeInfo.setOrgId(gjtSchoolInfo.getId());
				employeeInfo.setGjtSchoolInfo(gjtSchoolInfo);
				userAccount.setGjtOrg(gjtSchoolInfo.getGjtOrg());

				Subject subject = SecurityUtils.getSubject();
				boolean permitted = subject.isPermitted("/edumanage/teachEmployee/list$schoolModel");
				if (!permitted) {// 不是教辅云，不是院校模式，才有学习中心或者招生点
					GjtStudyCenter gjtStudyCenter = gjtStudyCenterService.queryById(item.getXxzxId());
					employeeInfo.setGjtStudyCenter(gjtStudyCenter);
				}

			}

			userAccount.setRealName(item.getXm());
			userAccount.setEmail(item.getDzxx());
			userAccount.setSjh(item.getSjh());
			userAccount.setUpdatedBy(user.getId());
			userAccount.setUpdatedDt(DateUtils.getNowTime());
			userAccount.setRemark(item.getIndividualitySign());
			userAccount.setTelephone(item.getLxdh());

			employeeInfo.setUpdatedDt(DateUtils.getNowTime());
			employeeInfo.setUpdatedBy(user.getId());
			employeeInfo.setDzxx(item.getDzxx());
			employeeInfo.setQq(item.getQq());
			employeeInfo.setXm(item.getXm());
			employeeInfo.setLxdh(item.getLxdh());
			employeeInfo.setSjh(item.getSjh());
			employeeInfo.setWorkTime(item.getWorkTime());
			employeeInfo.setXbm(item.getXbm());
			employeeInfo.setIndividualitySign(item.getIndividualitySign());
			employeeInfo.setZp(item.getZp());

			employeeInfo.setGjtUserAccount(userAccount);
			// if (!"headTeacher".equals(tempType)) {
			// employeeInfo.setOrgId(user.getGjtOrg().getId());
			// employeeInfo.setGjtSchoolInfo(user.getGjtOrg().getGjtSchoolInfo());
			// }
			// 把更新后的信息同步至EE平台
			EEIMUpdateInfo info = new EEIMUpdateInfo();
			List<EEIMUpdateInfo> employee = new ArrayList<EEIMUpdateInfo>();
			info.setUSER_ID(item.getEmployeeId());
			info.setUSER_NAME(item.getXm());
			info.setUSER_IMG("");
			employee.add(info);
			EEIMUpdateInfoReturnData resultData = eeIMService.updateDataD2(employee);
			if (Constants.BOOLEAN_1.equals(resultData.getSTATUS())) {
				gjtTeachEmployeeService.saveEmployee(employeeInfo);
			} else {
				log.info("更新后的教师信息同步至EE平台失败：" + resultData);
				log.info("更新后的教师信息同步至EE平台失败：" + item.getEmployeeId());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		model.addAttribute("tempType", tempType);
		return "redirect:/edumanage/teachEmployee/list?action={tempType}";
	}

	public String getType(String tempType) {
		if ("supervisorTeacher".equals(tempType)) {
			return Integer.toString(EmployeeTypeEnum.督导教师.getNum());
		}
		if ("coachTeacher".equals(tempType)) {
			return Integer.toString(EmployeeTypeEnum.辅导教师.getNum());
		}

		// if ("guideTeacher".equals(tempType)) {
		// return Integer.toString(EmployeeTypeEnum.论文指导教师.getNum());
		// }
		// if ("replyTeacher".equals(tempType)) {
		// return Integer.toString(EmployeeTypeEnum.论文答辩教师.getNum());
		// }
		// if ("practiceTeacher".equals(tempType)) {
		// return Integer.toString(EmployeeTypeEnum.社会实践教师.getNum());
		// }

		if ("classTeacher".equals(tempType)) {
			return Integer.toString(EmployeeTypeEnum.任课教师.getNum());
		}

		if ("paperTeacher".equals(tempType)) {
			return Integer.toString(EmployeeTypeEnum.论文教师.getNum());
		}

		return Integer.toString(EmployeeTypeEnum.班主任.getNum());
	}

	/**
	 * 校验帐号是否存在
	 */
	@RequestMapping(value = "checkLogin", method = { RequestMethod.POST })
	@ResponseBody
	public Feedback checkLogin(String zgh) throws IOException {
		GjtUserAccount userAccount = gjtUserAccountService.queryByLoginAccount(zgh);
		boolean bool = userAccount != null ? true : false;
		Feedback fe = new Feedback(bool, "");
		fe.setSuccessful(bool);
		return fe;
	}

	/**
	 * 根据学校id查询学习中心列表
	 * 
	 * @param xxId
	 * @return
	 */
	@RequestMapping(value = "querySchoolCenterBySchool", method = { RequestMethod.POST })
	@ResponseBody
	public List<Map<String, String>> querySchoolCenterBySchool(String xxId) {
		Map<String, String> map = commonMapService.getStudyCenterMap(xxId);
		Set set = map.entrySet();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>(set);
		return list;
	}

	// 单个删除
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids) throws IOException {
		Feedback fb = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(ids)) {
			try {
				GjtEmployeeInfo queryById = gjtEmployeeInfoService.queryById(ids);
				gjtEmployeeInfoService.deleteById(ids);
				gjtUserAccountService.deleteById(queryById.getGjtUserAccount().getId());
			} catch (Exception e) {
				fb = new Feedback(false, "删除失败");
				log.error(e.getMessage(), e);
			}
		}
		return fb;
	}

	// 密码重置
	@RequestMapping(value = "resetPassword")
	@ResponseBody
	public Feedback resetPassword(String id) throws IOException {
		Feedback fb = new Feedback(true, "密码重置成功");
		if (StringUtils.isNotBlank(id)) {
			try {
				// 根据employee重置userAccout的密码
				Boolean updatePwd = gjtEmployeeInfoService.updatePwd(id);
				if (!updatePwd) {
					fb = new Feedback(false, "密码重置失败");
				}
			} catch (Exception e) {
				fb = new Feedback(false, "密码重置失败");
				log.error(e.getMessage(), e);
			}
		}
		return fb;
	}

}
