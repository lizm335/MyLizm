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

import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.thesis.repository.GjtThesisArrangeRepository;
import com.gzedu.xlims.pojo.thesis.GjtThesisArrange;

@Repository
public class GjtThesisArrangeDao {
	
	@Autowired
	private GjtThesisArrangeRepository gjtThesisArrangeRepository;

	@Autowired
	private CommonDao commonDao;
	
	public Page<GjtThesisArrange> findAll(Specification<GjtThesisArrange> spec, PageRequest pageRequst) {
		return gjtThesisArrangeRepository.findAll(spec, pageRequst);
	}
	
	public GjtThesisArrange save(GjtThesisArrange entity) {
		return gjtThesisArrangeRepository.save(entity);
	}
	
	public void save(List<GjtThesisArrange> entities) {
		gjtThesisArrangeRepository.save(entities);
	}
	
	public GjtThesisArrange findOne(String id) {
		return gjtThesisArrangeRepository.findOne(id);
	}
	
	public GjtThesisArrange findByThesisPlanIdAndSpecialtyBaseIdAndIsDeleted(String thesisPlanId, String specialtyBaseId, String isDeleted) {
		return gjtThesisArrangeRepository.findByThesisPlanIdAndSpecialtyBaseIdAndIsDeleted(thesisPlanId, specialtyBaseId, isDeleted);
	}
	
	/**
	 * 查询可申请毕业论文的专业
	 * @param orgId
	 * @param gradeId
	 * @param thesisPlanId
	 * @return
	 */
	public List<Map<String, Object>> getCanApplySpecialty(String orgId, String gradeId, String thesisPlanId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("		 select ")
			.append("        	gsb.specialty_base_id as \"specialtyBaseId\", ")
			.append("        	gsb.specialty_code as \"specialtyCode\", ")
			.append("        	gsb.specialty_name as \"specialtyName\", ")
			.append("        	(select tsd.name from tbl_sys_data tsd where tsd.is_deleted = 'N' and tsd.type_code = 'TrainingLevel' and tsd.code = gsb.specialty_layer) as \"trainingLevel\", ")
			.append("        	count(distinct gsi.student_id) as \"canApplyNum\" ")
			.append("    from ")
			.append("        	view_teach_plan gtp, ")
			.append("        	gjt_course gc, ")
			.append("        	gjt_grade_specialty ggs, ")
			.append("        	gjt_specialty gs, ")
			.append("        	gjt_specialty_base gsb, ")
			.append("        	gjt_rec_result grr, ")
			.append("        	gjt_student_info gsi ")
			.append("    where ")
			.append("        	gtp.is_deleted = 'N' ")
			.append("        	and gtp.actual_grade_id in ")
			.append("        		( ")
			.append("        		 select ")
			.append("        		 		gg1.grade_id ")
			.append("        		 from ")
			.append("        		 		gjt_grade gg1 ")
			.append("        		 where ")
			.append("        		 		gg1.is_deleted = 'N' ")
			.append("        		 		and gg1.xx_id = :orgId ")
			.append("        		 		and gg1.start_date <= (select start_date from gjt_grade where grade_id = :gradeId) ")
			.append("        		) ")
			.append("        	and gtp.course_id = gc.course_id ")
			.append("        	and gc.course_category = 2 ")
			.append("        	and ggs.id = gtp.grade_specialty_id ")
			.append("        	and ggs.is_deleted = 'N' ")
			.append("        	and ggs.specialty_id = gs.specialty_id ")
			.append("        	and gs.is_deleted = 'N' ")
			.append("        	and gsb.specialty_base_id = gs.specialty_base_id ")
			.append("        	and gsb.is_deleted = 'N' ")
			.append("        	and grr.teach_plan_id = gtp.teach_plan_id ")
			.append("        	and grr.is_deleted = 'N' ")
			.append("        	and grr.student_id = gsi.student_id ")
			.append("        	and gsi.is_deleted = 'N' ")
			.append("        	and gsi.student_id not in ")
			.append("        		( ")
			.append("        		 select student_id from gjt_thesis_apply where is_deleted = 'N' and status = 13 ")
			.append("        		 union all ")
			.append("        		 select student_id from gjt_graduation_apply where is_deleted = 'N' and apply_type = 1 and status >= 6 ")
			.append("        		) ")
			.append("        	and not exists ")
			.append("        		( ")
			.append("        		 select ")
			.append("        		 		1 ")
			.append("        		 from ")
			.append("        		 		gjt_thesis_arrange ")
			.append("        		 where ")
			.append("        		 		is_deleted = 'N' ")
			.append("        		 		and thesis_plan_id = :thesisPlanId ")
			.append("        		 		and specialty_base_id = gsb.specialty_base_id ")
			.append("        		) ")
			.append("    group by gsb.specialty_base_id, gsb.specialty_code, gsb.specialty_name, gsb.specialty_layer ")
			.append("    order by gsb.specialty_code ");
		
		map.put("orgId", orgId);
		map.put("gradeId", gradeId);
		map.put("thesisPlanId", thesisPlanId);
		
		return commonDao.queryForStringObjectMapListNative(sb.toString(), map);
	}
	
	/**
	 * 查询可申请毕业论文的学生
	 * @param orgId
	 * @param gradeId
	 * @param thesisPlanId
	 * @param specialtyBaseId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> getCanApplyStudent(String orgId, String gradeId, String thesisPlanId, String specialtyBaseId,
			Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("		 select ")
			.append("        	distinct gsb.specialty_base_id as \"specialtyBaseId\", ")
			.append("        	gsb.specialty_code as \"specialtyCode\", ")
			.append("        	gsb.specialty_name as \"specialtyName\", ")
			.append("        	(select tsd.name from tbl_sys_data tsd where tsd.is_deleted = 'N' and tsd.type_code = 'TrainingLevel' and tsd.code = gsb.specialty_layer) as \"trainingLevel\", ")
			.append("        	gsi.xh as \"studentCode\", ")
			.append("        	gsi.xm as \"studentName\", ")
			.append("        	gsi.sjh as \"mobile\", ")
			.append("        	gg.grade_name as \"gradeName\", ")
			.append("        	gs.rule_code as \"ruleCode\", ")
			.append("        	gsc.sc_name as \"scName\", ")
			.append("        	to_char(nvl(( ")
			.append("        			select  ")
			.append("        					gta.status ")
			.append("        			from  ")
			.append("        					gjt_thesis_apply gta ")
			.append("        			where  ")
			.append("        					gta.is_deleted = 'N' ")
			.append("        					and gta.thesis_plan_id = :thesisPlanId ")
			.append("        					and gta.student_id = gsi.student_id ")
			.append("        	), -1)) as \"status\" ")
			.append("    from ")
			.append("        	view_teach_plan gtp, ")
			.append("        	gjt_course gc, ")
			.append("        	gjt_grade_specialty ggs, ")
			.append("        	gjt_specialty gs, ")
			.append("        	gjt_specialty_base gsb, ")
			.append("        	gjt_rec_result grr, ")
			.append("        	gjt_student_info gsi, ")
			.append("        	gjt_grade gg, ")
			.append("        	gjt_study_center gsc ")
			.append("    where ")
			.append("        	gtp.is_deleted = 'N' ")
			.append("        	and gtp.actual_grade_id in ")
			.append("        		( ")
			.append("        		 select ")
			.append("        		 		gg1.grade_id ")
			.append("        		 from ")
			.append("        		 		gjt_grade gg1 ")
			.append("        		 where ")
			.append("        		 		gg1.is_deleted = 'N' ")
			.append("        		 		and gg1.xx_id = :orgId ")
			.append("        		 		and gg1.start_date <= (select start_date from gjt_grade where grade_id = :gradeId) ")
			.append("        		) ")
			.append("        	and gtp.course_id = gc.course_id ")
			.append("        	and gc.course_category = 2 ")
			.append("        	and ggs.id = gtp.grade_specialty_id ")
			.append("        	and ggs.is_deleted = 'N' ")
			.append("        	and ggs.specialty_id = gs.specialty_id ")
			.append("        	and gs.is_deleted = 'N' ")
			.append("        	and gsb.specialty_base_id = gs.specialty_base_id ")
			.append("        	and gsb.is_deleted = 'N' ")
			.append("        	and grr.teach_plan_id = gtp.teach_plan_id ")
			.append("        	and grr.is_deleted = 'N' ")
			.append("        	and grr.student_id = gsi.student_id ")
			.append("        	and gsi.is_deleted = 'N' ")
			.append("        	and gsi.student_id not in ")
			.append("        		( ")
			.append("        		 select student_id from gjt_thesis_apply where is_deleted = 'N' and status = 13 and thesis_plan_id != :thesisPlanId ")
			.append("        		 union all ")
			.append("        		 select student_id from gjt_graduation_apply where is_deleted = 'N' and apply_type = 1 and status >= 6 ")
			.append("        		) ")
			.append("        	and gg.grade_id = gsi.nj ")
			.append("        	and gsc.id = gsi.xxzx_id ")
			.append("        	and gsb.specialty_base_id = :specialtyBaseId ");
		
		map.put("orgId", orgId);
		map.put("gradeId", gradeId);
		map.put("thesisPlanId", thesisPlanId);
		map.put("specialtyBaseId", specialtyBaseId);
		
		Object gradeId2 = searchParams.get("EQ_gradeId");
		if (gradeId2 != null && StringUtils.isNotBlank((String) gradeId2)) {
			sb.append(" and gsi.nj = :gradeId2 ");
			map.put("gradeId2", gradeId2);
		}
		
		Object specialtyId = searchParams.get("EQ_specialtyId");
		if (specialtyId != null && StringUtils.isNotBlank((String) specialtyId)) {
			sb.append(" and gs.specialty_id = :specialtyId ");
			map.put("specialtyId", specialtyId);
		}
		
		Object xh = searchParams.get("LIKE_xh");
		if (xh != null && StringUtils.isNotBlank((String) xh)) {
			sb.append(" and gsi.xh = :xh ");
			map.put("xh", xh);
		}
		
		Object xm = searchParams.get("LIKE_xm");
		if (xm != null && StringUtils.isNotBlank((String) xm)) {
			sb.append(" and gsi.xm like :xm ");
			map.put("xm", "%" + xm + "%");
		}
		
		sb.append("    order by gsc.sc_name ");
		
		return commonDao.queryForPageNative(sb.toString(), map, pageRequst);
	}
	
	/**
	 * 查询指导老师的指导人数
	 * @param thesisPlanId
	 * @param teacherId
	 * @param specialtyBaseId
	 * @return
	 */
	public Map<String, Object> getGuideNum(String thesisPlanId, String teacherId, String specialtyBaseId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("		 select ")
			.append("        	t1.num1 as \"num1\", ")
			.append("        	t2.num2 as \"num2\" ")
			.append("    from ")
			.append("        	( ")
			.append("        		select ")
			.append("        			sum(adv1.adviser_num) as num1 ")
			.append("        		from ")
			.append("        			gjt_thesis_arrange arr1, ")
			.append("        			gjt_thesis_adviser adv1 ")
			.append("   			where ")
			.append("        			arr1.is_deleted = 'N' ")
			.append("        			and arr1.thesis_plan_id = :thesisPlanId ")
			.append("        			and arr1.arrange_id = adv1.arrange_id ")
			.append("        		 	and adv1.adviser_type = 1 ")
			.append("        		 	and adv1.teacher_id = :teacherId ")
			.append("        	) t1, ")
			.append("        	( ")
			.append("        		select ")
			.append("        			sum(adv2.adviser_num) as num2 ")
			.append("        		from ")
			.append("        			gjt_thesis_arrange arr2, ")
			.append("        			gjt_thesis_adviser adv2 ")
			.append("   			where ")
			.append("        			arr2.is_deleted = 'N' ")
			.append("        			and arr2.thesis_plan_id = :thesisPlanId ")
			.append("        			and arr2.specialty_base_id = :specialtyBaseId ")
			.append("        			and arr2.arrange_id = adv2.arrange_id ")
			.append("        		 	and adv2.adviser_type = 1 ")
			.append("        		 	and adv2.teacher_id = :teacherId ")
			.append("        	) t2 ");
		
		map.put("thesisPlanId", thesisPlanId);
		map.put("teacherId", teacherId);
		map.put("specialtyBaseId", specialtyBaseId);
		
		return commonDao.queryObjectToMapNative(sb.toString(), map);
	}
	
	/**
	 * 查询上期有未通过论文学员的指导老师
	 * @param orgId
	 * @param gradeId
	 * @param specialtyBaseId
	 * @return
	 */
	public List<Map<String, Object>> getPreNoPass(String orgId, String gradeId, String specialtyBaseId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("		 select ")
			.append("        	* ")
			.append("    from ")
			.append("        	( ")
			.append("        		select ")
			.append("        			i.employee_id, ")
			.append("        			i.xm as \"teacherName\", ")
			.append("        			count(a.student_id) as \"studentNum\" ")
			.append("        		from ")
			.append("        			gjt_graduation_apply a, ")
			.append("        			gjt_graduation_batch b, ")
			.append("        			gjt_employee_info i, ")
			.append("        			gjt_student_info s, ")
			.append("        			gjt_specialty gs ")
			.append("   			where ")
			.append("        			a.is_deleted = 'N' ")
			.append("        			and a.APPLY_TYPE = 1 ")
			.append("        			and a.status < 6 ")
			.append("        		 	and a.batch_id = b.batch_id ")
			.append("        		 	and b.is_deleted = 'N' ")
			.append("        		 	and b.grade_id = :gradeId ")
			.append("        		 	and b.org_id = :orgId ")
			.append("        		 	and i.employee_id = a.guide_teacher ")
			.append("        		 	and s.student_id = a.student_id ")
			.append("        		 	and s.major = gs.specialty_id ")
			.append("        		 	and gs.specialty_base_id = :specialtyBaseId ")
			.append("   			group by i.employee_id, i.xm ")
			.append("        	) ")
			.append("        	union all ")
			.append("        	( ")
			.append("        		select ")
			.append("        			gei.employee_id, ")
			.append("        			gei.xm as \"teacherName\", ")
			.append("        			count(gta.student_id) as \"studentNum\" ")
			.append("        		from ")
			.append("        			gjt_thesis_apply gta, ")
			.append("        			gjt_thesis_plan gtp, ")
			.append("        			gjt_employee_info gei, ")
			.append("        			gjt_student_info gsi, ")
			.append("        			gjt_specialty gs1 ")
			.append("   			where ")
			.append("        			gta.is_deleted = 'N' ")
			.append("        			and gta.status < 13 ")
			.append("        		 	and gta.thesis_plan_id = gtp.thesis_plan_id ")
			.append("        		 	and gtp.is_deleted = 'N' ")
			.append("        		 	and gtp.grade_id = :gradeId ")
			.append("        		 	and gtp.org_id = :orgId ")
			.append("        		 	and gei.employee_id = gta.guide_teacher ")
			.append("        		 	and gsi.student_id = gta.student_id ")
			.append("        		 	and gsi.major = gs1.specialty_id ")
			.append("        		 	and gs1.specialty_base_id = :specialtyBaseId ")
			.append("   			group by gei.employee_id, gei.xm ")
			.append("        	) ");
		
		map.put("orgId", orgId);
		map.put("gradeId", gradeId);
		map.put("specialtyBaseId", specialtyBaseId);
		
		return commonDao.queryForStringObjectMapListNative(sb.toString(), map);
	}
	
	/**
	 * 查询上期有未通过论文学员的学员ID和指导老师ID键值对
	 * @param orgId
	 * @param gradeId
	 * @param specialtyBaseId
	 * @return
	 */
	public Map<String, String> getPreNoPassMap(String orgId, String gradeId, String specialtyBaseId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("		 select ")
			.append("        	* ")
			.append("    from ")
			.append("        	( ")
			.append("        		select ")
			.append("        			i.employee_id as \"NAME\", ")
			.append("        			a.student_id as \"ID\" ")
			.append("        		from ")
			.append("        			gjt_graduation_apply a, ")
			.append("        			gjt_graduation_batch b, ")
			.append("        			gjt_employee_info i, ")
			.append("        			gjt_student_info s, ")
			.append("        			gjt_specialty gs ")
			.append("   			where ")
			.append("        			a.is_deleted = 'N' ")
			.append("        			and a.APPLY_TYPE = 1 ")
			.append("        			and a.status < 6 ")
			.append("        		 	and a.batch_id = b.batch_id ")
			.append("        		 	and b.is_deleted = 'N' ")
			.append("        		 	and b.grade_id = :gradeId ")
			.append("        		 	and b.org_id = :orgId ")
			.append("        		 	and i.employee_id = a.guide_teacher ")
			.append("        		 	and s.student_id = a.student_id ")
			.append("        		 	and s.major = gs.specialty_id ")
			.append("        		 	and gs.specialty_base_id = :specialtyBaseId ")
			.append("        	) ")
			.append("        	union all ")
			.append("        	( ")
			.append("        		select ")
			.append("        			gei.employee_id as \"NAME\", ")
			.append("        			gta.student_id as \"ID\" ")
			.append("        		from ")
			.append("        			gjt_thesis_apply gta, ")
			.append("        			gjt_thesis_plan gtp, ")
			.append("        			gjt_employee_info gei, ")
			.append("        			gjt_student_info gsi, ")
			.append("        			gjt_specialty gs1 ")
			.append("   			where ")
			.append("        			gta.is_deleted = 'N' ")
			.append("        			and gta.status < 13 ")
			.append("        		 	and gta.thesis_plan_id = gtp.thesis_plan_id ")
			.append("        		 	and gtp.is_deleted = 'N' ")
			.append("        		 	and gtp.grade_id = :gradeId ")
			.append("        		 	and gtp.org_id = :orgId ")
			.append("        		 	and gei.employee_id = gta.guide_teacher ")
			.append("        		 	and gsi.student_id = gta.student_id ")
			.append("        		 	and gsi.major = gs1.specialty_id ")
			.append("        		 	and gs1.specialty_base_id = :specialtyBaseId ")
			.append("        	) ");
		
		map.put("orgId", orgId);
		map.put("gradeId", gradeId);
		map.put("specialtyBaseId", specialtyBaseId);
		
		return commonDao.getMapNative(sb.toString(), map);
	}

}
