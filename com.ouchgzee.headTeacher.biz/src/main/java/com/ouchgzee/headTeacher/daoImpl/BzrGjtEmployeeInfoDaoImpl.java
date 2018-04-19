/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.daoImpl;

import com.ouchgzee.headTeacher.daoImpl.base.BaseDaoImpl;
import com.ouchgzee.headTeacher.dto.CourseTeacherDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教职工信息操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年08月06日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtEmployeeInfoDaoImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
@Transactional(value="transactionManagerBzr", readOnly = true)
public class BzrGjtEmployeeInfoDaoImpl extends BaseDaoImpl {

	/**
	 * 获取教学班下面的课程班的辅导老师信息，SQL语句
	 *
	 * @param classId
	 * @return
	 */
	public List<CourseTeacherDto> findCourseTeacherByTeachClassId(String classId) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("select t.employee_id employeeId,t.xm,b.bjmc");
		querySql.append(" from gjt_employee_info t");
		querySql.append(" inner join gjt_class_info b on t.employee_id=b.bzr_id and b.is_deleted='N'");
		querySql.append(" where t.is_deleted='N' and t.employee_type='2' and b.class_type='course'");
		querySql.append(" and b.class_id in (select distinct x.class_id from gjt_class_student x where x.is_deleted='N' and x.student_id in (select y.student_id from gjt_class_student y where y.is_deleted='N' and y.class_id=:classId))");
		parameters.put("classId", classId);
		return super.findAllBySql(querySql, parameters, null, CourseTeacherDto.class);
	}

}
