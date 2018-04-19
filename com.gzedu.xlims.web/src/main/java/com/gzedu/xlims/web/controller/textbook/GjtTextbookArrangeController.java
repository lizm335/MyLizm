package com.gzedu.xlims.web.controller.textbook;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.pojo.textbook.GjtTextbookArrange;
import com.gzedu.xlims.pojo.textbook.GjtTextbookPlan;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.textbook.GjtTextbookArrangeService;
import com.gzedu.xlims.service.textbook.GjtTextbookPlanService;
import com.gzedu.xlims.service.textbook.GjtTextbookService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/textbookArrange")
public class GjtTextbookArrangeController {

	private static final Logger log = LoggerFactory.getLogger(GjtTextbookArrangeController.class);

	@Autowired
	private GjtTextbookArrangeService gjtTextbookArrangeService;

	@Autowired
	private GjtTextbookPlanService gjtTextbookPlanService;

	@Autowired
	private GjtTextbookService gjtTextbookService;

	@Autowired
	private GjtCourseService gjtCourseService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtOrgService gjtOrgService;

	@Autowired
	private GjtGradeService gjtGradeService;

	/*
	 * @RequestMapping(value = "list", method = RequestMethod.GET) public String
	 * list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
	 * 
	 * @RequestParam(value = "page.size", defaultValue = "10") int pageSize,
	 * Model model, HttpServletRequest request) { GjtUserAccount user =
	 * (GjtUserAccount)
	 * request.getSession().getAttribute(WebConstants.CURRENT_USER); List<Order>
	 * orders = new ArrayList<Order>(); orders.add(new Order(Direction.DESC,
	 * "gjtTextbookPlan.gjtGrade.startDate")); orders.add(new
	 * Order(Direction.DESC, "gjtTextbookPlan.createdDt")); orders.add(new
	 * Order(Direction.DESC, "gjtTextbook.createdDt")); PageRequest pageRequst =
	 * Servlets.buildPageRequest(pageNumber, pageSize, new Sort(orders));
	 * Map<String, Object> searchParams =
	 * Servlets.getParametersStartingWith(request, "search_"); if
	 * ("1".equals(user.getGjtOrg().getOrgType())) {
	 * searchParams.put("EQ_gjtTextbookPlan.orgId", user.getGjtOrg().getId()); }
	 * else { List<String> parents =
	 * gjtOrgService.queryParents(user.getGjtOrg().getId()); if (parents != null
	 * && parents.size() > 0) { searchParams.put("IN_gjtTextbookPlan.orgId",
	 * parents); } else { searchParams.put("EQ_gjtTextbookPlan.orgId",
	 * user.getGjtOrg().getId()); } }
	 * 
	 * // 查询“待编排”、“编排中”和“已编排" SimpleDateFormat sdf = new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); Map<String, Object> map = new
	 * HashMap<String, Object>(); map.putAll(searchParams);
	 * map.remove("EQ_status"); map.put("GT_gjtTextbookPlan.arrangeSdate",
	 * sdf.format(new Date())); model.addAttribute("notArrange",
	 * gjtTextbookArrangeService.findAll(map, pageRequst).getTotalElements());
	 * map.remove("GT_gjtTextbookPlan.arrangeSdate");
	 * map.put("LT_gjtTextbookPlan.arrangeEdate", sdf.format(new Date()));
	 * model.addAttribute("hadArrange", gjtTextbookArrangeService.findAll(map,
	 * pageRequst).getTotalElements());
	 * map.remove("LT_gjtTextbookPlan.arrangeEdate");
	 * map.put("LTE_gjtTextbookPlan.arrangeSdate", sdf.format(new Date()));
	 * map.put("GTE_gjtTextbookPlan.arrangeEdate", sdf.format(new Date()));
	 * model.addAttribute("arranging", gjtTextbookArrangeService.findAll(map,
	 * pageRequst).getTotalElements());
	 * 
	 * if (searchParams.get("EQ_status") != null) { String status =
	 * searchParams.get("EQ_status").toString(); if ("1".equals(status)) { //
	 * 待编排 searchParams.put("GT_gjtTextbookPlan.arrangeSdate", sdf.format(new
	 * Date())); } else if ("2".equals(status)) { // 编排中
	 * searchParams.put("LTE_gjtTextbookPlan.arrangeSdate", sdf.format(new
	 * Date())); searchParams.put("GTE_gjtTextbookPlan.arrangeEdate",
	 * sdf.format(new Date())); } else if ("3".equals(status)) { // 已编排
	 * searchParams.put("LT_gjtTextbookPlan.arrangeEdate", sdf.format(new
	 * Date())); }
	 * 
	 * searchParams.remove("EQ_status"); }
	 * 
	 * Page<GjtTextbookArrange> pageInfo =
	 * gjtTextbookArrangeService.findAll(searchParams, pageRequst);
	 * model.addAttribute("pageInfo", pageInfo);
	 * 
	 * // 设置状态 Date now = new Date(); for (GjtTextbookArrange arrange :
	 * pageInfo) { if
	 * (arrange.getGjtTextbookPlan().getArrangeSdate().compareTo(now) > 0) {
	 * arrange.setStatus(1); } else if
	 * (arrange.getGjtTextbookPlan().getArrangeEdate().compareTo(now) < 0) {
	 * arrange.setStatus(3); } else { arrange.setStatus(2); } }
	 * 
	 * Map<String, String> termMap =
	 * commonMapService.getGradeMap(user.getGjtOrg().getId());
	 * model.addAttribute("termMap", termMap);
	 * 
	 * Map<String, String> courseMap =
	 * commonMapService.getCourseMap(user.getGjtOrg().getId());
	 * model.addAttribute("courseMap", courseMap);
	 * 
	 * Map<String, String> textbookPlanMap =
	 * commonMapService.getTextbookPlanMap(user.getGjtOrg().getId());
	 * model.addAttribute("textbookPlanMap", textbookPlanMap);
	 * 
	 * // 获取权限 Subject subject = SecurityUtils.getSubject();
	 * model.addAttribute("isBtnImport",
	 * subject.isPermitted("/textbookArrange/list$importTextbookArrange"));
	 * model.addAttribute("isBtnCreate",
	 * subject.isPermitted("/textbookArrange/list$createTextbookArrange"));
	 * model.addAttribute("isBtnDelete",
	 * subject.isPermitted("/textbookArrange/list$delete"));
	 * 
	 * return "textbook/textbookArrange_list"; }
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		if ("1".equals(user.getGjtOrg().getOrgType())) {
			searchParams.put("orgId", user.getGjtOrg().getId());
		} else {
			GjtOrg parentOrg = gjtOrgService.queryParentBySonId(user.getGjtOrg().getId());
			if (parentOrg != null) {
				searchParams.put("orgId", parentOrg.getId());
			} else {
				searchParams.put("orgId", user.getGjtOrg().getId());
			}
		}
			  
		Page<Map<String, Object>> pageInfo = gjtTextbookArrangeService.findTextbookArrangeList(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);
		return "textbook/textbookArrange_list";
	}

	@RequiresPermissions("/textbookArrange/list$createTextbookArrange")
	@RequestMapping(value = "autoCreate")
	@ResponseBody
	public Feedback autoCreate(HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "生成教材发放安排成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			// 查询当前教材计划
			GjtTextbookPlan textbookPlan = gjtTextbookPlanService.findCurrentArrangePlan(user.getGjtOrg().getId());
			if (textbookPlan == null) {
				feedback = new Feedback(false, "当前没有可生成的教材发放安排");
			} else {
				// 查询可生成安排的主教材
				List<GjtTextbook> textbookList = gjtTextbookService.findCurrentArrangeTextbook(textbookPlan.getPlanId(),
						user.getGjtOrg().getId());
				if (textbookList != null && textbookList.size() > 0) {
					List<GjtTextbookArrange> textbookArrangeList = new ArrayList<GjtTextbookArrange>();
					for (GjtTextbook textbook : textbookList) {
						GjtTextbookArrange textbookArrange = new GjtTextbookArrange();
						textbookArrange.setGjtTextbookPlan(textbookPlan);
						textbookArrange.setGjtTextbook(textbook);
						textbookArrange.setCreatedBy(user.getId());

						textbookArrangeList.add(textbookArrange);
					}

					gjtTextbookArrangeService.insert(textbookArrangeList);
				}
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "生成教材发放安排失败");
			log.error(e.getMessage(), e);
		}

		return feedback;
	}

	@RequiresPermissions("/textbookArrange/list$delete")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, HttpServletRequest request) {
		if (StringUtils.isNotBlank(ids)) {
			try {
				GjtTextbookArrange textbookArrange = gjtTextbookArrangeService.findOne(ids);
				gjtTextbookArrangeService.delete(textbookArrange);

				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	@RequiresPermissions("/textbookArrange/list$importTextbookArrange")
	@RequestMapping(value = "import")
	@ResponseBody
	public ImportFeedback importTextbookArrange(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "学期", "教材计划编号", "教材计划名称", "书号", "教材名称", "教材类型", "发放课程号", "发放课程名称", "导入结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}

			if (dataList != null && dataList.size() > 0) {
				outer: for (String[] datas : dataList) {
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

					if ("".equals(datas[3])) { // 教材编号
						result[heads.length - 1] = "书号不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[6])) { // 发放课程号
						result[heads.length - 1] = "发放课程号不能为空";
						failedList.add(result);
						continue;
					}

					GjtTextbookPlan textbookPlan = gjtTextbookPlanService.findByPlanCodeAndOrgId(datas[1],
							user.getGjtOrg().getId());
					if (textbookPlan == null) {
						result[heads.length - 1] = "找不到教材计划";
						failedList.add(result);
						continue;
					}

					GjtTextbook textbook = gjtTextbookService.findByCode(datas[3], user.getGjtOrg().getId());
					if (textbook == null) {
						result[heads.length - 1] = "找不到教材";
						failedList.add(result);
						continue;
					}

					if (textbook.getStatus() == 0) {
						result[heads.length - 1] = "该教材已停用";
						failedList.add(result);
						continue;
					}

					if (textbook.getTextbookType() != 2) {
						result[heads.length - 1] = "该教材不是复习资料";
						failedList.add(result);
						continue;
					}

					GjtTextbookArrange textbookArrange = gjtTextbookArrangeService
							.findByPlanIdAndTextbookId(textbookPlan.getPlanId(), textbook.getTextbookId());
					if (textbookArrange != null) {
						result[heads.length - 1] = "记录已存在";
						failedList.add(result);
						continue;
					}
					
					List<GjtCourse> courseList = null;
					try { // 发放课程号
						String[] courseCodes = datas[6].split(",");
						if (courseCodes != null && courseCodes.length > 0) {
							courseList = new ArrayList<GjtCourse>();
							for (String courseCode : courseCodes) {
								List<GjtCourse> course = gjtCourseService.findByKchAndXxId(courseCode.trim(),
										user.getGjtOrg().getId());
								if (course == null || course.size() == 0) {
									result[heads.length - 1] = "课程号[" + courseCode + "]不存在";
									failedList.add(result);
									continue outer;
								} else {
									courseList.addAll(course);
								}
							}
						}
					} catch (Exception e) {
						result[heads.length - 1] = "发放课程号格式有误";
						failedList.add(result);
						continue;
					}

					textbookArrange = new GjtTextbookArrange();
					textbookArrange.setGjtTextbookPlan(textbookPlan);
					textbookArrange.setGjtTextbook(textbook);
					textbookArrange.setCreatedBy(user.getId());
					textbookArrange.setGjtCourseList(courseList);

					gjtTextbookArrangeService.insert(textbookArrange);

					result[heads.length - 1] = "新增成功";
					successList.add(result);
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "textbookArrange_success_" + currentTimeMillis + ".xls";
			String failedFileName = "textbookArrange_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "复习资料发放安排导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "复习资料发放安排导入失败记录");

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
	
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String setting(HttpServletRequest request, ModelMap model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtTextbookPlan textbookPlan = gjtTextbookPlanService.findCurrentArrangePlan(user.getGjtOrg().getId());
		if (textbookPlan != null) {
			// 查询可生成安排的主教材
			List<GjtTextbook> textbookList = gjtTextbookService.findCurrentArrangeTextbook(textbookPlan.getPlanId(),
					user.getGjtOrg().getId());
			model.addAttribute("textbookList", textbookList);
		}
		model.addAttribute("textbookPlan", textbookPlan);
		
		return "textbook/textbookArrange_setting";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Feedback save(@RequestParam("textbookPlanId") String textbookPlanId,
			@RequestParam("textbookIds") String textbookIds, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "设置成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtTextbookPlan textbookPlan = gjtTextbookPlanService.findOne(textbookPlanId);
			List<GjtTextbook> textbookList = gjtTextbookService.findAll(Arrays.asList(textbookIds.split(",")));
			if (textbookList != null && textbookList.size() > 0) {
				List<GjtTextbookArrange> textbookArrangeList = new ArrayList<GjtTextbookArrange>();
				for (GjtTextbook textbook : textbookList) {
					GjtTextbookArrange textbookArrange = new GjtTextbookArrange();
					textbookArrange.setGjtTextbookPlan(textbookPlan);
					textbookArrange.setGjtTextbook(textbook);
					textbookArrange.setCreatedBy(user.getId());

					textbookArrangeList.add(textbookArrange);
				}

				gjtTextbookArrangeService.insert(textbookArrangeList);
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "设置失败");
			log.error(e.getMessage(), e);
		}

		return feedback;
	}

	@RequestMapping(value = "findTextbookList", method = RequestMethod.GET)
	public String findTextbookList(String gradeId, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<GjtTextbook> gjtTextbooks = gjtTextbookService.findUnsetTextBookByGradeId(gradeId);
		model.addAttribute("gjtTextbooks", gjtTextbooks);
		model.addAttribute("gjtGrade", gjtGradeService.queryById(gradeId));
		return "textbook/chooseTextbook_list";
	}

	@RequestMapping(value = "findArrangeTextBook", method = RequestMethod.GET)
	public String findArrangeTextBook(String gradeId, @RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GjtTextbook> pageInfo = gjtTextbookService.findArrangeTextBook(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);
		model.addAttribute("grade", termMap.get(searchParams.get("gradeId")));
		return "textbook/arrangeTextbook_list";
	}

	@ResponseBody
	@RequestMapping(value = "saveArrangeTextbooks")
	public Feedback saveArrangeTextbooks(@RequestParam(value = "textbookIds[]") List<String> textbookIds, String gradeId) {
		Feedback feedback = new Feedback(true, null);
		try {
			gjtTextbookArrangeService.saveArrangeTextbooks(gradeId, textbookIds);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback.setSuccessful(false);
			feedback.setMessage("系统异常");
		}
		return feedback;
	}

	@ResponseBody
	@RequestMapping(value = "removeTextbook", method = RequestMethod.GET)
	public Feedback removeTextbook(String gradeId, String textbookId) {
		Feedback feedback = new Feedback(true, null);
		try {
			gjtTextbookArrangeService.removeTextbook(gradeId, textbookId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback.setSuccessful(false);
			feedback.setMessage("系统异常");
		}
		return feedback;
	}

	@RequestMapping(value = "changeStatus", method = RequestMethod.GET)
	public String changeStatus(String gradeId, int status) {
		GjtGrade gjtGrade = gjtGradeService.queryById(gradeId);
		gjtGrade.setTextbookStatus(status);
		gjtGradeService.updateEntity(gjtGrade);
		return "redirect:/textbookArrange/list";
	}
}
