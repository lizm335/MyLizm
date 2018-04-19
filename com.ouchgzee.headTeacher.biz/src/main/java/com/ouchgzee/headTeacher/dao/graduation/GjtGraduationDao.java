package com.ouchgzee.headTeacher.dao.graduation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.ouchgzee.headTeacher.dao.comm.CommonDao;
import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationApply;

@Deprecated @Repository("bzrGjtGraduationDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtGraduationDao {
	
	@Autowired
	private CommonDao commonDao;
	
	public Page<?> queryGraduationApply(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb
			.append(" from ")
			.append("     BzrGjtGraduationApply a1 ")
			.append(" left join ")
			.append("     BzrGjtGraduationApply a2 ")
			.append(" with ")
			.append("     a2.gjtStudentInfo = a1.gjtStudentInfo and a2.isDeleted = 'N' and a2.applyType = 2 ")
			.append(" where")
			.append("     a1.isDeleted = 'N' and a1.applyType = 1 ")
			.append(" union ")
			.append(" from ")
			.append("     BzrGjtGraduationApply a2 ")
			.append(" left join ")
			.append("     BzrGjtGraduationApply a1 ")
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
	public Page<BzrGjtGraduationApply> queryMyGuideList(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" from ")
			.append("      BzrGjtGraduationApply a")
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
				.append("            select r1 from BzrGjtGraduationGuideRecord r1")
				.append("            where r1.isDeleted = 'N' and r1.gjtGraduationBatch = a.gjtGraduationBatch")
				.append("            and r1.recordType = a.applyType and r1.gjtStudentInfo = a.gjtStudentInfo")
				.append("            and r1.isStudent = 1 and r1.createdDt = (")
				.append("                select max(r2.createdDt) from BzrGjtGraduationGuideRecord r2")
				.append("                where r2.isDeleted = 'N' and r2.gjtGraduationBatch = a.gjtGraduationBatch")
				.append("                and r2.recordType = a.applyType and r2.gjtStudentInfo = a.gjtStudentInfo")
				.append("            )")
				.append("          )");
		} else if ("1".equals((String)isGuide)) {
			sb.append(" and exists (")
				.append("            select r1 from BzrGjtGraduationGuideRecord r1")
				.append("            where r1.isDeleted = 'N' and r1.gjtGraduationBatch = a.gjtGraduationBatch")
				.append("            and r1.recordType = a.applyType and r1.gjtStudentInfo = a.gjtStudentInfo")
				.append("            and r1.isStudent = 0 and r1.createdDt = (")
				.append("                select max(r2.createdDt) from BzrGjtGraduationGuideRecord r2")
				.append("                where r2.isDeleted = 'N' and r2.gjtGraduationBatch = a.gjtGraduationBatch")
				.append("                and r2.recordType = a.applyType and r2.gjtStudentInfo = a.gjtStudentInfo")
				.append("            )")
				.append("          )");
		}
		
		sb.append(" order by a.updatedDt DESC");
		
		return (Page<BzrGjtGraduationApply>)commonDao.queryForPage(sb.toString(), map, pageRequst);
	}

}
