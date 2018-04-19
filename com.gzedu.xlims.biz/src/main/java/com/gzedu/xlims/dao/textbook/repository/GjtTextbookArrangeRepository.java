package com.gzedu.xlims.dao.textbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.textbook.GjtTextbookArrange;

public interface GjtTextbookArrangeRepository extends JpaRepository<GjtTextbookArrange, String>, JpaSpecificationExecutor<GjtTextbookArrange> {
	
	public GjtTextbookArrange findByPlanIdAndTextbookId(String planId, String textbookId);

	@Modifying
	@Query(value = "INSERT INTO GJT_GRADE_TEXTBOOK(GRADE_ID,TEXTBOOK_ID) VALUES (?1,?2)", nativeQuery = true)
	public void insertGradeTextbook(String gradeId, String textbookId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月22日 下午3:13:06
	 * @param gradeId
	 * @param textbookId
	 */
	@Modifying
	@Transactional
	@Query(value = "DELETE GJT_GRADE_TEXTBOOK WHERE GRADE_ID=?1 AND TEXTBOOK_ID=?2", nativeQuery = true)
	public void removeTextbook(String gradeId, String textbookId);

}
