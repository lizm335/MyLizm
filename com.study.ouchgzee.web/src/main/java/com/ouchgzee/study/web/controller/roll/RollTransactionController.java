/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.controller.roll;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.eefile.EEFileUploadUtil;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtSchoolRollTran;
import com.gzedu.xlims.pojo.GjtSchoolRollTransAudit;
import com.gzedu.xlims.pojo.GjtSchoolRollTransCost;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.pojo.status.StudentMessageMoveEnum;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.signup.GjtRollPlanService;
import com.gzedu.xlims.service.signup.GjtSignupService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranAuditService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTranService;
import com.gzedu.xlims.service.transaction.GjtSchoolRollTransCostService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.controller.roll.vo.RollTransactionAudtVO;

import net.sf.json.JSONObject;

/**
 * 功能说明：学籍异动--信息更正接口
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年9月7日
 * @version 2.5
 */
@RestController
@RequestMapping("/pcenter/roll/transaction")
public class RollTransactionController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(RollTransactionController.class);
	
	@Autowired
	private GjtRollPlanService gjtRollPlanService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtSignupService gjtSignupService;
	
	@Autowired
	private GjtSchoolRollTranService gjtSchoolRollTranService;
	
	@Autowired
	private GjtSchoolRollTranAuditService  gjtSchoolRollTranAuditService;
	
	@Autowired
	GjtSchoolRollTransCostService gjtSchoolRollTransCostService;

	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private GjtGradeService gjtGradeService;
	
	/**
	 * 学籍异动--信息更正申请
	 * @throws CommonException
	 */
	@RequestMapping(value = "updateStudentMessage", method = RequestMethod.POST)
	public void updateCertificateInfo(HttpServletRequest request) throws CommonException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> studentMap = new HashMap<String, Object>();
		String messageType=request.getParameter("studentMsgMoveType");//信息更正类型
		String isApplyFor=request.getParameter("isApplyFor");//是否是重新提交申请标识
		studentMap.put("transactionType", 5);//异动类型
		studentMap.put("messageType", messageType);
		/*//查询是否已存在异动申请，并且状态为待审核
		List<GjtSchoolRollTran>  list = gjtSchoolRollTranService.findByStudentIdAndIsDeletedOrderByCreatedDt(user.getGjtStudentInfo().getStudentId(),Constants.BOOLEAN_NO);
		int transactionStatus = 1;
		if(list!=null&&list.size()>0){
			if(list.size()==1){
				transactionStatus=list.get(0).getTransactionStatus().intValue();
			}
			if(list.size()>1){
				transactionStatus=list.get(list.size()-1).getTransactionStatus().intValue();
			}			
		}		
		if(transactionStatus==0){
			throw new CommonException(MessageCode.BIZ_ERROR, "已存在一个待审核的异动申请,暂时无法申请其他异动!");
		}else{*/
			String sign = request.getParameter("sign"); //学员 签名
			//如果信息更正类型为入学文化程度更变,则不需要传身份证或其他证明
			if(Integer.parseInt(messageType)!=StudentMessageMoveEnum.入学文化程度更变.getValue()){
				String proveFileType=request.getParameter("proveFileType");//信息变更证明文件类型
				if(StringUtils.isNotBlank(proveFileType)){
					if(proveFileType.equals("1")){
						studentMap.put("sfzz", request.getParameter("sfzz"));//身份证正面
						studentMap.put("sfzf", request.getParameter("sfzf"));	//身份证反面	
					}else{
						studentMap.put("otherProve", request.getParameter("otherProve"));	//其他证明	
					}
				}
				studentMap.put("proveFileType", proveFileType);
			}
			if(StringUtils.isNotBlank(sign)) {
				if(sign.contains("base64,")) {
					String tmpFolderPath = request.getSession().getServletContext().getRealPath("") + File.separator + "tmp" + File.separator
							+ DateFormatUtils.ISO_DATE_FORMAT.format(Calendar.getInstance());
					String signPath = EEFileUploadUtil.uploadImageBase64ToUrl(sign, tmpFolderPath);
					studentMap.put("sign", signPath); // 签名
				} else {
					studentMap.put("sign", sign); // 签名
				}
			}		
			if(Integer.parseInt(messageType)==StudentMessageMoveEnum.性别民族变更.getValue()){//0、性别民族变更 
				String sex=cacheService.getCachedDictionaryName(CacheService.DictionaryKey.SEX,request.getParameter("xbm"));//性别
				String nation=cacheService.getCachedDictionaryName(CacheService.DictionaryKey.NATIONALITYCODE,request.getParameter("mzm"));//民族			
				studentMap.put("xbm", request.getParameter("xbm"));
				studentMap.put("sex", sex);
				studentMap.put("mzm", request.getParameter("mzm"));
				studentMap.put("nation", nation);			
			}else if(Integer.parseInt(messageType)==StudentMessageMoveEnum.入学文化程度更变.getValue()){//1、入学文化程度更变
				String exedulevel=request.getParameter("exedulevel");//原学历层次
				String byzz=request.getParameter("byzz");
				studentMap.put("byzz", byzz);
				studentMap.put("exedulevel", exedulevel);
			}else if(Integer.parseInt(messageType)==StudentMessageMoveEnum.姓名变更.getValue()){//2、姓名变更
				studentMap.put("xm", request.getParameter("xm"));//姓名
			}else if(Integer.parseInt(messageType)==StudentMessageMoveEnum.身份证变更.getValue()){//3、身份证号变更
				studentMap.put("sfzh", request.getParameter("sfzh"));
			}
			try {
				List<GjtSchoolRollTran> gjtSchoolRollTran=gjtSchoolRollTranService.findSchoolRollTran(Integer.parseInt(messageType),user.getGjtStudentInfo().getStudentId(),5);
				if(gjtSchoolRollTran!=null&&gjtSchoolRollTran.size()>0&&isApplyFor.equals("0")){
					/*GjtSchoolRollTran rollTran=gjtSchoolRollTran.get(0);
					gjtSchoolRollTranService.updateGjtSchoolRollTran(studentMap,rollTran);
					// 更新审核记录
					gjtSchoolRollTranAuditService.initSchoolRollTranAudit(Integer.parseInt(messageType),user.getGjtStudentInfo().getStudentId(),5,rollTran);*/
					gjtSchoolRollTranService.insertGjtSchoolRollTran(studentMap,user.getGjtStudentInfo());
				}else{
					gjtSchoolRollTranService.insertGjtSchoolRollTran(studentMap,user.getGjtStudentInfo());
				}				
			} catch (Exception e) {
				throw new CommonException(MessageCode.BIZ_ERROR, "信息更正申请失败!");
			}		
//		}		
	}
	/**
	 *  学习空间查看详情
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getSchoolRollRransAuditList", method = RequestMethod.GET)
	public Map<String,Object> getSchoolRollRransAuditList(HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String transactionType=request.getParameter("transactionType");//异动类型
		List<GjtSchoolRollTran> gjtSchoolRollTran=null;
		List<Map<String,String>> auditList=null;
		if("5".equals(transactionType)){//异动类型：信息更正
			String messageType=request.getParameter("studentMsgMoveType");//信息更正类型
			//查询学员异动的内容
			gjtSchoolRollTran=gjtSchoolRollTranService.findSchoolRollTran(Integer.parseInt(messageType),user.getGjtStudentInfo().getStudentId(),Integer.parseInt(transactionType));				
			if(gjtSchoolRollTran!=null&&gjtSchoolRollTran.size()>0){
				GjtSchoolRollTran rollTran =gjtSchoolRollTran.get(0);
				//查询审核记录
				auditList=
						gjtSchoolRollTranService.querySchoolRollRransAuditList(user.getGjtStudentInfo().getStudentId(),rollTran.getTransactionId());
				String content=rollTran.getTransactionContent();//异动内容
				JSONObject jsonObject = JSONObject.fromObject(content);
				resultMap.put("newStudentInfo", jsonObject);
				resultMap.put("transactionStatus", rollTran.getTransactionStatus());
				resultMap.put("isApplyFor", rollTran.getIsApplyFor());
			}			
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
		}		
		return resultMap;
	}
	/**
	 * 身份证识别接口
	 * @param request
	 * @return
	 * @throws CommonException 
	 */
	@RequestMapping(value = "getCardDiscern", method = RequestMethod.GET)
	public void getCardDiscern(HttpServletRequest request) throws CommonException{
		boolean flag=true;
		String proveFileType=request.getParameter("proveFileType");//信息变更证明文件类型
		if("1".equals(proveFileType)){//身份证
			String sfzz=request.getParameter("sfzz");
			String sfzf=request.getParameter("sfzf");
			if (StringUtils.isNotBlank(sfzz)) {
				boolean idCard = EEFileUploadUtil.isIDCard(sfzz);
				if (!idCard) {
					flag= false;
				}
			}
			if (StringUtils.isNotBlank(sfzf)) {
				boolean idCard = EEFileUploadUtil.isIDCard(sfzf);
				if (!idCard) {
					flag= false;
				}
			}
		}
		if(!flag){
			throw new CommonException(600, "身份证照片拍摄不正确，无法识别，请按照示例要求重新拍照上传");
		}
	}
	/**
	 * 获取学籍异动页面异动类型情况
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getSchoolRollRransList", method = RequestMethod.GET)
	public Map<String,Object> getSchoolRollRransList(HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> specialty = new HashMap<String, Object>();
		Map<String, Object> leaveStudyMap = new HashMap<String, Object>();
		Map<String, Object> backStudyMap = new HashMap<String, Object>();
		Map<String, Object> dropOutStudyMap = new HashMap<String, Object>();
		Map<String, Object> messageCorrectMap = new HashMap<String, Object>();
		Map<String, Object> turnStudyMap = new HashMap<String, Object>();
		try {
			//转专业
			List<GjtSchoolRollTran> changeSpecialty=gjtSchoolRollTranService.findByStudentIdAndTransactionTypeIsDeleted(user.getGjtStudentInfo().getStudentId(),1,Constants.BOOLEAN_NO);
			if(changeSpecialty!=null&&changeSpecialty.size()>0){
				GjtSchoolRollTran tran=changeSpecialty.get(0);
				specialty.put("transactionType", tran.getTransactionType());
				specialty.put("transactionStatus", tran.getTransactionStatus());
				resultMap.put("changeSpecialty", specialty);
			}else{
				resultMap.put("changeSpecialty", specialty);
			}
			//休学
			List<GjtSchoolRollTran> leaveStudy=gjtSchoolRollTranService.findByStudentIdAndTransactionTypeIsDeleted(user.getGjtStudentInfo().getStudentId(),2,Constants.BOOLEAN_NO);
			if(leaveStudy!=null&&leaveStudy.size()>0){
				GjtSchoolRollTran tran=leaveStudy.get(0);
				int transactionStatus=tran.getTransactionPartStatus();
				if(transactionStatus==0){
					leaveStudyMap.put("transactionType", tran.getTransactionType());
					leaveStudyMap.put("transactionStatus", tran.getTransactionStatus());
					resultMap.put("leaveStudy", leaveStudyMap);
				}else{
					resultMap.put("leaveStudy", leaveStudyMap);
				}				
			}else{
				resultMap.put("leaveStudy",leaveStudyMap);
			}
			//复学
			List<GjtSchoolRollTran> backStudy=gjtSchoolRollTranService.findByStudentIdAndTransactionTypeIsDeleted(user.getGjtStudentInfo().getStudentId(),3,Constants.BOOLEAN_NO);
			if(backStudy!=null&&backStudy.size()>0){
				GjtSchoolRollTran tran=backStudy.get(0);
				backStudyMap.put("transactionType", tran.getTransactionType());
				backStudyMap.put("transactionStatus", tran.getTransactionStatus());
				resultMap.put("backStudy", backStudyMap);
			}else{
				resultMap.put("backStudy", backStudyMap);
			}
			//退学
			List<GjtSchoolRollTran> dropOutStudy=gjtSchoolRollTranService.findByStudentIdAndTransactionTypeIsDeleted(user.getGjtStudentInfo().getStudentId(),4,Constants.BOOLEAN_NO);
			if(dropOutStudy!=null&&dropOutStudy.size()>0){
				for(int i=0;i<dropOutStudy.size();i++){
					GjtSchoolRollTran tran=dropOutStudy.get(i);
					Object transactionStatus=tran.getTransactionStatus();
					if(!"13".equals(transactionStatus)){
						dropOutStudyMap.put("transactionType", tran.getTransactionType());
						dropOutStudyMap.put("operatorRole", tran.getAuditOperatorRole());
						dropOutStudyMap.put("transactionStatus", tran.getTransactionStatus());
						resultMap.put("dropOutStudy", dropOutStudyMap);
					}else{
						resultMap.put("dropOutStudy",dropOutStudyMap);
					}		
				}					
			}else{
				resultMap.put("dropOutStudy",dropOutStudyMap);
			}
			//信息更正
			List<GjtSchoolRollTran> messageCorrect=gjtSchoolRollTranService.findByStudentIdAndTransactionTypeIsDeleted(user.getGjtStudentInfo().getStudentId(),5,Constants.BOOLEAN_NO);
			if(messageCorrect!=null&&messageCorrect.size()>0){
				GjtSchoolRollTran tran=messageCorrect.get(0);
				int transactionStatus=tran.getTransactionPartStatus();
				if(transactionStatus==0){
					messageCorrectMap.put("transactionType", tran.getTransactionType());
					messageCorrectMap.put("transactionStatus", tran.getTransactionStatus());
					resultMap.put("messageCorrect", messageCorrectMap);
				}else{
					resultMap.put("messageCorrect", messageCorrectMap);
				}				
			}else{
				resultMap.put("messageCorrect", messageCorrectMap);
			}
			//转学
			List<GjtSchoolRollTran> turnStudy=gjtSchoolRollTranService.findByStudentIdAndTransactionTypeIsDeleted(user.getGjtStudentInfo().getStudentId(),6,Constants.BOOLEAN_NO);
			if(turnStudy!=null&&turnStudy.size()>0){
				GjtSchoolRollTran tran=turnStudy.get(0);
				turnStudyMap.put("transactionType", tran.getTransactionType());
				turnStudyMap.put("transactionStatus", tran.getTransactionStatus());
				resultMap.put("turnStudy", turnStudyMap);
			}else{
				resultMap.put("turnStudy", turnStudyMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 重新提交申请
	 * @param request
	 * @throws CommonException 
	 */
	@RequestMapping(value = "againSubmit", method = RequestMethod.POST)
	public void againSubmit(HttpServletRequest request) throws CommonException{
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String transactionType=request.getParameter("transactionType");
		Boolean result=true;
		if("5".equals(transactionType)){//信息更正
			String transactionPartStatus=request.getParameter("studentMsgMoveType");//信息更正类型
			//更新是否重新申请状态
			result=gjtSchoolRollTranService.againSubmitGjtSchoolRollTran(user.getGjtStudentInfo(),Integer.parseInt(transactionType),Integer.parseInt(transactionPartStatus));
			
		}else{//异动类型：转专业、转学、退学、休学、复学
			result=gjtSchoolRollTranService.againSubmitGjtSchoolRollTranNew(user.getGjtStudentInfo(),Integer.parseInt(transactionType));
		}
	}
	/**
	 * 学籍异动申请--休学
	 * @param request
	 * @throws CommonException 
	 */
	@RequestMapping(value = "tranLeaveStudy", method = RequestMethod.POST)
	public void schoolRollTranLeaveStudy(HttpServletRequest request) throws CommonException{
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> studentMap = new HashMap<String, Object>();
		String transactionType=request.getParameter("transactionType");//异动类型
		String isApplyFor=request.getParameter("isApplyFor");//是否是重新提交申请标识
		studentMap.put("transactionType", transactionType);//异动类型		
		String leaveStudyContent=request.getParameter("leaveStudyContent");//学员休学申请内容
		String sign = request.getParameter("sign"); //学员 签名
		if(StringUtils.isNotBlank(sign)) {
			if(sign.contains("base64,")) {
				String tmpFolderPath = request.getSession().getServletContext().getRealPath("") + File.separator + "tmp" + File.separator
						+ DateFormatUtils.ISO_DATE_FORMAT.format(Calendar.getInstance());
				String signPath = EEFileUploadUtil.uploadImageBase64ToUrl(sign, tmpFolderPath);
				studentMap.put("sign", signPath); // 签名
			} else {
				studentMap.put("sign", sign); // 签名
			}
		}
		studentMap.put("leaveStudyContent", leaveStudyContent);
		List<GjtSchoolRollTran> leaveStudy=
				gjtSchoolRollTranService.findByStudentIdAndTransactionTypeIsDeleted(user.getGjtStudentInfo().getStudentId(),Integer.parseInt(transactionType),Constants.BOOLEAN_NO);
		//如果存在并且是重新申请，则更新申请异动表和异动审核记录表
		if(leaveStudy!=null&&leaveStudy.size()>0&&isApplyFor.equals("0")){					
			/*GjtSchoolRollTran rollTran=leaveStudy.get(0);
			gjtSchoolRollTranService.updateGjtSchoolRollTran(studentMap,rollTran);
			gjtSchoolRollTranAuditService.initAudit(rollTran,user.getGjtStudentInfo().getStudentId());*/
			//如果是重新申请休学，则保留上一次申请记录，重新生成异动信息
			gjtSchoolRollTranService.insertGjtSchoolRollTran(studentMap,user.getGjtStudentInfo());
		}else{
			//新增异动信息
			gjtSchoolRollTranService.insertGjtSchoolRollTran(studentMap,user.getGjtStudentInfo());
		}							
	}
	/**
	 * 学籍异动申请--退学
	 * @param request
	 * @throws CommonException 
	 */
	@RequestMapping(value = "dropOutStudy", method = RequestMethod.POST)
	public void schoolRollDropOutStudy(HttpServletRequest request) throws CommonException{
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo gjtStudentInfo=user.getGjtStudentInfo();
		Map<String, Object> outStudyMap = new HashMap<String, Object>();
		//学员申请退学
		String bankCardNumber=request.getParameter("bankCardNumber");//银行卡号
		String openBank=request.getParameter("openBank");//开户银行
		String cause=request.getParameter("cause");//退学原因
		String sign = request.getParameter("sign"); //学员 签名
		String revocation="0";
		if(StringUtils.isNotBlank(sign)) {
			if(sign.contains("base64,")) {
				String tmpFolderPath = request.getSession().getServletContext().getRealPath("") + File.separator + "tmp" + File.separator
						+ DateFormatUtils.ISO_DATE_FORMAT.format(Calendar.getInstance());
				String signPath = EEFileUploadUtil.uploadImageBase64ToUrl(sign, tmpFolderPath);
				if(StringUtils.isNoneEmpty(signPath)){
					outStudyMap.put("sign", signPath); // 签名
				}else{
					throw new CommonException(600, "内部异常-签名上传失败!");
				}
			} else {
				outStudyMap.put("sign", sign); // 签名
			}
		}
		outStudyMap.put("revocation", revocation);
		outStudyMap.put("bankCardNumber", bankCardNumber);
		outStudyMap.put("accountName", gjtStudentInfo.getXm());
		outStudyMap.put("openBank", openBank);
		outStudyMap.put("cause", cause);
		//推送至招生平台
		int status=gjtSchoolRollTranService.syncDropOutStudy(outStudyMap,gjtStudentInfo);
		if(status==1){
			//新增退学申请记录
			boolean result=gjtSchoolRollTranService.inertDropOutStudy(outStudyMap,gjtStudentInfo);
			if(!result){
				throw new CommonException(600, "内部异常-退学申请失败!");	
			}
		}else{
			throw new CommonException(600, "招生平台退学接口发生异常!");	
		}					
	}
	/**
	 * 获取银行列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getBankInfo", method = RequestMethod.GET)
	public Map<String,Object> getBankInfo(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, String> bankMap =EnumUtil.getBankMap();	
		Object[] bankValues =  bankMap.values().toArray();
		resultMap.put("bankMap", bankValues);
		return resultMap;
	}
	
	/**
	 * 查看退学详情
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getDropOutStudyAudit", method = RequestMethod.GET)
	public Map<String,Object> getDropOutStudyAudit(HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String transactionType=request.getParameter("transactionType");//异动类型
		List<GjtSchoolRollTran> gjtSchoolRollTran=gjtSchoolRollTranService.
				findByDropOutStudyRollTran(user.getGjtStudentInfo().getStudentId(),Integer.parseInt(transactionType));
		List<GjtSchoolRollTransAudit> auditList=null;
		if(gjtSchoolRollTran!=null&&gjtSchoolRollTran.size()>0){
			auditList=gjtSchoolRollTranAuditService.queryDropOutStudyRransAuditList(gjtSchoolRollTran.get(0).getTransactionId(),user.getGjtStudentInfo().getStudentId());
			GjtSchoolRollTran rollTran =gjtSchoolRollTran.get(0);
			String content=rollTran.getTransactionContent();//异动内容
			JSONObject jsonObject = JSONObject.fromObject(content);
			resultMap.put("dropOutStudyInfo", jsonObject);
			resultMap.put("transactionStatus", rollTran.getTransactionStatus());
			resultMap.put("transactionId", rollTran.getTransactionId());
			resultMap.put("isApplyFor", rollTran.getIsApplyFor());			
			//查询学员退学相关费用
			GjtSchoolRollTransCost cost=gjtSchoolRollTransCostService.findByTransactionId(gjtSchoolRollTran.get(0).getTransactionId());
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
	
	/**
	 * 学员待确认应扣、应退金额
	 * @param request
	 */
	@RequestMapping(value = "studentConfirmCost", method = RequestMethod.POST)
	@ResponseBody
	public void studentConfirmCost(HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String transactionId=request.getParameter("transactionId");//退学异动ID		
		try {
			 if (EmptyUtils.isEmpty(transactionId)){
				 throw new CommonException(MessageCode.BAD_REQUEST, "transactionId不能为空");	
	         }
			 int result=gjtSchoolRollTranService.studentConfirmCost(transactionId,user.getGjtStudentInfo());
			 if(result!=1){
				 throw new CommonException(600, "学员待确认应扣、应退金额失败！");	 
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 学员撤销退学申请
	 * @param request
	 */
	@RequestMapping(value = "studentRevocation", method = RequestMethod.POST)
	@ResponseBody
	public void studentRevocation(HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String transactionId=request.getParameter("transactionId");//退学异动ID		
		try {
			 if (EmptyUtils.isEmpty(transactionId)){
				 throw new CommonException(MessageCode.BAD_REQUEST, "transactionId不能为空");	
	         }
			 int result=gjtSchoolRollTranService.studentRevocation(transactionId,user.getGjtStudentInfo());
			 if(result!=1){
				 throw new CommonException(600, "学员撤销退学申请失败！");	 
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 校验学员是否有申请退学、休学、复学、转专业和转学的资格
	 * @param request
	 */
	@RequestMapping(value = "studentOutStudyApplication", method = RequestMethod.GET)
	public Map<String,Object> studentOutStudyApplication(HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo gjtStudentInfo=user.getGjtStudentInfo();
		Map<String, Object> outStudyMap = new HashMap<String, Object>();
		Date now = new Date();
		String gradeId = gjtGradeService.getCurrentGradeId(gjtStudentInfo.getXxId());//当前学期
		if("2".equals(gjtStudentInfo.getXjzt())){//学员已注册学籍			
			String termId=gjtStudentInfo.getViewStudentInfo().getGradeId();//学员入学学期
//			if(info.getRollTransBeginDt()!=null && info.getRollTransEndDt()!=null 
//						&& now.after(info.getRollTransBeginDt())&&now.before(info.getRollTransEndDt())){
			//判断是否是在第一学期，如果当前学期是在第一个学期，则不能申请退学					
			if(!gradeId.equals(termId)){
				outStudyMap.put("isApplication", "1");//能申请异动
			}else{
				outStudyMap.put("isApplication", "0");//不能申请异动
			}												
		}else if("8".equals(gjtStudentInfo.getXjzt())){//已毕业的学员
			outStudyMap.put("isApplication", "0");//不能申请异动
		}else{
			outStudyMap.put("isApplication", "1");//能申请异动
		}		
		return outStudyMap;
	}
	
	/**
	 * 校验学员是否有申请信息更正的的资格
	 * @param request
	 */
	@RequestMapping(value = "checkMessageInfoApplication", method = RequestMethod.GET)
	public Map<String,Object> studentMessageInfoApplication(HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo gjtStudentInfo=user.getGjtStudentInfo();
		Map<String, Object> MessageInfoMap = new HashMap<String, Object>();
		Date now = new Date();
		String gradeId = gjtGradeService.getCurrentGradeId(gjtStudentInfo.getXxId());//当前学期
		if("2".equals(gjtStudentInfo.getXjzt())){//学员已注册学籍			
			String termId=gjtStudentInfo.getViewStudentInfo().getGradeId();//学员入学学期			
			//判断是否是在第一学期，如果不是在第一学期，则可以申请
			if(!gradeId.equals(termId)){
				MessageInfoMap.put("isApplication", "1");//能申请信息更正
			}else{
				//如果是在第一学期，并且正式学号还未下来，则能申请信息更正
				if(!gjtStudentInfo.getXh().equals(gjtStudentInfo.getSfzh())){
					MessageInfoMap.put("isApplication", "1");//能申请信息更正
				}else{
					MessageInfoMap.put("isApplication", "0");//不能申请信息更正
				}				
			}												
		}else{
			MessageInfoMap.put("isApplication", "1");//能申请信息更正
		}		
		return MessageInfoMap;
	}
	/**
	 * 获取转学、退学下载链接
	 * @return
	 */
	@RequestMapping(value = "getOutStudyTemplate", method = RequestMethod.GET)
	public Map<String, Object> getLinkXlzmTemplate(HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GjtStudentInfo studentInfo=user.getGjtStudentInfo();
		String templatePath=null;
		if(studentInfo.getXxId().equals("2f5bfcce71fa462b8e1f65bcd0f4c632")){//广州国开
			templatePath = (getBasePath(request) + "/static/resources/国家开放大学转学、退学退费规定.doc");			
		}
		if(studentInfo.getXxId().equals("9b2f42ececf64f38af621554d1ea5b79")){//北京国开
			templatePath = (getBasePath(request) + "/static/resources/国家开放大学实验学院转学、退学规定.doc");	
		}
		resultMap.put("link", templatePath);		
		return resultMap;
	}
}
