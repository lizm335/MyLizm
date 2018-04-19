/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtGradeSpecialtyPlan;

/**
 * 
 * 功能说明：年级专业教学计划管理
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月17日
 * @version 2.5
 *
 */
public interface GjtGradeSpecialtyPlanDao
		extends JpaRepository<GjtGradeSpecialtyPlan, String>, JpaSpecificationExecutor<GjtGradeSpecialtyPlan> {

	List<GjtGradeSpecialtyPlan> findByGradeIdAndSpecialtyIdOrderByCreatedDtAsc(String gradeId, String gjtSpecialtyId);
}
