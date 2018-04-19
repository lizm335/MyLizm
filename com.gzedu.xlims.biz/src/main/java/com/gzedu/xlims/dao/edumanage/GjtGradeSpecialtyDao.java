
/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.edumanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtGradeSpecialty;

/**
 * 
 * 功能说明：年级专业
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月20日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtGradeSpecialtyDao extends BaseDao<GjtGradeSpecialty, String> {
	/**
	 * 是否删除 N Y
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtGradeSpecialty g set g.isDeleted=?2 where g.id=?1 ")
	int deleteById(String id, String str);

	@Query("select gs from GjtGradeSpecialty gs where gs.gjtGrade.gradeId=?1 and gs.gjtSpecialty.specialtyId=?2 and gs.isDeleted = 'N' ")
	List<GjtGradeSpecialty> getByGjtGradeGradeIdAndGjtSpecialtySpecialtyId(String gradeId, String specialtyId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月21日 下午4:58:04
	 * @param gradeId
	 * @return
	 */
	@Query("select gs from GjtGradeSpecialty gs where gs.gjtGrade.gradeId=?1 and gs.isDeleted = 'N' and gs.gjtGrade.isDeleted = 'N' and gs.gjtSpecialty.isDeleted='N' order by gs.gjtSpecialty.specialtyId")
	List<GjtGradeSpecialty> findByGradeId(String gradeId);

	@Query("from GjtGradeSpecialty gs where gs.gjtGrade.gradeId=?1 and gs.gjtSpecialty.specialtyId=?2 and gs.isDeleted = 'N' ")
	List<GjtGradeSpecialty> findByGradeIdAndSpeciatyId(String gradeId, String specialtyId);

	@Query("select gs from GjtGradeSpecialty gs where gs.isDeleted = 'N' and gs.gjtGrade.isDeleted = 'N' and gs.gjtGrade.gjtSchoolInfo.id = ?1 and gs.gjtGrade.startDate <= sysdate and gs.gjtGrade.endDate >= sysdate ")
	List<GjtGradeSpecialty> findCurrentGradeSpecialtyList(String orgId);

	/**
	 * 获取通用的产品
	 * @param gradeId
	 * @param newSpecialty
	 * @param isDeleted
	 * @return
	 */
	@Query(value="select gg.* from gjt_grade_specialty gg  where gg.grade_id =?1 and gg.specialty_id =?2 and is_deleted = ?3 and not exists (select 1 from gjt_gs_study_center gssc where gssc.study_center_id is not null and gssc.grade_specialty_id = gg.id)", nativeQuery=true)
	GjtGradeSpecialty findByGjtGradeGradeIdAndGjtSpecialtySpecialtyIdAndIsDeleted(String gradeId, String newSpecialty, String isDeleted);

	/**
	 * 获取指定学习中心的产品
	 * @param newGradeId
	 * @param newSpecialty
	 * @param xxzxId
	 * @return
	 */
	@Query(value="select gg.* from gjt_grade_specialty gg  where gg.grade_id =?1 and gg.specialty_id =?2 and is_deleted = 'N' and exists  (select 1 from gjt_gs_study_center gssc where gssc.study_center_id =?3 and gssc.grade_specialty_id = gg.id)",nativeQuery=true)
	GjtGradeSpecialty findByGradeIdAndSpecialtyIdAndIsDeletedNew(String newGradeId, String newSpecialty,String xxzxId);

	/**
	 * 获取指定学习中心的产品
	 * @param gradeId
	 * @param specialtyId
	 * @param studyCenterId
	 * @param isDeleted
	 * @return
	 */
	GjtGradeSpecialty findByGjtGradeGradeIdAndGjtSpecialtySpecialtyIdAndGjtStudyCentersIdAndIsDeleted(String gradeId,
			String specialtyId, String studyCenterId, String isDeleted);

}
