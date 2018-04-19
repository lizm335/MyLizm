package com.gzedu.xlims.webservice.controller.gjt.student;

import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.service.studentClass.GjtStudentClassInfoService;
import com.gzedu.xlims.webservice.common.Servlets;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/clazz")
public class GjtStudentAndClassController {

    private static final Logger log = Logger.getLogger(GjtStudentAndClassController.class);

    @Autowired
    private GjtStudentClassInfoService gjtStudentClassInfoService;


    /**
     * 教学班查询-->对应接口平台api中旧接口：/api/clazz/search.do
     * @param page
     * @param pageSize
     * @param request
     * @return
     * @throws CommonException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/search")
    @ResponseBody
    public Map<String,Object> getTeachClassSearchInfo(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                HttpServletRequest request) throws CommonException,UnsupportedEncodingException{
        Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
        PageRequest pageRequst = Servlets.buildPageRequest(page, pageSize, null);
        Map resultMap = new LinkedHashMap();

        String xxId = ObjectUtils.toString(searchParams.get("xxId"),"").trim();
        if (EmptyUtils.isEmpty(xxId)){
            resultMap.put("result","0");
            resultMap.put("message","xxId不能为空！");
            return resultMap;
        }

        Page pageInfo = gjtStudentClassInfoService.getTeachClassInfo(searchParams,pageRequst);
        resultMap.put("pageInfo", pageInfo);

        return resultMap;
    }


    /**
     * 获取教学班学员信息--->/api/clazz/listTeachClassStudentInfo.do
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/listTeachClassStudentInfo")
    @ResponseBody
    public Map<String,Object> listTeachClassStudentInfo(@RequestParam(defaultValue = "1") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                                        HttpServletRequest request){
        Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
        PageRequest pageRequest = Servlets.buildPageRequest(page,pageSize,null);
        Map resultMap = new LinkedHashMap();

        String classId = ObjectUtils.toString(searchParams.get("classId"),"").trim();
        if (EmptyUtils.isEmpty(classId)){
            resultMap.put("result","0");
            resultMap.put("message","classId不能为空！");
            return resultMap;
        }

        Page pageInfo = gjtStudentClassInfoService.listTeachClassStudentInfo(searchParams,pageRequest);
        resultMap.put("pageInfo", pageInfo);

        return resultMap;
    }


    /**
     * 获取教学班信息--->/api/clazz/getTeachClassInfo.do
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/getTeachClassInfo")
    @ResponseBody
    public Map<String,Object> getTeachClassInfo(HttpServletRequest request){
        Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
        Map resultMap = new LinkedHashMap();

        String classId = ObjectUtils.toString(searchParams.get("classId"),"").trim();
        if (EmptyUtils.isEmpty(classId) ){
            resultMap.put("result","0");
            resultMap.put("message","classId不能为空！");
            return resultMap;
        }
        resultMap = gjtStudentClassInfoService.getTeachClassInfo(searchParams);

        return resultMap;
    }

}
