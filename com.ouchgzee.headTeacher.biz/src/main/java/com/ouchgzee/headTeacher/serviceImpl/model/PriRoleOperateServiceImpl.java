package com.ouchgzee.headTeacher.serviceImpl.model;

import com.gzedu.xlims.common.UUIDUtils;
import com.ouchgzee.headTeacher.dao.model.PriRoleOperateDao;
import com.ouchgzee.headTeacher.pojo.BzrPriRoleOperate;
import com.ouchgzee.headTeacher.service.model.BzrPriRoleOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Deprecated @Service("bzrPriRoleOperateServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class PriRoleOperateServiceImpl implements BzrPriRoleOperateService {

	@Autowired
	private PriRoleOperateDao priRoleOperateDao;
	
	@Override
	public List<BzrPriRoleOperate> findAll() {
		return priRoleOperateDao.findAll();
	}

	@Override
	public List<BzrPriRoleOperate> findByRoleIdAndModelId(String roleId, String modelId) {
		return priRoleOperateDao.findByRoleIdAndModelId(roleId, modelId);
	}

	@Override
	public BzrPriRoleOperate insert(BzrPriRoleOperate entity) {
		entity.setRoleOperateId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsdeleted("N");
		
		return priRoleOperateDao.save(entity);
	}

	@Override
	public List<BzrPriRoleOperate> insert(List<BzrPriRoleOperate> entities) {
		for (BzrPriRoleOperate entity : entities) {
			entity.setRoleOperateId(UUIDUtils.random());
			entity.setCreatedDt(new Date());
			entity.setIsdeleted("N");
		}
		
		return priRoleOperateDao.save(entities);
	}

	@Override
	public void delete(BzrPriRoleOperate entity) {
		priRoleOperateDao.delete(entity);;
	}

	@Override
	public void delete(List<BzrPriRoleOperate> entities) {
		priRoleOperateDao.delete(entities);
	}
	
	@Override
	public void deleteByRoleId(String roleId) {
		priRoleOperateDao.deleteByRoleId(roleId);
	}

}
