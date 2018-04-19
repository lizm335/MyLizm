/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.edumanage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.graduation.GjtRulesClass;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2017年2月15日
 * @version 1.0
 *
 */
public interface GjtRulesClassDao
		extends JpaRepository<GjtRulesClass, String>, JpaSpecificationExecutor<GjtRulesClass> {

	/**
	 * 根据年级ID查询GjtRulesClass
	 * @param gradeId
	 * @return
	 */
	GjtRulesClass getByGradeId(String gradeId);
	
	@Query("select c from GjtRulesClass c where c.orgId = ?1 and c.gradeId is null and c.type1 is null and c.type2 is null")
	GjtRulesClass queryCourseClassRule(String orgId);

	List<GjtRulesClass> findByOrgIdAndGradeId(String orgId, String currentGradeId);

	List<GjtRulesClass> findByOrgIdAndGradeIdAndPointClassType(String id, String currentGradeId, String pointClassType);

	GjtRulesClass findByOrgIdAndGradeIdAndPointClassTypeAndXxzxId(String id, String gradeId, String pointClassType,String xxzxId);
	
	@Query("select c from GjtRulesClass c where c.orgId = ?1 and c.gradeId =?2 and c.pointClassType =?3 and c.xxzxId =?4")
	List<GjtRulesClass> queryGjtRulesClassInfo(String id, String gradeId, String pointClassType, String xxzxId);

	GjtRulesClass findByOrgIdAndGradeIdAndXxzxId(String xxId, String gradeId, String xxzxId);
	
	@Query("select c from GjtRulesClass c where c.orgId = ?1 and c.gradeId =?2 and c.pointClassType =?3 ")
	GjtRulesClass queryByOrgIdAndGradeId(String xxId, String gradeId,String pointClassType);

}
