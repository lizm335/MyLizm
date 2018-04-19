package com.gzedu.xlims.web.controller.signup;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.gzedu.xlims.common.json.JsonUtils;
import com.gzedu.xlims.pojo.GjtSchoolRollTran;
import com.gzedu.xlims.pojo.GjtSchoolRollTransAudit;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.SchoolRollTransTypeEnum;
import com.gzedu.xlims.pojo.status.TransAuditRoleEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranAuditService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

import net.spy.memcached.MemcachedClient;

/**
 * 功能说明：学籍异动
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年9月5日
 * @version 2.5
 */
@Controller
@RequestMapping("/edumanage/schoolRollTransaction")
public class SchoolRollTransactionController extends BaseController{
	
	@Autowired
	CommonMapService commonMapService;
	
	@Autowired
	GjtStudentInfoService gjtStudentInfoService;
	
	@Autowired
	GjtSchoolRollTranService gjtSchoolRollTranService;
	
	@Autowired
	GjtSchoolRollTranAuditService gjtSchoolRollTranAuditService;
	
	@Autowired
	private MemcachedClient memcachedClient;
	
	/**
	 * 学籍异动列表
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
		
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());//学习中心
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());//学期
		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());//培养层次
		Map<String, String> rollTypeMap = commonMapService.getRollTypeMap();// 学籍状态
		Map<String, String> yearMap = commonMapService.getYearMap(user.getGjtOrg().getId());// 年级
		
		model.addAttribute("pageInfo", page);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);		
		model.addAttribute("rollTypeMap", rollTypeMap);
		model.addAttribute("yearMap", yearMap);
		model.addAttribute("transType", 5);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("studyCenterAudit", studyCenterAudit);
		model.addAttribute("rollAudit", rollAudit);
		model.addAttribute("studyCenterAuditPass", studyCenterAuditPass);
		model.addAttribute("rollAuditPass", rollAuditPass);
		model.addAttribute("finish", finish);
		
		request.getSession().setAttribute("downLoadTranactionListExportXls",searchParams);//导出数据的查询条件
		return "edumanage/roll/schoolRoll_transaction_list";		
	}
	/**
	 *	教务后台查看详情
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
		return "edumanage/roll/schoolRoll_transaction_info_view";		
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
	@RequestMapping(value = "transAudit", method = RequestMethod.POST)
	public String update(@RequestParam("transactionId") String transactionId,
			 			 @RequestParam("auditState") String auditState,
			 			@RequestParam("studentId") String studentId,
			 			 String auditContent,
			 			 HttpServletRequest request, RedirectAttributes redirectAttributes){
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
			if(StringUtils.isNotEmpty(request.getParameter("auditVoucher"))){
				data.put("auditVoucher", request.getParameter("auditVoucher"));//凭证
			}
			if(StringUtils.isNotEmpty(request.getParameter("approvalBook"))){
				data.put("approvalBook", request.getParameter("approvalBook"));//审批表地址
			}
			if(StringUtils.isNotEmpty(request.getParameter("approvalBookName"))){
				data.put("approvalBookName", request.getParameter("approvalBookName"));//审批表名称
			}
			/*暂时去掉学习中心审核
			 * //角色不同，获取的参数不同
			if("3".equals(roleCode)){//学习中心管理员
				boolean result=gjtSchoolRollTranAuditService.updateSchoolRollTranAudit(data);
				if(!result){
					feedback = new Feedback(false, "审核失败");
				}
			}*/
			if("5".equals(roleCode)){//学籍科	
				if("1".equals(auditState)){//如果审核通过
					if(StringUtils.isEmpty(request.getParameter("auditVoucher"))&&StringUtils.isEmpty(request.getParameter("approvalBook"))){
						data.put("transactionStatus", 16);
					}else{
						data.put("transactionStatus", 1);
					}
				}
				boolean result=gjtSchoolRollTranAuditService.updateTransAudit(data);
				if(!result){
					feedback = new Feedback(false, "审核失败");
				}
			}			
		} catch (Exception e) {
			feedback = new Feedback(false, "审核失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/schoolRollTransaction/list";
	}
	
	/**
	 * 按条件批量导出页面
	 * @param totalNum
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/transactionListExport/{totalNum}",method = {RequestMethod.GET,RequestMethod.POST})
	public String transactionListExport(@PathVariable("totalNum") String totalNum,HttpServletRequest request,Model model){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			String phone = ObjectUtils.toString(user.getSjh());
			if(EmptyUtils.isNotEmpty(phone)){
				model.addAttribute("mobileNumber",phone.substring(phone.length()-4,phone.length()));
			}
			model.addAttribute("totalNum",totalNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "edumanage/roll/transaction_list_export";
	}
	
	/**
	 * 获取验证码
	 * @param request
	 * @param feedback
	 * @return
	 */
	@RequestMapping(value = "getMessageCode",method = {RequestMethod.GET,RequestMethod.POST})
	public Feedback getMessageCode(HttpServletRequest request,Feedback feedback){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String phone = ObjectUtils.toString(user.getSjh());
		Random rd = new Random();
		String code = ObjectUtils.toString(rd.nextInt(900000) + 100000);
		memcachedClient.delete("CODE_"+user.getSjh());
		memcachedClient.set("CODE_"+user.getSjh(), 60*60*24, code); // 防止发送超时已经发送了而认为发送失败了，所以提前存储一份
		int smsResult = SMSUtil.sendTemplateMessageCode(phone, code, "gk");
		if(smsResult == 1){
			request.getSession().setAttribute(user.getSjh(),code);
			feedback.setSuccessful(true);
			feedback.setMessage("获取验证码成功！");
			feedback.setObj(code);
		}else {
			feedback.setSuccessful(false);
			feedback.setMessage("获取验证码失败！");
			feedback.setObj("");
		}
		return feedback;
	}
	/**
	 * 校验验证码
	 * @param request
	 * @param userCode
	 * @param feedback
	 * @return
	 */
	@RequestMapping(value = "getCheckCode",method = {RequestMethod.POST})
	@ResponseBody
	public Feedback getCheckCode(HttpServletRequest request,String userCode,Feedback feedback){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String sjh = ObjectUtils.toString(user.getSjh());
		String code = ObjectUtils.toString(request.getSession().getAttribute(user.getSjh()),"");
		String memCode = ObjectUtils.toString(memcachedClient.get("CODE_"+sjh));
		if(code.equals(userCode) || memCode.equals(userCode)){
			request.getSession().setAttribute("hasPermission",true);
			feedback.setSuccessful(true);
		}else {
			request.getSession().setAttribute("hasPermission",false);
			feedback.setSuccessful(false);
		}
		return feedback;		
	}
	/**
	 * 导出学员异动申请明细表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "downLoadTranactionList",method = {RequestMethod.POST})
	public void downLoadTranactionList(HttpServletRequest request, HttpServletResponse response){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if(flag!=null&&flag==true){
			try {
				Map searchParams = (Map) request.getSession().getAttribute("downLoadTranactionListExportXls");
				List<Map<String, Object>> resultList =gjtSchoolRollTranService.querySchoolRollTranList(searchParams,user.getGjtOrg().getId());
				String path = request.getSession().getServletContext().getRealPath("")
						+ WebConstants.EXCEL_DOWNLOAD_URL + "tranaction" + File.separator;
				String fileName="学员异动申请明细表.xls";
				String outFile =gjtSchoolRollTranService.downLoadTranactionList(resultList,fileName,path);
				super.downloadFile(request,response,path + outFile);
				request.getSession().setAttribute(user.getSjh(),"");
				FileKit.delFile(path + outFile);				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			throw new RuntimeException("您没有该权限");
		}
	}
}
