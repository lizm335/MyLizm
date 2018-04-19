/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.signup;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.gzedu.xlims.pojo.GjtRollPlan;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 学籍计划业务逻辑<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年05月05日
 * @version 2.5
 * @since JDK 1.7
 */
public interface GjtRollPlanService extends BaseService<GjtRollPlan> {

	Map<String, BigDecimal> countGroupbyAuditState(Map<String, Object> searchParams);

	/**
	 * 更新学籍计划
	 * @param entity
	 * @return
     */
	boolean updateRollPlan(GjtRollPlan entity);

	/**
	 * 审核学籍计划
	 * @param id
	 * @param auditState
	 * @param auditContent
	 * @param auditOperator
	 * @param updatedBy
	 * @return
	 */
	boolean auditRollPlan(String id, BigDecimal auditState, String auditContent, String auditOperator, String updatedBy);
	

	List<GjtRollPlan> findTermRollPlanList(String xxId, String gradeId);
}
