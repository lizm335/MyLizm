/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.systemManger;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.service.systemManage.ToDoService;

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
@Service
public class ToDoServiceImpl implements ToDoService {

	@PersistenceContext(unitName = "entityManagerFactory")
	private EntityManager em;

	public Integer getSql(String sql) {
		Query queryTotal = em.createNativeQuery(sql);
		int singleResult = NumberUtils.toInt(queryTotal.getSingleResult().toString());
		return singleResult;
	}

	@Override
	public Integer queryStudyYear(String schoolId) {
		String sql = "select count(0) from gjt_studyyear_info g where sysdate between g.study_start_date and g.studyyear_end_date and g.xx_id='"
				+ schoolId + "'";
		return getSql(sql);
	}

	@Override
	public Integer queryStudyYearRenWu(String schoolId) {
		String sql = "select count(0) from gjt_studyyear_info g inner join gjt_studyyear_assignment c on g.studyyear_id=c.studyyear_id "
				+ " where sysdate between g.study_start_date and g.studyyear_end_date  and g.xx_id='" + schoolId + "'";
		return getSql(sql);
	}

	@Override
	public Integer queryGrade(String schoolId) {
		Calendar calendar = Calendar.getInstance();
		int i = calendar.get(Calendar.YEAR);
		String sql = "select count(0) from gjt_grade g where g.belong_year ='" + i + "' and g.xx_id='" + schoolId + "'";
		return getSql(sql);
	}

	@Override
	public Integer queryTeachClass(String schoolId) {
		String sql = "select count(0) from gjt_class_info c where c.class_type='teach' and c.bzr_id is null and c.xx_id='"
				+ schoolId + "'";
		return getSql(sql);
	}

	@Override
	public Integer queryCourseClass(String schoolId) {
		String sql = "select count(0) from gjt_class_info c where c.class_type='course' and c.bzr_id is null and c.xx_id='"
				+ schoolId + "'";
		return getSql(sql);
	}

	@Override
	public Integer queryTeach(String schoolId, Integer employee) {
		String sql = "select count(0) from gjt_employee_info e where e.EMPLOYEE_TYPE='" + employee + "'  and e.xx_id='"
				+ schoolId + "'";
		return getSql(sql);
	}

}
