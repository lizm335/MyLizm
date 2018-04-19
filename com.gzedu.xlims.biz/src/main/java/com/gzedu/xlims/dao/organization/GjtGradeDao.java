
/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtGrade;

/**
 * 
 * 功能说明：年级管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月13日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtGradeDao extends JpaRepository<GjtGrade, String>, JpaSpecificationExecutor<GjtGrade> {
	/**
	 * 是否删除 N Y
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtGrade g set g.isDeleted=?2 where g.gradeId=?1 ")
	int deleteById(String id, String str);

	/**
	 * 是否启用1启用，0停用
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtGrade g set g.isEnabled=?2 where g.gradeId=?1 ")
	int updateIsEnabled(String id, String str);

	GjtGrade findByGradeCode(String gradeCode);

	GjtGrade findByGradeCodeAndIsDeleted(String gradeCode, String isDeleted);

	GjtGrade findByGradeNameAndGjtSchoolInfoIdAndIsDeleted(String gradeName, String xxId, String isDeleted);

	/**
	 * 获取院校当前的年级ID
	 * 
	 * @param xxId
	 * @return
	 */
	@Query(value = "select grade_id from (select grade_id from gjt_grade where xx_id=:xxId and is_deleted='N' and start_date <= sysdate and end_date >= sysdate order by start_date desc) temp where rownum=1", nativeQuery = true)
	String getCurrentGradeId(@Param("xxId") String xxId);

	@Query(value = "select grade_id from (select grade_id from gjt_grade where xx_id=:xxId and is_deleted='N' and end_date <= sysdate  order by end_date desc) temp where rownum=1", nativeQuery = true)
	String getPrevGradeId(@Param("xxId") String xxId);

	@Query("select g from GjtGrade g where isDeleted = 'N' and gjtSchoolInfo.id = ?1 and startDate <= sysdate and endDate >= sysdate order by startDate desc ")
	List<GjtGrade> findCurrentGrade(String xxId);

	@Query("select g from GjtGrade g where isDeleted = 'N' and gjtSchoolInfo.id = ?1 and endDate <= sysdate order by endDate desc ")
	List<GjtGrade> findPrevGrade(String xxId);

	@Query(value="select * from gjt_grade where xx_id=?1 and is_deleted = 'N' order by grade_name desc " ,nativeQuery=true)
	List<GjtGrade> findGradeIdByOrgId(String orgId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月1日 下午2:03:41
	 * @param yearId
	 * @param isEanbled
	 * @return
	 */
	@Query("from GjtGrade g where g.isDeleted='N' and g.gjtYear.gradeId=?1 and g.isEnabled=?2")
	List<GjtGrade> findGradeByYearIdAndIsEnabled(String yearId, String isEanbled);
	
	@Query("select g from GjtGrade g where g.isDeleted = 'N' and g.gjtSchoolInfo.id = ?1 and g.startDate < (select g2.startDate from GjtGrade g2 where g2.gradeId = ?2) order by g.startDate desc ")
	List<GjtGrade> findGradesBefore(String xxId, String gradeId);
	
	@Query(value="select grade_id from (select grade_id from gjt_grade where xx_id=:xxId and is_deleted='N' and is_enabled = '1' order by start_date desc) temp where rownum=1",nativeQuery = true)
	String queryGradeByYear(@Param("xxId") String xxId);
	/**
	 * 临时使用
	 * @param xxId
	 * @return
	 */
	@Query(value="select * from gjt_grade where xx_id=?1 and is_deleted = 'N' order by grade_name" ,nativeQuery=true)
	List<GjtGrade> findGradeIdByOrgIdNew(String xxId);

	@Query(value="select * from gjt_grade where xx_id=?2 and is_deleted = 'N' and start_date<?1 order by start_date desc" ,nativeQuery=true)
	List<GjtGrade> getGradeList(Date currentGradeDate, String xxId);
}
