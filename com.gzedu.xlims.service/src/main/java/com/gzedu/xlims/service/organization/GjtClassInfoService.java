/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.organization;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtGradeSpecialty;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtStudentInfo;

/**
 *
 * 功能说明：班级管理
 *
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtClassInfoService {
	// 根据条件查询数据源
	Page<GjtClassInfo> querySource(final GjtClassInfo searchEntity, PageRequest pageRequest);

	Page<GjtClassInfo> queryAll(String orgId, Map<String, Object> map, PageRequest pageRequest);

	List<GjtClassInfo> queryAllExport(String orgId, Map<String, Object> map);

	Map<String, Integer> queryClassPeople(List<String> classIds);

	List<Map<String, String>> queryClassInfo(List<String> str);

	// 添加班级信息
	Boolean saveEntity(GjtClassInfo entity);

	// 创建课程班级

	// 修改班级信息
	Boolean updateEntity(GjtClassInfo employeeInfo);

	// 查询班级信息
	GjtClassInfo queryById(String id);

	Boolean queryByBjmc(String bjmc, String xxId);

	// 查询所有班级
	List<GjtClassInfo> queryAll();

	// 删除（假）
	Boolean deleteById(String id);

	Boolean deleteById(String[] ids);

	// 删除（真）
	void delete(String id);

	// 是否启用停用
	Boolean updateIsEnabled(String id, String str);

	// 根据学年度、课程、院校ID，查询课程班级数量
	long count(int studyYearCode, String courseId, String xxId);

	void insertCreateClass(String xxId, String gradeId, String specialtyId);

	/**
	 * 创建教学班
	 * 
	 * @param termId
	 *            学期
	 * @param specialtyId
	 *            专业
	 * @param bh
	 *            序号（班号）
	 */
	GjtClassInfo createTeachClassInfo(String termId, String specialtyId, int bh, String xxzxId, String xxId,
			String operatorId) throws Exception;

	/**
	 * 创建课程班
	 * 
	 * @param termId
	 *            学期
	 * @param courseId
	 *            课程
	 * @param termcourseId
	 *            期课程ID
	 * @param bh
	 *            序号（班号）
	 */
	GjtClassInfo createCourseClassInfo(String termId, String courseId, String termcourseId, int bh, String xxId,
			String operatorId, String memo) throws Exception;

	/**
	 * 获得学员的教学班信息
	 *
	 * @param studentId
	 *            学员信息
	 * @return
	 */
	GjtClassInfo queryTeachClassInfo(String studentId);

	/**
	 * 根据课程ID、学年度编码获得课程班
	 *
	 * @param courseId
	 *            课程ID
	 * @param studyYearCode
	 *            学年度编码
	 * @return
	 */
	GjtClassInfo queryClassInfosByCourseIdAndStudyYearCode(String courseId, int studyYearCode);

	void setProgressAvg(List<GjtClassInfo> classInfoList);

	void setScoreAvg(List<GjtClassInfo> classInfoList);

	List<Object[]> findClassIdANDTermcourseId(String supervisorId, String classType);

	/**
	 * 根据是否设置班主任统计班级
	 *
	 * @author ouguohao@eenet.com
	 * @Date 2017年6月20日 下午2:44:51
	 * @param orgId
	 * @param map
	 * @return
	 */
	long countClassInfo(String orgId, Map<String, Object> map);

	/**
	 * 查询课程班级
	 */
	Page getClassList(Map searchParams, PageRequest pageRequst);

	/**
	 * 查询课程班级统计
	 */
	int getClassCount(Map searchParams);

	/**
	 * 查询教学班
	 * 
	 * @param newSpecialty
	 * @param gradeId
	 * @param pycc
	 * @param string
	 * @param id
	 * @return
	 */
	GjtClassInfo findBySpecialtyIdAndGradeIdAndPyccAndClassTypeAndxxzxIdAndIsDeleted(String newSpecialty,
			String gradeId, String pycc, String classType, String id, String isDeleted);

	/**
	 * 新建教学班
	 * 
	 * @param gjtClassInfo
	 * @param gjtStudentInfo
	 */
	void createGjtClassInfo(GjtStudentInfo gjtStudentInfo, GjtOrg gjtOrg, String newSpecialtyId, int bh,
			String operatorId, GjtClassInfo oldGjtClassInfo);

	/**
	 * 查询除自己本身以外的教学班级
	 * 
	 * @param id
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<GjtClassInfo> queryGjtClassInfo(String id, Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 根据班级名称及学习中心查询教学班级
	 * 
	 * @param string
	 * @param id
	 * @return
	 */
	GjtClassInfo queryByBjmcAndXxzxId(String bjmc, String xxzxId);

	/**
	 * 获取教学辅导机构的课程列表
	 * 
	 * @param formMap
	 * @return
	 */
	List getXxzxClassList(Map formMap);

	/**
	 * 获取教学辅导机构的课程列表
	 * 
	 * @param formMap
	 * @return
	 */
	List getXxzxCourseList(Map formMap);

	/**
	 * 新增学习中心和期课程关系
	 * 
	 * @param formMap
	 * @return
	 */
	public int addTermcourseOrg(Map formMap);

	/**
	 * 删除学习中心和期课程关系
	 * 
	 * @param formMap
	 * @return
	 */
	public int delTermcourseOrg(Map formMap);

	/**
	 * 把教务班级新的班主任同步至EE
	 * 
	 * @param classInfo
	 * @return
	 */
	boolean syncClassTeacherToEeChat(GjtClassInfo classInfo);

	/**
	 * 生成教学班和选课
	 * 
	 * @return
	 */
	boolean createTeachClassAndRec(GjtStudentInfo gjtStudentInfo, GjtOrg gjtStudyCenter,
			GjtGradeSpecialty gjtGradeSpecialty, String newSpecialty, String newGradeId, int bh);

	/**
	 * 获取学员的课程班
	 * 
	 * @param studentId
	 * @param courseId
	 * @return
	 */
	GjtClassInfo findCourseClassByStudentIdAndCourseId(String studentId, String courseId);

	/**
	 * 教辅云 自动创建教学班级
	 * 
	 * @param gjtGrade
	 * @param termId
	 * @param specialtyId
	 * @param pycc
	 * @param xxId
	 * @param zymc
	 * @param operatorId
	 * @param index
	 * @return
	 */
	GjtClassInfo createNewTeachingTeachClassInfo(GjtGrade gjtGrade, String specialtyId, String pycc, String xxId,
			String zymc, String operatorId, int index);
}
