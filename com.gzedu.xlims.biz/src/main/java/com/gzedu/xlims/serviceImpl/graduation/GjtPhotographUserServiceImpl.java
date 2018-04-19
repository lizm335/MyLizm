/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.graduation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.graduation.GjtPhotographUserDao;
import com.gzedu.xlims.pojo.graduation.GjtPhotographUser;
import com.gzedu.xlims.service.graduation.GjtPhotographUserService;

import java.util.List;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月28日
 * @version 3.0
 *
 */
@Service
public class GjtPhotographUserServiceImpl implements GjtPhotographUserService {

	@Autowired
	GjtPhotographUserDao gjtPhotographUserDao;

	@Override
	public GjtPhotographUser findByUserId(String userId) {
		return gjtPhotographUserDao.findByUserId(userId);
	}

	@Override
	public GjtPhotographUser save(GjtPhotographUser entity) {
		return gjtPhotographUserDao.save(entity);
	}

	@Override
	public GjtPhotographUser findByUserIdAndAddressId(String userId, String photographAddressId) {
		return gjtPhotographUserDao.findByUserIdAndAddressId(userId, photographAddressId);
	}

	@Override
	public void delete(GjtPhotographUser originalItem) {
		gjtPhotographUserDao.delete(originalItem);
	}
}
