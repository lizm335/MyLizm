/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.home.message;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.home.message.GjtMessageClassifyDao;
import com.gzedu.xlims.pojo.message.GjtMessageClassify;
import com.gzedu.xlims.service.home.message.GjtMessageClassifyService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年1月19日
 * @version 3.0
 *
 */
@Service
public class GjtMessageClassifyServiceImpl implements GjtMessageClassifyService {
	private static final Logger log = LoggerFactory.getLogger(GjtMessageClassifyServiceImpl.class);

	@Autowired
	private GjtMessageClassifyDao gjtMessageClassifyDao;

	@Override
	public List<GjtMessageClassify> findList(String infoType) {
		return gjtMessageClassifyDao.findByInfoType(infoType);
	}

	@Override
	public GjtMessageClassify save(GjtMessageClassify item) {
		item.setId(UUIDUtils.random().toString());
		item.setCreatedDt(DateUtils.getNowTime());
		return gjtMessageClassifyDao.save(item);
	}

	@Override
	public GjtMessageClassify update(GjtMessageClassify item) {
		return gjtMessageClassifyDao.save(item);
	}

	@Override
	public void delete(String id) {
		gjtMessageClassifyDao.delete(id);
	}

	@Override
	public GjtMessageClassify queryById(String id) {
		return gjtMessageClassifyDao.findOne(id);
	}

}
