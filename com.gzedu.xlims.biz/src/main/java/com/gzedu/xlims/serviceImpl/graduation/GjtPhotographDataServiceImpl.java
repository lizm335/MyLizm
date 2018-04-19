/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.graduation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.graduation.GjtPhotographDataDao;
import com.gzedu.xlims.pojo.graduation.GjtPhotographData;
import com.gzedu.xlims.service.graduation.GjtPhotographDataService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月28日
 * @version 3.0
 *
 */
@Service
public class GjtPhotographDataServiceImpl implements GjtPhotographDataService {

	@Autowired
	GjtPhotographDataDao gjtPhotographDataDao;

	@Override
	public GjtPhotographData save(GjtPhotographData entity) {
		return gjtPhotographDataDao.save(entity);
	}

	@Override
	public GjtPhotographData update(GjtPhotographData entity) {
		return gjtPhotographDataDao.save(entity);
	}

	@Override
	public GjtPhotographData findByXxId(String xxId) {
		return gjtPhotographDataDao.findByXxId(xxId);
	}

}
