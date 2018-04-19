/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.graduation;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtFirstCourse;
import com.gzedu.xlims.pojo.GjtSpecialtyBase;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 
 * 功能说明：开学第一堂课
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年10月9日
 * @version 3.0
 *
 */
public interface GjtFirstCourseService extends BaseService<GjtFirstCourse> {

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月10日 上午9:30:59
	 * @param searchParams
	 * @param pageRequest
	 */
	Page<GjtFirstCourse> queryFirstCoursesListByPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 统计所有可以观看该第一堂课的学生
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月10日 下午6:01:59
	 * @param firstCourseId
	 * @return
	 */
	long countAllFirstCourseStudentNum(String firstCourseId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月11日 上午10:41:47
	 * @param specialtyBaseId
	 */
	GjtFirstCourse queryBySpecialtyBaseId(String specialtyBaseId);

	/**
	 * 根据类型查询
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月11日 下午5:46:02
	 * @param orgId
	 * @param type
	 * @return
	 */
	List<GjtFirstCourse> queryByType(String orgId, int type);

	/**
	 * 查询学生观看第一堂课的记录
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月11日 上午11:39:10
	 * @param firstCourseId
	 * @param studentId
	 * @return
	 */
	Object queryByStudentId(String studentId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月12日 上午9:50:36
	 * @param firstCourseId
	 * @param studentId
	 */
	void saveFirstCourseStudent(String firstCourseId, String studentId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月12日 上午10:21:35
	 * @param id
	 * @param studentId
	 */
	Object queryByFourseCourseIdAndStduentId(String id, String studentId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月12日 下午2:36:48
	 * @param gjtFirstCourse
	 * @param videos
	 * @param specialtys
	 */
	void saveFirstCourse(GjtFirstCourse gjtFirstCourse, List<String> videos, List<String> specialtys);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月12日 下午4:14:02
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<GjtSpecialtyBase> querySpecialtyBasePage(Map<String, Object> searchParams, PageRequest pageRequst);

	Page<Map<String, Object>> queryFirstCourseStduentPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月13日 上午11:06:05
	 * @param orgId
	 * @return
	 */
	long countStudentByOrgId(String orgId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月18日 下午6:20:25
	 * @param firstCourseId
	 * @return
	 */
	long countFirstCourseStudentNum(String firstCourseId);

}
