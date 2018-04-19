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
public interface GjtTeachCourseService {
	
	/**
	 * 任教管理列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page getTeachCourseList(Map searchParams,PageRequest pageRequst);
	
	public List getTeachCourseList(Map searchParams);
	
	/**
	 * 任教管理列表（统计）
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int getTeachCourseCount(Map searchParams);
	
	/**
	 * 老师列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page getTeacherList(Map searchParams,PageRequest pageRequst);
	
	/**
	 * 删除任教信息
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int delTeacherInfo(Map searchParams);
	
	/**
	 * 更新任教信息
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int uptTeacherInfo(Map searchParams);
	
	/**
	 * 新增任教
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int addCourseTeacher(Map searchParams);
	
	/**
	 * 同步数据
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Map getSyncResult(Map searchParams, String basePath);
	
	/**
	 * 同步班级信息到学习网更新结果
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int updClassXxwSyncStatus(Map searchParams);
	
	/**
	 * 同步班级信息到学习网更新结果
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int updStudClassXxwSyncStatus(Map searchParams);
	
	/**
	 * 查询班级信息
	 * @return
	 */
	public List getClassInfo(Map searchParams);
	
	/**
	 * 更新调班信息
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public int updClassStudent(Map searchParams);
	
	/**
	 * 查询班级学员
	 * @return
	 */
	public List getClassStudent(Map searchParams);
	
	/**
	 * 导出选课记录
	 */
	/*public String expRecordRecResult(Map formMap);*/
}
