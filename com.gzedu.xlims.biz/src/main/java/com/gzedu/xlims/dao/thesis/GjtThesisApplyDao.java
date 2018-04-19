package com.gzedu.xlims.dao.thesis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.thesis.repository.GjtThesisApplyRepository;
import com.gzedu.xlims.pojo.thesis.GjtThesisApply;

@Repository
public class GjtThesisApplyDao {

	@Autowired
	private GjtThesisApplyRepository gjtThesisApplyRepository;

	@Autowired
	private CommonDao commonDao;

	public Page<GjtThesisApply> findAll(Specification<GjtThesisApply> spec, PageRequest pageRequst) {
		return gjtThesisApplyRepository.findAll(spec, pageRequst);
	}

	public long count(Specification<GjtThesisApply> spec) {
		return gjtThesisApplyRepository.count(spec);
	}

	public GjtThesisApply save(GjtThesisApply entity) {
		return gjtThesisApplyRepository.save(entity);
	}

	public List<GjtThesisApply> save(List<GjtThesisApply> entities) {
		return gjtThesisApplyRepository.save(entities);
	}

	public GjtThesisApply findOne(String id) {
		return gjtThesisApplyRepository.findOne(id);
	}

	public GjtThesisApply findByThesisPlanIdAndStudentIdAndIsDeleted(String thesisPlanId, String studentId,
			String isDeleted) {
		return gjtThesisApplyRepository.findByThesisPlanIdAndStudentIdAndIsDeleted(thesisPlanId, studentId, isDeleted);
	}

	public List<GjtThesisApply> findByThesisPlanIdAndSpecialtyBaseId(String thesisPlanId, String specialtyBaseId) {
		return gjtThesisApplyRepository.findByThesisPlanIdAndSpecialtyBaseId(thesisPlanId, specialtyBaseId);
	}

	public List<GjtThesisApply> findIsApplyByStudentId(String studentId, String gradeId) {
		return gjtThesisApplyRepository.findIsApplyByStudentId(studentId, gradeId);
	}

	public GjtThesisApply findByIsDeletedAndStudentIdAndStatus(String isDeleted, String studentId, int status) {
		return gjtThesisApplyRepository.findByIsDeletedAndStudentIdAndStatus(isDeleted, studentId, status);
	}

	/**
	 * 查询是否可以申请
	 * 
	 * @param orgId
	 * @param gradeId
	 * @param gradeSpecialtyId
	 * @param studentId
	 * @return
	 */
	public List<Map<String, Object>> getCanApply(String orgId, String gradeId, String gradeSpecialtyId,
			String studentId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("		 select ").append("        	gtp.teach_plan_id, ")
				.append("        	grr.exam_score1 as \"score\" ").append("    from ")
				.append("        	gjt_course gc, ").append("        	view_teach_plan gtp ").append("    left join ")
				.append("        	gjt_rec_result grr ").append("    on ").append("        	grr.is_deleted = 'N' ")
				.append("        	and grr.teach_plan_id = gtp.teach_plan_id ")
				.append("        	and grr.student_id = :studentId ").append("    where ")
				.append("        	gtp.is_deleted = 'N' ")
				.append("        	and gtp.grade_specialty_id = :gradeSpecialtyId ")
				.append("        	and gtp.actual_grade_id in ").append("        		( ")
				.append("        		 select ").append("        		 		gg1.grade_id ")
				.append("        		 from ").append("        		 		gjt_grade gg1 ")
				.append("        		 where ").append("        		 		gg1.is_deleted = 'N' ")
				.append("        		 		and gg1.xx_id = :orgId ")
				.append("        		 		and gg1.start_date <= (select start_date from gjt_grade where grade_id = :gradeId) ")
				.append("        		) ").append("        	and gtp.course_id = gc.course_id ")
				.append("        	and gc.course_category = 2 ");

		map.put("orgId", orgId);
		map.put("gradeId", gradeId);
		map.put("gradeSpecialtyId", gradeSpecialtyId);
		map.put("studentId", studentId);

		return commonDao.queryForStringObjectMapListNative(sb.toString(), map);
	}

	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer();
		sb.append("		 select ").append("        	gtp.teach_plan_id, ")
				.append("        	grr.exam_score1 as \"score\" ").append("    from ")
				.append("        	gjt_course gc, ").append("        	view_teach_plan gtp ").append("    left join ")
				.append("        	gjt_rec_result grr ").append("    on ").append("        	grr.is_deleted = 'N' ")
				.append("        	and grr.teach_plan_id = gtp.teach_plan_id ")
				.append("        	and grr.student_id = :studentId ").append("    where ")
				.append("        	gtp.is_deleted = 'N' ")
				.append("        	and gtp.grade_specialty_id = :gradeSpecialtyId ")
				.append("        	and gtp.actual_grade_id in ").append("        		( ")
				.append("        		 select ").append("        		 		gg1.grade_id ")
				.append("        		 from ").append("        		 		gjt_grade gg1 ")
				.append("        		 where ").append("        		 		gg1.is_deleted = 'N' ")
				.append("        		 		and gg1.xx_id = :orgId ")
				.append("        		 		and gg1.start_date <= (select start_date from gjt_grade where grade_id = :gradeId) ")
				.append("        		) ").append("        	and gtp.course_id = gc.course_id ")
				.append("        	and gc.course_category = 2 ");
		System.out.println(sb);
	}

	/**
	 * 查询老师指导记录
	 * 
	 * @param orgId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> findTeacherGuideList(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("		 select ").append("        	t1.guide_teacher as \"teacherId\", ")
				.append("        	t1.grade_specialty_id as \"gradeSpecialtyId\", ")
				.append("        	t1.thesis_plan_id as \"thesisPlanId\", ")
				.append("        	t1.thesis_plan_code as \"thesisPlanCode\", ")
				.append("        	t1.thesis_plan_name as \"thesisPlanName\", ")
				.append("        	t1.xm as \"teacherName\", ").append("        	t1.zp as \"teacherZp\", ")
				.append("        	t1.grade_name as \"gradeName\", ").append("        	t1.pycc as \"trainingLevel\", ")
				.append("        	t1.zymc as \"specialtyName\", ").append("        	t1.rule_code as \"ruleCode\", ")
				.append("        	t1.num as \"all\", ").append("        	( ").append("        		select ")
				.append("        			count(1) ").append("        		from ")
				.append("        			gjt_thesis_apply gta1, ").append("        			gjt_student_info gsi1 ")
				.append("        		where ").append("        			gta1.is_deleted = 'N' ")
				.append("        			and gta1.status = 2 ")
				.append("        			and gta1.thesis_plan_id = t1.thesis_plan_id ")
				.append("        			and gta1.guide_teacher = t1.guide_teacher ")
				.append("        			and gsi1.student_id = gta1.student_id ")
				.append("        			and gsi1.grade_specialty_id = t1.grade_specialty_id ")
				.append("        	) as \"submitPropose\", ").append("        	( ").append("        		select ")
				.append("        			count(1) ").append("        		from ")
				.append("        			gjt_thesis_apply gta2, ").append("        			gjt_student_info gsi2 ")
				.append("        		where ").append("        			gta2.is_deleted = 'N' ")
				.append("        			and gta2.status = 4 ")
				.append("        			and gta2.thesis_plan_id = t1.thesis_plan_id ")
				.append("        			and gta2.guide_teacher = t1.guide_teacher ")
				.append("        			and gsi2.student_id = gta2.student_id ")
				.append("        			and gsi2.grade_specialty_id = t1.grade_specialty_id ")
				.append("        	) as \"submitThesis\", ").append("        	( ").append("        		select ")
				.append("        			count(1) ").append("        		from ")
				.append("        			gjt_thesis_apply gta3, ").append("        			gjt_student_info gsi3 ")
				.append("        		where ").append("        			gta3.is_deleted = 'N' ")
				.append("        			and gta3.status = 5 ")
				.append("        			and gta3.thesis_plan_id = t1.thesis_plan_id ")
				.append("        			and gta3.guide_teacher = t1.guide_teacher ")
				.append("        			and gsi3.student_id = gta3.student_id ")
				.append("        			and gsi3.grade_specialty_id = t1.grade_specialty_id ")
				.append("        	) as \"teacherConfirm\", ").append("        	( ")
				.append("        		select ").append("        			count(1) ").append("        		from ")
				.append("        			gjt_thesis_apply gta4, ").append("        			gjt_student_info gsi4 ")
				.append("        		where ").append("        			gta4.is_deleted = 'N' ")
				.append("        			and gta4.status = 7 ")
				.append("        			and gta4.thesis_plan_id = t1.thesis_plan_id ")
				.append("        			and gta4.guide_teacher = t1.guide_teacher ")
				.append("        			and gsi4.student_id = gta4.student_id ")
				.append("        			and gsi4.grade_specialty_id = t1.grade_specialty_id ")
				.append("        	) as \"collegeConfirm\", ").append("        	( ")
				.append("        		select ").append("        			count(1) ").append("        		from ")
				.append("        			gjt_thesis_apply gta5, ").append("        			gjt_student_info gsi5 ")
				.append("        		where ").append("        			gta5.is_deleted = 'N' ")
				.append("        			and gta5.status = 6 ")
				.append("        			and gta5.thesis_plan_id = t1.thesis_plan_id ")
				.append("        			and gta5.guide_teacher = t1.guide_teacher ")
				.append("        			and gsi5.student_id = gta5.student_id ")
				.append("        			and gsi5.grade_specialty_id = t1.grade_specialty_id ")
				.append("        	) as \"reviewFailed\" ").append("    from ").append("        	( ")
				.append("        		select ").append("        			gta.guide_teacher, ")
				.append("        			gsi.grade_specialty_id, ").append("        			gtp.thesis_plan_id, ")
				.append("        			gtp.thesis_plan_code, ").append("        			gtp.thesis_plan_name, ")
				.append("        			gei.xm, ").append("        			gei.zp, ")
				.append("        			gg.grade_name, ")
				.append("        			(select tsd.name from tbl_sys_data tsd where tsd.is_deleted = 'N' and tsd.type_code = 'TrainingLevel' and tsd.code = gs.pycc) pycc, ")
				.append("        			gs.zymc, ").append("        			gs.rule_code, ")
				.append("        			count(*) num ").append("        		from ")
				.append("        			gjt_thesis_apply gta, ").append("        			gjt_thesis_plan gtp, ")
				.append("        			gjt_employee_info gei, ").append("        			gjt_student_info gsi, ")
				.append("        			gjt_grade_specialty ggs, ").append("        			gjt_grade gg, ")
				.append("        			gjt_specialty gs ").append("        		where ")
				.append("        			gta.is_deleted = 'N' ")
				.append("        			and gtp.thesis_plan_id = gta.thesis_plan_id ")
				.append("        			and gtp.org_id = :orgId ")
				.append("        			and gei.employee_id = gta.guide_teacher ")
				.append("        			and gsi.student_id = gta.student_id ")
				.append("        			and ggs.id = gsi.grade_specialty_id ")
				.append("        			and gg.grade_id = ggs.grade_id ")
				.append("        			and gs.specialty_id = ggs.specialty_id ");

		Object thesisPlanId = searchParams.get("EQ_thesisPlanId");
		if (thesisPlanId != null && StringUtils.isNotBlank((String) thesisPlanId)) {
			sb.append(" and gta.thesis_plan_id = :thesisPlanId ");
			map.put("thesisPlanId", thesisPlanId);
		}

		Object gradeId = searchParams.get("EQ_gradeId");
		if (gradeId != null && StringUtils.isNotBlank((String) gradeId)) {
			sb.append(" and gsi.nj = :gradeId ");
			map.put("gradeId", gradeId);
		}

		Object pycc = searchParams.get("EQ_pycc");
		if (pycc != null && StringUtils.isNotBlank((String) pycc)) {
			sb.append(" and gs.pycc = :pycc ");
			map.put("pycc", pycc);
		}

		Object specialtyId = searchParams.get("EQ_specialtyId");
		if (specialtyId != null && StringUtils.isNotBlank((String) specialtyId)) {
			sb.append(" and gs.specialty_id = :specialtyId ");
			map.put("specialtyId", specialtyId);
		}

		Object teacherId = searchParams.get("EQ_teacherId");
		if (teacherId != null && StringUtils.isNotBlank((String) teacherId)) {
			sb.append(" and gta.guide_teacher = :teacherId ");
			map.put("teacherId", teacherId);
		}

		Object gradeSpecialtyId = searchParams.get("EQ_gradeSpecialtyId");
		if (gradeSpecialtyId != null && StringUtils.isNotBlank((String) gradeSpecialtyId)) {
			sb.append(" and gsi.grade_specialty_id = :gradeSpecialtyId ");
			map.put("gradeSpecialtyId", gradeSpecialtyId);
		}

		sb.append(
				"        		group by gta.guide_teacher, gsi.grade_specialty_id, gtp.thesis_plan_id, gtp.thesis_plan_code, gtp.thesis_plan_name, gei.xm, gei.zp, gg.grade_name, gs.pycc, gs.zymc, gs.rule_code ")
				.append("        	) t1 ");

		map.put("orgId", orgId);

		return commonDao.queryForPageNative(sb.toString(), map, pageRequst);

	}

	/**
	 * 更新课程分数
	 * 
	 * @param studentId
	 * @param score
	 */
	@Transactional
	public void updateScore(String studentId, float score) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("		 update ").append("        	gjt_rec_result grr ").append("    set ")
				.append("        	grr.exam_score1 = :score ").append("    where ")
				.append("        	grr.is_deleted = 'N' ").append("        	and grr.student_id = :studentId ")
				.append("        	and exists ( ").append("        				select ")
				.append("        						1 ").append("        				from ")
				.append("        						gjt_course gc ").append("        				where ")
				.append("        						gc.course_id = grr.course_id ")
				.append("        						and gc.course_category = 2 ").append("        			   ) ");

		map.put("studentId", studentId);
		map.put("score", score);

		commonDao.updateForMapNative(sb.toString(), map);
	}

	/**
	 * 查询我的指导列表
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<GjtThesisApply> queryMyGuideList(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" from ").append("      GjtThesisApply a").append(" where ").append("      a.isDeleted = 'N' ");

		Object guideTeacher = searchParams.get("EQ_guideTeacher");
		if (guideTeacher != null && StringUtils.isNotBlank((String) guideTeacher)) {
			sb.append(" and a.guideTeacher = :guideTeacher ");
			map.put("guideTeacher", guideTeacher);
		}

		Object status = searchParams.get("IN_status");
		if (status != null) {
			sb.append(" and a.status in (:status) ");
			map.put("status", status);
		}

		Object status2 = searchParams.get("EQ_status");
		if (status2 != null && StringUtils.isNotBlank(status2.toString())) {
			sb.append(" and a.status = :status2 ");
			map.put("status2", Integer.parseInt(status2.toString()));
		}

		Object stundetCode = searchParams.get("LIKE_gjtStudentInfo.xh");
		if (stundetCode != null && StringUtils.isNotBlank((String) stundetCode)) {
			sb.append(" and a.gjtStudentInfo.xh like :stundetCode ");
			map.put("stundetCode", "%" + stundetCode + "%");
		}

		Object stundetName = searchParams.get("LIKE_gjtStudentInfo.xm");
		if (stundetName != null && StringUtils.isNotBlank((String) stundetName)) {
			sb.append(" and a.gjtStudentInfo.xm like :stundetName ");
			map.put("stundetName", "%" + stundetName + "%");
		}

		Object thesisPlanId = searchParams.get("EQ_thesisPlanId");
		if (thesisPlanId != null && StringUtils.isNotBlank((String) thesisPlanId)) {
			sb.append(" and a.thesisPlanId = :thesisPlanId ");
			map.put("thesisPlanId", thesisPlanId);
		}

		Object nj = searchParams.get("EQ_gjtStudentInfo.nj");
		if (nj != null && StringUtils.isNotBlank((String) nj)) {
			sb.append(" and a.gjtStudentInfo.nj = :nj ");
			map.put("nj", nj);
		}

		Object pycc = searchParams.get("EQ_gjtStudentInfo.gjtSpecialty.pycc");
		if (pycc != null && StringUtils.isNotBlank((String) pycc)) {
			sb.append(" and a.gjtStudentInfo.gjtSpecialty.pycc = :pycc ");
			map.put("pycc", pycc);
		}

		Object specialtyId = searchParams.get("EQ_gjtStudentInfo.gjtSpecialty.specialtyId");
		if (specialtyId != null && StringUtils.isNotBlank((String) specialtyId)) {
			sb.append(" and a.gjtStudentInfo.gjtSpecialty.specialtyId = :specialtyId ");
			map.put("specialtyId", specialtyId);
		}

		Object isGuide = searchParams.get("EQ_isGuide");
		if (isGuide == null || StringUtils.isBlank((String) isGuide)) {
			sb.append(" and exists (").append("            select r1 from GjtThesisGuideRecord r1")
					.append("            where r1.isDeleted = 'N' and r1.thesisPlanId = a.thesisPlanId")
					.append("            and r1.studentId = a.studentId")
					.append("            and r1.isStudent = 1 and r1.createdDt = (")
					.append("                select max(r2.createdDt) from GjtThesisGuideRecord r2")
					.append("                where r2.isDeleted = 'N' and r2.thesisPlanId = a.thesisPlanId")
					.append("                and r2.studentId = a.studentId").append("            )")
					.append("          )");
		} else if ("1".equals((String) isGuide)) {
			sb.append(" and exists (").append("            select r1 from GjtThesisGuideRecord r1")
					.append("            where r1.isDeleted = 'N' and r1.thesisPlanId = a.thesisPlanId")
					.append("            and r1.studentId = a.studentId")
					.append("            and r1.isStudent = 0 and r1.createdDt = (")
					.append("                select max(r2.createdDt) from GjtThesisGuideRecord r2")
					.append("                where r2.isDeleted = 'N' and r2.thesisPlanId = a.thesisPlanId")
					.append("                and r2.studentId = a.studentId").append("            )")
					.append("          )");
		}

		sb.append(" order by a.updatedDt DESC");

		return (Page<GjtThesisApply>) commonDao.queryForPage(sb.toString(), map, pageRequst);
	}

}
