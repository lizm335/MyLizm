package com.gzedu.xlims.service.edumanage;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtTextbookPlanOwnership;

/**
 * 
 * 功能说明：教学计划
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月17日
 * @version 2.5
 *
 */
public interface GjtSpecialtyPlanService {
	public Page<GjtSpecialtyPlan> queryAll(final String schoolId, Map<String, Object> searchParams,
			PageRequest pageRequst);

	public GjtSpecialtyPlan queryBy(String id);

	public void delete(String[] ids);

	public void delete(String id);

	public void insert(GjtSpecialtyPlan entity);

	public void insert(List<GjtSpecialtyPlan> entities);

	public void insert(GjtSpecialtyPlan entity, List<GjtTextbookPlanOwnership> gjtTextbookPlanOwnershipList);

	public void update(GjtSpecialtyPlan entity);

	public void update(GjtSpecialtyPlan entity, List<GjtTextbookPlanOwnership> gjtTextbookPlanOwnershipList);

	public List<GjtSpecialtyPlan> findBySpecialtyId(String id);
	
	/**
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月31日 上午9:59:46
	 * @param specialtyId
	 * @param notInCourseIds
	 *            排除的课程id
	 * @return
	 */
	public List<GjtSpecialtyPlan> findBySpecialtyId(String gradeSpecialtyId, String specialtyId,
			List<String> notInCourseIds);

	public int deleteBySpecialtyId(String specialtyId);

	/**
	 * 判断是否存在专业规则
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月22日 下午6:13:45
	 * @param specialtyId
	 * @param courseId
	 * @return
	 */
	public boolean isExists(String specialtyId, String courseId);

	/**
	 * 根据id统计数量
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月23日 上午11:52:34
	 * @param ids
	 * @return
	 */
	public Map<String, Long> countBySpecialtyId(List<String> ids);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年6月9日 下午4:28:17
	 * @param specialtyId
	 * @param courseId
	 * @return
	 */
	public GjtSpecialtyPlan findBySpecialtyIdAndCourseId(String specialtyId, String courseId);
}
