package com.gzedu.xlims.web.controller.signup;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtSchoolRollTran;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.SchoolRollTransTypeEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranService;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 功能说明：学籍异动--转专业
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年12月16日
 * @version 2.5
 */
@Controller
@RequestMapping("/edumanage/rollTrans/changeSpecialty")
public class SchoolRollChangeSpecialtyController {
	
	@Autowired
	GjtSchoolRollTranService gjtSchoolRollTranService;
	
	@Autowired
	CommonMapService commonMapService;
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
		Page<GjtSchoolRollTran> page=gjtSchoolRollTranService.queryRollTransAll(user.getGjtOrg().getId(),1, searchParams, pageRequst);
		//总计
		long totalNum=gjtSchoolRollTranService.queryTotalNum(user.getGjtOrg().getId(),1);
		//教务科待审核
		long teacherStayAudit=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),5,SchoolRollTransTypeEnum.待审核.getValue(),"1");
		//教务科审核不通过
		long teacherIsAudit=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),5,SchoolRollTransTypeEnum.不通过.getValue(),"1");		
		//财务科待审核
		long financeStayAudit=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),6,SchoolRollTransTypeEnum.待审核.getValue(),"1");
		//财务科审核不通过
		long financeIsAudit=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),6,SchoolRollTransTypeEnum.不通过.getValue(),"1");
		//院长待确认
		long rollOnHand=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),0,SchoolRollTransTypeEnum.待确认.getValue(),"1");
		//学员待确认
		long unconfirmed=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),1,SchoolRollTransTypeEnum.待确认.getValue(),"1");
		//学员待缴费
		long stayPayment=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),1,SchoolRollTransTypeEnum.待缴费.getValue(),"1");		
		//教务科待上传
		long stayUpload=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),5,SchoolRollTransTypeEnum.待上传.getValue(),"1");
		//已完成
		long finish=gjtSchoolRollTranService.queryStatusTotalNum(user.getGjtOrg().getId(),-1,SchoolRollTransTypeEnum.退学成功.getValue(),"1");
		
		model.addAttribute("pageInfo", page);
		model.addAttribute("userTypeMap", userTypeMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("rollTypeMap", rollTypeMap);
		model.addAttribute("transType", 1);//异动类型
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("teacherStayAudit", teacherStayAudit);
		model.addAttribute("teacherIsAudit", teacherIsAudit);
		model.addAttribute("financeStayAudit", financeStayAudit);
		model.addAttribute("financeIsAudit", financeIsAudit);
		model.addAttribute("rollOnHand", rollOnHand);
		model.addAttribute("unconfirmed", unconfirmed);
		model.addAttribute("stayPayment", stayPayment);
		model.addAttribute("stayUpload", stayUpload);
		model.addAttribute("finish", finish);		
		return "edumanage/roll/roll_trans_changeSpecialty_list";
	}
}
