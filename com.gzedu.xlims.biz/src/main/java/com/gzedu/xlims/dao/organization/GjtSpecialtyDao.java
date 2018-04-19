/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.dto.GjtSpecialtyDto;

/**
 * 功能说明：专业管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtSpecialtyDao extends BaseDao<GjtSpecialty, String> {

	@Modifying
	@Transactional
	@Query("update GjtSpecialty g set g.isDeleted=?2 where g.id=?1 ")
	int deleteById(String id, String str);

	@Query(value = "select s.* from  gjt_specialty s,gjt_grade_specialty gs where s.specialty_id = gs.specialty_id and s.is_deleted='N'  and gs.is_deleted='N' and gs.grade_id=?1 ", nativeQuery = true)
	List<GjtSpecialty> findSpecialtyByGradeId(String gradeId);

	@Query(value = "select s.* from  gjt_specialty s where s.is_deleted='N' and s.SPECIALTY_BASE_ID=?1 ", nativeQuery = true)
	List<GjtSpecialty> findSpecialtyBySpecialtyBaseId(String specialtyBaseId);

	@Query(value = "select new com.gzedu.xlims.pojo.dto.GjtSpecialtyDto(s,gs) from  GjtSpecialty s,GjtGradeSpecialty gs where s.specialtyId = gs.gjtSpecialty.specialtyId and s.isDeleted='N'  and gs.isDeleted='N' and gs.gjtGrade.gradeId=?1 ")
	List<GjtSpecialtyDto> findSpecialtyDtoByGradeId(String gradeId);

	/**
	 * 根据条件获取专业ID
	 * @param zymc
	 * @param pycc
	 * @param xxId
	 */
	@Query(value = "select gs.specialty_id from gjt_specialty gs where gs.is_deleted='N' and gs.zymc=:zymc and gs.pycc=:pycc and gs.xx_id=:xxId", nativeQuery = true)
	List<String> findSchoolSpcialty(@Param("zymc") String zymc, @Param("pycc") String pycc, @Param("xxId") String xxId);
	
	@Query(value="select * from gjt_specialty where xx_id=?1 and is_deleted = 'N' ",nativeQuery=true)
	List<GjtSpecialty> findSpecialtyByOrgId(String xxId);

	/**
	 * 根据规则号获取专业
	 * @param ruleCode
	 * @param xxId
	 * @param isDeleted
	 * @return
	 */
	GjtSpecialty findByRuleCodeAndXxIdAndIsDeleted(String ruleCode, String xxId, String isDeleted);

}
