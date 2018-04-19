package com.ouchgzee.headTeacher.service.graduation;

import java.util.Date;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationBatch;

/**
 * 毕业批次
 * @author eenet09
 *
 */
@Deprecated public interface BzrGjtGraduationBatchService {
	
	/**
	 * 查询毕业批次列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<BzrGjtGraduationBatch> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 查询时间段内毕业批次最大的编号
	 * @param createdDtStart
	 * @param createdDtEnd
	 * @return
	 */
	public String queryGraduationBatchMaxCode(Date createdDtStart, Date createdDtEnd);
	
	public void insert(BzrGjtGraduationBatch entity);
	
	public BzrGjtGraduationBatch queryById(String id);
	
	public void update(BzrGjtGraduationBatch entity);
	
	/**
	 * 逻辑删除毕业批次
	 * @param id
	 * @param deletedBy
	 */
	public void delete(String id, String deletedBy);
	
	/**
	 * 根据studyYearCode查询
	 * @param studyYearCode
	 * @param orgId
	 * @return
	 */
	public BzrGjtGraduationBatch findByStudyYearCode(int studyYearCode, String orgId);
	
	/**
	 * 查询批次集合
	 * @param orgId
	 * @return
	 */
	public Map<String, String> getGraduationBatchMap(String orgId);

}
