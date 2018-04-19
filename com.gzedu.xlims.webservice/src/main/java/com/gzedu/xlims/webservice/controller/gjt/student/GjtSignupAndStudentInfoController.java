package com.gzedu.xlims.webservice.controller.gjt.student;

import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.flow.GjtFlowRecord;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.flow.GjtFlowService;
import com.gzedu.xlims.service.studentClass.GjtStudentClassInfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.webservice.common.Servlets;
import com.gzedu.xlims.webservice.response.ResponseResult;
import com.gzedu.xlims.webservice.response.ResponseStatus;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("/api/student")
public class GjtSignupAndStudentInfoController {

    private final static Logger log = LoggerFactory.getLogger(GjtSignupAndStudentInfoController.class);

    @Autowired
    private GjtStudentClassInfoService gjtStudentClassInfoService;

    @Autowired
    private GjtStudentInfoService gjtStudentInfoService;

    @Autowired
    private GjtFlowService gjtFlowService;

    @Autowired
    private CacheService cacheService;

    /**
     * 批量获取学员学籍信息接口<br>
     * 请求参数：studentIds 多个studentId以英式分号隔开(',')<br>
     * 			USER_TYPE 学员类型<br>
     * 			XJZT 学籍状态<br>
     * 			AUDIT_STATE 学籍资料审核状态<br>
     * 响应格式：{"code": 200, "content": ...}/{"code": 500, "message": "服务器异常"}<br>
     * data内的参数描述：<br>
     *
     * @return
     */
    @RequestMapping(value = "/queryBatchSingupInfo")
    @ResponseBody
    public ResponseResult queryBatchSingupInfo(HttpServletRequest request) throws CommonException,UnsupportedEncodingException {
        ResponseResult result;
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, null);
        try {
            if (EmptyUtils.isEmpty(searchParams.get("studentIds"))){
                return new ResponseResult(ResponseStatus.PARAM_ERROR, "studentIds不能为空!");
            }
            String collegeCode = (String) searchParams.get("collegeCode");
            if (StringUtils.isEmpty(collegeCode)) {
                return new ResponseResult(ResponseStatus.PARAM_ERROR, "所属院校不能为空!");
            }

            Map<String,Object> params = new HashMap<String,Object>();
            String[] studentIds = ObjectUtils.toString(searchParams.get("studentIds"),"").split(",");
            params.put("collegeCodes", StringUtils.isNotBlank(collegeCode)?collegeCode.split(","):null);
            String learncenterCode = ObjectUtils.toString(searchParams.get("learncenterCode"));
            params.put("learncenterCodes", StringUtils.isNotBlank(learncenterCode)?learncenterCode.split(","):null);
            params.put("XM",searchParams.get("XM"));
            params.put("SFZH",searchParams.get("SFZH"));
            params.put("USER_TYPE",searchParams.get("USER_TYPE"));
            params.put("XJZT",searchParams.get("XJZT"));

            params.put("sjh", request.getParameter("sjh"));
            params.put("ZSD_BM", request.getParameter("zsdBm"));// 招生点编码
            params.put("ZSR_ID", request.getParameter("zsrId"));// 招生人id
            params.put("orderSn", StringUtils.isNotBlank(request.getParameter("orderSn")) ? request.getParameter("orderSn").split(",") : null); // 订单号
            params.put("termId", request.getParameter("termId")); // 学期ID
            params.put("specialtyCode", request.getParameter("specialtyCode")); // 专业代码

            params.put("AUDIT_STATE",searchParams.get("AUDIT_STATE"));
            String flowAuditOperatorRole = ObjectUtils.toString(searchParams.get("FLOW_AUDIT_OPERATOR_ROLE"));
            String flowAuditState = ObjectUtils.toString(searchParams.get("FLOW_AUDIT_STATE"));
            params.put("FLOW_AUDIT_OPERATOR_ROLE", EmptyUtils.isNotEmpty(flowAuditOperatorRole) ? NumberUtils.toInt(flowAuditOperatorRole) : null); // 审核记录当前审核人角色
            params.put("FLOW_AUDIT_STATE", flowAuditState); // 审核记录当前审核人的审核状态
            params.put("PERFECT_STATUS",searchParams.get("PERFECT_STATUS")); // 资料完善状态 1-已完善 0-未完善 禁止再传参数FLOW_AUDIT_OPERATOR_ROLE、 FLOW_AUDIT_STATE
            params.put("condFlow",StringUtils.isNotBlank(flowAuditOperatorRole) && StringUtils.isNotBlank(flowAuditState) ? 1 : StringUtils.isNotBlank(flowAuditOperatorRole) ? 2 : 0);

            List<Map<String,Object>> studentList = new ArrayList<Map<String, Object>>();
            int maxSize = 1000;
            int i = 0; // copy次数
            int size = (studentIds.length - (i + 1) * maxSize > 0) ? maxSize : (studentIds.length - i * maxSize); // copy个数
            while (size > 0){
                String[] studentsParam = new String[size];
                System.arraycopy(studentIds, i * maxSize, studentsParam, 0, size);

                params.put("studentIds", studentsParam);
                studentList.addAll(gjtStudentClassInfoService.queryStudentSignupInfoByAtIds(params));
                i++;
                size = (studentIds.length - (i + 1) * maxSize > 0) ? maxSize : (studentIds.length - i * maxSize); // copy个数
            }

            for (Iterator<Map<String,Object>> iter = studentList.iterator();iter.hasNext();){
                Map<String,Object> student = iter.next();
                student.put("XZ", "2.5年");
                student.put("ACADEMIC", "2.5年");
                if (EmptyUtils.isEmpty(student.get("EXEDUCERTIFICATE"))){
                    student.put("EXEDUCERTIFICATE", "身份证");
                }
                String pycc = (String) student.get("PYCC");
                student.put("isUndergraduateCourse", gjtStudentInfoService.isUndergraduateCourse(pycc) ? 1 : 0);
                student.put("XB", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.SEX, (String) student.get("XBM")));
                student.put("PYCC_NAME", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.TRAININGLEVEL, pycc));
            }
            result = new ResponseResult(ResponseStatus.SUCCESS, studentList);
        } catch (Exception e) {
            result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, "服务器异常！");
            log.error("countSingupNum fail ======== params:" + searchParams);
            log.error("countSingupNum fail ======== error:{} | {}", e, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取学员学籍信息，带分页接口<br>
     * 请求参数：USER_TYPE 学员类型<br>
     * 			XJZT 学籍状态<br>
     * 			AUDIT_STATE 学籍资料审核状态<br>
     * 响应格式：{"code": 200, "content": ...}/{"code": 500, "message": "服务器异常"}<br>
     * data内的参数描述：<br>
     *
     * @return
     */
    @RequestMapping(value = "/querySingupInfoByPage")
    @ResponseBody
    public ResponseResult querySingupInfoByPage(@RequestParam(defaultValue = "1") Integer page,
                                                         @RequestParam(defaultValue = "10") Integer pageSize,HttpServletRequest request) throws CommonException, UnsupportedEncodingException{
        ResponseResult result;
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, null);
        PageRequest pageRequst = Servlets.buildPageRequest(page, pageSize, null);
        try {
            String collegeCode = (String) searchParams.get("collegeCode");
            if (StringUtils.isEmpty(collegeCode)) {
                return new ResponseResult(ResponseStatus.PARAM_ERROR, "所属院校不能为空!");
            }

            Map<String,Object> params = new HashMap<String,Object>();
            params.put("collegeCodes", StringUtils.isNotBlank(collegeCode)?collegeCode.split(","):null);
            String learncenterCode = ObjectUtils.toString(searchParams.get("learncenterCode"));
            params.put("learncenterCodes", StringUtils.isNotBlank(learncenterCode)?learncenterCode.split(","):null);
            params.put("XM",searchParams.get("XM"));
            params.put("SFZH",searchParams.get("SFZH"));
            params.put("USER_TYPE",searchParams.get("USER_TYPE"));
            params.put("XJZT",searchParams.get("XJZT"));

            params.put("sjh", request.getParameter("sjh"));
            params.put("ZSD_BM", request.getParameter("zsdBm"));// 招生点编码
            params.put("ZSR_ID", request.getParameter("zsrId"));// 招生人id
            params.put("orderSn", StringUtils.isNotBlank(request.getParameter("orderSn")) ? request.getParameter("orderSn").split(",") : null); // 订单号
            params.put("termId", request.getParameter("termId")); // 学期ID
            params.put("specialtyCode", request.getParameter("specialtyCode")); // 专业代码

            params.put("AUDIT_STATE",searchParams.get("AUDIT_STATE"));
            String flowAuditOperatorRole = ObjectUtils.toString(searchParams.get("FLOW_AUDIT_OPERATOR_ROLE"));
            String flowAuditState = ObjectUtils.toString(searchParams.get("FLOW_AUDIT_STATE"));
            params.put("FLOW_AUDIT_OPERATOR_ROLE", EmptyUtils.isNotEmpty(flowAuditOperatorRole) ? NumberUtils.toInt(flowAuditOperatorRole) : null); // 审核记录当前审核人角色
            params.put("FLOW_AUDIT_STATE", flowAuditState); // 审核记录当前审核人的审核状态
            params.put("PERFECT_STATUS",searchParams.get("PERFECT_STATUS")); // 资料完善状态 1-已完善 0-未完善 禁止再传参数FLOW_AUDIT_OPERATOR_ROLE、 FLOW_AUDIT_STATE
            params.put("condFlow",StringUtils.isNotBlank(flowAuditOperatorRole) && StringUtils.isNotBlank(flowAuditState) ? 1 : StringUtils.isNotBlank(flowAuditOperatorRole) ? 2 : 0);
            params.put("advanceState", request.getParameter("advanceState")); // |预存状态 1-预存|
            
            Page<Map<String,Object>> studentList = gjtStudentClassInfoService.queryStudentSignupInfoByPage(params, pageRequst);
            for (Iterator<Map<String,Object>> iter = studentList.iterator();iter.hasNext();){
                Map<String, Object> student = iter.next();
                student.put("XZ", "2.5年");
                student.put("ACADEMIC", "2.5年");
                if (EmptyUtils.isEmpty(student.get("EXEDUCERTIFICATE"))){
                    student.put("EXEDUCERTIFICATE", "身份证");
                }
                String pycc = (String) student.get("PYCC");
                student.put("isUndergraduateCourse", gjtStudentInfoService.isUndergraduateCourse(pycc) ? 1 : 0);
                student.put("XB", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.SEX, (String) student.get("XBM")));
                student.put("PYCC_NAME", cacheService.getCachedDictionaryName(CacheService.DictionaryKey.TRAININGLEVEL, pycc));
            }
            result = new ResponseResult(ResponseStatus.SUCCESS, studentList);
        } catch (Exception e) {
            result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, "服务器异常！");
            log.error("countSingupNum fail ======== params:" + searchParams);
            log.error("countSingupNum fail ======== error:{} | {}", e, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取学员学籍数量
     * @param request
     * @return totalElements-总学籍数
     * 			waitCount-待审核学籍数
     *          passCount-已通过学籍数
     *          noPassCount-不通过学籍数
     *          currentRoleWaitCount-当前角色的待审核学籍数
     *          currentRolePassCount-当前角色的已通过学籍数
     *          currentRoleNoPassCount-当前角色的不通过学籍数
     *          noPerfectCount-资料未完善学籍数
     */
    @RequestMapping(value = "/countSingupInfoNum")
    @ResponseBody
    public ResponseResult countSingupInfoNum(HttpServletRequest request) {
        ResponseResult result;
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, null);
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            String collegeCode = (String) searchParams.get("collegeCode");
            if (StringUtils.isEmpty(collegeCode)) {
                return new ResponseResult(ResponseStatus.PARAM_ERROR, "所属院校不能为空!");
            }
            params.put("collegeCodes", collegeCode.split(","));
            String learncenterCode = (String) searchParams.get("learncenterCode");
            params.put("learncenterCodes", StringUtils.isNotBlank(learncenterCode) ? learncenterCode.split(",") : null);
            params.put("XM",searchParams.get("XM"));
            params.put("SFZH",searchParams.get("SFZH"));
            params.put("USER_TYPE",searchParams.get("USER_TYPE"));
            params.put("XJZT",searchParams.get("XJZT"));

            params.put("sjh", request.getParameter("sjh"));
            params.put("ZSD_BM", request.getParameter("zsdBm"));// 招生点编码
            params.put("ZSR_ID", request.getParameter("zsrId"));// 招生人id
            params.put("orderSn", StringUtils.isNotBlank(request.getParameter("orderSn")) ? request.getParameter("orderSn").split(",") : null); // 订单号
            params.put("termId", request.getParameter("termId")); // 学期ID
            params.put("specialtyCode", request.getParameter("specialtyCode")); // 专业代码

            String flowAuditOperatorRole = ObjectUtils.toString(searchParams.get("FLOW_AUDIT_OPERATOR_ROLE"));
            List<Map<String,Object>> countResultList = null;
            if (EmptyUtils.isNotEmpty(flowAuditOperatorRole)) {
                params.put("FLOW_AUDIT_OPERATOR_ROLE", EmptyUtils.isNotEmpty(flowAuditOperatorRole) ? NumberUtils.toInt(flowAuditOperatorRole) : null); // 审核记录当前审核人角色 1-学员 2-班主任 3-招生办 4-学籍科主任
                countResultList = gjtStudentClassInfoService.countStudentSignupNum(params);
            } else {
                countResultList = gjtStudentClassInfoService.countStudentSignupNumAll(params);
            }
            result = new ResponseResult(ResponseStatus.SUCCESS, countResultList);
        } catch (Exception e) {
            result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, "服务器异常！");
            log.error("countSingupNum fail ======== params:" + searchParams);
            log.error("countSingupNum fail ======== error:{} | {}", e, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取学员是否已完善资料
     * @param request
     * @return perfectStatus 1-已完善
     *                        0-未完善，进入第一步标识-确认个人信息
     *                        2-进入第二步标识-确认通讯信息
     *                        3-进入第三步标识-确认报读信息
     *                        4-进入第四步标识-确认原最高学历
     *                        5-进入第五步标识-确认证件信息
     *                        6-进入第六步标识-确认签名
     */
    @RequestMapping(value = "/isPerfect", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult isPerfect(HttpServletRequest request){
        ResponseResult result;
        Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
        Map resultMap = new LinkedHashMap();

        String studentId = ObjectUtils.toString(searchParams.get("studentId"),"").trim();
        if (EmptyUtils.isEmpty(studentId)){
            return new ResponseResult(ResponseStatus.PARAM_ERROR, "studentId不能为空");
        }
        int perfectStatus = gjtStudentClassInfoService.isPerfect(searchParams);
        resultMap.put("perfectStatus", perfectStatus);
        result = new ResponseResult(ResponseStatus.SUCCESS, resultMap);
        return result;
    }

    /**
     * 更改学员学习状态<br/>
     * 比如三个月未缴费（逾期）学员停止学习，逾期学费全部交清学员改为正常学习
     * @param studentId 学员唯一标识，多个studentId以英式分号隔开(',')
     * @param learningState 学习状态 1-正常学习 2-停止学习
     * @return
     */
    @RequestMapping(value = "/updateLearningState", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult updateLearningState(@RequestParam String studentId,
                                              @RequestParam int learningState,
                                              HttpServletRequest request){
        ResponseResult result;
        if (EmptyUtils.isEmpty(studentId)){
            return new ResponseResult(ResponseStatus.PARAM_ERROR, "studentId不能为空!");
        }
        if (learningState != 1 && learningState != 2){
            return new ResponseResult(ResponseStatus.PARAM_ERROR, "learningState参数有误");
        }
        List<GjtStudentInfo> list = new ArrayList<GjtStudentInfo>();
        String[] ids = studentId.split(",");
        for (String id : ids) {
            GjtStudentInfo info = gjtStudentInfoService.queryById(id);
            if(info == null) {
                return new ResponseResult(ResponseStatus.UNKNOW_ERROR, "学员不存在!");
            }
            list.add(info);
        }
        // 批量修改
        for (GjtStudentInfo info : list) {
            info.setLearningState(learningState);
            gjtStudentInfoService.saveEntity(info);
        }
        result = new ResponseResult(ResponseStatus.SUCCESS, null);
        return result;
    }

    /**
     * 获取学籍资料审核记录<br>
     * @param studentId
     * @return
     */
    @RequestMapping(value = "/getAuditSignupInfoFlowList", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult getAuditSignupInfoFlowList(@RequestParam String studentId,
                                              HttpServletRequest request){
        ResponseResult result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        
        List<Map<String, Object>> infos = new ArrayList<Map<String, Object>>();
        List<GjtFlowRecord> flowRecordList = gjtFlowService.queryFlowRecordByStudentId(studentId);
        for (GjtFlowRecord flowRecord : flowRecordList) {
            Map<String, Object> info = new HashMap<String, Object>();
            info.put("auditDt", flowRecord.getAuditDt() != null ? FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(flowRecord.getAuditDt()) : null);
            info.put("auditOperator", flowRecord.getAuditOperator());
            info.put("auditContent", flowRecord.getAuditContent());
            info.put("auditOperatorRole", flowRecord.getAuditOperatorRole());
            info.put("auditState", flowRecord.getAuditState());
            infos.add(info);
        }
        resultMap.put("infos", infos);
        result = new ResponseResult(ResponseStatus.SUCCESS, resultMap);
        return result;
    }

}
