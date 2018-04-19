package com.gzedu.xlims.web.controller.signup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.json.JsonUtils;
import com.gzedu.xlims.pojo.GjtSchoolRollTran;
import com.gzedu.xlims.pojo.GjtSchoolRollTransAudit;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.SchoolRollTransTypeEnum;
import com.gzedu.xlims.pojo.status.TransAuditRoleEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranAuditService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranService;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 功能说明：学籍异动--休学
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年02月26日
 * @version 2.5
 */
@Controller
@RequestMapping("/edumanage/rollTrans/tranLeaveStudy")
public class SchoolRollTranLeaveStudyController {
	
	@Autowired
	GjtSchoolRollTranService gjtSchoolRollTranService;
	
	@Autowired
	CommonMapService commonMapService;
	
	@Autowired
	GjtSchoolRollTranAuditService gjtSchoolRollTranAuditService;
	/**
	 * 转专业列表
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
		Map<String, String> userTypeMap =commonMapService.getDates("USER_TYPE");
		//查询列表数据
		Page<GjtSchoolRollTran> page=gjtSchoolRollTranService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);
		//总计
		long totalNum=gjtSchoolRollTranService.queryTotalNum(user.getGjtOrg().getId(),5);
		//学习中心待审核
		long studyCenterAudit=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),4,SchoolRollTransTypeEnum.待审核.getValue(),"5");
		//学籍科待审核
		long rollAudit=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),5,SchoolRollTransTypeEnum.待审核.getValue(),"5");
		//学习中心审核不通过
		long studyCenterAuditPass=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),4,SchoolRollTransTypeEnum.不通过.getValue(),"5");
		//学籍科审核不通过
		long rollAuditPass=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),5,SchoolRollTransTypeEnum.不通过.getValue(),"5");
		//已完成
		long finish=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),-1,SchoolRollTransTypeEnum.通过.getValue(),"5");
		
		model.addAttribute("pageInfo", page);
		model.addAttribute("userTypeMap", userTypeMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("rollTypeMap", rollTypeMap);
		model.addAttribute("transType", 2);//异动类型
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("studyCenterAudit", studyCenterAudit);
		model.addAttribute("rollAudit", rollAudit);
		model.addAttribute("studyCenterAuditPass", studyCenterAuditPass);
		model.addAttribute("rollAuditPass", rollAuditPass);
		model.addAttribute("finish", finish);
		return "edumanage/roll/roll_trans_leaveStudy_list";
	}
	/**
	 * 查看休学详情
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
		String code=TransAuditRoleEnum.getCode(user.getPriRoleInfo().getRoleId());
		//异动申请明细
		List <GjtSchoolRollTransAudit> transAuditList=
				gjtSchoolRollTranAuditService.queryTransAuditInfo(transactionId,studentId,Constants.BOOLEAN_NO);
		Map<String,Object> transactionContent=JsonUtils.toObject(gjtSchoolRollTran.getTransactionContent().toString(), HashMap.class);		
		model.addAttribute("item", gjtSchoolRollTran);
		model.addAttribute("transactionContent", transactionContent);//异动内容
		model.addAttribute("transAuditList", transAuditList);//审核记录流程
		if(!"".equals(code)){
			model.addAttribute("code",code);//角色
		}else{
			model.addAttribute("code","");//角色
		}			
		model.addAttribute("action","view");
		return "edumanage/roll/roll_trans_leaveStudy_view";	
	}
}
