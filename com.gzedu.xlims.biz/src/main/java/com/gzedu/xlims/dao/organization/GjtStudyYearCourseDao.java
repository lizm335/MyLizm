/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtStudyYearCourse;

/**
 * 
 * 功能说明：学期课程表
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月17日
 * @version 2.5 s
 */
public interface GjtStudyYearCourseDao
		extends JpaRepository<GjtStudyYearCourse, String>, JpaSpecificationExecutor<GjtStudyYearCourse> {

	// GjtStudyYearCourse findByCourseAndStudyYearInfo(GjtCourse course,
	// GjtStudyYearInfo studyYearInfo);
	GjtStudyYearCourse findByCourseIdAndStudyYearId(String courseId, String studyYearId);
}
