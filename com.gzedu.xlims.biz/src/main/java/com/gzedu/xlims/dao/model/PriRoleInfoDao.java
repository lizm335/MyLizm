package com.gzedu.xlims.dao.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.PriRoleInfo;

/**
 * 
 * 功能说明：角色
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月6日
 * @version 2.5
 *
 */
public interface PriRoleInfoDao extends JpaRepository<PriRoleInfo, String>, JpaSpecificationExecutor<PriRoleInfo> {

	@Query("select t.roleId,t.roleName from PriRoleInfo t")
	List<PriRoleInfo> findIdAndNameList();

	PriRoleInfo findByRoleNameAndIsdeleted(String roleName, String isdeleted);

	PriRoleInfo findByRoleCodeAndIsdeleted(String roleCode, String isdeleted);

	@Query("select t from PriRoleInfo t where t.isdeleted='N' order by roleName asc")
	List<PriRoleInfo> findAlls();

}