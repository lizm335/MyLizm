/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtOrg;

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
public interface GjtOrgDao extends BaseDao<GjtOrg, String> {

	@Query(nativeQuery = true, value = "select * from gjt_org where id = ?1 or perent_id = ?1")
	List<GjtOrg> findByIdOrParentGjtOrg(String orgId);

	/**
	 * 假删除
	 * 
	 * @param id
	 * @param str
	 */
	@Modifying
	@Transactional
	@Query("update GjtOrg g set g.isDeleted=?2,g.updatedDt=sysdate where g.id=?1")
	void deleteGjtOrgById(String id, String str);

	@Query(nativeQuery = true, value = "select * from gjt_org where perent_id = ?1 and org_name=?2")
	List<GjtOrg> findByParentGjtOrgAndOrgName(String orgId, String orgName);

	@Query(nativeQuery = true, value = "select * from gjt_org gg WHERE gg.CODE=?1 and gg.code<>?2")
	List<GjtOrg> queryByCode(String code, String oldCode);

	/**
	 * 根据院校ID获取总校ID
	 * 
	 * @param xxId
	 */
	@Query(value = "select t.id from gjt_org t where t.is_deleted='N' and t.org_type='1' start with t.id=:xxId connect by prior t.perent_id = t.id", nativeQuery = true)
	String findSystemAdministrativeOrganizationByXxId(@Param("xxId") String xxId);

	/**
	 * 查询下级机构
	 * 
	 * @param parentId
	 * @return
	 */
	@Query(nativeQuery = true, value = "SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=?1 CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID")
	List<String> queryChildsByParentId(String parentId);

	/**
	 * 查询下级机构符合orgType的
	 * 
	 * @param parentId
	 * @param orgType
	 * @return
	 */
	@Query(nativeQuery = true, value = "SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' and org.org_type=?2 START WITH org.ID=?1 CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID")
	List<String> queryChildsByParentId(String parentId, String orgType);

	/**
	 * 查询下级机构是否存在CODE
	 * 
	 * @param parentId
	 * @param code
	 * @return
	 */
	@Query(nativeQuery = true, value = "SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' and org.code=?2 START WITH org.ID=?1 CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID")
	String queryChildsByParentIdAndCode(String parentId, String code);

	/**
	 * 查询所有上级机构列表
	 * 
	 * @param childrenId
	 * @return
	 */
	@Query(nativeQuery = true, value = "SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=?1 CONNECT BY PRIOR ORG.PERENT_ID=ORG.ID")
	List<String> queryParents(String childrenId);

	/**
	 * 根据CODE获取机构信息
	 * 
	 * @param code
	 * @return
	 */
	@Query("select g from GjtOrg g where isDeleted='N' and code=?1 ")
	GjtOrg queryByCode(String code);

	/**
	 * 根据招生点或者学习中心Id获取院校Id
	 * 
	 * @param id
	 * @return
	 */
	@Query(nativeQuery = true, value = "select * from gjt_org org where org.is_deleted='N' and org_type='1' start with org.id=:id connect by prior org.perent_id=org.id  ")
	GjtOrg queryParentBySonId(@Param("id") String id);

	@Query(nativeQuery = true, value = "select * from gjt_org org where org.is_deleted='N' and org_type='3' and org.perent_id=:schoolId")
	List<GjtOrg> queryStudyBySchoolId(@Param("schoolId") String schoolId);

	@Modifying
	@Transactional
	@Query("update GjtOrg g set g.isEnabled=?2,g.updatedDt=sysdate where g.id=?1")
	long updateIsEnabled(String id, String str);

	/**
	 * 
	 * @param xxzxId
	 * @param string
	 * @return
	 */
	@Query(nativeQuery = true, value = "select * from gjt_org a where a.is_deleted = 'N' and a.org_type =:orgType  START WITH a.id =:xxzxId CONNECT BY PRIOR a.perent_id =a.id ")
	GjtOrg queryStudyCenterInfo(@Param("xxzxId") String xxzxId, @Param("orgType") String orgType);

	GjtOrg findByOrgNameAndIsDeleted(String orgName, String isDeleted);

}
