package com.gzedu.xlims.service.exam;

import com.gzedu.xlims.pojo.exam.GjtExamCorrectPapers;
import com.gzedu.xlims.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

/**
 * 报告批改业务逻辑类
 */
public interface GjtExamCorrectPapersService extends BaseService<GjtExamCorrectPapers> {

	/**
	 * 查询学员考试科目的报告信息
	 * @param studentId
	 * @param examPlanId
	 * @return
	 */
	GjtExamCorrectPapers findByStudentIdAndExamPlanId(String studentId, String examPlanId);

	/**
	 * 按条件查询报告批改信息，带分页
	 * @param orgId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<GjtExamCorrectPapers> findAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 按条件查询报告批改数量，统计
	 * @param orgId
	 * @param searchParams
	 * @return
	 */
	long count(String orgId, Map<String, Object> searchParams);

	/**
	 * 修改报告批改信息
	 * @param entity
	 * @return
	 */
	boolean update(GjtExamCorrectPapers entity);

	/**
	 * 批改成绩，更新选课的考试成绩
	 * @param id
	 * @param score
	 * @param correctBy 批改人
	 * @return
	 */
	boolean updateScore(String id, int score, String correctBy);

}
