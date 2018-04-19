package com.gzedu.xlims.dao.graduation;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyCertif;

import java.util.Date;

/**
 * 毕业记录
 * 
 * @author eenet09
 *
 */
public interface GjtGraduationApplyCertifDao extends BaseDao<GjtGraduationApplyCertif, String> {

	@Query("select r from GjtGraduationApplyCertif r where r.gjtStudentInfo.studentId = ?1 and r.isReceive = ?2")
	public GjtGraduationApplyCertif queryByStudentIdAndStatus(String studentId, int status);

	@Query("select r from GjtGraduationApplyCertif r where r.gjtStudentInfo.studentId = ?1")
	public GjtGraduationApplyCertif queryByStudentId(String studentId);

	@Query("select r from GjtGraduationApplyCertif r where r.gjtStudentInfo.studentId = ?1 and r.gjtGraduationPlan.id =?2")
	public GjtGraduationApplyCertif queryByStudentIdAndPlanId(String studentId, String planId);

	@Query("select r from GjtGraduationApplyCertif r where r.gjtStudentInfo.studentId = ?1 and r.gjtGraduationPlan.id =?2 and r.isReceive = ?3")
	public GjtGraduationApplyCertif queryByStudentIdAndPlanIdWithStatus(String studentId, String planId, int status);

	@Modifying
	@Transactional
	@Query("update GjtGraduationApplyCertif t set t.photoUrl=:photoUrl where studentId=:studentId ")
	public int updatePhotoUrl(@Param("studentId") String studentId, @Param("photoUrl") String photoUrl);

	/**
	 * 初始化入学学期时间之前的所有学员的毕业生毕业申请记录
	 *
	 * @param xxId
	 * @param graduationPlanId
	 * @param enterSchoolDate
	 * @return
	 */
	@Modifying
	@Query(value = "insert into GJT_GRADUATION_APPLY_CERTIF(apply_id,student_id,created_by,photo_url,graduation_plan_id)"
			+ "  (select LOWER(RAWTOHEX(sys_guid())),t.student_id,'初始化数据',t.graduated_avatar ,:graduationPlanId from gjt_student_info t"
			+ "   inner join gjt_grade b on b.grade_id=t.nj" + "   where t.is_deleted='N' and t.xjzt='2'"
			+ "   and t.xx_id=:xxId" + "   and b.start_date<=:enterSchoolDate"
			+ "   and not exists (select 1 from GJT_GRADUATION_APPLY_CERTIF x where x.is_deleted='N' and x.student_id=t.student_id and x.graduation_plan_id=:graduationPlanId)"
			+ "  )", nativeQuery = true)
	@Transactional
	int initCurrentTermGraduationStudent(@Param("xxId") String xxId, @Param("graduationPlanId") String graduationPlanId,
										 @Param("enterSchoolDate") Date enterSchoolDate);
}
