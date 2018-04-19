/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import com.gzedu.xlims.pojo.college.GjtSpecialtyCollege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年5月20日
 * @version 3.0
 *
 */
public interface GjtSpecialtyCollegeDao
		extends JpaRepository<GjtSpecialtyCollege, String>, JpaSpecificationExecutor<GjtSpecialtyCollege> {

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月22日 上午10:02:36
	 * @param ruleCode
	 * @param orgId
	 * @return
	 */
	GjtSpecialtyCollege getByRuleCodeAndOrgId(String ruleCode, String orgId);

	/**
	 * 根据条件获取专业ID
	 * @param zymc
	 * @param pycc
	 * @param xxId
	 */
	@Query(value = "select gs.specialty_id from GJT_SPECIALTY_COLLEGE gs where gs.NAME=:zymc and gs.SPECIALTY_LEVEL=:pycc and gs.ORG_ID=:xxId", nativeQuery = true)
	List<String> findSchoolSpcialty(@Param("zymc") String zymc, @Param("pycc") String pycc, @Param("xxId") String xxId);

}
