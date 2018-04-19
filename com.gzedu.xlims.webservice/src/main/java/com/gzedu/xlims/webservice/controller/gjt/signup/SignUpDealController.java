package com.gzedu.xlims.webservice.controller.gjt.signup;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtSyncLog;
import com.gzedu.xlims.service.GjtSyncLogService;
import com.gzedu.xlims.service.api.ApiOpenClassService;
import com.gzedu.xlims.service.flow.GjtFlowService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.signup.GjtSignUpInfoDataService;
import com.gzedu.xlims.service.signup.SignupDataAddService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.webservice.common.Servlets;

/**
 * 报读相关信息处理
 */
@Controller
@RequestMapping("/interface/signupDeal")
public class SignUpDealController {

    private final static Logger log = LoggerFactory.getLogger(SignUpDealController.class);

    @Autowired
    private GjtSignUpInfoDataService gjtSignUpInfoDataService;

    @Autowired
    private SignupDataAddService signupDataAddService;

    @Autowired
    private GjtStudentInfoService gjtStudentInfoService;

    @Autowired
    private GjtSyncLogService gjtSyncLogService;

    @Autowired
    private ApiOpenClassService apiOpenClassService;

    @Autowired
    private GjtFlowService gjtFlowService;

    @Autowired
	private GjtOrgService gjtOrgService;
    
    /**
     * 报读缴费状态确认，生成账号相关数据<br/>
     * 根据身份证来修改收费状态(0：已全额缴费，1：已部分缴费，2：待缴费，3：已欠费)
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @SysLog("报读缴费状态确认")
    @RequestMapping(value = "/chargeSignupNew",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> chargeSignupNew(@RequestParam String studentId,
                                              HttpServletRequest request) throws UnsupportedEncodingException{
        Map resultMap = new HashMap();
        Map requestParams = Servlets.getParametersStartingWith(request,"");
        GjtStudentInfo oldInfo = null;
        try {
            String describ = null;
            if (StringUtils.isNotBlank(ObjectUtils.toString(requestParams.get("describ")))){
                describ = URLDecoder.decode(ObjectUtils.toString(requestParams.get("describ")), "UTF-8");
            }
            requestParams.put("describ",describ);
            String charge = (String) requestParams.get("charge");
            oldInfo = gjtStudentInfoService.queryById(studentId);
            if(oldInfo == null) {
            	resultMap.put("success", "false");
                resultMap.put("message", "不存在该学员！");
                return resultMap;
            }
            /**
             * 根据培养层次是否存在学籍，若不存在学籍，则更改收费状态，若存在则无法操作；
             * 规则说明：同一学员只能报读一个专科/本科。
             */
            GjtStudentInfo existsInfo = signupDataAddService.queryExistsSignupDataByPycc(oldInfo.getXxId(), oldInfo.getSfzh(), oldInfo.getPycc());
            if(existsInfo == null) {
                resultMap = gjtSignUpInfoDataService.chargeSignupNew(requestParams);
                // 之前为待缴费改为已全额缴费或已部分缴费，则生成账号相关数据
                if ("2".equals(oldInfo.getGjtSignup().getCharge()) && ("0".equals(charge) || "1".equals(charge))
                        && "1".equals(ObjectUtils.toString(resultMap.get("result")))) {
                    // 记录收费状态更改为缴费
                    gjtSyncLogService.insert(new GjtSyncLog(oldInfo.getXm(), oldInfo.getXh(), Constants.RSBIZ_CODE_B0003, requestParams.toString(), resultMap.toString()));
                    try {
                        // 开启线程做其他事情
                        new Thread(new SignupDataRunnable(oldInfo.getStudentId(), signupDataAddService, gjtStudentInfoService, apiOpenClassService, gjtFlowService, gjtSyncLogService))
                                .start()
                        ;
                    } catch (Throwable e) {
                        // 开启线程做其他事情
                        new SignupDataRunnable(oldInfo.getStudentId(), signupDataAddService, gjtStudentInfoService, apiOpenClassService, gjtFlowService, gjtSyncLogService)
                                .run()
                        ;
                    }
                }
            } else {
                resultMap.put("success", "false");
                resultMap.put("message", "同一学员只能报读一个专科/本科");
            }
        } catch (Exception e) {
            resultMap.put("success","false");
            resultMap.put("message","未知异常：服务器异常！");
            log.error("chargeSignupNew fail ======== params:" + requestParams);
            log.error("chargeSignupNew fail ======== error:{} | {}", e, e.getMessage());
            // 记录同步失败日志
            gjtSyncLogService.insert(new GjtSyncLog(oldInfo != null ? oldInfo.getXm() : null, oldInfo != null ? oldInfo.getXh() : null, Constants.RSBIZ_CODE_B0003, requestParams.toString(), e.toString().length() > 500 ? e.toString().substring(0, 500) : e.toString()));
            e.printStackTrace();
        }
        return resultMap;
    }
    
    /**
     * 招生平台修改姓名、手机号码、招生机构（学习中心/招生点）
     * @param studentId
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateGjtStudentInfo",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateGjtStudentInfo(@RequestParam String studentId,HttpServletRequest request){
    	 Map resultMap = new HashMap();
    	 Map requestParams = Servlets.getParametersStartingWith(request,"");
    	 GjtStudentInfo studentInfo=gjtStudentInfoService.queryById(studentId);
    	 String studentName=ObjectUtils.toString(requestParams.get("studentName"),"").trim();//姓名
    	 String phone=ObjectUtils.toString(requestParams.get("phone"),"").trim();//手机号
    	 String studyCenterCode=ObjectUtils.toString(requestParams.get("studyCenterCode"),"").trim();//学习中心
    	 if(studentInfo==null){
    		 resultMap.put("success", "false");
             resultMap.put("message", "不存在该学员！");
             return resultMap;
    	 }
    	 if(StringUtils.isNotEmpty(studentName)){
    		 studentInfo.setXm(studentName);
    	 }
    	 if(StringUtils.isNotEmpty(phone)){
    		 studentInfo.setSjh(phone);
    	 }
    	 if(StringUtils.isNotEmpty(studyCenterCode)){
    		 GjtOrg learncenterOrg = gjtOrgService.queryByCode(studyCenterCode);
        	 if(learncenterOrg==null){
        		 resultMap.put("success", "false");
                 resultMap.put("message", "学习中心或招生点编码不存在：请确认学习中心或招生点是否已经同步!");
                 return resultMap;
        	 }else{
        		studentInfo.setGjtStudyCenter(new GjtStudyCenter(learncenterOrg.getId()));
        		studentInfo.setOrgId(learncenterOrg.getId());
        	 }
    	 }
    	 gjtStudentInfoService.updateEntityAndFlushCache(studentInfo);
    	 resultMap.put("success", "true");
    	 resultMap.put("message", "修改学员信息成功！");
    	 return resultMap;
    }
}
