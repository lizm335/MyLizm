/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.usermanage;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.PriRoleInfo;

/**
 * 
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月18日
 * @version 2.5
 *
 */
public interface PriRoleInfoService {

	// 获得院校管理员角色列表
	List<PriRoleInfo> queryOrgMagagerRoles();

	List<PriRoleInfo> queryAll();

	List<PriRoleInfo> queryAll(Iterable<String> ids);

	Page<PriRoleInfo> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	PriRoleInfo queryById(String id);

	void insert(PriRoleInfo entity);

	void update(PriRoleInfo entity);

	void delete(String id);

	void delete(Iterable<String> ids);

	PriRoleInfo queryByName(String roleName);

	/**
	 * 根据角色编号查询角色对象
	 * 
	 * @param roleCode
	 * @return
	 */
	PriRoleInfo queryByCode(String roleCode);

	/**
	 * 查询除了管理员外的所有角色
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月4日 下午3:34:35
	 * @return
	 */
	Map<String, String> queryRoleExcludeAdmin();

	// 查询用户，角色。课程，期课程ID
	Page<Map<String, Object>> queryUserRoleClassCourse(Map<String, Object> searchMap, PageRequest pageRequst);

	/**
	 * 返回该角色管理的下属角色
	 * 
	 * @param grantType
	 * @param roleId
	 * @return
	 */
	Map<String, String> queryRoles(String grantType, String roleId);
}
