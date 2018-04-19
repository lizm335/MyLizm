/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.organization;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtYear;

/**
 * 功能说明：年級管理
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年4月7日
 * @version 3.0
 *
 */
public interface GjtYearService {
	Page<GjtYear> queryAll(String schoolId, Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月7日 下午1:56:00
	 * @param id
	 * @return
	 */
	GjtYear queryById(String id);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月7日 下午2:16:52
	 * @param gjtYear
	 */
	void updateGjtYear(GjtYear gjtYear);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年7月18日 下午4:44:45
	 * @param orgId
	 * @return
	 */
	List<GjtYear> findByOrgId(String orgId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年7月18日 下午5:30:15
	 * @param gradeId
	 * @return
	 */
	GjtYear findOne(String gradeId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年7月19日 下午3:47:07
	 * @param gjtYear
	 */
	void save(GjtYear gjtYear);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年7月31日 上午11:28:17
	 * @param orgId
	 * @param isEnable
	 * @return
	 */
	List<GjtYear> findByOrgIdAndIsEnabled(String orgId, int isEnable);

	/**
	 * 查询存在未启用学期的年级
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月1日 上午10:46:47
	 * @param orgId
	 * @return
	 */
	List<GjtYear> findByExistsEnableGrade(String orgId);
}
