/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.common;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.pojo.mobileMessage.GjtMoblieMessageUser;
import com.gzedu.xlims.service.home.mobileMessage.GjtMoblieMessageUserService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年7月14日
 * @version 2.5
 * @since JDK1.7
 *
 */
public class MobileMessageAddThread implements Runnable {

	private final static Logger log = LoggerFactory.getLogger(MobileMessageAddThread.class);

	private GjtMoblieMessageUserService gjtMoblieMesaageUserService;
	private String messageId;
	private String userName;

	private final List<String> list;

	/**
	 * @param gjtMessageUserService
	 * @param messageId
	 * @param userName
	 * @param list
	 */
	public MobileMessageAddThread(GjtMoblieMessageUserService gjtMoblieMesaageUserService, String messageId,
			String userName, List<String> list) {
		super();
		this.gjtMoblieMesaageUserService = gjtMoblieMesaageUserService;
		this.messageId = messageId;
		this.userName = userName;
		this.list = list;
	}

	@Override
	public void run() {
		List<GjtMoblieMessageUser> listAdd = new ArrayList<GjtMoblieMessageUser>(); // save的list
		for (String userId : list) {
			GjtMoblieMessageUser gmu = new GjtMoblieMessageUser();
			gmu.setId(UUIDUtils.random());
			gmu.setCreatedDt(DateUtils.getNowTime());
			gmu.setMobileMessageId(messageId);
			gmu.setUserId(userId);
			gmu.setIsDeleted("N");
			gmu.setSendStauts("0");
			listAdd.add(gmu);
		}
		try {
			gjtMoblieMesaageUserService.insert(listAdd);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
