package com.gzedu.xlims.dao.textbook.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.textbook.GjtTextbookDistribute;

public interface GjtTextbookDistributeRepository
		extends JpaRepository<GjtTextbookDistribute, String>, JpaSpecificationExecutor<GjtTextbookDistribute> {

	public GjtTextbookDistribute findByOrderCodeAndStatusAndIsDeleted(String orderCode, int status, String isDeleted);

	public List<GjtTextbookDistribute> findByStudentIdAndIsDeletedAndStatusIn(String studentId, String isDeleted,
			Collection<Integer> statuses);

	@Query("select b from GjtTextbookDistribute b where b.studentId = ?1 and b.isDeleted = 'N' and b.status in (?2) order by createdDt desc ")
	public List<GjtTextbookDistribute> findByStudentIdAndStatus(String studentId, Collection<Integer> statuses);

	@Query("select count(gtd) from GjtTextbookDistribute gtd where gtd.studentId=?1 and gtd.isDeleted=?2 and gtd.status in (?3)")
	public Long countByStudentIdAndIsDeletedAndStatusIn(String studentId, String isDeleted, Collection<Integer> statuses);

	@Query("select b from GjtTextbookDistribute b where b.gjtStudentInfo.xxId = ?1 and b.isDeleted = ?2 and b.status in (?3) ")
	public List<GjtTextbookDistribute> findByIsDeletedAndStatusIn(String orgId, String isDeleted,
			Collection<Integer> statuses);

	public List<GjtTextbookDistribute> findByStudentIdAndIsDeletedAndStatus(String studentId, String isDeleted,
			int status);

	@Query("select b from GjtTextbookDistribute b where b.studentId = ?1 and b.isDeleted = ?2 order by createdDt desc")
	public List<GjtTextbookDistribute> findByStudentIdAndIsDeleted(String studentId, String isDeleted);
}
