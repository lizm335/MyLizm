package com.ouchgzee.headTeacher.web.controller.classManager.student;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.ouchgzee.headTeacher.dto.ActivityCommentDto;
import com.ouchgzee.headTeacher.dto.ActivityJoinDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtActivity;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.service.activity.BzrGjtActivityCommentService;
import com.ouchgzee.headTeacher.service.activity.BzrGjtActivityJoinService;
import com.ouchgzee.headTeacher.service.activity.BzrGjtActivityService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月23日
 * @version 2.5
 *
 */

@Controller
@RequestMapping("/api/activity")
public class GjtActivityStudyController extends BaseController {
	
	@Autowired
	BzrGjtActivityService gjtActivityService;
	@Autowired
	BzrGjtActivityJoinService gjtActivityJoinService;

	@Autowired
	BzrGjtActivityCommentService gjtActivityCommentService;

	/**
	 * 班级活动列表
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

		String currentClassId = super.getCurrentClassId(session);
		searchParams.put("GTE_endTime", DateUtils.getTodayTime());
		searchParams.put("EQ_receiveId", currentClassId);// 查询活动对象ID
		Page<BzrGjtActivity> infos = gjtActivityService.queryActivityInfoPage(searchParams, pageRequst);

		for (Iterator<BzrGjtActivity> iterator = infos.iterator(); iterator.hasNext();) {
			BzrGjtActivity info = iterator.next();
			Long colWaitActivityNum = gjtActivityJoinService.countApplyNum(info.getId(), "0");// 统计待审核人数
			info.setColWaitActivityNum(colWaitActivityNum);
		}
		model.addAttribute("infos", infos);

		model.addAttribute("unCount", gjtActivityService.countUnoverNum(searchParams)); // 当前全部进行中的活动

		Map<String, Object> overCountMap = new HashMap<String, Object>();
		overCountMap.put("LT_endTime", DateUtils.getTodayTime());
		overCountMap.put("EQ_receiveId", currentClassId);
		model.addAttribute("overCount", gjtActivityService.countUnoverNum(overCountMap)); // 当前全部过期活动

		model.addAttribute("flag", "0");// 标记jsp页面未结束活动的Tab
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "new/class/activity/list";
	}

	/**
	 * 已结束活动列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "overList", method = RequestMethod.GET)
	public String finishList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request,
			HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		String currentClassId = super.getCurrentClassId(session);
		searchParams.put("LT_endTime", DateUtils.getTodayTime());
		searchParams.put("EQ_receiveId", currentClassId);// 查询活动对象ID
		Page<BzrGjtActivity> infos = gjtActivityService.queryActivityInfoPage(searchParams, pageRequst);
		for (Iterator<BzrGjtActivity> iterator = infos.iterator(); iterator.hasNext();) {
			BzrGjtActivity info = iterator.next();
			Long colWaitActivityNum = gjtActivityJoinService.countApplyNum(info.getId(), "0");//// 统计待审核人数
			info.setColWaitActivityNum(colWaitActivityNum);
		}
		model.addAttribute("infos", infos);

		model.addAttribute("overCount", gjtActivityService.countUnoverNum(searchParams)); // 当前全部过期的活动

		Map<String, Object> unCountMap = new HashMap<String, Object>();
		unCountMap.put("GTE_endTime", DateUtils.getTodayTime());
		unCountMap.put("EQ_receiveId", currentClassId);
		model.addAttribute("unCount", gjtActivityService.countUnoverNum(unCountMap)); // 当前全部进行中活动

		model.addAttribute("flag", "1");// 标记jsp页面结束活动的Tab
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "new/class/activity/list";
	}

	/**
	 * 加载新增活动
	 * 
	 * @return
	 */
	@RequestMapping(value = "toCreateActivity", method = RequestMethod.GET)
	public String toCreateActivity(Model model, HttpServletRequest request) {
		model.addAttribute("isAdd", true);
		return "new/class/activity/form";
	}

	/**
	 * 创建班级活动
	 * 
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Feedback doCreateActivity(ServletRequest request, RedirectAttributes redirectAttributes,
			HttpSession session) {
		Feedback feedback = new Feedback(true, "创建成功");

		String beginTimeStr = request.getParameter("beginTime");
		String endTimeStr = request.getParameter("endTime");
		String[] activityPictures = request.getParameterValues("activityPicture");

		String activityTitle = request.getParameter("activityTitle");
		String ceilingNumStr = request.getParameter("ceilingNum");
		String auditStatusStr = request.getParameter("auditStatus");
		String isFreeStr = request.getParameter("isFree");
		String activityAddress = request.getParameter("activityAddress");
		String publicityPicture = request.getParameter("publicityPicture");
		String activityPicture = "";
		if (activityPictures.length > 0) {
			for (int i = 0; i < activityPictures.length; i++) {
				if (StringUtils.isNotBlank(activityPictures[i])) {
					activityPicture += activityPictures[i];
					if (i < activityPictures.length - 1) {
						activityPicture += ",";
					}
				}
			}
		}
		String activityIntroduce = request.getParameter("activityIntroduce");
		String chargeMoneyStr = request.getParameter("chargeMoney");
		BigDecimal auditStatus = new BigDecimal(auditStatusStr);
		BigDecimal isFree = new BigDecimal(isFreeStr);
		BigDecimal ceilingNum = new BigDecimal(ceilingNumStr);
		BigDecimal receiveType = new BigDecimal("4");
		BigDecimal chargeMoney;
		if (isFreeStr.equals("2") && StringUtils.isNotBlank(chargeMoneyStr)) {
			chargeMoney = new BigDecimal(chargeMoneyStr);
		} else {
			chargeMoney = new BigDecimal("0");
		}
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		try {
			BzrGjtActivity gjtActivity = new BzrGjtActivity();
			gjtActivity.setId(UUIDUtils.create());
			gjtActivity.setActivityAddress(activityAddress);
			gjtActivity.setActivityTitle(activityTitle);
			gjtActivity.setAuditStatus(auditStatus);
			gjtActivity.setActivityIntroduce(activityIntroduce);
			gjtActivity.setIsFree(isFree);
			gjtActivity.setPublicityPicture(publicityPicture);
			gjtActivity.setActivityPicture(activityPicture);
			gjtActivity.setCeilingNum(ceilingNum);
			gjtActivity.setCreatedBy(employeeInfo.getEmployeeId());
			gjtActivity.setChargeMoney(chargeMoney);
			gjtActivity.setBeginTime(DateUtils.getTime(beginTimeStr));
			gjtActivity.setEndTime(DateUtils.getTime(endTimeStr));
			gjtActivity.setReceiveId(super.getCurrentClassId(session));
			gjtActivity.setReceiveType(receiveType);
			String result = gjtActivityService.addActivity(gjtActivity);
			if (!"1".equals(result)) {
				feedback = new Feedback(false, "创建失败");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
			e.printStackTrace();
		}
		return feedback;
	}

	/**
	 * 班级活动详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize) {
		// 活动详情
		BzrGjtActivity gjtActivity = gjtActivityService.queryById(id);
		model.addAttribute("info", gjtActivity);
		// 参与人员详情
		List<ActivityJoinDto> waitList = gjtActivityJoinService.getActivityStudentsInfo(id, "0");// 待审核
		List<ActivityJoinDto> passList = gjtActivityJoinService.getActivityStudentsInfo(id, "1");// 审核通过
		List<ActivityJoinDto> unpassList = gjtActivityJoinService.getActivityStudentsInfo(id, "2");// 未通过
		model.addAttribute("waiNum", gjtActivityJoinService.countApplyNum(id, "0"));
		model.addAttribute("passNum", gjtActivityJoinService.countApplyNum(id, "1"));
		model.addAttribute("unpassNum", gjtActivityJoinService.countApplyNum(id, "2"));
		model.addAttribute("waitList", waitList);
		model.addAttribute("passList", passList);
		model.addAttribute("unpassList", unpassList);
		// 评论显示
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Page<ActivityCommentDto> commentsInfos = gjtActivityCommentService.getActivityCommentInfo(id, pageRequst);
		model.addAttribute("commentsInfos", commentsInfos);
		// model.addAttribute("commentsNum",
		// gjtActivityCommentService.countComentsNum(id));

		// 其他参数
		model.addAttribute("action", "view");
		return "new/class/activity/view";
	}

	/**
	 * 加载班级活动修改
	 * 
	 * @return
	 */
	@RequestMapping(value = "toUpdateActivity/{id}", method = RequestMethod.GET)
	public String toUpdateActivity(@PathVariable("id") String id, Model model) {
		BzrGjtActivity gjtActivity = gjtActivityService.queryById(id);
		String activityPicture = gjtActivity.getActivityPicture();

		if (StringUtils.isNotBlank(activityPicture)) {
			String[] activityPictures = activityPicture.split(",");
			List<String> list = new ArrayList<String>();
			for (String string : activityPictures) {
				list.add(string);
			}
			model.addAttribute("activityPictures", list);
		}
		model.addAttribute("info", gjtActivity);
		model.addAttribute("isAdd", false);
		return "new/class/activity/form";
	}

	/**
	 * 修改班级活动
	 * 
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback doUpdateActivity(String id, ServletRequest request, RedirectAttributes redirectAttributes,
			HttpSession session) {
		Feedback feedback = new Feedback(true, "修改成功！", "正在跳转！");

		String activityTitle = request.getParameter("activityTitle");
		String ceilingNumStr = request.getParameter("ceilingNum");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String auditStatusStr = request.getParameter("auditStatus");
		String activityAddress = request.getParameter("activityAddress");
		String isFreeStr = request.getParameter("isFree");
		String publicityPicture = request.getParameter("publicityPicture");

		String activityPicture = "";
		String[] activityPictures = request.getParameterValues("activityPicture");
		if (activityPictures.length > 0) {
			for (int i = 0; i < activityPictures.length; i++) {
				if (StringUtils.isNotBlank(activityPictures[i])) {
					activityPicture += activityPictures[i];
					if (i < activityPictures.length - 1) {
						activityPicture += ",";
					}
				}
			}
		}

		String activityIntroduce = request.getParameter("activityIntroduce");
		String chargeMoneyStr = request.getParameter("chargeMoney");
		BigDecimal auditStatus = new BigDecimal(auditStatusStr);
		BigDecimal ceilingNum = new BigDecimal(ceilingNumStr);
		BigDecimal isFree = new BigDecimal(isFreeStr);
		BigDecimal chargeMoney;
		if (isFreeStr.equals("2") && StringUtils.isNotBlank(chargeMoneyStr)) {
			chargeMoney = new BigDecimal(chargeMoneyStr);
		} else {
			chargeMoney = new BigDecimal("0");
		}
		BzrGjtActivity gjtActivity = gjtActivityService.queryById(id);
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		try {
			if (!"".equals(activityAddress))
				gjtActivity.setActivityAddress(activityAddress);
			if (!"".equals(activityTitle))
				gjtActivity.setActivityTitle(activityTitle);
			if (!"".equals(auditStatus))
				gjtActivity.setAuditStatus(auditStatus);
			if (!"".equals(activityIntroduce))
				gjtActivity.setActivityIntroduce(activityIntroduce);
			if (!"".equals(isFree))
				gjtActivity.setIsFree(isFree);
			if (!"".equals(publicityPicture))
				gjtActivity.setPublicityPicture(publicityPicture);
			if (!"".equals(activityPicture))
				gjtActivity.setActivityPicture(activityPicture);
			if (!"".equals(ceilingNum))
				gjtActivity.setCeilingNum(ceilingNum);
			if (!"".equals(beginTime))
				gjtActivity.setBeginTime(DateUtils.getTime(beginTime));
			if (!"".equals(endTime))
				gjtActivity.setEndTime(DateUtils.getTime(endTime));

			gjtActivity.setChargeMoney(chargeMoney);
			gjtActivity.setUpdatedBy(employeeInfo.getEmployeeId());
			gjtActivity.setUpdatedDt(DateUtils.getDate());

			String result = gjtActivityService.updateActivity(gjtActivity);
			if ("-2".equals(result)) {
				feedback = new Feedback(false, "修改失败!", "查询不到Id!");
			}
			if ("-1".equals(result)) {
				feedback = new Feedback(false, "修改失败!", "开始时间大于活动结束时间!");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "修改失败");
		}

		return feedback;
	}

	/**
	 * 待审核批量审核
	 * 
	 * @param recId
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "auditWait", method = RequestMethod.GET)
	public String batchAuditWait(@RequestParam String activityId, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "批量审核成功");
		try {
			gjtActivityJoinService.updateBatchAuditActivity(activityId, "0");
		} catch (Exception e) {
			feedback = new Feedback(false, "批量审核失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/home/class/activity/view/" + activityId;
	}

	/**
	 * 不通过批量审核
	 * 
	 * @param recId
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "auditUnpass", method = RequestMethod.GET)
	public String batchAuditUnpass(@RequestParam String activityId, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "批量审核成功");
		try {
			gjtActivityJoinService.updateBatchAuditActivity(activityId, "2");
		} catch (Exception e) {
			feedback = new Feedback(false, "批量审核失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/home/class/activity/view/" + activityId;
	}

	/**
	 * 由待审核到审核通过
	 * 
	 * @param recId
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "auditUnpassByStudentId", method = RequestMethod.GET)
	public String auditUnpassByStudentId(@RequestParam String activityId, @RequestParam String studentId,
			@RequestParam String auditStatus, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "审核成功");
		try {
			gjtActivityJoinService.updateBatchAuditActivityBystudentId(activityId, auditStatus, studentId);
		} catch (Exception e) {
			feedback = new Feedback(false, "审核失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/home/class/activity/view/" + activityId;
	}

	/**
	 * 由待审核到不通过
	 * 
	 * @param recId
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "auditUnpassByStudentIdtoUnpass", method = RequestMethod.GET)
	public String auditUnpassByStudentIdtoUnpass(@RequestParam String activityId, @RequestParam String studentId,
			@RequestParam String auditStatus, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "审核成功");
		try {
			gjtActivityJoinService.updateBatchAuditActivitytoUnpass(activityId, auditStatus, studentId);
		} catch (Exception e) {
			feedback = new Feedback(false, "审核失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/home/class/activity/view/" + activityId;
	}

	/**
	 * 导出活动人员
	 * 
	 * @param studentId
	 */
	@RequestMapping(value = "exportActivityNumber", method = RequestMethod.GET)
	public void exportActivityNumber(@RequestParam String activityId, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws IOException {
		BzrGjtActivity gjtActivity = gjtActivityService.queryById(activityId);
		String outputUrl = "班级活动参与人员_" + gjtActivity.getActivityTitle() + "_" + Calendar.getInstance().getTimeInMillis()
				+ ".xls";
		HSSFWorkbook workbook = gjtActivityJoinService.exportActivityNumberToExcel(activityId);

		super.downloadExcelFile(response, workbook, outputUrl);
	}

}
