/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.daoImpl.classActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import com.gzedu.xlims.pojo.dto.ActivityJoinDto;
import com.gzedu.xlims.pojo.dto.GjtActivityDto;

/**
 * 
 * 功能说明：班级活动参与学生的信息操作类
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月28日
 * @version 2.5
 *
 */
@Service
public class GjtActivityJoinDaoImpl extends BaseDaoImpl {
	/**
	 * 获取参与学生信息
	 * 
	 * @param activityId
	 * @return
	 */
	public List<ActivityJoinDto> getActivityStudentsInfo(String activityId, String auditStatus) {
		StringBuilder querySql = new StringBuilder(
				"select v.student_id,t.join_dt,v.xm,v.avatar,(case when v.xbm='1' then '男' when v.xbm='2' then '女' else '未知' end) as xb from gjt_activity_join t left join gjt_student_info v on t.student_id = v.student_id");
		querySql.append(" WHERE t.activity_id=:activityId and t.audit_status=:auditStatus");
		Map<String, Object> params = new HashMap();
		params.put("activityId", activityId);
		params.put("auditStatus", auditStatus);
		Sort sort = new Sort(Sort.Direction.DESC, "t.join_dt");
		return super.findAllBySql(querySql, params, sort, ActivityJoinDto.class);
	}

	public Page<ActivityJoinDto> getActivityStudentsInfoPage(String activityId, PageRequest pageRequest) {
		StringBuilder querySql = new StringBuilder("select t.audit_status,v.student_id,t.join_dt,v.xm,v.avatar,");
		querySql.append("(case when v.xbm='1' then '男' when v.xbm='2' then '女' else '未知' end) as xb ");
		querySql.append(" from gjt_activity_join t left join gjt_student_info v on t.student_id = v.student_id");
		querySql.append(" WHERE t.activity_id=:activityId ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("activityId", activityId);
		Sort sort = new Sort(Sort.Direction.DESC, "t.join_dt");
		PageRequest pageable = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);

		return super.findByPageSql(querySql, params, pageable, ActivityJoinDto.class);
	}

	public Page<GjtActivityDto> getActivityByStudent(Map<String, Object> map, PageRequest pageRequest) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder querySql = new StringBuilder("select a.ID,a.ACTIVITY_ADDRESS,a.ACTIVITY_INTRODUCE,");
		querySql.append("a.ACTIVITY_PICTURE,a.ACTIVITY_TITLE,c.AUDIT_STATUS,a.BEGIN_TIME,");
		querySql.append("a.CEILING_NUM,a.CHARGE_MONEY,a.COMMENT_NUM,a.CREATED_BY,");
		querySql.append("a.CREATED_DT,a.END_TIME,a.IS_FREE,a.JOIN_NUM,a.PUBLICITY_PICTURE from gjt_activity a ");
		querySql.append(" inner join gjt_activity_join c on a.id=c.activity_id where 1=1 ");

		String studentId = (String) map.get("studentId");
		if (StringUtils.isNotBlank(studentId)) {
			querySql.append(" and c.student_id=:studentId");
			params.put("studentId", studentId);
		}

		String type = (String) map.get("type");
		if (StringUtils.isNotBlank(type)) {
			if ("ongoing".equals(type)) {// 进行中的活动
				querySql.append(" and a.end_time<=sysdate");
			} else if ("over".equals(type)) {// 已经结束的活动
				querySql.append(" and a.end_time>sysdate");
			}
		}

		String activityTitle = (String) map.get("activityTitle");
		if (StringUtils.isNotBlank(activityTitle)) {
			querySql.append(" and a.activity_title like :activityTitle");
			params.put("activityTitle", "%" + activityTitle + "%");
		}

		Sort sort = new Sort(Sort.Direction.DESC, "a.created_dt");
		PageRequest pageable = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);
		return super.findByPageSql(querySql, params, pageable, GjtActivityDto.class);
	}

	/**
	 * 批量审核活动
	 * 
	 * @param activityId
	 * @return
	 */
	@Transactional
	public void batchAuditActivity(String activityId, String auditStatus) {
		StringBuilder updateSql = new StringBuilder("update gjt_activity_join t set t.audit_status = 1");
		updateSql.append(" WHERE t.activity_id=:activityId and t.audit_status=:auditStatus");
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
	@Transactional
	public void batchAuditActivityBystudentId(String activityId, String auditStatus, String studentId) {
		StringBuilder updateSql = new StringBuilder("update gjt_activity_join t set t.audit_status = 1");
		updateSql
				.append(" WHERE t.activity_id=:activityId and t.audit_status=:auditStatus and t.student_id=:studentId");
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
	@Transactional
	public void batchAuditActivityBystudentIdtoUnpass(String activityId, String auditStatus, String studentId) {
		StringBuilder updateSql = new StringBuilder("update gjt_activity_join t set t.audit_status = 2");
		updateSql
				.append(" WHERE t.activity_id=:activityId and t.audit_status=:auditStatus and t.student_id=:studentId");
		Map<String, Object> params = new HashMap();
		Query q = em.createNativeQuery(updateSql.toString());
		q.setParameter("activityId", activityId);
		q.setParameter("auditStatus", auditStatus);
		q.setParameter("studentId", studentId);
		q.executeUpdate();
	}

}
