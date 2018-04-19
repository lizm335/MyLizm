/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.edumanage;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtStudyYearCourse;

/**
 * 
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年7月25日
 * @version 2.5
 *
 */
public interface GjtStudyYearCourseService {
	public Page<GjtStudyYearCourse> queryAll(String orgId, Map<String, Object> map, PageRequest pageRequest);
}
