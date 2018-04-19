package com.gzedu.xlims.web.controller.textbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStock;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockApproval;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockOpera;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockOperaBatch;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.textbook.GjtTextbookService;
import com.gzedu.xlims.service.textbook.GjtTextbookStockOperaBatchService;
import com.gzedu.xlims.service.textbook.GjtTextbookStockOperaService;
import com.gzedu.xlims.service.textbook.GjtTextbookStockService;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/textbookStock")
public class GjtTextbookStockController {

	private final static Logger log = LoggerFactory.getLogger(GjtTextbookStockController.class);

	@Autowired
	private GjtTextbookService gjtTextbookService;

	@Autowired
	private GjtTextbookStockService gjtTextbookStockService;

	@Autowired
	private GjtTextbookStockOperaBatchService gjtTextbookStockOperaBatchService;

	@Autowired
	private GjtTextbookStockOperaService gjtTextbookStockOperaService;

	@Autowired
	private CodeGeneratorService codeGeneratorService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());

		Page<GjtTextbook> pageInfo = gjtTextbookService.findAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);

		// 查询“库存充足”和“库存不足”的教材总数
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.remove("EQ_stockStatus");
		model.addAttribute("all", gjtTextbookService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_stockStatus", 1);
		model.addAttribute("enoughStock", gjtTextbookService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_stockStatus", 2);
		model.addAttribute("notEnoughStock", gjtTextbookService.findAll(map, pageRequst).getTotalElements());

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnImport", subject.isPermitted("/textbookStock/list$importStock"));
		model.addAttribute("isBtnExport", subject.isPermitted("/textbookStock/list$export"));
		model.addAttribute("isBtnView", subject.isPermitted("/textbookStock/list$view"));

		return "textbook/textbookStock_list";

	}

	@RequiresPermissions("/textbookStock/list$view")
	@RequestMapping(value = "view/{textbookId}")
	public String view(@PathVariable("textbookId") String textbookId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtTextbook textbook = gjtTextbookService.findOne(textbookId);
		model.addAttribute("textbook", textbook);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, "createdDt", "DESC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_gjtTextbook.textbookId", textbookId);

		Page<GjtTextbookStockOpera> pageInfo = gjtTextbookStockOperaService.findAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);

		return "textbook/textbookStock_detail";
	}

	/**
	 * 返回导入页面
	 * 
	 * @return
	 */
	@RequiresPermissions("/textbookStock/list$importStock")
	@RequestMapping(value = "importForm", method = RequestMethod.GET)
	public String importForm() {
		return "textbook/textbookStock_import";
	}

	@RequiresPermissions("/textbookStock/list$importStock")
	@RequestMapping(value = "import")
	@ResponseBody
	public ImportFeedback importTextbook(@RequestParam("file") MultipartFile file,
			@RequestParam("operaType") int operaType, HttpServletRequest request, HttpServletResponse response) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "书号", "教材名称", "教材类型", "版次", "作者", "数量", "导入结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}
			Map<String, Integer> planOutStockNumMap = new HashMap<String, Integer>(); // 用于计算教材对应的库存操作数量

			if (dataList != null && dataList.size() > 0) {
				int totalNum = 0;
				List<GjtTextbookStock> textbookStockList = new ArrayList<GjtTextbookStock>();
				List<GjtTextbookStockOpera> operaList = new ArrayList<GjtTextbookStockOpera>();

				for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据

					if (datas.length < heads.length - 1) {
						result[heads.length - 1] = "数据不全";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[0])) { // 教材编号
						result[heads.length - 1] = "教材编号不能为空";
						failedList.add(result);
						continue;
					}

					try {
						if (Integer.parseInt(datas[5]) <= 0) { // 数量
							result[heads.length - 1] = "数量必须大于0";
							failedList.add(result);
							continue;
						}
					} catch (Exception e) {
						result[heads.length - 1] = "数量格式不正确";
						failedList.add(result);
						continue;
					}

					GjtTextbook entity = gjtTextbookService.findByCode(datas[0], user.getGjtOrg().getId());
					if (entity == null) {
						result[heads.length - 1] = "教材编号不存在";
						failedList.add(result);
						continue;
					}

					int num = Integer.parseInt(datas[5]);
					totalNum += num;
					// 新增库存操作明细
					GjtTextbookStockOpera opera = new GjtTextbookStockOpera();
					opera.setTextbookId(entity.getTextbookId());
					opera.setOperaType(operaType);
					opera.setStatus(1);
					opera.setCreatedBy(user.getId());
					if (operaType == 1) { // 教材入库操作
						opera.setApplyQuantity(num);
						opera.setDescription("教材入库");
					} else if (operaType == 2) { // 库存损耗操作
						opera.setApplyQuantity(-num);
						opera.setDescription("库存损耗");
					} else if (operaType == 3) { // 清理库存操作
						opera.setApplyQuantity(-num);
						opera.setDescription("清理库存");
					}
					operaList.add(opera);

					// 计算教材对应的库存操作数量
					GjtTextbookStock textbookStock = entity.getGjtTextbookStock();
					if (planOutStockNumMap.get(textbookStock.getStockId()) != null) {
						planOutStockNumMap.put(textbookStock.getStockId(),
								planOutStockNumMap.get(textbookStock.getStockId()) + num);
					} else {
						planOutStockNumMap.put(textbookStock.getStockId(), num);
					}
					textbookStockList.add(textbookStock);

					result[heads.length - 1] = "新增成功";
					successList.add(result);
				}

				// 新增库存操作批次
				GjtTextbookStockOperaBatch operaBatch = new GjtTextbookStockOperaBatch();
				operaBatch.setBatchCode(
						codeGeneratorService.codeGenerator(CacheConstants.TEXTBOOK_STOCK_OPERA_BATCH_CODE));
				operaBatch.setOperaType(operaType);
				operaBatch.setStatus(1);
				operaBatch.setCreatedBy(user.getId());
				if (operaType == 1) { // 教材入库操作
					operaBatch.setQuantity(totalNum);
					operaBatch.setDescription("教材入库");
				} else if (operaType == 2) { // 库存损耗操作
					operaBatch.setQuantity(-totalNum);
					operaBatch.setDescription("库存损耗");
				} else if (operaType == 3) { // 清理库存操作
					operaBatch.setQuantity(-totalNum);
					operaBatch.setDescription("清理库存");
				}

				operaBatch.setGjtTextbookStockOperas(operaList);

				// 新增库存操作审批
				GjtTextbookStockApproval approval = new GjtTextbookStockApproval();
				approval.setOperaRole(1);
				approval.setOperaType(1);
				approval.setCreatedBy(user.getId());
				if (operaType == 1) { // 教材入库操作
					approval.setDescription("教材入库");
				} else if (operaType == 2) { // 库存损耗操作
					approval.setDescription("库存损耗");
				} else if (operaType == 3) { // 清理库存操作
					approval.setDescription("清理库存");
				}

				operaBatch.addGjtTextbookStockApproval(approval);

				gjtTextbookStockOperaBatchService.insert(operaBatch);

				if (operaType != 1) { // 更新待出库数量
					for (GjtTextbookStock textbookStock : textbookStockList) {
						textbookStock.setPlanOutStockNum(textbookStock.getPlanOutStockNum()
								+ planOutStockNumMap.get(textbookStock.getStockId()));
					}
					gjtTextbookStockService.update(textbookStockList);
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "textbookStock" + operaType + "_success_" + currentTimeMillis + ".xls";
			String failedFileName = "textbookStock" + operaType + "_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "库存操作成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "库存操作失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "textbookStock" + File.separator;
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

	@RequiresPermissions("/textbookStock/list$export")
	@RequestMapping(value = "export")
	public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		
		Page<GjtTextbook> pageInfo = gjtTextbookService.findAll(searchParams, pageRequst);
		
		HSSFWorkbook workbook = this.getWorkbook(pageInfo.getContent());
		ExcelUtil.downloadExcelFile(response, workbook, "教材库存表.xls");
	}

	private HSSFWorkbook getWorkbook(List<GjtTextbook> list) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("教材表");
		String[] heads = { "书号（ISBN）", "教材名称", "教材类型", "总入库", "总出库", "剩余库存", "待出库", "库存状态" };

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

		for (GjtTextbook textbook : list) {
			row = sheet.createRow(rowIdx++);
			colIdx = 0;

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(textbook.getTextbookCode());

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(textbook.getTextbookName());

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			if (textbook.getTextbookType() == 1) {
				cell.setCellValue("主教材");
			} else {
				cell.setCellValue("复习资料");
			}

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(textbook.getGjtTextbookStock().getInStockNum());

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(textbook.getGjtTextbookStock().getOutStockNum());

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(textbook.getGjtTextbookStock().getStockNum());

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(textbook.getGjtTextbookStock().getPlanOutStockNum());

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			if (textbook.getGjtTextbookStock().getStockNum() != 0 &&
					textbook.getGjtTextbookStock().getStockNum() >= textbook.getGjtTextbookStock().getPlanOutStockNum()) {
				cell.setCellValue("库存充足");
			} else if (textbook.getGjtTextbookStock().getStockNum() < textbook.getGjtTextbookStock().getPlanOutStockNum()) {
				cell.setCellValue("库存不足");
			} else {
				cell.setCellValue("--");
			}
		}

		return wb;
	}

}
