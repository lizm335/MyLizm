/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.edumanage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtStudyYearCourse;
import com.gzedu.xlims.pojo.GjtStudyYearInfo;
import com.gzedu.xlims.pojo.dto.YearCourseDto;

/**
 * 
 * 功能说明：学年度基础信息
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月16日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtStudyYearService {
	public Page<GjtStudyYearInfo> queryAll(String orgId, Map<String, Object> map, PageRequest pageRequest);

	public Boolean saveEntity(GjtStudyYearInfo entity);

	public Boolean updateEntity(GjtStudyYearInfo entity);

	public GjtStudyYearInfo queryById(String id);

	public void delete(Iterable<String> ids);

	// 学院的学年度列表
	public List<GjtStudyYearInfo> queryAll(String orgId);

	// 学年度课程管理
	public Page<YearCourseDto> queryYearCourse(String orgId, Map<String, Object> searchParams, PageRequest pageRequest);

	// 学年度课程管理班级数
	public Integer queryCount(String shoolId, String course_id, int studyYearCode);

	// 根据时间获得学年度
	public List<GjtStudyYearInfo> queryByStudyStartDateAfter(Date studyyearDt);

	// 学年度管理课程数
	public Integer queryCourseCount(int studyYearId);

	// 根据开始时间和学期编号
	public GjtStudyYearInfo queryByBeginDateAndTermCode(Date beginDate, int termCode);

	public GjtStudyYearInfo queryByStudyYearCodeAndXxId(int studyYearCode, String xxId);

	public GjtStudyYearInfo queryByStudyYearCodeAndXxId2(int studyYearCode, String xxId);

	public GjtStudyYearCourse queryByCourseAndStudyYearInfo(GjtCourse course, GjtStudyYearInfo studyYearInfo);
	
	public void delete(String id);
}
