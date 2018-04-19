/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.web.controller.classManager.student;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.UUIDUtils;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtReport;
import com.ouchgzee.headTeacher.service.report.BzrGjtReportService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 日报控制器<br>
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月28日
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/home/class/report")
public class ReportController extends BaseController {
	@Autowired
	BzrGjtReportService gjtReportService;

	/**
	 * 日报列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(Model model) {
		PageRequest pageRequst = new PageRequest(0, 31);
		Map<String, Object> searchParams = new HashMap();
		// 日报
		searchParams.put("EQ_reportType", Constants.BOOLEAN_0);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String firstDay = format.format(c.getTime());
		searchParams.put("GTE_createdDt", firstDay);
		Page<BzrGjtReport> gjtReportList = gjtReportService
				.queryReportInfoByCreatedDtPage(searchParams, pageRequst);
		JSONArray dateJSON = new JSONArray();
		for (BzrGjtReport gjtReport : gjtReportList) {
			JSONObject obj = new JSONObject();
			obj.put("id", gjtReport.getId());
			String date = format.format(gjtReport.getCreatedDt());
			obj.put("date", date);
			obj.put("eventDes", gjtReport.getSummary());
			obj.put("hasRemark", !(gjtReport.getComments() == null));
			dateJSON.add(obj);
		}
		model.addAttribute("dateJSON", dateJSON.toString());
		return "new/class/report/list";
	}

	/**
	 * AJAX获取日报列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "ajaxList", method = RequestMethod.POST)
	@ResponseBody
	public String ajaxList(int monthNum) {
		PageRequest pageRequst = new PageRequest(0, 105);
		Map<String, Object> searchParams = new HashMap();
		// 日报
		searchParams.put("EQ_reportType", Constants.BOOLEAN_0);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		// int monthNumInt = monthNum;
		if (monthNum > 0) {
			c.add(Calendar.MONTH, monthNum + 1);

			c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
			String endDay = format.format(c.getTime());
			c.add(Calendar.MONTH, -2);
			c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			String firstDay = format.format(c.getTime());
			searchParams.put("GTE_createdDt", firstDay);
			searchParams.put("LTE_createdDt", endDay);
		} else {
			c.add(Calendar.MONTH, monthNum - 1);
			c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			String firstDay = format.format(c.getTime());
			searchParams.put("GTE_createdDt", firstDay);
			c.add(Calendar.MONTH, 2);
			c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
			String endDay = format.format(c.getTime());
			searchParams.put("LTE_createdDt", endDay);
		}
		Page<BzrGjtReport> gjtReportList = gjtReportService
				.queryReportInfoByCreatedDtPage(searchParams, pageRequst);
		JSONArray dateJSON = new JSONArray();
		for (BzrGjtReport gjtReport : gjtReportList) {
			JSONObject obj = new JSONObject();
			obj.put("id", gjtReport.getId());
			String date = format.format(gjtReport.getCreatedDt());
			obj.put("date", date);
			obj.put("eventDes", gjtReport.getSummary());
			obj.put("hasRemark", !(gjtReport.getComments() == null));
			dateJSON.add(obj);
		}
		return dateJSON.toString();
	}

	/**
	 * 获取周报信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "weeklyList/{beginTime}", method = RequestMethod.GET)
	public String weeklyList(ServletRequest request, Model model,
			@PathVariable("beginTime") String beginTime) {
		PageRequest pageRequst = new PageRequest(0, 2);
		Map<String, Object> searchParams = new HashMap();
		// 日报
		searchParams.put("EQ_reportType", Constants.BOOLEAN_1);

		// String beginTime = request.getParameter("beginTime");
		// String endTime = request.getParameter("endTime");
		searchParams.put("GTE_createdDt", beginTime);
		// searchParams.put("LTE_createdDt", endTime);
		Page<BzrGjtReport> gjtReportList = gjtReportService
				.queryReportInfoByCreatedDtPage(searchParams, pageRequst);
		model.addAttribute("gjtReportList", gjtReportList);
		return "new/class/report/weeklyList";

	}

	/**
	 * AJAX获取周报信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "weeklyInfo", method = RequestMethod.POST)
	@ResponseBody
	public String weeklyInfo(String beginTime, String endTime, Model model) {
		PageRequest pageRequst = new PageRequest(0, 2);
		Map<String, Object> searchParams = new HashMap();
		// 日报
		searchParams.put("EQ_reportType", Constants.BOOLEAN_1);
		searchParams.put("GTE_createdDt", beginTime);
		searchParams.put("LTE_createdDt", endTime);
		Page<BzrGjtReport> gjtReportList = gjtReportService
				.queryReportInfoByCreatedDtPage(searchParams, pageRequst);
		String result = "{}";
		model.addAttribute("gjtReportList", gjtReportList);
		if (gjtReportList.getNumberOfElements() == 0) {
			return result;
		} else {
			return gjtReportList.getContent().get(0).toString();
		}
		// System.out.println("gjtReportList.getContent().get(0):"
		// + gjtReportList.getContent().get(0));
		// GjtReport gjtReport = gjtReportList.getContent().get(0);
		// JSONObject result = new JSONObject();
		// result.put("gjtReport", gjtReport.toString());
		// result.put("beginTime", gjtReport.getBeginTime());
		// result.put("endTime", gjtReport.getEndTime());
		// result.put("summary", gjtReport.getSummary());
		// result.put("nextplan", gjtReport.getNextplan());

	}

	/**
	 * 加载新增日报
	 * 
	 * @return
	 */
	@RequestMapping(value = "toCreateReport", method = RequestMethod.GET)
	public String toCreateReport() {
		return "new/class/report/addDaily";
	}

	/**
	 * 创建日报
	 * 
	 * @return
	 */
	@RequestMapping(value = "doCreateReport", method = RequestMethod.POST)
	public String doCreateReport(ServletRequest request,
			RedirectAttributes redirectAttributes, HttpSession session) {
		Feedback feedback = new Feedback(true, "创建成功");

		String createdDtStr = request.getParameter("createdDt");
		String summary = request.getParameter("summary");
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		try {
			BzrGjtReport gjtReport = new BzrGjtReport();
			gjtReport.setId(UUIDUtils.create());
			gjtReport.setReportType(new BigDecimal("0"));
			gjtReport.setSummary(summary);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date createdDt = sdf.parse(createdDtStr);
			gjtReport.setCreatedDt(createdDt);
			gjtReport.setCreatedBy(employeeInfo.getEmployeeId());
			String result = gjtReportService.addReport(gjtReport);
			if (!"1".equals(result)) {
				feedback = new Feedback(false, "创建失败");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/home/class/report/list";
	}

	/**
	 * 加载日报详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "toViewReport/{id}", method = RequestMethod.GET)
	public String toViewReport(@PathVariable("id") String id, Model model) {
		BzrGjtReport gjtReport = gjtReportService.queryById(id);
		model.addAttribute("info", gjtReport);
		return "new/class/report/viewDaily";
	}

	/**
	 * 加载修改日报
	 * 
	 * @return
	 */
	@RequestMapping(value = "toUpdateReport/{id}", method = RequestMethod.GET)
	public String toUpdateReport(@PathVariable("id") String id, Model model) {
		BzrGjtReport gjtReport = gjtReportService.queryById(id);
		model.addAttribute("info", gjtReport);
		return "new/class/report/updateDaily";
	}

	/**
	 * 修改日报
	 * 
	 * @return
	 */
	@RequestMapping(value = "doUpdateReport/{id}", method = RequestMethod.POST)
	public String doUpdateReport(@PathVariable("id") String id,
			ServletRequest request, RedirectAttributes redirectAttributes,
			HttpSession session) {
		Feedback feedback = new Feedback(true, "修改成功");

		String updateDtStr = request.getParameter("updateDt");
		String summary = request.getParameter("summary");
		BzrGjtReport gjtReport = gjtReportService.queryById(id);
		try {
			if (!"".equals(summary))
				gjtReport.setSummary(summary);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date updateDt = sdf.parse(updateDtStr);
			gjtReport.setUpdatedDt(updateDt);

			String result = gjtReportService.updateReport(gjtReport);
			if (!"1".equals(result)) {
				feedback = new Feedback(false, "修改失败");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "修改失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/home/class/report/list";
	}

	/**
	 * 加载新增周报
	 * 
	 * @return
	 */
	@RequestMapping(value = "toCreateReportWeekly", method = RequestMethod.GET)
	public String toCreateReportWeekly() {
		return "new/class/report/addWeekly";
	}

	/**
	 * 创建周报
	 * 
	 * @return
	 */
	@RequestMapping(value = "doCreateReportWeekly", method = RequestMethod.POST)
	public String doCreateReportWeekly(ServletRequest request,
			RedirectAttributes redirectAttributes, HttpSession session) {
		Feedback feedback = new Feedback(true, "创建成功");

		String createdDtStr = request.getParameter("createdDt");
		String beginTimeStr = request.getParameter("beginTime");
		String endTimeStr = request.getParameter("endTime");
		String summary = request.getParameter("summary");
		String nextplan = request.getParameter("nextplan");
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		try {
			BzrGjtReport gjtReport = new BzrGjtReport();
			gjtReport.setId(UUIDUtils.create());
			gjtReport.setReportType(new BigDecimal("1"));
			gjtReport.setSummary(summary);
			gjtReport.setNextplan(nextplan);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date createdDt = sdf.parse(createdDtStr);
			Date beginTime = sdf.parse(beginTimeStr);
			Date endTime = sdf.parse(endTimeStr);
			gjtReport.setCreatedDt(createdDt);
			gjtReport.setBeginTime(beginTime);
			gjtReport.setEndTime(endTime);
			gjtReport.setCreatedBy(employeeInfo.getEmployeeId());
			String result = gjtReportService.addReport(gjtReport);
			if (!"1".equals(result)) {
				feedback = new Feedback(false, "创建失败");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/home/class/report/list";
	}

	/**
	 * 加载修改周报
	 * 
	 * @return
	 */
	@RequestMapping(value = "toUpdateReportWeekly/{id}", method = RequestMethod.GET)
	public String toUpdateReportWeekly(@PathVariable("id") String id,
			Model model) {
		BzrGjtReport gjtReport = gjtReportService.queryById(id);
		model.addAttribute("info", gjtReport);
		return "new/class/report/updateWeekly";
	}

	/**
	 * 修改周报
	 * 
	 * @return
	 */
	@RequestMapping(value = "doUpdateReportWeekly/{id}", method = RequestMethod.POST)
	public String doUpdateReportWeekly(@PathVariable("id") String id,
			ServletRequest request, RedirectAttributes redirectAttributes,
			HttpSession session) {
		Feedback feedback = new Feedback(true, "修改成功");

		String updatedDtStr = request.getParameter("updatedDt");
		String summary = request.getParameter("summary");
		String nextplan = request.getParameter("nextplan");
		String beginTimeStr = request.getParameter("beginTime");
		String endTimeStr = request.getParameter("endTime");
		BzrGjtReport gjtReport = gjtReportService.queryById(id);
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		try {
			// if (!"".equals(summary))
			gjtReport.setSummary(summary);
			// if (!"".equals(summary))
			gjtReport.setNextplan(nextplan);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date updatedDt = sdf.parse(updatedDtStr);
			gjtReport.setUpdatedDt(updatedDt);
			Date beginTime = sdf.parse(beginTimeStr);
			Date endTime = sdf.parse(endTimeStr);
			gjtReport.setUpdatedDt(updatedDt);
			gjtReport.setBeginTime(beginTime);
			gjtReport.setEndTime(endTime);
			gjtReport.setUpdatedBy(employeeInfo.getEmployeeId());
			String result = gjtReportService.updateReport(gjtReport);
			if (!"1".equals(result)) {
				feedback = new Feedback(false, "修改失败");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "修改失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String firstDay = format.format(c.getTime());
		return "redirect:/home/class/report/weeklyList/" + firstDay;
	}

}
