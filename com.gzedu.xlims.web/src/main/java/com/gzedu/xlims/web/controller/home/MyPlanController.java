package com.gzedu.xlims.web.controller.home;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.myplan.MyPlanService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by llx on 2017/8/17.
 */
@Controller
@RequestMapping("/myPlan")
public class MyPlanController extends BaseController{

    @Autowired
    private MyPlanService myPlanService;

    /**
     * 我的计划--列表
     * @param pageNumber
     * @param pageSize
     * @param model
     * @param request
     * @return
     */
       @RequestMapping(value = "/planList",method = RequestMethod.GET)
       public String planList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                              @RequestParam(value = "page.size", defaultValue = "5") int pageSize, Model model,
                              HttpServletRequest request){
           Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
           GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
           searchParams.put("USER_ID", ObjectUtils.toString(user.getId(),""));


           PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize);
           Page pageInfo = myPlanService.getPlanListPage(searchParams,pageRequest);
           model.addAttribute("pageInfo",pageInfo);
           return "/myplan/plan_list";
       }

    /**
     * 工作台获取我的计划
     * @param pageNumber
     * @param pageSize
     * @param model
     * @param request
     * @return
     */
       @RequestMapping(value = "/getPlan",method = RequestMethod.POST)
       public String getPlan(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                             @RequestParam(value = "page.size", defaultValue = "3") int pageSize, Model model,
                             HttpServletRequest request){
           Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
           GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
           searchParams.put("USER_ID", ObjectUtils.toString(user.getId(),""));
           PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize);
           Page pageInfo = myPlanService.getPlanListPage(searchParams,pageRequest);

           model.addAttribute("plan",pageInfo.getContent());
           return "/myplan/index_plan_view";
       }


    /**
     * 我的计划--异步获取计划数目统计项
     * @param request
     * @return
     */
       @ResponseBody
       @RequestMapping(value = "/getPlanCount",method = RequestMethod.POST)
       public Feedback getPlanCount(HttpServletRequest request){
           Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
           GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
           searchParams.put("USER_ID", ObjectUtils.toString(user.getId(),""));
           Feedback feedback = new Feedback();
           try {
               Map<String,Object> result =  myPlanService.getPlanCount(searchParams);
               feedback.setSuccessful(true);
               feedback.setMessage("success");
               feedback.setObj(result);
           }catch (Exception e){
               e.printStackTrace();
               feedback.setSuccessful(false);
               feedback.setMessage("fail");
           }

           return feedback;
       }


    /**
     * 我的计划--新增计划get
     * @param request
     * @return
     */
       @RequestMapping(value = "/createPlan",method = RequestMethod.GET)
       public String createPlan(HttpServletRequest request){

           return "/myplan/plan_create_page";
       }

    /**
     * 我的计划--新增计划post
     * @param request
     * @return
     */
    @RequestMapping(value = "/createPlan",method = RequestMethod.POST)
    public String savePlan(HttpServletRequest request){
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        searchParams.put("USER_ID", ObjectUtils.toString(user.getId(),""));
        boolean flag = myPlanService.createPlan(searchParams);
        if(flag){
            return "redirect:/myPlan/planList";
        }else {
            return "/myplan/plan_create_page";//创建失败
        }
    }

    /**
     * 我的计划--查看计划
     * @param request
     * @return
     */
    @RequestMapping(value = "/viewPlan/{plan_id}",method = RequestMethod.GET)
    public String viewPlan(HttpServletRequest request,@PathVariable("plan_id") String plan_id,Model model){
        Map<String, Object> searchParams = new HashMap<String, Object>();
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        searchParams.put("PLAN_ID",plan_id);
        searchParams.put("USER_ID", ObjectUtils.toString(user.getId(),""));
        Map<String,Object> result = myPlanService.getPlan(searchParams);
        model.addAttribute("result",result);
        return "/myplan/plan_view";
    }

    /**
     * 我的计划--删除计划
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deletePlan/{plan_id}",method = RequestMethod.POST)
    public Feedback deletePlan(HttpServletRequest request,@PathVariable("plan_id") String plan_id){
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        Map<String,Object> searchParams = new HashMap<String, Object>();
        searchParams.put("PLAN_ID",plan_id);
        searchParams.put("USER_ID", ObjectUtils.toString(user.getId(),""));
        Feedback feedback = new Feedback();
        boolean flag = myPlanService.deletePlan(searchParams);
        if(flag){
            feedback.setSuccessful(true);
            feedback.setMessage("success");
        }else {
            feedback.setSuccessful(false);
            feedback.setMessage("fail");
        }
        return feedback;
    }


    /**
     * 我的计划--开始计划
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/startPlan/{plan_id}",method = RequestMethod.POST)
    public Feedback startPlan(HttpServletRequest request, @PathVariable("plan_id") String plan_id){
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        Map<String,Object> searchParams = new HashMap<String, Object>();
        searchParams.put("START_DATE", DateUtil.toString(new Date(),"yyyy-MM-dd HH:mm"));
        searchParams.put("PLAN_ID",plan_id);
        searchParams.put("USER_ID", ObjectUtils.toString(user.getId(),""));
        Feedback feedback = new Feedback();
        boolean flag = myPlanService.startPlan(searchParams);
        if(flag){
            feedback.setSuccessful(true);
            feedback.setMessage("success");
        }else {
            feedback.setSuccessful(false);
            feedback.setMessage("fail");
        }
        return feedback;
    }

    /**
     * 我的计划--完成计划
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/finishPlan/{plan_id}",method = RequestMethod.POST)
    public Feedback finishPlan(HttpServletRequest request,@PathVariable("plan_id") String plan_id){
        Map<String, Object> searchParams = new HashMap<String, Object>();
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        searchParams.put("PLAN_ID",plan_id);
        searchParams.put("USER_ID", ObjectUtils.toString(user.getId(),""));
        searchParams.put("END_DATE", DateUtil.toString(new Date(),"yyyy-MM-dd HH:mm"));
        Feedback feedback = new Feedback();
        boolean flag = myPlanService.finishPlan(searchParams);
        if(flag){
            feedback.setSuccessful(true);
            feedback.setMessage("success");
        }else {
            feedback.setSuccessful(false);
            feedback.setMessage("fail");
        }

        return feedback;
    }


    /**
     * 我的计划--新增备注
     * @param request
     * @param plan_id
     * @param remark_content
     * @return
     */
    @RequestMapping(value = "/addPlanRemark/{plan_id}",method = RequestMethod.POST)
    public String addPlanRemark(HttpServletRequest request,@PathVariable("plan_id") String plan_id,@RequestParam("REMARK_CONTENT") String remark_content){
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        Map<String,Object> searchParams = new HashMap<String, Object>();
        searchParams.put("USER_ID", ObjectUtils.toString(user.getId(),""));
        searchParams.put("PLAN_ID",plan_id);
        searchParams.put("REMARK_CONTENT",remark_content);

        boolean flag = myPlanService.addPlanRemark(searchParams);
        if(flag){

        }else {

        }
        return "redirect:/myPlan/viewPlan/{plan_id}";
    }

    /**
     * 我的计划--修改备注
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editPlanRemark",method = RequestMethod.POST)
    public Feedback editPlanRemark(HttpServletRequest request){
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
        searchParams.put("USER_ID", ObjectUtils.toString(user.getId(),""));
        Feedback feedback = new Feedback();
        boolean flag = myPlanService.editPlanRemark(searchParams);
        if(flag){
            feedback.setSuccessful(true);
            feedback.setMessage("success");
        }else {
            feedback.setSuccessful(false);
            feedback.setMessage("fail");
        }
        return feedback;
    }

    /**
     * 我的计划--删除计划备注根据remark_id
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deletePlanRemark/{remark_id}",method = RequestMethod.POST)
    public Feedback deletePlanRemark(HttpServletRequest request,@PathVariable("remark_id") String remark_id){
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        Map<String,Object> searchParams = new HashMap<String, Object>();
        searchParams.put("REMARK_ID",remark_id);
        searchParams.put("USER_ID", ObjectUtils.toString(user.getId(),""));
        Feedback feedback = new Feedback();
        boolean flag = myPlanService.deletePlanRemark(searchParams);
        if(flag){
            feedback.setSuccessful(true);
            feedback.setMessage("success");
        }else {
            feedback.setSuccessful(false);
            feedback.setMessage("fail");
        }
        return feedback;
    }


}

















