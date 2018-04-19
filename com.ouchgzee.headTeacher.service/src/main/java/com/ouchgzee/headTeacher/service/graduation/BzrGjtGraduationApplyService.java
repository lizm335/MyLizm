package com.ouchgzee.headTeacher.service.graduation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationApply;

/**
 * 毕业申请
 * @author eenet09
 *
 */
@Deprecated public interface BzrGjtGraduationApplyService {
	
	/**
	 * 根据批次和专业查询学员申请列表
	 * @param batchId
	 * @param specialtyId
	 * @return
	 */
	public List<BzrGjtGraduationApply> queryListBySpecialty(String batchId, String specialtyId);
	
	/**
	 * 更新
	 * @param gjtGraduationApplyList
	 */
	public void update(List<BzrGjtGraduationApply> gjtGraduationApplyList);
	
	/**
	 * 查询毕业申请列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	/**
	 * 查询申请学员总数：totalCount-总数，completedCount-已通过数量，noCompletedCount-未通过数量
	 * @param batchId
	 * @param applyType 申请类型：1-毕业论文，2-社会实践
	 * @param teacherType  指导老师类型：1-指导老师，2-答辩老师
	 * @param teacherId
	 * @return
	 */
	public Map<String, Object> queryGraduationApplyCount(String batchId, int applyType, int teacherType, String teacherId);
	
	/**
	 * 查询毕业申请列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<BzrGjtGraduationApply> queryPage(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public BzrGjtGraduationApply queryOneByStudent(String batchId, String studentId, int applyType);

	public void update(BzrGjtGraduationApply entity);
	
	public BzrGjtGraduationApply queryOne(String applyId);

	public List<BzrGjtGraduationApply> queryList(String batchId, String specialtyId, int applyType, int defenceType, Set<Integer> status);
	
	/**
	 * 查询我的指导列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<BzrGjtGraduationApply> queryMyGuideList(Map<String, Object> searchParams, PageRequest pageRequst);

}
