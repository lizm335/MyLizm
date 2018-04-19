package com.ouchgzee.headTeacher.web.controller.classManager.message;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtMessageResult;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.LcmsMutualReply;
import com.gzedu.xlims.pojo.LcmsMutualSubject;
import com.gzedu.xlims.pojo.status.SubjectSourceEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.feedback.LcmsMutualReplyService;
import com.gzedu.xlims.service.feedback.LcmsMutualSubjectService;
import com.gzedu.xlims.service.home.GjtMessageResultService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.service.usermanage.BzrPriRoleInfoService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.common.vo.LcmsMutualSubjectVO;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * 
 * 功能说明：班主任平台：取学习平台表，新班级答疑
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年6月19日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/home/class/newFeedback")
public class FeedSubjectController extends BaseController {
	private final static Logger log = LoggerFactory.getLogger(FeedSubjectController.class);

	@Autowired
	BzrPriRoleInfoService oldpriRoleInfoService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	LcmsMutualSubjectService lcmsMutualSubjectService;

	@Autowired
	LcmsMutualReplyService lcmsMutualReplyService;

	@Autowired
	PriRoleInfoService priRoleInfoService;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtMessageResultService gjtMessageResultService;

	@Autowired
	GjtClassStudentService gjtClassStudentService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Value("#{configProperties['eeChatInterface']}")
	private String eeServer;

	/**
	 * 答疑列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, Model model,
			HttpServletRequest request, HttpSession session, String type, String title, String isTranspond,
			String isTimeout, String isSolve, String isAsk) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();
		String currentClassId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID))
				? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(session);
		searchParams.put("EQ_classId", currentClassId);

		if (StringUtils.isNotBlank(title)) {
			searchParams.put("LIKE_subjectTitle", title);
		}
		searchParams.put("EQ_isdeleted", "N");
		List<String> orgTree = gjtOrgService.queryByParentId(user.getGjtOrg().getId());
		searchParams.put("IN_orgId", orgTree);

		if ("F".equals(type)) {// 转出去的
			searchParams.put("EQ_initialAccountId", user.getId());
			searchParams.put("NE_forwardAccountId", user.getId());
			if ("N".equals(isAsk)) {
				searchParams.put("EQ_subjectStatus", "N");
			} else if ("YN".equals(isAsk)) {
				searchParams.put("EQ_subjectStatus", "Y");
				searchParams.put("EQ_isSolve", "0");
			} else if ("YY".equals(isAsk)) {
				searchParams.put("EQ_subjectStatus", "Y");
				searchParams.put("EQ_isSolve", "1");
			} else {

			}
		} else {
			searchParams.put("EQ_forwardAccountId", user.getId());
			if (StringUtils.isNotBlank(type) && !"N".equals(type)) {
				if ("Y".equals(type)) {// Y为已解答
					searchParams.put("EQ_subjectStatus", "Y");
					if (StringUtils.isNotBlank(isSolve)) {// 学生确认是否已经解决
						searchParams.put("EQ_isSolve", isSolve);
					}
				} else {
					searchParams.put("EQ_isCommend", "Y");// 常见问题
				}
			} else {// 待解答问题
				type = "N";
				searchParams.put("EQ_subjectStatus", "N");

				if ("Y".equals(isTranspond)) {// 是否转发Y（ 转发给我的）
					searchParams.put("NE_initialAccountId", user.getId());
				}
				if ("N".equals(isTranspond)) {// 是否转发N （向我提问的）
					searchParams.put("EQ_initialAccountId", user.getId());
				}
				String timeout = "";
				long time = DateUtils.getDate().getTime();
				if ("2".equals(isTimeout)) { // 超2小时未解答
					long time2 = time - (2 * 60 * 60 * 1000);
					Timestamp sql = new Timestamp(time2);
					timeout = DateUtils.getStringToDate(sql);
				}
				if ("24".equals(isTimeout)) {// 超24小时未解答
					long time2 = time - (24 * 60 * 60 * 1000);
					Timestamp sql = new Timestamp(time2);
					timeout = DateUtils.getStringToDate(sql);
				}
				searchParams.put("LT_updatedDt", timeout);
			}
		}

		Page<LcmsMutualSubject> infos = lcmsMutualSubjectService.queryPageInfo(searchParams, pageRequst);
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		List<LcmsMutualSubjectVO> list = new ArrayList<LcmsMutualSubjectVO>();
		for (LcmsMutualSubject item : infos) {
			if ("1".equals(item.getIsTranspond())) {// 转发的显示
				if (item.getForwardAccountId().equals(user.getId())) {// 别人转发给我的
					GjtUserAccount InitialUser = gjtUserAccountService.findOne(item.getInitialAccountId());
					item.setRemark(InitialUser.getRealName() + "(" + InitialUser.getPriRoleInfo().getRoleName() + ")");
					item.setFeedbackContacts("我" + "(" + user.getPriRoleInfo().getRoleName() + ")");
					item.setIsMessage(false);
				} else {// 我转发给别的
					GjtUserAccount forwardUser = gjtUserAccountService.findOne(item.getForwardAccountId());
					item.setRemark("我" + "(" + user.getPriRoleInfo().getRoleName() + ")");
					item.setFeedbackContacts(
							forwardUser.getRealName() + "(" + forwardUser.getPriRoleInfo().getRoleName() + ")");
					// 是否短信催促过
					boolean bool = gjtMessageResultService.queryByIncidentIdAndStartDateAndEndDate(item.getSubjectId());
					item.setIsMessage(bool);
				}
			} else {
				item.setFeedbackContacts("我" + "(" + user.getPriRoleInfo().getRoleName() + ")");
			}
			LcmsMutualSubjectVO vo = null;
			if ("0".equals(item.getSoleType())) {// 老师和学员提问的显示信息是不同的VO
				GjtStudentInfo studentInfo = null;
				GjtUserAccount createAccount = item.getCreateAccount();
				if (createAccount != null) {
					studentInfo = createAccount.getGjtStudentInfo();
				} else {
					studentInfo = gjtStudentInfoService.queryById(item.getCreatedBy());// 旧数据
				}
				vo = new LcmsMutualSubjectVO(item, pyccMap, studentInfo);
				if (studentInfo != null) {
					GjtUserAccount userAccount = studentInfo.getGjtUserAccount();
					GjtOrg gjtOrg = userAccount.getGjtOrg();
					vo.setOrgName(gjtOrg != null ? gjtOrg.getOrgName() : "");

					String singUrl = gjtUserAccountService.studentSimulationNew("APP002", studentInfo.getStudentId(),
							studentInfo.getXh(), "2");

					String url = gjtUserAccountService.studentSimulation(studentInfo.getStudentId(),
							studentInfo.getXh());

					vo.setUrl(url);
					vo.setSingUrl(singUrl);

					JSONObject object = new JSONObject();
					object.put("USER_ID", employeeInfo.getEeno());
					object.put("TO_IM_USERID", studentInfo.getEeno());
					String eeUrl = eeServer + "/openChatWithEENO.do?data=" + EncryptUtils.encrypt(object.toString());
					vo.setEeUrl(eeUrl);
				}
			} else {
				vo = new LcmsMutualSubjectVO(item);
			}
			list.add(vo);
		}

		Page<LcmsMutualSubjectVO> page = new PageImpl<LcmsMutualSubjectVO>(list, pageRequst, infos.getTotalElements());

		// 删除查询条件，统计总数
		searchParams.remove("EQ_isCommend");
		if ("Y".equals(isTranspond) || "F".equals(type)) {
			searchParams.remove("EQ_initialAccountId");
			searchParams.put("EQ_forwardAccountId", user.getId());
		}
		if ("N".equals(isTranspond) || "F".equals(type)) {
			searchParams.remove("NE_forwardAccountId");
			searchParams.put("EQ_forwardAccountId", user.getId());
		}

		// 为解决
		searchParams.put("EQ_subjectStatus", "N");
		long noSolve = lcmsMutualSubjectService.queryAllCount(searchParams);
		if (StringUtils.isBlank(type) || "N".equals(type)) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(title)) {
				map.put("LIKE_subjectTitle", title);
			}
			map.put("EQ_subjectStatus", "N");
			map.put("EQ_isdeleted", "N");

			if (EmptyUtils.isNotEmpty(searchParams.get("LT_updatedDt"))) {
				map.put("LT_updatedDt", searchParams.get("LT_updatedDt"));
			}
			map.put("EQ_classId", currentClassId);
			map.put("EQ_initialAccountId", user.getId());
			map.put("EQ_forwardAccountId", user.getId());
			long xiangWoTiWenCount = lcmsMutualSubjectService.queryAllCount(map);

			map.remove("EQ_initialAccountId");
			map.put("NE_initialAccountId", user.getId());
			long zhuanfaMeCount = lcmsMutualSubjectService.queryAllCount(map);

			model.addAttribute("xiangWoTiWenCount", xiangWoTiWenCount);
			model.addAttribute("zhuanfaMeCount", zhuanfaMeCount);
		}

		searchParams.put("EQ_subjectStatus", "Y");
		long yesSolve = lcmsMutualSubjectService.queryAllCount(searchParams);
		if ("Y".equals(type)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("EQ_subjectStatus", "Y");
			map.put("EQ_isSolve", "1");
			map.put("EQ_forwardAccountId", user.getId());
			if (StringUtils.isNotBlank(title)) {
				map.put("LIKE_subjectTitle", title);
			}
			map.put("EQ_classId", currentClassId);
			map.put("EQ_isdeleted", "N");
			long yesAffirmSolve = lcmsMutualSubjectService.queryAllCount(map);

			map.put("EQ_isSolve", "0");
			long noAffirmSolve = lcmsMutualSubjectService.queryAllCount(map);
			model.addAttribute("yesAffirmSolve", yesAffirmSolve);
			model.addAttribute("noAffirmSolve", noAffirmSolve);
		}

		searchParams.remove("EQ_subjectStatus");
		searchParams.put("EQ_isCommend", "Y");
		long oftenCount = lcmsMutualSubjectService.queryAllCount(searchParams);

		long forwardCount = 0;
		if ("F".equals(type)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("EQ_initialAccountId", user.getId());
			map.put("NE_forwardAccountId", user.getId());
			map.put("EQ_isdeleted", "N");
			map.put("EQ_classId", currentClassId);
			if (StringUtils.isNotBlank(title)) {
				map.put("LIKE_subjectTitle", title);
			}
			forwardCount = lcmsMutualSubjectService.queryAllCount(map);

			map.put("EQ_subjectStatus", "N");
			long forwardNoSolveCount = lcmsMutualSubjectService.queryAllCount(map);

			map.put("EQ_subjectStatus", "Y");
			map.put("EQ_isSolve", "0");
			long forwardYnSolve = lcmsMutualSubjectService.queryAllCount(map);

			map.put("EQ_isSolve", "1");
			long forwardYYSolve = lcmsMutualSubjectService.queryAllCount(map);

			model.addAttribute("forwardNoSolveCount", forwardNoSolveCount);
			model.addAttribute("forwardYnSolve", forwardYnSolve);
			model.addAttribute("forwardYYSolve", forwardYYSolve);

		} else {
			searchParams.remove("EQ_isCommend");
			searchParams.remove("EQ_forwardAccountId");
			searchParams.put("EQ_initialAccountId", user.getId());
			searchParams.put("NE_forwardAccountId", user.getId());
			forwardCount = lcmsMutualSubjectService.queryAllCount(searchParams);
		}
		model.addAttribute("pageInfo", page);
		model.addAttribute("noSolve", noSolve);
		model.addAttribute("yesSolve", yesSolve);
		model.addAttribute("oftenCount", oftenCount);
		model.addAttribute("forwardCount", forwardCount);
		model.addAttribute("type", type);
		model.addAttribute("info", employeeInfo);
		model.addAttribute("isTranspond", isTranspond);
		model.addAttribute("isTimeout", isTimeout);
		model.addAttribute("isSolve", isSolve);
		model.addAttribute("isAsk", isAsk);
		model.addAttribute("oftenTypeMap", commonMapService.getDates("OftenType"));

		if ("Y".equals(type)) {// Y为已解决
			return "new/class/newFeedback/solved_list";
		} else if ("F".equals(type)) {
			return "new/class/newFeedback/forward_solved_list";
		} else if ("E".equals(type)) {
			return "new/class/newFeedback/often_list";
		} else {
			return "new/class/newFeedback/no_solved_list";
		}

	}

	@RequestMapping(value = "settingOften", method = { RequestMethod.GET })
	public String settingOften(HttpServletRequest request, Model model, String subjectId, String oftenType) {
		Map<String, String> oftenTypeMap = commonMapService.getDates("OftenType");
		model.addAttribute("subjectId", subjectId);
		model.addAttribute("oftenTypeMap", oftenTypeMap);
		model.addAttribute("state", "Y");
		return "new/class/newFeedback/affirm_often";
	}

	// 设置常见问题Y,N
	@RequestMapping(value = "setOften", method = { RequestMethod.POST })
	@ResponseBody
	public Feedback setOften(String state, String id, String oftenType, String isCommendType) {
		Feedback fb = new Feedback(true, "操作成功！");
		try {
			boolean updateIsComm = lcmsMutualSubjectService.updateIsComm(id, state, oftenType, isCommendType);
			if (!updateIsComm) {
				fb = new Feedback(true, "操作失败！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(true, "系统异常,操作失败！");
		}

		return fb;
	}

	// 删除常见问题
	@RequestMapping(value = "delete", method = { RequestMethod.GET })
	@ResponseBody
	public Feedback delete(HttpServletRequest request, String id) {
		Feedback fb = new Feedback(true, "删除成功！");
		try {
			boolean i = lcmsMutualSubjectService.delete(id);// 主表
			lcmsMutualReplyService.deleteBySubjectId(id);// 从表
			if (!i) {
				fb = new Feedback(true, "删除失败！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(true, "系统异常,删除失败！");
		}

		return fb;
	}

	// 常见问题跳转
	@RequestMapping(value = "createOften", method = { RequestMethod.GET })
	public String createOften(HttpServletRequest request, Model model) {
		Map<String, String> oftenTypeMap = commonMapService.getDates("OftenType");
		model.addAttribute("oftenTypeMap", oftenTypeMap);
		model.addAttribute("action", "createOften");
		return "new/class/newFeedback/often_add";
	}

	// 自问自答常见问题新增
	@RequestMapping(value = "createOften", method = RequestMethod.POST)
	@ResponseBody
	public Feedback createOften(HttpServletRequest request, HttpSession session) throws CommonException {
		Feedback fb = new Feedback(true, "操作成功！");

		String classId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID))
				? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(session);

		String title = request.getParameter("title");
		String content1 = request.getParameter("content1");
		String content2 = request.getParameter("content2");
		String oftenType = request.getParameter("oftenType");
		String isCommendType = request.getParameter("isCommendType");
		String[] imgUrls1 = request.getParameterValues("imgUrl1");
		String[] imgUrls2 = request.getParameterValues("imgUrl2");

		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);

		if (StringUtils.isBlank(title) || StringUtils.isBlank(content1) || StringUtils.isBlank(content2)) {
			return new Feedback(false, "有必填参数为空!");
		}

		BzrGjtUserAccount bzruser = employeeInfo.getGjtUserAccount();
		GjtUserAccount user = gjtUserAccountService.findOne(bzruser.getId());

		LcmsMutualSubject subject = null;
		try {
			subject = lcmsMutualSubjectService.saveOften(oftenType, imgUrls1, user, content1, title, classId,
					SubjectSourceEnum.HEAD_TEACHER_BACKSTAGE.getNum(), isCommendType);
			if (subject == null) {
				fb = new Feedback(false, "新增答疑表失败");
			}
		} catch (Exception e) {
			fb = new Feedback(false, "发生异常,新增答疑表失败");
			log.error(e.getMessage(), e);
		}
		if (subject != null) {
			try {
				LcmsMutualReply reply = lcmsMutualReplyService.saveOftenReply(imgUrls2, subject, user, content2, title);
				if (reply == null) {
					fb = new Feedback(false, "新增答疑回复表失败");
				}
			} catch (Exception e) {
				fb = new Feedback(false, "发生异常，新增答疑回复表失败");
				log.error(e.getMessage(), e);
			}

		}
		return fb;
	}

	// 回复学员
	@RequestMapping(value = "askAgain", method = RequestMethod.POST)
	@ResponseBody
	public Feedback askAgain(String pid, String content, String is_comm, HttpServletRequest request,
			HttpSession session, String oftenType, String isCommendType) {

		Feedback feedback = new Feedback(true, "回复成功");
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		BzrGjtUserAccount bzruser = employeeInfo.getGjtUserAccount();
		GjtUserAccount user = new GjtUserAccount(bzruser.getId());

		if (StringUtils.isBlank(pid) || StringUtils.isBlank(content)) {
			return new Feedback(false, "有必填参数为空!");
		}

		LcmsMutualSubject entity = lcmsMutualSubjectService.queryById(pid);
		if (entity == null) {
			return new Feedback(false, "PID不存在记录!");
		}
		String[] imgs = request.getParameterValues("imgUrl");
		LcmsMutualReply save = null;

		try {
			save = lcmsMutualReplyService.teachReplyStudentSave(imgs, pid, entity, user, content, is_comm, oftenType,
					isCommendType);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "回复失败，发生异常！" + e.getMessage());
		}

		try {
			if (save != null) {
				// 发送短信和微信推送
				gjtMessageResultService.replyMessage(entity);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return feedback;
	}

	@RequestMapping(value = "findRole", method = { RequestMethod.GET })
	public String findRole(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, HttpServletRequest request,
			Model model, String id, HttpSession session, String roleId, String userName, String studentId,
			String className) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("orgId", employeeInfo.getXxId());

		searchMap.put("userName", userName);
		searchMap.put("studentId", studentId);
		searchMap.put("className", className);
		searchMap.put("benshen", employeeInfo.getGjtUserAccount().getId());

		Map<String, String> roleMap = commonMapService.getDates("AnswerTranspondRole");
		if (StringUtils.isNotBlank(roleId)) {
			searchMap.put("roleId", roleId);
		} else {
			searchMap.put("roleId", roleMap.keySet());
		}

		Page<Map<String, Object>> page = priRoleInfoService.queryUserRoleClassCourse(searchMap, pageRequst);

		model.addAttribute("roleMap", roleMap);
		model.addAttribute("page", page);
		model.addAttribute("subjectId", id);
		model.addAttribute("studentId", studentId);
		return "new/class/newFeedback/share_role_list";
	}

	// 转回初始班主任
	@RequestMapping(value = "updateInitial", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updateInitial(String id, String chushiId, String content, String studentName, String teacherName) {
		Feedback fb = new Feedback(true, "操作成功！");
		try {
			boolean bool = lcmsMutualSubjectService.updateForward(id, chushiId, studentName, teacherName, content);
			if (!bool) {
				fb = new Feedback(false, "转发失败！");
			}
		} catch (Exception e) {
			fb = new Feedback(false, "系统异常，转发失败！");
			log.error(e.getMessage(), e);
		}
		return fb;
	}

	// 短息催促
	@RequestMapping(value = "urgeRemind", method = RequestMethod.POST)
	@ResponseBody
	public Feedback urgeRemind(HttpServletRequest request, String id, String zhuanfaId, String studentName) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		GjtUserAccount user = gjtUserAccountService.findOne(employeeInfo.getGjtUserAccount().getId());
		Feedback fb = new Feedback(true, "操作成功！");
		try {
			GjtMessageResult messageSave = gjtMessageResultService.messageSave(id, user, zhuanfaId, studentName);
			if (messageSave == null) {
				fb = new Feedback(false, "查询不到转发的对象");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, "发生异常！");
		}
		return fb;
	}

	// 转发到其他人员
	@RequestMapping(value = "updateForward", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updateForward(String id, String classId, String teachId, String zhuanfaId, String roleId) {
		Feedback fb = new Feedback(true, "操作成功！");
		if (StringUtils.isBlank(id)) {
			return new Feedback(false, "id不能为空！");
		}
		try {
			LcmsMutualSubject item = lcmsMutualSubjectService.forwardOthers(id, classId, teachId, zhuanfaId, roleId);
			if (item == null) {
				fb = new Feedback(false, "该ID查询不到数据！");
			}
		} catch (Exception e) {
			fb = new Feedback(false, "系统异常，转发失败！");
			log.error(e.getMessage(), e);
		}
		return fb;
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String addFeedback(HttpServletRequest request, Model model) {
		return "new/class/newFeedback/give_student_addSolved";
	}

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "findAskStudent", method = RequestMethod.GET)
	public String findStudent(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, ModelMap model,
			HttpServletRequest request) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GjtStudentInfo> page = gjtStudentInfoService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);

		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次

		model.addAttribute("page", page);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		return "new/class/newFeedback/ask_student_list";
	}

	@RequestMapping(value = "findAnswerUser", method = RequestMethod.GET)
	public String findUser(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, HttpServletRequest request,
			Model model, HttpSession session, String roleId, String userName, String studentId) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("orgId", employeeInfo.getXxId());

		searchMap.put("userName", userName);
		searchMap.put("studentId", studentId);
		searchMap.put("benshen", employeeInfo.getGjtUserAccount().getId());

		Map<String, String> roleMap = commonMapService.getDates("AnswerTranspondRole");
		if (StringUtils.isNotBlank(roleId)) {
			searchMap.put("roleId", roleId);
		} else {
			searchMap.put("roleId", roleMap.keySet());
		}

		Page<Map<String, Object>> page = priRoleInfoService.queryUserRoleClassCourse(searchMap, pageRequst);

		model.addAttribute("page", page);
		model.addAttribute("studentId", studentId);
		model.addAttribute("userName", userName);
		model.addAttribute("roleMap", roleMap);
		return "new/class/newFeedback/answer_User_list";
	}

	// 老师帮学员提问
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Feedback addFeedbackFrom(HttpServletRequest request) {
		Feedback fb = new Feedback(true, "创建成功");
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();

		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String[] imgUrls = request.getParameterValues("imgUrls");
		String studentId = request.getParameter("studentId");
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);
		String answerUserId = request.getParameter("answerUserId");
		GjtUserAccount studentUser = studentInfo.getGjtUserAccount();

		String termId = request.getParameter("termId");
		String teacherClassId = request.getParameter("teacherClassId");
		if (StringUtils.isBlank(teacherClassId)) {
			GjtClassInfo classInfo = gjtClassStudentService.queryTeachClassInfoByStudetnId(studentId);
			if (classInfo != null) {
				teacherClassId = classInfo.getClassId();
			}
		}

		if (StringUtils.isBlank(title) || StringUtils.isBlank(content)) {
			return new Feedback(false, "有必填参数为空");
		}

		try {
			lcmsMutualSubjectService.teachHelpStudentSave(teacherClassId, studentUser, user.getId(), imgUrls, content,
					title, answerUserId, termId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, "发生异常！");
		}

		return fb;
	}

	// 常见问题跳转
	@RequestMapping(value = "updateOften", method = { RequestMethod.GET })
	public String updateOften(HttpServletRequest request, Model model, String id) {
		LcmsMutualSubject subject = lcmsMutualSubjectService.queryById(id);
		Map<String, String> oftenTypeMap = commonMapService.getDates("OftenType");

		// 图片集合
		List<String> subjectImgList = new ArrayList<String>();
		if (StringUtils.isNotBlank(subject.getResPath())) {
			String[] imgs = subject.getResPath().split(",");
			for (String str : imgs) {
				subjectImgList.add(str);
			}
		}

		LcmsMutualReply reply = subject.getGjtLcmsMutualReply().get(0);
		List<String> replyImgList = new ArrayList<String>();
		if (StringUtils.isNotBlank(reply.getResPath())) {
			String[] imgs = reply.getResPath().split(",");
			for (String str : imgs) {
				replyImgList.add(str);
			}
		}

		model.addAttribute("replyEntity", reply);
		model.addAttribute("replyImgList", replyImgList);
		model.addAttribute("oftenTypeMap", oftenTypeMap);
		model.addAttribute("subjectEntity", subject);
		model.addAttribute("subjectImgList", subjectImgList);
		model.addAttribute("action", "updateOften");
		return "new/class/newFeedback/often_add";
	}

	// 自问自答常见问题新增
	@RequestMapping(value = "updateOften", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updateOften(HttpServletRequest request) {
		Feedback fb = new Feedback(true, "操作成功！");

		String subjectId = request.getParameter("subjectId");
		String replyId = request.getParameter("replyId");
		String title = request.getParameter("title");
		String content1 = request.getParameter("content1");
		String content2 = request.getParameter("content2");
		String oftenType = request.getParameter("oftenType");
		String isCommendType = request.getParameter("isCommendType");
		String[] imgUrls1 = request.getParameterValues("imgUrl1");
		String[] imgUrls2 = request.getParameterValues("imgUrl2");

		try {
			LcmsMutualSubject subject = lcmsMutualSubjectService.updateOften(subjectId, title, imgUrls1, content1,
					oftenType, isCommendType);
			if (subject == null) {
				fb = new Feedback(false, "修改失败");
			}
		} catch (Exception e) {
			fb = new Feedback(false, "发生异常,修改失败");
			log.error(e.getMessage(), e);
		}

		try {
			LcmsMutualReply reply = lcmsMutualReplyService.updateOftenReply(replyId, content2, imgUrls2);
			if (reply == null) {
				fb = new Feedback(false, "修改失败");
			}
		} catch (Exception e) {
			fb = new Feedback(false, "发生异常，修改失败");
			log.error(e.getMessage(), e);
		}

		return fb;
	}

}
