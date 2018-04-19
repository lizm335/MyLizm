/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.organization;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.openClass.GjtCoachDate;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 
 * 功能说明：共享资料
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年8月24日
 * @version 3.0
 *
 */
public interface GjtCoachDateService extends BaseService<GjtCoachDate> {

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月24日 下午3:41:19
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<Map<String, Object>> findCoachDateList(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月28日 上午1:25:43
	 * @param gjtCoachDate
	 * @param termCourseIds
	 */
	void createCoachDate(GjtCoachDate gjtCoachDate, List<String> termCourseIds);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月10日 下午4:59:03
	 * @param termCourseId
	 * @return
	 */
	List<GjtCoachDate> queryByTermCourseId(String termCourseId);

}
