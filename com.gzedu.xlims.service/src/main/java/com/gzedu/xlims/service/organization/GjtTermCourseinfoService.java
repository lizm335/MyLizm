/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.organization;

import java.util.List;
import java.util.Map;

import com.gzedu.xlims.pojo.GjtTermCourseinfo;
import com.gzedu.xlims.service.base.BaseService;

public interface GjtTermCourseinfoService extends BaseService<GjtTermCourseinfo> {

	List<String> findTermIdsBytermCourseIds(List<String> termCourseIds);

	List<Map<String, Object>> queryCourseByTeacherId(String teachId);

}
