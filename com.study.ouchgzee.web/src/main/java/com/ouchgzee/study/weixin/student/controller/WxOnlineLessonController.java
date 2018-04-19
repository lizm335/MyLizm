/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.study.weixin.student.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.pojo.openClass.LcmsOnlineLesson;
import com.gzedu.xlims.service.edumanage.LcmsOnlineLessonService;
import com.ouchgzee.study.web.common.BaseController;

/**
 * 功能说明：直播微信页
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年12月14日
 * @version 3.0
 *
 */
@Controller
@RequestMapping("/wx/onlinelesson")
public class WxOnlineLessonController extends BaseController {

	@Autowired
	private LcmsOnlineLessonService lcmsOnlineLessonService;

	@ResponseBody
	@RequestMapping(value = "queryLiveVideoInfo")
	public Map<String, Object> queryLiveVideoInfo(HttpServletRequest request, String id) {
		LcmsOnlineLesson onlineLesson = lcmsOnlineLessonService.queryById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", onlineLesson.getId());
		result.put("lessonName", onlineLesson.getOnlinetutorName());
		result.put("startTime", com.gzedu.xlims.common.DateUtil.dateToString(onlineLesson.getOnlinetutorStart(), "yyyy-MM-dd HH:mm"));
		result.put("imgUrl", onlineLesson.getImgUrl());
		result.put("label", onlineLesson.getOnlinetutorLabel());
		result.put("content", onlineLesson.getActivityContent());
		long timestemp = System.currentTimeMillis();
		int status;
		if (onlineLesson.getOnlinetutorStart().getTime() > timestemp) {
			status = 0;
		} else if (onlineLesson.getOnlinetutorFinish().getTime() < timestemp) {
			status = 2;
		} else {
			status = 1;
		}
		result.put("status", status);
		result.put("roomId", onlineLesson.getNumber());
		result.put("clientToken", onlineLesson.getStudenttoken());
		result.put("appToken", onlineLesson.getStudentclienttoken());
		if (StringUtils.isEmpty(onlineLesson.getVideojoinurl())) {
			lcmsOnlineLessonService.saveVideoUrl(onlineLesson);
		}
		String videoRoomId = null;
		if (StringUtils.isNotEmpty(onlineLesson.getVideojoinurl())) {
			videoRoomId = onlineLesson.getVideojoinurl().substring(onlineLesson.getVideojoinurl().lastIndexOf("/") + 1);
		}
		result.put("videoRoomId", videoRoomId);
		result.put("videoToken", onlineLesson.getVideotoken());
		return result;
	}
}
