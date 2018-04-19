package com.gzedu.xlims.webservice.controller.gjt.student;

import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.dto.StudentSynthesizeInfoDto;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.webservice.common.Servlets;
import com.gzedu.xlims.webservice.response.ResponseResult;
import com.gzedu.xlims.webservice.response.ResponseStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 学员综合信息控制器
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2018年02月25日
 * @version 3.0
 */
@RestController
@RequestMapping("/interface/sudentSynthesize")
public class StudentSynthesizeController {

    private final static Logger log = LoggerFactory.getLogger(StudentSynthesizeController.class);

    @Autowired
    private GjtStudentInfoService gjtStudentInfoService;

    @Autowired
    private GjtOrgService gjtOrgService;

    /**
     * 学员综合信息查询接口<br/>
     * @param learncenterCode 学习中心CODE
     * @param termId 学期ID
     * @param xm 姓名
     * @param page 当前页,默认1
     * @param pageSize 当前页大小,默认10
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseResult specialtyList(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, ModelMap model,
                                        HttpServletRequest request) {
        ResponseResult result;
        PageRequest pageRequst = Servlets.buildPageRequest(page, pageSize);
        Map<String, Object> requestParams = Servlets.getParametersStartingWith(request, null);
        String learncenterCode = request.getParameter("learncenterCode");
        String termId = (String) requestParams.get("termId");
        String xm = (String) requestParams.get("xm");
        if (StringUtils.isEmpty(learncenterCode)) {
            return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心编码不能为空!");
        }
        GjtOrg learncenterOrg = gjtOrgService.queryByCode(learncenterCode);
        if (learncenterOrg == null) {
            return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心编码不存在：请确认学习中心是否已经同步!");
        }
        try {
            Map<String, Object> searchParams = new HashMap<String, Object>();
            searchParams.put("EQ_studyId", learncenterOrg.getId());
            searchParams.put("GRADE_ID", termId);
            searchParams.put("XM", xm);
            Page<StudentSynthesizeInfoDto> infos = gjtStudentInfoService.queryStudentSynthesizeListByPage(searchParams, pageRequst);

            result = new ResponseResult(ResponseStatus.SUCCESS, infos);
        } catch (Exception e) {
            result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, "服务器异常！");
            log.error("specialtyList fail ======== params:" + requestParams);
            log.error("specialtyList fail ======== error:{} | {}", e, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
