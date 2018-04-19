package com.gzedu.xlims.dao.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtSchoolRollTransAudit;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年9月9日
 * @version 2.5
 */
public interface GjtSchoolRollTranAuditDao extends JpaRepository<GjtSchoolRollTransAudit, String>, JpaSpecificationExecutor<GjtSchoolRollTransAudit> {

	List<GjtSchoolRollTransAudit> findByTransactionIdAndStudentIdAndIsDeletedOrderByAuditDtAsc(String transactionId,
			String studentId, String booleanNo);

	List<GjtSchoolRollTransAudit> findByTransactionIdAndStudentIdOrderByAuditDtAsc(String transactionId, String studentId);

}
