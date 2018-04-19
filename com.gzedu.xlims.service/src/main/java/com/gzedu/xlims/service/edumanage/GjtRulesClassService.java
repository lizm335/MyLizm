/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.edumanage;

import java.util.List;

import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtRulesClass;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2017年2月15日
 * @version 1.0
 *
 */
public interface GjtRulesClassService {
	GjtRulesClass queryById(String id);

	void update(GjtRulesClass gjtRulesClass);// 相当于save，有就update，无就insert

	GjtRulesClass getByGradeId(String gradeId);
	
	GjtRulesClass queryCourseClassRule(String orgId);

	List<GjtRulesClass> findByOrgIdAndGradeId(String orgId, String currentGradeId);

	List<GjtRulesClass> findByOrgIdAndGradeIdAndPointClassType(String id, String currentGradeId, String pointClassType);

	GjtRulesClass findByOrgIdAndGradeIdAndPointClassTypeAndXxzxId(String id, String gradeId, String type, String xxzxId);

	List<GjtRulesClass> queryGjtRulesClassInfo(String id, String gradeId, String string, String xxzxId);
	/**
	 * //新增【按年级专业层次总体分班】分班规则
	 * @param entity
	 * @param user 
	 */
	void insertPyccGjtRuleClass(GjtRulesClass entity, GjtUserAccount user);

	GjtRulesClass findByOrgIdAndGradeIdAndXxzxId(String xxId, String gradeId, String xxzxId);

	GjtRulesClass queryByOrgIdAndGradeId(String xxId, String gradeId,String pointClassType);
}
