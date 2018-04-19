package com.gzedu.xlims.web.controller.textbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import com.gzedu.xlims.common.constants.PermittedConstants;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.pojo.textbook.GjtTextbookOrder;
import com.gzedu.xlims.pojo.textbook.GjtTextbookOrderDetail;
import com.gzedu.xlims.pojo.textbook.GjtTextbookPlan;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.textbook.GjtTextbookOrderDetailService;
import com.gzedu.xlims.service.textbook.GjtTextbookOrderService;
import com.gzedu.xlims.service.textbook.GjtTextbookPlanService;
import com.gzedu.xlims.service.textbook.GjtTextbookService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/textbookOrder")
public class GjtTextbookOrderController {

	private static final Logger log = LoggerFactory.getLogger(GjtTextbookOrderController.class);

	@Autowired
	private GjtTextbookOrderService gjtTextbookOrderService;

	@Autowired
	private GjtTextbookOrderDetailService gjtTextbookOrderDetailService;

	@Autowired
	private GjtTextbookService gjtTextbookService;

	@Autowired
	private GjtTextbookPlanService gjtTextbookPlanService;

	@Autowired
	private GjtCourseService gjtCourseService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	CommonMapService commonMapService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());

		Page<Map<String, Object>> pageInfo = gjtTextbookOrderService.findAll(searchParams, pageRequst);
		for (Map<String, Object> order : pageInfo) {
			if ("0".equals(order.get("status").toString()) || "2".equals(order.get("status").toString())) {
				// 待提交状态需要实时统计“需订购总数量”和“订购总价”
				Map<String, Object> map = gjtTextbookOrderService
						.findNeedOrderNumAndPrice(order.get("orderId").toString());
				order.put("orderNum2", map.get("num"));
				order.put("orderPrice2", map.get("price"));
			}
		}
		model.addAttribute("pageInfo", pageInfo);

		// 查询“待提交”、“待审核”、“审核通过”和“审核不通过”
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.put("EQ_status", 0);
		model.addAttribute("preSubmit", gjtTextbookOrderService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_status", 1);
		model.addAttribute("preAudit", gjtTextbookOrderService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_status", 2);
		model.addAttribute("auditNotPass", gjtTextbookOrderService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_status", 3);
		model.addAttribute("auditPass", gjtTextbookOrderService.findAll(map, pageRequst).getTotalElements());

		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);

		Map<String, String> textbookPlanMap = commonMapService.getTextbookPlanMap(user.getGjtOrg().getId());
		model.addAttribute("textbookPlanMap", textbookPlanMap);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("canSubmitApproval", subject.isPermitted("/textbookOrder/list$sumit-approval"));
		model.addAttribute("canApproval", subject.isPermitted("/textbookOrder/list$approval"));
		model.addAttribute("isBtnView", subject.isPermitted("/textbookOrder/list$view"));

		return "textbook/textbookOrder_list";
	}

	@RequiresPermissions("/textbookOrder/list$view")
	@RequestMapping(value = "detailList", method = RequestMethod.GET)
	public String detailList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			@RequestParam(value = "orderId") String orderId, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest orderPage = Servlets.buildPageRequest(1, pageSize);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		searchParams.put("EQ_orderId", orderId);

		Page<Map<String, Object>> page = gjtTextbookOrderService.findAll(searchParams, orderPage);
		Map<String, Object> order = page.getContent().get(0);
		if ("0".equals(order.get("status").toString()) || "2".equals(order.get("status").toString())) {
			// 待提交状态需要实时统计“需订购总数量”和“订购总价”
			Map<String, Object> map = gjtTextbookOrderService.findNeedOrderNumAndPrice(order.get("orderId").toString());
			order.put("orderNum2", map.get("num"));
			order.put("orderPrice2", map.get("price"));
		}
		model.addAttribute("order", order);

		Page<Map<String, Object>> pageInfo = gjtTextbookOrderDetailService.findAllSummary(searchParams, pageRequst);
		for (Map<String, Object> orderDetail : pageInfo) {
			if (orderDetail.get("courseIds") != null) {
				String courseIds = (String)orderDetail.get("courseIds");
				List<GjtCourse> courses = gjtCourseService.findAll(Arrays.asList(courseIds.split(",")));
				orderDetail.put("courseList", courses);
			}
		}

		model.addAttribute("pageInfo", pageInfo);

		// 查询“需订购”和“库存充足”
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.put("EQ_stockStatus", 0);
		model.addAttribute("notEnough",
				gjtTextbookOrderDetailService.findAllSummary(map, pageRequst).getTotalElements());
		map.put("EQ_stockStatus", 1);
		model.addAttribute("enough", gjtTextbookOrderDetailService.findAllSummary(map, pageRequst).getTotalElements());

		Map<String, String> courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());
		model.addAttribute("courseMap", courseMap);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("canSubmitApproval",
				subject.isPermitted("/textbookOrder/list$" + PermittedConstants.SUMIT_APPROVAL.getCode()));
		model.addAttribute("canApproval",
				subject.isPermitted("/textbookOrder/list$" + PermittedConstants.APPROVAL.getCode()));
		model.addAttribute("isBtnImport", subject.isPermitted("/textbookOrder/list$importTextbookOrder"));
		model.addAttribute("isBtnExport", subject.isPermitted("/textbookOrder/list$exportTextbookOrder"));

		return "textbook/textbookOrderDetail_list";
	}

	@RequestMapping(value = "viewDetail", method = RequestMethod.GET)
	public String viewDetail(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			@RequestParam(value = "orderId") String orderId, @RequestParam(value = "textbookId") String textbookId,
			Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		GjtTextbookOrder order = gjtTextbookOrderService.findOne(orderId);
		model.addAttribute("order", order);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("EQ_orgId", user.getGjtOrg().getId());
		map.put("EQ_orderId", orderId);
		map.put("EQ_textbookId", textbookId);
		PageRequest page = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Page<Map<String, Object>> detailLSummary = gjtTextbookOrderDetailService.findAllSummary(map, page);
		model.addAttribute("detailLSummary", detailLSummary);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_gjtTextbookOrder.orderId", orderId);
		searchParams.put("EQ_gjtTextbook.textbookId", textbookId);
		Page<GjtTextbookOrderDetail> pageInfo = gjtTextbookOrderDetailService.findAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);

		// 查询“需发放”和“不发放”
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("EQ_gjtTextbookOrder.orderId", orderId);
		map2.put("EQ_gjtTextbook.textbookId", textbookId);
		map2.put("EQ_needDistribute", 0);
		model.addAttribute("notNeed", gjtTextbookOrderDetailService.findAll(map2, pageRequst).getTotalElements());
		map2.put("EQ_needDistribute", 1);
		model.addAttribute("need", gjtTextbookOrderDetailService.findAll(map2, pageRequst).getTotalElements());

		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		model.addAttribute("pyccMap", pyccMap);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isDistribute",
				subject.isPermitted("/textbookOrder/list$isDistribute"));

		return "textbook/textbookOrderDetail_view";
	}

	@RequestMapping(value = "setNeedDistribute")
	@ResponseBody
	public Feedback setNeedDistribute(@RequestParam String orderDetailIds, @RequestParam int needDistribute,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (StringUtils.isNotBlank(orderDetailIds)) {
			try {
				List<GjtTextbookOrderDetail> details = gjtTextbookOrderDetailService
						.findAll(Arrays.asList(orderDetailIds.split(",")));
				if (details != null && details.size() > 0) {
					for (GjtTextbookOrderDetail detail : details) {
						detail.setNeedDistribute(needDistribute);
						detail.setUpdatedBy(user.getId());
					}
				}
				gjtTextbookOrderDetailService.update(details);
				return new Feedback(true, "设置成功");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return new Feedback(false, "设置失败");
			}
		}

		return new Feedback(false, "设置失败");
	}

	@RequiresPermissions("/textbookOrder/list$sumit-approval")
	@RequestMapping(value = "submitApproval")
	@ResponseBody
	public Feedback submitApproval(@RequestParam String orderId, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			GjtTextbookOrder order = gjtTextbookOrderService.findOne(orderId);
			if (order.getStatus() != 0 && order.getStatus() != 2) {
				return new Feedback(false, "提交失败，状态不对");
			}

			List<GjtTextbookOrderDetail> orderDetails = order.getGjtTextbookOrderDetails();
			if (orderDetails == null || orderDetails.size() == 0) {
				return new Feedback(false, "提交失败，没有可提交的订购");
			}

			order.setStatus(1);
			order.setUpdatedBy(user.getId());
			order.setUpdatedDt(new Date());

			// 记录此刻的“教材发放总量”、“需订购总数量”和“订购总价”
			Set<String> textbooks = new HashSet<String>();
			for (GjtTextbookOrderDetail orderDetail : orderDetails) {
				orderDetail.setStatus(1);
				orderDetail.setUpdatedBy(user.getId());
				orderDetail.setUpdatedDt(new Date());

				if (!textbooks.contains(orderDetail.getGjtTextbook().getTextbookId())) {
					textbooks.add(orderDetail.getGjtTextbook().getTextbookId());
				}
			}
			// order.setDistributeNum(textbooks.size());
			order.setDistributeNum(orderDetails.size());

			// 查询“需订购总数量”和“订购总价”
			Map<String, Object> map = gjtTextbookOrderService.findNeedOrderNumAndPrice(orderId);
			order.setOrderNum(Integer.parseInt(map.get("num").toString()));
			order.setOrderPrice(Float.parseFloat(map.get("price").toString()));

			gjtTextbookOrderService.update(order);
			return new Feedback(true, "提交成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Feedback(false, "提交失败");
		}
	}

	@RequiresPermissions("/textbookOrder/list$approval")
	@RequestMapping(value = "approvalForm")
	public String approvalForm(@RequestParam String orderId) {
		return "textbook/textbookOrder_approval";
	}

	@RequiresPermissions("/textbookOrder/list$approval")
	@RequestMapping(value = "approval")
	@ResponseBody
	public Feedback approval(@RequestParam String orderId, @RequestParam int status, String reason,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			GjtTextbookOrder order = gjtTextbookOrderService.findOne(orderId);
			if (order.getStatus() != 1) {
				return new Feedback(false, "审核失败，状态不对");
			}

			List<GjtTextbookOrderDetail> orderDetails = order.getGjtTextbookOrderDetails();
			if (orderDetails == null || orderDetails.size() == 0) {
				return new Feedback(false, "审核失败，没有可审核的订购");
			}

			order.setStatus(status);
			order.setReason(reason);
			order.setUpdatedBy(user.getId());
			order.setUpdatedDt(new Date());

			for (GjtTextbookOrderDetail orderDetail : orderDetails) {
				orderDetail.setStatus(status);
				orderDetail.setUpdatedBy(user.getId());
				orderDetail.setUpdatedDt(new Date());
			}

			gjtTextbookOrderService.update(order);
			return new Feedback(true, "审核成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Feedback(false, "审核失败");
		}
	}

	@RequiresPermissions("/textbookOrder/list$importTextbookOrder")
	@RequestMapping(value = "import")
	@ResponseBody
	public ImportFeedback importTextbook(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "学期", "教材计划编号", "教材计划名称", "书号", "教材名称", "教材类型", "发放课程编号", "发放课程名称", "姓名", "学号", "年级",
					"层次", "专业", "学习中心", "状态", "导入结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<GjtTextbookOrderDetail> detailList = new ArrayList<GjtTextbookOrderDetail>();
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

					if ("".equals(datas[1])) { // 教材计划编号
						result[heads.length - 1] = "教材计划编号不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[3])) { // 书号
						result[heads.length - 1] = "书号不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[6])) { // 发放课程编号
						result[heads.length - 1] = "发放课程编号不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[9])) { // 学号
						result[heads.length - 1] = "学号不能为空";
						failedList.add(result);
						continue;
					}

					if (!"需发放".equals(datas[14]) && !"不发放".equals(datas[14])) { // 状态
						result[heads.length - 1] = "状态有误";
						failedList.add(result);
						continue;
					}

					GjtTextbookPlan textbookPlan = gjtTextbookPlanService.findByPlanCodeAndOrgId(datas[1],
							user.getGjtOrg().getId());
					if (textbookPlan == null) {
						result[heads.length - 1] = "教材计划编号[" + datas[1] + "]不存在";
						failedList.add(result);
						continue;
					}

					GjtTextbook textbook = gjtTextbookService.findByCode(datas[3], user.getGjtOrg().getId());
					if (textbook == null) {
						result[heads.length - 1] = "书号[" + datas[3] + "]不存在";
						failedList.add(result);
						continue;
					}

					List<GjtCourse> courseList = gjtCourseService.findByKchAndXxId(datas[6], user.getGjtOrg().getId());
					if (courseList == null || courseList.size() == 0) {
						result[heads.length - 1] = "发放课程编号[" + datas[6] + "]不存在";
						failedList.add(result);
						continue;
					} else if (courseList.size() > 1) {
						result[heads.length - 1] = "发放课程编号[" + datas[6] + "]存在多个";
						failedList.add(result);
						continue;
					}
					GjtCourse course = courseList.get(0);

					GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(datas[9]);
					if (studentInfo == null) {
						result[heads.length - 1] = "学号[" + datas[9] + "]不存在";
						failedList.add(result);
						continue;
					}

					GjtTextbookOrderDetail detail = gjtTextbookOrderDetailService
							.findByStudentIdAndTextbookIdAndCourseIdAndPlanId(studentInfo.getStudentId(),
									textbook.getTextbookId(), course.getCourseId(), textbookPlan.getPlanId());
					if (detail == null) {
						result[heads.length - 1] = "记录不存在";
						failedList.add(result);
						continue;
					}

					if ("需发放".equals(datas[14])) {
						detail.setNeedDistribute(1);
					} else {
						detail.setNeedDistribute(0);
					}
					detail.setUpdatedBy(user.getId());
					detailList.add(detail);

					result[heads.length - 1] = "新增成功";
					successList.add(result);
				}
			}

			if (detailList.size() > 0) {
				gjtTextbookOrderDetailService.update(detailList);
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "textbookOrderDetail_success_" + currentTimeMillis + ".xls";
			String failedFileName = "textbookOrderDetail_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "批量导入修改发放名单成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "批量导入修改发放名单失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "textbookOrderDetail" + File.separator;
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

	@RequiresPermissions("/textbookOrder/list$exportTextbookOrder")
	@RequestMapping(value = "exportDetail")
	public void exportDetail(@RequestParam String orderId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String fileName = "发放名单导入表.xls";
		GjtTextbookOrder textbookOrder = gjtTextbookOrderService.findOne(orderId);
		HSSFWorkbook workbook = this.getWorkbook(textbookOrder.getGjtTextbookOrderDetails());
		ExcelUtil.downloadExcelFile(response, workbook, fileName);
	}

	private HSSFWorkbook getWorkbook(List<GjtTextbookOrderDetail> list) {
		InputStream fis = null;
		try {
			fis = GjtTextbookOrderController.class.getResourceAsStream(WebConstants.EXCEL_MODEL_URL + "发放名单导入表.xls");
			HSSFWorkbook wb = new HSSFWorkbook(fis);
			HSSFSheet sheet = wb.getSheetAt(0);

			int rowIdx = 3;
			int colIdx = 0;
			HSSFCell cell;

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行

			// 为了能够使用换行，需要设置单元格的样式 wrap=true
			HSSFCellStyle s = wb.createCellStyle();
			s.setWrapText(true);
			
			// 创建字体样式
			HSSFFont font = wb.createFont();
			HSSFCellStyle style = wb.createCellStyle();
			style.setFont(font);
			style.setWrapText(true);

			Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
			HSSFRow row;
			for (GjtTextbookOrderDetail obj : list) {
				

				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.getGjtGrade().getGradeName());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.getGjtTextbookPlan().getPlanCode());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.getGjtTextbookPlan().getPlanName());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.getGjtTextbook().getTextbookCode());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.getGjtTextbook().getTextbookName());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (obj.getGjtTextbook().getTextbookType() == 1) {
					cell.setCellValue("主教材");
				} else {
					cell.setCellValue("复习资料");
				}

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.getGjtCourse().getKch());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.getGjtCourse().getKcmc());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.getGjtStudentInfo().getXm());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.getGjtStudentInfo().getXh());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.getGjtStudentInfo().getGjtGrade().getGradeName());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(pyccMap.get((obj.getGjtStudentInfo().getPycc())));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.getGjtStudentInfo().getGjtSpecialty().getZymc());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (obj.getGjtStudentInfo().getGjtStudyCenter() != null) {
					cell.setCellValue(obj.getGjtStudentInfo().getGjtStudyCenter().getScName());
				} else {
					cell.setCellValue("--");
				}

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (obj.getNeedDistribute() == 1) {
					font.setColor(HSSFColor.GREEN.index);
					cell.setCellStyle(style);
					cell.setCellValue("需发放");
				} else {
					font.setColor(HSSFColor.RED.index);
					cell.setCellStyle(style);
					cell.setCellValue("不发放");
				}

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

}
