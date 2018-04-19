package com.gzedu.xlims.dao.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtExemptExamInfoAudit;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月28日
 * @version 2.5
 */
public interface GjtExemptExamInfoAuditDao extends JpaRepository<GjtExemptExamInfoAudit, String>, JpaSpecificationExecutor<GjtExemptExamInfoAudit>{

	List<GjtExemptExamInfoAudit> findByExemptExamIdOrderByAuditDtAsc(String exemptExamId);

}
