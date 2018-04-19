package com.gzedu.xlims.service.exam;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import org.springframework.data.domain.Sort;

public interface GjtExamBatchNewService {

	public GjtExamBatchNew insert(GjtExamBatchNew entity);
	
	public GjtExamBatchNew insertNew(GjtExamBatchNew entity);

	public Page<GjtExamBatchNew> queryAll(Map<String, Object> searchParams,
			PageRequest pageRequst);

	/**
	 * 根据条件查询对象信息
	 *
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	List<GjtExamBatchNew> queryBy(Map<String, Object> searchParams, Sort sort);

	public int delete(List<String> ids, String xxid);

	public Map<String, Object> deleteBatch(String id, String xxid);

	public GjtExamBatchNew queryBy(String id);

	public GjtExamBatchNew update(GjtExamBatchNew entity);

	public GjtExamBatchNew queryByexamBatchCodeAndXxId(String examBatchCode, String xxid);

	GjtExamBatchNew findByStudyYearIdAndXxIdAndIsDeleted(String studyYearId, String xxId);
	
	public GjtExamBatchNew findByGradeIdAndXxId(String gradeId, String xxId);

	GjtExamBatchNew queryByExamBatchCode(String examBatchCode);
	
	/**
	 * 查询批次列表
	 * @return
	 */
	public Page getExamBatchList(Map searchParams, PageRequest pageRequst);
	
	Map queryExamBatchDetail(Map searchParams);
	
	/**
	 * 查询批次列表统计项
	 * @return
	 */
	public int getExamBatchCount(Map searchParams);
}
