/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.home.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.home.message.GjtMessagePutObjectDao;
import com.gzedu.xlims.pojo.message.GjtMessagePutObject;
import com.gzedu.xlims.service.home.message.GjtMessagePutObjectService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年1月31日
 * @version 3.0
 *
 */
@Service
public class GjtMessagePutObjectServiceImpl implements GjtMessagePutObjectService {

	@Autowired
	GjtMessagePutObjectDao gjtMessagePutObjectDao;

	@Override
	public void save(List<GjtMessagePutObject> entities) {
		gjtMessagePutObjectDao.save(entities);
	}

	@Override
	public void save(GjtMessagePutObject entity) {
		gjtMessagePutObjectDao.save(entity);

	}

}
