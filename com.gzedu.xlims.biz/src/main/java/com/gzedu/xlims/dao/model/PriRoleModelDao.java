package com.gzedu.xlims.dao.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.PriRoleModel;

/**
 * 
 * 功能说明：角色菜单关联关系表
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月6日
 * @version 2.5
 *
 */
public interface PriRoleModelDao extends JpaRepository<PriRoleModel, String>, JpaSpecificationExecutor<PriRoleModel> {

	List<PriRoleModel> findByRoleId(String roleId);
}