/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.biz.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.ouchgzee.headTeacher.pojo.BzrGjtActivity;
import com.ouchgzee.headTeacher.service.activity.BzrGjtActivityService;

/**
 * 班级活动接口测试类<br>
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月12日
 * @version 1.0
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class ActivityServiceTest {
	@Autowired
	private BzrGjtActivityService gjtActivityService;

	@Test
	public void waitActivityNumTest() {
//		System.out.println(gjtActivityService.countWaitActivityNum());
	}

	public void addActivityTest() {
		BzrGjtActivity gjtActivity = new BzrGjtActivity();
		gjtActivity.setId(UUIDUtils.create());
		String startdateStr = "2015-05-09 12:00";
		String enddateStr = "2015-08-10 12:36";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			gjtActivity.setBeginTime(sdf.parse(startdateStr));
			gjtActivity.setEndTime(sdf.parse(enddateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String result = "1";
		result = gjtActivityService.addActivity(gjtActivity);
		Assert.isTrue("1".equals(result));
	}

	public void updateActivityTest() {
		BzrGjtActivity gjtActivity = gjtActivityService.queryById("6a7f8bc048d011e603ce8742a1fae841");
		// String startdateStr = "2017-05-09 12:00:40";
		// String enddateStr = "2017-06-09 13:36:40";
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// try {
		// gjtActivity.setBeginTime(sdf.parse(startdateStr));
		// gjtActivity.setEndTime(sdf.parse(enddateStr));
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		gjtActivity.setActivityTitle("Title");
		String s = "xxxxx";
		try {
			// Clob activityIntroduce = new javax.sql.rowset.serial.SerialClob(
			// s.toCharArray());
			gjtActivity.setActivityIntroduce(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String result = "1";
		result = gjtActivityService.updateActivity(gjtActivity);
		Assert.isTrue("1".equals(result));
	}

	public void deleteActivityTest() {
		BzrGjtActivity gjtActivity = new BzrGjtActivity();
		gjtActivity.setId("d846130048cf11e66f290bb420b07709");
		String result = "1";
		result = gjtActivityService.deleteActivity(gjtActivity);
		Assert.isTrue("1".equals(result));
	}

	public void queryActivityInfoByClassIdPageTest() {
		PageRequest paramPageRequest = new PageRequest(0, 15);
		Map<String, Object> searchParams = new HashMap();
		searchParams.put("EQ_auditStatus", Constants.ServiceStatus_0);
		searchParams.put("LIKE_activityTitle", "Title");
		String classId = "4";
		Page<BzrGjtActivity> result = gjtActivityService.queryActivityInfoPage(searchParams, paramPageRequest);
		for (BzrGjtActivity gjtActivity : result) {
			System.out.println("id:" + gjtActivity.getActivityTitle());
		}
		Assert.notEmpty(result.getContent());

	}

	public void queryAllActivityTest() {
		Map<String, Object> searchParams = new HashMap();
		searchParams.put("EQ_receiveType", "4");
		searchParams.put("EQ_receiveId", "aaaa");
		PageRequest paramPageRequest = new PageRequest(0, 10);
		Page<BzrGjtActivity> result = gjtActivityService.queryByPage(searchParams, paramPageRequest);
		System.out.println(result.getContent().isEmpty());
		if (result != null && !result.getContent().isEmpty()) {
			List<BzrGjtActivity> gjtActivityList = result.getContent();
			for (BzrGjtActivity gjtActivity : gjtActivityList) {
				System.out.println("id:" + gjtActivity.getId());
			}
		}
		Assert.isTrue(!result.getContent().isEmpty());
	}

	public void countOverNumTest() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currenTime = format.format(date);
	}

}
