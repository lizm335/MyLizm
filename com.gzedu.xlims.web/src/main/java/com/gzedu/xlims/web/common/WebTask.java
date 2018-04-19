/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtYearService;

/**
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年6月13日
 * @version 3.0
 *
 */
@Component
@EnableScheduling
public class WebTask {
	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtGradeService gjtGradeService;

	@Autowired
	GjtYearService gjtYearService;

	private static final Logger log = LoggerFactory.getLogger(WebTask.class);

	@Scheduled(cron = "0 0 0 1 1 ?")
	public void sendMessage() {
		log.info("------------定时任务------------");
	}

}
