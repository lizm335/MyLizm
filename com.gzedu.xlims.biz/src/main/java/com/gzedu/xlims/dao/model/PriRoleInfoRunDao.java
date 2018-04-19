package com.gzedu.xlims.dao.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.PriRoleInfoRun;

/**
 * 
 * 功能说明：角色管理下属角色
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月25日
 * @version 2.5
 *
 */
public interface PriRoleInfoRunDao
		extends JpaRepository<PriRoleInfoRun, String>, JpaSpecificationExecutor<PriRoleInfoRun> {

	@Modifying
	@Transactional
	@Query("delete from PriRoleInfoRun o where o.roleId = ?1")
	public void deleteByRoleId(String roleId);
}