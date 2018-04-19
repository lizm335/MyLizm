/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.graduation;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.common.ResultFeedback;
import com.gzedu.xlims.pojo.graduation.GjtGraPlanFlowRecord;
import com.gzedu.xlims.pojo.graduation.GjtGraduationPlan;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 毕业计划业务逻辑<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年08月31日
 * @version 3.0
 * @since JDK 1.7
 */
public interface GjtGraduationPlanService extends BaseService<GjtGraduationPlan> {

	/**
	 * 分页查询毕业计划
	 * @param searchParams
	 * @param pageRequest
     * @return
     */
	Page<GjtGraduationPlan> queryGraduationPlanListByPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 统计审核状态数量
	 * @param searchParams
	 * @return
     */
	Map<String, Long> countGroupbyAuditState(Map<String, Object> searchParams);

	/**
	 * 更新毕业计划
	 * @param entity
	 * @return
     */
	boolean updateGraduationPlan(GjtGraduationPlan entity);

	/**
	 * 根据学期查找毕业计划
	 * @param termId
	 * @param xxId
     * @return
     */
	GjtGraduationPlan findByTermId(String termId, String xxId);

	/**
	 * 逻辑删除毕业计划
	 * @param id
	 * @param updatedBy
     * @return
     */
	boolean delete(String id, String updatedBy);

	//---------------------------------------- 毕业计划审核 -------------------------------------//

	/**
	 * 根据毕业计划ID获取毕业计划审核记录
	 * @param planId
	 * @return
	 */
	List<GjtGraPlanFlowRecord> queryGraPlanFlowRecordByPlanId(String planId);

	/**
	 * 初始化毕业计划审核记录
	 * @param planId
	 * @return
	 */
	boolean initAuditGraduationPlan(String planId);

	/**
	 * 审核毕业计划
	 * @param planId
	 * @param auditState
	 * @param auditContent
	 * @param operatorRole
	 * @param operatorRealName
	 * @return
	 */
	ResultFeedback auditGraduationPlan(String planId, Integer auditState, String auditContent, int operatorRole, String operatorRealName);

	GjtGraduationPlan queryByTermId(String currentGradeId);

}
