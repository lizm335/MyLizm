package com.ouchgzee.headTeacher.dao.graduation;

import java.util.Date;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationBatch;

/**
 * 毕业批次
 * @author eenet09
 *
 */
@Deprecated @Repository("bzrGjtGraduationBatchDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtGraduationBatchDao extends BaseDao<BzrGjtGraduationBatch, String> {

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
	@Query("select max(b.batchCode) from BzrGjtGraduationBatch b where b.createdDt between ?1 and ?2 ")
	public String queryGraduationBatchMaxCode(Date createdDtStart, Date createdDtEnd);
	
	/**
	 * 逻辑删除毕业批次
	 * @param id
	 * @param deletedBy
	 * @param deletedDt
	 */
	@Modifying
	@Transactional(value="transactionManagerBzr")
	@Query("update BzrGjtGraduationBatch set isDeleted = 'Y', deletedDt = ?3, deletedBy = ?2 where batchId = ?1 ")
	public void delete(String id, String deletedBy, Date deletedDt);
	
	/**
	 * 根据studyYearCode查询
	 * @param studyYearCode
	 * @param orgId
	 * @return
	 */
	@Query("select b from BzrGjtGraduationBatch b where b.isDeleted = 'N' and b.studyYearCode = ?1 and b.orgId = ?2 ")
	public BzrGjtGraduationBatch findByStudyYearCode(int studyYearCode, String orgId);

}
