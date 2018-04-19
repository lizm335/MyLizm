/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.home.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.home.message.GjtMessageSearchDao;
import com.gzedu.xlims.pojo.message.GjtMessageSearch;
import com.gzedu.xlims.service.home.message.GjtMessageSearchService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年1月30日
 * @version 3.0
 *
 */
@Service
public class GjtMessageSearchServiceImpl implements GjtMessageSearchService {

	@Autowired
	GjtMessageSearchDao gjtMessageSearchDao;

	@Override
	public void save(List<GjtMessageSearch> entity) {
		gjtMessageSearchDao.save(entity);
	}

}
