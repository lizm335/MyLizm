/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.service.activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.dto.ActivityCommentDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtActivityComment;
import com.ouchgzee.headTeacher.service.base.BaseService;

/**
 * 活动评价接口<br>
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月20日
 * @version 1.0
 *
 */
@Deprecated public interface BzrGjtActivityCommentService
		extends BaseService<BzrGjtActivityComment> {

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
	public Page<ActivityCommentDto> getActivityCommentInfo(String activityId,
			PageRequest pageRequest);

}
