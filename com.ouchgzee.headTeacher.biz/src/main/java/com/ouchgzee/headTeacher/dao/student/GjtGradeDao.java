/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.student;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtGrade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 年级信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月11日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtGradeDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtGradeDao extends BaseDao<BzrGjtGrade, String> {

	/**
	 * 根据学校ID获取学校的所有年级信息
	 * 
	 * @param xxId
	 * @return
	 */
	@Query("SELECT t FROM BzrGjtGrade t WHERE t.isDeleted='N' AND t.xxId=:xxId ORDER BY t.createdDt DESC")
	List<BzrGjtGrade> findByXxId(@Param("xxId") String xxId);

	/**
	 * 获取院校当前的年级ID
	 *
	 * @param xxId
	 * @return
	 */
	@Query(value = "select grade_id from (select grade_id from gjt_grade where xx_id=:xxId and is_deleted='N' and is_enabled='1' and START_DATE <= sysdate order by START_DATE desc) temp where rownum=1", nativeQuery = true)
	String getCurrentGradeId(@Param("xxId") String xxId);

	/**
	 * 获取当前学期及之前的学期
	 * @param xxId
	 * @return
	 */
	@Query("select g from BzrGjtGrade g where isDeleted = 'N' and xxId = ?1 and startDate <= sysdate order by startDate desc ")
	List<BzrGjtGrade> findCurrentGrade(String xxId);

	/**
	 * 获取当前学期及之前的学期
	 * @param xxId
	 * @return
	 */
	@Query("select g from BzrGjtGrade g where isDeleted = 'N' and xxId = ?1 and startDate <= sysdate order by startDate")
	List<BzrGjtGrade> findCurrentTermAndBeforeTerm(String xxId);

	/**
	 * 获取当前学期及之前的学期(不超过某学籍)
	 * @param xxId
	 * @return
	 */
	@Query("select g from BzrGjtGrade g where isDeleted = 'N' and xxId = ?1 and startDate <= sysdate and startDate >= (select x.startDate from BzrGjtGrade x where x.gradeId=?2) order by startDate")
	List<BzrGjtGrade> findCurrentTermAndBeforeTerm(String xxId, String termId);

}
