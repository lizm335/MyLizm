/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.home.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.home.message.GjtMessageFeedbackDao;
import com.gzedu.xlims.pojo.message.GjtMessageFeedback;
import com.gzedu.xlims.service.home.message.GjtMessageFeedbackService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年2月2日
 * @version 3.0
 *
 */
@Service
public class GjtMessageFeedbackServiceImpl implements GjtMessageFeedbackService {

	@Autowired
	GjtMessageFeedbackDao gjtMessageFeedbackDao;

	@Override
	public void save(List<GjtMessageFeedback> entities) {
		gjtMessageFeedbackDao.save(entities);
	}

	@Override
	public List<GjtMessageFeedback> findByMessageId(String messageId) {
		return gjtMessageFeedbackDao.findByMessageIdOrderByCodeAsc(messageId);
	}

}
