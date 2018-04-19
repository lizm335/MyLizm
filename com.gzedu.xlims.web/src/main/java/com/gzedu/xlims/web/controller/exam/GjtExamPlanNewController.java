package com.gzedu.xlims.web.controller.exam;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gzedu.xlims.common.MapKit;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.exam.GjtExamBatchNewService;
import com.gzedu.xlims.service.exam.GjtExamPlanNewService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.common.vo.CommonSelect;

/**
 * 开考科目
 */
@Controller
@RequestMapping("/exam/new/plan")
public class GjtExamPlanNewController {

	private final static Logger log = LoggerFactory.getLogger(GjtExamPlanNewController.class);

	@Autowired
	private GjtExamPlanNewService gjtExamPlanNewService;

	@Autowired
	private GjtExamBatchNewService gjtExamBatchNewService;

	@Autowired
	private GjtCourseService gjtCourseService;

	@Autowired
	private GjtSpecialtyService gjtSpecialtyService;

	@Autowired
	private GjtOrgService gjtOrgService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private CacheService cacheService;

	/**
	 * 开考科目列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, "createdDt", "DESC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String subjectType = (String) searchParams.get("EQ_subjectType");
		if (StringUtils.isBlank(subjectType)) {
			searchParams.put("EQ_subjectType", 1);// 默认中央课程
			subjectType = "1";
		}
		model.addAttribute("subjectType", subjectType);

		if ("1".equals(user.getGjtOrg().getOrgType())) {
			searchParams.put("EQ_xxId", user.getGjtOrg().getId());
		} else {
			List<String> parents = gjtOrgService.queryParents(user.getGjtOrg().getId());
			if (parents != null && parents.size() > 0) {
				searchParams.put("IN_xxId", parents);
			} else {
				searchParams.put("EQ_xxId", user.getGjtOrg().getId());
			}
		}

		String schoolId = user.getGjtOrg().getId();

		// 默认选择当前期(批次)
		if (EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EQ_examBatchCode"))) {
			String code = commonMapService.getCurrentGjtExamBatchNew(schoolId);
			searchParams.put("EQ_examBatchCode", code);
			model.addAttribute("examBatchCode", code);
		} else if (EmptyUtils.isNotEmpty(searchParams) && EmptyUtils.isNotEmpty(searchParams.get("EQ_examBatchCode"))) {
			model.addAttribute("examBatchCode", ObjectUtils.toString(searchParams.get("EQ_examBatchCode")));
		}

		// 查询“未开始”、“预约中”、“待考试”、“考试中”和“已结束”
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.remove("EQ_status");
		model.addAttribute("all", gjtExamPlanNewService.queryAll(map, pageRequst).getTotalElements());

		map.put("GT_examBatchNew.bookSt", today);
		model.addAttribute("notStart", gjtExamPlanNewService.queryAll(map, pageRequst).getTotalElements());

		map.remove("GT_examBatchNew.bookSt");
		map.put("LTE_examBatchNew.bookSt", today);
		map.put("GTE_examBatchNew.bookEnd", today);
		model.addAttribute("booking", gjtExamPlanNewService.queryAll(map, pageRequst).getTotalElements());

		map.remove("LTE_examBatchNew.bookSt");
		map.remove("GTE_examBatchNew.bookEnd");
		map.put("LT_examBatchNew.bookEnd", today);
		map.put("GT_examSt", today);
		model.addAttribute("preExam", gjtExamPlanNewService.queryAll(map, pageRequst).getTotalElements());

		map.remove("LT_examBatchNew.bookEnd");
		map.remove("GT_examSt");
		map.put("LTE_examSt", today);
		map.put("GT_examEnd", today);
		model.addAttribute("examing", gjtExamPlanNewService.queryAll(map, pageRequst).getTotalElements());

		map.remove("LTE_examSt");
		map.remove("GT_examEnd");
		map.put("LT_examEnd", today);
		model.addAttribute("end", gjtExamPlanNewService.queryAll(map, pageRequst).getTotalElements());

		// 状态转换
		if (searchParams.get("EQ_status") != null && !"".equals(searchParams.get("EQ_status"))) {
			int status = Integer.parseInt(searchParams.get("EQ_status").toString());
			if (status == 1) {
				searchParams.put("GT_examBatchNew.bookSt", today);
			} else if (status == 2) {
				searchParams.put("LTE_examBatchNew.bookSt", today);
				searchParams.put("GTE_examBatchNew.bookEnd", today);
			} else if (status == 3) {
				searchParams.put("LT_examBatchNew.bookEnd", today);
				searchParams.put("GT_examSt", today);
			} else if (status == 4) {
				searchParams.put("LTE_examSt", today);
				searchParams.put("GT_examEnd", today);
			} else if (status == 5) {
				searchParams.put("LT_examEnd", today);
			}
		}
		searchParams.remove("EQ_status");

		Page<GjtExamPlanNew> pageInfo = gjtExamPlanNewService.queryAll(searchParams, pageRequst);
		Date now = new Date();
		for (GjtExamPlanNew examPlan : pageInfo) {
			if (examPlan.getExamBatchNew().getBookSt().compareTo(now) > 0) {
				examPlan.setStatus(1); // 未开始
			} else if (examPlan.getExamBatchNew().getBookEnd().compareTo(now) >= 0
					&& examPlan.getExamBatchNew().getBookSt().compareTo(now) <= 0) {
				examPlan.setStatus(2); // 预约中
			} else if (examPlan.getExamBatchNew().getBookEnd().compareTo(now) < 0 && examPlan.getExamSt() != null
					&& examPlan.getExamSt().compareTo(now) > 0) {
				examPlan.setStatus(3); // 待考试
			} else if (examPlan.getExamSt() != null && examPlan.getExamSt().compareTo(now) <= 0
					&& examPlan.getExamEnd() != null && examPlan.getExamEnd().compareTo(now) > 0) {
				examPlan.setStatus(4); // 考试中
			} else if (examPlan.getExamEnd() != null && examPlan.getExamEnd().compareTo(now) < 0) {
				examPlan.setStatus(5); // 已结束
			}
		}
		// searchParams.put("schoolId",schoolId);
		// Page<Map> pageInfo
		// =gjtExamPlanNewService.queryExamPlan(searchParams,pageRequst);

		model.addAttribute("courseList", gjtCourseService.findByXxidAndIsDeleted(schoolId, "N"));
		model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(schoolId));
		model.addAttribute("examTypeMap", MapKit.toIntAscMap(commonMapService.getExamTypeIntMap()));
		model.addAttribute("examStyleMap", commonMapService.getDates("EXAM_STYLE"));
		model.addAttribute("pageInfo", pageInfo);

		return "edumanage/exam/exam_plan_listNew";
	}

	/**
	 * 创建开考科目
	 * 
	 * @param examPlan
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback add(HttpServletRequest request, @ModelAttribute GjtExamPlanNew examPlan) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		GjtExamBatchNew examBatch = gjtExamBatchNewService.queryByExamBatchCode(examPlan.getExamBatchCode());
		if (examBatch.getPlanSt().compareTo(new Date()) <= 0 && examBatch.getPlanEnd().compareTo(new Date()) >= 0) {
			GjtExamPlanNew examPlanNew = gjtExamPlanNewService.findByExamBatchCodeAndExamNoAndType(
					examPlan.getExamBatchCode(), examPlan.getExamNo(), examPlan.getType());
			if (examPlanNew == null) {
				String[] courseIds = request.getParameterValues("courseIds[]");
				if (courseIds != null && courseIds.length > 0) {
					List<GjtCourse> gjtCourseList = gjtCourseService.findAll(Arrays.asList(courseIds));
					examPlan.setGjtCourseList(gjtCourseList);
				}

				String[] specialtyIds = request.getParameterValues("specialtyIds[]");
				if (specialtyIds != null && specialtyIds.length > 0) {
					List<GjtSpecialty> gjtSpecialtyList = new ArrayList<GjtSpecialty>();
					for (int i = 0; i < specialtyIds.length; i++) {
						List<GjtSpecialty> list = gjtSpecialtyService.findSpecialtyBySpecialtyBaseId(specialtyIds[i]);
						gjtSpecialtyList.addAll(list);
					}
					if (gjtSpecialtyList != null && gjtSpecialtyList.size() > 0) {
						examPlan.setGjtSpecialtyList(gjtSpecialtyList);
					}
				}

				GjtExamBatchNew examBatchNew = gjtExamBatchNewService.queryByExamBatchCode(examPlan.getExamBatchCode());
				examPlan.setExamBatchId(examBatchNew.getExamBatchId());
				examPlan.setGradeId(examBatchNew.getGradeId());
				examPlan.setXxId(user.getGjtOrg().getId());
				examPlan.setCreatedBy(user.getId());
				examPlan.setCreatedDt(new Date());
				gjtExamPlanNewService.insert(examPlan);
				gjtExamPlanNewService.insertExamPlanToTongyongSpecialty(examPlan.getExamPlanId());
			} else {
				feedback = new Feedback(false, "创建失败：试卷号已存在！");
			}
		} else {
			feedback = new Feedback(false, "创建失败：不在该考试计划的开考科目设置时间内！");
		}

		return feedback;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "{op}/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable String op, @PathVariable String id, Model model, HttpServletRequest request,
			String subjectType) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtExamPlanNew planEntity = new GjtExamPlanNew();
		String schoolId = user.getGjtOrg().getId();
		if ("create".equals(op)) {
			model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(schoolId));
		} else {
			planEntity = gjtExamPlanNewService.queryBy(id);
		}

		model.addAttribute("examTypeMap", MapKit.toIntAscMap(commonMapService.getExamTypeIntMap()));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyBaseMap(schoolId));
		model.addAttribute("examStyleMap", commonMapService.getDates("EXAM_STYLE"));
		model.addAttribute("entity", planEntity);
		model.addAttribute("subjectType", subjectType);
		model.addAttribute("action", op);
		return "edumanage/exam/exam_plan_formNew";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(@ModelAttribute GjtExamPlanNew entity, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		GjtExamPlanNew examPlanNew = gjtExamPlanNewService
				.findByExamBatchCodeAndExamNoAndType(entity.getExamBatchCode(), entity.getExamNo(), entity.getType());
		if (examPlanNew == null || examPlanNew.getExamPlanId().equals(entity.getExamPlanId())) {
			GjtExamPlanNew gjtExamPlanNew = gjtExamPlanNewService.queryBy(entity.getExamPlanId());
			gjtExamPlanNew.setExamPlanName(entity.getExamPlanName());
			gjtExamPlanNew.setExamNo(entity.getExamNo());
			gjtExamPlanNew.setType(entity.getType());
			gjtExamPlanNew.setExamStyle(entity.getExamStyle());
			gjtExamPlanNew.setXkPercent(entity.getXkPercent());
			gjtExamPlanNew.setExamSt(entity.getExamSt());
			gjtExamPlanNew.setExamEnd(entity.getExamEnd());
			gjtExamPlanNew.setExamPlanOrder(entity.getExamPlanOrder());
			gjtExamPlanNew.setExamPlanLimit(entity.getExamPlanLimit());
			// 做了修改才更新数据
			if (StringUtils.isNotBlank(entity.getDocumentFileName()))
				gjtExamPlanNew.setDocumentFileName(entity.getDocumentFileName());
			if (StringUtils.isNotBlank(entity.getDocumentFilePath()))
				gjtExamPlanNew.setDocumentFilePath(entity.getDocumentFilePath());
			gjtExamPlanNew.setUpdatedBy(user.getId());
			gjtExamPlanNew.setUpdatedDt(new Date());

			String[] courseIds = request.getParameterValues("courseIds[]");
			if (courseIds != null && courseIds.length > 0) {
				List<GjtCourse> gjtCourseList = gjtCourseService.findAll(Arrays.asList(courseIds));
				gjtExamPlanNew.setGjtCourseList(gjtCourseList);
			}

			String[] specialtyIds = request.getParameterValues("specialtyIds[]");
			if (specialtyIds != null && specialtyIds.length > 0) {
				List<GjtSpecialty> gjtSpecialtyList = new ArrayList<GjtSpecialty>();
				for (int i = 0; i < specialtyIds.length; i++) {
					List<GjtSpecialty> list = gjtSpecialtyService.findSpecialtyBySpecialtyBaseId(specialtyIds[i]);
					gjtSpecialtyList.addAll(list);
				}
				if (gjtSpecialtyList != null && gjtSpecialtyList.size() > 0) {
					gjtExamPlanNew.setGjtSpecialtyList(gjtSpecialtyList);
				} else {
					gjtExamPlanNew.setGjtSpecialtyList(null);
				}
			} else {
				gjtExamPlanNew.setGjtSpecialtyList(null);
			}

			gjtExamPlanNewService.update(gjtExamPlanNew);
			gjtExamPlanNewService.insertExamPlanToTongyongSpecialty(gjtExamPlanNew.getExamPlanId());
		} else {
			feedback = new Feedback(false, "创建失败：试卷号已存在！");
		}

		return feedback;
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(@RequestParam String ids, HttpServletRequest request) {
		// GjtUserAccount user = (GjtUserAccount)
		// request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (StringUtils.isNotBlank(ids)) {
			try {
				gjtExamPlanNewService.delete(Arrays.asList(ids.split(",")));
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}

		return new Feedback(false, "删除失败");
	}

	/**
	 * 根据搜索条件导出开考科目
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "export")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(1, 10000, "createdDt", "DESC");// 单次最多导出10000条
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		if ("1".equals(user.getGjtOrg().getOrgType())) {
			searchParams.put("EQ_xxId", user.getGjtOrg().getId());
		} else {
			List<String> parents = gjtOrgService.queryParents(user.getGjtOrg().getId());
			if (parents != null && parents.size() > 0) {
				searchParams.put("IN_xxId", parents);
			} else {
				searchParams.put("EQ_xxId", user.getGjtOrg().getId());
			}
		}

		// 状态转换
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		if (searchParams.get("EQ_status") != null && !"".equals(searchParams.get("EQ_status"))) {
			int status = Integer.parseInt(searchParams.get("EQ_status").toString());
			if (status == 1) {
				searchParams.put("GT_examBatchNew.bookSt", today);
			} else if (status == 2) {
				searchParams.put("LTE_examBatchNew.bookSt", today);
				searchParams.put("GTE_examBatchNew.bookEnd", today);
			} else if (status == 3) {
				searchParams.put("LT_examBatchNew.bookEnd", today);
				searchParams.put("GT_examSt", today);
			} else if (status == 4) {
				searchParams.put("LTE_examSt", today);
				searchParams.put("GT_examEnd", today);
			} else if (status == 5) {
				searchParams.put("LT_examEnd", today);
			}
		}
		searchParams.remove("EQ_status");

		Page<GjtExamPlanNew> pageInfo = gjtExamPlanNewService.queryAll(searchParams, pageRequst);
		Date now = new Date();
		for (GjtExamPlanNew examPlan : pageInfo) {
			if (examPlan.getExamBatchNew().getBookSt().compareTo(now) > 0) {
				examPlan.setStatus(1); // 未开始
			} else if (examPlan.getExamBatchNew().getBookEnd().compareTo(now) >= 0
					&& examPlan.getExamBatchNew().getBookSt().compareTo(now) <= 0) {
				examPlan.setStatus(2); // 预约中
			} else if (examPlan.getExamBatchNew().getBookEnd().compareTo(now) < 0 && examPlan.getExamSt() != null
					&& examPlan.getExamSt().compareTo(now) > 0) {
				examPlan.setStatus(3); // 待考试
			} else if (examPlan.getExamSt() != null && examPlan.getExamSt().compareTo(now) <= 0
					&& examPlan.getExamEnd() != null && examPlan.getExamEnd().compareTo(now) > 0) {
				examPlan.setStatus(4); // 考试中
			} else if (examPlan.getExamEnd() != null && examPlan.getExamEnd().compareTo(now) < 0) {
				examPlan.setStatus(5); // 已结束
			}
		}

		HSSFWorkbook workbook = gjtExamPlanNewService.exportByList(pageInfo.getContent());
		String fileName = "开考科目表.xls";
		response.setContentType("application/x-msdownload;charset=utf-8");
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
		workbook.write(response.getOutputStream());
	}

	/**
	 * 返回导入页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "importForm", method = RequestMethod.GET)
	public String importForm(Model model, HttpServletRequest request, String subjectType) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(user.getGjtOrg().getId()));
		model.addAttribute("subjectType", subjectType);
		return "edumanage/exam/exam_plan_import";
	}

	@RequestMapping(value = "import")
	@ResponseBody
	public ImportFeedback importPlans(@RequestParam("file") MultipartFile file, String subjectType,
			@RequestParam("examBatchCode") String examBatchCode, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

			String[] heads = { "开考科目名称", "试卷号", "考试课程", "课程代码", "考试形式", "考试方式", "形考比例", "考试专业", "培养层次", "专业代码",
					"考试开始时间", "考试结束时间", "考试预约方式", "考试预约最低分数限制（分）", "导入结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 2, heads.length - 1);
				// 校验标题
				String[] dataTitles = dataList.remove(0);
				for (int i = 0; i < heads.length - 1; i++) {
					if (!dataTitles[i].trim().equals(heads[i])) {
						return new ImportFeedback(false, "请下载正确表格模版填写");
					}
				}
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}

			@SuppressWarnings("unchecked")
			Map<Integer, String> examTypes = commonMapService.getExamTypeIntMap();
			Map<String, String> examStyleMap = commonMapService.getDates("EXAM_STYLE");
			List<CacheService.Value> trainingLevels = cacheService
					.getCachedDictionary(CacheService.DictionaryKey.TRAININGLEVEL);//// 层次
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if (dataList != null && dataList.size() > 0) {
				outer: for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据

					if (datas.length < heads.length - 1) {
						result[heads.length - 1] = "数据不全";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[0])) { // 开考科目名称
						result[heads.length - 1] = "开考科目名称不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[1])) { // 试卷号
						result[heads.length - 1] = "试卷号不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[2])) { // 考试课程
						result[heads.length - 1] = "考试课程不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[3])) { // 课程代码
						result[heads.length - 1] = "课程代码不能为空";
						failedList.add(result);
						continue;
					}

					int type = -1; // 考试形式
					Iterator<Entry<Integer, String>> iterator = examTypes.entrySet().iterator();
					while (iterator.hasNext()) {
						Entry<Integer, String> next = iterator.next();
						if (datas[4].equals(next.getValue())) {
							type = next.getKey();
							break;
						}
					}
					if (type == -1) {
						result[heads.length - 1] = "考试形式有误";
						failedList.add(result);
						continue;
					}

					String examStyle = ""; // 考试方式
					Iterator<Entry<String, String>> iterator2 = examStyleMap.entrySet().iterator();
					while (iterator2.hasNext()) {
						Entry<String, String> next = iterator2.next();
						if (datas[5].equals(next.getValue())) {
							examStyle = next.getKey();
							break;
						}
					}
					if ("".equals(examStyle)) {
						result[heads.length - 1] = "考试方式有误";
						failedList.add(result);
						continue;
					}

					if (EmptyUtils.isNotEmpty(ObjectUtils.toString(datas[6]).trim())) {
						try {
							Integer.parseInt(datas[6]); // 形考比例
						} catch (Exception e) {
							result[heads.length - 1] = "形考比例格式不正确，请填写数字";
							failedList.add(result);
							continue;
						}
					}

					try {
						df.parse(datas[10]); // 考试开始时间
					} catch (ParseException e) {
						result[heads.length - 1] = "考试开始时间格式不正确，日期格式应为 yyyy-MM-dd HH:mm";
						failedList.add(result);
						continue;
					}

					try {
						df.parse(datas[11]); // 考试结束时间
					} catch (ParseException e) {
						result[heads.length - 1] = "考试结束时间格式不正确，日期格式应为 yyyy-MM-dd HH:mm";
						failedList.add(result);
						continue;
					}

					if (!"个人预约".equals(datas[12]) && !"集体预约".equals(datas[12])) { // 考试预约方式
						result[heads.length - 1] = "考试预约方式有误";
						failedList.add(result);
						continue;
					}

					List<GjtCourse> gjtCourseList = new ArrayList<GjtCourse>();
					String[] courseCodes = datas[3].split(",");
					for (String courseCode : courseCodes) {
						List<GjtCourse> course = gjtCourseService.findByKchAndXxId(courseCode,
								user.getGjtOrg().getId());
						if (course != null && course.size() > 0) {
							gjtCourseList.addAll(course);
						} else {
							result[heads.length - 1] = "找不到课程号【" + courseCode + "】的课程";
							failedList.add(result);
							continue outer;
						}
					}

					List<GjtSpecialty> gjtSpecialtyList = new ArrayList<GjtSpecialty>();
					if (!"".equals(datas[9])) {
						String pyccName = datas[8];
						String pycc = "";
						for (CacheService.Value trainingLevel : trainingLevels) {
							if (ObjectUtils.toString(trainingLevel.getName()).equals(pyccName)) {
								pycc = ObjectUtils.toString(trainingLevel.getCode());
								break;
							}
						}
						if (EmptyUtils.isEmpty(pyccName) || EmptyUtils.isEmpty(pycc)) {
							result[heads.length - 1] = "培养层次不正确";
							failedList.add(result);
							continue outer;
						}
						String[] specialtyCodes = datas[9].split(",");
						for (String specialtyCode : specialtyCodes) {
							Map<String, Object> searchParams = new HashMap<String, Object>();
							searchParams.put("EQ_gjtSpecialtyBase.specialtyCode", specialtyCode);
							searchParams.put("EQ_pycc", pycc);
							searchParams.put("EQ_xxId", user.getGjtOrg().getId());
							List<GjtSpecialty> gjtSpecialty = gjtSpecialtyService.queryBy(searchParams, null);
							if (gjtSpecialty == null || gjtSpecialty.size() == 0) {
								result[heads.length - 1] = "找不到专业代码【" + specialtyCode + "】【" + pyccName + "】的专业";
								failedList.add(result);
								continue outer;
							} else {
								gjtSpecialtyList.addAll(gjtSpecialty);
							}
						}
					}

					/**
					 * 因为存在试卷号开考形式相同的开考科目，比如溢达就有单独的开考科目，所以按照课程ID查询开考科目
					 * gjtExamPlanNewService.
					 * findByExamBatchCodeAndExamNoAndTypeAndGjtCourseListCourseId
					 * 2018-04-04 不想处理上面说的问题了，太坑爹了 判断是否有初始化的开考科目，没有再获取对应的开考科目
					 */
					GjtExamPlanNew examPlanNew = gjtExamPlanNewService
							.findByExamBatchCodeAndExamNoAndType(examBatchCode, datas[1], -1);
					if (examPlanNew == null) {
						examPlanNew = gjtExamPlanNewService.findByExamBatchCodeAndExamNoAndType(examBatchCode, datas[1],
								type);
					}
					// System.out.println("111 " + examPlanNew.toString());
					if (examPlanNew == null) {
						examPlanNew = new GjtExamPlanNew();
						examPlanNew.setExamPlanName(datas[0]);
						examPlanNew.setExamNo(datas[1]);
						examPlanNew.setType(type);
						examPlanNew.setExamStyle(examStyle);
						if (EmptyUtils.isNotEmpty(ObjectUtils.toString(datas[6]).trim())) {
							examPlanNew.setXkPercent(Integer.parseInt(datas[6]));
						} else {
							examPlanNew.setXkPercent(null);
						}

						examPlanNew.setExamSt(df.parse(datas[10]));
						examPlanNew.setExamEnd(df.parse(datas[11]));

						if ("个人预约".equals(datas[12])) {
							examPlanNew.setExamPlanOrder("1");
						} else {
							examPlanNew.setExamPlanOrder("2");
						}

						examPlanNew.setExamPlanLimit(NumberUtils.toInt(datas[13]) + "");

						GjtExamBatchNew examBatchNew = gjtExamBatchNewService.queryByExamBatchCode(examBatchCode);
						examPlanNew.setExamBatchId(examBatchNew.getExamBatchId());
						examPlanNew.setExamBatchCode(examBatchCode);
						examPlanNew.setGradeId(examBatchNew.getGradeId());
						examPlanNew.setXxId(user.getGjtOrg().getId());

						examPlanNew.setGjtCourseList(gjtCourseList);
						if (gjtSpecialtyList != null && gjtSpecialtyList.size() > 0) {
							examPlanNew.setGjtSpecialtyList(gjtSpecialtyList);
						} else {
							examPlanNew.setGjtSpecialtyList(null);
						}

						examPlanNew.setStudyYearId(examBatchNew.getStudyYearId());
						examPlanNew.setSubjectType(Integer.valueOf(subjectType)); // 科目类型
						examPlanNew.setCreatedBy(user.getId());
						examPlanNew.setCreatedDt(new Date());
						gjtExamPlanNewService.insert(examPlanNew);
						gjtExamPlanNewService.insertExamPlanToTongyongSpecialty(examPlanNew.getExamPlanId());

						result[heads.length - 1] = "新增成功";
						successList.add(result);
					} else {
						/*
						 * //判断是否在考试开始时间前 if
						 * (EmptyUtils.isNotEmpty(examPlanNew.getExamSt()) &&
						 * examPlanNew.getExamSt().compareTo(new Date()) <= 0 &&
						 * examPlanNew.getExamEnd().compareTo(new Date()) > 0 )
						 * {// 当前时间在考试期间不可修改 result[heads.length - 1] =
						 * "考试已开始，不允许修改"; failedList.add(result); continue; }
						 * else {
						 */
						examPlanNew.setExamPlanName(datas[0]);
						examPlanNew.setExamNo(datas[1]);
						examPlanNew.setType(type);
						examPlanNew.setExamStyle(examStyle);
						examPlanNew.setXkPercent(NumberUtils.toInt(datas[6]));
						examPlanNew.setExamSt(df.parse(datas[10]));
						examPlanNew.setExamEnd(df.parse(datas[11]));
						examPlanNew.setUpdatedBy(user.getId());
						examPlanNew.setUpdatedDt(new Date());

						if ("个人预约".equals(datas[12])) {
							examPlanNew.setExamPlanOrder("1");
						} else {
							examPlanNew.setExamPlanOrder("2");
						}

						examPlanNew.setExamPlanLimit(NumberUtils.toInt(datas[13]) + "");

						/*
						 * // 开考科目支持追加课程，一对多门课程 for (GjtCourse c :
						 * gjtCourseList) { examPlanNew.addGjtCourseList(c); }
						 */
						// 按道理应该直接一把导入，因为一行是支持多门课程的
						examPlanNew.setGjtCourseList(gjtCourseList);
						if (gjtSpecialtyList != null && gjtSpecialtyList.size() > 0) {
							examPlanNew.setGjtSpecialtyList(gjtSpecialtyList);
						} else {
							examPlanNew.setGjtSpecialtyList(null);
						}

						gjtExamPlanNewService.update(examPlanNew);
						gjtExamPlanNewService.insertExamPlanToTongyongSpecialty(examPlanNew.getExamPlanId());

						result[heads.length - 1] = "修改成功";
						successList.add(result);
						/* } */
					}

				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "examPlan_success_" + currentTimeMillis + ".xls";
			String failedFileName = "examPlan_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "批量导入开考科目成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "批量导入开考科目失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
					+ "examPlan" + File.separator;
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

	@RequestMapping(value = "queryExamPlansByExamBatchCode", method = RequestMethod.GET)
	@ResponseBody
	public List<CommonSelect> queryExamPlansByExamBatchCode(String examBatchCode, HttpServletRequest request) {
		List<CommonSelect> list = new ArrayList<CommonSelect>();

		if (StringUtils.isNotBlank(examBatchCode)) {
			List<GjtExamPlanNew> plans = gjtExamPlanNewService.findByExamBatchCode(examBatchCode);
			for (GjtExamPlanNew plan : plans) {
				list.add(new CommonSelect(plan.getExamPlanId(), plan.getExamPlanName() + "(" + plan.getExamNo() + ")"));
			}
		}

		return list;
	}

	/**
	 * 自动生成开考科目
	 * 
	 * @return
	 */
	@RequestMapping(value = "autoExamPlan", method = RequestMethod.GET)
	public String autoExamPlan(Model model, HttpServletRequest request) {
		return "edumanage/exam/exam_plan_auto_create";
	}

	/**
	 * 自动生成开考科目
	 * 
	 * @return
	 */
	@RequestMapping(value = "autoExamPlan", method = RequestMethod.POST)
	@ResponseBody
	public Feedback autoExamPlan(HttpServletRequest request, HttpServletResponse response) {
		Feedback feedback = new Feedback(true, "生成成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String code = commonMapService.getCurrentGjtExamBatchNew(user.getGjtOrg().getId());
		boolean isUpdate = gjtExamPlanNewService.isUpdate(code);
		if (!isUpdate) {
			boolean flag = gjtExamPlanNewService.initAutoExamPlan(user.getGjtOrg().getId(), code);
			if (!flag) {
				feedback = new Feedback(false, "生成失败：服务器异常！");
			}
		} else {
			feedback = new Feedback(false, "生成失败：开考科目已被修改过！");
		}
		return feedback;
	}

	/**
	 * 前后端日期格式转换
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true)); // true:允许输入空值，false:不能为空值
	}
}
