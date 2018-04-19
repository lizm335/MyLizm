package com.ouchgzee.headTeacher.service.textbook;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookDistribute;

@Deprecated public interface BzrGjtTextbookDistributeService {
	
	public void update(BzrGjtTextbookDistribute entity);
	
	public void update(List<BzrGjtTextbookDistribute> entities);
	
	public BzrGjtTextbookDistribute findByOrderCodeAndStatusAndIsDeleted(String orderCode, int status, String isDeleted);
	
	public Page<Map<String, Object>> findDistributeSummary(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public List<BzrGjtTextbookDistribute> findByStudentIdAndIsDeletedAndStatusIn(String studentId, String isDeleted, Collection<Integer> statuses);
	
	public List<BzrGjtTextbookDistribute> findByIsDeletedAndStatusIn(String orgId, String classId, String isDeleted, Collection<Integer> statuses);
	
	/**
	 * 查询当前待发教材总数
	 * @param orgId
	 * @param studyYearCode
	 * @return
	 */
	public int queryCurrentDistributeCount(String orgId, int studyYearCode);
	
	/**
	 * 查询当前待发教材列表
	 * @param orgId
	 * @param studyYearCode
	 * @param isEnough： true-只查询足够库存的，false或null-查询全部
	 * @return
	 */
	public List<Object[]> queryCurrentDistributeList(String orgId, int studyYearCode, Boolean isEnough);
	
	/**
	 * 查询学生的当前待发教材列表
	 * @param orgId
	 * @param studyYearCode
	 * @return
	 */
	public List<Object[]> queryStudentCurrentDistributeList(String orgId, int studyYearCode);

}
