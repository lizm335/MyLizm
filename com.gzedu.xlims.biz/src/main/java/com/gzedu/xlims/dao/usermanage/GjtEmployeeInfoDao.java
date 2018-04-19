/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.usermanage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtEmployeeInfo;

/**
 * 
 * 功能说明：教职工管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月27日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtEmployeeInfoDao
		extends JpaRepository<GjtEmployeeInfo, String>, JpaSpecificationExecutor<GjtEmployeeInfo> {

	/**
	 * 是否删除 N Y
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtEmployeeInfo g set g.isDeleted=?2 where g.employeeId=?1 ")
	public int deleteById(String id, String str);

	/**
	 * 是否启用1启用，0停用
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtEmployeeInfo g set g.isEnabled=?2 where g.employeeId=?1 ")
	public int updateIsEnabled(String id, String str);

	// @Query("select g from GjtEmployeeInfo g where g.employeeType = ?1 and
	// g.gjtEmployeePositionList.id.type = ?2 ")
	@Query("select g1 from GjtEmployeeInfo g1, GjtEmployeePosition g2 where g1.isDeleted='N' and g1.employeeType = ?1 and g1.employeeId = g2.id.employeeId and g2.id.type = ?2 and g1.xxId = ?3 ")
	public List<GjtEmployeeInfo> findListByType(String type, int subType, String orgId);

	@Query("select g1 from GjtEmployeeInfo g1 where g1.isDeleted='N' and g1.employeeType = ?1 and g1.xxId = ?2 ")
	public List<GjtEmployeeInfo> findListByType(String type, String orgId);

	@Query("select g1 from GjtEmployeeInfo g1 where g1.gjtUserAccount.id = ?1 ")
	public GjtEmployeeInfo findOneByAccountId(String accountId);

	@Query(value = "select * from (" + "  select distinct(employee_id),xm,eeno from gjt_employee_info t"
			+ "  inner join gjt_org o on o.id=t.org_id and o.is_deleted = 'N'"
			+ "  where t.is_deleted = 'N' and t.Employee_Type='1'"
			+ "  start with o.id=?1 connect by o.perent_id = prior o.id" + "  order by t.xm"
			+ ") temp where rownum=1", nativeQuery = true)
	Object[] findFirstTeacher(String xxId);
	
	@Query("select g1 from GjtEmployeeInfo g1 where g1.isDeleted='N' and g1.employeeType = ?3 and g1.xxId = ?2 and xxzxId=?1 ")
	public List<GjtEmployeeInfo> queryTeachClassBzr(String xxzxId, String xxId,String employeeType);
	
	@Query("select g1 from GjtEmployeeInfo g1 where g1.eeno = ?1")
	public GjtEmployeeInfo findByEeno(String eeno);

}
