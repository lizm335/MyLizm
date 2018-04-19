package com.gzedu.xlims.service.model;

import java.util.List;

import com.gzedu.xlims.pojo.PriRoleOperate;

public interface PriRoleOperateService {
	
	List<PriRoleOperate> findAll();
	
	List<PriRoleOperate> findByRoleIdAndModelId(String roleId, String modelId);
	
	PriRoleOperate insert(PriRoleOperate entity);
	
	List<PriRoleOperate> insert(List<PriRoleOperate> entities);

	void delete(PriRoleOperate entity);

	void delete(List<PriRoleOperate> entities);
	
	void deleteByRoleId(String roleId);

}
