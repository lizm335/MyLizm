package com.ouchgzee.study.web.controller.headTeaherService;

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
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.LcmsMutualReply;
import com.gzedu.xlims.pojo.LcmsMutualSubject;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.feedback.LcmsMutualReplyService;
import com.gzedu.xlims.service.feedback.LcmsMutualSubjectService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
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
@RequestMapping("/pcenter/headTeacherService/feedback")
public class NewFeedbackController extends BaseController {

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

	/**
	 * 答疑列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);

		Map<String, Object> searchParams = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
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

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		searchParams.put("EQ_createAccountId", user.getId());

		Page<LcmsMutualSubject> infos = lcmsMutualSubjectService.queryPageInfo(searchParams, pageRequst);

		List<LcmsMutualSubjectVO> list = new ArrayList<LcmsMutualSubjectVO>();
		for (LcmsMutualSubject item : infos) {
			LcmsMutualSubjectVO vo = new LcmsMutualSubjectVO(item, gjtEmployeeInfoService);
			list.add(vo);
		}

		Page<LcmsMutualSubjectVO> page = new PageImpl<LcmsMutualSubjectVO>(list, pageRequst, infos.getTotalElements());

		// 为解决
		searchParams.put("EQ_subjectStatus", "N");
		long noSolve = lcmsMutualSubjectService.queryAllCount(searchParams);

		searchParams.put("EQ_subjectStatus", "Y");
		long yesSolve = lcmsMutualSubjectService.queryAllCount(searchParams);

		resultMap.put("pageInfo", page);
		resultMap.put("noSolve", noSolve);
		resultMap.put("yesSolve", yesSolve);

		return resultMap;
	}

	// 所有常见问题
	@RequestMapping(value = "comlist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> comlist(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		// GjtStudentInfo student = (GjtStudentInfo)
		// request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String title = request.getParameter("title");
		if (StringUtils.isNotBlank(title)) {
			searchParams.put("LIKE_subjectTitle", title);
		}

		List<String> orgTree = gjtOrgService.queryByParentId(user.getGjtOrg().getId());
		searchParams.put("IN_orgId", orgTree);
		searchParams.put("EQ_isdeleted", "N");
		searchParams.put("EQ_isCommend", "Y");
		String oftenType = request.getParameter("oftenType");

		if (StringUtils.isNotBlank(oftenType)) {
			searchParams.put("EQ_oftenType", oftenType);
		}

		Page<LcmsMutualSubject> infos = lcmsMutualSubjectService.queryPageInfo(searchParams, pageRequst);

		List<LcmsMutualSubjectVO> list = new ArrayList<LcmsMutualSubjectVO>();
		for (LcmsMutualSubject item : infos) {
			LcmsMutualSubjectVO vo = new LcmsMutualSubjectVO(item, gjtEmployeeInfoService);
			list.add(vo);
		}

		Page<LcmsMutualSubjectVO> page = new PageImpl<LcmsMutualSubjectVO>(list, pageRequst, infos.getTotalElements());

		List<Map<String, Object>> oftenTypeList = commonMapService.getDateList("OftenType");

		resultMap.put("oftenTypeList", oftenTypeList);
		resultMap.put("pageInfo", page);

		return resultMap;
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public void CreateFeedback(HttpServletRequest request) throws CommonException {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String[] imgUrls = request.getParameterValues("imgUrls");
		String source = request.getParameter("source");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtClassInfo classInfo = (GjtClassInfo) request.getSession().getAttribute(WebConstants.TEACH_CLASS);
		if (StringUtils.isBlank(title)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "title参数为空");
		}
		if (StringUtils.isBlank(content)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "content参数为空");
		}
		if (classInfo == null) {
			throw new CommonException(MessageCode.BIZ_ERROR, "查询不到所在教务班级！");
		}
		if (classInfo.getGjtBzr() == null) {
			throw new CommonException(MessageCode.BIZ_ERROR, "查询到所在教务班级未分配班主任！");
		}
		source = source == null ? "1" : source;
		lcmsMutualSubjectService.save(source, classInfo, user, imgUrls, content, title);
	}

	// 学员追问
	@RequestMapping(value = "askAgain", method = RequestMethod.POST)
	@ResponseBody
	public void askAgain(HttpServletRequest request) throws CommonException {
		String pid = request.getParameter("pid");
		String content = request.getParameter("content");
		String[] imgUrls = request.getParameterValues("imgUrls");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

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

	@RequestMapping(value = "affirmSolve ", method = RequestMethod.POST)
	@ResponseBody
	public void affirmSolve(HttpServletRequest request) throws CommonException {
		String id = request.getParameter("id");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		if (StringUtils.isBlank(id)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "id参数为空!");
		}
		LcmsMutualSubject subject = lcmsMutualSubjectService.queryById(id);
		if ("Y".equals(subject.getSubjectStatus())) {
			subject.setIsSolve("1");
			subject.setUpdatedDt(DateUtils.getNowTime());
			subject.setUpdatedBy(user.getId());
			lcmsMutualSubjectService.update(subject);
		} else {
			throw new CommonException(101, "问题未解答");
		}
	}

}
