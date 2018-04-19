/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.home;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtWorkOrderAssignPerson;

/**
 * 
 * 功能说明：工单管理-指定任务人
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年9月5日
 * @version 3.0
 *
 */
public interface GjtWorkOrderAssignPersonDao
		extends JpaRepository<GjtWorkOrderAssignPerson, String>, JpaSpecificationExecutor<GjtWorkOrderAssignPerson> {

	/**
	 * 删除
	 * 
	 * @param messageId
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("UPDATE GjtWorkOrderAssignPerson  t SET t.isDeleted='Y' WHERE t.id=:id ")
	int updateIsDelete(@Param("id") String id);

}
