/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.student;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.ouchgzee.headTeacher.dto.CourseClassInfoDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtClassInfo;
import com.ouchgzee.headTeacher.service.base.BaseService;

/**
 * 班级业务接口<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月11日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public interface BzrGjtClassService extends BaseService<BzrGjtClassInfo> {

	/**
	 * 查询班主任管理的班级数
	 *
	 * @param employeeId
	 * @return 班级数
	 */
	Long countClassByBzr(String employeeId);

	/**
	 * 更新班级信息
	 * 
	 * @param classInfo
	 * @return
	 */
	boolean update(BzrGjtClassInfo classInfo);

	/**
	 * 关闭班级
	 * 
	 * @param classId
	 *            班级ID
	 * @param updatedBy
	 *            修改人
	 * @return
	 */
	boolean updateCloseClass(String classId, String updatedBy);

	/**
	 * 批量关闭班级
	 * 
	 * @param classIds
	 *            班级ID
	 * @param updatedBy
	 *            修改人
	 * @return
	 */
	boolean updateCloseClass(String[] classIds, String updatedBy);

	/**
	 * 重开班级
	 * 
	 * @param classId
	 *            班级ID
	 * @param updatedBy
	 *            修改人
	 * @return
	 */
	boolean updateReopenClass(String classId, String updatedBy);

	/**
	 * 批量重开班级
	 * 
	 * @param classIds
	 *            班级ID
	 * @param updatedBy
	 *            修改人
	 * @return
	 */
	boolean updateReopenClass(String[] classIds, String updatedBy);

	/**
	 * 分页根据条件查询班主任的班级信息/年级信息
	 *
	 * @param bzrId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<BzrGjtClassInfo> queryClassInfoByPage(String bzrId, Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 根据条件查询班主任的班级信息/年级信息
	 * 
	 * @param bzrId
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	List<BzrGjtClassInfo> queryClassInfoBy(String bzrId, Map<String, Object> searchParams, Sort sort);

	/**
	 * 根据学籍班获取所有课程班
	 * 
	 * @param classId
	 * @return
	 */
	List<CourseClassInfoDto> queryCourseClassInfoByTeachClassId(String classId);

	/**
	 * 学籍班获取某学期的课程班
	 * @param classId
	 * @param termId
     * @return
     */
	List<CourseClassInfoDto> queryCourseClassInfoByTeachClassIdAndTermId(String classId, String termId);

	/**
	 * 快速获取学生的ID
	 * 
	 * @param classId
	 * @return
	 */
	List<String> queryTeacherClassStudent(String classId);

	/**
	 * 检验是否属于该教职工管理下的班级
	 * 
	 * @param classId
	 * @param employeeId
	 * @return
	 */
	boolean isClassToTeacher(String classId, String employeeId);

	/**
	 * 统计当前班级数量
	 * 
	 * @param bzrId
	 * @return
	 */
	long countOpenClassNum(String bzrId);

	/**
	 * 统计关闭班级数量
	 * 
	 * @param bzrId
	 * @return
	 */
	long countCloseClassNum(String bzrId);

	List<Object[]> getStudentSpecialty(String specialtyId, List<String> ids);

	/**
	 * 获取班级学员对象
	 * 
	 * @param classId
	 * @return
	 */
	List<Map<String, Object>> queryStudentByClassId(String classId, String searchName);

	List<Map<String, Object>> queryClassInfoByTeachId(String bzrId);

}
