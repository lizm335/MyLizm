/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.daoImpl.classActivity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import com.gzedu.xlims.pojo.dto.ActivityCommentDto;

/**
 * 
 * 功能说明：班级活动评论操作类
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月28日
 * @version 2.5
 *
 */
@Service
public class GjtActivityCommentDaoImpl extends BaseDaoImpl {
	/**
	 * 评论详情
	 * 
	 * @param activityId
	 * @param pageRequest
	 * @return
	 */
	public Page<ActivityCommentDto> getActivityCommentInfo(String activityId, PageRequest pageRequest) {
		StringBuilder querySql = new StringBuilder(
				"select t.comments,t.comment_dt,v.xm,v.avatar from gjt_activity_comment t left join gjt_student_info v on t.student_id = v.student_id");
		querySql.append(" WHERE t.activity_id=:activityId");
		Map<String, Object> params = new HashMap();
		params.put("activityId", activityId);
		// DAO类一般忽略设置默认排序，可放在SERVICE层
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "t.comment_dt"));
		}
		return super.findByPageSql(querySql, params, pageRequest, ActivityCommentDto.class);
	}

}
