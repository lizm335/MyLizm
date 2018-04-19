/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.daoImpl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.StringUtils;
import com.ouchgzee.headTeacher.daoImpl.base.BaseDaoImpl;

/**
 * 留言反馈信息操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月31日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrFeedbackDaoImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
@Transactional(value="transactionManagerBzr", readOnly = true)
public class FeedbackDaoImpl extends BaseDaoImpl {

	private static Logger log = LoggerFactory.getLogger(FeedbackDaoImpl.class);

	/**
	 * 分页条件查询未解决的学员留言反馈信息，SQL语句
	 * 
	 * @param bzrId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<Map> findUnsolvedFeedBackByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder querySql = createSqlToFindUnsolvedFeedBack(searchParams, parameters);
		return super.findByPageToMap(querySql, parameters, pageRequest);
	}

	/**
	 * 根据条件查询未解决的学员留言反馈信息，SQL语句
	 * 
	 * @param bzrId
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	public List<Map> findAllUnsolvedFeedBack(String bzrId, Map<String, Object> searchParams, Sort sort) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder querySql = createSqlToFindUnsolvedFeedBack(searchParams, parameters);
		return super.findAllByToMap(querySql, parameters, sort);
	}

	/**
	 * 拼接SQL，查询未解决的学员留言反馈信息
	 * 
	 * @param bzrId
	 * @param searchParams
	 * @param parameters
	 * @return
	 */
	private StringBuilder createSqlToFindUnsolvedFeedBack(final Map<String, Object> searchParams,
			Map<String, Object> parameters) {
		StringBuilder querySql = new StringBuilder("select t.id,t.title,t.content,t.created_dt,");
		querySql.append(" t.imgurls,gsi.xm studentXm,gsi.avatar from gjt_class_info gci  ");
		querySql.append(" inner join gjt_class_student gcs on gcs.class_id = gci.class_id ");
		querySql.append(" inner join gjt_feedback t on gcs.student_id=t.created_by ");
		querySql.append(" inner join gjt_student_info gsi on gsi.student_id =gcs.student_id   ");
		querySql.append(" where gci.bzr_id = :bzrId and gci.class_id=:classId and gcs.is_deleted = 'N' ");
		querySql.append(" and gci.is_deleted = 'N'  and gsi.is_deleted = 'N' and (t.deal_result ='N'  ");
		querySql.append("  or t.deal_result is null) and t.type = 'feedback' ");

		parameters.put("bzrId", searchParams.get("bzrId"));
		parameters.put("classId", searchParams.get("classId"));

		if (StringUtils.isNotBlank(searchParams.get("title"))) {
			querySql.append(" AND t.title||t.content LIKE :title");
			parameters.put("title", "%" + searchParams.get("title") + "%");
		}
		// 提问时间
		try {
			if (StringUtils.isNotBlank(searchParams.get("createdDtBegin"))) {
				Date createdDtBegin = DateUtils.parseDate(searchParams.get("createdDtBegin") + " 00:00:00",
						"yyyy-MM-dd HH:mm:ss");
				querySql.append(" AND t.created_dt >= :createdDtBegin");
				parameters.put("createdDtBegin", createdDtBegin);
			}
			if (StringUtils.isNotBlank(searchParams.get("createdDtEnd"))) {
				Date createdDtEnd = DateUtils.parseDate(searchParams.get("createdDtEnd") + " 23:59:59",
						"yyyy-MM-dd HH:mm:ss");
				querySql.append(" AND t.created_dt <= :createdDtEnd");
				parameters.put("createdDtEnd", createdDtEnd);
			}
		} catch (ParseException e) {
			log.error("时间类型转化异常", e);
		}
		return querySql;
	}

	/**
	 * 统计班级未解决的留言反馈
	 * 
	 * @param searchParams
	 * @return
	 */
	public long countUnsolvedFeedBack(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder querySql = new StringBuilder("select count(*) from gjt_class_info gci ");
		querySql.append(" inner join gjt_class_student gcs on gcs.class_id = gci.class_id ");
		querySql.append(" inner join gjt_feedback t on gcs.student_id=t.created_by where ");
		querySql.append(" gci.bzr_id = :bzrId  and gci.class_id=:classId  and gcs.is_deleted = 'N' ");
		querySql.append(" and gci.is_deleted = 'N'  and (t.deal_result ='N' or t.deal_result is null) ");
		querySql.append(" and t.type = 'feedback'");
		parameters.put("bzrId", searchParams.get("bzrId"));
		parameters.put("classId", searchParams.get("classId"));

		return super.countBySql(querySql, parameters);
	}

	/**
	 * 统计班级已解决的留言反馈
	 * 
	 * @param bzrId
	 * @return
	 */
	public long countSolvedFeedBack(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder querySql = new StringBuilder("select  count(*) from gjt_class_info gci ");
		querySql.append(" inner join gjt_class_student gcs on gcs.class_id = gci.class_id ");
		querySql.append(" inner join gjt_feedback t on gcs.student_id=t.created_by ");
		querySql.append(" inner join gjt_student_info gsi on gsi.student_id =gcs.student_id ");
		querySql.append(" left join gjt_feedback c on t.id = c.pid and c.is_deleted = 'N' ");
		querySql.append(" where gci.bzr_id =:bzrId  and  gci.class_id=:classId and gcs.is_deleted = 'N'");
		querySql.append(" and gci.is_deleted = 'N'  and t.deal_result='Y'  and t.type = 'feedback'  ");

		parameters.put("bzrId", searchParams.get("bzrId"));
		parameters.put("classId", searchParams.get("classId"));
		return super.countBySql(querySql, parameters);
	}

	/**
	 * 分页条件查询已解决的学员留言反馈信息，SQL语句
	 * 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<Map> findSolvedFeedBackByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder querySql = createSqlToFindSolvedFeedBack(searchParams, parameters);
		return super.findByPageToMap(querySql, parameters, pageRequest);
	}

	/**
	 * 根据条件查询已解决的学员留言反馈信息，SQL语句
	 * 
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	public List<Map> findAllSolvedFeedBack(Map<String, Object> searchParams, Sort sort) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder querySql = createSqlToFindSolvedFeedBack(searchParams, parameters);
		return super.findAllByToMap(querySql, parameters, sort);
	}

	/**
	 * 拼接SQL，查询学员已解决的学员留言反馈信息
	 * 
	 * @param searchParams
	 * @param parameters
	 * @return
	 */
	private StringBuilder createSqlToFindSolvedFeedBack(final Map<String, Object> searchParams,
			Map<String, Object> parameters) {
		StringBuilder querySql = new StringBuilder(" SELECT  t.id,t.title,t.content,t.created_dt,gsi.xm studentXm,");
		querySql.append(" gsi.avatar, c.content answerContent,c.deal_dt answerDealDt,t.imgurls studentimgurls, ");
		querySql.append(" c.imgurls teacherimgurls,c.created_by employeeId from gjt_class_info gci ");
		querySql.append(" inner join gjt_class_student gcs on gcs.class_id = gci.class_id ");
		querySql.append(" inner join gjt_feedback t on gcs.student_id=t.created_by ");
		querySql.append(" inner join gjt_student_info gsi on gsi.student_id =GCS.Student_Id");
		querySql.append(" left join gjt_feedback c on t.id = c.pid and c.is_deleted = 'N'");
		querySql.append(" where gci.bzr_id =:bzrId  and  gci.class_id=:classId");
		querySql.append(" and gcs.is_deleted = 'N' and gci.is_deleted = 'N'  and gsi.is_deleted = 'N' ");
		querySql.append(" and t.deal_result='Y'   and t.type = 'feedback'   ");

		parameters.put("bzrId", searchParams.get("bzrId"));
		parameters.put("classId", searchParams.get("classId"));

		if (StringUtils.isNotBlank(searchParams.get("title"))) {
			querySql.append(" AND t.title||t.content LIKE :title");
			parameters.put("title", "%" + searchParams.get("title") + "%");
		}
		// 提问时间
		try {
			if (StringUtils.isNotBlank(searchParams.get("createdDtBegin"))) {
				Date createdDtBegin = DateUtils.parseDate(searchParams.get("createdDtBegin") + " 00:00:00",
						"yyyy-MM-dd HH:mm:ss");
				querySql.append(" AND t.created_dt >= :createdDtBegin");
				parameters.put("createdDtBegin", createdDtBegin);
			}
			if (StringUtils.isNotBlank(searchParams.get("createdDtEnd"))) {
				Date createdDtEnd = DateUtils.parseDate(searchParams.get("createdDtEnd") + " 23:59:59",
						"yyyy-MM-dd HH:mm:ss");
				querySql.append(" AND t.created_dt <= :createdDtEnd");
				parameters.put("createdDtEnd", createdDtEnd);
			}
		} catch (ParseException e) {
			log.error("时间类型转化异常", e);
		}
		return querySql;
	}

}
