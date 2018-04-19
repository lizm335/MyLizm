/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtStudyCenterAudit;

/**
 * 
 * 功能说明：学习中心审核
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月15日
 * @version 3.0
 *
 */
public interface GjtStudyCenterAuditDao
		extends JpaRepository<GjtStudyCenterAudit, String>, JpaSpecificationExecutor<GjtStudyCenterAudit> {

	List<GjtStudyCenterAudit> findByIncidentIdAndIsDeletedOrderByCreatedDtAsc(String orgId, String isDeleted);

}
