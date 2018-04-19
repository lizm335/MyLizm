
/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.pcourse;

import java.util.List;
import java.util.Map;

import com.gzedu.xlims.pojo.opi.OpiCourseData;
import com.gzedu.xlims.pojo.opi.OpiStudentData;
import com.gzedu.xlims.pojo.opi.OpiTeacherData;
import com.gzedu.xlims.pojo.opi.OpiTermChooseData;
import com.gzedu.xlims.pojo.opi.OpiTermClassData;
import com.gzedu.xlims.pojo.opi.OpiTermCourseData;
import com.gzedu.xlims.pojo.opi.PoiDelStudentData;
import com.gzedu.xlims.pojo.opi.RSimpleData;

/**
 * 
 * 功能说明：教学服务器接口
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年7月18日
 * @version 2.5
 *
 */
public interface PCourseServer {
	RSimpleData synchroCourse(OpiCourseData data);

	RSimpleData synchroTermCourse(OpiTermCourseData data);

	RSimpleData synchroStudDel(PoiDelStudentData data);

	RSimpleData synchroTermClass(OpiTermClassData data);

	RSimpleData synchroStudent(OpiStudentData data);

	Map delStudChoose(Map formMap);

	RSimpleData synchroTeacher(OpiTeacherData data);

	RSimpleData synchroTermChoose(OpiTermChooseData data);

	Map copyActDynaToNewCourse(Map formMap);

	/**
	 * 获取学习平台学习记录
	 * 
	 * @param formMap
	 */
	Map getStudyRecord(Map formMap);

	/**
	 * 获取学习平台的班级动态数据
	 * 
	 * @param formMap
	 * @return
	 */
	Map getClassDyna(Map formMap);

	/**
	 * 获取学习平台学习记录
	 * 
	 * @param formMap
	 */
	List getLoginStudent(Map formMap);

	int syncAllStudentData();

	/**
	 * 同步学习平台的学习记录
	 * 
	 * @param formMap
	 * @return
	 */
	int syncStudyData(Map formMap);

	/**
	 * 同步考试平台的考试记录
	 * 
	 * @param formMap
	 * @return
	 */
	int syncExamData(Map formMap);

	/**
	 * 同步接口记录新增
	 * 
	 * @param formMap
	 * @return
	 */
	int addSyncRecord(Map formMap);
}
