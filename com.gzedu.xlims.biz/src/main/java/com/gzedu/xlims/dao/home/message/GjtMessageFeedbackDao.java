/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.home.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.message.GjtMessageFeedback;

/**
 * 
 * 功能说明：反馈内容选项卡
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年1月11日
 * @version 3.0
 *
 */
public interface GjtMessageFeedbackDao
		extends JpaRepository<GjtMessageFeedback, String>, JpaSpecificationExecutor<GjtMessageFeedback> {

	public List<GjtMessageFeedback> findByMessageIdOrderByCodeAsc(String messageId);
}
