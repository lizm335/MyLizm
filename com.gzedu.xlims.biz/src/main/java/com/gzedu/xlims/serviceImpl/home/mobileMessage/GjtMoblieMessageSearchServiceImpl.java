/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.home.mobileMessage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.home.mobileMessage.GjtMoblieMessageSearchDao;
import com.gzedu.xlims.pojo.mobileMessage.GjtMoblieMessageSearch;
import com.gzedu.xlims.service.home.mobileMessage.GjtMoblieMessageSearchService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月16日
 * @version 3.0
 *
 */
@Service
public class GjtMoblieMessageSearchServiceImpl implements GjtMoblieMessageSearchService {

	@Autowired
	GjtMoblieMessageSearchDao gjtMoblieMessageSearchDao;

	@Override
	public GjtMoblieMessageSearch save(GjtMoblieMessageSearch entity) {
		return gjtMoblieMessageSearchDao.save(entity);
	}

	@Override
	public void save(List<GjtMoblieMessageSearch> entities) {
		gjtMoblieMessageSearchDao.save(entities);
	}

}
