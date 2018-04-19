/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.organization;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.organization.GjtSpecialtyBaseDao;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtSpecialtyBase;
import com.gzedu.xlims.service.organization.GjtSpecialtyBaseService;

/**
 * 
 * 功能说明：专业管理 实现接口
 * 
 * @author liangyijian
 * @Date 2017年8月24日
 *
 */

@Service
public class GjtSpecialtyBaseServiceImpl implements GjtSpecialtyBaseService {

	@Autowired
	public GjtSpecialtyBaseDao gjtSpecialtyBaseDao;
	
	@Override
	public void save(GjtSpecialtyBase entity) {
		gjtSpecialtyBaseDao.save(entity);
	}
	
	@Override
	public GjtSpecialtyBase queryById(String id) {
		return gjtSpecialtyBaseDao.findOne(id);
	}
	
	@Override
	public Boolean deleteById(String... ids) {
		if (ids != null) {
			for (String id : ids) {
				gjtSpecialtyBaseDao.deleteById(id, "Y");
			}
		}
		return true;
	}

	@Override
	public Page<GjtSpecialtyBase> queryAll(String xxId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Criteria<GjtSpecialtyBase> spec = new Criteria<GjtSpecialtyBase>();
		spec.add(Restrictions.eq("isDeleted", "N", true));
		spec.add(Restrictions.eq("xxId", xxId, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtSpecialtyBaseDao.findAll(spec, pageRequst);
	}
	
	@Override
	public GjtSpecialtyBase findByXxIdAndSpecialtyId(String xxId, String specialtyId) {
		return gjtSpecialtyBaseDao.findByXxIdAndSpecialtyId(xxId, specialtyId);
	}

	@Override
	public GjtSpecialtyBase findByCodeAndLayer(String xxId,String specialtyCode,int specialtyLayer) {
		return gjtSpecialtyBaseDao.findByCodeAndLayer(xxId,specialtyCode,specialtyLayer);
	}

	@Override
	public void updateStatus(String id, int status) {
		gjtSpecialtyBaseDao.updateStatus(id,status);
		
	}

}
