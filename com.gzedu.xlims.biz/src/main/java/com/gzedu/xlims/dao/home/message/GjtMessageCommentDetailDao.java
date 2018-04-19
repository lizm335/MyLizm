/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.home.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.message.GjtMessageCommentDetail;

/**
 * 
 * 功能说明：消息评论表下面的回复表
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年1月11日
 * @version 3.0
 *
 */

public interface GjtMessageCommentDetailDao
		extends JpaRepository<GjtMessageCommentDetail, String>, JpaSpecificationExecutor<GjtMessageCommentDetail> {

}
