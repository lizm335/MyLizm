/**
 * @Package com.gzedu.xlims.dao 
 * @Project com.gzedu.xlims.biz
 * @File LyUserDao.java
 * @Date:2016年4月19日下午2:22:01
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtCourse;

/**
 * 
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月1日
 * @version 2.5
 *
 */
public interface GjtCourseDao extends com.gzedu.xlims.dao.base.BaseDao<GjtCourse, String> {

	@Modifying
	@Transactional
	@Query("update GjtCourse g set g.syncStatus=?1 where g.courseId=?2 ")
	int updateSyncStatus(String syncStatus, String courseId);

	GjtCourse findByKchAndIsDeleted(String kch, String isDeleted);

	List<GjtCourse> findByXxId(String xxid);

	List<GjtCourse> findByXxIdAndIsDeleted(String xxid, String isDeleted);

	List<GjtCourse> findByKchAndXxIdAndIsDeleted(String kch, String xxid, String isDeleted);

	/**
	 * 当前院校的课程+分享课程
	 * 
	 * @param kch
	 * @param xxid
	 * @param courseIds
	 * @return
	 */
	@Query("select t from GjtCourse t where t.isDeleted='N' and t.kch=?1 and (t.xxId=?2 or t.courseId in (?3))")
	List<GjtCourse> findByKchAndXxIdAndIsDeleted(String kch, String xxid, List<String> courseIds);

	@Modifying
	@Transactional
	@Query("update GjtCourse g set g.isEnabled=?2 where g.courseId=?1 ")
	int updateIsEnabled(String courseId, String isEnabled);

	@Modifying
	@Transactional
	@Query("update GjtCourse g set g.isEnabled=?2,g.checkUser=?3,g.checkDt=sysdate,g.isOpen='1' where g.courseId=?1 ")
	int updateIsEnabled(String courseId, String isEnabled, String userName);

	/**
	 * 根据课程号查询补考费用
	 * 
	 * @param kch
	 * @return
	 */
	@Query(value = "SELECT NVL(( SELECT TSD.CODE FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'COURSE_COST' AND TSD.NAME = :kch ),( SELECT TSD.CODE FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'COURSE_COST' AND TSD.NAME = 'other' )) COURSE_COST FROM DUAL", nativeQuery = true)
	String findExamFeeByKch(@Param("kch") String kch);

	List<GjtCourse> findByXxIdAndCourseNatureAndCourseCategoryAndIsDeleted(String xxId, String courseNature,int courseCategory, String booleanNo);

}
