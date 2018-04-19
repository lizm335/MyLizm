/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtSpecialtyBase;

/**
 * 功能说明：专业管理
 * 
 * @author liangyijian
 * @Date 2017年8月24日
 *
 */
public interface GjtSpecialtyBaseDao extends JpaRepository<GjtSpecialtyBase, String>, JpaSpecificationExecutor<GjtSpecialtyBase> {

	@Query(value = "SELECT * FROM GJT_SPECIALTY_BASE WHERE SPECIALTY_BASE_ID IN "
			+ "(SELECT SPECIALTY_BASE_ID FROM GJT_SPECIALTY WHERE XX_ID=?1 AND SPECIALTY_ID=?2 AND IS_DELETED='N')", nativeQuery = true)
	public GjtSpecialtyBase findByXxIdAndSpecialtyId(String xxId,String specialtyId );

	@Query(value = "SELECT * FROM GJT_SPECIALTY_BASE WHERE  XX_ID IN "
			+ " (SELECT ID FROM GJT_ORG WHERE IS_DELETED = 'N' AND ORG_TYPE=1 START WITH ID=?1 CONNECT BY PRIOR PERENT_ID=ID) "
			+ " AND SPECIALTY_CODE=?2 AND SPECIALTY_LAYER=?3 AND IS_DELETED='N' ", nativeQuery = true)
	public GjtSpecialtyBase findByCodeAndLayer(String xxId,String specialtyCode,int specialtyLayer);
	
	@Modifying
	@Transactional
	@Query("update GjtSpecialtyBase g set g.isDeleted=?2 where g.specialtyBaseId=?1 ")
	public int deleteById(String id, String str);
	@Modifying
	@Transactional
	@Query("update GjtSpecialtyBase g set g.status=?2 where g.specialtyBaseId=?1 ")
	public void updateStatus(String id, int status);

}
