package com.gzedu.xlims.web.controller.exam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
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

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.MapKit;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.edumanage.TeachPlanDao;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtRecResult;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.pojo.exam.GjtExamCost;
import com.gzedu.xlims.pojo.exam.GjtExamPointAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamPointNew;
import com.gzedu.xlims.pojo.exam.ViewExamPlanSc;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.exam.GjtExamAppointmentNewService;
import com.gzedu.xlims.service.exam.GjtExamBatchNewService;
import com.gzedu.xlims.service.exam.GjtExamCostService;
import com.gzedu.xlims.service.exam.GjtExamPointAppointmentNewService;
import com.gzedu.xlims.service.exam.GjtExamPointNewService;
import com.gzedu.xlims.service.exam.ViewExamPlanScService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;
import com.ouchgzee.study.service.exam.ExamServeService;

/**
 * 考试预约
 */
@Controller
@RequestMapping("/exam/new/appointment")
public class GjtExamAppointmentController extends BaseController {
	
	private final static Logger log = LoggerFactory.getLogger(GjtExamAppointmentController.class);

	@Autowired
	private GjtExamPointNewService gjtExamPointNewService;
	
	@Autowired
	private GjtExamBatchNewService gjtExamBatchNewService;

	@Autowired
	private GjtExamAppointmentNewService gjtExamAppointmentNewService;

	@Autowired
	private GjtExamPointAppointmentNewService gjtExamPointAppointmentNewService;
	
	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtTeachPlanService gjtTeachPlanService;

	@Autowired
	private GjtRecResultService gjtRecResultService;

	@Autowired
	private GjtExamCostService gjtExamCostService;
	
	@Autowired
	private  GjtCourseService gjtCourseService;

	@Autowired
	private TeachPlanDao teachPlanDao;

	@Autowired
	private ViewExamPlanScService viewExamPlanScService;

	@Autowired
	ExamServeService examServeService;
		
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String schoolId= user.getGjtOrg().getId();
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
        // 默认选择当前期(批次)
        if(EmptyUtils.isEmpty(searchParams)|| StringUtils.isBlank((String) searchParams.get("EQ_examPlanNew.examBatchCode"))){
            String code = commonMapService.getCurrentGjtExamBatchNew(schoolId);
            searchParams.put("EQ_examPlanNew.examBatchCode", code);
            model.addAttribute("examBatchCode",code);
        }else if(EmptyUtils.isNotEmpty(searchParams) && EmptyUtils.isNotEmpty(searchParams.get("EQ_examPlanNew.examBatchCode")) ){
            model.addAttribute("examBatchCode", ObjectUtils.toString(searchParams.get("EQ_examPlanNew.examBatchCode")));
        }

        Page<GjtExamAppointmentNew> pageInfo = gjtExamAppointmentNewService.queryPage(schoolId, searchParams, pageRequst);
        
        for (GjtExamAppointmentNew examAppointment : pageInfo) {
        	//设置“预约考点”
        	GjtExamPointAppointmentNew examPointAppointment = gjtExamPointAppointmentNewService.findByExamBatchCodeAndStudentIdAndGjtExamPointNewExamType(
        			examAppointment.getExamBatchCode(), examAppointment.getStudentId(), examAppointment.getExamPlanNew().getType() + "");
        	if (examPointAppointment != null) {
        		examAppointment.setExamPointName(examPointAppointment.getGjtExamPointNew().getName());
        	}
        	
        	//设置“是否补考”和“补考费用”
        	List<GjtExamCost> examCostList = gjtExamCostService.findByStudentIdAndExamPlanId(examAppointment.getStudentId(), examAppointment.getExamPlanId());
        	if (examCostList != null && examCostList.size() > 0) {  //存在缴费记录就是补考
        		examAppointment.setIsResit(true);
        		examAppointment.setResitCost(examCostList.get(0).getCourseCost());
        	} else {
        		examAppointment.setIsResit(false);
        	}
        }
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId()); // 学习中心

		model.addAttribute("termMap", commonMapService.getGradeMap(user.getGjtOrg().getId()));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(schoolId));
		model.addAttribute("examTypeMap", MapKit.toIntAscMap(commonMapService.getExamTypeIntMap()));
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("pageInfo", pageInfo);
		request.getSession().setAttribute("downLoadExcelExportByExamAppointment", searchParams);// 导出数据的查询条件
		return "edumanage/exam/exam_appointment_listNew";
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable String id, Model model, HttpServletRequest request) {
		GjtExamAppointmentNew appointmentNew = gjtExamAppointmentNewService.queryOne(id);
		GjtExamPointAppointmentNew examPointAppointment = gjtExamPointAppointmentNewService.findByExamBatchCodeAndStudentIdAndGjtExamPointNewExamType(
				appointmentNew.getExamBatchCode(), appointmentNew.getStudentId(), appointmentNew.getExamPlanNew().getType() + "");
    	if (examPointAppointment != null) {
    		appointmentNew.setExamPointName(examPointAppointment.getGjtExamPointNew().getName());
    	}
    	
    	Map<String, String> areaMap = commonMapService.getAreaMap();
    	String areaId = examPointAppointment.getGjtExamPointNew().getAreaId();
		String provinceCode = areaId.substring(0, 2) + "0000";
		String cityCode = areaId.substring(0, 4) + "00";
		String districtCode = areaId;
		String provinceName = areaMap.get(provinceCode);
		String cityName = areaMap.get(cityCode);
		String districtName = areaMap.get(districtCode);
		appointmentNew.setAddress(provinceName + cityName + districtName + examPointAppointment.getGjtExamPointNew().getAddress());
		
		model.addAttribute("entity", appointmentNew);
		model.addAttribute("examTypeMap", MapKit.toIntAscMap(commonMapService.getExamTypeIntMap()));
		
		return "edumanage/exam/exam_appointment_view";
	}

	/**
	 * 导出预约考试数据
	 * @throws Exception
	 */
	@SysLog("考试预约-导出考试预约数据")
	@RequestMapping(value = "/export/plan", method = RequestMethod.POST)
	@ResponseBody
	public void exportPlanAppoinments(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			Map<String, Object> searchParams = (Map) request.getSession()
					.getAttribute("downLoadExcelExportByExamAppointment");

			String path = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
					+ "exam" + File.separator;
			String outFile = gjtExamAppointmentNewService.exportAppointPlan(user.getGjtOrg().getId(), searchParams, path);

			super.downloadFile(request, response, path + outFile);
			FileKit.delFile(path + outFile); // 删除临时文件
		} else {
			throw new RuntimeException("您没有权限");
		}
	}

	/**
	 * 返回导入页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "importForm", method = RequestMethod.GET)
	public String importForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(user.getGjtOrg().getId()));
		
		return "edumanage/exam/exam_appointment_import";
	}

	/**
	 * 导入考试预约
	 * @param file
	 * @param examBatchCode
     * @return
     */
	@RequestMapping(value = "/import/plan")
	@ResponseBody
	public ImportFeedback importPlanAppoinments(@RequestParam("file") MultipartFile file, @RequestParam("examBatchCode")String examBatchCode,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			GjtExamBatchNew examBatchNew = gjtExamBatchNewService.queryByExamBatchCode(examBatchCode);
			if (examBatchNew == null) {
				return new ImportFeedback(false, "找不到考试计划！");
			}
			/*// 为了配合考务的201706期末考试的预约工作，先暂时去掉
			if (examBatchNew.getBookSt().compareTo(new Date()) > 0 || examBatchNew.getBookEnd().compareTo(new Date()) < 0) {
				return new ImportFeedback(false, "不在该考试计划的预约时间范围内！");
			}*/
			
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "学号", "姓名", "课程号", "课程名称", "考试形式", "操作方式", "导入结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}

			@SuppressWarnings("unchecked")
			Map<Integer, String> examTypes = commonMapService.getExamTypeIntMap();
			if (dataList != null && dataList.size() > 0) {
				for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据

					if (datas.length < heads.length - 1) {
						result[heads.length - 1] = "数据不全";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[0])) { // 学号
						result[heads.length - 1] = "学号不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[1])) { // 姓名
						result[heads.length - 1] = "姓名不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[2])) { // 课程代码
						result[heads.length - 1] = "课程代码不能为空";
						failedList.add(result);
						continue;
					}
					
					int type = -1;  //考试形式
					Iterator<Entry<Integer, String>> iterator = examTypes.entrySet().iterator();
					while (iterator.hasNext()) {
						Entry<Integer, String> next = iterator.next();
						if (datas[4].equals(next.getValue())){
							type = next.getKey();
							break;
						}
					}
					if (type == -1) {
						result[heads.length - 1] = "考试形式有误";
						failedList.add(result);
						continue;
					}

					if (!"预约".equals(datas[5]) && !"取消预约".equals(datas[5])) { // 操作方式
						result[heads.length - 1] = "操作方式有误";
						failedList.add(result);
						continue;
					}

					System.out.println("xh："+datas[0]);
					GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(datas[0]);
					if (studentInfo == null) {
						result[heads.length - 1] = "找不到学号为【" + datas[0] + "】的学生";
						failedList.add(result);
						continue;
					}
					
					if (!datas[1].equals(studentInfo.getXm())) {
						result[heads.length - 1] = "学号和姓名对应不上";
						failedList.add(result);
						continue;
					}
					
					GjtCourse course = new GjtCourse();
					String courseId = "";
					String source_course_id = "";
					List courseList = teachPlanDao.getCourseByStuAndCourseNo(studentInfo.getStudentId(),datas[2]);
					
					if (EmptyUtils.isEmpty(courseList)) {
						result[heads.length - 1] = "找不到课程代码为【" + datas[2] + "】的课程";
						failedList.add(result);
						continue;
					} else {
						Map courseMap = (Map)courseList.get(0); 
						courseId = ObjectUtils.toString(courseMap.get("COURSE_ID"));
						source_course_id = ObjectUtils.toString(courseMap.get("SOURCE_COURSE_ID"));
						course.setCourseId(courseId);
						// course = courseList.get(0); //    这里会多个  gjtCourseService.findByKchAndXxId(datas[2], user.getGjtOrg().getId());
					}
					
					GjtTeachPlan teachPlan = null;
					/*

					try {
						teachPlan = gjtTeachPlanService.findByGradeIdAndKkzyAndCourseId(
								studentInfo.getNj(), studentInfo.getMajor(), course.getCourseId());
					} catch (Exception e) {
						result[heads.length - 1] = "数据异常，课程对应的教学计划过多";
						failedList.add(result);
						continue;
					}
					
					if (teachPlan == null) {
						result[heads.length - 1] = "查询为空，课程对应的教学计划未找到";
						failedList.add(result);
						continue;
					}*/
					// 视图获取 教学计划
					List<GjtTeachPlan>  teachPlans = gjtTeachPlanService.findView(studentInfo.getGradeSpecialtyId(), course.getCourseId());
					if(EmptyUtils.isEmpty(teachPlans)|| teachPlans.size()==0){
						result[heads.length - 1] = "查询为空，课程对应的教学计划未找到";
						failedList.add(result);
						continue;
					}else if(teachPlans.size()>1){
						result[heads.length - 1] = "数据异常，课程对应的教学计划过多";
						failedList.add(result);
						continue;
					}else {
						teachPlan = teachPlans.get(0);
					}


					GjtRecResult recResult = gjtRecResultService.findByStudentIdAndTeachPlanId(studentInfo.getStudentId(), teachPlan.getTeachPlanId());
					if (recResult == null) {
						System.out.println(studentInfo.getStudentId()+", "+teachPlan.getTeachPlanId());
						result[heads.length - 1] = "找不到学生的选课记录";
						failedList.add(result);
						continue;
					}
					
					//先找指定专业的科目，找不到再找通用的科目
					System.out.println("替换课程thID："+course.getCourseId());
					System.out.println("原始课程ysID："+source_course_id);
					System.out.println("类型type："+type);
					ViewExamPlanSc viewExamPlanSc = null;
					try {
						viewExamPlanSc = viewExamPlanScService.findByExamBatchCodeAndCourseIdAndSpecialtyIdAndType(
								examBatchCode, course.getCourseId(), studentInfo.getMajor(), type);
						if (viewExamPlanSc == null) {
							viewExamPlanSc = viewExamPlanScService.findByExamBatchCodeAndCourseIdAndSpecialtyIdAndType(
									examBatchCode, course.getCourseId(), "-1", type);
						}
						// 如果通用的科目找不到，则找原始课程的科目
						if (viewExamPlanSc == null) {
							viewExamPlanSc = viewExamPlanScService.findByExamBatchCodeAndCourseIdAndSpecialtyIdAndType(
									examBatchCode, source_course_id, studentInfo.getMajor(), type);
						}
						if (viewExamPlanSc == null) {
							viewExamPlanSc = viewExamPlanScService.findByExamBatchCodeAndCourseIdAndSpecialtyIdAndType(
									examBatchCode, source_course_id, "-1", type);
						}
					} catch (Exception e) {
						result[heads.length - 1] = "课程号对应的多个开考科目，导入失败！";
						failedList.add(result);
						continue;
					}
					if (viewExamPlanSc == null) {
						result[heads.length - 1] = "找不到对应开考科目";
						failedList.add(result);
						continue;
					}
					/* 20171110 注释掉是因为学习最低成绩是导成绩的时候再判断的
					if(viewExamPlanSc!=null){
						int examPlanLimit = NumberUtils.toInt(viewExamPlanSc.getExamPlanLimit());
						// 判断考试预约最低分数限制
						if(examPlanLimit > 0){
							//学习成绩大于或等于60分才能预约
							if(EmptyUtils.isNotEmpty(courseList)){
								Map courseMap = (Map)courseList.get(0);
								double examScore=Double.parseDouble(ObjectUtils.toString((courseMap.get("EXAM_SCORE"))));
								if(EmptyUtils.isEmpty(examScore)){
									result[heads.length - 1] = "课程名称为【" + datas[3] + "】的课程未开始学习";
									failedList.add(result);
									continue;
								}else{
									if(examScore < examPlanLimit){
										result[heads.length - 1] = "课程名称为【" + datas[3] + "】的课程的学习成绩低于" + examPlanLimit + "分";
										failedList.add(result);
										continue;
									}
								}
							}
						}
					}*/
					
					GjtExamAppointmentNew examAppointment = gjtExamAppointmentNewService.findByStudentIdAndExamPlanId(studentInfo.getStudentId(), viewExamPlanSc.getExamPlanId());
					if ("预约".equals(datas[5])) {
						String xjzt = ObjectUtils.toString(studentInfo.getXjzt());
						String user_type = ObjectUtils.toString(studentInfo.getUserType());
						// 休学4、退学5、转学10、异动中、毕业8  不可参加考试
						if ("4".equals(xjzt) || "5".equals(xjzt) || "8".equals(xjzt) || "10".equals(xjzt)) {
							result[heads.length - 1] = "学员的学籍状态为“"+commonMapService.getRollTypeMap().get(xjzt)+"”不可参加考试";
							failedList.add(result);
							continue;
						}
						// 是否还没到学习成绩登记截止时间
						if(examBatchNew.getRecordEnd().after(new Date())) {
							// 校验缴费状态
							if (Constants.BOOLEAN_0.equals(recResult.getPayState())) {
								result[heads.length - 1] = "缴费状态：待缴费，请先交清补考费再预约！";
								failedList.add(result);
								continue;
							}
						}
						// 预约机考或笔试的时候要先验证是否预约了考点
						if (type==8 || type==11) {
							Map formMap = new HashMap();
							formMap.put("STUDENT_ID", studentInfo.getStudentId());
							formMap.put("EXAM_BATCH_CODE", examBatchCode);
							List<Map> pointList = gjtExamAppointmentNewService.queryStudentPoint(formMap);
							if (pointList != null && pointList.size() > 0) {
								boolean isAppointmentPoint = false;
								for (Map point : pointList) {
									String examType = (String) point.get("EXAM_TYPE");
									if(examType != null && NumberUtils.toInt(examType) == type) {
										isAppointmentPoint = true;
										break;
									}
								}
								if(!isAppointmentPoint) {
									result[heads.length - 1] = "预约" + datas[4] + "的时候必须先预约考点";
									failedList.add(result);
									continue;
								}
							} else {
								result[heads.length - 1] = "预约"+datas[4]+"的时候必须先预约考点";
								failedList.add(result);
								continue;
							}
						}
						if (examAppointment == null) {
							examAppointment = new GjtExamAppointmentNew();
							examAppointment.setCreatedBy(user.getId());
							examAppointment.setCreatedDt(new Date());
							examAppointment.setXxId(user.getGjtOrg().getId());
							examAppointment.setStudentId(studentInfo.getStudentId());
							examAppointment.setExamPlanId(viewExamPlanSc.getExamPlanId());
							examAppointment.setExamBatchCode(viewExamPlanSc.getExamBatchCode());
							examAppointment.setRecId(recResult.getRecId());
							examAppointment.setType(type);
							examAppointment.setUpdatedBy(user.getId());
							examAppointment.setUpdatedDt(new Date());
							examAppointment.setStatus(0);
							examAppointment.setIsDeleted(0);
							gjtExamAppointmentNewService.insert(examAppointment);
						} else {
							examAppointment.setExamPlanId(viewExamPlanSc.getExamPlanId());
							examAppointment.setExamBatchCode(viewExamPlanSc.getExamBatchCode());
							examAppointment.setRecId(recResult.getRecId());
							examAppointment.setType(type);
							examAppointment.setUpdatedBy(user.getId());
							examAppointment.setUpdatedDt(new Date());
							examAppointment.setStatus(0);
							examAppointment.setIsDeleted(0);
							gjtExamAppointmentNewService.update(examAppointment);
						}
						
						//更新预约状态为“已预约”
						recResult.setBespeakState("1");
						gjtRecResultService.update(recResult);
						
						result[heads.length - 1] = "预约成功";
						successList.add(result);
					} else {
						if (examAppointment != null) {
							examAppointment.setUpdatedBy(user.getId());
							examAppointment.setUpdatedDt(new Date());
							examAppointment.setIsDeleted(1);
							gjtExamAppointmentNewService.update(examAppointment);
							
							//更新预约状态为“待预约”
							recResult.setBespeakState("0");
							gjtRecResultService.update(recResult);
							
							result[heads.length - 1] = "取消预约成功";
							successList.add(result);
						} else {
							result[heads.length - 1] = "还未预约";
							failedList.add(result);
							continue;
						}
					}
					
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "examAppointment_success_" + currentTimeMillis + ".xls";
			String failedFileName = "examAppointment_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "导入考试预约数据成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "导入考试预约数据失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "examAppointment" + File.separator;
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
	 * 导出预约考点数据
	 * 
	 * @param examBatchCode
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/export/point")
	@ResponseBody
	public void exportPointAppoinments(@RequestParam String examBatchCode, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String orgId = user.getGjtOrg().getId();

//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("EQ_examBatchCode", examBatchCode);

		List<Map> list = gjtExamAppointmentNewService.appointPointMapBySearch(examBatchCode, orgId);
		
		HSSFWorkbook workbook = gjtExamAppointmentNewService.exportAppointPoint(list);

		response.setContentType("application/x-msdownload;charset=utf-8");
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String("考点预约数据.xls".getBytes("UTF-8"), "ISO8859-1"));
		workbook.write(response.getOutputStream());

	}

	/**
	 * 返回导入页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "importPointForm", method = RequestMethod.GET)
	public String importPointForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(user.getGjtOrg().getId()));
		
		return "edumanage/exam/exam_point_appointment_import";
	}

	/**
	 * 考点预约导入
	 * @param file
	 * @param examBatchCode
     * @return
     */
	@RequestMapping(value = "/import/point")
	@ResponseBody
	public ImportFeedback importPointAppoinments(@RequestParam("file") MultipartFile file, @RequestParam("examBatchCode")String examBatchCode,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			GjtExamBatchNew examBatchNew = gjtExamBatchNewService.queryByExamBatchCode(examBatchCode);
			if (examBatchNew == null) {
				return new ImportFeedback(false, "找不到考试计划！");
			}
			/*// 为了配合考务的201706期末考试的预约工作，先暂时去掉
			if (examBatchNew.getBookSt().compareTo(new Date()) > 0 || examBatchNew.getBookEnd().compareTo(new Date()) < 0) {
				return new ImportFeedback(false, "不在该考试计划的预约时间范围内！");
			}*/
			
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "学号", "姓名", "考点编号", "考点名称", "操作方式", "导入结果" };
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

					if ("".equals(datas[0])) { // 学号
						result[heads.length - 1] = "学号不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[1])) { // 姓名
						result[heads.length - 1] = "姓名不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[2])) { // 考点编号
						result[heads.length - 1] = "考点编号不能为空";
						failedList.add(result);
						continue;
					}

					if (!"预约考点".equals(datas[4]) && !"取消预约".equals(datas[4])) { // 操作方式
						result[heads.length - 1] = "操作方式有误";
						failedList.add(result);
						continue;
					}
					
					GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(datas[0]);
					if (studentInfo == null) {
						result[heads.length - 1] = "找不到学号为【" + datas[0] + "】的学生";
						failedList.add(result);
						continue;
					} else {
						String xjzt = ObjectUtils.toString(studentInfo.getXjzt());
						String user_type = ObjectUtils.toString(studentInfo.getUserType());
						// 休学4、退学5、转学10、异动中、毕业8  不可参加考试
						if ("4".equals(xjzt) || "5".equals(xjzt) || "8".equals(xjzt) || "10".equals(xjzt)) {
							result[heads.length - 1] = "学员的学籍状态为“"+commonMapService.getRollTypeMap().get(xjzt)+"”不可参加考试";
							failedList.add(result);
							continue;
						}
					}
					
					if (!datas[1].equals(studentInfo.getXm())) {
						result[heads.length - 1] = "学号和姓名对应不上";
						failedList.add(result);
						continue;
					}
					
					GjtExamPointNew examPoint = gjtExamPointNewService.findByExamBatchCodeAndCode(examBatchCode, datas[2]);
					if (examPoint == null) {
						result[heads.length - 1] = "找不到考点编号为【" + datas[2] + "】的考点";
						failedList.add(result);
						continue;
					}
					
					if (!examBatchCode.equals(examPoint.getExamBatchCode())) {
						result[heads.length - 1] = "考点【" + datas[2] + "】不是在" + examBatchNew.getName() + "中";
						failedList.add(result);
						continue;
					}

					GjtExamPointAppointmentNew examPointAppointment = gjtExamPointAppointmentNewService.findByExamBatchCodeAndStudentIdAndGjtExamPointNewExamType(
							examPoint.getExamBatchCode(), studentInfo.getStudentId(), examPoint.getExamType() + "");
					if ("预约考点".equals(datas[4])) {
						if (examPointAppointment == null) {
							examPointAppointment = new GjtExamPointAppointmentNew();
							examPointAppointment.setExamBatchCode(examPoint.getExamBatchCode());
							examPointAppointment.setStudentId(studentInfo.getStudentId());
							examPointAppointment.setExamPointId(examPoint.getExamPointId());
							examPointAppointment.setGradeId(examBatchNew.getGradeId());
							examPointAppointment.setCreatedBy(user.getId());
							examPointAppointment.setCreatedDt(new Date());
							gjtExamPointAppointmentNewService.insert(examPointAppointment);
						} else {
							examPointAppointment.setExamPointId(examPoint.getExamPointId());
							examPointAppointment.setUpdatedBy(user.getId());
							examPointAppointment.setUpdatedDt(new Date());
							gjtExamPointAppointmentNewService.update(examPointAppointment);
						}
						
						result[heads.length - 1] = "预约成功";
						successList.add(result);
					} else {
						if (examPointAppointment != null) {
							examPointAppointment.setUpdatedBy(user.getId());
							examPointAppointment.setUpdatedDt(new Date());
							examPointAppointment.setIsDeleted(1);
							gjtExamPointAppointmentNewService.update(examPointAppointment);
							
							result[heads.length - 1] = "取消预约成功";
							successList.add(result);
						} else {
							result[heads.length - 1] = "还未预约";
							failedList.add(result);
							continue;
						}
					}
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "examPointAppointment_success_" + currentTimeMillis + ".xls";
			String failedFileName = "examPointAppointment_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "导入考点预约数据成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "导入考点预约数据失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "examPointAppointment" + File.separator;
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

	/*public Map<String, ViewStudentInfo> getViewStudentInfos(List<ImportExamAppointmentRecord> records) {
		Map<String, ViewStudentInfo> m = new HashMap<String, ViewStudentInfo>();
		// １、验证学号和姓名
		Set<String> xhs = new HashSet<String>();
		for (ImportExamAppointmentRecord record : records) {
			xhs.add(record.getXh());
		}
		Map<String, ViewStudentInfo> viewStudentInfos = gjtStudentInfoService.getViewStudentInfos(xhs);
		return viewStudentInfos;
	}*/

	/**
	 * 导出考试预约数据统计
	 */
	@SysLog("考试预约数据统计")
	@RequestMapping(value = "exportAppointmentSituation")
	public void exportAppointmentSituation(@RequestParam String examBatchCode, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		if(StringUtils.isBlank(examBatchCode)) {
			examBatchCode = "201710110001"; // 默认201801期末考试
		}
		searchParams.put("xxId", user.getGjtOrg().getId());
		String path = request.getSession().getServletContext().getRealPath("")
				+ WebConstants.EXCEL_DOWNLOAD_URL + "exam" + File.separator;
		String outFile = examServeService.exportExamAppointmentStudentSituation(examBatchCode, searchParams, path);

		super.downloadFile(request, response, path + outFile);
		FileUtils.copyFile(new File(path + outFile), new File(request.getSession().getServletContext().getRealPath("") + File.separator + "static" + File.separator + outFile));// 复制文件以便好手动下载
		FileKit.delFile(path + outFile);
	}

}