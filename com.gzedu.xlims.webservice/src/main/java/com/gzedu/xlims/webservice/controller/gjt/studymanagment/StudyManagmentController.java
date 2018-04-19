package com.gzedu.xlims.webservice.controller.gjt.studymanagment;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.ResultFeedback;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.studymanage.StudyManageService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.webservice.common.Servlets;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by llx on 2017/7/31.
 */

@RestController
@RequestMapping("/interface/studyManagment")
public class StudyManagmentController {

    @Autowired
    private StudyManageService studyManageService;

    @Autowired
    private GjtRecResultService gjtRecResultService;

    @Autowired
    private CommonMapService commonMapService;

    @Autowired
    private GjtClassStudentService gjtClassStudentService;

    @Autowired
    private GjtStudentInfoService gjtStudentInfoService;

    /**
     * 接口--学习管理学员总体概况列表
     * @param pageNumber
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "/list",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultFeedback studyManagmentList(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,HttpServletRequest request){
        ResultFeedback feedback = null;
        Map<String,Object> resultMap = new HashMap<String, Object>();
        Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
        PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
        if(EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID"),""))&&EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("XXZX_CODE"),""))){
            feedback = new ResultFeedback(false,"参数不能为空！","","");
            return feedback;
        }
        if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_CODE"),""))){
            String xxzxCode = ObjectUtils.toString(searchParams.get("XXZX_CODE"));
            Map<String, String> xxzxMap = commonMapService.getXxzxIdByXxzxCode(xxzxCode);
            if(EmptyUtils.isNotEmpty(xxzxMap)){
                searchParams.put("XXZX_ID",xxzxMap.keySet().iterator().next());
            }
        }
        Map<String, String> schoolMap = commonMapService.getxxIdByxxzxId(ObjectUtils.toString(searchParams.get("XXZX_ID")));
        if(EmptyUtils.isNotEmpty(schoolMap)){
            searchParams.put("XX_ID",schoolMap.keySet().iterator().next());
        }
        Page page;
        try {
            page = studyManageService.getStudentCourseList(searchParams, pageRequst);//
            searchParams.put("PASS_STATUS","Y");
            List<Map<String, String>> passList = studyManageService.getStudentCourseList(searchParams);
            searchParams.put("PASS_STATUS","N");
            List<Map<String, String>> unPassList = studyManageService.getStudentCourseList(searchParams);
            resultMap.put("passNum",passList.size());
            resultMap.put("unPassNum",unPassList.size());
            if(page!=null&&!page.hasContent()){
                feedback = new ResultFeedback(false,"暂无数据！","","");
            }else {
                List<Map<String,Object>> resultList = new ArrayList<Map<String, Object>>(10);
                List<Map<String, Object>> content = page.getContent();
                for (Map map:content){
                    Map<String,Object> tempMap = new HashMap<String, Object>();
                    tempMap.put("XM",map.get("XM"));
                    tempMap.put("PYCC_NAME",map.get("PYCC_NAME"));
                    tempMap.put("YEAR_NAME",map.get("YEAR_NAME"));
                    tempMap.put("GRADE_NAME",map.get("GRADE_NAME"));
                    tempMap.put("ZYMC",map.get("ZYMC"));
                    tempMap.put("REC_COUNT",map.get("REC_COUNT"));
                    tempMap.put("PASS_REC_COUNT",map.get("PASS_REC_COUNT"));
                    tempMap.put("SUM_GET_CREDITS",map.get("SUM_GET_CREDITS"));
                    tempMap.put("ZDBYXF",map.get("ZDBYXF"));
                    tempMap.put("ZXF",map.get("ZXF"));
                    tempMap.put("STUDENT_ID",map.get("STUDENT_ID"));
                    resultList.add(tempMap);
                }
                resultMap.put("size",page.getSize());//每页条数
                resultMap.put("pageNumber",page.getNumber());
                resultMap.put("lastPage",page.isLastPage());
                resultMap.put("totalPages",page.getTotalPages());
                resultMap.put("firstPage",page.isFirstPage());
                resultMap.put("numberOfElements",page.getNumberOfElements());
                resultMap.put("totalElements",page.getTotalElements());
                resultMap.put("hasNextPage",page.hasNextPage());
                resultMap.put("hasPreviousPage",page.hasPreviousPage());
                resultMap.put("content",resultList);
                feedback = new ResultFeedback(true,"获取数据成功！","json",resultMap);
            }
        }catch (Exception e){
            e.printStackTrace();
            feedback = new ResultFeedback(false,"接口异常！","","");
        }
        return feedback;
    }

    /**
     * 接口--学习管理学员详情
     * @param request
     * @return
     */
    @RequestMapping(value = "/details",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultFeedback studyManagmentDetails(HttpServletRequest request){
        ResultFeedback feedback = null;
        Map<String,Object> resultMap = new HashMap<String, Object>();
        Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
        if(EmptyUtils.isEmpty(searchParams)&&EmptyUtils.isEmpty(searchParams.get("STUDENT_ID"))){
            feedback = new ResultFeedback(false,"参数不能为空！");
            return feedback;
        }
        searchParams.put("studentId",ObjectUtils.toString(searchParams.get("STUDENT_ID")));
        GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(ObjectUtils.toString(searchParams.get("STUDENT_ID")));
        GjtClassInfo classInfo = gjtClassStudentService.queryTeachClassInfoByStudetnId(ObjectUtils.toString(searchParams.get("STUDENT_ID")));
        if(EmptyUtils.isNotEmpty(classInfo)){
            searchParams.put("classId",classInfo.getClassId());
        }
        if(EmptyUtils.isNotEmpty(studentInfo)){
            searchParams.put("xxId",studentInfo.getXxId());
        }
        try {
            Map<String, Object> conditionMap = gjtRecResultService.queryStuStudyCondition(searchParams);//学情信息总览
            if(EmptyUtils.isNotEmpty(conditionMap)){
                Map<String,Object> previewMap = new HashMap<String, Object>();
                previewMap.put("XM",conditionMap.get("XM"));
                previewMap.put("SEX",conditionMap.get("SEX"));
                previewMap.put("YEAR_NAME",conditionMap.get("NAME"));
                previewMap.put("GRADE_NAME",conditionMap.get("GRADE_NAME"));
                previewMap.put("PYCC_NAME",conditionMap.get("PYCC_NAME"));
                previewMap.put("ZYMC",conditionMap.get("ZYMC"));
                previewMap.put("ZXF",conditionMap.get("ZXF"));
                previewMap.put("ZDBYXF",conditionMap.get("CREDITS_MIN"));
                previewMap.put("GET_CREDITS",conditionMap.get("CREDITS_COUNT"));
                previewMap.put("COURSE_COUNT",conditionMap.get("COURSE_COUNT"));
                previewMap.put("PASS_COURSE",conditionMap.get("PASS_COURSE"));
                previewMap.put("EXAM_COUNT",conditionMap.get("EXAM_COUNT"));
                previewMap.put("PASS_EXAM",conditionMap.get("PASS_EXAM"));
                previewMap.put("ACT_PROGRESS",ObjectUtils.toString(conditionMap.get("ACT_PROGRESS"),""));
                resultMap.put("previewMap",previewMap);
            }
            List<Map<String, String>> conditionDetail = gjtRecResultService.queryStudentRecResultLearningDetail(searchParams);//学期课程明细列表
            if(EmptyUtils.isNotEmpty(conditionDetail)){
                List<Map<String,String>> detailList = new ArrayList<Map<String, String>>();
                for (Map<String,String> tempMap:conditionDetail){
                    Map<String,String> detailMap = new HashMap<String, String>();
                    detailMap.put("GRADE_NAME",tempMap.get("TERM_NAME"));
                    detailMap.put("GRADE_ID",tempMap.get("TERM_ID"));
                    detailMap.put("KCLBM_NAME",tempMap.get("KCLBM_NAME"));
                    detailMap.put("KCMC",tempMap.get("KCMC"));
                    detailMap.put("KSFS_NAME",ObjectUtils.toString(tempMap.get("KSFS_NAME"),""));
                    detailMap.put("GET_CREDITS",tempMap.get("GET_CREDITS"));
                    detailMap.put("XF",tempMap.get("XF"));
                    detailMap.put("EXAM_SCORE",ObjectUtils.toString(tempMap.get("EXAM_SCORE"),""));
                    detailMap.put("EXAM_SCORE1",ObjectUtils.toString(tempMap.get("EXAM_SCORE1"),""));
                    detailMap.put("EXAM_SCORE2",ObjectUtils.toString(tempMap.get("EXAM_SCORE2"),""));
                    if("0".equals(ObjectUtils.toString(tempMap.get("LOGIN_COUNT"),"0"))){
                        detailMap.put("EXAM_STATE","4");//未学习：未登陆过状态为未学习
                    }else {
                        detailMap.put("EXAM_STATE",tempMap.get("EXAM_STATE"));//
                    }
                    String state = ObjectUtils.toString(tempMap.get("STATE"), "");
                    if("0".equals(state)){
                        detailMap.put("STATE","未学习");//0 未学习 1落后 2 正常 3 学霸 4 考核通过
                    }else if("1".equals(state)){
                        detailMap.put("STATE","落后");
                    }else if("2".equals(state)){
                        detailMap.put("STATE","正常");
                    }else if("3".equals(state)){
                        detailMap.put("STATE","学霸");
                    }else if("4".equals(state)){
                        detailMap.put("STATE","考核通过");
                    }else {
                        detailMap.put("STATE","");
                    }
                    int class_avg_logins = Integer.valueOf(ObjectUtils.toString(tempMap.get("CLASS_AVG_LOGINS"), "0"));
                    int login_count = Integer.valueOf(ObjectUtils.toString(tempMap.get("LOGIN_COUNT"), "0"));
                    if(login_count>=class_avg_logins){
                        detailMap.put("KAOQIN_STATUS","考勤正常");//考勤状态
                    }else {
                        detailMap.put("KAOQIN_STATUS","考勤较差");//考勤状态
                    }
                    detailMap.put("SIGN_URL", AppConfig.getProperty("oclassUrl","http://oclass.oucnet.cn")+"/book/index/student/urlLogin.do?formMap.USER_INFO=" + EncryptUtils.encrypt(tempMap.get("CHOOSE_ID") + "," + tempMap.get("STUDENT_ID")) + "&formMap.IS_MANAGER=Y&&formMap.IS_TEST=Y");
                    detailList.add(detailMap);
                }
                resultMap.put("detailMap",detailList);
            }
            feedback = new ResultFeedback(true,"获取数据成功！","json",resultMap);
        }catch (Exception e){
            e.printStackTrace();
            feedback = new ResultFeedback(false,"接口异常","","");
        }


        return feedback;
    }



}
