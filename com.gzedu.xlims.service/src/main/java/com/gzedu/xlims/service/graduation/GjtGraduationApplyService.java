package com.gzedu.xlims.service.graduation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gzedu.xlims.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.graduation.GjtGraduationApply;

/**
 * 毕业申请
 * @author eenet09
 *
 */
public interface GjtGraduationApplyService extends BaseService<GjtGraduationApply> {
	
	/**
	 * 根据批次和专业查询学员申请列表
	 * @param batchId
	 * @param specialtyId
	 * @return
	 */
	public List<GjtGraduationApply> queryListBySpecialty(String batchId, String specialtyId);
	
	/**
	 * 更新
	 * @param gjtGraduationApplyList
	 */
	public void update(List<GjtGraduationApply> gjtGraduationApplyList);

	/**
	 * 查询毕业申请列表，带分页
	 * @param searchParams
	 * @param pageRequest
     * @return
     */
	Page<GjtGraduationApply> queryGraduationApplyByPage(Map<String, Object> searchParams, PageRequest pageRequest);
	
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
	public Page<GjtGraduationApply> queryPage(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public GjtGraduationApply queryOneByStudent(String batchId, String studentId, int applyType);

	public void update(GjtGraduationApply entity);
	
	public GjtGraduationApply queryOne(String applyId);

	public List<GjtGraduationApply> queryList(String batchId, String specialtyId, int applyType, int defenceType, Set<Integer> status);
	
	/**
	 * 查询我的指导列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<GjtGraduationApply> queryMyGuideList(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public GjtGraduationApply findByStudentIdAndApplyTypeAndStatusGreaterThanEqual(String studentId, int applyType, int status);

}
