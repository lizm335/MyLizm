package com.gzedu.xlims.service.transaction;

import java.util.List;

import com.gzedu.xlims.pojo.GjtExemptExamInfoAudit;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月29日
 * @version 2.5
 */
public interface GjtExemptExamInfoAuditService {

	List<GjtExemptExamInfoAudit> findByExemptExamIdOrderByAuditDtAsc(String exemptExamId);

}
