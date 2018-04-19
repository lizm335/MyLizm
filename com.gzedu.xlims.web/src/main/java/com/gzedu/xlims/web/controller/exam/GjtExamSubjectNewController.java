package com.gzedu.xlims.web.controller.exam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
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
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.common.collect.Lists;
import com.gzedu.xlims.common.ToolUtil;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamSubjectNew;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.exam.GjtExamSubjectCourseService;
import com.gzedu.xlims.service.exam.GjtExamSubjectNewService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.common.UploadTmpFile;

@Controller
@RequestMapping("/exam/new/subject")
public class GjtExamSubjectNewController {

	private static final Log log = LogFactory.getLog(GjtExamSubjectNewController.class);

	@Autowired
	private GjtExamSubjectNewService gjtExamSubjectNewService;

	@Autowired
	private GjtCourseService gjtCourseService;
	
	@Autowired
	CommonMapService commonMapService;
	
	@Autowired
	GjtExamSubjectCourseService gjtExamSubjectCourseService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		/** 过渡版本代码 start */
		if (null == user || null != request.getParameter("userid")) {
			user = new GjtUserAccount();
			user.setId(request.getParameter("userid"));
			GjtOrg org = new GjtOrg();
			org.setId(request.getParameter("xxid"));
			user.setGjtOrg(org);
			request.getSession().setAttribute(WebConstants.CURRENT_USER, user);
		}
		/** 过渡版本代码 end */
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		if (null == searchParams || null == searchParams.get("EQ_type") || "".equals(searchParams.get("EQ_type").toString())) {
			searchParams.put("EQ_type", "1");
		}
		Page<GjtExamSubjectNew> pageInfo = gjtExamSubjectNewService.queryAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		List<GjtExamSubjectNew> list = Lists.newArrayList(pageInfo.iterator());
		List<String> subjectCode = new ArrayList<String>();
		List<String> subjectId = new ArrayList<String>();
		for (GjtExamSubjectNew gjtExamSubjectNew : list) {
			subjectCode.add(gjtExamSubjectNew.getSubjectCode());
			subjectId.add(gjtExamSubjectNew.getSubjectId());
		}
		Map<String, String> countMap = gjtExamSubjectNewService.plansCountBySubject(subjectCode);
		Map<String, String> courseCodeMap = gjtExamSubjectNewService.queryTeachPlanBySubject(subjectId);
		model.addAttribute("exam_type", searchParams.get("EQ_type"));
		model.addAttribute("countMap", countMap);
		model.addAttribute("courseCodeMap", courseCodeMap);
		model.addAttribute("user", user);
		model.addAttribute("pageInfo", pageInfo);
		
		
		return "edumanage/exam/exam_subject_list";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback add(HttpServletRequest request, @ModelAttribute GjtExamSubjectNew examSubject) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		/** 过渡版本代码 start */
		if (null == user) {
			user = new GjtUserAccount();
			user.setId(request.getParameter("userid"));
			GjtOrg org = new GjtOrg();
			org.setId(request.getParameter("xxid"));
			user.setGjtOrg(org);
			request.getSession().setAttribute(WebConstants.CURRENT_USER, user);
		}
		/** 过渡版本代码 end */
		Date now = new Date();
		examSubject.setCreatedDt(now);
		examSubject.setCreatedBy(user.getId());
		examSubject.setUpdatedDt(now);
		examSubject.setUpdatedBy(user.getId());
		examSubject.setXxId(user.getGjtOrg().getId());
		if (examSubject.getType() != WebConstants.EXAM_WK_TYPE 
				&& examSubject.getType() != WebConstants.EXAM_XK_TYPE
				&& examSubject.getType() != WebConstants.EXAM_WKS_TYPE) {
			examSubject.setStatus(3);
		} else {
			examSubject.setStatus(1);
		}

		if (gjtExamSubjectNewService.isExamSubjectExist(examSubject) != null) {
			feedback.setSuccessful(false);
			feedback.setMessage("该试卷号对应的考试科目已存在, 请勿重复创建");
		} else {
			// TODO: @micarol 各种非空字段判断
			examSubject = gjtExamSubjectNewService.insert(examSubject);
		}

		return feedback;
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(@RequestParam String ids, HttpServletRequest request) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		/** 过渡版本代码 start */
		if (null == user) {
			user = new GjtUserAccount();
			user.setId(request.getParameter("userid"));
			GjtOrg org = new GjtOrg();
			org.setId(request.getParameter("xxid"));
			user.setGjtOrg(org);
			request.getSession().setAttribute(WebConstants.CURRENT_USER, user);
		}
		/** 过渡版本代码 end */
		Feedback feedback = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(ids)) {
			try {
				int rs = gjtExamSubjectNewService.delete(Arrays.asList(ids.split(",")), user.getGjtOrg().getId());
				if (rs == 0) {
					feedback.setSuccessful(false);
					feedback.setMessage("删除失败, 只能删除未同步数据.");
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				feedback.setSuccessful(false);
				feedback.setMessage("删除失败, 数据库异常");
			}
		}
		return feedback;
	}

	/**
	 * 单条考试科目表单, 新建 更新 查看均是本方法跳转到页面
	 * 
	 * @param id
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "{op}/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable String op, @PathVariable String id,
			@RequestParam(value = "examType", defaultValue = "2") int examType, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		/** 过渡版本代码 start */
		if (null == user) {
			user = new GjtUserAccount();
			user.setId(request.getParameter("userid"));
			GjtOrg org = new GjtOrg();
			org.setId(request.getParameter("xxid"));
			user.setGjtOrg(org);
			request.getSession().setAttribute(WebConstants.CURRENT_USER, user);
		}
		/** 过渡版本代码 end */
		GjtExamSubjectNew examSubject = new GjtExamSubjectNew();
		if ("create".equals(op)) {
			examSubject.setSubjectCode(gjtExamSubjectNewService.getSubjectCode(examType));
			examSubject.setType(examType);
		} else {
			examSubject = gjtExamSubjectNewService.queryBy(id);
			model.addAttribute("entity", examSubject);
		}
		
		List<GjtCourse> courseList = gjtCourseService.findByXxidAndIsDeleted(user.getGjtOrg().getId(), "N");
		model.addAttribute("courseList", courseList);

		/*List<Map<String, String>> plans = gjtExamSubjectNewService.findTeachPlanByXxid(user.getGjtOrg().getId(), WebConstants.examTypeMap.get(examType));
		model.addAttribute("plansList", plans);*/

		model.addAttribute("entity", examSubject);
		model.addAttribute("action", op);
		return "edumanage/exam/exam_subject_form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(@ModelAttribute GjtExamSubjectNew entity, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		/** 过渡版本代码 start */
		if (null == user) {
			user = new GjtUserAccount();
			user.setId(request.getParameter("userid"));
			GjtOrg org = new GjtOrg();
			org.setId(request.getParameter("xxid"));
			user.setGjtOrg(org);
			request.getSession().setAttribute(WebConstants.CURRENT_USER, user);
		}
		/** 过渡版本代码 end */
		GjtExamSubjectNew examSubject = gjtExamSubjectNewService.queryBy(entity.getSubjectId());
		examSubject.setUpdatedBy(user.getId());
		examSubject.setName(entity.getName());
		examSubject.setMemo(entity.getMemo());
		examSubject.setUpdatedBy(user.getId());

		if (null == gjtExamSubjectNewService.update(examSubject)) {
			feedback.setSuccessful(false);
			feedback.setMessage("更新失败");
		}
		return feedback;
	}

	/**
	 * 导入
	 * 
	 * @param request
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public void importSubjects(HttpServletRequest request, HttpServletResponse response, @RequestParam int examType,
			@RequestParam("file") CommonsMultipartFile file) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		/** 过渡版本代码 start */
		if (null == user) {
			user = new GjtUserAccount();
			user.setId(request.getParameter("userid"));
			GjtOrg org = new GjtOrg();
			org.setId(request.getParameter("xxid"));
			user.setGjtOrg(org);
			request.getSession().setAttribute(WebConstants.CURRENT_USER, user);
		}
		/** 过渡版本代码 end */
		List<String> filePaths = UploadTmpFile.uploadSimple(request);// TODO:
																		// @micarol
																		// 待优化
		if (null != filePaths && filePaths.size() > 0) {
			Workbook wb = gjtExamSubjectNewService.importGjtExamSubjectNew(filePaths.get(0), examType, user);
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String("考试科目导入结果.xls".getBytes("UTF-8"), "ISO8859-1"));
			wb.write(response.getOutputStream());
		}
	}

	/**
	 * 导出未创建考试科目的课程
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/export", method = RequestMethod.POST)
	@ResponseBody
	public void exportSubjects(@RequestParam int examType, HttpServletRequest request, HttpServletResponse response) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		/** 过渡版本代码 start */
		if (null == user) {
			user = new GjtUserAccount();
			user.setId(request.getParameter("userid"));
			GjtOrg org = new GjtOrg();
			org.setId(request.getParameter("xxid"));
			user.setGjtOrg(org);
			request.getSession().setAttribute(WebConstants.CURRENT_USER, user);
		}
		/** 过渡版本代码 end */
		 List<GjtCourse> courseList = gjtExamSubjectNewService.noSubjectCourseList(user.getGjtOrg().getId());
		 HSSFWorkbook workbook = gjtExamSubjectNewService.exportNoSubjectCourseList(courseList);

		// 考试科目数据结构变更...
		//List<Map<String, String>> planList = gjtExamSubjectNewService.noSubjectPlanList(user.getGjtOrg().getId(), WebConstants.examTypeMap.get(examType));
		//HSSFWorkbook workbook = gjtExamSubjectNewService.exportNoSubjectPlanList(planList);
		response.setContentType("application/x-msdownload;charset=utf-8");
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String("未创建考试科目课程表.xls".getBytes("UTF-8"), "ISO8859-1"));
		workbook.write(response.getOutputStream());

	}
	
	/**
	 * 导出未创建考试科目的课程
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "expCourseSubject")
	@ResponseBody
	public void expCourseSubject(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map formMap = Servlets.getParametersStartingWith(request, "");
		formMap.put("XX_ID", ObjectUtils.toString(user.getGjtOrg().getId()));
		String execleFileName = gjtExamSubjectNewService.expCourseSubject(formMap, request, response);
		ToolUtil.download(execleFileName, response);
	}
	
	/**
	 * 导入考试科目
	 * @return
	 */
	@RequestMapping(value = "getSubjectImport")
	public String getSubjectImport(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		model.addAttribute("EXAM_TYPE", ObjectUtils.toString(formMap.get("EXAM_TYPE")));
		return "edumanage/exam/subject/exam_subject_import";
	}
	
	/**
	 * 导入考试科目
	 * 
	 * @param request
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "importCourseSubject")
	public String importCourseSubject(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") CommonsMultipartFile file, Model model) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<String> filePaths = UploadTmpFile.uploadSimple(request);
		Map formMap = Servlets.getParametersStartingWith(request, "");
		formMap.put("XX_ID", user.getGjtOrg().getId());
		formMap.put("USER_ID", user.getId());
		Map resultMap = gjtExamSubjectNewService.importCourseSubject(filePaths.get(0), formMap, request, response);
		model.addAttribute("resultMap", resultMap);
		return "edumanage/exam/subject/exam_subject_import_result";
	}

	/**
	 * 下载成功失败记录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "downSubjectResult")
	@ResponseBody
	public void downSubjectResult(HttpServletRequest request, HttpServletResponse response, String execleFileName) {
		ToolUtil.download(execleFileName, response);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/getGjtCourseList")
	public String getGjtCourseList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		//层次
		Map pyccMap = commonMapService.getPyccMap();
		Map courseTypeMap = commonMapService.getCourseTypeMap();
		
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = gjtExamSubjectCourseService.getGjtCourseList(searchParams, pageRequest);
		
		model.addAttribute("subjectCode", ObjectUtils.toString(searchParams.get("COURSE_IDS")));
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("courseTypeMap", courseTypeMap);
		model.addAttribute("pyccMap", pyccMap);

		return "edumanage/exam/exam_subject_course_list";
	}
	
	/**
	 * 保存考试科目与课程数据
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/saveSubject")
	@ResponseBody
	public Feedback saveSubject(Model model,HttpServletRequest request,HttpSession session){
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		searchParams.put("CREATED_BY", user.getId());
		log.info("参数："+searchParams.toString());
		
//		Map resultMap = gjtExamSubjectCourseService.saveExamSubjectCourse(searchParams);
		Map result = gjtExamSubjectCourseService.getIsExistExamNo(searchParams);
		if("0".equals(ObjectUtils.toString(result.get("TEMP")))){
			gjtExamSubjectCourseService.saveExamSubjectCourse(searchParams);
		}else{
			feedback.setSuccessful(false);
			feedback.setMessage("该试卷号对应的考试科目已存在, 请勿重复创建");
		}
		
		return feedback;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/getIsExistExamNo")
	@ResponseBody
	public Map<String, Object> getIsExistExamNo(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		log.info("参数："+searchParams.toString());
		
		Map resultMap = gjtExamSubjectCourseService.getIsExistExamNo(searchParams);
		
		return resultMap;
	}
	
	
	/**
	 * 导出未创建考试科目的课程目录
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "expExamSubjectCourse")
	@ResponseBody
	public void expExamSubjectCourse(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", ObjectUtils.toString(user.getGjtOrg().getId()));
		searchParams.put("SUBJECT_TYPE", ObjectUtils.toString(searchParams.get("EXAM_TYPE")));
		String execleFileName = gjtExamSubjectCourseService.exportExamSubjectCourse(searchParams, request, response);
		ToolUtil.download(execleFileName, response);
	}
	
	/**
	 * 导入考试科目
	 * 
	 * @param request
	 * @param file
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "importExamCourseSubject")
	public String importExamCourseSubject(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") CommonsMultipartFile file, Model model) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<String> filePaths = UploadTmpFile.uploadSimple(request);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		searchParams.put("USER_ID", user.getId());
		Map resultMap = gjtExamSubjectCourseService.importCourseSubject(filePaths.get(0), searchParams, request, response);
		model.addAttribute("resultMap", resultMap);
		return "edumanage/exam/subject/exam_subject_import_result";
	}
	
}
