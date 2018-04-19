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
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.pojo.GjtFeedback;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.feedback.GjtFeedbackService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.common.Servlets;
import com.ouchgzee.study.web.vo.GjtFeedbackVo;

@Controller
@RequestMapping("/pcenter/headTeacherService/oldfeedback")
public class FeedbackController extends BaseController {

	@Autowired
	GjtFeedbackService gjtFeedbackService;

	@Autowired
	GjtEmployeeInfoService gjtEmployeeInfoService;

	/**
	 * 未解决的答疑列表
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
			searchParams.put("LIKE_title", title);
		}

		searchParams.put("EQ_type", "feedback");
		searchParams.put("EQ_isDeleted", "N");

		String dealResult = request.getParameter("dealResult");
		if (StringUtils.isNotBlank(dealResult)) {
			searchParams.put("EQ_dealResult", dealResult);// Y为已处理，null未处理
		} else {
			searchParams.put("EQ_dealResult", "N");
		}

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		searchParams.put("EQ_createdBy", user.getGjtStudentInfo().getStudentId());

		Page<GjtFeedback> infos = gjtFeedbackService.queryPageList(searchParams, pageRequst);
		List<GjtFeedbackVo> list = new ArrayList<GjtFeedbackVo>();
		for (GjtFeedback gjtFeedback : infos) {
			GjtFeedbackVo vo = new GjtFeedbackVo(gjtFeedback, gjtEmployeeInfoService);
			list.add(vo);
		}

		Page<GjtFeedbackVo> page = new PageImpl<GjtFeedbackVo>(list, pageRequst, infos.getTotalElements());

		// 为解决
		searchParams.put("EQ_dealResult", "N");

		long noSolve = gjtFeedbackService.finAllCount(searchParams);

		searchParams.put("EQ_dealResult", "Y");
		long yesSolve = gjtFeedbackService.finAllCount(searchParams);

		resultMap.put("pageInfo", page);
		resultMap.put("noSolve", noSolve);
		resultMap.put("yesSolve", yesSolve);

		return resultMap;
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public void toCreateFeedback(HttpServletRequest request) throws CommonException {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String[] imgUrls = request.getParameterValues("imgUrls");
		String source = request.getParameter("source");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (StringUtils.isBlank(title)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "title参数为空");
		}
		if (StringUtils.isBlank(content)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "content参数为空");
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
		if (source == null) {
			source = "PC";// 问题可以在多处提出
		}
		GjtFeedback gjtFeedback = new GjtFeedback();
		gjtFeedback.setCreatedBy(user.getGjtStudentInfo().getStudentId());
		gjtFeedback.setTitle(title);
		gjtFeedback.setContent(content);
		gjtFeedback.setId(UUIDUtils.random().toString());
		gjtFeedback.setImgUrls(imgUrl.toString());
		gjtFeedback.setCreatedDt(DateUtils.getNowTime());
		gjtFeedback.setType("feedback");
		gjtFeedback.setSource(source);
		gjtFeedback.setCreatorRole("student");
		gjtFeedback.setDealResult("N");
		gjtFeedback.setIsDeleted("N");
		gjtFeedbackService.saveGjtFeedback(gjtFeedback);
	}

	@RequestMapping(value = "askAgain", method = RequestMethod.POST)
	@ResponseBody
	public void askAgain(HttpServletRequest request) throws CommonException {
		String pid = request.getParameter("pid");
		String content = request.getParameter("content");
		String source = request.getParameter("source");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		if (StringUtils.isBlank(pid)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "pid参数为空!");
		}
		if (StringUtils.isBlank(content))
			throw new CommonException(MessageCode.BAD_REQUEST, "content参数为空!");
		if (source == null)
			source = "PC";

		GjtFeedback feedback = gjtFeedbackService.queryById(pid);
		if (feedback == null) {
			throw new CommonException(MessageCode.BIZ_ERROR, "PID不存在记录");
		}

		List<GjtFeedback> list = feedback.getGjtFeedbackList();

		boolean bool = false;// 是否可以追问

		if (list != null && list.size() > 0) {
			GjtFeedback gjtFeedback = list.get(list.size() - 1);
			String type = gjtFeedback.getType();
			if ("reply".equals(type)) {
				bool = true;
			}
		}

		if (bool) {
			GjtFeedback gjtFeedback = new GjtFeedback();
			gjtFeedback.setId(UUIDUtils.random().toString());
			gjtFeedback.setCreatedBy(user.getGjtStudentInfo().getStudentId());
			gjtFeedback.setContent(content);
			gjtFeedback.setPid(pid);
			gjtFeedback.setCreatedDt(DateUtils.getNowTime());
			gjtFeedback.setType("ask");
			gjtFeedback.setSource(source);
			gjtFeedback.setCreatorRole("student");
			gjtFeedback.setDealResult("N");
			gjtFeedbackService.saveGjtFeedback(gjtFeedback);

			// 追问以后把问题改成未解决
			gjtFeedbackService.updateDealResultById(pid);

		} else {
			throw new CommonException(MessageCode.BIZ_ERROR, "请耐心等待老师解答！");
		}
	}
}
