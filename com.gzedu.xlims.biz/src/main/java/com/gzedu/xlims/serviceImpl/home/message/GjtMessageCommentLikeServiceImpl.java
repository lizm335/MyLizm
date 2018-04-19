/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.home.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.home.message.GjtMessageCommentLikeDao;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.message.GjtMessageCommentLike;
import com.gzedu.xlims.service.home.message.GjtMessageCommentLikeService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年2月8日
 * @version 3.0
 *
 */
@Service
public class GjtMessageCommentLikeServiceImpl implements GjtMessageCommentLikeService {

	@Autowired
	GjtMessageCommentLikeDao gjtMessageCommentLikeDao;

	@Override
	public GjtMessageCommentLike save(GjtMessageCommentLike entity) {
		return gjtMessageCommentLikeDao.save(entity);
	}

	@Override
	public GjtMessageCommentLike save(String id, GjtUserAccount user) {
		GjtMessageCommentLike entity = new GjtMessageCommentLike();
		entity.setId(UUIDUtils.random());
		entity.setIsLike("1");
		entity.setUserId(user.getId());
		entity.setCommentId(id);
		return this.save(entity);
	}

	@Override
	public GjtMessageCommentLike findByCommentIdAndUserId(String commentId, String userId) {
		return gjtMessageCommentLikeDao.findByCommentIdAndUserId(commentId, userId);
	}

}
