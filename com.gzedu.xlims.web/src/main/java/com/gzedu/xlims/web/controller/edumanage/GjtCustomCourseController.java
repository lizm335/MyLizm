package com.gzedu.xlims.web.controller.edumanage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.dao.Collections3;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.pojo.GjtAuditOperateLine;
import com.gzedu.xlims.pojo.GjtCustomCourse;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtGrantCourseCertificate;
import com.gzedu.xlims.pojo.GjtGrantCoursePlan;
import com.gzedu.xlims.pojo.GjtGrantCourseScore;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.comm.OperateType;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtCustomCourseService;
import com.gzedu.xlims.service.edumanage.GjtGrantCourseCertificateService;
import com.gzedu.xlims.service.edumanage.GjtGrantCoursePlanService;
import com.gzedu.xlims.service.edumanage.GjtGrantCourseScoreService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 定制课程
 * @author lyj
 * @time 2017年7月24日 
 */
@Controller
@RequestMapping("/edumanage/custom/course")
public class GjtCustomCourseController {
	
	@Autowired
	private GjtOrgService gjtOrgService;
	@Autowired
	private GjtTeachPlanService gjtTeachPlanService;
	@Autowired
	private CommonMapService commonMapService;
	@Autowired
	private GjtCustomCourseService gjtCustomCourseService;
	@Autowired
	private GjtGrantCoursePlanService gjtGrantCoursePlanService;
	@Autowired
	private GjtGrantCourseCertificateService gjtGrantCourseCertificateService;
	@Autowired
	private GjtGrantCourseScoreService gjtGrantCourseScoreService;
	@Autowired
	private GjtGradeService gjtGradeService;
	
	
	/**
	 * 课程定制列表
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(HttpServletRequest request,@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize) {
		String gradeId = request.getParameter("gradeId");// 开课学期
		String courseName = request.getParameter("courseName");// 学院课程
		String replaceCourseName = request.getParameter("replaceCourseName");// 替换课程
		String planStatus = request.getParameter("planStatus");// 授课计划状态
		String certificateStatus = request.getParameter("certificateStatus");// 授课凭证状态
		String scoreStatus = request.getParameter("scoreStatus");// 授课成绩状态
		
		Map<String, Object> params = Maps.newHashMap();
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		// 1. 查询机构
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<String> orgIds = gjtOrgService.queryByParentId(user.getGjtOrg().getId());
		// 2.查询教学计划
		Page<Map<String, Object>> pageInfo = null;
		if(Collections3.isNotEmpty(orgIds)) {
			// 3. 查询定制课程
			params = Maps.newHashMap();
			params.put("orgIds", orgIds);
			if(StringUtils.isNotEmpty(gradeId)) {
				params.put("gradeId", gradeId);
			}
			if(StringUtils.isNotEmpty(courseName)) {
				params.put("courseName", courseName);
			}
			if(StringUtils.isNotEmpty(replaceCourseName)) {
				params.put("replaceCourseName", replaceCourseName);
			}
			if(StringUtils.isNotEmpty(planStatus)) {
				params.put("planStatus", planStatus);
			}
			if(StringUtils.isNotEmpty(certificateStatus)) {
				params.put("certificateStatus", certificateStatus);
			}
			if(StringUtils.isNotEmpty(scoreStatus)) {
				params.put("scoreStatus", scoreStatus);
			}
			pageInfo = gjtCustomCourseService.queryAll(params,pageRequst);
		}
		request.setAttribute("pageInfo", pageInfo);
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		request.setAttribute("gradeMap", gradeMap);
		return "/edumanage/customCourse/course-list";
	}
	
	@RequestMapping("/toPlan/{orgId}/{teachPlanId}")
	public String toPlan(@PathVariable("orgId") String orgId, @PathVariable("teachPlanId") String teachPlanId,
			HttpServletRequest request) {
		GjtCustomCourse customCourse = gjtCustomCourseService.findByTeachPlanIdAndOrgId(teachPlanId,orgId);
		GjtTeachPlan teachPlan = gjtTeachPlanService.findOne(teachPlanId);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (customCourse == null) {
			customCourse = new GjtCustomCourse();
			customCourse.setCustomCourseId(UUIDUtils.random());
			customCourse.setOrgId(orgId);
			customCourse.setTeachPlanId(teachPlanId);
			customCourse.setPlanStatus(GjtCustomCourse.NOT_UPLOAD);// 授课计划未上传
			customCourse.setCertificateStatus(GjtCustomCourse.NOT_UPLOAD); // 授课凭证未上传
			customCourse.setScoreStatus(GjtCustomCourse.NOT_UPLOAD);// 成绩未上传
			customCourse.setCreatedBy(user.getLoginAccount());
			customCourse.setCreatedDt(new Date());
			gjtCustomCourseService.save(customCourse);
		}
		request.setAttribute("teachPlan", teachPlan);
		request.setAttribute("customCourse", customCourse);
		return "/edumanage/customCourse/grant-course-plan";
	}
	
	/**
	 * 授课计划保存
	 * @return
	 */
	@RequestMapping(value="/createPlan",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String createPlan(GjtCustomCourse customCourse,HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<GjtGrantCoursePlan> planList = customCourse.getPlans();
		if(customCourse.getPlans() != null) {
			for(GjtGrantCoursePlan plan : planList) {
				try {
					plan.setStartDate(DateUtils.getTime(plan.getStartDateStr()));
					plan.setEndDate(DateUtils.getTime(plan.getEndDateStr()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				plan.setGrantCoursePlanId(UUIDUtils.random());
				plan.setCertificateStatus(GjtCustomCourse.NOT_UPLOAD);// 授课计划->授课凭证未上传
				plan.setCreatedBy(user.getLoginAccount());
				plan.setCreatedDt(new Date());
			}
			if(Collections3.isNotEmpty(planList)) {
				GjtCustomCourse courseDb = gjtCustomCourseService.findOne(customCourse.getCustomCourseId());
				// 删除旧授课计划
				if(Collections3.isNotEmpty(courseDb.getPlans())) {
					gjtGrantCoursePlanService.delete(courseDb.getPlans());
				}
				courseDb.setPlans(planList);
				courseDb.setPlanStatus(GjtCustomCourse.AUDIT_WAIT);
				
				// 记录发布授课计划操作
				GjtAuditOperateLine line = buildGjtAuditOperateLine(user, "发布授课计划", OperateType.GRANT_COURSE_PLAN_AUDIT);
				// 记录审核
				if(courseDb.getPlanAuditLines() != null) {
					courseDb.getPlanAuditLines().add(line);
				} else {
					List<GjtAuditOperateLine> auditLineList = Lists.newArrayList();
					courseDb.setPlanAuditLines(auditLineList);
					auditLineList.add(line);
				}
				gjtCustomCourseService.save(courseDb);
				
			}
		}
		return "redirect:/edumanage/custom/course/toPlan/" + customCourse.getTeachPlanId();
	}
	
	/**
	 * 授课凭证
	 * @param customCourseId
	 * @param request
	 * @return
	 */
	@RequestMapping("/toCertificate/{customCourseId}")
	public String toCertificate(@PathVariable("customCourseId") String customCourseId,HttpServletRequest request) {
		GjtCustomCourse customCourse = gjtCustomCourseService.findOne(customCourseId);
		if(customCourse == null || GjtCustomCourse.AUDIT_PASS != customCourse.getPlanStatus()) {// 2：审核通过，只有审核通过才能进到授课凭证
			return "redirect:/edumanage/custom/course/toPlan/"+customCourse.getOrgId()+"/"+customCourse.getTeachPlanId();
		}
		GjtTeachPlan teachPlan = gjtTeachPlanService.findOne(customCourse.getTeachPlanId());
		request.setAttribute("teachPlan", teachPlan);
		request.setAttribute("customCourse", customCourse);
		return "/edumanage/customCourse/grant-course-certificate";
	} 
	/**
	 * 进入上传授课凭证
	 * @return
	 */
	@RequestMapping("/toUploadCertificate/{grantCoursePlanId}")
	public String toUploadCertificate(@PathVariable("grantCoursePlanId")String grantCoursePlanId,HttpServletRequest request) {
		GjtGrantCoursePlan plan = gjtGrantCoursePlanService.findOne(grantCoursePlanId);
		GjtCustomCourse customCourse = gjtCustomCourseService.findOne(plan.getCustomCourseId());
		request.setAttribute("plan", plan);
		request.setAttribute("customCourse", customCourse);
		return "/edumanage/customCourse/upload-certificate";
	}
	
	@RequestMapping("/upload")
	public String upload(String id,String type,HttpServletRequest request) {
		if("certificate".equals(type)) {
			request.setAttribute("uploadUrl", "/edumanage/custom/course/uploadCertificate?grantCoursePlanId="+id);
			request.setAttribute("template", "授课凭证导入模板.xls");
		}
		if("score".equals(type)) {
			request.setAttribute("uploadUrl", "/edumanage/custom/course/uploadScore?customCourseId="+id);
			request.setAttribute("template", "授课成绩导入模板.xls");
		}
		return "/edumanage/customCourse/upload";
	}
	
	
	@RequestMapping("/uploadCertificate")
	@ResponseBody
	public ImportFeedback uploadCertificate(String grantCoursePlanId, @RequestParam("file") MultipartFile file,
			HttpServletRequest request) throws InvalidFormatException, IOException {
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        
		List<String[]> dataList = null;
		try {
			dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, 3);
		} catch (Exception e) {
			return new ImportFeedback(false, "请下载正确表格模版填写");
		}
        Set<GjtGrantCourseCertificate> certificates = Sets.newHashSet();
        List<String[]> successList = new ArrayList<String[]>();
		List<String[]> failedList = new ArrayList<String[]>();
        if (dataList != null && dataList.size() > 0) {
			for (String[] datas : dataList) {
				String[] result = new String[4]; // 记录导入结果
				System.arraycopy(datas, 0, result, 0, Math.min(datas.length, 3)); // 先拷贝数据
				try {
					String studentNo = datas[0]; // 学员
					String studentName = datas[1];// 姓名
					if(StringUtils.isEmpty(studentNo) || StringUtils.isEmpty(studentName)) {
						throw new Exception("学员/姓名不能为空");
					}
					int signStatus = Integer.parseInt(datas[2].trim()); // 签到状态(1:已签到，0：未签到 )
					GjtGrantCourseCertificate c = new GjtGrantCourseCertificate(studentName, studentNo, signStatus);
					c.setGrantCourseCertificateId(UUIDUtils.random());
					c.setCreatedBy(user.getLoginAccount());
					certificates.add(c);
					successList.add(result);
				} catch (Exception e) {
					result[3] = e.getMessage();
					failedList.add(result);
					e.printStackTrace();
				}
			}
			if(Collections3.isNotEmpty(certificates)) {
				GjtGrantCoursePlan plan = gjtGrantCoursePlanService.findOne(grantCoursePlanId);
				// 删除旧凭证
				if(Collections3.isNotEmpty(plan.getCertificates())) {
					gjtGrantCourseCertificateService.remove(plan.getCertificates());
				}
				plan.setCertificates(certificates);
				//plan.setFileName(fileName);
				//plan.setFileUrl(fileUrl);
				plan.setUpdatedBy(user.getLoginAccount());
				plan.setUpdatedDt(new Date());
				updateGjtGrantCoursePlan(plan,buildGjtAuditOperateLine(user, "上传授课凭证", OperateType.GRANT_COURSE_SCORE_AUDIT));
				//gjtGrantCoursePlanService.save(plan);
			}
			
			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "custom_course_success_" + currentTimeMillis + ".xls";
			String failedFileName = "custom_course_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(new String[]{"学号","姓名","签到状态(0:未签，1:已签到)"}, successList, "教职工导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(new String[]{"学号","姓名","签到状态(0:未签，1:已签到)"}, failedList, "教职工导入失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
					+ "custom" + File.separator;
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
        }
        return null;
	}
	
	@RequestMapping("/uploadCertificateImage")
	@ResponseBody
	public String uploadCertificateImage(String grantCoursePlanId, String[] images,
			HttpServletRequest request) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtGrantCoursePlan plan = gjtGrantCoursePlanService.findOne(grantCoursePlanId);
		if(images != null) {
			Set<String> imageSet = Sets.newHashSet(images);
//			if(plan.getImages() != null) {
//				plan.getImages().addAll(imageSet);
//			} else {
				plan.setImages(imageSet);
//			}
			plan.setUpdatedBy(user.getLoginAccount());
			plan.setUpdatedDt(new Date());
			
			updateGjtGrantCoursePlan(plan,buildGjtAuditOperateLine(user, "上传授课凭证扫描件", OperateType.GRANT_COURSE_SCORE_AUDIT));
			//gjtGrantCoursePlanService.save(plan);
		}
		return "success";
	}
	
	private void updateGjtGrantCoursePlan(GjtGrantCoursePlan plan,GjtAuditOperateLine line) {
		if(Collections3.isNotEmpty(plan.getImages()) && 
				Collections3.isNotEmpty(plan.getCertificates())) {
			plan.setCertificateStatus(GjtCustomCourse.ALREADY_UPLOADED); 
		} else {
			plan.setCertificateStatus(GjtCustomCourse.UPLOADING);
		}
		GjtCustomCourse customCourse = gjtCustomCourseService.findOne(plan.getCustomCourseId());
		// 记录审核
		/*if(line.getOperateType() == OperateType.GRANT_COURSE_PLAN_AUDIT) {
			if(Collections3.isEmpty(customCourse.getPlanAuditLines())){
				customCourse.setPlanAuditLines(new ArrayList<GjtAuditOperateLine>());
			}
			customCourse.getPlanAuditLines().add(line);
		}
		if(line.getOperateType() == OperateType.GRANT_COURSE_SCORE_AUDIT) {
			if(Collections3.isEmpty(customCourse.getScoreAuditLines())){
				customCourse.setScoreAuditLines(new ArrayList<GjtAuditOperateLine>());
			}
			customCourse.getScoreAuditLines().add(line);
		}*/
		boolean b = false;
		for(GjtGrantCoursePlan p : customCourse.getPlans()) {
			if(GjtCustomCourse.ALREADY_UPLOADED == p.getCertificateStatus()) {
				b = true;
			} else {
				b = false;
				break;
			}
		}
		if(b) { // 已上传：如果授课计划全部都已经上传授课凭证 ,则把定制课程的授课凭证状态已上传
			customCourse.setCertificateStatus(GjtCustomCourse.ALREADY_UPLOADED);
		} else {
			customCourse.setCertificateStatus(GjtCustomCourse.UPLOADING);
		}
		gjtCustomCourseService.save(customCourse);
	}

	@RequestMapping("/toScore/{customCourseId}")
	public String toScore(@PathVariable("customCourseId") String customCourseId ,HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtCustomCourse customCourse = gjtCustomCourseService.findOne(customCourseId);
		if(customCourse.getCertificateStatus() != GjtCustomCourse.ALREADY_UPLOADED) {
			// 上传成绩前，先要上传授课凭证
			return "/edumanage/custom/course/toCertificate/"+customCourseId;
		}
		GjtTeachPlan teachPlan = gjtTeachPlanService.findOne(customCourse.getTeachPlanId());
		request.setAttribute("teachPlan", teachPlan);
		request.setAttribute("customCourse", customCourse);
		// 成绩上传开始时间：（教学管理->学期管理->学期时间/课程教学周期）
		GjtGrade grade = gjtGradeService.queryById(teachPlan.getActualGradeId());
		// 成绩上传截止时间:
		//GjtExamBatchNew batchNew = gjtExamBatchNewService.findByGradeIdAndXxId(grade.getGradeId(), user.getGjtOrg().getId());
		Date startDate = null,endDate = null;
		if(grade != null) {
			startDate = grade.getCourseEndDate();
			//endDate = grade.getUpAchievementDate();
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, 30);
			endDate = c.getTime();
		}
		/*if(batchNew != null) {
			endDate = batchNew.getRecordEnd();
		}*/
		
		Date d = new Date();
		if(startDate != null && endDate != null && d.after(startDate) && d.before(endDate)) {
			request.setAttribute("isUploadScore", true);
		} else {
			request.setAttribute("isUploadScore", false);
		}
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		return "/edumanage/customCourse/grant-course-score";
	}
	
	@RequestMapping("/toUploadScore/{customCourseId}")
	public String toUploadScore(@PathVariable("customCourseId") String customCourseId,HttpServletRequest request) {
		GjtCustomCourse customCourse = gjtCustomCourseService.findOne(customCourseId);
		GjtTeachPlan teachPlan = gjtTeachPlanService.findOne(customCourse.getTeachPlanId());
		request.setAttribute("teachPlan", teachPlan);
		request.setAttribute("customCourse", customCourse);
		return "/edumanage/customCourse/upload-score";
	}
	
	@RequestMapping("/uploadScore" )
	@ResponseBody
	public ImportFeedback uploadScore(String customCourseId,@RequestParam("file") MultipartFile file,  HttpServletRequest request)
			throws MalformedURLException, IOException, InvalidFormatException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		List<String[]> dataList = null;
		try {
			dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, 4);
		} catch (Exception e) {
			return new ImportFeedback(false, "请下载正确表格模版填写");
		}
		Set<GjtGrantCourseScore> scores = Sets.newHashSet();
		List<String[]> successList = new ArrayList<String[]>();
		List<String[]> failedList = new ArrayList<String[]>();
		if (dataList != null && dataList.size() > 0) {
			for (String[] datas : dataList) {
				String[] result = new String[5]; // 记录导入结果
				System.arraycopy(datas, 0, result, 0, Math.min(datas.length, 4)); // 先拷贝数据
				GjtGrantCourseScore c = null;
				try {
					String studentNo = datas[0]; // 学员
					String studentName = datas[1];// 姓名
					if(StringUtils.isEmpty(studentNo) || StringUtils.isEmpty(studentName)) {
						throw new Exception("学员/姓名不能为空");
					}
					c = new GjtGrantCourseScore(studentName, studentNo, null, null);
					c.setId(UUIDUtils.random());
					c.setCreatedBy(user.getLoginAccount());
					int usualScore = Integer.parseInt(datas[2].trim()); // 平时成绩
					int terminalScore = Integer.parseInt(datas[3].trim()); // 期末成绩
					c.setUsualScore(usualScore);
					c.setTerminalScore(terminalScore);
					successList.add(result);
				} catch (Exception e) {
					result[3] = e.getMessage();
					failedList.add(result);
					e.printStackTrace();
				}
				scores.add(c);
			}
			if (Collections3.isNotEmpty(scores)) {
				GjtCustomCourse customCourse = gjtCustomCourseService.findOne(customCourseId);
				// TODO 删除旧成绩数据，防止数据冗余
				if(Collections3.isNotEmpty(customCourse.getScores())) {
					gjtGrantCourseScoreService.delete(customCourse.getScores());
				}
				customCourse.setScores(scores);
				//customCourse.setScoreFileUrl(fileUrl);
				customCourse.setUpdatedBy(user.getLoginAccount());
				customCourse.setUpdatedDt(new Date());
				customCourse.setScoreStatus(GjtCustomCourse.AUDIT_UPLOADING);
				gjtCustomCourseService.save(customCourse);
				//updateGjtCustomCourse(customCourse,buildGjtAuditOperateLine(user, "上传成绩", OperateType.GRANT_COURSE_SCORE_AUDIT));
			}
			
			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "custom_course_success_" + currentTimeMillis + ".xls";
			String failedFileName = "custom_course_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(null, successList, "教职工导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(null, failedList, "教职工导入失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
					+ "custom" + File.separator;
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
		}
		//return "上传成功!";
		return null;
	}
	@RequestMapping("/uploadScoreImage")
	@ResponseBody
	public String uploadScoreImage(String customCourseId, String[] images,
			HttpServletRequest request) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtCustomCourse customCourse = gjtCustomCourseService.findOne(customCourseId);
		if(images != null) {
			Set<String> imageSet = Sets.newHashSet(images);
			if (Collections3.isNotEmpty(imageSet)) {
				// 成绩扫描件
				if(customCourse.getImages() != null) {
					customCourse.getImages().addAll(imageSet);
				} else {
					customCourse.setImages(imageSet);
				}
				customCourse.setUpdatedBy(user.getLoginAccount());
				customCourse.setUpdatedDt(new Date());
				customCourse.setScoreStatus(GjtCustomCourse.AUDIT_UPLOADING);
				gjtCustomCourseService.save(customCourse);
				// updateGjtCustomCourse(customCourse,buildGjtAuditOperateLine(user, "上传成绩扫描件", OperateType.GRANT_COURSE_SCORE_AUDIT));
			}
		}
		return "上传成功!";
	}
	
	@RequestMapping("/removeScoreImage")
	@ResponseBody
	public String removeScoreImage(String customCourseId, String imgSrc,
			HttpServletRequest request) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtCustomCourse customCourse = gjtCustomCourseService.findOne(customCourseId);
		if(StringUtils.isNotEmpty(imgSrc)) {
			// 成绩扫描件
			if(customCourse.getImages() != null) {
				customCourse.getImages().remove(imgSrc);
			}
			customCourse.setUpdatedBy(user.getLoginAccount());
			customCourse.setUpdatedDt(new Date());
			gjtCustomCourseService.save(customCourse);
			//updateGjtCustomCourse(customCourse,buildGjtAuditOperateLine(user, "上传成绩扫描件", OperateType.GRANT_COURSE_SCORE_AUDIT));
		}
		return "上传成功!";
	}
	@RequestMapping("/submitAudit")
	@ResponseBody
	public String submitAudit(String customCourseId,
			HttpServletRequest request) {
		GjtCustomCourse customCourse = gjtCustomCourseService.findOne(customCourseId);
		if(customCourse.getScoreStatus() == GjtCustomCourse.AUDIT_UPLOADING &&
				customCourse.getImages() != null && customCourse.getImages().size() > 0 &&
				customCourse.getScores() != null && customCourse.getScores().size() > 0) {
			customCourse.setScoreStatus(GjtCustomCourse.AUDIT_WAIT);
			// 记录操作
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtAuditOperateLine line = buildGjtAuditOperateLine(user, "提交审核", OperateType.GRANT_COURSE_SCORE_AUDIT);
			if(customCourse.getScoreAuditLines() == null) {
				customCourse.setScoreAuditLines(new ArrayList<GjtAuditOperateLine>());
			}
			customCourse.getScoreAuditLines().add(line);
			gjtCustomCourseService.save(customCourse);
			return "success";
		} else {
			return "提交失败：请先上传成绩表和成绩扫描件!";
		}
	}
	
	private GjtAuditOperateLine buildGjtAuditOperateLine(GjtUserAccount user,String operate,OperateType operateType) {
		GjtAuditOperateLine line = new GjtAuditOperateLine();
		line.setAuditOperateLineId(UUIDUtils.random());
		line.setOperate(operate);
		line.setOperateType(operateType);
		line.setOperatorRole(user.getPriRoleInfo().getRoleName());
		line.setOperator(user.getLoginAccount());
		line.setOperatorName(user.getRealName());
		line.setOperateTime(new Date());
		return line;
	}
	
}
