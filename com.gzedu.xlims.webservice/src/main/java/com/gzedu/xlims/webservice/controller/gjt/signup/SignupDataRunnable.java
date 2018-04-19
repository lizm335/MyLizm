package com.gzedu.xlims.webservice.controller.gjt.signup;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.mail.util.MailUtil;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtSyncLog;
import com.gzedu.xlims.service.GjtSyncLogService;
import com.gzedu.xlims.service.api.ApiOpenClassService;
import com.gzedu.xlims.service.flow.GjtFlowService;
import com.gzedu.xlims.service.signup.SignupDataAddService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 学籍资料同步接口-线程<br/>
 * 功能说明：<br/>
 *      开启线程做其他事情，生成账号、发短信、同步EE、同步账号等。<br/>
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年08月30日
 * @version 3.0
 */
public class SignupDataRunnable implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(SignupDataRunnable.class);

    /**
     * 学员Id
     */
    private String studentId;

    private SignupDataAddService signupDataAddService;

    private GjtStudentInfoService gjtStudentInfoService;

    private ApiOpenClassService apiOpenClassService;

    private GjtFlowService gjtFlowService;

    private GjtSyncLogService gjtSyncLogService;

    public SignupDataRunnable(String studentId, SignupDataAddService signupDataAddService, GjtStudentInfoService gjtStudentInfoService,
                              ApiOpenClassService apiOpenClassService, GjtFlowService gjtFlowService, GjtSyncLogService gjtSyncLogService) {
        this.studentId = studentId;
        this.signupDataAddService = signupDataAddService;
        this.gjtStudentInfoService = gjtStudentInfoService;
        this.apiOpenClassService = apiOpenClassService;
        this.gjtFlowService = gjtFlowService;
        this.gjtSyncLogService = gjtSyncLogService;
    }

    @Override
    public void run() {
        GjtStudentInfo info = gjtStudentInfoService.queryById(studentId);
        // 收费状态 0-已全额缴费，1-已部分缴费，2-待缴费，3-已欠费
        if(info != null && ("0".equals(info.getGjtSignup().getCharge()) || "1".equals(info.getGjtSignup().getCharge()))) {
            // 报读缴费状态确认-删除预存学籍资料
            signupDataAddService.updateSignupDataByAwaitPayOrder(info.getGjtSchoolInfo().getId(), info.getSfzh(), info.getPycc(), info.getStudentId());

            // 已缴费的学员生成账号、分配教学班
            Map<String, Object> result = signupDataAddService.addCreateUserAccountAndDivideIntoClasses(studentId);
            if(!((Boolean) result.get("successful"))) {
                log.error("syncSignupData fail ======== params:" + studentId);
                log.error("syncSignupData fail ======== result:" + result);
                // 记录同步失败日志
                gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0001, studentId, result.toString()));
                return;
            }

            // 报读成功且已缴费发送短信通知学员及班主任
            signupDataAddService.signupDataToSms(studentId);

            // 初始化学员选课记录
            Map<String, Object> formMap = new HashMap<String, Object>();
            formMap.put("STUDENT_ID", studentId);
            apiOpenClassService.initStudentChoose(formMap);
            
            // 尝试确认提交资料
			gjtFlowService.initAuditSignupInfo(studentId);

            // 生成学员EE号
            boolean isCreateEENo = signupDataAddService.createEENo(studentId);
            if (isCreateEENo) {
                // 生成EE群，且学员加入群
                signupDataAddService.createGroupNoAndAddSingleStudent(studentId);
            }

            /**
             * 账号同步至运营平台策略<br/>
             * 若是推送失败次数大于3次（3分钟请求一次，需要设置超时时间）则邮件通知luyantong@eenet.com、liangxuyang@eenet.com，<br/>
             * 内容：时间、账号、失败原因，并通知其使用系统管理员登录进行人工干预。
             */
            boolean syncFlag = false;
            String errorMessage = null;
            for (int i = 0; i < 3; i++) {
                Map<String, Object> resultSync = signupDataAddService.syncYunYingCenter(studentId);
                syncFlag = (Boolean) resultSync.get("successful");
                if (syncFlag) {
                    break;
                } else {
                    errorMessage = (String) resultSync.get("message");
                    try {
                        Thread.sleep(3 * 60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!syncFlag && StringUtils.equals(AppConfig.getProperty("devMode"), "3")) {
                // 生产环境才发邮件
                MailUtil.sendHtmlMail(new String[]{"luyantong@eenet.com", "liangxuyang@eenet.com"},
                        "账号同步至运营平台失败通知",
                        String.format("<table style='border: solid 1px;'><tbody><tr><td>时间：</td><td>%s</td></tr><tr><td>账号：</td><td>%s</td></tr><tr><td>失败原因：</td><td>%s</td></tr></tbody></table><br/>请使用超级管理员账号登录教学教务后台手动同步！",
                                DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"),
                                gjtStudentInfoService.queryById(studentId).getGjtUserAccount().getLoginAccount(),
                                errorMessage
                        )
                );
            }
        }
    }

}
