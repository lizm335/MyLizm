/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtStudyCenterService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 
 * 功能说明：教学班级学员列表
 * 
 * @author 卢林林 lulinlin@eenet.com
 * @Date 2017年8月16日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Controller
@RequestMapping("/edumanage/teachClassStudent")
public class GjtTeachClassStudentController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(GjtTeachClassStudentController.class);

	@Autowired
	GjtClassStudentService gjtClassStudentService;

	@Autowired
	GjtClassInfoService gjtClassInfoService;

	@Autowired
	GjtGradeService gjtGradeService;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	CacheService cacheService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtStudyCenterService gjtStudyCenterService;

	/**
	 * 查询所有教务班级的学员
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param judge
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, String judge,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		// Page<GjtClassStudent> page =
		// gjtClassStudentService.queryAll(user.getGjtOrg().getId(),
		// searchParams,
		// pageRequst);
		/**
		 * 查询学员所在的教务班级
		 */
		Page<Map<String, Object>> page = gjtStudentInfoService.queryStudentTeachClassInfo(user.getGjtOrg().getId(),
				searchParams, pageRequst);
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 培养层次
		Map<String, String> specialtyMap = null;
		if ("3".equals(user.getGjtOrg().getSchoolModel())) {
			specialtyMap = commonMapService.getSchoolModelSpecialtyMap(user.getGjtOrg().getId());
		} else {
			specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		}
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("pageInfo", page);

		return "edumanage/teachClassStudent/list";
	}

	@RequestMapping(value = "export", method = RequestMethod.GET)
	public void export(String judge, HttpServletRequest request, String orgId, String specialtyId,
			HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("orgId", orgId);
		searchParams.put("specialtyId", specialtyId);

		List<Map<String, Object>> list = gjtStudentInfoService.exportStudentTeachClassInfo(user.getGjtOrg().getId(),
				searchParams);

		try {
			HSSFWorkbook workbook = this.getStudentInfoExcel(list);
			ExcelUtil.downloadExcelFile(response, workbook, "教务班级学员列表.xls");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private HSSFWorkbook getStudentInfoExcel(List<Map<String, Object>> list) {
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		Map<String, String> StudentNumberStatusMap = commonMapService.getDates("StudentNumberStatus");
		Map<String, String> userTypeMap = commonMapService.getDates("USER_TYPE");
		Map<String, String> zlspMap = new HashMap<String, String>();
		zlspMap.put("0", "不通过");
		zlspMap.put("1", "通过");
		zlspMap.put("2", "重新提交");
		zlspMap.put("3", "待审核");
		zlspMap.put("4", "未提交");
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("教务班级列表");
			String[] heads = { "姓名", "性别", "学号", "手机号", "身份证号", "层次", "年级", "专业", "学习中心", "学籍状态", "资料审批", "学员类型",
					"工作单位", "邮箱", "教材邮寄地址", "班级名称", "班主任" };

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

			for (Map<String, Object> item : list) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;
				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(ObjectUtils.toString(item.get("XM")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				String xbm = ObjectUtils.toString(item.get("XBM"));
				cell.setCellValue("1".equals(xbm) ? "男" : "女");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(ObjectUtils.toString(item.get("XH")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(ObjectUtils.toString(item.get("SJH")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(ObjectUtils.toString(item.get("SFZH")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				String pycc = ObjectUtils.toString(item.get("PYCC"));
				cell.setCellValue(pyccMap.get(pycc));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(ObjectUtils.toString(item.get("GRADE_NAME")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(ObjectUtils.toString(item.get("ZYMC")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(ObjectUtils.toString(item.get("ORG_NAME")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				String xjzt = ObjectUtils.toString(item.get("XJZT"));
				cell.setCellValue(StudentNumberStatusMap.get(xjzt));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				String auditState = ObjectUtils.toString(item.get("AUDIT_STATE"));
				cell.setCellValue(zlspMap.get(auditState));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				String userType = ObjectUtils.toString(item.get("USER_TYPE"));
				cell.setCellValue(userTypeMap.get(userType));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(ObjectUtils.toString(item.get("SC_CO")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(ObjectUtils.toString(item.get("DZXX")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(ObjectUtils.toString(item.get("TXDZ")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(ObjectUtils.toString(item.get("BJMC")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(ObjectUtils.toString(item.get("TEACH_NAME")));

			}
			return wb;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/** 进入导入页面 */
	@RequestMapping(value = "toImport")
	public String toImport() {
		return "edumanage/teachClassStudent/teach_class_import";
	}

	/** 下载文件 */
	@ResponseBody
	@RequestMapping(value = "download")
	public void download(HttpServletRequest request, HttpServletResponse response, String file) throws IOException {
		if ("template".equals(file)) {
			InputStream in = this.getClass().getResourceAsStream("/excel/model/批量调班信息表.xls");
			super.downloadInputStream(response, in, "批量调班信息表.xls");
		} else {
			String path = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
					+ "teachClass" + File.separator;
			super.downloadFile(request, response, path + file);
		}
	}

	/** 批量导入调班信息 */
	@SysLog("教务班级学员-批量导入调班信息")
	@RequiresPermissions("/edumanage/teachClassStudent/list$importStuClassInfo")
	@ResponseBody
	@RequestMapping(value = "importStuClassInfo")
	public Map importStuClassInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		String path = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
				+ "teachClass" + File.separator;
		String fileName = file.getOriginalFilename();
		File targetFile = new File(path, fileName);
		if (!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}
		file.transferTo(targetFile);
		Map result = gjtClassStudentService.importStuClassInfo(targetFile, path);
		targetFile.delete();
		return result;
	}
}
