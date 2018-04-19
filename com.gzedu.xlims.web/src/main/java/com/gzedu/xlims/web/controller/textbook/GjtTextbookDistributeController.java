package com.gzedu.xlims.web.controller.textbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.common.constants.TextbookConstant;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistribute;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistributeDetail;
import com.gzedu.xlims.pojo.textbook.GjtTextbookOrderDetail;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStock;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockApproval;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockOpera;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockOperaBatch;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.dictionary.GjtDistrictService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.textbook.GjtTextbookDistributeDetailService;
import com.gzedu.xlims.service.textbook.GjtTextbookDistributeService;
import com.gzedu.xlims.service.textbook.GjtTextbookOrderDetailService;
import com.gzedu.xlims.service.textbook.GjtTextbookService;
import com.gzedu.xlims.service.textbook.GjtTextbookStockOperaBatchService;
import com.gzedu.xlims.service.textbook.GjtTextbookStockService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/textbookDistribute")
public class GjtTextbookDistributeController {

	private static final Logger log = LoggerFactory.getLogger(GjtTextbookDistributeController.class);

	@Autowired
	private GjtTextbookDistributeService gjtTextbookDistributeService;

	@Autowired
	private GjtTeachPlanService gjtTeachPlanService;

	@Autowired
	private GjtTextbookDistributeDetailService gjtTextbookDistributeDetailService;

	@Autowired
	private GjtTextbookService gjtTextbookService;

	@Autowired
	private GjtTextbookStockService gjtTextbookStockService;

	@Autowired
	private GjtTextbookStockOperaBatchService gjtTextbookStockOperaBatchService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private CodeGeneratorService codeGeneratorService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtTextbookOrderDetailService gjtTextbookOrderDetailService;

	@Autowired
	private GjtOrgService gjtOrgService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtDistrictService gjtDistrictService;


	@Value("#{configProperties['queryOrderUrl']}")
	private String queryOrderUrl;

	private boolean isBookseller(GjtUserAccount user) {
		return "书商".equals(user.getPriRoleInfo().getRoleName());
	}


	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String orderList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("orgId", user.getGjtOrg().getId());
		String isBookseller = isBookseller(user) ? "true" : null;
		searchParams.put("isBookseller", isBookseller);
		Page<Map<String, Object>> pageInfo = gjtTextbookDistributeService.queryDistributeList(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		pageRequst = Servlets.buildPageRequest(1, 1);
		searchParams.remove("status");
		model.addAttribute("all", gjtTextbookDistributeService.queryDistributeList(searchParams, pageRequst).getTotalElements());
		searchParams.put("status", 1);
		model.addAttribute("notDistribute", gjtTextbookDistributeService.queryDistributeList(searchParams, pageRequst).getTotalElements());
		searchParams.put("status", 2);
		model.addAttribute("distributing", gjtTextbookDistributeService.queryDistributeList(searchParams, pageRequst).getTotalElements());
		searchParams.put("status", 3);
		model.addAttribute("signed", gjtTextbookDistributeService.queryDistributeList(searchParams, pageRequst).getTotalElements());

		List<String> distributeIds = new ArrayList<String>();
		for (Map<String, Object> map : pageInfo.getContent()) {
			distributeIds.add((String) map.get("DISTRIBUTE_ID"));
		}
		if (CollectionUtils.isNotEmpty(distributeIds)) {
			searchParams = new HashMap<String, Object>();
			searchParams.put("IN_distributeId", distributeIds);
			List<GjtTextbookDistributeDetail> details = gjtTextbookDistributeDetailService.findAll(searchParams);
			model.addAttribute("details", details);
		}

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnImport", subject.isPermitted("/textbookDistribute/list$importWaybillCode"));
		model.addAttribute("isBtnExport", subject.isPermitted("/textbookDistribute/list$exportDistribute"));
		model.addAttribute("isBtnExport2", subject.isPermitted("/textbookDistribute/list$exportNotSign"));
		model.addAttribute("isBtnView", subject.isPermitted("/textbookDistribute/list$view"));

		return "textbook/distribute_list";
	}

	/**
	 * 导出订单信息
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月24日 下午5:14:01
	 * @param model
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "exportOrderList", method = RequestMethod.GET)
	public void exportOrderList(Model model, HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> excelBeans = new HashMap<String, Object>();
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("orgId", user.getGjtOrg().getId());
		String isBookseller = isBookseller(user) ? "true" : null;
		searchParams.put("isBookseller", isBookseller);
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Page<Map<String, Object>> pageInfo = gjtTextbookDistributeService.queryDistributeList(searchParams, pageRequst);
		List<Map<String, Object>> list=pageInfo.getContent();
		excelBeans.put("list", list);
		List<Map<String, Object>> details = null;
		if (CollectionUtils.isNotEmpty(list)) {
			details = gjtTextbookDistributeService.queryDistributeDetailList(searchParams);
		}
		for (Map<String, Object> map : list) {
			searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_distributeId", map.get("DISTRIBUTE_ID"));

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < details.size(); i++) {
				if (details.get(i).get("distributeId").equals(map.get("DISTRIBUTE_ID"))) {
					sb.append(details.get(i).get("textbookName")).append(" ");
					sb.append("数量：").append(details.get(i).get("quantity")).append(" ");
					sb.append("单价：￥").append(details.get(i).get("price")).append(" ");
					if (i < details.size() - 1) {
						sb.append("\r\n");
					}
				}
			}
			map.put("detail", sb.toString());
			BigDecimal status = (BigDecimal) map.get("STATUS");
			if (status != null) {
				if (status.intValueExact() == 1)
					map.put("STATUS_NAME", "待配送");
				else if (status.intValueExact() == 2)
					map.put("STATUS_NAME", "已发货");
				else if (status.intValueExact() == 3)
					map.put("STATUS_NAME", "已完成");
			}

		}

		String path = getClass().getResource(WebConstants.EXCEL_MODEL_URL).getPath() + "教材订单导出模板.xls";
		com.gzedu.xlims.common.ExcelUtil.exportExcel(excelBeans, path, response, "教材订单信息.xls");
	}

	@RequestMapping(value = "view")
	public String view(String studentId, String distributeId, Model model) {
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);

		GjtTextbookDistribute textbookDistribute = gjtTextbookDistributeService.queryById(distributeId);
		model.addAttribute("expressMap", EnumUtil.getExpressMap());
		model.addAttribute("studentInfo", studentInfo);
		model.addAttribute("textbookDistribute", textbookDistribute);

		return "textbook/textbookDistribute_detail";
	}

	/**
	 * 导出待配送教材列表
	 * 
	 * @return
	 */
	@RequiresPermissions("/textbookDistribute/list$importWaybillCode")
	@RequestMapping(value = "exportCurrentDistributeList2")
	public void exportCurrentDistributeList2(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		String orgId = "";
		if ("1".equals(user.getGjtOrg().getOrgType())) {
			orgId = user.getGjtOrg().getId();
		} else {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(user.getGjtOrg().getId());
		}
		
		List<Map<String, String>> list = gjtTextbookOrderDetailService.queryCurrentDistributeList2(orgId);
		
		HSSFWorkbook workbook = this.getCurrentDistributeWorkbook(list);
		ExcelUtil.downloadExcelFile(response, workbook, "待配送教材订单.xls");
	}

	private HSSFWorkbook getCurrentDistributeWorkbook(List<Map<String, String>> list) {
		InputStream fis = null;
		try {
			fis = GjtTextbookDistributeController.class.getResourceAsStream(WebConstants.EXCEL_MODEL_URL + "运单号导入表.xls");
			HSSFWorkbook wb = new HSSFWorkbook(fis);
			HSSFSheet sheet = wb.getSheetAt(0);

			int rowIdx = 3;
			int colIdx = 0;
			HSSFCell cell;

			HSSFRow row;
			for (Map<String, String> obj : list) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("studentName"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("id"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("studentCode"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("gradeName"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("specialtyName"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("trainingLevel"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("mobile"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("address"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("textbookName"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("textbookCount"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(obj.get("orderCode"));
			}

			return wb;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e2) {

				}
			}
		}
		
		return null;
	}

	@RequiresPermissions("/textbookDistribute/list$importWaybillCode")
	@RequestMapping(value = "importWaybillCode")
	@ResponseBody
	public ImportFeedback importWaybillCode(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "姓名", "身份证号", "学号", "年级", "专业", "层次", "手机号", "收货地址", "发放教材", "发放数量", "订单号", "运单号", "物流公司",
					"导入结果" };
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

					if ("".equals(datas[10])) { // 订单号
						result[heads.length - 1] = "订单号不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[11])) { // 运单号
						result[heads.length - 1] = "运单号不能为空";
						failedList.add(result);
						continue;
					}

					// try {
					// Float.parseFloat(datas[12]); // 运费
					// } catch (Exception e) {
					// result[heads.length - 1] = "运费格式不正确";
					// failedList.add(result);
					// continue;
					// }

					if ("".equals(datas[12]) || !TextbookConstant.expressMap.containsKey(datas[12])) { // 物流公司
						result[heads.length - 1] = "物流公司不正确";
						failedList.add(result);
						continue;
					}

					GjtTextbookDistribute textbookDistribute = gjtTextbookDistributeService
							.findByOrderCodeAndStatusAndIsDeleted(datas[10], 1, "N");
					if (textbookDistribute == null) {
						result[heads.length - 1] = "根据订单号找不到待配送的订单";
						failedList.add(result);
						continue;
					}

					// 更新
					textbookDistribute.setWaybillCode(datas[11]);
					// textbookDistribute.setFreight(new BigDecimal(datas[12]));
					textbookDistribute.setLogisticsComp(datas[12]);
					textbookDistribute.setStatus(2);
					textbookDistribute.setDistributionDt(new Date());
					textbookDistribute.setUpdatedBy(user.getId());
					gjtTextbookDistributeService.update(textbookDistribute);

					// 更新明细状态
					List<GjtTextbookDistributeDetail> distributeDetails = textbookDistribute
							.getGjtTextbookDistributeDetails();
					for (GjtTextbookDistributeDetail distributeDetail : distributeDetails) {
						distributeDetail.setStatus(2);
					}
					gjtTextbookDistributeDetailService.update(distributeDetails);

					result[heads.length - 1] = "导入成功";
					successList.add(result);
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "textbookDistribute_success_" + currentTimeMillis + ".xls";
			String failedFileName = "textbookDistribute_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "运单号导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "运单号导入失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "textbookDistribute" + File.separator;
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
	 * 查询待发教材汇总信息
	 * 
	 * @param textbookType
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "queryCurrentDistributeInfo")
	public String queryCurrentDistributeInfo(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String orgId = user.getGjtOrg().getId();

		Map<String, String> textbookPlanMap = commonMapService.getTextbookPlanMap(user.getGjtOrg().getId());
		model.addAttribute("textbookPlanMap", textbookPlanMap);

		String planId = "-1";
		if (textbookPlanMap != null) {
			Iterator<Entry<String, String>> iterator = textbookPlanMap.entrySet().iterator();
			if (iterator.hasNext()) {
				planId = iterator.next().getKey();
			}
		}
		model.addAttribute("planId", planId);

		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_gjtTextbookPlan.orgId", orgId);
		searchParams.put("EQ_planId", planId);
		searchParams.put("EQ_status", 3);
		searchParams.put("EQ_needDistribute", 1);

		Map<String, Object> searchParams2 = new HashMap<String, Object>();
		searchParams2.put("EQ_orgId", orgId);
		searchParams2.put("EQ_planId", planId);
		searchParams2.put("EQ_status", 3);
		searchParams2.put("EQ_needDistribute", 1);
		searchParams2.put("EQ_stockStatus", 1); // 库存充足

		// 查询当前学期
		/*
		 * GjtGrade grade = gjtGradeService.findCurrentGrade(orgId); if
		 * (studentType == 1) { //只统计旧生 if (grade != null) {
		 * searchParams.put("NE_gjtStudentInfo.nj", grade.getGradeId());
		 * searchParams2.put("NE_gradeId", grade.getGradeId()); } } else {
		 * //只统计新生 if (grade != null) { searchParams.put("EQ_gjtStudentInfo.nj",
		 * grade.getGradeId()); searchParams2.put("EQ_gradeId",
		 * grade.getGradeId()); } else {
		 * searchParams.put("EQ_gjtStudentInfo.nj", " ");
		 * searchParams2.put("EQ_gradeId", " "); } }
		 */

		Page<GjtTextbookOrderDetail> pageInfo = gjtTextbookOrderDetailService.findAll(searchParams, pageRequst);
		model.addAttribute("currentDistributeCount", pageInfo.getTotalElements());

		// 查询库存充足的待发教材数
		Page<Map<String, Object>> pageInfo2 = gjtTextbookOrderDetailService.findAllSummary(searchParams2, pageRequst);
		int enoughCurrentDistributeCount = 0;
		if (pageInfo2 != null && pageInfo2.getContent() != null) {
			for (Map<String, Object> obj : pageInfo2) {
				enoughCurrentDistributeCount += ((BigDecimal) obj.get("distributeNum")).intValue();
			}
		}
		model.addAttribute("enoughCurrentDistributeCount", enoughCurrentDistributeCount);

		return "textbook/textbookDistribute_order";
	}

	/**
	 * 查询待发教材汇总信息
	 * 
	 * @param textbookType
	 * @param planId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "queryCurrentDistributeInfoJson")
	@ResponseBody
	public Map<String, Long> queryCurrentDistributeInfoJson(@RequestParam int textbookType, @RequestParam String planId,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String orgId = user.getGjtOrg().getId();

		Map<String, Long> map = new HashMap<String, Long>();

		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_gjtTextbookPlan.orgId", orgId);
		searchParams.put("EQ_gjtTextbook.textbookType", textbookType);
		searchParams.put("EQ_planId", planId);
		searchParams.put("EQ_status", 3);
		searchParams.put("EQ_needDistribute", 1);

		Map<String, Object> searchParams2 = new HashMap<String, Object>();
		searchParams2.put("EQ_orgId", orgId);
		searchParams2.put("EQ_textbookType", textbookType);
		searchParams2.put("EQ_planId", planId);
		searchParams2.put("EQ_status", 3);
		searchParams2.put("EQ_needDistribute", 1);
		searchParams2.put("EQ_stockStatus", 1); // 库存充足

		// 查询当前学期
		/*
		 * GjtGrade grade = gjtGradeService.findCurrentGrade(orgId); if
		 * (studentType == 1) { //只统计旧生 if (grade != null) {
		 * searchParams.put("NE_gjtStudentInfo.nj", grade.getGradeId());
		 * searchParams2.put("NE_gradeId", grade.getGradeId()); } } else {
		 * //只统计新生 if (grade != null) { searchParams.put("EQ_gjtStudentInfo.nj",
		 * grade.getGradeId()); searchParams2.put("EQ_gradeId",
		 * grade.getGradeId()); } else {
		 * searchParams.put("EQ_gjtStudentInfo.nj", " ");
		 * searchParams2.put("EQ_gradeId", " "); } }
		 */

		Page<GjtTextbookOrderDetail> pageInfo = gjtTextbookOrderDetailService.findAll(searchParams, pageRequst);
		map.put("currentDistributeCount", pageInfo.getTotalElements());

		// 查询库存充足的待发教材数
		Page<Map<String, Object>> pageInfo2 = gjtTextbookOrderDetailService.findAllSummary(searchParams2, pageRequst);
		long enoughCurrentDistributeCount = 0;
		if (pageInfo2 != null && pageInfo2.getContent() != null) {
			for (Map<String, Object> obj : pageInfo2) {
				enoughCurrentDistributeCount += ((BigDecimal) obj.get("distributeNum")).intValue();
			}
		}
		map.put("enoughCurrentDistributeCount", enoughCurrentDistributeCount);

		return map;
	}

	/**
	 * 导出待发教材列表
	 * 
	 * @return
	 */
	@RequiresPermissions("/textbookDistribute/list$exportDistribute")
	@RequestMapping(value = "exportCurrentDistributeList")
	public void exportCurrentDistributeList(@RequestParam int textbookType, @RequestParam String planId,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileName = "待发教材订单.xls";
		//GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		/*PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		searchParams.put("EQ_textbookType", textbookType);
		searchParams.put("EQ_planId", planId);
		searchParams.put("EQ_status", 3);
		searchParams.put("EQ_needDistribute", 1);*/

		// 查询当前学期
		/*
		 * GjtGrade grade =
		 * gjtGradeService.findCurrentGrade(user.getGjtOrg().getId()); if
		 * (studentType == 1) { //只统计旧生 if (grade != null) {
		 * searchParams.put("NE_gradeId", grade.getGradeId()); } } else {
		 * //只统计新生 if (grade != null) { searchParams.put("EQ_gradeId",
		 * grade.getGradeId()); } else { searchParams.put("EQ_gradeId", " "); }
		 * }
		 */

		//Page<Map<String, Object>> pageInfo = gjtTextbookOrderDetailService.findAllSummary(searchParams, pageRequst);
		List<Map<String, String>> list = gjtTextbookOrderDetailService.queryCurrentDistributeList(planId, textbookType);

		//HSSFWorkbook workbook = this.getWorkbook(pageInfo.getContent());
		HSSFWorkbook workbook = this.getWorkbook(list);
		ExcelUtil.downloadExcelFile(response, workbook, fileName);

	}

	/**
	 * 导出待发教材并提交发放申请
	 * 
	 * @return
	 */
	@RequiresPermissions("/textbookDistribute/list$exportDistribute")
	@RequestMapping(value = "exportCurrentDistributeAndCommit")
	@ResponseBody
	public Feedback exportCurrentDistributeAndCommit(@RequestParam int textbookType, @RequestParam String planId,
			@RequestParam String description, HttpServletRequest request, HttpServletResponse response) {
		Feedback feedback = new Feedback(true, "");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String orgId = user.getGjtOrg().getId();

			PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_gjtTextbookPlan.orgId", orgId);
			searchParams.put("EQ_gjtTextbook.textbookType", textbookType);
			searchParams.put("EQ_planId", planId);
			searchParams.put("EQ_status", 3);
			searchParams.put("EQ_needDistribute", 1);

			Map<String, Object> searchParams2 = new HashMap<String, Object>();
			searchParams2.put("EQ_orgId", orgId);
			searchParams2.put("EQ_textbookType", textbookType);
			searchParams2.put("EQ_planId", planId);
			searchParams2.put("EQ_status", 3);
			searchParams2.put("EQ_needDistribute", 1);

			// 查询当前学期
			/*
			 * GjtGrade grade = gjtGradeService.findCurrentGrade(orgId); if
			 * (studentType == 1) { //只统计旧生 if (grade != null) {
			 * searchParams.put("NE_gjtStudentInfo.nj", grade.getGradeId());
			 * searchParams2.put("NE_gradeId", grade.getGradeId()); } } else {
			 * //只统计新生 if (grade != null) { searchParams.put("EQ_gjtStudentInfo.nj",
			 * grade.getGradeId()); searchParams2.put("EQ_gradeId",
			 * grade.getGradeId()); } else {
			 * searchParams.put("EQ_gjtStudentInfo.nj", " ");
			 * searchParams2.put("EQ_gradeId", " "); } }
			 */

			Page<Map<String, Object>> pageInfo2 = gjtTextbookOrderDetailService.findAllSummary(searchParams2, pageRequst);

			if (pageInfo2.getContent() != null && pageInfo2.getContent().size() > 0) {
				int num = 0;
				List<GjtTextbookStock> textbookStockList = new ArrayList<GjtTextbookStock>();
				List<GjtTextbookStockOpera> operaList = new ArrayList<GjtTextbookStockOpera>();

				for (Map<String, Object> obj : pageInfo2) {
					num += ((BigDecimal) obj.get("distributeNum")).intValue();

					GjtTextbook textbook = gjtTextbookService.findOne(obj.get("textbookId").toString());

					// 新增操作明细
					GjtTextbookStockOpera opera = new GjtTextbookStockOpera();
					opera.setTextbookId(textbook.getTextbookId());
					opera.setOperaType(4);
					opera.setStatus(1);
					opera.setCreatedBy(user.getId());
					opera.setApplyQuantity(-((BigDecimal) obj.get("distributeNum")).intValue());
					opera.setDescription(description);
					operaList.add(opera);

					// 更新待出库数量
					GjtTextbookStock textbookStock = textbook.getGjtTextbookStock();
					textbookStock.setPlanOutStockNum(
							textbookStock.getPlanOutStockNum() + ((BigDecimal) obj.get("distributeNum")).intValue());
					textbookStockList.add(textbookStock);
				}

				// 新增库存操作批次
				GjtTextbookStockOperaBatch operaBatch = new GjtTextbookStockOperaBatch();
				operaBatch.setBatchCode(codeGeneratorService.codeGenerator(CacheConstants.TEXTBOOK_STOCK_OPERA_BATCH_CODE));
				operaBatch.setOperaType(4);
				operaBatch.setStatus(1);
				operaBatch.setCreatedBy(user.getId());
				operaBatch.setQuantity(-num);
				operaBatch.setDescription(description);
				operaBatch.setGjtTextbookStockOperas(operaList);

				// 新增库存操作审批
				GjtTextbookStockApproval approval = new GjtTextbookStockApproval();
				approval.setOperaRole(1);
				approval.setOperaType(1);
				approval.setCreatedBy(user.getId());
				approval.setDescription(description);

				operaBatch.addGjtTextbookStockApproval(approval);

				// 查询每个学生的待发教材列表
				Page<GjtTextbookOrderDetail> pageInfo = gjtTextbookOrderDetailService.findAll(searchParams, pageRequst);
				Map<String, List<GjtTextbookOrderDetail>> studentCurrentDistributeMap = new HashMap<String, List<GjtTextbookOrderDetail>>();
				for (GjtTextbookOrderDetail detail : pageInfo) {
					if (studentCurrentDistributeMap.get(detail.getStudentId()) == null) {
						List<GjtTextbookOrderDetail> textbookDistributeSummaryList = new ArrayList<GjtTextbookOrderDetail>();
						textbookDistributeSummaryList.add(detail);

						studentCurrentDistributeMap.put(detail.getStudentId(), textbookDistributeSummaryList);
					} else {
						List<GjtTextbookOrderDetail> textbookDistributeSummaryList = studentCurrentDistributeMap
								.get(detail.getStudentId());
						textbookDistributeSummaryList.add(detail);
					}

					detail.setStatus(4);
					detail.setUpdatedBy(user.getId());
				}

				// 新增教材发放列表
				List<GjtTextbookDistribute> textbookDistributes = new ArrayList<GjtTextbookDistribute>();
				for (String studentId : studentCurrentDistributeMap.keySet()) {
					GjtTextbookDistribute textbookDistribute = new GjtTextbookDistribute();
					textbookDistribute.setStudentId(studentId);
					textbookDistribute.setStatus(0);
					textbookDistribute.setCreatedBy(user.getId());

					// 新增教材发放明细列表
					List<GjtTextbookDistributeDetail> textbookDistributeDetails = new ArrayList<GjtTextbookDistributeDetail>();
					List<GjtTextbookOrderDetail> currentDistributes = studentCurrentDistributeMap.get(studentId);
					for (GjtTextbookOrderDetail currentDistribute : currentDistributes) {
						GjtTextbookDistributeDetail textbookDistributeDetail = new GjtTextbookDistributeDetail();
						textbookDistributeDetail.setTextbookId(currentDistribute.getGjtTextbook().getTextbookId());
						textbookDistributeDetail.setQuantity(1);
						textbookDistributeDetail.setPrice(currentDistribute.getGjtTextbook().getPrice());
						textbookDistributeDetail.setStatus(0);

						textbookDistributeDetails.add(textbookDistributeDetail);
					}

					textbookDistribute.setPlanId(currentDistributes.get(0).getPlanId());
					textbookDistribute.setGjtTextbookDistributeDetails(textbookDistributeDetails);

					textbookDistributes.add(textbookDistribute);
				}
				operaBatch.setGjtTextbookDistributes(textbookDistributes);

				// 入库
				gjtTextbookStockOperaBatchService.insert(operaBatch);
				gjtTextbookStockService.update(textbookStockList);
				gjtTextbookOrderDetailService.update(pageInfo.getContent());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "请求失败！");
		}
		
		return feedback;
	}

	private HSSFWorkbook getWorkbook(List<Map<String, String>> list) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("待发教材订单");
			String[] heads = { "学期", "学号", "姓名", "学习中心", "培养层次", "专业名称", "身份证号", "手机号", "所领教材", "册数" };

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
				// 创建字体样式
				//HSSFFont font = row.getSheet().getWorkbook().createFont();
				//HSSFCellStyle style = row.getSheet().getWorkbook().createCellStyle();
				//style.setFont(font);
				//style.setWrapText(true);

				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("gradeName"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("studentCode"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("studentName"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("scName"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("trainingLevel"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("specialtyName"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("id"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("mobile"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("textbookName"));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("textbookCount"));
			}

			return wb;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*private HSSFWorkbook getWorkbook(List<Map<String, Object>> list) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("待发教材表");
			String[] heads = { "教材编号", "教材名称", "待发数量", "库存状态" };

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

			for (Map<String, Object> obj : list) {
				// 创建字体样式
				HSSFFont font = row.getSheet().getWorkbook().createFont();
				HSSFCellStyle style = row.getSheet().getWorkbook().createCellStyle();
				style.setFont(font);
				style.setWrapText(true);

				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("textbookCode").toString());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("textbookName").toString());

				int stockNum = ((BigDecimal) obj.get("stockNum")).intValue();
				int currentDistributeCount = ((BigDecimal) obj.get("distributeNum")).intValue();

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(String.valueOf(currentDistributeCount));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				if (stockNum > currentDistributeCount) {
					font.setColor(HSSFColor.GREEN.index);
					cell.setCellStyle(style);
					cell.setCellValue("库存充足");
				} else {
					font.setColor(HSSFColor.RED.index);
					cell.setCellStyle(style);
					cell.setCellValue("库存不足");
				}
			}

			return wb;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/


	/**
	 * 获取学员的预交教材费
	 * 
	 * @param studentCardId
	 * @return
	 * @throws Exception
	 */
	private float getHasPayTextbookCost(String studentCardId) throws Exception {
		float hasPayTextbookCost = 0;

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("formMap.CARDID", studentCardId);
		String s = HttpClientUtils.doHttpGet(queryOrderUrl, params, 3000, "utf-8");

		Document doc = DocumentHelper.parseText(s);
		Element rootElement = doc.getRootElement();

		@SuppressWarnings("unchecked")
		Iterator<Element> orders = rootElement.elementIterator("ORDER");
		while (orders.hasNext()) {
			Element order = orders.next();

			@SuppressWarnings("unchecked")
			Iterator<Element> payRecords = order.elementIterator("PAY_RECORD");
			while (payRecords.hasNext()) {
				Element payRecord = payRecords.next();
				String payStatus = payRecord.elementTextTrim("PAY_STATUS");
				if ("Y".equals(payStatus)) {
					String payType = payRecord.elementTextTrim("GKXL_PAYMENT_TPYE");
					if ("G".equals(payType) || "H".equals(payType) || "I".equals(payType)) {
						String amount = payRecord.elementTextTrim("REC_AMT");
						try {
							hasPayTextbookCost += Float.parseFloat(amount);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

		}

		return hasPayTextbookCost;
	}

	/**
	 * 查询物流
	 * 
	 * @param logisticsComp
	 * @param waybillCode
	 * @return
	 */
	@RequestMapping(value = "queryLogistics")
	@ResponseBody
	public String queryLogistics(@RequestParam String logisticsComp, @RequestParam String waybillCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", logisticsComp);
		params.put("postid", waybillCode);
		String s = HttpClientUtils.doHttpPost("https://www.kuaidi100.com/query", params, 3000, "utf-8");
		return s;
	}

	/**
	 * 导出待签收教材订单
	 * 
	 * @return
	 */
	@RequiresPermissions("/textbookDistribute/list$exportNotSign")
	@RequestMapping(value = "exportNotSign")
	public void exportNotSign(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileName = "待签收教材表.xls";
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(2);
		List<GjtTextbookDistribute> textbookDistributes = gjtTextbookDistributeService
				.findByIsDeletedAndStatusIn(user.getGjtOrg().getId(), "N", statuses);
		HSSFWorkbook workbook = this.getNotSignWorkbook(textbookDistributes);
		ExcelUtil.downloadExcelFile(response, workbook, fileName);

	}

	private HSSFWorkbook getNotSignWorkbook(List<GjtTextbookDistribute> list) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("待签收教材表");
			String[] heads = { "订单号", "运单号", "学生姓名", "运费", "发放明细", "创建时间", "配送时间", "状态" };

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

			for (GjtTextbookDistribute obj : list) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.getOrderCode());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.getWaybillCode());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.getGjtStudentInfo().getXm());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.getFreight().doubleValue());

				String details = "";
				if (obj.getGjtTextbookDistributeDetails() != null) {
					for (GjtTextbookDistributeDetail detail : obj.getGjtTextbookDistributeDetails()) {
						details += detail.getGjtTextbook().getTextbookName() + ",";
					}
				}
				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(details);

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(df.format(obj.getCreatedDt()));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(df.format(obj.getDistributionDt()));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue("配送中");
			}

			return wb;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "updateDistributeLogistics")
	@ResponseBody
	public Feedback updateDistributeLogistics(HttpServletRequest request, String distributeId, String waybillCode, String logisticsComp) {
		Feedback feedback = new Feedback(true, null);
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtTextbookDistribute textbookDistribute = gjtTextbookDistributeService.queryById(distributeId);
			textbookDistribute.setWaybillCode(waybillCode);
			textbookDistribute.setLogisticsComp(logisticsComp);
			// 已输入运单号
			if(StringUtils.isBlank(textbookDistribute.getWaybillCode()) && StringUtils.isNotBlank(waybillCode)) {
				textbookDistribute.setStatus(2);
				textbookDistribute.setDistributionDt(new Date());
				// 更新明细状态
				List<GjtTextbookDistributeDetail> distributeDetails = textbookDistribute
						.getGjtTextbookDistributeDetails();
				for (GjtTextbookDistributeDetail distributeDetail : distributeDetails) {
					distributeDetail.setStatus(2);
				}
			}
			textbookDistribute.setUpdatedDt(new Date());
			textbookDistribute.setUpdatedBy(user.getId());
			gjtTextbookDistributeService.update(textbookDistribute);
		} catch (Exception e) {
			feedback.setMessage("系统异常");
			feedback.setSuccessful(false);
		}
		return feedback;
	}

}
