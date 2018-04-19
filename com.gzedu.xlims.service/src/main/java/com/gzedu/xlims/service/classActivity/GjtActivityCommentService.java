/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.classActivity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtActivityComment;
import com.gzedu.xlims.pojo.dto.ActivityCommentDto;

/**
 * 
 * 功能说明：活动评价接口
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月28日
 * @version 2.5
 *
 */
public interface GjtActivityCommentService {

	/**
	 * 获取评论数
	 * 
	 * @param activityId
	 * @return
	 */
	long countComentsNum(String activityId);

	/**
	 * 评论详情
	 * 
	 * @param activityId
	 * @param pageRequest
	 * @return
	 */
	public Page<ActivityCommentDto> getActivityCommentInfo(String activityId, PageRequest pageRequest);

	GjtActivityComment queryById(String id);

	Boolean insert(GjtActivityComment entity);
}
