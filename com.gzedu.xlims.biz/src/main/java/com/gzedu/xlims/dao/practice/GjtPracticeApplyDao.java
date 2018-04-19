package com.gzedu.xlims.dao.practice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.practice.repository.GjtPracticeApplyRepository;
import com.gzedu.xlims.pojo.practice.GjtPracticeApply;

@Repository
public class GjtPracticeApplyDao {
	private final static Logger log = LoggerFactory.getLogger(GjtPracticeApplyDao.class);

	@Autowired
	private GjtPracticeApplyRepository gjtPracticeApplyRepository;

	@Autowired
	private CommonDao commonDao;

	public Page<GjtPracticeApply> findAll(Specification<GjtPracticeApply> spec, PageRequest pageRequst) {
		return gjtPracticeApplyRepository.findAll(spec, pageRequst);
	}

	public long count(Specification<GjtPracticeApply> spec) {
		return gjtPracticeApplyRepository.count(spec);
	}

	public GjtPracticeApply save(GjtPracticeApply entity) {
		return gjtPracticeApplyRepository.save(entity);
	}

	public List<GjtPracticeApply> save(List<GjtPracticeApply> entities) {
		return gjtPracticeApplyRepository.save(entities);
	}

	public GjtPracticeApply findOne(String id) {
		return gjtPracticeApplyRepository.findOne(id);
	}

	public GjtPracticeApply findByPracticePlanIdAndStudentIdAndIsDeleted(String practicePlanId, String studentId,
			String isDeleted) {
		return gjtPracticeApplyRepository.findByPracticePlanIdAndStudentIdAndIsDeleted(practicePlanId, studentId,
				isDeleted);
	}

	public List<GjtPracticeApply> findPracticePlanIdByStudentId(String studentId, String gradeId) {
		return gjtPracticeApplyRepository.findPracticePlanIdByStudentId(studentId, gradeId);
	}

	public List<GjtPracticeApply> findByPracticePlanIdAndSpecialtyBaseId(String practicePlanId,
			String specialtyBaseId) {
		return gjtPracticeApplyRepository.findByPracticePlanIdAndSpecialtyBaseId(practicePlanId, specialtyBaseId);
	}

	public GjtPracticeApply findByIsDeletedAndStudentIdAndStatus(String isDeleted, String studentId, int status) {
		return gjtPracticeApplyRepository.findByIsDeletedAndStudentIdAndStatus(isDeleted, studentId, status);
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
				.append("        	and gc.course_category = 1 ");

		map.put("orgId", orgId);
		map.put("gradeId", gradeId);
		map.put("gradeSpecialtyId", gradeSpecialtyId);
		map.put("studentId", studentId);
		log.info("社会实践查询成绩是否合格参数：{}", map);
		return commonDao.queryForStringObjectMapListNative(sb.toString(), map);
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
				.append("        	t1.practice_plan_id as \"practicePlanId\", ")
				.append("        	t1.practice_plan_code as \"practicePlanCode\", ")
				.append("        	t1.practice_plan_name as \"practicePlanName\", ")
				.append("        	t1.xm as \"teacherName\", ").append("        	t1.zp as \"teacherZp\", ")
				.append("        	t1.grade_name as \"gradeName\", ").append("        	t1.pycc as \"trainingLevel\", ")
				.append("        	t1.zymc as \"specialtyName\", ").append("        	t1.rule_code as \"ruleCode\", ")
				.append("        	t1.num as \"all\", ").append("        	( ").append("        		select ")
				.append("        			count(1) ").append("        		from ")
				.append("        			gjt_practice_apply gpa1, ")
				.append("        			gjt_student_info gsi1 ").append("        		where ")
				.append("        			gpa1.is_deleted = 'N' ").append("        			and gpa1.status = 2 ")
				.append("        			and gpa1.practice_plan_id = t1.practice_plan_id ")
				.append("        			and gpa1.guide_teacher = t1.guide_teacher ")
				.append("        			and gsi1.student_id = gpa1.student_id ")
				.append("        			and gsi1.grade_specialty_id = t1.grade_specialty_id ")
				.append("        	) as \"submitPractice\", ").append("        	( ")
				.append("        		select ").append("        			count(1) ").append("        		from ")
				.append("        			gjt_practice_apply gpa2, ")
				.append("        			gjt_student_info gsi2 ").append("        		where ")
				.append("        			gpa2.is_deleted = 'N' ").append("        			and gpa2.status = 13 ")
				.append("        			and gpa2.review_score >= 60 ")
				.append("        			and gpa2.practice_plan_id = t1.practice_plan_id ")
				.append("        			and gpa2.guide_teacher = t1.guide_teacher ")
				.append("        			and gsi2.student_id = gpa2.student_id ")
				.append("        			and gsi2.grade_specialty_id = t1.grade_specialty_id ")
				.append("        	) as \"completed\", ").append("        	( ").append("        		select ")
				.append("        			count(1) ").append("        		from ")
				.append("        			gjt_practice_apply gpa3, ")
				.append("        			gjt_student_info gsi3 ").append("        		where ")
				.append("        			gpa3.is_deleted = 'N' ").append("        			and gpa3.status = 13 ")
				.append("        			and gpa3.review_score < 60 ")
				.append("        			and gpa3.practice_plan_id = t1.practice_plan_id ")
				.append("        			and gpa3.guide_teacher = t1.guide_teacher ")
				.append("        			and gsi3.student_id = gpa3.student_id ")
				.append("        			and gsi3.grade_specialty_id = t1.grade_specialty_id ")
				.append("        	) as \"failed\" ").append("    from ").append("        	( ")
				.append("        		select ").append("        			gpa.guide_teacher, ")
				.append("        			gsi.grade_specialty_id, ").append("        			gpp.practice_plan_id, ")
				.append("        			gpp.practice_plan_code, ")
				.append("        			gpp.practice_plan_name, ").append("        			gei.xm, ")
				.append("        			gei.zp, ").append("        			gg.grade_name, ")
				.append("        			(select tsd.name from tbl_sys_data tsd where tsd.is_deleted = 'N' and tsd.type_code = 'TrainingLevel' and tsd.code = gs.pycc) pycc, ")
				.append("        			gs.zymc, ").append("        			gs.rule_code, ")
				.append("        			count(*) num ").append("        		from ")
				.append("        			gjt_practice_apply gpa, ")
				.append("        			gjt_practice_plan gpp, ")
				.append("        			gjt_employee_info gei, ").append("        			gjt_student_info gsi, ")
				.append("        			gjt_grade_specialty ggs, ").append("        			gjt_grade gg, ")
				.append("        			gjt_specialty gs ").append("        		where ")
				.append("        			gpa.is_deleted = 'N' ")
				.append("        			and gpp.practice_plan_id = gpa.practice_plan_id ")
				.append("        			and gpp.org_id = :orgId ")
				.append("        			and gei.employee_id = gpa.guide_teacher ")
				.append("        			and gsi.student_id = gpa.student_id ")
				.append("        			and ggs.id = gsi.grade_specialty_id ")
				.append("        			and gg.grade_id = ggs.grade_id ")
				.append("        			and gs.specialty_id = ggs.specialty_id ");

		Object practicePlanId = searchParams.get("EQ_practicePlanId");
		if (practicePlanId != null && StringUtils.isNotBlank((String) practicePlanId)) {
			sb.append(" and gpa.practice_plan_id = :practicePlanId ");
			map.put("practicePlanId", practicePlanId);
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
			sb.append(" and gpa.guide_teacher = :teacherId ");
			map.put("teacherId", teacherId);
		}

		Object gradeSpecialtyId = searchParams.get("EQ_gradeSpecialtyId");
		if (gradeSpecialtyId != null && StringUtils.isNotBlank((String) gradeSpecialtyId)) {
			sb.append(" and gsi.grade_specialty_id = :gradeSpecialtyId ");
			map.put("gradeSpecialtyId", gradeSpecialtyId);
		}

		sb.append(
				"        		group by gpa.guide_teacher, gsi.grade_specialty_id, gpp.practice_plan_id, gpp.practice_plan_code, gpp.practice_plan_name, gei.xm, gei.zp, gg.grade_name, gs.pycc, gs.zymc, gs.rule_code ")
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
				.append("        						and gc.course_category = 1 ").append("        			   ) ");

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
	public Page<GjtPracticeApply> queryMyGuideList(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" from ").append("      GjtPracticeApply a").append(" where ").append("      a.isDeleted = 'N' ");

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

		Object practicePlanId = searchParams.get("EQ_practicePlanId");
		if (practicePlanId != null && StringUtils.isNotBlank((String) practicePlanId)) {
			sb.append(" and a.practicePlanId = :practicePlanId ");
			map.put("practicePlanId", practicePlanId);
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
			sb.append(" and exists (").append("            select r1 from GjtPracticeGuideRecord r1")
					.append("            where r1.isDeleted = 'N' and r1.practicePlanId = a.practicePlanId")
					.append("            and r1.studentId = a.studentId")
					.append("            and r1.isStudent = 1 and r1.createdDt = (")
					.append("                select max(r2.createdDt) from GjtPracticeGuideRecord r2")
					.append("                where r2.isDeleted = 'N' and r2.practicePlanId = a.practicePlanId")
					.append("                and r2.studentId = a.studentId").append("            )")
					.append("          )");
		} else if ("1".equals((String) isGuide)) {
			sb.append(" and exists (").append("            select r1 from GjtPracticeGuideRecord r1")
					.append("            where r1.isDeleted = 'N' and r1.practicePlanId = a.practicePlanId")
					.append("            and r1.studentId = a.studentId")
					.append("            and r1.isStudent = 0 and r1.createdDt = (")
					.append("                select max(r2.createdDt) from GjtPracticeGuideRecord r2")
					.append("                where r2.isDeleted = 'N' and r2.practicePlanId = a.practicePlanId")
					.append("                and r2.studentId = a.studentId").append("            )")
					.append("          )");
		}

		sb.append(" order by a.updatedDt DESC");

		return (Page<GjtPracticeApply>) commonDao.queryForPage(sb.toString(), map, pageRequst);
	}

}
