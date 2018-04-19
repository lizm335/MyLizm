/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.home.GjtWorkOrderCommentDao;
import com.gzedu.xlims.pojo.GjtWorkOrderComment;
import com.gzedu.xlims.service.home.GjtWorkOrderCommentService;

/**
 * 
 * 功能说明：工单详情-讨论内容
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月21日
 * @version 2.5
 *
 */
@Service
public class GjtWorkOrderCommentServiceImpl implements GjtWorkOrderCommentService {

	@Autowired
	GjtWorkOrderCommentDao gjtWorkOrderCommentDao;

	@Override
	public GjtWorkOrderComment save(GjtWorkOrderComment item) {
		return gjtWorkOrderCommentDao.save(item);
	}

	@Override
	public GjtWorkOrderComment update(GjtWorkOrderComment item) {
		return gjtWorkOrderCommentDao.save(item);
	}

	@Override
	public boolean delete(String id) {
		int i = gjtWorkOrderCommentDao.updateIsDelete(id);
		return i == 1 ? true : false;
	}

}
