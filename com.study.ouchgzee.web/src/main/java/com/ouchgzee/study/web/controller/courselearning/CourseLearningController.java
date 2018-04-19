package com.ouchgzee.study.web.controller.courselearning;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.PlatfromTypeEnum;
import com.gzedu.xlims.common.ToolUtil;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtFirstCourse;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtSetOrgCopyright;
import com.gzedu.xlims.pojo.GjtSpecialtyBase;
import com.gzedu.xlims.pojo.GjtSpecialtyVideo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtTermCourseinfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.openClass.GjtOnlineLessonFile;
import com.gzedu.xlims.pojo.openClass.LcmsOnlineLesson;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.edumanage.LcmsOnlineLessonService;
import com.gzedu.xlims.service.edumanage.OpenClassService;
import com.gzedu.xlims.service.graduation.GjtFirstCourseService;
import com.gzedu.xlims.service.organization.GjtCoachDateService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtSetOrgCopyrightService;
import com.gzedu.xlims.service.organization.GjtSpecialtyVideoService;
import com.gzedu.xlims.service.organization.GjtTermCourseinfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.ouchgzee.study.service.course.CourseLearningService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.common.Feedback;
import com.ouchgzee.study.web.common.FeedbackPage;
import com.ouchgzee.study.web.common.MessageCheck;
import com.ouchgzee.study.web.common.Servlets;

@Controller
@RequestMapping("/pcenter/course")
public class CourseLearningController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(CourseLearningController.class);

	private static final String KEY = "/book/index/student/urlLogin.do"; // 暂未用

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	CourseLearningService courseLearningService;

	@Autowired
	GjtRecResultService gjtRecResultService;

	@Autowired
	GjtSetOrgCopyrightService gjtSetOrgCopyrightService;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtSpecialtyVideoService gjtSpecialtyVideoService;

	@Autowired
	GjtFirstCourseService gjtFirstCourseService;

	@Autowired
	GjtGradeService gjtGradeService;

	@Autowired
	private OpenClassService openClassService;

	@Autowired
	private GjtCoachDateService gjtCoachDateService;

	@Autowired
	private GjtTermCourseinfoService gjtTermCourseinfoService;
	
	@Autowired
	private CacheService cacheService;

	@Autowired
	private LcmsOnlineLessonService lcmsOnlineLessonService;

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	/**
	 * 查询学期列表
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/learningByTerm")
	@ResponseBody
	public Map<String, Object> learningByTerm(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(student.getXxId(),
				PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("USER_TYPE", student.getUserType());
		searchParams.put("XH", student.getXh());
		searchParams.put("XJZT", student.getXjzt());
		searchParams.put("XX_ID", student.getXxId());
		searchParams.put("KKZY", student.getMajor());
		searchParams.put("SCHOOL_MODEL", ObjectUtils.toString(item.getSchoolModel(), "")); // 返回院校模式(0:非院校，1：院校)
		Map resultMap = courseLearningService.learningByTerm(searchParams);

		return resultMap;

	}

	/**
	 * 课程学习
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/courseLearning")
	@ResponseBody
	public Map<String, Object> courseLearning(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(student.getXxId(),
				PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("TERM_ID", ObjectUtils.toString(searchParams.get("TERM_ID")));
		searchParams.put("KKZY", student.getMajor());
		searchParams.put("XX_ID", student.getXxId());
		searchParams.put("CLASS_ID", super.getTeachClassId(session));
		searchParams.put("SCHOOL_MODEL", ObjectUtils.toString(item.getSchoolModel(), "")); // 返回院校模式(0:非院校，1：院校)
		// Map resultMap = courseLearningService.courseLearning(searchParams);

		searchParams.put("Fullname", student.getXm());
		searchParams.put("UserName", student.getXh());
		
		Map resultMap = courseLearningService.getCourseLearningData(searchParams);
		resultMap.put("USER_TYPE", student.getUserType());
		return resultMap;

	}

	/**
	 * 根据教学计划得到课程学习结果(已废弃)
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getCourseResult")
	@ResponseBody
	public Map<String, Object> getCourseResult(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(student.getXxId(),
				PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("XH", user.getGjtStudentInfo().getXh());
		searchParams.put("KKZY", student.getMajor());
		searchParams.put("PYCC", user.getGjtStudentInfo().getPycc());
		searchParams.put("SCHOOL_MODEL", ObjectUtils.toString(item.getSchoolModel(), "")); // 返回院校模式(0:非院校，1：院校)

		Map resultMap = courseLearningService.getCourseResult(searchParams);

		return resultMap;

	}

	/**
	 * 课程数据统计(提供给移动端)
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	@RequestMapping(value = "/getCourseAndTermData")
	@ResponseBody
	public Map<String, Object> getCourseAndTermData(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("XX_ID", student.getXxId());
		searchParams.put("KKZY", student.getMajor());
		searchParams.put("PYCC", user.getGjtStudentInfo().getPycc());
		Map resultMap = courseLearningService.getCourseAndTermData(searchParams);

		return resultMap;
	}

	/**
	 * 统计在学课程人数(已废弃)
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getPersonalCountByCourse")
	@ResponseBody
	public Map<String, Object> getPersonalCountByCourse(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("XX_ID", student.getXxId());
		searchParams.put("CLASS_IDS", searchParams.get("classIds"));
		searchParams.put("TERMCOURSE_IDS", searchParams.get("teachPlanIds"));

		Map resultMap = courseLearningService.getPersonalCountByCourse(searchParams);

		return resultMap;
	}

	/**
	 * 确认重修
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/learningRepair")
	@ResponseBody
	public Map<String, Object> learningRepair(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("XH", user.getGjtStudentInfo().getXh());
		searchParams.put("STUDENT_NAME", user.getGjtStudentInfo().getXm());
		searchParams.put("CHOOSE_ID", ObjectUtils.toString(searchParams.get("CHOOSE_ID")));
		searchParams.put("CREATED_BY", user.getGjtStudentInfo().getStudentId());
		searchParams.put("UPDATED_BY", user.getGjtStudentInfo().getStudentId());
		Map resultMap = courseLearningService.learningRepair(searchParams);

		return resultMap;

	}

	/**
	 * 5.7个人中心-首页-学习进度排名top5
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/studyRank")
	@ResponseBody
	public List<Map<String, Object>> getStudyRank(HttpServletRequest request, HttpSession session)
			throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(student.getXxId(),
				PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		String classId = super.getTeachClassId(session);
		searchParams.put("class_id", classId);
		searchParams.put("XX_ID", student.getXxId());
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());

		if (!"1".equals(ObjectUtils.toString(item.getSchoolModel()))) {
			if (EmptyUtils.isEmpty(searchParams.get("TERM_ID"))) {
				Map termMap = courseLearningService.getStudyRankByTerm(searchParams);
				searchParams.put("term_id", termMap.get("TERM_ID"));
			} else {
				searchParams.put("term_id", searchParams.get("TERM_ID"));
			}
		}

		List<Map<String, Object>> resultList = courseLearningService.getStudyRank(searchParams);

		return resultList;
	}

	/**
	 * 5.8个人中心-首页-成绩与学分查询
	 *
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/gradesAndRedits", method = RequestMethod.POST)
	public List<Map<String, Object>> gradesAndRedits(HttpServletRequest request) throws CommonException {

		// GjtUserAccount user = (GjtUserAccount)
		// request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo user = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		if (EmptyUtils.isEmpty(user)) {
			throw new CommonException(MessageCode.TOKEN_INVALID);
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		String studentId = ObjectUtils.toString(user.getStudentId(), "");
		if (EmptyUtils.isEmpty(studentId)) {
			throw new CommonException(MessageCode.TOKEN_INVALID);
		}

		try {
			List<Map<String, String>> list = gjtRecResultService.queryTeachTerm(studentId);// 获取教学班期信息
			if (EmptyUtils.isEmpty(list)) {
				return resultList;
			}
			for (Map entity : list) {
				Map<String, Object> mapResult = new LinkedHashMap<String, Object>();
				List<Map<String, Object>> list2 = new LinkedList<Map<String, Object>>();// 筛选目前需要的字段
				List<Map<String, String>> list1 = gjtRecResultService.queryTeachStudentSourceDetail(studentId,
						ObjectUtils.toString(entity.get("TERM_ID")));// 每个学期的明细
				if (EmptyUtils.isNotEmpty(list1)) {
					for (Map map : list1) {
						Map<String, Object> map2 = new LinkedHashMap<String, Object>();
						map2.put("TEACH_PLAN_ID", ObjectUtils.toString(map.get("TEACH_PLAN_ID")));
						map2.put("KCLBM_NAME", ObjectUtils.toString(map.get("KCLBM_NAME")));// 课程模块名称
						map2.put("KCMC", ObjectUtils.toString(map.get("KCMC")));// 课程名称
						map2.put("KCH", ObjectUtils.toString(map.get("KCH")));// 课程号
						if (EmptyUtils.isEmpty(map.get("EXAM_PLAN_KSFS_NAME"))) {
							map2.put("KSFS_NAME", ObjectUtils.toString(map.get("KSFS_NAME")));// 考试方式
						} else {
							map2.put("KSFS_NAME", ObjectUtils.toString(map.get("EXAM_PLAN_KSFS_NAME")));// 考试方式
						}
						map2.put("XF", ObjectUtils.toString(map.get("XF")));// 学分
						map2.put("GET_POINT", ObjectUtils.toString(map.get("GET_POINT")));// 已获得学分
						map2.put("XK_PERCENT", ObjectUtils.toString(map.get("XK_PERCENT")));
						map2.put("STUDY_SCORE", ObjectUtils.toString(map.get("STUDY_SCORE")));// 学习成绩
						double examScore = NumberUtils.toDouble(ObjectUtils.toString(map.get("EXAM_SCORE")));
						map2.put("EXAM_SCORE", ObjectUtils.toString(map.get("EXAM_SCORE")));// 考试成绩
						if (examScore < 0) {
							// 缺考等显示
							map2.put("EXAM_SCORE", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.EXAM_SCORE, ((int) examScore)+""));// 考试成绩
						}
						// 考试总成绩
						if (ToolUtil.isNumeric(ObjectUtils.toString(map.get("SUM_SCORE"))) && Double.parseDouble(ObjectUtils.toString(map.get("SUM_SCORE")))<0) {
							map2.put("SUM_SCORE", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.EXAM_SCORE, ObjectUtils.toString(map.get("SUM_SCORE"))));
						} else {
							map2.put("SUM_SCORE", ObjectUtils.toString(map.get("SUM_SCORE")));
						}
						map2.put("SCORE_STATE", ObjectUtils.toString(map.get("SCORE_STATE")));// 状态
						map2.put("EXAM_NUM", ObjectUtils.toString(map.get("EXAM_NUM")));
						map2.put("START_DATE", ObjectUtils.toString(map.get("START_DATE")));
						map2.put("END_DATE", ObjectUtils.toString(map.get("END_DATE")));
						map2.put("PROGRESS", ObjectUtils.toString(map.get("PROGRESS")));
						map2.put("LOGIN_COUNT", ObjectUtils.toString(map.get("LOGIN_COUNT")));
						map2.put("LOGIN_TIME", ObjectUtils.toString(map.get("LOGIN_TIME")));
						map2.put("IS_ONLINE", ObjectUtils.toString(map.get("IS_ONLINE")));
						map2.put("LEFT_DAY", ObjectUtils.toString(map.get("LEFT_DAY")));
						map2.put("BYOD_TYPE", ObjectUtils.toString(map.get("BYOD_TYPE")));
						list2.add(map2);
					}
				}
				mapResult.put("TERM_CODE", ObjectUtils.toString(entity.get("TERM_CODE"), ""));
				mapResult.put("TERM_NAME", ObjectUtils.toString(entity.get("TERM_NAME"), ""));
				mapResult.put("TERM_ID", ObjectUtils.toString(entity.get("TERM_ID"), ""));
				mapResult.put("IS_CURRENT", ObjectUtils.toString(entity.get("IS_CURRENT"), "N"));
				mapResult.put("TERM", list2);
				resultList.add(mapResult);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CommonException(MessageCode.SYSTEM_ERROR);
		}

		return resultList;
	}

	/**
	 * 成绩与学分成绩总览
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getResultsOverview", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> getResultsOverview(HttpServletRequest request) throws CommonException {
		GjtClassInfo classInfo = (GjtClassInfo) request.getSession().getAttribute("teach_class");
		Map<String, Object> seachParams = new HashMap<String, Object>();
		GjtStudentInfo user = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		String studentId = ObjectUtils.toString(user.getStudentId(), "");
		if (EmptyUtils.isEmpty(studentId)) {
			throw new CommonException(MessageCode.TOKEN_INVALID);
		}
		seachParams.put("STUDENT_ID", studentId);
		if (classInfo != null) {
			seachParams.put("CLASS_ID", classInfo.getClassId());
		}
		Map<String, Object> result;
		result = gjtRecResultService.queryResultsOverview(seachParams);
		return result;
	}

	/**
	 * 首页成绩与学分-学分详情-查看历史成绩
	 * 
	 * @return
	 */
	@RequestMapping(value = "/viewDetials", method = { RequestMethod.POST })
	@ResponseBody
	public List<Map<String, Object>> viewDetials(HttpServletRequest request, HttpSession session, String teachPlanId)
			throws CommonException {

		if (EmptyUtils.isEmpty(ObjectUtils.toString(teachPlanId, ""))) {
			throw new CommonException(MessageCode.BAD_REQUEST);
		}
		GjtStudentInfo student = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		if (EmptyUtils.isEmpty(student)) {
			throw new CommonException(MessageCode.TOKEN_INVALID);
		}
		Map<String, Object> formMap = new HashMap<String, Object>();
		formMap.put("teachPlanId", teachPlanId);
		formMap.put("studentId", student.getStudentId());
		List<Map<String, Object>> resultMap = null;
		try {
			resultMap = gjtRecResultService.getHistoryScore(formMap);
			for (Map<String, Object> map : resultMap) {
				List<Map> historyMsg = (List<Map>) map.get("HISTORY_MSG");
				if(resultMap != null &&  historyMsg.size() > 0) {
					for (Map map2 : historyMsg) {
						double examScore = NumberUtils.toDouble(ObjectUtils.toString(map2.get("ZJX_SCORE")));
						if (examScore < 0) {
							// 缺考等显示
							map2.put("ZJX_SCORE", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.EXAM_SCORE, ((int) examScore)+""));// 考试成绩
						}
						if (ToolUtil.isNumeric(ObjectUtils.toString(map2.get("ZCJ_SCORE"))) && Double.parseDouble(ObjectUtils.toString(map2.get("ZCJ_SCORE")))<0) {
							map2.put("ZCJ_SCORE", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.EXAM_SCORE, ObjectUtils.toString(map2.get("ZCJ_SCORE"))));// 考试成绩
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CommonException(MessageCode.SYSTEM_ERROR);
		}

		return resultMap;
	}

	/**
	 * 首页 成绩与学分--学分详情
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws CommonException
	 */
	@RequestMapping(value = "/getCreditDetail", method = { RequestMethod.POST })
	@ResponseBody
	public List<Map<String, String>> getCreditDetail(HttpServletRequest request, HttpSession session)
			throws CommonException {

		// GjtUserAccount user = (GjtUserAccount)
		// request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo user = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);

		if (EmptyUtils.isEmpty(user.getStudentId()) || EmptyUtils.isEmpty(user.getMajor())) {
			throw new CommonException(MessageCode.BAD_REQUEST);
		}
		// Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String, String>> result;
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("student_id", user.getStudentId());
			param.put("specialty_id", user.getMajor());
			result = gjtRecResultService.getCreditDetail(param);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CommonException(MessageCode.SYSTEM_ERROR);
		}
		return result;
	}

	/**
	 * 分页获取学员动态
	 *
	 * @param classId
	 *            班级id
	 * @param nomalcount
	 *            页码
	 * @param current
	 *            当前页
	 * @param isOnline
	 *            是否在线
	 * @param fromDyna
	 *            来源
	 * @return
	 */
	@RequestMapping(value = "/getStuLogList", method = RequestMethod.POST)
	@ResponseBody
	public FeedbackPage getStuLogList(@RequestParam("CLASS_ID") String classId,
			@RequestParam(value = "NOMALCOUNT", defaultValue = "6") int nomalcount,
			@RequestParam(value = "CURRENTPAGE", defaultValue = "1") int current,
			@RequestParam(value = "IS_ONLINE", required = false) String isOnline,
			@RequestParam("FROM_DYNA") String fromDyna) {
		System.out.println(current);
		List list = new ArrayList();
		FeedbackPage feed = new FeedbackPage();
		feed.setData(list);
		feed.setMsg("正常");
		feed.setResult(Feedback.Type.成功.getCode());
		feed.setCurrentPage(current);
		feed.setSize(list.size());
		feed.setNomalcount(nomalcount);
		return feed;
	}

	/**
	 * 课程学习(院校模式)
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/acadeMyLearnCourse")
	@ResponseBody
	public Map<String, Object> acadeMyLearnCourse(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtClassInfo classInfo = (GjtClassInfo) session.getAttribute(WebConstants.TEACH_CLASS);
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(student.getXxId(),
				PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("KKZY", student.getMajor());
		searchParams.put("XX_ID", student.getXxId());
		searchParams.put("CLASS_ID", super.getTeachClassId(session));
		searchParams.put("SCHOOL_MODEL", ObjectUtils.toString(item.getSchoolModel(), "")); // 返回院校模式(0:非院校，1：院校)
		// Map resultMap =
		// courseLearningService.acadeMyLearnCourse(searchParams);

		Map resultMap = courseLearningService.acadeMyLearnCourseData(searchParams);

		return resultMap;
	}

	@RequestMapping(value = "/acadeMyLearningByNotExam")
	@ResponseBody
	public Map<String, Object> acadeMyLearningByNotExam(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(student.getXxId(),
				PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		searchParams.put("XX_ID", student.getXxId());
		searchParams.put("CLASS_ID", super.getTeachClassId(session));
		searchParams.put("SCHOOL_MODEL", ObjectUtils.toString(item.getSchoolModel(), "")); // 返回院校模式(0:非院校，1：院校)

		Map resultMap = courseLearningService.acadeMyLearningByNotExam(searchParams);

		return resultMap;
	}

	/**
	 * 查看授课计划
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getGrantCoursePlan")
	@ResponseBody
	public Map<String, Object> getGrantCoursePlan(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XXZX_ID", student.getXxzxId());
		Map resultMap = courseLearningService.getGrantCousePlan(searchParams);

		return resultMap;
	}

	/**
	 * 查询授课凭证
	 * 
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getGrantCourseCert")
	@ResponseBody
	public Map<String, Object> getGrantCourseCert(Model model, HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		Map resultMap = courseLearningService.getGrantCourseCertData(searchParams);

		return resultMap;
	}

	/**
	 * 下载成绩单
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/downGradesExcel")
	public void downGradesExcel(Model model, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map formMap = new HashMap();
		formMap.put("STUDENT_ID", user.getGjtStudentInfo().getStudentId());
		String basePath = CourseLearningController.class.getClassLoader().getResource("").getPath();
		courseLearningService.downGradesExcel(formMap, request, response, basePath);
	}

	/**
	 * 查询专业介绍视频
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月30日 上午10:13:46
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/querySpecialtyVedio")
	@ResponseBody
	public Map<String, Object> querySpecialtyVedio(HttpServletRequest request, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("isView", true);
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(student.getStudentId());
		GjtSpecialtyBase specialty = gjtStudentInfo.getGjtSpecialty().getGjtSpecialtyBase();
		List<Map<String, Object>> videoList = new ArrayList<Map<String, Object>>();
		// 专业对应的开学第一课
		GjtFirstCourse firstCourse = gjtFirstCourseService.queryBySpecialtyBaseId(specialty.getSpecialtyBaseId());
		if (firstCourse != null) {
			result.put("content", firstCourse.getContent());
			for (GjtSpecialtyVideo video : firstCourse.getGjtSpecialtyVideoList()) {
				Map<String, Object> videlMap = new HashMap<String, Object>();
				videlMap.put("video", video.getVideoUrl());
				videlMap.put("title", video.getTitle());
				videoList.add(videlMap);
			}
		}

		// 公共视频
		List<GjtFirstCourse> courses = gjtFirstCourseService.queryByType(student.getXxId(), 1);
		if (CollectionUtils.isNotEmpty(courses)) {
			for (GjtFirstCourse c : courses) {
				for (GjtSpecialtyVideo video : c.getGjtSpecialtyVideoList()) {
					Map<String, Object> videlMap = new HashMap<String, Object>();
					videlMap.put("video", video.getVideoUrl());
					videlMap.put("title", video.getTitle());
					videoList.add(videlMap);
				}
			}
		}
		Object obj = gjtFirstCourseService.queryByStudentId(student.getStudentId());
		GjtGrade grade = gjtGradeService.findCurrentGrade(student.getXxId());
		String gradeId = grade == null ? null : grade.getGradeId();
		if (obj != null || videoList.isEmpty() || !student.getGjtGrade().getGradeId().equals(gradeId)) {// 存在观看记录，不需要再观看
			result.put("isView", false);
		}
		result.put("videoList", videoList);
		return result;
	}

	@RequestMapping(value = "/saveViewVedioRecord")
	@ResponseBody
	public void saveViewVedioRecord(HttpServletRequest request, HttpSession session) {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(student.getStudentId());
		GjtSpecialtyBase specialty = gjtStudentInfo.getGjtSpecialty().getGjtSpecialtyBase();
		// 专业对应的开学第一课
		GjtFirstCourse firstCourse = gjtFirstCourseService.queryBySpecialtyBaseId(specialty.getSpecialtyBaseId());
		if (firstCourse != null) {
			Object obj = gjtFirstCourseService.queryByFourseCourseIdAndStduentId(firstCourse.getId(),
					student.getStudentId());
			if (obj == null) {
				gjtFirstCourseService.saveFirstCourseStudent(firstCourse.getId(), student.getStudentId());
			}
		}

		// 公共视频
		List<GjtFirstCourse> courses = gjtFirstCourseService.queryByType(student.getXxId(), 1);
		if (CollectionUtils.isNotEmpty(courses)) {
			for (GjtFirstCourse c : courses) {
				Object obj = gjtFirstCourseService.queryByFourseCourseIdAndStduentId(c.getId(), student.getStudentId());
				if (obj == null) {
					gjtFirstCourseService.saveFirstCourseStudent(c.getId(), student.getStudentId());
				}
			}
		}

	}

	// 重修课程证码
	@ResponseBody
	@RequestMapping(value = "sendSmsCode")
	public void sendSmsCode(HttpServletRequest request, HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		MessageCheck.sendSmsCode(student.getSjh(), MessageCheck.REBUILDCODE, session);
	}

	@ResponseBody
	@RequestMapping(value = "doSmsValidateCode")
	public void doSmsValidateCode(String code, HttpSession session) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		MessageCheck.doSmsValidateCode(student.getSjh(), code, MessageCheck.REBUILDCODE, session);
	}

	/**
	 * 查询直播（首页）
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月9日 上午11:13:33
	 * @param request
	 * @param session
	 * @param lessonName
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryLiveVideoForIndex")
	public Map<String, Object> queryLiveVideoForIndex(HttpServletRequest request, HttpSession session, String lessonName, Integer status) {
		GjtStudentInfo student = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		PageRequest pageRequst = Servlets.buildPageRequest(1, 4, new Sort("onlinetutor_start", "asc"));
		List<Map<String, Object>> newlyList = courseLearningService.queryOnlineLessonList(student.getStudentId(), null, 4, null, pageRequst)
				.getContent();
		pageRequst = Servlets.buildPageRequest(1, 4, new Sort("onlinetutor_start", "desc"));
		List<Map<String, Object>> pastList = courseLearningService.queryOnlineLessonList(student.getStudentId(), null, 3, null, pageRequst)
				.getContent();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("newlyList", newlyList);
		result.put("pastList", pastList);
		return result;
	}

	/**
	 * 直播列表
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月15日 下午4:56:24
	 * @param request
	 * @param session
	 * @param pageNumber
	 * @param pageSize
	 * @param lessonName
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryLiveVideoList")
	public Map<String, Object> queryLiveVideoList(HttpServletRequest request, HttpSession session,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, String lessonName, Integer status, Integer type) {
		GjtStudentInfo student = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Page<Map<String, Object>> page = courseLearningService.queryOnlineLessonList(student.getStudentId(), lessonName, status, type, pageRequst);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("pageInfo", page);
		return result;
	}

	/**
	 * 直播详情
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月15日 下午4:56:04
	 * @param request
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryLiveVideoDetail")
	public Map<String, Object> queryLiveVideoDetail(HttpServletRequest request, String id) {
		GjtStudentInfo student = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		LcmsOnlineLesson onlineLesson = lcmsOnlineLessonService.queryById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		if(onlineLesson.getLessonType()==0){//教学直播
			List<String> tcIds = lcmsOnlineLessonService.findTermCourseIdByOnlinetutorId(onlineLesson.getOnlinetutorId());
			GjtTermCourseinfo termCourseinfo = gjtTermCourseinfoService.queryById(tcIds.get(0));
			if (termCourseinfo != null && termCourseinfo.getTeacherEmployee() != null) {
				result.put("classTeacher", termCourseinfo.getTeacherEmployee().getXm());
			}
		}else if(onlineLesson.getLessonType()==1){//活动直播
			GjtUserAccount user=gjtUserAccountService.findOne(onlineLesson.getCreatedBy());
			result.put("classTeacher", user.getRealName());
		}
		result.put("type", onlineLesson.getLessonType());
		result.put("currentTime", DateUtils.getStringToDate(new Date()));
		result.put("label", onlineLesson.getOnlinetutorLabel());
		result.put("lessonName", onlineLesson.getOnlinetutorName());
		if (onlineLesson.getOnlinetutorStart() != null) {
			result.put("startTime", DateUtil.toString(onlineLesson.getOnlinetutorStart(), "yyyy-MM-dd HH:mm"));
		}
		if (onlineLesson.getOnlinetutorFinish() != null) {
			result.put("endTime", DateUtil.toString(onlineLesson.getOnlinetutorFinish(), "yyyy-MM-dd HH:mm"));
		}
		Long[] counts = openClassService.getLessonStuCount(onlineLesson.getNumber());
		result.put("joinNum", counts[1] == null ? 0 : counts[1]);
		result.put("desc", onlineLesson.getOnlinetutorDesc());
		result.put("imgUrl", onlineLesson.getImgUrl());
		long timestemp = System.currentTimeMillis();
		int status;
		if (onlineLesson.getOnlinetutorStart().getTime() > timestemp) {
			status = 0;
		} else if (onlineLesson.getOnlinetutorFinish().getTime() < timestemp) {
			status = 2;
		} else {
			status = 1;
		}
		result.put("status", status);
		if (status == 2) {
			if (StringUtils.isEmpty(onlineLesson.getVideojoinurl())) {
				lcmsOnlineLessonService.saveVideoUrl(onlineLesson);
			}
			if (StringUtils.isNotEmpty(onlineLesson.getVideojoinurl())) {
				result.put("joinUrl", onlineLesson.getVideojoinurl() + "?nickname=" + student.getXm() + "&token=" + onlineLesson.getVideotoken());
			}
		} else {
			if (StringUtils.isNotEmpty(onlineLesson.getStudentjoinurl())) {
				result.put("joinUrl",
						onlineLesson.getStudentjoinurl() + "?nickname=" + student.getXm() + "&token=" + onlineLesson.getStudenttoken());
			}
		}


		List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
		List<GjtOnlineLessonFile> files = lcmsOnlineLessonService.findLessonFileByOnlinetutorId(onlineLesson.getOnlinetutorId());
		for (GjtOnlineLessonFile file : files) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", file.getFileName());
			map.put("path", file.getFileUrl());
			fileList.add(map);
		}
		result.put("fileList", fileList);
		return result;
	}

	/**
	 * 保存学员观看直播记录
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月15日 下午4:54:22
	 * @param request
	 * @param session
	 * @param id
	 *            直播id
	 */
	@ResponseBody
	@RequestMapping(value = "saveLiveVedioRecord")
	public void saveLiveVedioRecord(HttpServletRequest request, HttpSession session, String id,
			@RequestParam(value = "client", defaultValue = "0") int client) {
		GjtStudentInfo student = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		try {
			LcmsOnlineLesson lesson = lcmsOnlineLessonService.queryById(id);
			long timestemp = System.currentTimeMillis();
			if (lesson.getOnlinetutorFinish().getTime() < timestemp) {// 已结束
				lcmsOnlineLessonService.insertGjtLessonViewrecord(lesson.getOnlinetutorId(), student.getStudentId());
			} else {// 直播中
				lcmsOnlineLessonService.saveViewOnlineLessionRecord(lesson.getOnlinetutorId(), student.getStudentId(), client);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}
	
	/**
	 * 获取直播详情（APP调用）
	 * 
	 * @author ouguohao@eenet.com
	 * @return
	 * @Date 2017年12月13日 下午3:38:16
	 */
	@ResponseBody
	@RequestMapping(value = "queryLiveVideoInfo")
	public Map<String, Object> queryLiveVideoInfo(HttpServletRequest request, String id) {
		GjtStudentInfo student = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		LcmsOnlineLesson onlineLesson = lcmsOnlineLessonService.queryById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", onlineLesson.getId());
		result.put("lessonName", onlineLesson.getOnlinetutorName());
		result.put("startTime", com.gzedu.xlims.common.DateUtil.dateToString(onlineLesson.getOnlinetutorStart(), "yyyy-MM-dd HH:mm"));
		result.put("imgUrl", onlineLesson.getImgUrl());
		result.put("label", onlineLesson.getOnlinetutorLabel());
		result.put("content", onlineLesson.getActivityContent());
		long timestemp = System.currentTimeMillis();
		int status;
		if (onlineLesson.getOnlinetutorStart().getTime() > timestemp) {
			status = 0;
		} else if (onlineLesson.getOnlinetutorFinish().getTime() < timestemp) {
			status = 2;
		} else {
			status = 1;
		}
		result.put("status", status);
		result.put("roomId", onlineLesson.getNumber());
		result.put("clientToken", onlineLesson.getStudentclienttoken());
		if (StringUtils.isEmpty(onlineLesson.getVideojoinurl())) {
			lcmsOnlineLessonService.saveVideoUrl(onlineLesson);
		}
		String videoRoomId=null;
		if(StringUtils.isNotEmpty(onlineLesson.getVideojoinurl())){
			videoRoomId = onlineLesson.getVideojoinurl().substring(onlineLesson.getVideojoinurl().lastIndexOf("/") + 1);
		}
		result.put("videoRoomId", videoRoomId);
		result.put("videoToken", onlineLesson.getVideotoken());
		StringBuilder contentUrl = new StringBuilder();
		contentUrl.append(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort())
				.append(request.getContextPath());
		contentUrl.append("/api/course/queryActivityContent");
		contentUrl.append("?studentId=").append(student.getStudentId());
		contentUrl.append("&id=").append(onlineLesson.getId());
		result.put("contentUrl", contentUrl.toString());
		return result;
	}

	@RequestMapping(value = "queryActivityContent")
	public String queryActivityContent(HttpServletRequest request, HttpServletResponse response, Model model, String id) {
		LcmsOnlineLesson onlineLesson = lcmsOnlineLessonService.queryById(id);
		request.removeAttribute("isApiForward");
		model.addAttribute("html", StringUtils.defaultString(onlineLesson.getActivityContent()));
		return "simple/activity-content";
	}

}
