package com.gzedu.xlims.dao.myplan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 我的计划
 * Created by llx on 2017/8/17.
 */
public interface MyPlanDao {
    /**
     * 我的计划--计划列表查询
     * @param searchParams
     * @param pageRequest
     * @return
     */
    Page getPlanListPage(Map<String, Object> searchParams, PageRequest pageRequest);

    /**
     * 我的计划--计划列表统计
     * @param searchParams
     * @return
     */
    int getPlanCount(Map<String, Object> searchParams);

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
     * 我的计划--查看计划-备注列表
     * @param searchParams
     * @return
     */
    List<Map<String,String>> getPlanRemark(Map<String, Object> searchParams);

    /**
     * 我的计划--开始计划
     * @param searchParams
     * @return
     */
    int startPlan(Map<String, Object> searchParams);

    /**
     * 我的计划-完成计划
     * @param searchParams
     * @return
     */
    int finishPlan(Map<String, Object> searchParams);

    /**
     * 我的计划-增加备注
     * @param searchParams
     * @return
     */
    int addPlanRemark(Map<String, Object> searchParams);

    /**
     * 我的计划--删除计划
     * @param searchParams
     * @return
     */
    int deletePlan(Map<String, Object> searchParams);

    /**
     * 我的计划--删除备注
     * @param searchParams
     * @return
     */
    int deletePlanRemark(Map<String, Object> searchParams);

    /**
     * 我的计划--修改备注
     * @param searchParams
     * @return
     */
    boolean editPlanRemark(Map<String, Object> searchParams);
}
