package com.ouchgzee.study.weixin.student.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.LcmsMutualReply;
import com.gzedu.xlims.pojo.LcmsMutualSubject;
import com.gzedu.xlims.pojo.status.SubjectSourceEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.feedback.LcmsMutualReplyService;
import com.gzedu.xlims.service.feedback.LcmsMutualSubjectService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.common.Servlets;
import com.ouchgzee.study.web.vo.LcmsMutualSubjectVO;

/**
 * 
 * 功能说明：取学习平台表，答疑服务
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年6月19日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/wx/feedback")
public class WxFeedbackController extends BaseController {

	@Autowired
	LcmsMutualSubjectService lcmsMutualSubjectService;

	@Autowired
	LcmsMutualReplyService lcmsMutualReplyService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtClassInfoService gjtClassInfoService;

	@Autowired
	GjtClassStudentService gjtClassStudentService;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@RequestMapping(value = "oftenList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> oftenList(Model model, HttpServletRequest request, HttpSession session)
			throws CommonException, UnsupportedEncodingException {
		String title = request.getParameter("title");
		String orgCode = request.getParameter("orgCode");
		String oftenType = request.getParameter("oftenType");

		if (StringUtils.isBlank(orgCode)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "orgCode不能为空！");
		}

		GjtOrg gjtOrg = gjtOrgService.queryByCode(orgCode);
		if (gjtOrg == null) {
			throw new CommonException(MessageCode.BAD_REQUEST, "orgCode有误，查询不到院校信息");
		}

		Map<String, Object> searchParams = new HashMap<String, Object>();
		List<String> orgTree = gjtOrgService.queryByParentId(gjtOrg.getId());
		searchParams.put("IN_orgId", orgTree);
		searchParams.put("EQ_isdeleted", "N");
		searchParams.put("EQ_isCommend", "Y");
		if (StringUtils.isNotBlank(oftenType)) {
			searchParams.put("EQ_oftenType", oftenType);
		}

		List<LcmsMutualSubject> infos = lcmsMutualSubjectService.queryAllInfo(title, searchParams);
		List<LcmsMutualSubjectVO> dataList = new ArrayList<LcmsMutualSubjectVO>();
		for (LcmsMutualSubject item : infos) {
			LcmsMutualSubjectVO vo = new LcmsMutualSubjectVO(item, "");
			dataList.add(vo);
		}

		List<Map<String, Object>> oftenTypeAllList = commonMapService.getDateList("OftenType");

		if (StringUtils.isNotBlank(oftenType)) {
			for (Map<String, Object> map : oftenTypeAllList) {
				String code = (String) map.get("code");
				if (code.equals(oftenType)) {
					oftenTypeAllList = new ArrayList<Map<String, Object>>();
					oftenTypeAllList.add(map);
					break;
				}
			}
		}

		for (Map<String, Object> map : oftenTypeAllList) {
			String code = (String) map.get("code");
			List<LcmsMutualSubjectVO> oftenTypeMapList = new ArrayList<LcmsMutualSubjectVO>();
			for (LcmsMutualSubjectVO lcmsMutualSubject : dataList) {
				if (code.equals(lcmsMutualSubject.getOftenType())) {
					oftenTypeMapList.add(lcmsMutualSubject);
				}
			}
			map.put("oftenTypeMapList", oftenTypeMapList);
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();

		resultMap.put("oftenTypeList", oftenTypeAllList);
		return resultMap;
	}

	// 用于学员公众号的答疑明细和常见问题明细
	@RequestMapping(value = "askDetail", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> askDetail(Model model, HttpServletRequest request, HttpSession session)
			throws CommonException {
		String id = request.getParameter("id");
		if (StringUtils.isBlank(id)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "id不能为空！");
		}
		Map<String, Object> searchParams = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(id)) {
			searchParams.put("EQ_subjectId", id);
		}
		LcmsMutualSubject item = lcmsMutualSubjectService.queryById(id);
		LcmsMutualSubjectVO vo = null;
		if (item != null) {
			vo = new LcmsMutualSubjectVO(item, gjtEmployeeInfoService);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("vo", vo);
		return resultMap;
	}

	/** userId不能删，App在线答疑需要调用接口 */
	// 学生向老师提问的列表
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> studentList(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) throws CommonException {
		String userId = request.getParameter("userId");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (user == null && StringUtils.isBlank(userId)) {
			throw new CommonException(MessageCode.NOT_LOGGED_IN, "userId is null or session is null");
		} else {
			if (user == null) {
				user = gjtUserAccountService.findOne(userId);
			}
			if (user == null) {
				throw new CommonException(MessageCode.BAD_REQUEST, "userId 有误，查询不到信息");
			}
		}

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		String title = request.getParameter("title");
		if (StringUtils.isNotBlank(title)) {
			searchParams.put("LIKE_subjectTitle", title);
		}
		searchParams.put("EQ_isdeleted", "N");
		String dealResult = request.getParameter("dealResult");
		if (StringUtils.isNotBlank(dealResult)) {
			if ("Y".equals(dealResult)) {// Y为已解决
				searchParams.put("EQ_subjectStatus", "Y");
			}
			if ("N".equals(dealResult)) {// 为解决
				searchParams.put("EQ_subjectStatus", "N");
			}
		}
		searchParams.put("EQ_createAccountId", user.getId());
		Page<LcmsMutualSubject> infos = lcmsMutualSubjectService.queryPageInfo(searchParams, pageRequst);
		List<LcmsMutualSubjectVO> list = new ArrayList<LcmsMutualSubjectVO>();
		for (LcmsMutualSubject item : infos) {
			LcmsMutualSubjectVO vo = new LcmsMutualSubjectVO(item, gjtEmployeeInfoService);
			list.add(vo);
		}
		Page<LcmsMutualSubjectVO> page = new PageImpl<LcmsMutualSubjectVO>(list, pageRequst, infos.getTotalElements());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("pageInfo", page);
		return resultMap;
	}

	// 学生新增答疑
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public void CreateFeedback(HttpServletRequest request) throws CommonException {

		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String[] imgUrls = request.getParameterValues("imgUrls");

		String userId = request.getParameter("userId");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (user == null && StringUtils.isBlank(userId)) {
			throw new CommonException(MessageCode.NOT_LOGGED_IN, "userId is null or session is null");
		} else {
			if (user == null) {
				user = gjtUserAccountService.findOne(userId);
			}
			if (user == null) {
				throw new CommonException(MessageCode.BAD_REQUEST, "userId 有误，查询不到信息");
			}
		}
		GjtClassInfo classInfo = (GjtClassInfo) request.getSession().getAttribute(WebConstants.TEACH_CLASS);
		if (classInfo == null) {
			classInfo = gjtClassStudentService.queryTeachClassInfoByStudetnId(user.getGjtStudentInfo().getStudentId());
		}
		if (classInfo == null) {
			throw new CommonException(MessageCode.BIZ_ERROR, "查询不到该学员的教务班！");
		}

		if (StringUtils.isBlank(title)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "title参数为空");
		}
		if (StringUtils.isBlank(content)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "content参数为空");
		}
		lcmsMutualSubjectService.save(SubjectSourceEnum.WEIXIN_APP.getNum(), classInfo, user, imgUrls, content, title);
	}

	// 学员答疑追问
	@RequestMapping(value = "askAgain", method = RequestMethod.POST)
	@ResponseBody
	public void askAgain(HttpServletRequest request) throws CommonException {
		String pid = request.getParameter("pid");
		String content = request.getParameter("content");
		String[] imgUrls = request.getParameterValues("imgUrls");

		String userId = request.getParameter("userId");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (user == null && StringUtils.isBlank(userId)) {
			throw new CommonException(MessageCode.NOT_LOGGED_IN, "userId is null or session is null");
		} else {
			if (user == null) {
				user = gjtUserAccountService.findOne(userId);
			}
			if (user == null) {
				throw new CommonException(MessageCode.BAD_REQUEST, "userId 有误，查询不到信息");
			}
		}

		if (StringUtils.isBlank(pid)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "pid参数为空!");
		}
		if (StringUtils.isBlank(content))
			throw new CommonException(MessageCode.BAD_REQUEST, "content参数为空!");

		LcmsMutualReply save = lcmsMutualReplyService.save(imgUrls, pid, user, content);
		if (save == null) {
			throw new CommonException(MessageCode.BIZ_ERROR, "请耐心等待老师的解答!");
		}
	}

	// 学生问题确认已解决
	@RequestMapping(value = "affirmSolve ", method = RequestMethod.POST)
	@ResponseBody
	public void affirmSolve(HttpServletRequest request) throws CommonException {
		String id = request.getParameter("id");

		String userId = request.getParameter("userId");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (user == null && StringUtils.isBlank(userId)) {
			throw new CommonException(MessageCode.NOT_LOGGED_IN, "userId is null or session is null");
		} else {
			if (user == null) {
				user = gjtUserAccountService.findOne(userId);
			}
			if (user == null) {
				throw new CommonException(MessageCode.BAD_REQUEST, "userId 有误，查询不到信息");
			}
		}

		if (StringUtils.isBlank(id)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "id参数为空!");
		}
		LcmsMutualSubject subject = lcmsMutualSubjectService.queryById(id);
		subject.setIsSolve("1");
		subject.setUpdatedDt(DateUtils.getNowTime());
		subject.setUpdatedBy(user.getId());
		lcmsMutualSubjectService.update(subject);
	}

	// 老师向后台管理员提问列表
	@RequestMapping(value = "teachList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> teachList(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) throws CommonException {

		String userId = request.getParameter("userId");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER_TEACHER);
		if (user == null && StringUtils.isBlank(userId)) {
			throw new CommonException(MessageCode.NOT_LOGGED_IN, "userId is null or session is null");
		} else {
			if (user == null) {
				user = gjtUserAccountService.findOne(userId);
			}
			if (user == null) {
				throw new CommonException(MessageCode.BAD_REQUEST, "userId 有误，查询不到信息");
			}
		}

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		String title = request.getParameter("title");
		if (StringUtils.isNotBlank(title)) {
			searchParams.put("LIKE_subjectTitle", title);
		}
		searchParams.put("EQ_isdeleted", "N");
		String dealResult = request.getParameter("dealResult");
		if (StringUtils.isNotBlank(dealResult)) {
			if ("Y".equals(dealResult)) {// Y为已解决
				searchParams.put("EQ_subjectStatus", "Y");
			}
			if ("N".equals(dealResult)) {// 为解决
				searchParams.put("EQ_subjectStatus", "N");
			}
		}
		searchParams.put("NE_forwardAccountId", user.getId());
		Page<LcmsMutualSubject> infos = lcmsMutualSubjectService.queryAllPageInfo(user.getId(), searchParams,
				pageRequst);

		List<LcmsMutualSubjectVO> list = new ArrayList<LcmsMutualSubjectVO>();
		for (LcmsMutualSubject item : infos) {
			LcmsMutualSubjectVO vo = null;
			if ("0".equals(item.getSoleType())) {// 学员
				vo = new LcmsMutualSubjectVO(item, gjtEmployeeInfoService);
			} else {
				vo = new LcmsMutualSubjectVO(item);
			}

			list.add(vo);
		}
		Page<LcmsMutualSubjectVO> page = new PageImpl<LcmsMutualSubjectVO>(list, pageRequst, infos.getTotalElements());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("pageInfo", page);
		return resultMap;
	}

	// 老师提问题
	@RequestMapping(value = "teachAdd", method = RequestMethod.POST)
	@ResponseBody
	public void teachAdd(HttpServletRequest request) throws CommonException {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String roleType = request.getParameter("roleType");// 4-教学，3-教务，13-其他
		String[] imgUrls = request.getParameterValues("imgUrls");

		String userId = request.getParameter("userId");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER_TEACHER);
		if (user == null && StringUtils.isBlank(userId)) {
			throw new CommonException(MessageCode.NOT_LOGGED_IN, "userId is null or session is null");
		} else {
			if (user == null) {
				user = gjtUserAccountService.findOne(userId);
			}
			if (user == null) {
				throw new CommonException(MessageCode.BAD_REQUEST, "userId 有误，查询不到信息");
			}
		}

		if (StringUtils.isBlank(title) || StringUtils.isBlank(content) || StringUtils.isBlank(roleType)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "有必填参数为空！");
		}
		String adminUserId = "";

		if ("4".equals(roleType)) {
			adminUserId = "ef01341ecc304126a93422ed149edd83";// gkfudaolaoshi
		} else if ("10".equals(roleType)) {
			adminUserId = "09472cccc33e49ebbca21b9af8941eb3";// zhaoshengadmin
		} else {// 教务和其他问题
			adminUserId = "a49543f8267d47ad9cafc430b8c8ddd2";// gzjwadmin001
		}

		lcmsMutualSubjectService.teachSave(SubjectSourceEnum.WEIXIN_APP.getNum(), user, imgUrls, content, title,
				adminUserId);
	}

	// 老师追问
	@RequestMapping(value = "teachAskAgain", method = RequestMethod.POST)
	@ResponseBody
	public void teachAskAgain(HttpServletRequest request) throws CommonException {
		String pid = request.getParameter("pid");
		String content = request.getParameter("content");
		String[] imgUrls = request.getParameterValues("imgUrls");

		String userId = request.getParameter("userId");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER_TEACHER);
		if (user == null && StringUtils.isBlank(userId)) {
			throw new CommonException(MessageCode.NOT_LOGGED_IN, "userId is null or session is null");
		} else {
			if (user == null) {
				user = gjtUserAccountService.findOne(userId);
			}
			if (user == null) {
				throw new CommonException(MessageCode.BAD_REQUEST, "userId 有误，查询不到信息");
			}
		}

		if (StringUtils.isBlank(pid)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "pid参数为空!");
		}
		if (StringUtils.isBlank(content)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "content参数为空!");
		}

		LcmsMutualReply save = lcmsMutualReplyService.teachSave(imgUrls, pid, user, content);
		if (save == null) {
			throw new CommonException(MessageCode.BIZ_ERROR, "请耐心等待管理员的解答!");
		}
	}

	// 用于老师公众号的答疑明细和常见问题明细
	@RequestMapping(value = "teschAskDetail", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> teschAskDetail(Model model, HttpServletRequest request, HttpSession session)
			throws CommonException {
		String id = request.getParameter("id");
		if (StringUtils.isBlank(id)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "id不能为空！");
		}
		Map<String, Object> searchParams = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(id)) {
			searchParams.put("EQ_subjectId", id);
		}
		LcmsMutualSubject item = lcmsMutualSubjectService.queryById(id);
		LcmsMutualSubjectVO vo = null;
		if (item != null) {
			vo = new LcmsMutualSubjectVO(item);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("vo", vo);
		return resultMap;
	}

	// 老师帮学员提问
	@RequestMapping(value = "teachHelpStudentAskAdd", method = RequestMethod.POST)
	@ResponseBody
	public void teachHelpStudentAskAdd(HttpServletRequest request) throws CommonException {

		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String roleType = request.getParameter("roleType");// 4-教学，3-教务，13-其他
		String[] imgUrls = request.getParameterValues("imgUrls");
		String studentId = request.getParameter("studentId");

		String userId = request.getParameter("userId");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER_TEACHER);
		if (user == null && StringUtils.isBlank(userId)) {
			throw new CommonException(MessageCode.NOT_LOGGED_IN, "userId is null or session is null");
		} else {
			if (user == null) {
				user = gjtUserAccountService.findOne(userId);
			}
			if (user == null) {
				throw new CommonException(MessageCode.BAD_REQUEST, "userId 有误，查询不到信息");
			}
		}

		if (StringUtils.isBlank(title) || StringUtils.isBlank(content) || StringUtils.isBlank(roleType)
				|| StringUtils.isBlank(studentId)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "有必填参数为空！");
		}

		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);
		String adminUserId = "";

		if ("4".equals(roleType)) {
			adminUserId = "ef01341ecc304126a93422ed149edd83";// gkfudaolaoshi
		} else if ("10".equals(roleType)) {
			adminUserId = "09472cccc33e49ebbca21b9af8941eb3";// zhaoshengadmin
		} else {// 教务和其他问题
			adminUserId = "a49543f8267d47ad9cafc430b8c8ddd2";// gzjwadmin001
		}

		GjtClassInfo classInfo = gjtClassStudentService.queryTeachClassInfoByStudetnId(studentId);
		String teacherClassId = "";
		String termId = "";
		if (classInfo != null) {
			teacherClassId = classInfo.getClassId();
			termId = classInfo.getTermcourseId();
		}

		StringBuffer imgUrl = new StringBuffer("");
		if (imgUrls != null && imgUrls.length > 0) {
			for (int i = 0; i < imgUrls.length; i++) {
				imgUrl.append(imgUrls[i]);
				if (i < imgUrls.length - 1) {
					imgUrl.append(",");
				}
			}
		}

		LcmsMutualSubject item = new LcmsMutualSubject();
		item.setClassId(teacherClassId);
		item.setCreateAccount(studentInfo.getGjtUserAccount());
		item.setForwardAccountId(adminUserId);// 发送给班主任
		item.setInitialAccountId(user.getId());// 问题初始人
		item.setResPath(imgUrl.toString());
		item.setSubjectContent(content);
		item.setSubjectTitle(title);
		item.setUserId(studentId);// 兼容旧的userId字段和createBy字段，取studentId
		item.setCreatedBy(studentId);// 兼容旧的userId字段和createBy字段，取studentId
		item.setOrgId(studentInfo.getGjtSchoolInfo().getId());
		item.setTermcourseId(termId);
		item.setOftenType(roleType);
		item.setQuestionSource(SubjectSourceEnum.WEIXIN_APP.getNum());
		item.setQuestionSourcePath("微信公众号：老师帮学员提问");
		item.setIsTranspond("1");// 是转发
		lcmsMutualSubjectService.save(item);
	}

	@RequestMapping(value = "findStudent", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findStudentByXh(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, HttpServletRequest request)
			throws CommonException {
		String userId = request.getParameter("userId");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER_TEACHER);
		if (user == null && StringUtils.isBlank(userId)) {
			throw new CommonException(MessageCode.NOT_LOGGED_IN, "userId is null or session is null");
		} else {
			if (user == null) {
				user = gjtUserAccountService.findOne(userId);
			}
			if (user == null) {
				throw new CommonException(MessageCode.BAD_REQUEST, "userId 有误，查询不到信息");
			}
		}

		String value = request.getParameter("xhOrxm");
		String orgCode = request.getParameter("orgCode");
		if (StringUtils.isBlank(orgCode)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "orgCode不能为空！");
		}
		GjtOrg gjtOrg = gjtOrgService.queryByCode(orgCode);
		if (gjtOrg == null) {
			throw new CommonException(MessageCode.BAD_REQUEST, "orgCode有误，查询不到院校信息");
		}
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);

		Page<Map> page = gjtStudentInfoService.findStudent(value, user.getGjtOrg().getId(), pageRequst);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("page", page);
		return resultMap;
	}

	// 学生问题确认已解决
	@RequestMapping(value = "teachAffirmSolve ", method = RequestMethod.POST)
	@ResponseBody
	public void teachAffirmSolve(HttpServletRequest request) throws CommonException {
		String id = request.getParameter("id");

		String userId = request.getParameter("userId");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER_TEACHER);
		if (user == null && StringUtils.isBlank(userId)) {
			throw new CommonException(MessageCode.NOT_LOGGED_IN, "userId is null or session is null");
		} else {
			if (user == null) {
				user = gjtUserAccountService.findOne(userId);
			}
			if (user == null) {
				throw new CommonException(MessageCode.BAD_REQUEST, "userId 有误，查询不到信息");
			}
		}

		if (StringUtils.isBlank(id)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "id参数为空!");
		}
		LcmsMutualSubject subject = lcmsMutualSubjectService.queryById(id);
		subject.setIsSolve("1");
		subject.setUpdatedDt(DateUtils.getNowTime());
		subject.setUpdatedBy(user.getId());
		lcmsMutualSubjectService.update(subject);
	}

}
