package com.ouchgzee.headTeacher.service.model;

import com.ouchgzee.headTeacher.pojo.BzrPriRoleOperate;

import java.util.List;

@Deprecated public interface BzrPriRoleOperateService {
	
	List<BzrPriRoleOperate> findAll();
	
	List<BzrPriRoleOperate> findByRoleIdAndModelId(String roleId, String modelId);
	
	BzrPriRoleOperate insert(BzrPriRoleOperate entity);
	
	List<BzrPriRoleOperate> insert(List<BzrPriRoleOperate> entities);

	void delete(BzrPriRoleOperate entity);

	void delete(List<BzrPriRoleOperate> entities);
	
	void deleteByRoleId(String roleId);

}
