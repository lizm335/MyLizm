package com.gzedu.xlims.dao.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.PriRoleInfoWork;

/**
 * 
 * 功能说明：工单可以发送的角色
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年10月25日
 * @version 2.5
 *
 */
public interface PriRoleInfoWorkDao
		extends JpaRepository<PriRoleInfoWork, String>, JpaSpecificationExecutor<PriRoleInfoWork> {

	@Modifying
	@Transactional
	@Query("delete from PriRoleInfoWork o where o.roleId = ?1")
	public void deleteByRoleId(String roleId);
}