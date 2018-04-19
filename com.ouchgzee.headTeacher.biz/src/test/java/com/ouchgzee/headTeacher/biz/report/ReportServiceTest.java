/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.biz.report;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.UUIDUtils;
import com.ouchgzee.headTeacher.pojo.BzrGjtReport;
import com.ouchgzee.headTeacher.service.report.BzrGjtReportService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 日报测试类<br>
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月27日
 * @version 1.0
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class ReportServiceTest {
	@Autowired
	private BzrGjtReportService gjtReportService;

	public void updateReportTest() {
		BzrGjtReport gjtReport = gjtReportService
				.queryById("8fc9ad7053d411e6bee89b48058721be");
		System.out.println("0".equals(gjtReport.getReportType().toString()));
		gjtReport.setReportType(new BigDecimal("1"));
		String result = "1";
		result = gjtReportService.updateReport(gjtReport);
		Assert.isTrue("1".equals(result));
	}

	@Test
	public void ajaxWeeklyTest() {
		PageRequest pageRequst = new PageRequest(0, 105);
		Map<String, Object> searchParams = new HashMap();
		// 日报
		searchParams.put("EQ_reportType", Constants.BOOLEAN_0);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		int monthNumInt = 1;
		c.add(Calendar.MONTH, monthNumInt + 1);

		c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
		// String firstDay = format.format(c.getTime());
		String endDay = format.format(c.getTime());
		// c.add(Calendar.DATE, -1);
		// String endDay = format.format(c.getTime());
		if (monthNumInt > 0)
			c.add(Calendar.MONTH, -2);
		if (monthNumInt < 0)
			c.add(Calendar.MONTH, 2);
		// c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		// String endDay = format.format(c.getTime());
		String firstDay = format.format(c.getTime());
		System.out.println("monthNumInt:" + monthNumInt);
		System.out.println("firstDay:" + firstDay);
		System.out.println("endDay:" + endDay);
		searchParams.put("GTE_createdDt", firstDay);
		searchParams.put("LTE_createdDt", endDay);
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
		System.out.println(dateJSON.toString());
	}

	public void ajaxTest() {
		PageRequest pageRequst = new PageRequest(0, 2);
		Map<String, Object> searchParams = new HashMap();
		// 日报
		searchParams.put("EQ_reportType", Constants.BOOLEAN_1);
		searchParams.put("GTE_createdDt", "2016-8-1");
		searchParams.put("LTE_createdDt", "2016-8-7");
		Page<BzrGjtReport> gjtReportList = gjtReportService
				.queryReportInfoByCreatedDtPage(searchParams, pageRequst);
		System.out.println(
				"gjtReportList.getContent():" + gjtReportList.getContent());
		System.out.println("gjtReportList.getNumberOfElements():"
				+ (gjtReportList.getNumberOfElements() == 0));
	}

	public void queryReportInfoByIdPageTest() {
		PageRequest pageRequest = new PageRequest(0, 15);
		Map<String, Object> searchParams = new HashMap();
		// 日报
		searchParams.put("EQ_reportType", Constants.BOOLEAN_0);
		// 周报
		// searchParams.put("EQ_reportType", Constants.BOOLEAN_1);
		// String creatDt = "2015-07-01";
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		// try {
		// sdf.parse(creatDt);
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// Date date = new Date();
		// DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String currenTime = format.format(date);
		searchParams.put("GTE_createdDt", "2015-07-01");
		Page<BzrGjtReport> gjtReportList = gjtReportService
				.queryReportInfoByCreatedDtPage(searchParams, pageRequest);
		JSONArray dateJSON = new JSONArray();
		for (BzrGjtReport gjtReport : gjtReportList) {
			JSONObject obj = new JSONObject();
			obj.put("id", gjtReport.getId());
			obj.put("date", gjtReport.getCreatedDt());
			obj.put("eventDes", gjtReport.getSummary());
			obj.put("hasRemark", !(gjtReport.getComments() == null));
			dateJSON.add(obj);
		}
		System.out.println(dateJSON.toString());
		Assert.notEmpty(gjtReportList.getContent());
	}

	public void addReportTest() {
		BzrGjtReport gjtReport = new BzrGjtReport();
		gjtReport.setId(UUIDUtils.create());
		gjtReport.setReportType(new BigDecimal("0"));
		gjtReport.setCreatedBy("小城");
		String result = "1";
		result = gjtReportService.addReport(gjtReport);
		Assert.isTrue("1".equals(result));
	}
}
