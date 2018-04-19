package com.gzedu.xlims.serviceImpl.model;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.model.PriRoleOperateDao;
import com.gzedu.xlims.pojo.PriRoleOperate;
import com.gzedu.xlims.service.model.PriRoleOperateService;

@Service
public class PriRoleOperateServiceImpl implements PriRoleOperateService {

	@Autowired
	private PriRoleOperateDao priRoleOperateDao;
	
	@Override
	public List<PriRoleOperate> findAll() {
		return priRoleOperateDao.findAll();
	}

	@Override
	public List<PriRoleOperate> findByRoleIdAndModelId(String roleId, String modelId) {
		return priRoleOperateDao.findByRoleIdAndModelId(roleId, modelId);
	}

	@Override
	public PriRoleOperate insert(PriRoleOperate entity) {
		entity.setRoleOperateId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsdeleted("N");
		
		return priRoleOperateDao.save(entity);
	}

	@Override
	public List<PriRoleOperate> insert(List<PriRoleOperate> entities) {
		for (PriRoleOperate entity : entities) {
			entity.setRoleOperateId(UUIDUtils.random());
			entity.setCreatedDt(new Date());
			entity.setIsdeleted("N");
		}
		
		return priRoleOperateDao.save(entities);
	}

	@Override
	public void delete(PriRoleOperate entity) {
		priRoleOperateDao.delete(entity);;
	}

	@Override
	public void delete(List<PriRoleOperate> entities) {
		priRoleOperateDao.delete(entities);
	}
	
	@Override
	public void deleteByRoleId(String roleId) {
		priRoleOperateDao.deleteByRoleId(roleId);
	}

}
