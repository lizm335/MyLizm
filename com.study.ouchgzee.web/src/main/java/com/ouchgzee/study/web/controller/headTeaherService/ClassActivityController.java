/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.controller.headTeaherService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.pojo.GjtActivity;
import com.gzedu.xlims.pojo.GjtActivityComment;
import com.gzedu.xlims.pojo.GjtActivityJoin;
import com.gzedu.xlims.pojo.GjtActivityJoinPK;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.dto.ActivityCommentDto;
import com.gzedu.xlims.pojo.dto.ActivityJoinDto;
import com.gzedu.xlims.pojo.dto.GjtActivityDto;
import com.gzedu.xlims.service.classActivity.GjtActivityCommentService;
import com.gzedu.xlims.service.classActivity.GjtActivityJoinService;
import com.gzedu.xlims.service.classActivity.GjtActivityService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.common.Servlets;
import com.ouchgzee.study.web.vo.ClassActivityVO;

/**
 * 功能说明：个人中心-班主任服务-班级活动
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年3月2日
 * @version 2.5
 * 
 */
@Controller
@RequestMapping("/pcenter/headTeacherService/classActivity")
public class ClassActivityController extends BaseController {

	@Autowired
	GjtActivityService gjtActivityService;

	@Autowired
	GjtActivityJoinService gjtActivityJoinService;

	@Autowired
	GjtActivityCommentService gjtActivityCommentService;

	// 班级活动列表，进行中和已经结束
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, HttpServletRequest request) {
		GjtStudentInfo student = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		GjtClassInfo classInfo = (GjtClassInfo) request.getSession().getAttribute(WebConstants.TEACH_CLASS);

		Map<String, Object> resultMap = new HashMap<String, Object>();

		String classId = classInfo.getClassId();
		String type = request.getParameter("type");// 进行中还是已经结束
		String activityTitle = request.getParameter("activityTitle");// 模糊查询活动标题
		String studentId = student.getStudentId();// request.getParameter("studentId");

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_receiveId", classId);// 查询活动对象ID
		searchParams.put("LIKE_activityTitle", activityTitle);// 活动标题
		// searchParams.put("EQ_auditStatus", "1");// 1是审核通过的
		if (StringUtils.isNotBlank(type)) {
			if ("ongoing".equals(type)) {// 进行中的活动
				searchParams.put("GTE_endTime", DateUtils.getTodayTime());
			} else if ("over".equals(type)) {// 已经结束的活动
				searchParams.put("LT_endTime", DateUtils.getTodayTime());
			}
		}
		Page<GjtActivity> infos = gjtActivityService.queryActivityInfoPage(searchParams, pageRequst);// 罗列活动
		List<GjtActivityJoin> gjtActivityJoinList = gjtActivityJoinService.getActivityIdByStudentId(studentId);// 学员所有报名的活动
		List<ClassActivityVO> list = new ArrayList<ClassActivityVO>();
		for (GjtActivity info : infos) {
			int isEnter = 0;
			BigDecimal studentAuditStatus = null;
			if (gjtActivityJoinList != null && gjtActivityJoinList.size() > 0) {
				for (GjtActivityJoin activityJoin : gjtActivityJoinList) {
					boolean bool = info.getId().equals(activityJoin.getId().getActivityId());
					if (bool) {
						isEnter = 1;// 表示学员报名该活动
						studentAuditStatus = activityJoin.getAuditStatus();// 老师里审核记录
					}
				}
			}
			ClassActivityVO classActivityVO = new ClassActivityVO(info, isEnter, studentAuditStatus);// 页面VO
			list.add(classActivityVO);
		}

		Page<ClassActivityVO> page = new PageImpl<ClassActivityVO>(list, pageRequst, infos.getTotalElements());
		resultMap.put("pageInfo", page);

		Map<String, Object> ongoingCountMap = new HashMap<String, Object>();
		ongoingCountMap.put("GTE_endTime", DateUtils.getTodayTime());
		ongoingCountMap.put("EQ_receiveId", classId);
		ongoingCountMap.put("LIKE_activityTitle", activityTitle);// 活动标题
		// ongoingCountMap.put("EQ_auditStatus", "1");// 1是审核通过的

		resultMap.put("ongoingCount", gjtActivityService.countUnoverNum(ongoingCountMap)); // 当前班级全部进行中活动

		Map<String, Object> overCountMap = new HashMap<String, Object>();
		overCountMap.put("LT_endTime", DateUtils.getTodayTime());
		overCountMap.put("EQ_receiveId", classId);
		overCountMap.put("LIKE_activityTitle", activityTitle);// 活动标题
		// overCountMap.put("EQ_auditStatus", "1");// 1是审核通过的
		resultMap.put("overCount", gjtActivityService.countUnoverNum(overCountMap)); // 当前班级全部过期活动

		return resultMap;
	}

	// 学员报名的活动列表
	@RequestMapping(value = "applyList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> applyList(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, HttpServletRequest request) {
		GjtStudentInfo student = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);

		Map<String, Object> resultMap = new HashMap<String, Object>();

		String type = request.getParameter("type");// 进行中还是已经结束
		String activityTitle = request.getParameter("activityTitle");// 模糊查询活动标题
		String studentId = student.getStudentId();
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("activityTitle", activityTitle);// 活动标题
		searchParams.put("type", type);// 活动类型
		searchParams.put("studentId", studentId);// 学生Id

		Page<GjtActivityDto> infos = gjtActivityJoinService.getActivityByStudent(searchParams, pageRequst);// 学员所有报名的活动
		List<ClassActivityVO> list = new ArrayList<ClassActivityVO>();
		for (GjtActivityDto gjtActivityDto : infos) {
			ClassActivityVO classActivityVO = new ClassActivityVO(gjtActivityDto);// 页面VO
			list.add(classActivityVO);
		}

		Page<ClassActivityVO> page = new PageImpl<ClassActivityVO>(list, pageRequst, infos.getTotalElements());
		resultMap.put("pageInfo", page);
		return resultMap;
	}

	// 报名活动
	@RequestMapping(value = "applyAdd", method = RequestMethod.POST)
	@ResponseBody
	public void toCreateActivity(HttpServletRequest request) throws CommonException {
		String activityId = request.getParameter("activityId");// 活动ID
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String studentId = user.getGjtStudentInfo().getStudentId();// 学生ID
		GjtActivityJoin activityJoin = gjtActivityJoinService.queryById(studentId, activityId);
		if (activityJoin != null) {
			throw new CommonException(101, "已经报名,请勿重复报名");
		}

		if (StringUtils.isBlank(activityId)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "activityId参数为空");
		}
		GjtActivity gjtActivity = gjtActivityService.queryById(activityId);

		GjtActivityJoin item = new GjtActivityJoin();
		GjtActivityJoinPK pk = new GjtActivityJoinPK();
		pk.setActivityId(activityId);
		pk.setStudentId(studentId);
		item.setId(pk);
		item.setJoinDt(DateUtils.getNowTime());

		String db = String.valueOf(gjtActivity.getAuditStatus());// 参加活动人员是否要审核0否1是
		if ("0".equals(db)) {
			item.setAuditStatus(new BigDecimal(1));// 直接审核通过
		} else {
			item.setAuditStatus(new BigDecimal(0));// 待审核
		}
		boolean insert = gjtActivityJoinService.insert(item);
		if (insert) {
			gjtActivity.setJoinNum(gjtActivity.getJoinNum().add(new BigDecimal("1")));
			gjtActivityService.insert(gjtActivity);// 追加报名人数
		}
	}

	// 班级活动详情
	@RequestMapping(value = "activityDetails", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> activityDetails(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String activityId = request.getParameter("activityId");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String studentId = user.getGjtStudentInfo().getStudentId();// 学生ID
		GjtActivity gjtActivity = gjtActivityService.queryById(activityId); // 活动详情
		GjtActivityJoin item = gjtActivityJoinService.queryById(studentId, activityId);

		ClassActivityVO vo = new ClassActivityVO(gjtActivity);
		if (item != null) {
			vo.setIsEnter(1);
			vo.setStudentAuditStatus(item.getAuditStatus());
		} else {
			vo.setIsEnter(0);
		}

		resultMap.put("info", vo);
		return resultMap;
	}

	// 所有报名学员
	@RequestMapping(value = "getPersonnels", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getPersonnels(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String activityId = request.getParameter("activityId");
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Page<ActivityJoinDto> pageInfo = gjtActivityJoinService.getActivityStudentsInfoPage(activityId, pageRequst);// 参与人员详情
		for (Iterator<ActivityJoinDto> iterator = pageInfo.iterator(); iterator.hasNext();) {
			ActivityJoinDto info = iterator.next();
			info.setJoinDate(DateUtils.getStringToDate(info.getJoinDt()));
		}
		resultMap.put("pageInfo", pageInfo);
		return resultMap;
	}

	// 获取活动的所有评论
	@RequestMapping(value = "getComments", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getComments(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String activityId = request.getParameter("activityId");
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Page<ActivityCommentDto> pageInfo = gjtActivityCommentService.getActivityCommentInfo(activityId, pageRequst);// 评论显示
		for (Iterator<ActivityCommentDto> iterator = pageInfo.iterator(); iterator.hasNext();) {
			ActivityCommentDto info = iterator.next();
			info.setCommentDate(DateUtils.getStringToDate(info.getCommentDt()));
		}
		resultMap.put("pageInfo", pageInfo);
		return resultMap;
	}

	// 给活动发表评论
	@RequestMapping(value = "commentAdd", method = RequestMethod.POST)
	@ResponseBody
	public void batchAuditWait(HttpServletRequest request) throws CommonException {
		GjtStudentInfo student = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		String activityId = request.getParameter("activityId");// 活动ID
		if (StringUtils.isBlank(activityId)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "activityId参数为空");
		}

		String studentId = student.getStudentId();// 学生ID
		String comment = request.getParameter("comment");// 评论内容
		GjtActivityComment item = new GjtActivityComment();
		item.setCommentDt(DateUtils.getNowTime());
		item.setComments(comment);
		item.setActivityId(activityId);
		item.setStudentId(studentId);
		item.setId(UUID.randomUUID().toString());
		item.setStar(new BigDecimal(5));// 星级，因为页面上没有只能写死了
		boolean insert = gjtActivityCommentService.insert(item);
		if (insert) {
			GjtActivity gjtActivity = gjtActivityService.queryById(activityId);
			gjtActivity.setCommentNum(gjtActivity.getCommentNum().add(new BigDecimal("1")));
			gjtActivityService.insert(gjtActivity);// 追加评论数
		}
	}
}
