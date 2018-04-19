/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.edumanage;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 *  选课管理
 */
public interface GjtChooseManageService {
	
	/**
	 * 选课管理列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page getChooseManageList(Map searchParams,PageRequest pageRequst);
	
	/**
	 * 选课管理列表（统计）
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int getChooseManageCount(Map searchParams);
	
	/**
	 * 查询期课程下面未选课学员
	 * @return
	 */
	public List getNoChooseInfo(Map searchParams);
	
	/**
	 * 删除选课
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int delRecResult(Map searchParams);
	
	/**
	 * 新增选课
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int addRecResult(Map searchParams);
	
	/**
	 * 导出选课记录
	 */
	public String expRecordRecResult(Map formMap);
	
	/**
	 * 同步选课
	 */
	public Map getSyncRecList(Map formMap);
	
	/**
	 * 同步选课结果
	 */
	public Map getSyncRecResult(Map formMap, String basePath);
	
	/**
	 * 同步班级数据到国开学习网
	 */
	public Map synchClassStudentCourse(Map formMap);
	
}
