package com.gzedu.xlims.service.myplan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map; /**
 * 我的计划service
 * Created by llx on 2017/8/17.
 */
public interface MyPlanService {

    /**
     * 我的计划-计划列表查询
     * @param searchParams
     * @param pageRequest
     * @return
     */
    Page getPlanListPage(Map<String, Object> searchParams, PageRequest pageRequest);

    /**
     * 我的计划--计划列表统计项
     * @param searchParams
     * @return
     */
    Map<String,Object> getPlanCount(Map<String, Object> searchParams);

    /**
     * 我的计划--新建计划
     * @param searchParams
     * @return
     */
    boolean createPlan(Map<String, Object> searchParams);

    /**
     * 我的计划--查看计划
     * @param searchParams
     * @return
     */
    Map<String,Object> getPlan(Map<String, Object> searchParams);

    /**
     * 我的计划--开始计划
     * @param searchParams
     * @return
     */
    boolean startPlan(Map<String, Object> searchParams);

    /**
     * 我的计划--结束计划
     * @param searchParams
     * @return
     */
    boolean finishPlan(Map<String, Object> searchParams);

    /**
     * 我的计划--增加备注
     * @param searchParams
     * @return
     */
    boolean addPlanRemark(Map<String, Object> searchParams);

    /**
     * 我的计划--删除计划
     * @param searchParams
     * @return
     */
    boolean deletePlan(Map<String, Object> searchParams);

    /**
     * 我的计划--修改备注
     * @param searchParams
     * @return
     */
    boolean editPlanRemark(Map<String, Object> searchParams);

    /**
     * 我的计划--删除计划备注根据remark_id
     * @param searchParams
     * @return
     */
    boolean deletePlanRemark(Map<String, Object> searchParams);
}
