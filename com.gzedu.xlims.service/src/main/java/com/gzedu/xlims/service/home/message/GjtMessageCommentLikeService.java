/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.home.message;

import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.message.GjtMessageCommentLike;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年2月8日
 * @version 3.0
 *
 */
public interface GjtMessageCommentLikeService {
	GjtMessageCommentLike save(GjtMessageCommentLike entity);

	GjtMessageCommentLike save(String id, GjtUserAccount user);

	GjtMessageCommentLike findByCommentIdAndUserId(String commentId, String userId);

}
