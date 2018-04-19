/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.systemManage;

/**
 * 
 * 功能说明：代办事项
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年7月1日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface ToDoService {

	public Integer queryStudyYear(String schoolId);

	public Integer queryStudyYearRenWu(String schoolId);

	public Integer queryGrade(String schoolId);

	public Integer queryTeachClass(String schoolId);

	public Integer queryTeach(String schoolId, Integer employee);

	public Integer queryCourseClass(String schoolId);

}
