package com.gzedu.xlims.webservice.controller.gjt.student;

import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.pojo.GjtGradeSpecialty;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.webservice.common.Servlets;
import com.gzedu.xlims.webservice.response.ResponseResult;
import com.gzedu.xlims.webservice.response.ResponseStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习中心的教学计划控制器
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2018年01月24日
 * @version 3.0
 */
@RestController("interfaceGjtTeachPlanController")
@RequestMapping("/interface/teachPlan")
public class GjtTeachPlanController {

    private final static Logger log = LoggerFactory.getLogger(GjtTeachPlanController.class);

    @Autowired
    private GjtTeachPlanService gjtTeachPlanService;

    @Autowired
    private GjtGradeSpecialtyService gjtGradeSpecialtyService;

    @Autowired
    private GjtOrgService gjtOrgService;

    @Autowired
    private CacheService cacheService;

    /**
     * 获取产品的专业信息接口<br/>
     * @param learncenterCode 学习中心CODE
     * @param termId 学期ID
     * @return
     */
    @RequestMapping(value = "/specialtyList", method = RequestMethod.GET)
    public ResponseResult specialtyList(HttpServletRequest request) {
        ResponseResult result;
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> requestParams = Servlets.getParametersStartingWith(request, null);
        String termId = (String) requestParams.get("termId");
        if (StringUtils.isEmpty(termId)) {
            return new ResponseResult(ResponseStatus.PARAM_ERROR, "学期不能为空!");
        }
        String learncenterCode = request.getParameter("learncenterCode");
        if (StringUtils.isEmpty(learncenterCode)) {
            return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心编码不能为空!");
        }
        GjtOrg learncenterOrg = gjtOrgService.queryByCode(learncenterCode);
        if (learncenterOrg == null) {
            return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心编码不存在：请确认学习中心是否已经同步!");
        }
        try {
            String xxId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(learncenterOrg.getId());
            Map<String, Object> searchParams = new HashMap<String, Object>();
            searchParams.put("EQ_gjtGrade.gradeId", termId);
            Page<GjtGradeSpecialty> page = gjtGradeSpecialtyService.queryAll(xxId, searchParams, new PageRequest(0, 1000));

            List<SpecialtyVo> specialtyVos = new ArrayList<SpecialtyVo>();
            for (GjtGradeSpecialty p : page.getContent()) {
                // 通用或者学习中心定制的产品
                if(p.getStudyCenterIds().size() == 0 || p.getStudyCenterIds().contains(learncenterOrg.getId())) {
                    SpecialtyVo vo = new SpecialtyVo();
                    vo.setSpecialtyId(p.getGjtSpecialty().getSpecialtyId());
                    vo.setZymc(p.getGjtSpecialty().getZymc());
                    specialtyVos.add(vo);
                }
            }

            data.put("data", specialtyVos);
            result = new ResponseResult(ResponseStatus.SUCCESS, data);
        } catch (Exception e) {
            result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, "服务器异常！");
            log.error("specialtyList fail ======== params:" + requestParams);
            log.error("specialtyList fail ======== error:{} | {}", e, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取产品的教学计划接口<br/>
     * @param learncenterCode 学习中心CODE
     * @param termId 学期ID
     * @param specialtyId 专业ID
     * @return
     */
    @SysLog("获取产品的教学计划接口")
    @RequestMapping(value = "/plan", method = RequestMethod.GET)
    public ResponseResult plan(HttpServletRequest request) {
        ResponseResult result;
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> requestParams = Servlets.getParametersStartingWith(request, null);
        String termId = (String) requestParams.get("termId");
        String specialtyId = (String) requestParams.get("specialtyId");
        if (StringUtils.isEmpty(termId)) {
            return new ResponseResult(ResponseStatus.PARAM_ERROR, "学期不能为空!");
        }
        if (StringUtils.isEmpty(specialtyId)) {
            return new ResponseResult(ResponseStatus.PARAM_ERROR, "专业不能为空!");
        }
        String learncenterCode = request.getParameter("learncenterCode");
        if (StringUtils.isEmpty(learncenterCode)) {
            return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心编码不能为空!");
        }
        GjtOrg learncenterOrg = gjtOrgService.queryByCode(learncenterCode);
        if (learncenterOrg == null) {
            return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心编码不存在：请确认学习中心是否已经同步!");
        }
        try {
            // 优先选择学习中心范围下的产品
            String studyCenterId = null;
            if (StringUtils.equals(learncenterOrg.getOrgType(), "3")) {
                studyCenterId = learncenterOrg.getId();
            } else if (StringUtils.equals(learncenterOrg.getParentGjtOrg().getOrgType(), "3")) {
                studyCenterId = learncenterOrg.getParentGjtOrg().getId();
            }
            GjtGradeSpecialty studyCenterGradeSpecialtyProduct = gjtGradeSpecialtyService.queryByGradeIdAndSpecialtyIdAndStudyCenterId(termId, specialtyId, studyCenterId);
            if (studyCenterGradeSpecialtyProduct == null) {
                studyCenterGradeSpecialtyProduct = gjtGradeSpecialtyService.queryByGradeIdAndSpecialtyId(termId, specialtyId);
                if (studyCenterGradeSpecialtyProduct == null) {
                    return new ResponseResult(1001, "不存在教学计划!");
                } else {
                    // 即是通用产品，不做判断判断
                }
            }
            // 根据产品ID可以获取到教学计划
            List<GjtTeachPlan> gjtTeachPlans = gjtTeachPlanService.queryGjtTeachPlan(studyCenterGradeSpecialtyProduct.getId());
            int maxKkxq = 0;
            for (GjtTeachPlan plan : gjtTeachPlans) {
                if (plan.getKkxq() > maxKkxq) {
                    maxKkxq = plan.getKkxq();
                }
            }
            List<TeachPlanVo> teachPlanVos = new ArrayList<TeachPlanVo>();
            for (int i = 1; i <= maxKkxq; i++) {
                TeachPlanVo teachPlanVo = new TeachPlanVo();
                teachPlanVo.setTermName(String.format("第%s学期", i == 1 ? "一" : i == 2 ? "二" : i == 3 ? "三" : i == 4 ? "四" : i == 5 ? "五" : "六"));
                List<TeachPlanDetailVo> detailVos = new ArrayList<TeachPlanDetailVo>();
                for (GjtTeachPlan p : gjtTeachPlans) {
                    if(p.getKkxq() == i) {
                        TeachPlanDetailVo vo = new TeachPlanDetailVo();
                        vo.setCourseName(p.getGjtReplaceCourse() != null ? p.getGjtReplaceCourse().getKcmc() : p.getGjtCourse().getKcmc());
                        vo.setExamType(p.getKsfs());
                        vo.setExamTypeName(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.EXAMINATIONMODE, p.getKsfs()));
                        vo.setCredit(p.getXf().intValue());
                        detailVos.add(vo);

                        teachPlanVo.setTotalCredit(teachPlanVo.getTotalCredit() + p.getXf().intValue());
                        teachPlanVo.setCourseNum(teachPlanVo.getCourseNum() + 1);
                    }
                }
                teachPlanVo.setTeachPlanDetailList(detailVos);
                teachPlanVos.add(teachPlanVo);
            }


            data.put("data", teachPlanVos);
            result = new ResponseResult(ResponseStatus.SUCCESS, data);
        } catch (Exception e) {
            result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, "服务器异常！");
            log.error("plan fail ======== params:" + requestParams);
            log.error("plan fail ======== error:{} | {}", e, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
