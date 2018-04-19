package com.gzedu.xlims.service.graduation;

import java.util.Date;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.graduation.GjtGraduationBatch;

/**
 * 毕业批次
 * @author eenet09
 *
 */
public interface GjtGraduationBatchService {
	
	/**
	 * 查询毕业批次列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<GjtGraduationBatch> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 查询时间段内毕业批次最大的编号
	 * @param createdDtStart
	 * @param createdDtEnd
	 * @return
	 */
	public String queryGraduationBatchMaxCode(Date createdDtStart, Date createdDtEnd);
	
	public void insert(GjtGraduationBatch entity);
	
	public GjtGraduationBatch queryById(String id);
	
	public void update(GjtGraduationBatch entity);
	
	/**
	 * 逻辑删除毕业批次
	 * @param id
	 * @param deletedBy
	 */
	public void delete(String id, String deletedBy);
	
	/**
	 * 根据gradeId查询
	 * @param gradeId
	 * @param orgId
	 * @return
	 */
	public GjtGraduationBatch findByGradeId(String gradeId, String orgId);
	
	/**
	 * 查询批次集合
	 * @param orgId
	 * @return
	 */
	public Map<String, String> getGraduationBatchMap(String orgId);

}
