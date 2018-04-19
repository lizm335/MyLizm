/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.model;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.PriRoleOperate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月8日
 * @version 2.5
 *
 */
public interface PriRoleOperateDao extends BaseDao<PriRoleOperate, String> {

	// List<PriRoleOperate> findByPriUserRoleRoleId();
	
	public List<PriRoleOperate> findByRoleIdAndModelId(String roleId, String modelId);
	
	@Modifying
	@Transactional
	@Query("delete from PriRoleOperate o where o.roleId = ?1")
	public void deleteByRoleId(String roleId);
}
