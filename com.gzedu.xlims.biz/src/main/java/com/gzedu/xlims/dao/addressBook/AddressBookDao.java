package com.gzedu.xlims.dao.addressBook;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import com.gzedu.xlims.pojo.dto.AddressBookDto;

@Repository
@Transactional(readOnly = true)
public class AddressBookDao extends BaseDaoImpl {

	/**
	 * 学员所有的同学，不包含自己
	 * 
	 * @param params
	 * @param pageRequest
	 * @return
	 */
	public Page<AddressBookDto> findByPageHql(Map<String, Object> params, PageRequest pageRequest) {
		StringBuilder queryHql = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		queryHql.append(" SELECT new com.gzedu.xlims.pojo.dto.AddressBookDto(");
		queryHql.append(" GS.xm,GSP.zymc,GS.avatar,GS.eeno,GS.studentId) FROM GjtStudentInfo GS ");
		queryHql.append(" INNER JOIN GS.gjtClassStudentList GCS  ");
		queryHql.append(" INNER JOIN GCS.gjtClassInfo GC  ");
		queryHql.append(" left JOIN GS.gjtSpecialty GSP ");
		queryHql.append(" WHERE GCS.classId=:classId and GC.classType='teach'  ");
		queryHql.append(" and  GCS.isDeleted='N' and GS.studentId!=:studentId ");
		String userName = (String) params.get("userName");

		if (StringUtils.isNotBlank(userName)) {
			queryHql.append(" and GS.xm like :userName");
			map.put("userName", "%" + userName + "%");
		}
		map.put("classId", params.get("classId"));
		map.put("studentId", params.get("studentId"));

		return super.findByPageHql(queryHql, map, pageRequest, AddressBookDto.class);
	}

	/**
	 * 学员所有的辅导老师信息
	 * 
	 * @param params
	 * @return
	 */
	public Page<Map> findCourseTeacher(Map<String, Object> params, PageRequest pageRequst) {
		StringBuilder querySql = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		querySql.append("select  gei.employee_id,gei.xm,gei.zp,gei.eeno  from gjt_class_info  gci ");
		querySql.append("inner join gjt_class_student  gcs on gci.class_id=gcs.class_id ");
		querySql.append("inner join gjt_employee_info gei on gci.bzr_id=gei.employee_id ");
		querySql.append(" where gcs.student_id=:studentId and gci.class_type='course' and gcs.is_deleted='N' ");
		querySql.append("  and gci.is_deleted='N'  and gei.is_deleted='N' ");
		String userName = (String) params.get("userName");
		if (StringUtils.isNotBlank(userName)) {
			querySql.append(" and gei.xm like  :userName  ");
			map.put("userName", "%" + userName + "%");
		}
		map.put("studentId", params.get("studentId"));
		querySql.append(" group by gei.employee_id,gei.xm,gei.zp,gei.eeno");
		return super.findByPageSql(querySql, map, pageRequst, Map.class);
	}

	/**
	 * 统计学员所在教学班级的同学，不包含自己
	 * 
	 * @param params
	 * @return
	 */
	public long findAllCount(Map<String, Object> params) {
		StringBuilder querySql = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		querySql.append("select count(*) from gjt_student_info  gs  ");
		querySql.append(" inner join gjt_class_student gcs on gs.student_id=gcs.student_id ");
		querySql.append(" inner join gjt_class_info gc on gc.class_id=gcs.class_id ");
		querySql.append(" where gcs.class_id=:classId and gc.class_Type='teach' and  gcs.is_Deleted='N' ");
		querySql.append(" and gs.student_id!=:studentId ");

		String userName = (String) params.get("userName");
		if (StringUtils.isNotBlank(userName)) {
			querySql.append(" and gs.xm like  :userName  ");
			map.put("userName", "%" + userName + "%");
		}
		map.put("classId", params.get("classId"));
		map.put("studentId", params.get("studentId"));

		return super.countBySql(querySql, map);
	}

	/**
	 * 统计学员所有辅导老师
	 * 
	 * @param params
	 * @return
	 */
	public long findCourseTeacherCount(Map<String, Object> params) {
		StringBuilder querySql = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		querySql.append(" select count(*) from (select distinct gei.account_id from gjt_class_info  gci ");
		querySql.append(" inner join gjt_class_student  gcs on gci.class_id=gcs.class_id ");
		querySql.append(" inner join gjt_employee_info gei on gci.bzr_id=gei.employee_id ");
		querySql.append(" where gcs.student_id=:studentId and gci.class_type='course' and gcs.is_deleted='N' ");
		querySql.append("  and gci.is_deleted='N'  and gei.is_deleted='N' ");
		String userName = (String) params.get("userName");
		if (StringUtils.isNotBlank(userName)) {
			querySql.append(" and gei.xm like  :userName  ");
			map.put("userName", "%" + userName + "%");
		}
		querySql.append(" ) t");

		map.put("studentId", params.get("studentId"));
		return super.countBySql(querySql, map);
	}

}
