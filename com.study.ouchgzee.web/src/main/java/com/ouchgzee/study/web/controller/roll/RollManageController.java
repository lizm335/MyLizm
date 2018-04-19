/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.controller.roll;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.eefile.EEFileUploadUtil;
import com.gzedu.xlims.common.eefile.ValidateIDCard;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.gzedu.xlims.common.mail.util.MailUtil;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.dto.StudentSignupInfoDto;
import com.gzedu.xlims.pojo.flow.GjtFlowRecord;
import com.gzedu.xlims.pojo.status.SignupAuditStateEnum;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.flow.GjtFlowService;
import com.gzedu.xlims.service.signup.GjtSignupService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.ouchgzee.study.web.common.AdmissionNoticeUtil;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.vo.StudentAuditSituationVo;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 学籍管理
 * 功能说明：
 *
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年3月14日
 * @version 2.5
 *
 */
@RestController
@RequestMapping("/pcenter/roll")
public class RollManageController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(RollManageController.class);

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtSignupService gjtSignupService;

	@Autowired
	private GjtFlowService gjtFlowService;

	@Autowired
	private CacheService cacheService;

	/**
	 * 获取学籍基本资料
	 * @return
	 */
	@RequestMapping(value = "getBaseInfo", method = RequestMethod.GET)
	public Map<String, Object> getBaseInfo(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		StudentSignupInfoDto signupInfo = gjtStudentInfoService.getStudentSignupInfoById(user.getGjtStudentInfo().getStudentId());
		signupInfo.setXbmName(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.SEX, signupInfo.getXbm()));
		signupInfo.setPyccName(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.TRAININGLEVEL, signupInfo.getPycc()));

		Map<String, String> signupCopyData = gjtSignupService.getSignupCopyData(user.getGjtStudentInfo().getStudentId());
		signupInfo.setSign(signupCopyData.get("sign"));

		// 以下是页面上一些下拉的值
		resultMap.put("info", signupInfo);
		resultMap.put("dicNationList", cacheService.getCachedDictionary(CacheService.DictionaryKey.NATIONALITYCODE)); // 民族字典
		resultMap.put("dicPoliticsstatuList", CacheService.Dictionary.dicPoliticsstatuList); // 政治面貌字典
		resultMap.put("dicMaritalList", cacheService.getCachedDictionary(CacheService.DictionaryKey.MARITALSTATUSCODE)); // 婚姻状态字典
		resultMap.put("dicAccountsNatureList", cacheService.getCachedDictionary(CacheService.DictionaryKey.ACCOUNTSNATURECODE)); // 户口性质字典
		resultMap.put("dicOccupationList", cacheService.getCachedDictionary(CacheService.DictionaryKey.OCCUPATIONSTATUS)); // 职业状态字典
		resultMap.put("dicExedulevelList", CacheService.Dictionary.dicExedulevelList); // 原学历层次字典
		resultMap.put("dicExsubjectList", CacheService.Dictionary.dicExsubjectList); // 原学科字典
		resultMap.put("dicExedubaktypeList", CacheService.Dictionary.dicExedubaktypeList); // 原学历学习类型字典
		resultMap.put("dicExcertificateproveList", CacheService.Dictionary.dicExcertificateproveList); // 原学历证明材料字典
		resultMap.put("dicExeduCertificateList", CacheService.Dictionary.dicExeduCertificateList); // 原学历证件类型字典
		resultMap.put("dicIsgraduatebytvList", CacheService.Dictionary.dicIsgraduatebytvList); // 是否电大毕业字典
		resultMap.put("dicStudentNumberStatusList", cacheService.getCachedDictionary(CacheService.DictionaryKey.STUDENTNUMBERSTATUS)); // 学籍状态字典
		resultMap.put("dicSexList", cacheService.getCachedDictionary(CacheService.DictionaryKey.SEX)); // 性别字典
		return resultMap;
	}

	/**
	 * 修改基本资料
	 * @param info
	 * @throws CommonException
	 */
	@RequestMapping(value = "updateBaseInfo", method = RequestMethod.POST)
	public void updateBaseInfo(GjtStudentInfo info, HttpServletRequest request) throws CommonException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo modifyInfo = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());
		if (StringUtils.equals(modifyInfo.getGjtSignup().getAuditState(), Constants.BOOLEAN_1)) {
			throw new CommonException(MessageCode.BIZ_ERROR, "学员的学籍资料已审核通过，无法再修改!");
		}

		if(StringUtils.isNotEmpty(info.getNativeplace())) modifyInfo.setNativeplace(info.getNativeplace());
		if(StringUtils.isNotEmpty(info.getMzm())) modifyInfo.setMzm(info.getMzm());
		if(StringUtils.isNotEmpty(info.getPoliticsstatus())) modifyInfo.setPoliticsstatus(info.getPoliticsstatus());
		if(StringUtils.isNotEmpty(info.getHyzkm())) modifyInfo.setHyzkm(info.getHyzkm());
		if(StringUtils.isNotEmpty(info.getHkxzm())) modifyInfo.setHkxzm(info.getHkxzm());
		if(StringUtils.isNotEmpty(info.getXbm())) modifyInfo.setXbm(info.getXbm());
		if(StringUtils.isNotEmpty(info.getCsrq())) modifyInfo.setCsrq(info.getCsrq());

		if(StringUtils.isNotEmpty(info.getIsonjob())) {
			/*String jobName = cacheService.getCachedDictionaryName(CacheService.DictionaryKey.OCCUPATIONSTATUS, info.getIsonjob());
			if(jobName == null) {
				throw new CommonException(MessageCode.BAD_REQUEST, "在职状况参数有误，请下载最新APP！");
			}*/
			modifyInfo.setIsonjob(info.getIsonjob());
		}
		if(StringUtils.isNotEmpty(info.getHkszd())) modifyInfo.setHkszd(info.getHkszd());
		if(StringUtils.isNotEmpty(info.getSjh())) modifyInfo.setSjh(info.getSjh());
		if(StringUtils.isNotEmpty(info.getLxdh())) modifyInfo.setLxdh(info.getLxdh());
		if(StringUtils.isNotEmpty(info.getDzxx())) modifyInfo.setDzxx(info.getDzxx());
		if(StringUtils.isNotEmpty(info.getProvince())) modifyInfo.setProvince(info.getProvince());
		if(StringUtils.isNotEmpty(info.getCity())) modifyInfo.setCity(info.getCity());
		if(StringUtils.isNotEmpty(info.getArea())) modifyInfo.setArea(info.getArea());
		if(StringUtils.isNotEmpty(info.getTxdz())) modifyInfo.setTxdz(info.getTxdz().trim());
		if(StringUtils.isNotEmpty(info.getYzbm())) modifyInfo.setYzbm(info.getYzbm());
		if(StringUtils.isNotEmpty(info.getScCo())) modifyInfo.setScCo(info.getScCo());
		if(StringUtils.isNotEmpty(info.getScCoAddr())) modifyInfo.setScCoAddr(info.getScCoAddr());
		String jobPost = request.getParameter("jobPost");
		if(StringUtils.isNotEmpty(jobPost)) modifyInfo.getGjtSignup().setJobPost(jobPost);
		if(StringUtils.isNotEmpty(info.getExedulevel())) modifyInfo.setExedulevel(info.getExedulevel());
		if(StringUtils.isNotEmpty(info.getExschool())) modifyInfo.setExschool(info.getExschool());
		if(StringUtils.isNotEmpty(info.getExgraduatedtime())) modifyInfo.setExgraduatedtime(info.getExgraduatedtime());
		if(StringUtils.isNotEmpty(info.getExsubject())) modifyInfo.setExsubject(info.getExsubject());
		if(StringUtils.isNotEmpty(info.getExsubjectkind())) modifyInfo.setExsubjectkind(info.getExsubjectkind());
		if(StringUtils.isNotEmpty(info.getExedubaktype())) modifyInfo.setExedubaktype(info.getExedubaktype());
		if(StringUtils.isNotEmpty(info.getExedumajor())) modifyInfo.setExedumajor(info.getExedumajor());
		if(StringUtils.isNotEmpty(info.getExcertificatenum())) modifyInfo.setExcertificatenum(info.getExcertificatenum());
		if(StringUtils.isNotEmpty(info.getExcertificateprove())) modifyInfo.setExcertificateprove(info.getExcertificateprove());
		if(StringUtils.isNotEmpty(info.getExcertificateprovenum())) modifyInfo.setExcertificateprovenum(info.getExcertificateprovenum());
		if(StringUtils.isNotEmpty(info.getExeduname())) modifyInfo.setExeduname(info.getExeduname());
		if(StringUtils.isNotEmpty(info.getExedunum())) modifyInfo.setExedunum(info.getExedunum());
		if(StringUtils.isNotEmpty(info.getIsgraduatebytv())) modifyInfo.setIsgraduatebytv(info.getIsgraduatebytv());
		modifyInfo.setUpdatedBy(user.getId());

		try {
			gjtStudentInfoService.updateEntityAndFlushCache(modifyInfo);
		} catch (Exception e) {
			throw new CommonException(MessageCode.BIZ_ERROR, "更新失败");
		}
	}

	/**
	 * 获取证件资料
	 * @return
	 */
	@RequestMapping(value = "getCertificateInfo", method = RequestMethod.GET)
	public Map<String, Object> getCertificateInfo(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());
		Map<String, String> signupCopyDataTemp = gjtSignupService.getSignupCopyData(studentInfo.getStudentId());
		boolean isUndergraduateCourse = gjtStudentInfoService.isUndergraduateCourseById(studentInfo.getStudentId()); // 是否是本科层次

		Map<String, String> signupCopyData = new HashMap<String, String>();
		for (Entry<String, String> e : signupCopyDataTemp.entrySet()) {
			signupCopyData.put(e.getKey().replace("-", ""), e.getValue());
		}

		resultMap.putAll(signupCopyData);
		resultMap.put("isUndergraduateCourse", isUndergraduateCourse ? 1 : 0); // 是否是本科层次 1-是本科 0-非本科
		boolean isOffsite = OrgUtil.isOffsite(studentInfo.getGjtSchoolInfo().getGjtOrg().getCode(), studentInfo.getSfzh());
		resultMap.put("isOffsite", isOffsite ? 1 : 0); // 是否是异地学员(非广东) 1-是 0-否
		resultMap.put("userType", studentInfo.getUserType());
		resultMap.put("signupSfzType", studentInfo.getGjtSignup().getSignupSfzType());
		resultMap.put("zgxlRadioType", studentInfo.getGjtSignup().getZgxlRadioType());
		resultMap.put("signupByzType", studentInfo.getGjtSignup().getSignupByzType());
		resultMap.put("signupJzzType", studentInfo.getGjtSignup().getSignupJzzType());
		return resultMap;
	}

	/**
	 * 修改证件资料
	 * @throws CommonException
	 */
	@RequestMapping(value = "updateCertificateInfo", method = RequestMethod.POST)
	public void updateCertificateInfo(HttpServletRequest request) throws CommonException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo info = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());
		if (StringUtils.equals(info.getGjtSignup().getAuditState(), Constants.BOOLEAN_1)) {
			throw new CommonException(MessageCode.BIZ_ERROR, "学员的学籍资料已审核通过，无法再修改!");
		}

		// 更新各个选项
		String signupSfzType = request.getParameter("signupSfzType");
		String zgxlRadioType = request.getParameter("zgxlRadioType");
		String signupByzType = request.getParameter("signupByzType");
		String signupJzzType = request.getParameter("signupJzzType");
		if (StringUtils.isNotBlank(signupSfzType) || StringUtils.isNotBlank(zgxlRadioType) || StringUtils.isNotBlank(signupByzType) || StringUtils.isNotBlank(signupJzzType)) {
			gjtSignupService.updateEveryType(user.getGjtStudentInfo().getStudentId(), signupSfzType, zgxlRadioType, signupByzType, signupJzzType);
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
		if (StringUtils.isBlank(jbrSign) && StringUtils.equals(info.getGjtSchoolInfo().getGjtOrg().getCode(), OrgUtil.GK_SY)) {
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
		String complete = request.getParameter("complete"); // 资料是否填写完整 1-是

		ValidateIDCard validateIDCard = null;
		if("0".equals(signupSfzType)) {
			validateIDCard = new ValidateIDCard() {

				@Override
				public boolean validate() {
					// begin 识别身份证
					String sfzz = signupCopyData.get("sfz-z");
					String sfzf = signupCopyData.get("sfz-f");
					if (StringUtils.isNotBlank(sfzz)) {
						boolean idCard = EEFileUploadUtil.isIDCard(sfzz);
						if (!idCard) {
							return false;
						}
					}
					if (StringUtils.isNotBlank(sfzf)) {
						boolean idCard = EEFileUploadUtil.isIDCard(sfzf);
						if (!idCard) {
							return false;
						}
					}
					// end 识别身份证
					return true;
				}

			};
		}

		if(validateIDCard == null || validateIDCard.validate()) {
			gjtStudentInfoService.updateEntityAndFlushCache(info);
			gjtSignupService.updateSignupCopyData(user.getGjtStudentInfo().getStudentId(), signupCopyData);

			// 资料若已填写完整则确认提交
			if(Constants.BOOLEAN_1.equals(complete) || StringUtils.isNotBlank(signupCopyData.get("sign"))) {
				// 更新完善资料状态
				gjtStudentInfoService.perfectSignupAndCertificateInfo(user.getGjtStudentInfo().getStudentId());
				// 确认提交资料
				gjtFlowService.initAuditSignupInfo(user.getGjtStudentInfo().getStudentId());
			}
		} else {
			throw new CommonException(600, "身份证照片拍摄不正确，无法识别，请按照示例要求重新拍照上传");
		}
	}

	/**
	 * 入学通知书确认<br>
	 * @return
	 */
	@RequestMapping(value = "confirm", method = RequestMethod.GET)
	public void confirm(HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		gjtStudentInfoService.confirm(user.getGjtStudentInfo().getStudentId());
	}

	/**
	 * 获取通知书，返回IO流
	 */
	@RequestMapping(value = "getNoticeToFile", method = RequestMethod.GET)
	public void getNoticeToFile(HttpServletRequest request, HttpServletResponse response) {
		ServletOutputStream outStream = null;
		try {
			outStream = response.getOutputStream();
			// 设置响应的类型格式为图片格式
			response.setContentType("image/jpeg");
			// 禁止图像缓存。
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);

			// 创键编码器，用于编码内存中的图象数据。
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());
			BufferedImage tag = AdmissionNoticeUtil.writeStudentInfo(studentInfo, request);
			JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
			/** 压缩质量 */
			jep.setQuality(1f, true);
			JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(outStream, jep);
			en.encode(tag);
			outStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outStream.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 下载通知书
	 */
	@RequestMapping(value = "downloadNoticeToFile", method = RequestMethod.GET)
	public void downloadNoticeToFile(HttpServletRequest request, HttpServletResponse response) {
		String outputFileName = "tongzhishu.jpg";
		ServletOutputStream outStream = null;
		try {
			outStream = response.getOutputStream();
			response.setContentType("application/x-msdownload;charset=utf-8");
			// response.setHeader("Content-Disposition", "attachment; filename="
			// + new String(outputFileName.getBytes("UTF-8"), "ISO8859-1"));
			// 解决IE下载文件名乱码问题
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(outputFileName.getBytes("GB2312"), "ISO8859-1"));

			// 创键编码器，用于编码内存中的图象数据。
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());
			BufferedImage tag = AdmissionNoticeUtil.writeStudentInfo(studentInfo, request);
			JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
			/** 压缩质量 */
			jep.setQuality(1f, true);
			JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(outStream, jep);
			en.encode(tag);
			outStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				super.outputJsAlertCloseWindow(response, "下载出错！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				outStream.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 重交资料<br>
	 * @return
	 */
	@SysLog("重交资料")
	@RequestMapping(value = "again", method = RequestMethod.GET)
	public void again(HttpServletRequest request) {
		/*GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		gjtStudentInfoService.againSignupInfo(user.getGjtStudentInfo().getStudentId());*/
	}

	/**
	 * 获取学籍资料审核状态、学籍状态、毕业状态<br>
	 * 返回格式：{"type": "100", "content": {...}}<br>
	 * type的描述：100-学籍资料审核中 102-学籍资料审核不通过 3-待注册 0-注册中 2-在籍 8-毕业<br>
	 * @return
	 */
	@RequestMapping(value = "getRollStatus", method = RequestMethod.GET)
	public Map<String, Object> getRollStatus(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> content = new HashMap<String, Object>();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());
		boolean isUndergraduateCourse = gjtStudentInfoService.isUndergraduateCourseById(user.getGjtStudentInfo().getStudentId()); // 是否是本科层次

		if((SignupAuditStateEnum.通过.getValue() + "").equals(studentInfo.getGjtSignup().getAuditState())) {
			resultMap.put("type", studentInfo.getXjzt());
		} else if((SignupAuditStateEnum.不通过.getValue() + "").equals(studentInfo.getGjtSignup().getAuditState())) {
			resultMap.put("type", "102"); // 学籍资料审核不通过
			List<GjtFlowRecord> flowRecordList = gjtFlowService.queryFlowRecordByStudentId(user.getGjtStudentInfo().getStudentId());
			if(flowRecordList.size() > 0){
				GjtFlowRecord gjtFlowRecord = flowRecordList.get(flowRecordList.size()-1);
				content.put("auditDt", gjtFlowRecord.getAuditDt() != null ? DateFormatUtils.ISO_DATE_FORMAT.format(gjtFlowRecord.getAuditDt()) : null);
				content.put("auditOperator", gjtFlowRecord.getAuditOperator());
				content.put("auditContent", gjtFlowRecord.getAuditContent());
			} else {
				content.put("auditDt", "");
				content.put("auditOperator", "");
				content.put("auditContent", "");
			}
		} else {
			resultMap.put("type", "100"); // 学籍资料审核中
		}
		content.put("isUndergraduateCourse", isUndergraduateCourse ? 1 : 0);
		resultMap.put("content", content);
		return resultMap;
	}

	/**
	 * 发送学历证明模板到邮箱
	 * @return
	 */
	@RequestMapping(value = "sendMailXlzmTemplate", method = RequestMethod.POST)
	public void sendMailXlzmTemplate(HttpServletRequest request) throws CommonException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());
		if (StringUtils.isNotBlank(studentInfo.getDzxx())) {
			String fullPath = request.getSession().getServletContext().getRealPath("");
			String templatePath = (fullPath + "\\static\\resources\\xlzmTemplate.rar").replace("\\", "/").replace("//", "/");
			boolean flag = MailUtil.sendAttachMail(new String[] { studentInfo.getDzxx() }, "", new File(templatePath));
			if (flag) {

			} else {
				throw new CommonException(601, "发送到邮箱失败！");
			}
		} else {
			throw new CommonException(600, "无效的电子邮箱");
		}
	}

	/**
	 * 获取学历证明模板下载链接
	 * @return
	 */
	@RequestMapping(value = "getLinkXlzmTemplate", method = RequestMethod.GET)
	public Map<String, Object> getLinkXlzmTemplate(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String templatePath = (getBasePath(request) + "/static/resources/xlzmTemplate.rar");
		resultMap.put("link", templatePath);
		return resultMap;
	}

	/**
	 * 获取学籍审核情况
	 * @return
	 */
	@RequestMapping(value = "getAuditSituation", method = RequestMethod.GET)
	public Map<String, Object> getAuditSituation(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo info = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());
		
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_studentId", user.getGjtStudentInfo().getStudentId());
		Map<String, Object> everyAuditSituation = gjtStudentInfoService.queryStudentSignupInfoEveryAuditState(searchParams, null).get(0);
		
		StudentAuditSituationVo studentVo = new StudentAuditSituationVo();
		studentVo.setXm(info.getXm());
		studentVo.setUserType(info.getUserType());
		studentVo.setXjzt(info.getXjzt());
		studentVo.setStudyCenterName((String) everyAuditSituation.get("ORG_NAME"));
		studentVo.setTeachClassName((String) everyAuditSituation.get("TEACH_CLASS_NAME"));
		studentVo.setHeadTeacherName((String) everyAuditSituation.get("HEADTEACHER"));
		studentVo.setIsEnteringSchool(info.getIsEnteringSchool());
		studentVo.setPerfectSignup(info.getPerfectStatus() == 1 ? 1 : info.getPerfectStatus() >= 5 ? 1 : 0);
		studentVo.setPerfectCertificate(info.getPerfectStatus() == 1 ? 1 : 0);
		studentVo.setOneAuditState((BigDecimal) everyAuditSituation.get("ONE_AUDIT_STATE"));
		studentVo.setTwoAuditState((BigDecimal) everyAuditSituation.get("TWO_AUDIT_STATE"));
		studentVo.setThreeAuditState((BigDecimal) everyAuditSituation.get("THREE_AUDIT_STATE"));
		
		resultMap.put("dicStudentNumberStatusList", cacheService.getCachedDictionary(CacheService.DictionaryKey.STUDENTNUMBERSTATUS)); // 学籍状态字典

		// 以下是页面上一些下拉的值
		resultMap.put("info", studentVo);
		return resultMap;
	}

	/**
	 * 我的学籍记录
	 * @return
	 */
	@RequestMapping(value = "record", method = RequestMethod.GET)
	public Map<String, Object> record(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo info = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());

		List<GjtFlowRecord> flowRecordList = gjtFlowService.queryFlowRecordByStudentId(user.getGjtStudentInfo().getStudentId());
		if(flowRecordList.size() > 0) {
			GjtFlowRecord firstGjtFlowRecord = flowRecordList.get(0);
			resultMap.put("isSubmit", 1);
			resultMap.put("submitDt", firstGjtFlowRecord.getAuditDt() != null ? DateFormatUtils.ISO_DATE_FORMAT.format(firstGjtFlowRecord.getAuditDt()) : null);
			GjtFlowRecord gjtFlowRecord = flowRecordList.get(flowRecordList.size()-1);
			if(gjtFlowRecord.getAuditOperatorRole().intValue() == 4) {
				resultMap.put("lastAuditState", gjtFlowRecord.getAuditState());
				resultMap.put("lastAuditDt", gjtFlowRecord.getAuditDt() != null ? DateFormatUtils.ISO_DATE_FORMAT.format(gjtFlowRecord.getAuditDt()) : null);
			} else {
				resultMap.put("lastAuditState", 0);
			}
		} else {
			if(Constants.BOOLEAN_1.equals(info.getGjtSignup().getAuditState())) {
				resultMap.put("isSubmit", 1);
				resultMap.put("lastAuditState", 1);
			} else {
				resultMap.put("isSubmit", 0);
				resultMap.put("lastAuditState", 0);
			}
		}
		resultMap.put("isRegister", info.getXjzt() == null || "0".equals(info.getXjzt()) || "3".equals(info.getXjzt()) ? 0 : 1);
		return resultMap;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------- //

	/**
	 * 更新20170901之后的学员资料完善状态，资料若已填写完整则确认提交
	 * @param request
	 * @throws CommonException
	 */
	@RequestMapping(value = "updateBatchPerfectStatus", method = RequestMethod.GET)
	public void updateBatchPerfectStatus(HttpServletRequest request, HttpServletResponse response) throws CommonException, IOException {
//		Map<String, Object> searchParams = new HashMap<String, Object>();
		Date createdDt = DateUtil.parseDate("2017-09-01 00:00:00");
		List<String> studentInfos = gjtStudentInfoService.findSidByCreatedDtGreaterThanEqual(createdDt);
		int confirmCount = 0;
		for (String studentId : studentInfos) {
			GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);
			boolean flag = studentInfo.getPerfectStatus() != 1;
			// 更新完善资料状态
			gjtStudentInfoService.perfectSignupAndCertificateInfo(studentId);
			// 未完善的确认提交资料
			if(flag) {
				boolean confirm = gjtFlowService.initAuditSignupInfo(studentId);
				log.error("studentId:{} confirm:{}", studentId, confirm);
				confirmCount++;
			}
		}
		super.outputHtml(response, "student size:" + studentInfos.size() + ", comfirm signup size:" + confirmCount);
	}

	/**
	 * 检查学员姓名、身份证号和证件上面的姓名、身份证号不一致
	 * @param request
	 * @throws CommonException
	 */
	@RequestMapping(value = "validStudentName", method = RequestMethod.GET)
	public void validStudentName(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_perfectStatus", 1);
		searchParams.put("EQ_gjtSignup.signupSfzType", 0);
		searchParams.put("ISNULL_gjtSignup.mail", 0); 		// 用来标识本次是否校验成功，初始化不等于1的全部设置为null
		List gradeList = new ArrayList();
		gradeList.add("5a7eb15a82614101b55de86dad498041");	// 广州 2017春
		gradeList.add("e8748bbe49c7442584f7a2c8347aef41");	// 广州 2017秋
		gradeList.add("98b4d0bb7599458dbf0f9aa147c67f1d");	// 广州 2018春
		gradeList.add("a585553fb34742f99f3cd32481062805");	// 北京 2017春
		gradeList.add("54A2F7783B3E1378E0530881500AFD38");	// 北京 2017秋
		gradeList.add("41fa99135ce34a8a97e24d1595d1c496");	// 北京 2018春
		searchParams.put("IN_nj", gradeList);
//		List sfzhList = new ArrayList();
//		sfzhList.add(	"441881198511155620"	);
//		searchParams.put("IN_sfzh", sfzhList);

		String path = request.getSession().getServletContext().getRealPath("")
				+ WebConstants.EXCEL_DOWNLOAD_URL + "student" + File.separator;
		String outFile = gjtStudentInfoService.exportNotValidStudentName(searchParams, path);

		super.downloadFile(request, response, path + outFile);
		FileKit.delFile(path + outFile);
	}

	@Value("#{configProperties['ghpxeeSyncServer']}")
	private String ghpxeeSyncServer;

	/**
	 * 退学休学的学员同步到学付易
	 * @param request
	 * @throws CommonException
	 */
	@RequestMapping(value = "syncStudentToXuefuyi", method = RequestMethod.GET)
	public void syncStudentToXuefuyi(HttpServletRequest request, HttpServletResponse response) throws CommonException, IOException {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		List<String> xjztList = new ArrayList<String>();
		xjztList.add("4");
		xjztList.add("5");
		searchParams.put("IN_xjzt", xjztList);
		searchParams.put("EQ_gjtSignup.isDeleted", Constants.BOOLEAN_NO);
		searchParams.put("ISNOTNULL_gjtSignup.orderSn", Constants.BOOLEAN_1);
		List<GjtStudentInfo> studentInfos = gjtStudentInfoService.queryBy(searchParams, null);

		for (GjtStudentInfo info : studentInfos) {
			int suspension = "4".equals(info.getXjzt()) ? 1 : "5".equals(info.getXjzt()) ? 2 : 0;
			String admissionsOrderSn = info.getGjtSignup().getOrderSn();
			Map params = new HashMap();
			params.put("suspension", suspension);
			params.put("admissionsOrderSn", admissionsOrderSn);

			String url = ghpxeeSyncServer + "/xuefuyi/updateSuspension";
			String result = HttpClientUtils.doHttpGet(url, params, 60 * 1000, "GBK");
			log.error("studentId:{} params:{} result:{}", info.getStudentId(), params, result);
		}
		super.outputHtml(response, "student size:" + studentInfos.size());
	}
	
}
