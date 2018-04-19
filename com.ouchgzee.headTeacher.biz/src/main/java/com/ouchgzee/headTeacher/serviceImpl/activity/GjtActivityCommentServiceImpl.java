/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.serviceImpl.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.ouchgzee.headTeacher.dao.activity.GjtActivityCommentDao;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.daoImpl.BzrGjtActivityCommentDaoImpl;
import com.ouchgzee.headTeacher.dto.ActivityCommentDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtActivityComment;
import com.ouchgzee.headTeacher.service.activity.BzrGjtActivityCommentService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月20日
 * @version 1.0
 *
 */
@Deprecated @Service("bzrGjtActivityCommentServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtActivityCommentServiceImpl
		extends BaseServiceImpl<BzrGjtActivityComment>
		implements BzrGjtActivityCommentService {

	@Autowired
	private GjtActivityCommentDao gjtActivityCommentDao;
	@Autowired
	BzrGjtActivityCommentDaoImpl gjtActivityCommentImpl;

	@Override
	protected BaseDao<BzrGjtActivityComment, String> getBaseDao() {
		return gjtActivityCommentDao;
	}

	/**
	 * 评论数
	 * 
	 * @param activityId
	 * @return
	 */
	@Override
	public long countComentsNum(String activityId) {
		Criteria<BzrGjtActivityComment> spec = new Criteria();
		spec.add(Restrictions.eq("activityId", activityId, true));
		return gjtActivityCommentDao.count(spec);
	}

	/**
	 * 评论详情
	 * 
	 * @param activityId
	 * @param gjtActivityCommentImpl
	 * @return
	 */
	public Page<ActivityCommentDto> getActivityCommentInfo(String activityId,
			PageRequest pageRequest) {
		return gjtActivityCommentImpl.getActivityCommentInfo(activityId,
				pageRequest);
	}
}
