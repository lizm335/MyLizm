package com.gzedu.xlims.third.eechat.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.third.eechat.data.EEIMGroup;
import com.gzedu.xlims.third.eechat.data.EEIMGroupReturnData;
import com.gzedu.xlims.third.eechat.data.EEIMMembersEENO;
import com.gzedu.xlims.third.eechat.data.EEIMUserNew;
import com.gzedu.xlims.third.eechat.data.EEIMcreateUserReturnData;
import com.gzedu.xlims.third.eechat.util.EEIMService;

/**
 * EEChat业务逻辑类<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年08月21日
 * @version 3.0
 */
@Service("eeChatService")
public class EEChatServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(EEChatServiceImpl.class);

    @Autowired
    private EEIMService eEIMService;

    /**
     * 同步单个学员，获取ee号
     * @param appId
     * @param eeimUser
     * @return
     * @throws Exception
     */
    public String syncSingleStudents(final String appId, EEIMUserNew eeimUser) {
        String eeno = null;
        // 业务对象 转 接口对象
        List<EEIMUserNew> users = new ArrayList<EEIMUserNew>();
        users.add(eeimUser);
        // 请求接口
        try {
            EEIMcreateUserReturnData resultData = eEIMService.singleRemoteB10(appId, users);
            if(Constants.BOOLEAN_1.equals(resultData.getSTATUS())) {
                // 处理同步成功的ＥＥ帐号
                List<EEIMMembersEENO> membersEENOlist = resultData.getMembersEENOlist();
                for (EEIMMembersEENO eeimMembersEENO : membersEENOlist) {
                    eeno = eeimMembersEENO.getEE_NO();
                    log.info("同步： -----EEno:" + eeno);
                }
                return eeno;
            } else {
                log.error("同步：fail:" + resultData.getMESSAGE());
                return null;
            }
        } catch (Exception e) {
            log.error("同步成功的ＥＥ帐号失败" + e.getMessage());
            throw new RuntimeException("同步学生EE帐号失败");
        }
    }

    /**
     * 创建EE群，获得ee群号
     * @param appId
     * @param eeimGroup
     * @return
     * @throws Exception
     */
    public String sysnCreateGroup(String appId, EEIMGroup eeimGroup) {
        // 业务对象 转 接口对象
        List<EEIMGroup> eeimGroups = new ArrayList<EEIMGroup>();
        eeimGroup.setAPP_ID(appId);
        eeimGroup.setGROUP_TYPE("Public");
        eeimGroups.add(eeimGroup);
        // 请求接口
        try {
            EEIMGroupReturnData resultData = eEIMService.remoteC5(eeimGroups);
            // STATUS 1-建群成功 0-失败 3-群已存在
            if(Constants.BOOLEAN_1.equals(resultData.getSTATUS()) || "3".equals(resultData.getSTATUS())) {
                // 处理同步成功的ＥＥ帐号
                String groupeeno = resultData.getGROUP_EEIM_NO();
                String classId = resultData.getGROUP_ID();
                log.info("同步群：groupID:" + classId + "-----EEno:" + groupeeno);
                return groupeeno;
            } else {
                log.error("同步群：fail:" + resultData.getMESSAGE());
                return null;
            }
        } catch (Exception e) {
            log.error("同步成功的EE群失败" + e.getMessage());
            throw new RuntimeException("同步EE群失败");
        }
    }

}
