/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.home.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.message.GjtMessageCommentLike;

/**
 * 功能说明：评论点赞表
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年2月8日
 * @version 3.0
 *
 */
public interface GjtMessageCommentLikeDao
		extends JpaRepository<GjtMessageCommentLike, String>, JpaSpecificationExecutor<GjtMessageCommentLike> {

	GjtMessageCommentLike findByCommentIdAndUserId(String commentId, String userId);
}
