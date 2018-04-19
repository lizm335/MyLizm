/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.home.message;

import java.util.List;

import com.gzedu.xlims.pojo.message.GjtMessageFeedback;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年2月2日
 * @version 3.0
 *
 */
public interface GjtMessageFeedbackService {

	public void save(List<GjtMessageFeedback> entities);

	/**
	 * 查询反馈列表
	 * 
	 * @param messageId
	 * @return
	 */
	public List<GjtMessageFeedback> findByMessageId(String messageId);
}
