/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.daoImpl;

import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学籍计划操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年05月06日
 * @version 2.5
 * @since JDK 1.7
 */
@Repository
@Transactional(readOnly = true)
public class RollPlanDaoImpl extends BaseDaoImpl {

	private static Logger log = LoggerFactory.getLogger(RollPlanDaoImpl.class);

	/**
	 * 统计审核状态
	 * 
	 * @param searchParams
	 * @return
	 */
	public List<Map> countGroupbyAuditState(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder(
				"SELECT T.AUDIT_STATE,COUNT(*) PLAN_NUM");
		querySql.append(" FROM GJT_ROLL_PLAN T");
		querySql.append(" WHERE T.IS_DELETED='N'");

		conditionJoin(searchParams, querySql, parameters);
		querySql.append(
				" GROUP BY T.AUDIT_STATE");
		long beginTime = System.currentTimeMillis();
		try {
			return super.findAllByToMap(querySql, parameters, null);
		} finally {
			// 计算执行当前sql耗时时长
			log.info(String.format("function countGroupbyAuditState select use time:%1$sms, sql:%2$s, parameters:%3$s",
					System.currentTimeMillis() - beginTime,
					querySql,
					parameters));
		}
	}

	/**
	 * 查询条件拼接
	 * 
	 * @param searchParams
	 * @param querySql
	 * @param parameters
	 */
	private void conditionJoin(Map<String, Object> searchParams, StringBuilder querySql,
			Map<String, Object> parameters) {
		// 院校ID
		if (StringUtils.isNotBlank(searchParams.get("EQ_xxId"))) {
			querySql.append(" AND T.XX_ID = :xxId");
			parameters.put("xxId", searchParams.get("EQ_xxId"));
		}
		// 计划编号
		if (StringUtils.isNotBlank(searchParams.get("EQ_rollPlanNo"))) {
			querySql.append(" AND T.ROLL_PLAN_NO = :rollPlanNo");
			parameters.put("rollPlanNo", searchParams.get("EQ_rollPlanNo"));
		}
		// 年级
		if (StringUtils.isNotBlank(searchParams.get("EQ_gjtGrade.gradeId"))) {
			querySql.append(" AND T.GRADE_ID = :gradeId");
			parameters.put("gradeId", searchParams.get("EQ_gjtGrade.gradeId"));
		}
	}

}
