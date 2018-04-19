package com.ouchgzee.headTeacher.web.controller.textbook;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.HttpClientUtils;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtTeachPlan;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookDistribute;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookDistributeDetail;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookOrderDetail;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.edumanage.BzrGjtTeachPlanService;
import com.ouchgzee.headTeacher.service.student.BzrGjtClassService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.service.textbook.BzrGjtTextbookDistributeService;
import com.ouchgzee.headTeacher.service.textbook.BzrGjtTextbookOrderDetailService;
import com.ouchgzee.headTeacher.service.textbook.BzrGjtTextbookStockService;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

@Controller
@RequestMapping("/home/class/textbookDistribute")
public class GjtTextbookDistributeController extends BaseController {

	private static final Log log = LogFactory.getLog(GjtTextbookDistributeController.class);

	@Autowired
	BzrGjtTextbookDistributeService gjtTextbookDistributeService;

	@Autowired
	BzrCommonMapService commonMapService;

	@Autowired
	BzrGjtStudentService gjtStudentInfoService;

	@Autowired
	BzrGjtTeachPlanService gjtTeachPlanService;

	@Autowired
	BzrGjtClassService gjtClassService;

	@Autowired
	BzrGjtTextbookOrderDetailService gjtTextbookOrderDetailService;

	@Autowired
	BzrGjtTextbookStockService gjtTextbookStockService;

	@Value("#{configProperties['queryOrderUrl']}")
	private String queryOrderUrl;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, HttpSession session) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_gjtTextbookPlan.orgId", employeeInfo.getXxId());
		//searchParams.put("EQ_gjtTextbook.textbookType", 1);// 发放教材，教材管理
		Object status = searchParams.get("search_EQ_status");
		if (status != null && "34".equals(status.toString())) {
			searchParams.put("IN_status", new ArrayList<Integer>() {
				{
					add(3);
					add(4);
				}
			});
			searchParams.remove("search_EQ_status");
		} else {
			List<Integer> statuses = new ArrayList<Integer>();
			statuses.add(3);
			statuses.add(4);
			statuses.add(5);
			statuses.add(6);
			statuses.add(7);
			searchParams.put("IN_status", statuses);
		}
		searchParams.put("EQ_needDistribute", 1);

		String classId = super.getCurrentClassId(session);
		List<String> students = gjtClassService.queryTeacherClassStudent(classId);
		if (students == null) {
			students = new ArrayList<String>();
			students.add("-1");
		} else if (students.size() == 0) {
			students.add("-1");
		}
		searchParams.put("IN_studentId", students);

		Page<BzrGjtTextbookOrderDetail> pageInfo = gjtTextbookOrderDetailService.findAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);

		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(employeeInfo.getXxId()));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(employeeInfo.getXxId()));
		model.addAttribute("trainingLevelMap", commonMapService.getPyccMap());
		model.addAttribute("textbookPlanMap", commonMapService.getTextbookPlanMap(employeeInfo.getXxId()));
		return "new/class/textbook/textbookDistribute_list";
	}

	@RequestMapping(value = "view/{studentId}")
	public String view(@PathVariable("studentId") String studentId, Model model) {
		BzrGjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);

		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(1);
		statuses.add(2);
		statuses.add(3);
		List<BzrGjtTextbookDistribute> textbookDistributes = gjtTextbookDistributeService
				.findByStudentIdAndIsDeletedAndStatusIn(studentInfo.getStudentId(), "N", statuses);

		// 构建学生发放教材的id与发放状态对应map
		Map<String, Integer> statusMap = new HashMap<String, Integer>();
		if (textbookDistributes != null && textbookDistributes.size() > 0) {
			for (BzrGjtTextbookDistribute textbookDistribute : textbookDistributes) {
				List<BzrGjtTextbookDistributeDetail> textbookDistributeDetails = textbookDistribute
						.getGjtTextbookDistributeDetails();
				for (BzrGjtTextbookDistributeDetail detail : textbookDistributeDetails) {
					statusMap.put(detail.getTextbookId(), detail.getStatus());
				}
			}
		}

		// 查询学员的教学计划列表，并按照学期归并
		List<BzrGjtTeachPlan> gjtTeachPlans = gjtTeachPlanService.queryGjtTeachPlan(studentInfo.getGjtGrade().getGradeId(),
				studentInfo.getGjtSpecialty().getSpecialtyId());
		Map<Integer, List<BzrGjtTeachPlan>> gradeSpecialtyPlanMap = new LinkedHashMap<Integer, List<BzrGjtTeachPlan>>();
		if (gjtTeachPlans != null && gjtTeachPlans.size() > 0) {
			for (BzrGjtTeachPlan gjtTeachPlan : gjtTeachPlans) {
				int termTypeCode = gjtTeachPlan.getKkxq();
				if (gradeSpecialtyPlanMap.get(termTypeCode) == null) {
					List<BzrGjtTeachPlan> list = new ArrayList<BzrGjtTeachPlan>();
					list.add(gjtTeachPlan);
					gradeSpecialtyPlanMap.put(termTypeCode, list);
				} else {
					List<BzrGjtTeachPlan> list = gradeSpecialtyPlanMap.get(termTypeCode);
					list.add(gjtTeachPlan);
				}
			}
		}

		// 查询预交教材费用
		float hasPayTextbookCost = 0;
		try {
			hasPayTextbookCost = this.getHasPayTextbookCost(studentInfo.getSfzh());
		} catch (Exception e) {
			log.error("查询学号[" + studentInfo.getXh() + "]，身份证号码[" + studentInfo.getSfzh() + "]的学生预交教材费用出错！", e);
		}

		model.addAttribute("studentInfo", studentInfo);
		model.addAttribute("textbookDistributes", textbookDistributes);
		model.addAttribute("statusMap", statusMap);
		model.addAttribute("gradeSpecialtyPlanMap", gradeSpecialtyPlanMap);
		model.addAttribute("hasPayTextbookCost", hasPayTextbookCost);
		model.addAttribute("trainingLevelMap", commonMapService.getPyccMap());

		return "new/class/textbook/textbookDistribute_detail";
	}

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
	@RequestMapping(value = "exportNotSign")
	public void exportNotSign(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		String fileName = "待签收教材表.xls";
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);

		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(2);
		List<BzrGjtTextbookDistribute> textbookDistributes = gjtTextbookDistributeService
				.findByIsDeletedAndStatusIn(employeeInfo.getXxId(), super.getCurrentClassId(session), "N", statuses);
		HSSFWorkbook workbook = this.getNotSignWorkbook(textbookDistributes);
		super.downloadExcelFile(response, workbook, fileName);

	}

	private HSSFWorkbook getNotSignWorkbook(List<BzrGjtTextbookDistribute> list) {
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

			for (BzrGjtTextbookDistribute obj : list) {
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
				cell.setCellValue(obj.getFreight());

				String details = "";
				if (obj.getGjtTextbookDistributeDetails() != null) {
					for (BzrGjtTextbookDistributeDetail detail : obj.getGjtTextbookDistributeDetails()) {
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

}
