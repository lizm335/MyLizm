/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.common.task;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtCourseCheck;
import com.gzedu.xlims.service.GjtCourseService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年9月28日
 * @version 3.0
 *
 */
public class CheckCourseTask {
	private static final Logger log = LoggerFactory.getLogger(CheckCourseTask.class);

	@Autowired
	GjtCourseService gjtCourseService;

	public void checkTask() {
		List<Map<String, Object>> courseList = gjtCourseService.queryCourseIsEnabled();
		if (courseList.size() > 0) {
			for (Map<String, Object> map : courseList) {
				String courseId = (String) map.get("COURSE_ID");
				log.info("部分启用的课程Id={}", courseId);
				long checkCourseCount = gjtCourseService.checkCourseCount(courseId, "N");
				if (checkCourseCount == 0) {
					try {
						GjtCourse modifyInfo = gjtCourseService.queryBy(courseId);
						gjtCourseService.updateIsEnabled(courseId, "3");

						// 记录审批记录
						GjtCourseCheck item = new GjtCourseCheck();
						item.setId(UUIDUtils.random().toString());
						item.setAuditContent("系统把部分启用状态更改为待验收！");
						item.setAuditDt(DateUtils.getNowTime());
						item.setAuditState("1");
						item.setGjtCourse(modifyInfo);
						item.setCreatedBy("系统操作");
						item.setCreatedDt(DateUtils.getNowTime());
						item.setIsDeleted("N");
						item.setVersion(new BigDecimal("1"));
						gjtCourseService.sumbitCheck(item);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}

				}
			}
		}
	}
}
