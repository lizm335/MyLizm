package com.ouchgzee.study.web.controller.graduation;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtil;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.mail.util.MailUtil;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtApplyDegreeCertif;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyCertif;
import com.gzedu.xlims.pojo.graduation.GjtGraduationPlan;
import com.gzedu.xlims.pojo.graduation.GjtGraduationRegister;
import com.gzedu.xlims.pojo.status.ThesisStatusEnum;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.graduation.*;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.thesis.GjtThesisApplyService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by bobolin on 2018/4/3.
 */

@Controller
@RequestMapping("/pcenter/graduation/certif")
public class GraduationCertifController {


    private final static Logger log = LoggerFactory.getLogger(GraduationCertifController.class);

    @Autowired
    private GjtGraduationApplyCertifService gjtGraduationRecordService;

    @Autowired
    private GjtStudentInfoService gjtStudentInfoService;

    @Autowired
    private GjtGradeService gjtGradeService;

    @Autowired
    private GjtApplyDegreeCertifService gjtApplyDegreeCertifService;

    @Autowired
    private GjtGraduationPlanService gjtGraduationPlanService;

    @Autowired
    private GjtThesisApplyService gjtThesisApplyService;

    @Autowired
    private GjtGraduationRegisterService gjtGraduationRegisterService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private GjtCertificateRecordService gjtCertificateRecordService;

    @Autowired
    private GjtGraduationApplyCertifService gjtGraduationApplyCertifService;

    /**
     * 毕业管理首页（新）
     *
     * @param request
     * @param session
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "/main")
    @ResponseBody
    public Map<String, Object> graduationMain(HttpServletRequest request, HttpSession session) throws CommonException {
        Map<String, Object> result = Maps.newHashMap();
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        // 查询学生信息
        GjtStudentInfo student = gjtStudentInfoService.queryByAccountId(user.getId());
        // 查询当前的学期id
        String currentGradeId = gjtGradeService.getCurrentGradeId(student.getXxId());
        // 查询该学生当前是第几学期
        long currentGrade = gjtGradeService.getStudentCurrentTerm(currentGradeId, student.getStudentId());
        //总学分
        long creditTotal = gjtGradeService.getStudentCreditTotal(student.getStudentId());
        //已经获得学分
        long credit = gjtGradeService.getStudentCurrentCredit(student.getStudentId());
        //毕业计划
        GjtGraduationPlan gjtGraduationPlan = gjtGraduationPlanService.queryByTermId(currentGradeId);
        //当前申请状态
        int state = 0;
        if (gjtGraduationPlan != null) {
            state = gjtGradeService.getGraduationStateByStudent(student, gjtGraduationPlan.getId());
        }

        //毕业状态
        result.put("STATE", state);
        //学习天数
        result.put("STUDY_DATE", DateUtil.bettweenDateByTime(student.getCreatedDt(), new Date()));
        //当前学生的学期
        result.put("CURRENT_TERM", currentGrade);
        //当前学分
        result.put("CREDIT", credit);
        //总学分
        result.put("CREDIT_TOTAL", creditTotal);

        //是否有毕业答辩
        Map<String, Object> map = Maps.newHashMap();
        map.put("EQ_studentId", student.getStudentId());
        map.put("EQ_status", ThesisStatusEnum.THESIS_COMPLETED.getValue());
        int HAS_DEFENCE;
        if (gjtThesisApplyService.count(map) > 0) {
            HAS_DEFENCE = 1;
        } else {
            HAS_DEFENCE = 0;
        }
        result.put("HAS_DEFENCE", HAS_DEFENCE);

        GjtGraduationApplyCertif gjtGraduationApplyCertif = null;
        if (gjtGraduationPlan != null) {
            gjtGraduationApplyCertif = gjtGraduationApplyCertifService.queryByStudentIdAndPlanId(student.getStudentId(),gjtGraduationPlan.getId());
        }
        //是否采集照片
        int HAS_PHOTO;
        if (gjtGraduationApplyCertif != null && !Strings.isNullOrEmpty(gjtGraduationApplyCertif.getPhotoUrl())) {
            HAS_PHOTO = 1;
        } else {
            HAS_PHOTO = 0;
        }

        //毕业申请已提交
        if (state == 6 || state == 7) {
            result.put("DELAY_GRADUATION_DT", DateUtil.bettweenDateByTime(gjtGraduationApplyCertif.getDelayGraduationDT(), new Date()));
        } else {
            result.put("DELAY_GRADUATION_DT", 0);
        }

        result.put("xh", student.getXh());
        result.put("xm", student.getXm());


        //是否提交毕业生登记表
        result.put("EXPRESS_NUMBER", "");
        int HAS_REGISTER = 0;
        result.put("HAS_REGISTER", HAS_REGISTER);
        if (gjtGraduationPlan != null) {
            GjtGraduationRegister register = gjtGraduationRegisterService.queryOneByStudentIdAndPlanId(student.getStudentId(), gjtGraduationPlan.getId());
            if (register != null && !Strings.isNullOrEmpty(register.getExpressNumber())) {
                HAS_REGISTER = 1;
                result.put("HAS_REGISTER", HAS_REGISTER);
                result.put("EXPRESS_NUMBER", register.getExpressNumber());
            }
        }
        String xjzt = cacheService.getCachedDictionaryName(CacheService.DictionaryKey.STUDENTNUMBERSTATUS, student.getXjzt()); // 学籍状态字典
//        List<CacheService.Value> list = cacheService.getCachedDictionary(CacheService.DictionaryKey.STUDENTNUMBERSTATUS); // 学籍状态字典
        result.put("HISTORY_STUDENT", 0);
        if ("毕业".equals(xjzt) && state != 8) {
            result.put("STATE", 8);
            result.put("HISTORY_STUDENT", 1);

            //历史数据已经毕业的学生的照片与登记表都为已经完成
            HAS_PHOTO = 1;
            HAS_REGISTER = 1;

        }
        result.put("HAS_PHOTO", HAS_PHOTO);
        result.put("HAS_REGISTER", HAS_REGISTER);

        //TODO 暂时查找允许申请的同学
        List<Map<String, Object>> students = Lists.newArrayList();
        if (gjtGraduationPlan != null) {
            students = gjtGradeService.queryGraduationStudent(1, gjtGraduationPlan.getId());
        }

        //毕业进度 1.入学学习;2.照片采集;3.毕业登记表;4.毕业答辩;5.毕业申请;6.领取证书
        int progressStu = 0;
        Map<String, Object> auditInfoMap = gjtStudentInfoService.getAuditInfo(user.getGjtStudentInfo().getStudentId());
        if ("毕业".equals(xjzt) && state != 8) {
            progressStu = 6;  //历史数据已经毕业的学生的照片与登记表都为已经完成
        }else if (null != auditInfoMap) {
            progressStu = Constants.BOOLEAN_1.equals(auditInfoMap.get("auditState")) == true ? 1 : 0; //入学学习

            Map<String, Object> searchParams = Maps.newHashMap();
            searchParams.put("EQ_studentId", student.getStudentId());

            if (gjtCertificateRecordService.count(searchParams) > 0){//该学生的毕业证已经在发放
                progressStu = 6;
            }else if (state == 4) {//毕业申请
                progressStu = 5;
            }else if (HAS_DEFENCE == 1) {//毕业答辩
                progressStu = 4;
            }else if (HAS_REGISTER == 1) {//毕业登记表
                progressStu = 3;
            }else if (HAS_PHOTO == 1) { //照片采集
                progressStu = 2;
            }
        }
        result.put("progressStu", progressStu);

        result.put("STUDENTS", students);
        return result;
    }

    /**
     * 毕业申请操作 满足毕业条件（专科）；state=1
     *
     * @param request
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "/apply1", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> apply1(HttpServletRequest request) throws CommonException {
        Map<String, Object> result = new HashedMap();

        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        // 查询学生信息
        GjtStudentInfo student = gjtStudentInfoService.queryByAccountId(user.getId());

        // 查询当前的学期id
        String currentGradeId = gjtGradeService.getCurrentGradeId(student.getXxId());
        //毕业计划
        GjtGraduationPlan gjtGraduationPlan = gjtGraduationPlanService.queryByTermId(currentGradeId);
        GjtGraduationApplyCertif gjtGraduationApplyCertif = null;
        if (gjtGraduationPlan != null) {
            gjtGraduationApplyCertif = gjtGraduationRecordService.queryByStudentIdAndPlanId(student.getStudentId(), gjtGraduationPlan.getId());
        }else{
            gjtGraduationApplyCertif = gjtGraduationRecordService.queryByStudentId(student.getStudentId());
        }
        if (null != gjtGraduationApplyCertif) {
            //审核状态，默认0(0-待审核 1-分部通过 2-分部未通过 6-学员提交申请 11总部通过 12-总部不通过  )
            gjtGraduationRecordService.studentApply(gjtGraduationApplyCertif, user.getId());
        }

        return result;
    }

    /**
     * 可毕业无学位(本科)state=2
     * applyDegree=0
     *
     * @param request
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "/apply2", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> apply2(HttpServletRequest request, @RequestParam("graduationState") Integer graduationState) throws CommonException {
        Map<String, Object> result = new HashedMap();

        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        // 查询学生信息
        GjtStudentInfo student = gjtStudentInfoService.queryByAccountId(user.getId());

        // 查询当前的学期id
        String currentGradeId = gjtGradeService.getCurrentGradeId(student.getXxId());
        //毕业计划
        GjtGraduationPlan gjtGraduationPlan = gjtGraduationPlanService.queryByTermId(currentGradeId);
        GjtGraduationApplyCertif gjtGraduationApplyCertif = null;
        if (gjtGraduationPlan != null) {
            gjtGraduationApplyCertif = gjtGraduationRecordService.queryByStudentIdAndPlanId(student.getStudentId(), gjtGraduationPlan.getId());
        }else{
            gjtGraduationApplyCertif = gjtGraduationRecordService.queryByStudentId(student.getStudentId());
        }

        if (null != gjtGraduationApplyCertif) {
            //审核状态，默认0(0-待审核 1-分部通过 2-分部未通过 6-学员提交申请 11总部通过 12-总部不通过  )
            //gjtGraduationApplyCertif.setAuditState(6);

            //申请延迟毕业
            if (1 == graduationState) {
                gjtGraduationApplyCertif.setGraduationState(1);

                //延迟毕业天数
                gjtGraduationApplyCertif.setDelayGraduationDT( new Date());
                gjtGraduationRecordService.update(gjtGraduationApplyCertif);
            }else{
                gjtGraduationRecordService.studentApply(gjtGraduationApplyCertif, user.getId());
            }
        }

        return result;
    }


    /**
     * 可毕业有学位（本科：跳转学位承诺书）；state=3
     *
     * @param request
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "/apply3", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> apply3(HttpServletRequest request, @RequestParam("graduationState") Integer graduationState, @RequestParam("applyDegree") Integer applyDegree,@RequestParam("signature")String signature) throws CommonException {
        Map<String, Object> result = new HashedMap();

        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        // 查询学生信息
        GjtStudentInfo student = gjtStudentInfoService.queryByAccountId(user.getId());

        // 查询当前的学期id
        String currentGradeId = gjtGradeService.getCurrentGradeId(student.getXxId());
        //毕业计划
        GjtGraduationPlan gjtGraduationPlan = gjtGraduationPlanService.queryByTermId(currentGradeId);
        GjtGraduationApplyCertif gjtGraduationApplyCertif = null;
        if (gjtGraduationPlan != null) {
             gjtGraduationApplyCertif = gjtGraduationRecordService.queryByStudentIdAndPlanId(student.getStudentId(), gjtGraduationPlan.getId());
        }else{
             gjtGraduationApplyCertif = gjtGraduationRecordService.queryByStudentId(student.getStudentId());
        }
        if (null != gjtGraduationApplyCertif) {
            //审核状态，默认0(0-待审核 1-分部通过 2-分部未通过 6-学员提交申请 11总部通过 12-总部不通过  )
//            gjtGraduationApplyCertif.setAuditState(6);

            //签名
            gjtGraduationApplyCertif.setSignature(signature);
            //是否申请学位 1申请学位
            gjtGraduationApplyCertif.setApplyDegree(applyDegree);
            //申请延迟毕业
            if (1 == graduationState) {
                gjtGraduationApplyCertif.setGraduationState(1);
                //延迟毕业时间
                gjtGraduationApplyCertif.setDelayGraduationDT( new Date());
                gjtGraduationApplyCertifService.update(gjtGraduationApplyCertif);
            }else{
                gjtGraduationApplyCertifService.studentApply(gjtGraduationApplyCertif, user.getId());
            }

            //更新学业申请为已申请
            if (applyDegree == 1){
                GjtApplyDegreeCertif certif = gjtApplyDegreeCertifService.queryApplyDegreeCertifByStudentIdAndPlanId(student.getStudentId(),gjtGraduationApplyCertif.getGjtGraduationPlan().getId());
                gjtApplyDegreeCertifService.studentApplyDegree(certif, user.getId());
            }
        }
        return result;
    }

    /**
     * 发送毕业登记表到邮箱
     *
     * @return
     */
    @RequestMapping(value = "sendMail", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> sendMail(HttpServletRequest request, @RequestParam("email") String email) throws CommonException {
        Map<String, Object> result = new HashedMap();
        String path = Joiner.on(File.separator).join(
                request.getSession().getServletContext().getRealPath(""),
                "template", "毕业生登记表填写说明.docx").toString();
//			new DocumentHandler().createDocPath(new HashedMap(), path, "国家开放大学毕业生登记表.ftl");
//			URL url = this.getClass().getResource("/ftl/国家开放大学毕业生登记表.ftl");

        String fullPath = request.getSession().getServletContext().getRealPath("");
        String templatePath = (fullPath + "\\template\\毕业生登记表填写说明.docx").replace("\\", "/");

        boolean flag = MailUtil.sendAttachMail(new String[]{email}, "", new File(path));
        if (flag) {
            result.put("state", "1");
        } else {
            throw new CommonException(601, "发送到邮箱失败！");
        }
        return result;
    }


    /**
     * 更新毕业生登记表
     * <p>
     * //	 * @param expressCompany
     * //	 *            快递公司
     * //	 * @param expressNumber
     * //	 *            快递单号
     *
     * @return
     * @throws CommonException
     */
    @RequestMapping(value = "/updateGraduationRegisterNew", method = RequestMethod.POST)
    @ResponseBody
    public void updateGraduationRegisterNew(GjtGraduationRegister info, HttpServletRequest request)
            throws CommonException {
        if (StringUtils.isBlank(info.getExpressCompany()) || StringUtils.isBlank(info.getExpressNumber())) {
            throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数,请检查!");
        }

        // 获取登录用户ID
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        // 查询学生信息
        GjtStudentInfo student = gjtStudentInfoService.queryByAccountId(user.getId());
        // 查询当前的学期
        String currentGradeId = gjtGradeService.getCurrentGradeId(student.getXxId());
        // 根据当前学期查询是否存在毕业计划
        GjtGraduationPlan gjtGraduationPlan = gjtGraduationPlanService.queryByTermId(currentGradeId);
        // 查询毕业生登记表
        GjtGraduationRegister modifyInfo = gjtGraduationRegisterService.queryByStudentId(gjtGraduationPlan.getId(),
                student.getStudentId());
        boolean flag = false;
        if (modifyInfo == null) {
            info.setRegisterId(UUIDUtils.random());
            info.setGraduationPlanId(gjtGraduationPlan.getId());
            info.setStudentId(student.getStudentId());
            info.setSubmitType("2");
            info.setExpressSignState(1);
            info.setCreatedBy(user.getId());
            flag = gjtGraduationRegisterService.insert(info);
        } else {
            modifyInfo.setExpressCompany(info.getExpressCompany());
            modifyInfo.setExpressNumber(info.getExpressNumber());
            modifyInfo.setSubmitType("2");
            if (modifyInfo.getExpressSignState() != 2) {
                modifyInfo.setExpressSignState(1);
            }
            modifyInfo.setUpdatedBy(user.getId());
            flag = gjtGraduationRegisterService.update(modifyInfo);
        }
        if (!flag) {
            throw new CommonException(MessageCode.BIZ_ERROR, "服务器异常");
        }
    }

}
