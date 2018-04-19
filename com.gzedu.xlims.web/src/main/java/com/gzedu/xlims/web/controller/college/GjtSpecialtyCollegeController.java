/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.college;

import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.college.GjtSpecialtyCollege;
import com.gzedu.xlims.pojo.status.CourseCategory;
import com.gzedu.xlims.pojo.status.ExamUnit;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.edumanage.GjtSpecialtyPlanService;
import com.gzedu.xlims.service.organization.GjtSpecialtyCollegeService;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/edumanage/specialtyCollege")
public class GjtSpecialtyCollegeController {

	private static final Log log = LogFactory.getLog(GjtSpecialtyCollegeController.class);

	@Autowired
	GjtSpecialtyPlanService gjtSpecialtyPlanService;

	@Autowired
	CommonMapService commonMapService;
	@Autowired
	GjtSpecialtyCollegeService gjtSpecialtyCollegeService;
	@Autowired
	GjtCourseService gjtCourseService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Sort sort = new Sort(Direction.DESC, "createdDt");

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, sort);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<GjtSpecialtyCollege> pageInfo = gjtSpecialtyCollegeService.queryAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		List<String> ids = new ArrayList<String>();
		for (GjtSpecialtyCollege s : pageInfo.getContent()) {
			ids.add(s.getSpecialtyId());
		}
		if (ids.size() > 0) {
			Map<String, Long> countMap = gjtSpecialtyPlanService.countBySpecialtyId(ids);
			model.addAttribute("countMap", countMap);
		}
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("pageInfo", pageInfo);
		return "edumanage/college/specialty/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		List<GjtSpecialtyPlan> list = gjtSpecialtyPlanService.findBySpecialtyId(id);
		Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
		for (GjtSpecialtyPlan plan : list) {
			int termCode = plan.getTermTypeCode();
			if (countMap.containsKey(termCode)) {
				countMap.put(termCode, countMap.get(termCode) + 1);
			} else {
				countMap.put(termCode, 1);
			}
		}
		GjtSpecialtyCollege gjtSpecialtyCollege = gjtSpecialtyCollegeService.findOne(id);
		model.addAttribute("specialty", gjtSpecialtyCollege);
		Map<String, String> courseTypeMap = commonMapService.getDates("CourseType");// 课程模块
		model.addAttribute("courseTypeMap", courseTypeMap);
		model.addAttribute("list", list);
		model.addAttribute("countMap", countMap);
		return "edumanage/college/specialty//view";
	}

	/**
	 * 导入专业规则
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年6月6日 下午3:55:05
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@SysLog("专业规则-导入专业规则")
	@RequestMapping(value = "import")
	@ResponseBody
	public ImportFeedback importSpecialtyPlan(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "专业规则号", "专业名称", "专业层次", "课程模块", "课程代码", "课程名称", "课程性质", "课程类型", "学时", "建议开设学期",
					"要求考试单位", "试卷号", "导入结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}
			Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
			Map<String, String> courseTypeMap = commonMapService.getDates("CourseType");// 课程模块
			if (dataList != null && dataList.size() > 0) {
				outer: for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据

					if (datas.length < heads.length - 1) {
						result[heads.length - 1] = "数据不全";
						failedList.add(result);
						continue;
					}
					// 字段验证
					for (int i = 0; i < datas.length; i++) {
						if (StringUtils.isEmpty(datas[i])) {
							result[heads.length - 1] = heads[i] + " 不能为空";
							failedList.add(result);
							continue outer;
						}
						boolean flag = false;
						try {
							if ("专业层次".equals(heads[i])) {
								flag = pyccMap.containsValue(datas[i]);
							} else if ("课程模块".equals(heads[i])) {
								flag = courseTypeMap.containsValue(datas[i]);
							} else if ("课程性质".equals(heads[i])) {
								flag = CourseCategory.valueOf(datas[i]) != null;
							} else if ("学时".equals(heads[i])) {
								Integer.parseInt(datas[i]);
								flag = true;
							} else if ("建议开设学期".equals(heads[i])) {
								if (Integer.parseInt(datas[i]) >= 1 && Integer.parseInt(datas[i]) <= 6) {
									flag = true;
								}
							} else if ("要求考试单位".equals(heads[i])) {
								flag = ExamUnit.getCodeByName(datas[i]) != null;
							} else {
								flag = true;
							}
						} catch (Exception e) {
							flag = false;
						}
						if (!flag) {
							result[heads.length - 1] = heads[i] + " 数据有误";
							failedList.add(result);
							continue outer;
						}
					}

					// 专业计划唯一性验证
					GjtSpecialtyCollege gjtSpecialtyCollege = gjtSpecialtyCollegeService
							.existsSpecialtyByRuleCode(datas[0], user.getGjtOrg().getId());
					if (gjtSpecialtyCollege != null) {
						List<GjtCourse> courses = gjtCourseService.findByKchAndXxId(datas[4], user.getGjtOrg().getId());
						if (CollectionUtils.isNotEmpty(courses)) {
							boolean exists = gjtSpecialtyPlanService.isExists(gjtSpecialtyCollege.getSpecialtyId(),
									courses.get(0).getCourseId());
							if (exists) {
								result[heads.length - 1] = "专业规则已存在";
								failedList.add(result);
								continue;
							}
						}
					}
					result[heads.length - 1] = "新增成功";
					successList.add(result);
				}
			}

			// 保存成功数据到数据库
			gjtSpecialtyCollegeService.saveImportData(successList, user);
			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "secialtyPlan_success_" + currentTimeMillis + ".xls";
			String failedFileName = "secialtyPlan_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "专业计划导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "专业计划导入失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "secialtyPlan" + File.separator;
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
