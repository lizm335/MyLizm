/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.common.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzedu.xlims.service.textbook.GjtTextbookDistributeService;

/**
 * 功能说明：发放时间小于当前时间减7天的配送状态全部改成已签收
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年11月7日
 * @version 2.5
 *
 */
public class ConfirmOrderTask {
	@Autowired
	private GjtTextbookDistributeService gjtTextbookDistributeService;
	private static final Logger log = LoggerFactory.getLogger(ConfirmOrderTask.class);

	public void timingAffirmTask() {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		String date = sd.format(calendar.getTime());
		try {
			log.info("定时执行更改配送状态参数：{}", date);
			gjtTextbookDistributeService.updateStatus(date);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
