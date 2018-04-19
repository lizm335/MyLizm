package com.gzedu.xlims.web.controller.signup;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.WordTemplateUtil;
import com.gzedu.xlims.common.json.JsonUtils;
import com.gzedu.xlims.pojo.GjtSchoolRollTran;
import com.gzedu.xlims.pojo.GjtSchoolRollTransAudit;
import com.gzedu.xlims.pojo.GjtSchoolRollTransCost;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.SchoolRollTransTypeEnum;
import com.gzedu.xlims.pojo.status.TransAuditRoleEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranAuditService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTransCostService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

import net.sf.json.JSONObject;

/**
 * 功能说明：学籍异动--退学
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年12月15日
 * @version 2.5
 */
@Controller
@RequestMapping("/edumanage/rollTrans/dropOutStudy")
public class SchoolRollOutStudyController {
	
	@Autowired
	GjtSchoolRollTranService gjtSchoolRollTranService;
	
	@Autowired
	GjtSchoolRollTranAuditService gjtSchoolRollTranAuditService;
	
	@Autowired
	GjtSchoolRollTransCostService gjtSchoolRollTransCostService;
	
	@Autowired
	CommonMapService commonMapService;
	/**
	 * 退学列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());//学期
		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());//培养层次
		Map<String, String> rollTypeMap = commonMapService.getRollTypeMap();// 学籍状态
		Map<String, String> yearMap = commonMapService.getYearMap(user.getGjtOrg().getId());// 年级
		Map<String, String> userTypeMap =commonMapService.getDates("USER_TYPE");
		//查询列表数据
		Page<GjtSchoolRollTran> page=gjtSchoolRollTranService.queryRollTransAll(user.getGjtOrg().getId(), 4,searchParams, pageRequst);
		//总计
		long totalNum=gjtSchoolRollTranService.queryTotalNum(user.getGjtOrg().getId(),4);
		//学习中心劝学中
		long studyCenterTruancy=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),4,SchoolRollTransTypeEnum.劝学中.getValue(),"4");
		//学生事务部劝学中
		long studentTruancy=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),2,SchoolRollTransTypeEnum.劝学中.getValue(),"4");
		//学籍科劝学中
		long rollTruancy=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),5,SchoolRollTransTypeEnum.劝学中.getValue(),"4");
		//待核算
		long forAccount=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),-1,SchoolRollTransTypeEnum.待核算.getValue(),"4");
		//待确认
		long unconfirmed=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),1,SchoolRollTransTypeEnum.待确认.getValue(),"4");
		//院长待处理
		long onHand=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),0,SchoolRollTransTypeEnum.待确认.getValue(),"4");
		//学籍科待处理
		long rollOnHand=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),5,SchoolRollTransTypeEnum.待确认.getValue(),"4");
		//待登记
		long registered=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),-1,SchoolRollTransTypeEnum.待登记.getValue(),"4");
		//已完成
		long finish=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),-1,SchoolRollTransTypeEnum.退学成功.getValue(),"4");
		//待登记
		long revocation=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),-1,SchoolRollTransTypeEnum.撤销退学.getValue(),"4");
		
		model.addAttribute("pageInfo", page);
		model.addAttribute("userTypeMap", userTypeMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("yearMap", yearMap);
		model.addAttribute("rollTypeMap", rollTypeMap);
		model.addAttribute("transType", 4);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("studyCenterTruancy", studyCenterTruancy);
		model.addAttribute("studentTruancy", studentTruancy);
		model.addAttribute("rollTruancy", rollTruancy);
		model.addAttribute("forAccount", forAccount);
		model.addAttribute("unconfirmed", unconfirmed);
		model.addAttribute("onHand", onHand);
		model.addAttribute("rollOnHand", rollOnHand);
		model.addAttribute("registered", registered);
		model.addAttribute("finish", finish);
		model.addAttribute("revocation", revocation);
		return "edumanage/roll/roll_trans_dropOutStudy_list";
	}
	
	/**
	 *	查看详情
	 * @param transactionId
	 * @param studentId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "view/{transactionId}/{studentId}", method = RequestMethod.GET)
	public String queryTransactionAuditInfo(@PathVariable("transactionId") String transactionId, @PathVariable("studentId") String studentId,
			ModelMap model, HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		//异动申请
		GjtSchoolRollTran gjtSchoolRollTran=gjtSchoolRollTranService.findById(transactionId);
		//查询退学费用
		GjtSchoolRollTransCost gjtSchoolRollTransCost=gjtSchoolRollTransCostService.findByTransactionId(transactionId);
		String code=TransAuditRoleEnum.getCode(user.getPriRoleInfo().getRoleId());
		//异动申请明细
		List <GjtSchoolRollTransAudit> transAuditList=
				gjtSchoolRollTranAuditService.queryTransAuditInfo(transactionId,studentId,Constants.BOOLEAN_NO);
		Map<String,Object> transactionContent=JsonUtils.toObject(gjtSchoolRollTran.getTransactionContent().toString(), HashMap.class);		
		model.addAttribute("item", gjtSchoolRollTran);
		model.addAttribute("transCost", gjtSchoolRollTransCost);
		model.addAttribute("transactionContent", transactionContent);//异动内容
		model.addAttribute("transAuditList", transAuditList);//审核记录流程
		if(!"".equals(code)){
			model.addAttribute("code",code);//角色
		}else{
			model.addAttribute("code"," ");//角色
		}			
		model.addAttribute("action","view");					
		return "edumanage/roll/schoolRoll_transaction_dropOut_study";		
	}
	
	/**
	 * 退学审核流程
	 * @param transactionId
	 * @param auditState
	 * @param studentId
	 * @param auditContent
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "auditDropOutStudy", method = RequestMethod.POST)
	public String auditDropOutStudy(@RequestParam("transactionId") String transactionId,
			 						@RequestParam("auditState") String auditState,
			 						@RequestParam("studentId") String studentId,
			 						String auditContent,HttpServletRequest request, 
			 						RedirectAttributes redirectAttributes){
		Feedback feedback = new Feedback(true, "审核成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			String roleCode=TransAuditRoleEnum.getCode(user.getPriRoleInfo().getRoleId());
			data.put("transactionId", transactionId);
			data.put("auditState", auditState);
			data.put("studentId", studentId);
			data.put("roleCode", roleCode);
			data.put("auditContent", auditContent);
			data.put("roleName", user.getRealName());
			if("2".equals(roleCode)){//学生服务部
				gjtSchoolRollTranAuditService.insertOutStudyTransAudit(data);
			}
			if("5".equals(roleCode)){//学籍科
				if("9".equals(auditState)){//确认退学操作
					data.put("dropSchoolPhoto", request.getParameter("dropSchoolPhoto"));//退学申请表照片			
				}
				boolean result=gjtSchoolRollTranAuditService.insertOutStudyTransAudit(data);
				if(!result){
					feedback = new Feedback(false, "审核失败");
				}
			}
			if("0".equals(roleCode)){//院长确认退学
				boolean status=gjtSchoolRollTranAuditService.insertOutStudyTransAudit(data);
				if(!status){
					feedback = new Feedback(false, "审核失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			feedback = new Feedback(false, "审核失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/rollTrans/dropOutStudy/list";
	}
	
	/**
	 * 下载退学申请表
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value = "downloadOutStudyApplication")
	@ResponseBody
	public void downloadOutStudyApplication(HttpServletRequest request, HttpServletResponse response) throws IOException{
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		GjtSchoolRollTran rollTran=gjtSchoolRollTranService.findById(searchParams.get("transactionId").toString());
		Map<String, Object> applicationMsg = gjtSchoolRollTranService.queryStudentApplicationMsg(searchParams.get("studentId").toString());
		String content=rollTran.getTransactionContent();//异动内容
		JSONObject jsonObject = JSONObject.fromObject(content);
		String cause=jsonObject.getString("cause");//退学原因
		String sign=jsonObject.getString("sign");//签名
		String studentName = applicationMsg.get("studentName").toString();
		String fileName = "国家开放大学学生自愿退学申请表_" + studentName + ".doc";
		
		if (sign != null && !"".equals(sign.toString().trim())) {
			String realPath = request.getSession().getServletContext().getRealPath("");
			try {
				String encode = WordTemplateUtil.getRemoteSourceEncode(sign.toString().trim(), realPath);
				applicationMsg.put("sign", encode);
			} catch (Exception e) {
				applicationMsg.put("sign", " ");
			}
		}else{
			applicationMsg.put("sign", " ");
		}
		applicationMsg.put("cause",cause);
		response.setContentType("application/msword;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));		
		WordTemplateUtil.createWord(applicationMsg, "学生自愿退学申请表.ftl", response.getOutputStream());
	}
}
