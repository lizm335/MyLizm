/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.usermanage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtEmployeePosition;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月28日
 * @version 2.5
 *
 */
public interface GjtEmployeePositionDao
		extends JpaRepository<GjtEmployeePosition, String>, JpaSpecificationExecutor<GjtEmployeePosition> {

	@Modifying
	@Transactional
	@Query("delete from GjtEmployeePosition g  where g.id.employeeId=?1 ")
	public int deletePosition(String employeeId);

}
