/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.classActivity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtActivityComment;

/**
 * 
 * 功能说明：活动评论表操作类
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月28日
 * @version 2.5
 *
 */
public interface GjtActivityCommentDao
		extends JpaRepository<GjtActivityComment, String>, JpaSpecificationExecutor<GjtActivityComment> {

}
