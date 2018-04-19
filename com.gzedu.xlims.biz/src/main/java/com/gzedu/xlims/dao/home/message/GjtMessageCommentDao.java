/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.home.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.message.GjtMessageComment;

/**
 * 
 * 功能说明：消息评论表
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年1月11日
 * @version 3.0
 *
 */
public interface GjtMessageCommentDao
		extends JpaRepository<GjtMessageComment, String>, JpaSpecificationExecutor<GjtMessageComment> {

	@Modifying
	@Transactional
	@Query(value = "update GjtMessageComment g set g.likecount=(g.likecount+1) where id=:id")
	int addLike(@Param("id") String id);

}
