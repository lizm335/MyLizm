/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.graduation;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtFirstCourse;

/**
 * 开学第一堂课操作类 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年10月9日
 * @version 3.0
 *
 */
public interface GjtFirstCourseDao extends BaseDao<GjtFirstCourse, String>, JpaRepository<GjtFirstCourse, String> {
	/**
	 * 根据专业id查询第一堂课
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月11日 上午11:01:45
	 * @param specialtyBaseId
	 * @return
	 */
	@Query("select t from GjtFirstCourse t inner join t.gjtSpecialtyBaseList s where t.isDeleted='N' and s.specialtyBaseId=?1")
	List<GjtFirstCourse> queryBySpecialtyBaseId(String specialtyBaseId);
	
	@Query(value = "select * from (select * from GJT_FCOURSE_STUDENT t where  t.STUDENT_ID=?1) where rownum=1", nativeQuery = true)
	Object queryByStudentId(String studentId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月11日 下午5:47:54
	 * @param orgId
	 * @param type
	 * @return
	 */
	@Query("select t from GjtFirstCourse t where t.isDeleted='N' and t.orgId=?1 and t.type=?2")
	List<GjtFirstCourse> queryByType(String orgId, int type);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月12日 上午9:51:31
	 * @param firstCourseId
	 * @param studentId
	 */
	@Modifying
	@Transactional
	@Query(value = "insert into GJT_FCOURSE_STUDENT values (?1,?2,?3)  ", nativeQuery = true)
	void saveFirstCourseStudent(String firstCourseId, String studentId, Date date);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月12日 上午10:22:53
	 * @param courseId
	 * @param studentId
	 * @return
	 */
	@Query(value = "select * from GJT_FCOURSE_STUDENT where FIRST_COURSE_ID=?1 and STUDENT_ID=?2", nativeQuery = true)
	Object queryByFourseCourseIdAndStduentId(String courseId, String studentId);
}
