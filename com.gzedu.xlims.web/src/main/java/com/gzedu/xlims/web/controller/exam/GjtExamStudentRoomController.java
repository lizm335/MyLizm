package com.gzedu.xlims.web.controller.exam;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
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

import com.gzedu.xlims.common.MapKit;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.ServiceException;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.pojo.exam.GjtExamPointAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamPointNew;
import com.gzedu.xlims.pojo.exam.GjtExamRoomNew;
import com.gzedu.xlims.pojo.exam.GjtExamStudentRoomNew;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.exam.GjtExamAppointmentNewService;
import com.gzedu.xlims.service.exam.GjtExamBatchNewService;
import com.gzedu.xlims.service.exam.GjtExamPlanNewService;
import com.gzedu.xlims.service.exam.GjtExamPointAppointmentNewService;
import com.gzedu.xlims.service.exam.GjtExamStudentRoomNewService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 
 * 功能说明：学员考场记录
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年11月28日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/exam/new/student/room")
public class GjtExamStudentRoomController {

	private static final Log log = LogFactory.getLog(GjtExamStudentRoomController.class);

	@Autowired
	private GjtExamStudentRoomNewService gjtExamStudentRoomNewService;
	
	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;
	
	@Autowired
	private GjtExamBatchNewService gjtExamBatchNewService;
	
	@Autowired
	private GjtExamAppointmentNewService gjtExamAppointmentNewService;

	@Autowired
	private GjtExamPointAppointmentNewService gjtExamPointAppointmentNewService;

	@Autowired
	private GjtExamPlanNewService gjtExamPlanNewService;
	
	@Autowired
	private CommonMapService commonMapService;

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
        
        if(EmptyUtils.isEmpty(searchParams)|| StringUtils.isBlank((String) searchParams.get("EQ_examPlanNew.type")) || "".equals(searchParams.get("EQ_examPlanNew.type"))){
            searchParams.put("EQ_examPlanNew.type", 8);
        }

		Page<GjtExamAppointmentNew> pageInfo = gjtExamAppointmentNewService.queryPage(schoolId, searchParams, pageRequst);
		//Page<Map> pageInfo = gjtExamAppointmentNewService.queryList(searchParams,pageRequst);
        
        for (GjtExamAppointmentNew examAppointment : pageInfo) {
        	//设置“预约考点”
        	GjtExamPointAppointmentNew examPointAppointment = gjtExamPointAppointmentNewService.findByExamBatchCodeAndStudentIdAndGjtExamPointNewExamType(
					examAppointment.getExamBatchCode(), examAppointment.getStudentId(), examAppointment.getExamPlanNew().getType() + "");
        	if (examPointAppointment != null) {
        		examAppointment.setExamPointName(examPointAppointment.getGjtExamPointNew().getName());
        	}
        }
        
        // 查询“已排考”和“未排考”
 		Map<String, Object> map = new HashMap<String, Object>();
 		map.putAll(searchParams);
 		map.remove("EQ_status");
 		model.addAttribute("all", gjtExamAppointmentNewService.queryPage(schoolId, map, pageRequst).getTotalElements());
 		map.put("EQ_status", 1);
 		model.addAttribute("hasArrange", gjtExamAppointmentNewService.queryPage(schoolId, map, pageRequst).getTotalElements());
 		map.put("EQ_status", 0);
 		model.addAttribute("notArrange", gjtExamAppointmentNewService.queryPage(schoolId, map, pageRequst).getTotalElements());
 		
		model.addAttribute("termMap", commonMapService.getGradeMap(schoolId));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(schoolId));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(schoolId));
		model.addAttribute("examTypeMap", MapKit.toIntAscMap(commonMapService.getExamTypeIntMap()));
		model.addAttribute("schoolInfoMap", commonMapService.getStudyCenterMap(user.getGjtOrg().getId()));
		model.addAttribute("pageInfo", pageInfo);

		return "edumanage/exam/exam_student_room_listNew";
		
	}
	
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(@RequestParam String ids, HttpServletRequest request) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				List<GjtExamAppointmentNew> list = gjtExamAppointmentNewService.queryList(Arrays.asList(selectedIds));
				if (list != null && list.size() > 0) {
					for (GjtExamAppointmentNew examAppointment : list) {
						GjtExamStudentRoomNew studentRoom = examAppointment.getGjtExamStudentRoomNew();
						if (studentRoom != null) {
							gjtExamStudentRoomNewService.delete(studentRoom);
							
							examAppointment.setGjtExamStudentRoomNew(null);
							examAppointment.setStatus(0);
							examAppointment.setUpdatedBy(user.getId());
							examAppointment.setUpdatedDt(new Date());
							gjtExamAppointmentNewService.update(examAppointment);
						}
					}
				}

				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return new Feedback(false, "删除失败");
			}
		}
		
		return new Feedback(false, "删除失败");
	}

	@RequestMapping(value = "exportRoomSeat", method = RequestMethod.GET)
	public String exportRoomSeatForm(Model model, HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> batchMap = commonMapService.getGjtExamBatchNewIdNameMap(user.getGjtOrg().getId());
		model.addAttribute("batchMap", batchMap);
		return "edumanage/exam/mydialog/exam_student_room_export";
	}

	@RequestMapping(value = "exportRoomSeat", method = RequestMethod.POST)
	@ResponseBody
	public Feedback exportRoomSeat(Model model, String examBatchCode, String examPointId, int examType,
			HttpServletRequest request, HttpServletResponse response) {
		Workbook workbook = gjtExamStudentRoomNewService.exportExamStudentSeat(examBatchCode, examPointId, examType);

		Feedback fb = new Feedback(false, "失败");
		try {
			this.downloadExcelFile(response, workbook, "考场签到表.xlsx");
		} catch (IOException e) {
			fb = new Feedback(false, "导出异常");
		}
		fb = new Feedback(true, "成功");
		return fb;
	}

	@RequestMapping(value = "exportRoomSeat2", method = RequestMethod.GET)
	public String exportRoomSeatForm2(Model model, HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> batchMap = commonMapService.getGjtExamBatchNewIdNameMap(user.getGjtOrg().getId());
		model.addAttribute("batchMap", batchMap);
		return "edumanage/exam/mydialog/exam_student_room_export2";
	}

	@RequestMapping(value = "exportRoomSeat2", method = RequestMethod.POST)
	@ResponseBody
	public Feedback exportRoomSeat2(Model model, String examBatchCode, String examPointId, int examType,
			HttpServletRequest request, HttpServletResponse response) {
		Workbook workbook = gjtExamStudentRoomNewService.exportExamStudentSeat2(examBatchCode, examPointId, examType);

		//Feedback fb = new Feedback(false, "失败");
		Feedback fb ;
		try {
			this.downloadExcelFile(response, workbook, "排考明细表.xlsx");
		} catch (IOException e) {
			fb = new Feedback(false, "导出异常");
		}
		fb = new Feedback(true, "成功");
		return fb;
	}

	/**
	 * 返回排考页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "planForm", method = RequestMethod.GET)
	public String importForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(user.getGjtOrg().getId()));
		
		return "edumanage/exam/auto_arrangement";
	}

	/**
	 * 自动排考
	 * @param examBatchCode
	 * @param examPointId
	 * @param examType
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "plan", method = RequestMethod.POST)
	@ResponseBody
	public ImportFeedback plan(String examBatchCode, String examPointId, int examType,
			HttpServletRequest request, HttpServletResponse response) {
		GjtExamBatchNew examBatchNew = gjtExamBatchNewService.queryByExamBatchCode(examBatchCode);
		if (examBatchNew == null) {
			return new ImportFeedback(false, "找不到考试计划！");
		}
		if (examBatchNew.getArrangeSt().compareTo(new Date()) > 0 || examBatchNew.getArrangeEnd().compareTo(new Date()) < 0) {
			return new ImportFeedback(false, "不在该考试计划的排考时间范围内！");
		}
		
		//GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			List<GjtExamStudentRoomNew> successList = new ArrayList<GjtExamStudentRoomNew>();
			List<GjtExamStudentRoomNew> failedList = new ArrayList<GjtExamStudentRoomNew>();
			if ("-1".equals(examPointId)) {
				List<GjtExamPointNew> examPointNews = examBatchNew.getGjtExamPointNews();// 批次下的考点
				if (examPointNews != null) {
					for (GjtExamPointNew examPointNew : examPointNews) {
						try {
							Map<String, List<GjtExamStudentRoomNew>> map = gjtExamStudentRoomNewService.arrangement(
									examBatchCode, examPointNew.getExamPointId(), examType);
							successList.addAll(map.get("success"));
							failedList.addAll(map.get("failed"));
						} catch (Exception e) {
						}
					}
				}
			} else {
				Map<String, List<GjtExamStudentRoomNew>> map = gjtExamStudentRoomNewService.arrangement(
						examBatchCode, examPointId, examType);
				successList.addAll(map.get("success"));
				failedList.addAll(map.get("failed"));
			}
			
			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "autoArrangement_success_" + currentTimeMillis + ".xls";
			String failedFileName = "autoArrangement_failed_" + currentTimeMillis + ".xls";

			String[] heads = { "学号", "姓名", "手机", "层次", "年级", "学期", "专业", "考试计划", "考试计划编码", "考试科目",
					"考试科目编码", "考试形式", "试卷号", "预约考点", "考点编码", "考场", "座位号" };
			Workbook workbook1 = ExcelUtil.getWorkbook(heads, transfer(successList), "自动排考成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, transfer(failedList), "自动排考失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "autoArrangement" + File.separator;
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

			return new ImportFeedback(true, successList.size() + failedList.size(), successList.size(), failedList.size(), successFileName,
					failedFileName);
		} catch (ServiceException e) {
			log.error(e.getMessage(), e);
			return new ImportFeedback(false, e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ImportFeedback(false, "系统异常！");
		}
	}
	
	private List<String[]> transfer(List<GjtExamStudentRoomNew> examStudentRooms) {
		List<String[]> list = new ArrayList<String[]>();
		@SuppressWarnings("unchecked")
		Map<Integer, String> examTypeMap = commonMapService.getExamTypeIntMap();
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		for (GjtExamStudentRoomNew examStudentRoom : examStudentRooms) {
			String[] values = new String[17];
			values[0] = examStudentRoom.getGjtStudentInfo().getXh();
			values[1] = examStudentRoom.getGjtStudentInfo().getXm();
			values[2] = examStudentRoom.getGjtStudentInfo().getSjh();
			values[3] = pyccMap.get(examStudentRoom.getGjtStudentInfo().getPycc());
			if (examStudentRoom.getGjtStudentInfo().getGjtGrade() != null) {
				values[4] = examStudentRoom.getGjtStudentInfo().getGjtGrade().getGjtYear().getName();
				values[5] = examStudentRoom.getGjtStudentInfo().getGjtGrade().getGradeName();
			} else {
				values[4] = "";
				values[5] = "";
			}
			values[6] = examStudentRoom.getGjtStudentInfo().getGjtSpecialty().getZymc();
			values[7] = examStudentRoom.getGjtExamPlanNew().getExamBatchNew().getName();
			values[8] = examStudentRoom.getGjtExamPlanNew().getExamBatchNew().getExamBatchCode();
			
			String courseNames = "";
			String courseCodes = "";
			List<GjtCourse> courseList = examStudentRoom.getGjtExamPlanNew().getGjtCourseList();
			if (courseList != null) {
				for (int i = 0; i < courseList.size(); i++) {
					courseNames += courseList.get(i).getKcmc();
					courseCodes += courseList.get(i).getKch();
					
					if (i != courseList.size() - 1) {
						courseNames += ",";
						courseCodes += ",";
					}
				}
			}
			
			values[9] = courseNames;
			values[10] = courseCodes;
			values[11] = examTypeMap.get(examStudentRoom.getGjtExamPlanNew().getType());
			values[12] = examStudentRoom.getGjtExamPlanNew().getExamNo();
			values[13] = examStudentRoom.getGjtExamPointNew().getName();
			values[14] = examStudentRoom.getGjtExamPointNew().getCode();
			
			if (examStudentRoom.getGjtExamRoomNew() != null) {
				values[15] = examStudentRoom.getGjtExamRoomNew().getName();
			} else {
				values[15] = "";
			}
			
			values[16] = String.valueOf(examStudentRoom.getSeatNo());
			
			list.add(values);
		}
		
		return list;
	}

	@RequestMapping(value = "isArrangeOver", method = RequestMethod.GET)
	@ResponseBody
	public Feedback isArrangeOver(Model model, String examBatchCode, String examPointId, int examType,
			HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback fb = new Feedback();
		try {
			Boolean arrangeOver = gjtExamStudentRoomNewService.isArrangeOver(user.getGjtOrg().getId(), examBatchCode,
					examPointId, examType);
			fb.setSuccessful(true);
			fb.setObj(arrangeOver);
			if (!arrangeOver) {
				fb.setMessage("部分预约记录未进行排考或该考点该类型没有预约记录！请先排考再导出签到表");
			}
		} catch (Exception e) {
			fb.setSuccessful(false);
			fb.setMessage("查询异常");
		}
		return fb;
	}

	/**
	 * 返回导入排考页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "importExamStudentRoomForm", method = RequestMethod.GET)
	public String importExamStudentRoomForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(user.getGjtOrg().getId()));
		
		return "edumanage/exam/exam_student_room_import";
	}
	
	@RequestMapping(value = "importExamStudentRoom")
	@ResponseBody
	public ImportFeedback importExamStudentRoom(@RequestParam("file") MultipartFile file, @RequestParam("examBatchCode")String examBatchCode,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			GjtExamBatchNew examBatchNew = gjtExamBatchNewService.queryByExamBatchCode(examBatchCode);
			if (examBatchNew == null) {
				return new ImportFeedback(false, "找不到考试计划！");
			}
			if (examBatchNew.getArrangeSt().compareTo(new Date()) > 0 || examBatchNew.getArrangeEnd().compareTo(new Date()) < 0) {
				return new ImportFeedback(false, "不在该考试计划的排考时间范围内！");
			}
			
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "学号", "姓名", "身份证号", "考试科目名称", "试卷号", "考试形式", "考点名称", "考点编号", "考场名称", "座位号", "导入结果" };
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

					if ("".equals(datas[2])) { // 身份证号
						result[heads.length - 1] = "身份证号不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[4])) { // 试卷号
						result[heads.length - 1] = "试卷号不能为空";
						failedList.add(result);
						continue;
					}

					int examType = -1;
					if (!"笔试".equals(datas[5]) && !"机考".equals(datas[5])) { // 考试形式
						result[heads.length - 1] = "考试形式有误";
						failedList.add(result);
						continue;
					}
					if ("笔试".equals(datas[5])) {
						examType = 8;
					} else if ("机考".equals(datas[5])) {
						examType = 11;
					}

					if ("".equals(datas[7])) { // 考点编号
						result[heads.length - 1] = "考点编号不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[8])) { // 考场名称
						result[heads.length - 1] = "考场名称不能为空";
						failedList.add(result);
						continue;
					}
					
					int seatNo = -1;
					if (!"".equals(datas[9])) { //座位号
						try {
							seatNo = Integer.parseInt(datas[9]);
						} catch (Exception e) {
							result[heads.length - 1] = "座位号格式不正确，请填写数字";
							failedList.add(result);
							continue;
						}
					}
					
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
					
					if (!datas[2].equals(studentInfo.getSfzh())) {
						result[heads.length - 1] = "学号和身份证号对应不上";
						failedList.add(result);
						continue;
					}
					
					GjtExamPlanNew examPlanNew = gjtExamPlanNewService.findByExamBatchCodeAndExamNoAndType(examBatchCode, datas[4], examType);
					if (examPlanNew == null) {
						result[heads.length - 1] = "根据试卷号【" + datas[4] + "】和考试形式【" + datas[5] + "】找不到对应的开考科目";
						failedList.add(result);
						continue;
					}
					
					GjtExamAppointmentNew examAppointment = gjtExamAppointmentNewService.findByStudentIdAndExamPlanId(studentInfo.getStudentId(), examPlanNew.getExamPlanId());
					if (examAppointment == null) {
						result[heads.length - 1] = "该学生未预约该考试科目";
						failedList.add(result);
						continue;
					}
					
					GjtExamPointAppointmentNew examPointAppointment = gjtExamPointAppointmentNewService.findByExamBatchCodeAndStudentIdAndGjtExamPointNewExamType(
							examAppointment.getExamBatchCode(), examAppointment.getStudentId(), examAppointment.getExamPlanNew().getType() + "");
					if (examPointAppointment == null) {
						result[heads.length - 1] = "该学生还未预约考点";
						failedList.add(result);
						continue;
					}
					if (!datas[7].equals(examPointAppointment.getGjtExamPointNew().getCode())) {
						result[heads.length - 1] = "考点信息与学生预约的考点不一致";
						failedList.add(result);
						continue;
					}
					
					GjtExamRoomNew examRoom = null;
					List<GjtExamRoomNew> examRooms = examPointAppointment.getGjtExamPointNew().getGjtExamRoomNews();
					for (GjtExamRoomNew e : examRooms) {
						if (datas[8].equals(e.getName())) {
							examRoom = e;
							break;
						}
					}
					if (examRoom == null) {
						result[heads.length - 1] = "找不到考场名称为【" + datas[8] + "】的考场";
						failedList.add(result);
						continue;
					}
					
					if (seatNo != -1) {  //指定了座位
						if (examRoom.getSeats() < seatNo) {
							result[heads.length - 1] = "座位号超出了该考场的最大位置【" + examRoom.getSeats() + "】";
							failedList.add(result);
							continue;
						}
						//判断位置是否被占用
						GjtExamStudentRoomNew examStudentRoom = gjtExamStudentRoomNewService.findByExamPlanIdAndExamRoomIdAndSeatNo(examPlanNew.getExamPlanId(), examRoom.getExamRoomId(), seatNo);
						if (examStudentRoom != null) {
							if (examStudentRoom.getStudentId().equals(studentInfo.getStudentId())) {  //本人占用
								result[heads.length - 1] = "排考成功";
								successList.add(result);
								continue;
							} else {
								result[heads.length - 1] = "该座位号已有人，请先删除数据";
								failedList.add(result);
								continue;
							}
						} else {
							// 如果以前有位置则清空排考记录
							GjtExamStudentRoomNew oldStudentRoom = gjtExamStudentRoomNewService.findByStudentIdAndExamPlanId(studentInfo.getStudentId(), examPlanNew.getExamPlanId());
							if (oldStudentRoom != null) {
								gjtExamStudentRoomNewService.delete(oldStudentRoom);
								examAppointment.setGjtExamStudentRoomNew(null);
							}

							GjtExamStudentRoomNew saveObj = new GjtExamStudentRoomNew(examBatchNew.getExamBatchCode(), examPlanNew.getExamPlanId(),
									examPointAppointment.getGjtExamPointNew().getExamPointId(), examRoom.getExamRoomId(), seatNo, examType);
							saveObj.setAppointmentId(examAppointment.getAppointmentId());
							saveObj.setStudentId(studentInfo.getStudentId());
							saveObj.setExamBatchId(examBatchNew.getExamBatchId());
							saveObj.setCreatedBy(user.getId());
							gjtExamStudentRoomNewService.insert(saveObj);
							
							examAppointment.setStatus(1);
							examAppointment.setUpdatedBy(user.getId());
							gjtExamAppointmentNewService.update(examAppointment);
							
							result[heads.length - 1] = "排考成功";
							successList.add(result);
							continue;
						}
					} else {  //系统分配
						//是否已有位置
						GjtExamStudentRoomNew oldStudentRoom = gjtExamStudentRoomNewService.findByStudentIdAndExamPlanId(studentInfo.getStudentId(), examPlanNew.getExamPlanId());
						if (oldStudentRoom != null) {
							if (oldStudentRoom.getExamRoomId().equals(examRoom.getExamRoomId())) {
								//考场一致不作改变
								result[heads.length - 1] = "排考成功";
								successList.add(result);
								continue;
							} else {
								//重新分配到指定的考场
								List<GjtExamStudentRoomNew> exists = gjtExamStudentRoomNewService.queryExamStudentRoomNewsByExamRoomIdAndExamPlanId(
										examRoom.getExamRoomId(), examPlanNew.getExamPlanId());  //该考场已经分配的座位
								if (exists == null || exists.size() == 0) {
									//分配1号位置
									gjtExamStudentRoomNewService.delete(oldStudentRoom);
									examAppointment.setGjtExamStudentRoomNew(null);
									
									GjtExamStudentRoomNew saveObj = new GjtExamStudentRoomNew(examBatchNew.getExamBatchCode(), examPlanNew.getExamPlanId(),
											examPointAppointment.getGjtExamPointNew().getExamPointId(), examRoom.getExamRoomId(), 1, examType);
									saveObj.setAppointmentId(examAppointment.getAppointmentId());
									saveObj.setStudentId(studentInfo.getStudentId());
									saveObj.setExamBatchId(examBatchNew.getExamBatchId());
									saveObj.setCreatedBy(user.getId());
									gjtExamStudentRoomNewService.insert(saveObj);

									examAppointment.setStatus(1);
									examAppointment.setUpdatedBy(user.getId());
									gjtExamAppointmentNewService.update(examAppointment);
									
									result[heads.length - 1] = "排考成功";
									successList.add(result);
									continue;
								} else {
									GjtExamStudentRoomNew saveObj = null;
									for (int i = 1; i <= examRoom.getSeats(); i++) {
										boolean flag = true;
										for (GjtExamStudentRoomNew r : exists) {
											if (r.getSeatNo() == i) {
												flag = false;
												break;
											}
										}
										if (flag) {  //可以分配该位置
											gjtExamStudentRoomNewService.delete(oldStudentRoom);
											examAppointment.setGjtExamStudentRoomNew(null);
											
											saveObj = new GjtExamStudentRoomNew(examBatchNew.getExamBatchCode(), examPlanNew.getExamPlanId(),
													examPointAppointment.getGjtExamPointNew().getExamPointId(), examRoom.getExamRoomId(), i, examType);
											saveObj.setAppointmentId(examAppointment.getAppointmentId());
											saveObj.setStudentId(studentInfo.getStudentId());
											saveObj.setExamBatchId(examBatchNew.getExamBatchId());
											saveObj.setCreatedBy(user.getId());
											gjtExamStudentRoomNewService.insert(saveObj);

											examAppointment.setStatus(1);
											examAppointment.setUpdatedBy(user.getId());
											gjtExamAppointmentNewService.update(examAppointment);
											
											result[heads.length - 1] = "排考成功";
											successList.add(result);
											break;
										}
									}
									
									if (saveObj == null) {
										result[heads.length - 1] = "排考失败，没有空缺的座位";
										failedList.add(result);
										continue;
									}
								}
								
							}
						} else {
							List<GjtExamStudentRoomNew> exists = gjtExamStudentRoomNewService.queryExamStudentRoomNewsByExamRoomIdAndExamPlanId(
									examRoom.getExamRoomId(), examPlanNew.getExamPlanId());  //该考场已经分配的座位
							if (exists == null || exists.size() == 0) {
								//分配1号位置
								GjtExamStudentRoomNew saveObj = new GjtExamStudentRoomNew(examBatchNew.getExamBatchCode(), examPlanNew.getExamPlanId(),
										examPointAppointment.getGjtExamPointNew().getExamPointId(), examRoom.getExamRoomId(), 1, examType);
								saveObj.setAppointmentId(examAppointment.getAppointmentId());
								saveObj.setStudentId(studentInfo.getStudentId());
								saveObj.setExamBatchId(examBatchNew.getExamBatchId());
								saveObj.setCreatedBy(user.getId());
								gjtExamStudentRoomNewService.insert(saveObj);
								
								examAppointment.setStatus(1);
								examAppointment.setUpdatedBy(user.getId());
								gjtExamAppointmentNewService.update(examAppointment);
								
								result[heads.length - 1] = "排考成功";
								successList.add(result);
								continue;
							} else {
								GjtExamStudentRoomNew saveObj = null;
								for (int i = 1; i <= examRoom.getSeats(); i++) {
									boolean flag = true;
									for (GjtExamStudentRoomNew r : exists) {
										if (r.getSeatNo() == i) {
											flag = false;
											break;
										}
									}
									if (flag) {  //可以分配该位置
										saveObj = new GjtExamStudentRoomNew(examBatchNew.getExamBatchCode(), examPlanNew.getExamPlanId(),
												examPointAppointment.getGjtExamPointNew().getExamPointId(), examRoom.getExamRoomId(), i, examType);
										saveObj.setAppointmentId(examAppointment.getAppointmentId());
										saveObj.setStudentId(studentInfo.getStudentId());
										saveObj.setExamBatchId(examBatchNew.getExamBatchId());
										saveObj.setCreatedBy(user.getId());
										gjtExamStudentRoomNewService.insert(saveObj);
										
										examAppointment.setStatus(1);
										examAppointment.setUpdatedBy(user.getId());
										gjtExamAppointmentNewService.update(examAppointment);
										
										result[heads.length - 1] = "排考成功";
										successList.add(result);
										break;
									}
								}
								
								if (saveObj == null) {
									result[heads.length - 1] = "排考失败，没有空缺的座位";
									failedList.add(result);
									continue;
								}
							}
						}
					}
					
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "examStudentRoom_success_" + currentTimeMillis + ".xls";
			String failedFileName = "examStudentRoom_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "导入排考数据成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "导入排考数据失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "examStudentRoom" + File.separator;
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
	 * 导出未排考学员
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "exportNoArrange")
	@ResponseBody
	public void exportNoArrange(@RequestParam("examBatchCode")String examBatchCode,@RequestParam("examType")String examType,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = "笔试排考数据导入模板.xls";
		if ("11".equals(ObjectUtils.toString(examType))) {
			fileName = "机考排考数据导入模板.xls";
		}
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        searchParams.put("examBatchCode", examBatchCode);
        searchParams.put("status", 0);
        searchParams.put("examTypes", examType);
        searchParams.put("orgId", user.getGjtOrg().getId());
		List<Map> list = gjtExamAppointmentNewService.queryList(searchParams);
        HSSFWorkbook workbook =this.getWorkbook(list);
		ExcelUtil.downloadExcelFile(response, workbook, fileName);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HSSFWorkbook getWorkbook(List<Map> list) {
		try {
			InputStream fis = GjtExamStudentRoomController.class.getResourceAsStream(WebConstants.EXCEL_MODEL_URL + "排考数据导入模板.xls");
			HSSFWorkbook wb = new HSSFWorkbook(fis);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = sheet.getRow(3);

			int rowIdx = 3;
			int colIdx = 0;
			HSSFCell cell;

			// 为了能够使用换行，需要设置单元格的样式 wrap=true
			HSSFCellStyle s = wb.createCellStyle();
			s.setWrapText(true);
			
			for (Map<String, Object> map : list) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(String.valueOf(map.get("XH")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(String.valueOf(map.get("XM")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(String.valueOf(map.get("SFZH")));
				
				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(String.valueOf(map.get("EXAM_PLAN_NAME")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(String.valueOf(map.get("EXAM_NO")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(String.valueOf(map.get("EXAM_TYPE_NAME")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(String.valueOf(map.get("POINT_NAME")));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(String.valueOf(map.get("POINT_CODE")));
			}

			return wb;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 设置让浏览器弹出下载[Excel文件]对话框的Header.
	 * 
	 * @param response
	 * @param workbook
	 * @param outputFileName
	 * @throws IOException
	 */
	protected void downloadExcelFile(HttpServletResponse response, Workbook workbook, String outputFileName)
			throws IOException {
		if (workbook != null) {
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(outputFileName.getBytes("UTF-8"), "ISO8859-1"));
			workbook.write(response.getOutputStream());
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("<script type='application/javascript'>/*自动关闭当前窗口*/window.close();</script>");
			response.getWriter().flush();
		}
	}
	
	/**
	 * 查询准考证信息（临时）
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "listAdmissionTicket", method = RequestMethod.GET)
	public String listAdmissionTicket(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String schoolId= user.getGjtOrg().getId();
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		Page<Map<String, Object>> pageInfo = gjtExamStudentRoomNewService.findAdmissionTickets(schoolId, searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);

		return "edumanage/exam/exam_admission_ticket_list";
		
	}
}