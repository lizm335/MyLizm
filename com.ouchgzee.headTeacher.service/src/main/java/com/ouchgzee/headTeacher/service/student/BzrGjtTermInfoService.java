/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.student;

import java.util.List;
import java.util.Map;

import com.ouchgzee.headTeacher.pojo.BzrGjtTermInfo;
import com.ouchgzee.headTeacher.service.base.BaseService;

/**
 * 学期业务接口<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月16日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public interface BzrGjtTermInfoService extends BaseService<BzrGjtTermInfo> {

	/**
	 * 获取学员的当前学期
	 * 
	 * @param studentId
	 * @return
	 */
	String getStudentCurrentTerm(String studentId);

	/**
	 * 获取学员[总学期/已学习]的学期数
	 * 
	 * @param studentId
	 * @return
	 */
	Object[] countByStudentTerm(String studentId);

	/**
	 * 根据班级id查询全部的期
	 * 
	 * @param classId
	 * @return
	 */
	List<Map<String, Object>> findByClassTerms(String classId);

}
