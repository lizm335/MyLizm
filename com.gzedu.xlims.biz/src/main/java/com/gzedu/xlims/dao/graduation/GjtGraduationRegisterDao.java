/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.graduation;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.graduation.GjtGraduationRegister;

/**
 * 毕业登记操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年11月15日
 * @version 2.5
 * @since JDK 1.7
 */
public interface GjtGraduationRegisterDao extends BaseDao<GjtGraduationRegister, String> {

	/**
	 * 初始化入学学期时间之前的所有学员的毕业生登记数据
	 * 
	 * @param xxId
	 * @param graduationPlanId
	 * @param enterSchoolDate
	 * @return
	 */
	@Modifying
	@Query(value = "insert into GJT_GRADUATION_REGISTER(register_id,student_id,photo,created_by,submit_type,graduation_plan_id)"
			+ "  (select LOWER(RAWTOHEX(sys_guid())),t.student_id,t.graduated_avatar,'初始化数据','1',:graduationPlanId from gjt_student_info t"
			+ "   inner join gjt_grade b on b.grade_id=t.nj" + "   where t.is_deleted='N' and t.xjzt='2'"
			+ "   and t.xx_id=:xxId" + "   and b.start_date<=:enterSchoolDate"
			+ "   and not exists (select 1 from GJT_GRADUATION_REGISTER x where x.is_deleted='N' and x.student_id=t.student_id and x.graduation_plan_id=:graduationPlanId)"
			+ "  )", nativeQuery = true)
	@Transactional
	int initCurrentTermGraduationStudent(@Param("xxId") String xxId, @Param("graduationPlanId") String graduationPlanId,
			@Param("enterSchoolDate") Date enterSchoolDate);

	@Query("SELECT s FROM GjtGraduationRegister s where s.isDeleted='N' and s.gjtStudentInfo.studentId = ?1 and s.graduationPlanId = ?2")
	public GjtGraduationRegister queryOneByStudentIdAndPlanId(String studnetId, String graduationPlanId);

	@Query(nativeQuery = true, value = "SELECT info.STUDENT_ID FROM GJT_GRADUATION_REGISTER s, GJT_GRADUATION_REGISTER info where s.IS_DELETED='N' and  info.IS_DELETED='N' and s.STUDENT_ID=info.STUDENT_ID  and s.EXPRESS_SIGN_STATE = ?1")
	List<String> queryStudentIdByStatus(String status);

	@Modifying
	@Transactional
	@Query("update GjtGraduationRegister t set t.photo=:photo where studentId=:studentId ")
	public int updatePhoto(@Param("studentId") String studentId, @Param("photo") String photo);
}
