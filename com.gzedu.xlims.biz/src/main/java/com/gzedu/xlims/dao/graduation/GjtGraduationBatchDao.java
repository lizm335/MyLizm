package com.gzedu.xlims.dao.graduation;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.graduation.GjtGraduationBatch;

/**
 * 毕业批次
 * @author eenet09
 *
 */
public interface GjtGraduationBatchDao extends JpaRepository<GjtGraduationBatch, String>, JpaSpecificationExecutor<GjtGraduationBatch> {

	/**
	 * 查询列表
	 * @param spec
	 * @param pageRequst
	 * @return
	 */
	//@Query("SELECT g FROM GjtGraduationBatch g where g.isDeleted='N'")
	//public Page<GjtGraduationBatch> queryAll(Specification<GjtGraduationBatch> spec, PageRequest pageRequst);
	
	/**
	 * 查询时间段内毕业批次最大的编号 
	 * @param createdDtStart
	 * @param createdDtEnd
	 * @return
	 */
	@Query("select max(b.batchCode) from GjtGraduationBatch b where b.createdDt between ?1 and ?2 ")
	public String queryGraduationBatchMaxCode(Date createdDtStart, Date createdDtEnd);
	
	/**
	 * 逻辑删除毕业批次
	 * @param id
	 * @param deletedBy
	 * @param deletedDt
	 */
	@Modifying
	@Transactional
	@Query("update GjtGraduationBatch set isDeleted = 'Y', deletedDt = ?3, deletedBy = ?2 where batchId = ?1 ")
	public void delete(String id, String deletedBy, Date deletedDt);
	
	/**
	 * 根据gradeId查询
	 * @param gradeId
	 * @param orgId
	 * @return
	 */
	@Query("select b from GjtGraduationBatch b where b.isDeleted = 'N' and b.gradeId = ?1 and b.orgId = ?2 ")
	public GjtGraduationBatch findByGradeId(String gradeId, String orgId);
	
	@Query("select b from GjtGraduationBatch b where b.isDeleted = 'N' and b.orgId = ?1 and b.gjtGrade.startDate <= sysdate and b.gjtGrade.endDate >= sysdate")
	public List<GjtGraduationBatch> findCurrentGraduationBatchList(String orgId);

}
