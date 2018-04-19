/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

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
public interface GjtSchoolInfoDao
		extends JpaRepository<GjtSchoolInfo, String>, JpaSpecificationExecutor<GjtSchoolInfo> {

	GjtSchoolInfo findByGjtOrgCode(String code);

	List<GjtSchoolInfo> findByXxmc(String name);

	/**
	 * 根据当前登录用户查询所属院校信息
	 * @param id
	 * @return
	 */
	@Query(value="select b from GjtSchoolInfo b where b.isDeleted = 'N' and b.id = (select case a.orgType when '3' then a.parentGjtOrg.id  else a.id end  from GjtOrg a where a.id=?1)")
	GjtSchoolInfo queryAppidByOrgId(String id);
}
