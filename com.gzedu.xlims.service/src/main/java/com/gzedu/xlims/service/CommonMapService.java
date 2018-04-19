/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 功能说明：
 *
 * @author 李明 liming@eenet.com
 * @Date 2016年5月26日
 * @version 2.5
 *
 */
public interface CommonMapService {

	/** 获取当前考试计划code */
	public String getCurrentGjtExamBatchNew(String xxid);

	/** 获取当前考试计划id */
	public String getCurrentGjtExamBatchNewId(String xxid);

	/**
	 * 获得学院列表
	 */
	Map<String, String> getOrgMap();

	Map<String, String> getOrgMapByType(String orgType);

	Map<String, String> getChildOrgMap(String orgId, String orgTypes);

	/**
	 * 获得该用户学院列表
	 */
	Map<String, String> getOrgMap(String userId);

	Map<String, String> getOrgMapByOrgId(String orgId);

	/**
	 * 获得该用户的学院/分院
	 */
	Map<String, String> getOrgMapBy(String userId, boolean isChild);

	// // 获得该用户学院列表
	// public Map<String, String> getSchoolInfoMap(String userId);

	Map<String, String> getOrgTree(String parentId, boolean onlySchool);

	/**
	 * 获取机构下的学习中心和招生点
	 * @param orgId
	 * @return
	 */
	Map<String, String> getStudyCenterMap(String orgId);
	
	/**
	 * 获取机构下的学习中心
	 * @param orgId
	 * @return
	 */
	Map<String, String> getStudyCenterTypeThreeMap(String orgId);

	/**
	 * 根据学习中心id获取学校id
	 * 
	 * @param xxzxId
	 * @return
	 */
	Map<String, String> getxxIdByxxzxId(String xxzxId);

	Map<String, String> getStudyCenter();

	/**
	 * 学院下的专业列表
	 */
	Map<String, String> getSpecialtyMap(String orgId);

	Map<String, String> getSpecialtyMapBySpecialtyBaseId(String specialtyBaseId);

	Map<String, String> getSpecialtyCodeMap(String orgId);

	Map<String, String> getSpecialtyBaseMap(String orgId);

	/**
	 * 院校模式下的专业下拉
	 *
	 * @param orgId
	 * @return
	 */
	Map<String, String> getSchoolModelSpecialtyMap(String orgId);

	/**
	 * 学院下的年级
	 */
	Map<String, String> getGradeMap(String orgId);

	Map<String, String> getXjzt();

	Map<String, String> getChargeMap();

	Map<String, String> getAuditMap();

	/**
	 * 学院的培养层次，取redis缓存
	 */
	Map<String, String> getPyccMap();

	Map<String, String> getPyccMap(String orgId);

	/**
	 * 专业模块里面的课程
	 */
	Map<String, String> getCourseTypeMap();

	/**
	 * 学籍状态
	 */
	Map<String, String> getRollTypeMap();

	/**
	 * 院校的职员
	 */
	Map<String, String> getEmployeeMap(String orgId, EmployeeTypeEnum employeeType);

	/**
	 * 院校管理员列表
	 */
	Map<String, String> getOrgMagagerRoleMap();

	/**
	 * 院校班级
	 */
	Map<String, String> getClassInfoMap(String orgId, String classType);

	/**
	 * 获取院校课程班
	 * 
	 * @param orgId
	 * @param courseId
	 * @return
	 */
	Map<String, String> getCourseClassInfoMap(String orgId, String courseId);

	// 院校的学期(废弃)
	// public Map<String, String> getTermTypeMap(String orgId);

	/**
	 * 院校的课程
	 */
	Map<String, String> getCourseMap(String orgId);

	/**
	 * 院校的学年度
	 */
	Map<Integer, String> getStudyYearMap();

	/**
	 * 根据数据类型查询code，name键值对 Map<String, String>
	 * 
	 * @param typeCode
	 * @return
	 */
	Map<String, String> getDates(String typeCode);

	/**
	 * Map<Integer, String>
	 * 
	 * @param typeCode
	 * @return
	 */
	Map<Integer, String> getDatesBy(String typeCode);

	/**
	 * 根据数据字典的type_code获取数组
	 * 
	 * @param typeCode
	 * @return
	 */
	List<Map<String, Object>> getDateList(String typeCode);

	/**
	 * 根据数据类型查询id，name键值对
	 */
	Map<String, String> getIdNameMap(String typeCode);

	/**
	 * @return
	 */
	Map<String, String> getCategoryDates();

	/**
	 * @param xx_id
	 * @return
	 */
	Map<String, String> getMenuDates(String xx_id);

	/**
	 * @param name
	 * @return
	 */
	Map<String, String> getMenuByName(String name);

	/**
	 * @param GRADE_CODE
	 * @return
	 */
	Map<String, String> getGradeDates(String GRADE_CODE);

	/**
	 * @param xxId
	 * @return
	 */
	Map<String, String> getXxmcDates(String xxId);

	/**
	 * @return
	 */
	Map<String, String> getXxmcAllDates();

	/**
	 * @param id
	 * @return
	 */
	Map<String, String> getXxzxmcDates(String id);

	Map<String, String> getStudyYearMap(String xxid);

	Map<String, String> getGradeMapData(String xxid);

	Map<Integer, String> getStudyYearCodeNameMap(String xxid);

	Map<String, String> getGjtExamBatchNewIdNameMap(String xxid);

	Map<String, String> getGjtExamBatchNewMap(String xxid);

	Map<String, String> getProvinceMap();

	Map<String, String> getCityMap(String provinceCode);

	Map<String, String> getDistrictMap(String cityCode);

	Map<String, String> getAreaMap();

	Map<String, String> getExamPointMap(String xxid);

	/**
	 * 获取gjt_district表
	 *
	 * @return
	 */
	Map<String, String> getGjtDistrictMap();

	Map<String, String> getXxmcDatesExceptBS(String xxId);

	Map<String, String> getGjtExamSubjectNewIdNameMap(String xxid, int examType);

	Map<String, String> getExamPointMapByBatchCode(String examBatchCode);

	/*
	 * 查询除本身之外的班级
	 */
	public Map<String, String> getBjmcDatesExceptBS(String classId, String orgId);

	/**
	 * 获取考试方式
	 */
	Map<BigDecimal, String> getExamTypeMap();

	Map getExamTypeMapNew();

	/**
	 * 获取考试方式
	 */
	Map getExamTypeIntMap();

	/**
	 * 查询所有考试批次
	 *
	 * @param xxid
	 * @return
	 */
	Map<String, String> getAllExamBatchs(String xxid);

	/**
	 * 责任人岗位
	 */
	Map<String, String> getResponsibleMap();

	/**
	 * @param sql
	 * @return
	 */
	Map<String, String> getMap(String sql);

	/**
	 * @param specialtyId
	 * @return
	 */
	Map<String, String> getSpecialtyDates(String specialtyId);

	Map<String, String> getTermMap(String xxid);

	Map<String, String> getTextbookPlanMap(String orgId);

	Map<String, Map<String, String>> getExsubjectAndkindMap();

	Map<String, String> getExsubjectMap();

	Map<String, String> getExsubjectkindMap(String exsubjectKey);

	List<CacheService.Value> getExsubjectkindList(String exsubjectKey);

	Map<String, String> getYearMap(String orgId);

	/**
	 * 获取当前学期
	 * 
	 * @param orgId
	 * @return
	 */
	Map<String, String> getCurrentGradeMap(String orgId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年6月15日 下午6:25:20
	 * @param gjtClassInfo
	 * @param id
	 * @return
	 */
	public Map<String, String> getBjmcDatesExceptBS(GjtClassInfo gjtClassInfo, String id);

	/**
	 * 企业大学接口--根据学习中心ID或CODE查询院校ID
	 * 
	 * @param string
	 * @return
	 */
	public Map<String, String> getxxIdByCode(String prams);

	Map<String, String> getXxzxIdByXxzxCode(String orgCode);

	/**
	 * 根据机构ID查询毕业计划列表
	 * 
	 * @param orgId
	 * @return
	 */
	Map<String, String> getGraduationPlanMap(String orgId);
	/**
	 * 查询存在毕业计划的学期
	 * @param id
	 * @return
	 */
	public Map<String, String> getGraduationGradeMap(String id);
	
	/**
	 * 获取此班级下学生所有的选课列表
	 * @param classId
	 * @return
	 */
	public Map<String, String> getTeachClassCourse(String classId); 
	
	
	/**
	 * 获取此班级下学生的所有专业
	 * @param classId
	 * @return
	 */
	public Map<String, String> getTeachClassMajor(String classId);


	/**
	 * 导出学位申请记录
	 * @param formMap
	 * @param request
	 * @param response
	 * @return
	 */
	Workbook exportDegreeApply(Map formMap, HttpServletRequest request, HttpServletResponse response);
}
