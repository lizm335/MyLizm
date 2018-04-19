/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.controller.index;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.studymanage.StudyManageService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.ouchgzee.study.web.common.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学员链接
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2018年01月17日
 * @version 2.5
 */
@RestController
@RequestMapping("/pcenter/link")
public class LinkController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(LinkController.class);

    @Autowired
    private StudyManageService studyManageService;

    @Autowired
    private GjtStudentInfoService gjtStudentInfoService;

    /**
     * 获取链接信息
     * @return
     */
    @RequestMapping(value = "getLinkInfo", method = RequestMethod.GET)
    public Map<String, Object> getBaseInfo(HttpServletRequest request) throws CommonException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        GjtStudentInfo student = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());

        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("XX_ID", student.getXxId());
        searchParams.put("STUDENT_ID", student.getStudentId());

        List<Map<String, Object>> list = studyManageService.getStudentLinkList(searchParams);
        if(list != null && list.size() > 0) {
            Map e = list.get(0);

            BigDecimal pcLastLoginTime = (BigDecimal) e.get("PC_LAST_LOGIN_TIME");
            String pcIsOnline = (String) e.get("PC_IS_ONLINE");
            if(pcLastLoginTime != null) {
                resultMap.put("isUsePC", 1);
                if(StringUtils.equals(pcIsOnline, Constants.BOOLEAN_YES)) {
                    resultMap.put("isOnlinePC", 1);
                } else {
                    resultMap.put("isOnlinePC", 0);
                    resultMap.put("offlineDayPC", pcLastLoginTime.intValue());
                }
            } else {
                resultMap.put("isUsePC", 0);
            }
            BigDecimal appLastLoginTime = (BigDecimal) e.get("APP_LAST_LOGIN_TIME");
            String appIsOnline = (String) e.get("APP_IS_ONLINE");
            if(appLastLoginTime != null) {
                resultMap.put("isUseAPP", 1);
                if(StringUtils.equals(appIsOnline, Constants.BOOLEAN_YES)) {
                    resultMap.put("isOnlineAPP", 1);
                } else {
                    resultMap.put("isOnlineAPP", 0);
                    resultMap.put("offlineDayAPP", appLastLoginTime.intValue());
                }
            } else {
                resultMap.put("isUseAPP", 0);
            }
            resultMap.put("isBindingWx", e.get("IS_BANDING_WX"));
            resultMap.put("perfectStatus", e.get("PERFECT_STATUS"));
        } else {
            throw new CommonException(MessageCode.SYSTEM_ERROR);
        }
        return resultMap;
    }

    /**
     * 是否关注微信公众号
     * @return
     */
    @RequestMapping(value = "isBindingWx", method = RequestMethod.GET)
    public Map<String, Object> isBindingWx(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        resultMap.put("isBindingWx", StringUtils.isNotBlank(user.getWxOpenId()) ? 1 : 0);
        return resultMap;
    }

}
