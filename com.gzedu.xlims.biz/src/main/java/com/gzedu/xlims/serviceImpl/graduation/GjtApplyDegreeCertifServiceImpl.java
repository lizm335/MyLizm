/**
 * @Package com.gzedu.xlims.serviceImpl 
 * @Project com.gzedu.xlims.biz
 * @File TestServiceImpl.java
 * @Date:2016年4月18日下午5:11:41
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.serviceImpl.graduation;

import com.gzedu.xlims.common.*;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.graduation.GjtApplyDegreeCertifDao;
import com.gzedu.xlims.dao.graduation.GjtCertificateRecordDao;
import com.gzedu.xlims.dao.graduation.GjtGraApplyFlowRecordDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.pojo.graduation.*;
import com.gzedu.xlims.service.graduation.GjtApplyDegreeCertifService;
import com.gzedu.xlims.service.graduation.GjtCertificateRecordService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * 
 */
@Service
public class GjtApplyDegreeCertifServiceImpl extends BaseServiceImpl<GjtApplyDegreeCertif> implements GjtApplyDegreeCertifService {

	private static final Log log = LogFactory.getLog(GjtApplyDegreeCertifServiceImpl.class);

	@Autowired
	GjtApplyDegreeCertifDao gjtApplyDegreeCertifDao;

	@Autowired
	GjtGraApplyFlowRecordDao gjtGraApplyFlowRecordDao;

	@Autowired
	GjtOrgDao gjtOrgDao;

	@Override
	protected BaseDao<GjtApplyDegreeCertif, String> getBaseDao() {
		return gjtApplyDegreeCertifDao;
	}


	@Override
	public GjtApplyDegreeCertif queryApplyDegreeCertifByStudentIdAndPlanId(String studentId, String planId) {
		return gjtApplyDegreeCertifDao.queryApplyDegreeCertifByStudentIdAndPlanId(studentId, planId);
	}

	@Override
	public Map<String, Long> countGroupbyAuditState(Map<String, Object> searchParams) {

		Map<String, Long> result = new HashMap<String, Long>();
		Map<String, Object> searchParamsNew = new HashMap<String, Object>();
		searchParamsNew.putAll(searchParams);

		// int status = NumberUtils.toInt((String)
		// searchParamsNew.remove("EQ_auditState"), -1);
		result.put("", gjtApplyDegreeCertifDao.count(getSpecification(searchParamsNew)));

		searchParamsNew.put("EQ_auditState", 0);
		result.put("0", gjtApplyDegreeCertifDao.count(getSpecification(searchParamsNew)));
		searchParamsNew.put("EQ_auditState", 1);
		result.put("1", gjtApplyDegreeCertifDao.count(getSpecification(searchParamsNew)));
		searchParamsNew.put("EQ_auditState", 6);
		result.put("6", gjtApplyDegreeCertifDao.count(getSpecification(searchParamsNew)));
		searchParamsNew.put("EQ_auditState", 11);
		result.put("11", gjtApplyDegreeCertifDao.count(getSpecification(searchParamsNew)));
		searchParamsNew.put("EQ_auditState", 12);
		result.put("12", gjtApplyDegreeCertifDao.count(getSpecification(searchParamsNew)));
		return result;
	}

	/**
	 * 公共条件拼接
	 *
	 * @param searchParams
	 * @return
	 */
	private Criteria<GjtApplyDegreeCertif> getSpecification(Map<String, Object> searchParams) {
		Criteria<GjtApplyDegreeCertif> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfo.isDeleted", Constants.BOOLEAN_NO, true));
		Map<String, Object> searchParamsNew = new HashMap<String, Object>();
		searchParamsNew.putAll(searchParams);
		int status = NumberUtils.toInt(searchParamsNew.remove("EQ_auditState") + "", -1);
		if (status > -1) {
			spec.add(Restrictions.eq("auditState", status, true));
		}

		// 机构ID
		String orgId = (String) searchParamsNew.remove("EQ_orgId");
		if (StringUtils.isNotBlank(orgId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
			spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		}
		spec.addAll(Restrictions.parse(searchParamsNew));
		return spec;
	}

	/**
	 * 下载学位申请资料
	 * @param searchParams
	 * @param realPath
	 * @return
	 * @throws Exception
	 */
	@Override
	public String downloadReqFile(Map<String, Object> searchParams, String realPath) throws Exception {
		Criteria<GjtApplyDegreeCertif> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfo.isDeleted", Constants.BOOLEAN_NO, true));
		// 机构ID
		String orgId = (String) searchParams.remove("EQ_orgId");
		if (StringUtils.isNotBlank(orgId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
			spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		}
		spec.addAll(Restrictions.parse(searchParams));

		List<GjtApplyDegreeCertif> applyDegrees = gjtApplyDegreeCertifDao.findAll(spec);

		Calendar cal = Calendar.getInstance();
		String folderName = "学位申请材料_" + cal.getTimeInMillis();
		String outputPath = realPath + WebConstants.EXCEL_DOWNLOAD_URL + "degreeApply" + File.separator + DateFormatUtils.ISO_DATE_FORMAT.format(cal)
				+ File.separator;
		String outputFolderPath = outputPath + File.separator + folderName;
		File folder = new File(outputFolderPath);
		if (!folder.exists()) {
			folder.mkdirs();
			log.error("创建文件 " + outputFolderPath);
		}
		for (GjtApplyDegreeCertif apply : applyDegrees) {

			String xm = apply.getGjtStudentInfo().getXm().replace("*", "");
			String sfz = apply.getGjtStudentInfo().getSfzh().replace("*", "");
			String certificateFolderPath = outputFolderPath + File.separator + sfz + "（" + xm + "）";
			String suffix;
			String fileUrl = apply.getIdcardFrontUrl();
			if (StringUtils.isNotBlank(fileUrl)) {
				suffix = fileUrl.substring(fileUrl.lastIndexOf("."));
				FileUtils.copyURLToFile(new URL(fileUrl), new File(certificateFolderPath + File.separator + "身份证正面." + suffix));
			}
			fileUrl = apply.getIdcardBackUrl();
			if (StringUtils.isNotBlank(fileUrl)) {
				suffix = fileUrl.substring(fileUrl.lastIndexOf("."));
				FileUtils.copyURLToFile(new URL(fileUrl), new File(certificateFolderPath + File.separator + "身份证背面." + suffix));
			}

			fileUrl = apply.getThesisUrl();
			if (StringUtils.isNotBlank(fileUrl)) {
				suffix = fileUrl.substring(fileUrl.lastIndexOf("."));
				FileUtils.copyURLToFile(new URL(fileUrl), new File(certificateFolderPath + File.separator + "毕业论文." + suffix));
			}
			fileUrl = apply.getPreliminaryReportUrl();
			if (StringUtils.isNotBlank(fileUrl)) {
				suffix = fileUrl.substring(fileUrl.lastIndexOf("."));
				FileUtils.copyURLToFile(new URL(fileUrl), new File(certificateFolderPath + File.separator + "学位论文查重初检报告." + suffix));
			}
		}
		String outputFilePath = outputPath + File.separator + folderName + ".zip";
		ZipFileUtil.zipDir(outputFolderPath, outputFilePath);
		FileKit.delFile(outputFolderPath);
		return outputFilePath;
	}

	@Override
	public void studentApplyDegree(GjtApplyDegreeCertif certif, String userId) {
		certif.setApplyDegree(1);
		gjtApplyDegreeCertifDao.save(certif);

		GjtGraApplyFlowRecord record = new GjtGraApplyFlowRecord();
		record.setFlowRecordId(UUIDUtils.random());
		record.setApplyId(certif.getApplyId());
		record.setAuditState(0);
		record.setAuditDt(new Date());
		record.setAuditOperator(userId);
		record.setAuditOperatorRole(1);
		gjtGraApplyFlowRecordDao.save(record);
	}

	@Override
	public void createRecord(GjtApplyDegreeCertif entity, int auditState, int operatorRole, String auditContent){
		GjtGraApplyFlowRecord record = new GjtGraApplyFlowRecord();
		record.setFlowRecordId(UUIDUtils.random());
		record.setAuditContent(auditContent);
		record.setApplyId(entity.getApplyId());
		record.setAuditState(auditState);
		record.setAuditDt(new Date());
		record.setAuditOperator(entity.getCreatedBy());
		record.setAuditOperatorRole(operatorRole);
		gjtGraApplyFlowRecordDao.save(record);
	}

}
