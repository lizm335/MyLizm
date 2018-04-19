/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.classManager.message;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.ouchgzee.headTeacher.dto.CourseTeacherDto;
import com.ouchgzee.headTeacher.dto.FeedbackSolvedDto;
import com.ouchgzee.headTeacher.dto.FeedbackUnsolvedDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtFeedback;
import com.ouchgzee.headTeacher.pojo.status.FeedbackEnum;
import com.ouchgzee.headTeacher.service.employee.BzrGjtEmployeeInfoService;
import com.ouchgzee.headTeacher.service.feedback.BzrGjtFeedbackService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * 留言反馈控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月1日
 * @version 2.5
 */
@Controller
@RequestMapping("/home/class/feedback")
public class FeedbackController extends BaseController {

	@Autowired
	private BzrGjtFeedbackService gjtFeedbackService;

	@Autowired
	private BzrGjtEmployeeInfoService gjtEmployeeInfoService;

	/**
	 * 查询当前班主任，当前班级的学生提出的问题未解决的问题
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request,
			HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		String currentClassId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID))
				? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(session);

		searchParams.put("bzrId", employeeInfo.getEmployeeId());
		searchParams.put("classId", currentClassId);

		Page<FeedbackUnsolvedDto> infos = gjtFeedbackService.queryUnsolvedFeedBackByBzrIdPage(searchParams, pageRequst);

		// 获取教学班下面的课程班的辅导老师信息
		List<CourseTeacherDto> teacherList = gjtEmployeeInfoService.queryCourseTeacherByTeachClassId(currentClassId);

		model.addAttribute("infos", infos);
		model.addAttribute("teacherList", teacherList);
		model.addAttribute("umsCount", gjtFeedbackService.countUnsolvedFeedBack(searchParams));// 未解决的总数
		model.addAttribute("sCount", gjtFeedbackService.countSolvedFeedBack(searchParams));// 已经解决总数
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "new/class/feedback/list";
	}

	/**
	 * 已解决的答疑列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "solvedList", method = RequestMethod.GET)
	public String solvedList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request,
			HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		String currentClassId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID))
				? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(session);

		searchParams.put("bzrId", employeeInfo.getEmployeeId());
		searchParams.put("classId", currentClassId);

		Page<FeedbackSolvedDto> infos = gjtFeedbackService.querySolvedFeedBackByPage(searchParams, pageRequst);

		model.addAttribute("infos", infos);
		model.addAttribute("umsCount", gjtFeedbackService.countUnsolvedFeedBack(searchParams));
		model.addAttribute("sCount", gjtFeedbackService.countSolvedFeedBack(searchParams));
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "new/class/feedback/solved_list";
	}

	/**
	 * 回复
	 * 
	 * @param id
	 * @param answerContent
	 * @return
	 */
	@RequestMapping(value = "answer", method = RequestMethod.POST)
	@ResponseBody
	public Feedback answer(@RequestParam(value = "id") String id,
			@RequestParam(value = "answerContent") String answerContent, HttpServletRequest request,
			HttpSession session) {
		Feedback feedback = new Feedback(true, "回复成功");
		try {
			StringBuffer imgUrls = new StringBuffer();
			String[] parameterValues = request.getParameterValues("imgUrl");
			if (parameterValues != null) {
				for (int i = 0; i < parameterValues.length; i++) {
					imgUrls.append(parameterValues[i]);
					if (i < parameterValues.length - 1) {
						imgUrls.append(",");
					}
				}
			}

			BzrGjtFeedback gjtFeedback = new BzrGjtFeedback();
			gjtFeedback.setId(UUIDUtils.random());
			gjtFeedback.setPid(id);
			gjtFeedback.setContent(answerContent);
			gjtFeedback.setCreatedDt(DateUtils.getNowTime());
			gjtFeedback.setUserAgent(request.getHeader("User-Agent"));
			BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
			gjtFeedback.setCreatedBy(employeeInfo.getEmployeeId());
			gjtFeedback.setDealResult("Y");
			gjtFeedback.setImgUrls(imgUrls.toString());
			gjtFeedback.setCreatorRole(FeedbackEnum.Role.HEADTEACHER.getValue());
			gjtFeedback.setSource(FeedbackEnum.Source.PC.getValue());
			gjtFeedback.setType(FeedbackEnum.Type.REPLY.getValue());

			// 老师回复，并设置学员问题已经解决
			gjtFeedbackService.insert(gjtFeedback);

		} catch (Exception e) {
			e.printStackTrace();
			feedback = new Feedback(false, "回复失败");
		}
		return feedback;
	}

	/**
	 * 转发给其他老师
	 * 
	 * @param id
	 * @param shareEmployeeId
	 * @return
	 */
	@RequestMapping(value = "shareTeacher", method = RequestMethod.POST)
	@ResponseBody
	public Feedback shareTeacher(@RequestParam(value = "id") String id,
			@RequestParam(value = "shareEmployeeId") String shareEmployeeId, HttpServletRequest request,
			HttpSession session) {
		Feedback feedback = new Feedback(true, "转发成功");
		try {
			BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
			gjtFeedbackService.shareTeacher(id, shareEmployeeId, employeeInfo.getGjtUserAccount().getId());
		} catch (Exception e) {
			feedback = new Feedback(false, "转发失败");
		}
		return feedback;
	}

}
