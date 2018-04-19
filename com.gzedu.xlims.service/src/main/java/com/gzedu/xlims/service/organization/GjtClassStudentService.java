/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.organization;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtClassStudent;
import com.gzedu.xlims.pojo.GjtStudentInfo;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月25日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtClassStudentService {
	public Page<GjtClassStudent> queryAll(String orgId, Map<String, Object> map, PageRequest pageRequest);

	long findAllCount(Map<String, Object> map);

	public List<GjtClassStudent> queryAll();

	public List<GjtClassStudent> queryAll(String orgId, Map<String, Object> map);

	public Boolean save(GjtClassStudent gjtClassStudent);

	public void insertClassStudent(String studentids, String classId);

	public Boolean deleteById(String id);

	public Boolean deleteById(String[] ids);

	public void delete(String id);

	GjtClassStudent queryById(String id);

	public List<GjtClassStudent> queryByClassId(String classId);

	public Boolean save(List<GjtClassStudent> gjtClassStudentList);

	/**
	 * 获取教学班级
	 * 
	 * @param studentId
	 * @return
	 */
	GjtClassInfo queryTeachClassInfoByStudetnId(String studentId);

	/**
	 * 查询班级学员
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryClassStudentInfo(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 调班，同时调用EE接口
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年6月17日 下午4:47:28
	 * @param classStudents
	 * @param gjtClassInfo
	 *            需要调到的班级
	 * @param appId
	 * @return
	 */
	boolean updateClassStudent(List<GjtClassStudent> classStudents, GjtClassInfo gjtClassInfo, String appId);

	Map getStudentDataInfo(Map<String,Object> searchParams);

	/**
	 * 查询选课学生
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月18日 下午3:36:43
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryTermCourseStudentList(Map<String, Object> searchParams,
			PageRequest pageRequst);
	/**
	 * 根据学员ID删除学员的教学班和课程班
	 * @param studentId
	 */
	public Boolean deleteByStudentId(String studentId,String classId,String memo);
	/**
	 * 添加学员至教学班中
	 * @param gjtClassInfo
	 * @param gjtStudentInfo
	 */
	public Boolean addStudentToClassStudent(GjtClassInfo gjtClassInfo, GjtStudentInfo gjtStudentInfo,GjtClassInfo oldGjtClassInfo);
	/**
	 * 对学员进行调班
	 * @param targetFile
	 * @param path
	 * @return
	 */
	public Map importStuClassInfo(File targetFile, String path);
}
