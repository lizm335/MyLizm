/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.usermanage;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.BzrPriRoleInfo;

/**
 * 
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月18日
 * @version 2.5
 *
 */
@Deprecated public interface BzrPriRoleInfoService {

	// 获得院校管理员角色列表
	List<BzrPriRoleInfo> queryOrgMagagerRoles();

	List<BzrPriRoleInfo> queryAll();

	List<BzrPriRoleInfo> queryAll(Iterable<String> ids);

	Page<BzrPriRoleInfo> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	BzrPriRoleInfo getOne(String id);

	void insert(BzrPriRoleInfo entity);

	void update(BzrPriRoleInfo entity);

	void delete(String id);

	void delete(Iterable<String> ids);

	// 查询用户，角色。课程，期课程ID
	Page<Map<String, Object>> queryUserRoleClassCourse(Map<String, Object> searchMap, PageRequest pageRequst);
}
