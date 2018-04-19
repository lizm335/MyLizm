package com.ouchgzee.headTeacher.serviceImpl.flow;

import com.ouchgzee.headTeacher.dao.account.GjtUserAccountDao;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.flow.GjtFlowRecordDao;
import com.ouchgzee.headTeacher.dao.signup.GjtSignupDao;
import com.ouchgzee.headTeacher.dao.signup.GjtSignupDataDao;
import com.ouchgzee.headTeacher.dao.student.GjtStudentInfoDao;
import com.ouchgzee.headTeacher.pojo.flow.BzrGjtFlowRecord;
import com.ouchgzee.headTeacher.service.flow.BzrGjtFlowService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 审核流程逻辑类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年02月11日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Service("bzrGjtFlowServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtFlowServiceImpl extends BaseServiceImpl<BzrGjtFlowRecord> implements BzrGjtFlowService {

    @Autowired
    private GjtFlowRecordDao gjtFlowRecordDao;

    @Autowired
    private GjtSignupDao gjtSignupDao;

    @Autowired
    private GjtSignupDataDao gjtSignupDataDao;

    @Autowired
    private GjtUserAccountDao gjtUserAccountDao;

    @Autowired
    private GjtStudentInfoDao gjtStudentInfoDao;

    @Override
    protected BaseDao<BzrGjtFlowRecord, String> getBaseDao() {
        return gjtFlowRecordDao;
    }

    @Override
    public boolean auditSignupData(String studentId, String auditState, String auditContent, String operatorId, String operatorRealName) {
        /*boolean pass = false;
        if((pass = "1".equals(auditState)) || "2".equals(auditState)) {
            Date now = new Date();
            int resultNum = gjtFlowRecordDao.auditSignupData(studentId, new BigDecimal(auditState), auditContent, operatorRealName, now);
            if(resultNum != 0) {
                String smsType = null;
                GjtUserAccount user = gjtUserAccountDao.findOne(operatorId);
                if(user != null) {
                    // 这个逻辑来源于旧的版本
                    if(StringUtils.contains(user.getGjtOrg().getCode(), "00010011")) {
                        smsType = "lgzz";
                    } else if(StringUtils.contains(user.getGjtOrg().getCode(), "00010007")) {
                        smsType = "gk";
                    }
                }
                GjtStudentInfo studentInfo = gjtStudentInfoDao.findOne(studentId);
                if(pass) {
                    gjtSignupDao.auditSignupData(studentId, auditState, auditContent, operatorId, now);
                    gjtSignupDataDao.auditSignupData(studentId, auditState, auditContent, operatorId, now);

                    try {
                        // 发送短信通知审核结果
                        SMSUtil.sendMessage(studentInfo.getSjh(),
                                String.format("%1$s学员你好，你提交的学籍资料已通过审核，学籍正在注册中，你可以前往%2$s官网（http://www.oucgz.cn）登录个人学习平台，查询学籍注册状态和开始学习！",
                                        studentInfo.getXm(),
                                        user.getGjtOrg().getOrgName()),
                                smsType);
                    } catch (Exception e) {}
                } else {
                    gjtSignupDao.auditSignupData(studentId, SignupAuditStateEnum.不通过.getValue() + "", auditContent, operatorId, now);

                    try {
                        // 发送短信通知审核结果
                        SMSUtil.sendMessage(studentInfo.getSjh(),
                                String.format("%1$s学员你好，你提交的学籍资料未通过审核，为了不影响你的学籍正常注册，请立即前往%2$s官网（http://www.oucgz.cn）登录个人学习平台，查看审核不通过原因，并按要求重新提交资料！",
                                        studentInfo.getXm(),
                                        user.getGjtOrg().getOrgName()),
                                smsType);
                    } catch (Exception e) {}
                }
            }
            return true;
        }*/
        return false;
    }
}
