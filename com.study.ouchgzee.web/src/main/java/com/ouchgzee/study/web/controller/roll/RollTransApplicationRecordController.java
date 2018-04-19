package com.ouchgzee.study.web.controller.roll;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtSchoolRollTran;
import com.gzedu.xlims.pojo.GjtSchoolRollTransAudit;
import com.gzedu.xlims.pojo.GjtSchoolRollTransCost;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranAuditService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTransCostService;
import com.ouchgzee.study.web.controller.roll.vo.RollTransactionAudtVO;

import net.sf.json.JSONObject;

/**
 * 功能说明：查看我的异动申请记录接口
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月14日
 * @version 2.5
 */
@RestController
@RequestMapping("/pcenter/roll/applicationRecord")
public class RollTransApplicationRecordController {
	
	@Autowired
	private GjtSchoolRollTranService gjtSchoolRollTranService;
	
	@Autowired
	GjtSchoolRollTransCostService gjtSchoolRollTransCostService;
	
	@Autowired
	private GjtSchoolRollTranAuditService  gjtSchoolRollTranAuditService;
	
	/**
	 * 获取学员的全部异动申请记录
	 * @param request
	 * @return applicationRecordMap
	 */
	@RequestMapping(value = "getApplicationRecordList", method = RequestMethod.GET)
	public Map<String,Object> getApplicationRecordList(HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> restultMap=new HashMap<String, Object>();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> map=null;
		List<Map<String,Object>> rollTranList = new ArrayList<Map<String,Object>>();
		List<GjtSchoolRollTran> list = gjtSchoolRollTranService.findByStudentId(user.getGjtStudentInfo().getStudentId());
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				map=new HashMap<String, Object>();
				GjtSchoolRollTran rollTran=list.get(i);
				map.put("transactionId", rollTran.getTransactionId());
				map.put("createdDt", format.format(rollTran.getCreatedDt()));//申请时间
				map.put("transactionType", rollTran.getTransactionType());//异动类型
				if(rollTran.getTransactionType()==5){
					map.put("transactionPartStatus", rollTran.getTransactionPartStatus());//信息更正子类型
				}
				map.put("transactionStatus", rollTran.getTransactionStatus());//异动状态
				map.put("auditOperatorRole", rollTran.getAuditOperatorRole());//审核人角色
				rollTranList.add(map);
			}				
		}else{
			rollTranList.add(map);
		}
		restultMap.put("applicationRecordMap", rollTranList);
		return restultMap;
	}
	
	/**
	 *  查看信息更正申请记录详情
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getMessageApplicationRecord", method = RequestMethod.GET)
	public Map<String,Object> getMessageApplicationRecord(HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String transactionId=request.getParameter("transactionId");//异动ID
		//查询学员异动的内容
		GjtSchoolRollTran gjtSchoolRollTran=gjtSchoolRollTranService.findById(transactionId);				
		//查询审核记录
		List<Map<String,String>> auditList=
				gjtSchoolRollTranService.querySchoolRollRransAuditList(user.getGjtStudentInfo().getStudentId(),gjtSchoolRollTran.getTransactionId());
		String content=gjtSchoolRollTran.getTransactionContent();//异动内容
		JSONObject jsonObject = JSONObject.fromObject(content);
		resultMap.put("newStudentInfo", jsonObject);
		resultMap.put("transactionStatus", gjtSchoolRollTran.getTransactionStatus());		
		List<RollTransactionAudtVO> result = new ArrayList<RollTransactionAudtVO>();
		if(auditList!=null&&auditList.size()>0){
			for (Map data : auditList) {
				RollTransactionAudtVO audt=new RollTransactionAudtVO();
				Object auditState=data.get("AUDIT_STATE");//审核状态
				Object role=data.get("AUDIT_OPERATOR_ROLE");//审核人角色
				if(role!=null){
					audt.setAuditOperatoRole(role.toString());
				}else{
					audt.setAuditOperatoRole(new String());
				}			
				audt.setAuditState(auditState.toString());
				audt.setAuditContent((String)data.get("AUDIT_CONTENT"));//审核内容
				audt.setAuditOperator((String)data.get("AUDIT_OPERATOR"));//审核人姓名
				if(data.get("AUDIT_DT")!=null){
					SimpleDateFormat dataformat=new SimpleDateFormat("yyyy-MM-dd");			
					String date=dataformat.format(data.get("AUDIT_DT"));
					audt.setAuditDt(date);//审核时间
				}else{
					audt.setAuditDt(new String());
				}			
				result.add(audt);
			}
			resultMap.put("schoolRollRransAuditList", result);
		}					
		return resultMap;
	}
	/**
	 * 查看退学申请记录详情
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getDropStudyApplicationRecord", method = RequestMethod.GET)
	public Map<String,Object> getDropStudyApplicationRecord(HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String transactionId=request.getParameter("transactionId");//异动类型
		GjtSchoolRollTran gjtSchoolRollTran=gjtSchoolRollTranService.findById(transactionId);		
		List<GjtSchoolRollTransAudit> auditList=
				gjtSchoolRollTranAuditService.queryDropOutStudyRransAuditList(transactionId,user.getGjtStudentInfo().getStudentId());
		String content=gjtSchoolRollTran.getTransactionContent();//异动内容
		JSONObject jsonObject = JSONObject.fromObject(content);
		resultMap.put("dropOutStudyInfo", jsonObject);
		resultMap.put("transactionStatus", gjtSchoolRollTran.getTransactionStatus());
		resultMap.put("transactionId", gjtSchoolRollTran.getTransactionId());		
		//查询学员退学相关费用
		GjtSchoolRollTransCost cost=gjtSchoolRollTransCostService.findByTransactionId(transactionId);
		if(cost!=null){
			Map<String, Object> costMap = new HashMap<String, Object>();
			costMap.put("backPriceVoucher", cost.getBackPriceVoucher());//退费凭证
			costMap.put("networkMessagePrice", cost.getNetworkMessagePrice());//网络通信费
			costMap.put("originalPrice", cost.getOriginalPrice());//学费原价
			costMap.put("paidinPrice", cost.getPaidinPrice());//学费已缴纳金额
			costMap.put("payablePrice", cost.getPayablePrice());//学费应缴纳金额
			costMap.put("receivedPrice", cost.getReceivedPrice());//实收费用
			costMap.put("reducedPrice", cost.getReducedPrice());//学费优惠价
			costMap.put("rollRegisterPrice", cost.getRollRegisterPrice());//学籍注册费
			costMap.put("shouldBackPrice", cost.getShouldBackPrice());//应退费用
			costMap.put("realBackPrice", cost.getRealBackPrice());//实际退款费用
			costMap.put("studyPrice", cost.getStudyPrice());//学习费
			costMap.put("shouldTextbookPrice", cost.getShouldTextbookPrice());//教材费
			costMap.put("totalPrice", cost.getTotalPrice());//应扣费用总计
			resultMap.put("transCostMap", costMap);
		}else{
			resultMap.put("transCostMap", new HashMap());
		}	
		List<RollTransactionAudtVO> result = new ArrayList<RollTransactionAudtVO>();
		SimpleDateFormat dataformat=new SimpleDateFormat("yyyy-MM-dd");
		if(auditList!=null&&auditList.size()>0){
			for (int i=0;i<auditList.size();i++) {
				GjtSchoolRollTransAudit tranAudit=auditList.get(i);
				RollTransactionAudtVO audt=new RollTransactionAudtVO();
				Object auditState=tranAudit.getAuditState();//审核状态
				Object role=tranAudit.getAuditOperatorRole();//审核人角色
				if(role!=null){
					audt.setAuditOperatoRole(role.toString());
				}else{
					audt.setAuditOperatoRole(new String());
				}
				audt.setAuditState(auditState.toString());
				audt.setAuditContent(tranAudit.getAuditContent());//审核内容
				audt.setAuditOperator(tranAudit.getAuditOperator());//审核人姓名
				if(tranAudit.getAuditDt()!=null){
					dataformat=new SimpleDateFormat("yyyy-MM-dd");			
					String date=dataformat.format(tranAudit.getAuditDt());
					audt.setAuditDt(date);//审核时间
				}else{
					audt.setAuditDt(dataformat.format(new Date()));
				}
				if(Integer.parseInt(tranAudit.getAuditOperatorRole().toString())==4 && Integer.parseInt(tranAudit.getAuditState().toString())==4){
					audt.setAuditState("0");//待受理
					result.add(audt);
					continue;
				}
				if(Integer.parseInt(tranAudit.getAuditOperatorRole().toString())==4 && Integer.parseInt(tranAudit.getAuditState().toString())==3){
					audt.setAuditState("0");//待受理
					result.add(audt);
					continue;
				}
				if(Integer.parseInt(tranAudit.getAuditOperatorRole().toString())==2 && Integer.parseInt(tranAudit.getAuditState().toString())==4){
					audt.setAuditState("0");//待受理
					result.remove(result.get(result.size()-1));
					result.add(audt);
					continue;
				}
				if(Integer.parseInt(tranAudit.getAuditOperatorRole().toString())==2 && Integer.parseInt(tranAudit.getAuditState().toString())==3){
					audt.setAuditState("0");//待受理
					result.remove(result.get(result.size()-1));
					result.add(audt);
					continue;
				}
				if(Integer.parseInt(tranAudit.getAuditOperatorRole().toString())==5 && Integer.parseInt(tranAudit.getAuditState().toString())==3){
					audt.setAuditState("0");//已受理
					result.remove(result.get(result.size()-1));
					result.add(audt);
					continue;
				}	
				if(Integer.parseInt(tranAudit.getAuditOperatorRole().toString())==5 && Integer.parseInt(tranAudit.getAuditState().toString())==4){
					audt.setAuditState("1");//已受理
				}					
				result.add(audt);
			}
			resultMap.put("outStudyAuditList", result);
		}
		return resultMap;
	}
}
