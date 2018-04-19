/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.organization;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gzedu.xlims.pojo.GjtStudentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtEnrollBatch;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.dto.GjtGradeDto;

/**
 * 
 * 功能说明：年级管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月13日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtGradeService {
	/**
	 * // 根据条件查询数据源
	 * 
	 * @param searchEntity
	 * @param pageRequest
	 * @return
	 */
	Page<GjtGradeDto> querySource(final GjtGrade searchEntity, PageRequest pageRequest);

	Page<GjtGrade> queryAll(String schoolId, Map<String, Object> searchParams, PageRequest pageRequest);

	Boolean queryByGradeCode(String gradeCode);

	/**
	 * 根据学期名称查询
	 * @param gradeName
	 * @param xxId
	 * @return
	 */
	GjtGrade findByGradeNameAndGjtSchoolInfoId(String gradeName, String xxId);

	List<GjtEnrollBatch> findByGjtGrade(GjtGrade gjtGrade);

	/**
	 * // 添加年级信息
	 * 
	 * @param entity
	 * @return
	 */
	Boolean saveEntity(GjtGrade entity);

	Boolean saveAdd(GjtGrade item);

	/**
	 * // 修改年级信息
	 * 
	 * @param entity
	 * @return
	 */
	Boolean updateEntity(GjtGrade entity);

	Boolean updateAdd(GjtGrade entity, GjtUserAccount user);

	Boolean updateGjtGrade(GjtGrade entity);

	/**
	 * // 查询年级信息
	 * 
	 * @param id
	 * @return
	 */
	GjtGrade queryById(String id);

	/**
	 * // 查询所有年级
	 * 
	 * @return
	 */
	List<GjtGrade> queryAll();

	/**
	 * 获取院校当前的年级ID
	 * 
	 * @param xxId
	 * @return
	 */
	String getCurrentGradeId(String xxId);

	/**
	 * // 删除（假）
	 * 
	 * @param id
	 * @return
	 */
	Boolean deleteById(String[] id);

	/**
	 * // 删除（真）
	 * 
	 * @param id
	 */
	void delete(String id);

	/**
	 * // 是否启用停用
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	Boolean updateIsEnabled(String id, String str);

	/**
	 * 查询学院和子级学院的年级
	 * 
	 * @param orgId
	 * @return
	 */
	Map<String, String> findGradeMapByOrgId(String orgId);

	/**
	 * 查询年级信息
	 * 
	 * @param gradeCode
	 * @param orgId
	 * @return
	 */
	GjtGrade queryByGjtGradeCode(String gradeCode);

	/**
	 * 根据学生培养层次、专业、入学学期ID查
	 * 找VIEW_TEACH_PLAN表,找到最后的学期，然后再根据学期的ACTUAL_GRADE_ID，查找学期gjt_grage,得出学期开始时间、结束时间
	 * 
	 * @param pycc
	 * @param specialtyId
	 * @param gradeId
	 * @return
	 */
	GjtGrade findByPYCCAndSpecialtyIdAndGradeId(String pycc, String specialtyId, String gradeId);

	/**
	 * 根据学生培养层次、专业、入学学期ID统计学期(gjt_grade)
	 * 
	 * @param pycc
	 * @param specialtyId
	 * @param gradeId
	 * @return
	 */
	Map<String, Object> countGradeByStudent(String pycc, String specialtyId, String gradeId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月8日 下午4:27:32
	 * @param grade
	 * @param size
	 * @return
	 */
	List<GjtGrade> findGradeBySize(GjtGrade grade, int size);
	
	GjtGrade findCurrentGrade(String xxId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月26日 下午4:40:19
	 * @param orgId
	 * @param startTime
	 * @return
	 */
	List<String> findGradeIdByStartDate(String orgId, Date startTime);

	List<GjtGrade> findGradeIdByOrgId(String orgId);

	/**
	 * 给所有学院自动新建年级和学期信息
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年6月14日 下午4:05:16
	 */
	boolean batchCreateGrade();

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月1日 上午11:48:58
	 * @param yearId
	 * @return
	 */
	List<GjtGrade> findGradeByYearId(String yearId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月3日 下午4:48:52
	 * @param orgId
	 */
	void initYearAndGrade(String orgId);
	
	List<GjtGrade> findGradesBefore(String xxId, String gradeId);
	
	GjtGrade findPrevGradeBefore(String xxId, String gradeId);
	//临时使用
	List<GjtGrade> findGradeIdByOrgIdNew(String xxId);

	List<GjtGrade> getGradeList(Date currentGradeDate, String xxId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月30日 下午3:53:18
	 * @param gradeSpecialtyId
	 * @param studentId
	 * @return
	 */
	List<Map<String, Object>> findByGradeSpecialtyId(String gradeSpecialtyId, String studentId);

	/**
	 * 查询学生当前在那一学期
	 * @param currentGradeId
	 * @param studentId
	 * @return
	 */
	long getStudentCurrentTerm(String currentGradeId, String studentId);

	/**
	 * 查询学生当前学分
	 * @param studentId
	 * @return
	 */
	long getStudentCurrentCredit(String studentId);

	/**
	 * 查询学生最高学分
	 * @param studentId
	 * @return
	 */
	long getStudentCreditTotal(String studentId);

	/**
	 * 获取当前学生的毕业状态
	 * @param student
	 * @param planId
	 * @return
	 */
	int getGraduationStateByStudent(GjtStudentInfo student, String planId);

	/**
	 * 获取毕业状态的学生
	 * @param state
	 * @param planId
	 * @return
	 */
	List<Map<String,Object>> queryGraduationStudent(int state, String planId);
}
