/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.organization;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSetOrgCopyright;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月26日
 * @version 2.5
 *
 */
public interface GjtOrgService extends BaseService<GjtOrg> {

	List<GjtOrg> queryAll();

	/**
	 * 机构类型，用户所属机构ID
	 * 
	 * @param orgType
	 * @param orgId
	 * @return
	 */
	List<Map<String, String>> queryOrgByOrgType(String orgType, String orgId);

	Page<GjtOrg> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	Page<GjtOrg> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst);

	long queryAllCount(Map<String, Object> searchParams);

	/**
	 * 新学习中心查询
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<Map<String, Object>> queryAllStudyCenter(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 新学习中心和招生点统计
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	long queryAllStudyCenterCount(Map<String, Object> searchParams);

	/**
	 * 新学习体验中心查询（旧招生点）
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<Map<String, Object>> queryAllEnrollStudent(Map<String, Object> searchParams, PageRequest pageRequst);

	Page<GjtOrg> queryAllByParentId(String userId, boolean isChild, Map<String, Object> searchParams,
			PageRequest pageRequst);

	GjtOrg queryById(String id);

	void insert(GjtOrg entity, Map<Integer, String> domainMap);

	void insertChildOrg(String userId, GjtOrg entity);

	/**
	 * 更新总部，分部，分校
	 * 
	 * @param entity
	 */
	void update(GjtOrg entity);

	/**
	 * 更新学习中心，招生点
	 * 
	 * @param entity
	 */
	void updateStudyCenter(GjtOrg entity);

	void delete(List<String> ids);

	Page<GjtUserAccount> queryUserManagers(Map<String, Object> searchParams, PageRequest pageRequst);

	Page<GjtUserAccount> queryUserManagers(String orgId, Map<String, Object> searchParams, PageRequest pageRequst);

	long queryOrgSetNum(GjtSetOrgCopyright entity, String xxId);

	GjtOrg queryByschoolIdAndScName(String schoolId, String scName);

	boolean insertSetOrgCopyright(GjtSetOrgCopyright entity, String xxId, String userId);

	boolean updateSetOrgCopyright(GjtSetOrgCopyright entity, String xxId, String userId);

	/**
	 * 启用停用分校管理
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	boolean updateStatus(String id, String status);

	/**
	 * 唯一性校验，校验院校编码
	 * 
	 * @param code
	 * @param oldCode
	 * @return
	 */
	boolean queryByCode(String code, String oldCode);

	/**
	 * 查询下级机构
	 * 
	 * @param parentId
	 * @return
	 */
	List<String> queryByParentId(String parentId);

	/**
	 * 查询下级机构符合orgType的
	 * 
	 * @param parentId
	 * @param orgType
	 * @return
	 */
	List<String> queryByParentId(String parentId, String orgType);

	/**
	 * 查询下级机构是否存在CODE
	 * 
	 * @param parentId
	 * @param code
	 * @return
	 */
	String queryChildsByParentIdAndCode(String parentId, String code);

	/**
	 * 根据学习中心或者招生点id获取院校Id
	 * 
	 * @param sonId
	 * @return
	 */
	GjtOrg queryParentBySonId(String sonId);

	/**
	 * 根据院校Id获取学习中心
	 * 
	 * @param sonId
	 * @return
	 */
	List<GjtOrg> queryStudyBySchoolId(String schoolId);

	/**
	 * 根据编码获取机构信息
	 * 
	 * @param code
	 * @return
	 */
	GjtOrg queryByCode(String code);

	/**
	 * 查询院校下的用户数
	 * 
	 * @param id
	 * @return
	 */
	List<Map<String, String>> queryUserAcountNum(String id);

	/**
	 * 查询所有上级机构列表
	 * 
	 * @param childrenId
	 * @return
	 */
	List<String> queryParents(String childrenId);

	/**
	 * 获取总校ID
	 * 
	 * @param orgId
	 * @return
	 */
	String getSystemAdministrativeOrganizationByOrgId(String orgId);

	/**
	 * 查询学学员所属的学习中心
	 * 
	 * @param xxzxId
	 * @param orgType
	 * @return
	 */
	GjtOrg queryStudyCenterInfo(String xxzxId, String orgType);

	/**
	 * 根据类型，Id,所属Id,查询统计
	 * 
	 * @param searchParams
	 * @return
	 */
	long queryOrgCount(Map<String, Object> searchParams);

	/**
	 * 新增总部，分部
	 * 
	 * @param entity
	 * @return
	 */
	GjtOrg saveSchool(GjtOrg entity);

	GjtOrg saveStudyCenter(GjtOrg entity);

	/**
	 * 查询所有分部，分部下面的分校的下拉
	 * 
	 * @param parentId
	 * @return
	 */
	List<Map<String, String>> queryOrgTree(String parentId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月5日 上午10:26:11
	 * @param string
	 * @return
	 */
	GjtOrg queryByName(String string);

}
