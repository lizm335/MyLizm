package com.gzedu.xlims.dao.textbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.textbook.GjtTextbookFeedback;

public interface GjtTextbookFeedbackRepository extends JpaRepository<GjtTextbookFeedback, String>, JpaSpecificationExecutor<GjtTextbookFeedback> {
	
	@Query("select f from GjtTextbookFeedback f where f.status = ?1 and f.gjtStudentInfo.xxId = ?2 and f.isDeleted = 'N'")
	List<GjtTextbookFeedback> findByStatusAndOrgId(int status, String orgId);

}
