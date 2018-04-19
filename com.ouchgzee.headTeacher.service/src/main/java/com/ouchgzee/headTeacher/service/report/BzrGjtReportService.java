/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.service.report;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ouchgzee.headTeacher.pojo.BzrGjtReport;
import com.ouchgzee.headTeacher.service.base.BaseService;

/**
 * 日报接口<br>
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月27日
 * @version 1.0
 *
 */
@Deprecated public interface BzrGjtReportService extends BaseService<BzrGjtReport> {

	/**
	 * 增加一条日报
	 * 
	 * @param gjtReport
	 * @return 0:新增失败，1新增成功
	 */
	String addReport(BzrGjtReport gjtReport);

	/**
	 * 查询日报
	 * 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<BzrGjtReport> queryReportInfoByCreatedDtPage(
			Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 修改日报
	 * 
	 * @param gjtReport
	 * @return 0:修改失败，1:修改成功，-1:修改有误
	 */
	String updateReport(BzrGjtReport gjtReport);

}
