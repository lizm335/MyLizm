package com.gzedu.xlims.dao.graduation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApply;

@Repository
public class GjtGraduationDao {

	@Autowired
	private GjtOrgDao gjtOrgDao;
	
	@Autowired
	private CommonDao commonDao;
	
	public Page<?> queryGraduationApply(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb
			.append(" from ")
			.append("     GjtGraduationApply a1 ")
			.append(" left join ")
			.append("     GjtGraduationApply a2 ")
			.append(" with ")
			.append("     a2.gjtStudentInfo = a1.gjtStudentInfo and a2.isDeleted = 'N' and a2.applyType = 2 ")
			.append(" where")
			.append("     a1.isDeleted = 'N' and a1.applyType = 1 ")
			.append(" union ")
			.append(" from ")
			.append("     GjtGraduationApply a2 ")
			.append(" left join ")
			.append("     GjtGraduationApply a1 ")
			.append(" with ")
			.append("     a1.gjtStudentInfo = a2.gjtStudentInfo and a1.isDeleted = 'N' and a1.applyType = 1 ")
			.append(" where")
			.append("     a2.isDeleted = 'N' and a2.applyType = 2 ");
		
		return commonDao.queryForPage(sb.toString(), map, pageRequst);
	}
	
	/**
	 * 查询我的指导列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<GjtGraduationApply> queryMyGuideList(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" from ")
			.append("      GjtGraduationApply a")
			.append(" where ")
			.append("      a.isDeleted = 'N' ");

		Object applyType = searchParams.get("EQ_applyType");
		if (applyType != null) {
			sb.append(" and a.applyType = :applyType ");
			map.put("applyType", applyType);
		}
		
		Object guideTeacher = searchParams.get("EQ_guideTeacher");
		if (guideTeacher != null && StringUtils.isNotBlank((String)guideTeacher)) {
			sb.append(" and a.guideTeacher = :guideTeacher ");
			map.put("guideTeacher", guideTeacher);
		}
		
		Object status = searchParams.get("IN_status");
		if (status != null) {
			sb.append(" and a.status in (:status) ");
			map.put("status", status);
		}
		
		Object stundetCode = searchParams.get("LIKE_gjtStudentInfo.xh");
		if (stundetCode != null && StringUtils.isNotBlank((String)stundetCode)) {
			sb.append(" and a.gjtStudentInfo.xh like :stundetCode ");
			map.put("stundetCode", "%" + stundetCode + "%");
		}
		
		Object stundetName = searchParams.get("LIKE_gjtStudentInfo.xm");
		if (stundetName != null && StringUtils.isNotBlank((String)stundetName)) {
			sb.append(" and a.gjtStudentInfo.xm like :stundetName ");
			map.put("stundetName", "%" + stundetName + "%");
		}
		
		Object batchId = searchParams.get("EQ_gjtGraduationBatch.batchId");
		if (batchId != null && StringUtils.isNotBlank((String)batchId)) {
			sb.append(" and a.gjtGraduationBatch.batchId = :batchId ");
			map.put("batchId", batchId);
		}
		
		Object specialtyId = searchParams.get("EQ_gjtStudentInfo.gjtSpecialty.specialtyId");
		if (specialtyId != null && StringUtils.isNotBlank((String)specialtyId)) {
			sb.append(" and a.gjtStudentInfo.gjtSpecialty.specialtyId = :specialtyId ");
			map.put("specialtyId", specialtyId);
		}
		
		Object isGuide = searchParams.get("EQ_isGuide");
		if (isGuide == null || StringUtils.isBlank((String)isGuide)) {
			sb.append(" and exists (")
				.append("            select r1 from GjtGraduationGuideRecord r1")
				.append("            where r1.isDeleted = 'N' and r1.gjtGraduationBatch = a.gjtGraduationBatch")
				.append("            and r1.recordType = a.applyType and r1.gjtStudentInfo = a.gjtStudentInfo")
				.append("            and r1.isStudent = 1 and r1.createdDt = (")
				.append("                select max(r2.createdDt) from GjtGraduationGuideRecord r2")
				.append("                where r2.isDeleted = 'N' and r2.gjtGraduationBatch = a.gjtGraduationBatch")
				.append("                and r2.recordType = a.applyType and r2.gjtStudentInfo = a.gjtStudentInfo")
				.append("            )")
				.append("          )");
		} else if ("1".equals((String)isGuide)) {
			sb.append(" and exists (")
				.append("            select r1 from GjtGraduationGuideRecord r1")
				.append("            where r1.isDeleted = 'N' and r1.gjtGraduationBatch = a.gjtGraduationBatch")
				.append("            and r1.recordType = a.applyType and r1.gjtStudentInfo = a.gjtStudentInfo")
				.append("            and r1.isStudent = 0 and r1.createdDt = (")
				.append("                select max(r2.createdDt) from GjtGraduationGuideRecord r2")
				.append("                where r2.isDeleted = 'N' and r2.gjtGraduationBatch = a.gjtGraduationBatch")
				.append("                and r2.recordType = a.applyType and r2.gjtStudentInfo = a.gjtStudentInfo")
				.append("            )")
				.append("          )");
		}
		
		sb.append(" order by a.updatedDt DESC");
		
		return (Page<GjtGraduationApply>)commonDao.queryForPage(sb.toString(), map, pageRequst);
	}

	/**
	 * 学员毕业情况
	 * @param searchParams
	 * @return
	 */
	public Map<String, Object> countStudentApplyDegreeSituation(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("  select  count(distinct case when t.xjzt='8' then t.student_id else null end) CERTIF_COUNT,");
		sql.append("          count(distinct b.student_id) APPLY_CERTIF_COUNT");
		sql.append("  from gjt_student_info t");
		sql.append("  left join GJT_GRADUATION_APPLY_CERTIF b on b.student_id=t.student_id");
		sql.append("  WHERE t.IS_DELETED = 'N'");

		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND t.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND t.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}
		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND t.USER_TYPE=:userType");
			params.put("userType", userType);
		}

		// 院校ID
		String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(ObjectUtils.toString(searchParams.get("XXZX_ID")));
			sql.append(" AND t.XXZX_ID= :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		} else if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND t.XXZX_ID IN (:orgChilds)");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND t.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append(" AND t.NJ = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		List<Map<String, Object>> list = commonDao.queryForStringObjectMapListNative(sql.toString(), params);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * 学员学位情况
	 * @param searchParams
	 * @return
	 */
    public Map<String,Object> countStudentApplyCertifSituation(Map<String, Object> searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append("  select  count(distinct case when b.IS_RECEIVE=1 then t.student_id else null end) DEGREE_COUNT,");
		sql.append("          count(distinct b.student_id) APPLY_DEGREE_COUNT");
		sql.append("  from gjt_student_info t");
		sql.append("  left join GJT_GRADUATION_APPLY_DEGREE b on b.student_id=t.student_id");
		sql.append("  WHERE t.IS_DELETED = 'N'");

		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND t.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND t.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}
		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND t.USER_TYPE=:userType");
			params.put("userType", userType);
		}

		// 院校ID
		String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(ObjectUtils.toString(searchParams.get("XXZX_ID")));
			sql.append(" AND t.XXZX_ID= :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		} else if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND t.XXZX_ID IN (:orgChilds)");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND t.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append(" AND t.NJ = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		List<Map<String, Object>> list = commonDao.queryForStringObjectMapListNative(sql.toString(), params);
		return list != null && list.size() > 0 ? list.get(0) : null;
    }
}
