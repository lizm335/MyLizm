package com.gzedu.xlims.dao.textbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.textbook.GjtTextbookOrderDetail;

public interface GjtTextbookOrderDetailRepository
		extends JpaRepository<GjtTextbookOrderDetail, String>, JpaSpecificationExecutor<GjtTextbookOrderDetail> {

	@Modifying
	@Transactional
	@Query("delete from GjtTextbookOrderDetail d where d.gjtTextbookArrange.arrangeId = ?1")
	public void deleteByArrangeId(String arrangeId);

	public List<GjtTextbookOrderDetail> findByStudentIdAndTextbookIdAndStatus(String studentId, String textbookId,
			int status);

	public GjtTextbookOrderDetail findByStudentIdAndTextbookIdAndCourseIdAndPlanId(String studentId, String textbookId,
			String courseId, String planId);

	public List<GjtTextbookOrderDetail> findByStudentIdAndCourseIdAndGradeIdAndTextbookId(String studentId,
			String courseId, String gradeId, String textbookId);

}
