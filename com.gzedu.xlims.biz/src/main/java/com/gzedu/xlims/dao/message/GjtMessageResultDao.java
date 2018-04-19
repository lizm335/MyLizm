/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.message;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gzedu.xlims.pojo.GjtMessageResult;

/**
 * 
 * 功能说明：短信记录表
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年11月20日
 * @version 2.5
 *
 */
public interface GjtMessageResultDao
		extends JpaRepository<GjtMessageResult, String>, JpaSpecificationExecutor<GjtMessageResult> {
	@Query(value = "select g from GjtMessageResult g where g.incidentId =:incidentId "
			+ " and g.createdDt>=:startDate and g.createdDt <:endDate")
	public List<GjtMessageResult> findByIncidentId(@Param("incidentId") String incidentId,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
