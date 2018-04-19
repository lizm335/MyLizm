package com.gzedu.xlims.web.controller.signup;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.WordTemplateUtil;
import com.gzedu.xlims.dao.transaction.GjtExemptExamInfoDao;
import com.gzedu.xlims.pojo.GjtExemptExamInfo;
import com.gzedu.xlims.pojo.GjtExemptExamInfoAudit;
import com.gzedu.xlims.pojo.GjtExemptExamProve;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.SchoolRollTransTypeEnum;
import com.gzedu.xlims.pojo.status.TransAuditRoleEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.transaction.GjtExemptExamInfoAuditService;
import com.gzedu.xlims.service.transaction.GjtExemptExamInfoService;
import com.gzedu.xlims.service.transaction.GjtExemptExamProveService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 功能说明：学员申请免修免考详情
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月28日
 * @version 2.5
 */
@Controller
@RequestMapping("/edumanage/exemptExamInfo")
public class GjtExemptExamInfoController {
	private static final Logger log = LoggerFactory.getLogger(GjtExemptExamInfoController.class);
	@Autowired
	private CommonMapService commonMapService;
	
	@Autowired
	private GjtGradeService gjtGradeService;
	
	@Autowired
	private GjtExemptExamInfoService gjtExemptExamInfoService;
	
	@Autowired
	private GjtExemptExamInfoDao gjtExemptExamInfoDao;
	
	@Autowired
	private GjtExemptExamProveService gjtExemptExamProveService;
	 
	@Autowired
	private GjtExemptExamInfoAuditService gjtExemptExamInfoAuditService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 学期
		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());//层次
		String gradeId = gjtGradeService.getCurrentGradeId(user.getGjtOrg().getId());
		//统计
		long totalNum=gjtExemptExamInfoService.queryTotalNum(user.getGjtOrg().getId());//总计
		long stayAudit=gjtExemptExamInfoService.queryByStatusTotalNum(user.getGjtOrg().getId(),SchoolRollTransTypeEnum.待审核.getValue(),"5");//待审核
		long auditNoPass=gjtExemptExamInfoService.queryByStatusTotalNum(user.getGjtOrg().getId(),SchoolRollTransTypeEnum.不通过.getValue(),"5");//审核不通过
		long auditPass=gjtExemptExamInfoService.queryByStatusTotalNum(user.getGjtOrg().getId(),SchoolRollTransTypeEnum.通过.getValue(),"5");//审核通过
		
		Page<GjtExemptExamInfo> page=gjtExemptExamInfoService.queryAll(user.getGjtOrg().getId(), searchParams,pageRequst);
				
		model.addAttribute("pageInfo", page);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("defaultGradeId", gradeId);
		model.addAttribute("exemptExamType", "1");
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("stayAudit", stayAudit);
		model.addAttribute("auditNoPass", auditNoPass);
		model.addAttribute("auditPass", auditPass);
		return "edumanage/roll/exempt_exam_student_list";		
	}
	
	/**
	 * 跳转至学员申请免修免考详情页面
	 * @param installId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "toView/{exemptExamId}/{action}", method = RequestMethod.GET)
	public String toView(@PathVariable("exemptExamId") String exemptExamId,@PathVariable("action") String action,
			                    Model model,HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		//查询学员申请免修免考详情
		GjtExemptExamInfo exemptExamInfo=gjtExemptExamInfoDao.findOne(exemptExamId);
		//查询学员的资料证明材料
		List<GjtExemptExamProve>  proveList=gjtExemptExamProveService.findByExemptExamId(exemptExamId);
		//查询学员免修免考审核记录详情
		List<GjtExemptExamInfoAudit> auditList=gjtExemptExamInfoAuditService.findByExemptExamIdOrderByAuditDtAsc(exemptExamId);
		String code=TransAuditRoleEnum.getCode(user.getPriRoleInfo().getRoleId());
		if(!"".equals(code)){
			model.addAttribute("code",code);//角色
		}else{
			model.addAttribute("code"," ");//角色
		}		
		model.addAttribute("item", exemptExamInfo);
		model.addAttribute("proveList", proveList);
		model.addAttribute("auditList", auditList);	
		model.addAttribute("item", exemptExamInfo);
		model.addAttribute("action", action);
		return "edumanage/roll/exempt_exam_student_view";
	}
	/**
	 * 审核流程
	 * @param transactionId
	 * @param auditState
	 * @param studentId
	 * @param auditContent
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "exemptExamAudit", method = RequestMethod.POST)
	public String exemptExamAudit(@RequestParam("exemptExamId") String exemptExamId,
			 			 @RequestParam("auditState") String auditState,
			 			 @RequestParam("courseId") String courseId,
			 			 @RequestParam("studentId") String studentId,
			 			 String auditContent,
			 			 HttpServletRequest request, RedirectAttributes redirectAttributes){
		Feedback feedback = new Feedback(true, "审核成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			String roleCode=TransAuditRoleEnum.getCode(user.getPriRoleInfo().getRoleId());
			data.put("exemptExamId", exemptExamId);
			data.put("auditState", auditState);
			data.put("auditContent", auditContent);
			data.put("roleName", user.getRealName());
			data.put("courseId", courseId);
			data.put("studentId", studentId);
			if("5".equals(roleCode)){//学籍科					
				boolean result=gjtExemptExamInfoService.exemptExamAudit(data);
				if(!result){
					feedback = new Feedback(false, "审核失败");
				}
			}			
		} catch (Exception e) {
			feedback = new Feedback(false, "审核失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/exemptExamInfo/list";
	}
	
	/**
	 * 下载免修免考申请表
	 * @param exemptExamId
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "downloadApplication/{exemptExamId}")
	@ResponseBody
	public void downloadApplication(@PathVariable("exemptExamId") String exemptExamId,HttpServletRequest 
			request, HttpServletResponse response) throws Exception{
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);		
		//查询学员申请免修免考详情
		Map<String, Object> exemptExamInfoMap=gjtExemptExamInfoService.queryExemptExamInfo(exemptExamId);		
		String studentName = exemptExamInfoMap.get("xm").toString();
		String fileName = "国家开放大学免修免考申请表_" + studentName + ".doc";
		response.setContentType("application/msword;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
		WordTemplateUtil.createWord(exemptExamInfoMap, "国家开放大学免修免考申请表.ftl", response.getOutputStream());
	}
}
