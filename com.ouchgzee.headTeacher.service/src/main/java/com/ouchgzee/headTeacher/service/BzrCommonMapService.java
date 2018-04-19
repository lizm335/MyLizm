/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.service;

import com.ouchgzee.headTeacher.pojo.status.EmployeeType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：
 *
 * @author 李明 liming@eenet.com
 * @Date 2016年5月26日
 * @version 2.5
 *
 */
@Deprecated public interface BzrCommonMapService {

	public Map<String, String> getOrgMap();

	// 获得该用户学院列表
	public Map<String, String> getOrgMap(String userId);

	public Map<String, String> getOrgMapByOrgId(String orgId);

	// 获得该用户的学院/分院
	public Map<String, String> getOrgMapBy(String userId, boolean isChild);
	
	/**
	 * 获得该机构的指定为orgType的上级机构id
	 * @param orgId
	 * @param orgType
	 * @return
	 */
	public String getParentWithType(String orgId, String orgType);

	// // 获得该用户学院列表
	// public Map<String, String> getSchoolInfoMap(String userId);

	// 学习中心
	public Map<String, String> getStudyCenterMap(String orgId);

	// 学院下的专业列表
	public Map<String, String> getSpecialtyMap(String orgId);

	// 学院下的年级
	public Map<String, String> getGradeMap(String orgId);

	// 学院的培养层次
	public Map<String, String> getPyccMap();

	Map<String, String> getPyccMap(String orgId);

	// 学籍状态
	public Map<String, String> getRollTypeMap();

	// 院校的职员
	public Map<String, String> getEmployeeMap(String orgId, EmployeeType employeeType);

	// 院校管理员列表
	public Map<String, String> getOrgMagagerRoleMap();

	// 院校班级
	Map<String, String> getClassInfoMap(String orgId, String classType);

	/**
	 * 获取班主任所教的班级
	 * @param bzrId
	 * @return
	 */
	Map<String, String> getClassInfoMapByBzrId(String bzrId);

	// 班级下的学员
	Map<String, String> getStudentMap(String classId);

	public List<Object> queryStudentUserId(String classId);

	Map<String, String> getDates(String typeCode);

	/**
	 * 获取考试方式
	 * 
	 * @return
	 */
	Map<BigDecimal, String> getExamTypeMap();

	Map<String, String> getTextbookPlanMap(String orgId);

	/**
	 * 获取此班级下学生所有的选课列表
	 * @param classId
	 * @return
	 */
	Map<String,String> getTeachClassCourse(String classId);

	/**
	 * 获取此班级下学生的所有专业
	 * @param classId
	 * @return
	 */
    Map<String,String> getTeachClassMajor(String classId);
    
    public String getCurrentGjtExamBatchNew(String xxid);
    
    public Map<String, String> getGjtExamBatchNewIdNameMap(String xxid);

	/**
	 * 获取班主任所教的教学班所有学员拥有的专业
	 * @param bzrId
	 * @return
	 */
	Map<String,String> getBzrTeachMajor(String bzrId);

	/**
	 * 获取当前学期
	 *
	 * @param orgId
	 * @return
	 */
	Map<String, String> getCurrentGradeMap(String orgId);

	/**
	 * 获取院校所属的课程班
	 * @param orgId
	 * @param courseId
	 * @return
	 */
	Map getCourseClassInfoMap(String orgId, String courseId);

	/**
	 * 获取年级
	 * @param xxId
	 * @return
	 */
    Map getYearMap(String xxId);
}
