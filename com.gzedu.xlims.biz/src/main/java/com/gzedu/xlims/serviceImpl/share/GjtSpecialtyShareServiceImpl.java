/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.share;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.share.GjtSpecialtyShareDao;
import com.gzedu.xlims.pojo.GjtSpecialtyOwnership;
import com.gzedu.xlims.service.share.GjtSpecialtyShareService;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年12月29日
 * @version 1.0
 *
 */
@Service
public class GjtSpecialtyShareServiceImpl implements GjtSpecialtyShareService {

	@Autowired
	GjtSpecialtyShareDao gjtSpecialtyShareDao;

	@Override
	public GjtSpecialtyOwnership queryById(String id) {
		GjtSpecialtyOwnership gjtArticle = gjtSpecialtyShareDao.findOne(id);
		return gjtArticle;
	}

	@Override
	public Boolean saveGjtShare(GjtSpecialtyOwnership gjtSpecialtyOwnership) {
		GjtSpecialtyOwnership save = gjtSpecialtyShareDao.save(gjtSpecialtyOwnership);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean updateGjtShare(GjtSpecialtyOwnership gjtSpecialtyOwnership) {
		GjtSpecialtyOwnership save = gjtSpecialtyShareDao.save(gjtSpecialtyOwnership);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void delete(String id) {
		gjtSpecialtyShareDao.delete(id);
	}

	@Override
	public List<GjtSpecialtyOwnership> findByOrgCode(String orgCode) {
		return gjtSpecialtyShareDao.findByOrgCode(orgCode);
	}

}
