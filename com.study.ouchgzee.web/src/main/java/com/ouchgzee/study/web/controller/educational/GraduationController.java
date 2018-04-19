package com.ouchgzee.study.web.controller.educational;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gzedu.xlims.common.*;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.mail.util.MailUtil;
import com.gzedu.xlims.pojo.*;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyCertif;
import com.gzedu.xlims.pojo.graduation.GjtGraduationPlan;
import com.gzedu.xlims.pojo.graduation.GjtGraduationRegister;
import com.gzedu.xlims.pojo.graduation.GjtGraduationRegisterEdu;
import com.gzedu.xlims.pojo.status.ThesisStatusEnum;
import com.gzedu.xlims.pojo.thesis.GjtThesisPlan;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyCertifService;
import com.gzedu.xlims.service.graduation.GjtGraduationPlanService;
import com.gzedu.xlims.service.graduation.GjtGraduationRegisterService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolAddressService;
import com.gzedu.xlims.service.thesis.GjtThesisApplyService;
import com.gzedu.xlims.service.thesis.GjtThesisPlanService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.common.DocumentHandler;
import com.ouchgzee.study.web.vo.EduLiveVo;
import com.ouchgzee.study.web.vo.GjtGraduationRegisterVo;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 毕业服务
 *
 * @author lyj
 * @time 2017-03-31
 */
@Controller
@RequestMapping("/pcenter/graduation")
public class GraduationController extends BaseController {

    private final static Logger log = LoggerFactory.getLogger(GraduationController.class);

    @Autowired
    private GjtGraduationRegisterService gjtGraduationRegisterService;

    @Autowired
    private GjtStudentInfoService gjtStudentInfoService;
    @Autowired
    private GjtRecResultService gjtRecResultService;
    @Autowired
    private GjtGradeService gjtGradeService;
    @Autowired
    private GjtGraduationApplyCertifService gjtGraduationRecordService;

    @Autowired
    private GjtGraduationPlanService gjtGraduationPlanService;

    @Autowired
    private GjtSchoolAddressService gjtSchoolAddressService;

    @Autowired
    private GjtOrgService gjtOrgService;

    @Autowired
    private CommonMapService commonMapService;

    @Autowired
    private GjtTeachPlanService gjtTeachPlanService;

    @Autowired
    private GjtThesisApplyService gjtThesisApplyService;

    @Autowired
    private CacheService cacheService;


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
//        if (state == 8){
        //TODO 查找学生是否有毕业通知或者毕业典礼活动 （运营平台需要出的接口）
        //判断该学生是否有毕业活动
        //1：已毕业（总部审核通过）;
        //2：已毕业（总部审核通过）;
//        }

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

        //是否有毕业答辩 当前版本默认为否
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


        GjtGraduationApplyCertif gjtGraduationApplyCertif = gjtGraduationRecordService.queryByStudentId(student.getStudentId());
        //是否采集照片 当前版本默认为否
        int HAS_PHOTO;
        if (gjtGraduationApplyCertif != null && !Strings.isNullOrEmpty(gjtGraduationApplyCertif.getPhotoUrl())) {
            HAS_PHOTO = 1;
        } else {
            HAS_PHOTO = 0;
        }

        //毕业申请已提交
        if (state == 4) {
//            GjtGraduationApplyCertif gjtGraduationApplyCertif = gjtGraduationRecordService.queryByStudentId(student.getStudentId());
            result.put("DELAY_GRADUATION_DT", gjtGraduationApplyCertif.getDelayGraduationDT());
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
        if (null != auditInfoMap) {//入学学习
            progressStu = Constants.BOOLEAN_1.equals(auditInfoMap.get("auditState")) == true ? 1 : 0;
            if (HAS_PHOTO == 1) {//照片采集
                progressStu = 2;
            } else if (HAS_REGISTER == 1) {//毕业登记表
                progressStu = 3;
            } else if (HAS_DEFENCE == 1) {//毕业答辩
                progressStu = 4;
            } else if (state == 4) {//毕业申请
                progressStu = 5;
            }
            if ("毕业".equals(xjzt) && state != 8) {
                progressStu = 6;//历史数据已经毕业的学生的照片与登记表都为已经完成
                //TODO 领取证书功能未完
            }
        }
        result.put("progressStu", progressStu);

        result.put("STUDENTS", students);
        return result;
    }


    @RequestMapping("/index")
    @ResponseBody
    public Map<String, Object> index(HttpServletRequest request) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("GRADUATION_RECORD_STATUS", "register");// /pcenter/graduation/graduationRegister

        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        // 查询学生信息
        GjtStudentInfo student = gjtStudentInfoService.queryByAccountId(user.getId());
        // 查询当前的学期
        String currentGradeId = gjtGradeService.getCurrentGradeId(student.getXxId());
        // 根据当前学期查询是否存在毕业计划
        GjtGraduationPlan gjtGraduationPlan = gjtGraduationPlanService.queryByTermId(currentGradeId);
        // 查询毕业生登记表
        GjtGraduationRegister gjtGraduationRegister = gjtGraduationRegisterService
                .queryByStudentId(gjtGraduationPlan.getId(), student.getStudentId());
        if (gjtGraduationRegister != null) {
            GjtGraduationApplyCertif info = gjtGraduationRecordService.queryByStudentId(student.getStudentId());
            if (info != null) {
                // 状态：0-申请，1-已受理，2-初审未通过，3-终审未通过，4-终审已通过，5-已领取毕业证
                // if(info.getStatus() == 4 || info.getStatus() == 5) {
                // result.put("GRADUATION_RECORD_STATUS", "give_out");
                // }
                result.put("GRADUATION_RECORD_STATUS", "info");// pcenter/graduation/graduationRecord
            }
            result.put("GRADUATION_RECORD_STATUS", "init");// pcenter/graduation/graduationApply
        }
        return result;
    }


    /*
     * @RequestMapping("/index") public String index(HttpServletRequest request)
     * { GjtUserAccount user = (GjtUserAccount)
     * request.getSession().getAttribute(WebConstants.CURRENT_USER);
     * GjtStudentInfo student = user.getGjtStudentInfo(); String redirect =
     * "/pcenter/graduation/graduationRegister"; GjtGraduationRegister
     * gjtGraduationRegister =
     * gjtGraduationRegisterService.queryByStudentId(student.getStudentId());
     * if(gjtGraduationRegister != null) { GjtGraduationApplyCertif info =
     * gjtGraduationRecordService.queryByStudentId(student.getStudentId());
     * if(info != null) { // 状态：0-申请，1-已受理，2-初审未通过，3-终审未通过，4-终审已通过，5-已领取毕业证 //
     * if(info.getStatus() == 4 || info.getStatus() == 5) { //
     * result.put("GRADUATION_RECORD_STATUS", "give_out"); // } redirect =
     * "pcenter/graduation/graduationRecord"; } redirect =
     * "/pcenter/graduation/graduationApply"; } return "redirect:"+redirect; }
     */

    /**
     * 毕业生登记表
     *
     * @param request
     * @return
     */
    @RequestMapping("/graduationRegister")
    @ResponseBody
    public GjtGraduationRegisterVo graduationRegister(HttpServletRequest request) {
        // 获取登录用户ID
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        GjtGraduationRegisterVo vo = new GjtGraduationRegisterVo();
        // 查询学生信息
        GjtStudentInfo student = gjtStudentInfoService.queryByAccountId(user.getId());
        String studentId = student.getStudentId();
        GjtSpecialty gjtSpecialty = student.getGjtSpecialty();
        GjtGrade gjtGrade = student.getGjtGrade();
        // 查询当前的学期
        String currentGradeId = gjtGradeService.getCurrentGradeId(student.getXxId());
        // 根据当前学期查询是否存在毕业计划
        GjtGraduationPlan gjtGraduationPlan = gjtGraduationPlanService.queryByTermId(currentGradeId);
        // 查询学习经历
        List<GjtGraduationRegisterEdu> eduList = gjtGraduationRegisterService.queryEduByStudentId(studentId);
        List<EduLiveVo> list = Lists.newArrayList();
        for (GjtGraduationRegisterEdu e : eduList) {
            list.add(new EduLiveVo(e));
        }
        GjtGraduationRegister entity = null;
        if (gjtGraduationPlan != null) {
            entity = gjtGraduationRegisterService.queryByStudentId(gjtGraduationPlan.getId(), studentId);
            long currentTimeMillis = System.currentTimeMillis();
            // 是否在登记时间范围之内
            Date endDt = new Date(gjtGraduationPlan.getGraRegisterEndDt().getTime());
            endDt.setHours(23);
            endDt.setMinutes(59);
            endDt.setSeconds(59);
            if (currentTimeMillis >= gjtGraduationPlan.getGraRegisterBeginDt().getTime()
                    && currentTimeMillis <= endDt.getTime()) {
                vo.setIsUpdate(1);
            }
            if (entity != null) {
                vo.setSubmitType(entity.getSubmitType());
            } else {
                vo.setSubmitType("1");
            }
        }
        /**
         * 学籍信息
         */
        // 姓名
        vo.setXm(student.getXm());
        // 姓别
        if (student.getXbm() != null) {
            vo.setXbm(cacheService.getCachedDictionaryName("Sex", student.getXbm()));// TODO
        }
        // 学号
        vo.setXh(student.getXh());
        // 出生日期
        vo.setCsrq(student.getCsrq());
        // 民族
        if (student.getMzm() != null) {
            vo.setMzm(cacheService.getCachedDictionaryName("NationalityCode", student.getMzm()));// TODO
        }
        // 电子注册号
        vo.setErpRegistrationNumber(student.getErpRegistrationNumber());
        // 政治面貌
        vo.setPoliticsstatus(student.getPoliticsstatus());
        // 证件类型
        vo.setCertificatetype(student.getCertificatetype() != null ? (String) student.getCertificatetype() : "身份证");
        // 证件号
        vo.setSfzh(student.getSfzh());
        // 学生类别(1:开放教育专业,2:助力计划专业,3:开放教育专科)
        vo.setSpecialtyCategory(gjtSpecialty.getSpecialtyCategory());
        // 学历层次
        vo.setExedulevel(student.getExedulevel());

        // 专业
        if (gjtSpecialty != null) {
            vo.setZymc(gjtSpecialty.getZymc());
        }
        // 入学时间
        if (gjtGrade != null) {
            vo.setGradeName(gjtGrade.getGradeName());
        }
        // 毕业时间
        GjtGrade grade = gjtGradeService.findByPYCCAndSpecialtyIdAndGradeId(student.getPycc(), student.getMajor(),
                student.getNj());
        if (grade != null) {
            vo.setGraduationTime(com.gzedu.xlims.common.DateUtils.getTimeYM(grade.getEndDate()));
        }

        /**
         * 毕业申请信息
         */
        if (entity != null) {
            vo.setRegisterId(entity.getRegisterId());
            vo.setPhoto(entity.getPhoto());
            // 工作单位
            vo.setScCo(entity.getCompany());
            // 单位电话
            vo.setScCoPhone(entity.getCompanyPhone());
            // 手机号码
            vo.setSjh(entity.getPhone());
            // 家庭电话
            vo.setHomePhone(entity.getHomePhone());
            // E-mail
            vo.setDzxx(entity.getEmail());
            // 主要学习经历
            vo.setEduLiveList(list);
            // 毕业实习单位及主要内容
            vo.setPracticeContent(entity.getPracticeContent());
            ;
            // 毕业论文及毕业设计题目
            vo.setGraduationDesign(entity.getGraduationDesign());
            ;
            // 在校期间受过何种奖励和处分
            vo.setAwardRecord(entity.getAwardRecord());
            ;
            // 自我鉴定
            vo.setEvaluation(entity.getEvaluation());
            vo.setExpressCompany(entity.getExpressCompany());
            vo.setExpressNumber(entity.getExpressNumber());
        } else {
            vo.setPhoto(student.getAvatar());
            vo.setScCo(student.getScCo());
            vo.setSjh(student.getSjh());
            vo.setDzxx(student.getDzxx());
        }
        vo.setEduLiveList(list);
        vo.setStudentId(studentId);

        // 毕业服务-院校收货地址
        String xxId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(user.getGjtOrg().getId());
        GjtSchoolAddress schoolAddress = gjtSchoolAddressService.queryByXxIdAndType(xxId, 1);
        if (schoolAddress != null) {
            vo.setMobile(schoolAddress.getMobile());
            vo.setReceiver(schoolAddress.getReceiver());
            vo.setPostcode(schoolAddress.getPostcode());
            // 收货地址
            String address = "";
            if (schoolAddress.getProvinceCode() != null) {
                address += Objects.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.PROVINCE,
                        schoolAddress.getProvinceCode()), "");
                if (schoolAddress.getCityCode() != null) {
                    address += Objects.toString(cacheService.getCachedDictionaryName(
                            CacheService.DictionaryKey.CITY.replace("${Province}", schoolAddress.getProvinceCode()),
                            schoolAddress.getCityCode()), "");
                    address += Objects.toString(cacheService.getCachedDictionaryName(
                            CacheService.DictionaryKey.AREA.replace("${Province}", schoolAddress.getProvinceCode())
                                    .replace("${City}", schoolAddress.getCityCode()),
                            schoolAddress.getAreaCode()), "");
                }
            }
            address += Objects.toString(schoolAddress.getAddress(), "");
            vo.setAddress(address);
        }
        return vo;
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
     * 下载毕业生登记表，返回IO流
     */
    @RequestMapping(value = "downloadGraRegisterToFile", method = RequestMethod.GET)
    public void downloadGraRegisterToFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InputStream in = this.getClass().getResourceAsStream("/ftl/国家开放大学毕业生登记表.ftl");
        super.downloadInputStream(response, in, "国家开放大学毕业生登记表.doc");
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

    /**
     * 更新毕业生登记表
     *
     * @param request
     * @param vo
     * @return
     * @throws UnsupportedEncodingException
     * @throws CommonException
     */
    @RequestMapping(value = "/updateGraduationRegister", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateGraduationRegister(HttpServletRequest request, GjtGraduationRegisterVo vo)
            throws UnsupportedEncodingException, CommonException {
        Map<String, Object> map = Maps.newHashMap();
        Map<String, Object> registerMap = new HashMap<String, Object>();
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        String submitType = request.getParameter("submitType");// 提交或保存类型
        String company = request.getParameter("company");// 工作单位
        String companyPhone = request.getParameter("companyPhone");// 单位电话
        // String phone = request.getParameter("phone");//手机号码
        String homePhone = request.getParameter("homePhone");// 家庭电话
        String email = request.getParameter("email");// 邮箱
        String practiceContent = request.getParameter("practiceContent");// 实习内容
        String graduationDesign = request.getParameter("graduationDesign");// 毕业设计
        String awardRecord = request.getParameter("awardRecord");// 奖惩记录
        String evaluation = request.getParameter("evaluation");// 自我鉴定
        String photo = request.getParameter("photo");// 头像
        String registerId = request.getParameter("registerId");// ID
        registerMap.put("company", company);
        registerMap.put("companyPhone", companyPhone);
        // registerMap.put("phone", phone);
        registerMap.put("homePhone", homePhone);
        registerMap.put("email", email);
        registerMap.put("practiceContent", practiceContent);
        registerMap.put("graduationDesign", graduationDesign);
        registerMap.put("awardRecord", awardRecord);
        registerMap.put("evaluation", evaluation);
        registerMap.put("photo", photo);
        registerMap.put("registerId", registerId);
        registerMap.put("submitType", submitType);
        if ("1".equals(submitType)) {// 保存:毕业登记表信息不需要填写完整
            map = updateOrSaveGraduationRegister(registerMap, vo, user);
        }
        if ("2".equals(submitType)) {// 提交:毕业登记表信息必须要填写完整
            if (StringUtils.isNotBlank(homePhone) && StringUtils.isNotBlank(practiceContent)
                    && StringUtils.isNotBlank(graduationDesign) && StringUtils.isNotBlank(awardRecord)
                    && StringUtils.isNotBlank(evaluation) && StringUtils.isNotBlank(company)
                    && StringUtils.isNotBlank(companyPhone) && StringUtils.isNotBlank(email)) {
                map = updateOrSaveGraduationRegister(registerMap, vo, user);
            } else {
                throw new CommonException(MessageCode.BIZ_ERROR, "缺少参数,请检查!");
            }
        }
        return map;
    }

    private Map<String, Object> updateOrSaveGraduationRegister(Map<String, Object> registerMap,
                                                               GjtGraduationRegisterVo vo, GjtUserAccount user) throws UnsupportedEncodingException, CommonException {
        Map<String, Object> map = Maps.newHashMap();
        // GjtGraduationRegister entity =
        // gjtGraduationRegisterService.queryByStudentId(user.getGjtStudentInfo().getStudentId());
        // if(entity != null &&
        // !Strings.isNullOrEmpty(registerMap.get("registerId").toString())) {
        // // 修改
        // entity =
        // gjtGraduationRegisterService.queryById(registerMap.get("registerId").toString());
        // entity.setUpdatedBy(user.getId());
        // entity.setUpdatedDt(new Date());
        // entity.setSubmitType(registerMap.get("submitType").toString());
        // }
        // if(entity == null) {
        // // 新增
        // entity = new GjtGraduationRegister();
        // entity.setRegisterId(UUIDUtils.random().toString());
        // entity.setStudentId(user.getGjtStudentInfo().getStudentId());
        // entity.setCreatedBy(user.getId());
        // entity.setCreatedDt(new Date());
        // entity.setSubmitType(registerMap.get("submitType").toString());
        // }
        // entity.setCompany(ObjectUtils.toString(registerMap.get("company")));
        // entity.setCompanyPhone(ObjectUtils.toString(registerMap.get("companyPhone")));
        // //entity.setPhone(ObjectUtils.toString(registerMap.get("phone")));
        // entity.setHomePhone(ObjectUtils.toString(registerMap.get("homePhone")));
        // entity.setEmail(ObjectUtils.toString(registerMap.get("email")));
        // entity.setPracticeContent(ObjectUtils.toString(registerMap.get("practiceContent")));
        // entity.setGraduationDesign(ObjectUtils.toString(registerMap.get("graduationDesign")));
        // entity.setAwardRecord(ObjectUtils.toString(registerMap.get("awardRecord")));
        // entity.setEvaluation(ObjectUtils.toString(registerMap.get("evaluation")));
        // // 注意：照片在页面上如果是空字符串则是未修改过，所以不需要set
        // if(StringUtils.isNotEmpty(ObjectUtils.toString(registerMap.get("photo"))))
        // {
        // entity.setPhoto(URLDecoder.decode(ObjectUtils.toString(registerMap.get("photo")),
        // "UTF-8"));
        // }
        // boolean flag = false;
        // // 新增or修改
        // flag = gjtGraduationRegisterService.insert(entity);
        // if(!flag) {
        // throw new CommonException(MessageCode.BIZ_ERROR,"保存时发生错误!");
        // } else {
        // map.put("id", entity.getRegisterId());
        // }
        // //更新学历经历
        // if(vo.getEduLiveList() != null) {
        // for(EduLiveVo eVo : vo.getEduLiveList() ) {
        // if("0".equals(eVo.getStatus()) || "1".equals(eVo.getStatus())) {
        // updateGraduationRegisterEdu(eVo,user.getId(),user.getGjtStudentInfo().getStudentId());
        // } else if("2".equals(eVo.getStatus())) {
        // delGraduationRegisterEdu(eVo.getEduId(), user.getId());
        // }
        // }
        // }
        return map;
    }

    private String updateGraduationRegisterEdu(EduLiveVo vo, String userId, String studentId) throws CommonException {
        try {
            String beginTimeStr = vo.getBeginTime();
            String endTimeStr = vo.getEndTime();
            if (StringUtils.isNotBlank(vo.getSchool())) {
                GjtGraduationRegisterEdu entity = null;
                if ("1".equals(vo.getStatus())) {// 1为更新
                    if (!Strings.isNullOrEmpty(vo.getEduId())) {
                        entity = gjtGraduationRegisterService.findGraduationRegisterEdu(vo.getEduId());
                        entity.setUpdatedBy(userId);
                        entity.setUpdatedDt(new Date());
                    } else {
                        throw new CommonException(MessageCode.BIZ_ERROR, "学历经历ID为空,请检查!");
                    }
                }
                if (entity == null) {
                    entity = new GjtGraduationRegisterEdu();
                    entity.setEduId(UUIDUtils.random());
                    entity.setStudentId(studentId);
                    entity.setCreatedBy(userId);
                    entity.setCreatedDt(new Date());

                }
                entity.setBeginTime(StringUtils.isNotBlank(beginTimeStr)
                        ? DateUtils.parseDate(beginTimeStr, "yyyy-MM-dd", "yyyy-MM", "yyyy/MM", "yyyyMM") : null);
                entity.setEndTime(StringUtils.isNotBlank(endTimeStr)
                        ? DateUtils.parseDate(endTimeStr, "yyyy-MM-dd", "yyyy-MM", "yyyy/MM", "yyyyMM") : null);
                entity.setDegree(vo.getDegree());
                entity.setRegion(vo.getRegion());
                entity.setSchool(vo.getSchool());
                boolean flag = gjtGraduationRegisterService.saveGraduationRegisterEdu(entity);
                if (!flag) {
                    throw new CommonException(MessageCode.BIZ_ERROR, "操作失败");
                }
                return entity.getEduId();
            } else {
                throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException(MessageCode.SYSTEM_ERROR, e.getMessage());
        }
    }

    private String delGraduationRegisterEdu(@PathVariable(value = "id") String id, String userId)
            throws CommonException {
        if (Strings.isNullOrEmpty(id)) {
            throw new CommonException(MessageCode.BIZ_ERROR, "删除学历经历时,id不能为空!");
        }
        GjtGraduationRegisterEdu edu = gjtGraduationRegisterService.findGraduationRegisterEdu(id);
        edu.setIsDeleted("Y");
        edu.setUpdatedBy(userId);
        edu.setUpdatedDt(new Date());
        boolean flag = gjtGraduationRegisterService.saveGraduationRegisterEdu(edu);
        if (!flag) {
            throw new CommonException(MessageCode.BIZ_ERROR, "操作失败");
        }
        return "success";
    }

    /**
     * #########################################################################
     * #### ####################################################################
     * ####################################################################
     * ####################################################################
     * #########################################################################
     * ###################
     */
    /**
     * 申请毕业
     *
     * @param request
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/graduationApply")
    @ResponseBody
    public Map<String, Object> graduationApply(HttpServletRequest request) throws ParseException {
        Map<String, Object> result = Maps.newHashMap();
        GjtStudentInfo student = ((GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER))
                .getGjtStudentInfo();
        boolean isApply = false;
        String endDateStr = null;
        String rollRegisterDt = student.getRollRegisterDt();
        result.put("rollRegisterDt", rollRegisterDt); // 学籍注册时间
        if (!Strings.isNullOrEmpty(rollRegisterDt)) {
            Date endDate = DateUtils.parseDate(rollRegisterDt, "yyyy-MM", "yyyy/MM", "yyyyMM");
            endDate = DateUtils.addYears(endDate, 8);// 8年
            endDateStr = com.gzedu.xlims.common.DateUtils.getTimeYM(endDate);
            // 毕业要求:从学籍注册时间至当前时间的时长(学籍周期)大于2.5年
            Date tDate = DateUtils.addMonths(DateUtils.parseDate(rollRegisterDt, "yyyy-MM", "yyyy/MM", "yyyyMM"),
                    12 * 2 + 6);// 2.5年
            if (new Date().after(tDate)) {
                isApply = true;
            }
        }
        result.put("rollEndDt", endDateStr); // 学籍截止时间
        // 是否可以申请毕业
        result.put("isApply", isApply);
        // 根据学员培养层次/专业/年级查询教学计划表再关联gjt_grade年级表，统计总共有多少个学期，以及学期开始时间START_DATE小于当前时间有多少个学期
        /*
         * Map<String, Object> gradeCountMap =
         * gjtGradeService.countGradeByStudent(student.getPycc(),student.
         * getMajor(),student.getNj()); if(gradeCountMap.get("gradeCountMap") !=
         * null) { Integer currentGrade =
         * (Integer)gradeCountMap.get("currentGrade"); if(currentGrade >= 5) {
         * // 大于2.5年可申请毕业 isApply = true; } } result.put("gradeCountMap",
         * gradeCountMap);//countGrade:总学期数 ;currentGrade:当前学期数
         */
        // 最低毕业学分
        Double minGraduationCredit = null;
        // 总学分
        Integer creditSum = 0;
        if (student.getGjtSpecialty() != null) {
            minGraduationCredit = student.getGjtSpecialty().getZdbyxf();
            creditSum = student.getGjtSpecialty().getZxf();
        }
        result.put("minGraduationCredit", minGraduationCredit);
        result.put("creditSum", creditSum);
        // 学分要求-课程列表
        List<Map<String, String>> creditList = gjtRecResultService.queryStudentCredit(student.getStudentId());
        result.put("creditList", creditList);
        // 已修总学分
        int credit = 0;
        try {
            for (Map<String, String> map : creditList) {
                if (map.get("GETCREDITS") != null) {
                    credit += Integer.parseInt(map.get("GETCREDITS"));
                }
            }
        } catch (NumberFormatException e) {
        }
        result.put("credit", credit);
        // 提示
        if (isApply) {
            if (minGraduationCredit != null && credit >= minGraduationCredit) {
                result.put("message", "你已达到毕业条件，可以申请毕业");
            } else {
                result.put("message", "你的学分仍未达到毕业要求，申请毕业后加油学习哦");
            }
        } else {
            result.put("message", "学籍周期未达到毕业条件，暂时不能申请毕业哦");
        }
        return result;
    }

    /**
     * 申请毕业确认
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/graduationApplyConfirm")
    @ResponseBody
    public Object graduationApplyConfirm(HttpServletRequest request) throws IOException {
        ResultFeedback feedback = new ResultFeedback(true, "操作成功");
        try {
            String autograph = request.getParameter("autograph");
            GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
            GjtStudentInfo student = user.getGjtStudentInfo();

            GjtGraduationApplyCertif entity = new GjtGraduationApplyCertif();
            // entity.setApplyDegree(isApplyDegree);// 是否申请学位
            // entity.setCollegeId(collegeId);// 学位院校ID
            // 学位英语证书路径
            // entity.setEnglishCertificateUrl(StringUtils.isNotBlank(englishCertificateUrl)
            // ? englishCertificateUrl : englishFourUrl);
            // 签名，BASE64图片上传到eefile保存图片路径
            // entity.setSignature(autograph.getBytes());
            entity.setGjtStudentInfo(student);
            entity.setCreatedBy(user.getId());
            entity.setApplyId(UUIDUtils.random());
            entity.setCreatedBy(user.getId());
            entity.setCreatedDt(new Date());
            gjtGraduationRecordService.save(entity);
            feedback.setObj(entity.getApplyId());
        } catch (Exception e) {
            e.printStackTrace();
            feedback = new ResultFeedback(false, "服务器异常，请重试！");
        }
        return feedback;
    }

    /**
     * 查看毕业审核状态
     *
     * @return
     */
    @RequestMapping(value = "/graduationRecord")
    @ResponseBody
    public Map<String, Object> graduationRecord(HttpServletRequest request) {
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        GjtStudentInfo student = user.getGjtStudentInfo();
        GjtGraduationApplyCertif info = gjtGraduationRecordService.queryByStudentId(student.getStudentId());
        // request.setAttribute("info", info);
        Map<String, Object> map = Maps.newHashMap();
        map.put("status", info.getIsReceive());
        map.put("graduationCertificateUrl", info.getGraduationCertificateUrl()); // 毕业证书路径
        map.put("remark", info.getRemark());
        return map;
    }

    /**
     * 随机下载自我鉴定示例文档
     *
     * @throws IOException
     */
    @RequestMapping(value = "/downloadGraduationDoc")
    public void downloadGraduationDoc(HttpServletRequest request) throws IOException {
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        GjtStudentInfo student = user.getGjtStudentInfo();
        /*
         * Student user = getLoginedUser(); String docUrl =
         * gjtGraduationRegisterService.updateRandomGraduationDoc(user.getXxId()
         * , user.getSpecialId());
         *
         * if(StringUtils.isNotBlank(docUrl)) {
         * super.getResponse().sendRedirect(docUrl); } else {
         * super.outputJsAlertCloseWindow("无法下载，暂无示例文档！"); }
         */
    }

    /**
     * 毕业服务首页--判断是否开放
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/graduationServiceHomePage")
    @ResponseBody
    public Map<String, Object> graduationServiceHomePage(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> registerResult = Maps.newHashMap();// 毕业登记表
        Boolean isApply = false;
        GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
        GjtStudentInfo gjtStudentInfo = user.getGjtStudentInfo();
        String xxId = gjtStudentInfo.getXxId();
        String currentGradeId = gjtGradeService.getCurrentGradeId(xxId);// 查询当前的学期
        // 根据当前学期查询是否存在毕业计划
        GjtGraduationPlan gjtGraduationPlan = gjtGraduationPlanService.queryByTermId(currentGradeId);
        if (gjtGraduationPlan != null) {
            // 当前学期的学期开始时间
            Date currentGradeDate = gjtGradeService.queryById(currentGradeId).getStartDate();
            // 查询学员的学期的开始时间
            Date studentDate = gjtGradeService.queryById(gjtStudentInfo.getNj()).getStartDate();
            List<GjtGrade> gjtGradeList = gjtGradeService.getGradeList(currentGradeDate,
                    user.getGjtStudentInfo().getXxId());
            Date afterDate = null;
            if (gjtGradeList != null && gjtGradeList.size() > 0) {
                GjtGrade gjtGrade = gjtGradeList.get(3);// 当前学期往后推2年,即往后推4个学期
                afterDate = gjtGrade.getStartDate();
            }
            if (studentDate.getTime() <= afterDate.getTime() && "2".equals(gjtStudentInfo.getXjzt())) {
                isApply = true;
            }
        } else {
            isApply = false;
        }

        Boolean isLastGradeToApply = false;
        // 根据产品ID获取学员的所有学期中的最后一个学期
        List<Map<String, Object>> teachPlanList = gjtTeachPlanService
                .getGjtTeachPlanInfo(gjtStudentInfo.getGradeSpecialtyId());
        if (teachPlanList != null && teachPlanList.size() > 0) {
            Map<String, Object> teachMap = teachPlanList.get(teachPlanList.size() - 1);
            // 获取最后学期的学期开始时间
            GjtGrade gjtGrade = gjtGradeService.queryById(ObjectUtils.toString(teachMap.get("ACTUAL_GRADE_ID")));

            if (com.gzedu.xlims.common.DateUtils.getDate().getTime() > gjtGrade.getStartDate().getTime()) {
                isLastGradeToApply = true;
            }
        }

        registerResult.put("isLastGradeToApply", isLastGradeToApply);
        registerResult.put("isApply", isApply);
        resultMap.put("registerResult", registerResult);
        return resultMap;
    }
}
