/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.daoImpl;

import com.ouchgzee.headTeacher.daoImpl.base.BaseDaoImpl;
import com.ouchgzee.headTeacher.dto.ActivityJoinDto;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 班级活动参与学生的信息操作类<br>
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月20日
 * @version 1.0
 *
 */
@Deprecated @Repository("bzrGjtActivityJoinDaoImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class BzrGjtActivityJoinDaoImpl extends BaseDaoImpl {
	/**
	 * 获取参与学生信息
	 * 
	 * @param activityId
	 * @return
	 */
	public List<ActivityJoinDto> getActivityStudentsInfo(String activityId,
			String auditStatus) {
		StringBuilder querySql = new StringBuilder(
				"select v.student_id,t.join_dt,v.xm,v.avatar,(case when v.xbm='1' then '男' when v.xbm='2' then '女' else '未知' end) as xb from gjt_activity_join t left join gjt_student_info v on t.student_id = v.student_id");
		querySql.append(
				" WHERE t.activity_id=:activityId and t.audit_status=:auditStatus");
		Map<String, Object> params = new HashMap();
		params.put("activityId", activityId);
		params.put("auditStatus", auditStatus);
		Sort sort = new Sort(Sort.Direction.DESC, "t.join_dt");
		return super.findAllBySql(querySql, params, sort,
				ActivityJoinDto.class);
	}

	/**
	 * 批量审核活动
	 * 
	 * @param activityId
	 * @return
	 */
	@Transactional(value="transactionManagerBzr")
	public void batchAuditActivity(String activityId, String auditStatus) {
		StringBuilder updateSql = new StringBuilder(
				"update gjt_activity_join t set t.audit_status = 1");
		updateSql.append(
				" WHERE t.activity_id=:activityId and t.audit_status=:auditStatus");
		Map<String, Object> params = new HashMap();
		Query q = em.createNativeQuery(updateSql.toString());
		q.setParameter("activityId", activityId);
		q.setParameter("auditStatus", auditStatus);
		q.executeUpdate();
	}

	/**
	 * 单独审核学生的活动
	 * 
	 * @param activityId
	 * @return
	 */
	@Transactional(value="transactionManagerBzr")
	public void batchAuditActivityBystudentId(String activityId,
			String auditStatus, String studentId) {
		StringBuilder updateSql = new StringBuilder(
				"update gjt_activity_join t set t.audit_status = 1");
		updateSql.append(
				" WHERE t.activity_id=:activityId and t.audit_status=:auditStatus and t.student_id=:studentId");
		Map<String, Object> params = new HashMap();
		Query q = em.createNativeQuery(updateSql.toString());
		q.setParameter("activityId", activityId);
		q.setParameter("auditStatus", auditStatus);
		q.setParameter("studentId", studentId);
		q.executeUpdate();
	}

	/**
	 * 审核不通过的活动
	 * 
	 * @param activityId
	 * @return
	 */
	@Transactional(value="transactionManagerBzr")
	public void batchAuditActivityBystudentIdtoUnpass(String activityId,
			String auditStatus, String studentId) {
		StringBuilder updateSql = new StringBuilder(
				"update gjt_activity_join t set t.audit_status = 2");
		updateSql.append(
				" WHERE t.activity_id=:activityId and t.audit_status=:auditStatus and t.student_id=:studentId");
		Map<String, Object> params = new HashMap();
		Query q = em.createNativeQuery(updateSql.toString());
		q.setParameter("activityId", activityId);
		q.setParameter("auditStatus", auditStatus);
		q.setParameter("studentId", studentId);
		q.executeUpdate();
	}

	/**
	 * 统计班级待审核活动报名人数
	 *
	 * @param searchParams
	 * @return
	 */
	public long countWaitAuditStudentNum(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder querySql = new StringBuilder("select count(*) from gjt_class_info gci ");
		querySql.append(" inner join gjt_class_student gcs on gcs.class_id = gci.class_id and gcs.is_deleted = 'N' ");
		querySql.append(" inner join gjt_activity_join t on gcs.student_id=t.student_id ");
		querySql.append(" where gci.is_deleted = 'N' and gci.bzr_id = :bzrId and gci.class_id=:classId ");
		querySql.append(" and t.audit_status=0");
		parameters.put("bzrId", searchParams.get("bzrId"));
		parameters.put("classId", searchParams.get("classId"));
		return super.countBySql(querySql, parameters);
	}

}
