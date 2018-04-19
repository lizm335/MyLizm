/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtRulesClass;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtRulesClassService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2017年2月15日
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/edumanage/rulesClass")
public class GjtRulesClassController {
	@Autowired
	GjtRulesClassService gjtRulesClassService;
	
	@Autowired
	GjtGradeService gjtGradeService;
	
	@Autowired
	GjtClassInfoService gjtClassInfoService;
	
	@Autowired
	CommonMapService commonMapService;
	
	@Autowired
	GjtOrgService gjtOrgService;

	// 跳转到设置自动分班规则
	@RequestMapping(value = "autoDivideClass", method = RequestMethod.GET)
	public String autoDivideClass(HttpServletRequest request, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtRulesClass gjtRulesClass = gjtRulesClassService.queryById(user.getGjtOrg().getId());
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("info", gjtRulesClass);
		model.addAttribute("gradeMap", gradeMap);
		return "/edumanage/courseclass/autoDivideClass";
	}

	@ResponseBody
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public Feedback update(@Valid GjtRulesClass entity, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		entity.setXxid(user.getGjtOrg().getId());
		try {
			gjtRulesClassService.update(entity);
			return new Feedback(true, "分班规则设置成功");
		} catch (Exception e) {
			return new Feedback(false, "分班规则设置失败");
		}
	}

	/**
	 * 跳转到教学班分班规则页面
	 * @param gradeId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "setRule", method = RequestMethod.GET)
	public String setRule(String gradeId,HttpServletRequest request, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<GjtRulesClass> gjtRulesClass=null;
		List<GjtRulesClass> rulesClassCenter=null;
		if(request.getParameter("gradeId") == null){
			String currentGradeId = gjtGradeService.getCurrentGradeId(user.getGjtOrg().getId());
			//查询是否有按学习中心分班的数据
			rulesClassCenter=gjtRulesClassService.findByOrgIdAndGradeIdAndPointClassType(user.getGjtOrg().getId(),currentGradeId,"1");
			if(rulesClassCenter!=null&&rulesClassCenter.size()>0){
				model.addAttribute("rulesClassCenter", rulesClassCenter);
				model.addAttribute("centerPointType", rulesClassCenter.get(0).getPointClassType());
				model.addAttribute("centerPointNum", rulesClassCenter.get(0).getPointClassNum());
			}
			//查询是否有按年级专业层次总体分班的数据
			gjtRulesClass=gjtRulesClassService.findByOrgIdAndGradeIdAndPointClassType(user.getGjtOrg().getId(),currentGradeId,"2");
			if(gjtRulesClass!=null&&gjtRulesClass.size()>0){
				model.addAttribute("rulesClass", gjtRulesClass.get(0));
			}
			model.addAttribute("currentGradeId", currentGradeId);
		}
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		model.addAttribute("gradeMap", gradeMap);		
		return "/edumanage/courseclass/classRule";
	}
	
	/**
	 * 根据年级动态查询教学班分班规则内容
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryGjtRuleInfo", method = RequestMethod.GET)
	public Map<String, Object> queryGjtRuleInfo(Model model,HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		Map<String,Object> resultMap=new HashMap();
		//查询是否有按学习中心分班的数据
		List<GjtRulesClass> rulesClassCenter=gjtRulesClassService.findByOrgIdAndGradeIdAndPointClassType(user.getGjtOrg().getId(),searchParams.get("gradeId").toString(),"1");
		//查询是否有按年级专业层次总体分班的数据
		List<GjtRulesClass> gradeIdPointClass=gjtRulesClassService.findByOrgIdAndGradeIdAndPointClassType(user.getGjtOrg().getId(),searchParams.get("gradeId").toString(),"2");	
		if(rulesClassCenter!=null&&rulesClassCenter.size()>0){
			Map<String,Object> studyCenterMap=null;
			List<Map<String,Object>> studyCenterList = new ArrayList<Map<String,Object>>();
			for(int i=0;i<rulesClassCenter.size();i++){
				studyCenterMap=new HashMap<String,Object>();
				studyCenterMap.put("code", rulesClassCenter.get(i).getGjtStudyCenter().getGjtOrg().getCode());
				studyCenterMap.put("xxzxId", rulesClassCenter.get(i).getGjtStudyCenter().getGjtOrg().getId());
				studyCenterMap.put("orgName", rulesClassCenter.get(i).getGjtStudyCenter().getGjtOrg().getOrgName());
				studyCenterList.add(studyCenterMap);
			}
			resultMap.put("studyCenterMap", studyCenterList);
			resultMap.put("centerPointType", rulesClassCenter.get(0).getPointClassType());
			resultMap.put("centerPointNum", rulesClassCenter.get(0).getPointClassNum());
		}
		if(gradeIdPointClass!=null&&gradeIdPointClass.size()>0){
			resultMap.put("isDataInfo", true);
			resultMap.put("gradePointType", gradeIdPointClass.get(0).getPointClassType());
			resultMap.put("gradePointNum", gradeIdPointClass.get(0).getPointClassNum());
		}
		return resultMap;
	}
	/**
	 * 保存教学班分班规则
	 * @param entity
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addRule", method = RequestMethod.POST)
	public Feedback addRule(@Valid GjtRulesClass entity, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String pointClassNum1=request.getParameter("pointClassNum1");//按学习中心分班的人数上限
		String pointClassNum2=request.getParameter("pointClassNum2");//按年级专业层次总体分班的人数上限
		try {
			//修改分班规则
			List<GjtRulesClass> gjtRulesClass=gjtRulesClassService.findByOrgIdAndGradeId(user.getGjtOrg().getId(),entity.getGradeId());
			if(gjtRulesClass!=null&&gjtRulesClass.size()>0){
				if(entity.getXxzxId()!=null){
					String[] studyCenterId=entity.getXxzxId().split(",");
					for(String xxzxId:studyCenterId){
						//查询是否有按学习中心分班的数据
						GjtRulesClass center=gjtRulesClassService.findByOrgIdAndGradeIdAndPointClassTypeAndXxzxId(user.getGjtOrg().getId(),entity.getGradeId(),"1",xxzxId);
						if(center!=null){
							if(!"".equals(pointClassNum1)){
								center.setPointClassNum(Integer.parseInt(pointClassNum1));
								gjtRulesClassService.update(center);
							}	
						}else{
							if(!"".equals(pointClassNum1)){
								entity.setPointClassNum(Integer.parseInt(pointClassNum1));
							}else{
								entity.setPointClassNum(500);//默认为500人
							}
							entity.setXxid(UUIDUtils.random());
							entity.setXxzxId(xxzxId);								
							entity.setOrgId(user.getGjtOrg().getId());
							entity.setPointClassType("1");						
							gjtRulesClassService.update(entity);
						}
					}				
				}
				//查询是否有按年级专业层次总体分班的数据
				List<GjtRulesClass> gradeIdPointClass=gjtRulesClassService.findByOrgIdAndGradeIdAndPointClassType(user.getGjtOrg().getId(),entity.getGradeId(),"2");	
				if(gradeIdPointClass!=null&&gradeIdPointClass.size()>0){
					GjtRulesClass ruleClass=gradeIdPointClass.get(0);
					if(!"".equals(pointClassNum2)){
						ruleClass.setPointClassNum(Integer.parseInt(pointClassNum2));
					}
					gjtRulesClassService.update(ruleClass);
				}
			}else{						
				//新增【按学习中心分班】分班规则				
				if(!"".equals(entity.getXxzxId())&&entity.getXxzxId()!=null){
					String[] studyCenterId=entity.getXxzxId().split(",");
					for(String xxzxId:studyCenterId){
						entity.setXxzxId(xxzxId);
						entity.setXxid(UUIDUtils.random());
						entity.setOrgId(user.getGjtOrg().getId());
						entity.setPointClassType("1");
						if(!"".equals(pointClassNum1)){
							entity.setPointClassNum(Integer.parseInt(pointClassNum1));
						}else{
							entity.setPointClassNum(500);//默认为500人
						}
						gjtRulesClassService.update(entity);
					}					
				}
				//新增【按年级专业层次总体分班】分班规则
				if(!"".equals(pointClassNum2)){
					entity.setPointClassNum(Integer.parseInt(pointClassNum2));
				}else{
					entity.setPointClassNum(500);//默认为500人
				}
				gjtRulesClassService.insertPyccGjtRuleClass(entity,user);	
			}							
			return new Feedback(true, "分班规则设置成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Feedback(false, "分班规则设置失败"+e.getMessage());
		}
	}

	/**
	 * 跳转到设置课程班自动分班规则
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "autoCourseClass", method = RequestMethod.GET)
	public String autoCourseClass(HttpServletRequest request, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtRulesClass gjtRulesClass = gjtRulesClassService.queryCourseClassRule(user.getGjtOrg().getId());
		model.addAttribute("info", gjtRulesClass);
		
		return "/edumanage/courseclass/autoCourseClass";
	}
	/**
	 * 指定分班规则的学习中心
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "addStudyCenter", method = RequestMethod.GET)
	public String addStudyCenter(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		//查询院校下已启用的学习中心
		searchParams.put("orgId", user.getGjtOrg().getId());
		Page<Map<String, Object>> pageInfo = gjtOrgService.queryAllStudyCenter(searchParams, pageRequst);
		Map<String, String> studyServiceInfoMap = commonMapService.getDates("StudyServiceInfo");
		for (Map<String, Object> gjtOrg : pageInfo) {
			String serviceArea = (String) gjtOrg.get("SERVICE_AREA");// 拥有的服务项
			List<String> serviceList = new ArrayList<String>();
			if (StringUtils.isNotBlank(serviceArea)) {
				for (String str : serviceArea.split(",")) {
					serviceList.add(studyServiceInfoMap.get(str));
				}
				gjtOrg.put("serviceList", serviceList);
			}
		}
		model.addAttribute("pageInfo", pageInfo);
		return "/edumanage/courseclass/class_add_studyCenter";
	}
}
