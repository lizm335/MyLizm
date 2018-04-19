/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtSpecialtyPlan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * 功能说明：专业教学计划管理
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月17日
 * @version 2.5
 *
 */
public interface GjtSpecialtyPlanDao extends BaseDao<GjtSpecialtyPlan, String> {

	@Query("select g from  GjtSpecialtyPlan g  where g.specialtyId=?1 order by termTypeCode, courseId")
	List<GjtSpecialtyPlan> findBySpecialtyId(String id);
	
	@Modifying
	@Transactional
	@Query("delete from  GjtSpecialtyPlan g  where g.specialtyId=?1 ")
	int deleteBySpecialtyId(String specialtyId);

	List<GjtSpecialtyPlan> findBySpecialtyIdAndCourseIdIn(String specialtyId, List<String> courseIds);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月22日 下午6:16:59
	 * @param specialtyId
	 * @param courseId
	 */
	GjtSpecialtyPlan findBySpecialtyIdAndCourseId(String specialtyId, String courseId);

	/**
	 * 获取院校下的专业计划，根据专业和课程
	 * @param xxId
	 * @param specialtyId
	 * @param courseId
     * @return
     */
	GjtSpecialtyPlan findByXxIdAndSpecialtyIdAndCourseId(String xxId, String specialtyId, String courseId);
}
