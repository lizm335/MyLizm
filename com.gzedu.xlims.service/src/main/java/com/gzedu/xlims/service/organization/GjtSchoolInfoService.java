/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.organization;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtSchoolInfo;

/**
 * 
 * 功能说明：院校管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月28日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtSchoolInfoService {
	/**
	 * // 根据条件查询数据源
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<GjtSchoolInfo> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	List<GjtSchoolInfo> queryAll(Iterable<String> ids);

	/**
	 * // 添加学院信息
	 * 
	 * @param gjtSchoolInfo
	 */
	void saveEntity(GjtSchoolInfo gjtSchoolInfo);

	/**
	 * // 修改学院信息
	 * 
	 * @param employeeInfo
	 */
	void updateEntity(GjtSchoolInfo employeeInfo);

	/**
	 * // 查询学院信息
	 * 
	 * @param id
	 * @return
	 */
	GjtSchoolInfo queryById(String id);

	GjtSchoolInfo queryByName(String name);

	/**
	 * // 查询学院信息
	 * 
	 * @param code
	 * @return
	 */
	GjtSchoolInfo queryByCode(String code);

	/**
	 * 查询所有院校
	 */
	List<GjtSchoolInfo> queryAll();

	boolean delete(Iterable<String> ids);

	/**
	 * 根据当前登录用户查询所属院校信息
	 * @param id
	 * @return
	 */
	GjtSchoolInfo queryAppidByOrgId(String id);

}
