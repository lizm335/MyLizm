package com.ouchgzee.study.web.controller.exam;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.common.PdfFileExport;
import com.gzedu.xlims.common.PlatfromTypeEnum;
import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.ToolUtil;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.common.constants.CacheConstants.OrderType;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.ViewStudentInfoDao;
import com.gzedu.xlims.dao.exam.GjtExamPointNewDao;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtRecResult;
import com.gzedu.xlims.pojo.GjtSetOrgCopyright;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.ViewStudentInfo;
import com.gzedu.xlims.pojo.exam.GjtExamCorrectPapers;
import com.gzedu.xlims.pojo.exam.GjtExamCost;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.exam.GjtExamCorrectPapersService;
import com.gzedu.xlims.service.exam.GjtExamCostService;
import com.gzedu.xlims.service.exam.GjtExamPlanNewService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSetOrgCopyrightService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.serviceImpl.exam.wk.GjtExamWkService;
import com.itextpdf.text.DocumentException;
import com.ouchgzee.study.service.exam.ExamServeService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.common.Servlets;
import com.ouchgzee.study.web.vo.AdminssionVo;

import sun.misc.BASE64Encoder;


@Controller
@RequestMapping("/pcenter/exam")
public class ExamServeController extends BaseController {

	private static Log log = LogFactory.getLog(ExamServeController.class);

	private static final String ENTER_EXAM = "/exam/examIndex/login.do";

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
	private CodeGeneratorService codeGeneratorService;

	@Value("#{configProperties['pcenterStudyServer']}")
	private String pcenterStudyServer;

	@Value("#{configProperties['pcenterStudyServer.pay.examFeeNotify']}")
	private String pcenterStudyServerPayExamFeeNotify;

	@Value("#{configProperties['pcenterStudyServer.pay.examFeeReturn']}")
	private String pcenterStudyServerPayExamFeeReturn;

	// 缴费单号 存储到session中
	private static final String EXAM_PAY_SN = "PAY_SN";

	/**
	 * 查看考试须知
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/attention")
	@ResponseBody
	public Map<String, Object> examAttention(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", ObjectUtils.toString(student.getXxId()));
		Map resultMap = examServeService.examAttention(searchParams);

		return resultMap;
	}

	/**
	 * 查询准考证信息
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/examAdmissionInfo")
	@ResponseBody
	public Map<String, Object> examAdmissionInfo(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("XX_ID", ObjectUtils.toString(student.getXxId()));
		Map resultMap = examServeService.examAdmissionInfo(searchParams);

		return resultMap;
	}
	
	
	/**
	 * 查询考点信息列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 * @throws CommonException
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/queryPointList")
	@ResponseBody
	public Map<String, Object> queryPointList(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize, Model model, HttpServletRequest request,
			HttpSession session) throws CommonException, UnsupportedEncodingException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		String examBatchCode = (String) searchParams.get("EXAM_BATCH_CODE");// 考试计划编码
		String examType = (String) searchParams.get("EXAM_TYPE");// 考试类型 8:笔试；11:机考
		if(StringUtils.isBlank(examBatchCode) || StringUtils.isBlank(examType)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数");
		}
		searchParams.put("XX_ID", ObjectUtils.toString(student.getXxId()));
		String province_name = null;
		String city_name = null;
		String district_name = null;
		String point_name = null;
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PROVINCE_NAME")))){
			province_name = URLDecoder.decode(ObjectUtils.toString(searchParams.get("PROVINCE_NAME")),"UTF-8");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CITY_NAME")))){
			city_name = URLDecoder.decode(ObjectUtils.toString(searchParams.get("CITY_NAME")),"UTF-8");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("DISTRICT_NAME")))){
			district_name = URLDecoder.decode(ObjectUtils.toString(searchParams.get("DISTRICT_NAME")),"UTF-8");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("POINT_NAME")))){
			point_name = URLDecoder.decode(ObjectUtils.toString(searchParams.get("POINT_NAME")),"UTF-8");
		}
		searchParams.put("PROVINCE_NAME", province_name);
		searchParams.put("CITY_NAME", city_name);
		searchParams.put("DISTRICT_NAME", district_name);
		searchParams.put("POINT_NAME", point_name);
		GjtOrg gjtOrg = GjtOrgService.queryStudyCenterInfo(user.getGjtStudentInfo().getXxzxId(), "3"); // 对应的学习中心
		searchParams.put("studyCenterId", gjtOrg.getId()); // 学习中心
		searchParams.put("zsdId", user.getGjtStudentInfo().getXxzxId()); // 有可能是招生点
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = examServeService.queryPointList(searchParams, pageRequst);
		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("pageInfo", pageInfo);

		return resultMap;

	}
	
	
	/**
	 * 待预约考试
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/appointmentExam")
	@ResponseBody
	public Map<String, Object> appointmentExam(Model model, HttpServletRequest request, HttpSession session) {
		final GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		final GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(student.getXxId(), PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("USER_TYPE", student.getUserType());
		searchParams.put("username", student.getSfzh());
		searchParams.put("XJZT", student.getXjzt());
		searchParams.put("KKZY", student.getMajor());
		//searchParams.put("PYCC", user.getGjtStudentInfo().getPycc());
		searchParams.put("XX_ID",ObjectUtils.toString(student.getXxId()));
		searchParams.put("SCHOOL_MODEL",ObjectUtils.toString(item.getSchoolModel(),"")); //返回院校模式(0:非院校，1：院校)

		/*

		Map resultMap = new LinkedHashMap();


		if(!"1".equals(ObjectUtils.toString(item.getSchoolModel()))){
			resultMap = examServeService.queryAppointmentExam(searchParams);
		}else {
			resultMap = examServeService.queryAppointmentExamByAcadeMy(searchParams);
		}
		*/

		final Map resultMap = examServeService.queryAppointmentExam(searchParams);
		// 20171127 特殊处理麦当劳项目2016秋学员期末考试预约工作，全部无需缴费，预约期过了就可以去掉代码，没办法老是不按流程来
		if(StringUtils.equals((String) searchParams.get("EXAM_BATCH_CODE"), "201711220006")) {
			List<Map<String, Object>> termList = (List<Map<String, Object>>) resultMap.get("LIST");
			for (Map<String, Object> term : termList) {
				List<Map<String, Object>> appointmentList = (List<Map<String, Object>>) term.get("APPOINTMENTLIST");
				for (Map<String, Object> appointment : appointmentList) {
					// 未缴费的
					if ("0".equals(appointment.get("PAY_STATE"))) {
						appointment.put("PAY_STATE", "2");
						String recId = (String) appointment.get("REC_ID");
						GjtRecResult recResult = gjtRecResultService.queryById(recId);
						recResult.setPayState("2");
						gjtRecResultService.save(recResult);
					}
				}
			}
		}
		return resultMap;
	}

	@RequestMapping(value = "/createOrder")
	public void createOrder(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		String url = null;
		String recId = null;
		String examPlanId = null;
		try {
			String data = (String) searchParams.remove("data");
			List<Map> notifyDataList = null;
			if(StringUtils.isNotBlank(data)) {
				notifyDataList = GsonUtils.toBean(data, List.class);
				Map notifyData = notifyDataList.get(0);
				recId = (String) notifyData.get("recId");
				examPlanId = (String) notifyData.get("examPlanId");
			} else {
				return;
			}
			// 生成补考费支付链接
			GjtStudentInfo info = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());
			GjtRecResult recResult = gjtRecResultService.queryById(recId);
			GjtCourse course = gjtCourseService.queryById(recResult.getCourseId());
			String courseCost = gjtCourseService.queryExamFeeByKch(course.getKch());
			Map<String, String> params = new HashMap<String, String>();
			params.put("order_sn", ObjectUtils.toString(info.getGjtSignup().getOrderSn())); // 订单号
			String paySn = OrderType.EXAM_PAY.getCode() + codeGeneratorService.codeGenerator(CacheConstants.EXAM_PAY_ORDER_NO, 14);
			params.put("pay_sn", paySn); // 缴费单号,15位字符,由调用方生成
			params.put("title", "补考费-" + course.getKcmc()); // 缴费标题
			params.put("price", courseCost); // 缴费金额
			params.put("return_", String.format("%s%s?data=%s",
					pcenterStudyServer, pcenterStudyServerPayExamFeeReturn,
					URLEncoder.encode(GsonUtils.toJson(notifyDataList), Constants.CHARSET))); // 同步回调地址
			params.put("notify_", String.format("%s%s?data=%s",
					pcenterStudyServer, pcenterStudyServerPayExamFeeNotify,
					URLEncoder.encode(GsonUtils.toJson(notifyDataList), Constants.CHARSET))); // 异步回调地址
			url = ExamPayUtil.getExamPayUrl(params, info.getGjtSchoolInfo().getGjtOrg().getCode());

			// 1.记录下支付订单日志
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
			session.setAttribute(EXAM_PAY_SN, paySn); // 缴费单号存储到session中
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 2.跳转到支付
		super.outputJs(response, "window.location.href='" + url + "';");
	}
	
	/**
	 * 缴费完成,更新缴费状态<br/>
	 * 由于使用了新的支付接口，异步通知，所以不需要再查一遍，20171012暂停调用
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/accessPay")
	@ResponseBody
	public Map<String, Object> accessPay(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("PAY_SN", session.getAttribute(EXAM_PAY_SN));
		Map resultMap = examServeService.appointPay(searchParams);
		return resultMap;
	}
	
	
	/**
	 * 查询考试计划
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/getExamBatchData")
	@ResponseBody
	public Map<String, Object> getExamBatchData(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(student.getXxId(), PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("XX_ID", student.getXxId());
		searchParams.put("SCHOOL_MODEL",ObjectUtils.toString(item.getSchoolModel())); //返回院校模式(0:非院校，1：院校)

		Map resultMap = examServeService.getExamBatchData(searchParams);

		return resultMap;
		
	}

	/**
	 * 我的考试
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/myExamData")
	@ResponseBody
	public Map<String, Object> myExamData(Model model, HttpServletRequest request, HttpSession session) {
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		final GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		final GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);

		searchParams.put("USER_NO", student.getXh());
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(student.getXxId(), PlatfromTypeEnum.PERCENTPLATFORM.getNum());
//		searchParams.put("SCHOOL_MODEL",ObjectUtils.toString(item.getSchoolModel(),"")); //返回院校模式(0:非院校，1：院校)
		/*
		Map resultMap = new LinkedHashMap();

		if(!"1".equals(ObjectUtils.toString(item.getSchoolModel()))){
			resultMap = examServeService.myExamDataList(searchParams);
		}else {
			resultMap = examServeService.myExamDataListByAcadeMy(searchParams);
		}*/

		Map resultMap = examServeService.myExamDataList(searchParams);
		
		//异步同步网考、省网考考试成绩
		new Thread(new Runnable() {
			@Override
			public void run() {
				Map searchParams2 = new HashMap();
				searchParams2.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
				examServeService.updateExamScore(searchParams2);
			}
		}).start();
		
		return resultMap;
	}

	/**
	 * 上传报告
	 * @throws CommonException
	 */
	@RequestMapping(value = "/correctPapers/upload", method = RequestMethod.POST)
	@ResponseBody
	public void correctPapersUpload(HttpServletRequest request, HttpSession session, Model model) throws CommonException {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String examPlanId = request.getParameter("examPlanId");
			String recId = request.getParameter("recId");
			String papersFileName = request.getParameter("papersFileName");
			String papersFile = request.getParameter("papersFile");
			if(StringUtils.isBlank(examPlanId) || StringUtils.isBlank(recId) || StringUtils.isBlank(papersFileName) || StringUtils.isBlank(papersFile)) {
				throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数");
			}

			GjtExamPlanNew examPlan = gjtExamPlanNewService.queryById(examPlanId);
			String examBatchCode = examPlan.getExamBatchCode();
			GjtExamCorrectPapers examCorrectPapers = gjtExamCorrectPapersService.findByStudentIdAndExamPlanId(user.getGjtStudentInfo().getStudentId(), examPlanId);
			// 可以多次上传报告
			if(examCorrectPapers != null) {
				examCorrectPapers.setPapersFileName(papersFileName);
				examCorrectPapers.setPapersFile(papersFile);
				examCorrectPapers.setUpdatedBy(user.getGjtStudentInfo().getStudentId());
				gjtExamCorrectPapersService.update(examCorrectPapers);
			} else {
				GjtExamCorrectPapers entity = new GjtExamCorrectPapers();
				entity.setId(UUIDUtils.random());
				entity.setExamBatchCode(examBatchCode);
				entity.setGjtStudentInfo(new GjtStudentInfo(user.getGjtStudentInfo().getStudentId()));
				entity.setGjtExamPlanNew(new GjtExamPlanNew(examPlanId));
				entity.setRecId(recId);
				entity.setPapersFileName(papersFileName);
				entity.setPapersFile(papersFile);
				entity.setCreatedBy(user.getGjtStudentInfo().getStudentId());
				gjtExamCorrectPapersService.insert(entity);
			}
		} catch (CommonException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(MessageCode.SYSTEM_ERROR, "系统繁忙，请稍后重试！");
		}
	}

	/**
	 * 查看报告
	 * @throws CommonException
	 */
	@RequestMapping(value = "/correctPapers/view", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> correctPapersView(HttpServletRequest request, HttpSession session, Model model) throws CommonException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String examPlanId = request.getParameter("examPlanId");
		if(StringUtils.isBlank(examPlanId)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数");
		}
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			GjtExamPlanNew examPlan = gjtExamPlanNewService.queryById(examPlanId);
			result.put("documentFileName", examPlan.getDocumentFileName());
			result.put("documentFilePath", examPlan.getDocumentFilePath());
			GjtExamCorrectPapers examCorrectPapers = gjtExamCorrectPapersService.findByStudentIdAndExamPlanId(user.getGjtStudentInfo().getStudentId(), examPlanId);
			if(examCorrectPapers != null) {
				result.put("correctState", examCorrectPapers.getCorrectState()); // 报告状态，默认0 (0-待上传 1-待评分 2-已通过 3-未通过)
				result.put("score", examCorrectPapers.getScore());
				result.put("papersFileName", examCorrectPapers.getPapersFileName());
				result.put("papersFile", examCorrectPapers.getPapersFile());
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(MessageCode.SYSTEM_ERROR, "系统繁忙，请稍后重试！");
		}
	}
	
	/**
	 * 准备考试
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/entryExam")
	@ResponseBody
	public Map<String, Object> entryExam(Model model,HttpServletRequest request,HttpServletResponse response,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		
		Map resultMap = examServeService.entryExam(searchParams);
		
		return resultMap;
		
	}
	
	/**
	 * 统计考试数据
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/countExamDataInfo")
	@ResponseBody
	public Map<String, Object> countExamDataInfo(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("KKZY", student.getMajor());
		searchParams.put("XX_ID", student.getXxId());
		//searchParams.put("PYCC", user.getGjtStudentInfo().getPycc());
		
		Map resultMap = examServeService.countExamDataInfo(searchParams);
		
		return resultMap;
		
	}
	

	/**
	 * 进入考试
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/loginExamCenter")
	@ResponseBody
	public Map<String, Object> loginExamCenter(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		
		Map temp = examServeService.getSignUpId(searchParams);
		String signUpId = ObjectUtils.toString(temp.get("SIGNUP_ID"));
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if("19".equals(ObjectUtils.toString(searchParams.get("KSFS_FLAG")))){
			// 网考（省）指向新的考试平台进行考试【http://exam.oucnet.cn/ks
			String url = ObjectUtils.toString(AppConfig.getProperty("examOucnet_ks"));
			map.put("action", url);
			map.put("CHOOSE_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
			return map;
		}
		
		Map appointment = new HashMap();
		appointment.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		appointment.put("TEACH_PLAN_ID", ObjectUtils.toString(searchParams.get("TEACH_PLAN_ID")));
		Map appointmentNew = examServeService.queryGjtExamAppointmentNewData(appointment);
		if(appointmentNew == null){
			map.put("successful", false);
			map.put("messageInfo", "该课程未进行预约");
			return map;
		}
		
		String examNo = ObjectUtils.toString(appointmentNew.get("EXAM_NO"));
		String xxId = ObjectUtils.toString(appointmentNew.get("XX_ID"));
		
		ViewStudentInfo studentInfo = viewStudentInfoDao.findOne(ObjectUtils.toString(user.getGjtStudentInfo().getStudentId()));
		if (EmptyUtils.isEmpty(examNo)) {
			map.put("successful", false);
			map.put("messageInfo", "错误,同步考试平台失败：试卷号不能为空");
			return map;
		}
		try {
			gjtExamWkService.initWk(examNo, ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")),
					studentInfo.getStudentId(), studentInfo.getXm(), ObjectUtils.toString(searchParams.get("REC_ID")));
			
			map.put("successful", true);
			String url = AppConfig.getProperty("examServer") + AppConfig.getProperty("api.exam.login");
			map.put("action", url);
			map.put("CHOOSE_ID", ObjectUtils.toString(searchParams.get("REC_ID")));

			return map;
		} catch (RuntimeException e) {
			map.put("successful", false);
			map.put("messageInfo", "错误,同步考试平台失败："+e.getMessage());
			return map;
		}catch(Exception e){
			map.put("successful", false);
			map.put("messageInfo", "异常,同步考试平台失败："+e.getMessage());
			return map;
		}
		
	}

	/**
	 * 查询学员个人证件照
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getPersonalZP")
	@ResponseBody
	public Map<String, Object> getPersonalZP(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());

		Map resultMap = examServeService.getPersonalZP(searchParams);

		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/updatePersonalZP")
	@ResponseBody
	public Map<String, Object> updatePersonalZP(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("ID_NO", student.getSfzh());

		Map resultMap = examServeService.updatePersonalZP(searchParams);

		return resultMap;
	}

	/**
	 * 预约考试
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/makeAppointment")
	@ResponseBody
	public Map<String, Object> makeAppointment(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("XX_ID", student.getXxId());
		searchParams.put("EXAM_STATE", "1");
		searchParams.put("IS_CANCEL", "0");

		Map resultMap = examServeService.makeAppointment(searchParams);

		return resultMap;

	}

	/**
	 * 取消预约考试
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/cancelAppointment")
	@ResponseBody
	public Map<String, Object> cancelAppointment(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("XX_ID", student.getXxId());

		Map resultMap = examServeService.cancelAppointment(searchParams);

		return resultMap;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
//	@RequestMapping(value = "/expAdmissionTicket")
	public void expAdmissionTicket(Model model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws DocumentException, IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("XX_ID", ObjectUtils.toString(student.getXxId()));
		
		Map resultMap = examServeService.getAdmissionData(searchParams);
		String pdfFilePath = null;
		String outputFilePath = request.getSession().getServletContext().getRealPath("");
		
		if(EmptyUtils.isNotEmpty(resultMap)){
			List list = (List) resultMap.get("LIST");
			
			if(EmptyUtils.isNotEmpty(list)){
				Map map = (Map) list.get(0);
				Map temp = new LinkedHashMap();
				temp.put("STUDENT_ID", map.get("STUDENT_ID"));
				temp.put("ADMISSION_ZKZ", map.get("ADMISSION_ZKZ"));
				temp.put("STU_PHOTO", map.get("STU_PHOTO"));
//				temp.put("STU_PHOTO", "http://eefile.download.eenet.com//interface/files2/xlims/image/2c711b2b13e06b8eb2449cc0efc6e178.png");
				temp.put("ADMISSION_NAME", map.get("ADMISSION_NAME"));
				temp.put("STU_NUMBER", map.get("STU_NUMBER"));
				temp.put("EXAM_POINT_NAME", map.get("EXAM_POINT_NAME"));
				temp.put("EXAM_ADDRESS", map.get("EXAM_ADDRESS"));
				
				List list1 = (List) map.get("ADMISSION");
				
				String[] str = new String[] {"试卷号","考试科目","考试形式","考试方式","考试日期","考试时间","考场及座位号"};
				
				PdfFileExport pdfFileExport = new PdfFileExport();
				
				String root = request.getSession().getServletContext().getRealPath("");
				
				pdfFilePath = (root + "/admiodf/"+student.getXh()+".pdf").replace("\\", "/").replace("//", "/");
				
				pdfFileExport.exportTableContent(pdfFilePath, str, 1, 1, list1, temp,outputFilePath);
				
				ToolUtil.download(pdfFilePath, response);
			}
			
		}
		
		
	}
	
	/*
	 * 获取下载文件的输入流对象，返回值是InputStream，给框架用
	 */
	public InputStream getAdmissionStream() {// 后
		return is;
	}

	private InputStream is;
	
	/**
	 * 考点预约
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/saveExamPointApp")
	@ResponseBody
	public Map saveExamPointApp(Model model,HttpServletRequest request,HttpSession session) throws CommonException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		String examBatchCode = (String) searchParams.get("EXAM_BATCH_CODE");// 考试计划编码
		String examPointId = (String) searchParams.get("EXAM_POINT_ID");// 考点ID
		if(StringUtils.isBlank(examBatchCode) || StringUtils.isBlank(examPointId)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数");
		}
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		if(examServeService.saveExamPointApp(searchParams)) {
			resultMap.put("result", "1");
		} else {
			resultMap.put("result", "0");
			resultMap.put("messageInfo", "考点信息保存失败!");
		}
		return resultMap;
	}
	
	/**
	 * 下载准考证
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/expAdmissionTicket")
	public void exportAdmissionTicket(Model model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		String examBatchCode = (String) searchParams.get("EXAM_BATCH_CODE");
		if(StringUtils.isBlank(examBatchCode)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数");
		}
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("XX_ID", student.getXxId());
		Map resultMap = examServeService.getAdmissionData(searchParams);
		if(EmptyUtils.isNotEmpty(resultMap)){
			List list = (List) resultMap.get("LIST");
			Map temp = new LinkedHashMap();
			if(EmptyUtils.isNotEmpty(list)){
				Map map = (Map) list.get(0);
				
				List<AdminssionVo> info = new ArrayList<AdminssionVo>();
				AdminssionVo vo = new AdminssionVo();
				for(int i=0;i<list.size();i++){
					Map tempMap = (Map) list.get(i);
					vo.setStudentId(ObjectUtils.toString(tempMap.get("STUDENT_ID")));
					vo.setAdmissionZkz(ObjectUtils.toString(tempMap.get("ADMISSION_ZKZ")));
					if(EmptyUtils.isNotEmpty(ObjectUtils.toString(tempMap.get("STU_PHOTO")))){
						vo.setStuPhoto(getImageString(ObjectUtils.toString(tempMap.get("STU_PHOTO"))));
//						vo.setStuPhoto(getImageString("http://eefile.download.eenet.com//interface/files2/xlims/image/2c711b2b13e06b8eb2449cc0efc6e178.png"));
					}else{
						vo.setStuPhoto("");
					}
					vo.setAdmissionName(ObjectUtils.toString(tempMap.get("ADMISSION_NAME")));
					vo.setStuNumber(ObjectUtils.toString(tempMap.get("STU_NUMBER")));
					vo.setExamPointName(ObjectUtils.toString(tempMap.get("EXAM_POINT_NAME")));
					vo.setExamAddress(ObjectUtils.toString(tempMap.get("EXAM_ADDRESS")));
					vo.setAdmissionList((List)tempMap.get("ADMISSION"));
					vo.setType(ObjectUtils.toString(tempMap.get("TYPE")));
					info.add(vo);
					
				}
				temp.put("infoList", info);
				
				List list1 = (List) map.get("ADMISSION");
				temp.put("ADMISSION", list1);
			}
			
			String basePath = request.getSession().getServletContext().getRealPath("");
			String path = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
					+ "exam" + File.separator;
			ExportDoc exportDoc = new ExportDoc();
			exportDoc.create(temp, basePath, path, response);
			
		}
		
	}
	
	
	/**
	 * 将本地、网络图片转换成BASE64字符串
	 * @param imageUrl
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("restriction")
	public static String getImageString(String imageUrl) throws IOException{
		InputStream in = null;
		byte[] data = null;
		
		try{
			URL url = new URL(imageUrl);
			URLConnection conn = url.openConnection(); //打开网络输入流
			
			InputStream inputStream = conn.getInputStream();
			data = readInputStream(inputStream);
		}catch(IOException e){
			throw e;
		}finally{
			if(in!=null){
				in.close();
			}
		}
		
		BASE64Encoder encoder = new BASE64Encoder();
		
		return data !=null ?encoder.encode(data):"";
		
	}
	
	/**
	 * 将网络图片流转换成数组 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) throws IOException{
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while((len=inputStream.read(buffer))!=-1){
			bos.write(buffer,0,len);
		}
		bos.close();
		return bos.toByteArray();
	}
	
	/**
	 * 网络图片下載到本地 
	 * @param imgUrl
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String downloadImg(String imgUrl,HttpServletRequest request) throws Exception{
		// 构造URL
		URL url = new URL(imgUrl); 
		// 打开连接  
		URLConnection con = url.openConnection(); 
		//设置请求超时为5s  
		con.setConnectTimeout(5*1000); 
		// 输入流  
		InputStream is = con.getInputStream(); 
		// 1K的数据缓冲  
		byte[] bs = new byte[1024];
		int len;
		Calendar cal = Calendar.getInstance();
		String savePath = request.getSession().getServletContext().getRealPath("") + File.separator + "tmp"
				+ File.separator + DateFormatUtils.ISO_DATE_FORMAT.format(cal);
		File folder = new File(savePath);
		if (!folder.exists()) {
			folder.mkdirs();
			log.error("创建文件 " + savePath);
		}
		String filename = UUIDUtils.random()+".jpg";
		String returnUrl = savePath + filename;
		// 输出的文件流 
		OutputStream os = new FileOutputStream(folder.getPath()+"\\"+filename);
		while ((len = is.read(bs)) != -1) {  
            os.write(bs, 0, len);  
        }  
        // 完毕，关闭所有链接 
        os.flush();
        os.close();  
        is.close(); 
        
        return returnUrl;
		 
	}


    /**
     * 查询往期考试记录
     * @param model
     * @param request
     * @param session
     * @return
     */
	@RequestMapping(value = "/getPastLearnRecord")
    @ResponseBody
	public Map<String,Object> getPastLearnRecord(Model model,HttpServletRequest request,HttpSession session){
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
        GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(student.getXxId(), PlatfromTypeEnum.PERCENTPLATFORM.getNum());
        Map searchParams = Servlets.getParametersStartingWith(request, "");
        searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
        searchParams.put("KKZY", student.getMajor());
        searchParams.put("XX_ID", student.getXxId());

        Map resultMap = examServeService.getPastLearnRecord(searchParams);

        return resultMap;

    }
	
	/**
     * 新增学生下载准考证次数
     * @param model
     * @param request
     * @param session
     * @return
     */
	@RequestMapping(value = "/saveAdmissionTicketCount")
    @ResponseBody
	public int saveAdmissionTicketCount(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
        Map searchParams = Servlets.getParametersStartingWith(request, "");
        searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
        searchParams.put("LOG_ID", SequenceUUID.getSequence());

        int result = examServeService.saveStudentDownToken(searchParams);

        return result;

    }

}
