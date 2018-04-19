/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtSpecialtyModuleLimit;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月12日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtSpecialtyModuleLimitDao
		extends JpaRepository<GjtSpecialtyModuleLimit, String>, JpaSpecificationExecutor<GjtSpecialtyModuleLimit> {

	@Modifying
	@Transactional
	@Query("delete from  GjtSpecialtyModuleLimit g  where g.gjtSpecialty.specialtyId=?1 ")
	public int deleteBySpecialtyId(String specialtyId);
	
	public List<GjtSpecialtyModuleLimit> findBySpecialtyIdAndIsDeleted(String specialtyId, String isDeleted);

}
