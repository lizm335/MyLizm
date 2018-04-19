package com.ouchgzee.study.web.controller.exam;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.gzedu.xlims.pojo.*;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtil;
import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.common.PlatfromTypeEnum;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.common.constants.CacheConstants.OrderType;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.dao.ViewStudentInfoDao;
import com.gzedu.xlims.dao.exam.GjtExamPointNewDao;
import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.pojo.exam.GjtExamCost;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.pojo.exam.GjtExamPointAppointmentNew;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.exam.GjtExamAppointmentNewService;
import com.gzedu.xlims.service.exam.GjtExamBatchNewService;
import com.gzedu.xlims.service.exam.GjtExamCorrectPapersService;
import com.gzedu.xlims.service.exam.GjtExamCostService;
import com.gzedu.xlims.service.exam.GjtExamPlanNewService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSetOrgCopyrightService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.serviceImpl.exam.wk.GjtExamWkService;
import com.ouchgzee.study.service.exam.ExamServeService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.vo.exam.ExamBatchNewVo;


@Controller
@RequestMapping("/pcenter/2/exam")
public class NewExamController extends BaseController {

	private static Log log = LogFactory.getLog(NewExamController.class);

	@Autowired
	private GjtExamBatchNewService gjtExamBatchNewService;

	@Autowired
	private GjtExamAppointmentNewService gjtExamAppointmentNewService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	ExamServeService examServeService;
	
	@Autowired
	ViewStudentInfoDao viewStudentInfoDao;
	
	@Autowired
	GjtExamPointNewDao gjtExamPointNewDao;
	
	@Autowired
	GjtOrgService GjtOrgService;
	
	@Autowired
	GjtExamWkService gjtExamWkService;

	@Autowired
	GjtSetOrgCopyrightService gjtSetOrgCopyrightService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtRecResultService gjtRecResultService;

	@Autowired
	private GjtExamPlanNewService gjtExamPlanNewService;

	@Autowired
	private GjtCourseService gjtCourseService;

	@Autowired
	private GjtExamCostService gjtExamCostService;

	@Autowired
	private GjtExamCorrectPapersService gjtExamCorrectPapersService;

	@Autowired
	private GjtClassInfoService gjtClassInfoService;

	@Autowired
	private CodeGeneratorService codeGeneratorService;

	@Autowired
	private CacheService cacheService;

	@Value("#{configProperties['pcenterStudyServer']}")
	private String pcenterStudyServer;

	@Value("#{configProperties['pcenterStudyServer.pay.examFeeNotify']}")
	private String pcenterStudyServerPayExamFeeNotify;

	@Value("#{configProperties['pcenterStudyServer.pay.examFeeReturn']}")
	private String pcenterStudyServerPayExamFeeReturn;

	/**
	 * 查询考试计划
	 * @return
	 */
	@RequestMapping(value="/getExamBatchData", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getExamBatchData(HttpServletRequest request, HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		String examBatchCode = request.getParameter("examBatchCode");
		if(StringUtils.isNotBlank(examBatchCode)) {
			searchParams.put("EQ_examBatchCode", examBatchCode);
		}
		searchParams.put("EQ_xxId", student.getXxId());
		searchParams.put("EQ_planStatus", "3");
		List<GjtExamBatchNew> list = gjtExamBatchNewService.queryBy(searchParams, null);

		List<ExamBatchNewVo> resultList = new ArrayList<ExamBatchNewVo>();
		Date now = new Date();
		for (GjtExamBatchNew info : list) {
			ExamBatchNewVo vo = new ExamBatchNewVo();
			vo.setExamBatchId(info.getExamBatchId());
			vo.setExamBatchCode(info.getExamBatchCode());
			vo.setName(info.getName());
			vo.setBookSt(info.getBookSt());
			vo.setBookEnd(info.getBookEnd());
			if(info.getBooksSt() != null && now.after(info.getBooksSt())) {  // 如果当前时间在某时间之后
				vo.setBookSt(info.getBooksSt());
				vo.setBookEnd(info.getBooksEnd());
			}
			vo.setOnlineSt(info.getOnlineSt());
			vo.setOnlineEnd(info.getOnlineEnd());
			vo.setProvinceOnlineSt(info.getProvinceOnlineSt());
			vo.setProvinceOnlineEnd(info.getProvinceOnlineEnd());
			vo.setPaperSt(info.getPaperSt());
			vo.setPaperEnd(info.getPaperEnd());
			vo.setMachineSt(info.getMachineSt());
			vo.setMachineEnd(info.getMachineEnd());
			vo.setShapeEnd(info.getShapeEnd());
			vo.setThesisEnd(info.getThesisEnd());
			vo.setReportEnd(info.getReportEnd());
			vo.setRecordEnd(info.getRecordEnd());
			vo.setBktkBookSt(info.getBktkBookSt());
			vo.setBktkBookEnd(info.getBktkBookEnd());
			vo.setXwyyBookSt(info.getXwyyBookSt());
			vo.setXwyyBookEnd(info.getXwyyBookEnd());
			vo.setBktkExamSt(info.getBktkExamSt());
			vo.setBktkExamEnd(info.getBktkExamEnd());
			vo.setXwyyExamSt(info.getXwyyExamSt());
			vo.setXwyyExamEnd(info.getXwyyExamEnd());
			if(now.before(vo.getBookSt())) { // 如果当前时间在预约时间前
				vo.setStatus(0);
			} else if(vo.getIsBook() == 1) { // 如果当前时间在预约时间之内
				vo.setStatus(1);
			} else {
				vo.setStatus(2);
			}
			if((vo.getOnlineEnd() == null || now.after(vo.getOnlineEnd())) &&
					(vo.getProvinceOnlineEnd() == null || now.after(vo.getProvinceOnlineEnd())) &&
                    (vo.getPaperEnd() == null || now.after(vo.getPaperEnd())) &&
                    (vo.getMachineEnd() == null || now.after(vo.getMachineEnd()))) {
				vo.setStatus(3);
			}
			if(now.after(vo.getRecordEnd())) {
				vo.setStatus(4);
			}
			resultList.add(vo);
		}
		Map result = new HashMap();
		result.put("infos", resultList);
		return result;
	}

    /**
     * 预约中的课程
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value = "/appointmentExam", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> appointmentExam(HttpServletRequest request, HttpSession session) throws CommonException {
        final GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        final GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
        GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(student.getXxId(), PlatfromTypeEnum.PERCENTPLATFORM.getNum());
        Map<String, Object> searchParams = new HashMap<String, Object>();
        String examBatchCode = request.getParameter("examBatchCode");
        if(StringUtils.isBlank(examBatchCode)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数");
        }
		Map result = new HashMap();
		Date now = new Date();
		GjtExamBatchNew info = gjtExamBatchNewService.queryByExamBatchCode(examBatchCode);
		Date bookEnd = info.getBookEnd();
		if(info.getBooksSt() != null && now.after(info.getBooksSt())) {  // 如果当前时间在某时间之后
			bookEnd = info.getBooksEnd();
		}
		Map examBatch = new HashMap();
		examBatch.put("examBatchId", info.getExamBatchId());
		examBatch.put("examBatchCode", info.getExamBatchCode());
		examBatch.put("name", info.getName());
		examBatch.put("bookEnd", DateUtil.dateToString(bookEnd));
		examBatch.put("endDay", DateUtil.diffDays(DateUtil.strToYYMMDDDate(DateFormatUtils.ISO_DATE_FORMAT.format(now)), DateUtil.strToYYMMDDDate(DateFormatUtils.ISO_DATE_FORMAT.format(bookEnd))));
		result.put("examBatch", examBatch);

		searchParams.put("EXAM_BATCH_CODE", examBatchCode);
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
        searchParams.put("USER_TYPE", student.getUserType());
        searchParams.put("username", student.getSfzh());
        searchParams.put("XJZT", student.getXjzt());
        searchParams.put("KKZY", student.getMajor());
        //searchParams.put("PYCC", user.getGjtStudentInfo().getPycc());
        searchParams.put("XX_ID", ObjectUtils.toString(student.getXxId()));
        searchParams.put("SCHOOL_MODEL", ObjectUtils.toString(item.getSchoolModel(), "")); //返回院校模式(0:非院校，1：院校)
        final Map resultMap = examServeService.queryAppointmentExam(searchParams);
		result.put("ALREADY_COUNT", NumberUtils.toInt((String) resultMap.get("ALREADY_COUNT"))); // 已报考课程
		result.put("PENDING_COUNT", NumberUtils.toInt((String) resultMap.get("PENDING_COUNT"))); // 待报考课程
		result.put("MAX_COUNT", WebConstants.EXAM_APPOINTMENT_LIMIT); // 每学期最多报考课程数
		result.put("CAN_COUNT", WebConstants.EXAM_APPOINTMENT_LIMIT - NumberUtils.toInt((String) resultMap.get("ALREADY_COUNT")) < NumberUtils.toInt((String) resultMap.get("PENDING_COUNT"))
								? WebConstants.EXAM_APPOINTMENT_LIMIT - NumberUtils.toInt((String) resultMap.get("ALREADY_COUNT")) : NumberUtils.toInt((String) resultMap.get("PENDING_COUNT"))); // 可报考课程

        Map<String, Object> searchParams2 = new HashMap<String, Object>();
        searchParams2.put("EQ_examBatchCode", examBatchCode);
        searchParams2.put("EQ_studentId", student.getStudentId());
        List<Map<String, Object>> examList = gjtExamAppointmentNewService.queryExamAppointment(searchParams2);

        Map<String, Object> searchParams3 = new HashMap<String, Object>();
        searchParams3.put("EQ_examBatchCode", examBatchCode);
        searchParams3.put("EQ_studentId", student.getStudentId());
        List<GjtExamPointAppointmentNew> examPointList = gjtExamAppointmentNewService.queryExamPointAppointmentBy(searchParams3, null);
        Map<String, String> point = new HashMap<String, String>();
        for (GjtExamPointAppointmentNew e : examPointList) {
            point.put(e.getGjtExamPointNew().getExamType(), e.getGjtExamPointNew().getName());
			point.put("pointAddress" + e.getGjtExamPointNew().getExamType(), e.getGjtExamPointNew().getAddress());
        }

        List<Map<String, Object>> examCourse = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> e : examList) {
            Map<String, Object> examCourseMap = new HashMap<String, Object>();
            String courseId = (String) e.get("courseId");
            examCourseMap.put("termCourseId", e.get("termCourseId"));
            examCourseMap.put("courseId", courseId);
            examCourseMap.put("courseName", e.get("courseName"));
            examCourseMap.put("type", e.get("type"));
            examCourseMap.put("typeName", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.EXAMINATIONMODE, e.get("type") + ""));
			examCourseMap.put("learningStyle", e.get("learningStyle"));
            Date examSt = (Date) e.get("examSt");
            Date examEnd = (Date) e.get("examEnd");
            examCourseMap.put("examSt", examSt);
            examCourseMap.put("examEnd", examEnd);
            examCourseMap.put("pointName", point.get(e.get("type") + ""));
			examCourseMap.put("pointAddress", point.get("pointAddress" + e.get("type")));
			if(examSt == null) {
                examCourseMap.put("examDay", "--");
            } else {
                examCourseMap.put("examDay", DateUtil.diffDays(DateUtil.strToYYMMDDDate(DateFormatUtils.ISO_DATE_FORMAT.format(now)), DateUtil.strToYYMMDDDate(DateFormatUtils.ISO_DATE_FORMAT.format(examSt))));
            }
            examCourseMap.put("recId", e.get("recId"));
            examCourseMap.put("examPlanId", e.get("examPlanId"));
			GjtClassInfo courseClass = gjtClassInfoService.findCourseClassByStudentIdAndCourseId(student.getStudentId(), courseId);
			examCourseMap.put("courseClassId", courseClass != null ? courseClass.getClassId() : null);
			examCourse.add(examCourseMap);
		}

		result.put("examCourse", examCourse);
        return result;
    }

	/**
	 * 待预约的课程
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/appointmentCourse", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> appointmentCourse(HttpServletRequest request, HttpSession session) throws CommonException {
		final GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		final GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(student.getXxId(), PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		Map<String, Object> searchParams = new HashMap<String, Object>();
		String examBatchCode = request.getParameter("examBatchCode");
		if(StringUtils.isBlank(examBatchCode)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数");
		}
		Map result = new HashMap();
		searchParams.put("EXAM_BATCH_CODE", examBatchCode);
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("USER_TYPE", student.getUserType());
		searchParams.put("username", student.getSfzh());
		searchParams.put("XJZT", student.getXjzt());
		searchParams.put("KKZY", student.getMajor());
		//searchParams.put("PYCC", user.getGjtStudentInfo().getPycc());
		searchParams.put("XX_ID", ObjectUtils.toString(student.getXxId()));
		searchParams.put("SCHOOL_MODEL", ObjectUtils.toString(item.getSchoolModel(), "")); //返回院校模式(0:非院校，1：院校)
		final Map resultMap = examServeService.queryAppointmentExam(searchParams);
		result.put("ALREADY_COUNT", NumberUtils.toInt((String) resultMap.get("ALREADY_COUNT"))); // 已报考课程
		result.put("PENDING_COUNT", NumberUtils.toInt((String) resultMap.get("PENDING_COUNT"))); // 待报考课程
		result.put("MAX_COUNT", WebConstants.EXAM_APPOINTMENT_LIMIT); // 每学期最多报考课程数
		result.put("CAN_COUNT", WebConstants.EXAM_APPOINTMENT_LIMIT - NumberUtils.toInt((String) resultMap.get("ALREADY_COUNT")) < NumberUtils.toInt((String) resultMap.get("PENDING_COUNT"))
				? WebConstants.EXAM_APPOINTMENT_LIMIT - NumberUtils.toInt((String) resultMap.get("ALREADY_COUNT")) : NumberUtils.toInt((String) resultMap.get("PENDING_COUNT"))); // 可报考课程

		Map<String, Object> searchParams3 = new HashMap<String, Object>();
		searchParams3.put("EQ_examBatchCode", examBatchCode);
		searchParams3.put("EQ_studentId", student.getStudentId());
		List<GjtExamPointAppointmentNew> examPointList = gjtExamAppointmentNewService.queryExamPointAppointmentBy(searchParams3, null);
		Map<String, String> point = new HashMap<String, String>();
		for (GjtExamPointAppointmentNew e : examPointList) {
			point.put(e.getGjtExamPointNew().getExamType(), e.getGjtExamPointNew().getName());
			point.put("pointAddress" + e.getGjtExamPointNew().getExamType(), e.getGjtExamPointNew().getAddress());
		}

		Map zpMap = examServeService.getPersonalZP(searchParams);
		result.put("identificationPhoto", ((List<Object>) zpMap.get("PERSONLIST")).size() == 0 ? 0 : 1); // 是否需要上传证件照

		List<Map<String, Object>> rebuildCourse = new ArrayList<Map<String, Object>>();		// 需要重修课程列表
		List<Map<String, Object>> appointmentCourse = new ArrayList<Map<String, Object>>();	// 待预约的课程列表
		for(Map<String, Object> term : (List<Map<String, Object>>) resultMap.get("LIST")) {
			for (Map<String, Object> e : (List<Map<String, Object>>) term.get("APPOINTMENTLIST")) {
			    // 选择待预约的
                if(Constants.BOOLEAN_0.equals(e.get("BESPEAK_STATE"))) {
                    Map<String, Object> examCourseMap = new HashMap<String, Object>();
                    examCourseMap.put("courseName", e.get("COURSE_NAME"));
                    String type = (String) e.get("KSFS_FLAG");
                    examCourseMap.put("type", type);
                    examCourseMap.put("typeName", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.EXAMINATIONMODE, type));
					examCourseMap.put("learningStyle", NumberUtils.toInt((String) e.get("LEARNING_STYLE")));
                    examCourseMap.put("examSt", e.get("EXAM_STIME") + ":00");
                    examCourseMap.put("examEnd", e.get("EXAM_ETIME") + ":00");
                    if ("8".equals(type)) { // 笔试
                        examCourseMap.put("appointmentWrittenPoint", point.get(type) == null ? 0 : 1);
                        examCourseMap.put("appointmentComputerPoint", -1);
                    } else if ("11".equals(type)) { // 机考
                        examCourseMap.put("appointmentWrittenPoint", -1);
                        examCourseMap.put("appointmentComputerPoint", point.get(type) == null ? 0 : 1);
                    } else {
                        examCourseMap.put("appointmentWrittenPoint", -1);
                        examCourseMap.put("appointmentComputerPoint", -1);
                    }
                    examCourseMap.put("isRebuild", Constants.BOOLEAN_0.equals(e.get("EXAM_STATE")) ? 0 : -1);
                    examCourseMap.put("payState", NumberUtils.toInt((String) e.get("PAY_STATE"))); // 缴费状态：0 待缴费 1 已交费 2 无需缴费
                    String courseCost = gjtCourseService.queryExamFeeByKch((String) e.get("KCH"));
                    examCourseMap.put("price", courseCost);
                    examCourseMap.put("recId", e.get("REC_ID"));
                    examCourseMap.put("teachPlanId", e.get("TEACH_PLAN_ID"));
                    examCourseMap.put("examPlanId", e.get("EXAM_PLAN_ID"));
                    if (Constants.BOOLEAN_0.equals(e.get("EXAM_STATE"))) { // 未通过需要重修
                        rebuildCourse.add(examCourseMap);
                    } else {
                        appointmentCourse.add(examCourseMap);
                    }
                }
			}
		}

		result.put("rebuildCourse", rebuildCourse);
		result.put("appointmentCourse", appointmentCourse);
		return result;
	}

    /**
     * 我的考点
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value = "/myExamPoint", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> myExamPoint(HttpServletRequest request, HttpSession session) throws CommonException {
        final GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        Map<String, Object> searchParams = new HashMap<String, Object>();
        String examBatchCode = request.getParameter("examBatchCode");
        if (StringUtils.isBlank(examBatchCode)) {
            throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数");
        }
        Map result = new HashMap();
        searchParams.put("EXAM_BATCH_CODE", examBatchCode);
        searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
        List<Map> resultMap = examServeService.getAppointExamPointByStudent(searchParams);

        result.put("examPoint", resultMap);
        return result;
    }

    /**
     * 参加相同考试的同学
     * @return
     */
    @RequestMapping(value = "/joinClassmate", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> joinClassmate(HttpServletRequest request, HttpSession session) throws CommonException {
        final GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        final GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
        GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(student.getXxId(), PlatfromTypeEnum.PERCENTPLATFORM.getNum());
        Map<String, Object> searchParams = new HashMap<String, Object>();
        String examBatchCode = request.getParameter("examBatchCode");
        String recId = request.getParameter("recId");
        if (StringUtils.isBlank(examBatchCode) || StringUtils.isBlank(recId)) {
            throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数");
        }
        Map result = new HashMap();
        searchParams.put("EXAM_BATCH_CODE", examBatchCode);
        searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
        searchParams.put("USER_TYPE", student.getUserType());
        searchParams.put("username", student.getSfzh());
        searchParams.put("XJZT", student.getXjzt());
        searchParams.put("KKZY", student.getMajor());
        //searchParams.put("PYCC", user.getGjtStudentInfo().getPycc());
        searchParams.put("XX_ID", ObjectUtils.toString(student.getXxId()));
        searchParams.put("SCHOOL_MODEL", ObjectUtils.toString(item.getSchoolModel(), "")); //返回院校模式(0:非院校，1：院校)
        final Map resultMap = examServeService.queryAppointmentExam(searchParams);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        outside:
        for(Map<String, Object> term : (List<Map<String, Object>>) resultMap.get("LIST")) {
            for (Map<String, Object> e : (List<Map<String, Object>>) term.get("APPOINTMENTLIST")) {
                if(recId.equals(e.get("REC_ID"))) {
                    String examPlanId = (String) e.get("EXAM_PLAN_ID");
                    Map<String, Object> searchParams2 = new HashMap<String, Object>();
                    searchParams2.put("EQ_examPlanId", examPlanId);
                    Page<GjtExamAppointmentNew> page = gjtExamAppointmentNewService.queryByPage(searchParams2, new PageRequest(0, 10));
                    for (Iterator<GjtExamAppointmentNew> iter = page.iterator(); iter.hasNext();) {
                        GjtExamAppointmentNew appointment = iter.next();
                        Map<String, Object> info = new HashMap<String, Object>();
                        info.put("avatar", appointment.getStudent().getAvatar());
                        info.put("sfzh", appointment.getStudent().getSfzh());
                        list.add(info);
                    }
                    break outside;
                }
            }
        }
        result.put("list", list);
        return result;
    }

	/**
	 * 预约考试
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/makeAppointment", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> makeAppointment(HttpServletRequest request, HttpSession session) throws CommonException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        final GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
        GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(student.getXxId(), PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		String examBatchCode = request.getParameter("examBatchCode");
		String recIdArr = request.getParameter("recId");
		String examPlanIdArr = request.getParameter("examPlanId");
		if (StringUtils.isBlank(examBatchCode) || StringUtils.isBlank(recIdArr) || StringUtils.isBlank(examPlanIdArr)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数");
		}
		Map result = new HashMap();
		try {
			// 预约多门选课
			List<String> recIds = Arrays.asList(recIdArr.split(","));
			List<String> examPlanIds = Arrays.asList(examPlanIdArr.split(","));

			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EXAM_BATCH_CODE", examBatchCode);
			searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
			searchParams.put("USER_TYPE", student.getUserType());
			searchParams.put("username", student.getSfzh());
			searchParams.put("XJZT", student.getXjzt());
			searchParams.put("KKZY", student.getMajor());
			//searchParams.put("PYCC", user.getGjtStudentInfo().getPycc());
			searchParams.put("XX_ID", ObjectUtils.toString(student.getXxId()));
			searchParams.put("SCHOOL_MODEL", ObjectUtils.toString(item.getSchoolModel(), "")); //返回院校模式(0:非院校，1：院校)
			final Map resultMap = examServeService.queryAppointmentExam(searchParams);
			// 1.获取需要缴费的课程
			List<Map<String, Object>> payList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> term : (List<Map<String, Object>>) resultMap.get("LIST")) {
				for (Map<String, Object> e : (List<Map<String, Object>>) term.get("APPOINTMENTLIST")) {
					int payState = NumberUtils.toInt((String) e.get("PAY_STATE")); // 缴费状态：0 待缴费 1 已交费 2 无需缴费
					if (payState == 0) {
						Map<String, Object> payItem = new HashMap<String, Object>();
						payItem.put("recId", e.get("REC_ID"));
						payItem.put("courseName", e.get("COURSE_NAME"));
						String courseCost = gjtCourseService.queryExamFeeByKch((String) e.get("KCH"));
						payItem.put("price", courseCost);
						payList.add(payItem);
					}
				}
			}

			// 2.判断是否需要支付
			boolean isPay = false;
			int payCount = 0;
			String title = "补考费-"; // 缴费标题
			double price = 0; // 缴费金额
			List<Map> notifyDataList = new ArrayList<Map>();
			for (Map e : payList) {
				for (int i = 0; i < recIds.size(); i++) {
					String recId = recIds.get(i);
					String examPlanId = examPlanIds.get(i);

					if (StringUtils.equals(recId, (String) e.get("recId"))) {
						if (!isPay) {
							title += e.get("courseName");
						}
						price += NumberUtils.toDouble(e.get("price") + "");
						isPay = true;
						payCount++;
						Map notifyData = new HashMap();
						notifyData.put("recId", recId);
						notifyData.put("examPlanId", examPlanId);
						notifyDataList.add(notifyData);
						break;
					}
				}
			}
			if (payCount > 1) {
				title += "等" + payCount + "门课程";
			}

			// 3.需支付的生成支付链接，无需支付的直接预约
			if (isPay) {
				// 生成补考费支付链接
				GjtStudentInfo info = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());
				Map<String, String> params = new HashMap<String, String>();
				params.put("order_sn", ObjectUtils.toString(info.getGjtSignup().getOrderSn())); // 订单号
				String paySn = OrderType.EXAM_PAY.getCode() + codeGeneratorService.codeGenerator(CacheConstants.EXAM_PAY_ORDER_NO, 14);
				params.put("pay_sn", paySn); // 缴费单号,15位字符,由调用方生成
				params.put("title", title); // 缴费标题
				params.put("price", price + ""); // 缴费金额
				params.put("return_", String.format("%s%s?data=%s",
						pcenterStudyServer, pcenterStudyServerPayExamFeeReturn,
						URLEncoder.encode(GsonUtils.toJson(notifyDataList), Constants.CHARSET))); // 同步回调地址
				params.put("notify_", String.format("%s%s?data=%s",
						pcenterStudyServer, pcenterStudyServerPayExamFeeNotify,
						URLEncoder.encode(GsonUtils.toJson(notifyDataList), Constants.CHARSET))); // 异步回调地址
				String payUrl = ExamPayUtil.getExamPayUrl(params, info.getGjtSchoolInfo().getGjtOrg().getCode());

				// 1.记录下支付订单日志
				for (Map notifyData : notifyDataList) {
					String recId = (String) notifyData.get("recId");
					String examPlanId = (String) notifyData.get("examPlanId");

					GjtRecResult recResult = gjtRecResultService.queryById(recId);
					GjtCourse course = gjtCourseService.queryById(recResult.getCourseId());
					String courseCost = gjtCourseService.queryExamFeeByKch(course.getKch());

					GjtExamCost examCost = new GjtExamCost();
					examCost.setCostId(UUIDUtils.random());
					examCost.setPaySn(paySn);
					examCost.setStudentId(recResult.getStudentId());
					examCost.setCourseId(recResult.getCourseId());
					GjtExamPlanNew examPlanNew = gjtExamPlanNewService.queryById(examPlanId);
					examCost.setGradeId(examPlanNew.getGradeId());
					examCost.setTeachPlanId(recResult.getTeachPlanId());
					examCost.setExamBatchCode(examPlanNew.getExamBatchCode());
					examCost.setExamPlanId(examPlanNew.getExamPlanId());
					examCost.setKsfs(examPlanNew.getType() + "");
					examCost.setMakeup("1");
					examCost.setPayStatus("1");
					examCost.setCourseCost(courseCost);
					examCost.setCourseCode(course.getKch());
					gjtExamCostService.insert(examCost);
				}

				result.put("result", 2);
				result.put("payUrl", payUrl);
			} else {
				for (int i = 0; i < recIds.size(); i++) {
					String recId = recIds.get(i);
					String examPlanId = examPlanIds.get(i);

					Map<String, Object> searchParams2 = new HashMap<String, Object>();
					searchParams2.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
					searchParams2.put("EXAM_BATCH_CODE", examBatchCode);
					searchParams2.put("REC_ID", recId);
					searchParams2.put("EXAM_PLAN_ID", examPlanId);
					Map resultMap2 = examServeService.makeAppointment(searchParams2);
					if("3".equals(resultMap2.get("result"))) {
						throw new CommonException(MessageCode.BIZ_ERROR, "课程尚未缴费,请先缴费后再进行预约！");
					} else if("2".equals(resultMap2.get("result"))) {
						throw new CommonException(MessageCode.BIZ_ERROR, "最多只能预约" + WebConstants.EXAM_APPOINTMENT_LIMIT + "门考试！");
					} else if("4".equals(resultMap2.get("result"))) {
						throw new CommonException(MessageCode.BIZ_ERROR, "请先预约考点！");
					}
				}
				result.put("result", 1);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(MessageCode.SYSTEM_ERROR);
		}
		return result;
	}

	/**
	 * 取消预约考试
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/cancelAppointment", method = RequestMethod.POST)
	@ResponseBody
	public void cancelAppointment(HttpServletRequest request, HttpSession session) throws CommonException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String examBatchCode = request.getParameter("examBatchCode");
		String recId = request.getParameter("recId");
		String examPlanId = request.getParameter("examPlanId");
		if (StringUtils.isBlank(examBatchCode) || StringUtils.isBlank(recId) || StringUtils.isBlank(examPlanId)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数");
		}
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("EXAM_BATCH_CODE", examBatchCode);
		searchParams.put("REC_ID", recId);
		searchParams.put("EXAM_PLAN_ID", examPlanId);

		Map resultMap = examServeService.cancelAppointment(searchParams);
	}

	/**
	 * 考试中的课程
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/examCourse", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> examCourse(HttpServletRequest request, HttpSession session) throws CommonException {
		final GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		final GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		String examBatchCode = request.getParameter("examBatchCode");
		if(StringUtils.isBlank(examBatchCode)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数");
		}
		Map result = new HashMap();
		Date now = new Date();

		Map<String, Object> searchParams2 = new HashMap<String, Object>();
		searchParams2.put("EQ_examBatchCode", examBatchCode);
		searchParams2.put("EQ_studentId", student.getStudentId());
		List<Map<String, Object>> examList = gjtExamAppointmentNewService.queryExamAppointment(searchParams2);

		Map<String, Object> searchParams3 = new HashMap<String, Object>();
		searchParams3.put("EQ_examBatchCode", examBatchCode);
		searchParams3.put("EQ_studentId", student.getStudentId());
		List<GjtExamPointAppointmentNew> examPointList = gjtExamAppointmentNewService.queryExamPointAppointmentBy(searchParams3, null);
		Map<String, String> point = new HashMap<String, String>();
		for (GjtExamPointAppointmentNew e : examPointList) {
			point.put(e.getGjtExamPointNew().getExamType(), e.getGjtExamPointNew().getName());
			point.put("pointAddress" + e.getGjtExamPointNew().getExamType(), e.getGjtExamPointNew().getAddress());
		}

		List<Map<String, Object>> examCourse = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> e : examList) {
			Map<String, Object> examCourseMap = new HashMap<String, Object>();
			String courseId = (String) e.get("courseId");
			examCourseMap.put("termCourseId", e.get("termCourseId"));
			examCourseMap.put("courseId", courseId);
			examCourseMap.put("courseName", e.get("courseName"));
			examCourseMap.put("type", e.get("type"));
			examCourseMap.put("typeName", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.EXAMINATIONMODE, e.get("type") + ""));
			examCourseMap.put("learningStyle", e.get("learningStyle"));
			Date examSt = (Date) e.get("examSt");
			Date examEnd = (Date) e.get("examEnd");
			examCourseMap.put("examSt", examSt);
			examCourseMap.put("examEnd", examEnd);
			examCourseMap.put("pointName", point.get(e.get("type") + ""));
			examCourseMap.put("pointAddress", point.get("pointAddress" + e.get("type")));
			examCourseMap.put("xkPercent", e.get("xkPercent"));
			examCourseMap.put("ksPercent", e.get("ksPercent"));
			examCourseMap.put("examScore", e.get("examScore")); // 平时成绩
            if(examSt == null) {
                examCourseMap.put("examType", 1);
                examCourseMap.put("examDay", "--");
            } else if(now.before(examSt)) {
				examCourseMap.put("examType", 1);
				examCourseMap.put("examDay", DateUtil.diffDays(DateUtil.strToYYMMDDDate(DateFormatUtils.ISO_DATE_FORMAT.format(now)), DateUtil.strToYYMMDDDate(DateFormatUtils.ISO_DATE_FORMAT.format(examSt))));
			} else if(now.before(examEnd)) {
				examCourseMap.put("examType", 2);
				examCourseMap.put("examEndDay", DateUtil.diffDays(DateUtil.strToYYMMDDDate(DateFormatUtils.ISO_DATE_FORMAT.format(now)), DateUtil.strToYYMMDDDate(DateFormatUtils.ISO_DATE_FORMAT.format(examEnd))));
			} else {
				examCourseMap.put("examType", 3);
			}
            examCourseMap.put("recId", e.get("recId"));
            examCourseMap.put("examPlanId", e.get("examPlanId"));
			GjtClassInfo courseClass = gjtClassInfoService.findCourseClassByStudentIdAndCourseId(student.getStudentId(), courseId);
			examCourseMap.put("courseClassId", courseClass != null ? courseClass.getClassId() : null);
			examCourse.add(examCourseMap);
		}

		result.put("examCourse", examCourse);
		return result;
	}

    /**
     * 成绩登记中/已登记
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value = "/checkScore", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> checkScore(HttpServletRequest request, HttpSession session) throws CommonException {
        final GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        final GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
        String examBatchCode = request.getParameter("examBatchCode");
        if(StringUtils.isBlank(examBatchCode)) {
            throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数");
        }
        Map result = new HashMap();
        Date now = new Date();

        Map<String, Object> searchParams2 = new HashMap<String, Object>();
        searchParams2.put("EQ_examBatchCode", examBatchCode);
        searchParams2.put("EQ_studentId", student.getStudentId());
        List<Map<String, Object>> examList = gjtExamAppointmentNewService.queryExamAppointment(searchParams2);

        Map<String, Object> searchParams3 = new HashMap<String, Object>();
        searchParams3.put("EQ_examBatchCode", examBatchCode);
        searchParams3.put("EQ_studentId", student.getStudentId());
        List<GjtExamPointAppointmentNew> examPointList = gjtExamAppointmentNewService.queryExamPointAppointmentBy(searchParams3, null);
        Map<String, String> point = new HashMap<String, String>();
        for (GjtExamPointAppointmentNew e : examPointList) {
			point.put(e.getGjtExamPointNew().getExamType(), e.getGjtExamPointNew().getName());
			point.put("pointAddress" + e.getGjtExamPointNew().getExamType(), e.getGjtExamPointNew().getAddress());
        }

        List<Map<String, Object>> examCourse = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> e : examList) {
            Map<String, Object> examCourseMap = new HashMap<String, Object>();
            examCourseMap.put("courseName", e.get("courseName"));
            examCourseMap.put("type", e.get("type"));
            examCourseMap.put("typeName", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.EXAMINATIONMODE, e.get("type") + ""));
            examCourseMap.put("learningStyle", e.get("learningStyle"));
            Date examSt = (Date) e.get("examSt");
            Date examEnd = (Date) e.get("examEnd");
            examCourseMap.put("examSt", examSt);
            examCourseMap.put("examEnd", examEnd);
			examCourseMap.put("pointName", point.get(e.get("type") + ""));
			examCourseMap.put("pointAddress", point.get("pointAddress" + e.get("type")));
            examCourseMap.put("xkPercent", e.get("xkPercent"));
            examCourseMap.put("ksPercent", e.get("ksPercent"));
            examCourseMap.put("examScore", e.get("examScore")); // 平时成绩
            examCourseMap.put("examScore1", e.get("examScore1")); // 考试成绩
            examCourseMap.put("examScore2", e.get("examScore2")); // 综合成绩
            String score = (String) e.get("score");
            examCourseMap.put("score", score);
			String examStatus = (String) e.get("examStatus"); // 考试状态，0为待开始，1为已通过，2为未通过，3为考试中，4为待批改，5为已截止
            if(StringUtils.isBlank(score)) {
                examCourseMap.put("examType", 3);
            } else if(Constants.BOOLEAN_1.equals(examStatus)) {
				examCourseMap.put("examType", 1);
			} else if("2".equals(examStatus)) {
				examCourseMap.put("examType", 2);
			} else {
				examCourseMap.put("examType", 3);
			}
            examCourseMap.put("recId", e.get("recId"));
            examCourseMap.put("examPlanId", e.get("examPlanId"));
            examCourse.add(examCourseMap);
        }

        result.put("examCourse", examCourse);
        return result;
    }

}
