/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.organization.GjtSpecialtyVideoDao;
import com.gzedu.xlims.pojo.GjtSpecialtyVideo;
import com.gzedu.xlims.service.organization.GjtSpecialtyVideoService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

@Service
public class GjtSpecialtyVideoServiceImpl extends BaseServiceImpl<GjtSpecialtyVideo> implements GjtSpecialtyVideoService {

	@Autowired
	private GjtSpecialtyVideoDao gjtSpecialtyVideoDao;

	@Override
	protected BaseDao<GjtSpecialtyVideo, String> getBaseDao() {
		return gjtSpecialtyVideoDao;
	}

	@Override
	public GjtSpecialtyVideo queryByTypeName(String typeName) {
		return gjtSpecialtyVideoDao.queryByTypeName(typeName);
	}

}
