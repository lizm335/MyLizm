/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtStudentStudySituation;

/**
 * 
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年7月28日
 * @version 2.5
 *
 */
public interface GjtStudentStudySituationDao
		extends JpaRepository<GjtStudentStudySituation, String>, JpaSpecificationExecutor<GjtStudentStudySituation> {

	GjtStudentStudySituation findByStudentIdAndTeachPlanId(String studentId, String studyYearCourseId);
}
