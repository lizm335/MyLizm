package com.gzedu.xlims.serviceImpl.myplan;

import com.gzedu.xlims.dao.myplan.MyPlanDao;
import com.gzedu.xlims.service.myplan.MyPlanService;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by llx on 2017/8/17.
 */
@Service
public class MyPlanServiceImpl implements MyPlanService{

    @Autowired
    private MyPlanDao myPlanDao;

    /**
     * 我的计划-计划列表查询
     *
     * @param searchParams
     * @param pageRequest
     * @return
     */
    @Override
    public Page getPlanListPage(Map<String, Object> searchParams, PageRequest pageRequest) {
        return myPlanDao.getPlanListPage(searchParams,pageRequest);
    }

    /**
     * 我的计划--计划列表统计项
     *
     * @param searchParams
     * @return
     */
    @Override
    public Map<String,Object> getPlanCount(Map<String, Object> searchParams) {
        String[] statuses = ObjectUtils.toString(searchParams.get("STATUS"), "").split(",");//全部状态的数组
        Map<String,Object> resultMap = new HashMap<String,Object>();
        if(statuses!=null&&statuses.length>0){
            for (String status:statuses){
                searchParams.put("PLAN_STATUS",status);
                int result = myPlanDao.getPlanCount(searchParams);
                if("".equals(status)){
                    resultMap.put("all",result);
                }else if ("0".equals(status)){
                    resultMap.put("unstart",result);
                }else if ("1".equals(status)){
                    resultMap.put("starting",result);
                }else if ("2".equals(status)){
                    resultMap.put("finish",result);
                }
            }
        }
        return resultMap;
    }

    /**
     * 我的计划--新建计划
     *
     * @param searchParams
     * @return
     */
    @Override
    @Transactional
    public boolean createPlan(Map<String, Object> searchParams) {

        boolean flag =false;
        try {
            flag = myPlanDao.createPlan(searchParams);
        }catch (Exception e){
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 我的计划--查看计划
     *
     * @param searchParams
     * @return
     */
    @Override
    public Map<String, Object> getPlan(Map<String, Object> searchParams) {
        Map<String,Object> resultMap = myPlanDao.getPlan(searchParams);
        List<Map<String,String>> list = myPlanDao.getPlanRemark(searchParams);
        resultMap.put("REMARK_LIST",list);
        return resultMap;
    }

    /**
     * 我的计划--开始计划
     *
     * @param searchParams
     * @return
     */
    @Override
    @Transactional
    public boolean startPlan(Map<String, Object> searchParams) {
        int num = myPlanDao.startPlan(searchParams);
        if(num>0){
            return true;
        }
        return false;
    }

    /**
     * 我的计划--结束计划
     *
     * @param searchParams
     * @return
     */
    @Override
    @Transactional
    public boolean finishPlan(Map<String, Object> searchParams) {
        int num = myPlanDao.finishPlan(searchParams);
        if(num>0){
            return true;
        }
        return false;
    }

    /**
     * 我的计划--增加备注
     *
     * @param searchParams
     * @return
     */
    @Override
    @Transactional
    public boolean addPlanRemark(Map<String, Object> searchParams) {
        int num = myPlanDao.addPlanRemark(searchParams);
        if(num>0){
            return true;
        }
        return false;
    }

    /**
     * 我的计划--删除计划
     *
     * @param searchParams
     * @return
     */
    @Override
    @Transactional
    public boolean deletePlan(Map<String, Object> searchParams) {
        boolean flag = false;
        int num = myPlanDao.deletePlan(searchParams);
        int num2 = myPlanDao.deletePlanRemark(searchParams);
        if(num>0){
            flag = true;
        }
        return flag;
    }

    /**
     * 我的计划--修改备注
     *
     * @param searchParams
     * @return
     */
    @Override
    @Transactional
    public boolean editPlanRemark(Map<String, Object> searchParams) {
        return myPlanDao.editPlanRemark(searchParams);
    }

    /**
     * 我的计划--删除计划备注根据remark_id
     *
     * @param searchParams
     * @return
     */
    @Override
    public boolean deletePlanRemark(Map<String, Object> searchParams) {
        int num = myPlanDao.deletePlanRemark(searchParams);
        if(num>0){
            return true;
        }else {
            return false;
        }
    }
}




























