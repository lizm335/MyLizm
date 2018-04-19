package com.gzedu.xlims.dao.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtFeedback;

public interface GjtFeedbackDao extends JpaRepository<GjtFeedback, String>, JpaSpecificationExecutor<GjtFeedback> {

	@Modifying
	@Transactional
	@Query("update GjtFeedback g set g.dealResult='N' where g.id=?1")
	void updateDealResultById(String id);

}
