package com.gzedu.xlims.web.controller.textbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.textbook.GjtStudentTextbookOrder;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.textbook.GjtStudentTextbookOrderService;
import com.gzedu.xlims.service.textbook.GjtTextbookService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 
 * 功能说明：教材管理
 * 
 * @author 李志坚
 * @Date 2017年5月22日
 * @version 1.1
 */
@Controller
@RequestMapping("/textbook")
public class GjtTextbookController {
	private final static Logger log = LoggerFactory.getLogger(GjtTextbookController.class);

	@Autowired
	private GjtTextbookService gjtTextbookService;

	@Autowired
	private GjtCourseService gjtCourseService;

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

		// 查询“主教材”和“复习材料”的教材总数
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.remove("EQ_status");
		map.put("EQ_textbookType", 1);
		model.addAttribute("primaryTextbook", gjtTextbookService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_textbookType", 2);
		model.addAttribute("reviewTextbook", gjtTextbookService.findAll(map, pageRequst).getTotalElements());

		// 查询“已启用”和“已停用”的教材总数
		map.remove("EQ_textbookType");
		map.put("EQ_status", 1);
		model.addAttribute("enabled", gjtTextbookService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_status", 0);
		model.addAttribute("disabled", gjtTextbookService.findAll(map, pageRequst).getTotalElements());

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnCreate", subject.isPermitted("/textbook/list$createTextbook"));
		model.addAttribute("isBtnUpdate", subject.isPermitted("/textbook/list$update"));
		model.addAttribute("isBtnView", subject.isPermitted("/textbook/list$view"));
		model.addAttribute("isBtnExport", subject.isPermitted("/textbook/list$exportTextbook"));
		model.addAttribute("isBtnImport", subject.isPermitted("/textbook/list$importTextbook"));
		model.addAttribute("isBtnViewStock", subject.isPermitted("/textbook/list$viewStock"));

		return "textbook/textbook_list";
	}

	/**
	 * 返回新增页面
	 * 
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/textbook/list$createTextbook")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("entity", new GjtTextbook());
		model.addAttribute("action", "create");
		return "textbook/textbook_form";
	}

	/**
	 * 新增
	 * 
	 * @param entity
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequiresPermissions("/textbook/list$createTextbook")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtTextbook entity, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			// 校验教材编号唯一性
			GjtTextbook textbook = gjtTextbookService.findByCode(entity.getTextbookCode(), user.getGjtOrg().getId());
			if (textbook == null) {
				entity.setCreatedBy(user.getId());
				entity.setOrgId(user.getGjtOrg().getId());

				gjtTextbookService.insert(entity);
			} else {
				feedback = new Feedback(false, "教材编号【" + entity.getTextbookCode() + "】已存在！");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/textbook/list";
	}

	/**
	 * 返回编辑页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/textbook/list$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", gjtTextbookService.findOne(id));
		model.addAttribute("action", "update");
		return "textbook/textbook_form";
	}

	/**
	 * 更新
	 * 
	 * @param entity
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequiresPermissions("/textbook/list$update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity") GjtTextbook entity, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtTextbook textbook = gjtTextbookService.findByCode(entity.getTextbookCode(), user.getGjtOrg().getId());
			// 校验教材编号唯一性
			if (textbook == null || textbook.getTextbookId().equals(entity.getTextbookId())) {
				GjtTextbook modifyInfo = gjtTextbookService.findOne(entity.getTextbookId());
				modifyInfo.setIsbn(entity.getIsbn());
				modifyInfo.setPositionNo(entity.getPositionNo());
				modifyInfo.setTextbookCode(entity.getTextbookCode());
				modifyInfo.setTextbookName(entity.getTextbookName());
				modifyInfo.setTextbookType(entity.getTextbookType());
				modifyInfo.setAuthor(entity.getAuthor());
				modifyInfo.setPublishing(entity.getPublishing());
				modifyInfo.setRevision(entity.getRevision());
				modifyInfo.setPrice(entity.getPrice());
				modifyInfo.setDiscountPrice(entity.getDiscountPrice());
				modifyInfo.setStatus(entity.getStatus());
				modifyInfo.setReasonType(entity.getReasonType());
				modifyInfo.setReason(entity.getReason());
				modifyInfo.setUpdatedBy(user.getId());
				modifyInfo.setCover(entity.getCover());

				gjtTextbookService.update(modifyInfo);
			} else {
				feedback = new Feedback(false, "教材编号【" + entity.getTextbookCode() + "】已存在！");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/textbook/list";
	}

	@RequiresPermissions("/textbook/list$view")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", gjtTextbookService.findOne(id));
		model.addAttribute("action", "view");
		return "textbook/textbook_form";
	}

	/**
	 * 返回导入页面
	 * 
	 * @return
	 */
	@RequiresPermissions("/textbook/list$importTextbook")
	@RequestMapping(value = "importForm", method = RequestMethod.GET)
	public String importForm() {
		return "textbook/textbook_import";
	}

	@RequiresPermissions("/textbook/list$importTextbook")
	@RequestMapping(value = "import")
	@ResponseBody
	public ImportFeedback importTextbook(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "书号", "教材名称", "教材类型", "作者", "出版社", "版次", "原价", "定价", "导入结果"};
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

					if ("".equals(datas[0])) { // 教材编号
						result[heads.length - 1] = "书号不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[1])) { // 教材名称
						result[heads.length - 1] = "教材名称不能为空";
						failedList.add(result);
						continue;
					}

					if (!"主教材".equals(datas[2]) && !"复习资料".equals(datas[2])) { // 教材类型
						result[heads.length - 1] = "教材类型有误";
						failedList.add(result);
						continue;
					}

					Float price = null;
					if (!"".equals(datas[6])) {
						try {
							price = Float.parseFloat(datas[6]); // 原价
						} catch (Exception e) {
							result[heads.length - 1] = "原价格式不正确";
							failedList.add(result);
							continue;
						}
					}

					Float discountPrice = null;
					if (!"".equals(datas[7])) {
						try {
							discountPrice = Float.parseFloat(datas[7]); // 定价
						} catch (Exception e) {
							result[heads.length - 1] = "定价格式不正确";
							failedList.add(result);
							continue;
						}
					}

					/*if ("".equals(datas[8])) { // 使用课程编号
						result[heads.length - 1] = "使用课程编号不能为空";
						failedList.add(result);
						continue;
					}*/

					GjtTextbook entity = gjtTextbookService.findByCode(datas[0], user.getGjtOrg().getId());
					if (entity != null) {
						result[heads.length - 1] = "书号已存在";
						failedList.add(result);
						continue;
					}

					/*List<GjtCourse> courseList = null;
					try { // 使用课程编号
						String[] courseCodes = datas[8].split(",");
						if (courseCodes != null && courseCodes.length > 0) {
							courseList = new ArrayList<GjtCourse>();
							for (String courseCode : courseCodes) {
								List<GjtCourse> course = gjtCourseService.findByKchAndXxId(courseCode.trim(),
										user.getGjtOrg().getId());
								if (course == null || course.size() == 0) {
									result[heads.length - 1] = "课程编号[" + courseCode + "]不存在";
									failedList.add(result);
									continue outer;
								} else {
									courseList.addAll(course);
								}
							}
						}
					} catch (Exception e) {
						result[heads.length - 1] = "使用课程编号格式有误";
						failedList.add(result);
						continue;
					}*/

					GjtTextbook textbook = new GjtTextbook();
					textbook.setTextbookCode(datas[0]);
					textbook.setTextbookName(datas[1]);
					if ("主教材".equals(datas[2])) {
						textbook.setTextbookType(1);
					} else {
						textbook.setTextbookType(2);
					}
					textbook.setAuthor(datas[3]);
					textbook.setPublishing(datas[4]);
					textbook.setRevision(datas[5]);
					textbook.setPrice(price);
					textbook.setDiscountPrice(discountPrice);
					textbook.setStatus(1);
					//textbook.setGjtCourseList(courseList);
					textbook.setCreatedBy(user.getId());
					textbook.setOrgId(user.getGjtOrg().getId());
					gjtTextbookService.insert(textbook);

					result[heads.length - 1] = "新增成功";
					successList.add(result);
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "textbook_success_" + currentTimeMillis + ".xls";
			String failedFileName = "textbook_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "教材导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "教材导入失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "textbook" + File.separator;
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

	@RequestMapping(value = "checkTextbookCode")
	@ResponseBody
	public Feedback checkTextbookCode(String textbookCode, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtTextbook textbook = gjtTextbookService.findByCode(textbookCode, user.getGjtOrg().getId());
		if (textbook == null) {
			return new Feedback(false, "");
		} else {
			return new Feedback(true, "");
		}
	}

	@RequiresPermissions("/textbook/list$exportTextbook")
	@RequestMapping(value = "exportTextbook")
	public void exportTextbook(HttpServletRequest request, HttpServletResponse response) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		List<GjtTextbook> list = gjtTextbookService.findAll(searchParams);
		HSSFWorkbook workbook = this.getWorkbook(list);
		ExcelUtil.downloadExcelFile(response, workbook, "教材表.xls");
	}

	private HSSFWorkbook getWorkbook(List<GjtTextbook> list) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("教材表");
		String[] heads = { "书号（ISBN）", "教材名称", "教材类型", "作者", "出版社", "版次", "原价", "定价", "折扣比例", "状态",
				"原因" };

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

		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		nf.setRoundingMode(RoundingMode.UP);

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

			/*String courseCodes = "";
			String courseNames = "";
			for (GjtCourse course : textbook.getGjtCourseList()) {
				courseCodes += course.getKch() + ",";
				courseNames += course.getKcmc() + ",";
			}

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(courseCodes);

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(courseNames);*/

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(textbook.getAuthor());

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(textbook.getPublishing());

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(textbook.getRevision());

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(textbook.getPrice() != null ? ("￥" + textbook.getPrice()) : "");

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(textbook.getDiscountPrice() != null ? ("￥" + textbook.getDiscountPrice()) : "");

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			if (textbook.getPrice() != null && textbook.getDiscountPrice() != null) {
				cell.setCellValue(nf.format(textbook.getDiscountPrice() / textbook.getPrice() * 100) + "%");
			} else {
				cell.setCellValue("");
			}

			cell = row.createCell(colIdx++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			if (textbook.getStatus() == 1) {
				cell.setCellValue("已启用");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue("");
			} else {
				cell.setCellValue("已停用");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				if (textbook.getReasonType() == null) {
					cell.setCellValue("");
				} else if (textbook.getReasonType() == 1) {
					cell.setCellValue("改版:" + textbook.getReason());
				} else {
					cell.setCellValue("更换:" + textbook.getReason());
				}
			}
		}

		return wb;
	}

	@Autowired
	private GjtStudentTextbookOrderService gjtStudentTextbookOrderService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@ResponseBody
	@RequestMapping(value = "/initStudentTextbookOrder")
	public Object initStudentTextbookOrder() throws InvalidFormatException, IOException, InterruptedException {
		String path = getClass().getResource("/excel/temp/").getPath() + "student_order.xls";
		List<String> noexists = new ArrayList<String>();
		InputStream inputStream = new FileInputStream(path);
		List<String[]> dataList = ExcelUtil.readAsStringList(inputStream, 1, 3);
		List<String> sfzh = new ArrayList<String>();

		final int nThreads = Runtime.getRuntime().availableProcessors() * 8;
		ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
		final CountDownLatch complete = new CountDownLatch(dataList.size());
		for (String[] data : dataList) {
			if (sfzh.contains(data[0])) {
				continue;
			}
			sfzh.add(data[0]);
			TempThread temp = new TempThread(complete, gjtStudentTextbookOrderService, gjtStudentInfoService, gjtGradeService, noexists, data);
			executorService.execute(temp);
		}

		try {
			// 等待完成，设置超时时间25分钟
			complete.await(25, TimeUnit.MINUTES);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("noexists", noexists);
		return result;
	}

	public class TempThread implements Runnable {

		private CountDownLatch complete;
		private GjtStudentTextbookOrderService gjtStudentTextbookOrderService;
		private GjtStudentInfoService gjtStudentInfoService;
		private GjtGradeService gjtGradeService;
		private List<String> noexists;
		private String[] data;

		public TempThread(CountDownLatch complete, GjtStudentTextbookOrderService gjtStudentTextbookOrderService, GjtStudentInfoService gjtStudentInfoService,
				GjtGradeService gjtGradeService, List<String> noexists, String[] data) {
			this.complete = complete;
			this.gjtStudentTextbookOrderService = gjtStudentTextbookOrderService;
			this.gjtStudentInfoService = gjtStudentInfoService;
			this.gjtGradeService = gjtGradeService;
			this.noexists = noexists;
			this.data = data;
		}
		
		public void run() {
			try {
				GjtStudentInfo student = gjtStudentInfoService.queryBySfzh(data[0]);
				if (student == null) {
					noexists.add("sfzh:" + data[0]);
					return;
				}
				String currentGradeId = gjtGradeService.getCurrentGradeId(student.getXxId());
				if (currentGradeId == null) {
					noexists.add("sfzh:" + data[0] + " gradeId:" + currentGradeId);
				}
				GjtStudentTextbookOrder textbookOrder = gjtStudentTextbookOrderService.getByStudentIdAndGradeId(student.getStudentId(),
						currentGradeId);
				if (textbookOrder != null) {
					textbookOrder.setOrderNo(student.getGjtSignup().getOrderSn());
					textbookOrder.setTextbookCodes(data[2]);
					gjtStudentTextbookOrderService.save(textbookOrder);
					return;
				}
				textbookOrder = new GjtStudentTextbookOrder();
				textbookOrder.setId(UUIDUtils.random());
				textbookOrder.setGradeId(currentGradeId);
				textbookOrder.setIsDeleted(Constants.NODELETED);
				textbookOrder.setOrderNo(student.getGjtSignup().getOrderSn());
				textbookOrder.setStudentId(student.getStudentId());
				textbookOrder.setTextbookCodes(data[2]);
				gjtStudentTextbookOrderService.save(textbookOrder);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				complete.countDown();
			}
		}
	}
}
