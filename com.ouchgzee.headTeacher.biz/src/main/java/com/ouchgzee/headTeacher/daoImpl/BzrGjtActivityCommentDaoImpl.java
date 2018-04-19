/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.daoImpl;

import com.ouchgzee.headTeacher.daoImpl.base.BaseDaoImpl;
import com.ouchgzee.headTeacher.dto.ActivityCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 班级活动评论操作类<br>
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月20日
 * @version 1.0
 *
 */
@Deprecated @Repository("bzrGjtActivityCommentDaoImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class BzrGjtActivityCommentDaoImpl extends BaseDaoImpl {
	/**
	 * 评论详情
	 * 
	 * @param activityId
	 * @param pageRequest
	 * @return
	 */
	public Page<ActivityCommentDto> getActivityCommentInfo(String activityId,
			PageRequest pageRequest) {
		StringBuilder querySql = new StringBuilder(
				"select t.comments,t.comment_dt,v.xm,v.avatar from gjt_activity_comment t left join gjt_student_info v on t.student_id = v.student_id");
		querySql.append(" WHERE t.activity_id=:activityId");
		Map<String, Object> params = new HashMap();
		params.put("activityId", activityId);
		// DAO类一般忽略设置默认排序，可放在SERVICE层
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(),
					pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "t.comment_dt"));
		}
		return super.findByPageSql(querySql, params, pageRequest,
				ActivityCommentDto.class);
	}

}
