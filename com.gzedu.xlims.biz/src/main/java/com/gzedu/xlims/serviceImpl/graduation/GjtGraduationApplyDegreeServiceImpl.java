package com.gzedu.xlims.serviceImpl.graduation;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gzedu.xlims.dao.graduation.GjtGraduationDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.ZipFileUtil;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.graduation.GjtGraApplyFlowRecordDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationApplyDegreeDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.pojo.graduation.GjtGraApplyFlowRecord;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyDegree;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyDegreeService;
import com.gzedu.xlims.service.graduation.GjtGraduationRegisterService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

@Service
public class GjtGraduationApplyDegreeServiceImpl extends BaseServiceImpl<GjtGraduationApplyDegree> implements GjtGraduationApplyDegreeService {

	private static final Log log = LogFactory.getLog(GjtGraduationApplyDegreeServiceImpl.class);

	@Autowired
	private GjtGraduationApplyDegreeDao gjtGraduationApplyDegreeDao;

	@Autowired
	public GjtOrgDao gjtOrgDao;

	@Autowired
	private GjtGraduationRegisterService gjtGraduationRegisterService;

	@Autowired
	private GjtGraApplyFlowRecordDao gjtGraApplyFlowRecordDao;

	@Autowired
	private GjtGraduationDao gjtGraduationDao;

	@Autowired
	private CommonDao commonDao;

	@Override
	protected BaseDao<GjtGraduationApplyDegree, String> getBaseDao() {
		return this.gjtGraduationApplyDegreeDao;
	}

	@Override
	public Page<GjtGraduationApplyDegree> queryGraduationApplyCardByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(), new Sort(Sort.Direction.DESC, "createdDt"));
		}
		Map<String, Object> searchParamsNew = new HashMap<String, Object>();
		searchParamsNew.putAll(searchParams);
		Criteria<GjtGraduationApplyDegree> spec = new Criteria();
		// 设置连接方式，如果是内连接可省略掉
		// spec.setJoinType("gjtStudyCenter", JoinType.LEFT);
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfo.isDeleted", Constants.BOOLEAN_NO, true));
		// 机构ID
		String orgId = (String) searchParamsNew.remove("EQ_orgId");
		if (StringUtils.isNotBlank(orgId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
			spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		}
		spec.addAll(Restrictions.parse(searchParamsNew));

		Page<GjtGraduationApplyDegree> pageInfos = gjtGraduationApplyDegreeDao.findAll(spec, pageRequest);
		return pageInfos;
	}

	@Override
	public long countGraduationApplyCardByPage(Map<String, Object> searchParams) {
		Map<String, Object> searchParamsNew = new HashMap<String, Object>();
		searchParamsNew.putAll(searchParams);
		Criteria<GjtGraduationApplyDegree> spec = new Criteria();
		// 设置连接方式，如果是内连接可省略掉
		// spec.setJoinType("gjtStudyCenter", JoinType.LEFT);
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfo.isDeleted", Constants.BOOLEAN_NO, true));
		// 机构ID
		String orgId = (String) searchParamsNew.remove("EQ_orgId");
		if (StringUtils.isNotBlank(orgId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
			spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		}
		spec.addAll(Restrictions.parse(searchParamsNew));

		long count = gjtGraduationApplyDegreeDao.count(spec);
		return count;
	}

	@Override
	public List<Map<String, Object>> queryAchievementByStudentId(String studentId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  select tsd.name  \"modelName\",");
		sql.append("  tsd.id    \"modelId\",");
		sql.append("  gsml.totalscore  \"totalscore\",");
		sql.append("  gsml.crtvu_score \"crtvuScore\",");
		sql.append("  NVL(grr.get_credits,0)  \"getCredits\",");
		sql.append("  gc.kcmc          \"courseName\",");
		sql.append("  gc.kch           \"courseCode\",");
		sql.append("  gtp.kcsx         \"courseNature\",");
		sql.append("  gtp.course_type  \"courseType\",");
		sql.append("  gtp.xf           \"courseScore\",");
		sql.append("  gtp.kkxq         \"term\",");
		sql.append("  gtp.ksdw         \"examUnit\",");
		sql.append("  NVL(grr.exam_score2,0)  \"examScore\"");
		sql.append("  ");
		sql.append("  From gjt_student_info      gsi,");
		sql.append("  gjt_teach_plan             gtp,");
		sql.append("  gjt_course                 gc,");
		sql.append("  gjt_rec_result             grr,");
		sql.append("  tbl_sys_data               tsd,");
		sql.append("  gjt_specialty_module_limit gsml");
		sql.append("  where gsi.grade_specialty_id = gtp.grade_specialty_id");
		sql.append("  and gtp.course_id = gc.course_id");
		sql.append("  and gtp.teach_plan_id = grr.teach_plan_id");
		sql.append("  and gtp.kclbm = tsd.code");
		sql.append("  and tsd.id = gsml.module_id");
		sql.append("  and gsi.student_id = grr.student_id");
		sql.append("  and gsml.specialty_id=gtp.kkzy");
		sql.append("  and gtp.is_deleted = 'N'");
		sql.append("  and grr.is_deleted = 'N'");
		sql.append("  and tsd.type_code = 'CourseType'");
		sql.append("  and gsi.student_id = :studentId");
		sql.append("  order by tsd.code ");
		params.put("studentId", studentId);
		return commonDao.queryForMapList(sql.toString(), params);

	}

	@Override
	public GjtGraApplyFlowRecord queryFlowRecordById(String recordId) {
		return gjtGraApplyFlowRecordDao.findOne(recordId);
	}

	@Override
	public GjtGraApplyFlowRecord queryFlowRecordByApplyId(String applyId, int roleId) {
		PageRequest pageRequest = new PageRequest(1, 1, new Sort(Sort.Direction.DESC, "createdDt", "auditOperatorRole"));
		Criteria<GjtGraApplyFlowRecord> spec = new Criteria<GjtGraApplyFlowRecord>();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("applyId", applyId, true));
		spec.add(Restrictions.eq("auditOperatorRole", roleId, true));
		Page<GjtGraApplyFlowRecord> page = gjtGraApplyFlowRecordDao.findAll(spec, pageRequest);
		System.out.println(page.getContent().size());
		if (CollectionUtils.isNotEmpty(page.getContent())) {
			return page.getContent().get(0);
		}
		return null;
	}

	@Override
	public GjtGraApplyFlowRecord saveFlowRecord(GjtGraApplyFlowRecord record) {
		return gjtGraApplyFlowRecordDao.save(record);
	}

	@Override
	public GjtGraduationApplyDegree queryDegreeApplyByStudentId(String studentId) {
		Criteria<GjtGraduationApplyDegree> spec = new Criteria<GjtGraduationApplyDegree>();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("studentId", studentId, true));
		return gjtGraduationApplyDegreeDao.findOne(spec);
	}

	@Override
	public String downloadReqFile(Map<String, Object> searchParams, String realPath) throws Exception {
		Criteria<GjtGraduationApplyDegree> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfo.isDeleted", Constants.BOOLEAN_NO, true));
		// 机构ID
		String orgId = (String) searchParams.remove("EQ_orgId");
		if (StringUtils.isNotBlank(orgId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
			spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		}
		spec.addAll(Restrictions.parse(searchParams));

		List<GjtGraduationApplyDegree> applyDegrees = gjtGraduationApplyDegreeDao.findAll(spec);

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
		for (GjtGraduationApplyDegree apply : applyDegrees) {

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
			fileUrl = apply.getEnglishCertificateUrl();
			if (StringUtils.isNotBlank(fileUrl)) {
				suffix = fileUrl.substring(fileUrl.lastIndexOf("."));
				FileUtils.copyURLToFile(new URL(fileUrl), new File(certificateFolderPath + File.separator + "学位英语证书." + suffix));
			}
			fileUrl = apply.getSignature();
			if (StringUtils.isNotBlank(fileUrl)) {
				suffix = fileUrl.substring(fileUrl.lastIndexOf("."));
				FileUtils.copyURLToFile(new URL(fileUrl), new File(certificateFolderPath + File.separator + "签名." + suffix));
			}
			fileUrl = apply.getPaperUrl();
			if (StringUtils.isNotBlank(fileUrl)) {
				suffix = fileUrl.substring(fileUrl.lastIndexOf("."));
				FileUtils.copyURLToFile(new URL(fileUrl), new File(certificateFolderPath + File.separator + "毕业论文." + suffix));
			}
			fileUrl = apply.getPaperCheckUrl();
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
	public Map<String, Object> countStudentApplyDegreeSituation(Map<String, Object> searchParams) {
		return gjtGraduationDao.countStudentApplyDegreeSituation(searchParams);
	}

}
