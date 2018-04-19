package com.gzedu.xlims.webservice.controller.gjt.examManagment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.exam.GjtExamPlanNewService;
import com.gzedu.xlims.webservice.common.Servlets;

/**
 * 功能说明：企业大学接口--查询考试管理列表
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年8月7日
 * @version 2.5
 */
@RestController
@RequestMapping("/interface/examManagment")
public class ExamManagmentController {
	
	@Autowired
	private CommonMapService commonMapService;
	
	@Autowired
	private GjtExamPlanNewService gjtExamPlanNewService;
	
	/**
	 * 考试管理列表
	 * @return
	 */
	@RequestMapping("/getExamManagmentList")
	@ResponseBody
	public Map<String, Object> getExamManagmentList(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,HttpServletRequest request){
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		Map resultMap = new LinkedHashMap();
        String xxzx_id = ObjectUtils.toString(searchParams.get("XXZX_ID"),"").trim();
        String xxzx_code = ObjectUtils.toString(searchParams.get("XXZX_CODE"),"").trim();
        if (EmptyUtils.isEmpty(xxzx_id)&&EmptyUtils.isEmpty(xxzx_code)){
            resultMap.put("result","0");
            resultMap.put("message","XXZX_ID或XXZX_CODE不能为空！");
            return resultMap;
        }
        //根据学习中心查询院校信息
        if (EmptyUtils.isNotEmpty(xxzx_id)){
            Map<String, String> schoolMap = commonMapService.getxxIdByxxzxId(ObjectUtils.toString(searchParams.get("XXZX_ID")));
            if(EmptyUtils.isNotEmpty(schoolMap)){
                searchParams.put("XX_ID",schoolMap.keySet().iterator().next());
            }
        }
        //根据学习中心CODE查询院校信息
        if(EmptyUtils.isNotEmpty(xxzx_code)){
        	 Map<String, String> schoolMap = commonMapService.getxxIdByCode(ObjectUtils.toString(searchParams.get("XXZX_CODE")));
        	 if(EmptyUtils.isNotEmpty(schoolMap)){
                 searchParams.put("XX_ID",schoolMap.keySet().iterator().next());
             }
        }
        try{
        	 PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
             Page<Map<String, Object>> pageInfo=gjtExamPlanNewService.getExamManagmentList(searchParams,pageRequst);       
            if(EmptyUtils.isEmpty(pageInfo.getContent())){
            	resultMap.put("result","2");
            	resultMap.put("message", "暂无数据！");           	
            }else{
            	//因为一个考试科目对应有多个专业，所以需要把多个专业封装成一个对象
                for (Map<String, Object> examPlan : pageInfo.getContent()) {
                	List<Map<String,String>> specialtyList = new ArrayList<Map<String, String>>();
                	String[] zymcStr=ObjectUtils.toString(examPlan.get("ZYMC")).split(",");
                	Map zymcMap=new HashMap();
                	for(String zymc:zymcStr){
                		zymcMap.put("ZYMC", zymc);
                		specialtyList.add(zymcMap);            		
                	}
                	examPlan.remove("ZYMC");
                	examPlan.put("specialtyList", specialtyList);
                }
                resultMap.put("pageInfo", pageInfo);
            }                         
        }catch(Exception e){
        	e.printStackTrace();
        	resultMap.put("result","2");
        	resultMap.put("message", "接口数据异常");
        }      
		return resultMap;
	}
	/**
	 * 查询排考记录
	 * @param request
	 * @return
	 */
	 @RequestMapping(value = "/getArrangeExamRecord")
	 @ResponseBody
	 public Map<String, Object> studyManagmentDetails(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
				@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,HttpServletRequest request){
		 Map resultMap = new LinkedHashMap();
		 Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		 if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))&&EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("XXZX_CODE")))){
	            resultMap.put("result","0");
	            resultMap.put("message","XXZX_ID或XXZX_CODE不能为空！");
	            return resultMap;
	        }		
		 //考试批次
		 if(EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))){
			 resultMap.put("result","0");
	         resultMap.put("message","EXAM_BATCH_CODE不能为空！");
	         return resultMap;
	      }
		 //根据学习中心查询院校信息
        if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))){
            Map<String, String> schoolMap = commonMapService.getxxIdByxxzxId(ObjectUtils.toString(searchParams.get("XXZX_ID")));
            if(EmptyUtils.isNotEmpty(schoolMap)){
                searchParams.put("XX_ID",schoolMap.keySet().iterator().next());
            }
        }
        //根据学习中心CODE查询院校信息
        if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_CODE")))){
        	 Map<String, String> schoolMap = commonMapService.getxxIdByCode(ObjectUtils.toString(searchParams.get("XXZX_CODE")));
        	 if(EmptyUtils.isNotEmpty(schoolMap)){
                 searchParams.put("XX_ID",schoolMap.keySet().iterator().next());
             }
        }
        try {
        	PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
        	Page<Map<String, Object>> pageInfo=gjtExamPlanNewService.getArrangeExamRecordList(searchParams,pageRequst);
        	resultMap.put("pageInfo", pageInfo);
        } catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result","2");
        	resultMap.put("message", "接口数据异常");
		}
		 return resultMap;
	 }
	 /**
	  * 查看学员的准考证信息
	  * @param request
	  * @return
	  */
	 @RequestMapping(value = "/getStudentAdmissionTicket")
	 @ResponseBody
	 public Map<String,Object> getStudentAdmissionTicket(HttpServletRequest request){
		 Map resultMap = new LinkedHashMap();
		 Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		 if(EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("STUDENT_ID")))){
			 resultMap.put("result","0");
	         resultMap.put("message","STUDENT_ID不能为空！");
	         return resultMap;
	      }
		 //考试科目ID
		 if(EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")))){
			 resultMap.put("result","0");
	         resultMap.put("message","EXAM_PLAN_ID不能为空！");
	         return resultMap;
	      }
		 try {
			List<Map<String,String>> StudentAdmissionTicketInfo=gjtExamPlanNewService.getStudentAdmissionTicket(searchParams);
			resultMap.put("StudentAdmissionMap", StudentAdmissionTicketInfo);
		 } catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result","2");
        	resultMap.put("message", "接口数据异常");
		}
		 return resultMap;
	 }


	 @RequestMapping(value = "/getExamBatchList")
	 @ResponseBody
	 public Map<String,Object> getExamBatchList(HttpServletRequest request){
		 Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
		 Map resultMap = new LinkedHashMap();

		 String xxzx_id = ObjectUtils.toString(searchParams.get("XXZX_ID"),"").trim();
		 String xxzx_code = ObjectUtils.toString(searchParams.get("XXZX_CODE"),"").trim();
		 if (EmptyUtils.isEmpty(xxzx_id) && EmptyUtils.isEmpty(xxzx_code)){
			 resultMap.put("result","0");
			 resultMap.put("message","XXZX_ID或者XXZX_CODE不能为空！");
			 return resultMap;
		 }

		 if (EmptyUtils.isNotEmpty(xxzx_id)){
			 Map<String, String> schoolMap = commonMapService.getxxIdByxxzxId(ObjectUtils.toString(searchParams.get("XXZX_ID")));
			 if(EmptyUtils.isNotEmpty(schoolMap)){
				 searchParams.put("XXZX_ID",schoolMap.keySet().iterator().next());
			 }else {
				 searchParams.put("XXZX_ID",xxzx_id);
			 }
		 }

		 resultMap = gjtExamPlanNewService.getExamBatchList(searchParams);

		 return resultMap;
	 }
}
