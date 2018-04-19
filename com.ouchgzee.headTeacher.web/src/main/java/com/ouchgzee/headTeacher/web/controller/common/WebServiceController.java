package com.ouchgzee.headTeacher.web.controller.common;

import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.service.BzrEmployeeWebService;
import com.ouchgzee.headTeacher.web.common.Servlets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by paul on 2017/2/14.
 * 公共接口调用
 */
@Controller
@RequestMapping("/headTeacher/webservice")
public class WebServiceController {


    @Autowired
    private BzrEmployeeWebService employeeWebService;

    /**
     * @return 根据session获取登录信息, 查询班主任教导的班级列表
     */
    @RequestMapping(value = "/classList")
    @ResponseBody
    public List classList(HttpSession session) {
        BzrGjtEmployeeInfo employeeInfo = (BzrGjtEmployeeInfo) session.getAttribute(Servlets.SESSION_EMPLOYEE_NAME);
        return employeeWebService.classList(employeeInfo);
    }


    /**
     *
     * @param classid   班级ID
     * @param oldStus   已选择学员IDs
     * @return  已选择跟未选择的学生列表{STUNAME,STUID,STUPHONE}
     */
    @RequestMapping(value = "/stuList")
    @ResponseBody
    public Map stuList(@RequestParam("classid") String classid, @RequestParam("oldStus") String oldStus) {
        Map re = new HashMap();
        if (StringUtils.isEmpty(classid)) {
            return re;
        }
        return employeeWebService.stuList(classid,oldStus);
    }


}
