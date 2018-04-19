/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.pojo.message.GjtMessageUser;
import com.gzedu.xlims.service.home.message.GjtMessageUserService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年7月14日
 * @version 2.5
 * @since JDK1.7
 *
 */
public class MessageAddThread implements Runnable {

	private final static Logger log = LoggerFactory.getLogger(MessageAddThread.class);

	private GjtMessageUserService gjtMessageUserService;
	private String messageId;
	private String userName;

	private final List<String> list;

	/**
	 * @param gjtMessageUserService
	 * @param messageId
	 * @param userName
	 * @param list
	 */
	public MessageAddThread(GjtMessageUserService gjtMessageUserService, String messageId, String userName,
			List<String> list) {
		super();
		this.gjtMessageUserService = gjtMessageUserService;
		this.messageId = messageId;
		this.userName = userName;
		this.list = list;
	}

	@Override
	public void run() {
		List<GjtMessageUser> listAdd = new ArrayList<GjtMessageUser>(); // save的list
		for (String userId : list) {
			GjtMessageUser gmu = new GjtMessageUser();
			gmu.setId(UUIDUtils.random());
			gmu.setCreatedBy(userName);
			gmu.setCreatedDt(DateUtils.getNowTime());
			gmu.setMessageId(messageId);
			gmu.setUserId(userId);
			gmu.setIsDeleted("N");
			gmu.setIsEnabled("0");
			gmu.setVersion(new BigDecimal(1.0));
			listAdd.add(gmu);
		}
		try {
			gjtMessageUserService.insert(listAdd);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
