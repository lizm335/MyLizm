/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.organization;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.college.GjtSpecialtyCollege;

/**
 * 功能说明：
 * @author ouguohao@eenet.com
 * @Date 2017年5月20日
 * @version 3.0
 *
 */
public interface GjtSpecialtyCollegeService {

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月20日 下午3:55:55
	 * @param orgId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<GjtSpecialtyCollege> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 根据专业规则号判断是否存在专业
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月22日 上午9:58:16
	 * @param ruleCode
	 * @param orgId
	 * @return
	 */
	GjtSpecialtyCollege existsSpecialtyByRuleCode(String ruleCode, String orgId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月22日 上午10:13:12
	 */
	GjtSpecialtyCollege save(GjtSpecialtyCollege entity);

	/**
	 * 根据id查询专业
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月23日 下午5:09:28
	 * @param id
	 * @return
	 */
	GjtSpecialtyCollege findOne(String id);

	/**
	 * 保存导入的专业规则
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年6月6日 下午3:49:51
	 * @param datas
	 * @param user
	 */
	void saveImportData(List<String[]> datas, GjtUserAccount user);

}
