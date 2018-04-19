/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.classActivity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.classActivity.GjtActivityCommentDao;
import com.gzedu.xlims.daoImpl.classActivity.GjtActivityCommentDaoImpl;
import com.gzedu.xlims.pojo.GjtActivityComment;
import com.gzedu.xlims.pojo.dto.ActivityCommentDto;
import com.gzedu.xlims.service.classActivity.GjtActivityCommentService;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月28日
 * @version 2.5
 *
 */
@Service
public class GjtActivityCommentServiceImpl implements GjtActivityCommentService {

	@Autowired
	private GjtActivityCommentDao gjtActivityCommentDao;

	@Autowired
	GjtActivityCommentDaoImpl gjtActivityCommentImpl;

	/**
	 * 评论数
	 * 
	 * @param activityId
	 * @return
	 */
	@Override
	public long countComentsNum(String activityId) {
		Criteria<GjtActivityComment> spec = new Criteria();
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
	public Page<ActivityCommentDto> getActivityCommentInfo(String activityId, PageRequest pageRequest) {
		return gjtActivityCommentImpl.getActivityCommentInfo(activityId, pageRequest);
	}

	@Override
	public GjtActivityComment queryById(String id) {
		return gjtActivityCommentDao.findOne(id);
	}

	@Override
	public Boolean insert(GjtActivityComment entity) {
		GjtActivityComment activityComment = gjtActivityCommentDao.save(entity);
		if (activityComment != null) {
			return true;
		}
		return false;
	}

}
