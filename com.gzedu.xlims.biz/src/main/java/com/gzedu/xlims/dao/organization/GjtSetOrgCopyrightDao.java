package com.gzedu.xlims.dao.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.common.PlatfromTypeEnum;
import com.gzedu.xlims.pojo.GjtSetOrgCopyright;

/**
 * 功能说明：院校管理-->版权设置
 * 
 * @author 卢林林 lulinlin@eenet.com
 * @Date 2017年1月17日
 * @version 2.5
 */
public interface GjtSetOrgCopyrightDao
		extends JpaRepository<GjtSetOrgCopyright, String>, JpaSpecificationExecutor<GjtSetOrgCopyright> {
	/**
	 * 根据院校ID和平台类型查询数据
	 * 
	 * @param xxId
	 * @param platfromType
	 * @return
	 */
	GjtSetOrgCopyright findByXxIdAndPlatfromType(String xxId, String platfromType);

	GjtSetOrgCopyright findBySchoolRealmNameAndPlatfromType(String schoolRealmName, String platfromType);

	@Query(value = "SELECT GSOC.*  FROM GJT_SET_ORG_COPYRIGHT GSOC WHERE GSOC.IS_DELETED = 'N' AND GSOC.XX_ID = ?1 "
			+ " AND GSOC.PLATFROM_TYPE = ?2 ",nativeQuery = true)
	GjtSetOrgCopyright findByOrgIdAndPlatfromType(String orgId, String platfromType);
}
