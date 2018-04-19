package com.gzedu.xlims.webservice.controller.gjt.rollTransaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.transaction.GjtSchoolRollTranDao;
import com.gzedu.xlims.pojo.GjtSchoolRollTran;
import com.gzedu.xlims.pojo.GjtSchoolRollTransAudit;
import com.gzedu.xlims.pojo.GjtSchoolRollTransCost;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranAuditService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTransCostService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.webservice.common.Feedback;
import com.gzedu.xlims.webservice.response.ResponseResult;
import com.gzedu.xlims.webservice.response.ResponseStatus;

import net.sf.json.JSONObject;

/**
 * 功能说明：学籍异动接口--对接招生平台
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年12月16日
 * @version 2.5
 */
@Controller
@RequestMapping("/interface/rollTransaction")
public class GjtRollTransactionController {
	
	@Autowired
	private GjtOrgService gjtOrgService;
	
	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;
	
	@Autowired
	GjtSchoolRollTranService gjtSchoolRollTranService;
	
	@Autowired
	GjtSchoolRollTranDao gjtSchoolRollTranDao;
	
	@Autowired
	GjtSchoolRollTranAuditService gjtSchoolRollTranAuditService;
	
	@Autowired
	GjtSchoolRollTransCostService gjtSchoolRollTransCostService;
	
	/**
	 * 退学审核--招生接口
	 * @param transType
	 * @param orgCode
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateTransStatus", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult updateTransStatus(@RequestParam String studentId,
											@RequestParam int transType,
											@RequestParam int transStatus,
											@RequestParam int operatorRole,
											@RequestParam String operatorRoleName,
											@RequestParam String auditContent,
											GjtSchoolRollTransCost transCost,HttpServletRequest request){
		ResponseResult result = null;
		 Map<String,Object> paramsMap = new HashMap<String,Object>();
		try {
			 if (EmptyUtils.isEmpty(studentId)){
	             return new ResponseResult(ResponseStatus.PARAM_ERROR, "studentId不能为空!");
	         }
			 if(EmptyUtils.isEmpty(transType)){//异动类型
				 return new ResponseResult(ResponseStatus.PARAM_ERROR, "transType不能为空!");
			 }
			 if(EmptyUtils.isEmpty(transStatus)){//审核状态
				 return new ResponseResult(ResponseStatus.PARAM_ERROR, "transStatus不能为空!");
			 }
			 if(EmptyUtils.isEmpty(operatorRoleName)){//审核人姓名
				 return new ResponseResult(ResponseStatus.PARAM_ERROR, "operatorRoleName不能为空!");
			 }
			 if(EmptyUtils.isEmpty(operatorRole)){//审核人角色
				 return new ResponseResult(ResponseStatus.PARAM_ERROR, "operatorRole不能为空!");
			 }
			 //申请状态 0 待学习中心劝学  1 待学生服务部劝学  2 待教务部劝学  3 财务待核算   4 学员待确认   5 院长待确认  6 财务待退费  7 已退学  8 撤销申请
			 boolean status=gjtSchoolRollTranAuditService.updateOutStudyRransAudit(studentId,transType,transStatus,operatorRoleName,operatorRole,auditContent,transCost);
			 if(status){
				 result = new ResponseResult(ResponseStatus.SUCCESS, null);
			 }			 
		} catch (Exception e) {
			 result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, "服务器异常！");			 
		}		
		return result;
	}
	/**
	 * 查看学员退学审核流程--返回页面
	 * @param studentId
	 * @param request
	 * @return
	 * @throws CommonException 
	 */
	@RequestMapping(value = "outStudyViewForm/{studentId}/{transType}", method = RequestMethod.GET)
	public String outStudyViewForm(@PathVariable("studentId") String studentId, @PathVariable("transType") int transType,
			HttpServletRequest request,ModelMap model) throws CommonException{
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);
		if(studentInfo!=null){
			List<GjtSchoolRollTran> rollTran= gjtSchoolRollTranDao.queryGjtSchoolRollTran(studentId, transType, Constants.BOOLEAN_NO);
			if(rollTran!=null&&rollTran.size()>0){
				//异动申请
				GjtSchoolRollTran gjtSchoolRollTran=gjtSchoolRollTranService.findById(rollTran.get(0).getTransactionId());
				//查询退学费用
				GjtSchoolRollTransCost gjtSchoolRollTransCost=gjtSchoolRollTransCostService.findByTransactionId(rollTran.get(0).getTransactionId());
				//查询异动审核明细
				List <GjtSchoolRollTransAudit> transAuditList=
						gjtSchoolRollTranAuditService.queryTransAuditInfo(rollTran.get(0).getTransactionId(),studentId,Constants.BOOLEAN_NO);
				String content=gjtSchoolRollTran.getTransactionContent();//异动内容
				JSONObject jsonObject = JSONObject.fromObject(content);
				model.addAttribute("transactionContent", jsonObject);
				model.addAttribute("item", gjtSchoolRollTran);
				model.addAttribute("transCost", gjtSchoolRollTransCost);
				model.addAttribute("transAuditList", transAuditList);//审核记录流程
			}			
		}else{
			model.addAttribute("feedback", new Feedback(false, "学员不存在"));
		}
		return "edumanage/roll/schoolRoll_transaction_dropOut_study_api";		
	}
}
