package com.gzedu.xlims.webservice.controller.gjt.signup;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtil;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.ValidateUtil;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.eefile.EEFileUploadUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.pojo.GjtGradeSpecialty;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.GjtSignup;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtSyncLog;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtSyncLogService;
import com.gzedu.xlims.service.api.ApiOpenClassService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyService;
import com.gzedu.xlims.service.flow.GjtFlowService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.signup.GjtSignupService;
import com.gzedu.xlims.service.signup.SignupDataAddService;
import com.gzedu.xlims.service.studentClass.GjtStudentClassInfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.webservice.common.Servlets;
import com.gzedu.xlims.webservice.controller.BaseController;
import com.gzedu.xlims.webservice.response.ResponseResult;
import com.gzedu.xlims.webservice.response.ResponseStatus;

/**
 * 学籍资料同步接口
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年08月21日
 * @version 3.0
 */
@RestController
@RequestMapping("/interface/signupdata")
public class SignupDataController extends BaseController {

	private final static Logger log = LoggerFactory.getLogger(SignupDataController.class);

	@Autowired
	private SignupDataAddService signupDataAddService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtSignupService gjtSignupService;

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@Autowired
	private GjtOrgService gjtOrgService;

	@Autowired
	private GjtGradeSpecialtyService gjtGradeSpecialtyService;

	@Autowired
	private GjtSyncLogService gjtSyncLogService;

	@Autowired
	private ApiOpenClassService apiOpenClassService;

    @Autowired
    private GjtFlowService gjtFlowService;

	@Autowired
	private GjtStudentClassInfoService gjtStudentClassInfoService;

	@Autowired
	private CacheService cacheService;

	@Autowired
	CommonMapService commonMapService;

	/**
	 * 学籍资料同步接口<br/>
	 * 2017-09-23 支持预存学籍，可存多个报读专业学籍资料
	 * @param entity
	 * @return studentId 学员唯一标识
	 * 			syncType 同步类型 1-新增学籍 2-修改学籍
     */
	@SysLog("学籍资料同步接口")
	@RequestMapping(value = "/syncSignupData", method = RequestMethod.POST)
	public ResponseResult syncSignupData(GjtStudentInfo entity, HttpServletRequest request) {
		ResponseResult result;
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> requestParams = Servlets.getParametersStartingWith(request, null);
		try {
			if (StringUtils.isEmpty(entity.getStudentId()) || entity.getStudentId().length() != 32) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "学员ID不能为空或格式不正确!");
			}
			if (StringUtils.isEmpty(entity.getAtid())) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "ATID不能为空!");
			}
			if (StringUtils.isEmpty(entity.getXh())) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "学号不能为空!");
			}
			entity.setXh(entity.getXh().trim());
			if (StringUtils.isEmpty(entity.getXm())) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "姓名不能为空!");
			}
			entity.setXm(entity.getXm().trim());
			if (StringUtils.isEmpty(entity.getSfzh())) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "身份证号不能为空!");
			}
			entity.setSfzh(entity.getSfzh().trim());
			if (StringUtils.isEmpty(entity.getSjh()) || !ValidateUtil.isMobile(entity.getSjh())) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "手机号不能为空!");
			}
			entity.setSjh(entity.getSjh().trim());
			if (StringUtils.isEmpty(entity.getUserType()) || !ValidateUtil.isNumeric(entity.getUserType())) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "学员类型不能为空!");
			}
			if (StringUtils.isEmpty(entity.getGradeSpecialtyId())) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "产品ID不能为空!");
			}
			String collegeCode = request.getParameter("collegeCode");
			if (StringUtils.isEmpty(collegeCode)) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "院校编码不能为空!");
			}
			String learncenterCode = request.getParameter("learncenterCode");
			if (StringUtils.isEmpty(learncenterCode)) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心编码不能为空!");
			}
			GjtOrg schoolOrg = gjtOrgService.queryByCode(collegeCode);
			if(schoolOrg == null) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "院校编码不存在：找不到对应的院校编码!");
			}
			GjtOrg learncenterOrg = gjtOrgService.queryByCode(learncenterCode);
			if(learncenterOrg == null) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心编码不存在：请确认学习中心是否已经同步!");
			}
			String studyCenterOrgId = gjtOrgService.queryChildsByParentIdAndCode(schoolOrg.getId(), learncenterCode);
			if(studyCenterOrgId == null) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "院校下找不到学习中心：请确认院校与学习中心的关系!");
			}
			entity.setGjtSchoolInfo(new GjtSchoolInfo(schoolOrg.getId()));
			entity.setGjtStudyCenter(new GjtStudyCenter(studyCenterOrgId));
			entity.setOrgId(studyCenterOrgId);
			String zsdBm = request.getParameter("zsdBm");
			// 如果是招生点的学员，就查询出招生点机构信息
			if (StringUtils.isNotEmpty(zsdBm)) {
				String zsdOrgId = gjtOrgService.queryChildsByParentIdAndCode(schoolOrg.getId(), zsdBm);
				if(zsdOrgId == null) {
					return new ResponseResult(ResponseStatus.PARAM_ERROR, "招生点编码不存在：请确认招生点是否已经同步!");
				}
				entity.setGjtStudyCenter(new GjtStudyCenter(zsdOrgId));
				entity.setOrgId(zsdOrgId);
			}
			GjtGradeSpecialty gradeSpecialtyProduct = gjtGradeSpecialtyService.findOne(entity.getGradeSpecialtyId());
			if(gradeSpecialtyProduct == null) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "产品不存在，请确认产品ID是否对应正确!");
			}
			// 优先选择学习中心范围下的产品
			String studyCenterId = null;
			if(StringUtils.equals(learncenterOrg.getOrgType(), "3")) {
				studyCenterId = learncenterOrg.getId();
			} else if(StringUtils.equals(learncenterOrg.getParentGjtOrg().getOrgType(), "3")) {
				studyCenterId = learncenterOrg.getParentGjtOrg().getId();
			}
			GjtGradeSpecialty studyCenterGradeSpecialtyProduct = gjtGradeSpecialtyService.queryByGradeIdAndSpecialtyIdAndStudyCenterId(gradeSpecialtyProduct.getGjtGrade().getGradeId(), gradeSpecialtyProduct.getGjtSpecialty().getSpecialtyId(), studyCenterId);
			if (studyCenterGradeSpecialtyProduct != null) {
				if(!StringUtils.equals(gradeSpecialtyProduct.getId(), studyCenterGradeSpecialtyProduct.getId())) {
					return new ResponseResult(ResponseStatus.PARAM_ERROR, "产品ID不匹配，请确认学习中心下是否有该产品!");
				}
			} else {
				// 即是通用产品，不做判断判断
			}
			// 根据产品ID可以获取到专业、年级、培养层次
			entity.setGjtGrade(gradeSpecialtyProduct.getGjtGrade());
			entity.setGjtSpecialty(gradeSpecialtyProduct.getGjtSpecialty());
			entity.setMajor(gradeSpecialtyProduct.getGjtSpecialty().getSpecialtyId());
			entity.setPycc(gradeSpecialtyProduct.getGjtSpecialty().getPycc());
			String userTypeName = cacheService.getCachedDictionaryName(CacheService.DictionaryKey.USER_STUDENT_TYPE, entity.getUserType());
			if(userTypeName == null) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "学员类型不存在，请确认是否对应正确!");
			}
			GjtStudentInfo info = null;
			/**
			 * 根据培养层次是否存在学籍，已支付的订单，若不存在学籍，则更改可以预存学籍，若存在则无法操作；
			 * 规则说明：同一学员只能报读一个专科/本科。
			 */
			GjtStudentInfo existsInfo = signupDataAddService.queryExistsSignupDataByPycc(schoolOrg.getId(), entity.getSfzh(), entity.getPycc());
			if(existsInfo == null) {
				info = gjtStudentInfoService.queryById(entity.getStudentId());
				// 若不存在学籍，则新建学籍信息，若存在则更新学籍信息
				if (info == null) {
					if (gjtUserAccountService.queryByLoginAccount(entity.getXh()) != null) {
						return new ResponseResult(ResponseStatus.PARAM_ERROR, "学号已存在!");
					}
					entity.setGjtSignup(new GjtSignup(UUIDUtils.random()));
					String orderSn = request.getParameter("orderSn");
					String charge = request.getParameter("charge");
					String auditSource = request.getParameter("auditSource");
					if (StringUtils.isEmpty(orderSn)) {
						return new ResponseResult(ResponseStatus.PARAM_ERROR, "订单编号不能为空!");
					}
					entity.getGjtSignup().setOrderSn(orderSn);
					if (StringUtils.isEmpty(charge)) {
						return new ResponseResult(ResponseStatus.PARAM_ERROR, "收费状态不能为空!");
					}
					if (!ValidateUtil.isNumeric(charge)) {
						return new ResponseResult(ResponseStatus.PARAM_ERROR, "收费状态值类型有误!");
					}
					entity.getGjtSignup().setCharge(charge);
					/*if (StringUtils.isEmpty(auditSource)) {
						return new ResponseResult(ResponseStatus.PARAM_ERROR, "订单来源不能为空!");
					}
					entity.getGjtSignup().setAuditSource(auditSource);
					if (StringUtils.isEmpty(entity.getClassType())) {
						return new ResponseResult(ResponseStatus.PARAM_ERROR, "班级类型不能为空!");
					}
					if (StringUtils.equals(collegeCode, OrgUtil.SZLG)) {
						if (StringUtils.isBlank(entity.getUserclass())) {
							return new ResponseResult(ResponseStatus.PARAM_ERROR, "深圳市龙岗区社区学院的班级名称不能为空!");
						}
						if (signupDataAddService.queryExistsClassName(schoolOrg.getId(), entity.getUserclass()) == null) {
							return new ResponseResult(ResponseStatus.PARAM_ERROR, "班级名称不存在：深圳市龙岗区社区学院:" + entity.getUserclass());
						}
					}*/
					// 录入学籍
					Map<String, Object> addRec = signupDataAddService.addSignupData(entity);
					if (!((Boolean) addRec.get("successful"))) {
						log.error("syncSignupData fail ======== params:" + requestParams);
						log.error("syncSignupData fail ======== result:" + addRec);
						// 记录同步失败日志
						gjtSyncLogService.insert(new GjtSyncLog(entity.getXm(), entity.getXh(), Constants.RSBIZ_CODE_B0001, requestParams.toString(), addRec.toString()));
						return new ResponseResult(ResponseStatus.FAIL, "学籍信息同步失败:" + addRec.get("message"));
					}
					String studentId = (String) addRec.get("obj");
					info = gjtStudentInfoService.queryById(studentId);

					if(info != null && ("0".equals(info.getGjtSignup().getCharge()) || "1".equals(info.getGjtSignup().getCharge()))) {
						try {
							// 开启线程做其他事情
							new Thread(new SignupDataRunnable(info.getStudentId(), signupDataAddService, gjtStudentInfoService, apiOpenClassService, gjtFlowService, gjtSyncLogService))
									.start()
							;
						} catch(Throwable e) {
							// 开启线程做其他事情
							new SignupDataRunnable(info.getStudentId(), signupDataAddService, gjtStudentInfoService, apiOpenClassService, gjtFlowService, gjtSyncLogService)
									.run()
							;
						}
					}
					data.put("syncType", 1);
					// 记录同步成功日志
					gjtSyncLogService.insert(new GjtSyncLog(entity.getXm(), entity.getXh(), Constants.RSBIZ_CODE_B0001, requestParams.toString(), "success"));
				} else {
					if (StringUtils.equals(info.getGjtSignup().getAuditState(), Constants.BOOLEAN_1)) {
						return new ResponseResult(ResponseStatus.FAIL, "学员的学籍资料已审核通过，无法再修改!");
					}
					data.put("syncType", 2);
				}
			} else {
				info = existsInfo;
				// 这里是为了维护原来旧的atid
				if (!entity.getStudentId().equals(info.getStudentId())) {
					String msg = "学员ID不匹配，同一学员只能报读一个专科/本科!";
					// 记录同步失败日志
					gjtSyncLogService.insert(new GjtSyncLog(entity.getXm(), entity.getXh(), Constants.RSBIZ_CODE_B0001, requestParams.toString(), msg));
					return new ResponseResult(ResponseStatus.PARAM_ERROR, msg);
				}
				if (StringUtils.equals(info.getGjtSignup().getAuditState(), Constants.BOOLEAN_1)) {
					return new ResponseResult(ResponseStatus.FAIL, "学员的学籍资料已审核通过，无法再修改!");
				}
				data.put("syncType", 2);
			}
			entity.setStudentId(info.getStudentId());
			updateSignupDataInfo(entity, request);		// 更新学员学籍资料及证件照资料

			data.put("studentId", info.getStudentId());
			result = new ResponseResult(ResponseStatus.SUCCESS, data);
		} catch (Exception e) {
			result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, "服务器异常！");
			log.error("syncSignupData fail ======== params:" + requestParams);
			log.error("syncSignupData fail ======== error:{} | {}", e, e.getMessage());
			// 记录同步失败日志
			gjtSyncLogService.insert(new GjtSyncLog(entity.getXm(), entity.getXh(), Constants.RSBIZ_CODE_B0001, requestParams.toString(), e.toString().length() > 500 ? e.toString().substring(0, 500) : e.toString()));
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 更新学员学籍资料及证件照资料
	 * @param entity
	 * @param request
     */
	protected void updateSignupDataInfo(GjtStudentInfo entity, HttpServletRequest request) {
		// 1.修改学员学籍资料
		GjtStudentInfo modifyInfo = gjtStudentInfoService.queryById(entity.getStudentId());
		if(StringUtils.isNotEmpty(entity.getNativeplace())) modifyInfo.setNativeplace(entity.getNativeplace());
		if(StringUtils.isNotEmpty(entity.getMzm())) modifyInfo.setMzm(entity.getMzm());
		if(StringUtils.isNotEmpty(entity.getPoliticsstatus())) modifyInfo.setPoliticsstatus(entity.getPoliticsstatus());
		if(StringUtils.isNotEmpty(entity.getHyzkm())) modifyInfo.setHyzkm(entity.getHyzkm());
		if(StringUtils.isNotEmpty(entity.getHkxzm())) modifyInfo.setHkxzm(entity.getHkxzm());
		if(StringUtils.isNotEmpty(entity.getCsrq())) modifyInfo.setCsrq(entity.getCsrq());
		if(StringUtils.isNotEmpty(entity.getIsonjob())) modifyInfo.setIsonjob(entity.getIsonjob());
		if(StringUtils.isNotEmpty(entity.getHkszd())) modifyInfo.setHkszd(entity.getHkszd());
		if(StringUtils.isNotEmpty(entity.getSjh())) modifyInfo.setSjh(entity.getSjh());
		if(StringUtils.isNotEmpty(entity.getLxdh())) modifyInfo.setLxdh(entity.getLxdh());
		if(StringUtils.isNotEmpty(entity.getDzxx())) modifyInfo.setDzxx(entity.getDzxx());
		if(StringUtils.isNotEmpty(entity.getProvince())) modifyInfo.setProvince(entity.getProvince());
		if(StringUtils.isNotEmpty(entity.getCity())) modifyInfo.setCity(entity.getCity());
		if(StringUtils.isNotEmpty(entity.getArea())) modifyInfo.setArea(entity.getArea());
		if(StringUtils.isNotEmpty(entity.getTxdz())) modifyInfo.setTxdz(entity.getTxdz().trim());
		if(StringUtils.isNotEmpty(entity.getYzbm())) modifyInfo.setYzbm(entity.getYzbm());
		if(StringUtils.isNotEmpty(entity.getScCo())) modifyInfo.setScCo(entity.getScCo());
		if(StringUtils.isNotEmpty(entity.getScCoAddr())) modifyInfo.setScCoAddr(entity.getScCoAddr());
		String jobPost = request.getParameter("jobPost");
		if(StringUtils.isNotEmpty(jobPost)) modifyInfo.getGjtSignup().setJobPost(jobPost);
		if(StringUtils.isNotEmpty(entity.getExedulevel())) modifyInfo.setExedulevel(entity.getExedulevel());
		if(StringUtils.isNotEmpty(entity.getExschool())) modifyInfo.setExschool(entity.getExschool());
		if(StringUtils.isNotEmpty(entity.getExgraduatedtime())) modifyInfo.setExgraduatedtime(entity.getExgraduatedtime());
		if(StringUtils.isNotEmpty(entity.getExsubject())) modifyInfo.setExsubject(entity.getExsubject());
		if(StringUtils.isNotEmpty(entity.getExsubjectkind())) modifyInfo.setExsubjectkind(entity.getExsubjectkind());
		if(StringUtils.isNotEmpty(entity.getExedubaktype())) modifyInfo.setExedubaktype(entity.getExedubaktype());
		if(StringUtils.isNotEmpty(entity.getExedumajor())) modifyInfo.setExedumajor(entity.getExedumajor());
		if(StringUtils.isNotEmpty(entity.getExcertificatenum())) modifyInfo.setExcertificatenum(entity.getExcertificatenum());
		if(StringUtils.isNotEmpty(entity.getExcertificateprove())) modifyInfo.setExcertificateprove(entity.getExcertificateprove());
		if(StringUtils.isNotEmpty(entity.getExcertificateprovenum())) modifyInfo.setExcertificateprovenum(entity.getExcertificateprovenum());
		if(StringUtils.isNotEmpty(entity.getExeduname())) modifyInfo.setExeduname(entity.getExeduname());
		if(StringUtils.isNotEmpty(entity.getExedunum())) modifyInfo.setExedunum(entity.getExedunum());
		if(StringUtils.isNotEmpty(entity.getIsgraduatebytv())) modifyInfo.setIsgraduatebytv(entity.getIsgraduatebytv());
		modifyInfo.setUpdatedBy("新接口调用修改");
		try {
			gjtStudentInfoService.updateEntityAndFlushCache(modifyInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 2.修改证件照资料
		// 更新各个选项
		String signupSfzType = request.getParameter("signupSfzType");
		String zgxlRadioType = request.getParameter("zgxlRadioType");
		String signupByzType = request.getParameter("signupByzType");
		String signupJzzType = request.getParameter("signupJzzType");
		if (StringUtils.isNotBlank(signupSfzType) || StringUtils.isNotBlank(zgxlRadioType) || StringUtils.isNotBlank(signupByzType) || StringUtils.isNotBlank(signupJzzType)) {
			gjtSignupService.updateEveryType(entity.getStudentId(), signupSfzType, zgxlRadioType, signupByzType, signupJzzType);
		}

		final Map<String, String> signupCopyData = new HashMap<String, String>();
		signupCopyData.put("zp", request.getParameter("zp"));
		signupCopyData.put("sfz-z", request.getParameter("sfzz"));
		signupCopyData.put("sfz-f", request.getParameter("sfzf"));
		signupCopyData.put("jzz", request.getParameter("jzz")); // 异地学员(非广东)，需提供居住证或工牌
		signupCopyData.put("jzzf", request.getParameter("jzzf")); // 居住证反
		signupCopyData.put("ygzm", request.getParameter("ygzm")); // 工牌
		signupCopyData.put("byz-z", request.getParameter("byzz"));
		signupCopyData.put("xlz", request.getParameter("xlz"));
		signupCopyData.put("dzzch", request.getParameter("dzzch")); // 毕业电子注册号证明
		signupCopyData.put("xsz", request.getParameter("xsz")); // 国家开放大学或广州电大学生证原件
		signupCopyData.put("cjd", request.getParameter("cjd")); // 成绩单
		signupCopyData.put("lqmc", request.getParameter("lqmc")); // 录取名册或入学通知书
		signupCopyData.put("yjbyszm", request.getParameter("yjbyszm")); // 应届毕业生证明
		signupCopyData.put("ykcns", request.getParameter("ykcns")); // 预科生承诺书

		signupCopyData.put("xlzm", request.getParameter("xlzm")); // 学历证明
		signupCopyData.put("cns", request.getParameter("cns")); // 专科承诺书
		String jbrSign = request.getParameter("jbrSign"); // 经办人签名
		if (StringUtils.isBlank(jbrSign) && StringUtils.equals(modifyInfo.getGjtSchoolInfo().getGjtOrg().getCode(), OrgUtil.GK_SY)) {
			jbrSign = "http://eefile.download.eenet.com/interface/files2/xlims/image/5cfe1cad572ca1b345efe27143f9c362.png"; // 国开实验学院默认经办人
		}
		if(StringUtils.isNotBlank(jbrSign)) {
			if(jbrSign.contains("base64,")) {
				String tmpFolderPath = request.getSession().getServletContext().getRealPath("") + File.separator + "tmp" + File.separator
						+ DateFormatUtils.ISO_DATE_FORMAT.format(Calendar.getInstance());
				String signPath = EEFileUploadUtil.uploadImageBase64ToUrl(jbrSign, tmpFolderPath);
				signupCopyData.put("jbrSign", signPath); // 经办人签名
			} else {
				signupCopyData.put("jbrSign", jbrSign); // 经办人签名
			}
		}
		String sign = request.getParameter("sign"); // 签名
		if(StringUtils.isNotBlank(sign)) {
			if(sign.contains("base64,")) {
				String tmpFolderPath = request.getSession().getServletContext().getRealPath("") + File.separator + "tmp" + File.separator
						+ DateFormatUtils.ISO_DATE_FORMAT.format(Calendar.getInstance());
				String signPath = EEFileUploadUtil.uploadImageBase64ToUrl(sign, tmpFolderPath);
				signupCopyData.put("sign", signPath); // 签名
			} else {
				signupCopyData.put("sign", sign); // 签名
			}
		}
		try {
			gjtSignupService.updateSignupCopyData(entity.getStudentId(), signupCopyData);
			gjtStudentInfoService.updateEntityAndFlushCache(gjtStudentInfoService.queryById(entity.getStudentId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据atid或userId查询studentId接口
	 * @return studentId 学员唯一标识
	 * 			userId
	 * 			pycc 培养层次
	 * 			xjzt 学籍状态 2-在籍 3-待注册 0-注册中 4-休学 5-退学 8-毕业 10-转学
	 * 			auditState 学籍资料审核状态 0-不通过 1-通过 其他-待审核
	 */
	@RequestMapping(value = "/getStudentIds", method = RequestMethod.POST)
	public ResponseResult getStudentIds(HttpServletRequest request) {
		ResponseResult result;
		Map<String, Object> requestParams = Servlets.getParametersStartingWith(request, null);
		String xm = (String) requestParams.get("xm");
		String atid = (String) requestParams.get("atid");
		String userId = (String) requestParams.get("userId");
		String sfzh = (String) requestParams.get("sfzh");
		String orderSn = (String) requestParams.get("orderSn");
		try {
			if (StringUtils.isEmpty(xm)) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "姓名不能为空");
			}
			if (StringUtils.isEmpty(atid) && StringUtils.isEmpty(userId) && StringUtils.isEmpty(sfzh) && StringUtils.isEmpty(orderSn)) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "atid、userId、sfzh、orderSn必传其中一个");
			}

			List<Map> list = gjtStudentInfoService.queryStudentIdsBy(requestParams);

			result = new ResponseResult(ResponseStatus.SUCCESS, list);
		} catch (Exception e) {
			result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, "服务器异常！");
			log.error("getStudentIds fail ======== params:" + requestParams);
			log.error("getStudentIds fail ======== error:{} | {}", e, e.getMessage());
			// 记录同步失败日志
			gjtSyncLogService.insert(new GjtSyncLog(atid, userId, Constants.RSBIZ_CODE_B0001, requestParams.toString(), e.toString().length() > 500 ? e.toString().substring(0, 500) : e.toString()));
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 读取学期开设专业
	 * @return
     */
	@RequestMapping(value = "/listAll")
	public List<Map<String, Object>> listAll(HttpServletRequest request){
		Map searchParams = Servlets.getParametersStartingWith(request,"");
		String codes = ObjectUtils.toString(searchParams.get("collegeCode"));
		String gradeId = ObjectUtils.toString(searchParams.get("gradeId"));
		String xxId = null;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("gradeId", gradeId);
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		if (StringUtils.isBlank(codes)) { // 参数为空获取全部
			List<Map<String, Object>> orgAll = gjtStudentClassInfoService.getOrgAll();
			for (Map<String, Object> org : orgAll) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("collegeCode", org.get("CODE"));

				xxId = ObjectUtils.toString(org.get("ID"));
				param.put("xxId", xxId);
				List<Map<String, Object>> gradeSpecialts = gjtStudentClassInfoService.queryGradeSpecialt(param);
				if (gradeSpecialts != null && gradeSpecialts.size() > 0) {
					List<Map<String, Object>> datas = this.convert(gradeSpecialts);
					resultMap.put("datas", datas);
					resultMap.put("state", 1);
				} else {
					resultMap.put("state", 0);
				}

				results.add(resultMap);
			}
		}else {
			String[] strs = codes.split(",");
			List<Map<String, Object>> orgs = gjtStudentClassInfoService.getOrgByCodes(strs); // 根据院校编码查询
			for (String code : strs) { // 传入值
				Map<String, Object> resultMap = null;
				if (orgs != null && orgs.size() > 0) {
					for (Map<String, Object> org : orgs) {
						String extr_code = (String)org.get("CODE");
						if (code.equals(extr_code)) {
							resultMap = new HashMap<String, Object>();

							xxId = (String)org.get("ID");
							param.put("xxId", xxId);
							List<Map<String, Object>> gradeSpecialts = gjtStudentClassInfoService.queryGradeSpecialt(param);
							if (gradeSpecialts != null && gradeSpecialts.size() > 0) {
								List<Map<String, Object>> datas = this.convert(gradeSpecialts);
								resultMap.put("datas", datas);
								resultMap.put("state", 1);
							} else {
								resultMap.put("state", 0);
							}
							break;
						}
					}
					if (resultMap == null) {
						resultMap = new HashMap<String, Object>();
						resultMap.put("message", "院校编码有误，查询不到院校信息");
						resultMap.put("state", 0);
					}
				}else {
					resultMap = new HashMap<String, Object>();
					resultMap.put("message", "院校编码有误，查询不到院校信息");
					resultMap.put("state", 0);
				}
				resultMap.put("collegeCode", code);
				results.add(resultMap);
			}
		}
		return results;
	}


	private List<Map<String, Object>> convert(List<Map<String, Object>> gradeSpecialts) {
		List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
		Map<String, Map<String, Object>> datas = new HashMap<String, Map<String,Object>>();
		for (Map<String, Object> gradeSpecialt : gradeSpecialts) {
			String gradeId = (String) gradeSpecialt.get("GRADE_ID");
			String gradeName = (String) gradeSpecialt.get("GRADE_NAME");
			Date startDate = (Date) gradeSpecialt.get("START_DATE");
			Date endDate = (Date) gradeSpecialt.get("END_DATE");
			Map<String, Object> specialty = new HashMap<String, Object>();
			specialty.put("gradeSpecialtyId", gradeSpecialt.get("GRADE_SPECIALTY_ID"));
			specialty.put("specialtyId", gradeSpecialt.get("SPECIALTY_ID"));
			specialty.put("specialtyName", gradeSpecialt.get("ZYMC"));
			specialty.put("specialtyRuleCode", gradeSpecialt.get("RULE_CODE"));
			specialty.put("specialtyLevelId", gradeSpecialt.get("PYCC"));
			specialty.put("specialtyLevel", gradeSpecialt.get("SPECIALTY_LEVEL"));
			specialty.put("applyRange", gradeSpecialt.get("APPLY_RANGE"));
			specialty.put("applyRangeIds", StringUtils.defaultString((String) gradeSpecialt.get("APPLY_RANGE_IDS")));
			specialty.put("specialtySchoolAge", gradeSpecialt.get("XZ"));
			specialty.put("specialtyMinCredit", gradeSpecialt.get("ZDBYXF"));
			specialty.put("specialtySubject", gradeSpecialt.get("SUBJECT"));
			specialty.put("specialtyCode", gradeSpecialt.get("SPECIALTY_CODE"));

			if (datas.containsKey(gradeId)) {
				Map<String, Object> data = datas.get(gradeId);
				List<Map<String, Object>> specialtys = (List<Map<String, Object>>) data.get("specialtys");
				specialtys.add(specialty);
			} else {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("gradeId", gradeId);
				data.put("gradeName", gradeName);
				data.put("startDate", startDate != null ? DateUtil.dateToString(startDate, "yyyy-MM-dd") : null);
				data.put("endDate", endDate != null ? DateUtil.dateToString(endDate, "yyyy-MM-dd") : null);

				List<Map<String, Object>> specialtys = new ArrayList<Map<String, Object>>();
				specialtys.add(specialty);
				data.put("specialtys", specialtys);

				datas.put(gradeId, data);
			}
		}

		Iterator<Map<String, Object>> iterator = datas.values().iterator();
		while (iterator.hasNext()) {
			Map<String, Object> map = iterator.next();
			results.add(map);
		}

		return results;
	}

	/**
	 * 获取学期列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryGradeList")
	public List<Map<String,Object>> queryGradeList(HttpServletRequest request){
		Map searchParams = Servlets.getParametersStartingWith(request,"");
		String codes = ObjectUtils.toString(searchParams.get("collegeCode"));
		Map org = gjtStudentClassInfoService.getOrgByCollegeCode(codes);
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (EmptyUtils.isNotEmpty(org)){
			String xxId = (String) org.get("ID");
			List<Map<String, Object>> gradeList = gjtStudentClassInfoService.queryGradeList(xxId);
			resultMap.put("result", true);
			resultMap.put("result", gradeList);
		}else {
			resultMap.put("message", "院校编码有误，查询不到院校信息");
			resultMap.put("result", false);
		}

		results.add(resultMap);

		return results;
	}

	/**
	 * 注销学籍接口，或删除未缴费学员<br/>
	 * 2017-11-16 定义新接口<br/>
	 * @param studentId 学员唯一标识
	 * @param orderSn 订单号
	 * @return 
     */
	@SysLog("注销学籍接口")
	@RequestMapping(value = "/revokedSignup", method = RequestMethod.POST)
	public ResponseResult revokedSignup(HttpServletRequest request) {
		ResponseResult result;
		Map<String, Object> requestParams = Servlets.getParametersStartingWith(request, null);
			
		String studentId = (String) requestParams.get("studentId");
		String orderSn = (String) requestParams.get("orderSn");
		if (StringUtils.isEmpty(studentId)) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "学员ID不能为空!");
		}
		if (StringUtils.isEmpty(orderSn)) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "订单号不能为空!");
		}
		GjtStudentInfo info = gjtStudentInfoService.queryById(studentId);
		if(info == null) {
			return new ResponseResult(ResponseStatus.FAIL, "学籍不存在!");
		}
		if(!StringUtils.equals(info.getGjtSignup().getOrderSn(), orderSn)) {
			return new ResponseResult(ResponseStatus.FAIL, "订单号不匹配!");
		}
		try {
			// 注销学籍
			boolean force = StringUtils.equals((String) requestParams.get("force"), Constants.BOOLEAN_1);
			Map<String, Object> rec = signupDataAddService.revokedSignup(studentId, force);
			if (!((Boolean) rec.get("successful"))) {
				log.error("revokedSignup fail ======== params:" + requestParams);
				log.error("revokedSignup fail ======== result:" + rec);
				// 记录同步失败日志
				gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0004, requestParams.toString(), rec.toString()));
				return new ResponseResult(ResponseStatus.FAIL, "注销学籍失败：" + rec.get("message"));
			}
			
			// 记录同步成功日志
			gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0004, requestParams.toString(), "success"));
			result = new ResponseResult(ResponseStatus.SUCCESS, null);
		} catch (Exception e) {
			result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, "服务器异常！");
			log.error("revokedSignup fail ======== params:" + requestParams);
			log.error("revokedSignup fail ======== error:{} | {}", e, e.getMessage());
			// 记录同步失败日志
			gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0004, requestParams.toString(), e.toString().length() > 500 ? e.toString().substring(0, 500) : e.toString()));
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 国开在线服务学员微信信息同步接口-运营平台调用<br/>
	 * 2017-11-18 提供给运营平台使用<br/>
	 * @param sfzh 学员唯一标识
	 * @param wxOpenId 	是 	String 	微信用户标识openId
	 * @param wxUnionId 	是 	String 	微信用户标识unionId
	 * @param wxHeadPortrait 	否 	String 	微信头像
	 * @param wxNickName 	否 	String 	微信昵称
	 * @return 
     */
	@SysLog("国开在线服务学员微信信息同步接口")
	@RequestMapping(value = "/syncWxInfo", method = RequestMethod.POST)
	public ResponseResult syncWxInfo(HttpServletRequest request) {
		ResponseResult result;
		Map<String, Object> requestParams = Servlets.getParametersStartingWith(request, null);
			
		String sfzh = (String) requestParams.get("sfzh");
		String wxOpenId = (String) requestParams.get("wxOpenId");
		String wxUnionId = (String) requestParams.get("wxUnionId");
		String wxHeadPortrait = (String) requestParams.get("wxHeadPortrait");
		String wxNickName = (String) requestParams.get("wxNickName");
		if (StringUtils.isEmpty(sfzh)) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "身份证号不能为空!");
		}
		if (StringUtils.isEmpty(wxOpenId)) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "缺少参数!");
		}
		GjtStudentInfo info = gjtStudentInfoService.querySSOByXhOrSfzh(sfzh);
		if(info == null) {
			return new ResponseResult(ResponseStatus.FAIL, "学员不存在!");
		}
		if(info.getGjtUserAccount() == null) {
			return new ResponseResult(ResponseStatus.FAIL, "账号不存在!");
		}
		try {
			gjtStudentInfoService.updateWxInfo(sfzh, wxOpenId, requestParams, "接口同步-微信信息");
			// 记录同步成功日志
			gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0005, requestParams.toString(), "success"));
			result = new ResponseResult(ResponseStatus.SUCCESS, null);
		} catch (Exception e) {
			result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, "服务器异常！");
			log.error("syncWxInfo fail ======== params:" + requestParams);
			log.error("syncWxInfo fail ======== error:{} | {}", e, e.getMessage());
			// 记录同步失败日志
			gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0005, requestParams.toString(), e.toString().length() > 500 ? e.toString().substring(0, 500) : e.toString()));
			e.printStackTrace();
		}
		return result;
	}

}
