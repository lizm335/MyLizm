/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.organization;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.GjtYear;

/**
 * 功能说明：年级管理
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年4月7日
 * @version 3.0
 *
 */
public interface GjtYearDao extends JpaRepository<GjtYear, String>, JpaSpecificationExecutor<GjtYear> {

	public List<GjtYear> findByOrgIdOrderByStartYearDesc(String orgId);

	public List<GjtYear> findByOrgIdAndIsEnabledOrderByStartYearAsc(String orgId, int isEnable);

	@Query(value = "select y.* From gjt_year y where y.org_id=?1 and exists( select 1 from gjt_grade g where g.year_id=y.grade_id and g.is_deleted='N' and g.is_enabled=0) order by y.start_year", nativeQuery = true)
	public List<GjtYear> findByExistsEnableGrade(String orgId);
}
